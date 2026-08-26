# Relatório de Correção — Projeto de Algoritmos (TAAL 2026.1)

**Tema 16. Problema das N-Rainhas (N-Queens)**
**Grupo:** Gabriela Almeida de Andrade, Georis Samuel Martins Gouveia, Horlan Silva de Lacerda, Suelle Ferreira Maciel
**Repositório:** github.com/gabiandradeal/n-queens-analysis

## Avaliação

> **Nota revisada após a verificação detalhada de implementação (ver seção abaixo).** A nota do Relatório foi ajustada de 5,0 para 4,5 depois que a leitura do código revelou que o algoritmo nomeado "Branch and Bound" é, estruturalmente, uma técnica de forward-checking/propagação de restrições (CSP), não Branch and Bound clássico — uma nuance que a Fundamentação Teórica do relatório não sinaliza, embora o próprio Javadoc do código já a reconheça honestamente.

| Parte | Valor | Nota obtida |
|---|---|---|
| Relatório | 5,0 | 4,5 |
| Projeto (código-fonte) | 5,0 | 5,0 |
| **Total** | **10,0** | **9,5** |

## Relatório (4,5/5,0)

- Estrutura excepcional: provas formais de corretude por invariante de laço para os 3 algoritmos, 4 máquinas de hardware, teste de ponto de ruptura sob limiar de 1 minuto.
- Análise sofisticada e coerente: explica corretamente por que o Bitmask é ~20-23x mais rápido que o Backtracking clássico mesmo explorando o mesmo número de nós, e por que o Branch and Bound perde em N pequeno e ganha em N grande.
- Ponto que passou a limitar a nota (identificado na verificação detalhada abaixo): a Fundamentação Teórica (seção 2.3) descreve Branch and Bound no sentido clássico de otimização (relaxação de variáveis, comparação com um limite ótimo), mas o algoritmo de fato implementado é uma checagem de viabilidade de uma linha à frente (forward-checking) — uma técnica de poda para problemas de satisfação de restrições (CSP), legítima e eficaz, mas conceitualmente distinta do Branch and Bound clássico que o próprio texto teórico do relatório define. Essa distinção não é discutida no corpo do relatório, embora o comentário Javadoc do próprio código já a reconheça (`"constraint propagation de uma linha à frente, e não uma função de custo comparada a um ótimo, já que N-Queens não é um problema de otimização"`). Combinado com a já observada limitação de cobrir apenas 2 paradigmas de busca de base, isso reduz ligeiramente a nota da seção.

## Código-fonte (5,0/5,0)

- **Build:** sucesso via `javac`. **Conformidade com o README:** total.
- **Casos de teste:** N=1 (1 solução), N=2/N=3 (0 soluções), N=4 (2 soluções), N=8 (92 soluções) — confirmados nos 3 algoritmos. Contagem de chamadas recursivas idêntica entre Backtracking e Bitmask em todo N testado.

## Verificação de Implementação das Abordagens

Leitura completa de `Backtracking.java`, `Bitmask.java`, `BranchAndBound.java`, com compilação/execução real para N=4 e N=8 nos 3 algoritmos.

| Algoritmo | N=4 (chamadas/podas) | N=8 (chamadas/podas) | Veredito |
|---|---|---|---|
| Backtracking | 17 / 44 | 2.057 / 13.664 | **CONFIRMADO** — recursão linha a linha sobre `int[] board`, checagem O(row) de conflitos contra todas as rainhas anteriores. |
| Backtracking com Bit-masking | 17 / 44 (idêntico) | 2.057 / 13.664 (idêntico) | **CONFIRMADO** — nenhum array de tabuleiro; estado em 3 inteiros (`rowMask`, `ld`, `rd`), colunas livres extraídas em O(1) via `pos = done & (~(rowMask\|ld\|rd))`. Contagens idênticas ao Backtracking confirmam exatamente a alegação central do relatório (mesma árvore de busca, custo por nó menor). |
| Branch and Bound | 13 / 32 | 1.413 / 9.156 | **PARCIALMENTE CONFIRMADO** — usa tabelas O(1) (`cols`, `diag1`, `diag2`) **mais** uma checagem de viabilidade de uma linha à frente (`existeColunaViavel(row+1, n)`) antes de descer, com poda real quando não há coluna livre na próxima linha (menos nós que o Backtracking, confirmado: 13 vs. 17 em N=4). Porém, isso é estruturalmente **forward-checking/propagação de restrições**, não Branch and Bound clássico: não há função de custo nem comparação com um limite ótimo global, pois N-Queens aqui é um problema de satisfação (achar/contar soluções), não de otimização. O próprio Javadoc da classe já reconhece essa distinção honestamente — mas o relatório, ao descrever B&B no sentido clássico na Fundamentação Teórica, não sinaliza essa nuance ao leitor. |

**Conclusão da verificação:** Backtracking e Bitmask são autênticos e bem verificados; o "Branch and Bound" tem poda real e eficaz, mas é tecnicamente uma técnica de CSP (forward-checking) e não Branch and Bound no sentido em que o próprio relatório o define teoricamente — uma discrepância conceitual entre teoria descrita e o que foi de fato implementado, que motivou o ajuste de 0,5 ponto na nota do Relatório.

**Nota final: 9,5 / 10,0**
