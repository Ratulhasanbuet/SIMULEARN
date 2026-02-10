package com.example.simulearn;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class LigationController implements Initializable {

    @FXML
    private VBox mainpanel;
    @FXML
    private VBox instruction;

    private VBox buttonPanel;
    private ScrollPane scrollPane;
    private boolean isPredictionExpanded = false;

    // Selection tracking
    private Button selectedButton = null;

    private LigationLabWorkspace ligationLabWorkspace;

    // Button references
    private Button btnContext, btnMaterials, btnPredictions, btnProtocol, btnResults, btnReflection, btnSummary;
    // Lab workspace components
    private VBox instructionCommandPanel;
    private int currentProtocolStep = 1;
    private final int TOTAL_PROTOCOL_STEPS = 7;
    private VBox currentInstructionsList;
    private String currentHighlightedInstruction = null;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
//      setupLayoutofMainPanel();
        setupLayout();
    }

    private void setupLayout() {
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
        underline.setMaxWidth(200);
        underline.setStyle("-fx-background-color: #00aaff;");
        underline.setPrefHeight(3);


        Label text = new Label("In this simulation, you will learn how to unite two DNA " + "fragments using a technique called ligation. You will use an enzyme to join" + " fragments created from a previous restriction digest of the pKAN-R and pARA " + "plasmids in order to assemble all the components necessary for gene expression" + " and plasmid replication. Ligating the DNA fragments from the digested pKAN-R " + "and pARA plasmids will lead to multiple recombinant plasmids, but only one of " + "the products is the recombinant plasmid that you need — the pARA-R plasmid.");
        text.setWrapText(true);
        text.setFont(Font.font("Arial", 16));
        text.setLineSpacing(3);
        text.setStyle("-fx-text-fill: #333333;");

        Label heading1 = new Label("What do you need to know before this laboratory starts?");
        heading1.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        heading1.setStyle("-fx-text-fill: #1a5490;");
        heading1.setPadding(new Insets(20, 0, 10, 0));

        Label goalText = new Label("Your goal is to create the recombinant pARA-R plasmid to express red " + "fluorescent protein (RFP) in bacteria. The RFP protein is produced through expression of " + "the rfp gene, which is originally derived from sea anemones. The pARA-R plasmid also contains" + " a gene for ampicillin resistance (ampR) so that bacteria containing the plasmid can be easily" + " selected. Expression of the rfp gene is controlled by a promoter sequence (pBAD) found upstream" + " of the rfp gene and an arabinose-dependent regulatory protein AraC, produced by the araC gene. " + "Finally, the plasmid contains an origin of replication (ori), which is a sequence of DNA that is" + " required to initiate DNA replication and maintain the plasmid in bacterial cells during cell division.");
        goalText.setWrapText(true);
        goalText.setFont(Font.font("Arial", 16));
        goalText.setLineSpacing(3);
        goalText.setStyle("-fx-text-fill: #333333;");


        ImageView plasmidImage = SVGLoader.loadSVG("/Images/LigationImage/equipment/ligating_dna_fragments.svg", 650, 400);

        // Image container with styling
        StackPane imageContainer = new StackPane(plasmidImage);
        imageContainer.setStyle(
                "-fx-background-color: white;" +
                        "-fx-border-color: #d0d0d0;" +
                        "-fx-border-width: 1;" +
                        "-fx-border-radius: 9;" +
                        "-fx-background-radius: 5;" +
                        "-fx-padding: 10;" +
                        "-fx-effect: dropshadow(three-pass-box, rgba(60, 64, 67, 0.3), 15, 0, 0, 6);"
        );
        imageContainer.setMaxWidth(600);
        imageContainer.setPrefHeight(300);
        imageContainer.setPadding(new Insets(10, 0, 10, 0));


        Label heading2 = new Label("How does DNA ligase work?");
        heading2.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        heading2.setStyle("-fx-text-fill: #1a5490;");
        heading2.setPadding(new Insets(20, 0, 10, 0));

        Label works = new Label("To form the recombinant pARA-R plasmid, you will use DNA ligase, an enzyme involved in" + " DNA replication that catalyzes that joining of DNA fragments. DNA ligase functions as a molecular \"glue\"," + " joining DNA fragments from the same or different sources that have complementary sequences called sticky ends." + " These sticky ends are produced when DNA is cut with the same restriction enzymes. Joining of DNA fragments is " + "achieved through the formation of covalent bonds between the adjacent nucleotides after complementary base pairs" + " have been aligned in the correct sequence. DNA ligase seals the gap between two DNA molecules and, in the case " + "of a recombinant plasmid, creates a single piece of circular DNA.");
        works.setWrapText(true);
        works.setFont(Font.font("Arial", 16));
        works.setLineSpacing(3);
        works.setStyle("-fx-text-fill: #333333;");
        content.getChildren().addAll(title, underline, text, heading1, goalText, imageContainer, heading2, works);
        return content;
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

        // === OTHER EQUIPMENT SECTION ===
        Label otherEquipmentTitle = new Label("OTHER EQUIPMENT");
        otherEquipmentTitle.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        otherEquipmentTitle.setStyle("-fx-text-fill: #1a5490;");

        GridPane otherEquipmentGrid = createOtherEquipmentGrid();

        // Add all components
        content.getChildren().addAll(
                title,
                underline,
                infoText,
                new VBox(5, reagentsTitle, reagentsSubtitle),
                reagentsGrid,
                new VBox(5, micropipettingTitle, micropipettingSubtitle),
                micropipettingGrid,
                otherEquipmentTitle,
                otherEquipmentGrid
        );

        return content;
    }

    private GridPane createReagentsGrid() {
        GridPane grid = new GridPane();
        grid.setHgap(15);
        grid.setVgap(15);
        grid.setPadding(new Insets(20, 0, 20, 0));

        // Reagent items - SVG paths
        String[][] reagents = {
                {"/Images/LigationImage/reagents/K+-close.svg", "Digested pKAN-R\n(K+)", "#4a90e2"},
                {"/Images/LigationImage/reagents/A+-close.svg", "Digested pARA\n(A+)", "#e24a4a"},
                {"/Images/LigationImage/reagents/5XB-close.svg", "Ligation Buffer\n(5XB) Solution", "#50c878"},
                {"/Images/LigationImage/reagents/LIG-close.svg", "DNA Ligase (LIG)\nSolution", "#9b59b6"},
                {"/Images/LigationImage/reagents/dH20-close.svg", "Distilled Water\n(dH₂O)", "#3498db"}
        };

        int col = 0;
        int row = 0;
        for (String[] reagent : reagents) {
            VBox item = createMaterialItem(reagent[0], reagent[1], reagent[2], 60, 80);
            grid.add(item, col, row);
            col++;
            if (col == 4) {
                col = 0;
                row++;
            }
        }

        return grid;
    }

    private GridPane createMicropipettingGrid() {
        GridPane grid = new GridPane();
        grid.setHgap(15);
        grid.setVgap(15);
        grid.setPadding(new Insets(20, 0, 20, 0));

        // Micropipetting items - SVG paths
        String[][] pipettes = {
                {"/Images/LigationImage/pipettes/P2.svg", "P2 Pipette", "#ff6b6b"},
                {"/Images/LigationImage/pipettes/P20.svg", "P20 Pipette", "#ffd93d"},
                {"/Images/LigationImage/pipettes/P200.svg", "P200 Pipette", "#6bcf7f"},
                {"/Images/LigationImage/pipettes/P1000.svg", "P1000 Pipette", "#4d96ff"}
        };

        String[][] tipBoxes = {
                {"/Images/LigationImage/tipboxes/P2_Tip_Box.svg", "P2 Tip Box", "#ff6b6b"},
                {"/Images/LigationImage/tipboxes/P20_Tip_Box.svg", "P20 Tip Box", "#ffd93d"},
                {"/Images/LigationImage/tipboxes/P200_Tip_Box.svg", "P200 Tip Box", "#6bcf7f"},
                {"/Images/LigationImage/tipboxes/P1000_Tip_Box.svg", "P1000 Tip Box", "#4d96ff"}
        };

        // Add pipettes
        int col = 0;
        for (String[] pipette : pipettes) {
            VBox item = createMaterialItem(pipette[0], pipette[1], pipette[2], 70, 85);
            grid.add(item, col, 0);
            col++;
        }

        // Add tip boxes
        col = 0;
        for (String[] tipBox : tipBoxes) {
            VBox item = createMaterialItem(tipBox[0], tipBox[1], tipBox[2], 80, 70);
            grid.add(item, col, 1);
            col++;
        }

        return grid;
    }

    private GridPane createOtherEquipmentGrid() {
        GridPane grid = new GridPane();
        grid.setHgap(15);
        grid.setVgap(15);
        grid.setPadding(new Insets(20, 0, 20, 0));

        // Equipment items - SVG paths
        String[][] equipment = {
                {"/Images/LigationImage/equipment/Microcentrifuge.svg", "Microcentrifuge", "#7f8c8d"},
                {"/Images/LigationImage/equipment/Trash.svg", "Trash", "#95a5a6"},
                {"/Images/LigationImage/equipment/floating-solution-tube-rack-empty.svg", "Floating Tube\nRack", "#95a5a6"},
                {"/Images/LigationImage/equipment/freezer-closed.svg", "Freezer", "#3498db"},
                {"/Images/LigationImage/equipment/Water Bath.svg", "Water Bath", "#e74c3c"},
                {"/Images/LigationImage/equipment/Ice_Bucket.svg", "Ice Bucket", "#3498db"},
                {"/Images/LigationImage/equipment/vortex.svg", "Vortex", "#16a085"}
        };

        int col = 0;
        int row = 0;
        for (String[] item : equipment) {
            VBox equipItem = createMaterialItem(item[0], item[1], item[2], 80, 80);
            grid.add(equipItem, col, row);
            col++;
            if (col == 4) {
                col = 0;
                row++;
            }
        }

        return grid;
    }

    private VBox createMaterialItem(String imagePath, String labelText,
                                    String accentColor, double imgWidth, double imgHeight) {
        VBox container = new VBox(10);
        container.setAlignment(Pos.CENTER);
        container.setPrefSize(150, 180);
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

        // Image container with dark blue background
        StackPane imageContainer = new StackPane();
        imageContainer.setPrefSize(120, 100);
        imageContainer.setStyle(
                "-fx-background-color: #0d3d5c;" +
                        "-fx-background-radius: 5;"
        );

        // Load SVG using SVGLoader utility
        ImageView imageView = SVGLoader.loadSVG(imagePath, imgWidth, imgHeight,true);

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

        container.getChildren().addAll(imageContainer, label);

        // Hover and click effects
        addHoverEffects(container);
        container.setOnMouseClicked(e -> showMaterialDetails(labelText));

        return container;
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

    private void showMaterialDetails(String materialName) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Material Information");
        alert.setHeaderText(materialName);

        String details = getMaterialDescription(materialName);
        alert.setContentText(details);

        alert.showAndWait();
    }

    private String getMaterialDescription(String materialName) {
        String cleanName = materialName.trim().replace("\n", " ");

        // Reagents
        if (cleanName.contains("Digested pKAN-R")) {
            return "Contains digested pKAN-R plasmids, restriction enzymes, and restriction buffer.";
        } else if (cleanName.contains("Digested pARA")) {
            return "Contains digested pARA plasmids, restriction enzymes, and restriction buffer.";
        } else if (cleanName.contains("Ligation Buffer")) {
            return " Buffer at 5 times concentration that helps to regulate pH, provide ion cofactors and ATP energy needed for the enzyme to function properly. Labeled with a black label.";
        } else if (cleanName.contains("DNA Ligase")) {
            return "Contains the enzyme that can make covalent bonds between DNA fragments. Requires appropriate buffer to function properly. Labeled with a purple label.";
        } else if (cleanName.contains("Distilled Water")) {
            return "Sterile distilled water used to dilute the reagents to the proper concentration and prevent unwanted chemical reactions, and fill the balance tube for the centrifuge. Labeled with a blue label.";
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
        else if (cleanName.contains("Microcentrifuge")) {
            return "Motor-driven centrifuge that spins samples at high speed to collect liquid at the bottom of the tube for easy transfer.";
        } else if (cleanName.contains("Trash")) {
            return "Used to dispose of any non-hazardous waste resulting from the experiment, including micropipette tips.";
        } else if (cleanName.contains("Floating Tube")) {
            return "Used to hold solution tubes on lab bench and inside a water bath.";
        } else if (cleanName.contains("Freezer")) {
            return "Small counter top lab freezer set at -20°C. Used to store solutions for later use or analysis.";
        } else if (cleanName.contains("Water Bath")) {
            return "Used to incubate samples in water, at a constant temperature. Has a temperature setting and a timer.";
        } else if (cleanName.contains("Ice Bucket")) {
            return "Used to keep solutions at a cold temperature to prevent temperature fluctuations and enzymatic degradation.";
        } else if (cleanName.contains("Vortex")) {
            return "Motorized device used to mix small volumes of liquid by agitating the tube.";
        }

        return "Information about " + materialName;
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
                "It is important to summarize your methodology and observations after you have " +
                        "completed an experiment. Please view a recap of this simulation and tips " +
                        "regarding ligating DNA fragments below."
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
                "In this simulation, you learned how to ligate DNA fragments from a " +
                        "restriction digest using DNA ligase. You should be able to predict " +
                        "possible recombinant plasmids that can occur during the ligation " +
                        "process. The simulation explored how to:"
        );
        heading.setWrapText(true);
        heading.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        heading.setStyle("-fx-text-fill: #1a5490;");

        // Learning points with checkmarks
        VBox points = new VBox(12);

        points.getChildren().addAll(
                createChecklistItem("Model different recombinant plasmids."),
                createChecklistItem("Inactivate restriction enzymes."),
                createChecklistItem("Prepare and incubate a ligation reaction.")
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
                        "/Images/LigationImage/equipment/waterBath.svg",
                        "When setting up the water bath, always make sure there is enough distilled water and the lid is closed to reduce evaporation."
                },
                {
                        "/Images/LigationImage/equipment/degree.svg",
                        "It is always a good idea to confirm the temperature of the water with a thermometer, even if there is a digital temperature reading on the water bath."
                },
                {
                        "/Images/LigationImage/equipment/Ice_Bucket.svg",
                        "Make sure you always keep DNA ligase enzymes and ligation buffer on ice or on a cold block to prevent protein degradation."
                },
                {
                        "/Images/LigationImage/equipment/2tube.svg",
                        "Make sure your DNA ligase and ligation buffer are completely defrosted before use."
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

//        btnPredictions.setOnAction(e -> {
//            updateScrollContent(getPredictionsContent());
//            setSelectedButton(btnPredictions);
//            // EKHANE button panel replace hobe command list diye
//            switchToCommandList();
//        });
//
        btnProtocol.setOnAction(e -> {
            setSelectedButton(btnProtocol);

            // Hide scrollPane and show LabWorkspace
            scrollPane.setVisible(false);
            scrollPane.setManaged(false);

            // Create and add LabWorkspace
            if (ligationLabWorkspace == null) {
                ligationLabWorkspace = new LigationLabWorkspace(this);
            }

            // Remove scrollPane if present and add labWorkspace
            mainpanel.getChildren().clear();
            mainpanel.getChildren().add(ligationLabWorkspace.getLabWorkspace());
            VBox.setVgrow(ligationLabWorkspace.getLabWorkspace(), Priority.ALWAYS);

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

    /**
     * Set a button as selected (highlighted)
     */
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
        if (!isPredictionExpanded)
            return;
        isPredictionExpanded = false;
        instruction.getChildren().clear();
        buttonPanel = createButtonPanel();
        instruction.getChildren().add(buttonPanel);
    }

    private VBox createSpacer(int height) {
        VBox spacer = new VBox();
        spacer.setPrefHeight(height);
        return spacer;
    }

    private ImageView createImageView(String imagePath, double width, double height) {
        try {
            Image image = new Image(getClass().getResourceAsStream(imagePath), width, height, true, true);
            ImageView imageView = new ImageView(image);
            imageView.setPreserveRatio(true);
            imageView.setSmooth(true);

            return imageView;
        } catch (Exception e) {
            System.err.println("Error: " + imagePath);
            return new ImageView();
        }
    }

    private VBox getReflectionContent() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(40));
        content.setStyle("-fx-background-color: #f5f5f5;");

        // Title
        Label title = new Label("6. REFLECTION");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 32));
        title.setStyle("-fx-text-fill: #1a5490;");

        // Underline
        Separator underline = new Separator();
        underline.setMaxWidth(200);
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
        text.setMaxWidth(650);

        // Questions container
        VBox questionsContainer = new VBox(20);

        // Question 1
        QuizQuestion q1 = createQuizQuestion(
                1,
                "What do you think is the main purpose of this simulation?",
                new String[]{
                        "To differentiate between restriction enzymes and DNA ligase.",
                        "To model the process of ligation.",
                        "To verify that the correct recombinant plasmid has formed."
                },
                1, // Correct answer index (B - second option)
                "What skill did you practice during this simulation?",
                2  // Attempts allowed
        );

        // Question 2
        QuizQuestion q2 = createQuizQuestion(
                2,
                "Why are the K+ and A+ tubes placed in an 80°C water bath before ligation?",
                new String[]{
                        "To warm up the ligation buffer.",
                        "To defrost the frozen samples of DNA.",
                        "To inactivate the restriction enzymes."
                },
                2, // Correct answer index (C - third option)
                null,
                2
        );

        // Question 3
        QuizQuestion q3 = createQuizQuestion(
                3,
                "What is the function of DNA ligase when building a recombinant plasmid?",
                new String[]{
                        "It replicates the DNA molecules.",
                        "It catalyzes the joining of DNA fragments.",
                        "It adds nucleotides to the DNA fragments."
                },
                1, // Correct answer index (B)
                null,
                2
        );

        // Question 4
        QuizQuestion q4 = createQuizQuestion(
                4,
                "Why do enzymes need to be stored at cold temperatures?",
                new String[]{
                        "To avoid protein denaturation.",
                        "To increase enzyme activity.",
                        "To consolidate samples in the solution tubes."
                },
                0, // Correct answer index (A)
                null,
                2
        );

        // Question 5
        QuizQuestion q5 = createQuizQuestion(
                5,
                "Can any two DNA fragments be joined together?",
                new String[]{
                        "Yes, if the fragments were cut with the same restriction enzyme.",
                        "No, the ligase looks for a specific sequence and can only join fragments with that sequence.",
                        "Maybe, ligase can connect any two fragments if enough energy is added to the reaction."
                },
                0, // Correct answer index (A)
                null,
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

        questionsContainer.getChildren().addAll(
                q1.getQuestionBox(),
                q2.getQuestionBox(),
                q3.getQuestionBox(),
                q4.getQuestionBox(),
                q5.getQuestionBox(),
                feedbackTitle,
                q6.getQuestionBox()
        );

        content.getChildren().addAll(title, underline, text, questionsContainer);
        return content;
    }

    // Inner class to handle quiz questions
    private class QuizQuestion {
        private VBox questionBox;
        private ToggleGroup toggleGroup;
        private int correctAnswerIndex;
        private String feedbackText;
        private int attemptsLeft;
        private Label attemptsLabel;
        private Button submitButton;
        private boolean answered = false;
        private List<HBox> optionBoxes;

        public VBox getQuestionBox() {
            return questionBox;
        }

        public QuizQuestion(int questionNum, String questionText, String[] options,
                            int correctIndex, String feedback, int maxAttempts, boolean isFeedback) {
            this.correctAnswerIndex = correctIndex;
            this.feedbackText = feedback;
            this.attemptsLeft = maxAttempts;
            this.optionBoxes = new ArrayList<>();

            questionBox = new VBox(15);
            questionBox.setStyle(
                    "-fx-background-color: white;" +
                            "-fx-border-color: #d0d0d0;" +
                            "-fx-border-width: 1;" +
                            "-fx-border-radius: 8;" +
                            "-fx-background-radius: 8;" +
                            "-fx-padding: 25;"
            );
            questionBox.setMaxWidth(700);

            // Question text
            Label question = new Label(questionNum + ". " + questionText);
            question.setFont(Font.font("Arial", FontWeight.BOLD, 16));
            question.setStyle("-fx-text-fill: #1a5490;");
            question.setWrapText(true);

            // Options
            toggleGroup = new ToggleGroup();
            VBox optionsContainer = new VBox(10);

            for (int i = 0; i < options.length; i++) {
                HBox optionBox = createOptionBox(options[i], (char) ('A' + i), i);
                optionBoxes.add(optionBox);
                optionsContainer.getChildren().add(optionBox);
            }

            // Bottom section (attempts + submit button)
            HBox bottomSection = new HBox(20);
            bottomSection.setAlignment(Pos.CENTER_LEFT);

            if (!isFeedback) {
                attemptsLabel = new Label("Attempts left: " + attemptsLeft);
                attemptsLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
                attemptsLabel.setStyle("-fx-text-fill: #333333;");
            }

            submitButton = new Button("Submit answer");
            submitButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));
            submitButton.setStyle(
                    "-fx-background-color: #95a5a6;" +
                            "-fx-text-fill: white;" +
                            "-fx-padding: 10 30;" +
                            "-fx-background-radius: 5;" +
                            "-fx-cursor: hand;"
            );
            submitButton.setDisable(true);

            // Enable submit when an option is selected
            toggleGroup.selectedToggleProperty().addListener((obs, old, newVal) -> {
                if (newVal != null && !answered) {
                    submitButton.setDisable(false);
                    submitButton.setStyle(
                            "-fx-background-color: #f39c12;" +
                                    "-fx-text-fill: white;" +
                                    "-fx-padding: 10 30;" +
                                    "-fx-background-radius: 5;" +
                                    "-fx-cursor: hand;"
                    );
                }
            });

            submitButton.setOnAction(e -> handleSubmit(isFeedback));

            HBox.setHgrow(new Region(), Priority.ALWAYS);

            if (!isFeedback) {
                bottomSection.getChildren().addAll(attemptsLabel, submitButton);
            } else {
                bottomSection.getChildren().add(submitButton);
            }

            questionBox.getChildren().addAll(question, optionsContainer, bottomSection);
        }

        private HBox createOptionBox(String optionText, char letter, int index) {
            HBox box = new HBox(15);
            box.setAlignment(Pos.CENTER_LEFT);
            box.setStyle(
                    "-fx-background-color: #f9f9f9;" +
                            "-fx-border-color: #d0d0d0;" +
                            "-fx-border-width: 1;" +
                            "-fx-border-radius: 5;" +
                            "-fx-background-radius: 5;" +
                            "-fx-padding: 15;" +
                            "-fx-cursor: hand;"
            );
            box.setPrefHeight(60);

            // Letter circle
            Circle circle = new Circle(20);
            circle.setFill(Color.WHITE);
            circle.setStroke(Color.web("#333333"));
            circle.setStrokeWidth(2);

            Label letterLabel = new Label(String.valueOf(letter));
            letterLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
            letterLabel.setStyle("-fx-text-fill: #333333;");

            StackPane circleStack = new StackPane(circle, letterLabel);

            // Option text
            Label optionLabel = new Label(optionText);
            optionLabel.setFont(Font.font("Arial", 15));
            optionLabel.setStyle("-fx-text-fill: #333333;");
            optionLabel.setWrapText(true);
            optionLabel.setMaxWidth(550);

            HBox.setHgrow(optionLabel, Priority.ALWAYS);

            box.getChildren().addAll(circleStack, optionLabel);

            // Radio button (invisible, just for toggle group)
            RadioButton radioButton = new RadioButton();
            radioButton.setToggleGroup(toggleGroup);
            radioButton.setVisible(false);
            radioButton.setUserData(index);

            // Click handler
            box.setOnMouseClicked(e -> {
                if (!answered) {
                    radioButton.setSelected(true);
                    // Highlight selected option
                    for (HBox optBox : optionBoxes) {
                        optBox.setStyle(
                                "-fx-background-color: #f9f9f9;" +
                                        "-fx-border-color: #d0d0d0;" +
                                        "-fx-border-width: 1;" +
                                        "-fx-border-radius: 5;" +
                                        "-fx-background-radius: 5;" +
                                        "-fx-padding: 15;" +
                                        "-fx-cursor: hand;"
                        );
                    }
                    box.setStyle(
                            "-fx-background-color: #e8f4f8;" +
                                    "-fx-border-color: #00aaff;" +
                                    "-fx-border-width: 2;" +
                                    "-fx-border-radius: 5;" +
                                    "-fx-background-radius: 5;" +
                                    "-fx-padding: 15;" +
                                    "-fx-cursor: hand;"
                    );
                }
            });

            return box;
        }

        private void handleSubmit(boolean isFeedback) {
            if (answered) return;

            Toggle selectedToggle = toggleGroup.getSelectedToggle();
            if (selectedToggle == null) return;

            int selectedIndex = (int) selectedToggle.getUserData();

            if (isFeedback) {
                // For feedback question, just mark as answered
                answered = true;
                submitButton.setDisable(true);
                submitButton.setText("Submitted");
                submitButton.setStyle(
                        "-fx-background-color: #95a5a6;" +
                                "-fx-text-fill: white;" +
                                "-fx-padding: 10 30;" +
                                "-fx-background-radius: 5;"
                );
                // Highlight selected option in blue
                optionBoxes.get(selectedIndex).setStyle(
                        "-fx-background-color: #d4e6f1;" +
                                "-fx-border-color: #00aaff;" +
                                "-fx-border-width: 2;" +
                                "-fx-border-radius: 5;" +
                                "-fx-background-radius: 5;" +
                                "-fx-padding: 15;"
                );
            } else {
                // For quiz questions
                if (selectedIndex == correctAnswerIndex) {
                    // Correct answer - show green
                    answered = true;
                    showFeedback(selectedIndex, true);
                    submitButton.setDisable(true);
                } else {
                    // Wrong answer - show red
                    attemptsLeft--;
                    attemptsLabel.setText("Attempts left: " + attemptsLeft);
                    showFeedback(selectedIndex, false);

                    if (attemptsLeft <= 0) {
                        // No more attempts - show correct answer
                        answered = true;
                        showCorrectAnswer();
                        submitButton.setDisable(true);
                    } else {
                        // Reset selection for retry
                        toggleGroup.selectToggle(null);
                        submitButton.setDisable(true);
                        submitButton.setStyle(
                                "-fx-background-color: #95a5a6;" +
                                        "-fx-text-fill: white;" +
                                        "-fx-padding: 10 30;" +
                                        "-fx-background-radius: 5;" +
                                        "-fx-cursor: hand;"
                        );
                    }
                }
            }
        }

        private void showFeedback(int selectedIndex, boolean correct) {
            HBox selectedBox = optionBoxes.get(selectedIndex);

            if (correct) {
                // Green checkmark
                Circle checkCircle = new Circle(15);
                checkCircle.setFill(Color.web("#27ae60"));
                checkCircle.setStroke(Color.web("#229954"));
                checkCircle.setStrokeWidth(2);

                Label checkmark = new Label("✓");
                checkmark.setFont(Font.font("Arial", FontWeight.BOLD, 18));
                checkmark.setStyle("-fx-text-fill: white;");

                StackPane checkIcon = new StackPane(checkCircle, checkmark);

                selectedBox.getChildren().add(checkIcon);
                selectedBox.setStyle(
                        "-fx-background-color: #d5f4e6;" +
                                "-fx-border-color: #27ae60;" +
                                "-fx-border-width: 2;" +
                                "-fx-border-radius: 5;" +
                                "-fx-background-radius: 5;" +
                                "-fx-padding: 15;"
                );

                // Show feedback text if available
                if (feedbackText != null) {
                    Label feedback = new Label("Feedback: " + feedbackText);
                    feedback.setFont(Font.font("Arial", FontPosture.ITALIC, 14));
                    feedback.setStyle("-fx-text-fill: #27ae60;");
                    feedback.setPadding(new Insets(10, 0, 0, 0));
                    feedback.setWrapText(true);
                    questionBox.getChildren().add(questionBox.getChildren().size() - 1, feedback);
                }
            } else {
                // Red X
                Circle xCircle = new Circle(15);
                xCircle.setFill(Color.web("#e74c3c"));
                xCircle.setStroke(Color.web("#c0392b"));
                xCircle.setStrokeWidth(2);

                Label xMark = new Label("✗");
                xMark.setFont(Font.font("Arial", FontWeight.BOLD, 18));
                xMark.setStyle("-fx-text-fill: white;");

                StackPane xIcon = new StackPane(xCircle, xMark);

                selectedBox.getChildren().add(xIcon);
                selectedBox.setStyle(
                        "-fx-background-color: #fadbd8;" +
                                "-fx-border-color: #e74c3c;" +
                                "-fx-border-width: 2;" +
                                "-fx-border-radius: 5;" +
                                "-fx-background-radius: 5;" +
                                "-fx-padding: 15;"
                );
            }
        }

        private void showCorrectAnswer() {
            HBox correctBox = optionBoxes.get(correctAnswerIndex);

            Circle checkCircle = new Circle(15);
            checkCircle.setFill(Color.web("#27ae60"));
            checkCircle.setStroke(Color.web("#229954"));
            checkCircle.setStrokeWidth(2);

            Label checkmark = new Label("✓");
            checkmark.setFont(Font.font("Arial", FontWeight.BOLD, 18));
            checkmark.setStyle("-fx-text-fill: white;");

            StackPane checkIcon = new StackPane(checkCircle, checkmark);

            correctBox.getChildren().add(checkIcon);
            correctBox.setStyle(
                    "-fx-background-color: #d5f4e6;" +
                            "-fx-border-color: #27ae60;" +
                            "-fx-border-width: 2;" +
                            "-fx-border-radius: 5;" +
                            "-fx-background-radius: 5;" +
                            "-fx-padding: 15;"
            );
        }
    }

    private QuizQuestion createQuizQuestion(int num, String question, String[] options,
                                            int correctIndex, String feedback, int attempts) {
        return new QuizQuestion(num, question, options, correctIndex, feedback, attempts, false);
    }

    private QuizQuestion createFeedbackQuestion(int num, String question, String[] options) {
        return new QuizQuestion(num, question, options, -1, null, 0, true);
    }

    private VBox getResultsContent() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(40));

        Label title = new Label("5. RESULTS");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 32));
        title.setStyle("-fx-text-fill: #1a5490;");

        Separator underline = new Separator();
        underline.setMaxWidth(200);
        underline.setStyle("-fx-background-color: #00aaff;");
        underline.setPrefHeight(3);

        Label text = new Label("In this simulation, DNA ligase was used to join the DNA fragments resulting from the restriction digest of pKAN-R and pARA to create the recombinant pARA-R plasmid. Remember that multiple combinations of fragments can form when the fragments have complementary sticky ends.\n" +
                "\n" +
                "\n" +
                "Below you can view a closeup of your LIG tube to see whether your experiment has worked and you have produced circular plasmids from the fragments in the K+ and A+ tubes. If your experiment has failed, you will have only fragments in your LIG tube and no circular plasmids.\n" +
                "\n" +
                "\n" +
                "It is important to note that you will not normally be able to see anything in your LIG tube due to the microscopic size of DNA fragments and plasmids. The next experimental step is to run a gel with your stored solutions (LIG, A+ and K+) to verify whether you have successfully ligated the correct fragments to create the desired pARA-R plasmid.");
        text.setFont(Font.font("Arial", 16));
        text.setWrapText(true);
        text.setFont(Font.font("Arial", 16));
        text.setLineSpacing(3);

        Label text2 = new Label("Possible Plasmid ");
        text2.setFont(Font.font("Arial", 16));
        text2.setStyle("-fx-text-fill: #1a5490;");
        ImageView possibleplasimid = createImageView("/Images/LigationImage/equipment/results-en.png", 400, 720);

        // Image container with styling
        StackPane imageContainer = new StackPane(possibleplasimid);
        imageContainer.setStyle(
                "-fx-background-color: white;" +
                        "-fx-border-color: #d0d0d0;" +
                        "-fx-border-width: 1;" +
                        "-fx-border-radius: 7;" +
                        "-fx-background-radius: 5;" +
                        "-fx-padding: 10;" +
                        "-fx-effect: dropshadow(three-pass-box, rgba(60, 64, 67, 0.3), 15, 0, 0, 6);"
        );
        imageContainer.setMaxWidth(750);
        imageContainer.setPrefHeight(450);
        imageContainer.setPadding(new Insets(10, 0, 10, 0));

        content.getChildren().addAll(title, underline, text, text2, imageContainer);
        return content;
    }

    private void switchToInstructionPanel() {
        // Remove button panel
        instruction.getChildren().clear();

        // Create and add instruction panel
        instructionCommandPanel = createInstructionCommandPanel();
        instruction.getChildren().add(instructionCommandPanel);

        // Load first step
        loadProtocolStep(1);

        // Update main content to show lab workspace
        // updateScrollContent(getProtocolLabContent());  eta baki ase kora
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
//        protocolButton.setStyle(
//                "-fx-background-color: #c2fbd7; " +
//                        "-fx-background-radius: 100; " +
//                        "-fx-text-fill: green; " +
//                        "-fx-font-size: 16px; " +
//                        "-fx-padding: 7 20; " +
//                        "-fx-cursor: hand;"
//        );

//        btnResults.setOnAction(e -> {
//            updateScrollContent(getResultsContent());
//            scrollPane.setVisible(true);
//            scrollPane.setManaged(true);
//            setSelectedButton(btnResults);
//            resetToButtonView();
//        });

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
        Label stepIndicator = new Label("1/7");
        stepIndicator.setId("stepIndicator");
        stepIndicator.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        stepIndicator.setStyle("-fx-text-fill: #333;");

        section.getChildren().addAll(progressBar, stepIndicator);

        return section;
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

// ========== STEP LOADING ==========

    private void loadProtocolStep(int stepNumber) {
        currentProtocolStep = stepNumber;

        // Update progress
        ProgressBar progressBar = (ProgressBar) instructionCommandPanel.lookup("#protocolProgressBar");
        if (progressBar != null) {
            progressBar.setProgress((double) (stepNumber - 1) / TOTAL_PROTOCOL_STEPS);
        }

        Label stepIndicator = (Label) instructionCommandPanel.lookup("#stepIndicator");
        if (stepIndicator != null) {
            stepIndicator.setText(stepNumber + "/7");
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
                "1. Heat inactivate the restriction enzymes in the restriction digests.",
                "2. Set the volume of the P20 micropipette.",
                "3. Pipette restriction digests into the reaction tubes.",
                "4. Pipette additional reagents into the reaction tubes.",
                "5. Spin the reaction tubes.",
                "6. Incubate the reaction tubes.",
                "7. Store the ligation product."
        };

        // All instructions for each step
        String[][][] allInstructions = {
                // Step 1
                {
                        {"a", "Use the temperature setting to set the water bath temperature to 80°C.", String.valueOf(false)},
                        {"b", "Move the K+ and A+ tubes to the floating tube rack.", String.valueOf(false)},
                        {"c", "Open the water bath.", String.valueOf(false)},
                        {"d", "Move the rack containing the K+ and A+ tubes to the water bath.", String.valueOf(false)},
                        {"e", "Close the water bath.", String.valueOf(false)},
                        {"f", "Use the buttons on the water bath interface to set the timer for 20 minutes. Press start to begin the timer.", String.valueOf(false)},
                        {"g", "After 20 minutes, remove the floating solution tube rack from the water bath.", String.valueOf(false)}
                },
                // Step 2
                {
                        {"a", "Pick up the P20 micropipette from the micropipette rack.", String.valueOf(false)},
                        {"b", "Use the volume setting to set the volume to 4µl and click Save volume.", String.valueOf(false)},
                        {"c", "Open the P20 tip box.", String.valueOf(false)},
                        {"d", "Move the P20 micropipette onto the P20 tip box to attach a tip.", String.valueOf(false)},
                        {"e", "Close the P20 tip box.", String.valueOf(false)}
                },
                // Step 3
                {
                        {"a", "Open the A+ tube.", String.valueOf(false)},
                        {"b", "Pick up and move the micropipette so that the tip is above the A+ tube.", String.valueOf(false)},
                        {"c", "Draw up the A+ solution by pressing the plunger down until it reaches the first stop.", String.valueOf(false)},
                        {"d", "Close the A+ solution tube.", String.valueOf(false)},
                        {"e", "Open the LIG tube.", String.valueOf(false)},
                        {"f", "Dispense the A+ solution into the LIG tube by holding down the plunger until it reaches the second stop.", String.valueOf(false)},
                        {"g", "Move the micropipette over the trash can and use the eject icon to eject the tip.", String.valueOf(false)},
                        {"h", "Follow the same micropipetting procedure to dispense 4µl of the K+ solution into the LIG tube.", String.valueOf(false)}
                },
                // Step 4
                {
                        {"a", "Follow the same micropipetting procedure to dispense 3µl of the ligation buffer from the 5XB tube into the LIG tube.", String.valueOf(false)},
                        {"b", "Follow the micropipetting procedure to dispense 2µl of distilled water from the dH₂O tube into the LIG tube.", String.valueOf(false)},
                        {"c", "Use the vortex to mix the solution in the LIG tube.", String.valueOf(false)}
                },
                // Step 5
                {
                        {"a", "Open the microcentrifuge.", String.valueOf(false)},
                        {"b", "Place the LIG tube in the microcentrifuge.", String.valueOf(false)},
                        {"c", "Fill the balance tube with dH₂O so that it matches the total volume of the LIG tube (18µl).", String.valueOf(false)},
                        {"d", "Place the balance tube in the microcentrifuge opposite the LIG tube to balance it.", String.valueOf(false)},
                        {"e", "Close the microcentrifuge.", String.valueOf(false)},
                        {"f", "Press the pulse button to spin the tubes.", String.valueOf(false)},
                        {"g", "Once the spin is complete, open the microcentrifuge.", String.valueOf(false)},
                        {"h", "Move the LIG tube back to the tube rack.", String.valueOf(false)},
                        {"i", "Close the microcentrifuge lid.", String.valueOf(false)}
                },
                // Step 6
                {
                        {"a", "Leave the LIG tube in tube rack at room temperature for 10 minutes.", String.valueOf(false)}
                },
                // Step 7
                {
                        {"a", "Open the freezer.", String.valueOf(false)},
                        {"b", "Place the A+ and K+ tubes on the floating rack and move it to the freezer for later analysis.", String.valueOf(false)},
                        {"c", "Close the freezer.", String.valueOf(false)}
                }
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
                    "-fx-background-color: #ffa726;" +
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
                "-fx-background-color: #ffa726;" +
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
                    "-fx-background-color: #ffa726;" +
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

    private void resetToLabView() {
        instruction.getChildren().clear();

        // Create lab instruction panel
        instructionCommandPanel = createInstructionCommandPanel();
        instruction.getChildren().add(instructionCommandPanel);

        // Load step 1
        loadProtocolStep(1);
    }

}