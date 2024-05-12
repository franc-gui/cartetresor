package com.carbonit.cartetresor.domain.commons.entity;

public enum Extension {

    TXT("txt");

    private final String extension;

    Extension(String extension) {
        this.extension = extension;
    }

    public String getExtension() {
        return extension;
    }
}
