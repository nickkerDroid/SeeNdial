package info.fortheease.seendial;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "user_data")
public class User {
    @PrimaryKey
    @NonNull
    private String uid;
    @ColumnInfo(name = "phoneNumber")
    private final String phoneNum;
    @ColumnInfo(name = "profilePhoto")
    private final String imgAddress;

    public User(String phoneNum, @NonNull String imgAddress) {
        this.phoneNum = phoneNum;
        this.imgAddress = imgAddress;
        this.uid = imgAddress;
    }

    public void setUid(@NonNull String uid) {
        this.uid = uid;
    }
    @NonNull
    public String getUid() {
        return uid;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public String getImgAddress() {
        return imgAddress;
    }

}
