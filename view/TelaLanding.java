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
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.util.Duration;

public class TelaLanding {

    // ── Paleta ────────────────────────────────────────────────────────
    private static final String PINK     = "#FF6B8A";
    private static final String ORANGE   = "#FF8E53";
    private static final String PURPLE   = "#9B59B6";
    private static final String DARK     = "#09080F";
    private static final String CARD_BG  = "#0F081E";
    private static final String TEXT_SEC = "#9B8EC4";
    private static final String BORDER   = "#1E1035";

    public Scene getScene(Stage stage) {

        // ── ROOT ──────────────────────────────────────────────────────
        StackPane root = new StackPane();
        root.setStyle("-fx-background-color: " + DARK + ";");

        // ── ORBS ANIMADOS ─────────────────────────────────────────────
        Pane bgLayer = new Pane();
        bgLayer.setMouseTransparent(true);

        Circle orb1 = criarOrb(500, PURPLE, 0.13, 200, -150,  -80);
        Circle orb2 = criarOrb(450, ORANGE, 0.12, 180, 1200,  500);
        Circle orb3 = criarOrb(380, PINK,   0.10, 160,  600,  600);
        Circle orb4 = criarOrb(300, PURPLE, 0.09, 140, 1300,  -50);
        Circle orb5 = criarOrb(280, PINK,   0.08, 130,  200,  600);
        bgLayer.getChildren().addAll(orb1, orb2, orb3, orb4, orb5);

        animarOrb(orb1,  90,  50, 11000);
        animarOrb(orb2, -70, -80, 13000);
        animarOrb(orb3,  80, -60,  9000);
        animarOrb(orb4, -60,  70, 10500);
        animarOrb(orb5,  70, -40,  8000);

        // ── CONTEÚDO CENTRAL ──────────────────────────────────────────
        VBox center = new VBox(0);
        center.setAlignment(Pos.CENTER);
        center.setMaxWidth(900);
        center.setPadding(new Insets(40, 40, 40, 40));

        // ── LOGO / BADGE ───────────────────────────────────────────────
        HBox badgeBox = new HBox();
        badgeBox.setAlignment(Pos.CENTER);
        VBox.setMargin(badgeBox, new Insets(10, 0, 28, 0));

        Label badge = new Label("SISTEMA DE PONTO");
        badge.setFont(Font.font("Helvetica Neue", FontWeight.BOLD, 11));
        badge.setTextFill(Color.web(PINK));
        badge.setStyle(
            "-fx-background-color: rgba(255,107,138,0.12);" +
            "-fx-border-color: rgba(255,107,138,0.35);" +
            "-fx-border-radius: 20; -fx-background-radius: 20;" +
            "-fx-padding: 6 16 6 16;"
        );
        badgeBox.getChildren().add(badge);

        // ── TÍTULO PRINCIPAL ──────────────────────────────────────────
        Label lblTitulo = new Label("MistyPoint");
        lblTitulo.setFont(Font.font("Helvetica Neue", FontWeight.BOLD, 72));
        lblTitulo.setTextFill(Color.WHITE);
        lblTitulo.setAlignment(Pos.CENTER);
        lblTitulo.setMaxWidth(Double.MAX_VALUE);

        // Glow animado no título
        DropShadow titleGlow = new DropShadow(60, Color.web(PURPLE, 0.0));
        lblTitulo.setEffect(titleGlow);
        Timeline titlePulse = new Timeline(
            new KeyFrame(Duration.ZERO,        new KeyValue(titleGlow.colorProperty(), Color.web(PURPLE, 0.0))),
            new KeyFrame(Duration.seconds(2),  new KeyValue(titleGlow.colorProperty(), Color.web(PURPLE, 1.0))),
            new KeyFrame(Duration.seconds(4),  new KeyValue(titleGlow.colorProperty(), Color.web(PINK,   1.0))),
            new KeyFrame(Duration.seconds(6),  new KeyValue(titleGlow.colorProperty(), Color.web(ORANGE, 1.0))),
            new KeyFrame(Duration.seconds(8),  new KeyValue(titleGlow.colorProperty(), Color.web(PURPLE, 0.0)))
        );
        titlePulse.setCycleCount(Timeline.INDEFINITE);
        titlePulse.play();

        // ── SUBTÍTULO ─────────────────────────────────────────────────
        Label lblSub = new Label("Desenvolvido por Renan Zorzal Berger, Gabrielly Abreu e Samuel Moura");
        lblSub.setFont(Font.font("Helvetica Neue", 16));
        lblSub.setTextFill(Color.web(TEXT_SEC));
        lblSub.setAlignment(Pos.CENTER);
        lblSub.setTextAlignment(TextAlignment.CENTER);
        lblSub.setMaxWidth(Double.MAX_VALUE);
        lblSub.setWrapText(true);
        VBox.setMargin(lblSub, new Insets(10, 0, 0, 0));

        // ── LINHA DIVISÓRIA GRADIENTE ──────────────────────────────────
        Region divider = new Region();
        divider.setPrefHeight(1);
        divider.setMaxWidth(400);
        divider.setStyle("-fx-background-color: linear-gradient(to right, transparent, " + PURPLE + ", " + PINK + ", transparent);");
        HBox divBox = new HBox(divider);
        divBox.setAlignment(Pos.CENTER);
        VBox.setMargin(divBox, new Insets(36, 0, 36, 0));

        // ── CARDS DE ACESSO ───────────────────────────────────────────
        HBox cardsRow = new HBox(20);
        cardsRow.setAlignment(Pos.CENTER);

        Node cardEmpresa   = criarCardAcesso(
            "Empresa", "Login",
            "Acesse o painel da empresa para gerenciar funcionários e registros de ponto.",
            PURPLE, "#7D3DAA",
            "Entrar como Empresa",
            () -> stage.setScene(new TelaLoginEmpresa().getScene(stage)),
            false
        );

        Node cardFuncionario = criarCardAcesso(
            "Funcionário", "Login",
            "Registre seu ponto de entrada e saída de forma rápida e segura.",
            PINK, "#CC3D5E",
            "Entrar como Funcionário",
            () -> stage.setScene(new TelaLoginFuncionario().getScene(stage)),
            false
        );

        Node cardCadastro = criarCardAcesso(
            "Nova Empresa", "Cadastro",
            "Cadastre sua empresa no sistema e comece a gerenciar sua equipe agora.",
            ORANGE, "#CC6A2A",
            "Cadastrar Empresa",
            () -> stage.setScene(new TelaCadastroEmpresa().getScene(stage)),
            true
        );

        cardsRow.getChildren().addAll(cardEmpresa, cardFuncionario, cardCadastro);

        // ── RODAPÉ ────────────────────────────────────────────────────
        Label lblFooter = new Label("MistyPoint · Grupo G08");
        lblFooter.setFont(Font.font("Helvetica Neue", 11));
        lblFooter.setTextFill(Color.web(TEXT_SEC, 0.5));
        lblFooter.setAlignment(Pos.CENTER);
        lblFooter.setMaxWidth(Double.MAX_VALUE);
        VBox.setMargin(lblFooter, new Insets(40, 0, 0, 0));

        center.getChildren().addAll(badgeBox, lblTitulo, lblSub, divBox, cardsRow, lblFooter);

        root.getChildren().addAll(bgLayer, center);
        StackPane.setAlignment(center, Pos.CENTER);

        // ── ANIMAÇÕES DE ENTRADA ───────────────────────────────────────
        center.setOpacity(0);
        center.setTranslateY(30);
        new ParallelTransition(
            fade(center, 0, 1, 900),
            translY(center, 30, 0, 900)
        ).play();

        // Animação escalonada dos cards
        Node[] cards = { cardEmpresa, cardFuncionario, cardCadastro };
        for (int i = 0; i < cards.length; i++) {
            Node card = cards[i];
            card.setOpacity(0);
            card.setTranslateY(24);
            PauseTransition delay = new PauseTransition(Duration.millis(500 + i * 140));
            final Node fc = card;
            delay.setOnFinished(ev ->
                new ParallelTransition(
                    fade(fc, 0, 1, 400),
                    translY(fc, 24, 0, 400)
                ).play()
            );
            delay.play();
        }

        return new Scene(root, 1280, 720);
    }

    // ──────────────────────────────────────────────────────────────────
    //  CARD DE ACESSO
    // ──────────────────────────────────────────────────────────────────
    private Node criarCardAcesso(
            String tipo, String acao,
            String descricao,
            String cor1, String cor2,
            String labelBotao,
            Runnable onClick,
            boolean destaque) {

        // Wrapper com borda gradiente
        StackPane cardOuter = new StackPane();
        cardOuter.setPrefWidth(260);
        cardOuter.setPrefHeight(380);
        cardOuter.setMinHeight(380);
        cardOuter.setMaxHeight(380);
        cardOuter.setStyle(
            "-fx-background-color: linear-gradient(to bottom right, " + cor1 + ", " + cor2 + ");" +
            "-fx-background-radius: 20;" +
            "-fx-padding: " + (destaque ? "2" : "1.5") + ";"
        );

        DropShadow shadow = new DropShadow(destaque ? 40 : 25, Color.web(cor1, destaque ? 0.55 : 0.35));
        shadow.setSpread(0.05);
        cardOuter.setEffect(shadow);

        VBox card = new VBox(0);
        card.setPadding(new Insets(30, 28, 28, 28));
        card.setAlignment(Pos.TOP_LEFT);
        card.setStyle(
            "-fx-background-color: " + CARD_BG + ";" +
            "-fx-background-radius: " + (destaque ? "18.5" : "19") + ";"
        );

        // Badge de tipo (Login / Cadastro)
        Label lblAcao = new Label(acao.toUpperCase());
        lblAcao.setFont(Font.font("Helvetica Neue", FontWeight.BOLD, 10));
        lblAcao.setTextFill(Color.web(cor1));
        lblAcao.setStyle(
            "-fx-background-color: rgba(" + hexToRgb(cor1) + ",0.15);" +
            "-fx-border-color: rgba(" + hexToRgb(cor1) + ",0.40);" +
            "-fx-border-radius: 12; -fx-background-radius: 12;" +
            "-fx-padding: 3 10 3 10;"
        );
        VBox.setMargin(lblAcao, new Insets(0, 0, 16, 0));

        // Linha decorativa no lugar do ícone grande
        Region accentTop = new Region();
        accentTop.setPrefHeight(3);
        accentTop.setMaxWidth(40);
        accentTop.setStyle(
            "-fx-background-color: linear-gradient(to right, " + cor1 + ", " + cor2 + ");" +
            "-fx-background-radius: 2;"
        );
        VBox.setMargin(accentTop, new Insets(0, 0, 14, 0));

        // Título do card
        Label lblTipo = new Label(tipo);
        lblTipo.setFont(Font.font("Helvetica Neue", FontWeight.BOLD, 22));
        lblTipo.setTextFill(Color.WHITE);
        VBox.setMargin(lblTipo, new Insets(0, 0, 8, 0));

        // Descrição
        Label lblDesc = new Label(descricao);
        lblDesc.setFont(Font.font("Helvetica Neue", 13));
        lblDesc.setTextFill(Color.web(TEXT_SEC));
        lblDesc.setWrapText(true);
        lblDesc.setMaxWidth(220);

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);
        VBox.setMargin(spacer, new Insets(20, 0, 0, 0));

        // Linha separadora
        Region linha = new Region();
        linha.setPrefHeight(1);
        linha.setMaxWidth(Double.MAX_VALUE);
        linha.setStyle("-fx-background-color: " + BORDER + ";");
        VBox.setMargin(linha, new Insets(0, 0, 20, 0));

        // Botão de ação
        Button btn = new Button(labelBotao);
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setPrefHeight(44);
        btn.setFont(Font.font("Helvetica Neue", FontWeight.BOLD, 13));
        btn.setStyle(
            "-fx-background-color: linear-gradient(to right, " + cor1 + ", " + cor2 + ");" +
            "-fx-background-radius: 12;" +
            "-fx-text-fill: white;" +
            "-fx-cursor: hand;" +
            "-fx-effect: dropshadow(gaussian, rgba(" + hexToRgb(cor1) + ",0.45), 12, 0, 0, 2);"
        );
        btn.setOnMouseEntered(e -> {
            btn.setStyle(
                "-fx-background-color: linear-gradient(to right, " + cor2 + ", " + cor1 + ");" +
                "-fx-background-radius: 12;" +
                "-fx-text-fill: white;" +
                "-fx-cursor: hand;" +
                "-fx-effect: dropshadow(gaussian, rgba(" + hexToRgb(cor1) + ",0.70), 18, 0, 0, 4);"
            );
            escala(btn, 1.0, 1.03, 120).play();
        });
        btn.setOnMouseExited(e -> {
            btn.setStyle(
                "-fx-background-color: linear-gradient(to right, " + cor1 + ", " + cor2 + ");" +
                "-fx-background-radius: 12;" +
                "-fx-text-fill: white;" +
                "-fx-cursor: hand;" +
                "-fx-effect: dropshadow(gaussian, rgba(" + hexToRgb(cor1) + ",0.45), 12, 0, 0, 2);"
            );
            escala(btn, 1.03, 1.0, 120).play();
        });
        btn.setOnAction(e -> onClick.run());

        card.getChildren().addAll(lblAcao, accentTop, lblTipo, lblDesc, spacer, linha, btn);
        cardOuter.getChildren().add(card);

        // Hover no card inteiro
        cardOuter.setOnMouseEntered(e -> {
            escala(cardOuter, 1.0, 1.03, 180).play();
            shadow.setRadius(destaque ? 55 : 40);
            shadow.setColor(Color.web(cor1, destaque ? 0.70 : 0.55));
        });
        cardOuter.setOnMouseExited(e -> {
            escala(cardOuter, 1.03, 1.0, 180).play();
            shadow.setRadius(destaque ? 40 : 25);
            shadow.setColor(Color.web(cor1, destaque ? 0.55 : 0.35));
        });

        return cardOuter;
    }

    // ──────────────────────────────────────────────────────────────────
    //  HELPERS
    // ──────────────────────────────────────────────────────────────────
    /** Converte hex "#RRGGBB" para "R,G,B" para uso em rgba() no CSS */
    private String hexToRgb(String hex) {
        hex = hex.replace("#", "");
        int r = Integer.parseInt(hex.substring(0, 2), 16);
        int g = Integer.parseInt(hex.substring(2, 4), 16);
        int b = Integer.parseInt(hex.substring(4, 6), 16);
        return r + "," + g + "," + b;
    }

    private Circle criarOrb(double raio, String cor, double op, double blur, double x, double y) {
        Circle c = new Circle(raio);
        c.setFill(Color.web(cor, op));
        c.setEffect(new GaussianBlur(blur));
        c.setLayoutX(x); c.setLayoutY(y);
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

    private ScaleTransition escala(Node n, double from, double to, int ms) {
        ScaleTransition st = new ScaleTransition(Duration.millis(ms), n);
        st.setFromX(from); st.setFromY(from);
        st.setToX(to);     st.setToY(to);
        st.setInterpolator(Interpolator.EASE_OUT);
        return st;
    }
}
