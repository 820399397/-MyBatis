package cn.xuguowen.mybatis.reflection.wrapper;

import cn.xuguowen.mybatis.reflection.MetaObject;

/**
 * ClassName: ObjectWrapperFactory
 * Package: cn.xuguowen.mybatis.reflection.wrapper
 * Description:对象包装工厂接口，用于创建和管理 ObjectWrapper 对象。
 *
 * @Author 徐国文
 * @Create 2024/3/4 12:14
 * @Version 1.0
 */
public interface ObjectWrapperFactory {
    /**
     * 判断是否有对应的 ObjectWrapper 包装器。
     *
     * @param object 要判断的对象
     * @return 如果有对应的 ObjectWrapper 包装器返回 true，否则返回 false
     */
    boolean hasWrapperFor(Object object);

    /**
     * 获取指定对象的 ObjectWrapper 包装器。
     *
     * @param metaObject 原始的 MetaObject 对象
     * @param object 要包装的对象
     * @return 返回对象的 ObjectWrapper 包装器
     */
    ObjectWrapper getWrapperFor(MetaObject metaObject, Object object);
}
