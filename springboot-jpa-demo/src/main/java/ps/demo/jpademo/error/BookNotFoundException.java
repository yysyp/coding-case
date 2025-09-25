package ps.demo.jpademo.error;

public class BookNotFoundException extends BaseErrorException {

    public BookNotFoundException(Long id) {
        super("Book id not found : " + id);
    }

}