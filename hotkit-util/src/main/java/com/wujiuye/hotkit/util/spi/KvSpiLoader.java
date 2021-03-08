package com.wujiuye.hotkit.util.spi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * 服务加载器，自实现的SPI
 *
 * @author wujiuye
 * @version 1.0 on 2020/03/11
 */
public class KvSpiLoader<S> {

    private static final String PREFIX = "META-INF/kv/services/";

    private final Class<S> service;
    private final ClassLoader loader;
    private final Map<String, Class<S>> serviceImplMap = new HashMap<>();

    public Map<String, Class<S>> getAllServiceImpl() {
        return serviceImplMap;
    }

    public Class<S> getServiceImpl(String key) {
        return serviceImplMap.get(key);
    }

    private KvSpiLoader(Class<S> svc, ClassLoader cl) {
        service = Objects.requireNonNull(svc, "Service interface cannot be null");
        loader = (cl == null) ? ClassLoader.getSystemClassLoader() : cl;
    }

    public static synchronized <S> KvSpiLoader<S> load(Class<S> service) {
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        KvSpiLoader<S> sOnionServiceLoader = new KvSpiLoader<>(service, cl);
        sOnionServiceLoader.reload();
        return sOnionServiceLoader;
    }

    private void reload() {
        serviceImplMap.clear();
        String fullName = PREFIX + service.getName();
        Iterator<String> pending = parse(service, fullName);
        while (pending.hasNext()) {
            String line = pending.next();
            if (line.trim().startsWith("#")) {
                continue;
            }
            String[] nameClz = line.split("=");
            try {
                Class<S> clz = (Class<S>) Class.forName(nameClz[1].trim(), false, loader);
                serviceImplMap.putIfAbsent(nameClz[0].trim(), clz);
            } catch (Exception e) {
                fail(service, "Illegal line: " + line);
            }
        }
    }

    private int parseLine(BufferedReader r, int lc, List<String> names)
            throws IOException, ServiceConfigurationError {
        String ln = r.readLine();
        if (ln == null) {
            return -1;
        }
        int ci = ln.indexOf('#');
        if (ci >= 0) {
            ln = ln.substring(0, ci);
        }
        ln = ln.trim();
        int n = ln.length();
        if (n != 0) {
            if (!names.contains(ln)) {
                names.add(ln);
            }
        }
        return lc + 1;
    }

    private Iterator<String> parse(Class<?> service, String classFullName) throws ServiceConfigurationError {
        InputStream in = null;
        BufferedReader r = null;
        ArrayList<String> names = new ArrayList<>();
        try {
            if (loader == null) {
                in = ClassLoader.getSystemResourceAsStream(classFullName);
            } else {
                in = loader.getResourceAsStream(classFullName);
            }
            assert in != null;
            r = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
            int lc = 1;
            while ((lc = parseLine(r, lc, names)) >= 0) {
            }
        } catch (Exception x) {
            fail(service, "Error reading configuration file", x);
        } finally {
            try {
                if (r != null) {
                    r.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException y) {
                fail(service, "Error closing configuration file", y);
            }
        }
        return names.iterator();
    }

    private static void fail(Class<?> service, String msg, Throwable cause)
            throws ServiceConfigurationError {
        throw new ServiceConfigurationError(service.getName() + ": " + msg, cause);
    }

    private static void fail(Class<?> service, String msg)
            throws ServiceConfigurationError {
        throw new ServiceConfigurationError(service.getName() + ": " + msg);
    }

}
