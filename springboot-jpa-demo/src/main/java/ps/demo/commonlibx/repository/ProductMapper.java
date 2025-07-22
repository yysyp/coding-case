package ps.demo.commonlibx.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import ps.demo.commonlibx.entity.Product;

@Mapper
public interface ProductMapper extends BaseMapper<Product> {

}
