package br.com.dio.config;

import java.util.HashMap;
import java.util.Map;

import static br.com.dio.util.SudokuUtils.generateSudoku;
import static br.com.dio.util.SudokuUtils.removeKDigits;

public class GameConfig {

    private final static int BOARD_LIMIT = 9;

    private final static boolean[][] randomPositions = new boolean[BOARD_LIMIT][BOARD_LIMIT];

    public static Map<String, String> gameConfig(){

        for (int i = 0; i < BOARD_LIMIT; i++) {

            for (int j = 0; j < BOARD_LIMIT; j++) {

                randomPositions[i][j] = true;

            }
        }

        final var matrixTrueFalse = removeKDigits(randomPositions, 45);
        final var generatedSudoku = generateSudoku();

        final Map<String, String> gameConfig = new HashMap<>();

        for (int i = 0; i < BOARD_LIMIT; i++) {

            for (int j = 0; j < BOARD_LIMIT; j++) {

                gameConfig.put(i + "," + j, generatedSudoku[i][j] + "," + matrixTrueFalse[i][j]);

            }

        }

        return gameConfig;
    }
}
