package com.carbonit.cartetresor.application.job;

import com.carbonit.cartetresor.application.job.processcartetresor.ProcessCarteTresorFileJob;
import com.carbonit.cartetresor.domain.cartetresor.entity.response.ProcessCarteTresorResponse;
import com.carbonit.cartetresor.domain.cartetresor.usecase.port.input.IProcessCarteTresor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith({MockitoExtension.class, OutputCaptureExtension.class})
class ProcessCarteTresorFileJobTest {
    @Mock
    private IProcessCarteTresor iProcessCarteTresor;
    @InjectMocks
    private ProcessCarteTresorFileJob job;

    @Test
    @DisplayName("GIVEN job triggered WHEN usecase succeed THEN usecase should be called AND log info created")
    void runNominalTest(CapturedOutput output) {
        givenUsecaseResponseWithSuccess();
        job.run();
        verify(iProcessCarteTresor, times(1)).process();
        assertThat(output.toString()).contains("INFO");
        assertThat(output).contains("Succeeded - Carte trésor process - chemin fichier output : chemin");
    }

    @Test
    @DisplayName("GIVEN job triggered WHEN usecase fail THEN usecase should be called AND log error created")
    void runErrorTest(CapturedOutput output) {
        givenUsecaseResponseWithError();
        job.run();
        verify(iProcessCarteTresor, times(1)).process();
        assertThat(output.toString()).contains("ERROR");
        assertThat(output).contains("Failed - Carte trésor process - error : erreur");
    }

    void givenUsecaseResponseWithSuccess() {
        given(iProcessCarteTresor.process()).willReturn(ProcessCarteTresorResponse.defaultResponse("chemin"));
    }

    void givenUsecaseResponseWithError() {
        given(iProcessCarteTresor.process()).willReturn(ProcessCarteTresorResponse.errorResponse("erreur"));
    }
}
