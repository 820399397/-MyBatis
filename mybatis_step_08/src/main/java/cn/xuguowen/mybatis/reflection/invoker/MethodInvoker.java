package cn.xuguowen.mybatis.reflection.invoker;

import cn.xuguowen.mybatis.pojo.User;

import java.lang.reflect.Method;

/**
 * ClassName: MethodInvoker
 * Package: cn.xuguowen.mybatis.reflection.invoker
 * Description:使用反射调用方法的具体实现类
 *
 * @Author 徐国文
 * @Create 2024/3/4 11:55
 * @Version 1.0
 */
public class MethodInvoker implements Invoker {

    // 方法的参数类型或返回类型
    private Class<?> type;

    // 要调用的方法对象
    private Method method;

    /**
     * 使用指定的方法构造一个 MethodInvoker。
     *
     * @param method 表示要调用的方法的 Method 对象
     */
    public MethodInvoker(Method method) {
        this.method = method;

        // 如果只有一个参数，返回参数类型，否则返回 return 类型
        if (method.getParameterTypes().length == 1) {
            this.type = method.getParameterTypes()[0];
        } else {
            this.type = method.getReturnType();
        }
    }

    /**
     * 使用提供的参数在给定的目标对象上调用指定的方法。
     *
     * @param target 要调用方法的目标对象
     * @param args   调用方法时传递的参数
     * @return 方法调用的结果
     * @throws Exception 如果在方法调用过程中发生错误
     */
    @Override
    public Object invoke(Object target, Object[] args) throws Exception {
        return method.invoke(target,args);
    }

    /**
     * 获取此 Invoker 处理的方法的参数类型或返回类型。
     *
     * @return 表示方法参数类型或返回类型的 Class 对象
     */
    @Override
    public Class<?> getType() {
        return type;
    }

    public static void main(String[] args) throws Exception {
        User user = new User();

        // 获取user对象中的setUserName()方法
        Method setUserNameMethod = user.getClass().getMethod("setUserName", String.class);

        // 创建 MethodInvoker 实例
        MethodInvoker setUserNameInvoker = new MethodInvoker(setUserNameMethod);

        // 调用 setUserName 方法，传递参数 "Alice"
        setUserNameInvoker.invoke(user, new Object[]{"Alice"});

        // 使用反射获取 getUserName 方法
        Method getUserNameMethod = User.class.getMethod("getUserName");
        // 创建 MethodInvoker 实例
        MethodInvoker getUserNameInvoker = new MethodInvoker(getUserNameMethod);
        // 调用 getUserName 方法
        Object result = getUserNameInvoker.invoke(user, new Object[]{});
        System.out.println("UserName: " + result); // 输出: UserName: Alice

    }
}
