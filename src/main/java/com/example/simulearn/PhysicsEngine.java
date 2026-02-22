package com.example.simulearn;

import static com.example.simulearn.PhysicsConstants.*;

public class PhysicsEngine {

    public enum SimEvent { NONE, SCORE, MISS }

    private final SimState state;

    public PhysicsEngine(SimState state) { this.state = state; }

    public SimEvent step(boolean useAir, double Cd, boolean trail) {
        if (!state.running) return SimEvent.NONE;

        double v  = Math.sqrt(state.vx * state.vx + state.vy * state.vy);
        double ax = 0, ay = -G;

        if (useAir && v > 0.001) {
            double Fd = 0.5 * RHO * Cd * BALL_A * v * v;
            ax -= (Fd / BALL_M) * (state.vx / v);
            ay -= (Fd / BALL_M) * (state.vy / v);
        }

        state.vx += ax * DT;  state.vy += ay * DT;
        state.x  += state.vx * DT;  state.y += state.vy * DT;
        state.t  += DT;

        if (trail) {
            state.trail.add(new double[]{state.x, state.y});
            if (state.trail.size() > 600) state.trail.remove(0);
        }

        // Telemetry
        double fd = useAir ? 0.5 * RHO * Cd * BALL_A * v * v : 0;
        state.telSpeed  = v;
        state.telHeight = state.y;
        state.telDist   = Math.abs(state.x - state.hoopX);
        state.telTime   = state.t;
        state.telDrag   = fd;
        state.telAngle  = v > 0.5 ? Math.toDegrees(Math.atan2(state.vy, -state.vx)) : 0;

        // Scoring
        boolean hit = false;
        switch (state.mode) {
            case BASKETBALL -> {
                double dx = state.x - state.hoopX, dy = state.y - state.hoopY;
                if (!state.scored && dx*dx+dy*dy < Math.pow(HOOP_R*0.9,2) && state.vy < 0)
                    hit = true;
            }
            case BASEBALL -> {
                if (!state.scored && Math.abs(state.x - state.hoopX) < 0.3 && state.vy < 0
                        && state.y >= 0 && state.y <= state.targetH + 0.5)
                    hit = true;
            }
            case HOCKEY -> {
                if (!state.scored
                        && state.x <= state.hoopX
                        && state.x >= state.hoopX - state.targetDepth
                        && state.y >= 0 && state.y <= state.targetH)
                    hit = true;
            }
        }

        if (hit) {
            state.scored = true; state.score++;
            state.running = false; state.landed = true;
            return SimEvent.SCORE;
        }

        if (state.y <= 0 || state.x < state.hoopX - 5) {
            state.running = false; state.landed = true;
            return SimEvent.MISS;
        }

        return SimEvent.NONE;
    }
}
