package cn.xuguowen.mybatis.binding;

import cn.xuguowen.mybatis.mapping.MappedStatement;
import cn.xuguowen.mybatis.mapping.SqlCommandType;
import cn.xuguowen.mybatis.session.Configuration;
import cn.xuguowen.mybatis.session.SqlSession;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * ClassName: MapperMethod
 * Package: cn.xuguowen.mybatis.binding
 * Description:
 *
 * @Author 徐国文
 * @Create 2024/2/15 22:17
 * @Version 1.0
 */
public class MapperMethod {

    private final SqlCommand command;

    private final MethodSignature method;

    /**
     * 构造方法，
     * @param mapperInterface mapper的dao接口
     * @param method mapper接口中的方法
     * @param configuration  相关配置
     */
    public MapperMethod(Class<?> mapperInterface, Method method, Configuration configuration) {
        this.command = new SqlCommand(configuration, mapperInterface, method);
        this.method = new MethodSignature(configuration, method);
    }


    /**
     * 执行sql
     * @param sqlSession  sql会话
     * @param args 方法参数
     * @return
     */
    public Object execute(SqlSession sqlSession, Object[] args) {
        Object result = null;
        switch (command.getType()) {
            case INSERT: {
                Object param = method.convertArgsToSqlCommandParam(args);
                result = sqlSession.insert(command.getName(), param);
                break;
            }
            case DELETE: {
                Object param = method.convertArgsToSqlCommandParam(args);
                result = sqlSession.delete(command.getName(), param);
                break;
            }
            case UPDATE: {
                Object param = method.convertArgsToSqlCommandParam(args);
                result = sqlSession.update(command.getName(), param);
                break;
            }
            case SELECT: {
                Object param = method.convertArgsToSqlCommandParam(args);
                if (method.returnsMany) {
                    result = sqlSession.selectList(command.getName(), param);
                } else {
                    result = sqlSession.selectOne(command.getName(), param);
                }
                break;
            }
            default:
                throw new RuntimeException("Unknown execution method for: " + command.getName());
        }
        return result;
    }

    /**
     * SQL 指令
     */
    public static class SqlCommand {
        // sql指令名称
        private final String name;
        // sql指令类型
        private final SqlCommandType type;

        public SqlCommand(Configuration configuration, Class<?> mapperInterface, Method method) {
            //从类路径+类名称+.方法名称 获取到MappedStatement，从而拿到sql指令名称和类型
            String statementName = mapperInterface.getName() + "." + method.getName();
            MappedStatement ms = configuration.getMappedStatement(statementName);
            name = ms.getId();
            type = ms.getSqlCommandType();
        }

        public String getName() {
            return name;
        }

        public SqlCommandType getType() {
            return type;
        }
    }

    /**
     * 方法签名
     */
    public static class MethodSignature {

        private final boolean returnsMany;

        private final Class<?> returnType;

        // 按参数位置排序的参数映射
        private final SortedMap<Integer, String> params;

        // 构造函数，根据方法获取参数映射
        public MethodSignature(Configuration configuration, Method method) {
            // 这个方法根据传入的method对象，生成一个包含方法参数的SortedMap。键是参数的位置（索引），值是参数的名称（在当前实现中，名称是参数的顺序编号字符串）。
            SortedMap<Integer, String> paramsMap = this.getParams(method);
            // 这是Java集合框架中的一个静态方法，用于创建一个不可修改的视图。任何对这个视图的修改操作（如put、remove等）都会抛出UnsupportedOperationException异常。
            this.params = Collections.unmodifiableSortedMap(paramsMap);
            this.returnType = method.getReturnType();
            this.returnsMany = (configuration.getObjectFactory().isCollection(this.returnType) || this.returnType.isArray());
        }

        // 将方法参数转换为SQL命令参数
        public Object convertArgsToSqlCommandParam(Object[] args) {
            final int paramCount = params.size();
            if (args == null || paramCount == 0) {
                //如果没参数
                return null;
            } else if (paramCount == 1) {
                // 如果只有一个参数，直接返回该参数
                return args[params.keySet().iterator().next().intValue()];
            } else {
                // 否则，返回一个ParamMap，修改参数名，参数名就是其位置
                final Map<String, Object> param = new ParamMap<Object>();
                int i = 0;
                for (Map.Entry<Integer, String> entry : params.entrySet()) {
                    // 1.先加一个#{0},#{1},#{2}...参数
                    param.put(entry.getValue(), args[entry.getKey().intValue()]);
                    // issue #71, add param names as param1, param2...but ensure backward compatibility
                    // 为了向后兼容，添加#{param1}, #{param2}...参数
                    final String genericParamName = "param" + (i + 1);
                    if (!param.containsKey(genericParamName)) {
                        /*
                         * 2.再加一个#{param1},#{param2}...参数
                         * 你可以传递多个参数给一个映射器方法。如果你这样做了,
                         * 默认情况下它们将会以它们在参数列表中的位置来命名,比如:#{param1},#{param2}等。
                         * 如果你想改变参数的名称(只在多参数情况下) ,那么你可以在参数上使用@Param(“paramName”)注解。
                         */
                        param.put(genericParamName, args[entry.getKey()]);
                    }
                    i++;
                }
                return param;
            }
        }

        // 获取方法的参数映射
        private SortedMap<Integer, String> getParams(Method method) {
            // 用一个TreeMap，这样就保证还是按参数的先后顺序
            final SortedMap<Integer, String> params = new TreeMap<Integer, String>();
            final Class<?>[] argTypes = method.getParameterTypes();
            for (int i = 0; i < argTypes.length; i++) {
                String paramName = String.valueOf(params.size());
                // 不做 Param 的实现，这部分不处理。如果扩展学习，需要添加 Param 注解并做扩展实现。
                params.put(i, paramName);
            }
            return params;
        }

        public boolean returnsMany() {
            return returnsMany;
        }
    }

    /**
     * 参数map，静态内部类,更严格的get方法，如果没有相应的key，报错
     */
    public static class ParamMap<V> extends HashMap<String, V> {

        private static final long serialVersionUID = -2212268410512043556L;

        @Override
        public V get(Object key) {
            if (!super.containsKey(key)) {
                throw new RuntimeException("Parameter '" + key + "' not found. Available parameters are " + keySet());
            }
            return super.get(key);
        }

    }

}
