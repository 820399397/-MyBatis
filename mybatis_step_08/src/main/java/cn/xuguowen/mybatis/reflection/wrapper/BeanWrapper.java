package cn.xuguowen.mybatis.reflection.wrapper;

import cn.xuguowen.mybatis.reflection.MetaClass;
import cn.xuguowen.mybatis.reflection.MetaObject;
import cn.xuguowen.mybatis.reflection.SystemMetaObject;
import cn.xuguowen.mybatis.reflection.factory.ObjectFactory;
import cn.xuguowen.mybatis.reflection.invoker.Invoker;
import cn.xuguowen.mybatis.reflection.property.PropertyTokenizer;

import java.util.List;

/**
 * ClassName: BeanWrapper
 * Package: cn.xuguowen.mybatis.reflection.wrapper
 * Description:用于包装 Java Bean 对象并提供访问和修改其属性的方法。
 *
 * @Author 徐国文
 * @Create 2024/3/4 12:16
 * @Version 1.0
 */
public class BeanWrapper extends BaseWrapper{

    // 原来的对象
    private Object object;
    // 元类，用于反射操作
    private MetaClass metaClass;

    /**
     * 构造函数，初始化 MetaObject 和待包装的对象。
     * @param metaObject MetaObject 实例
     * @param object 待包装的对象
     */
    public BeanWrapper(MetaObject metaObject, Object object) {
        super(metaObject);
        this.object = object;
        this.metaClass = MetaClass.forClass(object.getClass());
    }

    /**
     * 获取属性值。
     * 如果属性有索引（表示集合），则解析集合并获取值；否则，获取 Bean 属性值。
     * @param prop 属性分词器
     * @return 属性值
     */
    public Object get(PropertyTokenizer prop) {
        // 如果有index(有中括号),说明是集合，那就要解析集合,调用的是 BaseWrapper.resolveCollection 和 getCollectionValue
        if (prop.getIndex() != null) {
            Object collection = resolveCollection(prop, object);
            return getCollectionValue(prop, collection);
        } else {
            // 否则，getBeanProperty
            return getBeanProperty(prop, object);
        }
    }

    /**
     * 设置属性值。
     * 如果属性有索引（表示集合），则解析集合并设置值；否则，设置 Bean 属性值。
     * @param prop 属性分词器
     * @param value 要设置的值
     */
    public void set(PropertyTokenizer prop, Object value) {
        // 如果有index,说明是集合，那就要解析集合,调用的是BaseWrapper.resolveCollection 和 setCollectionValue
        if (prop.getIndex() != null) {
            Object collection = resolveCollection(prop, object);
            setCollectionValue(prop, collection, value);
        } else {
            // 否则，setBeanProperty
            setBeanProperty(prop, object, value);
        }
    }

    /**
     * 查找属性名。
     * @param name 属性名
     * @param useCamelCaseMapping 是否使用驼峰命名法
     * @return 找到的属性名
     */
    @Override
    public String findProperty(String name, boolean useCamelCaseMapping) {
        return metaClass.findProperty(name, useCamelCaseMapping);
    }

    /**
     * 获取所有 getter 方法的名称。
     * @return 所有 getter 方法的名称数组
     */
    @Override
    public String[] getGetterNames() {
        return metaClass.getGetterNames();
    }

    /**
     * 获取所有 setter 方法的名称。
     * @return 所有 setter 方法的名称数组
     */
    @Override
    public String[] getSetterNames() {
        return metaClass.getSetterNames();
    }

    /**
     * 获取属性的 setter 方法参数类型。
     * @param name 属性名
     * @return setter 方法参数类型
     */
    @Override
    public Class<?> getSetterType(String name) {
        PropertyTokenizer prop = new PropertyTokenizer(name);
        if (prop.hasNext()) {
            MetaObject metaValue = metaObject.metaObjectForProperty(prop.getIndexedName());
            if (metaValue == SystemMetaObject.NULL_META_OBJECT) {
                return metaClass.getSetterType(name);
            } else {
                return metaValue.getSetterType(prop.getChildren());
            }
        } else {
            return metaClass.getSetterType(name);
        }
    }

    /**
     * 获取属性的 getter 方法返回类型。
     * @param name 属性名
     * @return getter 方法返回类型
     */
    @Override
    public Class<?> getGetterType(String name) {
        PropertyTokenizer prop = new PropertyTokenizer(name);
        if (prop.hasNext()) {
            MetaObject metaValue = metaObject.metaObjectForProperty(prop.getIndexedName());
            if (metaValue == SystemMetaObject.NULL_META_OBJECT) {
                return metaClass.getGetterType(name);
            } else {
                return metaValue.getGetterType(prop.getChildren());
            }
        } else {
            return metaClass.getGetterType(name);
        }
    }

    /**
     * 判断是否有指定属性的 setter 方法。
     * @param name 属性名
     * @return 是否有 setter 方法
     */
    @Override
    public boolean hasSetter(String name) {
        PropertyTokenizer prop = new PropertyTokenizer(name);
        if (prop.hasNext()) {
            if (metaClass.hasSetter(prop.getIndexedName())) {
                MetaObject metaValue = metaObject.metaObjectForProperty(prop.getIndexedName());
                if (metaValue == SystemMetaObject.NULL_META_OBJECT) {
                    return metaClass.hasSetter(name);
                } else {
                    return metaValue.hasSetter(prop.getChildren());
                }
            } else {
                return false;
            }
        } else {
            return metaClass.hasSetter(name);
        }
    }

    /**
     * 判断是否有指定属性的 getter 方法。
     * @param name 属性名
     * @return 是否有 getter 方法
     */
    @Override
    public boolean hasGetter(String name) {
        PropertyTokenizer prop = new PropertyTokenizer(name);
        if (prop.hasNext()) {
            if (metaClass.hasGetter(prop.getIndexedName())) {
                MetaObject metaValue = metaObject.metaObjectForProperty(prop.getIndexedName());
                if (metaValue == SystemMetaObject.NULL_META_OBJECT) {
                    return metaClass.hasGetter(name);
                } else {
                    return metaValue.hasGetter(prop.getChildren());
                }
            } else {
                return false;
            }
        } else {
            return metaClass.hasGetter(name);
        }
    }

    /**
     * 实例化属性值。
     * @param name 属性名
     * @param prop 属性分词器
     * @param objectFactory 对象工厂
     * @return 实例化后的 MetaObject
     */
    @Override
    public MetaObject instantiatePropertyValue(String name, PropertyTokenizer prop, ObjectFactory objectFactory) {
        MetaObject metaValue;
        Class<?> type = getSetterType(prop.getName());
        try {
            Object newObject = objectFactory.create(type);
            metaValue = MetaObject.forObject(newObject, metaObject.getObjectFactory(), metaObject.getObjectWrapperFactory());
            set(prop, newObject);
        } catch (Exception e) {
            throw new RuntimeException("Cannot set value of property '" + name + "' because '" + name + "' is null and cannot be instantiated on instance of " + type.getName() + ". Cause:" + e.toString(), e);
        }
        return metaValue;
    }

    /**
     * 判断是否是集合。
     * @return 是否是集合
     */
    @Override
    public boolean isCollection() {
        return false;
    }

    /**
     * 向集合中添加元素。
     * 不支持的操作，直接抛出异常。
     * @param element 要添加的元素
     */
    @Override
    public void add(Object element) {
        throw new UnsupportedOperationException();
    }

    /**
     * 向集合中添加所有元素。
     * 不支持的操作，直接抛出异常。
     * @param list 要添加的元素列表
     * @param <E> 元素类型
     */
    @Override
    public <E> void addAll(List<E> list) {
        throw new UnsupportedOperationException();
    }

    /**
     * 获取 Bean 属性值。
     * @param prop 属性分词器
     * @param object 目标对象
     * @return 属性值
     */
    private Object getBeanProperty(PropertyTokenizer prop, Object object) {
        try {
            // 得到getter方法，然后调用
            Invoker method = metaClass.getGetInvoker(prop.getName());
            return method.invoke(object, NO_ARGUMENTS);
        } catch (RuntimeException e) {
            throw e;
        } catch (Throwable t) {
            throw new RuntimeException("Could not get property '" + prop.getName() + "' from " + object.getClass() + ".  Cause: " + t.toString(), t);
        }
    }

    /**
     * 设置 Bean 属性值。
     * @param prop 属性分词器
     * @param object 目标对象
     * @param value 要设置的值
     */
    private void setBeanProperty(PropertyTokenizer prop, Object object, Object value) {
        try {
            // 得到setter方法，然后调用
            Invoker method = metaClass.getSetInvoker(prop.getName());
            Object[] params = {value};
            method.invoke(object, params);
        } catch (Throwable t) {
            throw new RuntimeException("Could not set property '" + prop.getName() + "' of '" + object.getClass() + "' with value '" + value + "' Cause: " + t.toString(), t);
        }
    }
}
