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
    public void render(GraphicsContext gc, double r, double cx, double cy, double scale) {

    }
    @Override
    public void review(GraphicsContext gc) {
        this.render(gc,128,gc.getCanvas().getWidth() / 2,gc.getCanvas().getHeight() / 2,1);
    }
}
