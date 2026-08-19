package com.nqueens.ui;

/**
 * Utilitário de interface para exibição textual do tabuleiro das N-Rainhas
 * no console, marcando as rainhas e as casas claras/escuras com emojis.
 *
 * @author Suelle
 * @version 1.0
 * @since 17/08/2026
 */
public class Board {

    /**
     * Imprime no console uma representação visual do tabuleiro para a
     * solução informada, com uma rainha (👑) por linha e as demais casas
     * alternando entre claras (⬜) e escuras (⬛).
     *
     * @param board         array onde {@code board[i]} guarda a coluna ocupada
     *                      pela rainha da linha {@code i}, ou {@code null}
     *                      caso não haja solução para exibir
     * @param algorithmName nome do algoritmo executado, usado para explicar o
     *                      motivo de {@code board} ser {@code null}
     * @param n             tamanho do tabuleiro usado na execução
     */
    public static void printBoard(int[] board, String algorithmName, int n) {
        if (board == null) {
            if ("Bitmask".equals(algorithmName)) {
                System.out.println("\n⚠️ O algoritmo selecionado (Bitmask) não utiliza matrizes ou vetores físicos, por isso, nenhuma representação visual disponível");
            } else {
                System.out.println("\n⚠️ Não existe solução para N = " + n + ", por isso, nenhuma representação visual disponível");
            }
            return;
        }
        System.out.println("\nExibindo uma solução válida:\n");
        for (int r = 0; r < n; r++) {
            for (int c = 0; c < n; c++) {
                if (board[r] == c) {
                    System.out.print("👑 ");
                } else {
                    if ((r + c) % 2 == 0) System.out.print("⬜ ");
                    else System.out.print("⬛ ");
                }
            }
            System.out.println();
        }
        System.out.println();
    }
}