package com.github.hehcrashes.simutils.magic_circle.controller;

import com.github.hehcrashes.simutils.magic_circle.res.marker.Marker;
import com.github.hehcrashes.simutils.magic_circle.res.ring.Ring;
import com.github.hehcrashes.simutils.magic_circle.res.rune.Rune;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TreeItem;

public class Render {
    public static void render(TreeItem<NodeData> node, GraphicsContext gc, double cx, double cy, double scale) {
        if (node == null || node.getValue() == null) return;
        NodeData data = node.getValue();

        if (data.type == "RING") {
            Ring ring = (Ring) data.obj;
            ring.render(gc, cx, cy, scale, 1);

            int runeIndex = 0;

            double angleStep = Math.PI / ring.getChildSlots();
            double c = Math.cos(angleStep);
            double s = Math.sin(angleStep);
            double x = 128 * scale, y = 0;
            double nx = x * c - y * s;
            double ny = x * s + y * c;
            x = nx;
            y = ny;

            for (TreeItem<NodeData> child : node.getChildren()) {
                NodeData cd = child.getValue();

                if (cd.type.equals("RING")) {

                    render(child, gc, cx + x, cy + y,scale*0.4);

                    nx =  (x * c - y * s) * c - (x * s + y * c) * s;
                    ny =  (x * c - y * s) * s + (x * s + y * c) * c;
                    x = nx;
                    y = ny;
                }
                else if (cd.type.equals("RUNE")){

                    runeIndex++;
                }
                else if (cd.type.equals("MARKER")) {

                }
            }
        } else {

        }
    }
}



