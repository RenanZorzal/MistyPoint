package view;

import dao.FuncionarioDAO;
import dao.HomeEmpresaDAO;
import dao.HomeEmpresaDAO.FuncionarioRow;
import dao.HomeEmpresaDAO.PontoRow;
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
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
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
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import model.Conexao;
import model.Empresa;

import java.util.List;

public class TelaHomeEmpresa {

    // ── Paleta (mesma do projeto) ────────────────────────────────────
    private static final String PINK     = "#FF6B8A";
    private static final String ORANGE   = "#FF8E53";
    private static final String PURPLE   = "#9B59B6";
    private static final String DARK     = "#09080F";
    private static final String SIDEBAR  = "#0A0715";
    private static final String CARD_BG  = "#0F081E";
    private static final String TEXT_SEC = "#9B8EC4";
    private static final String BORDER   = "#1E1035";

    private Empresa empresa;
    private int     idEmpresa;

    // Tabelas JavaFX
    private TableView<FuncionarioRow> tvFuncionarios;
    private TableView<PontoRow>       tvPontos;

    // Labels dos cartões de estatística
    private Label lblStatFunc;
    private Label lblStatHoje;
    private Label lblStatMes;

    // Painel de conteúdo central (troca de aba)
    private StackPane contentArea;

    // Painéis das abas
    private VBox painelFuncionarios;
    private VBox painelPontos;

    // Botões de aba (para destacar o ativo)
    private Button btnAbaFuncionarios;
    private Button btnAbaPontos;

    // Raiz da cena — usada para exibir overlays in-page
    private StackPane rootPane;

    public TelaHomeEmpresa(Empresa empresa, int idEmpresa) {
        this.empresa   = empresa;
        this.idEmpresa = idEmpresa;
    }

    public Scene getScene(Stage stage) {

        // ── LAYOUT PRINCIPAL ──────────────────────────────────────────
        BorderPane layout = new BorderPane();
        layout.setStyle("-fx-background-color: " + DARK + ";");

        // ── SIDEBAR ──────────────────────────────────────────────────
        VBox sidebar = criarSidebar(stage);
        layout.setLeft(sidebar);

        // ── CONTEÚDO PRINCIPAL (direto no centro — sem StackPane intermediário) ──
        VBox mainContent = criarConteudoPrincipal();
        layout.setCenter(mainContent);

        // ── FUNDO COM ORBS (overlay mouseTransparent — não bloqueia cliques) ──
        Pane bgLayer = new Pane();
        bgLayer.setMouseTransparent(true);
        bgLayer.setStyle("-fx-background-color: transparent;");

        Circle orb1 = criarOrb(350, PURPLE, 0.12, 140, 400, -100);
        Circle orb2 = criarOrb(300, ORANGE, 0.10, 120, 1100, 500);
        Circle orb3 = criarOrb(250, PINK,   0.08, 100, 900,  50);
        bgLayer.getChildren().addAll(orb1, orb2, orb3);
        animarOrb(orb1,  70,  40, 10000);
        animarOrb(orb2, -50, -60, 12000);
        animarOrb(orb3,  60, -40,  8000);

        // ── RAIZ DA CENA: layout atrás, orbs na frente (mas sem capturar mouse) ──
        rootPane = new StackPane(layout, bgLayer);

        // Carregar dados
        carregarDados();

        // Animação de entrada
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

        // Logo / Nome da empresa
        VBox logoBox = new VBox(6);
        logoBox.setPadding(new Insets(28, 20, 24, 20));
        logoBox.setStyle("-fx-border-color: " + BORDER + "; -fx-border-width: 0 0 1 0;");

        Label nomeLbl = new Label(empresa.getNomeEmpresa());
        nomeLbl.setFont(Font.font("Helvetica Neue", FontWeight.BOLD, 15));
        nomeLbl.setTextFill(Color.WHITE);
        nomeLbl.setWrapText(true);

        Label tagLbl = new Label("Painel da Empresa");
        tagLbl.setFont(Font.font("Helvetica Neue", 11));
        tagLbl.setTextFill(Color.web(TEXT_SEC));

        logoBox.getChildren().addAll(nomeLbl, tagLbl);

        // Linha decorativa gradiente
        Region gradLine = new Region();
        gradLine.setPrefHeight(2);
        gradLine.setStyle("-fx-background-color: linear-gradient(to right, " + PURPLE + ", " + ORANGE + ");");
        VBox.setMargin(gradLine, new Insets(0, 0, 10, 0));

        // Itens de navegação
        VBox navBox = new VBox(4);
        navBox.setPadding(new Insets(16, 12, 16, 12));

        btnAbaFuncionarios = criarBotaoNav("Funcionários", true);
        btnAbaPontos       = criarBotaoNav("Registro de Pontos", false);

        btnAbaFuncionarios.setOnAction(e -> mudarAba(true));
        btnAbaPontos.setOnAction(e -> mudarAba(false));

        // Separador
        Region sep = new Region();
        sep.setPrefHeight(1);
        sep.setStyle("-fx-background-color: " + BORDER + ";");
        VBox.setMargin(sep, new Insets(8, 0, 8, 0));

        // Botão de ação — Cadastrar Funcionário
        Button btnCadastrar = new Button("+  Cadastrar Funcionário");
        btnCadastrar.setMaxWidth(Double.MAX_VALUE);
        btnCadastrar.setPrefHeight(44);
        btnCadastrar.setFont(Font.font("Helvetica Neue", FontWeight.BOLD, 13));
        btnCadastrar.setStyle(
            "-fx-background-color: linear-gradient(to right, " + PURPLE + ", " + ORANGE + ");" +
            "-fx-background-radius: 10;" +
            "-fx-text-fill: white;" +
            "-fx-cursor: hand;"
        );
        btnCadastrar.setOnMouseEntered(e -> escala(btnCadastrar, 1.0, 1.03, 120).play());
        btnCadastrar.setOnMouseExited(e  -> escala(btnCadastrar, 1.03, 1.0, 120).play());
        btnCadastrar.setOnAction(e -> {
            TelaCadastroFuncionario tela = new TelaCadastroFuncionario(stage, empresa, idEmpresa);
            stage.setScene(tela.getScene());
        });

        navBox.getChildren().addAll(btnAbaFuncionarios, btnAbaPontos, sep, btnCadastrar);

        // Espaço expansível
        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        // Botão Sair
        Button btnSair = new Button("< Sair");
        btnSair.setMaxWidth(Double.MAX_VALUE);
        btnSair.setPrefHeight(44);
        btnSair.setFont(Font.font("Helvetica Neue", FontWeight.BOLD, 13));
        btnSair.setStyle(estiloBotaoNav(false));
        btnSair.setOnMouseEntered(e -> btnSair.setStyle(
            "-fx-background-color: rgba(255,59,92,0.12);" +
            "-fx-background-radius: 10;" +
            "-fx-text-fill: #FF3B5C;" +
            "-fx-cursor: hand;"
        ));
        btnSair.setOnMouseExited(e -> btnSair.setStyle(estiloBotaoNav(false)));
        btnSair.setOnAction(e -> {
            Conexao.desconectar();
            stage.setScene(new TelaLoginEmpresa().getScene(stage));
        });
        VBox.setMargin(btnSair, new Insets(0, 12, 20, 12));

        sidebar.getChildren().addAll(logoBox, gradLine, navBox, spacer, btnSair);
        return sidebar;
    }

    // ──────────────────────────────────────────────────────────────────
    //  CONTEÚDO PRINCIPAL
    // ──────────────────────────────────────────────────────────────────
    private VBox criarConteudoPrincipal() {
        VBox main = new VBox(0);
        main.setPadding(new Insets(0));
        HBox.setHgrow(main, Priority.ALWAYS);

        // ── TOPBAR ──────────────────────────────────────────────────
        HBox topbar = new HBox();
        topbar.setPadding(new Insets(22, 30, 22, 30));
        topbar.setAlignment(Pos.CENTER_LEFT);
        topbar.setStyle(
            "-fx-background-color: " + SIDEBAR + ";" +
            "-fx-border-color: " + BORDER + ";" +
            "-fx-border-width: 0 0 1 0;"
        );

        VBox tituloBox = new VBox(2);
        Label lblBoasVindas = new Label("Bem-vinda(o) de volta!");
        lblBoasVindas.setFont(Font.font("Helvetica Neue", 13));
        lblBoasVindas.setTextFill(Color.web(TEXT_SEC));
        Label lblNomeEmp = new Label(empresa.getNomeEmpresa());
        lblNomeEmp.setFont(Font.font("Helvetica Neue", FontWeight.BOLD, 22));
        lblNomeEmp.setTextFill(Color.WHITE);
        tituloBox.getChildren().addAll(lblBoasVindas, lblNomeEmp);

        Region sp = new Region();
        HBox.setHgrow(sp, Priority.ALWAYS);

        // Botão atualizar
        Button btnAtualizar = new Button("↺  Atualizar");
        btnAtualizar.setPrefHeight(38);
        btnAtualizar.setFont(Font.font("Helvetica Neue", FontWeight.BOLD, 13));
        btnAtualizar.setStyle(
            "-fx-background-color: linear-gradient(to right," + PURPLE + "," + ORANGE + ");" +
            "-fx-background-radius: 10;" +
            "-fx-text-fill: white;" +
            "-fx-cursor: hand;" +
            "-fx-padding: 0 18 0 18;" +
            "-fx-effect: dropshadow(gaussian, rgba(155,89,182,0.4), 10, 0, 0, 2);"
        );
        btnAtualizar.setOnMouseEntered(e -> escala(btnAtualizar, 1.0, 1.05, 120).play());
        btnAtualizar.setOnMouseExited(e  -> escala(btnAtualizar, 1.05, 1.0, 120).play());
        btnAtualizar.setOnAction(e -> carregarDados());

        topbar.getChildren().addAll(tituloBox, sp, btnAtualizar);

        // ── CARDS DE ESTATÍSTICA ─────────────────────────────────────
        HBox cardsBox = new HBox(16);
        cardsBox.setPadding(new Insets(24, 30, 0, 30));

        lblStatFunc = new Label("–");
        lblStatHoje = new Label("–");
        lblStatMes  = new Label("–");

        Node cardFunc = criarCard("Total de Funcionários", lblStatFunc, PURPLE,    "#7D3DAA");
        Node cardHoje = criarCard("Pontos Hoje",           lblStatHoje, "#FF6B8A", "#CC3D5E");
        Node cardMes  = criarCard("Pontos no Mês",         lblStatMes,  ORANGE,    "#CC6A2A");

        HBox.setHgrow(cardFunc, Priority.ALWAYS);
        HBox.setHgrow(cardHoje, Priority.ALWAYS);
        HBox.setHgrow(cardMes,  Priority.ALWAYS);
        cardsBox.getChildren().addAll(cardFunc, cardHoje, cardMes);

        // ── ÁREA DE ABAS ─────────────────────────────────────────────
        painelFuncionarios = criarPainelFuncionarios();
        painelPontos       = criarPainelPontos();

        contentArea = new StackPane();
        contentArea.setPadding(new Insets(20, 30, 30, 30));
        VBox.setVgrow(contentArea, Priority.ALWAYS);
        contentArea.getChildren().addAll(painelPontos, painelFuncionarios);
        painelPontos.setVisible(false);

        main.getChildren().addAll(topbar, cardsBox, contentArea);
        return main;
    }

    // ──────────────────────────────────────────────────────────────────
    //  PAINEL FUNCIONÁRIOS
    // ──────────────────────────────────────────────────────────────────
    @SuppressWarnings("unchecked")
    private VBox criarPainelFuncionarios() {
        VBox painel = new VBox(14);
        VBox.setVgrow(painel, Priority.ALWAYS);

        Label titulo = new Label("Funcionários cadastrados");
        titulo.setFont(Font.font("Helvetica Neue", FontWeight.BOLD, 16));
        titulo.setTextFill(Color.WHITE);

        tvFuncionarios = new TableView<>();
        tvFuncionarios.setStyle(estiloTabela());
        tvFuncionarios.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        VBox.setVgrow(tvFuncionarios, Priority.ALWAYS);
        tvFuncionarios.setPlaceholder(criarPlaceholder("Nenhum funcionário cadastrado ainda."));

        TableColumn<FuncionarioRow, String> colNome  = new TableColumn<>("Nome");
        colNome.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(d.getValue().getNome()));
        colNome.setPrefWidth(180);

        TableColumn<FuncionarioRow, String> colCpf   = new TableColumn<>("CPF");
        colCpf.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(d.getValue().getCpf()));
        colCpf.setPrefWidth(120);

        TableColumn<FuncionarioRow, String> colCargo = new TableColumn<>("Cargo");
        colCargo.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(d.getValue().getCargo()));
        colCargo.setPrefWidth(130);

        TableColumn<FuncionarioRow, String> colTel   = new TableColumn<>("Telefone");
        colTel.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(d.getValue().getTelefone()));
        colTel.setPrefWidth(120);

        TableColumn<FuncionarioRow, String> colEmail = new TableColumn<>("E-mail");
        colEmail.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(d.getValue().getEmail()));
        colEmail.setPrefWidth(170);

        // ── Coluna de Ações ──────────────────────────────────────────
        TableColumn<FuncionarioRow, Void> colAcoes = new TableColumn<>("Ações");
        colAcoes.setPrefWidth(140);
        colAcoes.setSortable(false);
        colAcoes.setCellFactory(col -> new TableCell<FuncionarioRow, Void>() {
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                    return;
                }

                Button btnEditar  = new Button("✎ Editar");
                Button btnExcluir = new Button("✕ Excluir");
                HBox   box        = new HBox(6, btnEditar, btnExcluir);
                box.setAlignment(Pos.CENTER);

                btnEditar.setFont(Font.font("Helvetica Neue", FontWeight.BOLD, 11));
                btnEditar.setPrefHeight(28);
                btnEditar.setStyle(
                    "-fx-background-color: linear-gradient(to right," + PURPLE + "," + ORANGE + ");" +
                    "-fx-background-radius: 7; -fx-text-fill: white; -fx-cursor: hand; -fx-padding: 0 10 0 10;"
                );
                btnEditar.setOnMouseEntered(e -> escala(btnEditar, 1.0, 1.08, 100).play());
                btnEditar.setOnMouseExited (e -> escala(btnEditar, 1.08, 1.0, 100).play());
                btnEditar.setOnAction(e -> abrirModalEdicao(
                    getTableView().getItems().get(getIndex())
                ));

                String estiloExcluirNormal =
                    "-fx-background-color: rgba(255,59,92,0.18);" +
                    "-fx-background-radius: 7; -fx-text-fill: #FF3B5C; -fx-cursor: hand; -fx-padding: 0 10 0 10;" +
                    "-fx-border-color: #FF3B5C; -fx-border-radius: 7; -fx-border-width: 1;";
                String estiloExcluirHover =
                    "-fx-background-color: rgba(255,59,92,0.35);" +
                    "-fx-background-radius: 7; -fx-text-fill: white; -fx-cursor: hand; -fx-padding: 0 10 0 10;" +
                    "-fx-border-color: #FF3B5C; -fx-border-radius: 7; -fx-border-width: 1;";

                btnExcluir.setFont(Font.font("Helvetica Neue", FontWeight.BOLD, 11));
                btnExcluir.setPrefHeight(28);
                btnExcluir.setStyle(estiloExcluirNormal);
                btnExcluir.setOnMouseEntered(e -> btnExcluir.setStyle(estiloExcluirHover));
                btnExcluir.setOnMouseExited (e -> btnExcluir.setStyle(estiloExcluirNormal));
                btnExcluir.setOnAction(e -> confirmarExclusao(
                    getTableView().getItems().get(getIndex())
                ));

                setGraphic(box);
            }
        });

        tvFuncionarios.getColumns().addAll(colNome, colCpf, colCargo, colTel, colEmail, colAcoes);

        painel.getChildren().addAll(titulo, tvFuncionarios);
        return painel;
    }

    // ──────────────────────────────────────────────────────────────────
    //  PAINEL PONTOS
    // ──────────────────────────────────────────────────────────────────
    @SuppressWarnings("unchecked")
    private VBox criarPainelPontos() {
        VBox painel = new VBox(14);
        VBox.setVgrow(painel, Priority.ALWAYS);

        Label titulo = new Label("Registro de Pontos");
        titulo.setFont(Font.font("Helvetica Neue", FontWeight.BOLD, 16));
        titulo.setTextFill(Color.WHITE);

        tvPontos = new TableView<>();
        tvPontos.setStyle(estiloTabela());
        tvPontos.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        VBox.setVgrow(tvPontos, Priority.ALWAYS);
        tvPontos.setPlaceholder(criarPlaceholder("Nenhum ponto registrado ainda."));

        TableColumn<PontoRow, String> colFunc = new TableColumn<>("Funcionário");
        colFunc.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(d.getValue().getNomeFuncionario()));
        colFunc.setPrefWidth(250);

        TableColumn<PontoRow, String> colData = new TableColumn<>("Data");
        colData.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(d.getValue().getDataPonto()));
        colData.setPrefWidth(140);

        TableColumn<PontoRow, String> colHora = new TableColumn<>("Horário");
        colHora.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(d.getValue().getHorario()));
        colHora.setPrefWidth(120);

        tvPontos.getColumns().addAll(colFunc, colData, colHora);

        painel.getChildren().addAll(titulo, tvPontos);
        return painel;
    }

    // ──────────────────────────────────────────────────────────────────
    //  CARREGAR DADOS DO BANCO
    // ──────────────────────────────────────────────────────────────────
    private void carregarDados() {
        try {
            Conexao.conectar();
            HomeEmpresaDAO homeDAO = new HomeEmpresaDAO(Conexao.conexao);

            // Estatísticas
            int totalFunc  = homeDAO.contarFuncionarios(idEmpresa);
            int pontosHoje = homeDAO.contarPontosHoje(idEmpresa);
            int pontosMes  = homeDAO.contarPontosMes(idEmpresa);

            lblStatFunc.setText(String.valueOf(totalFunc));
            lblStatHoje.setText(String.valueOf(pontosHoje));
            lblStatMes.setText(String.valueOf(pontosMes));

            // Funcionários
            List<FuncionarioRow> funcionarios = homeDAO.listarFuncionarios(idEmpresa);
            ObservableList<FuncionarioRow> olFunc = FXCollections.observableArrayList(funcionarios);
            tvFuncionarios.setItems(olFunc);

            // Pontos
            List<PontoRow> pontos = homeDAO.listarPontos(idEmpresa);
            ObservableList<PontoRow> olPontos = FXCollections.observableArrayList(pontos);
            tvPontos.setItems(olPontos);

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            Conexao.desconectar();
        }
    }

    // ──────────────────────────────────────────────────────────────────
    //  TROCA DE ABA
    // ──────────────────────────────────────────────────────────────────
    private void mudarAba(boolean mostrarFuncionarios) {
        btnAbaFuncionarios.setStyle(estiloBotaoNav(mostrarFuncionarios));
        btnAbaPontos.setStyle(estiloBotaoNav(!mostrarFuncionarios));

        Node entrando = mostrarFuncionarios ? painelFuncionarios : painelPontos;
        Node saindo   = mostrarFuncionarios ? painelPontos       : painelFuncionarios;

        saindo.setVisible(false);
        entrando.setOpacity(0);
        entrando.setVisible(true);
        fade(entrando, 0, 1, 300).play();
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

        HBox iconRow = new HBox(10);
        iconRow.setAlignment(Pos.CENTER_LEFT);

        Region accentBar = new Region();
        accentBar.setPrefWidth(3);
        accentBar.setPrefHeight(32);
        accentBar.setStyle(
            "-fx-background-color: linear-gradient(to bottom, " + cor1 + ", " + cor2 + ");" +
            "-fx-background-radius: 2;"
        );

        iconRow.getChildren().add(accentBar);

        Label lblTitulo = new Label(titulo);
        lblTitulo.setFont(Font.font("Helvetica Neue", 12));
        lblTitulo.setTextFill(Color.web(TEXT_SEC));

        lblValor.setFont(Font.font("Helvetica Neue", FontWeight.BOLD, 32));
        lblValor.setTextFill(Color.WHITE);

        card.getChildren().addAll(iconRow, lblTitulo, lblValor);

        // Hover
        card.setOnMouseEntered(e -> {
            card.setStyle(
                "-fx-background-color: #130825;" +
                "-fx-background-radius: 16;" +
                "-fx-border-color: " + cor1 + ";" +
                "-fx-border-radius: 16;" +
                "-fx-border-width: 1;"
            );
            escala(card, 1.0, 1.02, 150).play();
        });
        card.setOnMouseExited(e -> {
            card.setStyle(
                "-fx-background-color: " + CARD_BG + ";" +
                "-fx-background-radius: 16;" +
                "-fx-border-color: " + BORDER + ";" +
                "-fx-border-radius: 16;" +
                "-fx-border-width: 1;"
            );
            escala(card, 1.02, 1.0, 150).play();
        });

        return card;
    }

    private Button criarBotaoNav(String texto, boolean ativo) {
        Button btn = new Button(texto);
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setPrefHeight(44);
        btn.setFont(Font.font("Helvetica Neue", FontWeight.BOLD, 13));
        btn.setStyle(estiloBotaoNav(ativo));
        btn.setOnMouseEntered(e -> {
            if (!btn.getStyle().contains(PURPLE))
                btn.setStyle(estiloBotaoNav(true));
        });
        btn.setOnMouseExited(e -> {
            if (!btn.getStyle().contains(PURPLE))
                btn.setStyle(estiloBotaoNav(false));
        });
        return btn;
    }

    private String estiloBotaoNav(boolean ativo) {
        if (ativo) {
            return "-fx-background-color: linear-gradient(to right, " + PURPLE + "22, " + ORANGE + "22);" +
                   "-fx-background-radius: 10;" +
                   "-fx-text-fill: white;" +
                   "-fx-cursor: hand;" +
                   "-fx-border-color: " + PURPLE + ";" +
                   "-fx-border-radius: 10;" +
                   "-fx-border-width: 0 0 0 3;";
        }
        return "-fx-background-color: transparent;" +
               "-fx-background-radius: 10;" +
               "-fx-text-fill: " + TEXT_SEC + ";" +
               "-fx-cursor: hand;";
    }

    private <T> TableColumn<T, String> criarColuna(String header, String prop, double largura) {
        TableColumn<T, String> col = new TableColumn<>(header);
        col.setPrefWidth(largura);
        col.setStyle("-fx-text-fill: white;");
        return col;
    }

    private <T> TableColumn<T, String> criarColunaPonto(String header, String prop, double largura) {
        return criarColuna(header, prop, largura);
    }

    private Label criarPlaceholder(String msg) {
        Label lbl = new Label(msg);
        lbl.setFont(Font.font("Helvetica Neue", 14));
        lbl.setTextFill(Color.web(TEXT_SEC));
        return lbl;
    }

    private String estiloTabela() {
        return "";
    }

    // ──────────────────────────────────────────────────────────────────
    //  MODAL DE EDIÇÃO DE FUNCIONÁRIO
    // ──────────────────────────────────────────────────────────────────
    private void abrirModalEdicao(FuncionarioRow row) {
        // ── Overlay in-page (sem nova janela) ──────────────────────────
        StackPane overlay = new StackPane();
        overlay.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        overlay.setStyle("-fx-background-color: rgba(0,0,0,0.65);");

        // Wrapper que limita a altura ao conteúdo (mesmo padrão do login)
        StackPane cardOuter = new StackPane();
        cardOuter.setMaxWidth(480);
        cardOuter.setMaxHeight(javafx.scene.layout.Region.USE_PREF_SIZE);
        cardOuter.setStyle(
            "-fx-background-color: linear-gradient(to bottom right, " + PURPLE + ", #FF6B8A, " + ORANGE + ");" +
            "-fx-background-radius: 22;" +
            "-fx-padding: 1.5;"
        );
        DropShadow glow = new DropShadow(40, Color.web(PURPLE, 0.5));
        glow.setSpread(0.05);
        cardOuter.setEffect(glow);

        // Card interno
        VBox card = new VBox(18);
        card.setPadding(new Insets(32, 36, 32, 36));
        card.setStyle(
            "-fx-background-color: #0F081E;" +
            "-fx-background-radius: 21;"
        );

        // Título
        Label lblTitulo = new Label("Editar Funcionário");
        lblTitulo.setFont(Font.font("Helvetica Neue", FontWeight.BOLD, 20));
        lblTitulo.setTextFill(Color.WHITE);

        Region gradLine = new Region();
        gradLine.setPrefHeight(2);
        gradLine.setStyle("-fx-background-color: linear-gradient(to right, " + PURPLE + ", " + ORANGE + ");");
        VBox.setMargin(gradLine, new Insets(0, 0, 4, 0));

        // Campos do formulário
        GridPane form = new GridPane();
        form.setHgap(14);
        form.setVgap(14);

        TextField tfNome     = criarCampoEdicao(row.getNome());
        TextField tfCargo    = criarCampoEdicao(row.getCargo());
        TextField tfTelefone = criarCampoEdicao(row.getTelefone());
        TextField tfEmail    = criarCampoEdicao(row.getEmail());

        form.add(criarLabel("Nome"),     0, 0); form.add(tfNome,     1, 0);
        form.add(criarLabel("Cargo"),    0, 1); form.add(tfCargo,    1, 1);
        form.add(criarLabel("Telefone"), 0, 2); form.add(tfTelefone, 1, 2);
        form.add(criarLabel("E-mail"),   0, 3); form.add(tfEmail,    1, 3);

        javafx.scene.layout.ColumnConstraints cc0 = new javafx.scene.layout.ColumnConstraints();
        cc0.setPrefWidth(90);
        javafx.scene.layout.ColumnConstraints cc1 = new javafx.scene.layout.ColumnConstraints();
        cc1.setHgrow(Priority.ALWAYS);
        form.getColumnConstraints().addAll(cc0, cc1);

        // Botões
        HBox botoes = new HBox(12);
        botoes.setAlignment(Pos.CENTER_RIGHT);
        VBox.setMargin(botoes, new Insets(8, 0, 0, 0));

        Runnable fechar = () -> {
            FadeTransition ftOut = fade(overlay, 1, 0, 180);
            ftOut.setOnFinished(ev -> rootPane.getChildren().remove(overlay));
            ftOut.play();
        };

        Button btnCancelar = new Button("Cancelar");
        btnCancelar.setPrefHeight(40);
        btnCancelar.setFont(Font.font("Helvetica Neue", FontWeight.BOLD, 13));
        btnCancelar.setStyle(
            "-fx-background-color: transparent; -fx-text-fill: " + TEXT_SEC + ";" +
            "-fx-border-color: " + BORDER + "; -fx-border-radius: 10; -fx-background-radius: 10; -fx-cursor: hand; -fx-padding: 0 18 0 18;"
        );
        btnCancelar.setOnAction(e -> fechar.run());

        // Clique no fundo escuro também fecha
        overlay.setOnMouseClicked(e -> {
            if (e.getTarget() == overlay) fechar.run();
        });

        Button btnSalvar = new Button("Salvar alterações");
        btnSalvar.setPrefHeight(40);
        btnSalvar.setFont(Font.font("Helvetica Neue", FontWeight.BOLD, 13));
        btnSalvar.setStyle(
            "-fx-background-color: linear-gradient(to right," + PURPLE + "," + ORANGE + ");" +
            "-fx-background-radius: 10; -fx-text-fill: white; -fx-cursor: hand; -fx-padding: 0 18 0 18;"
        );
        btnSalvar.setOnMouseEntered(e -> escala(btnSalvar, 1.0, 1.05, 120).play());
        btnSalvar.setOnMouseExited (e -> escala(btnSalvar, 1.05, 1.0, 120).play());
        btnSalvar.setOnAction(e -> {
            String nome     = tfNome.getText().trim();
            String cargo    = tfCargo.getText().trim();
            String telefone = tfTelefone.getText().trim();
            String email    = tfEmail.getText().trim();

            if (nome.isEmpty() || cargo.isEmpty() || email.isEmpty()) {
                mostrarAlertaErro("Preencha todos os campos obrigatórios.");
                return;
            }
            try {
                Conexao.conectar();
                FuncionarioDAO dao = new FuncionarioDAO(Conexao.conexao);
                dao.atualizar(row.getId(), nome, cargo, telefone, email);
                Conexao.desconectar();
                fechar.run();
                carregarDados();
            } catch (Exception ex) {
                ex.printStackTrace();
                mostrarAlertaErro("Erro ao salvar: " + ex.getMessage());
            }
        });

        botoes.getChildren().addAll(btnCancelar, btnSalvar);
        card.getChildren().addAll(lblTitulo, gradLine, form, botoes);
        cardOuter.getChildren().add(card);

        overlay.getChildren().add(cardOuter);
        StackPane.setAlignment(cardOuter, Pos.CENTER);
        StackPane.setMargin(cardOuter, new Insets(20));

        // Adiciona o overlay por cima de tudo na cena
        rootPane.getChildren().add(overlay);

        // Animação de entrada
        overlay.setOpacity(0);
        cardOuter.setScaleX(0.92);
        cardOuter.setScaleY(0.92);
        ScaleTransition stEdit = new ScaleTransition(Duration.millis(250), cardOuter);
        stEdit.setFromX(0.92); stEdit.setFromY(0.92);
        stEdit.setToX(1.0);   stEdit.setToY(1.0);
        stEdit.setInterpolator(Interpolator.EASE_OUT);
        new ParallelTransition(fade(overlay, 0, 1, 250), stEdit).play();
    }

    // ──────────────────────────────────────────────────────────────────
    //  CONFIRMAÇÃO DE EXCLUSÃO
    // ──────────────────────────────────────────────────────────────────
    private void confirmarExclusao(FuncionarioRow row) {
        // ── Overlay in-page (sem nova janela) ──────────────────────────
        StackPane overlay = new StackPane();
        overlay.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        overlay.setStyle("-fx-background-color: rgba(0,0,0,0.65);");

        // Wrapper com borda vermelha (mesmo padrão do login, mas vermelho)
        StackPane cardOuter = new StackPane();
        cardOuter.setMaxWidth(420);
        cardOuter.setMaxHeight(javafx.scene.layout.Region.USE_PREF_SIZE);
        cardOuter.setStyle(
            "-fx-background-color: linear-gradient(to bottom right, #FF3B5C, #FF6B8A, #CC1F3A);" +
            "-fx-background-radius: 22;" +
            "-fx-padding: 1.5;"
        );
        DropShadow glowRed = new DropShadow(40, Color.web("#FF3B5C", 0.55));
        glowRed.setSpread(0.05);
        cardOuter.setEffect(glowRed);

        // Card interno
        VBox card = new VBox(16);
        card.setPadding(new Insets(36, 40, 36, 40));
        card.setAlignment(Pos.CENTER);
        card.setStyle(
            "-fx-background-color: #0F081E;" +
            "-fx-background-radius: 21;"
        );

        Label icone = new Label("⚠");
        icone.setFont(Font.font("Helvetica Neue", FontWeight.BOLD, 40));
        icone.setTextFill(Color.web("#FF3B5C"));

        Label msg = new Label("Excluir funcionário?");
        msg.setFont(Font.font("Helvetica Neue", FontWeight.BOLD, 18));
        msg.setTextFill(Color.WHITE);

        Label detalhe = new Label(row.getNome() + "\n" + row.getCpf());
        detalhe.setFont(Font.font("Helvetica Neue", 13));
        detalhe.setTextFill(Color.web(TEXT_SEC));
        detalhe.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
        detalhe.setWrapText(true);

        Label aviso = new Label("Esta ação é irreversível.");
        aviso.setFont(Font.font("Helvetica Neue", 12));
        aviso.setTextFill(Color.web("#FF3B5C", 0.8));
        VBox.setMargin(aviso, new Insets(0, 0, 8, 0));

        HBox botoes = new HBox(12);
        botoes.setAlignment(Pos.CENTER);

        Runnable fechar = () -> {
            FadeTransition ftOut = fade(overlay, 1, 0, 180);
            ftOut.setOnFinished(ev -> rootPane.getChildren().remove(overlay));
            ftOut.play();
        };

        Button btnCancelar = new Button("Cancelar");
        btnCancelar.setPrefHeight(40);
        btnCancelar.setFont(Font.font("Helvetica Neue", FontWeight.BOLD, 13));
        btnCancelar.setStyle(
            "-fx-background-color: transparent; -fx-text-fill: " + TEXT_SEC + ";" +
            "-fx-border-color: " + BORDER + "; -fx-border-radius: 10; -fx-background-radius: 10; -fx-cursor: hand; -fx-padding: 0 18 0 18;"
        );
        btnCancelar.setOnAction(e -> fechar.run());

        // Clique no fundo escuro também fecha
        overlay.setOnMouseClicked(e -> {
            if (e.getTarget() == overlay) fechar.run();
        });

        Button btnExcluir = new Button("Sim, excluir");
        btnExcluir.setPrefHeight(40);
        btnExcluir.setFont(Font.font("Helvetica Neue", FontWeight.BOLD, 13));
        btnExcluir.setStyle(
            "-fx-background-color: #FF3B5C;" +
            "-fx-background-radius: 10; -fx-text-fill: white; -fx-cursor: hand; -fx-padding: 0 18 0 18;"
        );
        btnExcluir.setOnMouseEntered(e -> escala(btnExcluir, 1.0, 1.05, 120).play());
        btnExcluir.setOnMouseExited (e -> escala(btnExcluir, 1.05, 1.0, 120).play());
        btnExcluir.setOnAction(e -> {
            try {
                Conexao.conectar();
                FuncionarioDAO dao = new FuncionarioDAO(Conexao.conexao);
                dao.excluir(row.getId());
                Conexao.desconectar();
                fechar.run();
                carregarDados();
            } catch (Exception ex) {
                ex.printStackTrace();
                mostrarAlertaErro("Erro ao excluir: " + ex.getMessage());
            }
        });

        botoes.getChildren().addAll(btnCancelar, btnExcluir);
        card.getChildren().addAll(icone, msg, detalhe, aviso, botoes);
        cardOuter.getChildren().add(card);

        overlay.getChildren().add(cardOuter);
        StackPane.setAlignment(cardOuter, Pos.CENTER);
        StackPane.setMargin(cardOuter, new Insets(20));

        // Adiciona o overlay por cima de tudo na cena
        rootPane.getChildren().add(overlay);

        // Animação de entrada
        overlay.setOpacity(0);
        cardOuter.setScaleX(0.92);
        cardOuter.setScaleY(0.92);
        ScaleTransition stDel = new ScaleTransition(Duration.millis(250), cardOuter);
        stDel.setFromX(0.92); stDel.setFromY(0.92);
        stDel.setToX(1.0);   stDel.setToY(1.0);
        stDel.setInterpolator(Interpolator.EASE_OUT);
        new ParallelTransition(fade(overlay, 0, 1, 250), stDel).play();
    }

    // ── Helpers do formulário de edição ──────────────────────────────
    private TextField criarCampoEdicao(String valor) {
        TextField tf = new TextField(valor);
        tf.setFont(Font.font("Helvetica Neue", 13));
        tf.setStyle(
            "-fx-background-color: #140A28;" +
            "-fx-border-color: " + BORDER + ";" +
            "-fx-border-radius: 8; -fx-background-radius: 8;" +
            "-fx-text-fill: white; -fx-prompt-text-fill: " + TEXT_SEC + ";" +
            "-fx-padding: 8 12 8 12;"
        );
        tf.focusedProperty().addListener((obs, o, focused) -> tf.setStyle(
            "-fx-background-color: #140A28;" +
            "-fx-border-color: " + (focused ? PURPLE : BORDER) + ";" +
            "-fx-border-radius: 8; -fx-background-radius: 8;" +
            "-fx-text-fill: white; -fx-prompt-text-fill: " + TEXT_SEC + ";" +
            "-fx-padding: 8 12 8 12;"
        ));
        tf.setMaxWidth(Double.MAX_VALUE);
        return tf;
    }

    private Label criarLabel(String texto) {
        Label lbl = new Label(texto);
        lbl.setFont(Font.font("Helvetica Neue", FontWeight.BOLD, 12));
        lbl.setTextFill(Color.web(TEXT_SEC));
        lbl.setAlignment(Pos.CENTER_RIGHT);
        lbl.setMaxWidth(Double.MAX_VALUE);
        return lbl;
    }

    private void mostrarAlertaErro(String mensagem) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Erro");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    // ──────────────────────────────────────────────────────────────────
    //  ORBS E ANIMAÇÕES (igual ao padrão do projeto)
    // ──────────────────────────────────────────────────────────────────
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
