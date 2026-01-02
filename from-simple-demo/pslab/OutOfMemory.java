package pslab;


import java.util.HashMap;
import java.util.Map;

public class OutOfMemory {
    /**
     * java -Xmx8g -Xms8g ps.demo.lab.OutOfMemory
     * @param args
     * @throws InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {
        Thread.currentThread().setName("The-MainThread");
        Thread monitoring = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    printRuntimeInfo();
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        monitoring.setName("The-MonitoringThread");
        monitoring.start();

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                int initSize = 500 * 1024 * 1024;
                long totalUsed = initSize;
                Map<String, Object> map = new HashMap<>();
                map.put("init", new byte[initSize]);
                int i = 0;
                while (true) {
                    try {
                        Thread.sleep(250);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    int use = 50 * 1024*1024;
                    totalUsed += use;
                    map.put("key"+i++, new byte[use]);
                    System.out.println(Thread.currentThread().getName()+"--->>the total used memeory(m)="+totalUsed/1024/1024);
                }
            }
        });
        thread.setName("The-WorkThread");
        thread.start();

        while (true) {
            Thread.sleep(1000);
            System.out.println(Thread.currentThread().getName()+" alive!");
        }

    }

    public static void printRuntimeInfo() {
        long maxMemory = Runtime.getRuntime().maxMemory();
        long totalMemory = Runtime.getRuntime().totalMemory();
        //long freeMemory = Runtime.getRuntime().freeMemory();
        int availableProcessors = Runtime.getRuntime().availableProcessors();

        System.out.println(Thread.currentThread().getName()+"--->>maxMemory(m)="+maxMemory/1024/1024
                +", totalMemory(m)=" +totalMemory/1024/1024+", availableMemory(m)="
                +(maxMemory-totalMemory)/1024/1024 +", availableProcessors="+availableProcessors);
        //log.info("--->>maxMemory="+maxMemory/1024/1024+", totalMemory="
        // +totalMemory/1024/1024+", availableMemory="+(maxMemory-totalMemory)/1024/1024);
    }

}
