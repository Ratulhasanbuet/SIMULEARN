package com.example.simulearn;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.net.URL;
import java.util.ResourceBundle;

public class LigationController implements Initializable {

    @FXML
    private VBox mainContainer; // Root VBox from FXML

    private ScrollPane scrollPane;
    private VBox rightSideContainer;
    private VBox buttonPanel;
    private boolean isPredictionExpanded = false;

    // Selection tracking
    private Button selectedButton = null;

    // Button references
    private Button btnContext, btnMaterials, btnPredictions, btnProtocol,
            btnResults, btnReflection, btnSummary;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Set up the main layout after FXML loads
        setupLayout();
    }

    private void setupLayout() {
        // Create horizontal box for content area (center + right panel)
        HBox contentArea = new HBox();
        VBox.setVgrow(contentArea, Priority.ALWAYS);

        // Left side - ScrollPane for main content
        scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: white; -fx-border-color: #cccccc;");
        HBox.setHgrow(scrollPane, Priority.ALWAYS);

        // Set initial content
        updateScrollContent(getContextContent());

        // Right side - Navigation panel
        rightSideContainer = new VBox(10);
        rightSideContainer.setPadding(new Insets(20));
        rightSideContainer.setStyle("-fx-background-color: #1a5490;");
        rightSideContainer.setPrefWidth(280);
        rightSideContainer.setMinWidth(280);

        // Create initial button panel
        buttonPanel = createButtonPanel();
        rightSideContainer.getChildren().add(buttonPanel);

        // Add both to content area
        contentArea.getChildren().addAll(scrollPane, rightSideContainer);

        // Add content area to main VBox (after the header which is already in FXML)
        mainContainer.getChildren().add(contentArea);
    }

    private VBox createButtonPanel() {
        VBox panel = new VBox(0); // No spacing, we'll add it manually
        panel.setAlignment(Pos.TOP_LEFT);
        panel.setPadding(new Insets(10));

        // Create all navigation buttons with circles
        btnContext = createNavButtonWithCircle("1", "CONTEXT");
        btnMaterials = createNavButtonWithCircle("2", "MATERIALS");
        btnPredictions = createNavButtonWithCircle("3", "PREDICTIONS");
        btnProtocol = createNavButtonWithCircle("4", "PROTOCOL");
        btnResults = createNavButtonWithCircle("5", "RESULTS");
        btnReflection = createNavButtonWithCircle("6", "REFLECTION");
        btnSummary = createNavButtonWithCircle("7", "SUMMARY");

        // Set button actions
        btnContext.setOnAction(e -> {
            updateScrollContent(getContextContent());
            setSelectedButton(btnContext);
            resetToButtonView();
        });

        btnMaterials.setOnAction(e -> {
            updateScrollContent(getMaterialsContent());
            setSelectedButton(btnMaterials);
            resetToButtonView();
        });

        btnPredictions.setOnAction(e -> {
            updateScrollContent(getPredictionsContent());
            setSelectedButton(btnPredictions);
            // EKHANE button panel replace hobe command list diye
            switchToCommandList();
        });

        btnProtocol.setOnAction(e -> {
            updateScrollContent(getProtocolContent());
            setSelectedButton(btnProtocol);
            resetToButtonView();
        });

        btnResults.setOnAction(e -> {
            updateScrollContent(getResultsContent());
            setSelectedButton(btnResults);
            resetToButtonView();
        });

        btnReflection.setOnAction(e -> {
            updateScrollContent(getReflectionContent());
            setSelectedButton(btnReflection);
            resetToButtonView();
        });

        btnSummary.setOnAction(e -> {
            updateScrollContent(getSummaryContent());
            setSelectedButton(btnSummary);
            resetToButtonView();
        });

        // Add buttons with spacing
        panel.getChildren().addAll(
                btnContext,
                createSpacer(5),
                btnMaterials,
                createSpacer(5),
                btnPredictions,
                createSpacer(5),
                btnProtocol,
                createSpacer(5),
                btnResults,
                createSpacer(5),
                btnReflection,
                createSpacer(5),
                btnSummary);

        // Set initial selection
        setSelectedButton(btnContext);

        return panel;
    }

    /**
     * Create navigation button with circle (like screenshot)
     */
    private Button createNavButtonWithCircle(String number, String text) {
        // Main container
        HBox container = new HBox(12);
        container.setAlignment(Pos.CENTER_LEFT);
        container.setPadding(new Insets(8, 15, 8, 15));
        container.setStyle("-fx-background-color: transparent;");

        // Number circle
        Circle circle = new Circle(15);
        circle.setFill(Color.web("#0066cc"));
        circle.setStroke(Color.WHITE);
        circle.setStrokeWidth(2);

        Label numberLabel = new Label(number);
        numberLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        numberLabel.setTextFill(Color.WHITE);

        // Stack circle and number
        StackPane circleStack = new StackPane();
        circleStack.getChildren().addAll(circle, numberLabel);

        // Button text
        Button btn = new Button(text);
        btn.setFont(Font.font("Arial", FontWeight.BOLD, 15));
        btn.setTextFill(Color.WHITE);
        btn.setStyle("-fx-background-color: transparent; " +
                "-fx-border-width: 0; " +
                "-fx-cursor: hand; " +
                "-fx-padding: 0;");
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(btn, Priority.ALWAYS);

        // Store container reference in button's userData
        btn.setUserData(container);

        // Hover and selection effects
        btn.setOnMouseEntered(e -> {
            if (btn != selectedButton) {
                container.setStyle("-fx-background-color: rgba(255,255,255,0.1); " +
                        "-fx-background-radius: 5;");
            }
        });

        btn.setOnMouseExited(e -> {
            if (btn != selectedButton) {
                container.setStyle("-fx-background-color: transparent;");
            }
        });

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

    /**
     * Create spacer for button gaps
     */
    private VBox createSpacer(int height) {
        VBox spacer = new VBox();
        spacer.setPrefHeight(height);
        return spacer;
    }

    // KEY METHOD - Button panel ke command list diye replace kore
    private void switchToCommandList() {
        if (isPredictionExpanded)
            return;

        isPredictionExpanded = true;

        // Clear right container
        rightSideContainer.getChildren().clear();

        // Create command list
        VBox commandList = new VBox(12);
        commandList.setPadding(new Insets(15));

        // Title
        Label title = new Label("Available Commands:");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        title.setTextFill(Color.WHITE);
        title.setPadding(new Insets(0, 0, 10, 0));

        // Command buttons
        Button cmd1 = createCommandButton("🧬 Add pKAN-R fragments");
        Button cmd2 = createCommandButton("🧬 Add pARA-R fragments");
        Button cmd3 = createCommandButton("🔬 Add DNA Ligase enzyme");
        Button cmd4 = createCommandButton("🌡️ Incubate at 25°C");
        Button cmd5 = createCommandButton("⏰ Wait for ligation");
        Button cmd6 = createCommandButton("📊 View results");

        // Add actions to commands
        cmd1.setOnAction(e -> {
            System.out.println("pKAN-R fragments added!");
            updateScrollContent(createCommandResultContent("pKAN-R fragments added successfully!"));
        });

        cmd2.setOnAction(e -> {
            System.out.println("pARA-R fragments added!");
            updateScrollContent(createCommandResultContent("pARA-R fragments added successfully!"));
        });

        cmd3.setOnAction(e -> {
            System.out.println("DNA Ligase added!");
            updateScrollContent(createCommandResultContent("DNA Ligase enzyme added to the mixture!"));
        });

        cmd4.setOnAction(e -> {
            System.out.println("Incubating...");
            updateScrollContent(createCommandResultContent("Sample is now incubating at 25°C..."));
        });

        cmd5.setOnAction(e -> {
            System.out.println("Waiting for ligation...");
            updateScrollContent(createCommandResultContent("Ligation in progress... Please wait."));
        });

        cmd6.setOnAction(e -> {
            System.out.println("Viewing results...");
            updateScrollContent(getResultsContent());
            setSelectedButton(btnResults);
        });

        // Separator
        Separator separator = new Separator();
        separator.setStyle("-fx-background-color: white;");
        separator.setPadding(new Insets(10, 0, 5, 0));

        // Back button
        Button backBtn = createCommandButton("← Back to Menu");
        backBtn.setStyle(
                "-fx-background-color: #cc0000; " +
                        "-fx-text-fill: white; " +
                        "-fx-border-color: white; " +
                        "-fx-border-width: 2; " +
                        "-fx-background-radius: 5; " +
                        "-fx-font-size: 14px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-cursor: hand;");
        backBtn.setOnAction(e -> resetToButtonView());

        // Add all to command list
        commandList.getChildren().addAll(
                title,
                cmd1, cmd2, cmd3, cmd4, cmd5, cmd6,
                separator,
                backBtn);

        // Add command list to right container
        rightSideContainer.getChildren().add(commandList);
    }

    private Button createCommandButton(String text) {
        Button btn = new Button(text);
        btn.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
        btn.setTextFill(Color.WHITE);
        btn.setStyle(
                "-fx-background-color: #0066cc; " +
                        "-fx-border-color: white; " +
                        "-fx-border-width: 1.5; " +
                        "-fx-background-radius: 5; " +
                        "-fx-cursor: hand;");
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setPrefHeight(40);
        btn.setAlignment(Pos.CENTER_LEFT);

        // Hover effect
        btn.setOnMouseEntered(e -> btn.setStyle(
                "-fx-background-color: #0088ee; " +
                        "-fx-border-color: white; " +
                        "-fx-border-width: 2; " +
                        "-fx-background-radius: 5; " +
                        "-fx-cursor: hand;"));

        btn.setOnMouseExited(e -> btn.setStyle(
                "-fx-background-color: #0066cc; " +
                        "-fx-border-color: white; " +
                        "-fx-border-width: 1.5; " +
                        "-fx-background-radius: 5; " +
                        "-fx-cursor: hand;"));

        return btn;
    }

    // Reset to button view
    private void resetToButtonView() {
        if (!isPredictionExpanded)
            return;

        isPredictionExpanded = false;
        rightSideContainer.getChildren().clear();
        buttonPanel = createButtonPanel();
        rightSideContainer.getChildren().add(buttonPanel);
    }

    // Update scroll pane content
    private void updateScrollContent(VBox content) {
        scrollPane.setContent(content);
    }

    // Content creation methods
    private VBox getContextContent() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(10));

        Label title = new Label("1. CONTEXT");
        title.setFont(Font.font("AdobeHebrew-Bold", 32));
        title.setStyle("-fx-text-fill: #1a5409;");
        Separator underline = new Separator();
        underline.setMaxWidth(200);
        underline.setMaxWidth(200);
        underline.setStyle("-fx-background-color: #00aaff;");
        underline.setPrefHeight(3);

        Label text = new Label("In this simulation, you will learn how to unite two DNA " +
                "fragments using a technique called ligation. You will use an enzyme to join" +
                " fragments created from a previous restriction digest of the pKAN-R and pARA " +
                "plasmids in order to assemble all the components necessary for gene expression" +
                " and plasmid replication. Ligating the DNA fragments from the digested pKAN-R " +
                "and pARA plasmids will lead to multiple recombinant plasmids, but only one of " +
                "the products is the recombinant plasmid that you need — the pARA-R plasmid.");
        text.setWrapText(true);
        text.setFont(Font.font("Arial", 16));
        text.setLineSpacing(3);
        text.setStyle("-fx-text-fill: #333333;");

        Label heading1 = new Label("What do you need to know before this laboratory starts?");
        heading1.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        heading1.setStyle("-fx-text-fill: #1a5490;");
        heading1.setPadding(new Insets(20, 0, 10, 0));

        Label goalText = new Label("Your goal is to create the recombinant pARA-R plasmid to express red " +
                "fluorescent protein (RFP) in bacteria. The RFP protein is produced through expression of " +
                "the rfp gene, which is originally derived from sea anemones. The pARA-R plasmid also contains" +
                " a gene for ampicillin resistance (ampR) so that bacteria containing the plasmid can be easily" +
                " selected. Expression of the rfp gene is controlled by a promoter sequence (pBAD) found upstream" +
                " of the rfp gene and an arabinose-dependent regulatory protein AraC, produced by the araC gene. " +
                "Finally, the plasmid contains an origin of replication (ori), which is a sequence of DNA that is" +
                " required to initiate DNA replication and maintain the plasmid in bacterial cells during cell division.");
        goalText.setWrapText(true);
        goalText.setFont(Font.font("Arial", 16));
        goalText.setLineSpacing(3);
        goalText.setStyle("-fx-text-fill: #333333;");

        Label heading2 = new Label("How does DNA ligase work?");
        heading2.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        heading2.setStyle("-fx-text-fill: #1a5490;");
        heading2.setPadding(new Insets(20, 0, 10, 0));

        Label works = new Label("To form the recombinant pARA-R plasmid, you will use DNA ligase, an enzyme involved in" +
                " DNA replication that catalyzes that joining of DNA fragments. DNA ligase functions as a molecular \"glue\"," +
                " joining DNA fragments from the same or different sources that have complementary sequences called sticky ends." +
                " These sticky ends are produced when DNA is cut with the same restriction enzymes. Joining of DNA fragments is " +
                "achieved through the formation of covalent bonds between the adjacent nucleotides after complementary base pairs" +
                " have been aligned in the correct sequence. DNA ligase seals the gap between two DNA molecules and, in the case " +
                "of a recombinant plasmid, creates a single piece of circular DNA.");
        works.setWrapText(true);
        works.setFont(Font.font("Arial", 16));
        works.setLineSpacing(3);
        works.setStyle("-fx-text-fill: #333333;");
        content.getChildren().addAll(title, underline, text, heading1, goalText, heading2, works);
        return content;
    }

    private VBox getMaterialsContent() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(40));

        Label title = new Label("2. MATERIALS");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 32));
        title.setStyle("-fx-text-fill: #1a5490;");

        Separator underline = new Separator();
        underline.setMaxWidth(200);
        underline.setStyle("-fx-background-color: #00aaff;");
        underline.setPrefHeight(3);

        Label text = new Label(
                "Materials required for this experiment:\n\n" +
                        "• Digested pKAN-R plasmid fragments\n" +
                        "• Digested pARA-R plasmid fragments\n" +
                        "• T4 DNA Ligase enzyme\n" +
                        "• Ligation buffer (10X)\n" +
                        "• ATP (Adenosine triphosphate)\n" +
                        "• Sterile distilled water\n" +
                        "• Micropipettes (P20, P200)\n" +
                        "• Microcentrifuge tubes\n" +
                        "• Ice bucket\n" +
                        "• 25°C water bath or heat block");
        text.setWrapText(true);
        text.setFont(Font.font("Arial", 16));
        text.setLineSpacing(5);
        text.setStyle("-fx-text-fill: #333333;");

        content.getChildren().addAll(title, underline, text);
        return content;
    }

    private VBox getPredictionsContent() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(40));

        Label title = new Label("3. PREDICTIONS");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 32));
        title.setStyle("-fx-text-fill: #1a5490;");

        Separator underline = new Separator();
        underline.setMaxWidth(200);
        underline.setStyle("-fx-background-color: #00aaff;");
        underline.setPrefHeight(3);

        Label text = new Label(
                "Before performing the ligation experiment, make predictions about what will happen:\n\n" +
                        "1. What products do you expect to form when the DNA fragments are ligated?\n" +
                        "2. Which orientation will the fragments join?\n" +
                        "3. Will all possible combinations form equally?\n\n" +
                        "Use the commands on the right panel to perform the experiment step by step.");
        text.setWrapText(true);
        text.setFont(Font.font("Arial", 16));
        text.setLineSpacing(5);
        text.setStyle("-fx-text-fill: #333333;");

        content.getChildren().addAll(title, underline, text);
        return content;
    }

    private VBox getProtocolContent() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(40));

        Label title = new Label("4. PROTOCOL");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 32));
        title.setStyle("-fx-text-fill: #1a5490;");

        Separator underline = new Separator();
        underline.setMaxWidth(200);
        underline.setStyle("-fx-background-color: #00aaff;");
        underline.setPrefHeight(3);

        Label text = new Label("Detailed protocol will appear here...");
        text.setFont(Font.font("Arial", 16));

        content.getChildren().addAll(title, underline, text);
        return content;
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

        Label text = new Label("Experimental results will be displayed here...");
        text.setFont(Font.font("Arial", 16));

        content.getChildren().addAll(title, underline, text);
        return content;
    }

    private VBox getReflectionContent() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(40));

        Label title = new Label("6. REFLECTION");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 32));
        title.setStyle("-fx-text-fill: #1a5490;");

        Separator underline = new Separator();
        underline.setMaxWidth(200);
        underline.setStyle("-fx-background-color: #00aaff;");
        underline.setPrefHeight(3);

        Label text = new Label("Reflection questions will appear here...");
        text.setFont(Font.font("Arial", 16));

        content.getChildren().addAll(title, underline, text);
        return content;
    }

    private VBox getSummaryContent() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(40));

        Label title = new Label("7. SUMMARY");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 32));
        title.setStyle("-fx-text-fill: #1a5490;");

        Separator underline = new Separator();
        underline.setMaxWidth(200);
        underline.setStyle("-fx-background-color: #00aaff;");
        underline.setPrefHeight(3);

        Label text = new Label("Summary of the experiment will appear here...");
        text.setFont(Font.font("Arial", 16));

        content.getChildren().addAll(title, underline, text);
        return content;
    }

    // Helper method for command results
    private VBox createCommandResultContent(String message) {
        VBox content = new VBox(20);
        content.setPadding(new Insets(40));

        Label title = new Label("✓ Action Completed");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        title.setStyle("-fx-text-fill: #00aa00;");

        Label text = new Label(message);
        text.setWrapText(true);
        text.setFont(Font.font("Arial", 18));
        text.setStyle("-fx-text-fill: #333333;");

        content.getChildren().addAll(title, text);
        return content;
    }
}