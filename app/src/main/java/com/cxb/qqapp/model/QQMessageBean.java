package com.cxb.qqapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.litesuits.orm.db.annotation.PrimaryKey;
import com.litesuits.orm.db.annotation.Table;
import com.litesuits.orm.db.enums.AssignType;

/**
 * QQ消息
 */

@Table("QQMessageBean")
public class QQMessageBean implements Parcelable {

    public static final String TAG_QQ_MESSAGE = "qq_message";

    @PrimaryKey(AssignType.AUTO_INCREMENT)
    private int _id;

    private String qqNumber;//qq号码
    private int avatarRes;//头像资源
    private String name;//名称
    private String content;//内容
    private String time;//时间

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getQqNumber() {
        return qqNumber;
    }

    public void setQqNumber(String qqNumber) {
        this.qqNumber = qqNumber;
    }

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

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(_id);
        dest.writeString(qqNumber);
        dest.writeInt(avatarRes);
        dest.writeString(name);
        dest.writeString(content);
        dest.writeString(time);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<QQMessageBean> CREATOR = new Creator<QQMessageBean>() {
        @Override
        public QQMessageBean createFromParcel(Parcel in) {
            QQMessageBean qqMessage = new QQMessageBean();
            qqMessage.set_id(in.readInt());
            qqMessage.setQqNumber(in.readString());
            qqMessage.setAvatarRes(in.readInt());
            qqMessage.setName(in.readString());
            qqMessage.setContent(in.readString());
            qqMessage.setTime(in.readString());
            return qqMessage;
        }

        @Override
        public QQMessageBean[] newArray(int size) {
            return new QQMessageBean[size];
        }
    };
}
