

package ps.demo.commonlibx.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Getter
@Setter
@ToString
@TableName("my_mock")
public class MyMock {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String uri;
    private Boolean regexMatch;
    private String method;
    private Integer status;
    private String headers;
    private String body;

}


