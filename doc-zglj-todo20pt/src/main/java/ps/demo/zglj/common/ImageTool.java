package ps.demo.zglj.common;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.io.*;

public class ImageTool {

    public static Dimension getScreenSize() {
        return Toolkit.getDefaultToolkit().getScreenSize();
    }

    public static Rectangle getScreenSizeRectangle() {
        Dimension d = getScreenSize();
        Rectangle rectangle = new Rectangle(0, 0, d.width, d.height);
        return rectangle;

    }

    public static BufferedImage screenShot() {
        Dimension screenSize = getScreenSize();
        int w = (int) screenSize.getWidth();
        int h = (int) screenSize.getHeight();
        Rectangle rectangle = new Rectangle();
        rectangle.setBounds(0, 0, w, h);
        return screenShot(rectangle);
    }

    public static Rectangle getLeftScreenSize(Rectangle r, double percentage) {
        int w = (int) (r.width * percentage);
        Rectangle rectangle = new Rectangle(r.x, r.y, w, r.height);
        return rectangle;
    }

    public static Point convertLeftScreenSizePoint(Point lsp, int lsw, double percentage) {
        Point fsp = new Point();
        fsp.y = lsp.y;
        fsp.x = lsp.x;
        return fsp;
    }

    public static Rectangle getRightScreenSize(Rectangle r, double percentage) {
        int w = (int) (r.width * (1 - percentage));
        Rectangle rectangle = new Rectangle(w, r.y, w, (int) r.height);
        return rectangle;
    }

    public static Point convertRightScreenSizePoint(Point rsp, int rsw, double percentage) {
        Point fsp = new Point();
        fsp.y = rsp.y;
        fsp.x = (int) (rsw + rsp.x);
        return fsp;
    }

    public static Rectangle getUpScreenSize(Rectangle r, double percentage) {
        int h = (int) (r.height * percentage);
        Rectangle rectangle = new Rectangle(0, 0, (int) r.width, h);
        return rectangle;
    }

    public static Point convertUpScreenSizePoint(Point usp, int ush, double percentage) {
        Point fsp = new Point();
        fsp.x = usp.x;
        fsp.y = usp.y;
        return fsp;
    }

    public static Rectangle getBottomScreenSize(Rectangle r, double percentage) {
        int h = (int) (r.height * (1 - percentage));
        Rectangle rectangle = new Rectangle(0, h, (int) r.width, h);
        return rectangle;
    }

    public static Point convertBottomScreenSizePoint(Point bsp, int bsh, double percentage) {
        Point fsp = new Point();
        fsp.x = bsp.x;
        fsp.y = (int) (bsh + bsp.y);
        return fsp;
    }


    public static Rectangle getQuadrant1(Rectangle fr) {
        Rectangle r1 = new Rectangle();
        int w = fr.width / 2;
        r1.x = w;
        r1.y = fr.y;
        r1.width = w;
        r1.height = fr.height / 2;
        return r1;
    }

    public static Point convertQuadrant1Point(Rectangle r1, Point p1) {
        Point fp = new Point();
        fp.y = p1.y;
        fp.x = r1.width + p1.x;
        return fp;
    }

    public static Rectangle getQuadrant2(Rectangle fr) {
        Rectangle r2 = new Rectangle();
        int w = fr.width / 2;
        int h = fr.height / 2;
        r2.x = w;
        r2.y = h;
        r2.width = w;
        r2.height = h;
        return r2;
    }

    public static Point convertQuadrant2Point(Rectangle r2, Point p2) {
        Point fp = new Point();
        fp.y = r2.height + p2.y;
        fp.x = r2.width + p2.x;
        return fp;
    }

    public static Rectangle getQuadrant3(Rectangle fr) {
        Rectangle r3 = new Rectangle();
        int w = fr.width / 2;
        int h = fr.height / 2;
        r3.x = fr.x;
        r3.y = h;
        r3.width = w;
        r3.height = h;
        return r3;
    }

    public static Point convertQuadrant3Point(Rectangle r3, Point p3) {
        Point fp = new Point();
        fp.y = r3.height + p3.y;
        fp.x = p3.x;
        return fp;
    }

    public static Rectangle getQuadrant4(Rectangle fr) {
        Rectangle r4 = new Rectangle();
        int w = fr.width / 2;
        int h = fr.height / 2;
        r4.x = fr.x;
        r4.y = fr.y;
        r4.width = w;
        r4.height = h;
        return r4;
    }

    public static Point convertQuadrant4Point(Rectangle r4, Point p4) {
        Point fp = new Point();
        fp.y = p4.y;
        fp.x = p4.x;
        return fp;
    }

    public static BufferedImage screenShot(Rectangle rectangle) {
        return screenShot(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
    }

    public static BufferedImage screenShot(int x, int y, int width, int height) {
        Robot robot = null;
        try {
            robot = new Robot();
            //Sleep to wait for other actions complete before screenShot.
            Thread.sleep(500);
        } catch (Exception e) {
            e.printStackTrace();
        }
        BufferedImage screenImg = robot.createScreenCapture(new Rectangle(x, y,
                width, height));
        return screenImg;
    }

    public static void screenShot(File file) {
        BufferedImage bufferedImage = screenShot();
        try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
            ImageIO.write(bufferedImage, "png", fileOutputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void screenShot(File file, Rectangle rectangle) {
        BufferedImage bufferedImage = screenShot(rectangle);
        try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
            ImageIO.write(bufferedImage, "png", fileOutputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void save(BufferedImage bi, File file) {
        try {
            ImageIO.write(bi, "png", file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static BufferedImage loadImage(File file) {
        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            return ImageIO.read(fileInputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static float compareImage(BufferedImage biA, BufferedImage biB) {
        float percentage = 0;
        try {
            // take buffer data from both image files //
            DataBuffer dbA = biA.getData().getDataBuffer();
            int sizeA = dbA.getSize();
            DataBuffer dbB = biB.getData().getDataBuffer();
            int sizeB = dbB.getSize();
            int count = 0;
            // compare data-buffer objects //
            if (sizeA == sizeB) {
                for (int i = 0; i < sizeA; i++) {
                    if (dbA.getElem(i) == dbB.getElem(i)) {
                        count = count + 1;
                    }
                }
                percentage = (count * 100) / sizeA;
            } else {
                System.out.println("Both the images are not of same size");
            }
        } catch (Exception e) {
            System.out.println("Failed to compare image files ...");
        }
        return percentage;
    }

    public static BufferedImage imageToBufferedImage(Image im) {
        BufferedImage bi = new BufferedImage
                (im.getWidth(null), im.getHeight(null), BufferedImage.TYPE_INT_RGB);
        Graphics bg = bi.getGraphics();
        bg.drawImage(im, 0, 0, null);
        bg.dispose();
        return bi;
    }

}
