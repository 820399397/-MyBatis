package cn.xuguowen.mybatis.binding;

import cn.xuguowen.mybatis.session.SqlSession;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * ClassName: MapperProxy
 * Package: cn.xuguowen.mybatis.binding
 * Description:
 *
 * @Author 徐国文
 * @Create 2024/2/9 15:01
 * @Version 1.0
 */
public class MapperProxy<T> implements InvocationHandler, Serializable {

    public static final long serialVersionUID = -6424540358554805482L;

    private SqlSession sqlSession;

    private final Class<T> mapperInterface;

    // 缓存mapper方法
    private final Map<Method, MapperMethod> methodCache;


    public MapperProxy(SqlSession sqlSession, Class<T> mapperInterface,Map<Method, MapperMethod> methodCache) {
        this.mapperInterface = mapperInterface;
        this.sqlSession = sqlSession;
        this.methodCache = methodCache;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //如果是Object类中的方法，直接执行
        if(Object.class.equals(method.getDeclaringClass())){
            return method.invoke(this,args);
        } else {
            //否则执行代理方法，这里从方法缓存中获取mapper方法
            MapperMethod mapperMethod = cachedMapperMethod(method);
            return mapperMethod.execute(sqlSession,args);
        }
    }

    /**
     * 缓存mapper方法
     * @param method
     * @return
     */
    private MapperMethod cachedMapperMethod(Method method) {
        //先从缓存中获取，如果没有则创建一个
        MapperMethod mapperMethod = methodCache.get(method);
        if(mapperMethod == null){
            mapperMethod = new MapperMethod(mapperInterface,method,sqlSession.getConfiguration());
            methodCache.put(method,mapperMethod);
        }
        return mapperMethod;
    }
}
