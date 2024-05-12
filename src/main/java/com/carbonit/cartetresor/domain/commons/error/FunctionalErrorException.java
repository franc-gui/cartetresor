package com.carbonit.cartetresor.domain.commons.error;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class FunctionalErrorException extends RuntimeException {

    public FunctionalErrorException(Throwable throwable) {
        super(throwable.getMessage());
    }

    public FunctionalErrorException(String message) {
        super(message);
    }

    public FunctionalErrorException(String message, Throwable throwable) {
        super(message, throwable);
    }

}
