package com.nqueens;

import com.nqueens.benchmark.*;
import com.nqueens.ui.*;
import com.nqueens.algorithms.*;

import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Gerencia as interações no terminal através do loop do menu principal.
 *
 * @author georis
 * @version 1.2
 * @since 18/08/2026
 */
public class Main {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        boolean exit = false;

        while (!exit) {
            try {
                System.out.println("\n<----- \uD83D\uDC51 PROJETO N-QUEENS \uD83D\uDC51 ----->");
                System.out.println("1. Executar um algoritmo (ver tabuleiro e métricas)");
                System.out.println("2. Executar Benchmark (Dashboard) e Exportar CSVs de Média");
                System.out.println("3. Teste de Viabilidade (Descobrir ponto de ruptura)"); // NOVO: Adicionado ao menu!
                System.out.println("0. Sair");
                System.out.print("Escolha: ");
                int op = sc.nextInt();

                if (op == 0) break;

                if (op == 1) {
                    System.out.print("Digite N (tamanho do tabuleiro - Máximo 16 recomendado): ");
                    int n = sc.nextInt();

                    if (n >= 32) {
                        System.out.println("ERRO: O algoritmo Bitmask suporta no máximo N=31 devido ao limite de 32 bits da variável 'int' no Java.");
                        System.out.println("Além disso, qualquer N acima de 16 vai demorar dias para rodar. Tente um número menor.\n");
                        continue;
                    }
                    if (n <= 0) {
                        System.out.println("\nERRO: N deve ser maior que zero.\n");
                        continue;
                    }

                    System.out.println("Qual algoritmo? (1-Backtracking, 2-BranchBound, 3-Bitmask)");
                    int algOp = sc.nextInt();

                    if (algOp < 1 || algOp > 3) {
                        System.out.println("\nERRO >> Opção de algoritmo inválida.\n");
                        continue;
                    }

                    var alg = algOp == 1 ? new Backtracking() : (algOp == 2 ? new BranchAndBound() : new Bitmask());
                    System.out.println("\nProcessando");
                    var m = alg.solve(n);

                    System.out.println("\nExecução Concluída");
                    System.out.println("Algoritmo: " + m.algorithmName);
                    System.out.println("Soluções encontradas: " + m.solutionsFound);
                    System.out.println("Tempo de execução: " + (m.timeNano / 1_000_000.0) + " ms");
                    System.out.println("Memória utilizada: " + (m.memoryUsedBytes / 1024.0) + " KB");
                    System.out.println("Chamadas Recursivas (Estados): " + m.recursiveCalls);
                    System.out.println("Podas Realizadas: " + m.podas);

                    Board.printBoard(m.firstSolution, m.algorithmName, n);

                } else if (op == 2) {
                    System.out.print("Executar benchmark do N = 4 até N = (Máximo recomendado = 15): ");
                    int maxN = sc.nextInt();

                    if (maxN >= 32) {
                        System.out.println("\nERRO: Para o benchmark, o N máximo absoluto é 31, mas recomendamos no máximo 15 para não travar sua máquina.\n");
                        continue;
                    }

                    if (maxN < 4) {
                        System.out.println("\nERRO: O benchmark começa a partir de N=4. Por favor, digite um valor maior ou igual a 4.\n");
                        continue;
                    }

                    System.out.print("Quantas rodadas de teste deseja executar para tirar a média?  ");
                    int rounds = sc.nextInt();

                    if (rounds <= 0) {
                        System.out.println("\nERRO: O número de rodadas deve ser maior que zero.\n");
                        continue;
                    }

                    List<List<com.nqueens.core.Metrics>> allRounds = new ArrayList<>();

                    for (int r = 1; r <= rounds; r++) {
                        System.out.println("\n<--------------------------------------------------------->");
                        System.out.println("⏳ EXECUTANDO RODADA " + r + " DE " + rounds + "...");
                        System.out.println("<---------------------------------------------------------->");

                        var results = BenchmarkRunner.run(4, maxN);
                        allRounds.add(results);
                    }

                    CsvExporter.exportAverages(allRounds, 4, maxN);

                } else if (op == 3) {
                    System.out.println("\nQual algoritmo? (1-Backtracking, 2-BranchBound, 3-Bitmask)");
                    int algOp = sc.nextInt();

                    if (algOp < 1 || algOp > 3) {
                        System.out.println("\nERRO >> Opção de algoritmo inválida.\n");
                        continue;
                    }

                    System.out.print("Defina o limite em minutos (Recomendado: 1-3): ");
                    int minutos = sc.nextInt();

                    if (minutos <= 0) {
                        System.out.println("\nERRO: O tempo deve ser maior que zero.\n");
                        continue;
                    }

                    var alg = algOp == 1 ? new Backtracking() : (algOp == 2 ? new BranchAndBound() : new Bitmask());

                    System.out.println("\nIniciando Teste de Viabilidade (" + alg.getName() + ")");
                    System.out.println("⏳ Limite: " + minutos + " minuto(s). O laço parará após a primeira execução que ultrapassar o limite");
                    System.out.println("-".repeat(80));

                    long limiteNano = minutos * 60L * 1_000_000_000L;
                    int currentN = 4;
                    boolean exit2 = false;

                    while (!exit2) {

                        if (currentN >= 32) {
                            System.out.println("-".repeat(80));
                            System.out.println("Limite Atingido");
                            System.out.println("O algoritmo atingiu o N = 31. O teste foi abortado para evitar um overflow silencioso de 32 bits no Java.");
                            break;
                        }

                        System.out.print("N = " + String.format("%-2d", currentN) + " ... ");

                        long start = System.nanoTime();
                        var m = alg.solve(currentN);
                        long end = System.nanoTime();

                        long decorridoNano = end - start;
                        double decorridoSegundos = decorridoNano / 1_000_000_000.0;

                        // Imprime apenas o tempo de forma mais limpa
                        System.out.printf(Locale.forLanguageTag("pt-BR"), "Concluído em %.2f segundos\n", decorridoSegundos);

                        if (decorridoNano > limiteNano) {
                            System.out.println("-".repeat(80));
                            System.out.println("LIMIAR ULTRAPASSADO");
                            System.out.println("O algoritmo levou " + String.format(Locale.forLanguageTag("pt-BR"), "%.2f", (decorridoSegundos / 60.0)) + " minutos no N = " + currentN);

                            // Adicionada a impressão dos dados completos do caso de quebra
                            System.out.println("\nDADOS DO N = " + currentN + ":");
                            System.out.printf(Locale.forLanguageTag("pt-BR"), "Memória utilizada: %.2f KB\n", (m.memoryUsedBytes / 1024.0));
                            System.out.println("Soluções encontradas: " + m.solutionsFound);
                            System.out.println("Chamadas Recursivas: " + m.recursiveCalls);
                            System.out.println("Podas Realizadas: " + m.podas);

                            System.out.println("\nCONCLUSÃO: O " + alg.getName() + " torna-se inviável para uso prático a partir de N = " + currentN + ".");
                            break;
                        }

                        currentN++;
                    }

                } else {
                    System.out.println("\nERRO: Opção inválida.\n");
                }

            } catch (InputMismatchException e) {
                System.out.println("\nERRO: Entrada inválida. Por favor, digite apenas números inteiros.\n");
                sc.nextLine();
            } catch (Exception e) {
                System.out.println("\nERRO INESPERADO: " + e.getMessage() + "\n");
                sc.nextLine();
            }
        }
        sc.close();
    }
}
