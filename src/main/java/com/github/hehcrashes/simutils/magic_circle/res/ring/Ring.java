package com.github.hehcrashes.simutils.magic_circle.res.ring;

import com.github.hehcrashes.simutils.magic_circle.res.marker.Marker;
import com.github.hehcrashes.simutils.magic_circle.res.rune.Rune;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class Ring {

    String name;

    // 结构
    int runeSlots;
    int childSlots;
    public int getRuneSlots(){
        return runeSlots;
    }
    public int getChildSlots(){
        return childSlots;
    }

    // 运行时结构
    private List<Rune> runes;
    private List<Ring> children;
    private Marker marker;

    public List<Rune> getRunes()     { return runes; }
    public List<Ring> getChildren()  { return children; }
    public Marker getMarkers() { return marker; }
    public void setMarker(Marker marker) {
        this.marker = marker;
    }

    public Ring(String name, int runeSlots, int childSlots) {
        this.name = name;
        this.runeSlots = runeSlots;
        this.childSlots = childSlots;

        runes   = new ArrayList<>(Collections.nCopies(runeSlots, null));
        marker = null;
        children= new ArrayList<>(Collections.nCopies(childSlots, null));
    }
}

