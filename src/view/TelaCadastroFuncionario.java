package view;

import java.sql.SQLException;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import model.Funcionario;

public class TelaCadastroFuncionario {

    private TextField tfNome;
    private TextField tfCpf;
    private TextField tfCargo;
    private TextField tfTelefone;
    private TextField tfEmail;
    private PasswordField pfSenha;
    private Label lblMensagem;

    public Scene getScene() {

        VBox root = new VBox(20);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(40));

        root.setStyle("-fx-background-color: linear-gradient(to bottom, #ffffff, #f8f0fc);");

        Label lblTitulo = new Label("CADASTRO DE FUNCIONARIO");
        lblTitulo.setFont(Font.font("Arial", FontWeight.BOLD, 26));
        lblTitulo.setTextFill(Color.PURPLE);

        // NOME
        HBox hboxNome = new HBox(15);
        hboxNome.setAlignment(Pos.CENTER_LEFT);
        Label lblNome = new Label("Nome:");
        lblNome.setPrefWidth(70);
        tfNome = new TextField();
        tfNome.setPrefWidth(250);
        tfNome.setPromptText("Digite o nome");
        hboxNome.getChildren().addAll(lblNome, tfNome);

        // CPF
        HBox hboxCpf = new HBox(15);
        hboxCpf.setAlignment(Pos.CENTER_LEFT);
        Label lblCpf = new Label("CPF:");
        lblCpf.setPrefWidth(70);
        tfCpf = new TextField();
        tfCpf.setPrefWidth(250);
        tfCpf.setPromptText("Digite o CPF");
        hboxCpf.getChildren().addAll(lblCpf, tfCpf);

        // Cargo
        HBox hboxCargo = new HBox(15);
        hboxCargo.setAlignment(Pos.CENTER_LEFT);
        Label lblCargo = new Label("Cargo:");
        lblCargo.setPrefWidth(70);
        tfCargo = new TextField();
        tfCargo.setPrefWidth(250);
        tfCargo.setPromptText("Digite o cargo");
        hboxCargo.getChildren().addAll(lblCargo, tfCargo);

        // Telefone
        HBox hboxTelefone = new HBox(15);
        hboxTelefone.setAlignment(Pos.CENTER_LEFT);
        Label lblTelefone = new Label("Telefone:");
        lblTelefone.setPrefWidth(70);
        tfTelefone = new TextField();
        tfTelefone.setPrefWidth(250);
        tfTelefone.setPromptText("Digite o telefone");
        hboxTelefone.getChildren().addAll(lblTelefone, tfTelefone);

        // Email
        HBox hboxEmail = new HBox(15);
        hboxEmail.setAlignment(Pos.CENTER_LEFT);
        Label lblEmail = new Label("Email:");
        lblEmail.setPrefWidth(70);
        tfEmail = new TextField();
        tfEmail.setPrefWidth(250);
        tfEmail.setPromptText("Digite o email");
        hboxEmail.getChildren().addAll(lblEmail, tfEmail);

        // Senha
        HBox hboxSenha = new HBox(15);
        hboxSenha.setAlignment(Pos.CENTER_LEFT);
        Label lblSenha = new Label("Senha:");
        lblSenha.setPrefWidth(70);
        pfSenha = new PasswordField();
        pfSenha.setPrefWidth(250);
        pfSenha.setPromptText("Digite a senha");
        hboxSenha.getChildren().addAll(lblSenha, pfSenha);

        Button btnCadastrar = new Button("Cadastrar");
        btnCadastrar.setPrefWidth(150);
        btnCadastrar.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        btnCadastrar.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-background-radius: 5; -fx-padding: 10 20;");
        btnCadastrar.setOnAction(e -> {
			try {
				handleCadastro();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});

        lblMensagem = new Label();
        lblMensagem.setTextFill(Color.RED);
        lblMensagem.setFont(Font.font(12));

        root.getChildren().addAll(
                lblTitulo,
                hboxNome, 
                hboxCpf,
                hboxCargo,
                hboxTelefone,
                hboxEmail,
                hboxSenha,
                btnCadastrar,
                lblMensagem
        );

        return new Scene(root, 500, 500);
    }

    private boolean validarCampos() {
        return !tfNome.getText().trim().isEmpty() &&
               !tfCpf.getText().trim().isEmpty() &&
               !tfCargo.getText().trim().isEmpty() &&
               !tfTelefone.getText().trim().isEmpty() &&
               !tfEmail.getText().trim().isEmpty() &&
               !pfSenha.getText().trim().isEmpty();
    }

    private void handleCadastro() throws SQLException{
        lblMensagem.setText("");

        if (validarCampos()) {
            lblMensagem.setTextFill(Color.GREEN);
            lblMensagem.setText("Funcionário cadastrado com sucesso!");
            
            String nomeTxt = tfNome.getText().trim();
            String cpfTxt = tfCpf.getText().trim();
            String cargoTxt = tfCargo.getText().trim();
            String telefoneTxt = tfTelefone.getText().trim();
            String emailTxt = tfEmail.getText().trim();
            String senhaTxt = pfSenha.getText().trim();
            
            Funcionario funcionario = new Funcionario (nomeTxt,cpfTxt,cargoTxt,telefoneTxt,emailTxt,senhaTxt);
        } else {
            lblMensagem.setTextFill(Color.RED);
            lblMensagem.setText("Preencha todos os campos!");
        }
    }
}