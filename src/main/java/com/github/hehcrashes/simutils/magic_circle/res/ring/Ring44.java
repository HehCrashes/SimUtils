package com.github.hehcrashes.simutils.magic_circle.res.ring;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;

public class Ring44 extends Ring{
    public Ring44() {
        super("四分六位点圆环", 4, 6);
    }

    @Override
    public void render(GraphicsContext gc, double cx, double cy, double scale, int cent) {
        double r = 128 * scale;
        double cr = 8 * scale;
        gc.setStroke(Color.WHITE);
        gc.setFill(Color.WHITE);

        // 环
        gc.setLineWidth(3);
        gc.strokeOval(cx - (r + 4),cy - (r + 4),2*(r + 4),2*(r + 4));
        gc.strokeOval(cx - (r - 4),cy - (r - 4),2*(r - 4),2*(r - 4));
/*
        //位点
        gc.setLineWidth(2);
        double angleStep = Math.PI / childSlots;
        double c = Math.cos(angleStep);
        double s = Math.sin(angleStep);
        double x = r, y = 0;
        for (int k = 0; k < childSlots * 2; k++) {
            if((k & 1) == 1){
                gc.fillOval(cx + x - cr,cy + y - cr,2*cr,2*cr);
            }
            double nx =  x * c - y * s;
            double ny =  x * s + y * c;
            x = nx;
            y = ny;
        }

        //符文
        double FullAngle = (double) 180 / runeSlots;
        for (int k = 0; k < runeSlots * 2; k++) {
            if((k & 1) != 1){
                gc.strokeArc(cx - r, cy - r, 2*r, 2*r, FullAngle * k - FullAngle/2, FullAngle, ArcType.OPEN);
            }
        }
*/

    }
}



