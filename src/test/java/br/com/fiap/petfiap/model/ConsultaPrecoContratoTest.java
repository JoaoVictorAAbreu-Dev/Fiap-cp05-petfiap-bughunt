package br.com.fiap.petfiap.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ConsultaPrecoContratoTest {

    @Test
    void deveManterPrecoFixoQuandoPorteDaConsultaVaria() {
        // Arrange
        LocalDateTime horario = LocalDateTime.of(2026, 12, 1, 10, 0);
        ConsultaVeterinaria pequeno = new ConsultaVeterinaria(1, "Rex", "PEQUENO", "Ana", horario);
        ConsultaVeterinaria medio = new ConsultaVeterinaria(2, "Rex", "MEDIO", "Ana", horario);
        ConsultaVeterinaria grande = new ConsultaVeterinaria(3, "Rex", "GRANDE", "Ana", horario);

        // Act
        double precoPequeno = pequeno.calcularPreco();
        double precoMedio = medio.calcularPreco();
        double precoGrande = grande.calcularPreco();

        // Assert
        assertEquals(150.0, precoPequeno, 0.001);
        assertEquals(150.0, precoMedio, 0.001);
        assertEquals(150.0, precoGrande, 0.001);
    }
}
