package cn.xuguowen.mybatis.reflection.invoker;

import cn.xuguowen.mybatis.pojo.User;

import java.lang.reflect.Field;

/**
 * ClassName: SetFieldInvoker
 * Package: cn.xuguowen.mybatis.reflection.invoker
 * Description:使用反射设置字段值的具体实现类
 *
 * @Author 徐国文
 * @Create 2024/3/4 12:01
 * @Version 1.0
 */
public class SetFieldInvoker implements Invoker{

    // 要操作的字段对象
    private Field field;

    /**
     * 使用指定的字段构造一个 SetFieldInvoker。
     *
     * @param field 表示要操作的字段的 Field 对象
     */
    public SetFieldInvoker(Field field) {
        this.field = field;
    }

    /**
     * 在给定的目标对象上设置字段的值。
     *
     * @param target 要设置字段值的目标对象
     * @param args   设置字段值时传递的参数数组，其中 args[0] 是要设置的值
     * @return null 因为设置字段值不返回任何结果
     * @throws Exception 如果在设置字段值过程中发生错误
     */
    @Override
    public Object invoke(Object target, Object[] args) throws Exception {
        field.set(target, args[0]);
        return null;
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
        user.setUserName("Alice");

        System.out.println("Original UserName: " + user.getUserName());

        // 使用反射获取 userName 字段
        Field userNameField = User.class.getDeclaredField("userName");
        // 由于 userName 是私有字段，需要设置可访问性
        userNameField.setAccessible(true);

        // 创建 SetFieldInvoker 实例
        SetFieldInvoker setUserNameInvoker = new SetFieldInvoker(userNameField);

        // 调用 invoke 方法设置 userName 字段的值
        setUserNameInvoker.invoke(user, new Object[]{"Bob"});

        // 验证设置是否成功
        System.out.println("Updated UserName: " + user.getUserName()); // 输出: Updated UserName: Bob
    }
}
