package org.example;

import com.googlecode.lanterna.graphics.TextGraphics;

public abstract class Elements {

    protected Position position;

    public Elements(int x, int y) {
        position = new Position(x, y);
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public abstract void draw(TextGraphics graphics);
}

