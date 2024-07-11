package cn.xuguowen.mybatis.executor.result;

import cn.xuguowen.mybatis.reflection.factory.ObjectFactory;
import cn.xuguowen.mybatis.session.ResultContext;
import cn.xuguowen.mybatis.session.ResultHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * ClassName: DefaultResultHandler
 * Package: cn.xuguowen.mybatis.executor.result
 * Description:
 *
 * @Author 徐国文
 * @Create 2024/6/6 15:34
 * @Version 1.0
 */
public class DefaultResultHandler implements ResultHandler {
    private final List<Object> list;

    public DefaultResultHandler() {
        this.list = new ArrayList<>();
    }

    /**
     * 通过 ObjectFactory 反射工具类，产生特定的 List
     */
    @SuppressWarnings("unchecked")
    public DefaultResultHandler(ObjectFactory objectFactory) {
        this.list = objectFactory.create(List.class);
    }

    public void handleResult(ResultContext context) {
        list.add(context.getResultObject());
    }

    public List<Object> getResultList() {
        return list;
    }

}
