package cn.xuguowen.mybatis.type;


import java.lang.reflect.Type;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

/**
 * ClassName: TypeHandlerRegistry
 * Package: cn.xuguowen.mybatis.type
 * Description:该类用于注册和管理各种类型的 TypeHandler，处理 Java 类型与 JDBC 类型之间的转换。
 *
 * @Author 徐国文
 * @Create 2024/5/30 9:42
 * @Version 1.0
 */
public class TypeHandlerRegistry {

    // 用于存储JDBC类型到TypeHandler的映射
    private final Map<JdbcType, TypeHandler<?>> JDBC_TYPE_HANDLER_MAP = new EnumMap<>(JdbcType.class);

    // 用于存储Java类型和JDBC类型到TypeHandler的映射
    private final Map<Type, Map<JdbcType, TypeHandler<?>>> TYPE_HANDLER_MAP = new HashMap<>();

    // 用于存储所有的TypeHandler
    private final Map<Class<?>, TypeHandler<?>> ALL_TYPE_HANDLERS_MAP = new HashMap<>();

    // 构造函数，注册常用的TypeHandler
    public TypeHandlerRegistry() {
        register(Long.class, new LongTypeHandler());
        register(long.class, new LongTypeHandler());

        register(String.class, new StringTypeHandler());
        register(String.class, JdbcType.CHAR, new StringTypeHandler());
        register(String.class, JdbcType.VARCHAR, new StringTypeHandler());
    }

    /**
     * 注册 Java 类型和对应的 TypeHandler
     *
     * @param javaType    Java 类型
     * @param typeHandler TypeHandler 实例
     * @param <T>         TypeHandler 处理的类型
     */
    private <T> void register(Type javaType, TypeHandler<? extends T> typeHandler) {
        register(javaType, null, typeHandler);
    }

    /**
     * 注册 Java 类型、JDBC 类型和对应的 TypeHandler
     *
     * @param javaType Java 类型
     * @param jdbcType JDBC 类型
     * @param handler  TypeHandler 实例
     */
    private void register(Type javaType, JdbcType jdbcType, TypeHandler<?> handler) {
        if (null != javaType) {
            Map<JdbcType, TypeHandler<?>> map = TYPE_HANDLER_MAP.computeIfAbsent(javaType, k -> new HashMap<>());
            map.put(jdbcType, handler);
        }
        ALL_TYPE_HANDLERS_MAP.put(handler.getClass(), handler);
    }

    /**
     * 获取指定 Java 类型和 JDBC 类型的 TypeHandler
     *
     * @param type     Java 类型
     * @param jdbcType JDBC 类型
     * @param <T>      TypeHandler 处理的类型
     * @return 对应的 TypeHandler 实例
     */
    @SuppressWarnings("unchecked")
    public <T> TypeHandler<T> getTypeHandler(Class<T> type, JdbcType jdbcType) {
        return getTypeHandler((Type) type, jdbcType);
    }

    /**
     * 检查是否存在指定 Java 类型的 TypeHandler
     *
     * @param javaType Java 类型
     * @return 如果存在则返回 true，否则返回 false
     */
    public boolean hasTypeHandler(Class<?> javaType) {
        return hasTypeHandler(javaType, null);
    }

    /**
     * 检查是否存在指定 Java 类型和 JDBC 类型的 TypeHandler
     *
     * @param javaType Java 类型
     * @param jdbcType JDBC 类型
     * @return 如果存在则返回 true，否则返回 false
     */
    public boolean hasTypeHandler(Class<?> javaType, JdbcType jdbcType) {
        return javaType != null && getTypeHandler((Type) javaType, jdbcType) != null;
    }

    /**
     * 获取指定 Java 类型和 JDBC 类型的 TypeHandler
     *
     * @param type     Java 类型
     * @param jdbcType JDBC 类型
     * @param <T>      TypeHandler 处理的类型
     * @return 对应的 TypeHandler 实例
     */
    private <T> TypeHandler<T> getTypeHandler(Type type, JdbcType jdbcType) {
        Map<JdbcType, TypeHandler<?>> jdbcHandlerMap = TYPE_HANDLER_MAP.get(type);
        TypeHandler<?> handler = null;
        if (jdbcHandlerMap != null) {
            handler = jdbcHandlerMap.get(jdbcType);
            if (handler == null) {
                handler = jdbcHandlerMap.get(null);
            }
        }
        // type drives generics here
        return (TypeHandler<T>) handler;
    }

}
