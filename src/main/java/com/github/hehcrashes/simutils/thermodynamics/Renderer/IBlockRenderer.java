package com.github.hehcrashes.simutils.thermodynamics.Renderer;

import com.github.hehcrashes.simutils.thermodynamics.Block.Block;
import javafx.scene.paint.Color;

public interface IBlockRenderer {
    Color render(Block b);
}