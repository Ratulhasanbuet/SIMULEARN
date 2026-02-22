package com.example.simulearn;

/** US Customary units — ft, lb, s */
public class PhysicsConstants {
    public static final double G       = 32.2;
    public static final double BALL_M  = 1.37 / 32.2;
    public static final double BALL_R  = 0.394;
    public static final double BALL_A  = Math.PI * BALL_R * BALL_R;
    public static final double RHO     = 0.00237;
    public static final double HOOP_R  = 0.75;
    public static final double DT      = 1.0 / 240.0;
    public static final int    STEPS_PER_FRAME = 4;
}
