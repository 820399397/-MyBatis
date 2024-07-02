package cn.xuguowen.mybatis.executor.result;

import cn.xuguowen.mybatis.session.ResultContext;

/**
 * ClassName: DefaultResultContext
 * Package: cn.xuguowen.mybatis.executor.result
 * Description:
 *
 * @Author 徐国文
 * @Create 2024/6/6 15:35
 * @Version 1.0
 */
public class DefaultResultContext implements ResultContext {
    private Object resultObject;
    private int resultCount;

    public DefaultResultContext() {
        this.resultObject = null;
        this.resultCount = 0;
    }

    @Override
    public Object getResultObject() {
        return resultObject;
    }

    @Override
    public int getResultCount() {
        return resultCount;
    }

    public void nextResultObject(Object resultObject) {
        resultCount++;
        this.resultObject = resultObject;
    }

}
