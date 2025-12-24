package com.github.hehcrashes.simutils.thermodynamics.Renderer;

import com.github.hehcrashes.simutils.thermodynamics.Block.Block;
import javafx.scene.paint.Color;

public class HeatRenderer implements IBlockRenderer {
    @Override
    public Color render(Block b) {
        double T = b.getTemperatureK();

        double minK = 0;
        double zeroC = 273.15;
        double roomK = 273.15 + 20;
        double meltK = b.melting_temperature;
        double boilK = b.boiling_temperature;

        if (T <= zeroC) {
            // 0K -> 黑色, 0°C -> 蓝色
            double t = T / zeroC;
            return interpolateColor(Color.BLACK, Color.BLUE, t);
        } else if (T <= roomK) {
            // 0°C -> 蓝色, 20°C -> 淡暖蓝（偏接近水蓝色/人体感受）
            double t = (T - zeroC) / (roomK - zeroC);
            Color coldBlue = Color.BLUE;
            Color warmBlue = Color.rgb(100, 180, 255); // 自然偏暖
            return interpolateColor(coldBlue, warmBlue, t);
        } else if (T <= meltK) {
            // 室温 -> 熔点, 蓝色偏暖 -> 红色
            double t = (T - roomK) / (meltK - roomK);
            Color warmBlue = Color.rgb(100, 180, 255);
            return interpolateColor(warmBlue, Color.RED, t);
        } else {
            // 熔点 -> 沸点, 红色 -> 白色
            double t = (T - meltK) / (boilK - meltK);
            return interpolateColor(Color.RED, Color.WHITE, t);
        }
    }

    // 线性插值
    private Color interpolateColor(Color c1, Color c2, double t) {
        t = Math.max(0, Math.min(1, t));
        double r = c1.getRed() + (c2.getRed() - c1.getRed()) * t;
        double g = c1.getGreen() + (c2.getGreen() - c1.getGreen()) * t;
        double b = c1.getBlue() + (c2.getBlue() - c1.getBlue()) * t;
        return new Color(r,g,b,1.0);
    }


}

