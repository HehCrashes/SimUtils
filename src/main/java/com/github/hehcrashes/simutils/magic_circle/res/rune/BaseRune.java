package com.github.hehcrashes.simutils.magic_circle.res.rune;

import com.github.hehcrashes.simutils.magic_circle.res.ExecutionContext;
import com.github.hehcrashes.simutils.magic_circle.res.ring.Ring;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.paint.Color;

import static com.github.hehcrashes.simutils.magic_circle.res.ResourceManager.textures;

public class BaseRune implements Rune{
    protected Image getTexture(){
        return textures.get("0001");
    }
    @Override
    public String getDisplayName() {
        return "基类 符文";
    }
    @Override
    public void render(GraphicsContext gc, double R, double cx, double cy, double beginAngle, double allAngle, double scale) {
        PixelReader reader = getTexture().getPixelReader();
        if (reader == null) return;
        int L = (int) getTexture().getWidth();
        int H = (int) getTexture().getHeight();
        double d = Math.toRadians(allAngle) / L;
        for (int x = 0; x < L; x++) {
            double phi = Math.toRadians(beginAngle) + x * d;
            for (int y = 0; y < H; y++) {
                double rr = R + (H - 1 - y) * scale;
                double px = cx + rr * Math.cos(phi);
                double py = cy + rr * Math.sin(phi);
                Color c = reader.getColor(x, y);
                if (c.getOpacity() > 0) {
                    gc.getPixelWriter().setColor((int)Math.round(px), (int)Math.round(py), c);
                }
            }
        }
    }
    @Override
    public void render(GraphicsContext gc, double r, double cx, double cy, double scale) {
        double imgW = getTexture().getWidth();
        double imgH = getTexture().getHeight();
        double scaleP = Math.min(gc.getCanvas().getWidth() / imgW, gc.getCanvas().getHeight() / imgH);
        double drawW = imgW * scaleP;
        double drawH = imgH * scaleP;
        double x = (gc.getCanvas().getWidth() - drawW) / 2;
        double y = (gc.getCanvas().getHeight() - drawH) / 2;
        gc.drawImage(getTexture(), x, y, drawW, drawH);
    }
    @Override
    public void preview(GraphicsContext gc) {
        this.render(gc,128,gc.getCanvas().getWidth() / 2,gc.getCanvas().getHeight() / 2,1);
    }
    @Override
    public void previewArc(GraphicsContext gc) {
        render(gc, 128, gc.getCanvas().getWidth() / 2,gc.getCanvas().getHeight() / 2, -90 ,90,0.15);
    }
    @Override
    public void apply(ExecutionContext ctx, Ring ring) {}
}
