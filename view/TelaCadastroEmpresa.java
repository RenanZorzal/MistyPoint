package view;

import controller.EmpresaController;
import dao.EnderecoDAO;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.ParallelTransition;
import javafx.animation.PauseTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;
import model.Conexao;
import model.Empresa;
import model.Estado;
import javafx.stage.Stage;

public class TelaCadastroEmpresa {

    // ── Paleta ───────────────────────────────────────────
    private static final String PINK     = "#FF6B8A";
    private static final String ORANGE   = "#FF8E53";
    private static final String PURPLE   = "#9B59B6";
    private static final String DARK     = "#09080F";
    private static final String CARD_BG  = "#0F081E";
    private static final String TEXT_SEC = "#9B8EC4";

    // ── Dados da empresa ─────────────────────────────────
    private TextField     tfNomeEmpresa;
    private TextField     tfCnpj;
    private TextField     tfRazaoSocial;
    private TextField     tfNomeFantasia;
    private TextField     tfInscricaoEstadual;
    private TextField     tfEmail;
    private PasswordField pfSenha;

    // ── Endereço (flat) ───────────────────────────────────
    private ComboBox<Estado>  cbEstado;
    private TextField         tfCidade;
    private TextField         tfLogradouro;
    private TextField         tfNumero;
    private TextField         tfComplemento;
    private TextField         tfCep;

    private Label lblMensagem;

    public Scene getScene(Stage stage) {

        // ── ROOT ─────────────────────────────────────────────────────
        StackPane root = new StackPane();
        root.setStyle("-fx-background-color: " + DARK + ";");

        // ── ORBS ANIMADOS NO FUNDO ────────────────────────────────────
        Pane bgLayer = new Pane();
        bgLayer.setMouseTransparent(true);

        Circle orb1 = criarOrb(380, PINK,   0.18, 150,  -80,  -60);
        Circle orb2 = criarOrb(420, PURPLE, 0.15, 180, 1050,  400);
        Circle orb3 = criarOrb(300, ORANGE, 0.13, 130,  550,  500);
        Circle orb4 = criarOrb(250, PINK,   0.10, 110, 1150,  -80);
        bgLayer.getChildren().addAll(orb1, orb2, orb3, orb4);
        bgLayer.prefWidthProperty().bind(root.widthProperty());
        bgLayer.prefHeightProperty().bind(root.heightProperty());

        animarOrb(orb1,  80,  40, 9000);
        animarOrb(orb2, -60, -70, 11000);
        animarOrb(orb3,  90, -50, 7500);
        animarOrb(orb4, -70,  60, 8500);

        // ── CARD COM BORDA GRADIENTE ───────────────────────────────────
        StackPane cardOuter = new StackPane();
        cardOuter.setMaxWidth(700);
        cardOuter.setStyle(
            "-fx-background-color: linear-gradient(to bottom right, " + PINK + ", " + ORANGE + ", " + PURPLE + ");" +
            "-fx-background-radius: 24;" +
            "-fx-padding: 1.5;"
        );
        DropShadow glow = new DropShadow(40, Color.web(PINK, 0.4));
        glow.setSpread(0.05);
        cardOuter.setEffect(glow);

        VBox card = new VBox(0);
        card.setPadding(new Insets(28, 38, 28, 38));
        card.setStyle(
            "-fx-background-color: " + CARD_BG + ";" +
            "-fx-background-radius: 23;"
        );

        // ── CABEÇALHO ─────────────────────────────────────────────────
        Region accentLine = new Region();
        accentLine.setPrefWidth(44);
        accentLine.setPrefHeight(3);
        accentLine.setStyle(
            "-fx-background-color: linear-gradient(to right, " + PINK + ", " + ORANGE + ");" +
            "-fx-background-radius: 2;"
        );
        VBox.setMargin(accentLine, new Insets(0, 0, 10, 0));

        Label lblTitulo = new Label("NOVA EMPRESA");
        lblTitulo.setFont(Font.font("Helvetica Neue", FontWeight.BOLD, 26));
        lblTitulo.setTextFill(Color.WHITE);

        DropShadow titleGlow = new DropShadow(20, Color.web(PINK, 0.0));
        lblTitulo.setEffect(titleGlow);
        Timeline titlePulse = new Timeline(
            new KeyFrame(Duration.ZERO,      new KeyValue(titleGlow.colorProperty(), Color.web(PINK, 0.0))),
            new KeyFrame(Duration.seconds(2), new KeyValue(titleGlow.colorProperty(), Color.web(PINK, 0.9))),
            new KeyFrame(Duration.seconds(4), new KeyValue(titleGlow.colorProperty(), Color.web(PINK, 0.0)))
        );
        titlePulse.setCycleCount(Timeline.INDEFINITE);
        titlePulse.play();

        Label lblSub = new Label("Preencha os dados para cadastrar a empresa");
        lblSub.setFont(Font.font("Helvetica Neue", 13));
        lblSub.setTextFill(Color.web(TEXT_SEC));
        VBox.setMargin(lblSub, new Insets(4, 0, 14, 0));

        // ── CAMPOS DA EMPRESA ─────────────────────────────────────────
        tfNomeEmpresa       = criarCampo("Nome da Empresa", false);
        tfCnpj              = criarCampo("CNPJ (ex: 00.000.000/0001-00)", false);
        MaskUtils.applyCnpjMask(tfCnpj);
        tfRazaoSocial       = criarCampo("Razão Social", false);
        tfNomeFantasia      = criarCampo("Nome Fantasia", false);
        tfInscricaoEstadual = criarCampo("Inscrição Estadual", false);
        tfEmail             = criarCampo("E-mail da Empresa", false);
        pfSenha             = (PasswordField) criarCampo("Senha (mín. 6 caracteres)", true);

        // ── CAMPOS DE ENDEREÇO (flat: Estado → Cidade → campos livres) ─
        cbEstado      = criarComboBox("Selecione o Estado");
        tfCidade      = criarCampo("Cidade", false);
        tfLogradouro  = criarCampo("Logradouro (Rua, Av, etc.)", false);
        tfNumero      = criarCampo("Número", false);
        tfComplemento = criarCampo("Complemento (opcional)", false);
        tfCep         = criarCampo("CEP (ex: 00000-000)", false);
        MaskUtils.applyCepMask(tfCep);

        carregarEstados();

        // ── LINHAS DO FORMULÁRIO ──────────────────────────────────────
        HBox linhaDados1 = linha(grupo("Nome da Empresa", tfNomeEmpresa), grupo("CNPJ", tfCnpj));
        HBox linhaDados2 = linha(grupo("Razão Social", tfRazaoSocial), grupo("Nome Fantasia", tfNomeFantasia));
        HBox linhaDados3 = linha(
            grupo("Inscrição Estadual", tfInscricaoEstadual),
            grupo("E-mail", tfEmail),
            grupo("Senha", pfSenha));

        // Endereço: Estado (dropdown) + Cidade (digitável) / Logradouro + CEP / Número + Complemento
        HBox linhaEnd1 = linha(grupo("Estado", cbEstado), grupo("Cidade", tfCidade));
        HBox linhaEnd2 = linha(grupo("Logradouro", tfLogradouro), grupo("CEP", tfCep));
        HBox linhaEnd3 = linha(grupo("Número", tfNumero), grupo("Complemento (opcional)", tfComplemento));

        HBox[] linhasForm = {
            linhaDados1, linhaDados2, linhaDados3,
            linhaEnd1, linhaEnd2, linhaEnd3
        };

        VBox camposBox = new VBox(8);
        camposBox.getChildren().addAll(linhasForm);

        // ── MENSAGEM ──────────────────────────────────────────────────
        lblMensagem = new Label();
        lblMensagem.setFont(Font.font("Helvetica Neue", 13));
        lblMensagem.setWrapText(true);
        VBox.setMargin(lblMensagem, new Insets(10, 0, 0, 0));

        // ── BOTÃO ─────────────────────────────────────────────────────
        Button btnCadastrar = new Button("Cadastrar Empresa");
        btnCadastrar.setMaxWidth(Double.MAX_VALUE);
        btnCadastrar.setPrefHeight(44);
        btnCadastrar.setFont(Font.font("Helvetica Neue", FontWeight.BOLD, 16));
        btnCadastrar.setStyle(estiloBotao(false));
        VBox.setMargin(btnCadastrar, new Insets(14, 0, 0, 0));

        ScaleTransition hoverIn  = escala(btnCadastrar, 1.00, 1.025, 150);
        ScaleTransition hoverOut = escala(btnCadastrar, 1.025, 1.00,  150);
        btnCadastrar.setOnMouseEntered(e -> { btnCadastrar.setStyle(estiloBotao(true));  hoverIn.play(); });
        btnCadastrar.setOnMouseExited(e ->  { btnCadastrar.setStyle(estiloBotao(false)); hoverOut.play(); });
        btnCadastrar.setOnAction(e -> handleCadastro());

        // ── MONTAR CARD ───────────────────────────────────────────────
        card.getChildren().addAll(accentLine, lblTitulo, lblSub, camposBox, btnCadastrar, lblMensagem);
        cardOuter.getChildren().add(card);

        root.getChildren().addAll(bgLayer, cardOuter);
        StackPane.setAlignment(cardOuter, Pos.CENTER);
        StackPane.setMargin(cardOuter, new Insets(20));

        // ── BOTÃO VOLTAR ─────────────────────────────────────────────
        Button btnVoltar = new Button("←  Voltar");
        btnVoltar.setFont(Font.font("Helvetica Neue", FontWeight.BOLD, 12));
        btnVoltar.setStyle(
            "-fx-background-color: rgba(255,142,83,0.15);" +
            "-fx-border-color: rgba(255,142,83,0.40);" +
            "-fx-border-radius: 10; -fx-background-radius: 10;" +
            "-fx-text-fill: " + ORANGE + "; -fx-cursor: hand; -fx-padding: 7 14 7 14;"
        );
        btnVoltar.setOnMouseEntered(e -> btnVoltar.setStyle(
            "-fx-background-color: rgba(255,142,83,0.28);" +
            "-fx-border-color: " + ORANGE + ";" +
            "-fx-border-radius: 10; -fx-background-radius: 10;" +
            "-fx-text-fill: white; -fx-cursor: hand; -fx-padding: 7 14 7 14;"
        ));
        btnVoltar.setOnMouseExited(e -> btnVoltar.setStyle(
            "-fx-background-color: rgba(255,142,83,0.15);" +
            "-fx-border-color: rgba(255,142,83,0.40);" +
            "-fx-border-radius: 10; -fx-background-radius: 10;" +
            "-fx-text-fill: " + ORANGE + "; -fx-cursor: hand; -fx-padding: 7 14 7 14;"
        ));
        btnVoltar.setOnAction(e -> stage.setScene(new TelaLanding().getScene(stage)));
        StackPane.setAlignment(btnVoltar, Pos.TOP_LEFT);
        StackPane.setMargin(btnVoltar, new Insets(20, 0, 0, 20));
        root.getChildren().add(btnVoltar);

        // ── ANIMAÇÃO DE ENTRADA DO CARD ───────────────────────────────
        cardOuter.setOpacity(0);
        cardOuter.setTranslateY(28);
        cardOuter.setScaleX(0.96);
        cardOuter.setScaleY(0.96);
        new ParallelTransition(
            fade(cardOuter, 0, 1, 700),
            translY(cardOuter, 28, 0, 700),
            escala(cardOuter, 0.96, 1.0, 700)
        ).play();

        for (int i = 0; i < linhasForm.length; i++) {
            HBox lf = linhasForm[i];
            lf.setOpacity(0);
            lf.setTranslateX(-18);
            PauseTransition delay = new PauseTransition(Duration.millis(350 + i * 80));
            final HBox flinha = lf;
            delay.setOnFinished(e ->
                new ParallelTransition(
                    fade(flinha, 0, 1, 320),
                    translX(flinha, -18, 0, 320)
                ).play()
            );
            delay.play();
        }

        return new Scene(root, 1280, 720);
    }

    // ── Helpers de layout ─────────────────────────────────────────────

    private HBox linha(VBox... grupos) {
        HBox h = new HBox(15, grupos);
        h.setAlignment(Pos.CENTER_LEFT);
        return h;
    }

    private Circle criarOrb(double raio, String cor, double opacidade, double blur, double x, double y) {
        Circle c = new Circle(raio);
        c.setFill(Color.web(cor, opacidade));
        c.setEffect(new GaussianBlur(blur));
        c.setLayoutX(x);
        c.setLayoutY(y);
        return c;
    }

    private void animarOrb(Circle orb, double dx, double dy, int ms) {
        Timeline t = new Timeline(
            new KeyFrame(Duration.ZERO,
                new KeyValue(orb.translateXProperty(), 0),
                new KeyValue(orb.translateYProperty(), 0)),
            new KeyFrame(Duration.millis(ms),
                new KeyValue(orb.translateXProperty(), dx, Interpolator.EASE_BOTH),
                new KeyValue(orb.translateYProperty(), dy, Interpolator.EASE_BOTH))
        );
        t.setAutoReverse(true);
        t.setCycleCount(Timeline.INDEFINITE);
        t.play();
    }

    private TextField criarCampo(String placeholder, boolean senha) {
        TextField field = senha ? new PasswordField() : new TextField();
        field.setPromptText(placeholder);
        field.setPrefHeight(42);
        field.setMinHeight(42);
        field.setMaxHeight(42);
        field.setFont(Font.font("Helvetica Neue", 15));
        field.setStyle(estiloField(false));
        field.focusedProperty().addListener((obs, old, focused) -> {
            field.setStyle(estiloField(focused));
            if (focused) {
                ScaleTransition st = escala(field, 1.0, 1.01, 120);
                st.setAutoReverse(true);
                st.setCycleCount(2);
                st.play();
            }
        });
        return field;
    }

    private <T> ComboBox<T> criarComboBox(String prompt) {
        ComboBox<T> cb = new ComboBox<>();
        cb.setPromptText(prompt);
        cb.setPrefHeight(42);
        cb.setMinHeight(42);
        cb.setMaxHeight(42);
        cb.setMaxWidth(Double.MAX_VALUE);
        cb.setStyle(estiloField(false));
        cb.focusedProperty().addListener((obs, old, focused) -> cb.setStyle(estiloField(focused)));

        javafx.util.Callback<javafx.scene.control.ListView<T>, javafx.scene.control.ListCell<T>> cellFactory =
            lv -> new javafx.scene.control.ListCell<T>() {
                @Override
                protected void updateItem(T item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                        setStyle("-fx-background-color: #130A24;");
                    } else {
                        setText(item.toString());
                        setStyle("-fx-text-fill: white; -fx-background-color: #130A24;" +
                                 "-fx-font-family: 'Helvetica Neue'; -fx-font-size: 14px;");
                    }
                }
            };
        cb.setCellFactory(cellFactory);
        cb.setButtonCell(new javafx.scene.control.ListCell<T>() {
            @Override
            protected void updateItem(T item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(prompt);
                    setStyle("-fx-text-fill: #5A4A7A; -fx-background-color: transparent;" +
                             "-fx-font-family: 'Helvetica Neue'; -fx-font-size: 15px;");
                } else {
                    setText(item.toString());
                    setStyle("-fx-text-fill: white; -fx-background-color: transparent;" +
                             "-fx-font-family: 'Helvetica Neue'; -fx-font-size: 15px;");
                }
            }
        });
        return cb;
    }

    private VBox grupo(String labelText, Node field) {
        Label lbl = new Label(labelText.toUpperCase());
        lbl.setFont(Font.font("Helvetica Neue", FontWeight.BOLD, 11));
        lbl.setTextFill(Color.web("#7B68A0"));
        VBox v = new VBox(7, lbl, field);
        HBox.setHgrow(v, javafx.scene.layout.Priority.ALWAYS);
        return v;
    }

    // ── Estilos CSS ───────────────────────────────────────────────────

    private String estiloField(boolean focused) {
        if (focused) {
            return "-fx-background-color: #1A0D35;" +
                   "-fx-background-radius: 12;" +
                   "-fx-border-color: " + PINK + ";" +
                   "-fx-border-radius: 12;" +
                   "-fx-border-width: 1.5;" +
                   "-fx-padding: 0 14 0 14;" +
                   "-fx-text-fill: white;" +
                   "-fx-prompt-text-fill: #5A4A7A;" +
                   "-fx-effect: dropshadow(gaussian, rgba(255,107,138,0.45), 12, 0, 0, 0);";
        }
        return "-fx-background-color: #130A24;" +
               "-fx-background-radius: 12;" +
               "-fx-border-color: #2D1F4A;" +
               "-fx-border-radius: 12;" +
               "-fx-border-width: 1;" +
               "-fx-padding: 0 14 0 14;" +
               "-fx-text-fill: white;" +
               "-fx-prompt-text-fill: #5A4A7A;" +
               "-fx-effect: none;";
    }

    private String estiloBotao(boolean hover) {
        String grad = hover
            ? "linear-gradient(to right, #FF4D75, #FF7A3D)"
            : "linear-gradient(to right, " + PINK + ", " + ORANGE + ")";
        String shadow = hover
            ? "-fx-effect: dropshadow(gaussian, rgba(255,107,138,0.65), 20, 0, 0, 4);"
            : "-fx-effect: dropshadow(gaussian, rgba(255,107,138,0.40), 12, 0, 0, 2);";
        return "-fx-background-color: " + grad + ";" +
               "-fx-background-radius: 14;" +
               "-fx-text-fill: white;" +
               "-fx-cursor: hand;" +
               shadow;
    }

    // ── Animações ─────────────────────────────────────────────────────

    private FadeTransition fade(Node n, double from, double to, int ms) {
        FadeTransition ft = new FadeTransition(Duration.millis(ms), n);
        ft.setFromValue(from); ft.setToValue(to);
        ft.setInterpolator(Interpolator.EASE_OUT);
        return ft;
    }

    private TranslateTransition translY(Node n, double from, double to, int ms) {
        TranslateTransition tt = new TranslateTransition(Duration.millis(ms), n);
        tt.setFromY(from); tt.setToY(to);
        tt.setInterpolator(Interpolator.EASE_OUT);
        return tt;
    }

    private TranslateTransition translX(Node n, double from, double to, int ms) {
        TranslateTransition tt = new TranslateTransition(Duration.millis(ms), n);
        tt.setFromX(from); tt.setToX(to);
        tt.setInterpolator(Interpolator.EASE_OUT);
        return tt;
    }

    private ScaleTransition escala(Node n, double from, double to, int ms) {
        ScaleTransition st = new ScaleTransition(Duration.millis(ms), n);
        st.setFromX(from); st.setFromY(from);
        st.setToX(to);     st.setToY(to);
        st.setInterpolator(Interpolator.EASE_OUT);
        return st;
    }

    // ── Validação ─────────────────────────────────────────────────────

    private boolean validarCampos() {
        if (tfNomeEmpresa.getText().trim().isEmpty())       { mostrarErro("Informe o nome da empresa.");            return false; }
        if (tfNomeEmpresa.getText().trim().length() < 2)    { mostrarErro("Nome deve ter pelo menos 2 caracteres.");return false; }
        if (tfCnpj.getText().trim().isEmpty())              { mostrarErro("Informe o CNPJ.");                       return false; }
        if (tfRazaoSocial.getText().trim().isEmpty())       { mostrarErro("Informe a Razão Social.");               return false; }
        if (tfNomeFantasia.getText().trim().isEmpty())      { mostrarErro("Informe o Nome Fantasia.");              return false; }
        if (tfInscricaoEstadual.getText().trim().isEmpty()) { mostrarErro("Informe a Inscrição Estadual.");         return false; }

        String email = tfEmail.getText().trim();
        if (email.isEmpty() || !email.contains("@") || !email.contains(".")) {
            mostrarErro(email.isEmpty() ? "Informe o e-mail." : "E-mail inválido."); return false;
        }
        String senha = pfSenha.getText().trim();
        if (senha.isEmpty() || senha.length() < 6) {
            mostrarErro(senha.isEmpty() ? "Informe a senha." : "Senha deve ter pelo menos 6 caracteres."); return false;
        }

        if (cbEstado.getValue() == null) { mostrarErro("Selecione um estado.");       return false; }
        if (tfCidade.getText().trim().isEmpty()) { mostrarErro("Informe a cidade."); return false; }
        if (tfLogradouro.getText().trim().isEmpty()) { mostrarErro("Informe o logradouro."); return false; }
        if (tfCep.getText().trim().isEmpty())        { mostrarErro("Informe o CEP.");        return false; }

        String numStr = tfNumero.getText().trim();
        if (numStr.isEmpty() || !numStr.matches("\\d+")) {
            mostrarErro("Informe um número válido (apenas dígitos)."); return false;
        }
        return true;
    }

    // ── Cadastro ──────────────────────────────────────────────────────

    private void handleCadastro() {
        lblMensagem.setText("");
        if (!validarCampos()) return;

        Empresa emp = new Empresa(
            tfCnpj.getText().trim(),
            tfRazaoSocial.getText().trim(),
            tfNomeFantasia.getText().trim(),
            tfInscricaoEstadual.getText().trim(),
            tfNomeEmpresa.getText().trim(),
            tfEmail.getText().trim(),
            pfSenha.getText().trim()
        );
        emp.setLogradouro(tfLogradouro.getText().trim());
        emp.setNumero(Integer.parseInt(tfNumero.getText().trim()));
        emp.setComplemento(tfComplemento.getText().trim());
        emp.setCep(tfCep.getText().trim());
        emp.setCidade(tfCidade.getText().trim());
        emp.setIdEstado(cbEstado.getValue().getIdEstado());

        try {
            new EmpresaController(emp).salvar();
            mostrarSucesso("Empresa cadastrada com sucesso!");
            limparCampos();
        } catch (IllegalArgumentException ex) {
            mostrarErro(ex.getMessage());
        } catch (Exception ex) {
            mostrarErro("Erro ao cadastrar: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void mostrarErro(String msg) {
        lblMensagem.setTextFill(Color.web("#FF3B5C"));
        lblMensagem.setText("⚠  " + msg);
        TranslateTransition shake = new TranslateTransition(Duration.millis(55), lblMensagem);
        shake.setFromX(-7); shake.setToX(7);
        shake.setCycleCount(5); shake.setAutoReverse(true);
        shake.play();
    }

    private void mostrarSucesso(String msg) {
        lblMensagem.setTextFill(Color.web("#34C759"));
        lblMensagem.setText("✓  " + msg);
    }

    private void limparCampos() {
        tfNomeEmpresa.clear(); tfCnpj.clear(); tfRazaoSocial.clear();
        tfNomeFantasia.clear(); tfInscricaoEstadual.clear();
        tfEmail.clear(); pfSenha.clear();
        tfLogradouro.clear(); tfNumero.clear();
        tfComplemento.clear(); tfCep.clear(); tfCidade.clear();
        cbEstado.getSelectionModel().clearSelection();
    }

    // ── Loaders DB ─────────────────────────────────────────────────────

    private void carregarEstados() {
        try {
            Conexao.conectar();
            EnderecoDAO dao = new EnderecoDAO(Conexao.conexao);
            cbEstado.setItems(FXCollections.observableArrayList(dao.listarEstados()));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Conexao.desconectar();
        }
    }
}
