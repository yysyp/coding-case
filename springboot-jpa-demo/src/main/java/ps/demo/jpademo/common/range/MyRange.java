package ps.demo.jpademo.common.range;

import com.google.common.collect.ImmutableMap;
import lombok.*;
import ps.demo.jpademo.common.ImageTool;


import java.awt.*;

@ToString
public class MyRange {

    private int screenWidth;
    private int screenHeight;

    //In full screen coordinate
    private int fx = 0;
    private int fy = 0;

    //In Range coordinate
    private int x = 0;
    private int y = 0;

    private int w;
    private int h;

    public MyRange() {
        Dimension d = ImageTool.getScreenSize();
        this.screenWidth = d.width;
        this.screenHeight = d.height;
        this.w = d.width;
        this.h = d.height;
    }

    public MyRange(Double topPercentCut, Double bottomPercentCut, Double leftPercentCut, Double rightPercentCut) {
        this();
        if (topPercentCut != null) {
            this.fy = (int)(this.screenHeight * topPercentCut);
            this.h -= this.fy;
        }
        if (bottomPercentCut != null) {
            this.h -= (int)(this.screenHeight * bottomPercentCut);
        }
        if (leftPercentCut != null) {
            this.fx = (int) (this.screenWidth * leftPercentCut);
            this.w -= this.fx;
        }
        if (rightPercentCut != null) {
            this.w -= (int) (this.screenWidth * rightPercentCut);
        }
    }

    public int getScreenWidth() {
        return screenWidth;
    }

    public void setScreenWidth(int screenWidth) {
        this.screenWidth = screenWidth;
    }

    public int getScreenHeight() {
        return screenHeight;
    }

    public void setScreenHeight(int screenHeight) {
        this.screenHeight = screenHeight;
    }

    public int getFx() {
        return fx;
    }

    public void setFx(int fx) {
        this.fx = fx;
    }

    public int getFy() {
        return fy;
    }

    public void setFy(int fy) {
        this.fy = fy;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
        this.fx += x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
        this.fy += y;
    }

    public int getW() {
        return w;
    }

    public void setW(int w) {
        this.w = w;
    }

    public int getH() {
        return h;
    }

    public void setH(int h) {
        this.h = h;
    }

    public static MyRange getQuadrant1(int screenWidth, int screenHeight) {
        MyRange r1 = new MyRange();
        quadrantInit(screenWidth, screenHeight, r1);
        r1.fx = r1.w + r1.x;
        r1.fy = r1.y;
        return r1;
    }

    private static void quadrantInit(int screenWidth, int screenHeight, MyRange qr) {
        qr.screenWidth = screenWidth;
        qr.screenHeight = screenHeight;
        qr.x = 0;
        qr.y = 0;
        qr.w = screenWidth / 2;
        qr.h = screenHeight / 2;
    }

    public static MyRange getQuadrant2(int screenWidth, int screenHeight) {
        MyRange r2 = new MyRange();
        quadrantInit(screenWidth, screenHeight, r2);
        r2.fx = r2.w + r2.x;
        r2.fy = r2.h + r2.y;
        return r2;
    }

    public static MyRange getQuadrant3(int screenWidth, int screenHeight) {
        MyRange r3 = new MyRange();
        quadrantInit(screenWidth, screenHeight, r3);
        r3.fx = r3.x;
        r3.fy = r3.h + r3.y;
        return r3;
    }

    public static MyRange getQuadrant4(int screenWidth, int screenHeight) {
        MyRange r4 = new MyRange();
        quadrantInit(screenWidth, screenHeight, r4);
        r4.fx = r4.x;
        r4.fy = r4.y;
        return r4;
    }

    public Rectangle toRectangle() {
        Rectangle rectangle = new Rectangle(x, y, w, h);
        return rectangle;
    }

    public Rectangle toRectangleInFullCoordinate() {
        Rectangle rectangle = new Rectangle(fx, fy, w, h);
        return rectangle;
    }

    public Point toPointInFullCoordinate() {
        Point point = new Point(this.fx, this.fy);
        return point;
    }

}
