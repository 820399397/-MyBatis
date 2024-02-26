package cn.xuguowen.mybatis.binding;

import cn.xuguowen.mybatis.session.SqlSession;

import java.lang.reflect.Proxy;

/**
 * ClassName: MapperProxyFactory
 * Package: cn.xuguowen.mybatis.binding
 * Description:简单工厂模式，用于创建MapperProxy的工厂类
 *
 * @Author 徐国文
 * @Create 2024/2/9 15:03
 * @Version 1.0
 */
public class MapperProxyFactory<T> {

    private final Class<T> mapperInterface;

    public MapperProxyFactory(Class<T> mapperInterface) {
        this.mapperInterface = mapperInterface;
    }

    public T newInstance(SqlSession sqlSession) {
        final MapperProxy<T> mapperProxy = new MapperProxy<>(sqlSession,mapperInterface);
        return (T) Proxy.newProxyInstance(mapperInterface.getClassLoader(),
                new Class[]{mapperInterface}, mapperProxy);
    }
}
