package cn.xuguowen.mybatis.reflection.invoker;

import java.lang.reflect.Field;

/**
 * ClassName: GetFieldInvoker
 * Package: cn.xuguowen.mybatis.reflection.invoker
 * Description:
 *
 * @Author 徐国文
 * @Create 2024/3/4 11:59
 * @Version 1.0
 */
public class GetFieldInvoker implements Invoker{

    private Field field;

    public GetFieldInvoker(Field field) {
        this.field = field;
    }

    @Override
    public Object invoke(Object target, Object[] args) throws Exception {
        return field.get(target);
    }

    @Override
    public Class<?> getType() {
        return field.getType();
    }
}
