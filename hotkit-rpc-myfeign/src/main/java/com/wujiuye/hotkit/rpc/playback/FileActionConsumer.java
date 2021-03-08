package com.wujiuye.hotkit.rpc.playback;

import com.wujiuye.hotkit.json.JsonUtils;
import org.openjdk.jmh.util.FileUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;

/**
 * 基于file的action重放消费
 *
 * @author wujiuye 2020/07/20
 */
public class FileActionConsumer extends AbstractActionConsumer {

    private static final String ROOT_PATH = System.getProperty("reaction.log.path", "/tmp/reaction");
    private final static String SUFFIX = ".json";
    private final static String CONSUMERING_SUFFIX = ".fail";

    private String filePath;
    private File failFile;
    private FileOutputStream fileOutputStream;

    private List<File> actionLogs;

    public FileActionConsumer(ExecutorService executorService,
                              List<ActionConsumListener> consumListeners) {
        super(executorService, consumListeners);
        this.filePath = ROOT_PATH + "/";
    }

    /**
     * 创建临时失败记录文件
     */
    @Override
    protected void init() {
        try {
            this.readFiles();
            failFile = new File(this.filePath + "reaction-"
                    + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm_ss")) + CONSUMERING_SUFFIX);
            if ((!failFile.exists() || failFile.delete()) && failFile.createNewFile()) {
                fileOutputStream = new FileOutputStream(failFile, true);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 遍历目录下的action文件
     */
    private void readFiles() {
        actionLogs = new ArrayList<>();
        File file = new File(filePath);
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            String suffix = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            if (files != null && files.length > 0) {
                for (File actionFile : files) {
                    // 如果文件是当天文件，则将当前文件内容拷贝到一个新文件并重新创建当天文件
                    if (actionFile.getName().endsWith(SUFFIX)) {
                        if (actionFile.getName().contains(suffix)) {
                            File newFile = new File(filePath +
                                    actionFile.getName().replace(suffix, "re-" + suffix));
                            try {
                                if ((!newFile.exists() || newFile.delete()) && newFile.createNewFile()) {
                                    actionLogs.add(newFile);
                                    FileUtils.copy(actionFile.getAbsolutePath(), newFile.getAbsolutePath());
                                    FileLockUtils.lock(actionFile);
                                    try {
                                        if (actionFile.delete()) {
                                            // don't do anything
                                        }
                                    } finally {
                                        FileLockUtils.unlock(actionFile);
                                    }
                                }
                            } catch (IOException ignored) {
                            }
                        } else {
                            actionLogs.add(actionFile);
                        }
                    }
                }
            }
        }
    }

    @Override
    protected void onFail(ActionRecord record, Throwable e) {
        // 失败时回写
        try {
            if (record.getPlaybackCount() >= record.getMaxPlaybackCount()) {
                return;
            }
            String json = JsonUtils.toJsonString(record) + "\n";
            fileOutputStream.write(json.getBytes());
        } catch (IOException ignored) {
        }
    }

    @Override
    protected void onSuccess(ActionRecord record) {

    }

    @Override
    protected void onFinish() {
        if (fileOutputStream != null) {
            try {
                fileOutputStream.close();
            } catch (IOException ignored) {
            }
        }
        // 将失败记录转移到新文件
        File failNew = new File(this.filePath + "reaction-fail-"
                + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm_ss")) + SUFFIX);
        try {
            FileUtils.copy(failFile.getAbsolutePath(), failNew.getAbsolutePath());
            // 删除临时失败记录文件
            if (failFile.delete()) {
                System.out.println("临时失败文件删除成功！");
            }
        } catch (IOException ignored) {
        }
        // 遍历删除本次已经回放的文件
        for (File file : actionLogs) {
            if (file.delete()) {
                System.out.println("文件：" + file.getName() + " 删除成功！");
            }
        }
    }

    @Override
    public Iterator<ActionRecord> iterator() {
        if (actionLogs == null) {
            return new Iterator<ActionRecord>() {
                @Override
                public boolean hasNext() {
                    return false;
                }

                @Override
                public ActionRecord next() {
                    return null;
                }
            };
        }
        return new LayzIterator(this.actionLogs);
    }

    /**
     * Action迭代器
     *
     * @author wujiuye 2020/07/20
     */
    static class LayzIterator implements Iterator<ActionRecord> {

        private Iterator<File> actionLogIterator;
        private FileReader reader;

        public LayzIterator(List<File> actionLogs) {
            actionLogIterator = actionLogs.iterator();
        }

        @Override
        public boolean hasNext() {
            if (reader == null) {
                if (actionLogIterator.hasNext()) {
                    reader = new FileReader(actionLogIterator.next());
                } else {
                    return false;
                }
            }
            boolean eof = reader.isEof();
            if (eof) {
                reader.close();
                reader = null;
                // 当前文件为空，但还有下一个文件的情况
                if (actionLogIterator.hasNext()) {
                    return hasNext();
                }
            }
            return !eof;
        }

        @Override
        public ActionRecord next() {
            return JsonUtils.fromJson(reader.nextLine(), ActionRecord.class);
        }
    }

    /**
     * 文件读取
     *
     * @author wujiuye 2020/07/20
     */
    private static class FileReader {
        private String nextLine;
        private FileInputStream fis;
        private InputStreamReader isr;
        private BufferedReader br;

        public FileReader(File file) {
            try {
                this.fis = new FileInputStream(file);
                this.isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
                this.br = new BufferedReader(isr);
            } catch (Exception e) {
                close();
            }
            readLine();
        }

        public boolean isEof() {
            return nextLine == null || nextLine.trim().length() == 0;
        }

        public String nextLine() {
            String line = nextLine;
            readLine();
            return line;
        }

        private void readLine() {
            if (br == null) {
                nextLine = null;
                return;
            }
            try {
                nextLine = br.readLine();
            } catch (IOException e) {
                nextLine = null;
            }
        }

        public void close() {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (isr != null) {
                try {
                    isr.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

}
