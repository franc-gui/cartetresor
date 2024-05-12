package com.carbonit.cartetresor.infrastructure.adapter.cartetresor;

import com.carbonit.cartetresor.infrastructure.commons.repository.FileRepository;
import com.carbonit.cartetresor.infrastructure.error.InfrastructureException;
import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith({MockitoExtension.class, OutputCaptureExtension.class})
class ProcessCarteTresorAdapterTest {

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

    @Mock
    private FileRepository fileRepository;

    @InjectMocks
    private ProcessCarteTresorAdapter adapter;

    @SneakyThrows
    @Test
    @DisplayName("GIVEN read request AND valid file THEN input file is fetched")
    void readNominalTest() {
        givenFileFetched();

        var filelines = adapter.readFile("carte_tresor.txt", "input");
        verify(fileRepository, times(1)).readFile("carte_tresor.txt", "input");
        assertThat(filelines).isEqualTo(NOMINAL_FILELINES);
    }

    @SneakyThrows
    @Test
    @DisplayName("GIVEN read request AND read file io exception THEN infrastructure exception is thrown")
    void readExceptionTest() {
        givenFetchFileThrewException();

        Exception exception = assertThrows(InfrastructureException.class, () -> adapter.readFile("carte_tresor.txt", "input"));
        verify(fileRepository, times(1)).readFile("carte_tresor.txt", "input");
        assertThat(exception.getMessage()).isEqualTo("error");
    }

    @SneakyThrows
    @Test
    @DisplayName("GIVEN delete request AND valid file THEN input file is deleted")
    void deleteNominalTest() {
        givenFileDeleted();

        adapter.deleteFile("carte_tresor.txt", "input");
        verify(fileRepository, times(1)).deleteFile("carte_tresor.txt", "input");
    }

    @SneakyThrows
    @Test
    @DisplayName("GIVEN delete request AND file didn't exist THEN log is written")
    void deleteFailedTest(CapturedOutput output) {
        givenFileNotDeleted();

        adapter.deleteFile("carte_tresor.txt", "input");
        verify(fileRepository, times(1)).deleteFile("carte_tresor.txt", "input");
        assertThat(output.toString()).contains("WARN");
        assertThat(output).contains("Le fichier input/carte_tresor.txt n'existait plus au moment de le supprimer");
    }

    @SneakyThrows
    @Test
    @DisplayName("GIVEN read request AND delete file io exception THEN infrastructure exception is thrown")
    void deleteExceptionTest() {
        givenDeleteFileThrewException();

        Exception exception = assertThrows(InfrastructureException.class, () -> adapter.deleteFile("carte_tresor.txt", "input"));
        verify(fileRepository, times(1)).deleteFile("carte_tresor.txt", "input");
        assertThat(exception.getMessage()).isEqualTo("error");
    }

    @SneakyThrows
    @Test
    @DisplayName("GIVEN write request AND valid file THEN output file is written")
    void writeNominalTest() {
        givenFileWritten();

        String path = adapter.writeFile(NOMINAL_FILELINES, "carte_tresor.txt", "input");
        verify(fileRepository, times(1)).writeFile(NOMINAL_FILELINES, "carte_tresor.txt", "input");
        assertThat(path).isEqualTo("chemin");
    }

    @SneakyThrows
    @Test
    @DisplayName("GIVEN write request AND write file io exception THEN infrastructure exception is thrown")
    void writeExceptionTest() {
        givenWriteFileThrewException();

        Exception exception = assertThrows(InfrastructureException.class, () -> adapter.writeFile(NOMINAL_FILELINES, "carte_tresor.txt", "input"));
        verify(fileRepository, times(1)).writeFile(NOMINAL_FILELINES, "carte_tresor.txt", "input");
        assertThat(exception.getMessage()).isEqualTo("error");
    }

    @SneakyThrows
    void givenFileFetched() {
        given(fileRepository.readFile(any(), any())).willReturn(NOMINAL_FILELINES);
    }

    @SneakyThrows
    void givenFetchFileThrewException() {
        given(fileRepository.readFile(any(), any())).willThrow(new IOException("error"));
    }

    @SneakyThrows
    void givenFileDeleted() {
        given(fileRepository.deleteFile(any(), any())).willReturn(true);
    }

    @SneakyThrows
    void givenFileNotDeleted() {
        given(fileRepository.deleteFile(any(), any())).willReturn(false);
    }

    @SneakyThrows
    void givenDeleteFileThrewException() {
        given(fileRepository.deleteFile(any(), any())).willThrow(new IOException("error"));
    }

    @SneakyThrows
    void givenFileWritten() {
        given(fileRepository.writeFile(any(), any(), any())).willReturn("chemin");
    }

    @SneakyThrows
    void givenWriteFileThrewException() {
        given(fileRepository.writeFile(any(), any(), any())).willThrow(new IOException("error"));
    }
}
