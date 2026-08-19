package com.nqueens.benchmark;
import java.util.ArrayList;
import java.util.List;

import com.nqueens.algorithms.Backtracking;
import com.nqueens.algorithms.Bitmask;
import com.nqueens.algorithms.BranchAndBound;
import com.nqueens.core.Metrics;
import com.nqueens.core.NQueensAlgorithm;

/**
 * Executor de benchmark que roda todos os algoritmos de N-Rainhas
 * ({@link Backtracking}, {@link BranchAndBound} e {@link Bitmask}) para uma
 * faixa de tamanhos de tabuleiro, imprimindo e coletando as métricas de
 * cada execução.
 *
 * @author Suelle
 * @version 1.0
 * @since 17/08/2026
 */
public class BenchmarkRunner {

    /**
     * Executa cada algoritmo de N-Rainhas para todo {@code n} no intervalo
     * {@code [minN, maxN]}, imprimindo uma tabela com tempo, memória e
     * chamadas recursivas de cada execução.
     *
     * @param minN menor tamanho de tabuleiro (N×N) a ser testado
     * @param maxN maior tamanho de tabuleiro (N×N) a ser testado
     * @return lista com as {@link Metrics} de cada execução, na ordem em
     *         que foram realizadas
     */
    public static List<Metrics> run(int minN, int maxN) {
        List<Metrics> results = new ArrayList<>();
        NQueensAlgorithm[] algorithms = {new Backtracking(), new BranchAndBound(), new Bitmask()};

        System.out.println("\nIniciando Benchmark...");
        System.out.printf("%-5s | %-25s | %-12s | %-12s | %-15s\n", "N", "Algoritmo", "Tempo (ms)", "Memória (KB)", "Chamadas Rec.");
        System.out.println("-".repeat(80));

        for (int n = minN; n <= maxN; n++) {
            for (NQueensAlgorithm alg : algorithms) {
                Metrics m = alg.solve(n);
                results.add(m);
                System.out.printf("%-5d | %-25s | %-12.2f | %-12.2f | %-15d\n",
                        n, m.algorithmName, (m.timeNano / 1_000_000.0), (m.memoryUsedBytes / 1024.0), m.recursiveCalls);
            }
        }
        return results;
    }
}