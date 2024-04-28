package cn.xuguowen.mybatis.reflection.wrapper;

import cn.xuguowen.mybatis.reflection.MetaObject;

/**
 * ClassName: DefaultObjectWrapperFactory
 * Package: cn.xuguowen.mybatis.reflection.wrapper
 * Description:
 *
 * @Author 徐国文
 * @Create 2024/3/4 12:30
 * @Version 1.0
 */
public class DefaultObjectWrapperFactory implements ObjectWrapperFactory{
    @Override
    public boolean hasWrapperFor(Object object) {
        return false;
    }

    @Override
    public ObjectWrapper getWrapperFor(MetaObject metaObject, Object object) {
        throw new RuntimeException("The DefaultObjectWrapperFactory should never be called to provide an ObjectWrapper.");
    }
}
