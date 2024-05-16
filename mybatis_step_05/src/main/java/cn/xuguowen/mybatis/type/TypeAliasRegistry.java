package cn.xuguowen.mybatis.type;

import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * ClassName: TypeAliasRegistry
 * Package: cn.xuguowen.mybatis.type
 * Description:在 Mybatis 框架中我们所需要的基本类型、数组类型以及自己定义的事务实现和事务工厂都需要注册到类型别名的注册器中进行管理，
 *             在我们需要使用的时候可以从注册器中获取到具体的对象类型，之后在进行实例化的方式进行使用。
 *             在 TypeAliasRegistry 类型别名注册器中先做了一些基本的类型注册，以及提供 registerAlias 注册方法和 resolveAlias 获取方法。
 *
 * @Author 徐国文
 * @Create 2024/2/15 21:21
 * @Version 1.0
 */
public class TypeAliasRegistry {

    private final Map<String, Class<?>> TYPE_ALIASES = new HashMap<>();

    public TypeAliasRegistry() {
        // 注册一些常用的类型别名
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

    public void registerAlias(String alias, Class<?> value) {
        String key = alias.toLowerCase(Locale.ENGLISH);
        TYPE_ALIASES.put(key, value);
    }

    public <T> Class<T> resolveAlias(String alias) {
        String key = alias.toLowerCase(Locale.ENGLISH);
        return (Class<T>) TYPE_ALIASES.get(key);
    }
}


