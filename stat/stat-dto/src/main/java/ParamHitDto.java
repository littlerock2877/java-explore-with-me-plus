import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

@Data
public class ParamHitDto {
    private String app;
    private String uri;
    private String ip;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String timestamp;
}
