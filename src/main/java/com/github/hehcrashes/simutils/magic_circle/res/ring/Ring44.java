package com.github.hehcrashes.simutils.magic_circle.res.ring;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;

public class Ring44 extends Ring{
    public Ring44() {
        super("四分四位点圆环", 4, 4);
    }

    @Override
    public void render(GraphicsContext gc,double r, double cx, double cy, double scale) {
        gc.setStroke(Color.WHITE);
        gc.setFill(Color.WHITE);
        gc.setLineWidth(3);
        gc.strokeOval(cx - (r * scale + 4),cy - (r * scale + 4),2*(r * scale + 4),2*(r * scale + 4));
        gc.strokeOval(cx - (r * scale - 4),cy - (r * scale - 4),2*(r * scale - 4),2*(r * scale - 4));
    }

    @Override
    public void review(GraphicsContext gc) {
        this.render(gc,128,gc.getCanvas().getWidth() / 2,gc.getCanvas().getHeight() / 2,1);
    }
}



