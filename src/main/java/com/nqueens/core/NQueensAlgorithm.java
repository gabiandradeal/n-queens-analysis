package com.nqueens.core;

/**
 * Interface que define o contrato  para todos os algoritmos
 * de resolução do problema das N-Queens👑.
 *
 * @author georis
 * @version 1.0
 * @since 15/08/2026
 */
public interface NQueensAlgorithm {
    String getName();
    Metrics solve(int n);
}