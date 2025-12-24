package com.github.hehcrashes.simutils.thermodynamics.Block;

import javafx.scene.paint.Color;

import static com.github.hehcrashes.simutils.thermodynamics.JavaFX_2D_Minecraft_Simulator.thermal_conductivity_speed;

public class GrassBlock extends Block {
    public GrassBlock(int r, int c) {
        super(r,c);
        this.c = 900;
        this.m = 1500;
        this.melting_temperature = 1423;
        this.boiling_temperature = 2273;;
        this.thermodynamics = false;
        this.thermal_conductivity = 0.5 * thermal_conductivity_speed;
    }
    @Override
    public String getDisplayName() { return "Grass"; }
    @Override
    public Color baseColor() { return Color.LIMEGREEN; }

    @Override
    public void onTick(Block[][] snapshot, Block[][] world, int r, int c, double dt) {
        // simple demonstration: if block above is empty (out of bounds or instance of DirtBlock), spread slightly
        // We'll implement a tiny behavior: if any adjacent dirt exists, there's a small chance to turn it into grass
        int[] dr = {-1,1,0,0};
        int[] dc = {0,0,-1,1};
        for (int i=0;i<4;i++) {
            int nr = r + dr[i];
            int nc = c + dc[i];
            if (nr<0||nr>=snapshot.length||nc<0||nc>=snapshot[0].length) continue;
            Block nb = snapshot[nr][nc];
            if (nb instanceof DirtBlock) {
                // simple deterministic rule for demo: convert
                // NOTE: snapshot is read-only view; world must be modified by outer tick using world reference.
                // For simplicity in this demo we do nothing here; actual conversion can be scheduled via a change list.
            }
        }
    }
}
