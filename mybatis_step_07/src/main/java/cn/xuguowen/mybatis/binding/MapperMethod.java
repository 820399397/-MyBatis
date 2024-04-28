package cn.xuguowen.mybatis.binding;

import cn.xuguowen.mybatis.mapping.MappedStatement;
import cn.xuguowen.mybatis.mapping.SqlCommandType;
import cn.xuguowen.mybatis.session.Configuration;
import cn.xuguowen.mybatis.session.SqlSession;

import java.lang.reflect.Method;

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

    /**
     * 构造方法，
     * @param mapperInterface mapper的dao接口
     * @param method mapper接口中的方法
     * @param configuration  相关配置
     */
    public MapperMethod(Class<?> mapperInterface, Method method, Configuration configuration) {
        this.command = new SqlCommand(configuration, mapperInterface, method);
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
            case INSERT:
                break;
            case DELETE:
                break;
            case UPDATE:
                break;
            case SELECT:
                result = sqlSession.selectOne(command.getName(), args);
                break;
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
}
