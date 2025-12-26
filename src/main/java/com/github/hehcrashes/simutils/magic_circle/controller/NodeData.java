package com.github.hehcrashes.simutils.magic_circle.controller;

import com.github.hehcrashes.simutils.magic_circle.res.IDisplayable;

public class NodeData {

    public String type; // WORKSPACE / MC / RING / RUNE / MARKER
    public Object obj;  // Ring, Rune, Marker, 或 String

    public NodeData(String type, Object obj) {
        this.type = type;
        this.obj = obj;
    }

    @Override
    public String toString() {
        if (obj instanceof String s) return s;
        if (obj instanceof IDisplayable d) return d.getDisplayName();
        return type;
    }
}
