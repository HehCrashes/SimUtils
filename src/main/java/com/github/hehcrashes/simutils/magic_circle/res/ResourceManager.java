package com.github.hehcrashes.simutils.magic_circle.res;

import com.github.hehcrashes.simutils.magic_circle.res.marker.HiMarker;
import com.github.hehcrashes.simutils.magic_circle.res.marker.Marker;
import com.github.hehcrashes.simutils.magic_circle.res.ring.Ring;
import com.github.hehcrashes.simutils.magic_circle.res.ring.Ring44;
import com.github.hehcrashes.simutils.magic_circle.res.rune.Rune;
import com.github.hehcrashes.simutils.magic_circle.res.rune.RuneCWD;

import java.util.ArrayList;
import java.util.List;

public class ResourceManager {

    public static List<Ring> rings = new ArrayList<>();
    public static List<Rune> runes = new ArrayList<>();
    public static List<Marker> markers = new ArrayList<>();
    public static List<Ring> prefabs = new ArrayList<>(); // 预制件

    static {
        // 测试数据
        rings.add(new Ring44());
        runes.add(new RuneCWD());
        markers.add(new HiMarker());
    }

    // 创建预制件
    public static void addPrefab(Ring prefab) {
        prefabs.add(prefab);
    }
}
