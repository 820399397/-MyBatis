package cn.xuguowen.mybatis.reflection;

import cn.xuguowen.mybatis.pojo.User;
import cn.xuguowen.mybatis.reflection.invoker.GetFieldInvoker;
import cn.xuguowen.mybatis.reflection.invoker.Invoker;
import cn.xuguowen.mybatis.reflection.invoker.MethodInvoker;
import cn.xuguowen.mybatis.reflection.invoker.SetFieldInvoker;
import cn.xuguowen.mybatis.reflection.property.PropertyNamer;

import java.lang.reflect.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ClassName: Reflector
 * Package: cn.xuguowen.mybatis.reflection
 * Description:通过反射获取类的元数据（构造函数、getter、setter等）并缓存，提高反射操作的效率
 *
 * @Author 徐国文
 * @Create 2024/3/4 12:02
 * @Version 1.0
 */
public class Reflector {

    // 是否启用类缓存
    private static boolean classCacheEnabled = true;

    // 空字符串数组常量
    private static final String[] EMPTY_STRING_ARRAY = new String[0];

    // 线程安全的类反射信息缓存
    private static final Map<Class<?>, Reflector> REFLECTOR_MAP = new ConcurrentHashMap<>();

    // 被反射的类
    private Class<?> type;
    // get 属性列表: 可读属性名称列表
    private String[] readablePropertyNames = EMPTY_STRING_ARRAY;
    // set 属性列表: 可写属性名称列表
    private String[] writeablePropertyNames = EMPTY_STRING_ARRAY;

    // set 方法列表：属性对应的 set 方法映射
    private Map<String, Invoker> setMethods = new HashMap<>();

    // get 方法列表：属性对应的 get 方法映射
    private Map<String, Invoker> getMethods = new HashMap<>();

    // set 类型列表：属性对应的 set 方法参数类型映射
    private Map<String, Class<?>> setTypes = new HashMap<>();

    // get 类型列表：属性对应的 get 方法返回类型映射
    private Map<String, Class<?>> getTypes = new HashMap<>();

    // 默认构造函数
    private Constructor<?> defaultConstructor;

    // 大小写不敏感的属性名称映射
    private Map<String, String> caseInsensitivePropertyMap = new HashMap<>();

    /**
     * 构造函数，通过反射获取类的元数据
     *
     * @param clazz 要反射的类
     */
    public Reflector(Class<?> clazz) {
        this.type = clazz;
        // 加入构造函数
        addDefaultConstructor(clazz);
        // 加入 getter
        addGetMethods(clazz);
        // 加入 setter
        addSetMethods(clazz);
        // 加入字段
        addFields(clazz);
        readablePropertyNames = getMethods.keySet().toArray(new String[getMethods.keySet().size()]);
        writeablePropertyNames = setMethods.keySet().toArray(new String[setMethods.keySet().size()]);
        for (String propName : readablePropertyNames) {
            caseInsensitivePropertyMap.put(propName.toUpperCase(Locale.ENGLISH), propName);
        }
        for (String propName : writeablePropertyNames) {
            caseInsensitivePropertyMap.put(propName.toUpperCase(Locale.ENGLISH), propName);
        }
    }

    /**
     * 添加默认构造函数
     *
     * @param clazz 要反射的类
     */
    private void addDefaultConstructor(Class<?> clazz) {
        Constructor<?>[] consts = clazz.getDeclaredConstructors();
        for (Constructor<?> constructor : consts) {
            if (constructor.getParameterTypes().length == 0) {
                if (canAccessPrivateMethods()) {
                    try {
                        constructor.setAccessible(true);
                    } catch (Exception ignore) {
                        // Ignored. This is only a final precaution, nothing we can do
                    }
                }
                if (constructor.isAccessible()) {
                    this.defaultConstructor = constructor;
                }
            }
        }
    }

    /**
     * 添加 getter 方法
     *
     * @param clazz 要反射的类
     */
    private void addGetMethods(Class<?> clazz) {
        Map<String, List<Method>> conflictingGetters = new HashMap<>();
        Method[] methods = getClassMethods(clazz);
        for (Method method : methods) {
            String name = method.getName();
            if (name.startsWith("get") && name.length() > 3) {
                if (method.getParameterTypes().length == 0) {
                    name = PropertyNamer.methodToProperty(name);
                    addMethodConflict(conflictingGetters, name, method);
                }
            } else if (name.startsWith("is") && name.length() > 2) {
                if (method.getParameterTypes().length == 0) {
                    name = PropertyNamer.methodToProperty(name);
                    addMethodConflict(conflictingGetters, name, method);
                }
            }
        }
        resolveGetterConflicts(conflictingGetters);
    }

    /**
     * 添加 setter 方法
     *
     * @param clazz 要反射的类
     */
    private void addSetMethods(Class<?> clazz) {
        Map<String, List<Method>> conflictingSetters = new HashMap<>();
        Method[] methods = getClassMethods(clazz);
        for (Method method : methods) {
            String name = method.getName();
            if (name.startsWith("set") && name.length() > 3) {
                if (method.getParameterTypes().length == 1) {
                    name = PropertyNamer.methodToProperty(name);
                    addMethodConflict(conflictingSetters, name, method);
                }
            }
        }
        resolveSetterConflicts(conflictingSetters);
    }

    /**
     * 解决 setter 方法冲突
     *
     * @param conflictingSetters 冲突的 setter 方法映射
     */
    private void resolveSetterConflicts(Map<String, List<Method>> conflictingSetters) {
        for (String propName : conflictingSetters.keySet()) {
            List<Method> setters = conflictingSetters.get(propName);
            Method firstMethod = setters.get(0);
            if (setters.size() == 1) {
                addSetMethod(propName, firstMethod);
            } else {
                Class<?> expectedType = getTypes.get(propName);
                if (expectedType == null) {
                    throw new RuntimeException("Illegal overloaded setter method with ambiguous type for property "
                            + propName + " in class " + firstMethod.getDeclaringClass() + ".  This breaks the JavaBeans " +
                            "specification and can cause unpredicatble results.");
                } else {
                    Iterator<Method> methods = setters.iterator();
                    Method setter = null;
                    while (methods.hasNext()) {
                        Method method = methods.next();
                        if (method.getParameterTypes().length == 1
                                && expectedType.equals(method.getParameterTypes()[0])) {
                            setter = method;
                            break;
                        }
                    }
                    if (setter == null) {
                        throw new RuntimeException("Illegal overloaded setter method with ambiguous type for property "
                                + propName + " in class " + firstMethod.getDeclaringClass() + ".  This breaks the JavaBeans " +
                                "specification and can cause unpredicatble results.");
                    }
                    addSetMethod(propName, setter);
                }
            }
        }
    }

    /**
     * 添加 setter 方法
     *
     * @param name   属性名称
     * @param method setter 方法
     */
    private void addSetMethod(String name, Method method) {
        if (isValidPropertyName(name)) {
            setMethods.put(name, new MethodInvoker(method));
            setTypes.put(name, method.getParameterTypes()[0]);
        }
    }

    /**
     * 添加字段
     *
     * @param clazz 要反射的类
     */
    private void addFields(Class<?> clazz) {
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (canAccessPrivateMethods()) {
                try {
                    field.setAccessible(true);
                } catch (Exception e) {
                    // Ignored. This is only a final precaution, nothing we can do.
                }
            }
            if (field.isAccessible()) {
                if (!setMethods.containsKey(field.getName())) {
                    // issue #379 - removed the check for final because JDK 1.5 allows
                    // modification of final fields through reflection (JSR-133). (JGB)
                    // pr #16 - final static can only be set by the classloader
                    int modifiers = field.getModifiers();
                    if (!(Modifier.isFinal(modifiers) && Modifier.isStatic(modifiers))) {
                        addSetField(field);
                    }
                }
                if (!getMethods.containsKey(field.getName())) {
                    addGetField(field);
                }
            }
        }
        if (clazz.getSuperclass() != null) {
            addFields(clazz.getSuperclass());
        }
    }

    /**
     * 添加 setter 字段
     *
     * @param field 字段
     */
    private void addSetField(Field field) {
        if (isValidPropertyName(field.getName())) {
            setMethods.put(field.getName(), new SetFieldInvoker(field));
            setTypes.put(field.getName(), field.getType());
        }
    }

    /**
     * 添加 getter 字段
     *
     * @param field 字段
     */
    private void addGetField(Field field) {
        if (isValidPropertyName(field.getName())) {
            getMethods.put(field.getName(), new GetFieldInvoker(field));
            getTypes.put(field.getName(), field.getType());
        }
    }

    /**
     * 解决 getter 方法冲突
     *
     * @param conflictingGetters 冲突的 getter 方法映射
     */
    private void resolveGetterConflicts(Map<String, List<Method>> conflictingGetters) {
        for (String propName : conflictingGetters.keySet()) {
            List<Method> getters = conflictingGetters.get(propName);
            Iterator<Method> iterator = getters.iterator();
            Method firstMethod = iterator.next();
            if (getters.size() == 1) {
                addGetMethod(propName, firstMethod);
            } else {
                Method getter = firstMethod;
                Class<?> getterType = firstMethod.getReturnType();
                while (iterator.hasNext()) {
                    Method method = iterator.next();
                    Class<?> methodType = method.getReturnType();
                    if (methodType.equals(getterType)) {
                        throw new RuntimeException("Illegal overloaded getter method with ambiguous type for property "
                                + propName + " in class " + firstMethod.getDeclaringClass()
                                + ".  This breaks the JavaBeans " + "specification and can cause unpredicatble results.");
                    } else if (methodType.isAssignableFrom(getterType)) {
                        // OK getter type is descendant
                    } else if (getterType.isAssignableFrom(methodType)) {
                        getter = method;
                        getterType = methodType;
                    } else {
                        throw new RuntimeException("Illegal overloaded getter method with ambiguous type for property "
                                + propName + " in class " + firstMethod.getDeclaringClass()
                                + ".  This breaks the JavaBeans " + "specification and can cause unpredicatble results.");
                    }
                }
                addGetMethod(propName, getter);
            }
        }
    }

    /**
     * 添加 getter 方法
     *
     * @param name   属性名称
     * @param method getter 方法
     */
    private void addGetMethod(String name, Method method) {
        if (isValidPropertyName(name)) {
            getMethods.put(name, new MethodInvoker(method));
            getTypes.put(name, method.getReturnType());
        }
    }

    /**
     * 验证属性名称是否合法
     *
     * @param name 属性名称
     * @return 是否合法
     */
    private boolean isValidPropertyName(String name) {
        return !(name.startsWith("$") || "serialVersionUID".equals(name) || "class".equals(name));
    }

    /**
     * 添加方法冲突
     *
     * @param conflictingMethods 冲突的方法映射
     * @param name               属性名称
     * @param method             方法
     */
    private void addMethodConflict(Map<String, List<Method>> conflictingMethods, String name, Method method) {
        List<Method> list = conflictingMethods.computeIfAbsent(name, k -> new ArrayList<>());
        list.add(method);
    }

    /**
     * 获取类的方法，包括其父类和接口的方法
     *
     * @param cls 要反射的类
     * @return 方法数组
     */
    private Method[] getClassMethods(Class<?> cls) {
        Map<String, Method> uniqueMethods = new HashMap<String, Method>();
        Class<?> currentClass = cls;
        while (currentClass != null) {
            addUniqueMethods(uniqueMethods, currentClass.getDeclaredMethods());

            // we also need to look for interface methods -
            // because the class may be abstract
            Class<?>[] interfaces = currentClass.getInterfaces();
            for (Class<?> anInterface : interfaces) {
                addUniqueMethods(uniqueMethods, anInterface.getMethods());
            }

            currentClass = currentClass.getSuperclass();
        }

        Collection<Method> methods = uniqueMethods.values();

        return methods.toArray(new Method[methods.size()]);
    }

    /**
     * 添加唯一的方法
     *
     * @param uniqueMethods 唯一方法映射
     * @param methods       方法数组
     */
    private void addUniqueMethods(Map<String, Method> uniqueMethods, Method[] methods) {
        for (Method currentMethod : methods) {
            if (!currentMethod.isBridge()) {
                //取得签名
                String signature = getSignature(currentMethod);
                // check to see if the method is already known
                // if it is known, then an extended class must have
                // overridden a method
                if (!uniqueMethods.containsKey(signature)) {
                    if (canAccessPrivateMethods()) {
                        try {
                            currentMethod.setAccessible(true);
                        } catch (Exception e) {
                            // Ignored. This is only a final precaution, nothing we can do.
                        }
                    }

                    uniqueMethods.put(signature, currentMethod);
                }
            }
        }
    }

    /**
     * 获取方法签名
     *
     * @param method 方法
     * @return 签名字符串
     */
    private String getSignature(Method method) {
        StringBuilder sb = new StringBuilder();
        Class<?> returnType = method.getReturnType();
        if (returnType != null) {
            sb.append(returnType.getName()).append('#');
        }
        sb.append(method.getName());
        Class<?>[] parameters = method.getParameterTypes();
        for (int i = 0; i < parameters.length; i++) {
            if (i == 0) {
                sb.append(':');
            } else {
                sb.append(',');
            }
            sb.append(parameters[i].getName());
        }
        return sb.toString();
    }

    /**
     * 检查是否可以访问私有方法
     *
     * @return 是否可以访问
     */
    private static boolean canAccessPrivateMethods() {
        try {
            SecurityManager securityManager = System.getSecurityManager();
            if (null != securityManager) {
                securityManager.checkPermission(new ReflectPermission("suppressAccessChecks"));
            }
        } catch (SecurityException e) {
            return false;
        }
        return true;
    }


    /**
     * 获取被反射的类
     *
     * @return 被反射的类
     */
    public Class<?> getType() {
        return type;
    }

    /**
     * 获取默认构造函数
     *
     * @return 默认构造函数
     */
    public Constructor<?> getDefaultConstructor() {
        if (defaultConstructor != null) {
            return defaultConstructor;
        } else {
            throw new RuntimeException("There is no default constructor for " + type);
        }
    }

    /**
     * 是否有默认构造函数
     *
     * @return 是否有默认构造函数
     */
    public boolean hasDefaultConstructor() {
        return defaultConstructor != null;
    }

    /**
     * 获取属性的 setter 方法类型
     *
     * @param propertyName 属性名称
     * @return setter 方法类型
     */
    public Class<?> getSetterType(String propertyName) {
        Class<?> clazz = setTypes.get(propertyName);
        if (clazz == null) {
            throw new RuntimeException("There is no setter for property named '" + propertyName + "' in '" + type + "'");
        }
        return clazz;
    }

    /**
     * 获取属性的 getter 方法调用器
     *
     * @param propertyName 属性名称
     * @return getter 方法调用器
     */
    public Invoker getGetInvoker(String propertyName) {
        Invoker method = getMethods.get(propertyName);
        if (method == null) {
            throw new RuntimeException("There is no getter for property named '" + propertyName + "' in '" + type + "'");
        }
        return method;
    }

    /**
     * 获取属性的 setter 方法调用器
     *
     * @param propertyName 属性名称
     * @return setter 方法调用器
     */
    public Invoker getSetInvoker(String propertyName) {
        Invoker method = setMethods.get(propertyName);
        if (method == null) {
            throw new RuntimeException("There is no setter for property named '" + propertyName + "' in '" + type + "'");
        }
        return method;
    }

    /**
     * 获取属性的 getter 方法类型
     *
     * @param propertyName 属性名称
     * @return getter 方法类型
     */
    public Class<?> getGetterType(String propertyName) {
        Class<?> clazz = getTypes.get(propertyName);
        if (clazz == null) {
            throw new RuntimeException("There is no getter for property named '" + propertyName + "' in '" + type + "'");
        }
        return clazz;
    }

    /**
     * 获取可读属性名称数组
     *
     * @return 可读属性名称数组
     */
    public String[] getGetablePropertyNames() {
        return readablePropertyNames;
    }

    /**
     * 获取可写属性名称数组
     *
     * @return 可写属性名称数组
     */
    public String[] getSetablePropertyNames() {
        return writeablePropertyNames;
    }

    /**
     * 检查类是否具有指定名称的可写属性
     *
     * @param propertyName 属性名称
     * @return 是否具有该可写属性
     */
    public boolean hasSetter(String propertyName) {
        return setMethods.keySet().contains(propertyName);
    }

    /**
     * 检查类是否具有指定名称的可读属性
     *
     * @param propertyName 属性名称
     * @return 是否具有该可读属性
     */
    public boolean hasGetter(String propertyName) {
        return getMethods.keySet().contains(propertyName);
    }

    /**
     * 查找属性名（忽略大小写）
     *
     * @param name 属性名称
     * @return 属性名称
     */
    public String findPropertyName(String name) {
        return caseInsensitivePropertyMap.get(name.toUpperCase(Locale.ENGLISH));
    }

    /**
     * 获取类的反射器，如果启用缓存则从缓存中获取
     *
     * @param clazz 要反射的类
     * @return 反射器
     */
    public static Reflector forClass(Class<?> clazz) {
        if (classCacheEnabled) {
            // synchronized (clazz) removed see issue #461
            // 对于每个类来说，我们假设它是不会变的，这样可以考虑将这个类的信息(构造函数，getter,setter,字段)加入缓存，以提高速度
            Reflector cached = REFLECTOR_MAP.get(clazz);
            if (cached == null) {
                cached = new Reflector(clazz);
                REFLECTOR_MAP.put(clazz, cached);
            }
            return cached;
        } else {
            return new Reflector(clazz);
        }
    }

    /**
     * 设置是否启用类缓存
     *
     * @param classCacheEnabled 是否启用类缓存
     */
    public static void setClassCacheEnabled(boolean classCacheEnabled) {
        Reflector.classCacheEnabled = classCacheEnabled;
    }

    /**
     * 是否启用类缓存
     *
     * @return 是否启用类缓存
     */
    public static boolean isClassCacheEnabled() {
        return classCacheEnabled;
    }

    public static void main(String[] args) throws Exception {
        // 获取 Person 类的 Reflector
        Reflector reflector = Reflector.forClass(User.class);

        // 创建 Person 类的实例
        Constructor<?> constructor = reflector.getDefaultConstructor();
        User user = (User) constructor.newInstance();

        // 设置属性值
        Invoker setNameInvoker = reflector.getSetInvoker("userName");
        setNameInvoker.invoke(user, new Object[]{"John"});

        Invoker setUserIdInvoker = reflector.getSetInvoker("userId");
        setUserIdInvoker.invoke(user, new Object[]{"25"});

        // 获取属性值
        Invoker getNameInvoker = reflector.getGetInvoker("userName");
        String name = (String) getNameInvoker.invoke(user, new Object[]{});

        Invoker getUserIdInvoker = reflector.getGetInvoker("userId");
        String userId = (String) getUserIdInvoker.invoke(user, new Object[]{});

        // 打印结果
        System.out.println("name: " + name);
        System.out.println("userId: " + userId);

    }
}
