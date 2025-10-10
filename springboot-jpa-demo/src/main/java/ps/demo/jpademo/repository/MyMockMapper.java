package ps.demo.jpademo.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import ps.demo.jpademo.entity.MyMock;

@Mapper
public interface MyMockMapper extends BaseMapper<MyMock> {

}
