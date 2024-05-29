package cn.xuguowen.mybatis.reflection.property;

import java.util.Locale;

/**
 * ClassName: PropertyNamer
 * Package: cn.xuguowen.mybatis.reflection.property
 * Description:属性命名器工具类，用于将方法名转换为属性名，以及判断方法是否为getter或setter。
 *
 * @Author 徐国文
 * @Create 2024/3/4 15:47
 * @Version 1.0
 */
public class PropertyNamer {

    // 私有构造函数，防止实例化
    private PropertyNamer() {
    }

    /**
     * 将方法名转换为属性名。
     * @param name 方法名
     * @return 属性名
     * @throws RuntimeException 如果方法名不以 'is', 'get' 或 'set' 开头，将抛出异常
     */
    public static String methodToProperty(String name) {
        if (name.startsWith("is")) {
            name = name.substring(2);
        } else if (name.startsWith("get") || name.startsWith("set")) {
            name = name.substring(3);
        } else {
            throw new RuntimeException("Error parsing property name '" + name + "'.  Didn't start with 'is', 'get' or 'set'.");
        }

        /*
         * 如果只有1个字母，转换为小写
         * 如果大于1个字母，第二个字母非大写，转换为小写
         */
        if (name.length() == 1 || (name.length() > 1 && !Character.isUpperCase(name.charAt(1)))) {
            name = name.substring(0, 1).toLowerCase(Locale.ENGLISH) + name.substring(1);
        }

        return name;
    }

    /**
     * 判断方法名是否为属性方法（以 'get', 'set' 或 'is' 开头）。
     * @param name 方法名
     * @return 如果方法名是属性方法，返回 true；否则返回 false
     */
    public static boolean isProperty(String name) {
        return name.startsWith("get") || name.startsWith("set") || name.startsWith("is");
    }

    /**
     * 判断方法名是否为 getter 方法（以 'get' 或 'is' 开头）。
     * @param name 方法名
     * @return 如果方法名是 getter 方法，返回 true；否则返回 false
     */
    public static boolean isGetter(String name) {
        return name.startsWith("get") || name.startsWith("is");
    }

    /**
     * 判断方法名是否为 setter 方法（以 'set' 开头）。
     * @param name 方法名
     * @return 如果方法名是 setter 方法，返回 true；否则返回 false
     */
    public static boolean isSetter(String name) {
        return name.startsWith("set");
    }
}
