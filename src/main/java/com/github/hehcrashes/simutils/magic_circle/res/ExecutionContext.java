package com.github.hehcrashes.simutils.magic_circle.res;


import com.github.hehcrashes.simutils.magic_circle.res.marker.Marker;
import com.github.hehcrashes.simutils.magic_circle.res.ring.Ring;
import com.github.hehcrashes.simutils.magic_circle.res.rune.Rune;

public class ExecutionContext {
    private int runeIndex = 0;
    private int childrenRingIndex = 0;
    public int getRuneIndex() {
        return runeIndex;
    }
    public int getChildrenRingIndex() {
        return childrenRingIndex;
    }

    public void run(Ring root) {
        int runeSlots = root.getRuneSlots();
        int childSlots = root.getChildSlots();
        while (getRuneIndex() < runeSlots || getChildrenRingIndex() < childSlots){
            runNextRune(root);
            runNextRing(root);
        }
        runMarker(root);
    }

    public void init(){
        runeIndex = 0;
        childrenRingIndex = 0;
    }

    public void runNextRune(Ring ring) {
        if (ring.getRunes().isEmpty()) return;
        if (runeIndex >= ring.getRuneSlots()) return;
        Rune now = ring.getRunes().get(runeIndex++);
        if (now == null) return;

        now.apply(this, ring);
    }
    public void runNextRing(Ring ring) {
        if (ring.getChildren().isEmpty()) return;
        if (childrenRingIndex >= ring.getChildSlots()) return;
        Ring now = ring.getChildren().get(childrenRingIndex++);
        if (now == null) return;

        ExecutionContext ctx = new ExecutionContext();
        ctx.run(now);
    }
    public void runMarker(Ring ring) {
        if (ring.getMarkers() == null) return;

        Marker now = ring.getMarkers();
        now.execute();
    }
}

