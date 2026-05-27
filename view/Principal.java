package view;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Principal extends Application{

    @Override
    public void start(Stage stage) {
        // Inicia na landing page
        TelaLanding landing = new TelaLanding();
        Scene scene = landing.getScene(stage);

        // Configura o Stage (janela)
        stage.setTitle("MistyPoint · Sistema de Pontos");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}