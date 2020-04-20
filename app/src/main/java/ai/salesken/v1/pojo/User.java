package ai.salesken.v1.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;
public class User {
    private Integer id;
    private String email;
    private String mobile;
    private String name;
    private String language;
    private String profileImage;
    private String password;
    private String newPassword;
    private String location;
    private User owner;
    private String designation;
    private Integer designationId;
    private String status;
    private Integer organizationId;
    private ArrayList<UserRole> roles = new ArrayList<UserRole>();
    private ArrayList<String> licenseKeys = new ArrayList<String>();

    private Integer sipId;
    private String sipUserName;
    private String sipPassword;
    private String sipURL;
    private String sipProvider;



    // Default constructor
    public User() {
        super();
    }

    // Minimal Constructor
    public User(Integer id, String email, String mobile, String name) {
        super();
        this.id = id;
        this.email = email;
        this.mobile = mobile;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public Integer getDesignationId() {
        return designationId;
    }

    public void setDesignationId(Integer designationId) {
        this.designationId = designationId;
    }

    /**
     * @return the organizationId
     */
    public Integer getOrganizationId() {
        return organizationId;
    }

    /**
     * @param organizationId the organisationId to set
     */
    public void setOrganizationId(Integer organizationId) {
        this.organizationId = organizationId;
    }

    public ArrayList<String> getLicenseKeys() {
        return licenseKeys;
    }

    public void setLicenseKeys(ArrayList<String> licenseKeys) {
        this.licenseKeys = licenseKeys;
    }

    public ArrayList<UserRole> getRoles() {
        return roles;
    }

    public void setRoles(ArrayList<UserRole> roles) {
        this.roles = roles;
    }

    public String getSipUserName() {
        return sipUserName;
    }

    public void setSipUserName(String sipUserName) {
        this.sipUserName = sipUserName;
    }

    public String getSipPassword() {
        return sipPassword;
    }

    public void setSipPassword(String sipPassword) {
        this.sipPassword = sipPassword;
    }

    public String getSipURL() {
        return sipURL;
    }

    public void setSipURL(String sipURL) {
        this.sipURL = sipURL;
    }



    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }


    public Integer getSipId() {
        return sipId;
    }

    public void setSipId(Integer sipId) {
        this.sipId = sipId;
    }

    public String getSipProvider() {
        return sipProvider;
    }

    public void setSipProvider(String sipProvider) {
        this.sipProvider = sipProvider;
    }

    @Override
    public String toString() {
        return "User [id=" + id + ", email=" + email + ", mobile=" + mobile + ", name=" + name + ", profileImage="
                + profileImage + ", password=" + password + ", newPassword=" + newPassword + ", location=" + location
                + ", owner=" + owner + ", designation=" + designation + ", designationId=" + designationId + ", status="
                + status + ", organizationId=" + organizationId + ", roles=" + roles + ", licenseKeys=" + licenseKeys
               + "]";
    }

    public enum UserRoleTypes {
        SUPER_ADMIN, OWNER, SALES_MANAGER, SALES_ASSOCIATE, IT_ADMIN
    }

}
