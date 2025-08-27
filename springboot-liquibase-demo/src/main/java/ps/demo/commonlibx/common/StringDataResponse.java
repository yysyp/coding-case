package ps.demo.commonlibx.common;

import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StringDataResponse extends BaseResponse {

    private String data;

    public static BaseResponse successWithData(Object strableObj) {
        StringDataResponse stringDataResponse = new StringDataResponse();
        stringDataResponse.setData(new Gson().toJson(strableObj));
        return stringDataResponse;
    }

}
