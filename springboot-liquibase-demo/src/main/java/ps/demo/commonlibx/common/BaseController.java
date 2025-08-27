package ps.demo.commonlibx.common;

import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BaseController {

    public final static String DATE_FORMAT_STR_ISO8601 = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";

    @InitBinder
    protected void init(HttpServletRequest request, ServletRequestDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_STR_ISO8601);
        dateFormat.setLenient(true);
        binder.registerCustomEditor(Date.class, new TheCustomDateEditor(dateFormat, true));
    }

    protected Pageable constructPagable(BasePageReq BasePageReq) {
        List<BasePageReq.OrderBy> orderByList = BasePageReq.getOrderBys();
        Sort sort = Sort.unsorted();
        if (CollectionUtils.isNotEmpty(orderByList)) {
            List<Sort.Order> orders = new ArrayList<>();
            for (BasePageReq.OrderBy orderBy : orderByList) {
                if (orderBy.getAsc()) {
                    orders.add(Sort.Order.asc(orderBy.getKey()));
                } else {
                    orders.add(Sort.Order.desc(orderBy.getKey()));
                }
            }
            sort = Sort.by(orders);
        }
        Pageable pageable = PageRequest.of((int) (BasePageReq.getCurrent() - 1), (int) BasePageReq.getSize(), sort);
        return pageable;
    }

    protected void initBaseCreateModifyTs(BaseDto myBaseDto) {
        //LoginUserDetail loginUserDetail = MyPrincipalUtils.getCurrentUser();
        if (myBaseDto.getCreatedOn() == null) {
            myBaseDto.setCreatedOn(Instant.now());
            myBaseDto.setIsActive(true);
            myBaseDto.setIsLogicalDeleted(false);
//            if (loginUserDetail != null) {
//                myBaseDto.setCreatedBy(loginUserDetail.getUserName());
//            } else {
//                myBaseDto.setCreatedBy("");
//            }
            myBaseDto.setCreatedBy("");
        }
        myBaseDto.setModifiedOn(Instant.now());
//        if (loginUserDetail != null) {
//            myBaseDto.setModifiedBy(loginUserDetail.getUserName());
//        } else {
//            myBaseDto.setModifiedBy("");
//        }
        myBaseDto.setModifiedBy("");
    }
}
