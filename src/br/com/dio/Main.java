package br.com.dio;

import br.com.dio.model.Board;
import br.com.dio.model.Space;

import java.util.*;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;


public class Main {
    
    private final static Scanner scanner = new Scanner(System.in);

    private static Board board;

    private final static int BOARD_LIMIT = 9;

    public static void main(String[] args) {

        boolean[][] randomPositions = new boolean[BOARD_LIMIT][BOARD_LIMIT];

        for (int i = 0 ; i < BOARD_LIMIT; i++){
            for(int j = 0; j < BOARD_LIMIT; j++){

                randomPositions[i][j] = true;

            }
        }

        final var matrixTrueFalse = removeKDigits(randomPositions, 45);
        final var generatedSudoku = generateSudoku();

       // System.out.print(Arrays.deepToString(generatedSudoku));


        final Map<String,String> positions = new HashMap<>();

        for(int i = 0 ; i < BOARD_LIMIT; i++){

            for(int j = 0; j < BOARD_LIMIT; j++){

                positions.put(i+","+j, generatedSudoku[i][j]+","+matrixTrueFalse[i][j]);

            }


        }

        var option = -1;
        while(true){

            System.out.println("Selecione uma das opções a seguir");
            System.out.println("1 - Iniciar novo Jogo");
            System.out.println("2 - Colocar um novo numero");
            System.out.println("3 - Remover um numero");
            System.out.println("4 - Visualizar Jogo atual");
            System.out.println("5 - Verificar Status do Jogo");
            System.out.println("6 - Limpar jogo");
            System.out.println("7 - Finalizar Jogo");

            option = scanner.nextInt();

            switch(option){

                case 1 -> startGame(positions);
                case 2 -> inputNumber();
                case 3 -> removeNumber();
                case 4 -> showCurrentGame();
                case 5 -> showGameStatus();
                case 6 -> clearGame();
                case 7 -> finishGame();
                case 8 -> System.exit(0);
                default -> System.out.println("Opção Invalida...selecione uma das opções do menu");

            }


        }

    }

    private static void finishGame() {
    }

    private static void clearGame() {
    }

    private static void showGameStatus() {
    }

    private static void showCurrentGame() {
    }

    private static void removeNumber() {

        verifyIfGameNotStarted();

        System.out.println("Informe a coluna em que o numero será removido:");
        var col = runUntilGetValidNumber(0,8);
        System.out.println("Informe a linha em que o numero será removido:");
        var row = runUntilGetValidNumber(0,8);
        System.out.printf("Informe o numero que vai sair na posição (%s, %s)\n", col , row);

        if(!board.clearValue(col, row)){

            System.out.printf("A posicao (%s, %s) tem um valor fixo\n", col, row);


        }

    }

    private static void inputNumber() {


        verifyIfGameNotStarted();

        System.out.println("Informe a coluna em que o numero será inserido:");
        var col = runUntilGetValidNumber(0,8);
        System.out.println("Informe a linha em que o numero será inserido:");
        var row = runUntilGetValidNumber(0,8);
        System.out.printf("Informe o numero que vai entrar na posição (%s, %s)\n", col , row);
        var value = runUntilGetValidNumber(1,9);
        if(!board.changeValue(col, row, value)){

            System.out.printf("A posicao (%s, %s) tem um valor fixo\n", col, row);


        }

    }

    private static void startGame(final Map<String, String> positions) {

        verifyIfGameIsStarted();

        List<List<Space>> spaces = new ArrayList<>();
        for(int i = 0; i < BOARD_LIMIT; i++){
            spaces.add(new ArrayList<>());
            for(int j = 0; j < BOARD_LIMIT; j++){
                var positionConfig = positions.get("%s,%s".formatted(i,j));
                var expected = Integer.parseInt(positionConfig.split(",")[0]);
                var fixed = Boolean.getBoolean(positionConfig.split(",")[1]);
                var currentSpace = new Space(expected, fixed);
                spaces.get(i).add(currentSpace);
            }
        }
        board = new Board(spaces);
        System.out.println("O jogo está pronto para começar");

    }


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

    private static boolean[][] removeKDigits(boolean [][] randomPositions, int k){


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

    private static int runUntilGetValidNumber(final int min, final int max){

       var current = scanner.nextInt();

       while(current < min || current > max){

           System.out.printf("Informe um numero entre %s e %s\n", min, max);
           current = scanner.nextInt();

       }

       return current;


    }

    private static void verifyIfGameNotStarted(){

        if(isNull(board)){

            System.out.println("O Jogo não foi iniciado");
            return;

        }

    }

    private static void verifyIfGameIsStarted(){

        if(nonNull(board)){

            System.out.println("O Jogo já foi iniciado");
            return;

        }
    }
}
