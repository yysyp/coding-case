// BookNotFoundException.java
package ps.demo.pg2.exception;

public class BookNotFoundException extends RuntimeException {
    public BookNotFoundException(String message) {
        super(message);
    }
}
