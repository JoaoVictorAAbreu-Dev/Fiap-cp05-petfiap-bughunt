package br.com.fiap.petfiap.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BanhoPrecoContratoTest {

    @Test
    void deveCobrarPrecoDoBanhoConformePorteQuandoCalcularPreco() {
        // Arrange
        LocalDateTime horario = LocalDateTime.of(2026, 12, 1, 10, 0);
        Banho pequeno = new Banho(1, "Rex", "PEQUENO", "Ana", horario);
        Banho medio = new Banho(2, "Rex", "MEDIO", "Ana", horario);
        Banho grande = new Banho(3, "Rex", "GRANDE", "Ana", horario);

        // Act
        double precoPequeno = pequeno.calcularPreco();
        double precoMedio = medio.calcularPreco();
        double precoGrande = grande.calcularPreco();

        // Assert
        assertEquals(60.0, precoPequeno, 0.001);
        assertEquals(80.0, precoMedio, 0.001);
        assertEquals(100.0, precoGrande, 0.001);
    }
}
