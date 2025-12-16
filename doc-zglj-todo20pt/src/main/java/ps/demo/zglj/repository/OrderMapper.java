package ps.demo.zglj.repository;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import ps.demo.zglj.entity.Order;

@Mapper
public interface OrderMapper extends BaseMapper<Order> {

}
