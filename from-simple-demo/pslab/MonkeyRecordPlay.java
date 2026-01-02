package pslab;

import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import ps.demo.monkey.MkRecordPlayMan;
import ps.demo.monkey.model.MkRecord;
import ps.demo.util.MyArgsUtil;
import ps.demo.util.MyJsonUtil;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
public class MonkeyRecordPlay {

    // Program arguments: file C:\Users\Dell\2022-04-24_152903-mkscript.txt delay 30
    public static void main(String[] args) throws NativeHookException, InterruptedException, IOException {
        Map<String, String> argsMap = MyArgsUtil.assemberArgs(args);
        File mkRecordFile = null;
        String fileStr = argsMap.get("file");
        String delayStr = argsMap.get("delay");
        long delay = 30;
        if (StringUtils.isBlank(fileStr)) {
            fileStr = "mk-record.txt";
            new File(fileStr).createNewFile();
        }
        try {
            delay = Long.parseLong(delayStr);
        } catch (Exception e) {
            e.printStackTrace();
        }

        mkRecordFile = new File(fileStr);

        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    GlobalScreen.unregisterNativeHook();
                } catch (NativeHookException e) {
                    e.printStackTrace();
                }
            }
        }));
        GlobalScreen.registerNativeHook();

        List<MkRecord> records = new ArrayList<>();
        List<String> lines = FileUtils.readLines(mkRecordFile, StandardCharsets.UTF_8);
        for (String line : lines) {
            if (StringUtils.isBlank(line) || line.trim().startsWith("#")) {
                continue;
            }
            try {
                MkRecord mkRecord = MyJsonUtil.jsonString2Object(line, MkRecord.class);
                records.add(mkRecord);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

//        mkRecordList.addAll(MkRecordPlayMan.type(
//                "`1234567890-=" +
//                "\tqwertyuiop[]\\" +
//                "asdfghjkl;'\n" +
//                        "zxcvbnm,./" +
//                        "[ ]"+
//                "~!@#$%^&*()_+"+
//                "QWERTYUIOP{}|" +
//                        "ASDFGHJKL:\"" +
//                        "ZXCVBNM<>?"));

        records.addAll(MkRecordPlayMan.type("You are so good!~"));
        records.addAll(MkRecordPlayMan.ctrlcCtrlv("你们都是好人!"));

        //
        long i = 0;
        for (MkRecord mkRecord : records) {
            i++;
            log.info("Mk processing line[{}]: {}", i, mkRecord);
            GlobalScreen.postNativeEvent(MkRecordPlayMan.getAsNativeEvent(mkRecord));
            Thread.sleep(delay);
        }

        MkRecordPlayMan.robotCtrlcCtrlv("\n你们都是坏人! haha");

        System.exit(0);
    }

}
