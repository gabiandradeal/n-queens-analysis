package com.nqueens.algorithms;
import com.nqueens.core.*;

/**
 * Implementação clássica de Backtracking para o problema das N-Rainhas.
 * <p>
 * A cada linha, tenta-se posicionar uma rainha em cada coluna, validando
 * a segurança da posição com uma varredura O(n) sobre as rainhas já
 * posicionadas (checagem de coluna e diagonais via {@link #isSafe}).
 * Caso nenhuma coluna seja segura, o algoritmo retrocede (backtrack)
 * para a linha anterior.
 * <p>
 * Serve como baseline de comparação para as versões otimizadas
 * ({@link BranchAndBound} e {@link Bitmask}).
 *
 * @author Gabi
 * @version 1.0
 * @since 17/08/2026
 */
public class Backtracking implements NQueensAlgorithm {

    /** Contador de chamadas recursivas realizadas durante a busca. */
    private long recursiveCalls = 0;

    /** Contador de soluções válidas encontradas. */
    private long solutions = 0;

    /** Contador de podas (tentativas de coluna rejeitadas por conflito). */
    private long podas = 0;

    /** Guarda a primeira solução válida encontrada, para fins de validação/visualização. */
    private int[] firstSolution = null;

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() { return "Backtracking Classico"; }

    /**
     * Executa o algoritmo de Backtracking clássico para um tabuleiro N×N,
     * coletando métricas de tempo, memória e exploração do espaço de busca.
     *
     * @param n tamanho do tabuleiro (N×N) e número de rainhas a posicionar
     * @return {@link Metrics} com os resultados da execução (tempo, memória,
     *         chamadas recursivas, podas, soluções encontradas e a primeira
     *         solução válida)
     */
    @Override
    public Metrics solve(int n) {
        recursiveCalls = 0; solutions = 0; podas = 0; firstSolution = null;
        int[] board = new int[n];

        System.gc();
        long memBefore = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        long start = System.nanoTime();

        solveNQueens(board, 0, n);

        long end = System.nanoTime();
        long memAfter = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();

        Metrics m = new Metrics();
        m.algorithmName = getName(); m.n = n;
        m.timeNano = end - start; m.memoryUsedBytes = Math.max(0, memAfter - memBefore);
        m.recursiveCalls = recursiveCalls; m.solutionsFound = solutions;
        m.podas = podas; // Salva a métrica
        m.firstSolution = firstSolution;
        return m;
    }

    /**
     * Busca recursiva em profundidade: tenta posicionar uma rainha em cada
     * coluna da linha atual, avançando para a próxima linha quando a posição
     * é segura, ou contabilizando uma poda quando não é.
     *
     * @param board array onde {@code board[i]} guarda a coluna ocupada pela
     *              rainha da linha {@code i}
     * @param row   linha atual sendo processada
     * @param n     tamanho do tabuleiro
     */
    private void solveNQueens(int[] board, int row, int n) {
        recursiveCalls++;
        if (row == n) {
            solutions++;
            if (firstSolution == null) firstSolution = board.clone();
            return;
        }
        for (int col = 0; col < n; col++) {
            if (isSafe(board, row, col)) {
                board[row] = col;
                solveNQueens(board, row + 1, n);
            } else {
                podas++; // O isSafe falhou, caminho cortado.
            }
        }
    }

    /**
     * Verifica se é seguro posicionar uma rainha em {@code (row, col)},
     * comparando com todas as rainhas já posicionadas nas linhas anteriores
     * (checagem de mesma coluna e de diagonais). Custo O(row), ou seja, O(n)
     * no pior caso.
     *
     * @param board array com as posições das rainhas já colocadas
     * @param row   linha da posição candidata
     * @param col   coluna da posição candidata
     * @return {@code true} se nenhuma rainha anterior ataca essa posição
     */
    private boolean isSafe(int[] board, int row, int col) {
        for (int i = 0; i < row; i++) {
            if (board[i] == col || Math.abs(board[i] - col) == Math.abs(i - row)) {
                return false;
            }
        }
        return true;
    }
}