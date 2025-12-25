package com.github.hehcrashes.simutils.magic_circle.res.marker;

public class HiMarker implements Marker{

    @Override
    public void execute() {
        System.out.println("Hi！标定运行！");
    }

    @Override
    public String getDisplayName() {
        return "Hi 标定";
    }

}
