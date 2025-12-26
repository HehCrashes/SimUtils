package com.github.hehcrashes.simutils.magic_circle.res.marker;

import javafx.scene.canvas.GraphicsContext;

public class EmptyMarker implements Marker{
    @Override
    public String getDisplayName() {
        return "空标定";
    }

    @Override
    public void render(GraphicsContext gc, double cx, double cy, double scale, int cent) {

    }

    @Override
    public void execute() {
        System.out.println("这里是空标定ですわ");
    }
}
