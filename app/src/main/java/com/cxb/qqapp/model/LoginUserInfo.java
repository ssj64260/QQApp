package com.cxb.qqapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.litesuits.orm.db.annotation.PrimaryKey;
import com.litesuits.orm.db.annotation.Table;
import com.litesuits.orm.db.enums.AssignType;

/**
 * QQ登录用户信息
 */

@Table("LoginUserInfo")
public class LoginUserInfo implements Parcelable {

    @PrimaryKey(AssignType.BY_MYSELF)
    private String username;//用户名
    private String password;//密码（忽略加密）
    private int avatar;//头像（暂时使用APP内部资源）
    private long timeStamp;//时间戳
    private String nickname;//昵称
    private int qqLevel;//qq等级
    private String sign;//个性签名

    public int getAvatar() {
        return avatar;
    }

    public void setAvatar(int avatar) {
        this.avatar = avatar;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getQqLevel() {
        return qqLevel;
    }

    public void setQqLevel(int qqLevel) {
        this.qqLevel = qqLevel;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(avatar);
        dest.writeString(username);
        dest.writeString(password);
        dest.writeLong(timeStamp);
        dest.writeString(nickname);
        dest.writeInt(qqLevel);
        dest.writeString(sign);
    }

    public static final Creator<LoginUserInfo> CREATOR = new Creator<LoginUserInfo>() {
        @Override
        public LoginUserInfo createFromParcel(Parcel in) {
            LoginUserInfo userInfo = new LoginUserInfo();
            userInfo.setAvatar(in.readInt());
            userInfo.setUsername(in.readString());
            userInfo.setPassword(in.readString());
            userInfo.setTimeStamp(in.readLong());
            userInfo.setNickname(in.readString());
            userInfo.setQqLevel(in.readInt());
            userInfo.setSign(in.readString());
            return userInfo;
        }

        @Override
        public LoginUserInfo[] newArray(int size) {
            return new LoginUserInfo[size];
        }
    };
}
