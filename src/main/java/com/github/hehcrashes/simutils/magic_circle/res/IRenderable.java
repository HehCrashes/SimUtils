package com.github.hehcrashes.simutils.magic_circle.res;

import javafx.scene.canvas.GraphicsContext;

public interface IRenderable {
    void render(GraphicsContext gc,double r, double cx, double cy,double scale);
    void review(GraphicsContext gc);
}

