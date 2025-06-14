package br.com.dio;

import static br.com.dio.config.GameConfig.*;

import br.com.dio.config.GameConfig;
import br.com.dio.model.Board;
import br.com.dio.ui.custom.screen.MainScreen;


public class Main {

    public static void main(String[] args) {

        var mainsScreen = new MainScreen(gameConfig());
        mainsScreen.buildMainScreen();

    }

}
