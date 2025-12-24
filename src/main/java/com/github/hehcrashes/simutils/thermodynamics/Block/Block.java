package com.github.hehcrashes.simutils.thermodynamics.Block;

import javafx.scene.paint.Color;

public abstract class Block {
    public int row;
    public int col;
    public double c;
    public double m;
    public double melting_temperature;
    public double boiling_temperature;
    public double energy = 0;
    public boolean thermodynamics;
    public double thermal_conductivity;


    Block(int r, int c) { this.row = r; this.col = c; }

    // display name shown in UI
    public abstract String getDisplayName();

    // base color used by NormalRenderer
    public abstract Color baseColor();

    public void setTemperatureK(double T) {
        double e = T * m * c;
        energy = Math.max(0, Math.min(e, m * c * boiling_temperature));
    }
    public double getTemperatureK() {
        return energy / (m * c);
    }
    public void addEnergy(double dE) {
        energy += dE;
        // 限制能量上下限
        energy = Math.max(0, Math.min(energy, m * c * boiling_temperature));
    }

    // called every tick; snapshot is a copy of the world at tick start so blocks can inspect neighbors
    public void onTick(Block[][] snapshot, Block[][] world, int r, int c, double dt)
    {
        // default: do nothing
    }
    // return a shallow copy suitable for snapshot reading
    public Block copy() {
        try {
            Block b = this.getClass()
                    .getDeclaredConstructor(int.class, int.class)
                    .newInstance(this.row, this.col);

            // 复制所有物理属性
            b.c = this.c;
            b.m = this.m;
            b.energy = this.energy;
            b.melting_temperature = this.melting_temperature;
            b.boiling_temperature = this.boiling_temperature;
            b.thermal_conductivity = this.thermal_conductivity;
            b.thermodynamics = this.thermodynamics;

            return b;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
