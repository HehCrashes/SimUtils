package com.github.hehcrashes.simutils.magic_circle.controller;

import com.github.hehcrashes.simutils.magic_circle.res.marker.Marker;
import com.github.hehcrashes.simutils.magic_circle.res.ring.Ring;
import com.github.hehcrashes.simutils.magic_circle.res.rune.Rune;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TreeItem;

public class Render {
    static final double r = 128;
    public static void render(TreeItem<NodeData> node, GraphicsContext gc, double cx, double cy, double scale) {
        if (node == null || node.getValue() == null) return;
        NodeData data = node.getValue();

        if (data.type == "RING") {
            Ring ring = (Ring) data.obj;
            ring.render(gc, r, cx, cy, scale);

            int runeIndex = 0;

            double angleStep = Math.PI / ring.getChildSlots();
            double c = Math.cos(angleStep);
            double s = Math.sin(angleStep);
            double x = r * scale, y = 0;
            double nx = x * c - y * s;
            double ny = x * s + y * c;
            x = nx;
            y = ny;
            double FullAngle = (double) 180 / ring.getRuneSlots();

            for (TreeItem<NodeData> child : node.getChildren()) {
                NodeData cd = child.getValue();

                if (cd.type.equals("RING")) {
                    render(child, gc, cx + x, cy + y,scale * 0.4);
                    nx =  (x * c - y * s) * c - (x * s + y * c) * s;
                    ny =  (x * c - y * s) * s + (x * s + y * c) * c;
                    x = nx;
                    y = ny;
                }
                else if (cd.type.equals("RUNE")){
                    Rune rune = (Rune) cd.obj;
                    rune.render(gc,r,cx,cy,FullAngle * runeIndex * 2 - FullAngle,FullAngle*2,scale * 0.15);
                    runeIndex++;
                }
                else if (cd.type.equals("MARKER")) {
                    Marker marker = (Marker) cd.obj;
                    marker.render(gc,r,cx,cy,scale);
                }
            }
        }
    }
    public static void strokeRotatedRect(GraphicsContext gc,double x,double y,double w,double h, double rotateAngle){
        double cosR = Math.cos(Math.toRadians(rotateAngle));
        double sinR = Math.sin(Math.toRadians(rotateAngle));
        double hw = w / 2.0;
        double hh = h / 2.0;
        double[][] vertices = new double[4][2];
        double[] relX = {-hw, hw, hw, -hw};
        double[] relY = {-hh, -hh, hh, hh};
        for (int i = 0; i < 4; i++) {
            double rotatedX = relX[i] * cosR + relY[i] * sinR;
            double rotatedY = -relX[i] * sinR + relY[i] * cosR;
            vertices[i][0] = x + rotatedX;
            vertices[i][1] = y + rotatedY;
        }
        gc.strokeLine(vertices[0][0],vertices[0][1],vertices[1][0],vertices[1][1]);
        gc.strokeLine(vertices[1][0],vertices[1][1],vertices[2][0],vertices[2][1]);
        gc.strokeLine(vertices[2][0],vertices[2][1],vertices[3][0],vertices[3][1]);
        gc.strokeLine(vertices[3][0],vertices[3][1],vertices[0][0],vertices[0][1]);
    }
}



