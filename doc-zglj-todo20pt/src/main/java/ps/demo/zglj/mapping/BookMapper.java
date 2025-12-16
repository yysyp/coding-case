package ps.demo.zglj.mapping;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ps.demo.zglj.dto.BookDto;
import ps.demo.zglj.entity.Book;

@Mapper
public interface BookMapper {

    BookMapper INSTANCE = Mappers.getMapper( BookMapper.class );

    BookDto toDto(Book book);

    Book toEntity(BookDto bookDto);


}
