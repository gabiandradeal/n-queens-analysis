package com.nqueens.algorithms;
import com.nqueens.core.*;

/**
 * Implementação de Branch and Bound para o problema das N-Rainhas.
 * <p>
 * Em relação ao {@link Backtracking} clássico, essa versão traz duas
 * otimizações:
 * <ol>
 *   <li><b>Checagem O(1):</b> arrays booleanos ({@code cols}, {@code diag1},
 *       {@code diag2}) marcam colunas e diagonais ocupadas, eliminando a
 *       varredura O(n) sobre rainhas anteriores usada no Backtracking.</li>
 *   <li><b>Poda de ramo (bound):</b> antes de descer para a próxima linha,
 *       {@link #existeColunaViavel} verifica se ainda existe alguma coluna
 *       livre para ela. Se não existir, o ramo inteiro é cortado ali,
 *       sem precisar visitar nó a nó a subárvore inviável — diferença que
 *       o Backtracking e o Bitmask não fazem.</li>
 * </ol>
 * Por causa da poda de ramo, o número de chamadas recursivas e de podas
 * observado é genuinamente menor que o do Backtracking, não apenas mais
 * rápido em tempo de execução.
 *
 * @author Gabi
 * @version 1.0
 * @since 17/08/2026
 */
public class BranchAndBound implements NQueensAlgorithm {

    /** Contador de chamadas recursivas realizadas durante a busca. */
    private long recursiveCalls = 0;

    /** Contador de soluções válidas encontradas. */
    private long solutions = 0;

    /** Contador de podas (locais, por coluna, e de ramo inteiro, via bound). */
    private long podas = 0;

    /** Guarda a primeira solução válida encontrada, para fins de validação/visualização. */
    private int[] firstSolution = null;

    /** Marca as colunas atualmente ocupadas por alguma rainha. */
    private boolean[] cols;

    /** Marca as diagonais principais ({@code row + col}) atualmente ocupadas. */
    private boolean[] diag1;

    /** Marca as diagonais secundárias ({@code row - col + n - 1}) atualmente ocupadas. */
    private boolean[] diag2;

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() { return "Branch and Bound"; }

    /**
     * Executa o algoritmo de Branch and Bound para um tabuleiro N×N,
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
        cols = new boolean[n];
        diag1 = new boolean[2 * n - 1];
        diag2 = new boolean[2 * n - 1];
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
     * Busca recursiva em profundidade com checagem O(1) via lookup tables e
     * poda antecipada de ramo (bound). Para cada coluna livre na linha atual,
     * posiciona a rainha e só desce para a próxima linha se
     * {@link #existeColunaViavel} confirmar que ela ainda é alcançável;
     * caso contrário, o ramo é podado sem visitar a subárvore.
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
            int d1 = row + col;
            int d2 = row - col + n - 1;
            // Se estiver livre, avança.
            if (!cols[col] && !diag1[d1] && !diag2[d2]) {
                board[row] = col;
                cols[col] = diag1[d1] = diag2[d2] = true;

                // BOUND: antes de descer, verifica se a PRÓXIMA linha ainda tem alguma coluna viável dado o estado atual do tabuleiro
                // Se não tiver, o ramo inteiro é inviável e é cortado aqui, sem sequer criar a chamada recursiva para a próxima linha
                if (row + 1 == n || existeColunaViavel(row + 1, n)) {
                    solveNQueens(board, row + 1, n);
                } else {
                    // Poda de RAMO completo: uma única poda aqui evita toda uma
                    // subárvore que o Backtracking/Bitmask teriam que visitar
                    // nó a nó para descobrir a mesma coisa.
                    podas++;
                }

                cols[col] = diag1[d1] = diag2[d2] = false;
            } else {
                // poda de COLUNA: mesma poda "local" que backtracking e bitmask fazem
                podas++;
            }
        }
    }

    /**
     * Verifica, em O(n), se existe ao menos uma coluna livre para a linha
     * {@code row}, dado o estado atual de {@code cols}/{@code diag1}/{@code diag2}.
     * Não altera nenhum estado (checagem somente-leitura).
     * <p>
     * É essa verificação que constitui o "bound" desta implementação: uma
     * checagem de viabilidade (constraint propagation de uma linha à frente),
     * e não uma função de custo comparada a um ótimo, já que N-Queens não é
     * um problema de otimização.
     *
     * @param row linha a ser verificada
     * @param n   tamanho do tabuleiro
     * @return {@code true} se existir ao menos uma coluna livre para {@code row}
     */
    private boolean existeColunaViavel(int row, int n) {
        for (int col = 0; col < n; col++) {
            int d1 = row + col;
            int d2 = row - col + n - 1;
            if (!cols[col] && !diag1[d1] && !diag2[d2]) {
                return true;
            }
        }
        return false;
    }
}