package com.teamresourceful.resourcefullib.common.color;

public class MutableColor extends Color {
    MutableColor(String name, int color) {
        super(color);
        this.specialName = name;
    }

    public void setRed(int r) {
        this.r = r;
    }

    public void setGreen(int g) {
        this.g = g;
    }

    public void setBlue(int b) {
        this.b = b;
    }
}