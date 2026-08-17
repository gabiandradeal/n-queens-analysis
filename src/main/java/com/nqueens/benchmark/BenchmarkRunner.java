package com.nqueens.benchmark;
import java.util.ArrayList;
import java.util.List;

import com.nqueens.algorithms.Backtracking;
import com.nqueens.algorithms.Bitmask;
import com.nqueens.algorithms.BranchAndBound;
import com.nqueens.core.Metrics;
import com.nqueens.core.NQueensAlgorithm;

public class BenchmarkRunner {
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