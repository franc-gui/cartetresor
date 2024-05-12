package com.carbonit.cartetresor.domain.commons.entity;

import java.util.Objects;

public enum Filename {

    CARTE_TRESOR("carte_tresor");

    private final String filename;

    Filename(String filename) {
        this.filename = filename;
    }

    public String getFilename() {
        return filename;
    }

    public String getFullFilename() {
        return filename + "." + Extension.TXT.getExtension();
    }

    public String getOutputFullFilename(String suffix) {
        return filename + "_" + Objects.requireNonNull(suffix) + "." + Extension.TXT.getExtension();
    }
}
