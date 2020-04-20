package ai.salesken.v1.pojo;

public class Task {
    private Integer id;
    private String startDate;
    private String endDate;
    private String updatedAt;
    private String status;
    private String companyName;
    private String contactName;
    private String contactNumber;
    private Integer leadId;
    private Integer salesContactId;
    private String taskType;
    private String disposition;
    private String callDuration;




    public Task() {
        super();
    }




    public Task(Integer id, String startDate, String endDate, String updatedAt, String status, String companyName,
                String contactName, String contactNumber, Integer leadId, Integer salesContactId, String taskType) {
        super();
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
        this.updatedAt = updatedAt;
        this.status = status;
        this.companyName = companyName;
        this.contactName = contactName;
        this.contactNumber = contactNumber;
        this.leadId = leadId;
        this.salesContactId = salesContactId;
        this.taskType = taskType;
    }




    public Integer getId() {
        return id;
    }




    public void setId(Integer id) {
        this.id = id;
    }




    public String getStartDate() {
        return startDate;
    }




    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }




    public String getEndDate() {
        return endDate;
    }




    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }




    public String getUpdatedAt() {
        return updatedAt;
    }




    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }




    public String getStatus() {
        return status;
    }




    public void setStatus(String status) {
        this.status = status;
    }




    public String getCompanyName() {
        return companyName;
    }




    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }




    public String getContactName() {
        return contactName;
    }




    public void setContactName(String contactName) {
        this.contactName = contactName;
    }




    public String getContactNumber() {
        return contactNumber;
    }




    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }




    public Integer getLeadId() {
        return leadId;
    }




    public void setLeadId(Integer leadId) {
        this.leadId = leadId;
    }




    public Integer getSalesContactId() {
        return salesContactId;
    }




    public void setSalesContactId(Integer salesContactId) {
        this.salesContactId = salesContactId;
    }




    public String getTaskType() {
        return taskType;
    }




    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }




    public String getDisposition() {
        return disposition;
    }




    public void setDisposition(String disposition) {
        this.disposition = disposition;
    }




    public String getCallDuration() {
        return callDuration;
    }




    public void setCallDuration(String callDuration) {
        this.callDuration = callDuration;
    }





}