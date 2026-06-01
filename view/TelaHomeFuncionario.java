package view;

import dao.PontoDAO;
import dao.PontoDAO.PontoRow;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.Conexao;
import model.Funcionario;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class TelaHomeFuncionario {

    // ── Paleta (tema rosa/laranja do funcionário) ─────────────────────
    private static final String PINK     = "#FF6B8A";
    private static final String ORANGE   = "#FF8E53";
    private static final String PURPLE   = "#9B59B6";
    private static final String DARK     = "#09080F";
    private static final String SIDEBAR  = "#0A0715";
    private static final String CARD_BG  = "#0F081E";
    private static final String TEXT_SEC = "#9B8EC4";
    private static final String BORDER   = "#1E1035";
    private static final String GREEN    = "#34C759";

    private final Funcionario funcionario;

    // Labels de estatística
    private Label lblStatHoje;
    private Label lblStatMes;
    private Label lblUltimoPonto;

    // Tabela de pontos
    private TableView<PontoRow> tvPontos;

    // Raiz — para overlays in-page
    private StackPane rootPane;

    // Relógio em tempo real
    private Label lblRelogio;
    private Timeline clockTimeline;

    // Botão principal e estado do ponto aberto
    private Button     btnBaterPonto;
    private PontoRow   pontoAbertoAtual = null;

    public TelaHomeFuncionario(Funcionario funcionario) {
        this.funcionario = funcionario;
    }

    public Scene getScene(Stage stage) {

        // ── LAYOUT PRINCIPAL ──────────────────────────────────────────
        BorderPane layout = new BorderPane();
        layout.setStyle("-fx-background-color: " + DARK + ";");

        // ── SIDEBAR ──────────────────────────────────────────────────
        layout.setLeft(criarSidebar(stage));

        // ── CONTEÚDO ──────────────────────────────────────────────────
        VBox mainContent = criarConteudoPrincipal();
        layout.setCenter(mainContent);

        // ── ORBS ─────────────────────────────────────────────────────
        Pane bgLayer = new Pane();
        bgLayer.setMouseTransparent(true);
        Circle orb1 = criarOrb(350, PINK,   0.12, 140,  400, -100);
        Circle orb2 = criarOrb(300, ORANGE, 0.10, 120, 1100,  500);
        Circle orb3 = criarOrb(250, PURPLE, 0.08, 100,  900,   50);
        bgLayer.getChildren().addAll(orb1, orb2, orb3);
        animarOrb(orb1,  70,  40, 10000);
        animarOrb(orb2, -50, -60, 12000);
        animarOrb(orb3,  60, -40,  8000);

        rootPane = new StackPane(layout, bgLayer);

        // ── CARREGAR DADOS ────────────────────────────────────────────
        carregarDados();

        // ── ANIMAÇÃO DE ENTRADA ───────────────────────────────────────
        mainContent.setOpacity(0);
        mainContent.setTranslateX(20);
        new ParallelTransition(
            fade(mainContent, 0, 1, 600),
            translX(mainContent, 20, 0, 600)
        ).play();

        Scene scene = new Scene(rootPane, 1280, 720);
        scene.getStylesheets().add(gerarCssTabela());
        return scene;
    }

    // ──────────────────────────────────────────────────────────────────
    //  SIDEBAR
    // ──────────────────────────────────────────────────────────────────
    private VBox criarSidebar(Stage stage) {
        VBox sidebar = new VBox(0);
        sidebar.setPrefWidth(230);
        sidebar.setStyle(
            "-fx-background-color: " + SIDEBAR + ";" +
            "-fx-border-color: " + BORDER + ";" +
            "-fx-border-width: 0 1 0 0;"
        );

        // ── Logo / nome do funcionário ────────────────────────────────
        VBox logoBox = new VBox(6);
        logoBox.setPadding(new Insets(28, 20, 24, 20));
        logoBox.setStyle("-fx-border-color: " + BORDER + "; -fx-border-width: 0 0 1 0;");

        Label nomeLbl = new Label(funcionario.getNome() != null ? funcionario.getNome() : "Funcionário");
        nomeLbl.setFont(Font.font("Helvetica Neue", FontWeight.BOLD, 15));
        nomeLbl.setTextFill(Color.WHITE);
        nomeLbl.setWrapText(true);

        Label tagLbl = new Label(funcionario.getCargo() != null ? funcionario.getCargo() : "Colaborador");
        tagLbl.setFont(Font.font("Helvetica Neue", 11));
        tagLbl.setTextFill(Color.web(TEXT_SEC));

        logoBox.getChildren().addAll(nomeLbl, tagLbl);

        // ── Linha decorativa gradiente ─────────────────────────────────
        Region gradLine = new Region();
        gradLine.setPrefHeight(2);
        gradLine.setStyle("-fx-background-color: linear-gradient(to right, " + PINK + ", " + ORANGE + ");");
        VBox.setMargin(gradLine, new Insets(0, 0, 10, 0));

        // ── Relógio em tempo real ──────────────────────────────────────
        VBox clockBox = new VBox(4);
        clockBox.setPadding(new Insets(16, 20, 16, 20));
        clockBox.setAlignment(Pos.CENTER);
        clockBox.setStyle(
            "-fx-background-color: #0D0820;" +
            "-fx-border-color: " + BORDER + ";" +
            "-fx-border-width: 0 0 1 0;"
        );

        Label lblDataHoje = new Label(LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        lblDataHoje.setFont(Font.font("Helvetica Neue", 11));
        lblDataHoje.setTextFill(Color.web(TEXT_SEC));

        lblRelogio = new Label(LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
        lblRelogio.setFont(Font.font("Helvetica Neue", FontWeight.BOLD, 28));
        lblRelogio.setTextFill(Color.WHITE);

        // Pulso de cor no relógio
        DropShadow clockGlow = new DropShadow(18, Color.web(PINK, 0.0));
        lblRelogio.setEffect(clockGlow);
        Timeline pulse = new Timeline(
            new KeyFrame(Duration.ZERO,       new KeyValue(clockGlow.colorProperty(), Color.web(PINK, 0.0))),
            new KeyFrame(Duration.seconds(1), new KeyValue(clockGlow.colorProperty(), Color.web(PINK, 0.7))),
            new KeyFrame(Duration.seconds(2), new KeyValue(clockGlow.colorProperty(), Color.web(PINK, 0.0)))
        );
        pulse.setCycleCount(Timeline.INDEFINITE);
        pulse.play();

        // Atualiza o relógio a cada segundo
        clockTimeline = new Timeline(new KeyFrame(Duration.seconds(1), e ->
            lblRelogio.setText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")))
        ));
        clockTimeline.setCycleCount(Timeline.INDEFINITE);
        clockTimeline.play();

        clockBox.getChildren().addAll(lblDataHoje, lblRelogio);

        // ── Espaço expansível ──────────────────────────────────────────
        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        // ── Botão Sair ─────────────────────────────────────────────────
        Button btnSair = new Button("< Sair");
        btnSair.setMaxWidth(Double.MAX_VALUE);
        btnSair.setPrefHeight(44);
        btnSair.setFont(Font.font("Helvetica Neue", FontWeight.BOLD, 13));
        String estNormal = "-fx-background-color: transparent; -fx-background-radius: 10;" +
                           "-fx-text-fill: " + TEXT_SEC + "; -fx-cursor: hand;";
        String estHover  = "-fx-background-color: rgba(255,59,92,0.12); -fx-background-radius: 10;" +
                           "-fx-text-fill: #FF3B5C; -fx-cursor: hand;";
        btnSair.setStyle(estNormal);
        btnSair.setOnMouseEntered(e -> btnSair.setStyle(estHover));
        btnSair.setOnMouseExited(e  -> btnSair.setStyle(estNormal));
        btnSair.setOnAction(e -> {
            if (clockTimeline != null) clockTimeline.stop();
            Conexao.desconectar();
            stage.setScene(new TelaLoginFuncionario().getScene(stage));
        });
        VBox.setMargin(btnSair, new Insets(0, 12, 20, 12));

        sidebar.getChildren().addAll(logoBox, gradLine, clockBox, spacer, btnSair);
        return sidebar;
    }

    // ──────────────────────────────────────────────────────────────────
    //  CONTEÚDO PRINCIPAL
    // ──────────────────────────────────────────────────────────────────
    @SuppressWarnings("unchecked")
    private VBox criarConteudoPrincipal() {
        VBox main = new VBox(0);
        main.setPadding(new Insets(0));
        HBox.setHgrow(main, Priority.ALWAYS);

        // ── TOPBAR ───────────────────────────────────────────────────
        HBox topbar = new HBox();
        topbar.setPadding(new Insets(22, 30, 22, 30));
        topbar.setAlignment(Pos.CENTER_LEFT);
        topbar.setStyle(
            "-fx-background-color: " + SIDEBAR + ";" +
            "-fx-border-color: " + BORDER + ";" +
            "-fx-border-width: 0 0 1 0;"
        );

        VBox tituloBox = new VBox(2);
        Label lblBoasVindas = new Label("Bem-vindo(a) de volta!");
        lblBoasVindas.setFont(Font.font("Helvetica Neue", 13));
        lblBoasVindas.setTextFill(Color.web(TEXT_SEC));
        Label lblNomeFunc = new Label(funcionario.getNome() != null ? funcionario.getNome() : "Funcionário");
        lblNomeFunc.setFont(Font.font("Helvetica Neue", FontWeight.BOLD, 22));
        lblNomeFunc.setTextFill(Color.WHITE);
        tituloBox.getChildren().addAll(lblBoasVindas, lblNomeFunc);

        Region sp = new Region();
        HBox.setHgrow(sp, Priority.ALWAYS);

        // ── Botão BATER / FECHAR PONTO ────────────────────────────
        btnBaterPonto = new Button("⏱  Bater Ponto");
        btnBaterPonto.setPrefHeight(46);
        btnBaterPonto.setFont(Font.font("Helvetica Neue", FontWeight.BOLD, 14));
        btnBaterPonto.setStyle(
            "-fx-background-color: linear-gradient(to right," + PINK + "," + ORANGE + ");" +
            "-fx-background-radius: 12;" +
            "-fx-text-fill: white;" +
            "-fx-cursor: hand;" +
            "-fx-padding: 0 24 0 24;" +
            "-fx-effect: dropshadow(gaussian, rgba(255,107,138,0.5), 14, 0, 0, 3);"
        );
        btnBaterPonto.setOnMouseEntered(e -> escala(btnBaterPonto, 1.0, 1.05, 120).play());
        btnBaterPonto.setOnMouseExited(e  -> escala(btnBaterPonto, 1.05, 1.0, 120).play());
        btnBaterPonto.setOnAction(e -> acaoPonto());

        topbar.getChildren().addAll(tituloBox, sp, btnBaterPonto);

        // ── CARDS DE ESTATÍSTICA ──────────────────────────────────────
        HBox cardsBox = new HBox(16);
        cardsBox.setPadding(new Insets(24, 30, 0, 30));

        lblStatHoje   = new Label("–");
        lblStatMes    = new Label("–");
        lblUltimoPonto = new Label("–");

        Node cardHoje   = criarCard("Pontos Hoje",     lblStatHoje,    PINK,   "#CC3D5E");
        Node cardMes    = criarCard("Horas no Mês",    lblStatMes,     ORANGE, "#CC6A2A");
        Node cardUltimo = criarCard("Último Registro", lblUltimoPonto, PURPLE, "#7D3DAA");

        HBox.setHgrow(cardHoje,   Priority.ALWAYS);
        HBox.setHgrow(cardMes,    Priority.ALWAYS);
        HBox.setHgrow(cardUltimo, Priority.ALWAYS);
        cardsBox.getChildren().addAll(cardHoje, cardMes, cardUltimo);

        // ── TABELA DE PONTOS ──────────────────────────────────────────
        VBox tabelaBox = new VBox(14);
        tabelaBox.setPadding(new Insets(24, 30, 30, 30));
        VBox.setVgrow(tabelaBox, Priority.ALWAYS);

        Label tituloTabela = new Label("Histórico de Pontos");
        tituloTabela.setFont(Font.font("Helvetica Neue", FontWeight.BOLD, 16));
        tituloTabela.setTextFill(Color.WHITE);

        tvPontos = new TableView<>();
        tvPontos.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        VBox.setVgrow(tvPontos, Priority.ALWAYS);

        Label placeholder = new Label("Nenhum ponto registrado ainda.");
        placeholder.setFont(Font.font("Helvetica Neue", 14));
        placeholder.setTextFill(Color.web(TEXT_SEC));
        tvPontos.setPlaceholder(placeholder);

        TableColumn<PontoRow, String> colData = new TableColumn<>("Data");
        colData.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(
                formatarData(d.getValue().getData())));
        colData.setPrefWidth(160);

        TableColumn<PontoRow, String> colEntrada = new TableColumn<>("Entrada");
        colEntrada.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(
                d.getValue().getHorario()));
        colEntrada.setPrefWidth(100);

        TableColumn<PontoRow, String> colSaida = new TableColumn<>("Saída");
        colSaida.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(
                d.getValue().getHorarioFechamento().isBlank() ? "–" : d.getValue().getHorarioFechamento()));
        colSaida.setPrefWidth(100);

        TableColumn<PontoRow, String> colTotal = new TableColumn<>("Total");
        colTotal.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(
                d.getValue().getTotalHoras()));
        colTotal.setPrefWidth(100);

        // Coluna de status com badge colorido (ABERTO = amarelo, FECHADO = verde)
        TableColumn<PontoRow, String> colStatus = new TableColumn<>("Status");
        colStatus.setSortable(false);
        colStatus.setCellFactory(col -> new TableCell<PontoRow, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getIndex() >= getTableView().getItems().size()) {
                    setGraphic(null);
                    return;
                }
                PontoRow row = getTableView().getItems().get(getIndex());
                boolean aberto = row.isAberto();
                String cor    = aberto ? "#FFD60A" : GREEN;
                String corBg  = aberto ? "rgba(255,214,10,0.15)" : "rgba(52,199,89,0.18)";
                String texto  = aberto ? "⏳  Em aberto" : "✓  Fechado";
                Label badge = new Label(texto);
                badge.setFont(Font.font("Helvetica Neue", FontWeight.BOLD, 11));
                badge.setStyle(
                    "-fx-background-color: " + corBg + ";" +
                    "-fx-border-color: " + cor + ";" +
                    "-fx-border-radius: 6; -fx-background-radius: 6;" +
                    "-fx-text-fill: " + cor + ";" +
                    "-fx-padding: 3 10 3 10;"
                );
                setGraphic(badge);
                setAlignment(Pos.CENTER);
            }
        });

        tvPontos.getColumns().addAll(colData, colEntrada, colSaida, colTotal, colStatus);

        tabelaBox.getChildren().addAll(tituloTabela, tvPontos);

        main.getChildren().addAll(topbar, cardsBox, tabelaBox);
        return main;
    }

    // ──────────────────────────────────────────────────────────────────
    //  AÇÃO DO BOTÃO (bater ou fechar ponto)
    // ──────────────────────────────────────────────────────────────────
    private void acaoPonto() {
        if (pontoAbertoAtual != null) {
            fecharPonto();
        } else {
            baterPonto();
        }
    }

    private void baterPonto() {
        try {
            Conexao.conectar();
            PontoDAO dao = new PontoDAO(Conexao.conexao);
            dao.registrarPonto(funcionario.getIdFuncionario());
            Conexao.desconectar();
            carregarDados();
            mostrarConfirmacaoPonto(false);
        } catch (IllegalStateException ex) {
            mostrarErroPonto(ex.getMessage());
        } catch (Exception ex) {
            ex.printStackTrace();
            mostrarErroPonto("Erro ao registrar ponto: " + ex.getMessage());
        }
    }

    private void fecharPonto() {
        try {
            Conexao.conectar();
            PontoDAO dao = new PontoDAO(Conexao.conexao);
            dao.fecharPonto(pontoAbertoAtual.getId(), funcionario.getIdFuncionario());
            Conexao.desconectar();
            carregarDados();
            mostrarConfirmacaoPonto(true);
        } catch (IllegalStateException ex) {
            mostrarErroPonto(ex.getMessage());
        } catch (Exception ex) {
            ex.printStackTrace();
            mostrarErroPonto("Erro ao fechar ponto: " + ex.getMessage());
        }
    }

    /** Atualiza aparência do botão de acordo com o estado atual do ponto. */
    private void atualizarBotaoPonto() {
        if (pontoAbertoAtual != null) {
            btnBaterPonto.setText("⏹  Fechar Ponto");
            btnBaterPonto.setStyle(
                "-fx-background-color: linear-gradient(to right, #FFD60A, #FF8E53);" +
                "-fx-background-radius: 12;" +
                "-fx-text-fill: #0F081E;" +
                "-fx-cursor: hand;" +
                "-fx-padding: 0 24 0 24;" +
                "-fx-effect: dropshadow(gaussian, rgba(255,214,10,0.5), 14, 0, 0, 3);"
            );
        } else {
            btnBaterPonto.setText("⏱  Bater Ponto");
            btnBaterPonto.setStyle(
                "-fx-background-color: linear-gradient(to right," + PINK + "," + ORANGE + ");" +
                "-fx-background-radius: 12;" +
                "-fx-text-fill: white;" +
                "-fx-cursor: hand;" +
                "-fx-padding: 0 24 0 24;" +
                "-fx-effect: dropshadow(gaussian, rgba(255,107,138,0.5), 14, 0, 0, 3);"
            );
        }
    }

    // ──────────────────────────────────────────────────────────────────
    //  OVERLAY DE CONFIRMAÇÃO (in-page, sem nova janela)
    // ──────────────────────────────────────────────────────────────────
    /**
     * @param fechando true = acabou de fechar ponto, false = acabou de abrir
     */
    private void mostrarConfirmacaoPonto(boolean fechando) {
        StackPane overlay = new StackPane();
        overlay.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        overlay.setStyle("-fx-background-color: rgba(0,0,0,0.65);");

        String cor1 = fechando ? GREEN : PINK;
        String cor2 = fechando ? "#2A9D47" : ORANGE;

        StackPane cardOuter = new StackPane();
        cardOuter.setMaxWidth(400);
        cardOuter.setMaxHeight(Region.USE_PREF_SIZE);
        cardOuter.setStyle(
            "-fx-background-color: linear-gradient(to bottom right, " + cor1 + ", " + cor2 + ");" +
            "-fx-background-radius: 22; -fx-padding: 1.5;"
        );
        DropShadow glow = new DropShadow(40, Color.web(cor1, 0.6));
        glow.setSpread(0.05);
        cardOuter.setEffect(glow);

        VBox card = new VBox(14);
        card.setPadding(new Insets(36, 40, 36, 40));
        card.setAlignment(Pos.CENTER);
        card.setStyle("-fx-background-color: #0F081E; -fx-background-radius: 21;");

        Label icone = new Label(fechando ? "⏹" : "⏱");
        icone.setFont(Font.font("Helvetica Neue", FontWeight.BOLD, 48));
        icone.setTextFill(Color.web(cor1));
        DropShadow iconGlow = new DropShadow(24, Color.web(cor1, 0.8));
        icone.setEffect(iconGlow);

        Label titulo = new Label(fechando ? "Ponto Fechado!" : "Ponto em Aberto!");
        titulo.setFont(Font.font("Helvetica Neue", FontWeight.BOLD, 20));
        titulo.setTextFill(Color.WHITE);

        String horarioAtual = java.time.LocalTime.now()
                .format(DateTimeFormatter.ofPattern("HH:mm"));
        String dataAtual = java.time.LocalDate.now()
                .format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        Label detalhe = new Label(dataAtual + "  ·  " + horarioAtual);
        detalhe.setFont(Font.font("Helvetica Neue", 14));
        detalhe.setTextFill(Color.web(TEXT_SEC));

        // Aviso apenas ao abrir ponto
        Label aviso = null;
        if (!fechando) {
            aviso = new Label("Lembre-se de fechar o ponto ao sair!");
            aviso.setFont(Font.font("Helvetica Neue", FontWeight.BOLD, 12));
            aviso.setTextFill(Color.web("#FFD60A"));
            aviso.setWrapText(true);
        }

        Button btnOk = new Button("OK");
        btnOk.setPrefHeight(42);
        btnOk.setPrefWidth(120);
        btnOk.setFont(Font.font("Helvetica Neue", FontWeight.BOLD, 14));
        btnOk.setStyle(
            "-fx-background-color: linear-gradient(to right," + cor1 + "," + cor2 + ");" +
            "-fx-background-radius: 10; -fx-text-fill: white; -fx-cursor: hand;"
        );
        VBox.setMargin(btnOk, new Insets(10, 0, 0, 0));

        Runnable fecharOverlay = () -> {
            FadeTransition ftOut = fade(overlay, 1, 0, 180);
            ftOut.setOnFinished(ev -> rootPane.getChildren().remove(overlay));
            ftOut.play();
        };

        btnOk.setOnAction(e -> fecharOverlay.run());
        btnOk.setOnMouseEntered(e -> escala(btnOk, 1.0, 1.05, 120).play());
        btnOk.setOnMouseExited(e  -> escala(btnOk, 1.05, 1.0, 120).play());
        overlay.setOnMouseClicked(e -> { if (e.getTarget() == overlay) fecharOverlay.run(); });

        // Auto-fechar após 4 segundos
        Timeline autoFechar = new Timeline(new KeyFrame(Duration.seconds(4), e -> fecharOverlay.run()));
        autoFechar.play();

        card.getChildren().addAll(icone, titulo, detalhe);
        if (aviso != null) card.getChildren().add(aviso);
        card.getChildren().add(btnOk);
        cardOuter.getChildren().add(card);
        overlay.getChildren().add(cardOuter);
        StackPane.setAlignment(cardOuter, Pos.CENTER);
        StackPane.setMargin(cardOuter, new Insets(20));

        rootPane.getChildren().add(overlay);

        overlay.setOpacity(0);
        cardOuter.setScaleX(0.88);
        cardOuter.setScaleY(0.88);
        ScaleTransition st = new ScaleTransition(Duration.millis(280), cardOuter);
        st.setFromX(0.88); st.setFromY(0.88);
        st.setToX(1.0);    st.setToY(1.0);
        st.setInterpolator(Interpolator.EASE_OUT);
        new ParallelTransition(fade(overlay, 0, 1, 280), st).play();
    }

    private void mostrarErroPonto(String mensagem) {
        StackPane overlay = new StackPane();
        overlay.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        overlay.setStyle("-fx-background-color: rgba(0,0,0,0.65);");

        StackPane cardOuter = new StackPane();
        cardOuter.setMaxWidth(380);
        cardOuter.setMaxHeight(Region.USE_PREF_SIZE);
        cardOuter.setStyle(
            "-fx-background-color: linear-gradient(to bottom right, #FF3B5C, #FF6B8A, #CC1F3A);" +
            "-fx-background-radius: 22; -fx-padding: 1.5;"
        );
        DropShadow glow = new DropShadow(40, Color.web("#FF3B5C", 0.55));
        glow.setSpread(0.05);
        cardOuter.setEffect(glow);

        VBox card = new VBox(14);
        card.setPadding(new Insets(36, 40, 36, 40));
        card.setAlignment(Pos.CENTER);
        card.setStyle("-fx-background-color: #0F081E; -fx-background-radius: 21;");

        Label icone = new Label("⚠");
        icone.setFont(Font.font("Helvetica Neue", FontWeight.BOLD, 40));
        icone.setTextFill(Color.web("#FF3B5C"));

        Label titulo = new Label("Erro ao Registrar");
        titulo.setFont(Font.font("Helvetica Neue", FontWeight.BOLD, 18));
        titulo.setTextFill(Color.WHITE);

        Label detalhe = new Label(mensagem);
        detalhe.setFont(Font.font("Helvetica Neue", 12));
        detalhe.setTextFill(Color.web(TEXT_SEC));
        detalhe.setWrapText(true);
        detalhe.setMaxWidth(300);

        Button btnOk = new Button("Fechar");
        btnOk.setPrefHeight(40);
        btnOk.setPrefWidth(120);
        btnOk.setFont(Font.font("Helvetica Neue", FontWeight.BOLD, 13));
        btnOk.setStyle(
            "-fx-background-color: #FF3B5C; -fx-background-radius: 10;" +
            "-fx-text-fill: white; -fx-cursor: hand;"
        );
        VBox.setMargin(btnOk, new Insets(8, 0, 0, 0));

        Runnable fechar = () -> {
            FadeTransition ftOut = fade(overlay, 1, 0, 180);
            ftOut.setOnFinished(ev -> rootPane.getChildren().remove(overlay));
            ftOut.play();
        };
        btnOk.setOnAction(e -> fechar.run());
        overlay.setOnMouseClicked(e -> { if (e.getTarget() == overlay) fechar.run(); });

        card.getChildren().addAll(icone, titulo, detalhe, btnOk);
        cardOuter.getChildren().add(card);
        overlay.getChildren().add(cardOuter);
        StackPane.setAlignment(cardOuter, Pos.CENTER);
        StackPane.setMargin(cardOuter, new Insets(20));
        rootPane.getChildren().add(overlay);

        overlay.setOpacity(0);
        cardOuter.setScaleX(0.88); cardOuter.setScaleY(0.88);
        ScaleTransition st = new ScaleTransition(Duration.millis(280), cardOuter);
        st.setFromX(0.88); st.setFromY(0.88); st.setToX(1.0); st.setToY(1.0);
        st.setInterpolator(Interpolator.EASE_OUT);
        new ParallelTransition(fade(overlay, 0, 1, 280), st).play();
    }

    // ──────────────────────────────────────────────────────────────────
    //  CARREGAR DADOS
    // ──────────────────────────────────────────────────────────────────
    private void carregarDados() {
        try {
            Conexao.conectar();
            PontoDAO dao = new PontoDAO(Conexao.conexao);

            int hoje = dao.contarPontosHoje(funcionario.getIdFuncionario());
            String mes  = dao.somarHorasMes(funcionario.getIdFuncionario());
            List<PontoRow> pontos = dao.listarPorFuncionario(funcionario.getIdFuncionario());

            // Detecta se existe ponto aberto hoje
            pontoAbertoAtual = dao.buscarPontoAberto(funcionario.getIdFuncionario());

            lblStatHoje.setText(String.valueOf(hoje));
            lblStatMes.setText(mes);

            if (!pontos.isEmpty()) {
                PontoRow ultimo = pontos.get(0);
                String label = ultimo.isAberto()
                    ? ultimo.getHorario() + " (aberto)"
                    : ultimo.getHorario();
                lblUltimoPonto.setText(label);
            } else {
                lblUltimoPonto.setText("–");
            }

            ObservableList<PontoRow> ol = FXCollections.observableArrayList(pontos);
            tvPontos.setItems(ol);

            // Atualiza aparência do botão
            if (btnBaterPonto != null) atualizarBotaoPonto();

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            Conexao.desconectar();
        }
    }

    // ──────────────────────────────────────────────────────────────────
    //  HELPERS DE COMPONENTES
    // ──────────────────────────────────────────────────────────────────
    private Node criarCard(String titulo, Label lblValor, String cor1, String cor2) {
        VBox card = new VBox(8);
        card.setPadding(new Insets(20, 22, 20, 22));
        card.setStyle(
            "-fx-background-color: " + CARD_BG + ";" +
            "-fx-background-radius: 16;" +
            "-fx-border-color: " + BORDER + ";" +
            "-fx-border-radius: 16;" +
            "-fx-border-width: 1;"
        );
        DropShadow shadow = new DropShadow(20, Color.web(cor1, 0.25));
        card.setEffect(shadow);

        Region accentBar = new Region();
        accentBar.setPrefWidth(3);
        accentBar.setPrefHeight(32);
        accentBar.setStyle(
            "-fx-background-color: linear-gradient(to bottom, " + cor1 + ", " + cor2 + ");" +
            "-fx-background-radius: 2;"
        );

        HBox iconRow = new HBox(10);
        iconRow.setAlignment(Pos.CENTER_LEFT);
        iconRow.getChildren().add(accentBar);

        Label lblTitulo = new Label(titulo);
        lblTitulo.setFont(Font.font("Helvetica Neue", 12));
        lblTitulo.setTextFill(Color.web(TEXT_SEC));

        lblValor.setFont(Font.font("Helvetica Neue", FontWeight.BOLD, 32));
        lblValor.setTextFill(Color.WHITE);

        card.getChildren().addAll(iconRow, lblTitulo, lblValor);

        String estNormal = "-fx-background-color: " + CARD_BG + "; -fx-background-radius: 16;" +
                           "-fx-border-color: " + BORDER + "; -fx-border-radius: 16; -fx-border-width: 1;";
        String estHover  = "-fx-background-color: #130825; -fx-background-radius: 16;" +
                           "-fx-border-color: " + cor1 + "; -fx-border-radius: 16; -fx-border-width: 1;";

        card.setOnMouseEntered(e -> { card.setStyle(estHover);  escala(card, 1.0, 1.02, 150).play(); });
        card.setOnMouseExited(e  -> { card.setStyle(estNormal); escala(card, 1.02, 1.0, 150).play(); });

        return card;
    }

    private String formatarData(String dataBanco) {
        // dataBanco vem como "yyyy-MM-dd" do SQL Server
        try {
            String[] p = dataBanco.split("-");
            return p[2] + "/" + p[1] + "/" + p[0];
        } catch (Exception e) {
            return dataBanco;
        }
    }

    // ──────────────────────────────────────────────────────────────────
    //  ORBS E ANIMAÇÕES
    // ──────────────────────────────────────────────────────────────────
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

    private String gerarCssTabela() {
        String css = ".table-view { -fx-background-color: transparent; -fx-border-color: #1E1035; -fx-border-radius: 12; -fx-background-radius: 12; }\n"
                   + ".table-view .column-header-background { -fx-background-color: #0F081E; -fx-background-radius: 12 12 0 0; }\n"
                   + ".table-view .column-header { -fx-background-color: transparent; -fx-border-color: #1E1035; -fx-border-width: 0 0 1 0; -fx-padding: 10; }\n"
                   + ".table-view .column-header .label { -fx-text-fill: #9B8EC4; -fx-font-weight: bold; -fx-font-family: \"Helvetica Neue\", sans-serif; }\n"
                   + ".table-view .table-row-cell { -fx-background-color: #0F081E; -fx-border-color: #1E1035; -fx-border-width: 0 0 1 0; -fx-padding: 4 0 4 0; }\n"
                   + ".table-view .table-row-cell:odd { -fx-background-color: #140A28; }\n"
                   + ".table-view .table-row-cell:hover { -fx-background-color: #20113B; }\n"
                   + ".table-view .table-row-cell:selected { -fx-background-color: #2D1854; }\n"
                   + ".table-view .table-cell { -fx-text-fill: white; -fx-font-family: \"Helvetica Neue\", sans-serif; -fx-padding: 8; -fx-alignment: center; -fx-border-width: 0; }\n"
                   + ".table-view .placeholder .label { -fx-text-fill: #9B8EC4; -fx-font-family: \"Helvetica Neue\", sans-serif; }";
        String b64 = java.util.Base64.getEncoder().encodeToString(css.getBytes(java.nio.charset.StandardCharsets.UTF_8));
        return "data:text/css;base64," + b64;
    }
}
