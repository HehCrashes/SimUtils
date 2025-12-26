package com.github.hehcrashes.simutils.magic_circle.res.ring;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class EmptyRing extends Ring{
    public EmptyRing() {
        super("空子环", 0, 0);
    }
    @Override
    public void render(GraphicsContext gc, double cx, double cy, double scale, int cent) {
        double r = 8 * scale;
        gc.setStroke(Color.WHITE);
        gc.setFill(Color.WHITE);
        gc.setLineWidth(2);
        gc.fillOval(cx-r,cy-r,2*r,2*r);
    }
}
