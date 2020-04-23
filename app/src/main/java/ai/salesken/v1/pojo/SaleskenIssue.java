package ai.salesken.v1.pojo;

public class SaleskenIssue {
    private String reason;
    private String description;

    public SaleskenIssue() {
    }

    public SaleskenIssue(String reason, String description) {
        this.reason = reason;
        this.description = description;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
