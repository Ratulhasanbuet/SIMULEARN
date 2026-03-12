package com.example.simulearn.SimuLearn.Biology.PPCC;

import com.example.simulearn.SimuLearn.Delta;
import com.example.simulearn.SimuLearn.SVGLoader;
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
import javafx.scene.effect.InnerShadow;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PPCClabworkspace {

    private Pane labWorkspace;
    private PPCCController controller;

    private static final double VORTEX_X = 450;
    private static final double VORTEX_Y = 760;

    private static final double MICROCENTRIFUGE_X = 300;
    private static final double MICROCENTRIFUGE_Y = 725;

    private static final double PIPETTE_STAND_X = 100;
    private static final double PIPETTE_STAND_Y = 125;

    private static final double TIP_BOX_P2_X = 250;
    private static final double TIP_BOX_P2_Y = 180;

    private static final double TIP_BOX_P20_X = 370;
    private static final double TIP_BOX_P20_Y = 180;

    private static final double TIP_BOX_P200_X = 490;
    private static final double TIP_BOX_P200_Y = 180;

    private static final double TIP_BOX_P1000_X = 610;
    private static final double TIP_BOX_P1000_Y = 180;

    private static final double TRASH_X = 780;
    private static final double TRASH_Y = 227;

    private static final double TUBE_RACK_X = 700;
    private static final double TUBE_RACK_Y = 766;

    private static final double WASTEJAR_X = 200;
    private static final double WASTEJAR_Y = 760;

    private static final double TIMER_X = 930;
    private static final double TIMER_Y = 175;

    private static final double CCOLUMN_X = 70;
    private static final double CCOLUMN_Y = 390;

    private static final double TABLE_Y = 850;

    private boolean microcentrifugeOpen = false;
    private Map<String, Boolean> tipBoxStates = new HashMap<>();

    // Microcentrifuge state
    private VBox microcentrifugeHoverBox;
    private List<String> tubesInMicrocentrifuge = new ArrayList<>();
    private int microcentrifugeRPM = 1300;
    private int microTimerHours = 0;
    private int microTimerMinutes = 0;
    private int microTimerSeconds = 0;
    private Timeline microTimerTimeline;
    private boolean microTimerRunning = false;
    private Stage microcentrifugeDialog;

    // Vortex state
    private String tubeInVortex = null;
    private VBox vortexButtonBox;
    private Timeline vortexTimeline;

    // Tube states
    private Map<String, TubeState> tubeStates = new HashMap<>();


    // Track currently active pipette
    private DraggablePipette currentlyActivePipette = null;

    // Trash state
    private boolean trashIsOpen = false;
    private static final String TRASH_CLOSED_SVG = "/Images/PPCCImages/trash-biohazard.svg";
    private static final String TRASH_OPEN_SVG   = "/Images/PPCCImages/trash-biohazard-open.svg";

    // Timer state
    private Stage timerDialog;
    private int labTimerHours = 0;
    private int labTimerMinutes = 0;
    private int labTimerSeconds = 0;
    private Timeline labTimerTimeline;
    private boolean labTimerRunning = false;

    // UI Components
    private ImageView vortexView;
    private ImageView pipetteStandView;
    private ImageView trashView;
    private ImageView microcentrifugeView;
    private ImageView CColumnView;
    private ImageView timerView;

    private Map<String, ImageView> tipBoxViews = new HashMap<>();

    // Pipettes  we have 4 separate draggable pipettes
    private Map<String, DraggablePipette> pipettes = new HashMap<>();

    // Tubes
    private Map<String, DraggableTube> tubes = new HashMap<>();

    public PPCClabworkspace(PPCCController controller) {
        this.controller = controller;
        initializeTubeStates();
        initializeTipBoxStates();
        createLabWorkspace();
    }

    private void initializeTubeStates() {
        tubeStates.put("Water Collection Jar", new TubeState("Water Collection Jar", 0, false, Color.TRANSPARENT));
        tubeStates.put("\uD835\uDE0C. \uD835\uDE24\uD835\uDE30\uD835\uDE2D\uD835\uDE2A (EC1)", new TubeState("\uD835\uDE0C. \uD835\uDE24\uD835\uDE30\uD835\uDE2D\uD835\uDE2A (EC1)", 10, false, Color.web("#4da6ff")));
        tubeStates.put("\uD835\uDE0C. \uD835\uDE24\uD835\uDE30\uD835\uDE2D\uD835\uDE2A (EC2)", new TubeState("\uD835\uDE0C. \uD835\uDE24\uD835\uDE30\uD835\uDE2D\uD835\uDE2A (EC2)", 10, false, Color.web("#4da6ff")));
        tubeStates.put("Elution Buffer (EB)", new TubeState("Elution Buffer (EB)", 0, false, Color.TRANSPARENT));
        tubeStates.put("Lysis Buffer (LyB)", new TubeState("Lysis Buffer (LyB)", 20, false, Color.web("#ffeb99")));
        tubeStates.put("Binding Buffer (BB)", new TubeState("Binding Buffer (BB)", 100, false, Color.TRANSPARENT));
        tubeStates.put("Wash Buffer (WB)", new TubeState("Wash Buffer (WB)", 0, false, Color.TRANSPARENT));
        tubeStates.put("Column Equilibration Buffer (CEB)", new TubeState("Column Equilibration Buffer (CEB)", 0, false, Color.TRANSPARENT));
        tubeStates.put("Balancing Tube", new TubeState("Balancing Tube", 0, false, Color.TRANSPARENT));
        tubeStates.put("RFP Tube", new TubeState("RFP Tube", 0, false, Color.TRANSPARENT));
        tubeStates.put("Super Tube", new TubeState("Super Tube", 0, false, Color.TRANSPARENT));
    }

    public Pane getppccLabWorkspace() {
        return labWorkspace;
    }

    private void initializeTipBoxStates() {
        tipBoxStates.put("P2", false);
        tipBoxStates.put("P20", false);
        tipBoxStates.put("P200", false);
        tipBoxStates.put("P1000", false);
    }

    private void createLabWorkspace() {
        labWorkspace = new Pane();
        labWorkspace.setPrefSize(1515, 1056);
        labWorkspace.setMinSize(1515, 1056);
        labWorkspace.setMaxSize(1515, 1056);
        labWorkspace.setStyle("-fx-background-color: linear-gradient(to bottom, #003E6B, #005A89);");

        // ── FLOOR ─────────────────────────────────────────────────────────────
        Rectangle floor = new Rectangle(0, TABLE_Y + 206, 1515, 1056 - (TABLE_Y + 206));
        floor.setFill(new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0.0, Color.web("#1a2a3a")),
                new Stop(1.0, Color.web("#0d1a26"))));
        labWorkspace.getChildren().add(floor);

        // ── CABINET BODY ──────────────────────────────────────────────────────
        Rectangle cabinetBody = new Rectangle(60, TABLE_Y + 18, 300, 188);
        cabinetBody.setFill(new LinearGradient(0, 0, 1, 0, true, CycleMethod.NO_CYCLE,
                new Stop(0.0, Color.web("#6a7a88")),
                new Stop(0.4, Color.web("#7d8e9c")),
                new Stop(1.0, Color.web("#5a6a78"))));
        cabinetBody.setArcWidth(4); cabinetBody.setArcHeight(4);
        cabinetBody.setEffect(new DropShadow(8, 3, 3, Color.rgb(0,0,0,0.5)));

        Rectangle cabinetInner = new Rectangle(68, TABLE_Y + 24, 284, 176);
        cabinetInner.setFill(new LinearGradient(0, 0, 1, 0, true, CycleMethod.NO_CYCLE,
                new Stop(0.0, Color.web("#5a6a78")),
                new Stop(0.5, Color.web("#6e7e8c")),
                new Stop(1.0, Color.web("#4e5e6c"))));
        cabinetInner.setArcWidth(3); cabinetInner.setArcHeight(3);
        labWorkspace.getChildren().addAll(cabinetBody, cabinetInner);

        // ── DRAWERS ────────────────────────────────────────────────────────────
        double dX = 72, dW = 276;
        double d1Y = TABLE_Y + 28;

        Rectangle drawer1 = new Rectangle(dX, d1Y, dW, 78);
        drawer1.setFill(new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0.0, Color.web("#8a9aaa")),
                new Stop(0.5, Color.web("#7a8a98")),
                new Stop(1.0, Color.web("#6a7a88"))));
        drawer1.setArcWidth(4); drawer1.setArcHeight(4);
        drawer1.setStroke(Color.web("#4a5a68")); drawer1.setStrokeWidth(1);
        drawer1.setEffect(new InnerShadow(4, 0, 2, Color.rgb(0,0,0,0.3)));
        Rectangle d1Hi = new Rectangle(dX+2, d1Y+1, dW-4, 3);
        d1Hi.setFill(Color.web("#aabbcc", 0.4)); d1Hi.setArcWidth(3); d1Hi.setArcHeight(3);
        Rectangle h1 = new Rectangle(dX + dW/2 - 35, d1Y+32, 70, 14);
        h1.setFill(new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0.0, Color.web("#c8d4de")), new Stop(0.4, Color.web("#a0b0bc")), new Stop(1.0, Color.web("#788898"))));
        h1.setArcWidth(7); h1.setArcHeight(7); h1.setStroke(Color.web("#5a6a78")); h1.setStrokeWidth(1);
        h1.setEffect(new DropShadow(3, 1, 2, Color.rgb(0,0,0,0.4)));
        Rectangle s1L = new Rectangle(dX + dW/2 - 28, d1Y+36, 6, 6);
        s1L.setFill(Color.web("#9aaabb")); s1L.setArcWidth(3); s1L.setArcHeight(3);
        Rectangle s1R = new Rectangle(dX + dW/2 + 22, d1Y+36, 6, 6);
        s1R.setFill(Color.web("#9aaabb")); s1R.setArcWidth(3); s1R.setArcHeight(3);
        Rectangle gap = new Rectangle(dX, d1Y+78, dW, 4);
        gap.setFill(Color.web("#3a4a58"));

        double d2Y = d1Y + 82;
        Rectangle drawer2 = new Rectangle(dX, d2Y, dW, 78);
        drawer2.setFill(new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0.0, Color.web("#8a9aaa")),
                new Stop(0.5, Color.web("#7a8a98")),
                new Stop(1.0, Color.web("#6a7a88"))));
        drawer2.setArcWidth(4); drawer2.setArcHeight(4);
        drawer2.setStroke(Color.web("#4a5a68")); drawer2.setStrokeWidth(1);
        drawer2.setEffect(new InnerShadow(4, 0, 2, Color.rgb(0,0,0,0.3)));
        Rectangle d2Hi = new Rectangle(dX+2, d2Y+1, dW-4, 3);
        d2Hi.setFill(Color.web("#aabbcc", 0.4)); d2Hi.setArcWidth(3); d2Hi.setArcHeight(3);
        Rectangle h2 = new Rectangle(dX + dW/2 - 35, d2Y+32, 70, 14);
        h2.setFill(new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0.0, Color.web("#c8d4de")), new Stop(0.4, Color.web("#a0b0bc")), new Stop(1.0, Color.web("#788898"))));
        h2.setArcWidth(7); h2.setArcHeight(7); h2.setStroke(Color.web("#5a6a78")); h2.setStrokeWidth(1);
        h2.setEffect(new DropShadow(3, 1, 2, Color.rgb(0,0,0,0.4)));
        Rectangle s2L = new Rectangle(dX + dW/2 - 28, d2Y+36, 6, 6);
        s2L.setFill(Color.web("#9aaabb")); s2L.setArcWidth(3); s2L.setArcHeight(3);
        Rectangle s2R = new Rectangle(dX + dW/2 + 22, d2Y+36, 6, 6);
        s2R.setFill(Color.web("#9aaabb")); s2R.setArcWidth(3); s2R.setArcHeight(3);

        labWorkspace.getChildren().addAll(drawer1, d1Hi, h1, s1L, s1R, gap,
                drawer2, d2Hi, h2, s2L, s2R);

        // ── TABLE TOP SURFACE ──────────────────────────────────────────────────
        Rectangle benchWall = new Rectangle(0, TABLE_Y+24, 1515, 182);
        benchWall.setFill(new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0.0, Color.web("#7a8a98")), new Stop(1.0, Color.web("#5a6878"))));
        Rectangle benchEdge = new Rectangle(0, TABLE_Y+18, 1515, 6);
        benchEdge.setFill(new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0.0, Color.web("#6a7a88")), new Stop(1.0, Color.web("#4a5a68"))));
        Rectangle benchSurface = new Rectangle(0, TABLE_Y, 1515, 18);
        benchSurface.setFill(new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0.0, Color.web("#d8e2ea")), new Stop(0.3, Color.web("#c2cdd8")),
                new Stop(0.7, Color.web("#b0bcc8")), new Stop(1.0, Color.web("#8a9aaa"))));
        benchSurface.setEffect(new DropShadow(12, 0, 4, Color.rgb(0,0,0,0.55)));
        Rectangle benchHi = new Rectangle(0, TABLE_Y, 1515, 2);
        benchHi.setFill(Color.web("#eef4f8", 0.7));
        labWorkspace.getChildren().addAll(benchWall, benchEdge, benchSurface, benchHi);

        // ── TABLE LEGS ────────────────────────────────────────────────────────
        Rectangle legL = new Rectangle(60, TABLE_Y+18, 18, 188);
        legL.setFill(new LinearGradient(0, 0, 1, 0, true, CycleMethod.NO_CYCLE,
                new Stop(0.0, Color.web("#7a8a98")), new Stop(0.3, Color.web("#9aaaba")), new Stop(1.0, Color.web("#5a6a78"))));
        Rectangle legR = new Rectangle(1437, TABLE_Y+18, 18, 188);
        legR.setFill(new LinearGradient(0, 0, 1, 0, true, CycleMethod.NO_CYCLE,
                new Stop(0.0, Color.web("#7a8a98")), new Stop(0.3, Color.web("#9aaaba")), new Stop(1.0, Color.web("#5a6a78"))));
        Rectangle padL = new Rectangle(50, TABLE_Y+196, 38, 10);
        padL.setFill(Color.web("#4a5a68")); padL.setArcWidth(4); padL.setArcHeight(4);
        Rectangle padR = new Rectangle(1427, TABLE_Y+196, 38, 10);
        padR.setFill(Color.web("#4a5a68")); padR.setArcWidth(4); padR.setArcHeight(4);
        labWorkspace.getChildren().addAll(legL, legR, padL, padR);

        // ── WALL SHELF ────────────────────────────────────────────────────────
        for (int bx : new int[]{80, 380, 680, 980, 1200}) {
            Rectangle br = new Rectangle(bx, 323, 8, 30);
            br.setFill(Color.web("#8a9aaa"));
            labWorkspace.getChildren().add(br);
        }
        Rectangle shelfBody = new Rectangle(50, 323, 1200, 16);
        shelfBody.setFill(new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0.0, Color.web("#c2cdd8")), new Stop(0.4, Color.web("#aabbcc")), new Stop(1.0, Color.web("#7a8a98"))));
        shelfBody.setEffect(new DropShadow(6, 0, 3, Color.rgb(0,0,0,0.4)));
        Rectangle shelfHi = new Rectangle(50, 323, 1200, 2);
        shelfHi.setFill(Color.web("#e8f0f8", 0.5));
        Rectangle shelfEdge = new Rectangle(50, 339, 1200, 4);
        shelfEdge.setFill(Color.web("#5a6a78"));
        labWorkspace.getChildren().addAll(shelfBody, shelfHi, shelfEdge);

        // Create all equipment
        createTubes();
        createEquipment();
        createTipBoxes();
        createPipettes();
    }

    private void createEquipment() {
        // Vortex
        vortexView = createEquipmentImage("/Images/PPCCImages/equipment/vortex.svg", 120, 100, VORTEX_X, VORTEX_Y);
        addEquipLabel("Vortex", VORTEX_X + 60, VORTEX_Y - 24);

        // Microcentrifuge
        microcentrifugeView = createEquipmentImage("/Images/PPCCImages/equipment/Microcentrifuge.svg", 150, 130, MICROCENTRIFUGE_X, MICROCENTRIFUGE_Y);
        addEquipLabel("Microcentrifuge", MICROCENTRIFUGE_X + 75, MICROCENTRIFUGE_Y - 24);

        // Create hover buttons for microcentrifuge
        createMicrocentrifugeHoverButtons();

        // Add mouse enter/exit handlers
        microcentrifugeView.setOnMouseEntered(e -> microcentrifugeHoverBox.setOpacity(1.0));
        microcentrifugeView.setOnMouseExited(e -> {
            if (!microcentrifugeHoverBox.isHover()) microcentrifugeHoverBox.setOpacity(0.0);
        });

        // Pipette Stand
        pipetteStandView = createEquipmentImage("/Images/PPCCImages/equipment/PipetteRack.svg", 180, 220, PIPETTE_STAND_X, PIPETTE_STAND_Y);

        // Trash
        trashView = createEquipmentImage("/Images/PPCCImages/trash-biohazard.svg", 120, 100, TRASH_X, TRASH_Y);
        trashView.setOnMouseClicked(e -> handleTrashClick());
        addEquipLabel("Biohazardous Waste", TRASH_X + 60, TRASH_Y - 24);

        // Chromatography Column
        CColumnView = createEquipmentImage("/Images/PPCCImages/column-blue-full-closed.svg", 120, 400, CCOLUMN_X, CCOLUMN_Y);
        addEquipLabel("Chromatography Column", CCOLUMN_X + 60, CCOLUMN_Y - 24);

        // Timer
        timerView = createEquipmentImage("/Images/PPCCImages/timer.svg", 150, 150, TIMER_X, TIMER_Y);
        timerView.setOnMouseClicked(e -> handleTimerClick());
        addEquipLabel("Timer", TIMER_X + 75, TIMER_Y - 24);
    }

    /** Centered horizontal label above equipment. centerX = image center, topY = just above image top */
    private void addEquipLabel(String text, double centerX, double topY) {
        Label lbl = new Label(text);
        lbl.setFont(Font.font("DM Sans Medium", FontWeight.BOLD, 11));
        lbl.setTextFill(Color.WHITE);
        lbl.setStyle(
                "-fx-background-color: rgba(0,20,45,0.65);" +
                        "-fx-background-radius: 4;" +
                        "-fx-padding: 3 8 3 8;" +
                        "-fx-border-color: rgba(255,255,255,0.2);" +
                        "-fx-border-radius: 4;" +
                        "-fx-border-width: 1;"
        );
        // Approximate centering: each char ~6.5px wide at 11pt bold
        lbl.setLayoutX(centerX - text.length() * 3.3);
        lbl.setLayoutY(topY);
        labWorkspace.getChildren().add(lbl);
    }

    private void handleTrashClick() {
        trashIsOpen = !trashIsOpen;

        // Swap the trash image
        String newPath = trashIsOpen ? TRASH_OPEN_SVG : TRASH_CLOSED_SVG;
        ImageView newView = SVGLoader.loadSVG(newPath, 120, 100);
        newView.setLayoutX(TRASH_X);
        newView.setLayoutY(TRASH_Y);
        newView.setCursor(Cursor.HAND);

        DropShadow shadow = new DropShadow();
        shadow.setColor(Color.rgb(0, 0, 0, 0.3));
        shadow.setRadius(5);
        newView.setEffect(shadow);

        newView.setOnMouseClicked(e -> handleTrashClick());

        labWorkspace.getChildren().remove(trashView);
        trashView = newView;
        labWorkspace.getChildren().add(trashView);

        // If trash is now open and a pipette with tip is nearby, eject the tip
        if (trashIsOpen && currentlyActivePipette != null && currentlyActivePipette.hasTip) {
            if (isNear(currentlyActivePipette.x, currentlyActivePipette.y, TRASH_X, TRASH_Y, 120)) {
                currentlyActivePipette.ejectTip();
                showMessage("Tip ejected into trash!");
            }
        }
    }

    private void handleTimerClick() {
        openTimerDialog();
    }

    private void openTimerDialog() {
        if (timerDialog != null && timerDialog.isShowing()) {
            return;
        }

        timerDialog = new Stage();
        timerDialog.initModality(Modality.APPLICATION_MODAL);
        timerDialog.setTitle("Lab Timer");
        timerDialog.setResizable(false);

        VBox dialogContent = new VBox(20);
        dialogContent.setPadding(new Insets(30));
        dialogContent.setAlignment(Pos.CENTER);
        dialogContent.setStyle("-fx-background-color: linear-gradient(to bottom, #0d4f6e, #1a5490);");

        HBox timerDisplay = new HBox(10);
        timerDisplay.setAlignment(Pos.CENTER);
        timerDisplay.setStyle("-fx-background-color: #003d5c; -fx-padding: 20; -fx-background-radius: 10;");

        Label hourLabel = new Label(String.format("%02d", labTimerHours));
        hourLabel.setId("hourLabel");
        hourLabel.setFont(Font.font("Digital-7", FontWeight.BOLD, 48));
        hourLabel.setTextFill(Color.web("#00ffff"));

        Label colon1 = new Label(":");
        colon1.setFont(Font.font("Digital-7", FontWeight.BOLD, 48));
        colon1.setTextFill(Color.web("#00ffff"));

        Label minuteLabel = new Label(String.format("%02d", labTimerMinutes));
        minuteLabel.setId("minuteLabel");
        minuteLabel.setFont(Font.font("Digital-7", FontWeight.BOLD, 48));
        minuteLabel.setTextFill(Color.web("#00ffff"));

        Label colon2 = new Label(":");
        colon2.setFont(Font.font("Digital-7", FontWeight.BOLD, 48));
        colon2.setTextFill(Color.web("#00ffff"));

        Label secondLabel = new Label(String.format("%02d", labTimerSeconds));
        secondLabel.setId("secondLabel");
        secondLabel.setFont(Font.font("Digital-7", FontWeight.BOLD, 48));
        secondLabel.setTextFill(Color.web("#00ffff"));

        timerDisplay.getChildren().addAll(hourLabel, colon1, minuteLabel, colon2, secondLabel);

        VBox controlsBox = new VBox(15);
        controlsBox.setAlignment(Pos.CENTER);

        HBox timeControls = new HBox(20);
        timeControls.setAlignment(Pos.CENTER);

        VBox hoursBox = createTimeControlBox("Hours", labTimerHours, hourLabel);
        VBox minutesBox = createTimeControlBox("Minutes", labTimerMinutes, minuteLabel);
        VBox secondsBox = createTimeControlBox("Seconds", labTimerSeconds, secondLabel);

        timeControls.getChildren().addAll(hoursBox, minutesBox, secondsBox);

        HBox buttonBox = new HBox(15);
        buttonBox.setAlignment(Pos.CENTER);

        Button startButton = new Button("Start");
        startButton.setFont(Font.font("DM Sans Medium", FontWeight.BOLD, 14));
        startButton.setStyle(
                "-fx-background-color: #4caf50;" +
                        "-fx-text-fill: white;" +
                        "-fx-padding: 10 30;" +
                        "-fx-background-radius: 5;" +
                        "-fx-cursor: hand;"
        );
        startButton.setOnAction(e -> {
            if (!labTimerRunning) {
                startLabTimer(hourLabel, minuteLabel, secondLabel);
            }
        });

        Button stopButton = new Button("Stop");
        stopButton.setFont(Font.font("DM Sans Medium", FontWeight.BOLD, 14));
        stopButton.setStyle(
                "-fx-background-color: #f44336;" +
                        "-fx-text-fill: white;" +
                        "-fx-padding: 10 30;" +
                        "-fx-background-radius: 5;" +
                        "-fx-cursor: hand;"
        );
        stopButton.setOnAction(e -> stopLabTimer());

        buttonBox.getChildren().addAll(startButton, stopButton);

        controlsBox.getChildren().addAll(timeControls, buttonBox);

        Button closeButton = new Button("Close");
        closeButton.setFont(Font.font("DM Sans Medium", 12));
        closeButton.setStyle(
                "-fx-background-color: #757575;" +
                        "-fx-text-fill: white;" +
                        "-fx-padding: 8 20;" +
                        "-fx-background-radius: 5;" +
                        "-fx-cursor: hand;"
        );
        closeButton.setOnAction(e -> timerDialog.close());

        dialogContent.getChildren().addAll(timerDisplay, controlsBox, closeButton);

        Scene dialogScene = new Scene(dialogContent, 500, 400);
        timerDialog.setScene(dialogScene);
        timerDialog.show();
    }

    private VBox createTimeControlBox(String label, int initialValue, Label displayLabel) {
        VBox box = new VBox(5);
        box.setAlignment(Pos.CENTER);

        Label titleLabel = new Label(label);
        titleLabel.setFont(Font.font("DM Sans Medium", FontWeight.BOLD, 12));
        titleLabel.setTextFill(Color.WHITE);

        Button upButton = new Button("▲");
        upButton.setFont(Font.font("DM Sans Medium", FontWeight.BOLD, 16));
        upButton.setStyle(
                "-fx-background-color: #1976d2;" +
                        "-fx-text-fill: white;" +
                        "-fx-padding: 5 15;" +
                        "-fx-background-radius: 3;" +
                        "-fx-cursor: hand;"
        );

        Button downButton = new Button("▼");
        downButton.setFont(Font.font("DM Sans Medium", FontWeight.BOLD, 16));
        downButton.setStyle(
                "-fx-background-color: #1976d2;" +
                        "-fx-text-fill: white;" +
                        "-fx-padding: 5 15;" +
                        "-fx-background-radius: 3;" +
                        "-fx-cursor: hand;"
        );

        final String labelType = label;

        upButton.setOnAction(e -> {
            if (labelType.equals("Hours")) {
                labTimerHours = (labTimerHours + 1) % 24;
                displayLabel.setText(String.format("%02d", labTimerHours));
            } else if (labelType.equals("Minutes")) {
                labTimerMinutes = (labTimerMinutes + 1) % 60;
                displayLabel.setText(String.format("%02d", labTimerMinutes));
            } else {
                labTimerSeconds = (labTimerSeconds + 1) % 60;
                displayLabel.setText(String.format("%02d", labTimerSeconds));
            }
        });

        downButton.setOnAction(e -> {
            if (labelType.equals("Hours")) {
                labTimerHours = (labTimerHours - 1 + 24) % 24;
                displayLabel.setText(String.format("%02d", labTimerHours));
            } else if (labelType.equals("Minutes")) {
                labTimerMinutes = (labTimerMinutes - 1 + 60) % 60;
                displayLabel.setText(String.format("%02d", labTimerMinutes));
            } else {
                labTimerSeconds = (labTimerSeconds - 1 + 60) % 60;
                displayLabel.setText(String.format("%02d", labTimerSeconds));
            }
        });

        box.getChildren().addAll(titleLabel, upButton, downButton);
        return box;
    }

    private void startLabTimer(Label hourLabel, Label minuteLabel, Label secondLabel) {
        if (labTimerRunning) return;

        labTimerRunning = true;
        labTimerTimeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            if (labTimerSeconds > 0) {
                labTimerSeconds--;
            } else if (labTimerMinutes > 0) {
                labTimerMinutes--;
                labTimerSeconds = 59;
            } else if (labTimerHours > 0) {
                labTimerHours--;
                labTimerMinutes = 59;
                labTimerSeconds = 59;
            } else {
                stopLabTimer();
                showMessage("Timer finished!");
                return;
            }

            hourLabel.setText(String.format("%02d", labTimerHours));
            minuteLabel.setText(String.format("%02d", labTimerMinutes));
            secondLabel.setText(String.format("%02d", labTimerSeconds));
        }));

        labTimerTimeline.setCycleCount(Timeline.INDEFINITE);
        labTimerTimeline.play();
    }

    private void stopLabTimer() {
        if (labTimerTimeline != null) {
            labTimerTimeline.stop();
        }
        labTimerRunning = false;
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

    private void createTubes() {

        Map<String, TubeImagePaths> tubeImageMap = new HashMap<>();

        // Define image paths for each tube type (closed and open versions)
        tubeImageMap.put("Water Collection Jar", new TubeImagePaths("/Images/PPCCImages/waste-collection-jar.svg",
                "/Images/PPCCImages/waste-collection-jar-open.svg"));

        tubeImageMap.put("\uD835\uDE0C. \uD835\uDE24\uD835\uDE30\uD835\uDE2D\uD835\uDE2A (EC1)", new TubeImagePaths("/Images/PPCCImages/solution-tube-ec-closed-red.svg",
                "/Images/PPCCImages/solution-tube-ec-open-red.svg"));

        tubeImageMap.put("\uD835\uDE0C. \uD835\uDE24\uD835\uDE30\uD835\uDE2D\uD835\uDE2A (EC2)", new TubeImagePaths("/Images/PPCCImages/solution-tube-ec-2-closed-red.svg",
                "/Images/PPCCImages/solution-tube-ec-2-open-red.svg"));

        tubeImageMap.put("Elution Buffer (EB)", new TubeImagePaths("/Images/PPCCImages/solution-tube-eb-closed.svg",
                "/Images/PPCCImages/solution-tube-eb-open.svg"));

        tubeImageMap.put("Lysis Buffer (LyB)", new TubeImagePaths("/Images/PPCCImages/solution-tube-ly-b-closed.svg",
                "/Images/PPCCImages/solution-tube-ly-b-open.svg"));

        tubeImageMap.put("Binding Buffer (BB)", new TubeImagePaths("/Images/PPCCImages/solution-tube-bb-closed.svg",
                "/Images/PPCCImages/solution-tube-bb-open.svg"));

        tubeImageMap.put("Wash Buffer (WB)", new TubeImagePaths("/Images/PPCCImages/solution-tube-wb-closed.svg",
                "/Images/PPCCImages/solution-tube-wb-open.svg"));

        tubeImageMap.put("Column Equilibration Buffer (CEB)", new TubeImagePaths("/Images/PPCCImages/solution-tube-ceb-closed.svg",
                "/Images/PPCCImages/solution-tube-ceb-open.svg"));

        tubeImageMap.put("Balancing Tube", new TubeImagePaths("/Images/PPCCImages/solution-tube-water-closed.svg",
                "/Images/PPCCImages/solution-tube-water-open.svg"));

        tubeImageMap.put("Super Tube", new TubeImagePaths("/Images/PPCCImages/solution-tube-super-empty.svg",
                "/Images/PPCCImages/solution-tube-super.svg"));

        tubeImageMap.put("RFP Tube", new TubeImagePaths("/Images/PPCCImages/solution-tube-rfp-empty.svg",
                "/Images/PPCCImages/solution-tube-rfp.svg"));


        String[] tubeNames = {"Water Collection Jar", "\uD835\uDE0C. \uD835\uDE24\uD835\uDE30\uD835\uDE2D\uD835\uDE2A (EC1)", "\uD835\uDE0C. \uD835\uDE24\uD835\uDE30\uD835\uDE2D\uD835\uDE2A (EC2)", "Lysis Buffer (LyB)", "RFP Tube", "Super Tube", "Balancing Tube", "Elution Buffer (EB)", "Binding Buffer (BB)", "Wash Buffer (WB)", "Column Equilibration Buffer (CEB)",};
        double startX = TUBE_RACK_X + 3;
        double tubeY = TUBE_RACK_Y -4;

        DraggableTube WaterJar = new DraggableTube("Water Collection Jar", WASTEJAR_X, WASTEJAR_Y, tubeImageMap.get("Water Collection Jar"));
        tubes.put("Water Collection Jar", WaterJar);
        labWorkspace.getChildren().add(WaterJar.getView());

        double tubeSpacing = 34;

        for (int i = 1; i < tubeNames.length; i++) {
            String tubeName = tubeNames[i];
            double xPos = startX + ((i-1) * tubeSpacing);

            DraggableTube tube = new DraggableTube(tubeName, xPos, tubeY, tubeImageMap.get(tubeName));
            tubes.put(tubeName, tube);
            labWorkspace.getChildren().add(tube.getView());
        }
        ImageView tubeRack = createEquipmentImage("/Images/PPCCImages/tube-stand-8.svg", 400, 130, TUBE_RACK_X, TUBE_RACK_Y);

    }

    private void createTipBoxes() {
        // ORIGINAL TIP BOX CREATION with exact paths - P2, P20, P200, P1000
        // P2 Tips
        createTipBoxImage("/Images/PPCCImages/tipboxes/P2-sm-close.svg", "P2", "/Images/PPCCImages/tipboxes/P2-sm-open.svg", TIP_BOX_P2_X, TIP_BOX_P2_Y);
        addTipBoxLabel("P2", TIP_BOX_P2_X, TIP_BOX_P2_Y - 20);

        // P20 Tips
        createTipBoxImage("/Images/PPCCImages/tipboxes/P20-sm-close.svg", "P20", "/Images/PPCCImages/tipboxes/P20-sm-open.svg", TIP_BOX_P20_X, TIP_BOX_P20_Y);
        addTipBoxLabel("P20", TIP_BOX_P20_X, TIP_BOX_P20_Y - 20);

        // P200 Tips
        createTipBoxImage("/Images/PPCCImages/tipboxes/P200-sm-close.svg", "P200", "/Images/PPCCImages/tipboxes/P200-sm-open.svg", TIP_BOX_P200_X, TIP_BOX_P200_Y);
        addTipBoxLabel("P200", TIP_BOX_P200_X, TIP_BOX_P200_Y - 20);

        // P1000 Tips
        createTipBoxImage("/Images/PPCCImages/tipboxes/P1000-sm-close.svg", "P1000", "/Images/PPCCImages/tipboxes/P1000-sm-open.svg", TIP_BOX_P1000_X, TIP_BOX_P1000_Y);
        addTipBoxLabel("P1000", TIP_BOX_P1000_X, TIP_BOX_P1000_Y - 20);
    }

    private void createTipBoxImage(String closedPath, String type, String openPath, double x, double y) {
        ImageView tipBox = createEquipmentImage(closedPath, 170, 141.67, x, y);
        tipBox.setOnMouseClicked(e -> toggleTipBox(type, openPath, closedPath));
        tipBoxViews.put(type, tipBox);
    }

    private void addTipBoxLabel(String text, double x, double y) {
        Label label = new Label(text + " Tips");
        label.setFont(Font.font("DM Sans Medium", FontWeight.BOLD, 10));
        label.setTextFill(Color.WHITE);
        label.setStyle(
                "-fx-background-color: rgba(0,20,45,0.65);" +
                        "-fx-background-radius: 4;" +
                        "-fx-padding: 3 7 3 7;" +
                        "-fx-border-color: rgba(255,255,255,0.2);" +
                        "-fx-border-radius: 4;" +
                        "-fx-border-width: 1;"
        );
        label.setRotate(-90);
        label.setLayoutX(x + 85);
        label.setLayoutY(y - 50);
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

    }

    private void createPipettes() {

        DraggablePipette p2 = new DraggablePipette("P2", PIPETTE_STAND_X + 25, PIPETTE_STAND_Y, "/Images/PPCCImages/pipettes/P2-body.svg", "/Images/PPCCImages/pipettes/P2-bodywithtip.svg");
        pipettes.put("P2", p2);
        labWorkspace.getChildren().add(p2.getView());


        DraggablePipette p20 = new DraggablePipette("P20", PIPETTE_STAND_X + 60, PIPETTE_STAND_Y, "/Images/PPCCImages/pipettes/P20-body.svg", "/Images/PPCCImages/pipettes/P20-bodywithtip.svg");
        pipettes.put("P20", p20);
        labWorkspace.getChildren().add(p20.getView());


        DraggablePipette p200 = new DraggablePipette("P200", PIPETTE_STAND_X + 95, PIPETTE_STAND_Y, "/Images/PPCCImages/pipettes/P200-body.svg", "/Images/PPCCImages/pipettes/P200-bodywithtip.svg");
        pipettes.put("P200", p200);
        labWorkspace.getChildren().add(p200.getView());


        DraggablePipette p1000 = new DraggablePipette("P1000", PIPETTE_STAND_X + 130, PIPETTE_STAND_Y, "/Images/PPCCImages/pipettes/P1000-body.svg", "/Images/PPCCImages/pipettes/P1000-bodywithtip.svg");
        pipettes.put("P1000", p1000);
        labWorkspace.getChildren().add(p1000.getView());
    }

    // ========== MICROCENTRIFUGE ==========
    private void createMicrocentrifugeHoverButtons() {
        microcentrifugeHoverBox = new VBox(5);
        microcentrifugeHoverBox.setLayoutX(MICROCENTRIFUGE_X + 60);
        microcentrifugeHoverBox.setLayoutY(MICROCENTRIFUGE_Y - 60);
        microcentrifugeHoverBox.setOpacity(0.0);
        microcentrifugeHoverBox.setVisible(true);
        microcentrifugeHoverBox.setStyle("-fx-background-color: white; -fx-padding: 5px; " + "-fx-background-radius: 5px; -fx-border-color: #ccc; " + "-fx-border-radius: 5px;");

        Button openBtn = new Button(microcentrifugeOpen ? "Close" : "Open");
        openBtn.setFont(Font.font("DM Sans Medium", FontWeight.BOLD, 11));
        openBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; " + "-fx-cursor: hand; -fx-padding: 5px 15px;");
        openBtn.setOnAction(e -> {
            // Check if trying to open while tubes inside and timer running
            if (!microcentrifugeOpen && !tubesInMicrocentrifuge.isEmpty() && microTimerRunning) {
                showMessage("Cannot open microcentrifuge while timer is running!");
                return;
            }

            microcentrifugeOpen = !microcentrifugeOpen;
            openBtn.setText(microcentrifugeOpen ? "Close" : "Open");
            updateMicrocentrifugeView();
            microcentrifugeHoverBox.setOpacity(0.0);
        });

        Button settingsBtn = new Button("Settings");
        settingsBtn.setFont(Font.font("DM Sans Medium", FontWeight.BOLD, 11));
        settingsBtn.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; " + "-fx-cursor: hand; -fx-padding: 5px 15px;");
        settingsBtn.setOnAction(e -> {
            openMicrocentrifugeSettingsDialog();
            microcentrifugeHoverBox.setOpacity(0.0);
        });

        microcentrifugeHoverBox.getChildren().addAll(openBtn, settingsBtn);

        microcentrifugeHoverBox.setOnMouseEntered(e -> {
            microcentrifugeHoverBox.setOpacity(1.0);
        });

        microcentrifugeHoverBox.setOnMouseExited(e -> {
            microcentrifugeHoverBox.setOpacity(0.0);
        });

        labWorkspace.getChildren().add(microcentrifugeHoverBox);
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

    private void updateMicrocentrifugeView() {
        String imagePath = microcentrifugeOpen ? "/Images/PPCCImages/equipment/microcentrifuge_body.svg" : "/Images/PPCCImages/equipment/Microcentrifuge.svg";

        ImageView newView = SVGLoader.loadSVG(imagePath, 150, 130);
        newView.setLayoutX(MICROCENTRIFUGE_X);
        newView.setLayoutY(MICROCENTRIFUGE_Y);
        newView.setCursor(Cursor.HAND);

        newView.setOnMouseEntered(e -> {
            microcentrifugeHoverBox.setOpacity(1.0);
        });

        newView.setOnMouseExited(e -> {
            if (!microcentrifugeHoverBox.isHover()) {
                microcentrifugeHoverBox.setOpacity(0.0);
            }
        });

        DropShadow shadow = new DropShadow();
        shadow.setColor(Color.rgb(0, 0, 0, 0.3));
        shadow.setRadius(5);
        newView.setEffect(shadow);

        labWorkspace.getChildren().remove(microcentrifugeView);
        microcentrifugeView = newView;
        labWorkspace.getChildren().add(microcentrifugeView);
        microcentrifugeView.toBack();
        microcentrifugeHoverBox.toFront();
    }

    private void openMicrocentrifugeSettingsDialog() {
        if (microcentrifugeDialog != null && microcentrifugeDialog.isShowing()) {
            return;
        }

        microcentrifugeDialog = new Stage();
        microcentrifugeDialog.initModality(Modality.APPLICATION_MODAL);
        microcentrifugeDialog.setTitle("Microcentrifuge");

        VBox mainBox = new VBox(20);
        mainBox.setPadding(new Insets(20));
        mainBox.setStyle("-fx-background-color: white;");

        // Title
        Label titleLabel = new Label("Programming the microcentrifuge");
        titleLabel.setFont(Font.font("DM Sans Medium", FontWeight.BOLD, 16));

        // Description
        Label descLabel = new Label("A microcentrifuge is used to spin samples (typically in 1.5 mL tubes) at\n" + "high speeds. To spin your samples at a set speed and time, set the RPM and\n" + "time using the arrows and buttons. To quickly spin down your samples,\n" + "pressing the Pulse button will accelerates the rotor to the preset RPM\n" + "then promptly decelerates it.");
        descLabel.setFont(Font.font("DM Sans Medium", 11));

        // Warning box
        VBox warningBox = new VBox(5);
        warningBox.setStyle("-fx-background-color: #FFF3CD; -fx-padding: 10px; -fx-border-color: #FFC107; -fx-border-width: 1px;");
        Label warningTitle = new Label("⚠ Double check");
        warningTitle.setFont(Font.font("DM Sans Medium", FontWeight.BOLD, 12));
        Label warningText = new Label("Make sure the microcentrifuge is balanced before begining your spin.");
        warningText.setFont(Font.font("DM Sans Medium", 11));

        Label balanceStatus = new Label("Tube balanced: " + (tubesInMicrocentrifuge.size() % 2 == 0 ? "Yes ✓" : "No ❌"));
        balanceStatus.setFont(Font.font("DM Sans Medium", FontWeight.BOLD, 12));
        balanceStatus.setStyle("-fx-background-color: " + (tubesInMicrocentrifuge.size() % 2 == 0 ? "#D4EDDA" : "#F8D7DA") + "; -fx-padding: 5px; -fx-background-radius: 5px;");

        warningBox.getChildren().addAll(warningTitle, warningText, balanceStatus);

        // RPM and Timer controls
        HBox controlsBox = new HBox(20);
        controlsBox.setAlignment(Pos.CENTER);

        // RPM Control
        VBox rpmBox = new VBox(10);
        rpmBox.setAlignment(Pos.CENTER);
        rpmBox.setStyle("-fx-background-color: #F5F5F5; -fx-padding: 15px; -fx-border-radius: 10px; -fx-background-radius: 10px;");

        Label rpmTitle = new Label("Set RPM");
        rpmTitle.setFont(Font.font("DM Sans Medium", FontWeight.BOLD, 12));

        VBox rpmDisplayBox = new VBox(5);
        rpmDisplayBox.setAlignment(Pos.CENTER);
        rpmDisplayBox.setStyle("-fx-background-color: #2C3E50; -fx-padding: 20px; -fx-border-radius: 10px; -fx-background-radius: 10px;");

        Label rpmLabel = new Label(String.format("%,d", microcentrifugeRPM));
        rpmLabel.setFont(Font.font("DM Sans Medium", FontWeight.BOLD, 36));
        rpmLabel.setTextFill(Color.WHITE);

        rpmDisplayBox.getChildren().add(rpmLabel);  // Add label to display box

        Button rpmUpBtn = new Button("▲");
        rpmUpBtn.setStyle("-fx-background-color: #3498DB; -fx-text-fill: white; -fx-font-size: 16px; -fx-cursor: hand;");
        rpmUpBtn.setOnAction(e -> {
            if (microcentrifugeRPM < 15000) {
                microcentrifugeRPM += 500;
                rpmLabel.setText(String.format("%,d", microcentrifugeRPM));
            }
        });

        Button rpmDownBtn = new Button("▼");
        rpmDownBtn.setStyle("-fx-background-color: #3498DB; -fx-text-fill: white; -fx-font-size: 16px; -fx-cursor: hand;");
        rpmDownBtn.setOnAction(e -> {
            if (microcentrifugeRPM > 0) {
                microcentrifugeRPM -= 500;
                rpmLabel.setText(String.format("%,d", microcentrifugeRPM));
            }
        });

        VBox rpmButtons = new VBox(5, rpmUpBtn, rpmDownBtn);
        rpmButtons.setAlignment(Pos.CENTER);

        HBox rpmControlRow = new HBox(10, rpmDisplayBox, rpmButtons);
        rpmControlRow.setAlignment(Pos.CENTER);

        Label rpmUnitLabel = new Label("RPM");
        rpmUnitLabel.setFont(Font.font("DM Sans Medium", FontWeight.BOLD, 12));

        rpmBox.getChildren().addAll(rpmTitle, rpmControlRow, rpmUnitLabel);

        // Timer Control (similar to water bath)
        VBox timerBox = new VBox(10);
        timerBox.setAlignment(Pos.CENTER);
        timerBox.setStyle("-fx-background-color: #F5F5F5; -fx-padding: 15px; -fx-border-radius: 10px; -fx-background-radius: 10px;");

        Label timerTitle = new Label("Timer");
        timerTitle.setFont(Font.font("DM Sans Medium", FontWeight.BOLD, 12));

        HBox timerDisplay = new HBox(5);
        timerDisplay.setAlignment(Pos.CENTER);
        timerDisplay.setStyle("-fx-background-color: #2C3E50; -fx-padding: 15px; -fx-border-radius: 10px; -fx-background-radius: 10px;");

        Label hourLabel = new Label(String.format("%02d", microTimerHours));
        hourLabel.setFont(Font.font("Courier New", FontWeight.BOLD, 32));
        hourLabel.setTextFill(Color.WHITE);

        Label colon1 = new Label(":");
        colon1.setFont(Font.font("Courier New", FontWeight.BOLD, 32));
        colon1.setTextFill(Color.WHITE);

        Label minuteLabel = new Label(String.format("%02d", microTimerMinutes));
        minuteLabel.setFont(Font.font("Courier New", FontWeight.BOLD, 32));
        minuteLabel.setTextFill(Color.WHITE);

        Label colon2 = new Label(":");
        colon2.setFont(Font.font("Courier New", FontWeight.BOLD, 32));
        colon2.setTextFill(Color.WHITE);

        Label secondLabel = new Label(String.format("%02d", microTimerSeconds));
        secondLabel.setFont(Font.font("Courier New", FontWeight.BOLD, 32));
        secondLabel.setTextFill(Color.WHITE);

        timerDisplay.getChildren().addAll(hourLabel, colon1, minuteLabel, colon2, secondLabel);

        HBox unitLabels = new HBox(45);
        unitLabels.setAlignment(Pos.CENTER);
        Label hhLabel = new Label("HH");
        Label mmLabel = new Label("MM");
        Label ssLabel = new Label("SS");
        hhLabel.setFont(Font.font("DM Sans Medium", FontWeight.BOLD, 10));
        mmLabel.setFont(Font.font("DM Sans Medium", FontWeight.BOLD, 10));
        ssLabel.setFont(Font.font("DM Sans Medium", FontWeight.BOLD, 10));
        unitLabels.getChildren().addAll(hhLabel, mmLabel, ssLabel);

        // Timer increment/decrement buttons
        HBox timerControls = new HBox(15);
        timerControls.setAlignment(Pos.CENTER);

        VBox hourControls = new VBox(5);
        hourControls.setAlignment(Pos.CENTER);
        Button hourUp = new Button("▲");
        hourUp.setStyle("-fx-background-color: #3498DB; -fx-text-fill: white; -fx-cursor: hand;");
        hourUp.setOnAction(e -> {
            microTimerHours = (microTimerHours + 1) % 100;
            hourLabel.setText(String.format("%02d", microTimerHours));
        });
        Button hourDown = new Button("▼");
        hourDown.setStyle("-fx-background-color: #3498DB; -fx-text-fill: white; -fx-cursor: hand;");
        hourDown.setOnAction(e -> {
            microTimerHours = (microTimerHours - 1 + 100) % 100;
            hourLabel.setText(String.format("%02d", microTimerHours));
        });
        hourControls.getChildren().addAll(hourUp, hourDown);

        VBox minuteControls = new VBox(5);
        minuteControls.setAlignment(Pos.CENTER);
        Button minuteUp = new Button("▲");
        minuteUp.setStyle("-fx-background-color: #3498DB; -fx-text-fill: white; -fx-cursor: hand;");
        minuteUp.setOnAction(e -> {
            microTimerMinutes = (microTimerMinutes + 1) % 60;
            minuteLabel.setText(String.format("%02d", microTimerMinutes));
        });
        Button minuteDown = new Button("▼");
        minuteDown.setStyle("-fx-background-color: #3498DB; -fx-text-fill: white; -fx-cursor: hand;");
        minuteDown.setOnAction(e -> {
            microTimerMinutes = (microTimerMinutes - 1 + 60) % 60;
            minuteLabel.setText(String.format("%02d", microTimerMinutes));
        });
        minuteControls.getChildren().addAll(minuteUp, minuteDown);

        VBox secondControls = new VBox(5);
        secondControls.setAlignment(Pos.CENTER);
        Button secondUp = new Button("▲");
        secondUp.setStyle("-fx-background-color: #3498DB; -fx-text-fill: white; -fx-cursor: hand;");
        secondUp.setOnAction(e -> {
            microTimerSeconds = (microTimerSeconds + 1) % 60;
            secondLabel.setText(String.format("%02d", microTimerSeconds));
        });
        Button secondDown = new Button("▼");
        secondDown.setStyle("-fx-background-color: #3498DB; -fx-text-fill: white; -fx-cursor: hand;");
        secondDown.setOnAction(e -> {
            microTimerSeconds = (microTimerSeconds - 1 + 60) % 60;
            secondLabel.setText(String.format("%02d", microTimerSeconds));
        });
        secondControls.getChildren().addAll(secondUp, secondDown);

        timerControls.getChildren().addAll(hourControls, minuteControls, secondControls);

        // Start/Stop/Pulse buttons
        HBox timerButtonBox = new HBox(10);
        timerButtonBox.setAlignment(Pos.CENTER);

        Button stopBtn = new Button("Stop");
        stopBtn.setStyle("-fx-background-color: #E74C3C; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8px 20px; -fx-cursor: hand;");
        stopBtn.setOnAction(e -> stopMicroTimer());

        Button startBtn = new Button("Start");
        startBtn.setStyle("-fx-background-color: #27AE60; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8px 20px; -fx-cursor: hand;");
        startBtn.setOnAction(e -> {
            if (!microcentrifugeOpen && tubesInMicrocentrifuge.isEmpty()) {
                showMessage("Please add tubes to microcentrifuge first!");
                return;
            }
            if (microcentrifugeOpen) {
                showMessage("Please close the microcentrifuge before starting!");
                return;
            }
            if (tubesInMicrocentrifuge.size() % 2 != 0) {
                showMessage("Warning: Microcentrifuge not balanced! Add even number of tubes.");
                return;
            }
            startMicroTimer(hourLabel, minuteLabel, secondLabel);
        });

        Button pulseBtn = new Button("Pulse");
        pulseBtn.setStyle("-fx-background-color: #3498DB; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8px 20px; -fx-cursor: hand;");
        pulseBtn.setOnAction(e -> {
            if (!microcentrifugeOpen && tubesInMicrocentrifuge.isEmpty()) {
                showMessage("Please add tubes to microcentrifuge first!");
                return;
            }
            if (microcentrifugeOpen) {
                showMessage("Please close the microcentrifuge before pulsing!");
                return;
            }
            showMessage("Microcentrifuge pulsed!");
        });

        timerButtonBox.getChildren().addAll(stopBtn, startBtn, pulseBtn);

        timerBox.getChildren().addAll(timerTitle, timerDisplay, unitLabels, timerControls, timerButtonBox);

        controlsBox.getChildren().addAll(rpmBox, timerBox);

        // Back button
        Button backButton = new Button("← Back");
        backButton.setFont(Font.font("DM Sans Medium", FontWeight.BOLD, 12));
        backButton.setStyle("-fx-background-color: #1976D2; -fx-text-fill: white; -fx-padding: 10px 20px; -fx-cursor: hand;");
        backButton.setOnAction(e -> microcentrifugeDialog.close());

        mainBox.getChildren().addAll(titleLabel, descLabel, warningBox, controlsBox, backButton);

        // Update timer display periodically if timer is running
        Timeline timerUpdateTimeline = new Timeline(new KeyFrame(Duration.millis(100), e -> {
            if (microTimerRunning) {
                hourLabel.setText(String.format("%02d", microTimerHours));
                minuteLabel.setText(String.format("%02d", microTimerMinutes));
                secondLabel.setText(String.format("%02d", microTimerSeconds));
            }
            // Update balance status
            balanceStatus.setText("Tube balanced: " + (tubesInMicrocentrifuge.size() % 2 == 0 ? "Yes ✓" : "No ❌"));
            balanceStatus.setStyle("-fx-background-color: " + (tubesInMicrocentrifuge.size() % 2 == 0 ? "#D4EDDA" : "#F8D7DA") + "; -fx-padding: 5px; -fx-background-radius: 5px;");
        }));
        timerUpdateTimeline.setCycleCount(Timeline.INDEFINITE);
        timerUpdateTimeline.play();

        microcentrifugeDialog.setOnHidden(e -> timerUpdateTimeline.stop());

        Scene dialogScene = new Scene(mainBox, 700, 600);
        microcentrifugeDialog.setScene(dialogScene);
        microcentrifugeDialog.show();
    }

    private void startMicroTimer(Label hourLabel, Label minuteLabel, Label secondLabel) {
        if (microTimerRunning) return;

        microTimerRunning = true;
        microTimerTimeline = new Timeline(new KeyFrame(Duration.millis(10), e -> {
            // Decrement milliseconds (displayed as seconds)
            microTimerSeconds--;
            if (microTimerSeconds < 0) {
                microTimerSeconds = 59;
                // Decrement seconds (displayed as minutes)
                microTimerMinutes--;
                if (microTimerMinutes < 0) {
                    microTimerMinutes = 59;
                    // Decrement minutes (displayed as hours)
                    microTimerHours--;
                    if (microTimerHours < 0) {
                        stopMicroTimer();
                        removeTubesFromMicrocentrifuge();
                        showMessage("Microcentrifuge timer finished! Tubes removed.");
                        return;
                    }
                }
            }

            hourLabel.setText(String.format("%02d", microTimerHours));
            minuteLabel.setText(String.format("%02d", microTimerMinutes));
            secondLabel.setText(String.format("%02d", microTimerSeconds));
        }));
        microTimerTimeline.setCycleCount(Timeline.INDEFINITE);
        microTimerTimeline.play();
    }

    private void stopMicroTimer() {
        if (microTimerTimeline != null) {
            microTimerTimeline.stop();
        }
        microTimerRunning = false;
    }

    private void removeTubesFromMicrocentrifuge() {
        for (String tubeName : tubesInMicrocentrifuge) {
            DraggableTube tube = tubes.get(tubeName);
            if (tube != null) {
                tube.getView().setVisible(true);
            }
        }
        tubesInMicrocentrifuge.clear();
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

            // Create volume badge (initially hidden)
            String volumeText = (currentVolume == (int) currentVolume) ?
                    (int)currentVolume + "μL" :
                    String.format("%.1f", currentVolume) + "μL";
            volumeBadge = new Label(volumeText);
            volumeBadge.setFont(Font.font("DM Sans Medium", FontWeight.BOLD, 14));
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
            ejectButton.setFont(Font.font("DM Sans Medium", FontWeight.BOLD, 12));
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
                ejectButton.setVisible(false);  // Hide button after ejecting
                e.consume();
            });

            // Create group with body, plunger, badge and eject button
            // Name label — sits above plunger, moves with the pipette
            Label nameLabel = new Label(type);
            nameLabel.setFont(Font.font("DM Sans Medium", FontWeight.BOLD, 11));
            nameLabel.setTextFill(Color.WHITE);
            nameLabel.setStyle(
                    "-fx-background-color: rgba(0,20,45,0.65);" +
                            "-fx-background-radius: 4;" +
                            "-fx-padding: 3 7 3 7;" +
                            "-fx-border-color: rgba(255,255,255,0.2);" +
                            "-fx-border-radius: 4;" +
                            "-fx-border-width: 1;"
            );
            // Center above the pipette body (body width ~30px)
            nameLabel.setLayoutX(-5);
            nameLabel.setLayoutY(-22);

            pipetteGroup = new Group(bodyView, plungerView, volumeBadge, ejectButton, nameLabel);
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

                e.consume();
            });

            pipetteGroup.setOnMouseDragged(e -> {
                x = e.getSceneX() + dragDelta.x;
                y = e.getSceneY() + dragDelta.y;
                pipetteGroup.setLayoutX(x);
                pipetteGroup.setLayoutY(y);

                // Show eject button only if trash is open, pipette is near trash, and has tip
                if (hasTip && trashIsOpen && isInVerticalColumn(x, y, TRASH_X, TRASH_Y, 80)) {
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
                notifyStepComplete("2", "d");
            }

            // Hide eject button when released (user can click it before releasing)
            if (!trashIsOpen || !isInVerticalColumn(x, y, TRASH_X, TRASH_Y, 80)) {
                ejectButton.setVisible(false);
            }

            // Check waste jar interaction for solution ejection
            if (hasTip && hasSolution && isNear(x, y, WASTEJAR_X, WASTEJAR_Y, 100)) {
                ejectSolutionToWaste();
            }

            // Check CColumn interaction for solution ejection (vertical position)
            if (hasTip && hasSolution && isInVerticalColumn(x, y, CCOLUMN_X, CCOLUMN_Y, 50)) {
                ejectSolutionToColumn();
            }

            // Check if near pipette stand to return
            if (isNear(x, y, initialX, initialY, 50)) {
                returnToStand();
            }
        }

        private void ejectSolutionToWaste() {
            showMessage("Solution (" + currentVolume + "µL) ejected into waste jar!");
            currentVolume = 0;
            hasSolution = false;
            updatePipetteImage();
            volumeBadge.setText("0μL");
        }

        private void ejectSolutionToColumn() {
            showMessage("Solution (" + currentVolume + "µL) ejected into Column!");
            currentVolume = 0;
            hasSolution = false;
            updatePipetteImage();
            volumeBadge.setText("0μL");
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

            // Check if pipette is over any tube
            String overTube = getTubeUnderPipette();

            if (overTube == null) {
                showMessage("Position pipette over a tube before drawing/dispensing solution!");
                return;
            }

            // Toggle solution state
            if (!hasSolution) {
                // Draw solution
                hasSolution = true;
                updatePipetteImage();
                showMessage("Solution drawn from " + overTube + " tube into " + type + " pipette!");
            } else {
                // Dispense solution
                hasSolution = false;
                updatePipetteImage();
                showMessage("Solution dispensed from " + type + " pipette into " + overTube + " tube!");
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
            titleLabel.setFont(Font.font("DM Sans Medium", FontWeight.BOLD, 14));
            titleLabel.setTextFill(Color.WHITE);
            titleLabel.setStyle("-fx-background-color: #1a5f7a; -fx-padding: 10px;");
            titleLabel.setMaxWidth(Double.MAX_VALUE);

            // Volume display and controls
            VBox volumeBox = new VBox(10);
            volumeBox.setPadding(new Insets(10));
            volumeBox.setStyle("-fx-border-color: #cccccc; -fx-border-width: 1px;");

            Label volumeLabel = new Label("Micropipette volume");
            volumeLabel.setFont(Font.font("DM Sans Medium", FontWeight.NORMAL, 12));

            HBox volumeControlBox = new HBox(10);
            volumeControlBox.setAlignment(Pos.CENTER);

            TextField volumeField = new TextField(String.valueOf(currentVolume));
            volumeField.setPrefWidth(100);
            volumeField.setStyle("-fx-font-size: 14px;");

            Label unitLabel = new Label("μL");
            unitLabel.setFont(Font.font("DM Sans Medium", FontWeight.NORMAL, 14));

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
            warningLabel.setFont(Font.font("DM Sans Medium", FontWeight.BOLD, 13));

            Label tipWarningText = new Label("Make sure there isn't a tip attached when you change the volume to avoid contamination.");
            tipWarningText.setWrapText(true);
            tipWarningText.setFont(Font.font("DM Sans Medium", FontWeight.NORMAL, 12));

            Label tipStatusLabel = new Label("Tip Attached: " + (hasTip ? "Yes ❌" : "No ✓"));
            tipStatusLabel.setFont(Font.font("DM Sans Medium", FontWeight.BOLD, 12));
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
                            volumeBadge.setText((int)newVolume + "μL");
                        } else {
                            volumeBadge.setText(String.format("%.1f", newVolume) + "μL");
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

    private class TubeImagePaths {
        String closedImagePath;
        String openImagePath;

        TubeImagePaths(String closedImagePath, String openImagePath) {
            this.closedImagePath = closedImagePath;
            this.openImagePath = openImagePath;
        }
    }

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

            // SET FIXED WIDTH for consistent spacing
            view.setMinWidth(60);
            view.setMaxWidth(60);
            view.setPrefWidth(60);

            ImageView tubeImage = SVGLoader.loadSVG(imagePaths.closedImagePath, 50, 70);
            tubeImage.setPreserveRatio(true);

            Label label = new Label(name);
            label.setFont(Font.font("DM Sans Medium", FontWeight.BOLD, 10));
            label.setTextFill(Color.WHITE);
            label.setStyle(
                    "-fx-background-color: rgba(0,20,45,0.65);" +
                            "-fx-background-radius: 4;" +
                            "-fx-padding: 3 7 3 7;" +
                            "-fx-border-color: rgba(255,255,255,0.2);" +
                            "-fx-border-radius: 4;" +
                            "-fx-border-width: 1;"
            );
            // Rotate vertically so it reads bottom-to-top
            label.setRotate(270);

            // Label first (above), then image (below)
            view.getChildren().addAll(label, tubeImage);

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

            // Update the image (index 1 because label is at index 0)
            ImageView newTubeImage = SVGLoader.loadSVG(imagePath, 50, 70);
            newTubeImage.setPreserveRatio(true);
            view.getChildren().set(1, newTubeImage);

            // Notify controller based on tube state changes
            if (isOpen) {
                switch (name) {
                    case "A+":
                        notifyStepComplete("3", "a");
                        break;
                    case "LIG":
                        notifyStepComplete("3", "e");
                        break;
                }
            } else {
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

                checkTubeInteractions();


                // Check if near microcentrifuge to place inside
                if (isNear(x, y, MICROCENTRIFUGE_X, MICROCENTRIFUGE_Y, 100) && microcentrifugeOpen) {
                    if (tubesInMicrocentrifuge.size() < 6) {
                        tubesInMicrocentrifuge.add(name);
                        view.setVisible(false);
                        showMessage("Tube placed in microcentrifuge!");
                    } else {
                        showMessage("Microcentrifuge is full (max 6 tubes)!");
                    }
                }


                e.consume();
            });
        }

        private void checkTubeInteractions() {

            if (isNear(x, y, MICROCENTRIFUGE_X, MICROCENTRIFUGE_Y, 100) && microcentrifugeOpen) {
                if (name.equals("LIG")) {
                    notifyStepComplete("5", "b");
                } else if (name.equals("BAL")) {
                    notifyStepComplete("5", "d");
                }
            }

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

    private void notifyStepComplete(String step, String subStep) {
        if (controller != null) {
            //controller.highlightNextInstruction(step, subStep);
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

}