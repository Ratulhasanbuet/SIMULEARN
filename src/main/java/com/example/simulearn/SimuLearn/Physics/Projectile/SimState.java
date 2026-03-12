package com.example.simulearn.SimuLearn.Physics.Projectile;

import java.util.ArrayList;
import java.util.List;

public class SimState {

    public ProjectileMode mode = ProjectileMode.BASKETBALL;

    // Ball kinematics
    public double x, y, vx, vy, t;
    public boolean running, landed, scored;

    // Score tracking
    public int score, attempts;

    // Trail points  [x, y]
    public final List<double[]> trail = new ArrayList<>();

    // World geometry (ft)
    public double launchX, launchY;
    public double hoopX,   hoopY;
    public double hoopDist;
    public double targetH, targetW, targetDepth;

    // Live telemetry
    public double telSpeed, telHeight, telDist, telTime, telDrag, telAngle;

    public SimState() { reset(); }

    public void reset() {
        x = launchX; y = launchY;
        vx = vy = t = 0;
        running = landed = scored = false;
        trail.clear();
        telSpeed = telHeight = telDist = telTime = telDrag = telAngle = 0;
    }
}
