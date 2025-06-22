package br.com.dio.ui.custom.screen;

import br.com.dio.model.Space;
import br.com.dio.service.BoardService;
import br.com.dio.service.NotifierService;
import br.com.dio.ui.custom.button.CheckGameStatusButton;
import br.com.dio.ui.custom.button.FinishGameButton;
import br.com.dio.ui.custom.button.NewGameButton;
import br.com.dio.ui.custom.button.ResetButton;
import br.com.dio.ui.custom.frame.MainFrame;
import br.com.dio.ui.custom.input.NumberText;
import br.com.dio.ui.custom.panel.MainPanel;
import br.com.dio.ui.custom.panel.SudokuSector;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static br.com.dio.config.GameConfig.gameConfig;
import static br.com.dio.enuns.HasErrorsEnum.HAS_ERRORS;
import static br.com.dio.enuns.HasErrorsEnum.NO_ERRORS;
import static br.com.dio.service.event.enuns.EventEnum.CLEAR_SPACE;
import static javax.swing.JOptionPane.*;

public class MainScreen {

    private final static Dimension dimension = new Dimension(600,600);

    private BoardService boardService;
    private final NotifierService notifierService;

    private JButton finishGameButton;
    private JButton checkGameStatusButton;
    private JButton resetButton;
    private JButton newGameButton;
    //private final JPanel mainPanel = new MainPanel(dimension);
    //private final JFrame mainFrame = new MainFrame(dimension, mainPanel);


    public MainScreen(final Map<String, String> gameConfig) {

        this.boardService = new BoardService(gameConfig);
        this.notifierService = new NotifierService();

    }


    private void addNewGameButton(JPanel mainPanel, JFrame mainFrame) {

        newGameButton = new NewGameButton(e -> {

            var dialogResult = showConfirmDialog(
                    null,
                    "Deseja iniciar um novo jogo?",
                    "Novo jogo",
                    YES_NO_OPTION,
                    QUESTION_MESSAGE
            );

            if (dialogResult == 0) {
                startNewGame(mainFrame);
            }
        });


        mainPanel.add(newGameButton);


    }



    private void startNewGame(JFrame mainFrame){

        JPanel mainPanel = new MainPanel(dimension);
        mainFrame.dispose();
        mainFrame = new MainFrame(dimension, mainPanel);
        boardService = new BoardService(gameConfig());


        mainPanel = getPanelSpaces(boardService,mainPanel);

        addNewGameButton(mainPanel, mainFrame);
        addResetButton(mainPanel);
        addCheckGameStatusButton(mainPanel);
        addFinishGameButton(mainPanel, mainFrame);
        mainFrame.revalidate();
        mainFrame.repaint();

    }

    public JPanel getPanelSpaces(BoardService boardService, JPanel mainPanel){

        for (int r = 0; r < 9; r+=3) {
            var endRow = r + 2;
            for (int c = 0; c < 9; c+=3) {
                var endCol = c + 2;
                var spaces = getSpacesFromSector(boardService.getSpaces(), c, endCol, r, endRow);
                JPanel sector = generateSection(spaces);
                mainPanel.add(sector);
            }
        }

        return mainPanel;

    }



    public void buildMainScreen(){

        JPanel mainPanel = new MainPanel(dimension);
        JFrame mainFrame = new MainFrame(dimension, mainPanel);

        mainPanel = getPanelSpaces(boardService,mainPanel);

        addNewGameButton(mainPanel, mainFrame);
        addResetButton(mainPanel);
        addCheckGameStatusButton(mainPanel);
        addFinishGameButton(mainPanel, mainFrame);
        mainFrame.revalidate();
        mainFrame.repaint();
    }



    private void addFinishGameButton(JPanel mainPanel, JFrame mainFrame) {

        finishGameButton = new FinishGameButton(e -> {
            if (boardService.gameIsFinished()){
                showMessageDialog(null, "Parabéns você concluiu o jogo");
                resetButton.setEnabled(false);
                checkGameStatusButton.setEnabled(false);
                finishGameButton.setEnabled(false);
                newGameButton.setEnabled(true);
            } else {
                var message = "Seu jogo está incompleto deseja mesmo encerrar?";
                var dialogResult = showConfirmDialog(
                        null,
                        message,
                        "Encerrar o Jogo",
                        YES_NO_OPTION,
                        QUESTION_MESSAGE
                );
                if (dialogResult == 0){
                    mainFrame.dispose();
                }
            }
        });
        mainPanel.add(finishGameButton);

    }

    private void addCheckGameStatusButton(JPanel mainPanel) {

        checkGameStatusButton = new CheckGameStatusButton(e -> {
            var hasErrors = boardService.hasErrors();
            var gameStatus = boardService.getStatus();
            var message = gameStatus.getLabel();
            message += hasErrors ? HAS_ERRORS.getLabel() : NO_ERRORS.getLabel();
            showMessageDialog(null, message);
        });
        mainPanel.add(MainScreen.this.checkGameStatusButton);


    }

    private void addResetButton(JPanel mainPanel) {

        resetButton = new ResetButton(e ->{
            var dialogResult = showConfirmDialog(
                    null,
                    "Deseja realmente reiniciar o jogo?",
                    "Limpar o jogo",
                    YES_NO_OPTION,
                    QUESTION_MESSAGE
            );
            if (dialogResult == 0){
                boardService.reset();
                notifierService.notify(CLEAR_SPACE);
            }
        });
        mainPanel.add(resetButton);

    }

    private JPanel generateSection(List<Space> spaces) {

        List<NumberText> fields = new ArrayList<>(spaces.stream().map(NumberText::new).toList());
        fields.forEach(t -> notifierService.subscribe(CLEAR_SPACE, t));
        return new SudokuSector(fields);

    }

    private List<Space> getSpacesFromSector(List<List<Space>> spaces, int initCol, int endCol, int initRow, int endRow) {

        List<Space> spaceSector = new ArrayList<>();
        for (int r = initRow; r <= endRow; r++) {
            for (int c = initCol; c <= endCol; c++) {
                spaceSector.add(spaces.get(c).get(r));
            }
        }
        return spaceSector;


    }

}
