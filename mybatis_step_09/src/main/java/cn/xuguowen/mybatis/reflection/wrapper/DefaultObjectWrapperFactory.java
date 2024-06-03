package cn.xuguowen.mybatis.reflection.wrapper;

import cn.xuguowen.mybatis.reflection.MetaObject;

/**
 * ClassName: DefaultObjectWrapperFactory
 * Package: cn.xuguowen.mybatis.reflection.wrapper
 * Description: 默认的 ObjectWrapperFactory 实现，不提供任何对象包装功能。
 *
 * @Author 徐国文
 * @Create 2024/3/4 12:30
 * @Version 1.0
 */
public class DefaultObjectWrapperFactory implements ObjectWrapperFactory{

    /**
     * 判断给定对象是否有对应的包装器。
     *
     * @param object 要检查的对象
     * @return 总是返回 false，表示没有对应的包装器
     */
    @Override
    public boolean hasWrapperFor(Object object) {
        return false;
    }

    /**
     * 获取给定对象的包装器。
     *
     * @param metaObject MetaObject 对象
     * @param object 要包装的对象
     * @return 没有实现此功能，始终抛出运行时异常
     * @throws RuntimeException 总是抛出异常，提示不应调用该方法
     */
    @Override
    public ObjectWrapper getWrapperFor(MetaObject metaObject, Object object) {
        throw new RuntimeException("The DefaultObjectWrapperFactory should never be called to provide an ObjectWrapper.");
    }
}
