package br.com.fiap.petfiap.model;

import br.com.fiap.petfiap.exception.StatusInvalidoException;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CancelamentoConcluidoContratoTest {

    @Test
    void deveRecusarCancelamentoQuandoAtendimentoJaFoiConcluido() {
        // Arrange
        Banho banho = new Banho(1, "Rex", "PEQUENO", "Ana",
                LocalDateTime.of(2026, 12, 1, 10, 0));
        banho.concluir();

        // Act + Assert
        assertThrows(StatusInvalidoException.class, banho::cancelar);
        assertEquals("CONCLUIDO", banho.getStatus());
    }
}
