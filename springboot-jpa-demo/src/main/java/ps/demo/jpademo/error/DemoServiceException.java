package ps.demo.jpademo.error;

public class DemoServiceException extends RuntimeException {

    public DemoServiceException(String message) {
        super(message);
    }

}