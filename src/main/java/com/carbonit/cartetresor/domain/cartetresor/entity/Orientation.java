package com.carbonit.cartetresor.domain.cartetresor.entity;

import com.carbonit.cartetresor.domain.commons.error.FunctionalErrorException;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum Orientation {

    NORD("N"),
    SUD("S"),
    OUEST("O"),
    EST("E");

    private final String orientation;

    Orientation(String orientation) {
        this.orientation = orientation;
    }

    public static Orientation valueOfOrientation(String orientation) {
        return Arrays.stream(values())
                .filter(orientationEnum -> orientationEnum.orientation.equals(orientation))
                .findFirst()
                .orElseThrow(() -> new FunctionalErrorException("Cette orientation n'existe pas : " + orientation));
    }
}
