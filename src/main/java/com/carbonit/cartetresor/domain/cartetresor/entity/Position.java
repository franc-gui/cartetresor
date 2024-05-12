package com.carbonit.cartetresor.domain.cartetresor.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class Position {
    private int x, y;

    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}
