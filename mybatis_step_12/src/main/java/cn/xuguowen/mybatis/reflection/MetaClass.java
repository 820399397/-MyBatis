package cn.xuguowen.mybatis.reflection;

import cn.xuguowen.mybatis.pojo.User;
import cn.xuguowen.mybatis.reflection.invoker.GetFieldInvoker;
import cn.xuguowen.mybatis.reflection.invoker.Invoker;
import cn.xuguowen.mybatis.reflection.invoker.MethodInvoker;
import cn.xuguowen.mybatis.reflection.property.PropertyTokenizer;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;

/**
 * ClassName: MetaClass
 * Package: cn.xuguowen.mybatis.reflection
 * Description:一个用于反射操作的工具类，主要用于类的内省和操作
 *
 * @Author 徐国文
 * @Create 2024/3/4 12:09
 * @Version 1.0
 */
public class MetaClass {

    // 持有 Reflector 对象，用于反射处理
    private Reflector reflector;

    // 私有构造函数，通过 forClass 方法创建Reflector实例
    private MetaClass(Class<?> type) {
        this.reflector = Reflector.forClass(type);
    }

    // 创建 MetaClass 的静态工厂方法
    public static MetaClass forClass(Class<?> type) {
        return new MetaClass(type);
    }

    // 获取类缓存是否启用
    public static boolean isClassCacheEnabled() {
        return Reflector.isClassCacheEnabled();
    }

    // 设置类缓存是否启用
    public static void setClassCacheEnabled(boolean classCacheEnabled) {
        Reflector.setClassCacheEnabled(classCacheEnabled);
    }

    // 获取属性的 MetaClass
    public MetaClass metaClassForProperty(String name) {
        Class<?> propType = reflector.getGetterType(name);
        return MetaClass.forClass(propType);
    }

    // 查找属性
    public String findProperty(String name) {
        StringBuilder prop = buildProperty(name, new StringBuilder());
        return prop.length() > 0 ? prop.toString() : null;
    }

    // 查找属性，支持驼峰命名法
    public String findProperty(String name, boolean useCamelCaseMapping) {
        if (useCamelCaseMapping) {
            name = name.replace("_", "");
        }
        return findProperty(name);
    }

    // 获取所有 getter 方法的名称
    public String[] getGetterNames() {
        return reflector.getGetablePropertyNames();
    }

    // 获取所有 setter 方法的名称
    public String[] getSetterNames() {
        return reflector.getSetablePropertyNames();
    }

    // 获取 setter 方法的参数类型
    public Class<?> getSetterType(String name) {
        PropertyTokenizer prop = new PropertyTokenizer(name);
        if (prop.hasNext()) {
            MetaClass metaProp = metaClassForProperty(prop.getName());
            return metaProp.getSetterType(prop.getChildren());
        } else {
            return reflector.getSetterType(prop.getName());
        }
    }

    // 获取 getter 方法的返回类型
    public Class<?> getGetterType(String name) {
        PropertyTokenizer prop = new PropertyTokenizer(name);
        if (prop.hasNext()) {
            MetaClass metaProp = metaClassForProperty(prop);
            return metaProp.getGetterType(prop.getChildren());
        }
        // issue #506. Resolve the type inside a Collection Object
        return getGetterType(prop);
    }

    // 获取属性的 MetaClass
    private MetaClass metaClassForProperty(PropertyTokenizer prop) {
        Class<?> propType = getGetterType(prop);
        return MetaClass.forClass(propType);
    }

    // 获取 getter 方法的返回类型
    private Class<?> getGetterType(PropertyTokenizer prop) {
        Class<?> type = reflector.getGetterType(prop.getName());
        if (prop.getIndex() != null && Collection.class.isAssignableFrom(type)) {
            Type returnType = getGenericGetterType(prop.getName());
            if (returnType instanceof ParameterizedType) {
                Type[] actualTypeArguments = ((ParameterizedType) returnType).getActualTypeArguments();
                if (actualTypeArguments != null && actualTypeArguments.length == 1) {
                    returnType = actualTypeArguments[0];
                    if (returnType instanceof Class) {
                        type = (Class<?>) returnType;
                    } else if (returnType instanceof ParameterizedType) {
                        type = (Class<?>) ((ParameterizedType) returnType).getRawType();
                    }
                }
            }
        }
        return type;
    }

    // 获取 getter 方法的泛型返回类型
    private Type getGenericGetterType(String propertyName) {
        try {
            Invoker invoker = reflector.getGetInvoker(propertyName);
            if (invoker instanceof MethodInvoker) {
                Field _method = MethodInvoker.class.getDeclaredField("method");
                _method.setAccessible(true);
                Method method = (Method) _method.get(invoker);
                return method.getGenericReturnType();
            } else if (invoker instanceof GetFieldInvoker) {
                Field _field = GetFieldInvoker.class.getDeclaredField("field");
                _field.setAccessible(true);
                Field field = (Field) _field.get(invoker);
                return field.getGenericType();
            }
        } catch (NoSuchFieldException | IllegalAccessException ignored) {
        }
        return null;
    }

    // 判断是否有 setter 方法
    public boolean hasSetter(String name) {
        PropertyTokenizer prop = new PropertyTokenizer(name);
        if (prop.hasNext()) {
            if (reflector.hasSetter(prop.getName())) {
                MetaClass metaProp = metaClassForProperty(prop.getName());
                return metaProp.hasSetter(prop.getChildren());
            } else {
                return false;
            }
        } else {
            return reflector.hasSetter(prop.getName());
        }
    }

    // 判断是否有 getter 方法
    public boolean hasGetter(String name) {
        PropertyTokenizer prop = new PropertyTokenizer(name);
        if (prop.hasNext()) {
            // 递归获取
            if (reflector.hasGetter(prop.getName())) {
                MetaClass metaProp = metaClassForProperty(prop);
                return metaProp.hasGetter(prop.getChildren());
            } else {
                return false;
            }
        } else {
            return reflector.hasGetter(prop.getName());
        }
    }

    // 获取 getter 方法的调用器
    public Invoker getGetInvoker(String name) {
        return reflector.getGetInvoker(name);
    }

    // 获取 setter 方法的调用器
    public Invoker getSetInvoker(String name) {
        return reflector.getSetInvoker(name);
    }

    // 构建属性名称
    private StringBuilder buildProperty(String name, StringBuilder builder) {
        PropertyTokenizer prop = new PropertyTokenizer(name);
        if (prop.hasNext()) {
            String propertyName = reflector.findPropertyName(prop.getName());
            if (propertyName != null) {
                builder.append(propertyName);
                builder.append(".");
                MetaClass metaProp = metaClassForProperty(propertyName);
                metaProp.buildProperty(prop.getChildren(), builder);
            }
        } else {
            String propertyName = reflector.findPropertyName(name);
            if (propertyName != null) {
                builder.append(propertyName);
            }
        }
        return builder;
    }

    // 判断是否有默认构造函数
    public boolean hasDefaultConstructor() {
        return reflector.hasDefaultConstructor();
    }

    public static void main(String[] args) {
        // 创建 MetaClass 实例
        MetaClass metaClass = MetaClass.forClass(User.class);

        // 获取所有 getter 方法的名称
        String[] getterNames = metaClass.getGetterNames();
        System.out.println("Getter 方法: ");
        for (String name : getterNames) {
            System.out.println(name);
        }

        // 获取所有 setter 方法的名称
        String[] setterNames = metaClass.getSetterNames();
        System.out.println("\nSetter 方法: ");
        for (String name : setterNames) {
            System.out.println(name);
        }

        // 查找属性
        String property = metaClass.findProperty("userName");
        System.out.println("\n查找到的属性: " + property);

        // 获取 getter 方法的返回类型
        Class<?> getterType = metaClass.getGetterType("userName");
        System.out.println("\n'getName' 方法的返回类型: " + getterType.getName());

        // 获取 setter 方法的参数类型
        Class<?> setterType = metaClass.getSetterType("userName");
        System.out.println("\n'setName' 方法的参数类型: " + setterType.getName());

        // 判断是否有特定的 getter 和 setter 方法
        boolean hasGetter = metaClass.hasGetter("userName");
        boolean hasSetter = metaClass.hasSetter("userName");
        System.out.println("\n是否有 'getName' 方法: " + hasGetter);
        System.out.println("是否有 'setName' 方法: " + hasSetter);

    }

}
