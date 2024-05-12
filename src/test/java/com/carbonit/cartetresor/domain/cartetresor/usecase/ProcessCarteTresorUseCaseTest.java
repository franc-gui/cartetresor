package com.carbonit.cartetresor.domain.cartetresor.usecase;

import com.carbonit.cartetresor.domain.cartetresor.entity.response.ProcessCarteTresorResponse;
import com.carbonit.cartetresor.domain.cartetresor.usecase.port.output.ProcessCarteTresorRepository;
import com.carbonit.cartetresor.domain.cartetresor.usecase.process.ProcessCarteTresorUseCase;
import com.carbonit.cartetresor.infrastructure.error.InfrastructureException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ProcessCarteTresorUseCaseTest {

    private static final List<String> NOMINAL_FILELINES = List.of(
            "C - 10 - 10",
            "# truc bidule",
            "M - 1 - 1",
            "M - 9 - 9",
            "T - 2 - 2 - 2",
            "T - 3 - 3 - 3",
            "T - 2 - 3 - 0",
            "A - Lara - 1 - 0 - N - ADDAAGADAAGADADADA",
            "A - Nathan - 3 - 3 - E - AAAADAAAA"
    );

    private static final List<String> ERROR_FILELINES = List.of(
            "C - 10 - 10",
            "truc bidule",
            "M - 1 - 1",
            "M - 9 - 9",
            "T - 2 - 2 - 2",
            "T - 3 - 3 - 3",
            "T - 2 - 3 - 0",
            "A - Lara - 1 - 0 - N - ADDAAGADAAGADADADA"
    );

    private static final List<String> NOMINAL_OUTPUT_FILELINES = List.of(
            "C - 10 - 10",
            "M - 1 - 1",
            "M - 9 - 9",
            "T - 2 - 2 - 0",
            "T - 3 - 3 - 2",
            "T - 2 - 3 - 0",
            "A - Lara - 2 - 2 - N - 3",
            "A - Nathan - 7 - 7 - S - 0"
    );

    private static final ProcessCarteTresorResponse NOMINAL_RESPONSE = ProcessCarteTresorResponse.defaultResponse("chemin");

    private static final ProcessCarteTresorResponse ERROR_RESPONSE_INCORRECT_VALUE = ProcessCarteTresorResponse.errorResponse("Ce caractère n'est pas reconnu : truc bidule");

    private static final ProcessCarteTresorResponse ERROR_RESPONSE_EXCEPTION = ProcessCarteTresorResponse.errorResponse("Erreur");

    @Mock
    private ProcessCarteTresorRepository repository;
    @InjectMocks
    private ProcessCarteTresorUseCase useCase;

    @Captor
    ArgumentCaptor<List<String>> outputFileslinesCaptor;

    @Test
    @DisplayName("GIVEN process called WHEN input file read AND output file written THEN output should equal")
    void testNominalProcess() {
        givenNominalFileRead();
        givenWriteFileSuccess();
        var response = useCase.process();
        verify(repository, times(1)).readFile("carte_tresor.txt", "input");
        verify(repository, times(1)).deleteFile("carte_tresor.txt", "input");
        verify(repository, times(1)).writeFile(any(), any(), eq("output"));
        assertThat(outputFileslinesCaptor.getValue()).containsExactlyInAnyOrderElementsOf(NOMINAL_OUTPUT_FILELINES);
        assertThat(response).isEqualTo(NOMINAL_RESPONSE);
    }

    @Test
    @DisplayName("GIVEN process called WHEN input file incorrect value THEN output should equal")
    void testErrorProcessWhenIncorrectValue() {
        givenIncorrectValueFileRead();
        var response = useCase.process();
        verify(repository, times(1)).readFile("carte_tresor.txt", "input");
        verify(repository, times(1)).deleteFile("carte_tresor.txt", "input");
        verify(repository, never()).writeFile(any(), any(), any());
        assertThat(response).isEqualTo(ERROR_RESPONSE_INCORRECT_VALUE);
    }

    @Test
    @DisplayName("GIVEN process called WHEN read file returns error THEN output should equal")
    void testErrorProcessWhenExceptionFromRepository() {
        givenExceptionFileRead();
        var response = useCase.process();
        verify(repository, times(1)).readFile("carte_tresor.txt", "input");
        verify(repository, never()).deleteFile(any(), any());
        verify(repository, never()).writeFile(any(), any(), any());
        assertThat(response).isEqualTo(ERROR_RESPONSE_EXCEPTION);
    }

    void givenNominalFileRead() {
        given(repository.readFile(any(), any())).willReturn(NOMINAL_FILELINES);
    }

    void givenIncorrectValueFileRead() {
        given(repository.readFile(any(), any())).willReturn(ERROR_FILELINES);
    }

    void givenExceptionFileRead() {
        given(repository.readFile(any(), any())).willThrow(new InfrastructureException("Erreur"));
    }

    void givenWriteFileSuccess() {
        given(repository.writeFile(outputFileslinesCaptor.capture(), any(), any())).willReturn("chemin");
    }
}
