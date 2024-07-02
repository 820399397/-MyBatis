package cn.xuguowen.mybatis.reflection;

import cn.xuguowen.mybatis.reflection.factory.DefaultObjectFactory;
import cn.xuguowen.mybatis.reflection.factory.ObjectFactory;
import cn.xuguowen.mybatis.reflection.wrapper.DefaultObjectWrapperFactory;
import cn.xuguowen.mybatis.reflection.wrapper.ObjectWrapperFactory;

/**
 * ClassName: SystemMetaObject
 * Package: cn.xuguowen.mybatis.reflection
 * Description:系统元对象类，提供默认的对象工厂、对象包装工厂以及空元对象的静态实例。
 *
 * @Author 徐国文
 * @Create 2024/3/4 12:17
 * @Version 1.0
 */
public class SystemMetaObject {
    // 默认对象工厂
    public static final ObjectFactory DEFAULT_OBJECT_FACTORY = new DefaultObjectFactory();

    // 默认对象包装工厂
    public static final ObjectWrapperFactory DEFAULT_OBJECT_WRAPPER_FACTORY = new DefaultObjectWrapperFactory();

    // 空元对象，防止NullPointerException
    public static final MetaObject NULL_META_OBJECT = MetaObject.forObject(NullObject.class, DEFAULT_OBJECT_FACTORY, DEFAULT_OBJECT_WRAPPER_FACTORY);

    // 私有构造函数，防止实例化
    private SystemMetaObject() {
        // Prevent Instantiation of Static Class
    }

    /**
     * 空对象内部类，作为NULL_META_OBJECT的类型
     */
    private static class NullObject {
    }

    /**
     * 使用默认的对象工厂和对象包装工厂创建一个MetaObject实例
     *
     * @param object 需要包装的对象
     * @return 创建的MetaObject实例
     */
    public static MetaObject forObject(Object object) {
        return MetaObject.forObject(object, DEFAULT_OBJECT_FACTORY, DEFAULT_OBJECT_WRAPPER_FACTORY);
    }
}
