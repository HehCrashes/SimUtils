package com.github.hehcrashes.simutils.magic_circle;

import javafx.scene.canvas.GraphicsContext;

@FunctionalInterface
public interface IPreviewer<T> {
    void preview(T res, GraphicsContext gc);
}
