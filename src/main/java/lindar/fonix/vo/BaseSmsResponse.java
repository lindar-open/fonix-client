package lindar.fonix.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by Steven on 16/03/2017.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public abstract class BaseSmsResponse {
    private String txguid;
    private String numbers;
    private String smsparts;
    private String encoding;
}
