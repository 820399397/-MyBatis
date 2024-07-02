package cn.xuguowen.mybatis.reflection.invoker;

/**
 * ClassName: Invoker
 * Package: cn.xuguowen.mybatis.reflection.invoker
 * Description:用于在目标对象上调用方法的接口:
 * 关于对象类中的属性值获取和设置可以分为 Field 字段的 get/set 还有普通的 Method 的调用，
 * 为了减少使用方的过多的处理，这里可以把集中调用者的实现包装成调用策略，统一接口不同策略不同的实现类
 *
 * @Author 徐国文
 * @Create 2024/3/4 9:42
 * @Version 1.0
 */
public interface Invoker {

    /**
     * 在给定的目标对象上使用指定的参数调用方法。
     *
     * @param target 要调用方法的目标对象
     * @param args   调用方法时传递的参数
     * @return 方法调用的结果
     * @throws Exception 如果在方法调用过程中发生错误
     */
    Object invoke(Object target, Object[] args) throws Exception;

    /**
     * 获取此 Invoker 设计处理的目标对象的类型。
     *
     * @return 表示目标对象类型的 Class
     */
    Class<?> getType();
}
