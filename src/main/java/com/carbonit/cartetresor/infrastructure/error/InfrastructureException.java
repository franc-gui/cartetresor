package com.carbonit.cartetresor.infrastructure.error;

import lombok.NoArgsConstructor;


@NoArgsConstructor
public class InfrastructureException extends RuntimeException {

    public InfrastructureException(Throwable throwable) {
        super(throwable.getMessage());
    }

    public InfrastructureException(String message) {
        super(message);
    }


}
