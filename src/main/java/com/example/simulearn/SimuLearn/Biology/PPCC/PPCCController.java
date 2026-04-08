package com.example.simulearn.SimuLearn.Biology.PPCC;

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
import javafx.scene.image.Image;
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
import java.util.HashMap;
import java.util.ResourceBundle;

public class PPCCController implements Initializable {

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

    private PPCClabworkspace ppcclabworkspace;

    private VBox instructionCommandPanel;
    private int currentProtocolStep = 1;
    private final int TOTAL_PROTOCOL_STEPS = 9;
    private VBox currentInstructionsList;
    private String currentHighlightedInstruction = null;

    private int prediction1SelectedDescription = -1;
    private int prediction1SelectedReason = -1;

    private int[] prediction2BandOrder = {0, 1, 2};

    private int activeStep = 1;
    private String activeSubStep = "a";

    private HashMap<String, Boolean> stepCompletionMap = new HashMap<>();

    private final String[][] STEP_SEQUENCES = {
            {"a", "b", "c", "d", "e", "f", "g", "h", "i"},
            {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n"},
            {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k"},
            {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m"},
            {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o"},
            {"a", "b", "c", "d", "e", "f", "g", "h"},
            {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j"},
            {"a", "b", "c", "d", "e", "f", "g"},
            {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j"}
    };

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
        instruction.setStyle(
                "-fx-background-color: rgba(4,12,18,0.95);" +
                        "-fx-border-color: rgba(34,211,238,0.18);" +
                        "-fx-border-radius: 14;" +
                        "-fx-background-radius: 14;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.55), 24, 0.2, 0, 4);"
        );
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
        title.setStyle("-fx-text-fill: #1a5409;");
        Separator underline = new Separator();
        underline.setMaxWidth(200);
        underline.setStyle("-fx-background-color: #00aaff;");
        underline.setPrefHeight(3);

        Label text = new Label("In this simulation, you will purify protein expressed in E. coli cells from a recombinant plasmid. In order to obtain the protein, you will first use lysis buffer to open the transformed E. coli cells.");
        text.setWrapText(true);
        text.setFont(Font.font("DM Sans Medium", 16));
        text.setLineSpacing(3);
        text.setStyle("-fx-text-fill: #333333;");

        Label whatToTitle = new Label("What do you need to know before beginning this lab?");
        whatToTitle.setFont(Font.font("DM Sans Medium", FontWeight.BOLD, 18));
        whatToTitle.setStyle("-fx-text-fill: #333333;");
        whatToTitle.setPadding(new Insets(20, 0, 10, 0));

        Label text2 = new Label("Before lysing the cells, it is important to increase the quantity of your protein of interest, in this case red fluorescent protein (RFP). The RFP is expressed from a recombinant plasmid that contains the rfp gene under the control of an arabinose-dependent promoter, as well as a gene for ampicillin resistance. A single transformed bacterial colony containing the introduced red fluorescent protein (rfp) gene is transferred to a flask containing Luria broth (LB) and ampicillin and incubated at 37°C. This lets the transformed cells replicate, creating a large population of identical cells and increasing the amount of RFP protein expressed. Ampicillin is an antibiotic that is added to the growth media to selectively allow only the transformed cells to grow. As the transformed cells divide, the plasmid is also copied and transferred to daughter cells. Arabinose is then added to the culture to activate rfp expression. The transformed bacterial cells are now protein factories, all producing RFP.");
        text2.setWrapText(true);
        text2.setFont(Font.font("DM Sans Medium", 16));
        text2.setLineSpacing(3);
        text2.setStyle("-fx-text-fill: #333333;");

        String imagePath = getClass().getResource("/Images/PPCCImages/Context-1-en.png").toExternalForm();
        ImageView image1 = new ImageView(new Image(imagePath));
        image1.setFitWidth(580);
        image1.setFitHeight(600);
        image1.setPreserveRatio(true);
        image1.setSmooth(true);
        StackPane imagePane = new StackPane(image1);
        imagePane.setStyle(
                "-fx-background-color: #2B5F7F;" +
                        "-fx-border-color: #d0d0d0;" +
                        "-fx-border-width: 1;" +
                        "-fx-border-radius: 8;" +
                        "-fx-background-radius: 8;" +
                        "-fx-padding: 15;" +
                        "-fx-cursor: hand;" +
                        "-fx-effect: dropshadow(three-pass-box, rgba(60, 64, 67, 0.3), 15, 0, 0, 6);"
        );
        imagePane.setPadding(new Insets(10));
        imagePane.setPrefHeight(550);
        imagePane.setAlignment(Pos.CENTER);

        Label whatwill = new Label("What will you be doing in this simulation?");
        whatwill.setFont(Font.font("DM Sans Medium", FontWeight.BOLD, 18));
        whatwill.setStyle("-fx-text-fill: #333333;");
        whatwill.setPadding(new Insets(20, 0, 10, 0));

        Label text3 = new Label("You have been given an aliquot of this culture containing RFP-producing \uD835\uDE0C. \uD835\uDE24\uD835\uDE30\uD835\uDE2D\uD835\uDE2A cells. Your goal is to extract and purify the red fluorescent protein. First, you will need to lyse the cells using a lysis buffer, which breaks open the bacterial cell walls. This releases many different proteins, nucleic acids, cell wall components, and other cellular debris. Next, you will use centrifugation to isolate the soluble components of the cell, which include your protein of interest. Finally the proteins in solution will be subjected to column chromatography, which specifically isolates and purifies the desired RFP protein.");
        text3.setWrapText(true);
        text3.setFont(Font.font("DM Sans Medium", 16));
        text3.setLineSpacing(3);
        text3.setStyle("-fx-text-fill: #333333;");

        ImageView image2 = SVGLoader.loadSVG("/Images/PPCCImages/Context-2-en.svg", 580, 612);

        StackPane imagePane2 = new StackPane(image2);
        imagePane2.setStyle(
                "-fx-background-color: #2B5F7F;" +
                        "-fx-border-color: #d0d0d0;" +
                        "-fx-border-width: 1;" +
                        "-fx-border-radius: 8;" +
                        "-fx-background-radius: 8;" +
                        "-fx-padding: 15;" +
                        "-fx-cursor: hand;" +
                        "-fx-effect: dropshadow(three-pass-box, rgba(60, 64, 67, 0.3), 15, 0, 0, 6);"
        );
        imagePane2.setPadding(new Insets(10));
        imagePane2.setPrefHeight(50);
        imagePane2.setAlignment(Pos.CENTER);

        Label whatAre = new Label("What are the safety considerations in this lab?");
        whatAre.setFont(Font.font("DM Sans Medium", FontWeight.BOLD, 18));
        whatAre.setStyle("-fx-text-fill: #333333;");
        whatAre.setPadding(new Insets(20, 0, 10, 0));

        Label text4 = new Label("It’s important to remember that you are working with biohazardous bacteria. This means you should use aseptic techniques and work in a sterile environment to prevent contamination. You should not touch anything that has been in contact with the bacteria directly and must only hold equipment on the outside, if necessary. There should be strict disposal of all used tubes and pipette tips into a biohazardous waste container. Always remember to wash your hands with soap when you are done with the experiment.");
        text4.setWrapText(true);
        text4.setFont(Font.font("DM Sans Medium", 16));
        text4.setLineSpacing(3);
        text4.setStyle("-fx-text-fill: #333333;");

        content.getChildren().addAll(title, underline, text, whatToTitle, text2, imagePane, whatwill, text3, imagePane2, whatAre, text4);
        return content;

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

        btnResults.setDisable(true);
        btnResults.setOpacity(0.5);

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

            if (ppcclabworkspace == null) {
                ppcclabworkspace = new PPCClabworkspace(this);
            }

            mainpanel.getChildren().clear();
            mainpanel.getChildren().add(ppcclabworkspace.getppccLabWorkspace());
            VBox.setVgrow(ppcclabworkspace.getppccLabWorkspace(), Priority.ALWAYS);

            resetToLabView();
        });

        btnResults.setOnAction(e -> {

            mainpanel.getChildren().clear();
            mainpanel.getChildren().add(scrollPane);
            scrollPane.setVisible(true);
            scrollPane.setManaged(true);
            VBox.setVgrow(scrollPane, Priority.ALWAYS);

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
                createSpacer(8),
                btnMaterials,
                createSpacer(8),
                btnPredictions,
                createSpacer(8),
                btnProtocol,
                createSpacer(8),
                btnResults,
                createSpacer(8),
                btnReflection,
                createSpacer(8),
                btnSummary);

        setSelectedButton(btnContext);

        return panel;
    }

    private Button createNavButton(String number, String text) {
        String icon = switch (text) {
            case "CONTEXT" -> "📋";
            case "MATERIALS" -> "🧪";
            case "PREDICTIONS" -> "🔮";
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

        String selectedText = (selectedButton != null) ? selectedButton.getText() : null;
        selectedButton = null;

        instruction.getChildren().clear();
        buttonPanel = createButtonPanel();
        instruction.getChildren().add(buttonPanel);

        if (selectedText != null) {
            Button[] allBtns = {btnContext, btnMaterials, btnPredictions, btnProtocol, btnResults, btnReflection, btnSummary};
            for (Button b : allBtns) {
                if (b != null && b.getText() != null && b.getText().contains(selectedText.replaceAll(".*  ", ""))) {
                    setSelectedButton(b);
                    break;
                }
            }
        }
    }

    private VBox createSpacer(int height) {
        VBox spacer = new VBox();
        spacer.setPrefHeight(height);
        return spacer;
    }

    private VBox getPredictionsContent() {
        VBox content = new VBox(25);
        content.setPadding(new Insets(40));
        content.setStyle("-fx-background-color: #f5f5f5;");

        Label title = new Label("3. PREDICTIONS");
        title.setFont(Font.font("DM Sans Medium", FontWeight.BOLD, 32));
        title.setStyle("-fx-text-fill: #1a5490;");

        Separator underline = new Separator();
        underline.setMaxWidth(300);
        underline.setStyle("-fx-background-color: #00aaff;");
        underline.setPrefHeight(3);

        Label introText = new Label(
                "Before starting this experiment, a single transformed bacterial colony containing the recombinant plasmid with the red fluorescent protein (rfp) gene was transferred into a Luria broth (LB) flask containing ampicillin and arabinose and incubated at 37°C. This allowed the transformed cells to replicate and create a large culture of identical cells to increase the production of RFP."
        );
        introText.setWrapText(true);
        introText.setFont(Font.font("DM Sans Medium", 16));
        introText.setStyle("-fx-text-fill: #333333;");
        introText.setMaxWidth(700);
        introText.setLineSpacing(3);

        VBox prediction1Box = createPrediction1Box();

        VBox prediction2Box = createPrediction2Box();

        content.getChildren().addAll(title, underline, introText, prediction1Box, prediction2Box);
        return content;
    }

    private VBox createPrediction1Box() {
        VBox box = new VBox(15);
        box.setStyle(
                "-fx-background-color: #1a5490;" +
                        "-fx-border-color: #d0d0d0;" +
                        "-fx-border-width: 1;" +
                        "-fx-border-radius: 10;" +
                        "-fx-background-radius: 10;" +
                        "-fx-padding: 25;"
        );

        Label question = new Label("What do you expect the RFP-producing culture to look like?");
        question.setFont(Font.font("DM Sans Medium", FontWeight.BOLD, 18));
        question.setStyle("-fx-text-fill: black;");
        question.setWrapText(true);

        Label subText = new Label("Select a description below that best matches your prediction.");
        subText.setFont(Font.font("DM Sans Medium", 14));
        subText.setStyle("-fx-text-fill: black;");

        HBox columnsLayout = new HBox(15);
        columnsLayout.setAlignment(Pos.TOP_LEFT);

        VBox descColumn = new VBox(10);
        descColumn.setPrefWidth(280);

        Label descHeader = new Label("Description");
        descHeader.setFont(Font.font("DM Sans Medium", FontWeight.BOLD, 16));
        descHeader.setStyle(
                "-fx-text-fill: white;" +
                        "-fx-background-color: #1a3a5c;" +
                        "-fx-background-radius: 6;" +
                        "-fx-padding: 10 20;"
        );
        descHeader.setMaxWidth(Double.MAX_VALUE);
        descHeader.setAlignment(Pos.CENTER);

        String[][] tubeOptions = {
                {"/Images/PPCCImages/equipment/solution-tube-desc.svg", "Pale yellow, clear"},
                {"/Images/PPCCImages/equipment/solution-tube-desc-2.svg", "Pale yellow, slightly cloudy"},
                {"/Images/PPCCImages/equipment/solution-tube-desc-4.svg", "Red, clear"},
                {"/Images/PPCCImages/equipment/solution-tube-desc-3.svg", "Red, slightly cloudy"},
                {"/Images/PPCCImages/equipment/solution-tube-desc-5.svg", "Colorless, clear"}
        };

        VBox[] tubeRows = new VBox[tubeOptions.length];
        for (int i = 0; i < tubeOptions.length; i++) {
            final int idx = i;
            HBox row = new HBox(12);
            row.setAlignment(Pos.CENTER_LEFT);
            row.setPadding(new Insets(10, 15, 10, 15));
            row.setStyle(getP1DescStyle(prediction1SelectedDescription == idx));

            ImageView tubeImg = SVGLoader.loadSVG(tubeOptions[i][0], 35, 55, true);
            if (tubeImg == null) {

                javafx.scene.shape.Rectangle rect = new javafx.scene.shape.Rectangle(25, 50);
                rect.setFill(i == 2 || i == 3 ? Color.web("#e63946") : i == 4 ? Color.TRANSPARENT : Color.web("#f7e07a"));
                rect.setStroke(Color.web("#aaaaaa"));
                StackPane imgPlaceholder = new StackPane(rect);
                imgPlaceholder.setPrefSize(35, 55);
                row.getChildren().add(imgPlaceholder);
            } else {
                tubeImg.setFitWidth(35);
                tubeImg.setFitHeight(55);
                tubeImg.setPreserveRatio(true);
                row.getChildren().add(tubeImg);
            }

            Label descLabel = new Label(tubeOptions[i][1]);
            descLabel.setFont(Font.font("DM Sans Medium", 14));
            descLabel.setStyle("-fx-text-fill: #333333;");
            descLabel.setWrapText(true);
            row.getChildren().add(descLabel);

            VBox rowWrapper = new VBox(row);
            rowWrapper.setStyle(getP1DescStyle(prediction1SelectedDescription == idx));
            tubeRows[idx] = rowWrapper;

            rowWrapper.setOnMouseClicked(e -> {
                prediction1SelectedDescription = idx;

                for (int j = 0; j < tubeRows.length; j++) {
                    tubeRows[j].setStyle(getP1DescStyle(prediction1SelectedDescription == j));
                }
            });
            rowWrapper.setStyle("-fx-cursor: hand;");

            descColumn.getChildren().add(rowWrapper);
        }

        for (int j = 0; j < tubeRows.length; j++) {
            tubeRows[j].setStyle(getP1DescStyle(prediction1SelectedDescription == j) + "-fx-cursor: hand;");
        }

        VBox reasonColumn = new VBox(10);
        HBox.setHgrow(reasonColumn, Priority.ALWAYS);

        Label reasonHeader = new Label("Reasons");
        reasonHeader.setFont(Font.font("DM Sans Medium", FontWeight.BOLD, 16));
        reasonHeader.setStyle(
                "-fx-text-fill: white;" +
                        "-fx-background-color: #1a3a5c;" +
                        "-fx-background-radius: 6;" +
                        "-fx-padding: 10 20;"
        );
        reasonHeader.setMaxWidth(Double.MAX_VALUE);
        reasonHeader.setAlignment(Pos.CENTER);

        String[] reasons = {
                "Culture was grown in yellow LB.",
                "You cannot see bacterial growth with the naked eye.",
                "The bacteria replicated in LB/ara and produced RFP in large amounts.",
                "The bacteria replicated in LB/amp/ara and produced RFP, which remained inside the cells.",
                "When bacteria replicate in large amounts in liquid culture, the culture will become cloudy."
        };

        VBox[] reasonRows = new VBox[reasons.length];
        reasonColumn.getChildren().add(reasonHeader);

        for (int i = 0; i < reasons.length; i++) {
            final int idx = i;
            VBox reasonRow = new VBox();
            reasonRow.setPadding(new Insets(12, 15, 12, 15));
            reasonRow.setStyle(getP1ReasonStyle(prediction1SelectedReason == idx) + "-fx-cursor: hand;");

            Label reasonLabel = new Label(reasons[i]);
            reasonLabel.setFont(Font.font("DM Sans Medium", 14));
            reasonLabel.setStyle("-fx-text-fill: #333333;");
            reasonLabel.setWrapText(true);
            reasonRow.getChildren().add(reasonLabel);

            reasonRows[idx] = reasonRow;
            reasonRow.setOnMouseClicked(e -> {
                prediction1SelectedReason = idx;
                for (int j = 0; j < reasonRows.length; j++) {
                    reasonRows[j].setStyle(getP1ReasonStyle(prediction1SelectedReason == j) + "-fx-cursor: hand;");
                }
            });

            reasonColumn.getChildren().add(reasonRow);
        }

        descColumn.getChildren().add(0, descHeader);
        columnsLayout.getChildren().addAll(descColumn, reasonColumn);

        box.getChildren().addAll(question, subText, columnsLayout);
        return box;
    }

    private String getP1DescStyle(boolean selected) {
        if (selected) {
            return "-fx-background-color: #cce5ff;" +
                    "-fx-border-color: #1a5490;" +
                    "-fx-border-width: 2;" +
                    "-fx-border-radius: 6;" +
                    "-fx-background-radius: 6;";
        } else {
            return "-fx-background-color: white;" +
                    "-fx-border-color: #d0d0d0;" +
                    "-fx-border-width: 1;" +
                    "-fx-border-radius: 6;" +
                    "-fx-background-radius: 6;";
        }
    }

    private String getP1ReasonStyle(boolean selected) {
        if (selected) {
            return "-fx-background-color: #cce5ff;" +
                    "-fx-border-color: #1a5490;" +
                    "-fx-border-width: 2;" +
                    "-fx-border-radius: 6;" +
                    "-fx-background-radius: 6;";
        } else {
            return "-fx-background-color: white;" +
                    "-fx-border-color: #d0d0d0;" +
                    "-fx-border-width: 1;" +
                    "-fx-border-radius: 6;" +
                    "-fx-background-radius: 6;";
        }
    }

    private VBox createPrediction2Box() {
        VBox box = new VBox(15);
        box.setStyle(
                "-fx-background-color: white;" +
                        "-fx-border-color: #d0d0d0;" +
                        "-fx-border-width: 1;" +
                        "-fx-border-radius: 10;" +
                        "-fx-background-radius: 10;" +
                        "-fx-padding: 25;"
        );

        Label chromoTitle = new Label("How Does Hydrophobic Column Chromatography Separate Proteins?");
        chromoTitle.setFont(Font.font("DM Sans Medium", FontWeight.BOLD, 18));
        chromoTitle.setStyle("-fx-text-fill: #1a5409;");
        chromoTitle.setWrapText(true);

        Label chromoDesc = new Label(
                "The protein of interest will be separated from other proteins in the cells with a chromatography column that takes advantage of chemical characteristics of the protein. To separate RFP, which is very hydrophobic, the chromatography column needs to be packed with resin-coated beads that bind to hydrophobic proteins. The more hydrophobic the protein, the stronger it will bind to the hydrophobic resin. All proteins will be unfolded in a binding buffer solution before being added to the column so hydrophobic residues will be exposed to the column resin. The separation of different proteins in the column depends on the properties of the molecules and the extent of their interactions with the resin. Strong interaction will result in proteins taking a longer time to wash out of the column, or elute, while weak interactions will result in proteins passing through the column more quickly.\n\nBelow are three groups of proteins exhibiting differences in hydrophobicity: highly hydrophobic RFP (red), moderately hydrophobic cellular proteins (purple), and hydrophilic cellular proteins (blue)."
        );
        chromoDesc.setWrapText(true);
        chromoDesc.setFont(Font.font("DM Sans Medium", 14));
        chromoDesc.setStyle("-fx-text-fill: #333333;");
        chromoDesc.setMaxWidth(700);
        chromoDesc.setLineSpacing(3);

        VBox reorderBox = new VBox(12);
        reorderBox.setStyle(
                "-fx-background-color: #1a3a5c;" +
                        "-fx-border-radius: 8;" +
                        "-fx-background-radius: 8;" +
                        "-fx-padding: 20;"
        );

        Label reorderTitle = new Label("Re-order protein bands");
        reorderTitle.setFont(Font.font("DM Sans Medium", FontWeight.BOLD, 18));
        reorderTitle.setStyle("-fx-text-fill: white;");

        Label reorderInstr = new Label("Drag and drop the protein bands onto the resin beads and arrange them in the order you predict the proteins to travel through the resin.");
        reorderInstr.setWrapText(true);
        reorderInstr.setFont(Font.font("DM Sans Medium", 13));
        reorderInstr.setStyle("-fx-text-fill: #ccddee;");
        reorderInstr.setMaxWidth(650);

        VBox bandsArea = createDraggableBandsArea();

        reorderBox.getChildren().addAll(reorderTitle, reorderInstr, bandsArea);
        box.getChildren().addAll(chromoTitle, chromoDesc, reorderBox);
        return box;
    }

    private VBox createDraggableBandsArea() {

        String[][] bandData = {
                {"#8B00FF", "Moderately hydrophobic Proteins"},
                {"#e63946", "Highly hydrophobic Proteins (RFP)"},
                {"#1565C0", "Hydrophilic Proteins"}
        };

        VBox bandsContainer = new VBox(8);
        bandsContainer.setPadding(new Insets(10, 0, 10, 0));

        rebuildBands(bandsContainer, bandData);

        return bandsContainer;
    }

    private void rebuildBands(VBox bandsContainer, String[][] bandData) {
        bandsContainer.getChildren().clear();

        for (int pos = 0; pos < prediction2BandOrder.length; pos++) {
            final int position = pos;
            int bandIdx = prediction2BandOrder[pos];
            String color = bandData[bandIdx][0];
            String label = bandData[bandIdx][1];

            HBox row = new HBox(15);
            row.setAlignment(Pos.CENTER_LEFT);
            row.setPadding(new Insets(8, 10, 8, 10));
            row.setStyle(
                    "-fx-background-color: rgba(255,255,255,0.08);" +
                            "-fx-border-color: rgba(255,255,255,0.15);" +
                            "-fx-border-radius: 6;" +
                            "-fx-background-radius: 6;" +
                            "-fx-cursor: hand;"
            );

            javafx.scene.shape.Rectangle band = new javafx.scene.shape.Rectangle(120, 28);
            band.setFill(Color.web(color));
            band.setArcWidth(6);
            band.setArcHeight(6);

            VBox arrows = new VBox(2);
            arrows.setAlignment(Pos.CENTER);

            Button upBtn = new Button("▲");
            upBtn.setFont(Font.font("DM Sans Medium", 10));
            upBtn.setStyle(
                    "-fx-background-color: rgba(255,255,255,0.2);" +
                            "-fx-text-fill: white;" +
                            "-fx-padding: 2 6;" +
                            "-fx-border-radius: 3;" +
                            "-fx-background-radius: 3;" +
                            "-fx-cursor: hand;"
            );
            upBtn.setDisable(position == 0);

            Button downBtn = new Button("▼");
            downBtn.setFont(Font.font("DM Sans Medium", 10));
            downBtn.setStyle(
                    "-fx-background-color: rgba(255,255,255,0.2);" +
                            "-fx-text-fill: white;" +
                            "-fx-padding: 2 6;" +
                            "-fx-border-radius: 3;" +
                            "-fx-background-radius: 3;" +
                            "-fx-cursor: hand;"
            );
            downBtn.setDisable(position == prediction2BandOrder.length - 1);

            arrows.getChildren().addAll(upBtn, downBtn);

            Label bandLabel = new Label(label);
            bandLabel.setFont(Font.font("DM Sans Medium", FontWeight.BOLD, 13));
            bandLabel.setStyle("-fx-text-fill: white;");
            bandLabel.setWrapText(true);
            bandLabel.setMaxWidth(220);

            row.getChildren().addAll(band, arrows, bandLabel);
            bandsContainer.getChildren().add(row);

            upBtn.setOnAction(e -> {
                if (position > 0) {
                    int temp = prediction2BandOrder[position];
                    prediction2BandOrder[position] = prediction2BandOrder[position - 1];
                    prediction2BandOrder[position - 1] = temp;
                    rebuildBands(bandsContainer, bandData);
                }
            });

            downBtn.setOnAction(e -> {
                if (position < prediction2BandOrder.length - 1) {
                    int temp = prediction2BandOrder[position];
                    prediction2BandOrder[position] = prediction2BandOrder[position + 1];
                    prediction2BandOrder[position + 1] = temp;
                    rebuildBands(bandsContainer, bandData);
                }
            });
        }
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
        underline.setPrefHeight(12);

        Separator underline2 = new Separator();
        underline2.setMaxWidth(250);
        underline2.setStyle("-fx-background-color: #00aaff;");
        underline2.setPrefHeight(2);

        Separator underline3 = new Separator();
        underline3.setMaxWidth(250);
        underline3.setStyle("-fx-background-color: #00aaff;");
        underline3.setPrefHeight(2);

        Separator underline4 = new Separator();
        underline4.setMaxWidth(250);
        underline4.setStyle("-fx-background-color: #00aaff;");
        underline4.setPrefHeight(2);

        Separator underline5 = new Separator();
        underline5.setMaxWidth(250);
        underline5.setStyle("-fx-background-color: #00aaff;");
        underline5.setPrefHeight(2);

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

        Label emptyTube = new Label("EMPTY TUBES");
        emptyTube.setFont(Font.font("DM Sans Medium", FontWeight.BOLD, 18));
        emptyTube.setStyle("-fx-text-fill: #1a5490;");

        Label emptytubeSubtitle = new Label("Used to hold solutions");
        emptytubeSubtitle.setFont(Font.font("DM Sans Medium", 14));
        emptytubeSubtitle.setStyle("-fx-text-fill: #666666;");

        GridPane emptyTubeGrid = createEmptyTubeGrid();

        Label otherEquipmentsTitle = new Label("Other Equipment");
        otherEquipmentsTitle.setFont(Font.font("DM Sans Medium", 14));
        otherEquipmentsTitle.setStyle("-fx-text-fill: #1a5490;");

        GridPane otherEquipmentGrid = createOtherEquipmentGrid();

        content.getChildren().addAll(
                title, underline, infoText, reagentsTitle, underline2, reagentsSubtitle,
                reagentsGrid, micropipettingTitle, underline3, micropipettingSubtitle, micropipettingGrid,
                emptyTube, underline4, emptytubeSubtitle, emptyTubeGrid, otherEquipmentsTitle, underline5, otherEquipmentGrid
        );
        return content;
    }

    private GridPane createReagentsGrid() {
        GridPane gridPane = new GridPane();
        gridPane.setHgap(15);
        gridPane.setVgap(15);
        gridPane.setPadding((new Insets(20, 0, 20, 0)));

        String[][] reagents = {
                {"/Images/PPCCImages/solution-tube-ec-closed-red.svg", "\uD835\uDE0C. \uD835\uDE24\uD835\uDE30\uD835\uDE2D\uD835\uDE2A (EC1) Solution", "#ff6b6b"},
                {"/Images/PPCCImages/solution-tube-ec-2-closed-red.svg", "\uD835\uDE0C. \uD835\uDE24\uD835\uDE30\uD835\uDE2D\uD835\uDE2A (EC2) Solution", "#ffd93d"},
                {"/Images/PPCCImages/solution-tube-eb-closed.svg", "Elution Buffer (EB)", "#6bcf7f"},
                {"/Images/PPCCImages/solution-tube-ly-b-closed.svg", "Lysis Buffer (LyB)", "#4d96ff"},
                {"/Images/PPCCImages/solution-tube-bb-closed.svg", "Binding Buffer (BB)", "#ff6b6b"},
                {"/Images/PPCCImages/solution-tube-wb-closed.svg", "Wash Buffer (WB)", "#ffd93d"},
                {"/Images/PPCCImages/solution-tube-ceb-closed.svg", "Column Equilibration Buffer (CEB)", "#6bcf7f"},
                {"/Images/PPCCImages/solution-tube-water-closed.svg", "Balancing Tube", "#4d96ff"}
        };
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

    private GridPane createMicropipettingGrid() {
        GridPane grid = new GridPane();
        grid.setHgap(15);
        grid.setVgap(15);
        grid.setPadding(new Insets(20, 0, 20, 0));

        String[][] pipettes = {
                {"/Images/PPCCImages/pipettes/P2.svg", "P2 Pipette", "#ff6b6b"},
                {"/Images/PPCCImages/pipettes/P20.svg", "P20 Pipette", "#ffd93d"},
                {"/Images/PPCCImages/pipettes/P200.svg", "P200 Pipette", "#6bcf7f"},
                {"/Images/PPCCImages/pipettes/P1000.svg", "P1000 Pipette", "#4d96ff"}
        };

        String[][] tipBoxes = {
                {"/Images/PPCCImages/tipboxes/P2_Tip_Box.svg", "P2 Tip Box", "#ff6b6b"},
                {"/Images/PPCCImages/tipboxes/P20_Tip_Box.svg", "P20 Tip Box", "#ffd93d"},
                {"/Images/PPCCImages/tipboxes/P200_Tip_Box.svg", "P200 Tip Box", "#6bcf7f"},
                {"/Images/PPCCImages/tipboxes/P1000_Tip_Box.svg", "P1000 Tip Box", "#4d96ff"}
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

    private GridPane createEmptyTubeGrid() {
        GridPane grid = new GridPane();
        grid.setHgap(15);
        grid.setVgap(15);
        grid.setPadding(new Insets(20, 0, 20, 0));

        String[][] emptyTube = {
                {"/Images/PPCCImages/solution-tube-rfp-empty.svg", "RFP Tube", "#ff6b6b"},
                {"/Images/PPCCImages/solution-tube-super-empty.svg", "Super tube", "#ff6b6b"}
        };
        int col = 0;

        for (String[] tube : emptyTube) {
            StackPane item = createMaterialItem(tube[0], tube[1], tube[2], 80, 80);
            grid.add(item, col, 0);
            col++;
        }
        return grid;
    }

    private GridPane createOtherEquipmentGrid() {

        GridPane gridPane = new GridPane();
        gridPane.setHgap(15);
        gridPane.setVgap(15);
        gridPane.setPadding((new Insets(20, 0, 20, 0)));

        String[][] equipments = {
                {"/Images/PPCCImages/equipment/Microcentrifuge.svg", "Microcentrifuge", "#ff6b6b"},
                {"/Images/PPCCImages/trash-biohazard.svg", "Biohazardous Waste", "#ff6b6b"},
                {"/Images/PPCCImages/waste-collection-jar.svg", "Waste Collection Jar", "#ff6b6b"},
                {"/Images/PPCCImages/timer.svg", "Timer", "#ff6b6b"},
                {"/Images/PPCCImages/equipment/vortex.svg", "Vortex", "#ff6b6b"},
                {"/Images/PPCCImages/column-blue-full-closed.svg", "Chromatography Column", "#ff6b6b"}

        };
        int col = 0;
        int row = 0;
        for (String[] equipment : equipments) {
            StackPane item = createMaterialItem(equipment[0], equipment[1], equipment[2], 80, 80);
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

    private String getMaterialDescription(String materialName) {
        String cleanName = materialName.trim().replace("\n", " ");

        if (cleanName.contains("\uD835\uDE0C. \uD835\uDE24\uD835\uDE30\uD835\uDE2D\uD835\uDE2A (EC1) Solution"))
            return "1ml of an \uD835\uDE0C. \uD835\uDE24\uD835\uDE30\uD835\uDE2D\uD835\uDE2A culture grown from a single transformed colony in a medium to enhance \uD835\uDE33\uD835\uDE27\uD835\uDE31 expression, labeled EC1.";
        else if (cleanName.contains("\uD835\uDE0C. \uD835\uDE24\uD835\uDE30\uD835\uDE2D\uD835\uDE2A (EC2) Solution"))
            return "1ml of an \uD835\uDE0C. \uD835\uDE24\uD835\uDE30\uD835\uDE2D\uD835\uDE2A culture grown from a single transformed colony in a medium to enhance \uD835\uDE33\uD835\uDE27\uD835\uDE31 expression, labeled EC2.";
        else if (cleanName.contains("Elution Buffer (EB)"))
            return "15ml tube with screw cab and red label containing 5ml of 10 mM Tris and 1 mM EDTA EB buffer to release hydrophobic proteins.";
        else if (cleanName.contains("Lysis Buffer (LyB)"))
            return "1.5mL tube labeled LyB with 160 µl of LyB.";
        else if (cleanName.contains("Binding Buffer (BB)"))
            return "15ml tube with screwcap containing 5ml binding buffer - 4.0 M (NH₄)₂SO₄ to wash out proteins that are hydrophilic.";
        else if (cleanName.contains("Wash Buffer (WB)"))
            return "15ml tube with screwcap containing 5ml wash buffer - 1.3 M (NH₄)₂SO₄ to wash out proteins that are somewhat hydrophobic.";
        else if (cleanName.contains("Column Equilibration Buffer (CEB)"))
            return "15ml tube with screwcap containing 5ml column equilibration buffer - 4.0 M (NH₄)₂SO₄.";
        else if (cleanName.contains("Balancing Tube"))
            return "Tube filled with water to assist the balancing of tubes in the microcentrifuge.";

        else if (cleanName.contains("P2 Pipette")) {
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
        } else if (cleanName.contains("RFP Tube")) {
            return "1.5ml empty solution tube labeled RFP.";
        } else if (cleanName.contains("Super Tube")) {
            return "1.5ml empty solution tube labeled SUPER.";
        } else if (cleanName.contains("Microcentrifuge")) {
            return "Motor-driven centrifuge that spins samples at high speed to collect liquid at the bottom of the tube for easy transfer.";
        } else if (cleanName.contains("Biohazardous Waste")) {
            return "Waste container. Used to dispose of any hazardous waste resulting from the experiment, including micropipette tips.";
        } else if (cleanName.contains("Waste Collection Jar")) {
            return "Empty transparent jar to receive discarded liquids";
        } else if (cleanName.contains("Timer")) {
            return "Used to monitor time during key points in the protocol.";
        } else if (cleanName.contains("Vortex")) {
            return "Motorized device used to mix small volumes of liquid by agitating the tube.";
        } else if (cleanName.contains("Chromatography Column")) {
            return "A hydrophobic column that allows molecules in a liquid to separate by hydrophobicity as they move down the column toward the valve. Used to separate different chemical compounds.";
        }
        return cleanName;
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

    private VBox getReflectionContent() {
        VBox content = new VBox(30);
        content.setPadding(new Insets(40));
        content.setStyle("-fx-background-color: #f5f5f5;");

        Label title = new Label("6. REFLECTION");
        title.setFont(Font.font("DM Sans Medium", FontWeight.BOLD, 32));
        title.setStyle("-fx-text-fill: #1a5490;");

        Separator underline = new Separator();
        underline.setMaxWidth(300);
        underline.setStyle("-fx-background-color: #00aaff;");
        underline.setPrefHeight(3);

        Label introText = new Label("Test your understanding of the protein purification process by answering these questions.");
        introText.setFont(Font.font("DM Sans Medium", 16));
        introText.setStyle("-fx-text-fill: #333333;");
        introText.setWrapText(true);
        introText.setMaxWidth(700);

        QuizQuestion quiz1 = createQuizQuestion(
                1,
                "Why is column chromatography useful?",
                new String[]{
                        "Column chromatography is used to purify proteins of interest for small- or large-scale production.",
                        "Column chromatography is used to separate large pools of cells containing the protein of interest based on cell size.",
                        "After cell lysis, column chromatography is used to separate cell debris, genetic material and cell organelles from the protein of interest."
                },
                0,
                "Column chromatography is an effective method for protein purification at various scales.",
                2
        );

        QuizQuestion quiz2 = createQuizQuestion(
                2,
                "What happens to bacterial cells during the cell lysis step?",
                new String[]{
                        "Proteins on the bacterial cell membrane and intracellular proteins are degraded to allow the release of genetic material.",
                        "The bacterial cell wall and cellular contents, including proteins, organelles and genetic material, are broken down by both mechanical and enzymatic activity.",
                        "The cell membrane is broken down, while the integrity of the cell contents such as proteins, organelles, DNA and RNA are maintained."
                },
                1,
                "Cell lysis involves breaking down the cell wall and releasing all cellular contents.",
                2

        );

        QuizQuestion quiz3 = createQuizQuestion(
                3,
                "What is the purpose of the wash buffer (WB) applied to the column after the supernatant?",
                new String[]{
                        "The wash buffer is used to remove the red fluorescent protein from the column so that it can be collected.",
                        "The wash buffer is used to remove hydrophilic cellular proteins that are not bound to the beads.",
                        "The wash buffer is used to remove the hydrophobic cellular proteins that are bound to the beads."
                },
                1,
                "The wash buffer removes unwanted hydrophilic proteins that don't bind to the column.",
                2

        );

        QuizQuestion quiz4 = createQuizQuestion(
                4,
                "Why do you have to spin the cell lysate before adding the supernatant to the column?",
                new String[]{
                        "Spinning the cell lysate will concentrate all proteins in the pellet without the genetic material and broken-down cell membrane components.",
                        "Spinning the cell lysate will pellet cell debris leaving cellular proteins in the supernatant.",
                        "Spinning the cell lysate will separate proteins into layers by size. Since RFP is a small protein, it will be present in the top layer, or supernatant, while larger proteins will be present in the pellet."
                },
                1,
                "Centrifugation separates cell debris (pellet) from soluble proteins (supernatant).",
                2
        );

        QuizQuestion quiz5 = createQuizQuestion(
                5,
                "What is the purpose of the elution buffer (EB)?",
                new String[]{
                        "Elution buffer has a high salt concentration and the correct pH to cause protein unfolding, releasing the bound proteins such as RFP from the column into the collection tube.",
                        "Elution buffer is a wash step to release other hydrophilic cellular proteins from the column that are not considered the protein of interest (RFP).",
                        "Elution buffer has lower salt concentrations and will cause bound RFP to return to its folded protein conformation, releasing it from the beads and allowing collection of purified RFP."
                },
                2,
                "Elution buffer changes conditions to release the bound protein while maintaining its structure.",
                2
        );

        Label feedbackTitle = new Label("FEEDBACK QUESTION");
        feedbackTitle.setFont(Font.font("DM Sans Medium", FontWeight.BOLD, 24));
        feedbackTitle.setStyle("-fx-text-fill: #1a5490;");
        feedbackTitle.setPadding(new Insets(30, 0, 10, 0));

        QuizQuestion quiz6 = createFeedbackQuestion(
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
                introText,
                quiz1.getQuestionBox(),
                quiz2.getQuestionBox(),
                quiz3.getQuestionBox(),
                quiz4.getQuestionBox(),
                quiz5.getQuestionBox(),
                feedbackTitle,
                quiz6.getQuestionBox()
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
                "In this simulation, you learned how to purify a protein from lysed bacterial cells using column chromatography. The simulation explored how to:"
        );
        heading.setWrapText(true);
        heading.setFont(Font.font("DM Sans Medium", FontWeight.BOLD, 16));
        heading.setStyle("-fx-text-fill: #1a5490;");

        VBox points = new VBox(12);

        points.getChildren().addAll(
                createChecklistItem("Lyse a culture of bacterial cells that have been transformed with a recombinant plasmid expressing red fluorescent protein."),
                createChecklistItem("Prepare a hydrophobic interaction chromatography column and apply cellular proteins to beads within the column."),
                createChecklistItem("Use buffers of different salt concentrations to separate, purify and elute RFP, the protein of interest, from the chromatography column.")
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
                        "/Images/PPCCImages/tip1.svg",
                        "When working with bacteria, always practice aseptic technique to avoid contamination. Wear gloves when handling bacteria and clean the lab bench thoroughly with bleach or alcohol solutions."
                },
                {
                        "/Images/PPCCImages/tip2.svg",
                        "Make sure the column remains in an upright position as you are working, and the valve is in the correct position (horizontal indicates closed)."
                },
                {
                        "/Images/PPCCImages/tip3.svg",
                        "Make sure the column is kept wet at all times. If it runs dry, the resin can crack, resulting in the mixing of the different protein bands. If the resin becomes dry, add additional buffer to rehydrate before continuing the experiment."
                },
                {
                        "/Images/PPCCImages/tip6.svg",
                        "When adding liquid to a column, it is good practice to pipette the liquid down the side of the column so that it does not disrupt the resin bed."
                }
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
                "-fx-background-color: #336589;" +
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

    private void resetToLabView() {
        instruction.getChildren().clear();

        instructionCommandPanel = createInstructionCommandPanel();
        instruction.getChildren().add(instructionCommandPanel);

        loadProtocolStep(1);
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
                "1. Pellet the cells.",
                "2. Pellet additional cells.",
                "3. Resuspend the cell pellet in EB.",
                "4. Lyse the cells.",
                "5. Equilibrate the column.",
                "6. Equilibrate the soluble fraction in BB.",
                "7. Apply the soluble fraction to the column.",
                "8. Wash the column.",
                "9. Elute and collect the RFP protein."
        };

        String[][][] allInstructions = {

                {
                        {"a", "Select the microcentrifuge to open it.", String.valueOf(false)},
                        {"b", "Place the EC1 (\uD835\uDE0C. \uD835\uDE24\uD835\uDE30\uD835\uDE2D\uD835\uDE2A 1) tube, and the balancing tube into the microcentrifuge.", String.valueOf(false)},
                        {"c", "Balance the microcentrifuge by placing the tubes so filled receptacles are symmetrical.", String.valueOf(false)},
                        {"d", "Select the microcentrifuge to close it.", String.valueOf(false)},
                        {"e", "Set the microcentrifuge to spin for 10 minutes.", String.valueOf(false)},
                        {"f", "Open the microcentrifuge and move the EC1 tube back to the tube rack.", String.valueOf(false)},
                        {"g", "Open the EC1 tube.", String.valueOf(false)},
                        {"h", "Open the waste collection jar.", String.valueOf(false)},
                        {"i", "Pipette the supernatant (the liquid found above a solid substance) without disturbing the cell pellet at the bottom and discard the liquid into the waste collection jar.", String.valueOf(false)},
                },

                {
                        {"a", "Pick up the P1000 micropipette from the micropipette rack.", String.valueOf(false)},
                        {"b", "Use the volume setting to change the volume to 1000μl and select “Save Volume”.", String.valueOf(false)},
                        {"c", "Open the P1000 tip box.", String.valueOf(false)},
                        {"d", "Move the P1000 micropipette onto the P1000 tip box to attach a tip.", String.valueOf(false)},
                        {"e", "Close the P1000 tip box.", String.valueOf(false)},
                        {"f", "Open the EC2 tube. To increase the amount of red fluorescent protein applied to the column, we will now add more cells from the second tube.", String.valueOf(false)},
                        {"g", "Move the micropipette tip over the EC2 tube, press the plunger to the 1st stop, and release to draw up 1000 microliters (μl) of solution.", String.valueOf(false)},
                        {"h", "Move your micropipette over the EC1 tube and dispense the culture into the tube by holding down the plunger until it reaches the second stop.", String.valueOf(false)},
                        {"i", "Open the biohazard waste container.", String.valueOf(false)},
                        {"j", "Move the micropipette over the biohazard waste container and use the eject icon to eject the tip.", String.valueOf(false)},
                        {"k", "Spin EC1 tube for 10 minutes.", String.valueOf(false)},
                        {"l", "Open the microcentrifuge and move the EC1 tube back to the tube rack.", String.valueOf(false)},
                        {"m", "Open the EC1 tube.", String.valueOf(false)},
                        {"n", "Pipette the supernatant (the liquid found above a solid substance) without disturbing the cell pellet at the bottom and discard the liquid into the waste collection jar.", String.valueOf(false)}
                },

                {
                        {"a", "Pick up the P200 micropipette from the micropipette rack", String.valueOf(false)},
                        {"b", "Use the volume setting to change the volume to 150μl and select “Save Volume”", String.valueOf(false)},
                        {"c", "Open the P200 tip box.", String.valueOf(false)},
                        {"d", "Move the P200 micropipette onto the P200 tip box to attach a tip.", String.valueOf(false)},
                        {"e", "Close the P200 tip box.", String.valueOf(false)},
                        {"f", "Open the EB (elution buffer) tube.", String.valueOf(false)},
                        {"g", "Move the micropipette tip over the EB tube.", String.valueOf(false)},
                        {"h", "Draw up 150μl of Elution Buffer by pressing the plunger to the first stop and slowly releasing.", String.valueOf(false)},
                        {"i", "Move your micropipette over the EC1 tube and dispense the culture into the tube by holding down the plunger until it reaches the second stop.", String.valueOf(false)},
                        {"j", "Close the EC1 tube.", String.valueOf(false)},
                        {"k", "Move the micropipette over the biohazard waste container and use the eject icon to eject the tip.", String.valueOf(false)},
                },

                {
                        {"a", "Move the EC1 tube to the vortex for 10 seconds to resuspend the cell pellet.", String.valueOf(false)},
                        {"b", " Pick up the P200 micropipette from the micropipette rack.", String.valueOf(false)},
                        {"c", "Open the P200 tip box.", String.valueOf(false)},
                        {"d", "Move the P200 micropipette onto the P200 tip box to attach a tip.", String.valueOf(false)},
                        {"e", "Close the P200 tip box.", String.valueOf(false)},
                        {"f", "Open the LyB tube.", String.valueOf(false)},
                        {"g", "Move the micropipette tip over the LyB tube.", String.valueOf(false)},
                        {"h", "Slowly draw up 150μl of Lysis Buffer by pressing to the first stop and slowly releasing.", String.valueOf(false)},
                        {"i", "Move your micropipette over the EC1 tube and dispense the Lysis Buffer by holding down the plunger until it reaches the second stop.", String.valueOf(false)},
                        {"j", "Open the biohazard waste container.", String.valueOf(false)},
                        {"k", "Move the micropipette over the biohazard waste container and use the eject icon to eject the tip.", String.valueOf(false)},
                        {"l", "Repeat the centrifuge step.", String.valueOf(false)},
                        {"m", "Set a timer for 12 hours to make sure all tubes are closed and incubate EC1 tube overnight at room temperature.", String.valueOf(false)},
                },

                {
                        {"a", "Place the waste collection jar under the valve of the Chromatography column.", String.valueOf(false)},
                        {"b", "Select the column to drain the liquid inside until it reaches the resin bed.", String.valueOf(false)},
                        {"c", "Spin the EC1 tube again for 10 minutes and then remove the tubes from the microcentrifuge.", String.valueOf(false)},
                        {"d", "Spin the EC1 tube again for 10 minutes and then remove the tubes from the microcentrifuge.", String.valueOf(false)},
                        {"e", "Use the volume setting to change the volume to 200μl and select “Save Volume”.", String.valueOf(false)},
                        {"f", "Open the P200 tip box.", String.valueOf(false)},
                        {"g", "Move the P200 micropipette onto the P200 tip box to attach a tip.", String.valueOf(false)},
                        {"h", "Close the P200 tip box.", String.valueOf(false)},
                        {"i", "Open the EC1 tube.", String.valueOf(false)},
                        {"j", "Move the micropipette tip over the EC1 tube.", String.valueOf(false)},
                        {"k", "Press the plunger to the first stop and slowly release the plunger to draw up 200μl of the supernatant.", String.valueOf(false)},
                        {"l", "Open the SUPER tube.", String.valueOf(false)},
                        {"m", "Move your micropipette over the SUPER tube and dispense the supernatant by holding down the plunger until it reaches the second stop.", String.valueOf(false)},
                        {"n", "Open the biohazard waste container.", String.valueOf(false)},
                        {"o", "Move the micropipette over the biohazard waste container and use the eject icon to eject the tip.", String.valueOf(false)},

                },

                {
                        {"a", "Open the P200 tip box.", String.valueOf(false)},
                        {"b", "Move the P200 micropipette onto the P200 tip box to attach a tip.", String.valueOf(false)},
                        {"c", "Close the P200 tip box.", String.valueOf(false)},
                        {"d", "Open the BB (binding buffer) tube.", String.valueOf(false)},
                        {"e", "Move the micropipette tip over the BB tube. Draw up the liquid by pressing the plunger to the first stop, then release it.", String.valueOf(false)},
                        {"f", "Move your micropipette over the SUPER tube and dispense the buffer into the SUPER tube by holding down the plunger until it reaches the second stop.", String.valueOf(false)},
                        {"g", "Open the biohazard waste container.", String.valueOf(false)},
                        {"h", "Move the micropipette over the biohazard waste container and use the eject icon to eject the tip.", String.valueOf(false)}
                },

                {
                        {"a", "Pick up the P1000 micropipette from the micropipette rack.", String.valueOf(false)},
                        {"b", "Set the P1000 micropipette to 400μl.", String.valueOf(false)},
                        {"c", "Open the P1000 tip box.", String.valueOf(false)},
                        {"d", "Move the P1000 micropipette onto the P1000 tip box to attach a tip.", String.valueOf(false)},
                        {"e", "Move the micropipette tip over the SUPER tube. Draw up the liquid by pressing the plunger down until it reaches the first stop, and then slowly release it.", String.valueOf(false)},
                        {"f", "Move the micropipette over the top of the chromatography column and dispense red SUPER solution down the side of the column by holding down the plunger until it reaches the second stop.", String.valueOf(false)},
                        {"g", "Open the biohazard waste container.", String.valueOf(false)},
                        {"h", "Move the micropipette over the biohazard waste container and use the eject icon to eject the tip.", String.valueOf(false)},
                        {"i", "Place the waste collection jar back under the valve.", String.valueOf(false)},
                        {"j", "Open the column by turning the valve and allow the liquid to drain into the waste container. Close the valve when the liquid is just above the resin bed.", String.valueOf(false)}
                },

                {
                        {"a", "Set the P1000 micropipette to 1000μl.", String.valueOf(false)},
                        {"b", "Attach a new P1000 tip.", String.valueOf(false)},
                        {"c", "Draw up 1000μl of WB (wash buffer) solution.", String.valueOf(false)},
                        {"d", "Dispense the WB solution into the chromatography column.", String.valueOf(false)},
                        {"e", "Open the biohazard waste container.", String.valueOf(false)},
                        {"f", "Move the micropipette over the biohazard waste container and use the eject icon to eject the tip.", String.valueOf(false)},
                        {"g", "Open the column by turning the valve, allow the liquid the drain into the waste container. Close the valve when the liquid is just above the resin bed.", String.valueOf(false)},
                },

                {
                        {"a", "Using a new tip, draw up 1000μl of the EB (elution buffer) solution. Dispense gently down the side of the column. Repeat this step so a total of 2ml EB is added to the column.", String.valueOf(false)},
                        {"b", "Open the column by turning the valve. Watch the red protein traveling through the column, when it nears the bottom close the valve.", String.valueOf(false)},
                        {"c", "Place the open RFP tube under the valve.", String.valueOf(false)},
                        {"d", "Open the column to capture the RFP.", String.valueOf(false)},
                        {"e", "Close the valve and RFP tube.", String.valueOf(false)},
                        {"f", "Place the waste collection jar back under the valve.", String.valueOf(false)},
                        {"g", "Draw up 1000μl of Column Equilibration Buffer (CEB) solution.", String.valueOf(false)},
                        {"h", "Dispense the Column Equilibration Buffer (CEB) into the column.", String.valueOf(false)},
                        {"i", "Open the column by turning the valve and allow the liquid to drain into the waste container. Close the valve when the liquid is just above the resin bed.", String.valueOf(false)}
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

        return item;
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
        protocolButton.getStyleClass().add("nav-btn");

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

        Label stepIndicator = new Label("1/9");
        stepIndicator.setId("stepIndicator");
        stepIndicator.setFont(Font.font("DM Sans Medium", FontWeight.BOLD, 12));
        stepIndicator.setStyle("-fx-text-fill: #333;");

        section.getChildren().addAll(progressBar, stepIndicator);

        return section;
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
}