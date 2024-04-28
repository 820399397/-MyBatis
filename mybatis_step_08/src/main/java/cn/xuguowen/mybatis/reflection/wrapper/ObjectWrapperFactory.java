package cn.xuguowen.mybatis.reflection.wrapper;

import cn.xuguowen.mybatis.reflection.MetaObject;

/**
 * ClassName: ObjectWrapperFactory
 * Package: cn.xuguowen.mybatis.reflection.wrapper
 * Description:
 *
 * @Author 徐国文
 * @Create 2024/3/4 12:14
 * @Version 1.0
 */
public interface ObjectWrapperFactory {
    boolean hasWrapperFor(Object object);

    ObjectWrapper getWrapperFor(MetaObject metaObject, Object object);
}
