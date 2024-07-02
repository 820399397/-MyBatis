package cn.xuguowen.mybatis.reflection.wrapper;

import cn.xuguowen.mybatis.reflection.MetaObject;
import cn.xuguowen.mybatis.reflection.SystemMetaObject;
import cn.xuguowen.mybatis.reflection.factory.ObjectFactory;
import cn.xuguowen.mybatis.reflection.property.PropertyTokenizer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ClassName: MapWrapper
 * Package: cn.xuguowen.mybatis.reflection.wrapper
 * Description:一个针对 Map 类型对象的包装类，用于在 MyBatis 反射中处理 Map 类型的属性和方法。
 *
 * @Author 徐国文
 * @Create 2024/3/4 12:13
 * @Version 1.0
 */
public class MapWrapper extends BaseWrapper{

    // 被包装的 Map 对象
    private Map<String, Object> map;

    /**
     * 构造函数，接受一个 MetaObject 和一个 Map 对象。
     *
     * @param metaObject MetaObject 对象
     * @param map 被包装的 Map 对象
     */
    public MapWrapper(MetaObject metaObject, Map<String, Object> map) {
        super(metaObject);
        this.map = map;
    }

    /**
     * 获取属性的值。如果属性是一个集合，则解析集合并返回对应的值。
     * get,set是允许的
     * @param prop 属性标记器
     * @return 属性的值
     */
    public Object get(PropertyTokenizer prop) {
        //如果有index,说明是集合，那就要分解集合,调用的是BaseWrapper.resolveCollection 和 getCollectionValue
        if (prop.getIndex() != null) {
            Object collection = resolveCollection(prop, map);
            return getCollectionValue(prop, collection);
        } else {
            return map.get(prop.getName());
        }
    }

    /**
     * 设置属性的值。如果属性是一个集合，则解析集合并设置对应的值。
     *
     * @param prop 属性标记器
     * @param value 要设置的值
     */
    public void set(PropertyTokenizer prop, Object value) {
        if (prop.getIndex() != null) {
            Object collection = resolveCollection(prop, map);
            setCollectionValue(prop, collection, value);
        } else {
            map.put(prop.getName(), value);
        }
    }

    /**
     * 查找属性的名称。
     *
     * @param name 属性名称
     * @param useCamelCaseMapping 是否使用驼峰命名映射
     * @return 属性名称
     */
    @Override
    public String findProperty(String name, boolean useCamelCaseMapping) {
        return name;
    }

    /**
     * 获取所有的 Getter 方法名称。
     *
     * @return Getter 方法名称数组
     */
    @Override
    public String[] getGetterNames() {
        return map.keySet().toArray(new String[map.keySet().size()]);
    }

    /**
     * 获取所有的 Setter 方法名称。
     *
     * @return Setter 方法名称数组
     */
    @Override
    public String[] getSetterNames() {
        return map.keySet().toArray(new String[map.keySet().size()]);
    }

    /**
     * 获取 Setter 方法的参数类型。
     *
     * @param name 属性名称
     * @return 参数类型
     */
    @Override
    public Class<?> getSetterType(String name) {
        PropertyTokenizer prop = new PropertyTokenizer(name);
        if (prop.hasNext()) {
            MetaObject metaValue = metaObject.metaObjectForProperty(prop.getIndexedName());
            if (metaValue == SystemMetaObject.NULL_META_OBJECT) {
                return Object.class;
            } else {
                return metaValue.getSetterType(prop.getChildren());
            }
        } else {
            if (map.get(name) != null) {
                return map.get(name).getClass();
            } else {
                return Object.class;
            }
        }
    }

    /**
     * 获取 Getter 方法的返回类型。
     *
     * @param name 属性名称
     * @return 返回类型
     */
    @Override
    public Class<?> getGetterType(String name) {
        PropertyTokenizer prop = new PropertyTokenizer(name);
        if (prop.hasNext()) {
            MetaObject metaValue = metaObject.metaObjectForProperty(prop.getIndexedName());
            if (metaValue == SystemMetaObject.NULL_META_OBJECT) {
                return Object.class;
            } else {
                return metaValue.getGetterType(prop.getChildren());
            }
        } else {
            if (map.get(name) != null) {
                return map.get(name).getClass();
            } else {
                return Object.class;
            }
        }
    }

    /**
     * 判断是否有对应属性的 Setter 方法。
     *
     * @param name 属性名称
     * @return 是否有 Setter 方法
     */
    @Override
    public boolean hasSetter(String name) {
        return true;
    }

    /**
     * 判断是否有对应属性的 Getter 方法。
     *
     * @param name 属性名称
     * @return 是否有 Getter 方法
     */
    @Override
    public boolean hasGetter(String name) {
        PropertyTokenizer prop = new PropertyTokenizer(name);
        if (prop.hasNext()) {
            if (map.containsKey(prop.getIndexedName())) {
                MetaObject metaValue = metaObject.metaObjectForProperty(prop.getIndexedName());
                if (metaValue == SystemMetaObject.NULL_META_OBJECT) {
                    return true;
                } else {
                    return metaValue.hasGetter(prop.getChildren());
                }
            } else {
                return false;
            }
        } else {
            return map.containsKey(prop.getName());
        }
    }

    /**
     * 实例化属性值。
     *
     * @param name 属性名称
     * @param prop 属性标记器
     * @param objectFactory 对象工厂
     * @return 实例化的 MetaObject
     */
    @Override
    public MetaObject instantiatePropertyValue(String name, PropertyTokenizer prop, ObjectFactory objectFactory) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        set(prop, map);
        return MetaObject.forObject(map, metaObject.getObjectFactory(), metaObject.getObjectWrapperFactory());
    }

    /**
     * 判断是否是集合类型。
     *
     * @return 是否是集合类型
     */
    @Override
    public boolean isCollection() {
        return false;
    }

    /**
     * 向集合添加元素。
     *
     * @param element 要添加的元素
     */
    @Override
    public void add(Object element) {
        throw new UnsupportedOperationException();
    }

    /**
     * 向集合添加所有元素。
     *
     * @param element 要添加的元素列表
     * @param <E> 元素类型
     */
    @Override
    public <E> void addAll(List<E> element) {
        throw new UnsupportedOperationException();
    }
}
