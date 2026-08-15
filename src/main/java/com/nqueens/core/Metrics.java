package com.nqueens.core;

/**
 * Classe que atua como modelo de dados para armazenar os resultados e métricas
 * coletadas durante a execução de cada algoritmo.
 *
 * @author georis
 * @version 1.0
 * @since 15/08/2026
 */
public class Metrics {
    public String algorithmName;
    public int n;
    public long timeNano;
    public long memoryUsedBytes;
    public long recursiveCalls;
    public long solutionsFound;
    public int[] firstSolution; // Para desenhar o tabuleiro
}