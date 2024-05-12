package com.carbonit.cartetresor.domain.commons.entity;

public enum Directory {

    INPUT("input"),
    OUTPUT("output");

    private final String directory;

    Directory(String directory) {
        this.directory = directory;
    }

    public String getDirectory() {
        return directory;
    }
}
