package cn.xuguowen.binding;

import java.lang.reflect.Proxy;
import java.util.Map;

/**
 * ClassName: MapperProxyFactory
 * Package: cn.xuguowen.binding
 * Description:映射器工厂
 *
 * @Author 徐国文
 * @Create 2024/2/1 13:13
 * @Version 1.0
 */
public class MapperProxyFactory<T> {

    private final Class<T> mapperInterface;

    public MapperProxyFactory(Class<T> mapperInterface) {
        this.mapperInterface = mapperInterface;
    }

    public T newInstance(Map<String,String> sqlSession) {

        MapperProxy<T> mapperProxy = new MapperProxy<>(sqlSession,mapperInterface);
        return (T) Proxy.newProxyInstance(mapperInterface.getClassLoader(), new Class[]{mapperInterface}, mapperProxy);
    }
}
