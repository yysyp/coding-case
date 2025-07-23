package ps.demo.pg.error;

public class DemoServiceException extends RuntimeException {

    public DemoServiceException(String message) {
        super(message);
    }

}