package com.example.simulearn;

import com.example.simulearn.Information.DatabaseHelper;
import com.example.simulearn.Information.SessionManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;


public class Menu extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        DatabaseHelper.initialize();
        SessionManager.tryAutoLogin();

        Font loadedFont = Font.loadFont(getClass().getResourceAsStream("/com/example/simulearn/Fonts/DMSans-Medium.ttf"), 16);

        System.out.println("Font name: " + loadedFont.getName());
        System.out.println("Font family: " + loadedFont.getFamily());
        FXMLLoader fxmlLoader = new FXMLLoader(Menu.class.getResource("/com/example/simulearn/Menu.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1920, 1080);
        stage.setTitle("SimuLearn");
        stage.setScene(scene);

        stage.setMaximized(true);
        stage.show();
    }

}
