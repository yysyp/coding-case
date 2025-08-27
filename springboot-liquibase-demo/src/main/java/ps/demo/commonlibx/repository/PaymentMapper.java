package ps.demo.commonlibx.repository;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import ps.demo.commonlibx.entity.Payment;

@Mapper
public interface PaymentMapper extends BaseMapper<Payment> {

}
