package ps.demo.commonlibx.common;

import java.util.*;

public class MyArgsTool {

    public static Map<String, String> assemberArgs(String[] args) {
        Map<String, String> argsMap = new LinkedHashMap<String, String>();

        if (args != null) {
            for (int i = 0, n = args.length; i < n; i += 2) {
                String key = (args[i] + "").trim().toLowerCase();
                if (i + 1 < args.length) {
                    String value = (args[i + 1] + "").trim();
                    argsMap.put(key, value);
                } else {
                    argsMap.put(key, "");
                }
            }
        }
        return argsMap;
    }

    public static Map<String, List<String>> argsToMap(String[] args) {
        Map<String, List<String>> map = new HashMap<>();
        String key = null;
        for (int i = 0, n = args.length; i < n; i++) {
            String arg = args[i]+"";
            if (arg.startsWith("-")) {
                key = arg;
                map.put(key, null);
            } else {
                List<String> val = map.get(key);
                if (val == null) {
                    val = new ArrayList<>();
                }
                val.add(arg);
                map.put(key, val);
            }
        }
        return map;
    }

}
