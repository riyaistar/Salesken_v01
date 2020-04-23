package ai.salesken.v1.pojo;

public class TaskSubmission {
    private Integer id;
    private String disposition;
    private Boolean isFollowup;
    private String followupDate;
    private String followupTime;
    private Integer followupActor;
    private Boolean isSendSMS;
    private Boolean isLeadLost;
    private Boolean isLeadWon;
    private String smsContent;

    private Boolean isScheduleActivity;
    private Integer stageId;
    private String scheduleDate;
    private String scheduletime;
    private String callNotes;

    private Boolean isEditLead;
    private String companyName;
    private String contactName;
    private String contactDesignation;
    private String contactEmail;

    private Boolean isDnd;

    private Float dealValue;

    public TaskSubmission() {
        super();
        // TODO Auto-generated constructor stub
    }

    public TaskSubmission(Integer id, String disposition, Boolean isFollowup, String followupDate, String followupTime,
                          Boolean isSendSMS) {
        super();
        this.id = id;
        this.disposition = disposition;
        this.isFollowup = isFollowup;
        this.followupDate = followupDate;
        this.followupTime = followupTime;
        this.isSendSMS = isSendSMS;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDisposition() {
        return disposition;
    }

    public void setDisposition(String disposition) {
        this.disposition = disposition;
    }

    public Boolean getIsFollowup() {
        return isFollowup;
    }

    public void setIsFollowup(Boolean isFollowup) {
        this.isFollowup = isFollowup;
    }

    public String getFollowupDate() {
        return followupDate;
    }

    public void setFollowupDate(String followupDate) {
        this.followupDate = followupDate;
    }

    public String getFollowupTime() {
        return followupTime;
    }

    public void setFollowupTime(String followupTime) {
        this.followupTime = followupTime;
    }

    public Boolean getIsSendSMS() {
        return isSendSMS;
    }

    public void setIsSendSMS(Boolean isSendSMS) {
        this.isSendSMS = isSendSMS;
    }

    public Integer getFollowupActor() {
        return followupActor;
    }

    public void setFollowupActor(Integer followupActor) {
        this.followupActor = followupActor;
    }

    public Boolean getIsLeadLost() {
        return isLeadLost;
    }

    public void setIsLeadLost(Boolean isLeadLost) {
        this.isLeadLost = isLeadLost;
    }

    public String getSmsContent() {
        return smsContent;
    }

    public void setSmsContent(String smsContent) {
        this.smsContent = smsContent;
    }

    public Boolean getIsLeadWon() {
        return isLeadWon;
    }

    public void setIsLeadWon(Boolean isLeadWon) {
        this.isLeadWon = isLeadWon;
    }

    public Boolean getIsScheduleActivity() {
        return isScheduleActivity;
    }

    public void setIsScheduleActivity(Boolean isScheduleActivity) {
        this.isScheduleActivity = isScheduleActivity;
    }

    public String getScheduleDate() {
        return scheduleDate;
    }

    public void setScheduleDate(String scheduleDate) {
        this.scheduleDate = scheduleDate;
    }

    public String getScheduletime() {
        return scheduletime;
    }

    public void setScheduletime(String scheduletime) {
        this.scheduletime = scheduletime;
    }

    public String getCallNotes() {
        return callNotes;
    }

    public void setCallNotes(String callNotes) {
        this.callNotes = callNotes;
    }

    public Integer getStageId() {
        return stageId;
    }

    public void setStageId(Integer stageId) {
        this.stageId = stageId;
    }

    public Float getDealValue() {
        return dealValue;
    }

    public void setDealValue(Float dealValue) {
        this.dealValue = dealValue;
    }

    public Boolean getIsEditLead() {
        return isEditLead;
    }

    public void setIsEditLead(Boolean isEditLead) {
        this.isEditLead = isEditLead;
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

    public String getContactDesignation() {
        return contactDesignation;
    }

    public void setContactDesignation(String contactDesignation) {
        this.contactDesignation = contactDesignation;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public Boolean getIsDnd() {
        return isDnd;
    }

    public void setIsDnd(Boolean isDnd) {
        this.isDnd = isDnd;
    }


}
