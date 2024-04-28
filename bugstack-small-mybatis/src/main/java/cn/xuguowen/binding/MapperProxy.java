package cn.xuguowen.binding;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * ClassName: MapperProxy
 * Package: cn.xuguowen.binding
 * Description:映射器代理处理类
 * 1.记录疑问：基于JDK实现的动态代理中，类A实现了InvocationHandler接口，并重写了invoke方法，那这个类A可以被称之为代理对嘛？
 *      不完全正确。在基于 JDK 实现的动态代理中，类 A 实现了 InvocationHandler 接口，并重写了 invoke 方法。
 *      这样的类 A 称为代理处理器（Proxy Handler），而不是代理对象。
 *      实际上，代理对象是通过 Java 标准库中的 Proxy 类的静态方法 newProxyInstance() 来创建的。该方法接受一个类加载器、一组接口以及一个 InvocationHandler 对象，并返回一个代理对象。
 *      在这个过程中，类加载器会动态创建一个代理类，该代理类实现了指定的接口，并在调用方法时将调用转发给传入的 InvocationHandler 对象的 invoke 方法。
 *      因此，在基于 JDK 实现的动态代理中，代理对象是由 Proxy 类动态生成的，而不是实现了 InvocationHandler 接口的类。实现 InvocationHandler 接口的类才是代理处理器，它负责实际的代理逻辑。
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
