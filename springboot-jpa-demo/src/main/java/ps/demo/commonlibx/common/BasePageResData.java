package ps.demo.commonlibx.common;


import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.domain.Page;

import java.util.List;


@ToString
@Getter
@Setter
@EqualsAndHashCode
public class BasePageResData<T> implements java.io.Serializable {

    private Long current = 1L;
    private Long size = 10L;
    private Long total = 0L;
    private List<T> list;

    //Extra calculation properties Begin
    private Long pages = 0L; // (total + size - 1)/size
    private Boolean isFirst = false; //current == 1;
    private Boolean isLast = false; //current == pages;
    //Extra calculation properties END

    private void calcProperites() {
        this.pages = (this.total + this.size - 1) / this.size;
        if (this.pages < 0) {
            this.pages = 0L;
            this.isFirst = true;
            this.isLast = true;
            return;
        }
        this.isFirst = this.current == 1;
        this.isLast = this.current == this.pages;
    }

    public BasePageResData() {

    }

    public BasePageResData(Page<T> jpage, Long current, Long size) {
        this.total = jpage.getTotalElements();
        this.current = current;
        this.size = size;
        this.list = jpage.getContent();
        calcProperites();
    }

    public BasePageResData(Long current, Long size, Long total, List<T> list) {
        this.current = current;
        this.size = size;
        this.total = total;
        calcProperites();
        this.setList(list);
    }


}
