package com.github.hehcrashes.simutils.thermodynamics;

public abstract class Item {
    String name;
    Item(String name) { this.name = name; }
    public String toString() { return name; }
}

