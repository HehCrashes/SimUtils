package com.github.hehcrashes.simutils.magic_circle.res;

import com.github.hehcrashes.simutils.magic_circle.controller.MainController;
import com.github.hehcrashes.simutils.magic_circle.res.marker.EmptyMarker;
import com.github.hehcrashes.simutils.magic_circle.res.marker.HiMarker;
import com.github.hehcrashes.simutils.magic_circle.res.marker.Marker;
import com.github.hehcrashes.simutils.magic_circle.res.ring.EmptyRing;
import com.github.hehcrashes.simutils.magic_circle.res.ring.Ring;
import com.github.hehcrashes.simutils.magic_circle.res.ring.Ring44;
import com.github.hehcrashes.simutils.magic_circle.res.rune.EmptyRune;
import com.github.hehcrashes.simutils.magic_circle.res.rune.Rune;
import com.github.hehcrashes.simutils.magic_circle.res.rune.RuneCWD;
import javafx.scene.image.Image;

import java.util.*;

public class ResourceManager {

    public static List<Ring> rings = new ArrayList<>();
    public static List<Rune> runes = new ArrayList<>();
    public static List<Marker> markers = new ArrayList<>();
    public static List<Ring> prefabs = new ArrayList<>(); // 预制件
    public static HashMap<String,Image> textures = new HashMap<>();

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
        registerTextures(Map.of(
                "HuzrahYol", new Image(ResourceManager.class.getResource("/images/runes/HuzrahYol.png").toString()),

                "0001", new Image(ResourceManager.class.getResource("/images/runes/0001.png").toString())
        ));
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
    public static void registerTextures(Map<String, Image> textureMap) {
        textures.putAll(textureMap);
    }
    // 创建预制件
    public static void addPrefab(Ring prefab) {
        prefabs.add(prefab);
    }

}
