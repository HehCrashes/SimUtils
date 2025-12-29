package com.github.hehcrashes.simutils.magic_circle.res.rune;

import com.github.hehcrashes.simutils.magic_circle.res.ExecutionContext;
import com.github.hehcrashes.simutils.magic_circle.res.ring.Ring;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;

public class EmptyRune implements Rune{
    @Override
    public String getDisplayName() {
        return "空符文";
    }

    @Override
    public void render(GraphicsContext gc, double r, double cx, double cy, double beginAngle, double allAngle, double scale) {
        gc.setStroke(Color.WHITE);
        gc.setFill(Color.WHITE);
        gc.setLineWidth(2);
        gc.strokeArc(cx - r*scale, cy - r*scale, 2*r*scale, 2*r*scale, beginAngle, allAngle, ArcType.OPEN);
    }

    @Override
    public void apply(ExecutionContext ctx, Ring ring) {

    }
}
