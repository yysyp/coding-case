package ps.demo.jpademo.test.webdriverauto;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import io.github.bonigarcia.wdm.WebDriverManager;
import ps.demo.commonlibx.common.SettingTool;
import ps.demo.commonlibx.common.WebDriverTool;

import java.time.Duration;

public class EnterpriseLoginExample {

    private WebDriver driver;
    private WebDriverWait wait;

    public void setup() {
        //Option1: autodownload
        WebDriverManager.chromedriver().setup(); //auto download

        //Option2:
//        String str = System.getProperty("webdriver.chrome.driver");
//        if (StringUtils.isEmpty(str)) {
//            str = new File("c:\\Users\\paul\\Downloads\\chromedriver_win32\\chromedriver.exe").getPath();
//            System.setProperty("webdriver.chrome.driver", str);
//        }

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        // options.addArguments("--headless"); // 生产环境可以使用无头模式

        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));

    }

    public void login(String url, String username, String password) {
        try {
            // 导航到登录页面
            driver.get(url);

            // 等待并输入用户名
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("username")));
            driver.findElement(By.id("username")).sendKeys(username);

            // 输入密码
            driver.findElement(By.id("password")).sendKeys(password);

            // 点击登录按钮
            driver.findElement(By.cssSelector("button[type='submit']")).click();

            // 验证登录是否成功
            wait.until(ExpectedConditions.urlContains("dashboard"));
            System.out.println("登录成功，当前URL: " + driver.getCurrentUrl());

        } catch (TimeoutException e) {
            System.err.println("元素加载超时: " + e.getMessage());
        } catch (NoSuchElementException e) {
            System.err.println("找不到元素: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("登录过程中发生错误: " + e.getMessage());
        }
    }

    public void teardown() {
        if (driver != null) {
            driver.quit();
        }
    }

    public static void main(String[] args) {
        EnterpriseLoginExample example = new EnterpriseLoginExample();
        String password = SettingTool.getConfigByKey("webpass");
        try {
            example.setup();
            WebDriver driver = example.driver;
            driver.get("about:blank;");
            //example.login("https://example.com/login", "your_username", password);
            WebDriverTool.debugInteract(driver);
//            example.driver.get("https://baidu.com");
//            File file = WebDriverTool.takeFullPageScreenShot(driver);
//            System.out.println("file = " + file);
            // 这里可以添加登录后的其他操作...
        } finally {
            example.teardown();
        }
    }




}
