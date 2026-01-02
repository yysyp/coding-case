package ps.demo.jpademo.common;

import org.apache.commons.lang3.RandomUtils;
import org.awaitility.Awaitility;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

public class AwaitTool {

    public static <T> T await(Callable<T> callable, int timeoutSeconds) {
        return Awaitility.await().timeout(timeoutSeconds, TimeUnit.SECONDS)
                .pollDelay(500, TimeUnit.MILLISECONDS)
                .pollInterval(1, TimeUnit.SECONDS)
                .until(callable, (result) -> result != null);

    }

    public static void main(String[] args) {
        Integer r = await(() -> {
            int i = RandomUtils.nextInt(1, 10);
            System.out.println("i=" + i);
            return i > 5 ? i : null;
        }, 10);
        System.out.println("r=" + r);
    }

}
