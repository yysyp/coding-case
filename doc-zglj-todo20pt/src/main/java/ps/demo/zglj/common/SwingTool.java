package ps.demo.zglj.common;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

public class SwingTool {

    static ChromeDriver driver = null;
    static JTextArea inputArea = null;
    static JTextArea msgArea = null;

    final static Long WAIT_TIME = 2000L;
    final static ScriptEngineManager engineManager = new ScriptEngineManager();
    final static ScriptEngine engine = engineManager.getEngineByName("Nashorn");

    public static void main(String[] args) {
        MyFrame myFrame = new MyFrame();
        myFrame.setAlwaysOnTop(true);
        myFrame.setVisible(true);
    }

    /**
     * ============ Public Methods For Javascript Invocation Begin ============
     **/
    public static final String TIP =
            "\n/* var st = Java.type(\"" + SwingTool.class.getName() + "\");\n"
            +"st.open('url');\n"
            +"st.back();\n"
            +"st.msg('hi');\n"
            +"st.screenshot();\n"
            +"st.pageSource();\n"
            +"st.save(st.getX('/html/body').getText());\n"
            +"st.ss();\n"
            +"st.sleep(ms);\n"
            +"st.getX('xpath').sendKeys('kw\\ue007');\n"
            +"st.linkHas('text').click();\n"
            +"st.js(\"let my_a_list1 = document.getElementsByTagName('a'); [...my_a_list1].map(a => {a.setAttribute('target', '_self')});\");\n"
            +"st.clickById('id');\n"
            +"st.clickByName('name');\n"
            +"st.clickX('xpath');\n"
            +"st.textById('id', 'kw');\n"
            +"st.textByName('name', 'kw');\n"
            +"st.textX('xpath', 'kw');\n"
            +"st.exit(); */\n";

    public static void msg(String message) {
        msg(message, false);
    }

    public static void msg(String message, boolean append) {
        String msg = "[" + TimeTool.getNowStr("HH:mm:ss.SSS") + "] "
                + message + System.lineSeparator();
        if (append) {
            msgArea.append(msg);
        } else {
            msgArea.setText(msg);
        }
    }

    public static void open(String url) {
        driver.get(url);
    }

    public static void back() {
        driver.navigate().back();
    }

    public static void exit() {
        msg("Going to exit.", false);
        System.exit(0);
    }

    public static void screenshot() {
        try {
            sleep(WAIT_TIME);
            WebDriverTool.takeFullPageScreenShot(driver);
        } catch (Exception e) {
            msg(e.getMessage());
        }
    }

    public static void pageSource() {
        sleep(WAIT_TIME);
        File html = FileUtilTool.getFileDateDirInHomeDir(driver.getTitle() + ".html");
        ReadWriteTool.writeFileContent(html, driver.getPageSource());
    }

    public static void save(String text) {
        sleep(WAIT_TIME);
        File txt = FileUtilTool.getFileDateDirInHomeDir(driver.getTitle() + ".txt");
        ReadWriteTool.writeFileContent(txt, text);
    }

    public static Object js(String script, Object... args) {
        sleep(WAIT_TIME);
        Object object = driver.executeScript(script, args);
        msg(object+"");
        return object;
    }

    public static void sleep(long ms) {
        try {
            Thread.sleep(ms);
            driver.getPageSource();
        } catch (InterruptedException e) {
            msg(e.getMessage());
        }
    }

    public static void ss() {
        try {
            sleep(WAIT_TIME);
            WebDriverTool.takeScreenshot(driver);
        } catch (Exception e) {
            msg(e.getMessage());
        }
    }

    public static WebElement getByXpath(String xpath) {
        WebElement webElement = driver.findElement(By.xpath(xpath));
        msg(webElement+"");
        //webElement.getText();
        return webElement;
    }
//
    public static WebElement getLinkHas(String text) {
        WebElement webElement = driver.findElement(By.partialLinkText(text));
        msg(webElement+"");
        return webElement;
    }

    public static void clickById(String id) {
        new WebDriverWait(driver, Duration.ofSeconds(30)).until(ExpectedConditions.elementToBeClickable(By.id(id)));
        WebElement idElem = driver.findElement(By.id(id));
        idElem.click();
    }

    public static void clickByName(String name) {
        new WebDriverWait(driver, Duration.ofSeconds(30)).until(ExpectedConditions.elementToBeClickable(By.name(name)));
        WebElement nameElem = driver.findElement(By.name(name));
        nameElem.click();
    }

    public static void clickByXpath(String xpath) {
        By by = By.xpath(xpath);
        new WebDriverWait(driver, Duration.ofSeconds(30)).until(ExpectedConditions.elementToBeClickable(by));
        driver.findElement(by).click();
    }

    public static void textById(String id, String text) {
        WebElement idElem = driver.findElement(By.id(id));
        idElem.clear();
        idElem.sendKeys(text);
        idElem.sendKeys(Keys.ENTER);
    }

    public static void textByName(String name, String text) {
        WebElement nameElem = driver.findElement(By.name(name));
        nameElem.clear();
        nameElem.sendKeys(text);
        nameElem.sendKeys(Keys.ENTER);
    }

    public static void textByXpath(String xpath, String text) {
        WebElement webElement = getByXpath(xpath);
        webElement.clear();
        webElement.sendKeys(text);
        webElement.sendKeys(Keys.ENTER);
    }

    /**
     * ============ Public Methods For Javascript Invocation End ============
     **/

    public static void evalScript() {
        String scriptText = inputArea.getText();
        try {
            Object result = engine.eval(scriptText);
            msg(result + "");
        } catch (ScriptException e) {
            msg(e.getMessage());
        }
    }

    public static void openChrome(String url) {
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("start-maximized");
        //chromeOptions.addArguments("disable-infobars");
        //chromeOptions.addArguments("--disable-extensions");
        ChromeDriverService driverService = ChromeDriverService.createDefaultService();
        driver = (ChromeDriver) WebDriverTool.initWebDriver(null);
        driver.get(url);
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
//        driver.executeScript("let my_a_list1 = document.getElementsByTagName('a'); " +
//                "[...my_a_list1].map(a => {a.setAttribute('target', '_self')});");
    }

    public static class MyFrame extends JFrame {

        public MyFrame() {
            setTitle("simple-demo");
            setBounds(0, 0, 800, 600);
            setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    e.getWindow().dispose();
                    if (driver != null) {
                        driver.quit();
                    }
                }
            });
            final JToolBar toolBar = new JToolBar();
            getContentPane().add(toolBar, BorderLayout.NORTH);
            final JButton btn_open = new JButton();
            btn_open.setText("Open Chrome");
            btn_open.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    msg("Open Chrome...");
                    try {
                        openChrome("https://cn.bing.com/");
                    } catch (Exception ex) {
                        msg(ex.getMessage(), true);
                    }
                }
            });
            toolBar.add(btn_open);
            final JButton btn_execute = new JButton();
            btn_execute.setText(" Execute ");
            btn_execute.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    msg("Execute...");
                    evalScript();
                }
            });
            toolBar.add(btn_execute);
            final JButton btn_pause = new JButton();
            btn_pause.setText(" Tips ");
            btn_pause.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    msg(TIP);
                    inputArea.append(TIP);
                }
            });
            toolBar.add(btn_pause);
            final JButton btn_screenshot = new JButton();
            btn_screenshot.setText("Take [S]creenshot");
            btn_screenshot.setMnemonic('S');
            btn_screenshot.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    msg("Take Screenshot...");
                    try {
                        WebDriverTool.takeFullPageScreenShot(driver);
                        msg(" screenshot taken.", true);
                    } catch (Exception ex) {
                        msg(ex.getMessage(), true);
                    }
                }
            });
            toolBar.add(btn_screenshot);

            final JButton btn_saveTxt = new JButton();
            btn_saveTxt.setText("[D]ownload Text");
            btn_saveTxt.setMnemonic('D');
            btn_saveTxt.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    msg("Download text...");
                    try {
                        save(SwingTool.getByXpath("/html/body").getText());
                    } catch (Exception ex) {
                        msg(ex.getMessage());
                    }
                }
            });
            toolBar.add(btn_saveTxt);

            final JScrollPane scrollPane = new JScrollPane();
            inputArea = new JTextArea();
            inputArea.setLineWrap(true);
            Font font = new Font("Default", Font.BOLD,18);
            inputArea.setFont(font);
            inputArea.setText("var st = Java.type(\"" + SwingTool.class.getName() + "\");\n");
            inputArea.setToolTipText(TIP);
            scrollPane.setViewportView(inputArea);
            getContentPane().add(scrollPane, BorderLayout.CENTER);

            final JScrollPane scrollPane2 = new JScrollPane();
            msgArea = new JTextArea();
            msgArea.setLineWrap(true);
            msgArea.setBackground(Color.lightGray);
            msgArea.setRows(2);
            String str = System.getProperty("webdriver.chrome.driver");
            if (StringUtils.isEmpty(str)) {
//                str = FileUtilTool.getFileInHomeDir("chromedriver.exe").getPath();
//                System.setProperty("webdriver.chrome.driver", str);
                WebDriverManager.chromedriver().setup();
            }
            msg("webdriver.chrome.driver=" + str);
            scrollPane2.setViewportView(msgArea);
            getContentPane().add(scrollPane2, BorderLayout.SOUTH);
        }

    }

}
