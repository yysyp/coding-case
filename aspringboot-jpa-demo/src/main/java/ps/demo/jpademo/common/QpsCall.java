package ps.demo.jpademo.common;

import java.util.concurrent.Callable;

public abstract class QpsCall<T> implements Callable<T> {
    private T t;
    public QpsCall(T t) {
        this.t = t;
    }

    public abstract T call() throws Exception;
}
