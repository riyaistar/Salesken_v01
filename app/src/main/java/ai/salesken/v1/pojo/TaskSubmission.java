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
    private Boolean isNextStage;

    public TaskSubmission() {
        super();
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

    public Boolean getIsNextStage() {
        return isNextStage;
    }

    public void setIsNextStage(Boolean isNextStage) {
        this.isNextStage = isNextStage;
    }





}
