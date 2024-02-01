package cn.xuguowen.binding;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * ClassName: MapperProxy
 * Package: cn.xuguowen.binding
 * Description:映射器对象
 *
 * @Author 徐国文
 * @Create 2024/2/1 13:14
 * @Version 1.0
 */
public class MapperProxy<T> implements InvocationHandler, Serializable {

    private static final long serialVersionUID = -6424540358554806844L;

    /**
     * 构造简单的sqlSession对象
     */
    private Map<String,String> sqlSession;

    /**
     * Dao层接口
     */
    private final Class<T> mapperInterface;


    public MapperProxy(Map<String, String> sqlSession, Class<T> mapperInterface) {
        this.sqlSession = sqlSession;
        this.mapperInterface = mapperInterface;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (Object.class.equals(method.getDeclaringClass())) {
            return method.invoke(this,args);
        } else {
            return "你被代理了！ " + sqlSession.get(mapperInterface.getName()+ "." + method.getName());
        }
    }
}
