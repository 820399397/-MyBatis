package cn.xuguowen.mybatis.reflection.invoker;

import cn.xuguowen.mybatis.pojo.User;

import java.lang.reflect.Field;

/**
 * ClassName: GetFieldInvoker
 * Package: cn.xuguowen.mybatis.reflection.invoker
 * Description:使用反射获取字段值的具体实现类
 *
 * @Author 徐国文
 * @Create 2024/3/4 11:59
 * @Version 1.0
 */
public class GetFieldInvoker implements Invoker{

    // 要操作的字段对象
    private Field field;

    /**
     * 使用指定的字段构造一个 GetFieldInvoker。
     *
     * @param field 表示要操作的字段的 Field 对象
     */
    public GetFieldInvoker(Field field) {
        this.field = field;
    }

    /**
     * 在给定的目标对象上获取字段的值。
     *
     * @param target 要获取字段值的目标对象
     * @param args   不使用此参数，但需要传递空数组以符合接口要求
     * @return 字段的值
     * @throws Exception 如果在获取字段值过程中发生错误
     */
    @Override
    public Object invoke(Object target, Object[] args) throws Exception {
        return field.get(target);
    }

    /**
     * 获取此 Invoker 处理的字段的类型。
     *
     * @return 表示字段类型的 Class 对象
     */
    @Override
    public Class<?> getType() {
        return field.getType();
    }

    public static void main(String[] args) throws Exception {
        // 创建 User 类的实例
        User user = new User();
        user.setUserName("张三");

        // 使用反射获取 userName 字段
        Field userNameField = User.class.getDeclaredField("userName");
        // 由于 userName 是私有字段，需要设置可访问性
        userNameField.setAccessible(true);
        // 创建 GetFieldInvoker 实例
        GetFieldInvoker getUserNameInvoker = new GetFieldInvoker(userNameField);

        // 调用 invoke 方法获取 userName 字段的值
        Object result = getUserNameInvoker.invoke(user, new Object[]{});
        System.out.println("UserName: " + result); // 输出: UserName: Alice
    }
}
