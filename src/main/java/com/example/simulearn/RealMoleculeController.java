//
//package com.example.simulearn;
//
//import javafx.event.ActionEvent;
//import javafx.fxml.FXML;
//import javafx.fxml.FXMLLoader;
//import javafx.fxml.Initializable;
//import javafx.geometry.Insets;
//import javafx.geometry.Point3D;
//import javafx.geometry.Pos;
//import javafx.scene.*;
//import javafx.scene.control.*;
//import javafx.scene.image.ImageView;
//import javafx.scene.input.MouseEvent;
//import javafx.scene.layout.*;
//import javafx.scene.paint.Color;
//import javafx.scene.paint.PhongMaterial;
//import javafx.scene.shape.*;
//import javafx.scene.text.Font;
//import javafx.scene.text.FontWeight;
//import javafx.scene.transform.Rotate;
//import javafx.stage.Stage;
//
//import java.net.URL;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.ResourceBundle;
//
//public class RealMoleculeController implements Initializable {
//
//    @FXML
//    private void onBackButtonClicked(ActionEvent event) {
//        try {
//            // Load FXML correctly
//            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/simulearn/chemistryMenu.fxml"));
//            Parent root = loader.load();
//            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
//            stage.setScene(new Scene(root));
//            stage.setMaximized(true);
//            stage.show();
//        } catch (java.io.IOException e) {
//            System.out.println("Failed to open the window.");
//        }
//    }
//
//
//    @FXML
//    private VBox mainpanel;
//    @FXML
//    private VBox instruction;
//
//    private Group moleculeGroup;
//    private Rotate rotateX = new Rotate(0, Rotate.X_AXIS);
//    private Rotate rotateY = new Rotate(0, Rotate.Y_AXIS);
//    private double anchorX, anchorY;
//    private double anchorAngleX = 0;
//    private double anchorAngleY = 0;
//
//    private int selectedBondType = 1;
//    private boolean showBondAngles = false;
//
//    private Molecule molecule;
//    private SubScene subscene;
//    private StackPane view3DContainer; // Container for 3D view and overlays
//
//    private Button btnContext, btnLaunch, btnReflection;
//    private Button selectedButton = null;
//
//    private ScrollPane scrollPane;
//    private VBox buttonPanel;
//
//    // For geometry info updates
//    private Label geometryInfoLabel;
//    private Label bondAngleLabel;
//    private Label electronGeometryLabel;
//    private Label molecularGeometryLabel;
//
//    // Molecule selector dropdown
//    private ComboBox<String> moleculeSelector;
//
//    // IMAGE PATHS - Replace these with your actual image file paths
//    private static final String LINEAR_GEOMETRY_IMAGE = "/Images/RealMolecule/lineargeometry.jpg";
//    private static final String TRIGONAL_PLANAR_IMAGE = "/Images/RealMolecule/trigonalplanar.jpg";
//    private static final String TETRAHEDRAL_IMAGE = "/Images/RealMolecule/tetrahedralgeometry.jpg";
//    private static final String TRIGONAL_PYRAMIDAL_IMAGE = "/Images/RealMolecule/trigonalpyramidal.jpg";
//
//    @Override
//    public void initialize(URL location, ResourceBundle resources) {
//        molecule = new Molecule();
//
//        setupLayout();
//        setup3DView();
//
//        updateMolecule();
//    }
//
//    private void setupLayout() {
//        scrollPane = new ScrollPane();
//        scrollPane.setPrefWidth(800);
//        scrollPane.setMaxWidth(Region.USE_PREF_SIZE);
//        scrollPane.setFitToWidth(true);
//        scrollPane.setStyle(
//                "-fx-background: white;" +
//                        "-fx-background-color: white;" +
//                        "-fx-border-color: #cccccc;" +
//                        "-fx-border-radius: 8;" +
//                        "-fx-background-radius: 8;");
//        VBox.setVgrow(scrollPane, Priority.ALWAYS);
//
//        instruction.setPadding(new Insets(20));
//        instruction.setStyle(
//                "-fx-background-color: #2a2a2a;" +
//                        "-fx-border-color: #3a3a3a;" +
//                        "-fx-border-radius: 8;" +
//                        "-fx-background-radius: 8;");
//        instruction.setPrefWidth(380);
//        instruction.setMinWidth(380);
//
//        buttonPanel = createButtonPanel();
//        instruction.getChildren().add(buttonPanel);
//
//        mainpanel.setAlignment(Pos.TOP_RIGHT);
//        mainpanel.getChildren().add(scrollPane);
//
//        updateScrollContent(getContextContent());
//    }
//
//    private VBox createButtonPanel() {
//        VBox panel = new VBox(0);
//        panel.setAlignment(Pos.TOP_LEFT);
//        panel.setPadding(new Insets(10));
//
//        btnContext = createNavButton("1", "CONTEXT");
//        btnLaunch = createNavButton("2", "LAUNCH");
//        btnReflection = createNavButton("3", "REFLECTION");
//
//        btnContext.setOnAction(e -> {
//            setSelectedButton(btnContext);
//            showContextMode();
//        });
//
//        btnLaunch.setOnAction(e -> {
//            setSelectedButton(btnLaunch);
//            showLaunchMode();
//        });
//
//        btnReflection.setOnAction(e -> {
//            setSelectedButton(btnReflection);
//            showReflectionMode();
//        });
//
//        panel.getChildren().addAll(btnContext, btnLaunch, btnReflection);
//
//        setSelectedButton(btnContext);
//
//        return panel;
//    }
//
//    private Button createNavButton(String number, String title) {
//        VBox buttonContent = new VBox(8);
//        buttonContent.setAlignment(Pos.CENTER);
//        buttonContent.setPadding(new Insets(20, 15, 20, 15));
//
//        Circle circle = new Circle(28);
//        circle.setFill(Color.web("#1e1e1e"));
//        circle.setStroke(Color.web("#4a4a4a"));
//        circle.setStrokeWidth(2);
//
//        Label numLabel = new Label(number);
//        numLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 20));
//        numLabel.setStyle("-fx-text-fill: #000000;");
//
//        StackPane circleStack = new StackPane(circle, numLabel);
//
//        Label titleLabel = new Label(title);
//        titleLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
//        titleLabel.setStyle("-fx-text-fill: #000000;");
//
//        buttonContent.getChildren().addAll(circleStack, titleLabel);
//
//        Button button = new Button();
//        button.setGraphic(buttonContent);
//        button.setMaxWidth(Double.MAX_VALUE);
//        button.setStyle(
//                "-fx-background-color: #1e1e1e;" +
//                        "-fx-background-radius: 10;" +
//                        "-fx-border-color: #3a3a3a;" +
//                        "-fx-border-radius: 10;" +
//                        "-fx-border-width: 2;" +
//                        "-fx-cursor: hand;" +
//                        "-fx-padding: 0;");
//
//        button.setOnMouseEntered(e -> {
//            if (selectedButton != button) {
//                button.setStyle(
//                        "-fx-background-color: #2a2a2a;" +
//                                "-fx-background-radius: 10;" +
//                                "-fx-border-color: #4a4a4a;" +
//                                "-fx-border-radius: 10;" +
//                                "-fx-border-width: 2;" +
//                                "-fx-cursor: hand;" +
//                                "-fx-padding: 0;");
//            }
//        });
//
//        button.setOnMouseExited(e -> {
//            if (selectedButton != button) {
//                button.setStyle(
//                        "-fx-background-color: #1e1e1e;" +
//                                "-fx-background-radius: 10;" +
//                                "-fx-border-color: #3a3a3a;" +
//                                "-fx-border-radius: 10;" +
//                                "-fx-border-width: 2;" +
//                                "-fx-cursor: hand;" +
//                                "-fx-padding: 0;");
//            }
//        });
//
//        VBox.setMargin(button, new Insets(0, 0, 15, 0));
//
//        return button;
//    }
//
//    private void setSelectedButton(Button button) {
//        if (selectedButton != null) {
//            selectedButton.setStyle(
//                    "-fx-background-color: #1e1e1e;" +
//                            "-fx-background-radius: 10;" +
//                            "-fx-border-color: #3a3a3a;" +
//                            "-fx-border-radius: 10;" +
//                            "-fx-border-width: 2;" +
//                            "-fx-cursor: hand;" +
//                            "-fx-padding: 0;");
//        }
//
//        selectedButton = button;
//        button.setStyle(
//                "-fx-background-color: #0d7377;" +
//                        "-fx-background-radius: 10;" +
//                        "-fx-border-color: #14a6ac;" +
//                        "-fx-border-radius: 10;" +
//                        "-fx-border-width: 3;" +
//                        "-fx-cursor: hand;" +
//                        "-fx-padding: 0;");
//    }
//
//    private void showContextMode() {
//        // Show scrollpane in mainpanel
//        mainpanel.getChildren().clear();
//        mainpanel.getChildren().add(scrollPane);
//        scrollPane.setVisible(true);
//        scrollPane.setManaged(true);
//        VBox.setVgrow(scrollPane, Priority.ALWAYS);
//
//        // Reset instruction panel to just buttons
//        instruction.getChildren().clear();
//        instruction.getChildren().add(buttonPanel);
//
//        updateScrollContent(getContextContent());
//    }
//
//    private void showLaunchMode() {
//        // Hide scrollpane, show 3D view
//        scrollPane.setVisible(false);
//        scrollPane.setManaged(false);
//
//        mainpanel.getChildren().clear();
//
//        view3DContainer = new StackPane();
//        view3DContainer.setStyle("-fx-background-color: #0d0d0d;");
//        VBox.setVgrow(view3DContainer, Priority.ALWAYS);
//
//        if (subscene != null) {
//            subscene.widthProperty().bind(view3DContainer.widthProperty());
//            subscene.heightProperty().bind(view3DContainer.heightProperty());
//            view3DContainer.getChildren().add(subscene);
//        }
//
//        mainpanel.getChildren().add(view3DContainer);
//
//        // REPLACE instruction panel with controls
//        createLaunchControlPanel();
//    }
//
//    private void showReflectionMode() {
//        mainpanel.getChildren().clear();
//        mainpanel.getChildren().add(scrollPane);
//        scrollPane.setVisible(true);
//        scrollPane.setManaged(true);
//        VBox.setVgrow(scrollPane, Priority.ALWAYS);
//
//        instruction.getChildren().clear();
//        instruction.getChildren().add(buttonPanel);
//
//        updateScrollContent(getReflectionContent());
//    }
//
//    private void createLaunchControlPanel() {
//        // CLEAR and REBUILD instruction panel
//        instruction.getChildren().clear();
//
//        // Add button panel first
//        instruction.getChildren().add(buttonPanel);
//
//        // Molecule Selector Panel
//        VBox moleculeSelectorPanel = createControlPanel("Select Molecule");
//
//        moleculeSelector = new ComboBox<>();
//        moleculeSelector.getItems().addAll(
//                "Custom (Build Your Own)",
//                "H₂O - Water",
//                "CO₂ - Carbon Dioxide",
//                "NH₃ - Ammonia",
//                "CH₄ - Methane",
//                "BF₃ - Boron Trifluoride",
//                "SF₆ - Sulfur Hexafluoride",
//                "SO₂ - Sulfur Dioxide",
//                "XeF₂ - Xenon Difluoride",
//                "ClF₃ - Chlorine Trifluoride",
//                "XeF₄ - Xenon Tetrafluoride",
//                "BrF₅ - Bromine Pentafluoride",
//                "PCl₅ - Phosphorus Pentachloride");
//        moleculeSelector.setValue("Custom (Build Your Own)");
//        moleculeSelector.setMaxWidth(Double.MAX_VALUE);
//        moleculeSelector.setStyle(
//                "-fx-background-color: #1e1e1e;" +
//                        "-fx-text-fill: #000000;" +
//                        "-fx-font-size: 14px;" +
//                        "-fx-padding: 12;" +
//                        "-fx-background-radius: 8;" +
//                        "-fx-border-color: #3a3a3a;" +
//                        "-fx-border-radius: 8;" +
//                        "-fx-border-width: 1;");
//
//        moleculeSelector.setOnAction(e -> loadPresetMolecule(moleculeSelector.getValue()));
//
//        moleculeSelectorPanel.getChildren().add(moleculeSelector);
//
//        // Bonding Panel
//        VBox bondingPanel = createControlPanel("Bonding");
//
//        VBox bondsContainer = new VBox(8);
//
//        Button singleBondBtn = createBondButton("Single", 1);
//        Button doubleBondBtn = createBondButton("Double", 2);
//        Button tripleBondBtn = createBondButton("Triple", 3);
//
//        bondsContainer.getChildren().addAll(singleBondBtn, doubleBondBtn, tripleBondBtn);
//        bondingPanel.getChildren().add(bondsContainer);
//
//        // Lone Pair Panel
//        VBox lonePairPanel = createControlPanel("Lone Pair");
//
//        Button lpButton = new Button();
//        lpButton.setMaxWidth(Double.MAX_VALUE);
//
//        HBox lpContent = new HBox(12);
//        lpContent.setAlignment(Pos.CENTER_LEFT);
//        lpContent.setPadding(new Insets(12, 15, 12, 15));
//
//        // Lone pair visual
//        StackPane lpVisual = new StackPane();
//        lpVisual.setPrefSize(50, 30);
//
//        Ellipse e1 = new Ellipse(12, 8);
//        e1.setFill(Color.web("#888888"));
//        e1.setTranslateX(-8);
//
//        Ellipse e2 = new Ellipse(12, 8);
//        e2.setFill(Color.web("#888888"));
//        e2.setTranslateX(8);
//
//        lpVisual.getChildren().addAll(e1, e2);
//
//        Label lpLabel = new Label("Add Lone Pair");
//        lpLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 13));
//        lpLabel.setTextFill(Color.web("#ffffff"));
//
//        lpContent.getChildren().addAll(lpVisual, lpLabel);
//        lpButton.setGraphic(lpContent);
//
//        lpButton.setStyle(
//                "-fx-background-color: #1e1e1e;" +
//                        "-fx-background-radius: 8;" +
//                        "-fx-border-color: #3a3a3a;" +
//                        "-fx-border-width: 1;" +
//                        "-fx-border-radius: 8;" +
//                        "-fx-cursor: hand;");
//
//        // Add click action to directly add lone pair
//        lpButton.setOnAction(e -> {
//            molecule.addLonePair();
//            updateMolecule();
//            System.out.println("Added lone pair");
//        });
//
//        // Hover effects
//        lpButton.setOnMouseEntered(e -> {
//            lpButton.setStyle(
//                    "-fx-background-color: #0d3436;" +
//                            "-fx-background-radius: 8;" +
//                            "-fx-border-color: #14a6ac;" +
//                            "-fx-border-width: 2;" +
//                            "-fx-border-radius: 8;" +
//                            "-fx-cursor: hand;");
//        });
//
//        lpButton.setOnMouseExited(e -> {
//            lpButton.setStyle(
//                    "-fx-background-color: #1e1e1e;" +
//                            "-fx-background-radius: 8;" +
//                            "-fx-border-color: #3a3a3a;" +
//                            "-fx-border-width: 1;" +
//                            "-fx-border-radius: 8;" +
//                            "-fx-cursor: hand;");
//        });
//
//        lonePairPanel.getChildren().add(lpButton);
//
//        // Remove All Button
//        Button removeAllBtn = new Button("Remove All");
//        removeAllBtn.setMaxWidth(Double.MAX_VALUE);
//        removeAllBtn.setFont(Font.font("Segoe UI", FontWeight.BOLD, 13));
//        removeAllBtn.setPadding(new Insets(12));
//        removeAllBtn.setStyle(
//                "-fx-background-color: #d4a00c;" +
//                        "-fx-text-fill: white;" +
//                        "-fx-background-radius: 8;" +
//                        "-fx-cursor: hand;");
//        removeAllBtn.setOnAction(e -> {
//            molecule.clear();
//            updateMolecule();
//        });
//        removeAllBtn.setOnMouseEntered(e -> removeAllBtn.setOpacity(0.85));
//        removeAllBtn.setOnMouseExited(e -> removeAllBtn.setOpacity(1.0));
//
//        // Remove Last Button
//        Button removeLastBtn = new Button("Remove Last");
//        removeLastBtn.setFont(Font.font("Segoe UI", FontWeight.BOLD, 13));
//        removeLastBtn.setMaxWidth(Double.MAX_VALUE);
//        removeLastBtn.setStyle(
//                "-fx-background-color: #c44536;" +
//                        "-fx-text-fill: white;" +
//                        "-fx-background-radius: 8;" +
//                        "-fx-padding: 12 20;" +
//                        "-fx-cursor: hand;");
//        removeLastBtn.setOnAction(e -> {
//            molecule.removeLast();
//            updateMolecule();
//        });
//        removeLastBtn.setOnMouseEntered(e -> removeLastBtn.setOpacity(0.85));
//        removeLastBtn.setOnMouseExited(e -> removeLastBtn.setOpacity(1.0));
//
//        // Options Panel
//        VBox optionsPanel = createControlPanel("Options");
//
//        CheckBox showLpCheck = new CheckBox("Show Lone Pairs");
//        showLpCheck.setSelected(true);
//        showLpCheck.setFont(Font.font("Segoe UI", 13));
//        showLpCheck.setTextFill(Color.web("#ffffff"));
//
//        CheckBox showAnglesCheck = new CheckBox("Show Bond Angles");
//        showAnglesCheck.setSelected(false);
//        showAnglesCheck.setFont(Font.font("Segoe UI", 13));
//        showAnglesCheck.setTextFill(Color.web("#ffffff"));
//        showAnglesCheck.setOnAction(e -> {
//            showBondAngles = showAnglesCheck.isSelected();
//            updateMolecule();
//        });
//
//        VBox optionsBox = new VBox(10, showLpCheck, showAnglesCheck);
//        optionsBox.setPadding(new Insets(5));
//        optionsPanel.getChildren().add(optionsBox);
//
//        // Name Panel (Electron/Molecule Geometry)
//        VBox namePanel = createControlPanel("Name");
//
//        HBox nameBox = new HBox(15);
//        nameBox.setAlignment(Pos.CENTER_LEFT);
//        nameBox.setPadding(Insets.EMPTY);
//        nameBox.setStyle(
//                "-fx-background-color: transparent;");
//
//        // Create VBox for geometry labels
//        VBox geomLabels = new VBox(12);
//        geomLabels.setPadding(new Insets(8, 12, 8, 12));
//        geomLabels.setStyle(
//                "-fx-background-color: #1e1e1e;" +
//                        "-fx-background-radius: 8;" +
//                        "-fx-border-color: #3a3a3a;" +
//                        "-fx-border-width: 1;" +
//                        "-fx-border-radius: 8;");
//
//        // Electron Geometry Label
//        VBox electronBox = new VBox(4);
//        Label electronTitle = new Label("Electron Geometry");
//        electronTitle.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 11));
//        electronTitle.setTextFill(Color.web("#888888"));
//
//        electronGeometryLabel = new Label("None");
//        electronGeometryLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 13));
//        electronGeometryLabel.setTextFill(Color.web("#f4d03f"));
//        electronGeometryLabel.setWrapText(true);
//
//        electronBox.getChildren().addAll(electronTitle, electronGeometryLabel);
//
//        // Molecular Geometry Label
//        VBox molecularBox = new VBox(4);
//        Label molecularTitle = new Label("Molecule Geometry");
//        molecularTitle.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 11));
//        molecularTitle.setTextFill(Color.web("#888888"));
//
//        molecularGeometryLabel = new Label("None");
//        molecularGeometryLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 13));
//        molecularGeometryLabel.setTextFill(Color.web("#ffffff"));
//        molecularGeometryLabel.setWrapText(true);
//
//        molecularBox.getChildren().addAll(molecularTitle, molecularGeometryLabel);
//
//        geomLabels.getChildren().addAll(electronBox, molecularBox);
//        nameBox.getChildren().add(geomLabels);
//        namePanel.getChildren().add(nameBox);
//
//        // Geometry Info Display
//        VBox infoDisplay = new VBox(8);
//        infoDisplay.setPadding(new Insets(10));
//        infoDisplay.setStyle(
//                "-fx-background-color: #1a1a1a;" +
//                        "-fx-background-radius: 8;" +
//                        "-fx-border-color: #0d7377;" +
//                        "-fx-border-width: 2;" +
//                        "-fx-border-radius: 8;");
//
//        geometryInfoLabel = new Label("Shape: None");
//        geometryInfoLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
//        geometryInfoLabel.setTextFill(Color.web("#14a6ac"));
//        geometryInfoLabel.setWrapText(true);
//
//        bondAngleLabel = new Label("Angles: N/A");
//        bondAngleLabel.setFont(Font.font("Segoe UI", 13));
//        bondAngleLabel.setTextFill(Color.web("#ffffff"));
//        bondAngleLabel.setWrapText(true);
//
//        infoDisplay.getChildren().addAll(geometryInfoLabel, bondAngleLabel);
//
//        // Add all to instruction panel
//        instruction.getChildren().addAll(
//                moleculeSelectorPanel,
//                bondingPanel,
//                lonePairPanel,
//                removeAllBtn,
//                removeLastBtn,
//                optionsPanel,
//                namePanel,
//                infoDisplay);
//
//        VBox.setMargin(moleculeSelectorPanel, new Insets(0, 0, 10, 0));
//        VBox.setMargin(removeAllBtn, new Insets(5, 0, 5, 0));
//        VBox.setMargin(removeLastBtn, new Insets(5, 0, 5, 0));
//
//        updateMolecule();
//    }
//
//    private VBox createControlPanel(String title) {
//        VBox panel = new VBox(10);
//        panel.setPadding(new Insets(12));
//        panel.setStyle(
//                "-fx-background-color: transparent;" +
//                        "-fx-border-color: #4a4a4a;" +
//                        "-fx-border-width: 1.5;" +
//                        "-fx-border-radius: 10;" +
//                        "-fx-background-radius: 10;");
//
//        Label titleLabel = new Label(title);
//        titleLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
//        titleLabel.setTextFill(Color.web("#ffffff"));
//
//        panel.getChildren().add(titleLabel);
//
//        return panel;
//    }
//
//    private Button createBondButton(String label, int bondType) {
//        Button button = new Button();
//        button.setMaxWidth(Double.MAX_VALUE);
//
//        HBox content = new HBox(12);
//        content.setAlignment(Pos.CENTER_LEFT);
//        content.setPadding(new Insets(12, 15, 12, 15));
//
//        // Bond visual
//        int bondCount = label.equals("Single") ? 1 : label.equals("Double") ? 2 : 3;
//        StackPane bondVisual = new StackPane();
//        bondVisual.setPrefSize(60, 30);
//
//        for (int i = 0; i < bondCount; i++) {
//            Line line = new Line(0, 0, 50, 0);
//            line.setStroke(Color.web("#cccccc"));
//            line.setStrokeWidth(3);
//
//            if (bondCount > 1) {
//                line.setTranslateY((i - (bondCount - 1) / 2.0) * 6);
//            }
//            bondVisual.getChildren().add(line);
//        }
//
//        Circle c1 = new Circle(8, Color.web("#999999"));
//        c1.setTranslateX(-25);
//        Circle c2 = new Circle(8, Color.web("#999999"));
//        c2.setTranslateX(25);
//
//        bondVisual.getChildren().addAll(c1, c2);
//
//        Label bondLabel = new Label("Add " + label + " Bond");
//        bondLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 13));
//        bondLabel.setTextFill(Color.web("#ffffff"));
//
//        content.getChildren().addAll(bondVisual, bondLabel);
//        button.setGraphic(content);
//
//        button.setStyle(
//                "-fx-background-color: #1e1e1e;" +
//                        "-fx-background-radius: 8;" +
//                        "-fx-border-color: #3a3a3a;" +
//                        "-fx-border-width: 1;" +
//                        "-fx-border-radius: 8;" +
//                        "-fx-cursor: hand;");
//
//        // Add click action to directly add atom with this bond type
//        button.setOnAction(e -> {
//            molecule.addAtom("H", bondType);
//            updateMolecule();
//            System.out.println("Added H atom with " + label.toLowerCase() + " bond (type " + bondType + ")");
//        });
//
//        // Hover effects
//        button.setOnMouseEntered(e -> {
//            button.setStyle(
//                    "-fx-background-color: #0d3436;" +
//                            "-fx-background-radius: 8;" +
//                            "-fx-border-color: #14a6ac;" +
//                            "-fx-border-width: 2;" +
//                            "-fx-border-radius: 8;" +
//                            "-fx-cursor: hand;");
//        });
//
//        button.setOnMouseExited(e -> {
//            button.setStyle(
//                    "-fx-background-color: #1e1e1e;" +
//                            "-fx-background-radius: 8;" +
//                            "-fx-border-color: #3a3a3a;" +
//                            "-fx-border-width: 1;" +
//                            "-fx-border-radius: 8;" +
//                            "-fx-cursor: hand;");
//        });
//
//        return button;
//    }
//
//    private void updateScrollContent(VBox content) {
//        scrollPane.setContent(content);
//    }
//
//    private VBox getContextContent() {
//        VBox content = new VBox(25);
//        content.setPadding(new Insets(40));
//        content.setStyle("-fx-background-color: white;");
//
//        Label title = new Label("1. CONTEXT");
//        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 32));
//        title.setStyle("-fx-text-fill: #0d7377;");
//
//        Separator underline = new Separator();
//        underline.setMaxWidth(200);
//        underline.setStyle("-fx-background-color: #0d7377;");
//        underline.setPrefHeight(3);
//
//        // Introduction paragraph
//        Label introText = new Label(
//                "When we look at Lewis structures, we are only seeing a two-dimensional picture. To get the full picture of "
//                        +
//                        "molecules, we need to find ways to represent those atoms in three dimensions. Why? Because the shape, or "
//                        +
//                        "geometry, of a molecule helps us understand how that molecule will behave. In fact, a molecule's geometry "
//                        +
//                        "affects its physical interaction with other molecules, determines which region of the molecule is likely to "
//                        +
//                        "undergo chemical changes, influences properties like melting point and density, and influences the products "
//                        +
//                        "that will form when it reacts with different substances. So let's take a look at how we can determine the "
//                        +
//                        "shapes of molecules.");
//        introText.setWrapText(true);
//        introText.setFont(Font.font("Segoe UI", 15));
//        introText.setLineSpacing(3);
//        introText.setStyle("-fx-text-fill: #333333;");
//
//        // Linear Geometry Section
//        Label linearHeading = new Label("Linear Geometry");
//        linearHeading.setFont(Font.font("Segoe UI", FontWeight.BOLD, 20));
//        linearHeading.setStyle("-fx-text-fill: #000000;");
//        linearHeading.setPadding(new Insets(20, 0, 10, 0));
//
//        // Try to load linear geometry image
//        VBox linearImageBox = createImageBox(LINEAR_GEOMETRY_IMAGE,
//                "Figure 5-12. Linear Geometry\n" +
//                        "In carbon dioxide, there is a central atom with two repelling regions of electron density (the electrons in "
//                        +
//                        "the bonds). This forms a bond angle of 180°.");
//
//        // VSEPR Theory Section
//        Label vseprHeading = new Label("Valence Shell Electron Pair Repulsion Theory");
//        vseprHeading.setFont(Font.font("Segoe UI", FontWeight.BOLD, 20));
//        vseprHeading.setStyle("-fx-text-fill: #000000;");
//        vseprHeading.setPadding(new Insets(20, 0, 10, 0));
//
//        Label vseprText = new Label(
//                "There is a simple model that allows us to predict, based on the Lewis structure, a molecule's three-" +
//                        "dimensional geometry. It is called the Valence Shell Electron Pair Repulsion theory, or VSEPR (pronounced "
//                        +
//                        "\"vesper\"). In this model, we look at the arrangement of atoms and lone pairs around each atom individually. "
//                        +
//                        "We call the atom under consideration the central atom and the other atoms or lone pairs attached to it "
//                        +
//                        "the substituents of that atom. We consider each atom one at a time and then combine them to get the full "
//                        +
//                        "picture.\n\n" +
//                        "The way VSEPR theory works is that we imagine that the electrons in the bonds or lone pairs desperately "
//                        +
//                        "want to get as far away from each other as they can since all are negatively charged. But, since they are "
//                        +
//                        "bound to the central atom, the best they can do is distribute themselves around the central atom with "
//                        +
//                        "the angles between the atoms or lone pairs as large as possible. Imagine a central atom with bonds to two "
//                        +
//                        "other things, for example the carbon in carbon dioxide, which is bound to two oxygens (Figure 5-12). "
//                        +
//                        "The repelling electrons in the bonds will move to opposite sides of the central atom until they are 180° "
//                        +
//                        "away from each other. We define the bond angle as the angle between the bonds to the central atom. "
//                        +
//                        "When atoms arrange themselves in a straight line like this, we call it \"linear geometry.\"");
//        vseprText.setWrapText(true);
//        vseprText.setFont(Font.font("Segoe UI", 15));
//        vseprText.setLineSpacing(3);
//        vseprText.setStyle("-fx-text-fill: #333333;");
//
//        // Trigonal Planar Section
//        Label trigonalHeading = new Label("Trigonal Planar Geometry");
//        trigonalHeading.setFont(Font.font("Segoe UI", FontWeight.BOLD, 20));
//        trigonalHeading.setStyle("-fx-text-fill: #000000;");
//        trigonalHeading.setPadding(new Insets(20, 0, 10, 0));
//
//        VBox trigonalImageBox = createImageBox(TRIGONAL_PLANAR_IMAGE,
//                "Figure 5-13. Trigonal Planar Geometry\n" +
//                        "In sulfur trioxide, SO₃, three repelling substituents form an equilateral triangle in a " +
//                        "plane around the central atom. Each bond angle is 120°.");
//
//        Label trigonalText = new Label(
//                "If there are three substituents around the central atom, as in sulfur trioxide (SO₃), the farthest they can "
//                        +
//                        "get from each other while still remaining bound to the central atom is to form a triangle in a plane around it, a "
//                        +
//                        "geometry called \"trigonal planar.\" The bond angles here are all 120°, as expected for an equilateral triangle.");
//        trigonalText.setWrapText(true);
//        trigonalText.setFont(Font.font("Segoe UI", 15));
//        trigonalText.setLineSpacing(3);
//        trigonalText.setStyle("-fx-text-fill: #333333;");
//
//        // Tetrahedral Section
//        Label tetrahedralHeading = new Label("Tetrahedral Geometry");
//        tetrahedralHeading.setFont(Font.font("Segoe UI", FontWeight.BOLD, 20));
//        tetrahedralHeading.setStyle("-fx-text-fill: #000000;");
//        tetrahedralHeading.setPadding(new Insets(20, 0, 10, 0));
//
//        VBox tetrahedralImageBox = createImageBox(TETRAHEDRAL_IMAGE,
//                "Figure 5-14. Tetrahedral Geometry\n" +
//                        "In methane, CH₄, the four mutually repelling bonds adopt a symmetric distribution around the central "
//                        +
//                        "carbon, a geometry known as tetrahedral. The angle that separates each of the bonds is 109.5°.");
//
//        Label tetrahedralText = new Label(
//                "If there are four substituents repelling each other, the resulting geometry is called \"tetrahedral,\" as shown "
//                        +
//                        "in Figure 5-14, and each of the bond angles is approximately 109.5°. Even though the Lewis structure makes "
//                        +
//                        "any molecule seem flat, by combining the Lewis structure with VSEPR theory, we can understand and determine "
//                        +
//                        "the 3-D structures of molecules.");
//        tetrahedralText.setWrapText(true);
//        tetrahedralText.setFont(Font.font("Segoe UI", 15));
//        tetrahedralText.setLineSpacing(3);
//        tetrahedralText.setStyle("-fx-text-fill: #333333;");
//
//        // Molecular Geometry vs Electron Geometry
//        Label molecularHeading = new Label("Molecular Geometry: Accounting for Lone Pairs");
//        molecularHeading.setFont(Font.font("Segoe UI", FontWeight.BOLD, 20));
//        molecularHeading.setStyle("-fx-text-fill: #000000;");
//        molecularHeading.setPadding(new Insets(20, 0, 10, 0));
//
//        Label molecularText = new Label(
//                "When describing molecular geometry, we deduce the shape by looking at the distribution of electrons around "
//                        +
//                        "the central atom, including not just atoms but also lone electron pairs. But our description of the molecule "
//                        +
//                        "depends only on the position of atomic nuclei. This means a molecule like ammonia, which has four repulsive "
//                        +
//                        "regions of electron density (three bonds and one lone pair), has the geometry shown in Figure 5-15. The "
//                        +
//                        "substituents arrange themselves tetrahedrally, just as we saw with methane. But when naming the molecular shape, "
//                        +
//                        "we consider only the positions of the atoms, not the lone pairs; so rather than being tetrahedral, we would refer "
//                        +
//                        "to this molecule as being trigonal pyramidal.");
//        molecularText.setWrapText(true);
//        molecularText.setFont(Font.font("Segoe UI", 15));
//        molecularText.setLineSpacing(3);
//        molecularText.setStyle("-fx-text-fill: #333333;");
//
//        VBox pyramidalImageBox = createImageBox(TRIGONAL_PYRAMIDAL_IMAGE,
//                "Figure 5-15. Trigonal Pyramidal Geometry\n" +
//                        "In ammonia (NH₃), the three hydrogens and one lone pair adopt a tetrahedral arrangement to avoid "
//                        +
//                        "electron-electron repulsion. Since we cannot see the lone pair, the geometry gets called \"trigonal pyramidal.\"");
//
//        content.getChildren().addAll(
//                title, underline,
//                introText,
//                linearHeading, linearImageBox,
//                vseprHeading, vseprText,
//                trigonalHeading, trigonalImageBox, trigonalText,
//                tetrahedralHeading, tetrahedralImageBox, tetrahedralText,
//                molecularHeading, molecularText, pyramidalImageBox);
//
//        return content;
//    }
//
//    private VBox createImageBox(String imageFilePath, String caption) {
//        VBox imageBox = new VBox(10);
//        imageBox.setAlignment(Pos.CENTER);
//        imageBox.setPadding(new Insets(15));
//        imageBox.setStyle(
//                "-fx-background-color: #f5f5f5;" +
//                        "-fx-border-color: #dddddd;" +
//                        "-fx-border-radius: 8;" +
//                        "-fx-background-radius: 8;");
//
//        ImageView image= new ImageView(getClass().getResource(imageFilePath).toExternalForm());
//
//        // Image container with styling
//        StackPane imageContainer = new StackPane(image);
//        imageContainer.setStyle("-fx-background-color: white;" + "-fx-border-color: #d0d0d0;" + "-fx-border-width: 1;" + "-fx-border-radius: 9;" + "-fx-background-radius: 5;" + "-fx-padding: 10;" + "-fx-effect: dropshadow(three-pass-box, rgba(60, 64, 67, 0.3), 15, 0, 0, 6);");
//        imageContainer.setMaxWidth(600);
//        imageContainer.setPrefHeight(300);
//        imageContainer.setPadding(new Insets(10, 0, 10, 0));
//
//        // Add caption
//        Label captionLabel = new Label(caption);
//        captionLabel.setWrapText(true);
//        captionLabel.setMaxWidth(600);
//        captionLabel.setFont(Font.font("Segoe UI", 13));
//        captionLabel.setStyle("-fx-text-fill: #666666;");
//        captionLabel.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
//        imageBox.getChildren().addAll(imageContainer,captionLabel);
//
//        return imageBox;
//    }
//
//    private VBox createGeometryTable() {
//        VBox table = new VBox(10);
//        table.setStyle(
//                "-fx-background-color: #2a2a2a;" +
//                        "-fx-border-color: #3a3a3a;" +
//                        "-fx-border-radius: 8;" +
//                        "-fx-background-radius: 8;" +
//                        "-fx-padding: 20;");
//
//        addGeometryRow(table, "Linear", "2", "180°", "CO₂, BeCl₂");
//        addGeometryRow(table, "Trigonal Planar", "3", "120°", "BF₃, SO₃");
//        addGeometryRow(table, "Tetrahedral", "4", "109.5°", "CH₄, NH₄⁺");
//        addGeometryRow(table, "Trigonal Pyramidal", "4 (1 LP)", "~107°", "NH₃, PCl₃");
//        addGeometryRow(table, "Bent", "4 (2 LP)", "~104.5°", "H₂O, H₂S");
//        addGeometryRow(table, "Trigonal Bipyramidal", "5", "90°, 120°, 180°", "PCl₅, AsF₅");
//        addGeometryRow(table, "Octahedral", "6", "90°, 180°", "SF₆, PF₆⁻");
//
//        return table;
//    }
//
//    private void addGeometryRow(VBox parent, String geometry, String pairs, String angles, String examples) {
//        HBox row = new HBox(15);
//        row.setAlignment(Pos.CENTER_LEFT);
//        row.setPadding(new Insets(10));
//        row.setStyle(
//                "-fx-background-color: #1e1e1e;" +
//                        "-fx-background-radius: 5;");
//
//        Label geoLabel = new Label(geometry);
//        geoLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
//        geoLabel.setStyle("-fx-text-fill: #14a6ac;");
//        geoLabel.setPrefWidth(180);
//
//        Label pairsLabel = new Label("Pairs: " + pairs);
//        pairsLabel.setFont(Font.font("Segoe UI", 13));
//        pairsLabel.setStyle("-fx-text-fill: #000000;");
//        pairsLabel.setPrefWidth(100);
//
//        Label anglesLabel = new Label(angles);
//        anglesLabel.setFont(Font.font("Segoe UI", 13));
//        anglesLabel.setStyle("-fx-text-fill: #999999;");
//        anglesLabel.setPrefWidth(120);
//
//        Label examplesLabel = new Label(examples);
//        examplesLabel.setFont(Font.font("Segoe UI", 13));
//        examplesLabel.setStyle("-fx-text-fill: #666666;");
//        examplesLabel.setWrapText(true);
//
//        row.getChildren().addAll(geoLabel, pairsLabel, anglesLabel, examplesLabel);
//        parent.getChildren().add(row);
//    }
//
//    private VBox getReflectionContent() {
//        VBox content = new VBox(25);
//        content.setPadding(new Insets(40));
//        content.setStyle("-fx-background-color: #1a1a1a;");
//
//        Label title = new Label("3. REFLECTION");
//        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 32));
//        title.setStyle("-fx-text-fill: #14a6ac;");
//
//        Separator underline = new Separator();
//        underline.setMaxWidth(250);
//        underline.setStyle("-fx-background-color: #0d7377;");
//        underline.setPrefHeight(3);
//
//        Label intro = new Label("Test your understanding of VSEPR theory and molecular geometry:");
//        intro.setFont(Font.font("Segoe UI", 15));
//        intro.setStyle("-fx-text-fill: #333333;");
//        intro.setWrapText(true);
//
//        QuizQuestionDark q1 = new QuizQuestionDark(
//                1,
//                "What is the bond angle in a tetrahedral molecule?",
//                new String[]{"90°", "109.5°", "120°", "180°"},
//                1,
//                "Tetrahedral geometry has bond angles of 109.5° when four electron pairs arrange to minimize repulsion.",
//                3);
//
//        QuizQuestionDark q2 = new QuizQuestionDark(
//                2,
//                "How many lone pairs does an ammonia (NH₃) molecule have?",
//                new String[]{"0", "1", "2", "3"},
//                1,
//                "Nitrogen in NH₃ has 5 valence electrons. Three form bonds with hydrogen, leaving one lone pair.",
//                3);
//
//        QuizQuestionDark q3 = new QuizQuestionDark(
//                3,
//                "What is the molecular geometry of water (H₂O)?",
//                new String[]{"Linear", "Bent", "Trigonal Planar", "Tetrahedral"},
//                1,
//                "Water has 4 electron pairs (2 bonding, 2 lone pairs) giving it a bent shape with ~104.5° angle.",
//                3);
//
//        QuizQuestionDark q4 = new QuizQuestionDark(
//                4,
//                "Which has the largest bond angle?",
//                new String[]{"NH₃ (~107°)", "H₂O (~104.5°)", "CH₄ (109.5°)", "H₂S (~92°)"},
//                2,
//                "CH₄ has perfect tetrahedral geometry with no lone pairs, giving the largest angle of 109.5°.",
//                3);
//
//        QuizQuestionDark q5 = new QuizQuestionDark(
//                5,
//                "Why do lone pairs affect bond angles?",
//                new String[]{
//                        "They don't affect bond angles",
//                        "They take up more space than bonding pairs",
//                        "They make molecules larger",
//                        "They create ionic bonds"
//                },
//                1,
//                "Lone pairs occupy more space than bonding pairs, causing greater repulsion and smaller bond angles.",
//                3);
//
//        content.getChildren().addAll(
//                title, underline, intro,
//                q1.getQuestionBox(),
//                q2.getQuestionBox(),
//                q3.getQuestionBox(),
//                q4.getQuestionBox(),
//                q5.getQuestionBox());
//
//        return content;
//    }
//
//    private void setup3DView() {
//        moleculeGroup = new Group();
//
//        PerspectiveCamera camera = new PerspectiveCamera(true);
//        camera.setTranslateZ(-500);
//        camera.setNearClip(0.1);
//        camera.setFarClip(2000);
//
//        Group root3D = new Group();
//        root3D.getChildren().add(moleculeGroup);
//        moleculeGroup.getTransforms().addAll(rotateX, rotateY);
//
//        AmbientLight ambient = new AmbientLight(Color.web("#666666"));
//        PointLight point = new PointLight(Color.WHITE);
//        point.setTranslateZ(-300);
//        root3D.getChildren().addAll(ambient, point);
//
//        subscene = new SubScene(root3D, 800, 600, true, SceneAntialiasing.BALANCED);
//        subscene.setFill(Color.web("#0d0d0d"));
//        subscene.setCamera(camera);
//
//        subscene.setOnMousePressed((MouseEvent event) -> {
//            anchorX = event.getSceneX();
//            anchorY = event.getSceneY();
//            anchorAngleX = rotateX.getAngle();
//            anchorAngleY = rotateY.getAngle();
//        });
//
//        subscene.setOnMouseDragged((MouseEvent event) -> {
//            rotateX.setAngle(anchorAngleX - (anchorY - event.getSceneY()));
//            rotateY.setAngle(anchorAngleY + anchorX - event.getSceneX());
//        });
//    }
//
//    private void updateMolecule() {
//        moleculeGroup.getChildren().clear();
//
//        Sphere centralAtom = new Sphere(20);
//        PhongMaterial centralMat = new PhongMaterial(getAtomColor(molecule.centralAtom));
//        centralMat.setSpecularColor(Color.WHITE);
//        centralAtom.setMaterial(centralMat);
//        moleculeGroup.getChildren().add(centralAtom);
//
//        VSEPRGeometry geometry = molecule.getGeometry();
//        Point3D[] positions = geometry.getPositions();
//
//        for (int i = 0; i < molecule.bondedAtoms.size(); i++) {
//            String atom = molecule.bondedAtoms.get(i);
//            int bondType = molecule.bondTypes.get(i);
//            Point3D pos = positions[i];
//
//            drawBond(Point3D.ZERO, pos, bondType);
//
//            Sphere atomSphere = new Sphere(15); // Smaller atoms
//            PhongMaterial atomMat = new PhongMaterial(getAtomColor(atom));
//            atomMat.setSpecularColor(Color.WHITE);
//            atomSphere.setMaterial(atomMat);
//            atomSphere.setTranslateX(pos.getX());
//            atomSphere.setTranslateY(pos.getY());
//            atomSphere.setTranslateZ(pos.getZ());
//            moleculeGroup.getChildren().add(atomSphere);
//
//            // Show bond angle arc if enabled and we have at least 2 bonds
//            if (showBondAngles && i > 0 && molecule.bondedAtoms.size() >= 2) {
//                Point3D prevPos = positions[i - 1];
//                double angle = calculateBondAngle(prevPos, Point3D.ZERO, pos);
//                drawBondAngleArc(prevPos, Point3D.ZERO, pos, angle);
//            }
//        }
//
//        int startIdx = molecule.bondedAtoms.size();
//        for (int i = 0; i < molecule.lonePairs; i++) {
//            Point3D pos = positions[startIdx + i];
//
//            Sphere lp1 = new Sphere(6); // Smaller lone pairs
//            Sphere lp2 = new Sphere(6);
//            PhongMaterial lpMat = new PhongMaterial(Color.web("#888888"));
//            lpMat.setSpecularColor(Color.web("#aaaaaa"));
//            lp1.setMaterial(lpMat);
//            lp2.setMaterial(lpMat);
//
//            // Better perpendicular calculation for 3D space
//            Point3D direction = pos.normalize();
//            // Find a perpendicular vector using cross product with an arbitrary vector
//            Point3D arbitrary = Math.abs(direction.getY()) < 0.9 ? new Point3D(0, 1, 0) : new Point3D(1, 0, 0);
//            Point3D perpendicular = direction.crossProduct(arbitrary).normalize().multiply(10);
//
//            lp1.setTranslateX(pos.getX() + perpendicular.getX());
//            lp1.setTranslateY(pos.getY() + perpendicular.getY());
//            lp1.setTranslateZ(pos.getZ() + perpendicular.getZ());
//
//            lp2.setTranslateX(pos.getX() - perpendicular.getX());
//            lp2.setTranslateY(pos.getY() - perpendicular.getY());
//            lp2.setTranslateZ(pos.getZ() - perpendicular.getZ());
//
//            moleculeGroup.getChildren().addAll(lp1, lp2);
//        }
//
//        updateGeometryInfo();
//    }
//
//    private void drawBond(Point3D start, Point3D end, int bondType) {
//        Point3D direction = end.subtract(start);
//        double length = direction.magnitude();
//
//        for (int i = 0; i < bondType; i++) {
//            Cylinder bond = new Cylinder(1.5, length);
//            PhongMaterial bondMat = new PhongMaterial(Color.web("#cccccc"));
//            bondMat.setSpecularColor(Color.WHITE);
//            bond.setMaterial(bondMat);
//
//            Point3D mid = start.midpoint(end);
//            bond.setTranslateX(mid.getX());
//            bond.setTranslateY(mid.getY());
//            bond.setTranslateZ(mid.getZ());
//
//            if (bondType > 1) {
//                double offset = (i - (bondType - 1) / 2.0) * 5; // Increased spacing
//                // Better perpendicular calculation that works in 3D
//                Point3D arbitrary = Math.abs(direction.getY()) < 0.9 ? new Point3D(0, 1, 0) : new Point3D(1, 0, 0);
//                Point3D perpendicular = direction.normalize().crossProduct(arbitrary).normalize();
//                bond.setTranslateX(bond.getTranslateX() + perpendicular.getX() * offset);
//                bond.setTranslateY(bond.getTranslateY() + perpendicular.getY() * offset);
//                bond.setTranslateZ(bond.getTranslateZ() + perpendicular.getZ() * offset);
//            }
//
//            Point3D yAxis = new Point3D(0, 1, 0);
//            Point3D axis = yAxis.crossProduct(direction.normalize());
//            double angle = Math.acos(yAxis.dotProduct(direction.normalize()));
//
//            if (axis.magnitude() > 0.0001) {
//                bond.setRotationAxis(axis);
//                bond.setRotate(Math.toDegrees(angle));
//            }
//
//            moleculeGroup.getChildren().add(bond);
//        }
//    }
//
//    private double calculateBondAngle(Point3D p1, Point3D center, Point3D p2) {
//        Point3D v1 = p1.subtract(center).normalize();
//        Point3D v2 = p2.subtract(center).normalize();
//        double dot = v1.dotProduct(v2);
//        return Math.toDegrees(Math.acos(Math.max(-1.0, Math.min(1.0, dot))));
//    }
//
//    private void drawBondAngleArc(Point3D p1, Point3D center, Point3D p2, double angle) {
//        // Draw red arc between two bonds
//        Point3D v1 = p1.subtract(center).normalize();
//        Point3D v2 = p2.subtract(center).normalize();
//
//        double arcRadius = 25; // Radius of the arc
//        int segments = 30; // More segments for smoother arc
//
//        // Calculate rotation axis (perpendicular to both vectors)
//        Point3D axis = v1.crossProduct(v2).normalize();
//
//        if (axis.magnitude() < 0.001)
//            return; // Vectors are parallel
//
//        // Create arc using small cylinders
//        for (int i = 0; i < segments; i++) {
//            double t1 = (double) i / segments;
//            double t2 = (double) (i + 1) / segments;
//
//            // Interpolate between v1 and v2
//            Point3D dir1 = rotateVector(v1, axis, angle * t1).multiply(arcRadius);
//            Point3D dir2 = rotateVector(v1, axis, angle * t2).multiply(arcRadius);
//
//            // Draw small cylinder segment
//            Point3D start = center.add(dir1);
//            Point3D end = center.add(dir2);
//
//            drawArcSegment(start, end);
//        }
//
//        // No text label needed - angle info shown in side panel
//    }
//
//    private void drawArcSegment(Point3D start, Point3D end) {
//        Point3D direction = end.subtract(start);
//        double length = direction.magnitude();
//
//        if (length < 0.1)
//            return;
//
//        Cylinder segment = new Cylinder(1, length);
//        PhongMaterial mat = new PhongMaterial(Color.web("#ff6b6b")); // Red color
//        segment.setMaterial(mat);
//
//        Point3D mid = start.midpoint(end);
//        segment.setTranslateX(mid.getX());
//        segment.setTranslateY(mid.getY());
//        segment.setTranslateZ(mid.getZ());
//
//        // Rotate segment to align with direction
//        Point3D yAxis = new Point3D(0, 1, 0);
//        Point3D axis = yAxis.crossProduct(direction.normalize());
//        double angle = Math.acos(yAxis.dotProduct(direction.normalize()));
//
//        if (axis.magnitude() > 0.0001) {
//            segment.setRotationAxis(axis);
//            segment.setRotate(Math.toDegrees(angle));
//        }
//
//        moleculeGroup.getChildren().add(segment);
//    }
//
//    private Point3D rotateVector(Point3D vector, Point3D axis, double angleDeg) {
//        double angleRad = Math.toRadians(angleDeg);
//        double cos = Math.cos(angleRad);
//        double sin = Math.sin(angleRad);
//        double oneMinusCos = 1.0 - cos;
//
//        double x = axis.getX();
//        double y = axis.getY();
//        double z = axis.getZ();
//
//        double[][] rotMatrix = {
//                {cos + x * x * oneMinusCos, x * y * oneMinusCos - z * sin, x * z * oneMinusCos + y * sin},
//                {y * x * oneMinusCos + z * sin, cos + y * y * oneMinusCos, y * z * oneMinusCos - x * sin},
//                {z * x * oneMinusCos - y * sin, z * y * oneMinusCos + x * sin, cos + z * z * oneMinusCos}
//        };
//
//        double vx = vector.getX();
//        double vy = vector.getY();
//        double vz = vector.getZ();
//
//        return new Point3D(
//                rotMatrix[0][0] * vx + rotMatrix[0][1] * vy + rotMatrix[0][2] * vz,
//                rotMatrix[1][0] * vx + rotMatrix[1][1] * vy + rotMatrix[1][2] * vz,
//                rotMatrix[2][0] * vx + rotMatrix[2][1] * vy + rotMatrix[2][2] * vz);
//    }
//
//    private Color getAtomColor(String atom) {
//        Map<String, Color> colors = new HashMap<>();
//        colors.put("H", Color.web("#ffffff"));
//        colors.put("C", Color.web("#909090"));
//        colors.put("N", Color.web("#3050f8"));
//        colors.put("O", Color.web("#ff0d0d"));
//        colors.put("F", Color.web("#90e050"));
//        colors.put("Cl", Color.web("#1ff01f"));
//        colors.put("Br", Color.web("#a62929"));
//        colors.put("S", Color.web("#ffff30"));
//        colors.put("P", Color.web("#ff8000"));
//        return colors.getOrDefault(atom, Color.GRAY);
//    }
//
//    private void updateGeometryInfo() {
//        VSEPRGeometry geometry = molecule.getGeometry();
//
//        // Update electron geometry label
//        if (electronGeometryLabel != null) {
//            electronGeometryLabel.setText(geometry.electronGeometry);
//        }
//
//        // Update molecular geometry label
//        if (molecularGeometryLabel != null) {
//            molecularGeometryLabel.setText(geometry.name);
//        }
//
//        // Keep old label for compatibility (if still used elsewhere)
//        if (geometryInfoLabel != null) {
//            String shapeName = geometry.name;
//            shapeName = shapeName.replaceAll("\\s*\\([^)]*\\)", "");
//            geometryInfoLabel.setText("Shape: " + shapeName);
//        }
//
//        if (bondAngleLabel != null) {
//            bondAngleLabel.setText("Bond Angles: " + geometry.bondAngles);
//        }
//    }
//
//    private void loadPresetMolecule(String selection) {
//        molecule.clear();
//
//        switch (selection) {
//            case "H₂O - Water":
//                // 2 H atoms with single bonds, 2 lone pairs
//                molecule.addAtom("H", 1);
//                molecule.addAtom("H", 1);
//                molecule.addLonePair();
//                molecule.addLonePair();
//                break;
//
//            case "CO₂ - Carbon Dioxide":
//                // 2 O atoms with double bonds (linear)
//                molecule.addAtom("O", 2);
//                molecule.addAtom("O", 2);
//                break;
//
//            case "NH₃ - Ammonia":
//                // 3 H atoms with single bonds, 1 lone pair
//                molecule.addAtom("H", 1);
//                molecule.addAtom("H", 1);
//                molecule.addAtom("H", 1);
//                molecule.addLonePair();
//                break;
//
//            case "CH₄ - Methane":
//                // 4 H atoms with single bonds (tetrahedral)
//                molecule.addAtom("H", 1);
//                molecule.addAtom("H", 1);
//                molecule.addAtom("H", 1);
//                molecule.addAtom("H", 1);
//                break;
//
//            case "BF₃ - Boron Trifluoride":
//                // 3 F atoms with single bonds (trigonal planar)
//                molecule.addAtom("F", 1);
//                molecule.addAtom("F", 1);
//                molecule.addAtom("F", 1);
//                break;
//
//            case "SF₆ - Sulfur Hexafluoride":
//                // 6 F atoms with single bonds (octahedral)
//                molecule.addAtom("F", 1);
//                molecule.addAtom("F", 1);
//                molecule.addAtom("F", 1);
//                molecule.addAtom("F", 1);
//                molecule.addAtom("F", 1);
//                molecule.addAtom("F", 1);
//                break;
//
//            case "SO₂ - Sulfur Dioxide":
//                // 2 O atoms with double bonds, 1 lone pair (bent)
//                molecule.addAtom("O", 2);
//                molecule.addAtom("O", 2);
//                molecule.addLonePair();
//                break;
//
//            case "XeF₂ - Xenon Difluoride":
//                // 2 F atoms with single bonds, 3 lone pairs (linear from trigonal bipyramidal)
//                molecule.addAtom("F", 1);
//                molecule.addAtom("F", 1);
//                molecule.addLonePair();
//                molecule.addLonePair();
//                molecule.addLonePair();
//                break;
//
//            case "ClF₃ - Chlorine Trifluoride":
//                // 3 F atoms with single bonds, 2 lone pairs (T-shaped)
//                molecule.addAtom("F", 1);
//                molecule.addAtom("F", 1);
//                molecule.addAtom("F", 1);
//                molecule.addLonePair();
//                molecule.addLonePair();
//                break;
//
//            case "XeF₄ - Xenon Tetrafluoride":
//                // 4 F atoms with single bonds, 2 lone pairs (square planar)
//                molecule.addAtom("F", 1);
//                molecule.addAtom("F", 1);
//                molecule.addAtom("F", 1);
//                molecule.addAtom("F", 1);
//                molecule.addLonePair();
//                molecule.addLonePair();
//                break;
//
//            case "BrF₅ - Bromine Pentafluoride":
//                // 5 F atoms with single bonds, 1 lone pair (square pyramidal)
//                molecule.addAtom("F", 1);
//                molecule.addAtom("F", 1);
//                molecule.addAtom("F", 1);
//                molecule.addAtom("F", 1);
//                molecule.addAtom("F", 1);
//                molecule.addLonePair();
//                break;
//
//            case "PCl₅ - Phosphorus Pentachloride":
//                // 5 Cl atoms with single bonds (trigonal bipyramidal)
//                molecule.addAtom("Cl", 1);
//                molecule.addAtom("Cl", 1);
//                molecule.addAtom("Cl", 1);
//                molecule.addAtom("Cl", 1);
//                molecule.addAtom("Cl", 1);
//                break;
//
//            case "Custom (Build Your Own)":
//            default:
//                // Keep empty for custom building
//                break;
//        }
//
//        updateMolecule();
//    }
//}
//


package com.example.simulearn;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Point3D;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class RealMoleculeController implements Initializable {

    @FXML
    private void onBackButtonClicked(ActionEvent event) {
        try {
            // Load FXML correctly
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/simulearn/chemistryMenu.fxml"));
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

    private Group moleculeGroup;
    private Rotate rotateX = new Rotate(0, Rotate.X_AXIS);
    private Rotate rotateY = new Rotate(0, Rotate.Y_AXIS);
    private double anchorX, anchorY;
    private double anchorAngleX = 0;
    private double anchorAngleY = 0;

    private int selectedBondType = 1;
    private boolean showBondAngles = false;

    private Molecule molecule;
    private SubScene subscene;
    private StackPane view3DContainer; // Container for 3D view and overlays

    private Button btnContext, btnLaunch, btnReflection;
    private Button selectedButton = null;

    private ScrollPane scrollPane;
    private VBox buttonPanel;

    // For geometry info updates
    private Label geometryInfoLabel;
    private Label bondAngleLabel;
    private Label electronGeometryLabel;
    private Label molecularGeometryLabel;

    // Molecule selector dropdown
    private ComboBox<String> moleculeSelector;

    // IMAGE PATHS - Replace these with your actual image file paths
    private static final String LINEAR_GEOMETRY_IMAGE = "/Images/RealMolecule/lineargeometry.jpg";
    private static final String TRIGONAL_PLANAR_IMAGE = "/Images/RealMolecule/trigonalplanar.jpg";
    private static final String TETRAHEDRAL_IMAGE = "/Images/RealMolecule/tetrahedralgeometry.jpg";
    private static final String TRIGONAL_PYRAMIDAL_IMAGE = "/Images/RealMolecule/trigonalpyramidal.jpg";

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        molecule = new Molecule();

        setupLayout();
        setup3DView();

        updateMolecule();
    }

    private void setupLayout() {
        scrollPane = new ScrollPane();
        scrollPane.setPrefWidth(800);
        scrollPane.setMaxWidth(Region.USE_PREF_SIZE);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle(
                "-fx-background: white;" +
                        "-fx-background-color: white;" +
                        "-fx-border-color: #cccccc;" +
                        "-fx-border-radius: 8;" +
                        "-fx-background-radius: 8;");
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        instruction.setPadding(new Insets(20));
        instruction.setStyle(
                "-fx-background-color: #2a2a2a;" +
                        "-fx-border-color: #3a3a3a;" +
                        "-fx-border-radius: 8;" +
                        "-fx-background-radius: 8;");
        instruction.setPrefWidth(380);
        instruction.setMinWidth(380);

        buttonPanel = createButtonPanel();
        instruction.getChildren().add(buttonPanel);

        mainpanel.setAlignment(Pos.TOP_RIGHT);
        mainpanel.getChildren().add(scrollPane);

        updateScrollContent(getContextContent());
    }

    private VBox createButtonPanel() {
        VBox panel = new VBox(0);
        panel.setAlignment(Pos.TOP_LEFT);
        panel.setPadding(new Insets(10));

        btnContext = createNavButton("1", "CONTEXT");
        btnLaunch = createNavButton("2", "LAUNCH");
        btnReflection = createNavButton("3", "REFLECTION");

        btnContext.setOnAction(e -> {
            setSelectedButton(btnContext);
            showContextMode();
        });

        btnLaunch.setOnAction(e -> {
            setSelectedButton(btnLaunch);
            showLaunchMode();
        });

        btnReflection.setOnAction(e -> {
            setSelectedButton(btnReflection);
            showReflectionMode();
        });

        panel.getChildren().addAll(btnContext, btnLaunch, btnReflection);

        setSelectedButton(btnContext);

        return panel;
    }

    private Button createNavButton(String number, String title) {
        VBox buttonContent = new VBox(8);
        buttonContent.setAlignment(Pos.CENTER);
        buttonContent.setPadding(new Insets(20, 15, 20, 15));

        Circle circle = new Circle(28);
        circle.setFill(Color.web("#1e1e1e"));
        circle.setStroke(Color.web("#4a4a4a"));
        circle.setStrokeWidth(2);

        Label numLabel = new Label(number);
        numLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 20));
        numLabel.setStyle("-fx-text-fill: #000000;");

        StackPane circleStack = new StackPane(circle, numLabel);

        Label titleLabel = new Label(title);
        titleLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
        titleLabel.setStyle("-fx-text-fill: #000000;");

        buttonContent.getChildren().addAll(circleStack, titleLabel);

        Button button = new Button();
        button.setGraphic(buttonContent);
        button.setMaxWidth(Double.MAX_VALUE);
        button.setStyle(
                "-fx-background-color: #1e1e1e;" +
                        "-fx-background-radius: 10;" +
                        "-fx-border-color: #3a3a3a;" +
                        "-fx-border-radius: 10;" +
                        "-fx-border-width: 2;" +
                        "-fx-cursor: hand;" +
                        "-fx-padding: 0;");

        button.setOnMouseEntered(e -> {
            if (selectedButton != button) {
                button.setStyle(
                        "-fx-background-color: #2a2a2a;" +
                                "-fx-background-radius: 10;" +
                                "-fx-border-color: #4a4a4a;" +
                                "-fx-border-radius: 10;" +
                                "-fx-border-width: 2;" +
                                "-fx-cursor: hand;" +
                                "-fx-padding: 0;");
            }
        });

        button.setOnMouseExited(e -> {
            if (selectedButton != button) {
                button.setStyle(
                        "-fx-background-color: #1e1e1e;" +
                                "-fx-background-radius: 10;" +
                                "-fx-border-color: #3a3a3a;" +
                                "-fx-border-radius: 10;" +
                                "-fx-border-width: 2;" +
                                "-fx-cursor: hand;" +
                                "-fx-padding: 0;");
            }
        });

        VBox.setMargin(button, new Insets(0, 0, 15, 0));

        return button;
    }

    private void setSelectedButton(Button button) {
        if (selectedButton != null) {
            selectedButton.setStyle(
                    "-fx-background-color: #1e1e1e;" +
                            "-fx-background-radius: 10;" +
                            "-fx-border-color: #3a3a3a;" +
                            "-fx-border-radius: 10;" +
                            "-fx-border-width: 2;" +
                            "-fx-cursor: hand;" +
                            "-fx-padding: 0;");
        }

        selectedButton = button;
        button.setStyle(
                "-fx-background-color: #0d7377;" +
                        "-fx-background-radius: 10;" +
                        "-fx-border-color: #14a6ac;" +
                        "-fx-border-radius: 10;" +
                        "-fx-border-width: 3;" +
                        "-fx-cursor: hand;" +
                        "-fx-padding: 0;");
    }

    private void showContextMode() {
        // Show scrollpane in mainpanel
        mainpanel.getChildren().clear();
        mainpanel.getChildren().add(scrollPane);
        scrollPane.setVisible(true);
        scrollPane.setManaged(true);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        // Reset instruction panel to just buttons
        instruction.getChildren().clear();
        instruction.getChildren().add(buttonPanel);

        updateScrollContent(getContextContent());
    }

    private void showLaunchMode() {
        // Hide scrollpane, show 3D view
        scrollPane.setVisible(false);
        scrollPane.setManaged(false);

        mainpanel.getChildren().clear();

        view3DContainer = new StackPane();
        view3DContainer.setStyle("-fx-background-color: #0d0d0d;");
        VBox.setVgrow(view3DContainer, Priority.ALWAYS);

        if (subscene != null) {
            subscene.widthProperty().bind(view3DContainer.widthProperty());
            subscene.heightProperty().bind(view3DContainer.heightProperty());
            view3DContainer.getChildren().add(subscene);
        }

        mainpanel.getChildren().add(view3DContainer);

        // REPLACE instruction panel with controls
        createLaunchControlPanel();
    }

    private void showReflectionMode() {
        mainpanel.getChildren().clear();
        mainpanel.getChildren().add(scrollPane);
        scrollPane.setVisible(true);
        scrollPane.setManaged(true);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        instruction.getChildren().clear();
        instruction.getChildren().add(buttonPanel);

        updateScrollContent(getReflectionContent());
    }

    private void createLaunchControlPanel() {
        // CLEAR and REBUILD instruction panel
        instruction.getChildren().clear();

        // Add button panel first
        instruction.getChildren().add(buttonPanel);

        // Molecule Selector Panel
        VBox moleculeSelectorPanel = createControlPanel("Select Molecule");

        moleculeSelector = new ComboBox<>();
        moleculeSelector.getItems().addAll(
                "Custom (Build Your Own)",
                "H₂O - Water",
                "CO₂ - Carbon Dioxide",
                "NH₃ - Ammonia",
                "CH₄ - Methane",
                "BF₃ - Boron Trifluoride",
                "SF₆ - Sulfur Hexafluoride",
                "SO₂ - Sulfur Dioxide",
                "XeF₂ - Xenon Difluoride",
                "ClF₃ - Chlorine Trifluoride",
                "XeF₄ - Xenon Tetrafluoride",
                "BrF₅ - Bromine Pentafluoride",
                "PCl₅ - Phosphorus Pentachloride");
        moleculeSelector.setValue("Custom (Build Your Own)");
        moleculeSelector.setMaxWidth(Double.MAX_VALUE);
        moleculeSelector.setStyle(
                "-fx-background-color: #1e1e1e;" +
                        "-fx-text-fill: #000000;" +
                        "-fx-font-size: 14px;" +
                        "-fx-padding: 12;" +
                        "-fx-background-radius: 8;" +
                        "-fx-border-color: #3a3a3a;" +
                        "-fx-border-radius: 8;" +
                        "-fx-border-width: 1;");

        moleculeSelector.setOnAction(e -> loadPresetMolecule(moleculeSelector.getValue()));

        moleculeSelectorPanel.getChildren().add(moleculeSelector);

        // Bonding Panel
        VBox bondingPanel = createControlPanel("Bonding");

        VBox bondsContainer = new VBox(8);

        Button singleBondBtn = createBondButton("Single", 1);
        Button doubleBondBtn = createBondButton("Double", 2);
        Button tripleBondBtn = createBondButton("Triple", 3);

        bondsContainer.getChildren().addAll(singleBondBtn, doubleBondBtn, tripleBondBtn);
        bondingPanel.getChildren().add(bondsContainer);

        // Lone Pair Panel
        VBox lonePairPanel = createControlPanel("Lone Pair");

        Button lpButton = new Button();
        lpButton.setMaxWidth(Double.MAX_VALUE);

        HBox lpContent = new HBox(12);
        lpContent.setAlignment(Pos.CENTER_LEFT);
        lpContent.setPadding(new Insets(12, 15, 12, 15));

        // Lone pair visual
        StackPane lpVisual = new StackPane();
        lpVisual.setPrefSize(50, 30);

        Ellipse e1 = new Ellipse(12, 8);
        e1.setFill(Color.web("#888888"));
        e1.setTranslateX(-8);

        Ellipse e2 = new Ellipse(12, 8);
        e2.setFill(Color.web("#888888"));
        e2.setTranslateX(8);

        lpVisual.getChildren().addAll(e1, e2);

        Label lpLabel = new Label("Add Lone Pair");
        lpLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 13));
        lpLabel.setTextFill(Color.web("#ffffff"));

        lpContent.getChildren().addAll(lpVisual, lpLabel);
        lpButton.setGraphic(lpContent);

        lpButton.setStyle(
                "-fx-background-color: #1e1e1e;" +
                        "-fx-background-radius: 8;" +
                        "-fx-border-color: #3a3a3a;" +
                        "-fx-border-width: 1;" +
                        "-fx-border-radius: 8;" +
                        "-fx-cursor: hand;");

        // Add click action to directly add lone pair
        lpButton.setOnAction(e -> {
            molecule.addLonePair();
            updateMolecule();
            System.out.println("Added lone pair");
        });

        // Hover effects
        lpButton.setOnMouseEntered(e -> {
            lpButton.setStyle(
                    "-fx-background-color: #0d3436;" +
                            "-fx-background-radius: 8;" +
                            "-fx-border-color: #14a6ac;" +
                            "-fx-border-width: 2;" +
                            "-fx-border-radius: 8;" +
                            "-fx-cursor: hand;");
        });

        lpButton.setOnMouseExited(e -> {
            lpButton.setStyle(
                    "-fx-background-color: #1e1e1e;" +
                            "-fx-background-radius: 8;" +
                            "-fx-border-color: #3a3a3a;" +
                            "-fx-border-width: 1;" +
                            "-fx-border-radius: 8;" +
                            "-fx-cursor: hand;");
        });

        lonePairPanel.getChildren().add(lpButton);

        // Remove All Button
        Button removeAllBtn = new Button("Remove All");
        removeAllBtn.setMaxWidth(Double.MAX_VALUE);
        removeAllBtn.setFont(Font.font("Segoe UI", FontWeight.BOLD, 13));
        removeAllBtn.setPadding(new Insets(12));
        removeAllBtn.setStyle(
                "-fx-background-color: #d4a00c;" +
                        "-fx-text-fill: white;" +
                        "-fx-background-radius: 8;" +
                        "-fx-cursor: hand;");
        removeAllBtn.setOnAction(e -> {
            molecule.clear();
            updateMolecule();
        });
        removeAllBtn.setOnMouseEntered(e -> removeAllBtn.setOpacity(0.85));
        removeAllBtn.setOnMouseExited(e -> removeAllBtn.setOpacity(1.0));

        // Remove Last Button
        Button removeLastBtn = new Button("Remove Last");
        removeLastBtn.setFont(Font.font("Segoe UI", FontWeight.BOLD, 13));
        removeLastBtn.setMaxWidth(Double.MAX_VALUE);
        removeLastBtn.setStyle(
                "-fx-background-color: #c44536;" +
                        "-fx-text-fill: white;" +
                        "-fx-background-radius: 8;" +
                        "-fx-padding: 12 20;" +
                        "-fx-cursor: hand;");
        removeLastBtn.setOnAction(e -> {
            molecule.removeLast();
            updateMolecule();
        });
        removeLastBtn.setOnMouseEntered(e -> removeLastBtn.setOpacity(0.85));
        removeLastBtn.setOnMouseExited(e -> removeLastBtn.setOpacity(1.0));

        // Options Panel
        VBox optionsPanel = createControlPanel("Options");

        CheckBox showLpCheck = new CheckBox("Show Lone Pairs");
        showLpCheck.setSelected(true);
        showLpCheck.setFont(Font.font("Segoe UI", 13));
        showLpCheck.setTextFill(Color.web("#ffffff"));

        CheckBox showAnglesCheck = new CheckBox("Show Bond Angles");
        showAnglesCheck.setSelected(false);
        showAnglesCheck.setFont(Font.font("Segoe UI", 13));
        showAnglesCheck.setTextFill(Color.web("#ffffff"));
        showAnglesCheck.setOnAction(e -> {
            showBondAngles = showAnglesCheck.isSelected();
            updateMolecule();
        });

        VBox optionsBox = new VBox(10, showLpCheck, showAnglesCheck);
        optionsBox.setPadding(new Insets(5));
        optionsPanel.getChildren().add(optionsBox);

        // Name Panel (Electron/Molecule Geometry)
        VBox namePanel = createControlPanel("Name");

        HBox nameBox = new HBox(15);
        nameBox.setAlignment(Pos.CENTER_LEFT);
        nameBox.setPadding(Insets.EMPTY);
        nameBox.setStyle(
                "-fx-background-color: transparent;");

        // Create VBox for geometry labels
        VBox geomLabels = new VBox(12);
        geomLabels.setPadding(new Insets(8, 12, 8, 12));
        geomLabels.setStyle(
                "-fx-background-color: #1e1e1e;" +
                        "-fx-background-radius: 8;" +
                        "-fx-border-color: #3a3a3a;" +
                        "-fx-border-width: 1;" +
                        "-fx-border-radius: 8;");

        // Electron Geometry Label
        VBox electronBox = new VBox(4);
        Label electronTitle = new Label("Electron Geometry");
        electronTitle.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 11));
        electronTitle.setTextFill(Color.web("#888888"));

        electronGeometryLabel = new Label("None");
        electronGeometryLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 13));
        electronGeometryLabel.setTextFill(Color.web("#f4d03f"));
        electronGeometryLabel.setWrapText(true);

        electronBox.getChildren().addAll(electronTitle, electronGeometryLabel);

        // Molecular Geometry Label
        VBox molecularBox = new VBox(4);
        Label molecularTitle = new Label("Molecule Geometry");
        molecularTitle.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 11));
        molecularTitle.setTextFill(Color.web("#888888"));

        molecularGeometryLabel = new Label("None");
        molecularGeometryLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 13));
        molecularGeometryLabel.setTextFill(Color.web("#ffffff"));
        molecularGeometryLabel.setWrapText(true);

        molecularBox.getChildren().addAll(molecularTitle, molecularGeometryLabel);

        geomLabels.getChildren().addAll(electronBox, molecularBox);
        nameBox.getChildren().add(geomLabels);
        namePanel.getChildren().add(nameBox);

        // Geometry Info Display
        VBox infoDisplay = new VBox(8);
        infoDisplay.setPadding(new Insets(10));
        infoDisplay.setStyle(
                "-fx-background-color: #1a1a1a;" +
                        "-fx-background-radius: 8;" +
                        "-fx-border-color: #0d7377;" +
                        "-fx-border-width: 2;" +
                        "-fx-border-radius: 8;");

        geometryInfoLabel = new Label("Shape: None");
        geometryInfoLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
        geometryInfoLabel.setTextFill(Color.web("#14a6ac"));
        geometryInfoLabel.setWrapText(true);

        bondAngleLabel = new Label("Angles: N/A");
        bondAngleLabel.setFont(Font.font("Segoe UI", 13));
        bondAngleLabel.setTextFill(Color.web("#ffffff"));
        bondAngleLabel.setWrapText(true);

        infoDisplay.getChildren().addAll(geometryInfoLabel, bondAngleLabel);

        // Add all to instruction panel
        instruction.getChildren().addAll(
                moleculeSelectorPanel,
                bondingPanel,
                lonePairPanel,
                removeAllBtn,
                removeLastBtn,
                optionsPanel,
                namePanel,
                infoDisplay);

        VBox.setMargin(moleculeSelectorPanel, new Insets(0, 0, 10, 0));
        VBox.setMargin(removeAllBtn, new Insets(5, 0, 5, 0));
        VBox.setMargin(removeLastBtn, new Insets(5, 0, 5, 0));

        updateMolecule();
    }

    private VBox createControlPanel(String title) {
        VBox panel = new VBox(10);
        panel.setPadding(new Insets(12));
        panel.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-border-color: #4a4a4a;" +
                        "-fx-border-width: 1.5;" +
                        "-fx-border-radius: 10;" +
                        "-fx-background-radius: 10;");

        Label titleLabel = new Label(title);
        titleLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
        titleLabel.setTextFill(Color.web("#ffffff"));

        panel.getChildren().add(titleLabel);

        return panel;
    }

    private Button createBondButton(String label, int bondType) {
        Button button = new Button();
        button.setMaxWidth(Double.MAX_VALUE);

        HBox content = new HBox(12);
        content.setAlignment(Pos.CENTER_LEFT);
        content.setPadding(new Insets(12, 15, 12, 15));

        // Bond visual
        int bondCount = label.equals("Single") ? 1 : label.equals("Double") ? 2 : 3;
        StackPane bondVisual = new StackPane();
        bondVisual.setPrefSize(60, 30);

        for (int i = 0; i < bondCount; i++) {
            Line line = new Line(0, 0, 50, 0);
            line.setStroke(Color.web("#cccccc"));
            line.setStrokeWidth(3);

            if (bondCount > 1) {
                line.setTranslateY((i - (bondCount - 1) / 2.0) * 6);
            }
            bondVisual.getChildren().add(line);
        }

        Circle c1 = new Circle(8, Color.web("#999999"));
        c1.setTranslateX(-25);
        Circle c2 = new Circle(8, Color.web("#999999"));
        c2.setTranslateX(25);

        bondVisual.getChildren().addAll(c1, c2);

        Label bondLabel = new Label("Add " + label + " Bond");
        bondLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 13));
        bondLabel.setTextFill(Color.web("#ffffff"));

        content.getChildren().addAll(bondVisual, bondLabel);
        button.setGraphic(content);

        button.setStyle(
                "-fx-background-color: #1e1e1e;" +
                        "-fx-background-radius: 8;" +
                        "-fx-border-color: #3a3a3a;" +
                        "-fx-border-width: 1;" +
                        "-fx-border-radius: 8;" +
                        "-fx-cursor: hand;");

        // Add click action to directly add atom with this bond type
        button.setOnAction(e -> {
            molecule.addAtom("H", bondType);
            updateMolecule();
            System.out.println("Added H atom with " + label.toLowerCase() + " bond (type " + bondType + ")");
        });

        // Hover effects
        button.setOnMouseEntered(e -> {
            button.setStyle(
                    "-fx-background-color: #0d3436;" +
                            "-fx-background-radius: 8;" +
                            "-fx-border-color: #14a6ac;" +
                            "-fx-border-width: 2;" +
                            "-fx-border-radius: 8;" +
                            "-fx-cursor: hand;");
        });

        button.setOnMouseExited(e -> {
            button.setStyle(
                    "-fx-background-color: #1e1e1e;" +
                            "-fx-background-radius: 8;" +
                            "-fx-border-color: #3a3a3a;" +
                            "-fx-border-width: 1;" +
                            "-fx-border-radius: 8;" +
                            "-fx-cursor: hand;");
        });

        return button;
    }

    private void updateScrollContent(VBox content) {
        scrollPane.setContent(content);
    }

    private VBox getContextContent() {
        VBox content = new VBox(25);
        content.setPadding(new Insets(40));
        content.setStyle("-fx-background-color: white;");

        Label title = new Label("1. CONTEXT");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 32));
        title.setStyle("-fx-text-fill: #0d7377;");

        Separator underline = new Separator();
        underline.setMaxWidth(200);
        underline.setStyle("-fx-background-color: #0d7377;");
        underline.setPrefHeight(3);

        // Introduction paragraph
        Label introText = new Label(
                "When we look at Lewis structures, we are only seeing a two-dimensional picture. To get the full picture of "
                        +
                        "molecules, we need to find ways to represent those atoms in three dimensions. Why? Because the shape, or "
                        +
                        "geometry, of a molecule helps us understand how that molecule will behave. In fact, a molecule's geometry "
                        +
                        "affects its physical interaction with other molecules, determines which region of the molecule is likely to "
                        +
                        "undergo chemical changes, influences properties like melting point and density, and influences the products "
                        +
                        "that will form when it reacts with different substances. So let's take a look at how we can determine the "
                        +
                        "shapes of molecules.");
        introText.setWrapText(true);
        introText.setFont(Font.font("Segoe UI", 15));
        introText.setLineSpacing(3);
        introText.setStyle("-fx-text-fill: #333333;");

        // Linear Geometry Section
        Label linearHeading = new Label("Linear Geometry");
        linearHeading.setFont(Font.font("Segoe UI", FontWeight.BOLD, 20));
        linearHeading.setStyle("-fx-text-fill: #000000;");
        linearHeading.setPadding(new Insets(20, 0, 10, 0));

        // Try to load linear geometry image
        VBox linearImageBox = createImageBox(LINEAR_GEOMETRY_IMAGE,
                "Figure 5-12. Linear Geometry\n" +
                        "In carbon dioxide, there is a central atom with two repelling regions of electron density (the electrons in "
                        +
                        "the bonds). This forms a bond angle of 180°.");

        // VSEPR Theory Section
        Label vseprHeading = new Label("Valence Shell Electron Pair Repulsion Theory");
        vseprHeading.setFont(Font.font("Segoe UI", FontWeight.BOLD, 20));
        vseprHeading.setStyle("-fx-text-fill: #000000;");
        vseprHeading.setPadding(new Insets(20, 0, 10, 0));

        Label vseprText = new Label(
                "There is a simple model that allows us to predict, based on the Lewis structure, a molecule's three-" +
                        "dimensional geometry. It is called the Valence Shell Electron Pair Repulsion theory, or VSEPR (pronounced "
                        +
                        "\"vesper\"). In this model, we look at the arrangement of atoms and lone pairs around each atom individually. "
                        +
                        "We call the atom under consideration the central atom and the other atoms or lone pairs attached to it "
                        +
                        "the substituents of that atom. We consider each atom one at a time and then combine them to get the full "
                        +
                        "picture.\n\n" +
                        "The way VSEPR theory works is that we imagine that the electrons in the bonds or lone pairs desperately "
                        +
                        "want to get as far away from each other as they can since all are negatively charged. But, since they are "
                        +
                        "bound to the central atom, the best they can do is distribute themselves around the central atom with "
                        +
                        "the angles between the atoms or lone pairs as large as possible. Imagine a central atom with bonds to two "
                        +
                        "other things, for example the carbon in carbon dioxide, which is bound to two oxygens (Figure 5-12). "
                        +
                        "The repelling electrons in the bonds will move to opposite sides of the central atom until they are 180° "
                        +
                        "away from each other. We define the bond angle as the angle between the bonds to the central atom. "
                        +
                        "When atoms arrange themselves in a straight line like this, we call it \"linear geometry.\"");
        vseprText.setWrapText(true);
        vseprText.setFont(Font.font("Segoe UI", 15));
        vseprText.setLineSpacing(3);
        vseprText.setStyle("-fx-text-fill: #333333;");

        // Trigonal Planar Section
        Label trigonalHeading = new Label("Trigonal Planar Geometry");
        trigonalHeading.setFont(Font.font("Segoe UI", FontWeight.BOLD, 20));
        trigonalHeading.setStyle("-fx-text-fill: #000000;");
        trigonalHeading.setPadding(new Insets(20, 0, 10, 0));

        VBox trigonalImageBox = createImageBox(TRIGONAL_PLANAR_IMAGE,
                "Figure 5-13. Trigonal Planar Geometry\n" +
                        "In sulfur trioxide, SO₃, three repelling substituents form an equilateral triangle in a " +
                        "plane around the central atom. Each bond angle is 120°.");

        Label trigonalText = new Label(
                "If there are three substituents around the central atom, as in sulfur trioxide (SO₃), the farthest they can "
                        +
                        "get from each other while still remaining bound to the central atom is to form a triangle in a plane around it, a "
                        +
                        "geometry called \"trigonal planar.\" The bond angles here are all 120°, as expected for an equilateral triangle.");
        trigonalText.setWrapText(true);
        trigonalText.setFont(Font.font("Segoe UI", 15));
        trigonalText.setLineSpacing(3);
        trigonalText.setStyle("-fx-text-fill: #333333;");

        // Tetrahedral Section
        Label tetrahedralHeading = new Label("Tetrahedral Geometry");
        tetrahedralHeading.setFont(Font.font("Segoe UI", FontWeight.BOLD, 20));
        tetrahedralHeading.setStyle("-fx-text-fill: #000000;");
        tetrahedralHeading.setPadding(new Insets(20, 0, 10, 0));

        VBox tetrahedralImageBox = createImageBox(TETRAHEDRAL_IMAGE,
                "Figure 5-14. Tetrahedral Geometry\n" +
                        "In methane, CH₄, the four mutually repelling bonds adopt a symmetric distribution around the central "
                        +
                        "carbon, a geometry known as tetrahedral. The angle that separates each of the bonds is 109.5°.");

        Label tetrahedralText = new Label(
                "If there are four substituents repelling each other, the resulting geometry is called \"tetrahedral,\" as shown "
                        +
                        "in Figure 5-14, and each of the bond angles is approximately 109.5°. Even though the Lewis structure makes "
                        +
                        "any molecule seem flat, by combining the Lewis structure with VSEPR theory, we can understand and determine "
                        +
                        "the 3-D structures of molecules.");
        tetrahedralText.setWrapText(true);
        tetrahedralText.setFont(Font.font("Segoe UI", 15));
        tetrahedralText.setLineSpacing(3);
        tetrahedralText.setStyle("-fx-text-fill: #333333;");

        // Molecular Geometry vs Electron Geometry
        Label molecularHeading = new Label("Molecular Geometry: Accounting for Lone Pairs");
        molecularHeading.setFont(Font.font("Segoe UI", FontWeight.BOLD, 20));
        molecularHeading.setStyle("-fx-text-fill: #000000;");
        molecularHeading.setPadding(new Insets(20, 0, 10, 0));

        Label molecularText = new Label(
                "When describing molecular geometry, we deduce the shape by looking at the distribution of electrons around "
                        +
                        "the central atom, including not just atoms but also lone electron pairs. But our description of the molecule "
                        +
                        "depends only on the position of atomic nuclei. This means a molecule like ammonia, which has four repulsive "
                        +
                        "regions of electron density (three bonds and one lone pair), has the geometry shown in Figure 5-15. The "
                        +
                        "substituents arrange themselves tetrahedrally, just as we saw with methane. But when naming the molecular shape, "
                        +
                        "we consider only the positions of the atoms, not the lone pairs; so rather than being tetrahedral, we would refer "
                        +
                        "to this molecule as being trigonal pyramidal.");
        molecularText.setWrapText(true);
        molecularText.setFont(Font.font("Segoe UI", 15));
        molecularText.setLineSpacing(3);
        molecularText.setStyle("-fx-text-fill: #333333;");

        VBox pyramidalImageBox = createImageBox(TRIGONAL_PYRAMIDAL_IMAGE,
                "Figure 5-15. Trigonal Pyramidal Geometry\n" +
                        "In ammonia (NH₃), the three hydrogens and one lone pair adopt a tetrahedral arrangement to avoid "
                        +
                        "electron-electron repulsion. Since we cannot see the lone pair, the geometry gets called \"trigonal pyramidal.\"");

        content.getChildren().addAll(
                title, underline,
                introText,
                linearHeading, linearImageBox,
                vseprHeading, vseprText,
                trigonalHeading, trigonalImageBox, trigonalText,
                tetrahedralHeading, tetrahedralImageBox, tetrahedralText,
                molecularHeading, molecularText, pyramidalImageBox);

        return content;
    }

    private VBox createImageBox(String imageFilePath, String caption) {
        VBox imageBox = new VBox(10);
        imageBox.setAlignment(Pos.CENTER);
        imageBox.setPadding(new Insets(15));
        imageBox.setStyle(
                "-fx-background-color: #f5f5f5;" +
                        "-fx-border-color: #dddddd;" +
                        "-fx-border-radius: 8;" +
                        "-fx-background-radius: 8;");

        ImageView image= new ImageView(getClass().getResource(imageFilePath).toExternalForm());

        // Image container with styling
        StackPane imageContainer = new StackPane(image);
        imageContainer.setStyle("-fx-background-color: white;" + "-fx-border-color: #d0d0d0;" + "-fx-border-width: 1;" + "-fx-border-radius: 9;" + "-fx-background-radius: 5;" + "-fx-padding: 10;" + "-fx-effect: dropshadow(three-pass-box, rgba(60, 64, 67, 0.3), 15, 0, 0, 6);");
        imageContainer.setMaxWidth(600);
        imageContainer.setPrefHeight(300);
        imageContainer.setPadding(new Insets(10, 0, 10, 0));

        // Add caption
        Label captionLabel = new Label(caption);
        captionLabel.setWrapText(true);
        captionLabel.setMaxWidth(600);
        captionLabel.setFont(Font.font("Segoe UI", 13));
        captionLabel.setStyle("-fx-text-fill: #666666;");
        captionLabel.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
        imageBox.getChildren().addAll(imageContainer,captionLabel);

        return imageBox;
    }

    private VBox createGeometryTable() {
        VBox table = new VBox(10);
        table.setStyle(
                "-fx-background-color: #2a2a2a;" +
                        "-fx-border-color: #3a3a3a;" +
                        "-fx-border-radius: 8;" +
                        "-fx-background-radius: 8;" +
                        "-fx-padding: 20;");

        addGeometryRow(table, "Linear", "2", "180°", "CO₂, BeCl₂");
        addGeometryRow(table, "Trigonal Planar", "3", "120°", "BF₃, SO₃");
        addGeometryRow(table, "Tetrahedral", "4", "109.5°", "CH₄, NH₄⁺");
        addGeometryRow(table, "Trigonal Pyramidal", "4 (1 LP)", "~107°", "NH₃, PCl₃");
        addGeometryRow(table, "Bent", "4 (2 LP)", "~104.5°", "H₂O, H₂S");
        addGeometryRow(table, "Trigonal Bipyramidal", "5", "90°, 120°, 180°", "PCl₅, AsF₅");
        addGeometryRow(table, "Octahedral", "6", "90°, 180°", "SF₆, PF₆⁻");

        return table;
    }

    private void addGeometryRow(VBox parent, String geometry, String pairs, String angles, String examples) {
        HBox row = new HBox(15);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(10));
        row.setStyle(
                "-fx-background-color: #1e1e1e;" +
                        "-fx-background-radius: 5;");

        Label geoLabel = new Label(geometry);
        geoLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
        geoLabel.setStyle("-fx-text-fill: #14a6ac;");
        geoLabel.setPrefWidth(180);

        Label pairsLabel = new Label("Pairs: " + pairs);
        pairsLabel.setFont(Font.font("Segoe UI", 13));
        pairsLabel.setStyle("-fx-text-fill: #000000;");
        pairsLabel.setPrefWidth(100);

        Label anglesLabel = new Label(angles);
        anglesLabel.setFont(Font.font("Segoe UI", 13));
        anglesLabel.setStyle("-fx-text-fill: #999999;");
        anglesLabel.setPrefWidth(120);

        Label examplesLabel = new Label(examples);
        examplesLabel.setFont(Font.font("Segoe UI", 13));
        examplesLabel.setStyle("-fx-text-fill: #666666;");
        examplesLabel.setWrapText(true);

        row.getChildren().addAll(geoLabel, pairsLabel, anglesLabel, examplesLabel);
        parent.getChildren().add(row);
    }

    private VBox getReflectionContent() {
        VBox content = new VBox(25);
        content.setPadding(new Insets(40));
        content.setStyle("-fx-background-color: #1a1a1a;");

        Label title = new Label("3. REFLECTION");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 32));
        title.setStyle("-fx-text-fill: #14a6ac;");

        Separator underline = new Separator();
        underline.setMaxWidth(250);
        underline.setStyle("-fx-background-color: #0d7377;");
        underline.setPrefHeight(3);

        Label intro = new Label("Test your understanding of VSEPR theory and molecular geometry:");
        intro.setFont(Font.font("Segoe UI", 15));
        intro.setStyle("-fx-text-fill: #333333;");
        intro.setWrapText(true);

        QuizQuestionDark q1 = new QuizQuestionDark(
                1,
                "What is the bond angle in a tetrahedral molecule?",
                new String[]{"90°", "109.5°", "120°", "180°"},
                1,
                "Tetrahedral geometry has bond angles of 109.5° when four electron pairs arrange to minimize repulsion.",
                3);

        QuizQuestionDark q2 = new QuizQuestionDark(
                2,
                "How many lone pairs does an ammonia (NH₃) molecule have?",
                new String[]{"0", "1", "2", "3"},
                1,
                "Nitrogen in NH₃ has 5 valence electrons. Three form bonds with hydrogen, leaving one lone pair.",
                3);

        QuizQuestionDark q3 = new QuizQuestionDark(
                3,
                "What is the molecular geometry of water (H₂O)?",
                new String[]{"Linear", "Bent", "Trigonal Planar", "Tetrahedral"},
                1,
                "Water has 4 electron pairs (2 bonding, 2 lone pairs) giving it a bent shape with ~104.5° angle.",
                3);

        QuizQuestionDark q4 = new QuizQuestionDark(
                4,
                "Which has the largest bond angle?",
                new String[]{"NH₃ (~107°)", "H₂O (~104.5°)", "CH₄ (109.5°)", "H₂S (~92°)"},
                2,
                "CH₄ has perfect tetrahedral geometry with no lone pairs, giving the largest angle of 109.5°.",
                3);

        QuizQuestionDark q5 = new QuizQuestionDark(
                5,
                "Why do lone pairs affect bond angles?",
                new String[]{
                        "They don't affect bond angles",
                        "They take up more space than bonding pairs",
                        "They make molecules larger",
                        "They create ionic bonds"
                },
                1,
                "Lone pairs occupy more space than bonding pairs, causing greater repulsion and smaller bond angles.",
                3);

        content.getChildren().addAll(
                title, underline, intro,
                q1.getQuestionBox(),
                q2.getQuestionBox(),
                q3.getQuestionBox(),
                q4.getQuestionBox(),
                q5.getQuestionBox());

        return content;
    }

    private void setup3DView() {
        moleculeGroup = new Group();

        PerspectiveCamera camera = new PerspectiveCamera(true);
        camera.setTranslateZ(-800);
        camera.setNearClip(0.1);
        camera.setFarClip(2000);

        Group root3D = new Group();
        root3D.getChildren().add(moleculeGroup);
        moleculeGroup.getTransforms().addAll(rotateX, rotateY);

        AmbientLight ambient = new AmbientLight(Color.web("#666666"));
        PointLight point = new PointLight(Color.WHITE);
        point.setTranslateZ(-300);
        root3D.getChildren().addAll(ambient, point);

        subscene = new SubScene(root3D, 800, 600, true, SceneAntialiasing.BALANCED);
        subscene.setFill(Color.web("#0d0d0d"));
        subscene.setCamera(camera);

        subscene.setOnMousePressed((MouseEvent event) -> {
            anchorX = event.getSceneX();
            anchorY = event.getSceneY();
            anchorAngleX = rotateX.getAngle();
            anchorAngleY = rotateY.getAngle();
        });

        subscene.setOnMouseDragged((MouseEvent event) -> {
            rotateX.setAngle(anchorAngleX - (anchorY - event.getSceneY()));
            rotateY.setAngle(anchorAngleY + anchorX - event.getSceneX());
        });
    }

    private void updateMolecule() {
        moleculeGroup.getChildren().clear();

        Sphere centralAtom = new Sphere(12);
        PhongMaterial centralMat = new PhongMaterial(getAtomColor(molecule.centralAtom));
        centralMat.setSpecularColor(Color.WHITE);
        centralAtom.setMaterial(centralMat);
        moleculeGroup.getChildren().add(centralAtom);

        VSEPRGeometry geometry = molecule.getGeometry();
        Point3D[] positions = geometry.getPositions();

        for (int i = 0; i < molecule.bondedAtoms.size(); i++) {
            String atom = molecule.bondedAtoms.get(i);
            int bondType = molecule.bondTypes.get(i);
            Point3D pos = positions[i];

            drawBond(Point3D.ZERO, pos, bondType);

            Sphere atomSphere = new Sphere(9); // Smaller atoms
            PhongMaterial atomMat = new PhongMaterial(getAtomColor(atom));
            atomMat.setSpecularColor(Color.WHITE);
            atomSphere.setMaterial(atomMat);
            atomSphere.setTranslateX(pos.getX());
            atomSphere.setTranslateY(pos.getY());
            atomSphere.setTranslateZ(pos.getZ());
            moleculeGroup.getChildren().add(atomSphere);

            // Show bond angle arc if enabled and we have at least 2 bonds
            if (showBondAngles && i > 0 && molecule.bondedAtoms.size() >= 2) {
                Point3D prevPos = positions[i - 1];
                double angle = calculateBondAngle(prevPos, Point3D.ZERO, pos);
                drawBondAngleArc(prevPos, Point3D.ZERO, pos, angle);
            }
        }

        int startIdx = molecule.bondedAtoms.size();
        for (int i = 0; i < molecule.lonePairs; i++) {
            Point3D pos = positions[startIdx + i];

            Sphere lp1 = new Sphere(6); // Smaller lone pairs
            Sphere lp2 = new Sphere(6);
            PhongMaterial lpMat = new PhongMaterial(Color.web("#888888"));
            lpMat.setSpecularColor(Color.web("#aaaaaa"));
            lp1.setMaterial(lpMat);
            lp2.setMaterial(lpMat);

            // Better perpendicular calculation for 3D space
            Point3D direction = pos.normalize();
            // Find a perpendicular vector using cross product with an arbitrary vector
            Point3D arbitrary = Math.abs(direction.getY()) < 0.9 ? new Point3D(0, 1, 0) : new Point3D(1, 0, 0);
            Point3D perpendicular = direction.crossProduct(arbitrary).normalize().multiply(10);

            lp1.setTranslateX(pos.getX() + perpendicular.getX());
            lp1.setTranslateY(pos.getY() + perpendicular.getY());
            lp1.setTranslateZ(pos.getZ() + perpendicular.getZ());

            lp2.setTranslateX(pos.getX() - perpendicular.getX());
            lp2.setTranslateY(pos.getY() - perpendicular.getY());
            lp2.setTranslateZ(pos.getZ() - perpendicular.getZ());

            moleculeGroup.getChildren().addAll(lp1, lp2);
        }

        updateGeometryInfo();
    }

    private void drawBond(Point3D start, Point3D end, int bondType) {
        Point3D direction = end.subtract(start);
        double length = direction.magnitude();

        for (int i = 0; i < bondType; i++) {
            Cylinder bond = new Cylinder(1.5, length);
            PhongMaterial bondMat = new PhongMaterial(Color.web("#cccccc"));
            bondMat.setSpecularColor(Color.WHITE);
            bond.setMaterial(bondMat);

            Point3D mid = start.midpoint(end);
            bond.setTranslateX(mid.getX());
            bond.setTranslateY(mid.getY());
            bond.setTranslateZ(mid.getZ());

            if (bondType > 1) {
                double offset = (i - (bondType - 1) / 2.0) * 5; // Increased spacing
                // Better perpendicular calculation that works in 3D
                Point3D arbitrary = Math.abs(direction.getY()) < 0.9 ? new Point3D(0, 1, 0) : new Point3D(1, 0, 0);
                Point3D perpendicular = direction.normalize().crossProduct(arbitrary).normalize();
                bond.setTranslateX(bond.getTranslateX() + perpendicular.getX() * offset);
                bond.setTranslateY(bond.getTranslateY() + perpendicular.getY() * offset);
                bond.setTranslateZ(bond.getTranslateZ() + perpendicular.getZ() * offset);
            }

            Point3D yAxis = new Point3D(0, 1, 0);
            Point3D axis = yAxis.crossProduct(direction.normalize());
            double angle = Math.acos(yAxis.dotProduct(direction.normalize()));

            if (axis.magnitude() > 0.0001) {
                bond.setRotationAxis(axis);
                bond.setRotate(Math.toDegrees(angle));
            }

            moleculeGroup.getChildren().add(bond);
        }
    }

    private double calculateBondAngle(Point3D p1, Point3D center, Point3D p2) {
        Point3D v1 = p1.subtract(center).normalize();
        Point3D v2 = p2.subtract(center).normalize();
        double dot = v1.dotProduct(v2);
        return Math.toDegrees(Math.acos(Math.max(-1.0, Math.min(1.0, dot))));
    }

    private void drawBondAngleArc(Point3D p1, Point3D center, Point3D p2, double angle) {
        // Draw red arc between two bonds
        Point3D v1 = p1.subtract(center).normalize();
        Point3D v2 = p2.subtract(center).normalize();

        double arcRadius = 25; // Radius of the arc
        int segments = 30; // More segments for smoother arc

        // Calculate rotation axis (perpendicular to both vectors)
        Point3D axis = v1.crossProduct(v2).normalize();

        if (axis.magnitude() < 0.001)
            return; // Vectors are parallel

        // Create arc using small cylinders
        for (int i = 0; i < segments; i++) {
            double t1 = (double) i / segments;
            double t2 = (double) (i + 1) / segments;

            // Interpolate between v1 and v2
            Point3D dir1 = rotateVector(v1, axis, angle * t1).multiply(arcRadius);
            Point3D dir2 = rotateVector(v1, axis, angle * t2).multiply(arcRadius);

            // Draw small cylinder segment
            Point3D start = center.add(dir1);
            Point3D end = center.add(dir2);

            drawArcSegment(start, end);
        }

        // No text label needed - angle info shown in side panel
    }

    private void drawArcSegment(Point3D start, Point3D end) {
        Point3D direction = end.subtract(start);
        double length = direction.magnitude();

        if (length < 0.1)
            return;

        Cylinder segment = new Cylinder(1, length);
        PhongMaterial mat = new PhongMaterial(Color.web("#ff6b6b")); // Red color
        segment.setMaterial(mat);

        Point3D mid = start.midpoint(end);
        segment.setTranslateX(mid.getX());
        segment.setTranslateY(mid.getY());
        segment.setTranslateZ(mid.getZ());

        // Rotate segment to align with direction
        Point3D yAxis = new Point3D(0, 1, 0);
        Point3D axis = yAxis.crossProduct(direction.normalize());
        double angle = Math.acos(yAxis.dotProduct(direction.normalize()));

        if (axis.magnitude() > 0.0001) {
            segment.setRotationAxis(axis);
            segment.setRotate(Math.toDegrees(angle));
        }

        moleculeGroup.getChildren().add(segment);
    }

    private Point3D rotateVector(Point3D vector, Point3D axis, double angleDeg) {
        double angleRad = Math.toRadians(angleDeg);
        double cos = Math.cos(angleRad);
        double sin = Math.sin(angleRad);
        double oneMinusCos = 1.0 - cos;

        double x = axis.getX();
        double y = axis.getY();
        double z = axis.getZ();

        double[][] rotMatrix = {
                {cos + x * x * oneMinusCos, x * y * oneMinusCos - z * sin, x * z * oneMinusCos + y * sin},
                {y * x * oneMinusCos + z * sin, cos + y * y * oneMinusCos, y * z * oneMinusCos - x * sin},
                {z * x * oneMinusCos - y * sin, z * y * oneMinusCos + x * sin, cos + z * z * oneMinusCos}
        };

        double vx = vector.getX();
        double vy = vector.getY();
        double vz = vector.getZ();

        return new Point3D(
                rotMatrix[0][0] * vx + rotMatrix[0][1] * vy + rotMatrix[0][2] * vz,
                rotMatrix[1][0] * vx + rotMatrix[1][1] * vy + rotMatrix[1][2] * vz,
                rotMatrix[2][0] * vx + rotMatrix[2][1] * vy + rotMatrix[2][2] * vz);
    }

    private Color getAtomColor(String atom) {
        Map<String, Color> colors = new HashMap<>();
        colors.put("H", Color.web("#ffffff"));
        colors.put("C", Color.web("#909090"));
        colors.put("N", Color.web("#3050f8"));
        colors.put("O", Color.web("#ff0d0d"));
        colors.put("F", Color.web("#90e050"));
        colors.put("Cl", Color.web("#1ff01f"));
        colors.put("Br", Color.web("#a62929"));
        colors.put("S", Color.web("#ffff30"));
        colors.put("P", Color.web("#ff8000"));
        return colors.getOrDefault(atom, Color.GRAY);
    }

    private void updateGeometryInfo() {
        VSEPRGeometry geometry = molecule.getGeometry();

        // Update electron geometry label
        if (electronGeometryLabel != null) {
            electronGeometryLabel.setText(geometry.electronGeometry);
        }

        // Update molecular geometry label
        if (molecularGeometryLabel != null) {
            molecularGeometryLabel.setText(geometry.name);
        }

        // Keep old label for compatibility (if still used elsewhere)
        if (geometryInfoLabel != null) {
            String shapeName = geometry.name;
            shapeName = shapeName.replaceAll("\\s*\\([^)]*\\)", "");
            geometryInfoLabel.setText("Shape: " + shapeName);
        }

        if (bondAngleLabel != null) {
            bondAngleLabel.setText("Bond Angles: " + geometry.bondAngles);
        }
    }

    private void loadPresetMolecule(String selection) {
        molecule.clear();

        switch (selection) {
            case "H₂O - Water":
                // 2 H atoms with single bonds, 2 lone pairs
                molecule.addAtom("H", 1);
                molecule.addAtom("H", 1);
                molecule.addLonePair();
                molecule.addLonePair();
                break;

            case "CO₂ - Carbon Dioxide":
                // 2 O atoms with double bonds (linear)
                molecule.addAtom("O", 2);
                molecule.addAtom("O", 2);
                break;

            case "NH₃ - Ammonia":
                // 3 H atoms with single bonds, 1 lone pair
                molecule.addAtom("H", 1);
                molecule.addAtom("H", 1);
                molecule.addAtom("H", 1);
                molecule.addLonePair();
                break;

            case "CH₄ - Methane":
                // 4 H atoms with single bonds (tetrahedral)
                molecule.addAtom("H", 1);
                molecule.addAtom("H", 1);
                molecule.addAtom("H", 1);
                molecule.addAtom("H", 1);
                break;

            case "BF₃ - Boron Trifluoride":
                // 3 F atoms with single bonds (trigonal planar)
                molecule.addAtom("F", 1);
                molecule.addAtom("F", 1);
                molecule.addAtom("F", 1);
                break;

            case "SF₆ - Sulfur Hexafluoride":
                // 6 F atoms with single bonds (octahedral)
                molecule.addAtom("F", 1);
                molecule.addAtom("F", 1);
                molecule.addAtom("F", 1);
                molecule.addAtom("F", 1);
                molecule.addAtom("F", 1);
                molecule.addAtom("F", 1);
                break;

            case "SO₂ - Sulfur Dioxide":
                // 2 O atoms with double bonds, 1 lone pair (bent)
                molecule.addAtom("O", 2);
                molecule.addAtom("O", 2);
                molecule.addLonePair();
                break;

            case "XeF₂ - Xenon Difluoride":
                // 2 F atoms with single bonds, 3 lone pairs (linear from trigonal bipyramidal)
                molecule.addAtom("F", 1);
                molecule.addAtom("F", 1);
                molecule.addLonePair();
                molecule.addLonePair();
                molecule.addLonePair();
                break;

            case "ClF₃ - Chlorine Trifluoride":
                // 3 F atoms with single bonds, 2 lone pairs (T-shaped)
                molecule.addAtom("F", 1);
                molecule.addAtom("F", 1);
                molecule.addAtom("F", 1);
                molecule.addLonePair();
                molecule.addLonePair();
                break;

            case "XeF₄ - Xenon Tetrafluoride":
                // 4 F atoms with single bonds, 2 lone pairs (square planar)
                molecule.addAtom("F", 1);
                molecule.addAtom("F", 1);
                molecule.addAtom("F", 1);
                molecule.addAtom("F", 1);
                molecule.addLonePair();
                molecule.addLonePair();
                break;

            case "BrF₅ - Bromine Pentafluoride":
                // 5 F atoms with single bonds, 1 lone pair (square pyramidal)
                molecule.addAtom("F", 1);
                molecule.addAtom("F", 1);
                molecule.addAtom("F", 1);
                molecule.addAtom("F", 1);
                molecule.addAtom("F", 1);
                molecule.addLonePair();
                break;

            case "PCl₅ - Phosphorus Pentachloride":
                // 5 Cl atoms with single bonds (trigonal bipyramidal)
                molecule.addAtom("Cl", 1);
                molecule.addAtom("Cl", 1);
                molecule.addAtom("Cl", 1);
                molecule.addAtom("Cl", 1);
                molecule.addAtom("Cl", 1);
                break;

            case "Custom (Build Your Own)":
            default:
                // Keep empty for custom building
                break;
        }

        updateMolecule();
    }
}
