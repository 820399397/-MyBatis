package cn.xuguowen.mybatis.scripting;

import java.util.HashMap;
import java.util.Map;

/**
 * ClassName: LanguageDriverRegistry
 * Package: cn.xuguowen.mybatis.scripting
 * Description:脚本语言注册器，用于注册和管理 MyBatis 中的脚本语言驱动。
 *
 * @Author 徐国文
 * @Create 2024/5/30 9:44
 * @Version 1.0
 */
public class LanguageDriverRegistry {



    // 存储脚本语言驱动的映射关系
    private final Map<Class<?>, LanguageDriver> LANGUAGE_DRIVER_MAP = new HashMap<Class<?>, LanguageDriver>();

    // 默认的脚本语言驱动类
    private Class<?> defaultDriverClass = null;

    /**
     * 注册脚本语言驱动类
     *
     * @param cls 要注册的脚本语言驱动类
     */
    public void register(Class<?> cls) {
        if (cls == null) {
            throw new IllegalArgumentException("null is not a valid Language Driver");
        }
        if (!LanguageDriver.class.isAssignableFrom(cls)) {
            throw new RuntimeException(cls.getName() + " does not implements " + LanguageDriver.class.getName());
        }
        // 如果没注册过，再去注册
        LanguageDriver driver = LANGUAGE_DRIVER_MAP.get(cls);
        if (driver == null) {
            try {
                //单例模式，即一个Class只有一个对应的LanguageDriver
                driver = (LanguageDriver) cls.newInstance();
                LANGUAGE_DRIVER_MAP.put(cls, driver);
            } catch (Exception ex) {
                throw new RuntimeException("Failed to load language driver for " + cls.getName(), ex);
            }
        }
    }

    /**
     * 获取指定类对应的脚本语言驱动对象
     *
     * @param cls 脚本语言驱动类
     * @return 对应的脚本语言驱动对象
     */
    public LanguageDriver getDriver(Class<?> cls) {
        return LANGUAGE_DRIVER_MAP.get(cls);
    }

    /**
     * 获取默认的脚本语言驱动对象
     *
     * @return 默认的脚本语言驱动对象
     */
    public LanguageDriver getDefaultDriver() {
        return getDriver(getDefaultDriverClass());
    }

    /**
     * 获取默认的脚本语言驱动类
     *
     * @return 默认的脚本语言驱动类
     */
    public Class<?> getDefaultDriverClass() {
        return defaultDriverClass;
    }

    /**
     * 设置默认的脚本语言驱动类
     * Configuration()有调用，默认的为XMLLanguageDriver
     * @param defaultDriverClass 默认的脚本语言驱动类
     */
    public void setDefaultDriverClass(Class<?> defaultDriverClass) {
        register(defaultDriverClass);
        this.defaultDriverClass = defaultDriverClass;
    }

}
