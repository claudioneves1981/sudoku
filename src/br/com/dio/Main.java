package br.com.dio;

import br.com.dio.model.Board;

import java.util.*;


public class Main {
    
    private final static Scanner scanner = new Scanner(System.in);

    private static Board board;

    private final static int BOARD_LIMIT = 9;

    public static void main(String[] args) {

        final var positions = generateSudoku();
        System.out.print(positions);

        //teste board
        /*
        Space space = new Space(1, true);
        Space space2 = new Space(2, false);
        List<Space> listSpace = new ArrayList<>();
        List<List<Space>> spaces = new ArrayList<>();
        listSpace.add(space);
        listSpace.add(space2);
        spaces.add(listSpace);
        Board board = new Board(spaces);
        boolean teste = board.clearValue(0,1);
        System.out.print(teste);
        */

    }


    public static Map<String, String> generateSudoku(){

        Map<String,String > coordinates = new HashMap<>(81);
        fillDiagonal(coordinates);
        fillRemaining(coordinates, 0, 3);
        removeKDigits(coordinates, 45);
        return coordinates;

    }

    private static void fillDiagonal(Map<String, String> coordinates){

        for(int i = 0; i < BOARD_LIMIT; i++){

            fillBox(coordinates, i, i);

        }

    }

    private static void fillBox(Map<String, String> coordinates, int rowStart, int colStart){

        List<Integer> numbers = new ArrayList<>();

        for(int i = 1; i <= BOARD_LIMIT; i++){

            numbers.add(i);

        }

        Collections.shuffle(numbers);
        int numIndex = 0;

        for(int i = 0; i < 3; i++){

            for(int j = 0 ; j < 3; j++){

                coordinates.put((rowStart)+","+(numIndex), numbers.get(numIndex++)+","+true);

            }

        }


    }

    private static boolean isSafe(Map<String, String> coordinates, int row, int col, int num){

        for(int c = 0; c < BOARD_LIMIT; c++){

            if(coordinates.get(row+","+c).contains(String.valueOf(num))){

                return false;

            }

        }

        for(int r = 0; r < BOARD_LIMIT; r++){

            if(coordinates.get(r+","+col).contains(String.valueOf(num))){

                return false;

            }

        }

        int boxRowStart = row  - row % 3;
        int boxColStart = col -  col % 3;

        for(int r = boxRowStart; r < boxRowStart + 3; r++){

            for(int c = boxColStart; c < boxColStart + 3; c++){

                if(coordinates.get(r+","+c).contains(String.valueOf(num))){

                    return false;
                }

            }

        }

        return true;

    }

    private static boolean fillRemaining(Map<String, String> coordinates, int row, int col){

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
                coordinates.put(row+","+col, num+","+true);
                if(fillRemaining(coordinates, row, col + 1)){
                    return true;
                }
                coordinates.put(row+","+col, num+","+false);

            }

        }
        return false;

    }

    private static void removeKDigits(Map<String, String> coordinates, int k){


        Random random = new Random();
        int count = 0;
        while(count < k){

            int row = random.nextInt(BOARD_LIMIT);
            int col = random.nextInt(BOARD_LIMIT);

            if(coordinates.get(row+","+col).contains("true")) {
                String value = coordinates.get(row + "," + col);
                String[] newString = value.split(",");
                newString[1] = "false";
                value = newString[0]+","+newString[1];
                coordinates.replace(row+","+col, value);
                count++;
            }

        }
    }
}
