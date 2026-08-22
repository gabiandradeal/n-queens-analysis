package com.nqueens.benchmark;

import com.nqueens.core.Metrics;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * Utilitário responsável por consolidar e exportar os resultados dos benchmarks
 * dos algoritmos do problema das N-Rainhas em formato CSV.
 * 
 * <p>Esta classe processa métricas coletadas em múltiplas rodadas de execução,
 * calcula médias de tempo e consumo de memória, e gera arquivos tabulares
 * separados para cada algoritmo avaliado.</p>
 * 
 * @author Seu Nome
 * @version 1.0
 * @since 1.0
 */
public class CsvExporter {

    /**
     * Calcula as médias das execuções e exporta os dados para arquivos CSV individuais por algoritmo.
     *
     * <p>Para cada algoritmo pré-definido (Backtracking Clássico, Branch and Bound, Bitmask) e para cada
     * tamanho de tabuleiro {@code N} no intervalo fornecido:</p>
     * <ul>
     *   <li>Calcula a média de tempo de execução (convertido para milissegundos).</li>
     *   <li>Calcula a média de memória utilizada (convertida para kilobytes).</li>
     *   <li>Extrai contagens determinísticas (chamadas recursivas, podas e soluções encontradas).</li>
     *   <li>Grava os dados no diretório {@code resultados_csv/} com formato numérico pt-BR (vírgula decimal).</li>
     * </ul>
     *
     * @param allRounds Lista de rodadas de benchmark, onde cada item contém as métricas coletadas naquela rodada.
     * @param minN      Tamanho mínimo do tabuleiro {@code N} a ser considerado na exportação.
     * @param maxN      Tamanho máximo do tabuleiro {@code N} a ser considerado na exportação.
     */
    public static void exportAverages(List<List<Metrics>> allRounds, int minN, int maxN) {
        String[] algos = {"Backtracking Classico", "Branch and Bound", "Bitmask"};

        // Pega a quantidade exata de rodadas (tentativas) executadas
        int tentativas = allRounds.size();

        String diretorio = "resultados_csv";

        // Cria a pasta no caso ela ainda não exista
        File pasta = new File(diretorio);
        if (!pasta.exists()) {
            pasta.mkdirs();
        }

        // Adiciona o número de tentativas dinamicamente no nome do arquivo
        String[] fileNames = {
                diretorio + "/medias_Backtracking_" + tentativas + "_tentativas.csv",
                diretorio + "/medias_BranchAndBound_" + tentativas + "_tentativas.csv",
                diretorio + "/medias_Bitmask_" + tentativas + "_tentativas.csv"
        };

        System.out.println("\n📊 Calculando médias das " + tentativas + " rodadas e gerando CSVs...");

        for (int i = 0; i < algos.length; i++) {
            String targetAlg = algos[i];
            String filename = fileNames[i];

            try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
                // Cabeçalho padronizado para as planilhas
                writer.println("N;Algoritmo;TempoMedio_ms;MemoriaMedia_KB;Chamadas_Recursivas;Podas;Solucoes");

                for (int n = minN; n <= maxN; n++) {
                    double totalTime = 0;
                    double totalMem = 0;
                    long chamadas = 0;
                    long solucoes = 0;
                    int count = 0;
                    long podas = 0;

                    // Varre todas as rodadas coletando os dados específicos deste N e deste Algoritmo
                    for (List<Metrics> round : allRounds) {
                        for (Metrics m : round) {
                            if (m.n == n && m.algorithmName.equals(targetAlg)) {
                                totalTime += (m.timeNano / 1_000_000.0);
                                totalMem += (m.memoryUsedBytes / 1024.0);
                                chamadas = m.recursiveCalls;
                                solucoes = m.solutionsFound;
                                podas = m.podas; // <-- Atualizado para usar a variável 'podas'
                                count++;
                            }
                        }
                    }

                    // Calcula e escreve a média
                    if (count > 0) {
                        double avgTime = totalTime / count;
                        double avgMem = totalMem / count;
                        // não tem média das podas, chamadas e soluções, pois são constantes a cada execução
                        writer.printf(new java.util.Locale("pt", "BR"), "%d;%s;%.2f;%.2f;%d;%d;%d\n",
                                n, targetAlg, avgTime, avgMem, chamadas, podas, solucoes);
                    }
                }
                System.out.println("Arquivo exportado com sucesso: " + filename);
            } catch (IOException e) {
                System.err.println("Erro ao exportar " + filename + ": " + e.getMessage());
            }
        }
    }
}