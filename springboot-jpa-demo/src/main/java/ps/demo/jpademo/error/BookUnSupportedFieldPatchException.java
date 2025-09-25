package ps.demo.jpademo.error;

import java.util.Set;
public class BookUnSupportedFieldPatchException extends BaseErrorException {

    public BookUnSupportedFieldPatchException(Set<String> keys) {
        super("Field " + keys.toString() + " update is not allow.");
    }

}