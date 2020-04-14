package ai.salesken.v1.pojo;

public class ContactPojo {
    String name;
    String status;
    String mobile;
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
}
