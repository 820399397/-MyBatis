package cn.xuguowen.mybatis.builder;

import cn.xuguowen.mybatis.session.Configuration;

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

    protected Configuration configuration;

    public BaseBuilder(Configuration configuration) {
        this.configuration = configuration;
    }
}
