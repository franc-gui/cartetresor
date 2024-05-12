package com.carbonit.cartetresor.application.job.processcartetresor;

import com.carbonit.cartetresor.application.job.ReadFilesJob;
import com.carbonit.cartetresor.domain.cartetresor.entity.response.ProcessCarteTresorResponse;
import com.carbonit.cartetresor.domain.cartetresor.usecase.port.input.IProcessCarteTresor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service("jobProcessCarteTresor")
@RequiredArgsConstructor
@Slf4j
public class ProcessCarteTresorFileJob implements ReadFilesJob {
    private final IProcessCarteTresor iProcessCarteTresor;

    @Override
    public void run() {
        log.info("Start carte trésor process");
        var response = iProcessCarteTresor.process();
        interpretResponse(response);
        log.info("End carte trésor process");
    }

    private void interpretResponse(ProcessCarteTresorResponse response) {
        if (response.isValid()) {
            log.info("Succeeded - Carte trésor process - chemin fichier output : " + response.pathCarteTresorOutput());
        } else {
            log.error("Failed - Carte trésor process - error : " + response.errorMessage());
        }
    }
}
