package com.github.hehcrashes.simutils.magic_circle.res.marker;


import com.github.hehcrashes.simutils.magic_circle.res.IDisplayable;
import com.github.hehcrashes.simutils.magic_circle.res.IRenderable;

public interface Marker extends IDisplayable, IRenderable {
    void execute();
}

