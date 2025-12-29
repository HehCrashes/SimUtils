package com.github.hehcrashes.simutils.magic_circle.res.rune;

import com.github.hehcrashes.simutils.magic_circle.res.ExecutionContext;
import com.github.hehcrashes.simutils.magic_circle.res.ring.Ring;
import javafx.scene.image.Image;

import static com.github.hehcrashes.simutils.magic_circle.res.ResourceManager.textures;

public class RuneCWD extends BaseRune {
    @Override
    protected Image getTexture(){
        return textures.get("HuzrahYol");
    }
    @Override
    public String getDisplayName() {
        return "倾听:火 符文";
    }
    @Override
    public void apply(ExecutionContext ctx, Ring ring) {

    }
}
