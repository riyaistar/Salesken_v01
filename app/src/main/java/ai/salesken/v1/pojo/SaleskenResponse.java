package ai.salesken.v1.pojo;

public class SaleskenResponse {
    private Integer responseCode;
    private String responseMessage;
    private Object response;

    public SaleskenResponse() {
        super();
    }


    public SaleskenResponse(Integer responseCode, String responseMessage) {
        super();
        this.responseCode = responseCode;
        this.responseMessage = responseMessage;
    }

    public Integer getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(Integer responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public Object getResponse() {
        return response;
    }

    public void setResponse(Object response) {
        this.response = response;
    }

    public SaleskenResponse(Integer responseCode, String responseMessage, Object response) {
        super();
        this.responseCode = responseCode;
        this.responseMessage = responseMessage;
        this.response = response;
    }



}
