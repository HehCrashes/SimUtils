package com.github.hehcrashes.simutils.magic_circle.res.rune;

import com.github.hehcrashes.simutils.magic_circle.res.ExecutionContext;
import com.github.hehcrashes.simutils.magic_circle.res.IDisplayable;
import com.github.hehcrashes.simutils.magic_circle.res.IRenderable;
import com.github.hehcrashes.simutils.magic_circle.res.ring.Ring;

public interface Rune extends IDisplayable, IRenderable {
    void apply(ExecutionContext ctx, Ring ring);

}

