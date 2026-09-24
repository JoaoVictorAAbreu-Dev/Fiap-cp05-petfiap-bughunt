package br.com.fiap.petfiap.model;

// Padrao Singleton (Aula 14): uma unica instancia em toda a aplicacao,
// responsavel por gerar os protocolos sequenciais dos atendimentos.
// Thread-safe para o uso concorrente do pet shop.
public class GeradorProtocolo {

    private static final GeradorProtocolo instancia = new GeradorProtocolo();

    private int contador;

    private GeradorProtocolo() {
        contador = 0;
        System.out.println("GeradorProtocolo criado!");
    }

    public static GeradorProtocolo getInstancia() {
        return instancia;
    }

    public synchronized int proximo() {
        contador++;
        return contador;
    }
}
