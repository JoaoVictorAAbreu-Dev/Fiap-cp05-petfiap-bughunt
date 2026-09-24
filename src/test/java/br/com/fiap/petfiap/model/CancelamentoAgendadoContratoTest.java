package br.com.fiap.petfiap.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CancelamentoAgendadoContratoTest {

    @Test
    void deveMudarParaCanceladoQuandoCancelarAtendimentoAgendado() {
        // Arrange
        Banho banho = new Banho(1, "Rex", "PEQUENO", "Ana",
                LocalDateTime.of(2026, 12, 1, 10, 0));
        assertEquals("AGENDADO", banho.getStatus());

        // Act
        banho.cancelar();

        // Assert
        assertEquals("CANCELADO", banho.getStatus());
    }
}
