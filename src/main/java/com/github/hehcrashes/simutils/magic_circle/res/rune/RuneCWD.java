package com.github.hehcrashes.simutils.magic_circle.res.rune;

import com.github.hehcrashes.simutils.magic_circle.res.ExecutionContext;
import com.github.hehcrashes.simutils.magic_circle.res.ring.Ring;

public class RuneCWD implements Rune{
    @Override
    public void apply(ExecutionContext ctx, Ring ring) {
        int runeSlots = ring.getRuneSlots();
        int childSlots = ring.getChildSlots();
        while (ctx.getRuneIndex() < runeSlots || ctx.getChildrenRingIndex() < childSlots){
            ctx.runNextRing(ring);
            ctx.runNextRune(ring);
        }
        ctx.runMarker(ring);
    }
}
