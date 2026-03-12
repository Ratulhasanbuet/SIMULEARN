package com.example.simulearn.SimuLearn.Physics.Projectile;

/**
 * The three simulation modes.
 * Units: feet (ft) and ft/s throughout.
 *
 * Baseball (P11.111):  14 m ≈ 46 ft, launch 0.6 m ≈ 2 ft, catch 0.68 m ≈ 2.2 ft
 * Hockey   (P11.113):  16 ft dist, goal 4 ft tall × 2.5 ft deep
 */
public enum ProjectileMode {

    BASKETBALL("🏀", "Basketball",
        24.0, 30.0, 16.0, 6.8,
        10.0, 1.5, 0.0),

    BASEBALL("⚾", "Baseball",
        50.0, 10.0, 46.0, 2.0,
        2.2,  1.5, 0.0),

    HOCKEY("🏒", "Hockey",
        35.0, 8.0, 16.0, 0.15,
        4.0,  3.0, 2.5);

    public final String icon, label;
    public final double defaultSpeed, defaultAngle, defaultDist, defaultLaunchH;
    public final double targetH, targetW, targetDepth;

    ProjectileMode(String icon, String label,
                   double speed, double angle, double dist, double launchH,
                   double targetH, double targetW, double targetDepth) {
        this.icon         = icon;
        this.label        = label;
        this.defaultSpeed = speed;
        this.defaultAngle = angle;
        this.defaultDist  = dist;
        this.defaultLaunchH = launchH;
        this.targetH      = targetH;
        this.targetW      = targetW;
        this.targetDepth  = targetDepth;
    }
}
