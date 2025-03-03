package co.edu.uptc.view;

import java.awt.Image;

public class CardView {

    private int x;
    private int y;
    private int width;
    private int height;
    private Image image;
    private boolean paint;

    public CardView(int x, int y, int width, int height, Image image) {
        paint = false;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.image = image;
    }

    public CardView(int x, int y, int width, int height) {
        paint = false;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public boolean isPaint() {
        return paint;
    }

    public void setPaint(boolean paint) {
        this.paint = paint;
    }

    @Override
    public String toString() {
        return "CardView [x=" + x + ", y=" + y + ", width=" + width + ", height=" + height + ", paint=" + paint + "]";
    }

}
