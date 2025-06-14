package br.com.dio.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;


public class SudokuUtils {

    private static final int BOARD_LIMIT = 9;

    public static int[][] generateSudoku(){

        int[][] coordinates = new int[BOARD_LIMIT][BOARD_LIMIT];


        fillDiagonal(coordinates);
        fillRemaining(coordinates, 0, 3);

        return coordinates;

    }

    private static void fillDiagonal(int[][] coordinates){

        for(int i = 0; i < BOARD_LIMIT; i+=3){

            fillBox(coordinates, i, i);

        }

    }

    private static void fillBox(int[][] coordinates, int rowStart, int colStart){

        List<Integer> numbers = new ArrayList<>();

        for(int i = 1; i <= BOARD_LIMIT; i++){

            numbers.add(i);

        }

        Collections.shuffle(numbers);
        int numIndex = 0;

        for(int i = 0; i < 3; i++){

            for(int j = 0 ; j < 3; j++){

                coordinates[rowStart+i][colStart+j] = numbers.get(numIndex++);

            }

        }


    }

    private static boolean isSafe(int[][] coordinates, int row, int col, int num){

        for(int c = 0; c < BOARD_LIMIT; c++){

            if(coordinates[row][c] == num){

                return false;

            }

        }

        for(int r = 0; r < BOARD_LIMIT; r++){

            if(coordinates[r][col] == num){

                return false;

            }

        }

        int boxRowStart = row - row % 3;
        int boxColStart = col - col % 3;

        for(int r = boxRowStart; r < boxRowStart + 3; r++){

            for(int c = boxColStart; c < boxColStart + 3; c++){

                if(coordinates[r][c] == num){

                    return false;
                }

            }

        }

        return true;

    }

    private static boolean fillRemaining(int[][] coordinates, int row, int col){

        if(col >= BOARD_LIMIT && row < BOARD_LIMIT - 1){

            row = row + 1;
            col = 0;

        }
        if(row >= BOARD_LIMIT && col >= BOARD_LIMIT){

            return true;

        }
        if(row < 3){
            if(col < 3){
                col = 3;
            }
        }else if(row < BOARD_LIMIT - 3){
            if(col == (int) (row / 3) * 3){
                col = col + 3;
            }

        }else{
            if(col == BOARD_LIMIT - 3){
                row = row + 1;
                col  = 0;
                if(row >= BOARD_LIMIT){
                    return true;
                }

            }
        }

        for(int num = 1; num <= BOARD_LIMIT;num++){
            if(isSafe(coordinates,row,col,num)){
                coordinates[row][col] = num;
                if(fillRemaining(coordinates, row, col + 1)){
                    return true;
                }
                coordinates[row][col] = 0;

            }

        }
        return false;

    }

    public static boolean[][] removeKDigits(boolean[][] randomPositions, int k){


        Random random = new Random();
        int count = 0;
        while(count < k){

            int row = random.nextInt(BOARD_LIMIT);
            int col = random.nextInt(BOARD_LIMIT);

            if(randomPositions[row][col]) {
                randomPositions[row][col] = false;
                count++;
            }

        }

        return randomPositions;
    }




}
