package com.github.hehcrashes.simutils.magic_circle.res;

import javafx.scene.canvas.GraphicsContext;

public interface IRenderableArc {
    void render(GraphicsContext gc, double r, double cx, double cy, double beginAngle, double allAngle, double scale);
    void previewArc(GraphicsContext gc);
}
