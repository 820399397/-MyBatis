package cn.xuguowen.pojo;

import java.util.Date;

/**
 * ClassName: User
 * Package: cn.xuguowen.pojo
 * Description:
 *
 * @Author 徐国文
 * @Create 2024/2/8 21:19
 * @Version 1.0
 */
public class User {

    private Long id;

    private String userId;

    private String userHead;

    private Date createTime;

    private Date updateTime;

    private String userName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserHead() {
        return userHead;
    }

    public void setUserHead(String userHead) {
        this.userHead = userHead;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
