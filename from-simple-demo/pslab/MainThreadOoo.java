package pslab;

import java.util.ArrayList;
import java.util.List;

public class MainThreadOoo {

    public static void main(String[] args) throws InterruptedException {
        List<Object> addMem = new ArrayList<>();
        int size = 50 * 1024*1024;
        long used = 0;
        while (true) {
            addMem.add(new byte[size]);
            used += size;
            System.out.println("Used up mem(m)="+used/1024/1024);
            Thread.sleep(500);
        }

    }

}
