package view;


import controller.FuncionarioController;
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
        btnCadastrar.setOnAction(e -> handleCadastro());

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
        String nome     = tfNome.getText().trim();
        String cpf      = tfCpf.getText().trim();
        String cargo    = tfCargo.getText().trim();
        String telefone = tfTelefone.getText().trim();
        String email    = tfEmail.getText().trim();
        String senha    = pfSenha.getText().trim();

        if (nome.isEmpty()) {
            lblMensagem.setTextFill(Color.RED);
            lblMensagem.setText("Informe o nome do funcionário!");
            return false;
        }
        if (nome.length() < 3) {
            lblMensagem.setTextFill(Color.RED);
            lblMensagem.setText("Nome muito curto!");
            return false;
        }
        
        if (cpf.isEmpty()) {
            lblMensagem.setTextFill(Color.RED);
            lblMensagem.setText("Informe o CPF!");
            return false;
        }
        if (cpf.length() != 11) {
            lblMensagem.setTextFill(Color.RED);
            lblMensagem.setText("CPF deve ter 11 dígitos!");
            return false;
        }

        if (cargo.isEmpty()) {
            lblMensagem.setTextFill(Color.RED);
            lblMensagem.setText("Informe o cargo!");
            return false;
        }

        if (telefone.isEmpty()) {
            lblMensagem.setTextFill(Color.RED);
            lblMensagem.setText("Informe o telefone!");
            return false;
        }
        if (telefone.length() < 10) {
            lblMensagem.setTextFill(Color.RED);
            lblMensagem.setText("Telefone deve ter pelo menos 10 dígitos!");
            return false;
        }

        if (email.isEmpty()) {
            lblMensagem.setTextFill(Color.RED);
            lblMensagem.setText("Informe o email!");
            return false;
        }
        if (!email.contains("@") || !email.contains(".")) {
            lblMensagem.setTextFill(Color.RED);
            lblMensagem.setText("Email inválido!");
            return false;
        }

        if (senha.isEmpty()) {
            lblMensagem.setTextFill(Color.RED);
            lblMensagem.setText("Informe a senha!");
            return false;
        }
        if (senha.length() < 6) {
            lblMensagem.setTextFill(Color.RED);
            lblMensagem.setText("Senha deve ter pelo menos 6 caracteres!");
            return false;
        }

        return true;
    }

    private void handleCadastro() {
        lblMensagem.setText("");

        if (!validarCampos()) {
            return;
        }

        String nomeTxt     = tfNome.getText().trim();
        String cpfTxt      = tfCpf.getText().trim();
        String cargoTxt    = tfCargo.getText().trim();
        String telefoneTxt = tfTelefone.getText().trim();
        String emailTxt    = tfEmail.getText().trim();
        String senhaTxt    = pfSenha.getText().trim();

        Funcionario funcionario = new Funcionario(nomeTxt, cpfTxt, cargoTxt, telefoneTxt, emailTxt, senhaTxt);
        FuncionarioController funcionarioCtrl = new FuncionarioController(funcionario);

        try {
            funcionarioCtrl.salvarFuncionario();
            // Só exibe sucesso se o banco aceitou sem erros
            lblMensagem.setTextFill(Color.GREEN);
            lblMensagem.setText("Funcionário cadastrado com sucesso!");
        } catch (IllegalArgumentException ex) {
            // CPF ou e-mail duplicado
            lblMensagem.setTextFill(Color.RED);
            lblMensagem.setText(ex.getMessage());
        } catch (Exception ex) {
            // Qualquer outro erro de banco
            lblMensagem.setTextFill(Color.RED);
            lblMensagem.setText("Erro ao cadastrar: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}