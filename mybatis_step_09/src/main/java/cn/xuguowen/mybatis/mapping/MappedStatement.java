package cn.xuguowen.mybatis.mapping;

import cn.xuguowen.mybatis.scripting.LanguageDriver;
import cn.xuguowen.mybatis.session.Configuration;

/**
 * ClassName: MappedStatement
 * Package: cn.xuguowen.mybatis.mapping
 * Description:
 *
 * @Author 徐国文
 * @Create 2024/2/14 23:14
 * @Version 1.0
 */
public class MappedStatement {
    // 保存配置项相关信息
    private Configuration configuration;
    // 唯一标识，一般是方法名称
    private String id;
    // SQL命令类型
    private SqlCommandType sqlCommandType;

    // SQL信息：sql语句、参数类型、参数集合、返回值类型
    private SqlSource sqlSource;

    Class<?> resultType;

    private LanguageDriver lang;


    private MappedStatement() {
        // constructor disabled
    }

    /**
     * 建造者
     */
    public static class Builder {

        private MappedStatement mappedStatement = new MappedStatement();

        public Builder(Configuration configuration, String id, SqlCommandType sqlCommandType, SqlSource sqlSource, Class<?> resultType) {
            mappedStatement.configuration = configuration;
            mappedStatement.id = id;
            mappedStatement.sqlCommandType = sqlCommandType;
            mappedStatement.sqlSource = sqlSource;
            mappedStatement.resultType = resultType;
            mappedStatement.lang = configuration.getDefaultScriptingLanguageInstance();
        }

        public MappedStatement build() {
            assert mappedStatement.configuration != null;
            assert mappedStatement.id != null;
            return mappedStatement;
        }

    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public String getId() {
        return id;
    }

    public SqlCommandType getSqlCommandType() {
        return sqlCommandType;
    }

    public SqlSource getSqlSource() {
        return sqlSource;
    }

    public Class<?> getResultType() {
        return resultType;
    }

    public LanguageDriver getLang() {
        return lang;
    }


}
