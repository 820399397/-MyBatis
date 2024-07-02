package cn.xuguowen.mybatis.builder;

import cn.xuguowen.mybatis.session.Configuration;
import cn.xuguowen.mybatis.type.TypeAliasRegistry;
import cn.xuguowen.mybatis.type.TypeHandlerRegistry;

/**
 * ClassName: BaseBuilder
 * Package: cn.xuguowen.mybatis.builder
 * Description:
 *
 * @Author 徐国文
 * @Create 2024/2/14 22:53
 * @Version 1.0
 */
public class BaseBuilder {

    protected final Configuration configuration;
    protected final TypeAliasRegistry typeAliasRegistry;
    protected final TypeHandlerRegistry typeHandlerRegistry;


    public BaseBuilder(Configuration configuration) {
        this.configuration = configuration;
        this.typeAliasRegistry = this.configuration.getTypeAliasRegistry();
        this.typeHandlerRegistry = this.configuration.getTypeHandlerRegistry();
    }


    protected Class<?> resolveAlias(String alias) {
        return typeAliasRegistry.resolveAlias(alias);
    }

}
