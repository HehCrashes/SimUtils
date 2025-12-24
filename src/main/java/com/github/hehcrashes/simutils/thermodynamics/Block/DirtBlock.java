package com.github.hehcrashes.simutils.thermodynamics.Block;

import javafx.scene.paint.Color;

import static com.github.hehcrashes.simutils.thermodynamics.JavaFX_2D_Minecraft_Simulator.thermal_conductivity_speed;

public class DirtBlock extends Block {

    public DirtBlock(int r, int c) {
        super(r,c);
        this.c = 900;
        this.m = 1500;
        this.melting_temperature = 1423;
        this.boiling_temperature = 2273;
        this.thermodynamics = false;
        this.thermal_conductivity = 0.5 * thermal_conductivity_speed;
    }
    @Override
    public String getDisplayName() { return "Dirt"; }
    @Override
    public Color baseColor() { return Color.SIENNA; }

    @Override
    public void onTick(Block[][] snapshot, Block[][] world, int r, int c, double dt) {

        Block self_s = snapshot[r][c]; // 只读快照
        Block self = this;                     // 写 world

        int[][] dirs = {{-1,0},{1,0},{0,-1},{0,1}};
        double deltaEnergySelf = 0.0;

        for (int[] d : dirs) {
            int nr = r + d[0];
            int nc = c + d[1];

            if (nr < 0 || nr >= snapshot.length || nc < 0 || nc >= snapshot[0].length)
                continue;

            Block neigh_s = snapshot[nr][nc];  // 只读快照
            Block neigh = world[nr][nc];       // 写 world（关键！）

            if (!neigh.thermodynamics) continue;

            double dT = neigh_s.getTemperatureK() - self_s.getTemperatureK();
            double k = 0.5 * (self_s.thermal_conductivity + neigh_s.thermal_conductivity);

            double Q = k * dT * dt;

            deltaEnergySelf += Q;  // 自己加能量
            neigh.addEnergy(-Q);   // 邻居加能量（写 world！）
        }

        self.addEnergy(deltaEnergySelf);    // 写 world
    }
}
