package com.example.simulearn.SimuLearn.Biology.MicropipettingSolution;

import com.example.simulearn.SimuLearn.Delta;
import com.example.simulearn.SimuLearn.SVGLoader;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
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
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
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

    private Map<String, TubeState> tubeStates = new HashMap<>();

    private Map<String, DraggablePipette> pipettes = new HashMap<>();

    private Map<String, ImageView> tipBoxViews = new HashMap<>();
    private Map<String, Boolean> tipBoxStates = new HashMap<>();

    private DraggablePipette currentlyActivePipette = null;

    private BlottingPaper blottingPaper;

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
        cabinetBody.setArcWidth(4);
        cabinetBody.setArcHeight(4);
        cabinetBody.setEffect(new DropShadow(8, 3, 3, Color.rgb(0, 0, 0, 0.5)));

        Rectangle cabinetInner = new Rectangle(68, TABLE_Y + 24, 284, 176);
        cabinetInner.setFill(new LinearGradient(0, 0, 1, 0, true, CycleMethod.NO_CYCLE,
                new Stop(0.0, Color.web("#5a6a78")),
                new Stop(0.5, Color.web("#6e7e8c")),
                new Stop(1.0, Color.web("#4e5e6c"))));
        cabinetInner.setArcWidth(3);
        cabinetInner.setArcHeight(3);
        labWorkspace.getChildren().addAll(cabinetBody, cabinetInner);

        double dX = 72, dW = 276;
        double d1Y = TABLE_Y + 28;

        Rectangle drawer1 = new Rectangle(dX, d1Y, dW, 78);
        drawer1.setFill(new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0.0, Color.web("#8a9aaa")),
                new Stop(0.5, Color.web("#7a8a98")),
                new Stop(1.0, Color.web("#6a7a88"))));
        drawer1.setArcWidth(4);
        drawer1.setArcHeight(4);
        drawer1.setStroke(Color.web("#4a5a68"));
        drawer1.setStrokeWidth(1);
        drawer1.setEffect(new InnerShadow(4, 0, 2, Color.rgb(0, 0, 0, 0.3)));
        Rectangle d1Hi = new Rectangle(dX + 2, d1Y + 1, dW - 4, 3);
        d1Hi.setFill(Color.web("#aabbcc", 0.4));
        d1Hi.setArcWidth(3);
        d1Hi.setArcHeight(3);
        Rectangle h1 = new Rectangle(dX + dW / 2 - 35, d1Y + 32, 70, 14);
        h1.setFill(new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0.0, Color.web("#c8d4de")), new Stop(0.4, Color.web("#a0b0bc")), new Stop(1.0, Color.web("#788898"))));
        h1.setArcWidth(7);
        h1.setArcHeight(7);
        h1.setStroke(Color.web("#5a6a78"));
        h1.setStrokeWidth(1);
        h1.setEffect(new DropShadow(3, 1, 2, Color.rgb(0, 0, 0, 0.4)));
        Rectangle s1L = new Rectangle(dX + dW / 2 - 28, d1Y + 36, 6, 6);
        s1L.setFill(Color.web("#9aaabb"));
        s1L.setArcWidth(3);
        s1L.setArcHeight(3);
        Rectangle s1R = new Rectangle(dX + dW / 2 + 22, d1Y + 36, 6, 6);
        s1R.setFill(Color.web("#9aaabb"));
        s1R.setArcWidth(3);
        s1R.setArcHeight(3);
        Rectangle gap = new Rectangle(dX, d1Y + 78, dW, 4);
        gap.setFill(Color.web("#3a4a58"));

        double d2Y = d1Y + 82;
        Rectangle drawer2 = new Rectangle(dX, d2Y, dW, 78);
        drawer2.setFill(new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0.0, Color.web("#8a9aaa")),
                new Stop(0.5, Color.web("#7a8a98")),
                new Stop(1.0, Color.web("#6a7a88"))));
        drawer2.setArcWidth(4);
        drawer2.setArcHeight(4);
        drawer2.setStroke(Color.web("#4a5a68"));
        drawer2.setStrokeWidth(1);
        drawer2.setEffect(new InnerShadow(4, 0, 2, Color.rgb(0, 0, 0, 0.3)));
        Rectangle d2Hi = new Rectangle(dX + 2, d2Y + 1, dW - 4, 3);
        d2Hi.setFill(Color.web("#aabbcc", 0.4));
        d2Hi.setArcWidth(3);
        d2Hi.setArcHeight(3);
        Rectangle h2 = new Rectangle(dX + dW / 2 - 35, d2Y + 32, 70, 14);
        h2.setFill(new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0.0, Color.web("#c8d4de")), new Stop(0.4, Color.web("#a0b0bc")), new Stop(1.0, Color.web("#788898"))));
        h2.setArcWidth(7);
        h2.setArcHeight(7);
        h2.setStroke(Color.web("#5a6a78"));
        h2.setStrokeWidth(1);
        h2.setEffect(new DropShadow(3, 1, 2, Color.rgb(0, 0, 0, 0.4)));
        Rectangle s2L = new Rectangle(dX + dW / 2 - 28, d2Y + 36, 6, 6);
        s2L.setFill(Color.web("#9aaabb"));
        s2L.setArcWidth(3);
        s2L.setArcHeight(3);
        Rectangle s2R = new Rectangle(dX + dW / 2 + 22, d2Y + 36, 6, 6);
        s2R.setFill(Color.web("#9aaabb"));
        s2R.setArcWidth(3);
        s2R.setArcHeight(3);

        labWorkspace.getChildren().addAll(drawer1, d1Hi, h1, s1L, s1R, gap,
                drawer2, d2Hi, h2, s2L, s2R);

        Rectangle benchWall = new Rectangle(0, TABLE_Y + 24, 1515, 182);
        benchWall.setFill(new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0.0, Color.web("#7a8a98")), new Stop(1.0, Color.web("#5a6878"))));
        Rectangle benchEdge = new Rectangle(0, TABLE_Y + 18, 1515, 6);
        benchEdge.setFill(new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0.0, Color.web("#6a7a88")), new Stop(1.0, Color.web("#4a5a68"))));
        Rectangle benchSurface = new Rectangle(0, TABLE_Y, 1515, 18);
        benchSurface.setFill(new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0.0, Color.web("#d8e2ea")), new Stop(0.3, Color.web("#c2cdd8")),
                new Stop(0.7, Color.web("#b0bcc8")), new Stop(1.0, Color.web("#8a9aaa"))));
        benchSurface.setEffect(new DropShadow(12, 0, 4, Color.rgb(0, 0, 0, 0.55)));
        Rectangle benchHi = new Rectangle(0, TABLE_Y, 1515, 2);
        benchHi.setFill(Color.web("#eef4f8", 0.7));
        labWorkspace.getChildren().addAll(benchWall, benchEdge, benchSurface, benchHi);

        Rectangle legL = new Rectangle(60, TABLE_Y + 18, 18, 188);
        legL.setFill(new LinearGradient(0, 0, 1, 0, true, CycleMethod.NO_CYCLE,
                new Stop(0.0, Color.web("#7a8a98")), new Stop(0.3, Color.web("#9aaaba")), new Stop(1.0, Color.web("#5a6a78"))));
        Rectangle legR = new Rectangle(1437, TABLE_Y + 18, 18, 188);
        legR.setFill(new LinearGradient(0, 0, 1, 0, true, CycleMethod.NO_CYCLE,
                new Stop(0.0, Color.web("#7a8a98")), new Stop(0.3, Color.web("#9aaaba")), new Stop(1.0, Color.web("#5a6a78"))));
        Rectangle padL = new Rectangle(50, TABLE_Y + 196, 38, 10);
        padL.setFill(Color.web("#4a5a68"));
        padL.setArcWidth(4);
        padL.setArcHeight(4);
        Rectangle padR = new Rectangle(1427, TABLE_Y + 196, 38, 10);
        padR.setFill(Color.web("#4a5a68"));
        padR.setArcWidth(4);
        padR.setArcHeight(4);
        labWorkspace.getChildren().addAll(legL, legR, padL, padR);

        for (int bx : new int[]{80, 380, 680, 980, 1200}) {
            Rectangle br = new Rectangle(bx, 323, 8, 30);
            br.setFill(Color.web("#8a9aaa"));
            labWorkspace.getChildren().add(br);
        }
        Rectangle shelfBody = new Rectangle(50, 323, 1200, 16);
        shelfBody.setFill(new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0.0, Color.web("#c2cdd8")), new Stop(0.4, Color.web("#aabbcc")), new Stop(1.0, Color.web("#7a8a98"))));
        shelfBody.setEffect(new DropShadow(6, 0, 3, Color.rgb(0, 0, 0, 0.4)));
        Rectangle shelfHi = new Rectangle(50, 323, 1200, 2);
        shelfHi.setFill(Color.web("#e8f0f8", 0.5));
        Rectangle shelfEdge = new Rectangle(50, 339, 1200, 4);
        shelfEdge.setFill(Color.web("#5a6a78"));
        labWorkspace.getChildren().addAll(shelfBody, shelfHi, shelfEdge);

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

        pipetteStandView = createEquipmentImage("/Images/MSImages/PipetteRack.svg",
                180, 220, PIPETTE_STAND_X, PIPETTE_STAND_Y);
    }

    private void createTubes() {

        Map<String, TubeImagePaths> tubeImageMap = new HashMap<>();

        tubeImageMap.put("Red Dye", new TubeImagePaths(
                "/Images/MSImages/RedDye-close.svg",
                "/Images/MSImages/RedDye-open.svg"
        ));
        DraggableTube tube = new DraggableTube("Red Dye", TUBE_RACK_X + 80, TUBE_RACK_Y + 5, tubeImageMap.get("Red Dye"));

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

        String type;
        if (message.toLowerCase().contains("attach") ||
                message.toLowerCase().contains("first") ||
                message.toLowerCase().contains("please") ||
                message.toLowerCase().contains("invalid") ||
                message.toLowerCase().contains("position") ||
                message.toLowerCase().contains("must be") ||
                message.toLowerCase().contains("cannot") ||
                message.toLowerCase().contains("warning")) {
            type = "warning";
        } else if (message.toLowerCase().contains("finish") ||
                message.toLowerCase().contains("complete") ||
                message.toLowerCase().contains("mixed") ||
                message.toLowerCase().contains("spin") ||
                message.toLowerCase().contains("drawn") ||
                message.toLowerCase().contains("dispensed") ||
                message.toLowerCase().contains("attached") ||
                message.toLowerCase().contains("ejected") ||
                message.toLowerCase().contains("placed") ||
                message.toLowerCase().contains("removed")) {
            type = "success";
        } else {
            type = "info";
        }
        showToast(message, type, 4);
    }

    private void showToast(String message, String type, int seconds) {

        String icon = switch (type) {
            case "success" -> "✓";
            case "warning" -> "⚠";
            case "error" -> "✕";
            default -> "ℹ";
        };

        String accent = switch (type) {
            case "success" -> "rgba(34, 197, 94";
            case "warning" -> "rgba(245, 158, 11";
            case "error" -> "rgba(239, 68, 68";
            default -> "rgba(34, 211, 238";
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

            Label nameLabel = new Label(type);
            nameLabel.setFont(Font.font("DM Sans Medium", FontWeight.BOLD, 12));
            nameLabel.setTextFill(Color.WHITE);
            nameLabel.setStyle("-fx-background-color: #FFA500; -fx-padding: 3px 8px; -fx-background-radius: 10px;");
            nameLabel.setLayoutX(-5);
            nameLabel.setLayoutY(-20);

            String volumeText = (currentVolume == (int) currentVolume) ?
                    (int) currentVolume + "μL" :
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

                if (controller != null) {

                    if (controller.getActiveStep() == 3 && controller.getActiveSubStep().equals("c")) {
                        controller.completeCurrentStep();
                    }
                }

                ejectButton.setVisible(false);
                e.consume();
            });

            pipetteGroup = new Group(bodyView, plungerView, nameLabel, volumeBadge, ejectButton);
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

                if (type.equals("P20") && controller != null) {

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

                if (type.equals("P20") && controller != null) {

                    if (controller.getActiveStep() == 1 && controller.getActiveSubStep().equals("d")) {
                        controller.completeCurrentStep();
                    }
                }
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

            String circlePosition = blottingPaper.getCircleAtPosition(x, y);
            if (circlePosition != null && hasSolution) {

                blottingPaper.dispenseVolume(circlePosition, currentVolume);
                hasSolution = false;
                updatePipetteImage();
                showMessage("Dispensed " + currentVolume + "µl onto circle " + circlePosition + "!");

                if (circlePosition.equals("A") && controller != null) {

                    if (controller.getActiveStep() == 3 && controller.getActiveSubStep().equals("a")) {
                        controller.completeCurrentStep();
                        controller.completeCurrentStep();
                    }
                }

                if (circlePosition.equals("B") && controller != null) {

                    if (controller.getActiveStep() == 4 && controller.getActiveSubStep().equals("a")) {
                        controller.completeCurrentStep();
                    }
                }

                if (circlePosition.equals("C") && controller != null) {

                    if (controller.getActiveStep() == 4 && controller.getActiveSubStep().equals("b")) {
                        controller.completeCurrentStep();
                    }
                }

                if (circlePosition.equals("D") && controller != null) {

                    if (controller.getActiveStep() == 4 && controller.getActiveSubStep().equals("c")) {
                        controller.completeCurrentStep();
                    }
                }

                return;
            }

            String overTube = getTubeUnderPipette();

            if (overTube == null && circlePosition == null) {
                showMessage("Position pipette over a tube or blotting paper!");
                return;
            }

            if (overTube != null) {

                if (!hasSolution) {

                    hasSolution = true;
                    updatePipetteImage();
                    showMessage("Solution drawn from " + overTube + " tube into " + type + " pipette!");

                    if (overTube.equals("Red Dye") && controller != null) {

                        if (controller.getActiveStep() == 2 && controller.getActiveSubStep().equals("b")) {
                            controller.completeCurrentStep();
                            controller.completeCurrentStep();
                        }
                    }
                } else {

                    hasSolution = false;
                    updatePipetteImage();
                    showMessage("Solution dispensed from " + type + " pipette into " + overTube + " tube!");
                }
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
                            volumeBadge.setText((int) newVolume + "μL");
                        } else {
                            volumeBadge.setText(String.format("%.1f", newVolume) + "μL");
                        }

                        if (type.equals("P20") && newVolume == 20.0 && controller != null) {

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

                if (type.equals("P20") && controller != null) {

                    if (controller.getActiveStep() == 3 && controller.getActiveSubStep().equals("d")) {
                        controller.completeCurrentStep();
                    }
                }
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

    private void createTipBoxes() {

        createTipBoxImage("/Images/MSImages/tipboxes/P2-sm-close.svg",
                "P2",
                "/Images/MSImages/tipboxes/P2-sm-open.svg",
                TIP_BOX_P2_X, TIP_BOX_P2_Y);
        addTipBoxLabel("P2", TIP_BOX_P2_X, TIP_BOX_P2_Y - 20);

        createTipBoxImage("/Images/MSImages/tipboxes/P20-sm-close.svg",
                "P20",
                "/Images/MSImages/tipboxes/P20-sm-open.svg",
                TIP_BOX_P20_X, TIP_BOX_P20_Y);
        addTipBoxLabel("P20", TIP_BOX_P20_X, TIP_BOX_P20_Y - 20);

        createTipBoxImage("/Images/MSImages/tipboxes/P200-sm-close.svg",
                "P200",
                "/Images/MSImages/tipboxes/P200-sm-open.svg",
                TIP_BOX_P200_X, TIP_BOX_P200_Y);
        addTipBoxLabel("P200", TIP_BOX_P200_X, TIP_BOX_P200_Y - 20);

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

        if (!isOpen && type.equals("P20") && controller != null) {

            if (controller.getActiveStep() == 1 && controller.getActiveSubStep().equals("c")) {
                controller.completeCurrentStep();
            }
        }

        if (isOpen && type.equals("P20") && controller != null) {

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

    private boolean isNear(double x1, double y1, double x2, double y2, double threshold) {
        double distance = Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
        return distance < threshold;
    }

    private boolean isInVerticalColumn(double pipetteX, double pipetteY, double targetX, double targetY, double xThreshold) {

        boolean xMatch = Math.abs(pipetteX - targetX) < xThreshold;

        boolean yAbove = pipetteY <= targetY;
        return xMatch && yAbove;
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
            label.setRotate(-90);

            view.getChildren().addAll(label, tubeImage);

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
            view.getChildren().set(1, newTubeImage);

            if (isOpen) {

                if (name.equals("Red Dye") && controller != null) {

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

                if (name.equals("Red Dye") && controller != null) {

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

            circleVolumes.put("A", 0.0);
            circleVolumes.put("B", 0.0);
            circleVolumes.put("C", 0.0);
            circleVolumes.put("D", 0.0);

            createView();
        }

        private void createView() {
            paperGroup = new Group();

            Rectangle paper = new Rectangle(400, 200);
            paper.setFill(Color.WHITE);
            paper.setStroke(Color.web("#333333"));
            paper.setStrokeWidth(2);
            paper.setArcWidth(10);
            paper.setArcHeight(10);

            DropShadow shadow = new DropShadow();
            shadow.setColor(Color.rgb(0, 0, 0, 0.3));
            shadow.setRadius(8);
            paper.setEffect(shadow);

            paperGroup.getChildren().add(paper);

            Label titleLabel = new Label("Blotting Paper");
            titleLabel.setFont(Font.font("DM Sans Medium", FontWeight.BOLD, 14));

            titleLabel.setStyle("-fx-text-fill: #333333;");
            titleLabel.setLayoutX(140);
            titleLabel.setLayoutY(10);
            paperGroup.getChildren().add(titleLabel);

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

            Circle outerCircle = new Circle(circleX, circleY, 35);
            outerCircle.setFill(Color.TRANSPARENT);
            outerCircle.setStroke(Color.BLACK);
            outerCircle.setStrokeWidth(2);

            Circle redCircle = new Circle(circleX, circleY, 0);
            redCircle.setFill(Color.RED);
            redCircles.put(label, redCircle);

            Label circleLabel = new Label(label);
            circleLabel.setFont(Font.font("DM Sans Medium", FontWeight.BOLD, 14));
            circleLabel.setStyle("-fx-text-fill: #333333;");
            circleLabel.setLayoutX(circleX - 7);
            circleLabel.setLayoutY(circleY + 45);

            Label volumeLabel = new Label("0.0µl");
            volumeLabel.setFont(Font.font("DM Sans Medium", FontWeight.NORMAL, 11));
            volumeLabel.setStyle("-fx-text-fill: #333333;");
            volumeLabel.setLayoutX(circleX - 15);
            volumeLabel.setLayoutY(circleY + 65);
            volumeLabels.put(label, volumeLabel);

            paperGroup.getChildren().addAll(outerCircle, redCircle, circleLabel, volumeLabel);
        }

        public Group getView() {
            return paperGroup;
        }

        public boolean isInCircleArea(double pipetteX, double pipetteY, String circleName) {

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

            return (Math.abs(pipetteX - circleX) < 40) && (pipetteY < y + 200) && (pipetteY > y - 100);
        }

        public String getCircleAtPosition(double pipetteX, double pipetteY) {

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

            double newVolume = circleVolumes.get(circleName) + volume;
            circleVolumes.put(circleName, newVolume);

            double radius = Math.min((newVolume / 20.0) * 35, 35);
            redCircles.get(circleName).setRadius(radius);

            String volumeText = (newVolume == (int) newVolume) ?
                    (int) newVolume + "µl" :
                    String.format("%.1f", newVolume) + "µl";
            volumeLabels.get(circleName).setText(volumeText);
        }

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

    public double getBlottingPaperRadius(String circleName) {
        return blottingPaper.getCircleRadius(circleName);
    }

}