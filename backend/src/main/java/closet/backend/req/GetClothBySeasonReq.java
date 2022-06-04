package closet.backend.req;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.ToString;

@Data
@Getter
@ToString
@AllArgsConstructor
public class GetClothBySeasonReq {

    private String idToken;
    private String season;
}
