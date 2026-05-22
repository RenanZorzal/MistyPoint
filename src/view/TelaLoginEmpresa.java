package view;

import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.ParallelTransition;
import javafx.animation.PauseTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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

public class TelaLoginEmpresa {

    // ── Paleta ───────────────────────────────────────────
    private static final String PINK     = "#FF6B8A";
    private static final String ORANGE   = "#FF8E53";
    private static final String PURPLE   = "#9B59B6";
    private static final String DARK     = "#09080F";
    private static final String CARD_BG  = "#0F081E";
    private static final String TEXT_SEC = "#9B8EC4";

    private TextField     tfEmail;
    private PasswordField pfSenha;
    private Label         lblMensagem;

    public Scene getScene() {

        // ── ROOT ─────────────────────────────────────────────────────
        StackPane root = new StackPane();
        root.setStyle("-fx-background-color: " + DARK + ";");

        // ── ORBS ANIMADOS NO FUNDO ────────────────────────────────────
        Pane bgLayer = new Pane();
        bgLayer.setMouseTransparent(true);

        Circle orb1 = criarOrb(380, PURPLE, 0.18, 150,  -80,  -60);
        Circle orb2 = criarOrb(420, ORANGE, 0.15, 180, 1050,  400);
        Circle orb3 = criarOrb(300, PINK,   0.13, 130,  550,  500);
        Circle orb4 = criarOrb(250, PURPLE, 0.10, 110, 1150,  -80);
        bgLayer.getChildren().addAll(orb1, orb2, orb3, orb4);
        bgLayer.prefWidthProperty().bind(root.widthProperty());
        bgLayer.prefHeightProperty().bind(root.heightProperty());

        animarOrb(orb1,  80,  40, 9000);
        animarOrb(orb2, -60, -70, 11000);
        animarOrb(orb3,  90, -50, 7500);
        animarOrb(orb4, -70,  60, 8500);

        // ── CARD ─────────────────────────────────────────────────────
        StackPane cardOuter = new StackPane();
        cardOuter.setMaxWidth(460);
        cardOuter.setMaxHeight(javafx.scene.layout.Region.USE_PREF_SIZE);
        cardOuter.setStyle(
            "-fx-background-color: linear-gradient(to bottom right, " + PURPLE + ", " + PINK + ", " + ORANGE + ");" +
            "-fx-background-radius: 24;" +
            "-fx-padding: 1.5;"
        );
        DropShadow glow = new DropShadow(40, Color.web(PURPLE, 0.4));
        glow.setSpread(0.05);
        cardOuter.setEffect(glow);

        VBox card = new VBox(0);
        card.setPadding(new Insets(44, 44, 40, 44));
        card.setStyle(
            "-fx-background-color: " + CARD_BG + ";" +
            "-fx-background-radius: 23;"
        );

        // ── CABEÇALHO ─────────────────────────────────────────────────
        Region accentLine = new Region();
        accentLine.setPrefWidth(44);
        accentLine.setPrefHeight(3);
        accentLine.setStyle(
            "-fx-background-color: linear-gradient(to right, " + PURPLE + ", " + ORANGE + ");" +
            "-fx-background-radius: 2;"
        );
        VBox.setMargin(accentLine, new Insets(0, 0, 14, 0));

        Label lblTitulo = new Label("ÁREA DA EMPRESA");
        lblTitulo.setFont(Font.font("Helvetica Neue", FontWeight.BOLD, 24));
        lblTitulo.setTextFill(Color.WHITE);

        DropShadow titleGlow = new DropShadow(20, Color.web(PURPLE, 0.0));
        lblTitulo.setEffect(titleGlow);
        Timeline titlePulse = new Timeline(
            new KeyFrame(Duration.ZERO,       new KeyValue(titleGlow.colorProperty(), Color.web(PURPLE, 0.0))),
            new KeyFrame(Duration.seconds(2), new KeyValue(titleGlow.colorProperty(), Color.web(PURPLE, 0.9))),
            new KeyFrame(Duration.seconds(4), new KeyValue(titleGlow.colorProperty(), Color.web(PURPLE, 0.0)))
        );
        titlePulse.setCycleCount(Timeline.INDEFINITE);
        titlePulse.play();

        Label lblSub = new Label("Faça login para gerenciar sua empresa");
        lblSub.setFont(Font.font("Helvetica Neue", 13));
        lblSub.setTextFill(Color.web(TEXT_SEC));
        VBox.setMargin(lblSub, new Insets(6, 0, 32, 0));

        // ── CAMPOS ────────────────────────────────────────────────────
        tfEmail = criarCampo("E-mail da Empresa", false);
        pfSenha = (PasswordField) criarCampo("Senha", true);

        HBox[] linhasForm = {
            grupoLinha("E-mail", tfEmail),
            grupoLinha("Senha", pfSenha)
        };

        VBox camposBox = new VBox(14);
        camposBox.getChildren().addAll(linhasForm);

        // ── MENSAGEM ──────────────────────────────────────────────────
        lblMensagem = new Label();
        lblMensagem.setFont(Font.font("Helvetica Neue", 13));
        lblMensagem.setWrapText(true);
        lblMensagem.setManaged(false);  // não ocupa espaço quando vazio
        lblMensagem.setVisible(false);
        VBox.setMargin(lblMensagem, new Insets(14, 0, 0, 0));

        // ── BOTÃO ENTRAR ──────────────────────────────────────────────
        Button btnEntrar = new Button("Entrar");
        btnEntrar.setMaxWidth(Double.MAX_VALUE);
        btnEntrar.setPrefHeight(50);
        btnEntrar.setFont(Font.font("Helvetica Neue", FontWeight.BOLD, 16));
        btnEntrar.setStyle(estiloBotao(false));
        VBox.setMargin(btnEntrar, new Insets(22, 0, 0, 0));

        btnEntrar.setOnMouseEntered(e -> { btnEntrar.setStyle(estiloBotao(true));  escala(btnEntrar, 1.00, 1.025, 150).play(); });
        btnEntrar.setOnMouseExited(e ->  { btnEntrar.setStyle(estiloBotao(false)); escala(btnEntrar, 1.025, 1.00, 150).play(); });
        btnEntrar.setOnAction(e -> handleLogin());

        // ── LINK CADASTRO ─────────────────────────────────────────────
        Label lblLink = new Label("Ainda não tem conta?  Cadastre sua empresa");
        lblLink.setFont(Font.font("Helvetica Neue", 12));
        lblLink.setTextFill(Color.web(TEXT_SEC));
        lblLink.setStyle("-fx-cursor: hand;");
        lblLink.setAlignment(Pos.CENTER);
        lblLink.setMaxWidth(Double.MAX_VALUE);
        VBox.setMargin(lblLink, new Insets(18, 0, 0, 0));
        lblLink.setOnMouseEntered(e -> lblLink.setTextFill(Color.web(PURPLE)));
        lblLink.setOnMouseExited(e ->  lblLink.setTextFill(Color.web(TEXT_SEC)));

        // ── MONTAR CARD ───────────────────────────────────────────────
        card.getChildren().addAll(
            accentLine, lblTitulo, lblSub,
            camposBox, btnEntrar, lblMensagem, lblLink
        );
        cardOuter.getChildren().add(card);

        root.getChildren().addAll(bgLayer, cardOuter);
        StackPane.setAlignment(cardOuter, Pos.CENTER);
        StackPane.setMargin(cardOuter, new Insets(20));

        // ── ANIMAÇÃO DE ENTRADA ───────────────────────────────────────
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
            HBox linha = linhasForm[i];
            linha.setOpacity(0);
            linha.setTranslateX(-18);
            PauseTransition delay = new PauseTransition(Duration.millis(450 + i * 100));
            final HBox fl = linha;
            delay.setOnFinished(ev ->
                new ParallelTransition(
                    fade(fl, 0, 1, 320),
                    translX(fl, -18, 0, 320)
                ).play()
            );
            delay.play();
        }

        return new Scene(root, 1280, 720);
    }

    // ── Criação de componentes ────────────────────────────────────────

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
        field.setPrefHeight(48);
        field.setMinHeight(48);
        field.setMaxHeight(48);
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
        field.setOnAction(e -> handleLogin());
        return field;
    }

    private HBox grupoLinha(String labelText, Node field) {
        Label lbl = new Label(labelText.toUpperCase());
        lbl.setFont(Font.font("Helvetica Neue", FontWeight.BOLD, 11));
        lbl.setTextFill(Color.web("#7B68A0"));
        VBox v = new VBox(7, lbl, field);
        v.setMaxWidth(Double.MAX_VALUE);
        HBox h = new HBox(v);
        HBox.setHgrow(v, javafx.scene.layout.Priority.ALWAYS);
        return h;
    }

    // ── Estilos CSS ───────────────────────────────────────────────────

    private String estiloField(boolean focused) {
        if (focused) {
            return "-fx-background-color: #1A0D35;" +
                   "-fx-background-radius: 12;" +
                   "-fx-border-color: " + PURPLE + ";" +
                   "-fx-border-radius: 12;" +
                   "-fx-border-width: 1.5;" +
                   "-fx-padding: 0 14 0 14;" +
                   "-fx-text-fill: white;" +
                   "-fx-prompt-text-fill: #5A4A7A;" +
                   "-fx-effect: dropshadow(gaussian, rgba(155,89,182,0.45), 12, 0, 0, 0);";
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
            ? "linear-gradient(to right, #7D3DAA, #FF7A3D)"
            : "linear-gradient(to right, " + PURPLE + ", " + ORANGE + ")";
        String shadow = hover
            ? "-fx-effect: dropshadow(gaussian, rgba(155,89,182,0.65), 20, 0, 0, 4);"
            : "-fx-effect: dropshadow(gaussian, rgba(155,89,182,0.40), 12, 0, 0, 2);";
        return "-fx-background-color: " + grad + ";" +
               "-fx-background-radius: 14;" +
               "-fx-text-fill: white;" +
               "-fx-cursor: hand;" +
               shadow;
    }

    // ── Helpers de animação ───────────────────────────────────────────

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

    // ── Login ─────────────────────────────────────────────────────────

    private void handleLogin() {
        lblMensagem.setText("");
        String email = tfEmail.getText().trim();
        String senha = pfSenha.getText().trim();

        if (email.isEmpty() || !email.contains("@")) {
            mostrarErro("Informe um e-mail válido.");
            return;
        }
        if (senha.isEmpty() || senha.length() < 6) {
            mostrarErro("A senha deve ter pelo menos 6 caracteres.");
            return;
        }

        try {
            Conexao.conectar();
            dao.EmpresaDAO dao = new dao.EmpresaDAO(Conexao.conexao);
            model.Empresa empresa = dao.autenticarRetornarEmpresa(email, senha);
            if (empresa != null) {
                mostrarSucesso("Login realizado com sucesso! Redirecionando...");
                javafx.stage.Stage stage = (javafx.stage.Stage) tfEmail.getScene().getWindow();
                TelaHomeEmpresa home = new TelaHomeEmpresa(empresa, empresa.getIdEmpresa());
                stage.setScene(home.getScene(stage));
            } else {
                Conexao.desconectar();
                mostrarErro("E-mail ou senha incorretos.");
            }
        } catch (Exception ex) {
            mostrarErro("Erro ao conectar: " + ex.getMessage());
            ex.printStackTrace();
            Conexao.desconectar();
        }
    }


    private void mostrarErro(String msg) {
        lblMensagem.setManaged(true);
        lblMensagem.setVisible(true);
        lblMensagem.setTextFill(Color.web("#FF3B5C"));
        lblMensagem.setText(msg);
        TranslateTransition shake = new TranslateTransition(Duration.millis(55), lblMensagem);
        shake.setFromX(-7); shake.setToX(7);
        shake.setCycleCount(5); shake.setAutoReverse(true);
        shake.play();
    }

    private void mostrarSucesso(String msg) {
        lblMensagem.setManaged(true);
        lblMensagem.setVisible(true);
        lblMensagem.setTextFill(Color.web("#34C759"));
        lblMensagem.setText(msg);
    }
}
