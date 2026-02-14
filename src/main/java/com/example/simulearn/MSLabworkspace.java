package com.example.simulearn;


import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.HashMap;
import java.util.Map;

public class MSLabworkspace {
    private Pane labWorkspace;
    private MicropipettingSolutionController controller;

    private static final double TRASH_X = 1050;
    private static final double TRASH_Y = 270;

    private static final double TUBE_RACK_X = 400;
    private static final double TUBE_RACK_Y = 760;

    private static final double TABLE_Y = 850;

    private static final double PIPETTE_STAND_X = 100;
    private static final double PIPETTE_STAND_Y = 125;

    private static final double TIP_BOX_P2_X = 320;
    private static final double TIP_BOX_P2_Y = 180;

    private static final double TIP_BOX_P20_X = 490;
    private static final double TIP_BOX_P20_Y = 180;

    private static final double TIP_BOX_P200_X = 660;
    private static final double TIP_BOX_P200_Y = 180;

    private static final double TIP_BOX_P1000_X = 830;
    private static final double TIP_BOX_P1000_Y = 180;

    private static final double BLOTTING_PAPER_X = 600;
    private static final double BLOTTING_PAPER_Y = 650;

    private ImageView pipetteStandView;
    private ImageView trashView;

    // Tube states
    private Map<String, TubeState> tubeStates = new HashMap<>();
    // Pipettes  we have 4 separate draggable pipettes
    private Map<String, DraggablePipette> pipettes = new HashMap<>();

    private Map<String, ImageView> tipBoxViews = new HashMap<>();
    private Map<String, Boolean> tipBoxStates = new HashMap<>();

    // Track currently active pipette
    private DraggablePipette currentlyActivePipette = null;

    // Blotting paper
    private BlottingPaper blottingPaper;


    // Tubes
    private Map<String, DraggableTube> tubes = new HashMap<>();


    public MSLabworkspace(MicropipettingSolutionController controller) {
        this.controller = controller;
        initializeTubeStates();
        initializeTipBoxStates();
        createMSLabWorkspace();
    }

    private void initializeTubeStates() {
        tubeStates.put("Red Dye", new TubeState("Red Dye", 10, false, Color.web("#ff0000")));
    }

    private void initializeTipBoxStates() {
        tipBoxStates.put("P2", false);
        tipBoxStates.put("P20", false);
        tipBoxStates.put("P200", false);
        tipBoxStates.put("P1000", false);
    }

    public Pane getMSLabWorkspace() {
        return labWorkspace;
    }

    private void createMSLabWorkspace() {
        labWorkspace = new Pane();
        labWorkspace.setPrefSize(1515, 1056);
        labWorkspace.setMinSize(1515, 1056);
        labWorkspace.setMaxSize(1515, 1056);
        labWorkspace.setStyle("-fx-background-color: linear-gradient(to bottom, #0d5f7f 0%, #1a8ab5 50%);");

        // Create bench surface
        Rectangle bench = new Rectangle(0, TABLE_Y, 1515, 1056 - TABLE_Y);
        bench.setFill(Color.web("#b0b8c0"));
        labWorkspace.getChildren().add(bench);

        // Create drawers
        Rectangle drawer1 = new Rectangle(100, TABLE_Y + 50, 150, 60);
        drawer1.setFill(Color.web("#8a95a0"));
        drawer1.setStroke(Color.web("#6a7580"));
        drawer1.setStrokeWidth(2);
        drawer1.setArcWidth(5);
        drawer1.setArcHeight(5);

        Rectangle drawerHandle1 = new Rectangle(160, TABLE_Y + 75, 30, 10);
        drawerHandle1.setFill(Color.web("#5a6570"));
        drawerHandle1.setArcWidth(3);
        drawerHandle1.setArcHeight(3);

        Rectangle drawer2 = new Rectangle(100, TABLE_Y + 125, 150, 60);
        drawer2.setFill(Color.web("#8a95a0"));
        drawer2.setStroke(Color.web("#6a7580"));
        drawer2.setStrokeWidth(2);
        drawer2.setArcWidth(5);
        drawer2.setArcHeight(5);

        Rectangle drawerHandle2 = new Rectangle(160, TABLE_Y + 150, 30, 10);
        drawerHandle2.setFill(Color.web("#5a6570"));
        drawerHandle2.setArcWidth(3);
        drawerHandle2.setArcHeight(3);

        Rectangle shelf = new Rectangle(50, 323, 1200, 20);
        shelf.setFill(Color.web("#b0b8c0"));

        labWorkspace.getChildren().addAll(drawer1, drawerHandle1, drawer2, drawerHandle2, shelf);

        // Create all equipment
        createTubes();
        createEquipment();
        createTipBoxes();
        createPipettes();
        createBlottingPaper();
    }

    private void createBlottingPaper() {
        blottingPaper = new BlottingPaper(BLOTTING_PAPER_X, BLOTTING_PAPER_Y);
        labWorkspace.getChildren().add(blottingPaper.getView());
    }

    private void createEquipment() {
        Image image = new Image(
                getClass().getResource("/Images/MSImages/equipment/Trash.png").toExternalForm()
        );
        trashView = new ImageView(image);
        trashView.setLayoutX(TRASH_X);
        trashView.setLayoutY(TRASH_Y);
        trashView.setCursor(Cursor.HAND);
        labWorkspace.getChildren().add(trashView);

        // Pipette Stand - ORIGINAL
        pipetteStandView = createEquipmentImage("/Images/MSImages/PipetteRack.svg",
                180, 220, PIPETTE_STAND_X, PIPETTE_STAND_Y);
    }

    private void createTubes() {

        Map<String, TubeImagePaths> tubeImageMap = new HashMap<>();

        // Define image paths for each tube type (closed and open versions)
        tubeImageMap.put("Red Dye", new TubeImagePaths(
                "/Images/MSImages/RedDye-close.svg",
                "/Images/MSImages/RedDye-open.svg"
        ));
        DraggableTube tube = new DraggableTube("Red Dye", TUBE_RACK_X+80, TUBE_RACK_Y+5, tubeImageMap.get("Red Dye"));

        tubes.put("Red Dye", tube);
        labWorkspace.getChildren().add(tube.getView());
        ImageView tubeRack = createEquipmentImage("/Images/MSImages/equipment/empty-tube-stand.svg",
                200, 90, TUBE_RACK_X, TUBE_RACK_Y);
    }

    private void createPipettes() {

        DraggablePipette p2 = new DraggablePipette("P2",
                PIPETTE_STAND_X + 25,
                PIPETTE_STAND_Y,
                "/Images/MSImages/pipettes/P2-body.svg",
                "/Images/MSImages/pipettes/P2-bodywithtip.svg");
        pipettes.put("P2", p2);
        labWorkspace.getChildren().add(p2.getView());


        DraggablePipette p20 = new DraggablePipette("P20",
                PIPETTE_STAND_X + 60,
                PIPETTE_STAND_Y,
                "/Images/MSImages/pipettes/P20-body.svg",
                "/Images/MSImages/pipettes/P20-bodywithtip.svg");
        pipettes.put("P20", p20);
        labWorkspace.getChildren().add(p20.getView());


        DraggablePipette p200 = new DraggablePipette("P200",
                PIPETTE_STAND_X + 95,
                PIPETTE_STAND_Y,
                "/Images/MSImages/pipettes/P200-body.svg",
                "/Images/MSImages/pipettes/P200-bodywithtip.svg");
        pipettes.put("P200", p200);
        labWorkspace.getChildren().add(p200.getView());


        DraggablePipette p1000 = new DraggablePipette("P1000",
                PIPETTE_STAND_X + 130,
                PIPETTE_STAND_Y,
                "/Images/MSImages/pipettes/P1000-body.svg",
                "/Images/MSImages/pipettes/P1000-bodywithtip.svg");
        pipettes.put("P1000", p1000);
        labWorkspace.getChildren().add(p1000.getView());
    }

    private void showMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Lab Action");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.show();

        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(2), e -> alert.close()));
        timeline.play();
    }


    private void notifyStepComplete(String step, String subStep) {
        if (controller != null) {
            controller.highlightNextInstruction(step, subStep);
        }
    }

    private class DraggablePipette {
        private Group pipetteGroup;
        private ImageView bodyView;
        private ImageView plungerView;
        private Label volumeBadge;

        private String type;
        private double x, y;
        private boolean hasTip = false;
        private boolean hasSolution = false;
        private double initialX, initialY;
        private String noTipImagePath;
        private String withTipImagePath;
        private String withTipWithSolutionPath;
        private String plungerPath;

        // Volume settings
        private double currentVolume;
        private double minVolume;
        private double maxVolume;
        private Stage volumeDialog;

        // Eject tip button (shown only when near trash)
        private Button ejectButton;

        DraggablePipette(String type, double x, double y, String noTipPath, String withTipPath) {
            this.type = type;
            this.x = x;
            this.y = y;
            this.initialX = x;
            this.initialY = y;
            this.noTipImagePath = noTipPath;
            this.withTipImagePath = withTipPath;
            this.plungerPath = noTipPath.replace("-body.svg", "-plunger.svg");
            this.withTipWithSolutionPath = withTipPath.replace("-bodywithtip.svg", "-withsolution.svg");

            // Set volume limits based on pipette type
            setVolumeLimits();

            createView();
        }

        private void setVolumeLimits() {
            switch (type) {
                case "P2":
                    minVolume = 0.2;  // 0.5 rounded to 1
                    maxVolume = 2;
                    currentVolume = 0.2;
                    break;
                case "P20":
                    minVolume = 2;
                    maxVolume = 20;
                    currentVolume = 2;
                    break;
                case "P200":
                    minVolume = 20;
                    maxVolume = 200;
                    currentVolume = 20;
                    break;
                case "P1000":
                    minVolume = 200;
                    maxVolume = 1000;
                    currentVolume = 200;
                    break;
            }
        }

        private void createView() {
            // Load body and plunger separately
            bodyView = SVGLoader.loadSVG(noTipImagePath, 30, 200);
            plungerView = SVGLoader.loadSVG(plungerPath, 20, 30);

            // Position plunger at top
            plungerView.setLayoutX(5);
            plungerView.setLayoutY(3);

            // Position body below plunger
            bodyView.setLayoutX(0);
            bodyView.setLayoutY(15);

            // Create pipette name label (always visible above pipette)
            Label nameLabel = new Label(type);
            nameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
            nameLabel.setTextFill(Color.WHITE);
            nameLabel.setStyle("-fx-background-color: #FFA500; -fx-padding: 3px 8px; -fx-background-radius: 10px;");
            nameLabel.setLayoutX(-5);  // Center above pipette
            nameLabel.setLayoutY(-20); // Above the plunger

            // Create volume badge (initially hidden)
            String volumeText = (currentVolume == (int) currentVolume) ?
                    (int) currentVolume + "μL" :
                    String.format("%.1f", currentVolume) + "μL";
            volumeBadge = new Label(volumeText);
            volumeBadge.setFont(Font.font("Arial", FontWeight.BOLD, 14));
            volumeBadge.setTextFill(Color.WHITE);
            volumeBadge.setStyle("-fx-background-color: #54BCE3; -fx-padding: 5px 10px; -fx-background-radius: 15px;");
            volumeBadge.setLayoutX(-60);  // Left side of pipette
            volumeBadge.setLayoutY(100);
            volumeBadge.setVisible(false);  // Hidden initially
            volumeBadge.setCursor(Cursor.HAND);

            // Volume badge click opens dialog
            volumeBadge.setOnMouseClicked(e -> {
                openVolumeDialog();
                e.consume();
            });

            // Create eject tip button (initially hidden)
            ejectButton = new Button("Eject Tip");
            ejectButton.setFont(Font.font("Arial", FontWeight.BOLD, 12));
            ejectButton.setStyle("-fx-background-color: #E74C3C; -fx-text-fill: white; " +
                    "-fx-padding: 8px 15px; -fx-background-radius: 15px; " +
                    "-fx-cursor: hand;");
            ejectButton.setLayoutX(-70);  // Left side of pipette
            ejectButton.setLayoutY(140);  // Below volume badge
            ejectButton.setVisible(false);  // Hidden initially
            ejectButton.setCursor(Cursor.HAND);

            // Eject button click handler
            ejectButton.setOnAction(e -> {
                ejectTip();
                showMessage("Tip ejected from " + type + " pipette!");

                // ✅ Step 3.c: Eject tip over trash
                if (controller != null) {
                    // Only complete if we're on step 3.c
                    if (controller.getActiveStep() == 3 && controller.getActiveSubStep().equals("c")) {
                        controller.completeCurrentStep();
                    }
                }

                ejectButton.setVisible(false);  // Hide button after ejecting
                e.consume();
            });

            // Create group with body, plunger, name label, badge and eject button
            pipetteGroup = new Group(bodyView, plungerView, nameLabel, volumeBadge, ejectButton);
            pipetteGroup.setLayoutX(x);
            pipetteGroup.setLayoutY(y);
            pipetteGroup.setCursor(Cursor.HAND);

            DropShadow shadow = new DropShadow();
            shadow.setColor(Color.rgb(0, 0, 0, 0.3));
            shadow.setRadius(5);
            pipetteGroup.setEffect(shadow);

            // Only plunger double-clickable for solution draw/dispense
            plungerView.setOnMouseClicked(e -> {
                if (e.getClickCount() == 2) { // Double click required
                    handlePlungerClick();
                }
                e.consume();
            });

            makePipetteDraggable();
        }

        private void makePipetteDraggable() {
            final Delta dragDelta = new Delta();

            pipetteGroup.setOnMousePressed(e -> {
                dragDelta.x = pipetteGroup.getLayoutX() - e.getSceneX();
                dragDelta.y = pipetteGroup.getLayoutY() - e.getSceneY();
                pipetteGroup.setCursor(Cursor.MOVE);
                pipetteGroup.toFront();

                // Hide all other pipettes' volume badges
                if (currentlyActivePipette != null && currentlyActivePipette != DraggablePipette.this) {
                    currentlyActivePipette.hideVolumeBadge();
                }

                // Set this pipette as currently active
                currentlyActivePipette = DraggablePipette.this;

                // Show volume badge when dragged from stand
                if (!volumeBadge.isVisible()) {
                    volumeBadge.setVisible(true);
                }

                // ✅ Step 1.a: Select the P20 micropipette
                if (type.equals("P20") && controller != null) {
                    // Only complete if we're on step 1.a
                    if (controller.getActiveStep() == 1 && controller.getActiveSubStep().equals("a")) {
                        controller.completeCurrentStep();
                    }
                }

                e.consume();
            });

            pipetteGroup.setOnMouseDragged(e -> {
                x = e.getSceneX() + dragDelta.x;
                y = e.getSceneY() + dragDelta.y;
                pipetteGroup.setLayoutX(x);
                pipetteGroup.setLayoutY(y);

                // Show eject button if in trash vertical column and has tip
                if (hasTip && isInVerticalColumn(x, y, TRASH_X, TRASH_Y, 80)) {
                    ejectButton.setVisible(true);
                } else {
                    ejectButton.setVisible(false);
                }

                e.consume();
            });

            pipetteGroup.setOnMouseReleased(e -> {
                pipetteGroup.setCursor(Cursor.HAND);
                checkPipetteInteractions();
                e.consume();
            });
        }

        private void checkPipetteInteractions() {
            // Get tip box position for this pipette type
            double tipBoxX = 0, tipBoxY = 0;

            switch (type) {
                case "P2":
                    tipBoxX = TIP_BOX_P2_X;
                    tipBoxY = TIP_BOX_P2_Y;
                    break;
                case "P20":
                    tipBoxX = TIP_BOX_P20_X;
                    tipBoxY = TIP_BOX_P20_Y;
                    break;
                case "P200":
                    tipBoxX = TIP_BOX_P200_X;
                    tipBoxY = TIP_BOX_P200_Y;
                    break;
                case "P1000":
                    tipBoxX = TIP_BOX_P1000_X;
                    tipBoxY = TIP_BOX_P1000_Y;
                    break;
            }

            // Check if in tip box vertical column and tip box is open and pipette doesn't have tip
            if (isInVerticalColumn(x, y, tipBoxX, tipBoxY, 80) &&
                    tipBoxStates.get(type) &&
                    !hasTip) {

                attachTip();
                showMessage("Tip attached to " + type + " pipette!");

                // ✅ Step 1.d: Attach tip to P20 micropipette
                if (type.equals("P20") && controller != null) {
                    // Only complete if we're on step 1.d
                    if (controller.getActiveStep() == 1 && controller.getActiveSubStep().equals("d")) {
                        controller.completeCurrentStep();
                    }
                }
            }

            // Hide eject button when released (user can click it before releasing)
            if (!isInVerticalColumn(x, y, TRASH_X, TRASH_Y, 80)) {
                ejectButton.setVisible(false);
            }

            // Check if near pipette stand to return
            if (isNear(x, y, initialX, initialY, 50)) {
                returnToStand();
            }
        }

        private void attachTip() {
            hasTip = true;
            updatePipetteImage();
        }

        private void ejectTip() {
            hasTip = false;
            hasSolution = false; // Tip chole gele solution o chole jabe
            updatePipetteImage();
        }

        private void updatePipetteImage() {
            String imagePath;

            // Choose image based on tip and solution state
            // Plunger stays same, only body changes
            if (hasTip && hasSolution) {
                imagePath = withTipWithSolutionPath;
            } else if (hasTip && !hasSolution) {
                imagePath = withTipImagePath;
            } else {
                imagePath = noTipImagePath;
            }

            // Update only body, plunger stays same
            ImageView newBody = SVGLoader.loadSVG(imagePath, 30, 200);
            newBody.setLayoutX(0);
            newBody.setLayoutY(15);

            pipetteGroup.getChildren().remove(bodyView);
            bodyView = newBody;
            pipetteGroup.getChildren().add(0, bodyView);
        }


        private void handlePlungerClick() {
            if (!hasTip) {
                showMessage("Attach a tip first before using pipette!");
                return;
            }

            // Check if pipette is over blotting paper
            String circlePosition = blottingPaper.getCircleAtPosition(x, y);
            if (circlePosition != null && hasSolution) {
                // Dispense onto blotting paper
                blottingPaper.dispenseVolume(circlePosition, currentVolume);
                hasSolution = false;
                updatePipetteImage();
                showMessage("Dispensed " + currentVolume + "µl onto circle " + circlePosition + "!");

                // ✅ Step 3.a and 3.b: Dispense to circle A
                if (circlePosition.equals("A") && controller != null) {
                    // Check if we're on step 3.a
                    if (controller.getActiveStep() == 3 && controller.getActiveSubStep().equals("a")) {
                        controller.completeCurrentStep(); // Complete 3.a (move to circle A)
                        controller.completeCurrentStep(); // Complete 3.b (dispense)
                    }
                }

                // ✅ Step 4.a: Dispense to circle B (15µl)
                if (circlePosition.equals("B") && controller != null) {
                    // Check if we're on step 4.a
                    if (controller.getActiveStep() == 4 && controller.getActiveSubStep().equals("a")) {
                        controller.completeCurrentStep();
                    }
                }

                // ✅ Step 4.b: Dispense to circle C (7.5µl)
                if (circlePosition.equals("C") && controller != null) {
                    // Check if we're on step 4.b
                    if (controller.getActiveStep() == 4 && controller.getActiveSubStep().equals("b")) {
                        controller.completeCurrentStep();
                    }
                }

                // ✅ Step 4.c: Dispense to circle D (2µl)
                if (circlePosition.equals("D") && controller != null) {
                    // Check if we're on step 4.c
                    if (controller.getActiveStep() == 4 && controller.getActiveSubStep().equals("c")) {
                        controller.completeCurrentStep();
                    }
                }

                return;
            }

            // Check if pipette is over any tube
            String overTube = getTubeUnderPipette();

            if (overTube == null && circlePosition == null) {
                showMessage("Position pipette over a tube or blotting paper!");
                return;
            }

            if (overTube != null) {
                // Toggle solution state
                if (!hasSolution) {
                    // Draw solution
                    hasSolution = true;
                    updatePipetteImage();
                    showMessage("Solution drawn from " + overTube + " tube into " + type + " pipette!");

                    // ✅ Step 2.b and 2.c: Place pipette in Red Dye and draw solution
                    if (overTube.equals("Red Dye") && controller != null) {
                        // Check if we're on step 2.b
                        if (controller.getActiveStep() == 2 && controller.getActiveSubStep().equals("b")) {
                            controller.completeCurrentStep(); // Complete 2.b (place in solution)
                            controller.completeCurrentStep(); // Complete 2.c (draw solution)
                        }
                    }
                } else {
                    // Dispense solution
                    hasSolution = false;
                    updatePipetteImage();
                    showMessage("Solution dispensed from " + type + " pipette into " + overTube + " tube!");
                }
            }
        }

        private String getTubeUnderPipette() {
            // Check if pipette is in vertical column above any tube
            for (Map.Entry<String, DraggableTube> entry : tubes.entrySet()) {
                DraggableTube tube = entry.getValue();
                // Use vertical column check (same X, pipette at or above tube)
                if (isInVerticalColumn(x, y, tube.getX(), tube.getY(), 40)) {
                    // Check if tube is open
                    TubeState state = tubeStates.get(entry.getKey());
                    if (state != null && state.isOpen) {
                        return entry.getKey();
                    } else if (state != null && !state.isOpen) {
                        showMessage("Please open the " + entry.getKey() + " tube first!");
                        return null;
                    }
                }
            }
            return null;
        }

        private void openVolumeDialog() {
            if (volumeDialog != null && volumeDialog.isShowing()) {
                return;
            }

            volumeDialog = new Stage();
            volumeDialog.initModality(Modality.APPLICATION_MODAL);
            volumeDialog.setTitle(type + " micropipette volume");

            VBox mainBox = new VBox(15);
            mainBox.setPadding(new Insets(20));
            mainBox.setStyle("-fx-background-color: white;");

            // Title section
            Label titleLabel = new Label("Turn the top to change the volume.");
            titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
            titleLabel.setTextFill(Color.WHITE);
            titleLabel.setStyle("-fx-background-color: #1a5f7a; -fx-padding: 10px;");
            titleLabel.setMaxWidth(Double.MAX_VALUE);

            // Volume display and controls
            VBox volumeBox = new VBox(10);
            volumeBox.setPadding(new Insets(10));
            volumeBox.setStyle("-fx-border-color: #cccccc; -fx-border-width: 1px;");

            Label volumeLabel = new Label("Micropipette volume");
            volumeLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 12));

            HBox volumeControlBox = new HBox(10);
            volumeControlBox.setAlignment(Pos.CENTER);

            TextField volumeField = new TextField(String.valueOf(currentVolume));
            volumeField.setPrefWidth(100);
            volumeField.setStyle("-fx-font-size: 14px;");

            Label unitLabel = new Label("μL");
            unitLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 14));

            Button plusButton = new Button("+");
            plusButton.setStyle("-fx-background-color: #FFA500; -fx-text-fill: white; -fx-font-size: 18px; -fx-min-width: 40px; -fx-min-height: 40px; -fx-background-radius: 50%;");

            Button minusButton = new Button("-");
            minusButton.setStyle("-fx-background-color: #FFA500; -fx-text-fill: white; -fx-font-size: 18px; -fx-min-width: 40px; -fx-min-height: 40px; -fx-background-radius: 50%;");

            volumeControlBox.getChildren().addAll(minusButton, volumeField, unitLabel, plusButton);

            Button resetButton = new Button("Reset volume");
            resetButton.setStyle("-fx-background-color: transparent; -fx-text-fill: #1a5f7a; -fx-underline: true;");
            resetButton.setOnAction(e -> {
                double defaultVol = (minVolume + maxVolume) / 2;
                volumeField.setText(String.valueOf(defaultVol));
            });

            volumeBox.getChildren().addAll(volumeLabel, volumeControlBox, resetButton);

            // Tip check section
            VBox tipCheckBox = new VBox(10);
            tipCheckBox.setPadding(new Insets(10));
            tipCheckBox.setStyle("-fx-border-color: #cccccc; -fx-border-width: 1px; -fx-background-color: #f9f9f9;");

            Label warningLabel = new Label("⚠ Double check");
            warningLabel.setFont(Font.font("Arial", FontWeight.BOLD, 13));

            Label tipWarningText = new Label("Make sure there isn't a tip attached when you change the volume to avoid contamination.");
            tipWarningText.setWrapText(true);
            tipWarningText.setFont(Font.font("Arial", FontWeight.NORMAL, 12));

            Label tipStatusLabel = new Label("Tip Attached: " + (hasTip ? "Yes ❌" : "No ✓"));
            tipStatusLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
            tipStatusLabel.setStyle("-fx-background-color: " + (hasTip ? "#ffcccc" : "#ccffcc") + "; -fx-padding: 5px 10px; -fx-background-radius: 5px;");

            tipCheckBox.getChildren().addAll(warningLabel, tipWarningText, tipStatusLabel);

            // Back button
            Button backButton = new Button("← Back");
            backButton.setStyle("-fx-background-color: white; -fx-text-fill: #1a5f7a; -fx-font-size: 14px; -fx-padding: 10px 20px; -fx-border-color: #1a5f7a; -fx-border-width: 2px;");
            backButton.setOnAction(e -> {
                // Update volume from field
                try {
                    double newVolume = Double.parseDouble(volumeField.getText());
                    if (newVolume >= minVolume && newVolume <= maxVolume) {
                        currentVolume = newVolume;
                        // Format badge display
                        if (newVolume == (int) newVolume) {
                            volumeBadge.setText((int) newVolume + "μL");
                        } else {
                            volumeBadge.setText(String.format("%.1f", newVolume) + "μL");
                        }

                        // ✅ Step 1.b: Set volume to 20μl and save
                        if (type.equals("P20") && newVolume == 20.0 && controller != null) {
                            // Only complete if we're on step 1.b
                            if (controller.getActiveStep() == 1 && controller.getActiveSubStep().equals("b")) {
                                controller.completeCurrentStep();
                            }
                        }
                    } else {
                        showMessage("Volume must be between " + minVolume + " and " + maxVolume + " μL");
                    }
                } catch (NumberFormatException ex) {
                    showMessage("Invalid volume value!");
                }
                volumeDialog.close();
            });

            // Plus button action
            plusButton.setOnAction(e -> {
                try {
                    double vol = Double.parseDouble(volumeField.getText());
                    // Increment based on pipette type
                    if (type.equals("P2")) {
                        vol += 0.2;
                    } else if (type.equals("P20")) {
                        vol += 2;
                    } else {
                        vol += 5;
                    }

                    if (vol <= maxVolume) {
                        // Format based on whether it has decimals
                        if (vol == (int) vol) {
                            volumeField.setText(String.valueOf((int) vol));
                        } else {
                            volumeField.setText(String.format("%.1f", vol));
                        }
                    }
                } catch (NumberFormatException ex) {
                    showMessage("Invalid volume value!");
                }
            });

            // Minus button action
            minusButton.setOnAction(e -> {
                try {
                    double vol = Double.parseDouble(volumeField.getText());
                    // Decrement based on pipette type
                    if (type.equals("P2")) {
                        vol -= 0.2;
                    } else if (type.equals("P20")) {
                        vol -= 2;
                    } else {
                        vol -= 5;
                    }

                    if (vol >= minVolume) {
                        // Format based on whether it has decimals
                        if (vol == (int) vol) {
                            volumeField.setText(String.valueOf((int) vol));
                        } else {
                            volumeField.setText(String.format("%.1f", vol));
                        }
                    }
                } catch (NumberFormatException ex) {
                    showMessage("Invalid volume value!");
                }
            });

            mainBox.getChildren().addAll(titleLabel, volumeBox, tipCheckBox, backButton);

            Scene dialogScene = new Scene(mainBox, 400, 450);
            volumeDialog.setScene(dialogScene);
            volumeDialog.show();
        }

        private void returnToStand() {
            TranslateTransition transition = new TranslateTransition(Duration.millis(300), pipetteGroup);
            transition.setToX(initialX - x);
            transition.setToY(initialY - y);
            transition.setOnFinished(e -> {
                pipetteGroup.setLayoutX(initialX);
                pipetteGroup.setLayoutY(initialY);
                pipetteGroup.setTranslateX(0);
                pipetteGroup.setTranslateY(0);
                x = initialX;
                y = initialY;

                // ✅ Step 3.d: Return P20 pipette to stand
                if (type.equals("P20") && controller != null) {
                    // Only complete if we're on step 3.d
                    if (controller.getActiveStep() == 3 && controller.getActiveSubStep().equals("d")) {
                        controller.completeCurrentStep();
                    }
                }
            });
            transition.play();
        }

        // Helper method to hide volume badge
        private void hideVolumeBadge() {
            volumeBadge.setVisible(false);
            ejectButton.setVisible(false);  // Also hide eject button
        }

        public Group getView() {
            return pipetteGroup;
        }

        public double getX() {
            return x;
        }

        public double getY() {
            return y;
        }

        public boolean hasTip() {
            return hasTip;
        }
    }

    private void createTipBoxes() {
        // ORIGINAL TIP BOX CREATION with exact paths - P2, P20, P200, P1000
        // P2 Tips
        createTipBoxImage("/Images/MSImages/tipboxes/P2-sm-close.svg",
                "P2",
                "/Images/MSImages/tipboxes/P2-sm-open.svg",
                TIP_BOX_P2_X, TIP_BOX_P2_Y);
        addTipBoxLabel("P2", TIP_BOX_P2_X, TIP_BOX_P2_Y - 20);

        // P20 Tips
        createTipBoxImage("/Images/MSImages/tipboxes/P20-sm-close.svg",
                "P20",
                "/Images/MSImages/tipboxes/P20-sm-open.svg",
                TIP_BOX_P20_X, TIP_BOX_P20_Y);
        addTipBoxLabel("P20", TIP_BOX_P20_X, TIP_BOX_P20_Y - 20);

        // P200 Tips
        createTipBoxImage("/Images/MSImages/tipboxes/P200-sm-close.svg",
                "P200",
                "/Images/MSImages/tipboxes/P200-sm-open.svg",
                TIP_BOX_P200_X, TIP_BOX_P200_Y);
        addTipBoxLabel("P200", TIP_BOX_P200_X, TIP_BOX_P200_Y - 20);

        // P1000 Tips
        createTipBoxImage("/Images/MSImages/tipboxes/P1000-sm-close.svg",
                "P1000",
                "/Images/MSImages/tipboxes/P1000-sm-open.svg",
                TIP_BOX_P1000_X, TIP_BOX_P1000_Y);
        addTipBoxLabel("P1000", TIP_BOX_P1000_X, TIP_BOX_P1000_Y - 20);
    }

    private void createTipBoxImage(String closedPath, String type, String openPath, double x, double y) {
        ImageView tipBox = createEquipmentImage(closedPath, 170, 141.67, x, y);
        tipBox.setOnMouseClicked(e -> toggleTipBox(type, openPath, closedPath));
        tipBoxViews.put(type, tipBox);
    }

    private void addTipBoxLabel(String text, double x, double y) {
        Label label = new Label(text + " Tips");
        label.setFont(Font.font("Arial", FontWeight.BOLD, 13));
        label.setTextFill(Color.WHITE);
        label.setLayoutX(x);
        label.setLayoutY(y);
        labWorkspace.getChildren().add(label);
    }

    private void toggleTipBox(String type, String openPath, String closedPath) {
        boolean isOpen = tipBoxStates.get(type);
        tipBoxStates.put(type, !isOpen);

        String imagePath = !isOpen ? openPath : closedPath;

        ImageView tipBox = tipBoxViews.get(type);
        ImageView newView = SVGLoader.loadSVG(imagePath, 170, 141.667);
        newView.setLayoutX(tipBox.getLayoutX());
        newView.setLayoutY(tipBox.getLayoutY());
        newView.setCursor(Cursor.HAND);
        newView.setOnMouseClicked(e -> toggleTipBox(type, openPath, closedPath));

        DropShadow shadow = new DropShadow();
        shadow.setColor(Color.rgb(0, 0, 0, 0.3));
        shadow.setRadius(5);
        newView.setEffect(shadow);

        labWorkspace.getChildren().remove(tipBox);
        tipBoxViews.put(type, newView);
        labWorkspace.getChildren().add(newView);

        // ✅ Step 1.c: Open P20 tip box
        if (!isOpen && type.equals("P20") && controller != null) {
            // Only complete if we're on step 1.c
            if (controller.getActiveStep() == 1 && controller.getActiveSubStep().equals("c")) {
                controller.completeCurrentStep();
            }
        }

        // ✅ Step 1.e: Close P20 tip box
        if (isOpen && type.equals("P20") && controller != null) {
            // Only complete if we're on step 1.e
            if (controller.getActiveStep() == 1 && controller.getActiveSubStep().equals("e")) {
                controller.completeCurrentStep();
            }
        }
    }

    private ImageView createEquipmentImage(String path, double width, double height, double x, double y) {
        ImageView view = SVGLoader.loadSVG(path, width, height);
        view.setLayoutX(x);
        view.setLayoutY(y);
        view.setCursor(Cursor.HAND);

        DropShadow shadow = new DropShadow();
        shadow.setColor(Color.rgb(0, 0, 0, 0.3));
        shadow.setRadius(5);
        view.setEffect(shadow);

        labWorkspace.getChildren().add(view);
        return view;
    }

    // ========== HELPER METHODS ==========
    private boolean isNear(double x1, double y1, double x2, double y2, double threshold) {
        double distance = Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
        return distance < threshold;
    }

    // Check if pipette is in vertical column above target (same X, any Y above)
    private boolean isInVerticalColumn(double pipetteX, double pipetteY, double targetX, double targetY, double xThreshold) {
        // Check if X is within threshold
        boolean xMatch = Math.abs(pipetteX - targetX) < xThreshold;
        // Check if pipette is at or above the target (Y <= targetY since Y increases downward)
        boolean yAbove = pipetteY <= targetY;
        return xMatch && yAbove;
    }

    private class Delta {
        double x, y;
    }

    private class TubeImagePaths {
        String closedImagePath;
        String openImagePath;

        TubeImagePaths(String closedImagePath, String openImagePath) {
            this.closedImagePath = closedImagePath;
            this.openImagePath = openImagePath;
        }
    }

    // ========== DRAGGABLE TUBE CLASS ==========
    private class DraggableTube {
        private VBox view;
        private String name;
        private double x, y;
        private double initialX, initialY;  // Store initial position for freezer
        private boolean isOpen = false;  // Initially CLOSED
        private TubeImagePaths imagePaths;

        // Rack attachment
        private boolean inRack = false;
        private int rackPosition = -1;  // Position in rack (0, 1, 2)
        private double rackBaseX = 0;
        private double rackBaseY = 0;

        DraggableTube(String name, double x, double y, TubeImagePaths imagePaths) {
            this.name = name;
            this.x = x;
            this.y = y;
            this.initialX = x;  // Store initial position
            this.initialY = y;
            this.imagePaths = imagePaths;
            createView();
        }

        private void createView() {
            view = new VBox(2);
            view.setAlignment(Pos.CENTER);
            view.setLayoutX(x);
            view.setLayoutY(y);
            view.setCursor(Cursor.HAND);

            // Initially show CLOSED tube with specific image for this tube
            ImageView tubeImage = SVGLoader.loadSVG(imagePaths.closedImagePath, 50, 70);

            Label label = new Label(name);
            label.setFont(Font.font("Arial", FontWeight.BOLD, 10));
            label.setTextFill(Color.WHITE);
            label.setRotate(-90);

            view.getChildren().addAll(label,tubeImage);

            makeTubeDraggable();
        }

        private void toggleTube() {
            // Toggle state
            isOpen = !isOpen;

            // Update tube state
            TubeState state = tubeStates.get(name);
            if (state != null) {
                state.isOpen = isOpen;
            }

            // Choose appropriate image based on state
            String imagePath = isOpen ? imagePaths.openImagePath : imagePaths.closedImagePath;

            // Update the image
            ImageView newTubeImage = SVGLoader.loadSVG(imagePath, 50, 70);
            view.getChildren().set(0, newTubeImage);

            // Notify controller based on tube state changes
            if (isOpen) {
                // ✅ Step 2.a: Open Red Dye tube
                if (name.equals("Red Dye") && controller != null) {
                    // Only complete if we're on step 2.a
                    if (controller.getActiveStep() == 2 && controller.getActiveSubStep().equals("a")) {
                        controller.completeCurrentStep();
                    }
                }

                switch (name) {
                    case "A+":
                        notifyStepComplete("3", "a");
                        break;
                    case "LIG":
                        notifyStepComplete("3", "e");
                        break;
                }
            } else {
                // ✅ Step 2.d: Close Red Dye tube
                if (name.equals("Red Dye") && controller != null) {
                    // Only complete if we're on step 2.d
                    if (controller.getActiveStep() == 2 && controller.getActiveSubStep().equals("d")) {
                        controller.completeCurrentStep();
                    }
                }

                switch (name) {
                    case "A+":
                        notifyStepComplete("3", "d");
                        break;
                }
            }
        }

        private void makeTubeDraggable() {
            final Delta dragDelta = new Delta();
            final boolean[] isDragging = {false}; // Track if actually dragging

            view.setOnMousePressed(e -> {
                dragDelta.x = view.getLayoutX() - e.getSceneX();
                dragDelta.y = view.getLayoutY() - e.getSceneY();
                isDragging[0] = false; // Reset drag flag
                e.consume();
            });

            view.setOnMouseDragged(e -> {
                isDragging[0] = true; // Mark as dragging
                view.setCursor(Cursor.MOVE);

                // If in rack, detach from rack when starting to drag


                x = e.getSceneX() + dragDelta.x;
                y = e.getSceneY() + dragDelta.y;
                view.setLayoutX(x);
                view.setLayoutY(y);
                e.consume();
            });

            view.setOnMouseReleased(e -> {
                view.setCursor(Cursor.HAND);

                // Only toggle if NOT dragging (pure click)
                if (!isDragging[0] && e.getClickCount() == 1) {
                    toggleTube();
                }


                e.consume();
            });
        }


        public VBox getView() {
            return view;
        }

        public double getX() {
            return x;
        }

        public double getY() {
            return y;
        }

        // Rack attachment methods
        public void setInRack(boolean inRack, int position, double rackX, double rackY) {
            this.inRack = inRack;
            this.rackPosition = position;
            this.rackBaseX = rackX;
            this.rackBaseY = rackY;

            if (inRack) {
                // Position tube in rack (spacing: 35 pixels between tubes)
                x = rackX + 25 + (position * 35);
                y = rackY + 15;
                view.setLayoutX(x);
                view.setLayoutY(y);
            }
        }

        public void setRackPosition(int position, double rackX, double rackY) {
            this.rackPosition = position;
            this.rackBaseX = rackX;
            this.rackBaseY = rackY;

            // Update position
            x = rackX + 25 + (position * 35);
            y = rackY + 15;
            view.setLayoutX(x);
            view.setLayoutY(y);
        }

        public void moveWithRack(double rackX, double rackY) {
            if (inRack) {
                this.rackBaseX = rackX;
                this.rackBaseY = rackY;
                x = rackX + 25 + (rackPosition * 35);
                y = rackY + 15;
                view.setLayoutX(x);
                view.setLayoutY(y);
            }
        }
    }

    private class TubeState {
        String name;
        int volume;
        boolean isOpen;
        Color liquidColor;

        TubeState(String name, int volume, boolean isOpen, Color liquidColor) {
            this.name = name;
            this.volume = volume;
            this.isOpen = isOpen;
            this.liquidColor = liquidColor;
        }
    }

    private class BlottingPaper {
        private Group paperGroup;
        private double x, y;
        private Map<String, Double> circleVolumes = new HashMap<>();
        private Map<String, Circle> redCircles = new HashMap<>();
        private Map<String, Label> volumeLabels = new HashMap<>();

        BlottingPaper(double x, double y) {
            this.x = x;
            this.y = y;

            // Initialize volumes for each circle
            circleVolumes.put("A", 0.0);
            circleVolumes.put("B", 0.0);
            circleVolumes.put("C", 0.0);
            circleVolumes.put("D", 0.0);

            createView();
        }

        private void createView() {
            paperGroup = new Group();

            // Create white paper background
            Rectangle paper = new Rectangle(400, 200);
            paper.setFill(Color.WHITE);
            paper.setStroke(Color.web("#333333"));
            paper.setStrokeWidth(2);
            paper.setArcWidth(10);
            paper.setArcHeight(10);

            // Add shadow effect
            DropShadow shadow = new DropShadow();
            shadow.setColor(Color.rgb(0, 0, 0, 0.3));
            shadow.setRadius(8);
            paper.setEffect(shadow);

            paperGroup.getChildren().add(paper);

            // Label at top
            Label titleLabel = new Label("Blotting Paper");
            titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
            titleLabel.setTextFill(Color.web("#333333"));
            titleLabel.setLayoutX(140);
            titleLabel.setLayoutY(10);
            paperGroup.getChildren().add(titleLabel);

            // Create 4 circles in a row
            String[] labels = {"A", "B", "C", "D"};

            for (int i = 0; i < 4; i++) {
                double circleX = 50 + (i * 90);
                double circleY = 90;
                createCircleSpot(labels[i], circleX, circleY);
            }

            paperGroup.setLayoutX(x);
            paperGroup.setLayoutY(y);
        }

        private void createCircleSpot(String label, double circleX, double circleY) {
            // Outer black circle (fixed size - maximum possible)
            Circle outerCircle = new Circle(circleX, circleY, 35);
            outerCircle.setFill(Color.TRANSPARENT);
            outerCircle.setStroke(Color.BLACK);
            outerCircle.setStrokeWidth(2);

            // Inner red circle (variable size based on volume)
            Circle redCircle = new Circle(circleX, circleY, 0);
            redCircle.setFill(Color.RED);
            redCircles.put(label, redCircle);

            // Label below circle
            Label circleLabel = new Label(label);
            circleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
            circleLabel.setTextFill(Color.web("#333333"));
            circleLabel.setLayoutX(circleX - 7);
            circleLabel.setLayoutY(circleY + 45);

            // Volume display below label
            Label volumeLabel = new Label("0.0µl");
            volumeLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 11));
            volumeLabel.setTextFill(Color.web("#666666"));
            volumeLabel.setLayoutX(circleX - 15);
            volumeLabel.setLayoutY(circleY + 65);
            volumeLabels.put(label, volumeLabel);

            paperGroup.getChildren().addAll(outerCircle, redCircle, circleLabel, volumeLabel);
        }

        public Group getView() {
            return paperGroup;
        }

        public boolean isInCircleArea(double pipetteX, double pipetteY, String circleName) {
            // Check if pipette is in the vertical column above the circle
            String[] labels = {"A", "B", "C", "D"};
            int index = -1;
            for (int i = 0; i < labels.length; i++) {
                if (labels[i].equals(circleName)) {
                    index = i;
                    break;
                }
            }

            if (index == -1) return false;

            double circleX = x + 50 + (index * 90);
            double circleY = y + 90;

            // Check if pipette is within horizontal range and in vertical area
            return (Math.abs(pipetteX - circleX) < 40) && (pipetteY < y + 200) && (pipetteY > y - 100);
        }

        public String getCircleAtPosition(double pipetteX, double pipetteY) {
            // Check each circle to see if pipette is above it
            String[] labels = {"A", "B", "C", "D"};
            for (String label : labels) {
                if (isInCircleArea(pipetteX, pipetteY, label)) {
                    return label;
                }
            }
            return null;
        }

        public void dispenseVolume(String circleName, double volume) {
            if (!circleVolumes.containsKey(circleName)) return;

            // Add volume to existing
            double newVolume = circleVolumes.get(circleName) + volume;
            circleVolumes.put(circleName, newVolume);

            // Update red circle size (max radius is 35)
            // Scale: 20µl = full circle (radius 35)
            double radius = Math.min((newVolume / 20.0) * 35, 35);
            redCircles.get(circleName).setRadius(radius);

            // Update volume label
            String volumeText = (newVolume == (int) newVolume) ?
                    (int) newVolume + "µl" :
                    String.format("%.1f", newVolume) + "µl";
            volumeLabels.get(circleName).setText(volumeText);
        }

        /**
         * Get the actual radius of a specific circle on blotting paper
         * @param circleName Circle name (A, B, C, or D)
         * @return Radius in pixels
         */
        public double getCircleRadius(String circleName) {
            Circle circle = redCircles.get(circleName);
            return circle != null ? circle.getRadius() : 0.0;
        }

        public double getX() {
            return x;
        }

        public double getY() {
            return y;
        }
    }

    /**
     * Get actual blotting paper circle radius for result comparison
     * @param circleName Circle name (A, B, C, or D)
     * @return Radius in pixels
     */
    public double getBlottingPaperRadius(String circleName) {
        return blottingPaper.getCircleRadius(circleName);
    }

}