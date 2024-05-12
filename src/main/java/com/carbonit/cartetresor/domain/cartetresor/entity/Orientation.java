package com.carbonit.cartetresor.domain.cartetresor.entity;

public enum Orientation {

    N("N"),
    S("S"),
    W("O"),
    E("E");

    private final String orientation;

    Orientation(String orientation) {
        this.orientation = orientation;
    }

    public String getOrientation() {
        return orientation;
    }
}
