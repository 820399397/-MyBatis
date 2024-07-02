package cn.xuguowen.mybatis.reflection.wrapper;

import cn.xuguowen.mybatis.reflection.MetaObject;
import cn.xuguowen.mybatis.reflection.factory.ObjectFactory;
import cn.xuguowen.mybatis.reflection.property.PropertyTokenizer;

import java.util.Collection;
import java.util.List;

/**
 * ClassName: CollectionWrapper
 * Package: cn.xuguowen.mybatis.reflection.wrapper
 * Description:用于包装集合对象，并提供向集合中添加元素的方法。
 *
 * @Author 徐国文
 * @Create 2024/3/4 12:15
 * @Version 1.0
 */
public class CollectionWrapper implements ObjectWrapper{

    // 被包装的集合对象
    private Collection<Object> object;

    /**
     * 构造函数，初始化 MetaObject 和待包装的集合对象。
     * @param metaObject MetaObject 实例
     * @param object 待包装的集合对象
     */
    public CollectionWrapper(MetaObject metaObject, Collection<Object> object) {
        this.object = object;
    }

    /**
     * 获取属性值。
     * 集合类型不支持该操作，直接抛出 UnsupportedOperationException 异常。get,set都是不允许的,只能添加元素
     * @param prop 属性分词器
     * @return 无返回值，总是抛出异常
     */
    @Override
    public Object get(PropertyTokenizer prop) {
        throw new UnsupportedOperationException();
    }

    /**
     * 设置属性值。
     * 集合类型不支持该操作，直接抛出 UnsupportedOperationException 异常。get,set都是不允许的,只能添加元素
     * @param prop 属性分词器
     * @param value 要设置的值
     */
    @Override
    public void set(PropertyTokenizer prop, Object value) {
        throw new UnsupportedOperationException();
    }

    /**
     * 查找属性名。
     * 集合类型不支持该操作，直接抛出 UnsupportedOperationException 异常。
     * @param name 属性名
     * @param useCamelCaseMapping 是否使用驼峰命名法
     * @return 无返回值，总是抛出异常
     */
    @Override
    public String findProperty(String name, boolean useCamelCaseMapping) {
        throw new UnsupportedOperationException();
    }

    /**
     * 获取所有 getter 方法的名称。
     * 集合类型不支持该操作，直接抛出 UnsupportedOperationException 异常。
     * @return 无返回值，总是抛出异常
     */
    @Override
    public String[] getGetterNames() {
        throw new UnsupportedOperationException();
    }

    /**
     * 获取所有 setter 方法的名称。
     * 集合类型不支持该操作，直接抛出 UnsupportedOperationException 异常。
     * @return 无返回值，总是抛出异常
     */
    @Override
    public String[] getSetterNames() {
        throw new UnsupportedOperationException();
    }

    /**
     * 获取属性的 setter 方法参数类型。
     * 集合类型不支持该操作，直接抛出 UnsupportedOperationException 异常。
     * @param name 属性名
     * @return 无返回值，总是抛出异常
     */
    @Override
    public Class<?> getSetterType(String name) {
        throw new UnsupportedOperationException();
    }

    /**
     * 获取属性的 getter 方法返回类型。
     * 集合类型不支持该操作，直接抛出 UnsupportedOperationException 异常。
     * @param name 属性名
     * @return 无返回值，总是抛出异常
     */
    @Override
    public Class<?> getGetterType(String name) {
        throw new UnsupportedOperationException();
    }

    /**
     * 判断是否有指定属性的 setter 方法。
     * 集合类型不支持该操作，直接抛出 UnsupportedOperationException 异常。
     * @param name 属性名
     * @return 无返回值，总是抛出异常
     */
    @Override
    public boolean hasSetter(String name) {
        throw new UnsupportedOperationException();
    }

    /**
     * 判断是否有指定属性的 getter 方法。
     * 集合类型不支持该操作，直接抛出 UnsupportedOperationException 异常。
     * @param name 属性名
     * @return 无返回值，总是抛出异常
     */
    @Override
    public boolean hasGetter(String name) {
        throw new UnsupportedOperationException();
    }

    /**
     * 实例化属性值。
     * 集合类型不支持该操作，直接抛出 UnsupportedOperationException 异常。
     * @param name 属性名
     * @param prop 属性分词器
     * @param objectFactory 对象工厂
     * @return 无返回值，总是抛出异常
     */
    @Override
    public MetaObject instantiatePropertyValue(String name, PropertyTokenizer prop, ObjectFactory objectFactory) {
        throw new UnsupportedOperationException();
    }

    /**
     * 判断是否是集合。
     * @return 返回 true，表示是集合类型
     */
    @Override
    public boolean isCollection() {
        return true;
    }

    /**
     * 向集合中添加元素。
     * @param element 要添加的元素
     */
    @Override
    public void add(Object element) {
        object.add(element);
    }

    /**
     * 向集合中添加所有元素。
     * @param element 要添加的元素列表
     * @param <E> 元素类型
     */
    @Override
    public <E> void addAll(List<E> element) {
        object.addAll(element);
    }
}
