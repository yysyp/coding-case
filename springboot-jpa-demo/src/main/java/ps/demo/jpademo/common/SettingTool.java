package ps.demo.jpademo.common;

import cn.hutool.setting.Setting;
import com.alibaba.excel.util.StringUtils;

public class SettingTool {

    public static String getConfigByKey(String key) {
        return getConfigByGroupAndKey(null, key);
    }

    public static String getConfigByGroupAndKey(String group, String key) {
        return getConfigByGroupAndKey("./ignore/setting.conf", group, key);
    }

    public static String getConfigByGroupAndKey(String file, String group, String key) {
        Setting setting = new Setting(file);
        if (StringUtils.isNotBlank(group)) {
            return setting.get(group, key);
        }
        return setting.get(key);
    }

}
