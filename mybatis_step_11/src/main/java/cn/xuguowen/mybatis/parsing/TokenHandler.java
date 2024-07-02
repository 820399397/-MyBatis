package cn.xuguowen.mybatis.parsing;

/**
 * ClassName: TokenHandler
 * Package: cn.xuguowen.mybatis.parsing
 * Description:记号处理器接口，用于处理占位符的内容。
 *
 * @Author 徐国文
 * @Create 2024/5/30 10:04
 * @Version 1.0
 */
public interface TokenHandler {

    /**
     * 处理占位符的内容。
     *
     * @param content 占位符中的内容
     * @return 处理后的内容
     */
    String handleToken(String content);

}

