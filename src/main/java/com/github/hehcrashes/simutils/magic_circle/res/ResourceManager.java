package com.github.hehcrashes.simutils.magic_circle.res;

import com.github.hehcrashes.simutils.magic_circle.res.marker.EmptyMarker;
import com.github.hehcrashes.simutils.magic_circle.res.marker.HiMarker;
import com.github.hehcrashes.simutils.magic_circle.res.marker.Marker;
import com.github.hehcrashes.simutils.magic_circle.res.ring.EmptyRing;
import com.github.hehcrashes.simutils.magic_circle.res.ring.Ring;
import com.github.hehcrashes.simutils.magic_circle.res.ring.Ring44;
import com.github.hehcrashes.simutils.magic_circle.res.rune.EmptyRune;
import com.github.hehcrashes.simutils.magic_circle.res.rune.Rune;
import com.github.hehcrashes.simutils.magic_circle.res.rune.RuneCWD;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ResourceManager {

    public static List<Ring> rings = new ArrayList<>();
    public static List<Rune> runes = new ArrayList<>();
    public static List<Marker> markers = new ArrayList<>();
    public static List<Ring> prefabs = new ArrayList<>(); // 预制件

    static {
        registerRings(
                new EmptyRing(),
                new Ring44()
        );
        registerRunes(
                new EmptyRune(),
                new RuneCWD()
        );
        registerMarkers(
                new EmptyMarker(),
                new HiMarker()
        );
    }

    public static void registerRings(Ring... rs) {
        rings.addAll(Arrays.asList(rs));
    }
    public static void registerRunes(Rune... rs) {
        runes.addAll(Arrays.asList(rs));
    }
    public static void registerMarkers(Marker... ms) {
        markers.addAll(Arrays.asList(ms));
    }
    // 创建预制件
    public static void addPrefab(Ring prefab) {
        prefabs.add(prefab);
    }
}
