package com.carbonit.cartetresor.domain.cartetresor.entity;

import com.carbonit.cartetresor.domain.commons.error.FunctionalErrorException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;

@Slf4j
@Getter
@AllArgsConstructor
public class Aventurier {
    private String nom;
    private Position position;
    private Orientation orientation;
    private String movementsSequence;
    private int tresorsPickedNb;

    public void move(Carte carte) {
        int deltax = 0, deltay = 0;
        switch (orientation) {
            case Orientation.NORD -> deltay--;
            case Orientation.SUD -> deltay++;
            case Orientation.OUEST -> deltax--;
            case Orientation.EST -> deltax++;
        }
        var newPosition = new Position(position.getX() + deltax, position.getY() + deltay);
        if (!carte.getMontagnes().contains(newPosition) && isInBounds(newPosition, carte)) {
            position = newPosition;
        } else {
            log.info("L'aventurier %s n'était pas capable de se déplacer vers le %s depuis la position %s".formatted(nom, orientation, position.toString()));
        }
    }

    private boolean isInBounds(Position position, Carte carte) {
        return 0 <= position.getX() && 0 <= position.getY()
                && position.getX() <= carte.getLargeur() && position.getY() <= carte.getHauteur();
    }

    public void turnLeft() {
        switch (orientation) {
            case NORD -> orientation = Orientation.OUEST;
            case SUD -> orientation = Orientation.EST;
            case EST -> orientation = Orientation.NORD;
            case OUEST -> orientation = Orientation.SUD;
        }
    }

    public void turnRight() {
        switch (orientation) {
            case NORD -> orientation = Orientation.EST;
            case SUD -> orientation = Orientation.OUEST;
            case EST -> orientation = Orientation.SUD;
            case OUEST -> orientation = Orientation.NORD;
        }
    }

    public void processSequence(Carte carte) {
        movementsSequence.chars().mapToObj(c -> (char) c).forEachOrdered(action -> {
            var oldPosition = position;
            switch (action) {
                case 'A' -> move(carte);
                case 'G' -> turnLeft();
                case 'D' -> turnRight();
                default ->
                        throw new FunctionalErrorException("Un caractère est inconnu dans la séquence de mouvements");
            }
            if (ObjectUtils.notEqual(oldPosition, position)) {
                int tresorsNb = carte.getTresorsNb(position.getX(), position.getY());
                if (tresorsNb > 0) {
                    carte.removeTresor(position.getX(), position.getY());
                    tresorsPickedNb++;
                }
            }
        });
    }
}
