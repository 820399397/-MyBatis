package cn.xuguowen.mybatis.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

/**
 * ClassName: Resources
 * Package: cn.xuguowen.mybatis.io
 * Description:
 *
 * @Author 徐国文
 * @Create 2024/2/14 23:17
 * @Version 1.0
 */
public class Resources {

    public static Reader getResourceAsReader(String resource) throws IOException {
        // 它将字节流转换为字符流
        return new InputStreamReader(getResourceAsStream(resource));
    }

    /**
     * 根据资源路径获取资源流
     * @param resource
     * @return
     * @throws IOException
     */
    private static InputStream getResourceAsStream(String resource) throws IOException {
        ClassLoader[] classLoaders = getClassLoaders();
        for (ClassLoader classLoader : classLoaders) {
            InputStream inputStream = classLoader.getResourceAsStream(resource);
            if (null != inputStream) {
                return inputStream;
            }
        }
        throw new IOException("Could not find resource " + resource);
    }

    /**
     * 返回默认的类加载器
     * @return
     */
    private static ClassLoader[] getClassLoaders() {
        return new ClassLoader[]{
                ClassLoader.getSystemClassLoader(),
                Thread.currentThread().getContextClassLoader()};
    }

    /*
     * 通过包路径结构+类名的方式获取类
     * @param className - the class to fetch
     * @return The loaded class
     * @throws ClassNotFoundException If the class cannot be found (duh!)
     */
    public static Class<?> classForName(String className) throws ClassNotFoundException {
        return Class.forName(className);
    }
}
