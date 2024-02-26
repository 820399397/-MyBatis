package cn.xuguowen.mybatis.binding;

import cn.hutool.core.lang.ClassScanner;
import cn.xuguowen.mybatis.session.SqlSession;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * ClassName: MapperRegistry
 * Package: cn.xuguowen.mybatis.binding
 * Description:映射器注册机：扫描指定包下的类，完成接口对象的代理类注册。
 *
 * @Author 徐国文
 * @Create 2024/2/9 15:37
 * @Version 1.0
 */
public class MapperRegistry {

    // 用于保存Mapper接口和对应的MapperProxyFactory的映射关系
    private final Map<Class<?>,MapperProxyFactory<?>> knownMappers = new HashMap<>();

    /**
     * 获取mapper映射器
     * @param type mapper接口类型
     * @param sqlSession
     * @param <T>
     * @return
     */
    public <T> T getMapper(Class<T> type, SqlSession sqlSession){
        // 根据类型到knownMappers找到映射器代理类工厂
        final MapperProxyFactory<T> mapperProxyFactory = (MapperProxyFactory<T>) knownMappers.get(type);
        // 如果没有找到，抛出异常
        if (Objects.isNull(mapperProxyFactory)) {
            throw new RuntimeException("Type" + type + " is not known to the MapperRegistry.");
        }

        try {
            return mapperProxyFactory.newInstance(sqlSession);
        } catch (Exception e) {
            throw new RuntimeException("Error getting mapper instance. Cause: "+ e, e);
        }
    }

    /**
     * 添加mapper
     * @param type
     * @param <T>
     */
    public <T> void addMapper(Class<T> type){
        // 判断接口是否为映射器接口
        if (type.isInterface()){
            // 判断是否已经注册过
            if (hasMapper(type)){
                throw new RuntimeException("Type " + type + " is already known to the MapperRegistry.");
            }
            // 注册映射器接口
            knownMappers.put(type, new MapperProxyFactory<>(type));
        }
    }

    /**
     * 判断knownMappers中是否包含type的mapper
     * @param type
     * @param <T>
     * @return
     */
    public <T> boolean hasMapper(Class<T> type) {
        return knownMappers.containsKey(type);
    }

    /**
     * 批量添加mapper
     * @param packageName
     */
    public void addMappers(String packageName){
        Set<Class<?>> mapperSet = ClassScanner.scanPackage(packageName);
        for (Class<?> mapperClass : mapperSet) {
            addMapper(mapperClass);
        }
    }
}
