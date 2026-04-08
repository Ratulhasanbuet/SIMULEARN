package com.example.simulearn.SimuLearn.Biology.Ligation;

import com.example.simulearn.SimuLearn.Delta;
import com.example.simulearn.SimuLearn.SVGLoader;
import javafx.animation.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.InnerShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
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

public class LigationLabWorkspace {

    private Pane labWorkspace;
    private LigationController controller;

    private static final double ICE_BUCKET_X = 80;
    private static final double ICE_BUCKET_Y = 750;

    private static final double VORTEX_X = 250;
    private static final double VORTEX_Y = 760;

    private static final double FLOATING_RACK_X = 650;
    private static final double FLOATING_RACK_Y = 760;

    private static final double MICROCENTRIFUGE_X = 800;
    private static final double MICROCENTRIFUGE_Y = 725;

    private static final double WATER_BATH_X = 928;
    private static final double WATER_BATH_Y = 705;

    private static final double FREEZER_X = 1160;
    private static final double FREEZER_Y = 655;

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

    private static final double TRASH_X = 1050;
    private static final double TRASH_Y = 270;

    private static final double TUBE_RACK_X = 400;
    private static final double TUBE_RACK_Y = 780;

    private static final double TABLE_Y = 850;

    private boolean waterBathOpen = false;
    private boolean freezerOpen = false;
    private boolean microcentrifugeOpen = false;
    private Map<String, Boolean> tipBoxStates = new HashMap<>();
    private int waterBathTemp = 20;

    private int waterBathSetTemp = 25;
    private int waterBathCurrentTemp = 25;
    private Timeline temperatureTransition;
    private int timerHours = 0;
    private int timerMinutes = 0;
    private int timerSeconds = 30;
    private Timeline timerTimeline;
    private boolean timerRunning = false;

    private VBox waterBathHoverBox;
    private boolean showWaterBathButtons = false;

    private VBox microcentrifugeHoverBox;
    private List<String> tubesInMicrocentrifuge = new ArrayList<>();
    private int microcentrifugeRPM = 1300;
    private int microTimerHours = 0;
    private int microTimerMinutes = 0;
    private int microTimerSeconds = 0;
    private Timeline microTimerTimeline;
    private boolean microTimerRunning = false;
    private Stage microcentrifugeDialog;

    private List<String> tubesInFreezer = new ArrayList<>();

    private String tubeInVortex = null;
    private VBox vortexButtonBox;
    private Timeline vortexTimeline;

    private double floatingRackX = FLOATING_RACK_X;
    private double floatingRackY = FLOATING_RACK_Y;
    private List<String> tubesInRack = new ArrayList<>();
    private boolean rackInWaterBath = false;
    private double rackOriginalX = FLOATING_RACK_X;
    private double rackOriginalY = FLOATING_RACK_Y;

    private Map<String, TubeState> tubeStates = new HashMap<>();

    private ImageView waterBathView;
    private ImageView freezerView;
    private ImageView vortexView;
    private ImageView iceBucketView;
    private ImageView pipetteStandView;
    private ImageView trashView;
    private ImageView microcentrifugeView;
    private ImageView floatingRackView;

    private Map<String, ImageView> tipBoxViews = new HashMap<>();

    private Map<String, DraggablePipette> pipettes = new HashMap<>();

    private DraggablePipette currentlyActivePipette = null;

    private Map<String, DraggableTube> tubes = new HashMap<>();

    private Stage waterBathDialog;

    private int currentStep = 1;
    private String currentSubStep = "a";

    public LigationLabWorkspace(LigationController controller) {
        this.controller = controller;
        initializeTubeStates();
        initializeTipBoxStates();
        createLabWorkspace();
    }

    private void initializeTubeStates() {
        tubeStates.put("A+", new TubeState("A+", 10, false, Color.web("#4da6ff")));
        tubeStates.put("K+", new TubeState("K+", 10, false, Color.web("#4da6ff")));
        tubeStates.put("LIG", new TubeState("LIG", 0, false, Color.TRANSPARENT));
        tubeStates.put("5XB", new TubeState("5XB", 20, false, Color.web("#ffeb99")));
        tubeStates.put("dH2O", new TubeState("dH2O", 100, false, Color.TRANSPARENT));
        tubeStates.put("BAL", new TubeState("BAL", 0, false, Color.TRANSPARENT));
    }

    private void initializeTipBoxStates() {
        tipBoxStates.put("P2", false);
        tipBoxStates.put("P20", false);
        tipBoxStates.put("P200", false);
        tipBoxStates.put("P1000", false);
    }

    public Pane getLabWorkspace() {
        return labWorkspace;
    }

    private void createLabWorkspace() {
        labWorkspace = new Pane();
        labWorkspace.setPrefSize(1515, 1056);
        labWorkspace.setMinSize(1515, 1056);
        labWorkspace.setMaxSize(1515, 1056);
        labWorkspace.setStyle("-fx-background-color: linear-gradient(to bottom, #003E6B, #005A89);");

        Rectangle floor = new Rectangle(0, TABLE_Y + 206, 1515, 1056 - (TABLE_Y + 206));
        floor.setFill(new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0.0, Color.web("#1a2a3a")),
                new Stop(1.0, Color.web("#0d1a26"))));
        labWorkspace.getChildren().add(floor);

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

        createTubes();
        createEquipment();
        createTipBoxes();
        createPipettes();
    }

    private void createEquipment() {

        iceBucketView = createEquipmentImage("/Images/LigationImage/equipment/ice_bucket.svg",
                150, 120, ICE_BUCKET_X, ICE_BUCKET_Y);

        vortexView = createEquipmentImage("/Images/LigationImage/equipment/vortex.svg",
                120, 100, VORTEX_X, VORTEX_Y);
        vortexView.setOnMouseClicked(e -> handleVortex());

        floatingRackView = createEquipmentImage("/Images/LigationImage/equipment/floating-solution-tube-rack-empty.svg",
                120, 90, FLOATING_RACK_X, FLOATING_RACK_Y);
        makeFloatingRackDraggable();

        microcentrifugeView = createEquipmentImage("/Images/LigationImage/equipment/Microcentrifuge.svg",
                150, 130, MICROCENTRIFUGE_X, MICROCENTRIFUGE_Y);

        createMicrocentrifugeHoverButtons();

        microcentrifugeView.setOnMouseEntered(e -> {
            microcentrifugeHoverBox.setOpacity(1.0);
        });

        microcentrifugeView.setOnMouseExited(e -> {
            if (!microcentrifugeHoverBox.isHover()) {
                microcentrifugeHoverBox.setOpacity(0.0);
            }
        });

        waterBathView = createEquipmentImage("/Images/LigationImage/equipment/Water Bath.svg",
                180, 150, WATER_BATH_X, WATER_BATH_Y);

        createWaterBathHoverButtons();

        waterBathView.setOnMouseEntered(e -> {
            waterBathHoverBox.setOpacity(1.0);
        });

        waterBathView.setOnMouseExited(e -> {

            if (!waterBathHoverBox.isHover()) {
                waterBathHoverBox.setOpacity(0.0);
            }
        });

        freezerView = createEquipmentImage("/Images/LigationImage/equipment/freezer-closed.svg",
                150, 200, FREEZER_X, FREEZER_Y);
        freezerView.setOnMouseClicked(e -> toggleFreezer());

        pipetteStandView = createEquipmentImage("/Images/LigationImage/equipment/PipetteRack.svg",
                180, 220, PIPETTE_STAND_X, PIPETTE_STAND_Y);

        Image image = new Image(
                getClass().getResource("/Images/LigationImage/equipment/Trash.png").toExternalForm()
        );
        trashView = new ImageView(image);
        trashView.setLayoutX(TRASH_X);
        trashView.setLayoutY(TRASH_Y);
        trashView.setCursor(Cursor.HAND);
        labWorkspace.getChildren().add(trashView);
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

    private void createTipBoxes() {

        createTipBoxImage("/Images/LigationImage/tipboxes/P2-sm-close.svg",
                "P2",
                "/Images/LigationImage/tipboxes/P2-sm-open.svg",
                TIP_BOX_P2_X, TIP_BOX_P2_Y);
        addTipBoxLabel("P2", TIP_BOX_P2_X, TIP_BOX_P2_Y - 20);

        createTipBoxImage("/Images/LigationImage/tipboxes/P20-sm-close.svg",
                "P20",
                "/Images/LigationImage/tipboxes/P20-sm-open.svg",
                TIP_BOX_P20_X, TIP_BOX_P20_Y);
        addTipBoxLabel("P20", TIP_BOX_P20_X, TIP_BOX_P20_Y - 20);

        createTipBoxImage("/Images/LigationImage/tipboxes/P200-sm-close.svg",
                "P200",
                "/Images/LigationImage/tipboxes/P200-sm-open.svg",
                TIP_BOX_P200_X, TIP_BOX_P200_Y);
        addTipBoxLabel("P200", TIP_BOX_P200_X, TIP_BOX_P200_Y - 20);

        createTipBoxImage("/Images/LigationImage/tipboxes/P1000-sm-close.svg",
                "P1000",
                "/Images/LigationImage/tipboxes/P1000-sm-open.svg",
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
        label.setFont(Font.font("DM Sans Medium", FontWeight.BOLD, 13));
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

        if (!isOpen) {
            notifyStepComplete("2", "c");
        }
    }

    private void createPipettes() {

        DraggablePipette p2 = new DraggablePipette("P2",
                PIPETTE_STAND_X + 25,
                PIPETTE_STAND_Y,
                "/Images/LigationImage/pipettes/P2-body.svg",
                "/Images/LigationImage/pipettes/P2-bodywithtip.svg");
        pipettes.put("P2", p2);
        labWorkspace.getChildren().add(p2.getView());

        DraggablePipette p20 = new DraggablePipette("P20",
                PIPETTE_STAND_X + 60,
                PIPETTE_STAND_Y,
                "/Images/LigationImage/pipettes/P20-body.svg",
                "/Images/LigationImage/pipettes/P20-bodywithtip.svg");
        pipettes.put("P20", p20);
        labWorkspace.getChildren().add(p20.getView());

        DraggablePipette p200 = new DraggablePipette("P200",
                PIPETTE_STAND_X + 95,
                PIPETTE_STAND_Y,
                "/Images/LigationImage/pipettes/P200-body.svg",
                "/Images/LigationImage/pipettes/P200-bodywithtip.svg");
        pipettes.put("P200", p200);
        labWorkspace.getChildren().add(p200.getView());

        DraggablePipette p1000 = new DraggablePipette("P1000",
                PIPETTE_STAND_X + 130,
                PIPETTE_STAND_Y,
                "/Images/LigationImage/pipettes/P1000-body.svg",
                "/Images/LigationImage/pipettes/P1000-bodywithtip.svg");
        pipettes.put("P1000", p1000);
        labWorkspace.getChildren().add(p1000.getView());
    }

    private void createTubes() {

        Map<String, TubeImagePaths> tubeImageMap = new HashMap<>();

        tubeImageMap.put("A+", new TubeImagePaths(
                "/Images/LigationImage/reagents/A+-close.svg",
                "/Images/LigationImage/reagents/A+-open.svg"
        ));

        tubeImageMap.put("K+", new TubeImagePaths(
                "/Images/LigationImage/reagents/K+-close.svg",
                "/Images/LigationImage/reagents/K+-open.svg"
        ));

        tubeImageMap.put("LIG", new TubeImagePaths(
                "/Images/LigationImage/reagents/LIG-close.svg",
                "/Images/LigationImage/reagents/LIG-open.svg"
        ));

        tubeImageMap.put("5XB", new TubeImagePaths(
                "/Images/LigationImage/reagents/5XB-close.svg",
                "/Images/LigationImage/reagents/5XB-open.svg"
        ));

        tubeImageMap.put("dH2O", new TubeImagePaths(
                "/Images/LigationImage/reagents/dH20-close.svg",
                "/Images/LigationImage/reagents/dH20-open.svg"
        ));

        tubeImageMap.put("BAL", new TubeImagePaths(
                "/Images/LigationImage/reagents/BAL-close.svg",
                "/Images/LigationImage/reagents/BAL-open.svg"
        ));

        String[] tubeNames = {"A+", "K+", "LIG", "5XB", "dH2O", "BAL"};
        double startX = TUBE_RACK_X + 30;
        double tubeY = TUBE_RACK_Y + 15;
        DraggableTube A_positive = new DraggableTube(tubeNames[0], ICE_BUCKET_X + 30, ICE_BUCKET_Y - 5, tubeImageMap.get(tubeNames[0]));
        tubes.put(tubeNames[0], A_positive);
        labWorkspace.getChildren().add(A_positive.getView());

        DraggableTube K_positive = new DraggableTube(tubeNames[1], ICE_BUCKET_X + 60, ICE_BUCKET_Y - 5, tubeImageMap.get(tubeNames[1]));
        tubes.put(tubeNames[1], K_positive);
        labWorkspace.getChildren().add(K_positive.getView());

        for (int i = 2; i < tubeNames.length; i++) {
            String tubeName = tubeNames[i];
            DraggableTube tube = new DraggableTube(tubeName, startX + ((i - 2) * 45)-14, tubeY-25, tubeImageMap.get(tubeName));
            tubes.put(tubeName, tube);
            labWorkspace.getChildren().add(tube.getView());
        }
        ImageView tubeRack = createEquipmentImage("/Images/LigationImage/equipment/tube-rack-on-shelf.svg",
                200, 90, TUBE_RACK_X, TUBE_RACK_Y);

    }

    private void createWaterBathHoverButtons() {
        waterBathHoverBox = new VBox(5);

        updateHoverButtonPosition();
        waterBathHoverBox.setOpacity(0.0);
        waterBathHoverBox.setVisible(true);
        waterBathHoverBox.setStyle("-fx-background-color: white; -fx-padding: 5px; " +
                "-fx-background-radius: 5px; -fx-border-color: #ccc; " +
                "-fx-border-radius: 5px;");

        Button openBtn = new Button(waterBathOpen ? "Close     " : "Open     ");
        openBtn.setFont(Font.font("DM Sans Medium", FontWeight.BOLD, 11));
        openBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; " +
                "-fx-cursor: hand; -fx-padding: 5px 15px;");
        openBtn.setOnAction(e -> {

            if (!waterBathOpen && rackInWaterBath && timerRunning) {
                showMessage("Cannot open water bath while timer is running with rack inside!");
                return;
            }

            waterBathOpen = !waterBathOpen;
            openBtn.setText(waterBathOpen ? "Close     " : "Open     ");
            updateWaterBathView();
            updateHoverButtonPosition();
            if (waterBathOpen) {
                notifyStepComplete("1", "a");
            }
            waterBathHoverBox.setOpacity(0.0);
        });

        Button settingsBtn = new Button("Settings");
        settingsBtn.setFont(Font.font("DM Sans Medium", FontWeight.BOLD, 11));
        settingsBtn.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; " +
                "-fx-cursor: hand; -fx-padding: 5px 15px;");
        settingsBtn.setOnAction(e -> {
            openWaterBathSettingsDialog();
            waterBathHoverBox.setOpacity(0.0);
        });

        waterBathHoverBox.getChildren().addAll(openBtn, settingsBtn);

        waterBathHoverBox.setOnMouseEntered(e -> {
            waterBathHoverBox.setOpacity(1.0);
        });

        waterBathHoverBox.setOnMouseExited(e -> {
            waterBathHoverBox.setOpacity(0.0);
        });

        labWorkspace.getChildren().add(waterBathHoverBox);
    }

    private void updateHoverButtonPosition() {

        double yOffset = waterBathOpen ? -80 : -60;
        waterBathHoverBox.setLayoutX(WATER_BATH_X + 60);
        waterBathHoverBox.setLayoutY(WATER_BATH_Y + yOffset);
    }

    private void createMicrocentrifugeHoverButtons() {
        microcentrifugeHoverBox = new VBox(5);
        microcentrifugeHoverBox.setLayoutX(MICROCENTRIFUGE_X + 60);
        microcentrifugeHoverBox.setLayoutY(MICROCENTRIFUGE_Y - 60);
        microcentrifugeHoverBox.setOpacity(0.0);
        microcentrifugeHoverBox.setVisible(true);
        microcentrifugeHoverBox.setStyle("-fx-background-color: white; -fx-padding: 5px; " +
                "-fx-background-radius: 5px; -fx-border-color: #ccc; " +
                "-fx-border-radius: 5px;");

        Button openBtn = new Button(microcentrifugeOpen ? "Close" : "Open");
        openBtn.setFont(Font.font("DM Sans Medium", FontWeight.BOLD, 11));
        openBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; " +
                "-fx-cursor: hand; -fx-padding: 5px 15px;");
        openBtn.setOnAction(e -> {

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
        settingsBtn.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; " +
                "-fx-cursor: hand; -fx-padding: 5px 15px;");
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

    private void updateMicrocentrifugeView() {
        String imagePath = microcentrifugeOpen ?
                "/Images/LigationImage/equipment/microcentrifuge_body.svg" :
                "/Images/LigationImage/equipment/Microcentrifuge.svg";

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

    private void openWaterBathDialog() {

        openWaterBathSettingsDialog();
    }

    private void openWaterBathSettingsDialog() {
        if (waterBathDialog != null && waterBathDialog.isShowing()) {
            return;
        }

        waterBathDialog = new Stage();
        waterBathDialog.initModality(Modality.APPLICATION_MODAL);
        waterBathDialog.setTitle("Water Bath");

        VBox mainBox = new VBox(20);
        mainBox.setPadding(new Insets(20));
        mainBox.setStyle("-fx-background-color: white;");

        Label titleLabel = new Label("Water bath");
        titleLabel.setFont(Font.font("DM Sans Medium", FontWeight.BOLD, 18));

        Label descLabel = new Label("A water bath is a piece of lab equipment\nused to incubate samples at a constant\ntemperature.");
        descLabel.setFont(Font.font("DM Sans Medium", 12));

        VBox hintBox = new VBox(5);
        hintBox.setStyle("-fx-background-color: #E3F2FD; -fx-padding: 10px; -fx-border-color: #2196F3; -fx-border-width: 1px;");
        Label hintTitle = new Label("Hint");
        hintTitle.setFont(Font.font("DM Sans Medium", FontWeight.BOLD, 12));
        hintTitle.setTextFill(Color.web("#1976D2"));
        Label hintText = new Label("Only add the floating rack with tupes\nwhen the set temperature matches\nthe current temperature.");
        hintText.setFont(Font.font("DM Sans Medium", 11));
        hintBox.getChildren().addAll(hintTitle, hintText);

        HBox controlsBox = new HBox(20);
        controlsBox.setAlignment(Pos.CENTER);

        VBox tempBox = new VBox(10);
        tempBox.setAlignment(Pos.CENTER);
        tempBox.setStyle("-fx-background-color: #F5F5F5; -fx-padding: 15px; -fx-border-radius: 10px; -fx-background-radius: 10px;");

        Label tempTitle = new Label("Set temperature");
        tempTitle.setFont(Font.font("DM Sans Medium", FontWeight.BOLD, 12));

        VBox tempDisplayBox = new VBox(5);
        tempDisplayBox.setAlignment(Pos.CENTER);
        tempDisplayBox.setStyle("-fx-background-color: #2C3E50; -fx-padding: 20px; -fx-border-radius: 10px; -fx-background-radius: 10px;");

        Label setTempLabel = new Label(waterBathSetTemp + "°C");
        setTempLabel.setFont(Font.font("DM Sans Medium", FontWeight.BOLD, 36));
        setTempLabel.setTextFill(Color.WHITE);

        Button tempUpBtn = new Button("▲");
        tempUpBtn.setStyle("-fx-background-color: #3498DB; -fx-text-fill: white; -fx-font-size: 16px; -fx-cursor: hand;");
        tempUpBtn.setOnAction(e -> {
            if (waterBathSetTemp < 100) {
                waterBathSetTemp++;
                setTempLabel.setText(waterBathSetTemp + "°C");
                startTemperatureTransition();
            }
        });

        Button tempDownBtn = new Button("▼");
        tempDownBtn.setStyle("-fx-background-color: #3498DB; -fx-text-fill: white; -fx-font-size: 16px; -fx-cursor: hand;");
        tempDownBtn.setOnAction(e -> {
            if (waterBathSetTemp > 0) {
                waterBathSetTemp--;
                setTempLabel.setText(waterBathSetTemp + "°C");
                startTemperatureTransition();
            }
        });

        VBox tempButtons = new VBox(5, tempUpBtn, tempDownBtn);
        tempButtons.setAlignment(Pos.CENTER);

        HBox tempControlRow = new HBox(10, tempDisplayBox, tempButtons);
        tempControlRow.setAlignment(Pos.CENTER);

        Label currentTempTitle = new Label("Current temperature");
        currentTempTitle.setFont(Font.font("DM Sans Medium", 10));
        currentTempTitle.setTextFill(Color.GRAY);

        Label currentTempLabel = new Label(waterBathCurrentTemp + "°C");
        currentTempLabel.setFont(Font.font("DM Sans Medium", FontWeight.BOLD, 24));
        currentTempLabel.setTextFill(Color.web("#27AE60"));
        currentTempLabel.setStyle("-fx-background-color: #2C3E50; -fx-padding: 10px; -fx-border-radius: 5px; -fx-background-radius: 5px;");

        tempBox.getChildren().addAll(tempTitle, tempControlRow, currentTempTitle, currentTempLabel);

        VBox timerBox = new VBox(10);
        timerBox.setAlignment(Pos.CENTER);
        timerBox.setStyle("-fx-background-color: #F5F5F5; -fx-padding: 15px; -fx-border-radius: 10px; -fx-background-radius: 10px;");

        Label timerTitle = new Label("Set timer");
        timerTitle.setFont(Font.font("DM Sans Medium", FontWeight.BOLD, 12));

        HBox timerDisplay = new HBox(5);
        timerDisplay.setAlignment(Pos.CENTER);
        timerDisplay.setStyle("-fx-background-color: #2C3E50; -fx-padding: 15px; -fx-border-radius: 10px; -fx-background-radius: 10px;");

        Label hourLabel = new Label(String.format("%02d", timerHours));
        hourLabel.setFont(Font.font("Courier New", FontWeight.BOLD, 32));
        hourLabel.setTextFill(Color.WHITE);

        Label colon1 = new Label(":");
        colon1.setFont(Font.font("Courier New", FontWeight.BOLD, 32));
        colon1.setTextFill(Color.WHITE);

        Label minuteLabel = new Label(String.format("%02d", timerMinutes));
        minuteLabel.setFont(Font.font("Courier New", FontWeight.BOLD, 32));
        minuteLabel.setTextFill(Color.WHITE);

        Label colon2 = new Label(":");
        colon2.setFont(Font.font("Courier New", FontWeight.BOLD, 32));
        colon2.setTextFill(Color.WHITE);

        Label secondLabel = new Label(String.format("%02d", timerSeconds));
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

        HBox timerControls = new HBox(15);
        timerControls.setAlignment(Pos.CENTER);

        VBox hourControls = new VBox(5);
        hourControls.setAlignment(Pos.CENTER);
        Button hourUp = new Button("▲");
        hourUp.setStyle("-fx-background-color: #3498DB; -fx-text-fill: white; -fx-cursor: hand;");
        hourUp.setOnAction(e -> {
            timerHours = (timerHours + 1) % 100;
            hourLabel.setText(String.format("%02d", timerHours));
        });
        Button hourDown = new Button("▼");
        hourDown.setStyle("-fx-background-color: #3498DB; -fx-text-fill: white; -fx-cursor: hand;");
        hourDown.setOnAction(e -> {
            timerHours = (timerHours - 1 + 100) % 100;
            hourLabel.setText(String.format("%02d", timerHours));
        });
        hourControls.getChildren().addAll(hourUp, hourDown);

        VBox minuteControls = new VBox(5);
        minuteControls.setAlignment(Pos.CENTER);
        Button minuteUp = new Button("▲");
        minuteUp.setStyle("-fx-background-color: #3498DB; -fx-text-fill: white; -fx-cursor: hand;");
        minuteUp.setOnAction(e -> {
            timerMinutes = (timerMinutes + 1) % 60;
            minuteLabel.setText(String.format("%02d", timerMinutes));
        });
        Button minuteDown = new Button("▼");
        minuteDown.setStyle("-fx-background-color: #3498DB; -fx-text-fill: white; -fx-cursor: hand;");
        minuteDown.setOnAction(e -> {
            timerMinutes = (timerMinutes - 1 + 60) % 60;
            minuteLabel.setText(String.format("%02d", timerMinutes));
        });
        minuteControls.getChildren().addAll(minuteUp, minuteDown);

        VBox secondControls = new VBox(5);
        secondControls.setAlignment(Pos.CENTER);
        Button secondUp = new Button("▲");
        secondUp.setStyle("-fx-background-color: #3498DB; -fx-text-fill: white; -fx-cursor: hand;");
        secondUp.setOnAction(e -> {
            timerSeconds = (timerSeconds + 1) % 60;
            secondLabel.setText(String.format("%02d", timerSeconds));
        });
        Button secondDown = new Button("▼");
        secondDown.setStyle("-fx-background-color: #3498DB; -fx-text-fill: white; -fx-cursor: hand;");
        secondDown.setOnAction(e -> {
            timerSeconds = (timerSeconds - 1 + 60) % 60;
            secondLabel.setText(String.format("%02d", timerSeconds));
        });
        secondControls.getChildren().addAll(secondUp, secondDown);

        timerControls.getChildren().addAll(hourControls, minuteControls, secondControls);

        HBox timerButtonBox = new HBox(10);
        timerButtonBox.setAlignment(Pos.CENTER);

        Button stopBtn = new Button("Stop");
        stopBtn.setStyle("-fx-background-color: #8B0000; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8px 20px; -fx-cursor: hand;");
        stopBtn.setOnAction(e -> stopTimer());

        Button startBtn = new Button("Start");
        startBtn.setStyle("-fx-background-color: #FFA500; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8px 20px; -fx-cursor: hand;");
        startBtn.setOnAction(e -> {

            if (rackInWaterBath && waterBathOpen) {
                showMessage("Please close the water bath before starting timer!");
                return;
            }

            if (!rackInWaterBath) {
                showMessage("Note: Starting timer without rack in water bath.");
            }

            startTimer(hourLabel, minuteLabel, secondLabel);
        });

        timerButtonBox.getChildren().addAll(stopBtn, startBtn);

        timerBox.getChildren().addAll(timerTitle, timerDisplay, unitLabels, timerControls, timerButtonBox);

        controlsBox.getChildren().addAll(tempBox, timerBox);

        Button backButton = new Button("← Back");
        backButton.setFont(Font.font("DM Sans Medium", FontWeight.BOLD, 12));
        backButton.setStyle("-fx-background-color: #1976D2; -fx-text-fill: white; -fx-padding: 10px 20px; -fx-cursor: hand;");
        backButton.setOnAction(e -> waterBathDialog.close());

        mainBox.getChildren().addAll(titleLabel, descLabel, hintBox, controlsBox, backButton);

        Timeline tempUpdateTimeline = new Timeline(new KeyFrame(Duration.millis(500), e -> {
            currentTempLabel.setText(waterBathCurrentTemp + "°C");
        }));
        tempUpdateTimeline.setCycleCount(Timeline.INDEFINITE);
        tempUpdateTimeline.play();

        waterBathDialog.setOnHidden(e -> tempUpdateTimeline.stop());

        Scene dialogScene = new Scene(mainBox, 700, 600);
        waterBathDialog.setScene(dialogScene);
        waterBathDialog.show();
    }

    private void startTemperatureTransition() {
        if (temperatureTransition != null) {
            temperatureTransition.stop();
        }

        temperatureTransition = new Timeline(new KeyFrame(Duration.millis(100), e -> {
            if (waterBathCurrentTemp < waterBathSetTemp) {
                waterBathCurrentTemp++;
            } else if (waterBathCurrentTemp > waterBathSetTemp) {
                waterBathCurrentTemp--;
            }
        }));
        temperatureTransition.setCycleCount(Timeline.INDEFINITE);
        temperatureTransition.play();
    }

    private void startTimer(Label hourLabel, Label minuteLabel, Label secondLabel) {
        if (timerRunning) return;

        timerRunning = true;
        timerTimeline = new Timeline(new KeyFrame(Duration.millis(10), e -> {

            timerSeconds--;
            if (timerSeconds < 0) {
                timerSeconds = 59;

                timerMinutes--;
                if (timerMinutes < 0) {
                    timerMinutes = 59;

                    timerHours--;
                    if (timerHours < 0) {

                        stopTimer();

                        if (rackInWaterBath) {
                            removeRackFromWaterBath();
                            showMessage("Timer finished! Rack removed from water bath.");
                        } else {
                            showMessage("Timer finished!");
                        }
                        return;
                    }
                }
            }

            hourLabel.setText(String.format("%02d", timerHours));
            minuteLabel.setText(String.format("%02d", timerMinutes));
            secondLabel.setText(String.format("%02d", timerSeconds));
        }));
        timerTimeline.setCycleCount(Timeline.INDEFINITE);
        timerTimeline.play();
    }

    private void stopTimer() {
        if (timerTimeline != null) {
            timerTimeline.stop();
        }
        timerRunning = false;
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

        Label titleLabel = new Label("Programming the microcentrifuge");
        titleLabel.setFont(Font.font("DM Sans Medium", FontWeight.BOLD, 16));

        Label descLabel = new Label("A microcentrifuge is used to spin samples (typically in 1.5 mL tubes) at\n" +
                "high speeds. To spin your samples at a set speed and time, set the RPM and\n" +
                "time using the arrows and buttons. To quickly spin down your samples,\n" +
                "pressing the Pulse button will accelerates the rotor to the preset RPM\n" +
                "then promptly decelerates it.");
        descLabel.setFont(Font.font("DM Sans Medium", 11));

        VBox warningBox = new VBox(5);
        warningBox.setStyle("-fx-background-color: #FFF3CD; -fx-padding: 10px; -fx-border-color: #FFC107; -fx-border-width: 1px;");
        Label warningTitle = new Label("⚠ Double check");
        warningTitle.setFont(Font.font("DM Sans Medium", FontWeight.BOLD, 12));
        Label warningText = new Label("Make sure the microcentrifuge is balanced before begining your spin.");
        warningText.setFont(Font.font("DM Sans Medium", 11));

        Label balanceStatus = new Label("Tube balanced: " + (tubesInMicrocentrifuge.size() % 2 == 0 ? "Yes ✓" : "No ❌"));
        balanceStatus.setFont(Font.font("DM Sans Medium", FontWeight.BOLD, 12));
        balanceStatus.setStyle("-fx-background-color: " +
                (tubesInMicrocentrifuge.size() % 2 == 0 ? "#D4EDDA" : "#F8D7DA") +
                "; -fx-padding: 5px; -fx-background-radius: 5px;");

        warningBox.getChildren().addAll(warningTitle, warningText, balanceStatus);

        HBox controlsBox = new HBox(20);
        controlsBox.setAlignment(Pos.CENTER);

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

        rpmDisplayBox.getChildren().add(rpmLabel);

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

        Button backButton = new Button("← Back");
        backButton.setFont(Font.font("DM Sans Medium", FontWeight.BOLD, 12));
        backButton.setStyle("-fx-background-color: #1976D2; -fx-text-fill: white; -fx-padding: 10px 20px; -fx-cursor: hand;");
        backButton.setOnAction(e -> microcentrifugeDialog.close());

        mainBox.getChildren().addAll(titleLabel, descLabel, warningBox, controlsBox, backButton);

        Timeline timerUpdateTimeline = new Timeline(new KeyFrame(Duration.millis(100), e -> {
            if (microTimerRunning) {
                hourLabel.setText(String.format("%02d", microTimerHours));
                minuteLabel.setText(String.format("%02d", microTimerMinutes));
                secondLabel.setText(String.format("%02d", microTimerSeconds));
            }

            balanceStatus.setText("Tube balanced: " + (tubesInMicrocentrifuge.size() % 2 == 0 ? "Yes ✓" : "No ❌"));
            balanceStatus.setStyle("-fx-background-color: " +
                    (tubesInMicrocentrifuge.size() % 2 == 0 ? "#D4EDDA" : "#F8D7DA") +
                    "; -fx-padding: 5px; -fx-background-radius: 5px;");
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

            microTimerSeconds--;
            if (microTimerSeconds < 0) {
                microTimerSeconds = 59;

                microTimerMinutes--;
                if (microTimerMinutes < 0) {
                    microTimerMinutes = 59;

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

    private void updateWaterBathView() {
        String imagePath = waterBathOpen ?
                "/Images/LigationImage/equipment/Water-bath-open.svg" :
                "/Images/LigationImage/equipment/Water Bath.svg";

        ImageView newView = SVGLoader.loadSVG(imagePath, 180, 150);
        newView.setLayoutX(WATER_BATH_X);
        newView.setLayoutY(WATER_BATH_Y);
        newView.setCursor(Cursor.HAND);

        newView.setOnMouseEntered(e -> {
            waterBathHoverBox.setOpacity(1.0);
        });

        newView.setOnMouseExited(e -> {
            if (!waterBathHoverBox.isHover()) {
                waterBathHoverBox.setOpacity(0.0);
            }
        });

        DropShadow shadow = new DropShadow();
        shadow.setColor(Color.rgb(0, 0, 0, 0.3));
        shadow.setRadius(5);
        newView.setEffect(shadow);

        labWorkspace.getChildren().remove(waterBathView);
        waterBathView = newView;
        labWorkspace.getChildren().add(waterBathView);
        waterBathView.toBack();

        updateHoverButtonPosition();

        waterBathHoverBox.toFront();
    }

    private void makeFloatingRackDraggable() {
        final Delta dragDelta = new Delta();

        floatingRackView.setOnMousePressed(e -> {
            dragDelta.x = floatingRackView.getLayoutX() - e.getSceneX();
            dragDelta.y = floatingRackView.getLayoutY() - e.getSceneY();
            floatingRackView.setCursor(Cursor.MOVE);
            e.consume();
        });

        floatingRackView.setOnMouseDragged(e -> {
            floatingRackX = e.getSceneX() + dragDelta.x;
            floatingRackY = e.getSceneY() + dragDelta.y;
            floatingRackView.setLayoutX(floatingRackX);
            floatingRackView.setLayoutY(floatingRackY);

            for (String tubeName : tubesInRack) {
                DraggableTube tube = tubes.get(tubeName);
                if (tube != null) {
                    tube.moveWithRack(floatingRackX, floatingRackY);
                }
            }

            e.consume();
        });

        floatingRackView.setOnMouseReleased(e -> {
            floatingRackView.setCursor(Cursor.HAND);

            if (waterBathOpen && isNear(floatingRackX, floatingRackY, WATER_BATH_X, WATER_BATH_Y, 100)) {

                placeRackInWaterBath();
            }

            e.consume();
        });
    }

    private void placeRackInWaterBath() {
        if (tubesInRack.isEmpty()) {
            showMessage("Please add tubes to the rack first!");
            return;
        }

        rackOriginalX = FLOATING_RACK_X;
        rackOriginalY = FLOATING_RACK_Y;

        rackInWaterBath = true;

        floatingRackView.setVisible(false);
        for (String tubeName : tubesInRack) {
            DraggableTube tube = tubes.get(tubeName);
            if (tube != null) {
                tube.getView().setVisible(false);
            }
        }

        showMessage("Rack placed in water bath! Close the lid and start timer.");
    }

    private void removeRackFromWaterBath() {
        rackInWaterBath = false;

        floatingRackX = rackOriginalX;
        floatingRackY = rackOriginalY;
        floatingRackView.setLayoutX(floatingRackX);
        floatingRackView.setLayoutY(floatingRackY);
        floatingRackView.setVisible(true);

        for (int i = 0; i < tubesInRack.size(); i++) {
            String tubeName = tubesInRack.get(i);
            DraggableTube tube = tubes.get(tubeName);
            if (tube != null) {
                tube.setRackPosition(i, floatingRackX, floatingRackY);
                tube.getView().setVisible(true);
            }
        }

        showMessage("Rack removed from water bath with tubes!");
    }

    private void attachTubeToRack(String tubeName, DraggableTube tube) {
        if (!tubesInRack.contains(tubeName)) {
            tubesInRack.add(tubeName);
            tube.setInRack(true, tubesInRack.indexOf(tubeName), floatingRackX, floatingRackY);
        }
    }

    private void detachTubeFromRack(String tubeName) {
        tubesInRack.remove(tubeName);
        DraggableTube tube = tubes.get(tubeName);
        if (tube != null) {
            tube.setInRack(false, -1, 0, 0);
        }

        for (int i = 0; i < tubesInRack.size(); i++) {
            DraggableTube t = tubes.get(tubesInRack.get(i));
            if (t != null) {
                t.setRackPosition(i, floatingRackX, floatingRackY);
            }
        }
    }

    private void toggleFreezer() {
        freezerOpen = !freezerOpen;

        String imagePath = freezerOpen ?
                "/Images/LigationImage/equipment/freezer-open.svg" :
                "/Images/LigationImage/equipment/freezer-closed.svg";

        ImageView newView = SVGLoader.loadSVG(imagePath, 150, 200);
        newView.setLayoutX(freezerView.getLayoutX());
        newView.setLayoutY(freezerView.getLayoutY());
        newView.setCursor(Cursor.HAND);
        newView.setOnMouseClicked(e -> toggleFreezer());

        DropShadow shadow = new DropShadow();
        shadow.setColor(Color.rgb(0, 0, 0, 0.3));
        shadow.setRadius(5);
        newView.setEffect(shadow);

        labWorkspace.getChildren().remove(freezerView);
        freezerView = newView;
        labWorkspace.getChildren().add(freezerView);

        if (freezerOpen) {
            notifyStepComplete("7", "a");

            if (!tubesInFreezer.isEmpty()) {
                StringBuilder message = new StringBuilder("Tubes removed from freezer: ");
                for (int i = 0; i < tubesInFreezer.size(); i++) {
                    String tubeName = tubesInFreezer.get(i);
                    DraggableTube tube = tubes.get(tubeName);
                    if (tube != null) {
                        tube.getView().setVisible(true);

                        tube.getView().setLayoutX(tube.initialX);
                        tube.getView().setLayoutY(tube.initialY);
                    }
                    message.append(tubeName);
                    if (i < tubesInFreezer.size() - 1) {
                        message.append(", ");
                    }
                }
                showMessage(message.toString());
                tubesInFreezer.clear();
            }
        } else {

            for (String tubeName : tubesInFreezer) {
                DraggableTube tube = tubes.get(tubeName);
                if (tube != null) {
                    tube.getView().setVisible(false);
                }
            }
        }
    }

    private void toggleMicrocentrifuge() {
        microcentrifugeOpen = !microcentrifugeOpen;

        String imagePath = microcentrifugeOpen ?
                "/Images/LigationImage/equipment/Microcentrifuge.svg" :
                "/Images/LigationImage/equipment/microcentrifuge_body.svg";

        ImageView newView = SVGLoader.loadSVG(imagePath, 150, 130);
        newView.setLayoutX(microcentrifugeView.getLayoutX());
        newView.setLayoutY(microcentrifugeView.getLayoutY());
        newView.setCursor(Cursor.HAND);
        newView.setOnMouseClicked(e -> toggleMicrocentrifuge());

        DropShadow shadow = new DropShadow();
        shadow.setColor(Color.rgb(0, 0, 0, 0.3));
        shadow.setRadius(5);
        newView.setEffect(shadow);

        labWorkspace.getChildren().remove(microcentrifugeView);
        microcentrifugeView = newView;
        labWorkspace.getChildren().add(microcentrifugeView);

        if (microcentrifugeOpen) {
            notifyStepComplete("5", "a");
        } else {
            notifyStepComplete("5", "e");
        }
    }

    public void spinMicrocentrifuge() {
        RotateTransition rotate = new RotateTransition(Duration.seconds(3), microcentrifugeView);
        rotate.setByAngle(360);
        rotate.setCycleCount(2);
        rotate.setOnFinished(e -> {
            notifyStepComplete("5", "f");
            showMessage("Spin complete!");
        });
        rotate.play();
    }

    private void handleVortex() {
        DraggableTube ligTube = tubes.get("LIG");
        if (ligTube != null && isNear(ligTube.getX(), ligTube.getY(), VORTEX_X, VORTEX_Y, 80)) {
            ScaleTransition shake = new ScaleTransition(Duration.millis(100), vortexView);
            shake.setFromX(1.0);
            shake.setFromY(1.0);
            shake.setToX(1.05);
            shake.setToY(1.05);
            shake.setCycleCount(10);
            shake.setAutoReverse(true);
            shake.setOnFinished(e -> {
                notifyStepComplete("4", "c");
                showMessage("Solution mixed!");
            });
            shake.play();
        }
    }

    private boolean isNear(double x1, double y1, double x2, double y2, double threshold) {
        double distance = Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
        return distance < threshold;
    }

    private boolean isInVerticalColumn(double pipetteX, double pipetteY, double targetX, double targetY, double xThreshold) {

        boolean xMatch = Math.abs(pipetteX - targetX) < xThreshold;

        boolean yAbove = pipetteY <= targetY;
        return xMatch && yAbove;
    }

    private void notifyStepComplete(String step, String subStep) {
        if (controller != null) {
            controller.highlightNextInstruction(step, subStep);
        }
    }

    private void showMessage(String message) {
        showToast(message, "info", 4);
    }

    private void showToast(String message, String type, int seconds) {

        String icon = switch (type) {
            case "success" -> "✓";
            case "warning" -> "⚠";
            case "error"   -> "✕";
            default        -> "ℹ";
        };

        String accent = switch (type) {
            case "success" -> "rgba(34, 197, 94";
            case "warning" -> "rgba(245, 158, 11";
            case "error"   -> "rgba(239, 68, 68";
            default        -> "rgba(34, 211, 238";
        };

        Label iconLabel = new Label(icon);
        iconLabel.setStyle(
                "-fx-font-size: 15px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: " + accent + ", 1.0);" +
                        "-fx-background-color: " + accent + ", 0.15);" +
                        "-fx-background-radius: 50;" +
                        "-fx-min-width: 32px;" +
                        "-fx-min-height: 32px;" +
                        "-fx-max-width: 32px;" +
                        "-fx-max-height: 32px;" +
                        "-fx-alignment: center;"
        );

        Label msgLabel = new Label(message);
        msgLabel.setFont(Font.font("DM Sans Medium", 14));
        msgLabel.setStyle("-fx-text-fill: #ecfeff;");
        msgLabel.setWrapText(true);
        msgLabel.setMaxWidth(320);

        Label closeBtn = new Label("✕");
        closeBtn.setStyle(
                "-fx-text-fill: rgba(207,250,254,0.50);" +
                        "-fx-font-size: 12px;" +
                        "-fx-cursor: hand;" +
                        "-fx-padding: 0 0 0 8;"
        );

        HBox row = new HBox(12, iconLabel, msgLabel, closeBtn);
        row.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(msgLabel, javafx.scene.layout.Priority.ALWAYS);

        HBox toast = new HBox(row);
        toast.setAlignment(Pos.CENTER_LEFT);
        toast.setStyle(
                "-fx-background-color: rgba(4, 14, 20, 0.96);" +
                        "-fx-background-radius: 12;" +
                        "-fx-border-color: " + accent + ", 0.35);" +
                        "-fx-border-width: 1;" +
                        "-fx-border-radius: 12;" +
                        "-fx-padding: 14 18 14 14;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.55), 24, 0.3, 0, 6)," +
                        "            dropshadow(gaussian, " + accent + ", 0.25), 18, 0.2, 0, 0);"
        );
        toast.setPrefWidth(380);
        toast.setMaxWidth(380);

        double workspaceWidth = labWorkspace.getPrefWidth();
        toast.setLayoutX((workspaceWidth - 380) / 2.0);
        toast.setLayoutY(-80);

        labWorkspace.getChildren().add(toast);

        TranslateTransition slideIn = new TranslateTransition(Duration.millis(380), toast);
        slideIn.setFromY(-80);
        slideIn.setToY(80);
        slideIn.setInterpolator(Interpolator.EASE_OUT);

        TranslateTransition slideOut = new TranslateTransition(Duration.millis(320), toast);
        slideOut.setFromY(80);
        slideOut.setToY(-120);
        slideOut.setInterpolator(Interpolator.EASE_IN);
        slideOut.setOnFinished(e -> labWorkspace.getChildren().remove(toast));

        Timeline autoClose = new Timeline(
                new KeyFrame(Duration.seconds(seconds), e -> slideOut.play())
        );

        closeBtn.setOnMouseClicked(e -> {
            autoClose.stop();
            slideOut.play();
        });

        slideIn.setOnFinished(e -> autoClose.play());
        slideIn.play();
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

        private double currentVolume;
        private double minVolume;
        private double maxVolume;
        private Stage volumeDialog;

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

            setVolumeLimits();

            createView();
        }

        private void setVolumeLimits() {
            switch (type) {
                case "P2":
                    minVolume = 0.2;
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

            bodyView = SVGLoader.loadSVG(noTipImagePath, 30, 200);
            plungerView = SVGLoader.loadSVG(plungerPath, 20, 30);

            plungerView.setLayoutX(5);
            plungerView.setLayoutY(3);

            bodyView.setLayoutX(0);
            bodyView.setLayoutY(15);

            String volumeText = (currentVolume == (int) currentVolume) ?
                    (int)currentVolume + "μL" :
                    String.format("%.1f", currentVolume) + "μL";
            volumeBadge = new Label(volumeText);
            volumeBadge.setFont(Font.font("DM Sans Medium", FontWeight.BOLD, 14));
            volumeBadge.setTextFill(Color.WHITE);
            volumeBadge.setStyle("-fx-background-color: #54BCE3; -fx-padding: 5px 10px; -fx-background-radius: 15px;");
            volumeBadge.setLayoutX(-60);
            volumeBadge.setLayoutY(100);
            volumeBadge.setVisible(false);
            volumeBadge.setCursor(Cursor.HAND);

            volumeBadge.setOnMouseClicked(e -> {
                openVolumeDialog();
                e.consume();
            });

            ejectButton = new Button("Eject Tip");
            ejectButton.setFont(Font.font("DM Sans Medium", FontWeight.BOLD, 12));
            ejectButton.setStyle("-fx-background-color: #E74C3C; -fx-text-fill: white; " +
                    "-fx-padding: 8px 15px; -fx-background-radius: 15px; " +
                    "-fx-cursor: hand;");
            ejectButton.setLayoutX(-70);
            ejectButton.setLayoutY(140);
            ejectButton.setVisible(false);
            ejectButton.setCursor(Cursor.HAND);

            ejectButton.setOnAction(e -> {
                ejectTip();
                showMessage("Tip ejected from " + type + " pipette!");
                notifyStepComplete("3", "f");
                ejectButton.setVisible(false);
                e.consume();
            });

            pipetteGroup = new Group(bodyView, plungerView, volumeBadge, ejectButton);
            pipetteGroup.setLayoutX(x);
            pipetteGroup.setLayoutY(y);
            pipetteGroup.setCursor(Cursor.HAND);

            DropShadow shadow = new DropShadow();
            shadow.setColor(Color.rgb(0, 0, 0, 0.3));
            shadow.setRadius(5);
            pipetteGroup.setEffect(shadow);

            plungerView.setOnMouseClicked(e -> {
                if (e.getClickCount() == 2) {
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

                if (currentlyActivePipette != null && currentlyActivePipette != DraggablePipette.this) {
                    currentlyActivePipette.hideVolumeBadge();
                }

                currentlyActivePipette = DraggablePipette.this;

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

            if (isInVerticalColumn(x, y, tipBoxX, tipBoxY, 80) &&
                    tipBoxStates.get(type) &&
                    !hasTip) {

                attachTip();
                showMessage("Tip attached to " + type + " pipette!");
                notifyStepComplete("2", "d");
            }

            if (!isInVerticalColumn(x, y, TRASH_X, TRASH_Y, 80)) {
                ejectButton.setVisible(false);
            }

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
            hasSolution = false;
            updatePipetteImage();
        }

        private void updatePipetteImage() {
            String imagePath;

            if (hasTip && hasSolution) {
                imagePath = withTipWithSolutionPath;
            } else if (hasTip && !hasSolution) {
                imagePath = withTipImagePath;
            } else {
                imagePath = noTipImagePath;
            }

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

            String overTube = getTubeUnderPipette();

            if (overTube == null) {
                showMessage("Position pipette over a tube before drawing/dispensing solution!");
                return;
            }

            if (!hasSolution) {

                hasSolution = true;
                updatePipetteImage();
                showMessage("Solution drawn from " + overTube + " tube into " + type + " pipette!");
            } else {

                hasSolution = false;
                updatePipetteImage();
                showMessage("Solution dispensed from " + type + " pipette into " + overTube + " tube!");
            }
        }

        private String getTubeUnderPipette() {

            for (Map.Entry<String, DraggableTube> entry : tubes.entrySet()) {
                DraggableTube tube = entry.getValue();

                if (isInVerticalColumn(x, y, tube.getX(), tube.getY(), 40)) {

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

            Label titleLabel = new Label("Turn the top to change the volume.");
            titleLabel.setFont(Font.font("DM Sans Medium", FontWeight.BOLD, 14));
            titleLabel.setTextFill(Color.WHITE);
            titleLabel.setStyle("-fx-background-color: #1a5f7a; -fx-padding: 10px;");
            titleLabel.setMaxWidth(Double.MAX_VALUE);

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

            Button backButton = new Button("← Back");
            backButton.setStyle("-fx-background-color: white; -fx-text-fill: #1a5f7a; -fx-font-size: 14px; -fx-padding: 10px 20px; -fx-border-color: #1a5f7a; -fx-border-width: 2px;");
            backButton.setOnAction(e -> {

                try {
                    double newVolume = Double.parseDouble(volumeField.getText());
                    if (newVolume >= minVolume && newVolume <= maxVolume) {
                        currentVolume = newVolume;

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

            plusButton.setOnAction(e -> {
                try {
                    double vol = Double.parseDouble(volumeField.getText());

                    if (type.equals("P2")) {
                        vol += 0.2;
                    } else if (type.equals("P20")) {
                        vol += 2;
                    } else {
                        vol += 5;
                    }

                    if (vol <= maxVolume) {

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

            minusButton.setOnAction(e -> {
                try {
                    double vol = Double.parseDouble(volumeField.getText());

                    if (type.equals("P2")) {
                        vol -= 0.2;
                    } else if (type.equals("P20")) {
                        vol -= 2;
                    } else {
                        vol -= 5;
                    }

                    if (vol >= minVolume) {

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

        private void hideVolumeBadge() {
            volumeBadge.setVisible(false);
            ejectButton.setVisible(false);
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
        private double initialX, initialY;
        private boolean isOpen = false;
        private TubeImagePaths imagePaths;

        private boolean inRack = false;
        private int rackPosition = -1;
        private double rackBaseX = 0;
        private double rackBaseY = 0;

        DraggableTube(String name, double x, double y, TubeImagePaths imagePaths) {
            this.name = name;
            this.x = x;
            this.y = y;
            this.initialX = x;
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

            ImageView tubeImage = SVGLoader.loadSVG(imagePaths.closedImagePath, 50, 70);

            Label label = new Label(name);
            label.setFont(Font.font("DM Sans Medium", FontWeight.BOLD, 10));
            label.setTextFill(Color.WHITE);

            view.getChildren().addAll(tubeImage, label);

            makeTubeDraggable();
        }

        private void toggleTube() {

            isOpen = !isOpen;

            TubeState state = tubeStates.get(name);
            if (state != null) {
                state.isOpen = isOpen;
            }

            String imagePath = isOpen ? imagePaths.openImagePath : imagePaths.closedImagePath;

            ImageView newTubeImage = SVGLoader.loadSVG(imagePath, 50, 70);
            view.getChildren().set(0, newTubeImage);

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
            final boolean[] isDragging = {false};

            view.setOnMousePressed(e -> {
                dragDelta.x = view.getLayoutX() - e.getSceneX();
                dragDelta.y = view.getLayoutY() - e.getSceneY();
                isDragging[0] = false;
                e.consume();
            });

            view.setOnMouseDragged(e -> {
                isDragging[0] = true;
                view.setCursor(Cursor.MOVE);

                if (inRack) {
                    detachTubeFromRack(name);
                }

                x = e.getSceneX() + dragDelta.x;
                y = e.getSceneY() + dragDelta.y;
                view.setLayoutX(x);
                view.setLayoutY(y);
                e.consume();
            });

            view.setOnMouseReleased(e -> {
                view.setCursor(Cursor.HAND);

                if (!isDragging[0] && e.getClickCount() == 1) {
                    toggleTube();
                }

                checkTubeInteractions();

                if (isNear(x, y, floatingRackX, floatingRackY, 80) && tubesInRack.size() < 3) {
                    attachTubeToRack(name, this);
                }

                if (isNear(x, y, MICROCENTRIFUGE_X, MICROCENTRIFUGE_Y, 100) && microcentrifugeOpen) {
                    if (tubesInMicrocentrifuge.size() < 6) {
                        tubesInMicrocentrifuge.add(name);
                        view.setVisible(false);
                        showMessage("Tube placed in microcentrifuge!");
                    } else {
                        showMessage("Microcentrifuge is full (max 6 tubes)!");
                    }
                }

                if (isNear(x, y, FREEZER_X, FREEZER_Y, 100) && freezerOpen) {
                    tubesInFreezer.add(name);
                    view.setVisible(false);
                    showMessage("Tube placed in freezer!");
                }

                e.consume();
            });
        }

        private void checkTubeInteractions() {
            if (isNear(x, y, WATER_BATH_X, WATER_BATH_Y, 120) && waterBathOpen) {
                if (name.equals("A+") || name.equals("K+")) {
                    notifyStepComplete("1", "b");
                }
            }

            if (isNear(x, y, MICROCENTRIFUGE_X, MICROCENTRIFUGE_Y, 100) && microcentrifugeOpen) {
                if (name.equals("LIG")) {
                    notifyStepComplete("5", "b");
                } else if (name.equals("BAL")) {
                    notifyStepComplete("5", "d");
                }
            }

            if (isNear(x, y, FREEZER_X, FREEZER_Y, 120) && freezerOpen) {
                if (name.equals("A+") || name.equals("K+")) {
                    notifyStepComplete("7", "b");
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

        public void setInRack(boolean inRack, int position, double rackX, double rackY) {
            this.inRack = inRack;
            this.rackPosition = position;
            this.rackBaseX = rackX;
            this.rackBaseY = rackY;

            if (inRack) {

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
}