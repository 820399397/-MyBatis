package cn.xuguowen.mybatis.reflection;

import cn.xuguowen.mybatis.reflection.factory.DefaultObjectFactory;
import cn.xuguowen.mybatis.reflection.factory.ObjectFactory;
import cn.xuguowen.mybatis.reflection.wrapper.DefaultObjectWrapperFactory;
import cn.xuguowen.mybatis.reflection.wrapper.ObjectWrapperFactory;

/**
 * ClassName: SystemMetaObject
 * Package: cn.xuguowen.mybatis.reflection
 * Description:
 *
 * @Author 徐国文
 * @Create 2024/3/4 12:17
 * @Version 1.0
 */
public class SystemMetaObject {
    public static final ObjectFactory DEFAULT_OBJECT_FACTORY = new DefaultObjectFactory();
    public static final ObjectWrapperFactory DEFAULT_OBJECT_WRAPPER_FACTORY = new DefaultObjectWrapperFactory();
    public static final MetaObject NULL_META_OBJECT = MetaObject.forObject(NullObject.class, DEFAULT_OBJECT_FACTORY, DEFAULT_OBJECT_WRAPPER_FACTORY);

    private SystemMetaObject() {
        // Prevent Instantiation of Static Class
    }

    /**
     * 空对象
     */
    private static class NullObject {
    }

    public static MetaObject forObject(Object object) {
        return MetaObject.forObject(object, DEFAULT_OBJECT_FACTORY, DEFAULT_OBJECT_WRAPPER_FACTORY);
    }
}
