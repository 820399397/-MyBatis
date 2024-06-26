package cn.xuguowen.mybatis.reflection.factory;

import java.util.List;
import java.util.Properties;

/**
 * ClassName: ObjectFactory
 * Package: cn.xuguowen.mybatis.reflection.wrapper
 * Description:对象工厂接口，定义创建对象和设置属性的方法，支持不同类型的对象创建。
 *
 * @Author 徐国文
 * @Create 2024/3/4 12:13
 * @Version 1.0
 */
public interface ObjectFactory {
    /**
     * Sets configuration properties.
     * 设置配置属性
     *
     * @param properties configuration properties
     */
    void setProperties(Properties properties);

    /**
     * Creates a new object with default constructor.
     * 使用默认构造函数创建一个新对象
     *
     * @param type Object type
     * @return <T>
     */
    <T> T create(Class<T> type);

    /**
     * Creates a new object with the specified constructor and params.
     * 生产对象，使用指定的构造函数和构造函数参数
     *
     * @param type                Object type
     * @param constructorArgTypes Constructor argument types
     * @param constructorArgs     Constructor argument values
     * @return <T>
     */
    <T> T create(Class<T> type, List<Class<?>> constructorArgTypes, List<Object> constructorArgs);

    /**
     * Returns true if this object can have a set of other objects.
     * It's main purpose is to support non-java.util.Collection objects like Scala collections.
     * 判断指定类型是否为集合类型。主要用于支持非 java.util.Collection 的集合类型，比如 Scala 集合。
     *
     * @param type Object type
     * @return whether it is a collection or not
     * @since 3.1.0
     */
    <T> boolean isCollection(Class<T> type);
}
