package com.nqueens.algorithms;
import com.nqueens.core.*;

/**
 * Implementação otimizada por máscaras de bits (bitmask) para o problema
 * das N-Rainhas.
 * <p>
 * Em vez de arrays booleanos ou de um tabuleiro N×N, as restrições de
 * colunas e diagonais ocupadas são representadas inteiramente por três
 * inteiros ({@code rowMask}, {@code ld}, {@code rd}), manipulados via
 * operadores de bits (AND, OR, shifts). As colunas livres para a linha
 * atual são obtidas em O(1) por {@code done & ~(rowMask | ld | rd)}, e a
 * iteração sobre elas é feita extraindo o bit menos significativo a cada
 * passo ({@code pos & -pos}).
 * <p>
 * É a versão com menor overhead de alocação (não usa arrays nem clona o
 * tabuleiro a cada chamada), servindo como o algoritmo mais rápido entre
 * os três em termos de tempo de execução.
 *
 * @author Gabi
 * @version 1.0
 * @since 17/08/2026
 */
public class Bitmask implements NQueensAlgorithm {

    /** Contador de chamadas recursivas realizadas durante a busca. */
    private long recursiveCalls = 0;

    /** Contador de soluções válidas encontradas. */
    private long solutions = 0;

    /** Contador de podas (colunas bloqueadas, somadas por chamada recursiva). */
    private long podas = 0;

    /** Máscara com os N bits menos significativos ligados, representa "tabuleiro completo". */
    private int done;

    /** Tamanho do tabuleiro (N), guardado para uso no cálculo de podas. */
    private int nBoard;

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() { return "Bitmask"; }

    /**
     * Executa o algoritmo de Bitmask para um tabuleiro N×N, coletando
     * métricas de tempo, memória e exploração do espaço de busca.
     *
     * @param n tamanho do tabuleiro (N×N) e número de rainhas a posicionar
     * @return {@link Metrics} com os resultados da execução (tempo, memória,
     *         chamadas recursivas, podas e soluções encontradas)
     */
    @Override
    public Metrics solve(int n) {
        recursiveCalls = 0; solutions = 0; podas = 0;
        nBoard = n;
        done = (1 << n) - 1;

        System.gc();
        long memBefore = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        long start = System.nanoTime();

        solveNQueens(0, 0, 0);

        long end = System.nanoTime();
        long memAfter = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();

        Metrics m = new Metrics();
        m.algorithmName = getName(); m.n = n;
        m.timeNano = end - start; m.memoryUsedBytes = Math.max(0, memAfter - memBefore);
        m.recursiveCalls = recursiveCalls; m.solutionsFound = solutions;
        m.podas = podas; // Salva a métrica
        return m;
    }

    /**
     * Busca recursiva em profundidade operando inteiramente sobre máscaras
     * de bits. Cada bit ligado em {@code pos} representa uma coluna livre
     * para a linha atual; a cada iteração do laço, extrai-se o bit menos
     * significativo e propaga-se as três máscaras para a próxima linha
     * ({@code ld} desloca para a esquerda, {@code rd} desloca para a direita).
     *
     * @param rowMask máscara com as colunas já ocupadas por rainhas
     * @param ld      máscara das diagonais "esquerda" bloqueadas para a linha atual
     * @param rd      máscara das diagonais "direita" bloqueadas para a linha atual
     */
    private void solveNQueens(int rowMask, int ld, int rd) {
        recursiveCalls++;
        if (rowMask == done) {
            solutions++;
            return;
        }

        // 'pos' contém apenas os bits das casas que ESTÃO LIVRES
        int pos = done & (~(rowMask | ld | rd));

        // O número de podas é o total de casas na linha (N) menos as casas que estão livres (bits 1)
        podas += (nBoard - Integer.bitCount(pos));

        while (pos > 0) {
            int bit = pos & -pos;
            pos -= bit;
            solveNQueens(rowMask | bit, (ld | bit) << 1, (rd | bit) >> 1);
        }
    }
}