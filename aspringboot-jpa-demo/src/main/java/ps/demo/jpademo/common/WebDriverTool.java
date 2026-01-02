package ps.demo.jpademo.common;

import com.alibaba.excel.util.StringUtils;
import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;


@Slf4j
public class WebDriverTool {

    public static WebDriver initWebDriver(String driverExePath) {
        WebDriver driver = null;
        //private WebDriverWait wait;
        if (StringUtils.isBlank(driverExePath)) {
            WebDriverManager.chromedriver().setup(); //auto download
        } else {
            System.setProperty("webdriver.chrome.driver", driverExePath);
        }

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        // options.addArguments("--headless"); // 生产环境可以使用无头模式

        WebDriver webDriver =  new ChromeDriver(options);
        //wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        webDriver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        return webDriver;

    }

    public static File takeScreenshot(WebDriver driver) {
        File File = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
        File imageFile = FileUtilTool.getFileDateDirInHomeDir(driver.getTitle()+".jpeg");
        try {
            FileUtils.copyFile(File, imageFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return imageFile;
    }

    public static File takeFullPageScreenShot(WebDriver driver) {
        try {
            JavascriptExecutor jsExec = (JavascriptExecutor) driver;
            jsExec.executeScript("window.scrollTo(0, 0);");
            Long innerHeight = (Long) jsExec.executeScript("return window.innerHeight;");
            Long scroll = innerHeight;
            Long scrollHeight = (Long) jsExec.executeScript("return document.body.scrollHeight;");

            scrollHeight = scrollHeight + scroll;

            List<byte[]> images = new ArrayList<>();

            do {
                byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
                images.add(screenshot);
                jsExec.executeScript("window.scrollTo(0, " + innerHeight + ");");
                innerHeight = innerHeight + scroll;
            } while (scrollHeight >= innerHeight);
            jsExec.executeScript("window.scrollTo(0, 0);");

            BufferedImage result = null;
            Graphics g = null;

            int gapFixing = 5;
            int lastHightSeek = (int) (scroll - scrollHeight % scroll - gapFixing);

            int x = 0, y = 0;
            for (int i = 0, len = images.size(); i < len; i++) {
                byte[] image = images.get(i);
                boolean isLast = i + 1 >= len;
                if (isLast) {
                    y -= lastHightSeek;
                }
                InputStream is = new ByteArrayInputStream(image);
                BufferedImage bi = ImageIO.read(is);
                if (result == null) {
                    // Lazy init so we can infer height and width
                    result = new BufferedImage(
                            bi.getWidth(), bi.getHeight() * images.size() - lastHightSeek,
                            BufferedImage.TYPE_INT_RGB);
                    g = result.getGraphics();
                }
                g.drawImage(bi, x, y, null);
                y += bi.getHeight();
            }
            File file = FileUtilTool.getFileDateDirInHomeDir(FileUtilTool.toValidFileName(driver.getTitle() + ".png"));
            ImageIO.write(result, "png", file);
            return file;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public static void debugInteract(WebDriver webDriver) {
        //((JavascriptExecutor)webDriver).executeScript("window.alert('IDE console debug!')");
//        try {
//            Thread.sleep(3000);
//            Alert alert = webDriver.switchTo().alert();
//            System.out.println("alert text: " + alert.getText());
//            alert.accept();
//        } catch (Exception e) {
//        }
        String input = StringXTool.readLineFromSystemIn("Input[open:url || [click|text]:[id|name|xpath|plink]]:xxx to continue: ").trim();

        if (input.toLowerCase().startsWith("by:")) {
            String identityEtc = input.substring("by:".length());
            List<WebElement> webElements = getWebElements(webDriver, identityEtc);
            if (webElements == null || webElements.isEmpty()) {
                log.info("By {} Not find anything!", identityEtc);
            } else {
                printWebElements(webElements);
            }
        } else if (input.toLowerCase().startsWith("open:")) {
            String url = input.substring("open:".length());
            webDriver.get(url);
        } else if (input.toLowerCase().startsWith("click:")) {
            String identityEtc = input.substring("click:".length());
            WebElement webElement = getWebElement(webDriver, identityEtc);
            log.info("Click webElement : " + webElement);
            if (webElement != null) {
                webElement.click();
            }
        } else if (input.toLowerCase().startsWith("text:")) {
            String identityEtc = input.substring("text:".length());
            WebElement webElement = getWebElement(webDriver, identityEtc);
            log.info("Text webElement : " + webElement);
            if (webElement != null) {
                webElement.sendKeys("1234");
            }
        } else if (input.toLowerCase().startsWith("javascript:")) {
            try {
                ((JavascriptExecutor) webDriver).executeScript(input.substring("javascript:".length()));
            } catch (Exception e) {
                log.info(e.getMessage());
            }
        }

        if (!input.toLowerCase().startsWith("exit") && !input.toLowerCase().startsWith("q")) {
            WebDriverTool.debugInteract(webDriver);
        }
    }

    public static void printWebElements(Collection<WebElement> elements) {
        if (elements == null) {
            log.info("Null");
        }
        int i = 0;
        for (WebElement e : elements) {
            log.info("<{}>: TAG: {}, TEXT: {}", i++, e.getTagName(), e.getText());
        }
    }

    public static WebElement getWebElement(WebDriver webDriver, String identityEtc) {
        WebElement webElement = null;
        try {
            if (identityEtc.startsWith("id:")) {
                webElement = webDriver.findElement(By.id(identityEtc.substring("id:".length())));
            } else if (identityEtc.startsWith("class:")) {
                webElement = webDriver.findElement(By.className(identityEtc.substring("class:".length())));
            } else if (identityEtc.startsWith("name:")) {
                webElement = webDriver.findElement(By.name(identityEtc.substring("name:".length())));
            } else if (identityEtc.startsWith("xpath:")) {
                webElement = webDriver.findElement(By.xpath(identityEtc.substring("xpath:".length())));
            } else if (identityEtc.startsWith("plink:")) {
                webElement = webDriver.findElement(By.partialLinkText(identityEtc.substring("plink:".length())));
            }
        } catch (Exception e) {}
        return webElement;
    }

    public static List<WebElement> getWebElements(WebDriver webDriver, String identityEtc) {
        List<WebElement> webElements = null;
        try {
            if (identityEtc.startsWith("id:")) {
                webElements = webDriver.findElements(By.id(identityEtc.substring("id:".length())));
            } else if (identityEtc.startsWith("class:")) {
                webElements = webDriver.findElements(By.className(identityEtc.substring("class:".length())));
            } else if (identityEtc.startsWith("name:")) {
                webElements = webDriver.findElements(By.name(identityEtc.substring("name:".length())));
            } else if (identityEtc.startsWith("xpath:")) {
                webElements = webDriver.findElements(By.xpath(identityEtc.substring("xpath:".length())));
            } else if (identityEtc.startsWith("plink:")) {
                webElements = webDriver.findElements(By.partialLinkText(identityEtc.substring("plink:".length())));
            }
        } catch (Exception e) {}
        return webElements;
    }

    public static WebElement findMostMatch(List<WebElement> webElements, String tag, String text) {
        for (WebElement webElement : webElements) {
            if (webElement.getTagName().equalsIgnoreCase(tag.trim())
            && webElement.getText().trim().equalsIgnoreCase(text.trim())) {
                return webElement;
            }
        }
        return null;
    }

    public static List<WebElement> findElementsByTextContains(WebDriver webDriver, String tag, String text) {
        if (StringUtils.isBlank(tag)) {
            return webDriver.findElements(By.xpath("//*[contains(normalize-space(), '" + text + "')]"));
        } else {
            return webDriver.findElements(By.xpath("//"+tag+"[contains(normalize-space(), '" + text + "')]"));
        }
    }

    public static WebElement waitElementsByTextContains(WebDriver webDriver, By by) {
        WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(30));
        WebElement element = wait.until(
                ExpectedConditions.visibilityOfElementLocated(by)
                );
        return element;
    }

    public static List<WebElement> findByClassNames(WebDriver webDriver, String ... classNames) {
        String xpression = "//*[";
        for (int i = 0, n = classNames.length; i < n; i++) {
            xpression += "contains(@class, '"+classNames[i]+"')";
            if (i + 1 < n) {
                xpression += " and ";
            }
        }
        xpression += "]";

        List<WebElement> webElements = webDriver.findElements(
                By.xpath(xpression)
        );
        return webElements;
    }

    public static void clickAlert(WebDriver webDriver) {
        try {
            Alert alert = webDriver.switchTo().alert();
            System.out.println("alert text: " + alert.getText());
            alert.accept();
        } catch (NoAlertPresentException e) {
            e.printStackTrace();
        }
    }


}
