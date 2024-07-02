package cn.xuguowen.mybatis.session;

/**
 * ClassName: ResultContext
 * Package: cn.xuguowen.mybatis.session
 * Description:
 *
 * @Author 徐国文
 * @Create 2024/6/6 15:36
 * @Version 1.0
 */
public interface ResultContext {

    /**
     * 获取结果
     */
    Object getResultObject();

    /**
     * 获取记录数
     */
    int getResultCount();


}
