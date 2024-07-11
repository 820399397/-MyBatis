package cn.xuguowen.mybatis.builder;

import cn.xuguowen.mybatis.mapping.BoundSql;
import cn.xuguowen.mybatis.mapping.ParameterMapping;
import cn.xuguowen.mybatis.mapping.SqlSource;
import cn.xuguowen.mybatis.session.Configuration;

import java.util.List;

/**
 * ClassName: StaticSqlSource
 * Package: cn.xuguowen.mybatis.builder
 * Description:
 *
 * @Author 徐国文
 * @Create 2024/5/30 11:45
 * @Version 1.0
 */
public class StaticSqlSource implements SqlSource {
    private String sql;
    private List<ParameterMapping> parameterMappings;
    private Configuration configuration;

    public StaticSqlSource(Configuration configuration, String sql) {
        this(configuration, sql, null);
    }

    public StaticSqlSource(Configuration configuration, String sql, List<ParameterMapping> parameterMappings) {
        this.sql = sql;
        this.parameterMappings = parameterMappings;
        this.configuration = configuration;
    }

    @Override
    public BoundSql getBoundSql(Object parameterObject) {
        return new BoundSql(configuration, sql, parameterMappings, parameterObject);
    }

}
