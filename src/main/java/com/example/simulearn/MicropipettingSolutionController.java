package com.example.simulearn;

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
            // Load FXML correctly
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
    // Button references
    private Button btnContext, btnMaterials, btnPredictions, btnProtocol, btnResults, btnReflection, btnSummary;

    // Selection tracking
    private Button selectedButton = null;
    private MSLabworkspace msLabworkspace;

    // Lab workspace components
    private VBox instructionCommandPanel;
    private int currentProtocolStep = 1;
    private final int TOTAL_PROTOCOL_STEPS = 4;
    private VBox currentInstructionsList;
    private String currentHighlightedInstruction = null;

    // Step completion tracking
    private int activeStep = 1;
    private String activeSubStep = "a";

    // Map to track completion of each step (key format: "1_a", "1_b", etc.)
    private java.util.HashMap<String, Boolean> stepCompletionMap = new java.util.HashMap<>();

    // Define the sequence of sub-steps for each main step
    private final String[][] STEP_SEQUENCES = {
            {"a", "b", "c", "d", "e"},  // Step 1: a, b, c, d, e
            {"a", "b", "c", "d"},        // Step 2: a, b, c, d
            {"a", "b", "c", "d"},        // Step 3: a, b, c, d
            {"a", "b", "c"}              // Step 4: a, b, c
    };

    // Prediction circles state - stored as percentage of max radius (0.0 to 1.0)
    private double[] predictionSizes = {0.2, 0.3, 0.15, 0.1}; // Initial sizes for A, B, C, D

    // Store actual prediction radius values in pixels for result comparison
    private double[] predictionRadiusA = new double[1]; // Stores radius for circle A
    private double[] predictionRadiusB = new double[1]; // Stores radius for circle B
    private double[] predictionRadiusC = new double[1]; // Stores radius for circle C
    private double[] predictionRadiusD = new double[1]; // Stores radius for circle D

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
        instruction.setStyle("-fx-background-color: white;" + "-fx-border-color: #cccccc;" + "-fx-border-radius: 8;" + "-fx-background-radius: 8;" + "-fx-effect: dropshadow(gaussian, rgba(6,24,44,0.65), 6, 0.2, 0, 4);");
        instruction.setPrefWidth(400);
        instruction.setMinWidth(400);

        //create initial button panel
        buttonPanel = createButtonPanel();
        instruction.getChildren().add(buttonPanel);
        mainpanel.setAlignment(Pos.TOP_RIGHT);
        mainpanel.getChildren().add(scrollPane);
    }

    private void updateScrollContent(VBox content) {
        scrollPane.setContent(content);
    }

    // Content creation methods
    private VBox getContextContent() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(40));

        Label title = new Label("1. CONTEXT");
        title.setFont(Font.font("Arial", 32));
        title.setStyle("-fx-text-fill: #1a5409;");
        Separator underline = new Separator();
        underline.setMaxWidth(200);
        underline.setStyle("-fx-background-color: #00aaff;");
        underline.setPrefHeight(3);

        Label text = new Label("Micropipettes are widely used in molecular biology labs to pipette small volumes of solutions in the microliter (μL) range. In this simulation, you will practice dispensing different volumes of liquid using a P20 micropipette.");
        text.setWrapText(true);
        text.setFont(Font.font("Arial", 16));
        text.setLineSpacing(3);
        text.setStyle("-fx-text-fill: #333333;");


        Label howToTitle = new Label("HOW TO USE A MICROPIPETTE");
        howToTitle.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        howToTitle.setStyle("-fx-text-fill: #333333;");
        howToTitle.setPadding(new Insets(20, 0, 10, 0));

        Separator underline2 = new Separator();
        underline2.setMaxWidth(660);
        underline2.setPrefHeight(2);
        underline2.setStyle("-fx-background-color: #333333;");
//        Images/MSImages/Context1-en.svg

        VBox imageBox1 = createImageBox("Adding a tip",
                SVGLoader.loadSVG("/Images/MSImages/Context1-en.svg", 150, 600));
        VBox imageBox2 = createImageBox("Setting volume",
                SVGLoader.loadSVG("/Images/MSImages/Context2-en.svg", 150, 600));
        VBox imageBox3 = createImageBox("Drawing up liquid",
                SVGLoader.loadSVG("/Images/MSImages/Context3-en.svg", 150, 600));
        VBox imageBox4 = createImageBox("Expelling liquid",
                SVGLoader.loadSVG("/Images/MSImages/Context4-en.svg", 150, 600));

        // HBox for all 4 images
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

        // Heading label
        Label headingLabel = new Label(heading);
        headingLabel.setFont(Font.font("Arial", FontWeight.BOLD, 13));
        headingLabel.setTextFill(Color.WHITE);
        headingLabel.setAlignment(Pos.CENTER);
        headingLabel.setMaxWidth(Double.MAX_VALUE);
        headingLabel.setPadding(new Insets(8, 5, 8, 5));
        headingLabel.setWrapText(true);
        headingLabel.setStyle("-fx-background-color: #2B5F7F;");

        // Image container
        StackPane imagePane = new StackPane(image);
        imagePane.setStyle("-fx-background-color: #2B5F7F;");
        imagePane.setPadding(new Insets(10));
        imagePane.setPrefHeight(280);
        imagePane.setAlignment(Pos.CENTER);

        box.getChildren().addAll(headingLabel, imagePane);
        return box;
    }

    private VBox createButtonPanel() {
        VBox panel = new VBox(0); // No spacing, we'll add it manually
        panel.setAlignment(Pos.TOP_LEFT);
        panel.setPadding(new Insets(10));

        // Create all navigation buttons with circles
        btnContext = createNavButton("1", "CONTEXT");
        btnMaterials = createNavButton("2", "MATERIALS");
        btnPredictions = createNavButton("3", "PREDICTIONS");
        btnProtocol = createNavButton("4", "PROTOCOL");
        btnResults = createNavButton("5", "RESULTS");
        btnReflection = createNavButton("6", "REFLECTION");
        btnSummary = createNavButton("7", "SUMMARY");

        // Disable Results button initially - will enable after Step 4 completion
        btnResults.setDisable(true);
        btnResults.setOpacity(0.5);

        // Set button actions
        btnContext.setOnAction(e -> {
            // Restore scrollPane
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
            // Restore scrollPane
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
            // Restore scrollPane
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

            // Hide scrollPane and show LabWorkspace
            scrollPane.setVisible(false);
            scrollPane.setManaged(false);

            // Create and add LabWorkspace
            if (msLabworkspace == null) {
                msLabworkspace = new MSLabworkspace(this);
            }

            // Remove scrollPane if present and add labWorkspace
            mainpanel.getChildren().clear();
            mainpanel.getChildren().add(msLabworkspace.getMSLabWorkspace());
            VBox.setVgrow(msLabworkspace.getMSLabWorkspace(), Priority.ALWAYS);

            // Show instruction panel
            resetToLabView();
        });

        btnResults.setOnAction(e -> {
            // Restore scrollPane
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
            // Restore scrollPane
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
            // Restore scrollPane
            mainpanel.getChildren().clear();
            mainpanel.getChildren().add(scrollPane);
            scrollPane.setVisible(true);
            scrollPane.setManaged(true);
            VBox.setVgrow(scrollPane, Priority.ALWAYS);

            updateScrollContent(getSummaryContent());
            setSelectedButton(btnSummary);
            resetToButtonView();
        });

        // Add buttons with spacing
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

        // Set initial selection
        setSelectedButton(btnContext);

        return panel;
    }

    private Button createNavButton(String number, String text) {
        HBox container = new HBox(12);
        container.setAlignment(Pos.CENTER_LEFT);
        container.setPadding(new Insets(8, 15, 8, 15));
        container.setStyle("-fx-background-color: transparent;");

        // Number circle
        Circle circle = new Circle(15);
        circle.setFill(Color.web("#ffffff"));
        circle.setStroke(Color.WHITE);
        circle.setStrokeWidth(2);

        Label numberLabel = new Label(number);
        numberLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        numberLabel.setTextFill(Color.WHITE);

        // Stack circle and number
        StackPane circleStack = new StackPane();
        circleStack.getChildren().addAll(circle, numberLabel);

        //Button text
        Button btn = new Button(text);
        btn.setFont(Font.font("Arial", FontWeight.BOLD, 15));
        btn.setTextFill(Color.WHITE);
        btn.getStyleClass().add("neu-button");
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(btn, Priority.ALWAYS);

        // Store container reference in button's userData
        btn.setUserData(container);
        container.getChildren().addAll(circleStack, btn);
        return btn;

    }

    private void setSelectedButton(Button btn) {
        // Clear previous selection
        if (selectedButton != null) {
            HBox prevContainer = (HBox) selectedButton.getUserData();
            if (prevContainer != null) {
                prevContainer.setStyle("-fx-background-color: transparent;");
            }
        }

        // Set new selection
        selectedButton = btn;
        if (btn != null) {
            HBox container = (HBox) btn.getUserData();
            if (container != null) {
                container.setStyle("-fx-background-color: rgba(255,255,255,0.2); " +
                        "-fx-background-radius: 5;");
            }
        }
    }

    private void resetToButtonView() {
//        if (!isPredictionExpanded)
//            return;
//        isPredictionExpanded = false;
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

        // Header with circle and title
        HBox header = createProtocolHeader();

        // Progress bar and step indicator
        HBox progressSection = createProgressSection();

        // Step title
        Label stepTitleLabel = new Label();
        stepTitleLabel.setId("stepTitle");
        stepTitleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 15));
        stepTitleLabel.setStyle("-fx-text-fill: #1a5490;");
        stepTitleLabel.setWrapText(true);
        stepTitleLabel.setMaxWidth(350);

        // Scroll pane for instructions
        ScrollPane instructionsScroll = new ScrollPane();
        instructionsScroll.setFitToWidth(true);
        instructionsScroll.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");
        instructionsScroll.setPrefHeight(300);
        VBox.setVgrow(instructionsScroll, Priority.ALWAYS);

        currentInstructionsList = new VBox(8);
        currentInstructionsList.setId("instructionsList");
        instructionsScroll.setContent(currentInstructionsList);

        // Navigation buttons
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

        // Previous button
        Button previousBtn = new Button("◀ Previous");
        previousBtn.setId("previousBtn");
        previousBtn.setFont(Font.font("Arial", FontWeight.BOLD, 12));
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

        // Circle button (1.a, 2.b, etc)
        Circle stepCircle = new Circle(18);
        stepCircle.setFill(Color.web("#1a5490"));
        stepCircle.setStroke(Color.WHITE);
        stepCircle.setStrokeWidth(2);

        Label stepLabel = new Label("1.a");
        stepLabel.setId("stepCircleLabel");
        stepLabel.setFont(Font.font("Arial", FontWeight.BOLD, 11));
        stepLabel.setTextFill(Color.WHITE);

        StackPane circleBtn = new StackPane(stepCircle, stepLabel);

        // Next button
        Button nextBtn = new Button("Next ▶");
        nextBtn.setId("nextBtn");
        nextBtn.setFont(Font.font("Arial", FontWeight.BOLD, 12));
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

        // Circle with number
        Circle circle = new Circle(18);
        circle.setFill(Color.web("#1a5490"));
        circle.setStroke(Color.WHITE);
        circle.setStrokeWidth(2);

        Label numberLabel = new Label("4");
        numberLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        numberLabel.setTextFill(Color.WHITE);

        StackPane circleStack = new StackPane(circle, numberLabel);

        // Title
        Label titleLabel = new Label("PROTOCOL");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        titleLabel.setStyle("-fx-text-fill: #1a5490;");

        // Notes button (right side)
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button notesBtn = new Button("📋 Notes");
        notesBtn.setFont(Font.font("Arial", FontWeight.BOLD, 11));
        notesBtn.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-text-fill: #1a5490;" +
                        "-fx-cursor: hand;"
        );

        header.getChildren().addAll(circleStack, titleLabel, spacer, notesBtn);
        Button protocolButton = new Button();
        protocolButton.setGraphic(header);
        protocolButton.getStyleClass().add("button-33");


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

        // Progress bar
        ProgressBar progressBar = new ProgressBar(0);
        progressBar.setId("protocolProgressBar");
        progressBar.setPrefWidth(280);
        progressBar.setPrefHeight(8);
        progressBar.setStyle("-fx-accent: #1a5490;");

        // Step indicator
        Label stepIndicator = new Label("1/4");
        stepIndicator.setId("stepIndicator");
        stepIndicator.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        stepIndicator.setStyle("-fx-text-fill: #333;");

        section.getChildren().addAll(progressBar, stepIndicator);

        return section;
    }


    private void resetToLabView() {
        instruction.getChildren().clear();

        // Create lab instruction panel
        instructionCommandPanel = createInstructionCommandPanel();
        instruction.getChildren().add(instructionCommandPanel);

        // Load step 1
        loadProtocolStep(1);
    }

    private VBox getPredictionsContent() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(40));
        content.setStyle("-fx-background-color: #f5f5f5;");

        // Title
        Label title = new Label("3. PREDICTIONS");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 32));
        title.setStyle("-fx-text-fill: #1a5490;");

        // Underline
        Separator underline = new Separator();
        underline.setMaxWidth(300);
        underline.setStyle("-fx-background-color: #00aaff;");
        underline.setPrefHeight(3);

        // Description text
        Label descText = new Label("During this simulation, you will be dispensing different volumes of liquid red dye with a micropipette onto a blotting paper. How do you think the different volumes will influence the size of red circles you expect to see on the blotting paper? Make your predictions here by adjusting the controls to make each circle larger or smaller.");
        descText.setWrapText(true);
        descText.setFont(Font.font("Arial", 16));
        descText.setLineSpacing(3);
        descText.setStyle("-fx-text-fill: #333333;");
        descText.setMaxWidth(700);

        // Estimate title
        Label estimateTitle = new Label("Estimate the size of each blot if you set the micropipette to:");
        estimateTitle.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        estimateTitle.setStyle("-fx-text-fill: #333333;");
        estimateTitle.setPadding(new Insets(20, 0, 10, 0));

        // Create the prediction circles container
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

        // SAMPLE circle (fixed, no controls)
        VBox sampleBox = createFixedCircle("10µl", "SAMPLE", 0.15);
        container.getChildren().add(sampleBox);

        // Adjustable circles A, B, C, D
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

        // Volume label
        Label volLabel = new Label(volumeLabel);
        volLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        volLabel.setStyle("-fx-text-fill: #333333;");

        // Circle container
        double outerRadius = 50;
        StackPane circleStack = new StackPane();
        circleStack.setPrefSize(outerRadius * 2, outerRadius * 2);

        // Outer black circle
        Circle outerCircle = new Circle(outerRadius);
        outerCircle.setFill(Color.TRANSPARENT);
        outerCircle.setStroke(Color.BLACK);
        outerCircle.setStrokeWidth(2);

        // Inner red circle (fixed)
        Circle innerCircle = new Circle(outerRadius * fillRatio);
        innerCircle.setFill(Color.RED);

        circleStack.getChildren().addAll(outerCircle, innerCircle);

        // Name label
        Label nameLabel = new Label(name);
        nameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        nameLabel.setStyle("-fx-text-fill: #333333;");

        box.getChildren().addAll(volLabel, circleStack, nameLabel);
        return box;
    }

    private VBox createAdjustableCircle(String volumeLabel, String name, int index) {
        VBox box = new VBox(5);
        box.setAlignment(Pos.CENTER);
        box.setPrefWidth(120);

        // Volume label
        Label volLabel = new Label(volumeLabel);
        volLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        volLabel.setStyle("-fx-text-fill: #333333;");

        // Circle container
        double outerRadius = 50;
        StackPane circleStack = new StackPane();
        circleStack.setPrefSize(outerRadius * 2, outerRadius * 2);

        // Outer black circle
        Circle outerCircle = new Circle(outerRadius);
        outerCircle.setFill(Color.TRANSPARENT);
        outerCircle.setStroke(Color.BLACK);
        outerCircle.setStrokeWidth(2);

        // Inner red circle (adjustable)
        Circle innerCircle = new Circle(outerRadius * predictionSizes[index]);
        innerCircle.setFill(Color.RED);

        // Initialize stored prediction radius
        storePredictionRadius(index, innerCircle.getRadius());

        circleStack.getChildren().addAll(outerCircle, innerCircle);

        // Control buttons row
        HBox controlsRow = new HBox(10);
        controlsRow.setAlignment(Pos.CENTER);

        // Minus button
        Button minusBtn = new Button("-");
        minusBtn.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        minusBtn.setPrefSize(35, 35);
        minusBtn.setStyle(
                "-fx-background-color: #FFA500;" +
                        "-fx-text-fill: white;" +
                        "-fx-background-radius: 50%;" +
                        "-fx-cursor: hand;"
        );
        minusBtn.setOnAction(e -> {
            if (predictionSizes[index] > 0.05) { // Minimum size
                predictionSizes[index] -= 0.05;
                double newRadius = outerRadius * predictionSizes[index];
                innerCircle.setRadius(newRadius);

                // Store the radius value for result comparison
                storePredictionRadius(index, newRadius);
            }
        });

        // Small dot (visual separator)
        Circle dot = new Circle(3, Color.RED);

        // Plus button
        Button plusBtn = new Button("+");
        plusBtn.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        plusBtn.setPrefSize(35, 35);
        plusBtn.setStyle(
                "-fx-background-color: #FFA500;" +
                        "-fx-text-fill: white;" +
                        "-fx-background-radius: 50%;" +
                        "-fx-cursor: hand;"
        );
        plusBtn.setOnAction(e -> {
            if (predictionSizes[index] < 1.0) { // Maximum size (up to outer circle)
                predictionSizes[index] += 0.05;
                double newRadius = outerRadius * predictionSizes[index];
                innerCircle.setRadius(newRadius);

                // Store the radius value for result comparison
                storePredictionRadius(index, newRadius);
            }
        });

        controlsRow.getChildren().addAll(minusBtn, dot, plusBtn);

        // Name label
        Label nameLabel = new Label(name);
        nameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        nameLabel.setStyle("-fx-text-fill: #333333;");

        box.getChildren().addAll(volLabel, controlsRow, circleStack, nameLabel);
        return box;
    }

    private VBox getMaterialsContent() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(40));
        content.setStyle("-fx-background-color: #f5f5f5;");

        // Title
        Label title = new Label("2. MATERIALS");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 32));
        title.setStyle("-fx-text-fill: #1a5490;");

        // Underline
        Separator underline = new Separator();
        underline.setMaxWidth(250);
        underline.setStyle("-fx-background-color: #00aaff;");
        underline.setPrefHeight(3);

        // Info box
        Label infoText = new Label("Click on the images to learn more about them.");
        infoText.setFont(Font.font("Arial", 14));
        infoText.setStyle(
                "-fx-background-color: #e8f4f8;" +
                        "-fx-padding: 15;" +
                        "-fx-border-color: #d0d0d0;" +
                        "-fx-border-width: 1;" +
                        "-fx-border-radius: 5;" +
                        "-fx-background-radius: 5;"
        );
        infoText.setMaxWidth(600);

        // === REAGENTS SECTION ===
        Label reagentsTitle = new Label("REAGENTS");
        reagentsTitle.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        reagentsTitle.setStyle("-fx-text-fill: #1a5490;");

        Label reagentsSubtitle = new Label("Chemicals that can be used in this experiment");
        reagentsSubtitle.setFont(Font.font("Arial", 14));
        reagentsSubtitle.setStyle("-fx-text-fill: #666666;");

        GridPane reagentsGrid = createReagentsGrid();


        // === MICROPIPETTING EQUIPMENT SECTION ===
        Label micropipettingTitle = new Label("MICROPIPETTING EQUIPMENT");
        micropipettingTitle.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        micropipettingTitle.setStyle("-fx-text-fill: #1a5490;");

        Label micropipettingSubtitle = new Label("Used to measure small volumes");
        micropipettingSubtitle.setFont(Font.font("Arial", 14));
        micropipettingSubtitle.setStyle("-fx-text-fill: #666666;");

        GridPane micropipettingGrid = createMicropipettingGrid();

        // === EQUIPMENT SECTION ===
        Label equipmentTitle = new Label("EQUIPMENT");
        equipmentTitle.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        equipmentTitle.setStyle("-fx-text-fill: #1a5490;");

        Label equipmentSubtitle = new Label("Micropipettes, tip boxes, and equipment");
        equipmentSubtitle.setFont(Font.font("Arial", 14));
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

        // Add pipettes
        int col = 0;
        for (String[] pipette : pipettes) {
            StackPane item = createMaterialItem(pipette[0], pipette[1], pipette[2], 70, 85);
            grid.add(item, col, 0);
            col++;
        }

        // Add tip boxes
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
        // I use StackPane so info panel can overlay on top
        StackPane mainContainer = new StackPane();
        mainContainer.setMaxWidth(150);
        mainContainer.setAlignment(Pos.TOP_CENTER);

        // The card container (clickable image + label)
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

        // Image container with dark blue background
        StackPane imageContainer = new StackPane();
        imageContainer.setPrefSize(120, 100);
        imageContainer.setStyle(
                "-fx-background-color: #0d3d5c;" +
                        "-fx-background-radius: 5;"
        );

        // Load SVG using SVGLoader utility
        ImageView imageView = SVGLoader.loadSVG(imagePath, imgWidth, imgHeight, true);

        if (imageView != null) {
            imageView.setFitWidth(imgWidth);
            imageView.setFitHeight(imgHeight);
            imageView.setPreserveRatio(true);
            imageView.setSmooth(true);
            imageContainer.getChildren().add(imageView);
        } else {
            // Fallback: create a colored circle
            Circle placeholder = new Circle(30);
            placeholder.setFill(Color.web(accentColor));
            imageContainer.getChildren().add(placeholder);
        }

        // Label
        Label label = new Label(labelText);
        label.setFont(Font.font("Arial", FontWeight.SEMI_BOLD, 12));
        label.setTextAlignment(TextAlignment.CENTER);
        label.setWrapText(true);
        label.setMaxWidth(130);
        label.setStyle("-fx-text-fill: #333333;");

        cardContainer.getChildren().addAll(imageContainer, label);

        // Create overlay info panel (initially hidden, positioned on top)
        VBox infoPanel = createOverlayInfoPanel(labelText);
        infoPanel.setTranslateY(-200); // Start above (hidden)
        infoPanel.setOpacity(0);
        infoPanel.setVisible(false);
        StackPane.setAlignment(infoPanel, Pos.TOP_CENTER);

        // Add both to main container (info panel on top)
        mainContainer.getChildren().addAll(cardContainer, infoPanel);

        // Hover effects on card
        addHoverEffects(cardContainer);

        // Click to show/hide overlay
        final boolean[] isExpanded = {false};
        cardContainer.setOnMouseClicked(e -> {
            toggleOverlayPanel(infoPanel, isExpanded);
            e.consume();
        });

        // Also allow clicking info panel to close it
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
                "-fx-background-color: rgba(26, 84, 144, 0.95);" + // Semi-transparent dark blue
                        "-fx-border-color: #00aaff;" +
                        "-fx-border-width: 2;" +
                        "-fx-border-radius: 8;" +
                        "-fx-background-radius: 8;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,170,255,0.6), 20, 0, 0, 8);"
        );

        // Close hint (small X or tap indicator)
        Label closeHint = new Label("✕");
        closeHint.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        closeHint.setStyle("-fx-text-fill: white; -fx-cursor: hand;");
        closeHint.setAlignment(Pos.CENTER_RIGHT);
        closeHint.setMaxWidth(Double.MAX_VALUE);

        // Title
        Label titleLabel = new Label(materialName);
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        titleLabel.setStyle("-fx-text-fill: white;");
        titleLabel.setWrapText(true);
        titleLabel.setMaxWidth(120);

        // Separator line
        Separator separator = new Separator();
        separator.setStyle("-fx-background-color: rgba(255,255,255,0.3);");
        separator.setPrefHeight(1);

        // Description
        String description = getMaterialDescription(materialName);
        Label descLabel = new Label(description);
        descLabel.setFont(Font.font("Arial", 10));
        descLabel.setStyle("-fx-text-fill: white;");
        descLabel.setWrapText(true);
        descLabel.setMaxWidth(120);

        // Wrap in ScrollPane for long content
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
            // Show - slide down from top
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
            // Hide - slide up to top
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

        // Reagents
        if (cleanName.contains("Red Dye Solution")) {
            return "Red dye in tube.";
        }

        // Micropipettes
        else if (cleanName.contains("P2 Pipette")) {
            return "Used to draw up and dispense volumes of liquid between 0.2 and 2 microliters.";
        } else if (cleanName.contains("P20 Pipette")) {
            return "Used to draw up and dispense volumes of liquid between 2 and 20 microliters.";
        } else if (cleanName.contains("P200 Pipette")) {
            return "Used to draw up and dispense volumes of liquid between 20 and 200 microliters.";
        } else if (cleanName.contains("P1000 Pipette")) {
            return "Used to draw up and dispense volumes of liquid between 200 and 1000 microliters.";
        }

        // Tip Boxes
        else if (cleanName.contains("P2 Tip Box")) {
            return "Filled with P2 tips. Tips are attached to the micropipette and discarded after single use to avoid contamination between transfers.";
        } else if (cleanName.contains("P20 Tip Box")) {
            return "Filled with P20 tips. Tips are attached to the micropipette and discarded after single use to avoid contamination between transfers.";
        } else if (cleanName.contains("P200 Tip Box")) {
            return "Filled with P200 tips. Tips are attached to the micropipette and discarded after single use to avoid contamination between transfers.";
        } else if (cleanName.contains("P1000 Tip Box")) {
            return "Filled with P1000 tips. Tips are attached to the micropipette and discarded after single use to avoid contamination between transfers.";
        }

        // Other Equipment
        else if (cleanName.contains("Trash")) {
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

        // Title
        Label title = new Label("5. RESULTS");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 32));
        title.setStyle("-fx-text-fill: #1a5490;");

        // Underline
        Separator underline = new Separator();
        underline.setMaxWidth(200);
        underline.setStyle("-fx-background-color: #00aaff;");
        underline.setPrefHeight(3);

        // Introduction text
        Label introText = new Label(
                "Look at the relative size difference between your micropipetted circles. You " +
                        "should be able to see that accuracy of the liquid transferred is very important " +
                        "and will impact the success of your experiments. Compare your results with the " +
                        "ideal results. If the size of your circles matches the circles in the ideal results, " +
                        "you have pipetted the correct volumes. If they do not match, you have pipetted " +
                        "either too much or too little liquid."
        );
        introText.setWrapText(true);
        introText.setFont(Font.font("Arial", 16));
        introText.setLineSpacing(3);
        introText.setStyle("-fx-text-fill: #333333;");
        introText.setMaxWidth(700);

        // Predicted Results Section
        VBox predictedSection = createPredictedResultsSection();

        // Actual Results Section
        VBox actualSection = createActualResultsSection();

        // Ideal Results Section
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
        sectionTitle.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        sectionTitle.setStyle("-fx-text-fill: #333333;");

        Label sectionDesc = new Label("These are your initial predictions.");
        sectionDesc.setFont(Font.font("Arial", 14));
        sectionDesc.setStyle("-fx-text-fill: #666666;");

        // Create circles container
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
        sectionTitle.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        sectionTitle.setStyle("-fx-text-fill: #333333;");

        Label sectionDesc = new Label("These are your results based on the volumes you pipetted.");
        sectionDesc.setFont(Font.font("Arial", 14));
        sectionDesc.setStyle("-fx-text-fill: #666666;");

        // Create circles container
        HBox circlesContainer = new HBox(30);
        circlesContainer.setAlignment(Pos.CENTER);
        circlesContainer.setPadding(new Insets(20));

        // Get actual results from blotting paper (via MSLabworkspace)
        String[] circleNames = {"A", "B", "C", "D"};
        String[] volumes = {"0µl", "0µl", "0µl", "0µl"};

        // We'll need to get actual radius from blotting paper
        // For now, using default values - will be updated when integrated with MSLabworkspace
        double[] actualRadii = new double[4];
        if (msLabworkspace != null) {
            for (int i = 0; i < 4; i++) {
                double radius = msLabworkspace.getBlottingPaperRadius(circleNames[i]);
                // Convert radius back to approximate percentage (max radius is 35)
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
        sectionTitle.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        sectionTitle.setStyle("-fx-text-fill: #333333;");

        Label sectionDesc = new Label("These are the sizes you would expect to see if you pipetted the correct volumes.");
        sectionDesc.setFont(Font.font("Arial", 14));
        sectionDesc.setStyle("-fx-text-fill: #666666;");

        // Create circles container
        HBox circlesContainer = new HBox(30);
        circlesContainer.setAlignment(Pos.CENTER);
        circlesContainer.setPadding(new Insets(20));

        // Ideal volumes: A=20µl, B=15µl, C=7.5µl, D=2µl
        // Scale: 20µl = 100% (1.0), proportional for others
        double[] idealSizes = {1.0, 0.75, 0.375, 0.1};  // 20µl, 15µl, 7.5µl, 2µl
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

        // Outer black circle
        Circle outerCircle = new Circle(outerRadius);
        outerCircle.setFill(Color.TRANSPARENT);
        outerCircle.setStroke(Color.BLACK);
        outerCircle.setStrokeWidth(2);

        // Inner red circle
        Circle innerCircle = new Circle(outerRadius * fillRatio);
        innerCircle.setFill(Color.RED);

        circleStack.getChildren().addAll(outerCircle, innerCircle);

        // Label below circle
        Label circleLabel = new Label(label);
        circleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        circleLabel.setStyle("-fx-text-fill: #333333;");

        box.getChildren().addAll(circleStack, circleLabel);

        // Add "Incorrect!" badge if needed
        if (showIncorrect && fillRatio > 0.01) {
            Label incorrectBadge = new Label("Incorrect!");
            incorrectBadge.setFont(Font.font("Arial", FontWeight.BOLD, 11));
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

        // Title
        Label title = new Label("6. REFLECTION");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 32));
        title.setStyle("-fx-text-fill: #1a5490;");

        // Underline
        Separator underline = new Separator();
        underline.setMaxWidth(300);
        underline.setStyle("-fx-background-color: #00aaff;");
        underline.setPrefHeight(3);

        // Introduction text
        Label text = new Label(
                "Let's look at what you learned from this simulation. Please click on your answer " +
                        "to each of the following questions to save them in your notebook."
        );
        text.setFont(Font.font("Arial", 16));
        text.setWrapText(true);
        text.setStyle("-fx-text-fill: #333333;");
        text.setMaxWidth(700);
        text.setLineSpacing(3);

        // Question 1
        QuizQuestion q1 = createQuizQuestion(
                1,
                "What do you think is the main purpose of this simulation?",
                new String[]{
                        "To learn about the different equipment you will encounter in a lab.",
                        "To familiarize yourself with proper lab etiquette.",
                        "To learn the correct way to handle a micropipette in a laboratory.",
                        "To practice making solutions of different concentrations."
                },
                2, // Correct answer index (C - third option)
                "What specific skill did you practice during this simulation?",
                3  // Attempts allowed
        );

        // Question 2
        QuizQuestion q2 = createQuizQuestion(
                2,
                "What volume should be set on a P20 micropipette to measure 18.5 microliters?",
                new String[]{
                        "18.5",
                        "185",
                        "1.85",
                        "0.185"
                },
                1, // Correct answer index (B - 185)
                "How many decimal places are displayed on a P20 micropipette?",
                3
        );

        // Question 3
        QuizQuestion q3 = createQuizQuestion(
                3,
                "If the window on a P20 micropipette displays the numbers \"1 5 5\" from top to bottom, what volume will be measured?",
                new String[]{
                        "1.55 microliters",
                        "15.5 microliters",
                        "155 microliters"
                },
                1, // Correct answer index (B - 15.5 microliters)
                "Can a P20 measure this volume of liquid?",
                3
        );

        // Question 4
        QuizQuestion q4 = createQuizQuestion(
                4,
                "What is the main purpose of disposable tips on a micropipette?",
                new String[]{
                        "To prevent cross contamination between samples.",
                        "To reach into small tubes.",
                        "To make the micropipettes measure larger volumes."
                },
                0, // Correct answer index (A)
                "Disposable tips cannot change the volume range of a micropipette.",
                3
        );

        // Question 5
        QuizQuestion q5 = createQuizQuestion(
                5,
                "What happens if you press the plunger of a micropipette past the first stop when drawing up liquid?",
                new String[]{
                        "You will draw up more liquid than you wanted.",
                        "You will draw up less liquid than you wanted.",
                        "It will have no effect on the volume you draw up."
                },
                0, // Correct answer index (A)
                "The second stop is used when expelling liquid, not when drawing it up.",
                3
        );

        // Feedback Question (separate section)
        Label feedbackTitle = new Label("FEEDBACK QUESTION");
        feedbackTitle.setFont(Font.font("Arial", FontWeight.BOLD, 24));
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

        // Title
        Label title = new Label("7. SUMMARY");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 32));
        title.setStyle("-fx-text-fill: #1a5490;");

        // Underline
        Separator underline = new Separator();
        underline.setMaxWidth(200);
        underline.setStyle("-fx-background-color: #00aaff;");
        underline.setPrefHeight(3);

        // Introduction text
        Label introText = new Label(
                "It is always good to summarize your methodology and observations after you have completed an experiment. Please view a recap of this simulation and takeaway messages regarding micropipetting below."
        );
        introText.setWrapText(true);
        introText.setFont(Font.font("Arial", 16));
        introText.setStyle("-fx-text-fill: #333333;");
        introText.setMaxWidth(650);

        // Learning outcomes box
        VBox learningBox = createLearningOutcomesBox();

        // Takeaway messages section
        Label takeawayTitle = new Label("Takeaway messages");
        takeawayTitle.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        takeawayTitle.setStyle("-fx-text-fill: #1a5490;");
        takeawayTitle.setPadding(new Insets(20, 0, 10, 0));

        // Takeaway items
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

        // Main heading
        Label heading = new Label(
                "In this simulation, you learned the correct way to use a micropipette in a lab. This is a crucial technique that is used frequently in molecular biology. The simulation explored how to:"
        );
        heading.setWrapText(true);
        heading.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        heading.setStyle("-fx-text-fill: #1a5490;");

        // Learning points with checkmarks
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

        // Blue checkmark circle
        Circle checkCircle = new Circle(10);
        checkCircle.setFill(Color.web("#4a90e2"));
        checkCircle.setStroke(Color.web("#3a7bc8"));
        checkCircle.setStrokeWidth(1);

        // Checkmark symbol (✓)
        Label checkmark = new Label("✓");
        checkmark.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        checkmark.setStyle("-fx-text-fill: white;");

        StackPane checkIcon = new StackPane(checkCircle, checkmark);
        checkIcon.setPrefSize(20, 20);

        // Text
        Label itemText = new Label(text);
        itemText.setFont(Font.font("Arial", 15));
        itemText.setStyle("-fx-text-fill: #333333;");
        itemText.setWrapText(true);
        itemText.setMaxWidth(550);

        item.getChildren().addAll(checkIcon, itemText);
        return item;
    }

    private VBox createTakeawayItems() {
        VBox container = new VBox(15);
        container.setMaxWidth(750);

        // Takeaway messages data: [image_path, message_text]
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

        // Image container with dark blue background
        StackPane imageContainer = new StackPane();
        imageContainer.setPrefSize(120, 90);
        imageContainer.setStyle(
                "-fx-background-color: #0d3d5c;" +
                        "-fx-background-radius: 5;"
        );

        // Load SVG image
        ImageView imageView = SVGLoader.loadSVG(imagePath, 80, 80);

        if (imageView != null) {
            imageView.setFitWidth(80);
            imageView.setFitHeight(80);
            imageView.setPreserveRatio(true);
            imageView.setSmooth(true);
            imageContainer.getChildren().add(imageView);
        }

        // Message text
        Label message = new Label(messageText);
        message.setWrapText(true);
        message.setFont(Font.font("Arial", 15));
        message.setStyle("-fx-text-fill: #333333;");
        message.setMaxWidth(580);
        message.setLineSpacing(3);

        // Set HBox to grow with text
        HBox.setHgrow(message, Priority.ALWAYS);

        container.getChildren().addAll(imageContainer, message);

        return container;
    }
    // ========== STEP LOADING ==========

    private void loadProtocolStep(int stepNumber) {
        currentProtocolStep = stepNumber;

        // Update progress
        ProgressBar progressBar = (ProgressBar) instructionCommandPanel.lookup("#protocolProgressBar");
        if (progressBar != null) {
            progressBar.setProgress((double) stepNumber / TOTAL_PROTOCOL_STEPS);
        }

        Label stepIndicator = (Label) instructionCommandPanel.lookup("#stepIndicator");
        if (stepIndicator != null) {
            stepIndicator.setText(stepNumber + "/" + TOTAL_PROTOCOL_STEPS);
        }

        // Update step title and instructions
        updateStepContent(stepNumber);

        // Update Previous button state
        Button previousBtn = (Button) instructionCommandPanel.lookup("#previousBtn");
        if (previousBtn != null) {
            previousBtn.setDisable(stepNumber == 1);
        }

        // Update Next button state
        Button nextBtn = (Button) instructionCommandPanel.lookup("#nextBtn");
        if (nextBtn != null) {
            nextBtn.setDisable(stepNumber == TOTAL_PROTOCOL_STEPS);
        }
    }

    private void updateStepContent(int stepNumber) {
        // Step titles
        String[] stepTitles = {
                "1. Set the volume on the P20 micropipette.",
                "2. Draw up the red dye into the micropipette.",
                "3. Pipette the red dye onto blotting paper.",
                "4. Pipette different volumes of red dye onto the blotting paper.",
        };

        // All instructions for each step
        String[][][] allInstructions = {
                // Step 1
                {
                        {"a", "Select the P20 micropipette.", String.valueOf(false)},
                        {"b", "Select the volume setting to set the volume to 20 μl and then select Save volume.", String.valueOf(false)},
                        {"c", "Select the P20 tip box to open it.", String.valueOf(false)},
                        {"d", "Move the P20 micropipette onto the P20 tip box to attach a tip.", String.valueOf(false)},
                        {"e", "Select the P20 tip box to close it.", String.valueOf(false)},
                },
                // Step 2
                {
                        {"a", "Select the red dye solution tube to open it.", String.valueOf(false)},
                        {"b", "Select the micropipette and place it in the red dye solution.", String.valueOf(false)},
                        {"c", "Draw up the red dye by first pushing down the plunger until it reaches the first stop and slowly releasing the plunger.", String.valueOf(false)},
                        {"d", "Close the red dye solution tube.", String.valueOf(false)},
                },
                // Step 3
                {
                        {"a", "Move the micropipette to circle A on the blotting paper to dispense the red dye.", String.valueOf(false)},
                        {"b", "Dispense the red dye by holding down the plunger until it reaches the second stop and slowly releasing it.", String.valueOf(false)},
                        {"c", "Move the micropipette over the trash can and press on the eject mechanism to eject the tip.", String.valueOf(false)},
                        {"d", "Select the P20 micropipette and place it back onto the rack.", String.valueOf(false)},
                },
                // Step 4
                {
                        {"a", "In circle B on the blotting paper, follow the same process as before to dispense 15 μl of red dye.", String.valueOf(false)},
                        {"b", "In circle C on the blotting paper, follow the same process as before to dispense 7.5 μl of red dye.", String.valueOf(false)},
                        {"c", "In circle D on the blotting paper, follow the same process as before to dispense 2 μl of red dye.", String.valueOf(false)}
                },
        };

        // Update title
        Label stepTitle = (Label) instructionCommandPanel.lookup("#stepTitle");
        if (stepTitle != null) {
            stepTitle.setText(stepTitles[stepNumber - 1]);
        }

        // Clear and populate instructions
        currentInstructionsList.getChildren().clear();

        String[][] instructions = allInstructions[stepNumber - 1];

        for (int i = 0; i < instructions.length; i++) {
            String letter = instructions[i][0];
            String text = instructions[i][1];
            boolean isHighlighted = i == 0; // First instruction highlighted by default

            HBox instructionItem = createInstructionItem(letter, text, isHighlighted, stepNumber);
            currentInstructionsList.getChildren().add(instructionItem);
        }

        // Update circle label
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

        // Highlight style
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

        // Letter circle
        Circle circle = new Circle(14);
        circle.setFill(Color.web("#1a5490"));
        circle.setStroke(Color.WHITE);
        circle.setStrokeWidth(2);

        Label letterLabel = new Label(letter);
        letterLabel.setFont(Font.font("Arial", FontWeight.BOLD, 11));
        letterLabel.setTextFill(Color.WHITE);

        StackPane letterCircle = new StackPane(circle, letterLabel);
        letterCircle.setMinSize(28, 28);
        letterCircle.setMaxSize(28, 28);

        // Text
        Label instructionText = new Label(text);
        instructionText.setFont(Font.font("Arial", 12));
        instructionText.setStyle("-fx-text-fill: #333;");
        instructionText.setWrapText(true);
        instructionText.setMaxWidth(280);
        HBox.setHgrow(instructionText, Priority.ALWAYS);

        // Edit icon (pencil)
        Label editIcon = new Label("✏");
        editIcon.setFont(Font.font("Arial", 12));
        editIcon.setStyle("-fx-text-fill: #1a5490; -fx-cursor: hand;");

        item.getChildren().addAll(letterCircle, instructionText, editIcon);

        // Click to highlight
        item.setOnMouseClicked(e -> highlightInstruction(item, stepNumber + "." + letter));

        return item;
    }

    private void highlightInstruction(HBox item, String instructionId) {
        // Remove previous highlight
        if (currentHighlightedInstruction != null) {
            HBox prevItem = (HBox) currentInstructionsList.lookup("#" + currentHighlightedInstruction);
            if (prevItem != null) {
                prevItem.setStyle("-fx-background-color: transparent;");
            }
        }

        // Highlight current
        item.setStyle(
                "-fx-background-color: #ADD8E6;" +
                        "-fx-border-radius: 5;" +
                        "-fx-background-radius: 5;"
        );
        currentHighlightedInstruction = item.getId();

        // Update circle label
        Label circleLabel = (Label) instructionCommandPanel.lookup("#stepCircleLabel");
        if (circleLabel != null) {
            circleLabel.setText(instructionId);
        }
    }
    // ========== NAVIGATION HANDLERS ==========

    private void handlePreviousStep() {
        if (currentProtocolStep == 1) {
            // Go back to button panel
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

        // Find and highlight the instruction
        String instructionId = "instruction_" + stepNumber + "_" + subStep;

        // Remove previous highlight
        if (currentHighlightedInstruction != null) {
            HBox prevItem = (HBox) currentInstructionsList.lookup("#" + currentHighlightedInstruction);
            if (prevItem != null) {
                prevItem.setStyle("-fx-background-color: transparent;");
            }
        }

        // Highlight current instruction
        HBox currentItem = (HBox) currentInstructionsList.lookup("#" + instructionId);
        if (currentItem != null) {
            currentItem.setStyle(
                    "-fx-background-color: #ADD8E6;" +
                            "-fx-border-radius: 5;" +
                            "-fx-background-radius: 5;"
            );
            currentHighlightedInstruction = instructionId;

            // Update circle label
            Label circleLabel = (Label) instructionCommandPanel.lookup("#stepCircleLabel");
            if (circleLabel != null) {
                circleLabel.setText(stepNumber + "." + subStep);
            }
        }
    }

    /**
     * Store prediction radius for later comparison with actual results
     *
     * @param index  Circle index (0=A, 1=B, 2=C, 3=D)
     * @param radius Radius value in pixels
     */
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

    /**
     * Get prediction radius for a specific circle
     *
     * @param circleName Circle name (A, B, C, or D)
     * @return Radius in pixels
     */
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

    /**
     * Complete current step and auto-advance to next sub-step
     * Called by MSLabworkspace when user completes an action
     */
    public void completeCurrentStep() {
        // Mark current step as complete
        String stepKey = activeStep + "_" + activeSubStep;
        stepCompletionMap.put(stepKey, true);

        System.out.println("✅ Completed: Step " + activeStep + "." + activeSubStep);

        // Check if this was the last step (Step 4.c)
        if (activeStep == 4 && activeSubStep.equals("c")) {
            // Enable Results button
            enableResultsButton();
            System.out.println("🎊 All protocol steps completed! Results section now available.");
        }

        // Map to determine next sub-step
        String nextSubStep = getNextSubStep(activeStep, activeSubStep);

        if (nextSubStep != null) {
            // Move to next sub-step
            activeSubStep = nextSubStep;
            highlightNextInstruction(String.valueOf(activeStep), activeSubStep);
            System.out.println("➡️ Moving to: Step " + activeStep + "." + activeSubStep);
        } else {
            // Current step's all sub-steps complete, check if we can move to next step
            if (activeStep < TOTAL_PROTOCOL_STEPS) {
                activeStep++;
                activeSubStep = "a";
                // Auto-load next step if needed
                if (currentProtocolStep != activeStep) {
                    loadProtocolStep(activeStep);
                }
                System.out.println("➡️ Moving to: Step " + activeStep + "." + activeSubStep);
            } else {
                System.out.println("🎉 All steps completed!");
            }
        }
    }

    /**
     * Enable the Results button after all protocol steps are complete
     */
    private void enableResultsButton() {
        if (btnResults != null) {
            btnResults.setDisable(false);
            btnResults.setOpacity(1.0);

            // Show congratulatory message
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
            });

            // Optional: Add a subtle highlight animation to draw attention
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
            timeline.setCycleCount(2); // Pulse twice
            timeline.play();
        }
    }

    /**
     * Get the next sub-step based on current step and sub-step
     */
    private String getNextSubStep(int step, String currentSub) {
        // Define the progression for each step
        String[][] stepProgression = {
                {"a", "b", "c", "d", "e"},  // Step 1: a->b->c->d->e
                {"a", "b", "c", "d"},        // Step 2: a->b->c->d
                {"a", "b", "c", "d"},        // Step 3: a->b->c->d
                {"a", "b", "c"}              // Step 4: a->b->c
        };

        if (step < 1 || step > stepProgression.length) return null;

        String[] progression = stepProgression[step - 1];
        for (int i = 0; i < progression.length - 1; i++) {
            if (progression[i].equals(currentSub)) {
                return progression[i + 1];
            }
        }

        return null; // Last sub-step of current step
    }

    /**
     * Get step index for tracking completion
     */
    private int getStepIndex(int step, String subStep) {
        int baseIndex = 0;

        // Calculate base index based on previous steps
        if (step == 2) baseIndex = 5;      // Step 1 has 5 sub-steps (a-e)
        else if (step == 3) baseIndex = 9;  // Step 1: 5 + Step 2: 4
        else if (step == 4) baseIndex = 13; // Step 1: 5 + Step 2: 4 + Step 3: 4

        // Add sub-step offset
        int subIndex = subStep.charAt(0) - 'a';
        return baseIndex + subIndex;
    }

    /**
     * Check if a specific step is already completed
     * Used to prevent users from skipping steps
     */
    public boolean isStepCompleted(int step, String subStep) {
        String stepKey = step + "_" + subStep;
        return stepCompletionMap.getOrDefault(stepKey, false);
    }

    /**
     * Check if current step allows the user to perform a specific action
     * Returns true only if we're on that step or it's already completed
     */
    public boolean canPerformAction(int step, String subStep) {
        // Can perform if it's the current active step OR if it's already completed
        return (activeStep == step && activeSubStep.equals(subStep)) ||
                isStepCompleted(step, subStep);
    }

    /**
     * Get current active step number
     */
    public int getActiveStep() {
        return activeStep;
    }

    /**
     * Get current active sub-step
     */
    public String getActiveSubStep() {
        return activeSubStep;
    }
}