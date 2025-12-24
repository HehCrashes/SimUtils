package com.github.hehcrashes.simutils.thermodynamics.Renderer;

import com.github.hehcrashes.simutils.thermodynamics.Block.Block;
import javafx.scene.paint.Color;

public class TinHeatRenderer implements IBlockRenderer {

    @Override
    public Color render(Block b) {
        double T = b.getTemperatureK();

        // 限制范围 [0, 1000]
        T = Math.max(0, Math.min(1000, T));

        if (T <= 200) {
            // 0 → 200 K : 黑色 → 深蓝
            double t = T / 200.0;
            return interpolateColor(Color.BLACK, Color.rgb(0, 0, 150), t);

        } else if (T <= 400) {
            // 200 → 400 K : 深蓝 → 天蓝
            double t = (T - 200) / 200.0;
            return interpolateColor(Color.rgb(0, 0, 150), Color.rgb(80, 160, 255), t);

        } else if (T <= 600) {
            // 400 → 600 K : 天蓝 → 黄色
            double t = (T - 400) / 200.0;
            return interpolateColor(Color.rgb(80, 160, 255), Color.YELLOW, t);

        } else if (T <= 800) {
            // 600 → 800 K : 黄色 → 橙色
            double t = (T - 600) / 200.0;
            return interpolateColor(Color.YELLOW, Color.ORANGE, t);

        } else {
            // 800 → 1000 K : 橙色 → 白色
            double t = (T - 800) / 200.0;
            return interpolateColor(Color.ORANGE, Color.WHITE, t);
        }
    }

    // 线性插值
    private Color interpolateColor(Color c1, Color c2, double t) {
        t = Math.max(0, Math.min(1, t));
        double r = c1.getRed() + (c2.getRed() - c1.getRed()) * t;
        double g = c1.getGreen() + (c2.getGreen() - c1.getGreen()) * t;
        double b = c1.getBlue() + (c2.getBlue() - c1.getBlue()) * t;
        return new Color(r, g, b, 1.0);
    }
}
