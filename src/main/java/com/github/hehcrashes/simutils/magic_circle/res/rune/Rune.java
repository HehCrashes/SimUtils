package com.github.hehcrashes.simutils.magic_circle.res.rune;

import com.github.hehcrashes.simutils.magic_circle.res.ExecutionContext;
import com.github.hehcrashes.simutils.magic_circle.res.IDisplayable;
import com.github.hehcrashes.simutils.magic_circle.res.ring.Ring;

public interface Rune extends IDisplayable {
    void apply(ExecutionContext ctx, Ring ring);

}

