package com.example.simulearn.SimuLearn.Biology.MicropipettingSolution;

import com.example.simulearn.SimuLearn.QuizQuestion;
import com.example.simulearn.SimuLearn.SVGLoader;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class MicropipettingSolutionController implements Initializable {

    @FXML
    private void onBackButtonClicked(ActionEvent event) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/simulearn/biologyMenu.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setMaximized(true);
            stage.show();
        } catch (java.io.IOException e) {
            System.out.println("Failed to open the window.");
        }
    }

    @FXML
    private VBox mainpanel;
    @FXML
    private VBox instruction;

    private ScrollPane scrollPane;
    private VBox buttonPanel;

    private Button btnContext, btnMaterials, btnPredictions, btnProtocol, btnResults, btnReflection, btnSummary;

    private Button selectedButton = null;
    private MSLabworkspace msLabworkspace;

    private boolean allStepsCompleted = false;

    private VBox instructionCommandPanel;
    private int currentProtocolStep = 1;
    private final int TOTAL_PROTOCOL_STEPS = 4;
    private VBox currentInstructionsList;
    private String currentHighlightedInstruction = null;

    private int activeStep = 1;
    private String activeSubStep = "a";

    private java.util.HashMap<String, Boolean> stepCompletionMap = new java.util.HashMap<>();

    private final String[][] STEP_SEQUENCES = {
            {"a", "b", "c", "d", "e"},
            {"a", "b", "c", "d"},
            {"a", "b", "c", "d"},
            {"a", "b", "c"}
    };

    private double[] predictionSizes = {0.2, 0.3, 0.15, 0.1};

    private double[] predictionRadiusA = new double[1];
    private double[] predictionRadiusB = new double[1];
    private double[] predictionRadiusC = new double[1];
    private double[] predictionRadiusD = new double[1];

    public void initialize(URL url, ResourceBundle resourceBundle) {
        setUpLayout();
    }

    private void setUpLayout() {
        scrollPane = new ScrollPane();
        scrollPane.setPrefWidth(800);
        scrollPane.setMaxWidth(Region.USE_PREF_SIZE);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: white;" + "-fx-border-color: #cccccc;" + "-fx-border-radius: 8;" + "-fx-background-radius: 8;" + "-fx-effect: dropshadow(gaussian, rgba(6,24,44,0.65), 6, 0.2, 0, 4);");
        VBox.setVgrow(scrollPane, Priority.ALWAYS);
        updateScrollContent(getContextContent());

        instruction.setPadding(new Insets(20));
        instruction.setStyle("-fx-background-color: rgba(4,12,18,0.95);" + "-fx-border-color: rgba(34,211,238,0.18);" + "-fx-border-radius: 14;" + "-fx-background-radius: 14;" + "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.55), 24, 0.2, 0, 4);");
        instruction.setPrefWidth(400);
        instruction.setMinWidth(400);

        buttonPanel = createButtonPanel();
        instruction.getChildren().add(buttonPanel);
        mainpanel.setAlignment(Pos.TOP_RIGHT);
        mainpanel.getChildren().add(scrollPane);
    }

    private void updateScrollContent(VBox content) {
        scrollPane.setContent(content);
    }

    private VBox getContextContent() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(40));

        Label title = new Label("1. CONTEXT");
        title.setFont(Font.font("DM Sans Medium", 32));
        title.setStyle("-fx-text-fill: #1a5409;" +
                "-fx-font-family: Georgia;");
        Separator underline = new Separator();
        underline.setMaxWidth(200);
        underline.setStyle("-fx-background-color: #00aaff;");
        underline.setPrefHeight(3);

        Label text = new Label("Micropipettes are widely used in molecular biology labs to pipette small volumes of solutions in the microliter (μL) range. In this simulation, you will practice dispensing different volumes of liquid using a P20 micropipette.");
        text.setWrapText(true);
        text.setFont(Font.font("DM Sans Medium", 16));
        text.setLineSpacing(3);
        text.setStyle("-fx-text-fill: #333333;"
                + "-fx-font-family: Trebuchet MS;");

        Label howToTitle = new Label("HOW TO USE A MICROPIPETTE");
        howToTitle.setFont(Font.font("DM Sans Medium", FontWeight.BOLD, 18));
        howToTitle.setStyle("-fx-text-fill: #333333;" +
                "-fx-font-family:Century Gothic;");
        howToTitle.setPadding(new Insets(20, 0, 10, 0));

        Separator underline2 = new Separator();
        underline2.setMaxWidth(660);
        underline2.setPrefHeight(2);
        underline2.setStyle("-fx-background-color: #333333;");

        VBox imageBox1 = createImageBox("Adding a tip",
                SVGLoader.loadSVG("/Images/MSImages/Context1-en.svg", 150, 600));
        VBox imageBox2 = createImageBox("Setting volume",
                SVGLoader.loadSVG("/Images/MSImages/Context2-en.svg", 150, 600));
        VBox imageBox3 = createImageBox("Drawing up liquid",
                SVGLoader.loadSVG("/Images/MSImages/Context3-en.svg", 150, 600));
        VBox imageBox4 = createImageBox("Expelling liquid",
                SVGLoader.loadSVG("/Images/MSImages/Context4-en.svg", 150, 600));

        HBox imagesRow = new HBox(0);
        imagesRow.setStyle(
                "-fx-background-color: white;" +
                        "-fx-border-color: #d0d0d0;" +
                        "-fx-border-width: 1;" +
                        "-fx-border-radius: 8;" +
                        "-fx-background-radius: 8;" +
                        "-fx-padding: 15;" +
                        "-fx-cursor: hand;" +
                        "-fx-effect: dropshadow(three-pass-box, rgba(60, 64, 67, 0.3), 15, 0, 0, 6);"
        );
        imagesRow.setAlignment(Pos.CENTER);
        imagesRow.getChildren().addAll(imageBox1, imageBox2, imageBox3, imageBox4);

        content.getChildren().addAll(title, underline, text, howToTitle, underline2, imagesRow);
        return content;
    }

    private VBox createImageBox(String heading, ImageView image) {
        VBox box = new VBox(0);
        box.setAlignment(Pos.TOP_CENTER);
        box.setPrefWidth(185);
        box.setMaxWidth(185);

        Label headingLabel = new Label(heading);
        headingLabel.setFont(Font.font("DM Sans Medium", FontWeight.BOLD, 13));
        headingLabel.setTextFill(Color.WHITE);
        headingLabel.setAlignment(Pos.CENTER);
        headingLabel.setMaxWidth(Double.MAX_VALUE);
        headingLabel.setPadding(new Insets(8, 5, 8, 5));
        headingLabel.setWrapText(true);
        headingLabel.setStyle("-fx-background-color: #2B5F7F;");

        StackPane imagePane = new StackPane(image);
        imagePane.setStyle("-fx-background-color: #2B5F7F;");
        imagePane.setPadding(new Insets(10));
        imagePane.setPrefHeight(280);
        imagePane.setAlignment(Pos.CENTER);

        box.getChildren().addAll(headingLabel, imagePane);
        return box;
    }

    private VBox createButtonPanel() {
        VBox panel = new VBox(0);
        panel.setAlignment(Pos.TOP_LEFT);
        panel.setPadding(new Insets(16, 12, 16, 12));

        btnContext = createNavButton("1", "CONTEXT");
        btnMaterials = createNavButton("2", "MATERIALS");
        btnPredictions = createNavButton("3", "PREDICTIONS");
        btnProtocol = createNavButton("4", "PROTOCOL");
        btnResults = createNavButton("5", "RESULTS");
        btnReflection = createNavButton("6", "REFLECTION");
        btnSummary = createNavButton("7", "SUMMARY");

        if (!allStepsCompleted) {
            btnResults.setDisable(true);
            btnResults.setOpacity(0.5);
        }

        btnContext.setOnAction(e -> {

            mainpanel.getChildren().clear();
            mainpanel.getChildren().add(scrollPane);
            scrollPane.setVisible(true);
            scrollPane.setManaged(true);
            VBox.setVgrow(scrollPane, Priority.ALWAYS);

            updateScrollContent(getContextContent());
            setSelectedButton(btnContext);
            resetToButtonView();
        });

        btnMaterials.setOnAction(e -> {

            mainpanel.getChildren().clear();
            mainpanel.getChildren().add(scrollPane);
            scrollPane.setVisible(true);
            scrollPane.setManaged(true);
            VBox.setVgrow(scrollPane, Priority.ALWAYS);

            updateScrollContent(getMaterialsContent());
            setSelectedButton(btnMaterials);
            resetToButtonView();
        });

        btnPredictions.setOnAction(e -> {

            mainpanel.getChildren().clear();
            mainpanel.getChildren().add(scrollPane);
            scrollPane.setVisible(true);
            scrollPane.setManaged(true);
            VBox.setVgrow(scrollPane, Priority.ALWAYS);

            updateScrollContent(getPredictionsContent());
            setSelectedButton(btnPredictions);
            resetToButtonView();
        });

        btnProtocol.setOnAction(e -> {
            setSelectedButton(btnProtocol);

            scrollPane.setVisible(false);
            scrollPane.setManaged(false);

            if (msLabworkspace == null) {
                msLabworkspace = new MSLabworkspace(this);
            }

            mainpanel.getChildren().clear();
            mainpanel.getChildren().add(msLabworkspace.getMSLabWorkspace());
            VBox.setVgrow(msLabworkspace.getMSLabWorkspace(), Priority.ALWAYS);

            resetToLabView();
        });

        btnResults.setOnAction(e -> {

            mainpanel.getChildren().clear();
            mainpanel.getChildren().add(scrollPane);
            scrollPane.setVisible(true);
            scrollPane.setManaged(true);
            VBox.setVgrow(scrollPane, Priority.ALWAYS);

            updateScrollContent(getResultsContent());
            setSelectedButton(btnResults);
            resetToButtonView();
        });

        btnReflection.setOnAction(e -> {

            mainpanel.getChildren().clear();
            mainpanel.getChildren().add(scrollPane);
            scrollPane.setVisible(true);
            scrollPane.setManaged(true);
            VBox.setVgrow(scrollPane, Priority.ALWAYS);

            updateScrollContent(getReflectionContent());
            setSelectedButton(btnReflection);
            resetToButtonView();
        });

        btnSummary.setOnAction(e -> {

            mainpanel.getChildren().clear();
            mainpanel.getChildren().add(scrollPane);
            scrollPane.setVisible(true);
            scrollPane.setManaged(true);
            VBox.setVgrow(scrollPane, Priority.ALWAYS);

            updateScrollContent(getSummaryContent());
            setSelectedButton(btnSummary);
            resetToButtonView();
        });

        panel.getChildren().addAll(
                btnContext,
                createSpacer(20),
                btnMaterials,
                createSpacer(20),
                btnPredictions,
                createSpacer(20),
                btnProtocol,
                createSpacer(20),
                btnResults,
                createSpacer(20),
                btnReflection,
                createSpacer(20),
                btnSummary);

        setSelectedButton(btnContext);

        return panel;
    }

    private Button createNavButton(String number, String text) {

        String icon = switch (text) {
            case "CONTEXT" -> "📋";
            case "MATERIALS" -> "🧪";
            case "PROTOCOL" -> "⚗️";
            case "RESULTS" -> "📊";
            case "REFLECTION" -> "💡";
            case "SUMMARY" -> "📝";
            default -> "•";
        };

        Button btn = new Button(icon + "  " + text);
        btn.setFont(Font.font("DM Sans Medium", FontWeight.BOLD, 13));
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setAlignment(Pos.CENTER_LEFT);
        btn.setPadding(new Insets(13, 20, 13, 20));
        btn.setStyle(
                "-fx-background-color: rgba(4, 18, 26, 0.70);" +
                        "-fx-background-radius: 10;" +
                        "-fx-border-color: rgba(34, 211, 238, 0.18);" +
                        "-fx-border-width: 1;" +
                        "-fx-border-radius: 10;" +
                        "-fx-text-fill: rgba(207, 250, 254, 0.70);" +
                        "-fx-cursor: hand;"
        );

        btn.setOnMouseEntered(e -> {
            if (btn != selectedButton) {
                btn.setStyle(
                        "-fx-background-color: rgba(34, 211, 238, 0.08);" +
                                "-fx-background-radius: 10;" +
                                "-fx-border-color: rgba(34, 211, 238, 0.38);" +
                                "-fx-border-width: 1;" +
                                "-fx-border-radius: 10;" +
                                "-fx-text-fill: #67e8f9;" +
                                "-fx-cursor: hand;"
                );
            }
        });
        btn.setOnMouseExited(e -> {
            if (btn != selectedButton) {
                btn.setStyle(
                        "-fx-background-color: rgba(4, 18, 26, 0.70);" +
                                "-fx-background-radius: 10;" +
                                "-fx-border-color: rgba(34, 211, 238, 0.18);" +
                                "-fx-border-width: 1;" +
                                "-fx-border-radius: 10;" +
                                "-fx-text-fill: rgba(207, 250, 254, 0.70);" +
                                "-fx-cursor: hand;"
                );
            }
        });

        btn.setUserData(btn);
        return btn;
    }

    private void setSelectedButton(Button btn) {

        if (selectedButton != null) {
            selectedButton.setStyle(
                    "-fx-background-color: rgba(4, 18, 26, 0.70);" +
                            "-fx-background-radius: 10;" +
                            "-fx-border-color: rgba(34, 211, 238, 0.18);" +
                            "-fx-border-width: 1;" +
                            "-fx-border-radius: 10;" +
                            "-fx-text-fill: rgba(207, 250, 254, 0.70);" +
                            "-fx-cursor: hand;"
            );
        }

        selectedButton = btn;
        if (btn != null) {
            btn.setStyle(
                    "-fx-background-color: rgba(34, 211, 238, 0.14);" +
                            "-fx-background-radius: 10;" +
                            "-fx-border-color: #22d3ee;" +
                            "-fx-border-width: 1.5;" +
                            "-fx-border-radius: 10;" +
                            "-fx-text-fill: #ecfeff;" +
                            "-fx-font-weight: bold;" +
                            "-fx-cursor: hand;" +
                            "-fx-effect: dropshadow(gaussian, rgba(34,211,238,0.22), 10, 0.2, 0, 2);"
            );
        }
    }

    private void resetToButtonView() {

        instruction.getChildren().clear();
        buttonPanel = createButtonPanel();
        instruction.getChildren().add(buttonPanel);
    }

    private VBox createSpacer(int height) {
        VBox spacer = new VBox();
        spacer.setPrefHeight(height);
        return spacer;
    }

    private VBox createInstructionCommandPanel() {
        VBox panel = new VBox(15);
        panel.setPadding(new Insets(15));
        panel.setAlignment(Pos.TOP_LEFT);

        HBox header = createProtocolHeader();

        HBox progressSection = createProgressSection();

        Label stepTitleLabel = new Label();
        stepTitleLabel.setId("stepTitle");
        stepTitleLabel.setFont(Font.font("DM Sans Medium", FontWeight.BOLD, 15));
        stepTitleLabel.setStyle("-fx-text-fill: #1a5490;");
        stepTitleLabel.setWrapText(true);
        stepTitleLabel.setMaxWidth(350);

        ScrollPane instructionsScroll = new ScrollPane();
        instructionsScroll.setFitToWidth(true);
        instructionsScroll.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");
        instructionsScroll.setPrefHeight(300);
        VBox.setVgrow(instructionsScroll, Priority.ALWAYS);

        currentInstructionsList = new VBox(8);
        currentInstructionsList.setId("instructionsList");
        instructionsScroll.setContent(currentInstructionsList);

        HBox navigationButtons = createNavigationButtons();

        panel.getChildren().addAll(
                header,
                progressSection,
                stepTitleLabel,
                instructionsScroll,
                navigationButtons
        );

        return panel;
    }

    private HBox createNavigationButtons() {
        HBox navButtons = new HBox(10);
        navButtons.setAlignment(Pos.CENTER);
        navButtons.setPadding(new Insets(10, 0, 5, 0));

        Button previousBtn = new Button("◀ Previous");
        previousBtn.setId("previousBtn");
        previousBtn.setFont(Font.font("DM Sans Medium", FontWeight.BOLD, 12));
        previousBtn.setStyle(
                "-fx-background-color: white;" +
                        "-fx-text-fill: #1a5490;" +
                        "-fx-border-color: #1a5490;" +
                        "-fx-border-width: 2;" +
                        "-fx-border-radius: 5;" +
                        "-fx-background-radius: 5;" +
                        "-fx-padding: 8 20;" +
                        "-fx-cursor: hand;"
        );
        previousBtn.setOnAction(e -> handlePreviousStep());

        Circle stepCircle = new Circle(18);
        stepCircle.setFill(Color.web("#1a5490"));
        stepCircle.setStroke(Color.WHITE);
        stepCircle.setStrokeWidth(2);

        Label stepLabel = new Label("1.a");
        stepLabel.setId("stepCircleLabel");
        stepLabel.setFont(Font.font("DM Sans Medium", FontWeight.BOLD, 11));
        stepLabel.setTextFill(Color.WHITE);

        StackPane circleBtn = new StackPane(stepCircle, stepLabel);

        Button nextBtn = new Button("Next ▶");
        nextBtn.setId("nextBtn");
        nextBtn.setFont(Font.font("DM Sans Medium", FontWeight.BOLD, 12));
        nextBtn.setStyle(
                "-fx-background-color: #1a5490;" +
                        "-fx-text-fill: white;" +
                        "-fx-border-radius: 5;" +
                        "-fx-background-radius: 5;" +
                        "-fx-padding: 8 20;" +
                        "-fx-cursor: hand;"
        );
        nextBtn.setOnAction(e -> handleNextStep());

        navButtons.getChildren().addAll(previousBtn, circleBtn, nextBtn);

        return navButtons;
    }

    private HBox createProtocolHeader() {
        HBox header = new HBox(12);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(5, 0, 5, 0));

        Circle circle = new Circle(18);
        circle.setFill(Color.web("#1a5490"));
        circle.setStroke(Color.WHITE);
        circle.setStrokeWidth(2);

        Label numberLabel = new Label("4");
        numberLabel.setFont(Font.font("DM Sans Medium", FontWeight.BOLD, 14));
        numberLabel.setTextFill(Color.WHITE);

        StackPane circleStack = new StackPane(circle, numberLabel);

        Label titleLabel = new Label("PROTOCOL");
        titleLabel.setFont(Font.font("DM Sans Medium", FontWeight.BOLD, 20));
        titleLabel.setStyle("-fx-text-fill: #1a5490;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button notesBtn = new Button("📋 Notes");
        notesBtn.setFont(Font.font("DM Sans Medium", FontWeight.BOLD, 11));
        notesBtn.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-text-fill: #1a5490;" +
                        "-fx-cursor: hand;"
        );

        header.getChildren().addAll(circleStack, titleLabel, spacer, notesBtn);
        Button protocolButton = new Button();
        protocolButton.setGraphic(header);
        protocolButton.getStyleClass().add("nav-btn-active");

        protocolButton.setOnAction(e -> {
            instruction.getChildren().clear();
            buttonPanel = createButtonPanel();
            instruction.getChildren().add(buttonPanel);
            scrollPane.setVisible(true);
            scrollPane.setVisible(true);
            updateScrollContent(getMaterialsContent());

        });

        HBox header1 = new HBox(12);
        header1.setAlignment(Pos.CENTER_LEFT);
        header1.setPadding(new Insets(5, 0, 5, 0));
        header1.getChildren().add(protocolButton);

        return header1;
    }

    private HBox createProgressSection() {
        HBox section = new HBox(10);
        section.setAlignment(Pos.CENTER_LEFT);
        section.setPadding(new Insets(5, 0, 10, 0));

        ProgressBar progressBar = new ProgressBar(0);
        progressBar.setId("protocolProgressBar");
        progressBar.setPrefWidth(280);
        progressBar.setPrefHeight(8);
        progressBar.setStyle("-fx-accent: #1a5490;");

        Label stepIndicator = new Label("1/4");
        stepIndicator.setId("stepIndicator");
        stepIndicator.setFont(Font.font("DM Sans Medium", FontWeight.BOLD, 12));
        stepIndicator.setStyle("-fx-text-fill: #333;");

        section.getChildren().addAll(progressBar, stepIndicator);

        return section;
    }

    private void resetToLabView() {
        instruction.getChildren().clear();

        instructionCommandPanel = createInstructionCommandPanel();
        instruction.getChildren().add(instructionCommandPanel);

        loadProtocolStep(1);
    }

    private VBox getPredictionsContent() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(40));
        content.setStyle("-fx-background-color: #f5f5f5;");

        Label title = new Label("3. PREDICTIONS");
        title.setFont(Font.font("DM Sans Medium", FontWeight.BOLD, 32));
        title.setStyle("-fx-text-fill: #1a5490;");

        Separator underline = new Separator();
        underline.setMaxWidth(300);
        underline.setStyle("-fx-background-color: #00aaff;");
        underline.setPrefHeight(3);

        Label descText = new Label("During this simulation, you will be dispensing different volumes of liquid red dye with a micropipette onto a blotting paper. How do you think the different volumes will influence the size of red circles you expect to see on the blotting paper? Make your predictions here by adjusting the controls to make each circle larger or smaller.");
        descText.setWrapText(true);
        descText.setFont(Font.font("DM Sans Medium", 16));
        descText.setLineSpacing(3);
        descText.setStyle("-fx-text-fill: #333333;");
        descText.setMaxWidth(700);

        Label estimateTitle = new Label("Estimate the size of each blot if you set the micropipette to:");
        estimateTitle.setFont(Font.font("DM Sans Medium", FontWeight.BOLD, 16));
        estimateTitle.setStyle("-fx-text-fill: #333333;");
        estimateTitle.setPadding(new Insets(20, 0, 10, 0));

        HBox predictionBox = createPredictionCircles();

        content.getChildren().addAll(title, underline, descText, estimateTitle, predictionBox);
        return content;
    }

    private HBox createPredictionCircles() {
        HBox container = new HBox(15);
        container.setAlignment(Pos.CENTER);
        container.setPadding(new Insets(30));
        container.setStyle(
                "-fx-background-color: white;" +
                        "-fx-border-color: #d0d0d0;" +
                        "-fx-border-width: 1;" +
                        "-fx-border-radius: 8;" +
                        "-fx-background-radius: 8;" +
                        "-fx-effect: dropshadow(three-pass-box, rgba(60, 64, 67, 0.3), 15, 0, 0, 6);"
        );

        VBox sampleBox = createFixedCircle("10µl", "SAMPLE", 0.15);
        container.getChildren().add(sampleBox);

        String[] labels = {"20µl", "15µl", "7.5µl", "2µl"};
        String[] names = {"A", "B", "C", "D"};

        for (int i = 0; i < 4; i++) {
            VBox circleBox = createAdjustableCircle(labels[i], names[i], i);
            container.getChildren().add(circleBox);
        }

        return container;
    }

    private VBox createFixedCircle(String volumeLabel, String name, double fillRatio) {
        VBox box = new VBox(10);
        box.setAlignment(Pos.CENTER);
        box.setPrefWidth(120);

        Label volLabel = new Label(volumeLabel);
        volLabel.setFont(Font.font("DM Sans Medium", FontWeight.BOLD, 14));
        volLabel.setStyle("-fx-text-fill: #333333;");

        double outerRadius = 50;
        StackPane circleStack = new StackPane();
        circleStack.setPrefSize(outerRadius * 2, outerRadius * 2);

        Circle outerCircle = new Circle(outerRadius);
        outerCircle.setFill(Color.TRANSPARENT);
        outerCircle.setStroke(Color.BLACK);
        outerCircle.setStrokeWidth(2);

        Circle innerCircle = new Circle(outerRadius * fillRatio);
        innerCircle.setFill(Color.RED);

        circleStack.getChildren().addAll(outerCircle, innerCircle);

        Label nameLabel = new Label(name);
        nameLabel.setFont(Font.font("DM Sans Medium", FontWeight.BOLD, 14));
        nameLabel.setStyle("-fx-text-fill: #333333;");

        box.getChildren().addAll(volLabel, circleStack, nameLabel);
        return box;
    }

    private VBox createAdjustableCircle(String volumeLabel, String name, int index) {
        VBox box = new VBox(5);
        box.setAlignment(Pos.CENTER);
        box.setPrefWidth(120);

        Label volLabel = new Label(volumeLabel);
        volLabel.setFont(Font.font("DM Sans Medium", FontWeight.BOLD, 14));
        volLabel.setStyle("-fx-text-fill: #333333;");

        double outerRadius = 50;
        StackPane circleStack = new StackPane();
        circleStack.setPrefSize(outerRadius * 2, outerRadius * 2);

        Circle outerCircle = new Circle(outerRadius);
        outerCircle.setFill(Color.TRANSPARENT);
        outerCircle.setStroke(Color.BLACK);
        outerCircle.setStrokeWidth(2);

        Circle innerCircle = new Circle(outerRadius * predictionSizes[index]);
        innerCircle.setFill(Color.RED);

        storePredictionRadius(index, innerCircle.getRadius());

        circleStack.getChildren().addAll(outerCircle, innerCircle);

        HBox controlsRow = new HBox(10);
        controlsRow.setAlignment(Pos.CENTER);

        Button minusBtn = new Button("-");
        minusBtn.setFont(Font.font("DM Sans Medium", FontWeight.BOLD, 18));
        minusBtn.setPrefSize(35, 35);
        minusBtn.setStyle(
                "-fx-background-color: #FFA500;" +
                        "-fx-text-fill: white;" +
                        "-fx-background-radius: 50%;" +
                        "-fx-cursor: hand;"
        );
        minusBtn.setOnAction(e -> {
            if (predictionSizes[index] > 0.05) {
                predictionSizes[index] -= 0.05;
                double newRadius = outerRadius * predictionSizes[index];
                innerCircle.setRadius(newRadius);

                storePredictionRadius(index, newRadius);
            }
        });

        Circle dot = new Circle(3, Color.RED);

        Button plusBtn = new Button("+");
        plusBtn.setFont(Font.font("DM Sans Medium", FontWeight.BOLD, 18));
        plusBtn.setPrefSize(35, 35);
        plusBtn.setStyle(
                "-fx-background-color: #FFA500;" +
                        "-fx-text-fill: white;" +
                        "-fx-background-radius: 50%;" +
                        "-fx-cursor: hand;"
        );
        plusBtn.setOnAction(e -> {
            if (predictionSizes[index] < 1.0) {
                predictionSizes[index] += 0.05;
                double newRadius = outerRadius * predictionSizes[index];
                innerCircle.setRadius(newRadius);

                storePredictionRadius(index, newRadius);
            }
        });

        controlsRow.getChildren().addAll(minusBtn, dot, plusBtn);

        Label nameLabel = new Label(name);
        nameLabel.setFont(Font.font("DM Sans Medium", FontWeight.BOLD, 14));
        nameLabel.setStyle("-fx-text-fill: #333333;");

        box.getChildren().addAll(volLabel, controlsRow, circleStack, nameLabel);
        return box;
    }

    private VBox getMaterialsContent() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(40));
        content.setStyle("-fx-background-color: #f5f5f5;");

        Label title = new Label("2. MATERIALS");
        title.setFont(Font.font("DM Sans Medium", FontWeight.BOLD, 32));
        title.setStyle("-fx-text-fill: #1a5490;");

        Separator underline = new Separator();
        underline.setMaxWidth(250);
        underline.setStyle("-fx-background-color: #00aaff;");
        underline.setPrefHeight(3);

        Label infoText = new Label("Click on the images to learn more about them.");
        infoText.setFont(Font.font("DM Sans Medium", 14));
        infoText.setStyle(
                "-fx-background-color: #e8f4f8;" +
                        "-fx-padding: 15;" +
                        "-fx-border-color: #d0d0d0;" +
                        "-fx-border-width: 1;" +
                        "-fx-border-radius: 5;" +
                        "-fx-background-radius: 5;"
        );
        infoText.setMaxWidth(600);

        Label reagentsTitle = new Label("REAGENTS");
        reagentsTitle.setFont(Font.font("DM Sans Medium", FontWeight.BOLD, 18));
        reagentsTitle.setStyle("-fx-text-fill: #1a5490;");

        Label reagentsSubtitle = new Label("Chemicals that can be used in this experiment");
        reagentsSubtitle.setFont(Font.font("DM Sans Medium", 14));
        reagentsSubtitle.setStyle("-fx-text-fill: #666666;");

        GridPane reagentsGrid = createReagentsGrid();

        Label micropipettingTitle = new Label("MICROPIPETTING EQUIPMENT");
        micropipettingTitle.setFont(Font.font("DM Sans Medium", FontWeight.BOLD, 18));
        micropipettingTitle.setStyle("-fx-text-fill: #1a5490;");

        Label micropipettingSubtitle = new Label("Used to measure small volumes");
        micropipettingSubtitle.setFont(Font.font("DM Sans Medium", 14));
        micropipettingSubtitle.setStyle("-fx-text-fill: #666666;");

        GridPane micropipettingGrid = createMicropipettingGrid();

        Label equipmentTitle = new Label("EQUIPMENT");
        equipmentTitle.setFont(Font.font("DM Sans Medium", FontWeight.BOLD, 18));
        equipmentTitle.setStyle("-fx-text-fill: #1a5490;");

        Label equipmentSubtitle = new Label("Micropipettes, tip boxes, and equipment");
        equipmentSubtitle.setFont(Font.font("DM Sans Medium", 14));
        equipmentSubtitle.setStyle("-fx-text-fill: #666666;");

        GridPane equipmentGrid = createEquipmentGrid();

        content.getChildren().addAll(
                title, underline, infoText,
                reagentsTitle, reagentsSubtitle, reagentsGrid,
                micropipettingTitle, micropipettingSubtitle, micropipettingGrid,
                equipmentTitle, equipmentSubtitle, equipmentGrid
        );

        return content;
    }

    private GridPane createEquipmentGrid() {
        GridPane grid = new GridPane();
        grid.setHgap(15);
        grid.setVgap(15);
        grid.setPadding(new Insets(20, 0, 20, 0));
        String[][] equipments = {
                {"/Images/MSImages/Blotting_Paper.svg", "Blotting Paper", "#ff6b6b"}
                , {"/Images/MSImages/equipment/Trash.svg", "Trash", "#ff6b6b"}
        };
        int col = 0;
        for (String[] equipment : equipments) {
            StackPane item = createMaterialItem(equipment[0], equipment[1], equipment[2], 70, 85);
            grid.add(item, col, 0);
            col++;
        }
        return grid;
    }

    private GridPane createMicropipettingGrid() {
        GridPane grid = new GridPane();
        grid.setHgap(15);
        grid.setVgap(15);
        grid.setPadding(new Insets(20, 0, 20, 0));

        String[][] pipettes = {
                {"/Images/MSImages/pipettes/P2.svg", "P2 Pipette", "#ff6b6b"},
                {"/Images/MSImages/pipettes/P20.svg", "P20 Pipette", "#ffd93d"},
                {"/Images/MSImages/pipettes/P200.svg", "P200 Pipette", "#6bcf7f"},
                {"/Images/MSImages/pipettes/P1000.svg", "P1000 Pipette", "#4d96ff"}
        };

        String[][] tipBoxes = {
                {"/Images/MSImages/tipboxes/P2_Tip_Box.svg", "P2 Tip Box", "#ff6b6b"},
                {"/Images/MSImages/tipboxes/P20_Tip_Box.svg", "P20 Tip Box", "#ffd93d"},
                {"/Images/MSImages/tipboxes/P200_Tip_Box.svg", "P200 Tip Box", "#6bcf7f"},
                {"/Images/MSImages/tipboxes/P1000_Tip_Box.svg", "P1000 Tip Box", "#4d96ff"}
        };

        int col = 0;
        for (String[] pipette : pipettes) {
            StackPane item = createMaterialItem(pipette[0], pipette[1], pipette[2], 70, 85);
            grid.add(item, col, 0);
            col++;
        }

        col = 0;
        for (String[] tipBox : tipBoxes) {
            StackPane item = createMaterialItem(tipBox[0], tipBox[1], tipBox[2], 80, 70);
            grid.add(item, col, 1);
            col++;
        }

        return grid;
    }

    private GridPane createReagentsGrid() {
        GridPane gridPane = new GridPane();
        gridPane.setHgap(15);
        gridPane.setVgap(15);
        gridPane.setPadding((new Insets(20, 0, 20, 0)));

        String[][] reagents = {{"/Images/MSImages/RedDye-close.svg", "Red Dye Solution", "#4a90e2"}};

        int col = 0;
        int row = 0;
        for (String[] reagent : reagents) {
            StackPane item = createMaterialItem(reagent[0], reagent[1], reagent[2], 80, 80);
            gridPane.add(item, col, row);
            col++;
            if (col == 4) {
                col = 0;
                row++;
            }
        }
        return gridPane;
    }

    private StackPane createMaterialItem(String imagePath, String labelText,
                                         String accentColor, double imgWidth, double imgHeight) {

        StackPane mainContainer = new StackPane();
        mainContainer.setMaxWidth(150);
        mainContainer.setAlignment(Pos.TOP_CENTER);

        VBox cardContainer = new VBox(10);
        cardContainer.setAlignment(Pos.CENTER);
        cardContainer.setPrefSize(150, 180);
        cardContainer.setStyle(
                "-fx-background-color: white;" +
                        "-fx-border-color: #d0d0d0;" +
                        "-fx-border-width: 1;" +
                        "-fx-border-radius: 8;" +
                        "-fx-background-radius: 8;" +
                        "-fx-padding: 15;" +
                        "-fx-cursor: hand;" +
                        "-fx-effect: dropshadow(three-pass-box, rgba(60, 64, 67, 0.3), 15, 0, 0, 6);"
        );

        StackPane imageContainer = new StackPane();
        imageContainer.setPrefSize(120, 100);
        imageContainer.setStyle(
                "-fx-background-color: #0d3d5c;" +
                        "-fx-background-radius: 5;"
        );

        ImageView imageView = SVGLoader.loadSVG(imagePath, imgWidth, imgHeight, true);

        if (imageView != null) {
            imageView.setFitWidth(imgWidth);
            imageView.setFitHeight(imgHeight);
            imageView.setPreserveRatio(true);
            imageView.setSmooth(true);
            imageContainer.getChildren().add(imageView);
        } else {

            Circle placeholder = new Circle(30);
            placeholder.setFill(Color.web(accentColor));
            imageContainer.getChildren().add(placeholder);
        }

        Label label = new Label(labelText);
        label.setFont(Font.font("DM Sans Medium", FontWeight.SEMI_BOLD, 12));
        label.setTextAlignment(TextAlignment.CENTER);
        label.setWrapText(true);
        label.setMaxWidth(130);
        label.setStyle("-fx-text-fill: #333333;");

        cardContainer.getChildren().addAll(imageContainer, label);

        VBox infoPanel = createOverlayInfoPanel(labelText);
        infoPanel.setTranslateY(-200);
        infoPanel.setOpacity(0);
        infoPanel.setVisible(false);
        StackPane.setAlignment(infoPanel, Pos.TOP_CENTER);

        mainContainer.getChildren().addAll(cardContainer, infoPanel);

        addHoverEffects(cardContainer);

        final boolean[] isExpanded = {false};
        cardContainer.setOnMouseClicked(e -> {
            toggleOverlayPanel(infoPanel, isExpanded);
            e.consume();
        });

        infoPanel.setOnMouseClicked(e -> {
            toggleOverlayPanel(infoPanel, isExpanded);
            e.consume();
        });

        return mainContainer;
    }

    private VBox createOverlayInfoPanel(String materialName) {
        VBox panel = new VBox(10);
        panel.setPrefSize(150, 180);
        panel.setPadding(new Insets(15));
        panel.setStyle(
                "-fx-background-color: rgba(26, 84, 144, 0.95);" +
                        "-fx-border-color: #00aaff;" +
                        "-fx-border-width: 2;" +
                        "-fx-border-radius: 8;" +
                        "-fx-background-radius: 8;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,170,255,0.6), 20, 0, 0, 8);"
        );

        Label closeHint = new Label("✕");
        closeHint.setFont(Font.font("DM Sans Medium", FontWeight.BOLD, 16));
        closeHint.setStyle("-fx-text-fill: white; -fx-cursor: hand;");
        closeHint.setAlignment(Pos.CENTER_RIGHT);
        closeHint.setMaxWidth(Double.MAX_VALUE);

        Label titleLabel = new Label(materialName);
        titleLabel.setFont(Font.font("DM Sans Medium", FontWeight.BOLD, 12));
        titleLabel.setStyle("-fx-text-fill: white;");
        titleLabel.setWrapText(true);
        titleLabel.setMaxWidth(120);

        Separator separator = new Separator();
        separator.setStyle("-fx-background-color: rgba(255,255,255,0.3);");
        separator.setPrefHeight(1);

        String description = getMaterialDescription(materialName);
        Label descLabel = new Label(description);
        descLabel.setFont(Font.font("DM Sans Medium", 10));
        descLabel.setStyle("-fx-text-fill: white;");
        descLabel.setWrapText(true);
        descLabel.setMaxWidth(120);

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(descLabel);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(90);
        scrollPane.setMaxHeight(90);
        scrollPane.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-border-color: transparent;" +
                        "-fx-background: transparent;"
        );
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        panel.getChildren().addAll(closeHint, titleLabel, separator, scrollPane);
        return panel;
    }

    private void toggleOverlayPanel(VBox infoPanel, boolean[] isExpanded) {
        isExpanded[0] = !isExpanded[0];

        if (isExpanded[0]) {

            infoPanel.setVisible(true);

            Timeline showTimeline = new Timeline(
                    new KeyFrame(
                            Duration.ZERO,
                            new KeyValue(infoPanel.translateYProperty(), -200),
                            new KeyValue(infoPanel.opacityProperty(), 0)
                    ),
                    new KeyFrame(
                            Duration.millis(400),
                            new KeyValue(infoPanel.translateYProperty(), 0,
                                    Interpolator.EASE_OUT),
                            new KeyValue(infoPanel.opacityProperty(), 1)
                    )
            );
            showTimeline.play();
        } else {

            Timeline hideTimeline = new Timeline(
                    new KeyFrame(
                            Duration.ZERO,
                            new KeyValue(infoPanel.translateYProperty(), 0),
                            new KeyValue(infoPanel.opacityProperty(), 1)
                    ),
                    new KeyFrame(
                            Duration.millis(400),
                            new KeyValue(infoPanel.translateYProperty(), -200,
                                    Interpolator.EASE_IN),
                            new KeyValue(infoPanel.opacityProperty(), 0)
                    )
            );
            hideTimeline.setOnFinished(e -> infoPanel.setVisible(false));
            hideTimeline.play();
        }
    }

    private void addHoverEffects(VBox container) {
        container.setOnMouseEntered(e -> {
            container.setStyle(
                    "-fx-background-color: #e8f4f8;" +
                            "-fx-border-color: #00aaff;" +
                            "-fx-border-width: 2;" +
                            "-fx-border-radius: 8;" +
                            "-fx-background-radius: 8;" +
                            "-fx-padding: 15;" +
                            "-fx-cursor: hand;" +
                            "-fx-effect: dropshadow(gaussian, rgba(0,170,255,0.4), 15, 0, 0, 6);"
            );
        });

        container.setOnMouseExited(e -> {
            container.setStyle(
                    "-fx-background-color: white;" +
                            "-fx-border-color: #d0d0d0;" +
                            "-fx-border-width: 1;" +
                            "-fx-border-radius: 8;" +
                            "-fx-background-radius: 8;" +
                            "-fx-padding: 15;" +
                            "-fx-cursor: hand;" +
                            "-fx-effect: dropshadow(three-pass-box, rgba(60, 64, 67, 0.3), 15, 0, 0, 6);"
            );
        });
    }

    private String getMaterialDescription(String materialName) {
        String cleanName = materialName.trim().replace("\n", " ");

        if (cleanName.contains("Red Dye Solution")) {
            return "Red dye in tube.";
        } else if (cleanName.contains("P2 Pipette")) {
            return "Used to draw up and dispense volumes of liquid between 0.2 and 2 microliters.";
        } else if (cleanName.contains("P20 Pipette")) {
            return "Used to draw up and dispense volumes of liquid between 2 and 20 microliters.";
        } else if (cleanName.contains("P200 Pipette")) {
            return "Used to draw up and dispense volumes of liquid between 20 and 200 microliters.";
        } else if (cleanName.contains("P1000 Pipette")) {
            return "Used to draw up and dispense volumes of liquid between 200 and 1000 microliters.";
        } else if (cleanName.contains("P2 Tip Box")) {
            return "Filled with P2 tips. Tips are attached to the micropipette and discarded after single use to avoid contamination between transfers.";
        } else if (cleanName.contains("P20 Tip Box")) {
            return "Filled with P20 tips. Tips are attached to the micropipette and discarded after single use to avoid contamination between transfers.";
        } else if (cleanName.contains("P200 Tip Box")) {
            return "Filled with P200 tips. Tips are attached to the micropipette and discarded after single use to avoid contamination between transfers.";
        } else if (cleanName.contains("P1000 Tip Box")) {
            return "Filled with P1000 tips. Tips are attached to the micropipette and discarded after single use to avoid contamination between transfers.";
        } else if (cleanName.contains("Trash")) {
            return "Waste container. Used to dispose of any non-hazardous waste resulting from the experiment, including micropipette tips.";
        } else if (cleanName.contains("Blotting Paper")) {
            return "Used to practice dispensing different volumes from a micropipette. Divided into four sections labeled A, B, C and D.";
        }
        return cleanName;
    }

    private VBox getResultsContent() {
        VBox content = new VBox(30);
        content.setPadding(new Insets(40));
        content.setStyle("-fx-background-color: #f5f5f5;");

        Label title = new Label("5. RESULTS");
        title.setFont(Font.font("DM Sans Medium", FontWeight.BOLD, 32));
        title.setStyle("-fx-text-fill: #1a5490;");

        Separator underline = new Separator();
        underline.setMaxWidth(200);
        underline.setStyle("-fx-background-color: #00aaff;");
        underline.setPrefHeight(3);

        Label introText = new Label(
                "Look at the relative size difference between your micropipetted circles. You " +
                        "should be able to see that accuracy of the liquid transferred is very important " +
                        "and will impact the success of your experiments. Compare your results with the " +
                        "ideal results. If the size of your circles matches the circles in the ideal results, " +
                        "you have pipetted the correct volumes. If they do not match, you have pipetted " +
                        "either too much or too little liquid."
        );
        introText.setWrapText(true);
        introText.setFont(Font.font("DM Sans Medium", 16));
        introText.setLineSpacing(3);
        introText.setStyle("-fx-text-fill: #333333;");
        introText.setMaxWidth(700);

        VBox predictedSection = createPredictedResultsSection();

        VBox actualSection = createActualResultsSection();

        VBox idealSection = createIdealResultsSection();

        content.getChildren().addAll(
                title,
                underline,
                introText,
                predictedSection,
                actualSection,
                idealSection
        );

        return content;
    }

    private VBox createPredictedResultsSection() {
        VBox section = new VBox(15);
        section.setPadding(new Insets(20));
        section.setStyle("-fx-background-color: white; -fx-border-radius: 8; -fx-background-radius: 8;");

        Label sectionTitle = new Label("Predicted Results");
        sectionTitle.setFont(Font.font("DM Sans Medium", FontWeight.BOLD, 18));
        sectionTitle.setStyle("-fx-text-fill: #333333;");

        Label sectionDesc = new Label("These are your initial predictions.");
        sectionDesc.setFont(Font.font("DM Sans Medium", 14));
        sectionDesc.setStyle("-fx-text-fill: #666666;");

        HBox circlesContainer = new HBox(30);
        circlesContainer.setAlignment(Pos.CENTER);
        circlesContainer.setPadding(new Insets(20));

        String[] labels = {"A | 0µl", "B | 0µl", "C | 0µl", "D | 0µl"};
        for (int i = 0; i < 4; i++) {
            VBox circleBox = createResultCircle(predictionSizes[i], labels[i], false);
            circlesContainer.getChildren().add(circleBox);
        }

        section.getChildren().addAll(sectionTitle, sectionDesc, circlesContainer);
        return section;
    }

    private VBox createActualResultsSection() {
        VBox section = new VBox(15);
        section.setPadding(new Insets(20));
        section.setStyle("-fx-background-color: white; -fx-border-radius: 8; -fx-background-radius: 8;");

        Label sectionTitle = new Label("Actual Results");
        sectionTitle.setFont(Font.font("DM Sans Medium", FontWeight.BOLD, 18));
        sectionTitle.setStyle("-fx-text-fill: #333333;");

        Label sectionDesc = new Label("These are your results based on the volumes you pipetted.");
        sectionDesc.setFont(Font.font("DM Sans Medium", 14));
        sectionDesc.setStyle("-fx-text-fill: #666666;");

        HBox circlesContainer = new HBox(30);
        circlesContainer.setAlignment(Pos.CENTER);
        circlesContainer.setPadding(new Insets(20));

        String[] circleNames = {"A", "B", "C", "D"};
        String[] volumes = {"0µl", "0µl", "0µl", "0µl"};

        double[] actualRadii = new double[4];
        if (msLabworkspace != null) {
            for (int i = 0; i < 4; i++) {
                double radius = msLabworkspace.getBlottingPaperRadius(circleNames[i]);

                actualRadii[i] = Math.min(radius / 35.0, 1.0);
            }
        }

        for (int i = 0; i < 4; i++) {
            VBox circleBox = createResultCircle(actualRadii[i], circleNames[i] + " | " + volumes[i], true);
            circlesContainer.getChildren().add(circleBox);
        }

        section.getChildren().addAll(sectionTitle, sectionDesc, circlesContainer);
        return section;
    }

    private VBox createIdealResultsSection() {
        VBox section = new VBox(15);
        section.setPadding(new Insets(20));
        section.setStyle("-fx-background-color: white; -fx-border-radius: 8; -fx-background-radius: 8;");

        Label sectionTitle = new Label("Ideal Results");
        sectionTitle.setFont(Font.font("DM Sans Medium", FontWeight.BOLD, 18));
        sectionTitle.setStyle("-fx-text-fill: #333333;");

        Label sectionDesc = new Label("These are the sizes you would expect to see if you pipetted the correct volumes.");
        sectionDesc.setFont(Font.font("DM Sans Medium", 14));
        sectionDesc.setStyle("-fx-text-fill: #666666;");

        HBox circlesContainer = new HBox(30);
        circlesContainer.setAlignment(Pos.CENTER);
        circlesContainer.setPadding(new Insets(20));

        double[] idealSizes = {1.0, 0.75, 0.375, 0.1};
        String[] labels = {"A | 20µl", "B | 15µl", "C | 7.5µl", "D | 2µl"};

        for (int i = 0; i < 4; i++) {
            VBox circleBox = createResultCircle(idealSizes[i], labels[i], false);
            circlesContainer.getChildren().add(circleBox);
        }

        section.getChildren().addAll(sectionTitle, sectionDesc, circlesContainer);
        return section;
    }

    private VBox createResultCircle(double fillRatio, String label, boolean showIncorrect) {
        VBox box = new VBox(8);
        box.setAlignment(Pos.CENTER);
        box.setPrefWidth(130);

        double outerRadius = 50;
        StackPane circleStack = new StackPane();
        circleStack.setPrefSize(outerRadius * 2 + 20, outerRadius * 2 + 20);

        Circle outerCircle = new Circle(outerRadius);
        outerCircle.setFill(Color.TRANSPARENT);
        outerCircle.setStroke(Color.BLACK);
        outerCircle.setStrokeWidth(2);

        Circle innerCircle = new Circle(outerRadius * fillRatio);
        innerCircle.setFill(Color.RED);

        circleStack.getChildren().addAll(outerCircle, innerCircle);

        Label circleLabel = new Label(label);
        circleLabel.setFont(Font.font("DM Sans Medium", FontWeight.BOLD, 12));
        circleLabel.setStyle("-fx-text-fill: #333333;");

        box.getChildren().addAll(circleStack, circleLabel);

        if (showIncorrect && fillRatio > 0.01) {
            Label incorrectBadge = new Label("Incorrect!");
            incorrectBadge.setFont(Font.font("DM Sans Medium", FontWeight.BOLD, 11));
            incorrectBadge.setStyle(
                    "-fx-background-color: #ffebee;" +
                            "-fx-text-fill: #c62828;" +
                            "-fx-padding: 5px 12px;" +
                            "-fx-border-color: #c62828;" +
                            "-fx-border-width: 1.5;" +
                            "-fx-border-radius: 5;" +
                            "-fx-background-radius: 5;"
            );
            box.getChildren().add(incorrectBadge);
        }

        return box;
    }

    private VBox getReflectionContent() {
        VBox content = new VBox(25);
        content.setPadding(new Insets(40));
        content.setStyle("-fx-background-color: #f5f5f5;");

        Label title = new Label("6. REFLECTION");
        title.setFont(Font.font("DM Sans Medium", FontWeight.BOLD, 32));
        title.setStyle("-fx-text-fill: #1a5490;");

        Separator underline = new Separator();
        underline.setMaxWidth(300);
        underline.setStyle("-fx-background-color: #00aaff;");
        underline.setPrefHeight(3);

        Label text = new Label(
                "Let's look at what you learned from this simulation. Please click on your answer " +
                        "to each of the following questions to save them in your notebook."
        );
        text.setFont(Font.font("DM Sans Medium", 16));
        text.setWrapText(true);
        text.setStyle("-fx-text-fill: #333333;");
        text.setMaxWidth(700);
        text.setLineSpacing(3);

        QuizQuestion q1 = createQuizQuestion(
                1,
                "What do you think is the main purpose of this simulation?",
                new String[]{
                        "To learn about the different equipment you will encounter in a lab.",
                        "To familiarize yourself with proper lab etiquette.",
                        "To learn the correct way to handle a micropipette in a laboratory.",
                        "To practice making solutions of different concentrations."
                },
                2,
                "What specific skill did you practice during this simulation?",
                3
        );

        QuizQuestion q2 = createQuizQuestion(
                2,
                "What volume should be set on a P20 micropipette to measure 18.5 microliters?",
                new String[]{
                        "18.5",
                        "185",
                        "1.85",
                        "0.185"
                },
                1,
                "How many decimal places are displayed on a P20 micropipette?",
                3
        );

        QuizQuestion q3 = createQuizQuestion(
                3,
                "If the window on a P20 micropipette displays the numbers \"1 5 5\" from top to bottom, what volume will be measured?",
                new String[]{
                        "1.55 microliters",
                        "15.5 microliters",
                        "155 microliters"
                },
                1,
                "Can a P20 measure this volume of liquid?",
                3
        );

        QuizQuestion q4 = createQuizQuestion(
                4,
                "What is the main purpose of disposable tips on a micropipette?",
                new String[]{
                        "To prevent cross contamination between samples.",
                        "To reach into small tubes.",
                        "To make the micropipettes measure larger volumes."
                },
                0,
                "Disposable tips cannot change the volume range of a micropipette.",
                3
        );

        QuizQuestion q5 = createQuizQuestion(
                5,
                "What happens if you press the plunger of a micropipette past the first stop when drawing up liquid?",
                new String[]{
                        "You will draw up more liquid than you wanted.",
                        "You will draw up less liquid than you wanted.",
                        "It will have no effect on the volume you draw up."
                },
                0,
                "The second stop is used when expelling liquid, not when drawing it up.",
                3
        );

        Label feedbackTitle = new Label("FEEDBACK QUESTION");
        feedbackTitle.setFont(Font.font("DM Sans Medium", FontWeight.BOLD, 24));
        feedbackTitle.setStyle("-fx-text-fill: #1a5490;");
        feedbackTitle.setPadding(new Insets(30, 0, 10, 0));

        QuizQuestion q6 = createFeedbackQuestion(
                6,
                "How confident do you feel about using the technique you learned?",
                new String[]{
                        "Not confident at all.",
                        "Slightly confident.",
                        "Somewhat confident.",
                        "Fairly confident.",
                        "Very confident."
                }
        );

        content.getChildren().addAll(
                title,
                underline,
                text,
                q1.getQuestionBox(),
                q2.getQuestionBox(),
                q3.getQuestionBox(),
                q4.getQuestionBox(),
                q5.getQuestionBox(),
                feedbackTitle,
                q6.getQuestionBox()
        );

        return content;
    }

    private QuizQuestion createQuizQuestion(int num, String question, String[] options,
                                            int correctIndex, String feedback, int attempts) {
        return new QuizQuestion(num, question, options, correctIndex, feedback, attempts, false);
    }

    private QuizQuestion createFeedbackQuestion(int num, String question, String[] options) {
        return new QuizQuestion(num, question, options, -1, null, 0, true);
    }

    private VBox getSummaryContent() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(40));
        content.setStyle("-fx-background-color: #f5f5f5;");

        Label title = new Label("7. SUMMARY");
        title.setFont(Font.font("DM Sans Medium", FontWeight.BOLD, 32));
        title.setStyle("-fx-text-fill: #1a5490;");

        Separator underline = new Separator();
        underline.setMaxWidth(200);
        underline.setStyle("-fx-background-color: #00aaff;");
        underline.setPrefHeight(3);

        Label introText = new Label(
                "It is always good to summarize your methodology and observations after you have completed an experiment. Please view a recap of this simulation and takeaway messages regarding micropipetting below."
        );
        introText.setWrapText(true);
        introText.setFont(Font.font("DM Sans Medium", 16));
        introText.setStyle("-fx-text-fill: #333333;");
        introText.setMaxWidth(650);

        VBox learningBox = createLearningOutcomesBox();

        Label takeawayTitle = new Label("Takeaway messages");
        takeawayTitle.setFont(Font.font("DM Sans Medium", FontWeight.BOLD, 20));
        takeawayTitle.setStyle("-fx-text-fill: #1a5490;");
        takeawayTitle.setPadding(new Insets(20, 0, 10, 0));

        VBox takeawayItems = createTakeawayItems();

        content.getChildren().addAll(
                title,
                underline,
                introText,
                learningBox,
                takeawayTitle,
                takeawayItems
        );

        return content;
    }

    private VBox createLearningOutcomesBox() {
        VBox box = new VBox(15);
        box.setStyle(
                "-fx-background-color: white;" +
                        "-fx-border-color: #d0d0d0;" +
                        "-fx-border-width: 1;" +
                        "-fx-border-radius: 8;" +
                        "-fx-background-radius: 8;" +
                        "-fx-padding: 25;"
        );
        box.setMaxWidth(650);

        Label heading = new Label(
                "In this simulation, you learned the correct way to use a micropipette in a lab. This is a crucial technique that is used frequently in molecular biology. The simulation explored how to:"
        );
        heading.setWrapText(true);
        heading.setFont(Font.font("DM Sans Medium", FontWeight.BOLD, 16));
        heading.setStyle("-fx-text-fill: #1a5490;");

        VBox points = new VBox(12);

        points.getChildren().addAll(
                createChecklistItem("Set the volume on a micropipette."),
                createChecklistItem("Attach a tip to the micropipette."),
                createChecklistItem("Use the plunger to draw up liquid (press to first stop)."),
                createChecklistItem("Use the plunger to expel liquid (press to second stop)."),
                createChecklistItem("Discard the micropipette tip after use.")
        );

        box.getChildren().addAll(heading, points);
        return box;
    }

    private HBox createChecklistItem(String text) {
        HBox item = new HBox(10);
        item.setAlignment(Pos.TOP_LEFT);

        Circle checkCircle = new Circle(10);
        checkCircle.setFill(Color.web("#4a90e2"));
        checkCircle.setStroke(Color.web("#3a7bc8"));
        checkCircle.setStrokeWidth(1);

        Label checkmark = new Label("✓");
        checkmark.setFont(Font.font("DM Sans Medium", FontWeight.BOLD, 14));
        checkmark.setStyle("-fx-text-fill: white;");

        StackPane checkIcon = new StackPane(checkCircle, checkmark);
        checkIcon.setPrefSize(20, 20);

        Label itemText = new Label(text);
        itemText.setFont(Font.font("DM Sans Medium", 15));
        itemText.setStyle("-fx-text-fill: #333333;");
        itemText.setWrapText(true);
        itemText.setMaxWidth(550);

        item.getChildren().addAll(checkIcon, itemText);
        return item;
    }

    private VBox createTakeawayItems() {
        VBox container = new VBox(15);
        container.setMaxWidth(750);

        String[][] takeaways = {
                {
                        "/Images/MSImages/tip2.svg",
                        "Always remember to close tip boxes when you are done to prevent contamination."
                },
                {
                        "/Images/MSImages/tip3.svg",
                        "Never place a micropipette with liquid drawn up back on the rack."
                },
                {
                        "/Images/MSImages/tip4.svg",
                        "Always dispose of micropipette tips in the trash when you are done."
                },
        };

        for (String[] takeaway : takeaways) {
            HBox item = createTakeawayItem(takeaway[0], takeaway[1]);
            container.getChildren().add(item);
        }

        return container;
    }

    private HBox createTakeawayItem(String imagePath, String messageText) {
        HBox container = new HBox(15);
        container.setAlignment(Pos.CENTER_LEFT);
        container.setStyle(
                "-fx-background-color: white;" +
                        "-fx-border-color: #d0d0d0;" +
                        "-fx-border-width: 1;" +
                        "-fx-border-radius: 8;" +
                        "-fx-background-radius: 8;" +
                        "-fx-padding: 15;"
        );
        container.setPrefHeight(120);

        StackPane imageContainer = new StackPane();
        imageContainer.setPrefSize(120, 90);
        imageContainer.setStyle(
                "-fx-background-color: #0d3d5c;" +
                        "-fx-background-radius: 5;"
        );

        ImageView imageView = SVGLoader.loadSVG(imagePath, 80, 80);

        if (imageView != null) {
            imageView.setFitWidth(80);
            imageView.setFitHeight(80);
            imageView.setPreserveRatio(true);
            imageView.setSmooth(true);
            imageContainer.getChildren().add(imageView);
        }

        Label message = new Label(messageText);
        message.setWrapText(true);
        message.setFont(Font.font("DM Sans Medium", 15));
        message.setStyle("-fx-text-fill: #333333;");
        message.setMaxWidth(580);
        message.setLineSpacing(3);

        HBox.setHgrow(message, Priority.ALWAYS);

        container.getChildren().addAll(imageContainer, message);

        return container;
    }

    private void loadProtocolStep(int stepNumber) {
        currentProtocolStep = stepNumber;

        ProgressBar progressBar = (ProgressBar) instructionCommandPanel.lookup("#protocolProgressBar");
        if (progressBar != null) {
            progressBar.setProgress((double) stepNumber / TOTAL_PROTOCOL_STEPS);
        }

        Label stepIndicator = (Label) instructionCommandPanel.lookup("#stepIndicator");
        if (stepIndicator != null) {
            stepIndicator.setText(stepNumber + "/" + TOTAL_PROTOCOL_STEPS);
        }

        updateStepContent(stepNumber);

        Button previousBtn = (Button) instructionCommandPanel.lookup("#previousBtn");
        if (previousBtn != null) {
            previousBtn.setDisable(stepNumber == 1);
        }

        Button nextBtn = (Button) instructionCommandPanel.lookup("#nextBtn");
        if (nextBtn != null) {
            nextBtn.setDisable(stepNumber == TOTAL_PROTOCOL_STEPS);
        }
    }

    private void updateStepContent(int stepNumber) {

        String[] stepTitles = {
                "1. Set the volume on the P20 micropipette.",
                "2. Draw up the red dye into the micropipette.",
                "3. Pipette the red dye onto blotting paper.",
                "4. Pipette different volumes of red dye onto the blotting paper.",
        };

        String[][][] allInstructions = {

                {
                        {"a", "Select the P20 micropipette.", String.valueOf(false)},
                        {"b", "Select the volume setting to set the volume to 20 μl and then select Save volume.", String.valueOf(false)},
                        {"c", "Select the P20 tip box to open it.", String.valueOf(false)},
                        {"d", "Move the P20 micropipette onto the P20 tip box to attach a tip.", String.valueOf(false)},
                        {"e", "Select the P20 tip box to close it.", String.valueOf(false)},
                },

                {
                        {"a", "Select the red dye solution tube to open it.", String.valueOf(false)},
                        {"b", "Select the micropipette and place it in the red dye solution.", String.valueOf(false)},
                        {"c", "Draw up the red dye by first pushing down the plunger until it reaches the first stop and slowly releasing the plunger.", String.valueOf(false)},
                        {"d", "Close the red dye solution tube.", String.valueOf(false)},
                },

                {
                        {"a", "Move the micropipette to circle A on the blotting paper to dispense the red dye.", String.valueOf(false)},
                        {"b", "Dispense the red dye by holding down the plunger until it reaches the second stop and slowly releasing it.", String.valueOf(false)},
                        {"c", "Move the micropipette over the trash can and press on the eject mechanism to eject the tip.", String.valueOf(false)},
                        {"d", "Select the P20 micropipette and place it back onto the rack.", String.valueOf(false)},
                },

                {
                        {"a", "In circle B on the blotting paper, follow the same process as before to dispense 15 μl of red dye.", String.valueOf(false)},
                        {"b", "In circle C on the blotting paper, follow the same process as before to dispense 7.5 μl of red dye.", String.valueOf(false)},
                        {"c", "In circle D on the blotting paper, follow the same process as before to dispense 2 μl of red dye.", String.valueOf(false)}
                },
        };

        Label stepTitle = (Label) instructionCommandPanel.lookup("#stepTitle");
        if (stepTitle != null) {
            stepTitle.setText(stepTitles[stepNumber - 1]);
        }

        currentInstructionsList.getChildren().clear();

        String[][] instructions = allInstructions[stepNumber - 1];

        for (int i = 0; i < instructions.length; i++) {
            String letter = instructions[i][0];
            String text = instructions[i][1];
            boolean isHighlighted = i == 0;

            HBox instructionItem = createInstructionItem(letter, text, isHighlighted, stepNumber);
            currentInstructionsList.getChildren().add(instructionItem);
        }

        Label circleLabel = (Label) instructionCommandPanel.lookup("#stepCircleLabel");
        if (circleLabel != null) {
            circleLabel.setText(stepNumber + "." + instructions[0][0]);
        }
    }

    private HBox createInstructionItem(String letter, String text, boolean isHighlighted, int stepNumber) {
        HBox item = new HBox(10);
        item.setAlignment(Pos.TOP_LEFT);
        item.setPadding(new Insets(10));
        item.setId("instruction_" + stepNumber + "_" + letter);

        if (isHighlighted) {
            item.setStyle(
                    "-fx-background-color: #ADD8E6;" +
                            "-fx-border-radius: 5;" +
                            "-fx-background-radius: 5;"
            );
            currentHighlightedInstruction = "instruction_" + stepNumber + "_" + letter;
        } else {
            item.setStyle(
                    "-fx-background-color: transparent;"
            );
        }

        Circle circle = new Circle(14);
        circle.setFill(Color.web("#1a5490"));
        circle.setStroke(Color.WHITE);
        circle.setStrokeWidth(2);

        Label letterLabel = new Label(letter);
        letterLabel.setFont(Font.font("DM Sans Medium", FontWeight.BOLD, 11));
        letterLabel.setTextFill(Color.WHITE);

        StackPane letterCircle = new StackPane(circle, letterLabel);
        letterCircle.setMinSize(28, 28);
        letterCircle.setMaxSize(28, 28);

        Label instructionText = new Label(text);
        instructionText.setFont(Font.font("DM Sans Medium", 12));
        instructionText.setStyle("-fx-text-fill: #333;");
        instructionText.setWrapText(true);
        instructionText.setMaxWidth(280);
        HBox.setHgrow(instructionText, Priority.ALWAYS);

        Label editIcon = new Label("✏");
        editIcon.setFont(Font.font("DM Sans Medium", 12));
        editIcon.setStyle("-fx-text-fill: #1a5490; -fx-cursor: hand;");

        item.getChildren().addAll(letterCircle, instructionText, editIcon);

        item.setOnMouseClicked(e -> highlightInstruction(item, stepNumber + "." + letter));

        return item;
    }

    private void highlightInstruction(HBox item, String instructionId) {

        if (currentHighlightedInstruction != null) {
            HBox prevItem = (HBox) currentInstructionsList.lookup("#" + currentHighlightedInstruction);
            if (prevItem != null) {
                prevItem.setStyle("-fx-background-color: transparent;");
            }
        }

        item.setStyle(
                "-fx-background-color: #ADD8E6;" +
                        "-fx-border-radius: 5;" +
                        "-fx-background-radius: 5;"
        );
        currentHighlightedInstruction = item.getId();

        Label circleLabel = (Label) instructionCommandPanel.lookup("#stepCircleLabel");
        if (circleLabel != null) {
            circleLabel.setText(instructionId);
        }
    }

    private void handlePreviousStep() {
        if (currentProtocolStep == 1) {

            resetToButtonView();
        } else {
            loadProtocolStep(currentProtocolStep - 1);
        }
    }

    private void handleNextStep() {
        if (currentProtocolStep < TOTAL_PROTOCOL_STEPS) {
            loadProtocolStep(currentProtocolStep + 1);
        }
    }

    public void highlightNextInstruction(String stepNumber, String subStep) {
        if (currentInstructionsList == null) {
            return;
        }

        String instructionId = "instruction_" + stepNumber + "_" + subStep;

        if (currentHighlightedInstruction != null) {
            HBox prevItem = (HBox) currentInstructionsList.lookup("#" + currentHighlightedInstruction);
            if (prevItem != null) {
                prevItem.setStyle("-fx-background-color: transparent;");
            }
        }

        HBox currentItem = (HBox) currentInstructionsList.lookup("#" + instructionId);
        if (currentItem != null) {
            currentItem.setStyle(
                    "-fx-background-color: #ADD8E6;" +
                            "-fx-border-radius: 5;" +
                            "-fx-background-radius: 5;"
            );
            currentHighlightedInstruction = instructionId;

            Label circleLabel = (Label) instructionCommandPanel.lookup("#stepCircleLabel");
            if (circleLabel != null) {
                circleLabel.setText(stepNumber + "." + subStep);
            }
        }
    }

    private void storePredictionRadius(int index, double radius) {
        switch (index) {
            case 0:
                predictionRadiusA[0] = radius;
                break;
            case 1:
                predictionRadiusB[0] = radius;
                break;
            case 2:
                predictionRadiusC[0] = radius;
                break;
            case 3:
                predictionRadiusD[0] = radius;
                break;
        }
    }

    public double getPredictionRadius(String circleName) {
        switch (circleName) {
            case "A":
                return predictionRadiusA[0];
            case "B":
                return predictionRadiusB[0];
            case "C":
                return predictionRadiusC[0];
            case "D":
                return predictionRadiusD[0];
            default:
                return 0.0;
        }
    }

    public void completeCurrentStep() {

        String stepKey = activeStep + "_" + activeSubStep;
        stepCompletionMap.put(stepKey, true);

        System.out.println("✅ Completed: Step " + activeStep + "." + activeSubStep);

        if (activeStep == 4 && activeSubStep.equals("c")) {

            enableResultsButton();
            System.out.println("🎊 All protocol steps completed! Results section now available.");
        }

        String nextSubStep = getNextSubStep(activeStep, activeSubStep);

        if (nextSubStep != null) {

            activeSubStep = nextSubStep;
            highlightNextInstruction(String.valueOf(activeStep), activeSubStep);
            System.out.println("➡️ Moving to: Step " + activeStep + "." + activeSubStep);
        } else {

            if (activeStep < TOTAL_PROTOCOL_STEPS) {
                activeStep++;
                activeSubStep = "a";

                if (currentProtocolStep != activeStep) {
                    loadProtocolStep(activeStep);
                }
                System.out.println("➡️ Moving to: Step " + activeStep + "." + activeSubStep);
            } else {
                System.out.println("🎉 All steps completed!");
            }
        }
    }

    private void enableResultsButton() {
        allStepsCompleted = true;

        if (btnResults != null) {
            btnResults.setDisable(false);
            btnResults.setOpacity(1.0);

            javafx.application.Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Congratulations!");
                alert.setHeaderText("Protocol Complete! 🎉");
                alert.setContentText(
                        "You have successfully completed all protocol steps!\n\n" +
                                "The Results section is now available. Click on the 'RESULTS' button " +
                                "to compare your predictions with your actual results and see how you did!"
                );
                alert.showAndWait();

                mainpanel.getChildren().clear();
                mainpanel.getChildren().add(scrollPane);
                scrollPane.setVisible(true);
                scrollPane.setManaged(true);
                javafx.scene.layout.VBox.setVgrow(scrollPane, javafx.scene.layout.Priority.ALWAYS);
                updateScrollContent(getResultsContent());
                resetToButtonView();
                setSelectedButton(btnResults);
            });

            Timeline timeline = new Timeline(
                    new KeyFrame(
                            Duration.millis(0),
                            new KeyValue(btnResults.scaleXProperty(), 1.0),
                            new KeyValue(btnResults.scaleYProperty(), 1.0)
                    ),
                    new KeyFrame(
                            Duration.millis(300),
                            new KeyValue(btnResults.scaleXProperty(), 1.1),
                            new KeyValue(btnResults.scaleYProperty(), 1.1)
                    ),
                    new KeyFrame(
                            Duration.millis(600),
                            new KeyValue(btnResults.scaleXProperty(), 1.0),
                            new KeyValue(btnResults.scaleYProperty(), 1.0)
                    )
            );
            timeline.setCycleCount(2);
            timeline.play();
        }
    }

    private String getNextSubStep(int step, String currentSub) {

        String[][] stepProgression = {
                {"a", "b", "c", "d", "e"},
                {"a", "b", "c", "d"},
                {"a", "b", "c", "d"},
                {"a", "b", "c"}
        };

        if (step < 1 || step > stepProgression.length) return null;

        String[] progression = stepProgression[step - 1];
        for (int i = 0; i < progression.length - 1; i++) {
            if (progression[i].equals(currentSub)) {
                return progression[i + 1];
            }
        }

        return null;
    }

    private int getStepIndex(int step, String subStep) {
        int baseIndex = 0;

        if (step == 2) baseIndex = 5;
        else if (step == 3) baseIndex = 9;
        else if (step == 4) baseIndex = 13;

        int subIndex = subStep.charAt(0) - 'a';
        return baseIndex + subIndex;
    }

    public boolean isStepCompleted(int step, String subStep) {
        String stepKey = step + "_" + subStep;
        return stepCompletionMap.getOrDefault(stepKey, false);
    }

    public boolean canPerformAction(int step, String subStep) {

        return (activeStep == step && activeSubStep.equals(subStep)) ||
                isStepCompleted(step, subStep);
    }

    public int getActiveStep() {
        return activeStep;
    }

    public String getActiveSubStep() {
        return activeSubStep;
    }
}