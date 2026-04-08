package com.example.simulearn.SimuLearn.Physics.Projectile;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

import static com.example.simulearn.SimuLearn.Physics.Projectile.PhysicsConstants.*;

public class SceneRenderer {

    private final Canvas canvas;
    private final GraphicsContext gc;
    private final SimState state;

    private double ppf, originX, originY;

    public boolean showTrail = true, showVectors = true, showDims = true;


    private static final Color BG = Color.web("#0a0e14");
    private static final Color ACCENT = Color.web("#f97316");
    private static final Color ACCENT2 = Color.web("#38bdf8");
    private static final Color GREEN = Color.web("#22c55e");
    private static final Color RED = Color.web("#ef4444");
    private static final Color COURT = Color.web("#c8924a");
    private static final Color MUTED = Color.web("#64748b");


    private String resultText = "";
    private Color resultColor = GREEN;
    private double resultAlpha = 0;
    private long resultTimer = 0;

    public SceneRenderer(Canvas canvas, SimState state) {
        this.canvas = canvas;
        this.gc = canvas.getGraphicsContext2D();
        this.state = state;
    }


    public void recalc(double dist) {
        double W = canvas.getWidth(), H = canvas.getHeight();
        ppf = Math.min((W - 40) / (dist * 1.5 + 4), (H - 60) / 14.0);
        originX = W - ppf * 2;
        originY = H - 40;
    }

    private double wx(double x) {
        return originX + x * ppf;
    }

    private double wy(double y) {
        return originY - y * ppf;
    }


    public void draw(double angleDeg) {
        gc.setFill(BG);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        drawGround();

        switch (state.mode) {
            case BASKETBALL -> {
                drawBasketballCourt();
                drawHoop();
                drawBackboard();
            }
            case BASEBALL -> {
                drawBaseballField();
                drawBatter();
            }
            case HOCKEY -> {
                drawHockeyRink();
                drawGoal();
            }
        }

        if (showTrail) drawTrail();
        drawBall();
        drawLauncher(angleDeg);
        if (showVectors) drawVelocityVector();
        if (showDims) drawDimensions(angleDeg);
        drawResultOverlay();
    }


    private void drawGround() {
        double fy = wy(0), W = canvas.getWidth();
        gc.setFill(switch (state.mode) {
            case BASKETBALL -> Color.web("#1a1208");
            case BASEBALL -> Color.web("#1a2e0f");
            case HOCKEY -> Color.web("#0d1e2e");
        });
        gc.fillRect(0, fy, W, canvas.getHeight() - fy);

        gc.setStroke(switch (state.mode) {
            case BASKETBALL -> COURT;
            case BASEBALL -> Color.web("#2d5a1e");
            case HOCKEY -> Color.web("#1e4a6e");
        });
        gc.setLineWidth(2);
        gc.strokeLine(0, fy, W, fy);


        gc.setStroke(Color.web("#0f1820"));
        gc.setLineWidth(1);
        gc.setLineDashes(4, 6);
        for (int ft = -2; ft <= 60; ft += 2) {
            double gx = wx(-ft);
            if (gx > 0 && gx < W) gc.strokeLine(gx, 0, gx, fy);
        }
        for (int ft = 0; ft <= 16; ft += 2) {
            double gy = wy(ft);
            if (gy > 0 && gy < fy) gc.strokeLine(0, gy, W, gy);
        }
        gc.setLineDashes(null);
    }


    private void drawBasketballCourt() {
        double fy = wy(0), hoopPx = wx(state.hoopX), laneW = ppf * 12;
        gc.setStroke(Color.web("#2a1e0a"));
        gc.setLineWidth(1);
        gc.strokeRect(hoopPx - laneW * 0.5, fy - ppf * 15, laneW, ppf * 15);
    }

    private void drawHoop() {
        double hx = wx(state.hoopX), hy = wy(state.hoopY), hr = HOOP_R * ppf;
        gc.setStroke(Color.web("#ffffff44"));
        gc.setLineWidth(1);
        for (int i = 0; i < 8; i++) {
            double a = Math.PI + (i / 8.0) * Math.PI;
            gc.strokeLine(hx + Math.cos(a) * hr, hy, hx + Math.cos(a) * hr * 0.4, hy + ppf * 1.5);
        }
        gc.setStroke(Color.web("#ffffff33"));
        for (int i = 1; i <= 3; i++) {
            double ny = hy + ppf * 1.5 * i / 3.0, w = hr * (1 - .5 * i / 3.0);
            gc.strokeLine(hx - w, ny, hx + w, ny);
        }
        gc.setStroke(Color.web("#f9731644"));
        gc.setLineWidth(8);
        gc.strokeOval(hx - hr, hy - hr * .3, hr * 2, hr * .6);
        gc.setStroke(ACCENT);
        gc.setLineWidth(3.5);
        gc.strokeOval(hx - hr, hy - hr * .3, hr * 2, hr * .6);
        gc.setLineWidth(1);
    }

    private void drawBackboard() {
        double bx = wx(state.hoopX - .5), by = wy(state.hoopY + 2), bh = ppf * 3.5, bw = ppf * .3;
        gc.setFill(Color.web("#ffffff18"));
        gc.fillRect(bx, by, bw, bh);
        gc.setStroke(Color.web("#ffffff44"));
        gc.setLineWidth(1);
        gc.strokeRect(bx, by, bw, bh);
        gc.setStroke(MUTED);
        gc.setLineWidth(3);
        gc.strokeLine(bx + bw / 2, wy(0), bx + bw / 2, by + bh);
    }


    private void drawBaseballField() {
        double fy = wy(0);
        for (int i = 0; i < 6; i++) {
            double x1 = wx(-state.hoopDist * i / 5.0), x2 = wx(-state.hoopDist * (i + 1) / 5.0);
            gc.setFill(i % 2 == 0 ? Color.web("#1a2e0f") : Color.web("#1e3512"));
            gc.fillRect(Math.min(x1, x2), fy, Math.abs(x2 - x1), 6);
        }

        gc.setFill(Color.web("#8B6914"));
        gc.fillOval(wx(0) - 8, fy - 4, 16, 8);


        double tx = wx(state.hoopX), ty1 = wy(state.targetH), ty2 = wy(0), tw = ppf * state.targetW;
        gc.setFill(Color.web("#38bdf811"));
        gc.fillRect(tx - tw / 2, ty1, tw, ty2 - ty1);
        gc.setStroke(Color.web("#38bdf866"));
        gc.setLineWidth(1.5);
        gc.setLineDashes(4, 4);
        gc.strokeRect(tx - tw / 2, ty1, tw, ty2 - ty1);
        gc.setLineDashes(null);


        gc.setFill(Color.web("#e2e8f0"));
        gc.fillRect(tx - 5, fy - 4, 10, 4);

        gc.setFill(ACCENT2);
        gc.setFont(Font.font("Courier New", 10));
        gc.setTextAlign(TextAlignment.CENTER);
        gc.fillText("B", tx, ty1 - 6);
    }

    private void drawBatter() {
        double bx = wx(state.hoopX), fy = wy(0), figH = ppf * 2.2;
        double shoulder = fy - figH * .72, hip = fy - figH * .38, headR = figH * .09;
        gc.setFill(Color.web("#00000055"));
        gc.fillOval(bx - 10, fy - 3, 20, 6);
        gc.setStroke(Color.web("#1e3a5f"));
        gc.setLineWidth(Math.max(3, ppf * .09));
        gc.setLineCap(StrokeLineCap.ROUND);
        gc.strokeLine(bx - 2, hip, bx - 6, fy);
        gc.strokeLine(bx + 2, hip, bx + 5, fy);
        gc.setFill(Color.web("#2563eb"));
        gc.fillRoundRect(bx - 7, shoulder, 14, hip - shoulder, 4, 4);
        gc.setFill(Color.web("#d4a76a"));
        gc.fillOval(bx - headR, shoulder - headR * 2, headR * 2, headR * 2);
        gc.setFill(Color.web("#1e1e1e"));
        gc.fillArc(bx - headR, shoulder - headR * 2 - headR * .3, headR * 2, headR * 2, 0, 180, ArcType.CHORD);

        gc.setStroke(Color.web("#8B4513"));
        gc.setLineWidth(Math.max(2, ppf * .06));
        double batA = Math.toRadians(-50), batLen = figH * .45;
        gc.strokeLine(bx + 5, shoulder + 5, bx + 5 + batLen * Math.cos(batA), shoulder + 5 + batLen * Math.sin(batA));
        gc.setStroke(Color.web("#d4a76a"));
        gc.setLineWidth(Math.max(2, ppf * .07));
        gc.strokeLine(bx + 4, shoulder + 6, bx + 5, shoulder + 5);
        gc.setLineCap(StrokeLineCap.BUTT);
        gc.setFill(Color.web("#fbbf24"));
        gc.setFont(Font.font("Courier New", 10));
        gc.setTextAlign(TextAlignment.CENTER);
        gc.fillText("B", bx, fy + 14);
    }


    private void drawHockeyRink() {
        double fy = wy(0), W = canvas.getWidth();
        gc.setFill(Color.web("#b8d4e8"));
        gc.fillRect(0, fy - 3, W, 3);
        gc.setStroke(Color.web("#1d4ed8"));
        gc.setLineWidth(3);
        double blueX = wx(state.hoopX * .5);
        gc.strokeLine(blueX, fy, blueX, fy - 8);
        gc.setStroke(Color.web("#dc2626"));
        gc.setLineWidth(2);
        double centX = wx(-state.hoopDist * .5);
        gc.strokeLine(centX, fy, centX, fy - 8);
    }

    private void drawGoal() {
        double gx = wx(state.hoopX), gbx = wx(state.hoopX - state.targetDepth);
        double gtop = wy(state.targetH), gbot = wy(0);
        double gw = gx - gbx, gh = gbot - gtop;
        gc.setFill(Color.web("#38bdf811"));
        gc.fillRect(gbx, gtop, gw, gh);
        gc.setStroke(Color.web("#ffffff33"));
        gc.setLineWidth(0.8);
        for (int c = 0; c <= 8; c++) {
            double nx = gbx + gw * c / 8;
            gc.strokeLine(nx, gtop, nx, gbot);
        }
        for (int r = 0; r <= 5; r++) {
            double ny = gtop + gh * r / 5;
            gc.strokeLine(gbx, ny, gx, ny);
        }
        gc.setStroke(Color.web("#dc2626"));
        gc.setLineWidth(3.5);
        gc.strokeLine(gx, gtop, gx, gbot);
        gc.strokeLine(gx, gtop, gbx, gtop);
        gc.strokeLine(gbx, gtop, gbx, gbot);
        gc.setStroke(Color.web("#38bdf855"));
        gc.setLineWidth(8);
        gc.strokeLine(gx, gtop, gx, gbot);
        gc.setLineWidth(1);
        gc.setFill(ACCENT2);
        gc.setFont(Font.font("Courier New", 10));
        gc.setTextAlign(TextAlignment.CENTER);
        gc.fillText("C", gbx, gtop - 6);
        gc.fillText("D", gx, gtop - 6);
        gc.fillText("E", gx, gbot + 14);
        gc.fillText("B", gx - gw / 2, gbot + 14);
    }


    private void drawTrail() {
        int n = state.trail.size();
        if (n < 2) return;
        Color tc = switch (state.mode) {
            case BASKETBALL -> Color.web("#f97316");
            case BASEBALL -> Color.web("#e2e8f0");
            case HOCKEY -> Color.web("#38bdf8");
        };
        for (int i = 1; i < n; i++) {
            double a = (double) i / n;
            gc.setStroke(Color.color(tc.getRed(), tc.getGreen(), tc.getBlue(), a * .7));
            gc.setLineWidth(1.5);
            double[] p = state.trail.get(i - 1), p2 = state.trail.get(i);
            gc.strokeLine(wx(p[0]), wy(p[1]), wx(p2[0]), wy(p2[1]));
        }
    }


    private void drawBall() {
        double bx = wx(state.x), by = wy(state.y), br = BALL_R * ppf;
        gc.setFill(Color.web("#00000066"));
        gc.fillOval(bx - br * .9, wy(0) - br * .2, br * 1.8, br * .4);
        switch (state.mode) {
            case BASKETBALL -> {

                Color[] stops = {Color.web("#f97316"), Color.web("#ea6010"), Color.web("#c2440c"), Color.web("#7c2d08")};
                double[] radii = {1.0, .75, .5, .25};
                gc.setFill(Color.web("#f9731622"));
                gc.fillOval(bx - br * 1.3, by - br * 1.3, br * 2.6, br * 2.6);
                for (int i = stops.length - 1; i >= 0; i--) {
                    gc.setFill(stops[i]);
                    double r = br * radii[i];
                    gc.fillOval(bx - r, by - r, r * 2, r * 2);
                }
                gc.setStroke(Color.web("#7c3c0a"));
                gc.setLineWidth(1.2);
                gc.strokeOval(bx - br * .5, by - br, br, br * 2);
                gc.strokeLine(bx - br, by, bx + br, by);
                gc.setFill(Color.web("#ffffff33"));
                gc.fillOval(bx - br * .3, by - br * .6, br * .5, br * .4);
            }
            case BASEBALL -> {
                gc.setFill(Color.web("#f5f0e8"));
                gc.fillOval(bx - br, by - br, br * 2, br * 2);
                gc.setStroke(Color.web("#cc2222"));
                gc.setLineWidth(1.0);
                gc.strokeArc(bx - br * .3, by - br, br * .6, br * 2, 70, 40, ArcType.OPEN);
                gc.strokeArc(bx - br * .3, by - br, br * .6, br * 2, 250, 40, ArcType.OPEN);
                gc.setFill(Color.web("#ffffff55"));
                gc.fillOval(bx - br * .3, by - br * .6, br * .4, br * .3);
            }
            case HOCKEY -> {
                gc.setFill(Color.web("#1a1a1a"));
                gc.fillOval(bx - br * 1.4, by - br * .5, br * 2.8, br);
                gc.setStroke(Color.web("#444444"));
                gc.setLineWidth(1);
                gc.strokeOval(bx - br * 1.4, by - br * .5, br * 2.8, br);
                gc.setFill(Color.web("#ffffff22"));
                gc.fillOval(bx - br, by - br * .4, br * .8, br * .3);
            }
        }
    }


    private void drawLauncher(double angleDeg) {
        switch (state.mode) {
            case BASKETBALL -> drawBasketballPlayer(angleDeg);
            case BASEBALL -> drawPitcher(angleDeg);
            case HOCKEY -> drawHockeyPlayer(angleDeg);
        }
    }

    private void drawBasketballPlayer(double angleDeg) {
        double px = wx(state.launchX), py = wy(state.launchY), fy = wy(0);
        double armA = -Math.toRadians(angleDeg), armL = ppf * .5, hr = ppf * .2;
        gc.setFill(Color.web("#00000055"));
        gc.fillOval(px - 12, fy - 4, 24, 8);
        LinearGradient tg = new LinearGradient(0, 0, 1, 0, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.web("#dc2626")), new Stop(1, Color.web("#7f1d1d")));
        gc.setFill(tg);
        gc.fillRect(px - 9, wy(state.launchY * .45), 18, fy - wy(state.launchY * .45));
        gc.setFill(Color.WHITE);
        gc.setFont(Font.font("DM Sans Medium", FontWeight.BOLD, Math.max(10, ppf * .25)));
        gc.setTextAlign(TextAlignment.CENTER);
        gc.fillText("23", px, wy(state.launchY * .2));
        gc.setFill(Color.web("#d4a76a"));
        gc.fillOval(px - hr, wy(state.launchY * .78) - hr, hr * 2, hr * 2);
        gc.setStroke(Color.web("#d4a76a"));
        gc.setLineWidth(Math.max(3, ppf * .1));
        gc.setLineCap(StrokeLineCap.ROUND);
        gc.strokeLine(px - 5, wy(state.launchY * .6), px + armL * Math.cos(Math.PI + armA * .7), wy(state.launchY * .6) + armL * Math.sin(armA * .7));
        gc.setStroke(Color.web("#1e3a5f"));
        gc.setLineWidth(Math.max(4, ppf * .13));
        gc.strokeLine(px, wy(state.launchY * .15), px - 8, fy);
        gc.strokeLine(px, wy(state.launchY * .15), px + 6, fy);
        gc.setLineCap(StrokeLineCap.BUTT);
        gc.setStroke(Color.web("#fbbf24"));
        gc.setLineWidth(2);
        gc.setLineDashes(2, 2);
        gc.strokeOval(px - 4, py - 4, 8, 8);
        gc.setLineDashes(null);
        gc.setFill(Color.web("#fbbf24"));
        gc.setFont(Font.font("Courier New", 10));
        gc.fillText("A", px + 12, py + 4);
    }

    private void drawPitcher(double angleDeg) {
        double px = wx(state.launchX), fy = wy(0), figH = ppf * 2.0;
        double shoulder = fy - figH * .72, hip = fy - figH * .38, headR = figH * .09;
        gc.setFill(Color.web("#00000055"));
        gc.fillOval(px - 10, fy - 3, 20, 6);
        gc.setStroke(Color.web("#1e3a5f"));
        gc.setLineWidth(Math.max(3, ppf * .09));
        gc.setLineCap(StrokeLineCap.ROUND);
        gc.strokeLine(px - 2, hip, px - 8, fy);
        gc.strokeLine(px + 2, hip, px + 5, fy);
        LinearGradient tg = new LinearGradient(0, 0, 1, 0, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.web("#dc2626")), new Stop(1, Color.web("#7f1d1d")));
        gc.setFill(tg);
        gc.fillRoundRect(px - 7, shoulder, 14, hip - shoulder, 4, 4);
        gc.setFill(Color.WHITE);
        gc.setFont(Font.font("DM Sans Medium", FontWeight.BOLD, 8));
        gc.setTextAlign(TextAlignment.CENTER);
        gc.fillText("P", px, shoulder + (hip - shoulder) * .6);
        gc.setFill(Color.web("#d4a76a"));
        gc.fillOval(px - headR, shoulder - headR * 2, headR * 2, headR * 2);
        gc.setFill(Color.web("#1e3a5f"));
        gc.fillArc(px - headR, shoulder - headR * 2 - headR * .2, headR * 2, headR * 2, 0, 180, ArcType.CHORD);
        double armA = -Math.toRadians(angleDeg), armLen = figH * .32;
        gc.setStroke(Color.web("#d4a76a"));
        gc.setLineWidth(Math.max(2, ppf * .08));
        gc.strokeLine(px - 4, shoulder + 6, px - 4 + armLen * Math.cos(Math.PI + armA * .7), shoulder + 6 + armLen * Math.sin(armA * .7));
        gc.setLineCap(StrokeLineCap.BUTT);
        double py = wy(state.launchY);
        gc.setStroke(Color.web("#fbbf24"));
        gc.setLineWidth(2);
        gc.setLineDashes(2, 2);
        gc.strokeOval(px - 4, py - 4, 8, 8);
        gc.setLineDashes(null);
        gc.setFill(Color.web("#fbbf24"));
        gc.setFont(Font.font("Courier New", 10));
        gc.fillText("A", px + 12, py + 4);
    }

    private void drawHockeyPlayer(double angleDeg) {
        double px = wx(state.launchX), fy = wy(0), figH = ppf * 2.0;
        double shoulder = fy - figH * .72, hip = fy - figH * .38, headR = figH * .09;
        double stickMidY = wy(Math.max(state.launchY * .5, .2));
        gc.setFill(Color.web("#00000055"));
        gc.fillOval(px - 10, fy - 3, 20, 6);
        gc.setStroke(Color.web("#1e3a5f"));
        gc.setLineWidth(Math.max(3, ppf * .1));
        gc.setLineCap(StrokeLineCap.ROUND);
        gc.strokeLine(px - 2, hip, px - 10, fy);
        gc.strokeLine(px + 2, hip, px + 8, fy);
        gc.setStroke(Color.web("#94a3b8"));
        gc.setLineWidth(Math.max(2, ppf * .06));
        gc.strokeLine(px - 13, fy, px - 6, fy);
        gc.strokeLine(px + 5, fy, px + 12, fy);
        gc.setFill(Color.web("#f97316"));
        gc.fillRoundRect(px - 8, shoulder, 16, hip - shoulder, 4, 4);
        gc.setFill(Color.WHITE);
        gc.setFont(Font.font("DM Sans Medium", FontWeight.BOLD, 8));
        gc.setTextAlign(TextAlignment.CENTER);
        gc.fillText("99", px, shoulder + (hip - shoulder) * .6);
        gc.setFill(Color.web("#d4a76a"));
        gc.fillOval(px - headR, shoulder - headR * 2, headR * 2, headR * 2);
        gc.setFill(Color.web("#1e1e1e"));
        gc.fillArc(px - headR, shoulder - headR * 2 - headR * .3, headR * 2, headR * 2, 0, 200, ArcType.CHORD);
        double stickAngle = -Math.toRadians(angleDeg * .5 + 10), stickLen = figH * .9;
        double stickEndX = px - 6 + stickLen * Math.cos(Math.PI + stickAngle);
        double stickEndY = stickMidY + stickLen * Math.sin(stickAngle);
        gc.setStroke(Color.web("#8B4513"));
        gc.setLineWidth(Math.max(2, ppf * .06));
        gc.strokeLine(px - 6, stickMidY, stickEndX, stickEndY);
        gc.setStroke(Color.web("#475569"));
        gc.setLineWidth(Math.max(3, ppf * .1));
        gc.strokeLine(stickEndX, stickEndY, stickEndX - ppf * .4, fy);
        gc.setStroke(Color.web("#d4a76a"));
        gc.setLineWidth(Math.max(2, ppf * .07));
        gc.strokeLine(px - 5, shoulder + 8, px - 6, stickMidY);
        gc.setLineCap(StrokeLineCap.BUTT);
        double py = wy(state.launchY);
        gc.setStroke(Color.web("#fbbf24"));
        gc.setLineWidth(2);
        gc.setLineDashes(2, 2);
        gc.strokeOval(px - 4, py - 4, 8, 8);
        gc.setLineDashes(null);
        gc.setFill(Color.web("#fbbf24"));
        gc.setFont(Font.font("Courier New", 10));
        gc.fillText("A", px + 12, py + 4);
    }


    private void drawVelocityVector() {
        if (!state.running) return;
        double v = Math.sqrt(state.vx * state.vx + state.vy * state.vy);
        if (v < .5) return;
        double bx = wx(state.x), by = wy(state.y), sc = ppf * .25;
        double ex = bx + state.vx * sc, ey = by - state.vy * sc;
        gc.setStroke(ACCENT2);
        gc.setLineWidth(2);
        gc.setLineDashes(4, 3);
        gc.strokeLine(bx, by, ex, by);
        gc.setStroke(GREEN);
        gc.strokeLine(ex, by, ex, ey);
        gc.setLineDashes(null);
        gc.setStroke(ACCENT);
        gc.setLineWidth(2.5);
        gc.strokeLine(bx, by, ex, ey);
        double ang = Math.atan2(ey - by, ex - bx), ah = 8;
        gc.setFill(ACCENT);
        gc.fillPolygon(new double[]{ex, ex - ah * Math.cos(ang - .4), ex - ah * Math.cos(ang + .4)},
                new double[]{ey, ey - ah * Math.sin(ang - .4), ey - ah * Math.sin(ang + .4)}, 3);
    }


    private void drawDimensions(double angleDeg) {
        double lx = wx(state.launchX), ly = wy(state.launchY), hx = wx(state.hoopX), fy = wy(0);
        gc.setFont(Font.font("Courier New", 11));
        gc.setTextAlign(TextAlignment.CENTER);


        drawArrow(hx, wy(-0.8), lx, wy(-0.8), ACCENT2, String.format("%.1f ft", state.hoopDist));

        drawArrow(hx - state.targetW * ppf - 30, fy, hx - state.targetW * ppf - 30, wy(state.targetH),
                GREEN, String.format("%.1f ft", state.targetH));

        if (state.launchY > 0.5)
            drawArrow(lx + BALL_R * ppf + 35, fy, lx + BALL_R * ppf + 35, ly,
                    Color.web("#a78bfa"), String.format("%.1f ft", state.launchY));

        if (state.mode == ProjectileMode.HOCKEY && state.targetDepth > 0)
            drawArrow(hx, wy(state.targetH + .5), wx(state.hoopX - state.targetDepth), wy(state.targetH + .5),
                    Color.web("#f472b6"), String.format("%.1f ft", state.targetDepth));

        if (!state.running && !state.landed) {
            double arcR = 40;
            gc.setStroke(Color.web("#fbbf24"));
            gc.setLineWidth(1.5);
            gc.strokeArc(lx - arcR, ly - arcR, arcR * 2, arcR * 2, 180, angleDeg, ArcType.OPEN);
            gc.setFill(Color.web("#fbbf24"));
            gc.fillText(String.format("%.0f°", angleDeg), lx - arcR - 22, ly - arcR * .4);
        }
    }

    private void drawArrow(double x1, double y1, double x2, double y2, Color c, String lbl) {
        double ah = 6, ang = Math.atan2(y2 - y1, x2 - x1);
        gc.setStroke(c);
        gc.setFill(c);
        gc.setLineWidth(1.5);
        gc.setLineDashes(3, 3);
        gc.strokeLine(x1, y1, x2, y2);
        gc.setLineDashes(null);
        for (int t = 0; t <= 1; t++) {
            double tx = t == 1 ? x2 : x1, ty = t == 1 ? y2 : y1, a = t == 1 ? ang : ang + Math.PI;
            gc.fillPolygon(new double[]{tx, tx - ah * Math.cos(a - .4), tx - ah * Math.cos(a + .4)},
                    new double[]{ty, ty - ah * Math.sin(a - .4), ty - ah * Math.sin(a + .4)}, 3);
        }
        double mx = (x1 + x2) / 2, my = (y1 + y2) / 2, dx = x2 - x1, dy = y2 - y1, len = Math.sqrt(dx * dx + dy * dy);
        if (len > 30) {
            gc.setFill(c);
            gc.setFont(Font.font("Courier New", 11));
            gc.fillText(lbl, mx - dy / len * 14, my + dx / len * 14 + 4);
        }
    }


    public void showResult(boolean scored) {
        resultText = scored ? (state.mode.icon + " HIT!") : "✗ MISSED";
        resultColor = scored ? GREEN : RED;
        resultAlpha = 1.0;
        resultTimer = System.currentTimeMillis();
    }

    private void drawResultOverlay() {
        long elapsed = System.currentTimeMillis() - resultTimer;
        if (elapsed > 2500 || resultText.isEmpty()) {
            resultAlpha = 0;
            return;
        }
        if (elapsed > 2000) resultAlpha = 1.0 - (elapsed - 2000) / 500.0;
        double cx = canvas.getWidth() / 2, cy = canvas.getHeight() / 2;
        gc.setGlobalAlpha(resultAlpha);
        gc.setFill(resultColor);
        gc.setFont(Font.font("DM Sans Medium", FontWeight.BOLD, 52));
        gc.setTextAlign(TextAlignment.CENTER);
        gc.fillText(resultText, cx, cy);
        gc.setFill(MUTED);
        gc.setFont(Font.font("Courier New", 14));
        gc.fillText(String.format("flight: %.2fs", state.telTime), cx, cy + 30);
        gc.setGlobalAlpha(1.0);
    }
}