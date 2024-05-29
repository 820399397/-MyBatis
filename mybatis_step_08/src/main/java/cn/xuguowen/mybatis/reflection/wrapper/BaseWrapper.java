package cn.xuguowen.mybatis.reflection.wrapper;

import cn.xuguowen.mybatis.reflection.MetaObject;
import cn.xuguowen.mybatis.reflection.property.PropertyTokenizer;

import java.util.List;
import java.util.Map;

/**
 * ClassName: BaseWrapper
 * Package: cn.xuguowen.mybatis.reflection.wrapper
 * Description:抽象基类，用于实现对象包装器接口，提供处理集合属性的方法。
 *
 * @Author 徐国文
 * @Create 2024/3/4 12:28
 * @Version 1.0
 */
public abstract class BaseWrapper implements ObjectWrapper{

    // 无参数的常量
    protected static final Object[] NO_ARGUMENTS = new Object[0];

    // 元对象
    protected MetaObject metaObject;

    /**
     * 构造函数，初始化 MetaObject。
     * @param metaObject MetaObject 实例
     */
    protected BaseWrapper(MetaObject metaObject) {
        this.metaObject = metaObject;
    }

    /**
     * 解析集合属性。
     * @param prop 属性分词器
     * @param object 对象
     * @return 如果属性名为空，返回对象本身；否则，返回属性值
     */
    protected Object resolveCollection(PropertyTokenizer prop, Object object) {
        if ("".equals(prop.getName())) {
            return object;
        } else {
            return metaObject.getValue(prop.getName());
        }
    }

    /**
     * 获取集合的值。
     * 中括号有两种含义，可以表示 Map 或 List/数组。
     * @param prop 属性分词器
     * @param collection 集合对象
     * @return 集合中的值
     */
    protected Object getCollectionValue(PropertyTokenizer prop, Object collection) {
        if (collection instanceof Map) {
            //map['name']
            return ((Map) collection).get(prop.getIndex());
        } else {
            int i = Integer.parseInt(prop.getIndex());
            if (collection instanceof List) {
                //list[0]
                return ((List) collection).get(i);
            } else if (collection instanceof Object[]) {
                return ((Object[]) collection)[i];
            } else if (collection instanceof char[]) {
                return ((char[]) collection)[i];
            } else if (collection instanceof boolean[]) {
                return ((boolean[]) collection)[i];
            } else if (collection instanceof byte[]) {
                return ((byte[]) collection)[i];
            } else if (collection instanceof double[]) {
                return ((double[]) collection)[i];
            } else if (collection instanceof float[]) {
                return ((float[]) collection)[i];
            } else if (collection instanceof int[]) {
                return ((int[]) collection)[i];
            } else if (collection instanceof long[]) {
                return ((long[]) collection)[i];
            } else if (collection instanceof short[]) {
                return ((short[]) collection)[i];
            } else {
                throw new RuntimeException("The '" + prop.getName() + "' property of " + collection + " is not a List or Array.");
            }
        }
    }

    /**
     * 设置集合的值。
     * 中括号有两种含义，可以表示 Map 或 List/数组。
     * @param prop 属性分词器
     * @param collection 集合对象
     * @param value 要设置的值
     */
    protected void setCollectionValue(PropertyTokenizer prop, Object collection, Object value) {
        if (collection instanceof Map) {
            ((Map) collection).put(prop.getIndex(), value);
        } else {
            int i = Integer.parseInt(prop.getIndex());
            if (collection instanceof List) {
                ((List) collection).set(i, value);
            } else if (collection instanceof Object[]) {
                ((Object[]) collection)[i] = value;
            } else if (collection instanceof char[]) {
                ((char[]) collection)[i] = (Character) value;
            } else if (collection instanceof boolean[]) {
                ((boolean[]) collection)[i] = (Boolean) value;
            } else if (collection instanceof byte[]) {
                ((byte[]) collection)[i] = (Byte) value;
            } else if (collection instanceof double[]) {
                ((double[]) collection)[i] = (Double) value;
            } else if (collection instanceof float[]) {
                ((float[]) collection)[i] = (Float) value;
            } else if (collection instanceof int[]) {
                ((int[]) collection)[i] = (Integer) value;
            } else if (collection instanceof long[]) {
                ((long[]) collection)[i] = (Long) value;
            } else if (collection instanceof short[]) {
                ((short[]) collection)[i] = (Short) value;
            } else {
                throw new RuntimeException("The '" + prop.getName() + "' property of " + collection + " is not a List or Array.");
            }
        }
    }
}
