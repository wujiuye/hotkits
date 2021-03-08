package com.wujiuye.hotkit.rpc.playback;

import com.wujiuye.hotkit.json.JsonUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * 默认操作日记写入器，使用文件持久化
 *
 * @author wujiuye 2020/07/20
 */
class FileActionLogger extends AbstractActionLogger {

    private static final String ROOT_PATH = System.getProperty("reaction.log.path", "/tmp/reaction");
    private final static String SUFFIX = ".json";

    private String filePath;
    private FileOutputStream fileOutputStream;

    static {
        File file = new File(ROOT_PATH);
        if (!file.exists() && file.mkdirs()) {
        }
    }

    public FileActionLogger(String business) {
        super(business);
        this.filePath = ROOT_PATH + "/" + getBusiness() + "-";
    }

    /**
     * 确保文件存在、输出流存在
     */
    private void ensureFile(File file) {
        try {
            if (!file.exists() && file.createNewFile()) {
                synchronized (this) {
                    if (fileOutputStream != null) {
                        try {
                            fileOutputStream.close();
                        } catch (Exception ignored) {
                        }
                    }
                    fileOutputStream = new FileOutputStream(file, true);
                }
            }
            if (fileOutputStream == null) {
                synchronized (this) {
                    if (fileOutputStream == null) {
                        fileOutputStream = new FileOutputStream(file, true);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private File getFile() {
        String yyyyMmDd = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String fileName = filePath + yyyyMmDd + SUFFIX;
        return new File(fileName);
    }

    @Override
    protected void doSava(ActionRecord record) {
        File file = getFile();
        FileLockUtils.lock(file);
        try {
            ensureFile(file);
            String commitLog = JsonUtils.toJsonString(record) + "\n";
            fileOutputStream.write(commitLog.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            FileLockUtils.unlock(file);
        }
    }

}
