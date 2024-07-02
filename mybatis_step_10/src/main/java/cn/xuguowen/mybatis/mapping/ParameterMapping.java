package cn.xuguowen.mybatis.mapping;

import cn.xuguowen.mybatis.session.Configuration;
import cn.xuguowen.mybatis.type.JdbcType;
import cn.xuguowen.mybatis.type.TypeHandler;
import cn.xuguowen.mybatis.type.TypeHandlerRegistry;

/**
 * ClassName: ParameterMapping
 * Package: cn.xuguowen.mybatis.mapping
 * Description:表示一个SQL参数的映射信息，用于在处理SQL语句时进行参数绑定
 *
 * @Author 徐国文
 * @Create 2024/5/30 12:00
 * @Version 1.0
 */
public class ParameterMapping {
    // MyBatis配置对象
    private Configuration configuration;

    // 参数属性名
    private String property;

    // javaType = int
    // 参数的Java类型，默认是Object类型
    private Class<?> javaType = Object.class;

    // 参数的JDBC类型
    // jdbcType=NUMERIC
    private JdbcType jdbcType;

    // 参数的类型处理器
    private TypeHandler<?> typeHandler;

    // 私有构造方法，防止直接实例化
    private ParameterMapping() {
    }

    /**
     * Builder类，用于构建ParameterMapping对象
     */
    public static class Builder {

        private ParameterMapping parameterMapping = new ParameterMapping();

        /**
         * 构造函数，初始化Builder
         * @param configuration MyBatis配置对象
         * @param property 参数属性名
         * @param javaType 参数的Java类型
         */
        public Builder(Configuration configuration, String property, Class<?> javaType) {
            parameterMapping.configuration = configuration;
            parameterMapping.property = property;
            parameterMapping.javaType = javaType;
        }

        /**
         * 设置参数的Java类型
         * @param javaType 参数的Java类型
         * @return 当前Builder实例
         */
        public Builder javaType(Class<?> javaType) {
            parameterMapping.javaType = javaType;
            return this;
        }

        /**
         * 设置参数的JDBC类型
         * @param jdbcType 参数的JDBC类型
         * @return 当前Builder实例
         */
        public Builder jdbcType(JdbcType jdbcType) {
            parameterMapping.jdbcType = jdbcType;
            return this;
        }

        /**
         * 构建ParameterMapping对象
         * @return ParameterMapping对象
         */
        public ParameterMapping build() {
            if (parameterMapping.typeHandler == null && parameterMapping.javaType != null) {
                Configuration configuration = parameterMapping.configuration;
                TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
                parameterMapping.typeHandler = typeHandlerRegistry.getTypeHandler(parameterMapping.javaType, parameterMapping.jdbcType);
            }

            return parameterMapping;
        }

    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public String getProperty() {
        return property;
    }

    public Class<?> getJavaType() {
        return javaType;
    }

    public JdbcType getJdbcType() {
        return jdbcType;
    }

    public TypeHandler<?> getTypeHandler() {
        return typeHandler;
    }

}
