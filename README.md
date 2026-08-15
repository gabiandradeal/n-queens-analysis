# N-Quenns

Repositório dedicado ao estudo comparativo e análise experimental de algoritmos exatos para o clássico Problema das N-Rainhas, desenvolvido como projeto prático da disciplina de Técnicas de Análise de Algoritmos (TAAL).

## Autores

* Gabriela Andrade [(@gabiandradeal)](https://github.com/gabiandradeal)
* Georis Samuel [(@georiSamuel)](https://github.com/georiSamuel)
* Horlan Lacerda [(@Horlanlacerda)](https://github.com/Horlanlacerda)
* Suelle Maciel [(@SuelleMaciel)](https://github.com/SuelleMaciel)

## O Problema

Posicionar N rainhas em um tabuleiro N×N de forma que nenhuma rainha ataque outra (mesma linha, coluna ou diagonal).

## Proposta do Estudo

Comparar experimentalmente diferentes estratégias de busca e poda aplicadas ao N-Queens, observando como cada uma lida com o crescimento exponencial do espaço de busca conforme N aumenta.

Abordagens definidas

- **Backtracking** clássico
- **Branch and Bound** (poda com tabelas de controle)
- **Bitmask** (representação das restrições via operações bit a bit)

## O que será medido

- Tempo de execução e consumo de memória
- Estados explorados, chamadas recursivas, podas realizadas, profundidade máxima da busca
- Ponto em que cada abordagem deixa de ser viável conforme N cresce

## Entregáveis (visão geral)

- Código-fonte (Java/Maven)
- Instâncias de teste e scripts para reproduzir os experimentos
- Relatório técnico (PDF)
- Dashboard/gráficos comparando as três abordagens (tempo, memória, etc.)
- Interface simples, se der tempo
