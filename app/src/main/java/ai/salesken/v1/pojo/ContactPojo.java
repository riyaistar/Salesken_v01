package ai.salesken.v1.pojo;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "contactpojo",indices={
        @Index(value="uid"),
        @Index(value="mobile", unique = true)
})
public class ContactPojo {
    @PrimaryKey(autoGenerate = true)
    public int uid;
    @ColumnInfo(name = "name")
    String name;
    @ColumnInfo(name = "status")
    String status;
    @ColumnInfo(name = "mobile")
    String mobile;
    @ColumnInfo(name = "email")
    String email;

    public ContactPojo() {
    }

    public ContactPojo(String name, String status) {
        this.name = name;
        this.status = status;
    }

    public ContactPojo(String name, String status, String mobile, String email) {
        this.name = name;
        this.status = status;
        this.mobile = mobile;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }
}
