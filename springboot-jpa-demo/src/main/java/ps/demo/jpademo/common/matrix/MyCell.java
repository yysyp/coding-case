package ps.demo.jpademo.common.matrix;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
public class MyCell implements Serializable {
    protected Integer x;
    protected Integer y;

    protected Object data;

    public MyCell() {

    }

    public MyCell(int x, int y) {
        this.x = x;
        this.y = y;
    }

}
