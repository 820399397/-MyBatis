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

    public MapperProxy(SqlSession sqlSession, Class<T> mapperInterface) {
        this.sqlSession = sqlSession;
        this.mapperInterface = mapperInterface;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (Object.class.equals(method.getDeclaringClass())) {
            return method.invoke(this, args);
        }

        return "你被代理了！" + sqlSession.selectOne(method.getName(),args);
    }
}
