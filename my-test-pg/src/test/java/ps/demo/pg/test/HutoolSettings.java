package ps.demo.pg.test;

import cn.hutool.setting.Setting;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HutoolSettings {
    /**
     * ignore/setting.txt:
     *
     * [dev]
     * userName=xiaoming
     *
     */

    //
    public static void main(String[] args) {
        cn.hutool.setting.Setting setting = new Setting("./ignore/setting.txt");
        String devGroup = "dev";
        String userName = setting.getByGroup("userName", devGroup);
        log.info("userName={}", userName);

    }
}
