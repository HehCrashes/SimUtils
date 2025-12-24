package com.github.hehcrashes.simutils.thermodynamics.Renderer;

import com.github.hehcrashes.simutils.thermodynamics.Block.Block;
import javafx.scene.paint.Color;

public class NormalRenderer implements IBlockRenderer {
    @Override public Color render(Block b) {
        return b.baseColor();
    }
}
