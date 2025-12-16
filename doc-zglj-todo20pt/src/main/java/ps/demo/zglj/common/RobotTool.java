package ps.demo.zglj.common;

import org.apache.commons.lang3.RandomUtils;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

public class RobotTool {

    public static long defaultDelay = 50;
    public static Robot robot = null;

    static {
        try {
            robot = new Robot();
            //robot.setAutoDelay((int) defaultDelay);
            //robot.setAutoWaitForIdle(true);
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    public static void delay() {
        robot.delay((int) defaultDelay);
    }

    public static void ctrlcCtrlv(String str) {
        StringSelection stringSelection = new StringSelection(str);
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);
        try {
            robot.keyPress(KeyEvent.VK_CONTROL);
            robot.keyPress(KeyEvent.VK_V);
            delay();
            robot.keyRelease(KeyEvent.VK_V);
            robot.keyRelease(KeyEvent.VK_CONTROL);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String ctrlcCtrlv() {
        try {
            robot.keyPress(KeyEvent.VK_CONTROL);
            robot.keyPress(KeyEvent.VK_C);
            delay();
            robot.keyRelease(KeyEvent.VK_C);
            robot.keyRelease(KeyEvent.VK_CONTROL);
            return (String) Toolkit.getDefaultToolkit().getSystemClipboard()
                    .getData(DataFlavor.stringFlavor);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static void typeEnter() {
        type('\n');
    }

    public static void typeTab() {
        type('\t');
    }

    public static void type(char c) {
        switch (c) {
            case '\n':
                robot.keyPress(KeyEvent.VK_ENTER);
                delay();
                robot.keyRelease(KeyEvent.VK_ENTER);
            case '\t':
                robot.keyPress(KeyEvent.VK_TAB);
                delay();
                robot.keyRelease(KeyEvent.VK_TAB);
                return;
        }
    }

    public static void typeByVk(int vk) {
        robot.keyPress(vk);
        delay();
        robot.keyRelease(vk);
    }

    public static void typeByVks(int... keycode) {
        for (int i = 0; i < keycode.length; i++) {
            robot.keyPress(keycode[i]);
            robot.delay(50);
        }

        for (int i = keycode.length - 1; i >= 0; i--) {
            robot.keyRelease(keycode[i]);
            robot.delay(50);
        }
    }

    public static void sendAlphabeticKeys(String keys) {
        for (char c : keys.toCharArray()) {
            int keyCode = KeyEvent.getExtendedKeyCodeForChar(c);
            if (KeyEvent.CHAR_UNDEFINED == keyCode) {
                throw new RuntimeException("Key code not found for '" + c + "'");
            }
            if (Character.isUpperCase(c)) {
                robot.keyPress(KeyEvent.VK_SHIFT);
            }
            robot.keyPress(keyCode);
            robot.delay(robot.getAutoDelay());
            robot.keyRelease(keyCode);
            if (Character.isUpperCase(c)) {
                robot.keyRelease(KeyEvent.VK_SHIFT);
            }
            robot.delay(robot.getAutoDelay());
        }
    }

    public static void click() {
        click(InputEvent.BUTTON1_MASK);
    }

    public static void click(int button) {
        robot.mousePress(button);
        delay();
        robot.mouseRelease(button);
    }

    public static void click(Point p, double scalingRatio) {
        click(p, scalingRatio, InputEvent.BUTTON1_MASK);
    }

    public static void click(Point p, double scalingRatio, int button) {
        moveTo(p, scalingRatio);
        delay();
        click(button);
    }

    public static void doubleClick() {
        click();
        delay();
        delay();
        delay();
        delay();
        delay();
        click();
    }

    public static void rightClick() {
        click(InputEvent.BUTTON3_MASK);
    }

    public static void rightClick(Point p, double scalingRatio) {
        click(p, scalingRatio, InputEvent.BUTTON3_MASK);
    }

    public static void wheel(int wheelAmt) {
        delay();
        robot.mouseWheel(wheelAmt);
    }

    public static void moveTo(Point p, double scalingRatio) {
        delay();
        robot.mouseMove(-1, -1);
        delay();
        robot.mouseMove((int) (p.x / scalingRatio), (int) (p.y / scalingRatio));
    }

    public static void cursorRandomMove(int stepSize, double scalingRatio) {
        Point cp = MouseInfo.getPointerInfo().getLocation();
        Dimension d = ImageTool.getScreenSize();
        int loopCount = 0;
        int stepx = RandomUtils.nextInt(1, stepSize);
        if (RandomUtils.nextBoolean()) {
            stepx = -stepx;
        }
        int x = cp.x + stepx;
        int stepy = RandomUtils.nextInt(1, stepSize);
        if (RandomUtils.nextBoolean()) {
            stepy = -stepy;
        }
        int y = cp.y + stepy;
        while (x < 0 || x > d.width || y < 0 || y > d.height) {
            if (loopCount++ > 100) {
                return;
            }
            stepx = RandomUtils.nextInt(1, stepSize);
            if (RandomUtils.nextBoolean()) {
                stepx = -stepx;
            }
            x = cp.x + stepx;
            stepy = RandomUtils.nextInt(1, stepSize);
            if (RandomUtils.nextBoolean()) {
                stepy = -stepy;
            }
            y = cp.y + stepy;
        }
        robot.mouseMove(x, y);
    }

    public Image getImageFromClipboard() {
        Transferable transferable = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);
        if (transferable != null && transferable.isDataFlavorSupported(DataFlavor.imageFlavor)) {
            try {
                return (Image) transferable.getTransferData(DataFlavor.imageFlavor);
            } catch (Exception e) {
                return null;
            }
        } else {
            return null;
        }
    }

    public String getTextFromClipboard() {
        Transferable transferable = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);
        if (transferable != null && transferable.isDataFlavorSupported(DataFlavor.stringFlavor)) {
            try {
                return (String) transferable.getTransferData(DataFlavor.stringFlavor);
            } catch (Exception e) {
                return "";
            }
        } else {
            return "";
        }
    }

}
