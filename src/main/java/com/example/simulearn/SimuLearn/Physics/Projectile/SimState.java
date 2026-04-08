package com.example.simulearn.SimuLearn.Physics.Projectile;

import java.util.ArrayList;
import java.util.List;

public class SimState {

    public ProjectileMode mode = ProjectileMode.BASKETBALL;


    public double x, y, vx, vy, t;
    public boolean running, landed, scored;


    public int score, attempts;


    public final List<double[]> trail = new ArrayList<>();


    public double launchX, launchY;
    public double hoopX, hoopY;
    public double hoopDist;
    public double targetH, targetW, targetDepth;


    public double telSpeed, telHeight, telDist, telTime, telDrag, telAngle;

    public SimState() {
        reset();
    }

    public void reset() {
        x = launchX;
        y = launchY;
        vx = vy = t = 0;
        running = landed = scored = false;
        trail.clear();
        telSpeed = telHeight = telDist = telTime = telDrag = telAngle = 0;
    }
}
