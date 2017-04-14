package com.cxb.qqapp.model;

import java.io.Serializable;

/**
 * QQ消息
 */

public class QQMessageBean implements Serializable {

    private int avatarRes;//头像资源
    private String name;//名称
    private String content;//内容
    private String time;//时间

    public int getAvatarRes() {
        return avatarRes;
    }

    public void setAvatarRes(int avatarRes) {
        this.avatarRes = avatarRes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
