package com.github.hehcrashes.simutils.magic_circle.res.marker;

import javafx.scene.canvas.GraphicsContext;

import static com.github.hehcrashes.simutils.magic_circle.controller.Render.strokeRotatedRect;

public class EmptyMarker implements Marker{
    @Override
    public String getDisplayName() {
        return "空标定";
    }

    @Override
    public void render(GraphicsContext gc, double r, double cx, double cy, double scale) {
        strokeRotatedRect(gc,cx,cy,r/2*scale,r/2*scale,0);
        strokeRotatedRect(gc,cx,cy,r/2*scale,r/2*scale,45);
    }

    @Override
    public void execute() {
        System.out.println("这里是空标定ですわ");
    }
}
