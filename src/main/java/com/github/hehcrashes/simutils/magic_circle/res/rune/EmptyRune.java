package com.github.hehcrashes.simutils.magic_circle.res.rune;

import com.github.hehcrashes.simutils.magic_circle.res.ExecutionContext;
import com.github.hehcrashes.simutils.magic_circle.res.ring.Ring;
import javafx.scene.canvas.GraphicsContext;

public class EmptyRune implements Rune{
    @Override
    public String getDisplayName() {
        return "空符文";
    }

    @Override
    public void render(GraphicsContext gc, double cx, double cy, double scale, int cent) {

    }

    @Override
    public void apply(ExecutionContext ctx, Ring ring) {

    }
}
