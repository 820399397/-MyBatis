package cn.xuguowen.mybatis.reflection;

import cn.xuguowen.mybatis.pojo.User;
import cn.xuguowen.mybatis.reflection.factory.DefaultObjectFactory;
import cn.xuguowen.mybatis.reflection.factory.ObjectFactory;
import cn.xuguowen.mybatis.reflection.property.PropertyTokenizer;
import cn.xuguowen.mybatis.reflection.wrapper.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * ClassName: MetaObject
 * Package: cn.xuguowen.mybatis.reflection
 * Description:该类用于包装对象并提供对其属性的访问和修改功能。它通过委派给适当的 ObjectWrapper 实现不同类型对象的操作。
 *
 * @Author 徐国文
 * @Create 2024/3/4 12:13
 * @Version 1.0
 */
public class MetaObject {
    // 原对象
    private Object originalObject;
    // 对象包装器
    private ObjectWrapper objectWrapper;
    // 对象工厂
    private ObjectFactory objectFactory;
    // 对象包装工厂
    private ObjectWrapperFactory objectWrapperFactory;

    /**
     * 私有构造方法，用于初始化 MetaObject 实例。
     *
     * @param object 原始对象
     * @param objectFactory 对象工厂
     * @param objectWrapperFactory 对象包装工厂
     */
    private MetaObject(Object object, ObjectFactory objectFactory, ObjectWrapperFactory objectWrapperFactory) {
        this.originalObject = object;
        this.objectFactory = objectFactory;
        this.objectWrapperFactory = objectWrapperFactory;

        if (object instanceof ObjectWrapper) {
            // 如果对象本身已经是ObjectWrapper型，则直接赋给objectWrapper
            this.objectWrapper = (ObjectWrapper) object;
        } else if (objectWrapperFactory.hasWrapperFor(object)) {
            // 如果有包装器,调用ObjectWrapperFactory.getWrapperFor
            this.objectWrapper = objectWrapperFactory.getWrapperFor(this, object);
        } else if (object instanceof Map) {
            // 如果是Map型，返回MapWrapper
            this.objectWrapper = new MapWrapper(this, (Map) object);
        } else if (object instanceof Collection) {
            // 如果是Collection型，返回CollectionWrapper
            this.objectWrapper = new CollectionWrapper(this, (Collection) object);
        } else {
            // 除此以外，返回BeanWrapper
            this.objectWrapper = new BeanWrapper(this, object);
        }
    }

    /**
     * 静态工厂方法，用于创建 MetaObject 实例。
     *
     * @param object 原始对象
     * @param objectFactory 对象工厂
     * @param objectWrapperFactory 对象包装工厂
     * @return MetaObject 实例
     */
    public static MetaObject forObject(Object object, ObjectFactory objectFactory, ObjectWrapperFactory objectWrapperFactory) {
        if (object == null) {
            // 处理一下null,将null包装起来
            return SystemMetaObject.NULL_META_OBJECT;
        } else {
            return new MetaObject(object, objectFactory, objectWrapperFactory);
        }
    }

    public ObjectFactory getObjectFactory() {
        return objectFactory;
    }

    public ObjectWrapperFactory getObjectWrapperFactory() {
        return objectWrapperFactory;
    }

    public Object getOriginalObject() {
        return originalObject;
    }

    /* --------以下方法都是委派给 ObjectWrapper------ */

    /**
     * 查找属性名。
     *
     * @param propName 属性名
     * @param useCamelCaseMapping 是否使用驼峰命名法
     * @return 找到的属性名
     */
    public String findProperty(String propName, boolean useCamelCaseMapping) {
        return objectWrapper.findProperty(propName, useCamelCaseMapping);
    }

    /**
     * 获取所有 getter 方法的名字。
     *
     * @return getter 方法名字数组
     */
    public String[] getGetterNames() {
        return objectWrapper.getGetterNames();
    }

    /**
     * 获取所有 setter 方法的名字。
     *
     * @return setter 方法名字数组
     */
    public String[] getSetterNames() {
        return objectWrapper.getSetterNames();
    }

    /**
     * 获取指定 setter 方法的类型。
     *
     * @param name 属性名
     * @return setter 方法的类型
     */
    public Class<?> getSetterType(String name) {
        return objectWrapper.getSetterType(name);
    }

    /**
     * 获取指定 getter 方法的类型。
     *
     * @param name 属性名
     * @return getter 方法的类型
     */
    public Class<?> getGetterType(String name) {
        return objectWrapper.getGetterType(name);
    }

    /**
     * 检查是否存在指定属性的 setter 方法。
     *
     * @param name 属性名
     * @return 如果存在 setter 方法，则返回 true；否则返回 false
     */
    public boolean hasSetter(String name) {
        return objectWrapper.hasSetter(name);
    }

    /**
     * 检查是否存在指定属性的 getter 方法。
     *
     * @param name 属性名
     * @return 如果存在 getter 方法，则返回 true；否则返回 false
     */
    public boolean hasGetter(String name) {
        return objectWrapper.hasGetter(name);
    }

    /**
     * 获取指定属性的值。
     * 如 班级[0].学生.成绩
     * @param name 属性名
     * @return 属性值
     */
    public Object getValue(String name) {
        PropertyTokenizer prop = new PropertyTokenizer(name);
        if (prop.hasNext()) {
            MetaObject metaValue = metaObjectForProperty(prop.getIndexedName());
            if (metaValue == SystemMetaObject.NULL_META_OBJECT) {
                // 如果上层就是null了，那就结束，返回null
                return null;
            } else {
                // 否则继续看下一层，递归调用getValue
                return metaValue.getValue(prop.getChildren());
            }
        } else {
            return objectWrapper.get(prop);
        }
    }

    /**
     * 设置指定属性的值。
     * 如 班级[0].学生.成绩
     * @param name 属性名
     * @param value 要设置的值
     */
    public void setValue(String name, Object value) {
        PropertyTokenizer prop = new PropertyTokenizer(name);
        if (prop.hasNext()) {
            MetaObject metaValue = metaObjectForProperty(prop.getIndexedName());
            if (metaValue == SystemMetaObject.NULL_META_OBJECT) {
                if (value == null && prop.getChildren() != null) {
                    // don't instantiate child path if value is null
                    // 如果上层就是 null 了，还得看有没有儿子，没有那就结束
                    return;
                } else {
                    // 否则还得 new 一个，委派给 ObjectWrapper.instantiatePropertyValue
                    metaValue = objectWrapper.instantiatePropertyValue(name, prop, objectFactory);
                }
            }
            // 递归调用setValue
            metaValue.setValue(prop.getChildren(), value);
        } else {
            // 到了最后一层了，所以委派给 ObjectWrapper.set
            objectWrapper.set(prop, value);
        }
    }

    /**
     * 为属性生成元对象。
     *
     * @param name 属性名
     * @return 生成的 MetaObject 实例
     */
    public MetaObject metaObjectForProperty(String name) {
        // 实际是递归调用
        Object value = getValue(name);
        return MetaObject.forObject(value, objectFactory, objectWrapperFactory);
    }

    public ObjectWrapper getObjectWrapper() {
        return objectWrapper;
    }

    /**
     * 检查对象是否是集合类型。
     *
     * @return 如果对象是集合类型，则返回 true；否则返回 false
     */
    public boolean isCollection() {
        return objectWrapper.isCollection();
    }

    /**
     * 向集合中添加元素。
     *
     * @param element 要添加的元素
     */
    public void add(Object element) {
        objectWrapper.add(element);
    }

    /**
     * 向集合中添加所有元素。
     *
     * @param list 要添加的元素列表
     * @param <E> 元素类型
     */
    public <E> void addAll(List<E> list) {
        objectWrapper.addAll(list);
    }

    public static void main(String[] args) {
        User user = new User();
        user.setUserId("1001");
        user.setUserName("John");
        user.setScores(Arrays.asList(98,99));

        // 创建 MetaObject 实例
        MetaObject metaObject = MetaObject.forObject(user, new DefaultObjectFactory(), new DefaultObjectWrapperFactory());

        // 测试获取属性值
        System.out.println("userName: " + metaObject.getValue("userName")); // 输出: John
        System.out.println("userId: " + metaObject.getValue("userId")); // 输出: 30
        System.out.println("scores: " + metaObject.getValue("scores")); // 输出: 90

        // 测试设置属性值
        metaObject.setValue("userName", "Jane");
        metaObject.setValue("userId", "25");
        metaObject.setValue("scores[1]", 95);

        // 再次获取属性值以验证更改
        System.out.println("Updated userName: " + metaObject.getValue("userName")); // 输出: Jane
        System.out.println("Updated userId: " + metaObject.getValue("userId")); // 输出: 25
        System.out.println("Updated Second Score: " + metaObject.getValue("scores[1]")); // 输出: 95
    }

}
