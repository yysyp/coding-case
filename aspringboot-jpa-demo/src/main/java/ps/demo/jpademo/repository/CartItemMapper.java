package ps.demo.jpademo.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import ps.demo.jpademo.entity.CartItem;

@Mapper
public interface CartItemMapper extends BaseMapper<CartItem> {

}
