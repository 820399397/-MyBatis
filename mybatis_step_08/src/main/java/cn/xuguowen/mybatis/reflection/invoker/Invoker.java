package cn.xuguowen.mybatis.reflection.invoker;

/**
 * ClassName: Invoker
 * Package: cn.xuguowen.mybatis.reflection.invoker
 * Description:
 *
 * @Author 徐国文
 * @Create 2024/3/4 9:42
 * @Version 1.0
 */
public interface Invoker {

    Object invoke(Object target, Object[] args) throws Exception;

    Class<?> getType();
}
