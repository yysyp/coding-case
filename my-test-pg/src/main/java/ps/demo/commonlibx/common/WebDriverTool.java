package ps.demo.commonlibx.common;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class WebDriverTool {

    public static void takeScreenshot(WebDriver driver) {
        File File = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
        File imageFile = FileUtilTool.getFileDateDirInHomeDir(driver.getTitle()+".jpeg");
        try {
            FileUtils.copyFile(File, imageFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void takeFullPageScreenShot(WebDriver driver) {
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
            ImageIO.write(result, "png", FileUtilTool.getFileDateDirInHomeDir(driver.getTitle() + ".png"));

        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

}
