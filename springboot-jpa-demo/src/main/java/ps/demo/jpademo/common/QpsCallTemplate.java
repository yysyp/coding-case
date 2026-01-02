package ps.demo.jpademo.common;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class QpsCallTemplate {

    private double[] qps;
    private int calls;

    public QpsCallTemplate(double[] qps, int calls) {
        this.qps = qps;
        this.calls = calls;
    }

    private ExecutorService executorService = Executors.newCachedThreadPool();

    public <T> List<Future<T>> submit(List<QpsCall<T>> callables) {
        int index = 0;
        List<Future<T>> futureList = new ArrayList<>();
        for (int i = 0, len = qps.length; i < len; i++) {
            double curQps = qps[i];
            for (int c = 0; c < calls; c++) {
                futureList.add(executorService.submit(callables.get(index++)));
                try {
                    Thread.sleep((long) (1000 / curQps));
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return futureList;
    }

    public <T> List<T> getAll(List<Future<T>> futureList) {
        List<T> results = new ArrayList<>();
        for (Future<T> future : futureList) {
            try {
                results.add(future.get());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return results;
    }

    public <T> List<T> invoke(List<QpsCall<T>> callables) {
        List<Future<T>> futureList = submit(callables);
        return getAll(futureList);
    }

    public ExecutorService getExecutorService() {
        return this.executorService;
    }

    public void shutdown() {
        this.executorService.shutdown();
    }

    public void shutdownNow() {
        this.executorService.shutdownNow();
    }

}

