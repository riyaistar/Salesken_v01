package ai.salesken.v1.pojo;

public class PipelineStage {
    private Integer id;
    private String stageName;
    public PipelineStage() {
        super();
    }
    public PipelineStage(Integer id, String stageName) {
        super();
        this.id = id;
        this.stageName = stageName;
    }
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getStageName() {
        return stageName;
    }
    public void setStageName(String stageName) {
        this.stageName = stageName;
    }

}
