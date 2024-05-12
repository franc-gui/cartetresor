package com.carbonit.cartetresor.domain.cartetresor.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Getter
@Setter
public class Carte {
    private int largeur, hauteur;
    private Set<Position> montagnes;
    private Map<Position, Integer> tresors;

    public Carte() {
        montagnes = new HashSet<>();
        tresors = new HashMap<>();
    }

    public Carte(int largeur, int hauteur) {
        this.largeur = largeur;
        this.hauteur = hauteur;
        montagnes = new HashSet<>();
        tresors = new HashMap<>();
    }

    public void addMontagne(int x, int y) {
        montagnes.add(new Position(x, y));
    }

    public void addTresor(int x, int y, int nbTresors) {
        tresors.put(new Position(x, y), nbTresors);
    }

    public int getTresorsNb(int x, int y) {
        return tresors.getOrDefault(new Position(x, y), 0);
    }

    public void removeTresor(int x, int y) {
        Position caseTresor = new Position(x, y);
        if (tresors.containsKey(caseTresor)) {
            int nbTresorsRestants = tresors.get(caseTresor) - 1;
            tresors.put(caseTresor, nbTresorsRestants);
        }
    }
}
