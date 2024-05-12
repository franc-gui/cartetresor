package com.carbonit.cartetresor.domain.cartetresor.entity.response;

import lombok.Builder;
import org.apache.commons.lang3.StringUtils;

import java.nio.file.Path;

@Builder
public record ProcessCarteTresorResponse(String pathCarteTresorOutput, String errorMessage) {

    public static ProcessCarteTresorResponse defaultResponse(String pathCarteTresorOutput) {
        return ProcessCarteTresorResponse.builder().pathCarteTresorOutput(pathCarteTresorOutput).build();
    }

    public static ProcessCarteTresorResponse errorResponse(String errorMessage) {
        return ProcessCarteTresorResponse.builder().errorMessage(errorMessage).build();
    }

    public boolean isValid() {
        return StringUtils.isBlank(errorMessage);
    }
}
