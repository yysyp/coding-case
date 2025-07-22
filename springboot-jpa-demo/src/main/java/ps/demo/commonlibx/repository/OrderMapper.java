package ps.demo.commonlibx.repository;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import ps.demo.commonlibx.entity.Order;

@Mapper
public interface OrderMapper extends BaseMapper<Order> {

}
