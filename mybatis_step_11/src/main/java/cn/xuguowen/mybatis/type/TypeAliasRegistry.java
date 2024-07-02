package cn.xuguowen.mybatis.type;

import cn.xuguowen.mybatis.io.Resources;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * ClassName: TypeAliasRegistry
 * Package: cn.xuguowen.mybatis.type
 * Description:该类用于注册和解析类型别名，将字符串别名映射到对应的 Java 类。
 *
 * @Author 徐国文
 * @Create 2024/2/15 21:21
 * @Version 1.0
 */
public class TypeAliasRegistry {

    // 存储类型别名和对应的 Java 类之间的映射关系
    private final Map<String, Class<?>> TYPE_ALIASES = new HashMap<>();

    /**
     * 构造函数，初始化时注册一些常用的类型别名。
     */
    public TypeAliasRegistry() {
        registerAlias("string", String.class);

        registerAlias("byte", Byte.class);
        registerAlias("long", Long.class);
        registerAlias("short", Short.class);
        registerAlias("int", Integer.class);
        registerAlias("integer", Integer.class);
        registerAlias("double", Double.class);
        registerAlias("float", Float.class);
        registerAlias("boolean", Boolean.class);
        registerAlias("long", Long.class);
        registerAlias("map", Map.class);
    }

    /**
     * 注册类型别名
     *
     * @param alias 别名
     * @param value 别名对应的 Java 类
     */
    public void registerAlias(String alias, Class<?> value) {
        String key = alias.toLowerCase(Locale.ENGLISH);
        TYPE_ALIASES.put(key, value);
    }

    /**
     * 解析类型别名，将字符串别名解析为对应的 Java 类
     *
     * @param string 别名字符串
     * @param <T>    类型参数
     * @return 别名对应的 Java 类
     * @throws RuntimeException 如果无法解析别名
     */
    public <T> Class<T> resolveAlias(String string) {
        try {
            if (string == null) {
                return null;
            }
            // java.lang.Long ==> java.lang.long
            String key = string.toLowerCase(Locale.ENGLISH);
            Class<T> value;
            if (TYPE_ALIASES.containsKey(key)) {
                value = (Class<T>) TYPE_ALIASES.get(key);
            } else {
                value = (Class<T>) Resources.classForName(string);
            }
            return value;
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Could not resolve type alias '" + string + "'.  Cause: " + e, e);
        }
    }

}


