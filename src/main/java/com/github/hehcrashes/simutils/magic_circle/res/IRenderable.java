package com.github.hehcrashes.simutils.magic_circle.res;

import javafx.scene.canvas.GraphicsContext;

public interface IRenderable {
    void render(GraphicsContext gc, double cx, double cy,double scale, int cent);
}
