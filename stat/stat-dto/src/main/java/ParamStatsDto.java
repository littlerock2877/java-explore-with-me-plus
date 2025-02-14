import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class ParamStatsDto {
    private List<String> uris;
    private Boolean unique;
    private LocalDateTime start;
    private LocalDateTime end;
}
