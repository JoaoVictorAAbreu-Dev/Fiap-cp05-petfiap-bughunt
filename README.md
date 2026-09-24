# Checkpoint 5 — Bug Hunt PetFiap

## Identificação

**Grupo:** João Victor e Rodrigo  
**Turma:** 2CCPH

| Integrante | RM | Turma |
|---|---|---|
| João Victor Alves de Abreu | 564946 | 2CCPH |
| Rodrigo Kenshin Viana Matayoshi | 564026 | 2CCPH |

| Campo | Resultado |
|---|---|
| **Total de bugs corrigidos** | **12 / 12** |
| **Total de ajustes de Clean Code** | **6 / 6** |
| **Total de testes novos escritos** | **6 / 6** |
| **Suíte final (JUnit 5 / Maven)** | **26 testes, 0 falhas** |

O primeiro commit registra o ZIP original. Cada correção, ajuste de Clean Code e teste novo tem um commit próprio. Os 20 testes recebidos permanecem intactos.

## Parte 1 — Bugs encontrados

| # | Sintoma observado (o que fiz/vi) | Causa raiz (arquivo e linha aproximada) | Correção aplicada | Conceito da disciplina |
|---|---|---|---|---|
| bug01 | `deveMontarAtendimentoCompleto`: esperado `Rex`, recebido `null`. | `AtendimentoBuilder.java:24`: `petNome = petNome` só reatribuía o parâmetro. | Atribuição a `this.petNome`. | Encapsulamento, atributos e `this`. |
| bug02 | Builder aceitava pet sem nome ou sem porte. | `AtendimentoBuilder.java:39`: `construir()` delegava sem validar. | Validação de nome e porte ausentes ou em branco antes de criar o objeto. | Builder e invariantes de objeto. |
| bug03 | Factory devolvia `Banho` para tipo `TOSA`. | `AtendimentoFactory.java:18`: ramo `TOSA` instanciava a classe errada. | Ramo passa a instanciar `Tosa`. | Factory e polimorfismo. |
| bug04 | Consulta recebia dados, mas `getPetNome()` devolvia `null`. | `ConsultaVeterinaria.java:17`: construtor chamava `super()` vazio. | Passagem dos dados ao construtor de `Atendimento`. | Herança e construtores. |
| bug05 | Duas chamadas de `getInstancia()` devolviam objetos diferentes; protocolos recomeçavam em 1. | `GeradorProtocolo.java:8`: a instância criada não era guardada. | Instância única estática; `proximo()` sincronizado para preservar sequência sob concorrência. | Singleton e estado compartilhado. |
| bug06 | Agendamento duplicado chegava a `save()` em vez de lançar `HorarioOcupadoException`. | `AgendaService.java:29`: `==` comparava identidade de `String` e `LocalDateTime`. | Comparação por `.equals()` dos valores. | Igualdade de objetos. |
| bug07 | Busca por ID ausente retornava `null`; o teste esperava exceção. | `AgendaService.java:40`: `catch (Exception)` engolia a exceção lançada por `orElseThrow`. | Remoção do `catch` genérico; exceção chega ao controller. | Exceções e tratamento de erros. |
| bug08 | Novo teste: banho pequeno custava R$ 100,00, mas o contrato pede R$ 60,00. | `Banho.java:28-32`: preços de pequeno e grande invertidos. | Pequeno R$ 60,00; médio R$ 80,00; grande R$ 100,00. | Regra de negócio no model. |
| bug09 | Novo teste: `Atendimento` de tipo `Tosa` devolvia 30 minutos, não 60. | `Tosa.java:41`: método com parâmetro `String` sobrecarregava em vez de sobrescrever. | Mesma assinatura da superclasse, sem parâmetro, com `@Override`. | Sobrescrita vs. sobrecarga. |
| bug10 | Novo teste: data passada seguia até o repositório. | `AgendaService.java:24`: faltava checagem temporal antes da consulta. | Rejeição com `IllegalArgumentException` antes de acessar o banco. | Validação e ordem das operações. |
| bug11 | Novo teste: atendimento concluído podia virar cancelado. | `Atendimento.java:64`: `cancelar()` não verificava o status. | Só `AGENDADO` pode ser cancelado; demais estados lançam `StatusInvalidoException`. | Máquina de estados e exceções. |
| bug12 | Revisão do fluxo JPA: novos atendimentos tinham `id` nulo e a entidade não definia geração desse ID. | `Atendimento.java:14-16`: `@Id` sem `@GeneratedValue`. | Geração `IDENTITY`; POST em H2 confirmou gravação com `id: 1` e GET de resumo. | JPA, chave primária e persistência. |

## Parte 2 — Ajustes de Clean Code

| # | Onde estava | Princípio/boa prática violado | O que mudou |
|---|---|---|---|
| clean01 | `AtendimentoFactory.criar` | Parâmetros de uma letra (`p`, `t`, `n`, `po`, `tu`, `d`) dificultavam leitura. | Nomes que expressam protocolo, tipo, pet, porte, tutor e horário. |
| clean02 | `AtendimentoController` | Método privado de desconto não usado e comentários especulativos. | Remoção do código morto; nenhum desconto inexistente é sugerido pela API. |
| clean03 | `GeradorProtocolo` | Construtor imprimia em `System.out` a cada criação. | Remoção do efeito colateral de console. |
| clean04 | `AgendaService` | `System.out` imprimia nome do pet e tutor e misturava apresentação com regra de negócio. | Remoção do recibo de console; o método retorna o atendimento salvo. |
| clean05 | `AgendaService` | Injeção por campo deixava a dependência mutável e implícita. | `AtendimentoRepository` final e injetado pelo construtor. |
| clean06 | `AtendimentoController` | Injeção por campo deixava a dependência do serviço implícita. | `AgendaService` final e injetado pelo construtor. |

## Parte 3 — Testes novos (regras antes sem cobertura)

Cada teste foi adicionado em um commit `test:` separado. Os quatro testes vermelhos foram executados antes das correções correspondentes.

| # | Teste escrito (classe.método) | Regra coberta | Resultado ao escrever |
|---|---|---|---|
| teste01 | `BanhoPrecoContratoTest.deveCobrarPrecoDoBanhoConformePorteQuandoCalcularPreco` | Banho: R$ 60,00 / R$ 80,00 / R$ 100,00 por porte. | Vermelho: pequeno retornava R$ 100,00 (bug08). |
| teste02 | `TosaDuracaoContratoTest.deveDurar60MinutosQuandoConsultarDuracaoDaTosa` | Tosa dura 60 minutos via referência `Atendimento`. | Vermelho: retornava 30 minutos (bug09). |
| teste03 | `AgendamentoPassadoContratoTest.deveRecusarAntesDeConsultarBancoQuandoHorarioEstaNoPassado` | Horário passado deve ser rejeitado sem acesso ao repositório mockado. | Vermelho: lançava `NullPointerException` após chegar ao repositório (bug10). |
| teste04 | `CancelamentoConcluidoContratoTest.deveRecusarCancelamentoQuandoAtendimentoJaFoiConcluido` | Concluído não pode ser cancelado nem mudar de status. | Vermelho: nenhuma exceção era lançada (bug11). |
| teste05 | `ConsultaPrecoContratoTest.deveManterPrecoFixoQuandoPorteDaConsultaVaria` | Consulta custa R$ 150,00 para qualquer porte. | Verde de cara: regra existente correta. |
| teste06 | `CancelamentoAgendadoContratoTest.deveMudarParaCanceladoQuandoCancelarAtendimentoAgendado` | Agendado pode virar cancelado. | Verde de cara: regra existente correta. |

## Parte 4 — Perguntas de reflexão

### 1. A suíte como contrato (Aula 15)

A primeira execução mostrou exatamente 20 testes e 9 falhas, como previsto no enunciado.  
Usei `expected: <Rex> but was: <null>` para seguir de `AtendimentoBuilderTest` até a atribuição errada em `comPet`.  
O erro de tipo em `AtendimentoFactoryTest` apontou para o ramo `TOSA`, que construía `Banho`.  
No conflito de agenda, o `NullPointerException` era consequência do `save()` mockado devolver `null`; a causa real era a comparação com `==`.  
Os testes isolam a regra e repetem o cenário com rapidez, sem Oracle e sem subir o Spring.  
Com `curl`, seria necessário preparar dados manualmente, controlar a ordem e inspecionar cada resposta; a suíte registra expectativas e detecta regressões automaticamente.

### 2. Mock e injeção de dependência (Aulas 13 a 15)

Em produção, o Spring cria o bean `AtendimentoRepository` e o entrega ao construtor do `AgendaService`.  
Nos testes, `@Mock` cria um repository falso e `@InjectMocks` o entrega ao mesmo construtor.  
Assim, `AgendaService` executa suas regras reais, mas `findByPetNome`, `findById` e `save` recebem os retornos configurados com `when`.  
O teste de conflito usa `verify(repository, never()).save(any())` para provar que nada é persistido quando há choque de horário.  
O novo teste de data passada usa `verifyNoInteractions(repository)` para confirmar que a validação ocorre antes de qualquer consulta.  
Nenhum desses testes precisa de conexão Oracle nem inicializa o container Spring.

### 3. `==` vs. `.equals()` (Aula 7)

`==` verifica se duas referências apontam para o mesmo objeto, não se os valores são iguais.  
Dois pedidos podem conter `Rex` e o mesmo `LocalDateTime`, mas serem objetos distintos na memória.  
Por isso, o código original não reconhecia a reserva do mesmo pet no mesmo horário.  
Um literal como `"Rex"` pode parecer funcionar por causa do *interning* de strings da JVM, mas isso é coincidência para esse cenário.  
O teste cria outro `LocalDateTime` com o mesmo valor para evitar essa falsa segurança.  
Com `.equals()` em ambos os campos, o conflito é detectado e `save()` não é chamado.

### 4. Sobrescrita vs. sobrecarga (Aula 7)

`Atendimento.getDuracaoMinutos()` não recebe argumentos e devolve 30 por padrão.  
`Tosa` tinha `getDuracaoMinutos(String porte)`, que é outro método: isso é sobrecarga.  
Uma variável declarada como `Atendimento` continuava chamando o método sem argumento da superclasse e recebia 30.  
A correção implementou `getDuracaoMinutos()` em `Tosa`, que devolve 60.  
Isso é sobrescrita: a assinatura coincide e o despacho dinâmico escolhe a implementação da subclasse.  
`@Override` agora faz o compilador acusar uma futura mudança de assinatura equivocada.

### 5. Singleton manual vs. bean do Spring (Aula 14)

`GeradorProtocolo` deve manter um único contador para toda a aplicação em execução.  
O método original construía uma instância nova quando o campo ainda era nulo, mas não a armazenava.  
Por isso, `getInstancia()` devolvia objetos diferentes e cada protocolo recomeçava em 1.  
A instância estática única preserva o contador; `proximo()` sincronizado impede dois pedidos concorrentes de receberem o mesmo número.  
`AgendaService` é anotado com `@Service` e o Spring gerencia seu ciclo de vida como bean singleton por padrão.  
Nos testes, o Mockito constrói o serviço e injeta o mock sem depender desse gerenciamento do Spring.

### 6. Cobertura de testes: onde parar? (Aula 15)

Quatro testes novos ficaram vermelhos e revelaram bugs; dois ficaram verdes imediatamente.  
Manter os verdes é útil: eles documentam que preço fixo da consulta e cancelamento de agendado são contratos que não devem regredir.  
Neste projeto, comecei pelos fluxos que a suíte inicial já mostrava como quebrados e depois comparei o restante das tabelas do enunciado com a cobertura existente.  
Em um prazo real, priorizaria regras de negócio críticas, mudanças de estado e caminhos de erro com impacto visível.  
O teste de data passada, por exemplo, verifica também a ausência de acesso ao banco, efeito importante além da exceção.  
Não perseguiria 100% de cobertura numérica se ela apenas repetisse detalhes internos sem proteger comportamento relevante.

## Como rodar e validar

Requer JDK 17 ou superior e Maven. Na raiz do projeto, execute `mvn test`; a suíte unitária usa JUnit 5 e Mockito e não precisa de banco. Para gerar o pacote, execute `mvn package`. No Eclipse, importe como **Existing Maven Projects** e execute `src/test/java` como **JUnit Test**.

O `application.properties` mantém `SEU_RM` e `SUA_SENHA` conforme recebido. Para subir a API com Oracle, configure suas próprias credenciais apenas localmente e não as publique. Também é possível usar o bloco H2 comentado no arquivo para testes manuais. Nesta entrega, a API foi iniciada com H2 em memória por variáveis de ambiente, sem modificar o arquivo: POST criou `id: 1`, GET `/1/resumo` devolveu preço e duração corretos; conflito, data passada, ID inexistente e cancelamento de concluído retornaram 409, 400, 404 e 409, respectivamente.

O grupo deve enviar o link público deste repositório no Teams.

## Limitações verificadas

O fluxo HTTP foi validado com H2 em memória; não foram usadas credenciais nem conexão com o Oracle da FIAP. A checagem de conflito em `AgendaService` consulta a agenda antes de salvar, mas não possui uma garantia transacional ou restrição de banco para duas requisições simultâneas do mesmo pet e horário. Os testes do checkpoint cobrem chamadas sequenciais; o tratamento de concorrência entre transações e instâncias da API exigiria desenho adicional de persistência.
