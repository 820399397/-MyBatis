package cn.xuguowen.mybatis.reflection.invoker;

import java.lang.reflect.Method;

/**
 * ClassName: MethodInvoker
 * Package: cn.xuguowen.mybatis.reflection.invoker
 * Description:
 *
 * @Author 徐国文
 * @Create 2024/3/4 11:55
 * @Version 1.0
 */
public class MethodInvoker implements Invoker {

    private Class<?> type;

    private Method method;

    public MethodInvoker(Method method) {
        this.method = method;

        // 如果只有一个参数，返回参数类型，否则返回 return 类型
        if (method.getParameterTypes().length == 1) {
            this.type = method.getParameterTypes()[0];
        } else {
            this.type = method.getReturnType();
        }
    }
    @Override
    public Object invoke(Object target, Object[] args) throws Exception {
        return method.invoke(target,args);
    }

    @Override
    public Class<?> getType() {
        return type;
    }
}
