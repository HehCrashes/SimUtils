package com.github.hehcrashes.simutils.magic_circle.res.ring;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class EmptyRing extends Ring{
    public EmptyRing() {
        super("空子环", 0, 0);
    }
    @Override
    public void render(GraphicsContext gc, double r, double cx, double cy, double scale) {
        gc.setStroke(Color.WHITE);
        gc.setFill(Color.WHITE);
        gc.setLineWidth(2);
        gc.fillOval(cx - 8*scale,cy - 8*scale,2*8*scale,2*8*scale);
    }
    @Override
    public void review(GraphicsContext gc) {
        this.render(gc,128,gc.getCanvas().getWidth() / 2,gc.getCanvas().getHeight() / 2,1);
    }
}
