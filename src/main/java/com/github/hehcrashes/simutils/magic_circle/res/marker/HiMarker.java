package com.github.hehcrashes.simutils.magic_circle.res.marker;

import javafx.scene.canvas.GraphicsContext;

public class HiMarker implements Marker{

    @Override
    public void execute() {
        System.out.println("Hi！标定运行！");
    }

    @Override
    public String getDisplayName() {
        return "Hi 标定";
    }

    @Override
    public void render(GraphicsContext gc, double cx, double cy, double scale, int cent) {

    }
}
