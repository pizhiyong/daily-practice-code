package com.pizhiyong.dailypractice.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * TiFlash用户实体类
 * 模拟TiFlash数据源返回的用户结构（与MySQL结构不同）
 *
 * @author pizhiyong
 * @since 1.0
 */
public class TiFlashUser implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /** 用户ID */
    private Long userId;
    
    /** 用户名称 */
    private String userName;
    
    /** 用户年龄 */
    private Integer userAge;
    
    /** 性别代码：1-男，2-女，0-未知 */
    private Integer genderCode;
    
    /** 创建时间 */
    private LocalDateTime createTime;
    
    /** 更新时间 */
    private LocalDateTime updateTime;
    
    /** 用户状态：ACTIVE, INACTIVE, DELETED */
    private String status;
    
    /** 扩展信息（JSON格式） */
    private String extInfo;
    
    // 构造函数
    public TiFlashUser() {}
    
    public TiFlashUser(Long userId, String userName, Integer userAge, Integer genderCode) {
        this.userId = userId;
        this.userName = userName;
        this.userAge = userAge;
        this.genderCode = genderCode;
        this.createTime = LocalDateTime.now();
        this.updateTime = LocalDateTime.now();
        this.status = "ACTIVE";
    }
    
    // Getter和Setter方法
    public Long getUserId() {
        return userId;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    public String getUserName() {
        return userName;
    }
    
    public void setUserName(String userName) {
        this.userName = userName;
    }
    
    public Integer getUserAge() {
        return userAge;
    }
    
    public void setUserAge(Integer userAge) {
        this.userAge = userAge;
    }
    
    public Integer getGenderCode() {
        return genderCode;
    }
    
    public void setGenderCode(Integer genderCode) {
        this.genderCode = genderCode;
    }
    
    public LocalDateTime getCreateTime() {
        return createTime;
    }
    
    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }
    
    public LocalDateTime getUpdateTime() {
        return updateTime;
    }
    
    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getExtInfo() {
        return extInfo;
    }
    
    public void setExtInfo(String extInfo) {
        this.extInfo = extInfo;
    }
    
    @Override
    public String toString() {
        return "TiFlashUser{" +
                "userId=" + userId +
                ", userName='" + userName + '\'' +
                ", userAge=" + userAge +
                ", genderCode=" + genderCode +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", status='" + status + '\'' +
                ", extInfo='" + extInfo + '\'' +
                '}';
    }
}
