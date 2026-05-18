package view;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Principal extends Application{

    @Override
    public void start(Stage stage) {
        // Cria a tela de login
        TelaCadastroFuncionario telaLogin = new TelaCadastroFuncionario();
        Scene scene = telaLogin.getScene();

        // Configura o Stage (janela)
        stage.setTitle("Sistema de Pontos");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}