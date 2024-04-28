package cn.xuguowen.mybatis.reflection.invoker;

import java.lang.reflect.Field;

/**
 * ClassName: SetFieldInvoker
 * Package: cn.xuguowen.mybatis.reflection.invoker
 * Description:
 *
 * @Author 徐国文
 * @Create 2024/3/4 12:01
 * @Version 1.0
 */
public class SetFieldInvoker implements Invoker{

    private Field field;

    public SetFieldInvoker(Field field) {
        this.field = field;
    }

    @Override
    public Object invoke(Object target, Object[] args) throws Exception {
        field.set(target, args[0]);
        return null;
    }

    @Override
    public Class<?> getType() {
        return field.getType();
    }
}
