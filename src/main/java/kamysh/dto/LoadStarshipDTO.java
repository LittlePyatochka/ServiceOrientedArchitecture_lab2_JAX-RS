package kamysh.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoadStarshipDTO {
    private Integer SpaceMarineId;
    private Integer StarshipId;
}