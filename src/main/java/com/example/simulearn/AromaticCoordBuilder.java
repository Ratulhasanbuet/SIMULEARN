package com.example.simulearn;

import javafx.geometry.Point3D;
import java.util.List;

/**
 * Hardcoded 3D coordinates for aromatic / cyclic molecules.
 * All rings are flat in the XZ plane (Y=0).
 *
 * Geometry constants:
 *   B        = 72.0  (bond length)
 *   R6       = B     (hexagon radius: center-to-vertex = bond length when n=6)
 *   APO6     = B * cos(30°) ≈ 62.35  (hexagon apothem = center-to-edge-midpoint)
 *   R5       = B / (2*sin(36°)) ≈ 61.2 (pentagon radius)
 *
 * Atom index order matches exactly what MoleculePresets creates.
 */
public class AromaticCoordBuilder {

    private static final double B    = 72.0;
    private static final double R6   = B;
    private static final double APO6 = B * Math.cos(Math.toRadians(30)); // ≈ 62.35
    private static final double R5   = B / (2 * Math.sin(Math.toRadians(36))); // ≈ 61.2

    public static boolean apply(String name, MoleculeGraph graph) {
        List<MoleculeAtom> a = graph.getAtoms();
        switch (name) {
            case "Benzene"     -> setBenzene(a);
            case "Toluene"     -> setToluene(a);
            case "Phenol"      -> setPhenol(a);
            case "Aniline"     -> setAniline(a);
            case "Naphthalene" -> setNaphthalene(a);
            case "Pyridine"    -> setPyridine(a);
            case "Furan"       -> setFuran(a);
            case "Cyclohexane" -> setCyclohexane(a);
            default            -> { return false; }
        }
        return true;
    }

    // ── helpers ───────────────────────────────────────────────────────────────

    /** Set atom position in XZ plane (Y=0). x,z in raw units. */
    private static void pos(MoleculeAtom a, double x, double z) {
        a.position = new Point3D(x, 0, z);
    }

    /** Set atom position with Y offset (for cyclohexane chair). */
    private static void pos3(MoleculeAtom a, double x, double y, double z) {
        a.position = new Point3D(x, y, z);
    }

    /**
     * Place a regular n-gon centered at (cx,0,cz), starting at startDeg,
     * going counterclockwise. Returns positions as double[n][2] (x,z).
     */
    private static double[][] ring(double cx, double cz, double radius,
                                    int n, double startDeg) {
        double[][] pts = new double[n][2];
        for (int i = 0; i < n; i++) {
            double a = Math.toRadians(startDeg + i * (360.0 / n));
            pts[i][0] = cx + radius * Math.cos(a);
            pts[i][1] = cz + radius * Math.sin(a);
        }
        return pts;
    }

    /**
     * Place H outward from a ring atom.
     * ringCX, ringCZ = center of the ring this atom belongs to.
     */
    private static void placeH(MoleculeAtom h, MoleculeAtom c,
                                 double ringCX, double ringCZ) {
        double dx = c.position.getX() - ringCX;
        double dz = c.position.getZ() - ringCZ;
        double len = Math.sqrt(dx * dx + dz * dz);
        h.position = new Point3D(
            c.position.getX() + dx / len * B,
            0,
            c.position.getZ() + dz / len * B);
    }

    // ═════════════════════════════════════════════════════════════════════════
    // BENZENE  C6H6
    // atoms: C0..C5 (ring), H6..H11 (one H per C, addH order: C0,C1..C5)
    // ═════════════════════════════════════════════════════════════════════════
    private static void setBenzene(List<MoleculeAtom> a) {
        double[][] r = ring(0, 0, R6, 6, 90);
        for (int i = 0; i < 6; i++) pos(a.get(i), r[i][0], r[i][1]);
        // H outward from ring center (0,0,0)
        for (int i = 0; i < 6; i++) placeH(a.get(6 + i), a.get(i), 0, 0);
    }

    // ═════════════════════════════════════════════════════════════════════════
    // TOLUENE  C7H8
    // atoms: C0..C5 (ring), C6=Cme, H7..H9=Cme's H, H10..H14=ring H(C1..C5)
    // Preset: addH(Cme,3) then for i=1..5 addH(C[i],1)
    // ═════════════════════════════════════════════════════════════════════════
    private static void setToluene(List<MoleculeAtom> a) {
        // Ring: C0 at top (90°), going CCW
        double[][] r = ring(0, 0, R6, 6, 90);
        for (int i = 0; i < 6; i++) pos(a.get(i), r[i][0], r[i][1]);

        // C0 is at top: (0, R6). CH3 goes outward = upward (+Z direction)
        // Outward from ring center through C0 = (0, +1) in XZ → +Z
        double cmex = r[0][0];
        double cmez = r[0][1];
        double odx = cmex / R6, odz = cmez / R6; // unit outward
        // Cme = C6
        pos(a.get(6), cmex + odx * B, cmez + odz * B);

        // Cme H atoms (3): one straight up, two sides
        // Cme position
        double mx = a.get(6).position.getX();
        double mz = a.get(6).position.getZ();
        // perpendicular to outward in XZ plane
        double px = -odz, pz = odx;
        // H7: continue outward
        pos(a.get(7),  mx + odx * B,          mz + odz * B);
        // H8, H9: spread ±120° from outward around Cme
        double cos120 = Math.cos(Math.toRadians(120));
        double sin120 = Math.sin(Math.toRadians(120));
        // rotate outward by ±120°
        double h8x = odx * cos120 - odz * sin120;
        double h8z = odx * sin120 + odz * cos120;
        double h9x = odx * cos120 + odz * sin120;
        double h9z = -odx * sin120 + odz * cos120;
        pos(a.get(8),  mx + h8x * B, mz + h8z * B);
        pos(a.get(9),  mx + h9x * B, mz + h9z * B);

        // Ring H: C1..C5 (atoms 10..14)
        for (int i = 1; i <= 5; i++) placeH(a.get(9 + i), a.get(i), 0, 0);
    }

    // ═════════════════════════════════════════════════════════════════════════
    // PHENOL  C6H5OH
    // atoms: C0..C5 (ring), O6, H7=O-H, H8..H12=ring H(C1..C5)
    // ═════════════════════════════════════════════════════════════════════════
    private static void setPhenol(List<MoleculeAtom> a) {
        double[][] r = ring(0, 0, R6, 6, 90);
        for (int i = 0; i < 6; i++) pos(a.get(i), r[i][0], r[i][1]);

        // O attached to C0, outward
        double c0x = r[0][0], c0z = r[0][1];
        double odx = c0x / R6, odz = c0z / R6;
        pos(a.get(6), c0x + odx * B * 0.85, c0z + odz * B * 0.85); // O (shorter bond)

        // O-H: continue outward from O
        double ox = a.get(6).position.getX();
        double oz = a.get(6).position.getZ();
        pos(a.get(7), ox + odx * B * 0.85, oz + odz * B * 0.85);

        // Ring H: C1..C5
        for (int i = 1; i <= 5; i++) placeH(a.get(7 + i), a.get(i), 0, 0);
    }

    // ═════════════════════════════════════════════════════════════════════════
    // ANILINE  C6H5NH2
    // atoms: C0..C5 (ring), N6, H7..H8=N-H, H9..H13=ring H(C1..C5)
    // ═════════════════════════════════════════════════════════════════════════
    private static void setAniline(List<MoleculeAtom> a) {
        double[][] r = ring(0, 0, R6, 6, 90);
        for (int i = 0; i < 6; i++) pos(a.get(i), r[i][0], r[i][1]);

        // N attached to C0, outward
        double c0x = r[0][0], c0z = r[0][1];
        double odx = c0x / R6, odz = c0z / R6;
        pos(a.get(6), c0x + odx * B, c0z + odz * B); // N

        // NH2: two H atoms spread ±30° from outward in XZ plane
        double nx = a.get(6).position.getX();
        double nz = a.get(6).position.getZ();
        double spreadRad = Math.toRadians(30);
        double cos30 = Math.cos(spreadRad), sin30 = Math.sin(spreadRad);
        // H7: rotate outward +30°
        double h7x = odx * cos30 - odz * sin30;
        double h7z = odx * sin30 + odz * cos30;
        // H8: rotate outward -30°
        double h8x = odx * cos30 + odz * sin30;
        double h8z = -odx * sin30 + odz * cos30;
        pos(a.get(7), nx + h7x * B, nz + h7z * B);
        pos(a.get(8), nx + h8x * B, nz + h8z * B);

        // Ring H: C1..C5
        for (int i = 1; i <= 5; i++) placeH(a.get(8 + i), a.get(i), 0, 0);
    }

    // ═════════════════════════════════════════════════════════════════════════
    // NAPHTHALENE  C10H8 — two fused hexagons
    // Ring1: C0-C1-C2-C3-C4-C5, Ring2: C4-C9-C8-C7-C6-C5 (shared C4,C5)
    // H on: C0,C1,C2,C3 then C6,C7,C8,C9  → atoms 10..17
    // ═════════════════════════════════════════════════════════════════════════
    private static void setNaphthalene(List<MoleculeAtom> a) {
        // Ring1 center: (-APO6, 0), shared edge on right
        // Ring2 center: (+APO6, 0), shared edge on left
        // Shared atoms C4,C5 are at x=0

        // Ring1: start at 90° so C0 is at top-left
        // Angles for C0..C5: 90°, 150°, 210°, 270°, 330°(=C4, x=0,z=-36), 30°(=C5, x=0,z=+36)
        double[] r1ang = {90, 150, 210, 270, 330, 30};
        for (int i = 0; i < 6; i++) {
            double ang = Math.toRadians(r1ang[i]);
            pos(a.get(i), -APO6 + R6 * Math.cos(ang), R6 * Math.sin(ang));
        }

        // Ring2: C6,C7,C8,C9 are the new atoms (C4,C5 already placed)
        // Angles from ring2 center (+APO6, 0):
        //   C5(shared) at 150°, C6 at 90°, C7 at 30°, C8 at 330°, C9 at 270°, C4(shared) at 210°
        double[] r2ang = {90, 30, 330, 270}; // for C6,C7,C8,C9
        int[]    r2idx = {6,  7,  8,   9  };
        for (int i = 0; i < 4; i++) {
            double ang = Math.toRadians(r2ang[i]);
            pos(a.get(r2idx[i]), APO6 + R6 * Math.cos(ang), R6 * Math.sin(ang));
        }

        // H atoms (10..17): on C0,C1,C2,C3 (ring1 center) then C6,C7,C8,C9 (ring2 center)
        int[] hCarbon   = {0, 1, 2, 3, 6, 7, 8, 9};
        double[] hRingX = {-APO6, -APO6, -APO6, -APO6, APO6, APO6, APO6, APO6};
        for (int i = 0; i < 8; i++) {
            placeH(a.get(10 + i), a.get(hCarbon[i]), hRingX[i], 0);
        }
    }

    // ═════════════════════════════════════════════════════════════════════════
    // PYRIDINE  C5H5N — 6-membered ring with N at position 0
    // atoms: N(0), C1..C5 (ring), H6..H10 (on C1..C5)
    // ═════════════════════════════════════════════════════════════════════════
    private static void setPyridine(List<MoleculeAtom> a) {
        // Same as benzene ring, N at top (90°)
        double[][] r = ring(0, 0, R6, 6, 90);
        // N=a[0], C1..C5=a[1..5]
        pos(a.get(0), r[0][0], r[0][1]); // N
        for (int i = 1; i <= 5; i++) pos(a.get(i), r[i][0], r[i][1]);
        // H on C1..C5 (no H on N)
        for (int i = 1; i <= 5; i++) placeH(a.get(5 + i), a.get(i), 0, 0);
    }

    // ═════════════════════════════════════════════════════════════════════════
    // FURAN  C4H4O — 5-membered ring with O at position 0
    // atoms: O(0), C1..C4 (ring), H5..H8 (on C1..C4)
    // ═════════════════════════════════════════════════════════════════════════
    private static void setFuran(List<MoleculeAtom> a) {
        // Pentagon: O at top (90°)
        double[][] r = ring(0, 0, R5, 5, 90);
        pos(a.get(0), r[0][0], r[0][1]); // O
        for (int i = 1; i <= 4; i++) pos(a.get(i), r[i][0], r[i][1]);
        // H on C1..C4
        for (int i = 1; i <= 4; i++) placeH(a.get(4 + i), a.get(i), 0, 0);
    }

    // ═════════════════════════════════════════════════════════════════════════
    // CYCLOHEXANE  C6H12 — chair conformation
    // atoms: C0..C5 (ring), H6..H11 (axial H), H12..H17 (equatorial H)
    // Preset: for each C, addH(C,2) → H order: C0ax,C0eq, C1ax,C1eq, ...
    // ═════════════════════════════════════════════════════════════════════════
    private static void setCyclohexane(List<MoleculeAtom> a) {
        // Chair conformation: alternating up/down carbons
        // Ring carbons at radius R in XZ plane, alternating Y = +chairH / -chairH
        double ringR  = R6 * 0.95;  // slightly compressed for chair
        double chairY = B * 0.25;   // vertical offset for chair

        // Place 6 ring carbons, alternating up/down
        for (int i = 0; i < 6; i++) {
            double ang = Math.toRadians(90 + i * 60);
            double y   = (i % 2 == 0) ? +chairY : -chairY;
            pos3(a.get(i), ringR * Math.cos(ang), y, ringR * Math.sin(ang));
        }

        // Each C has 2 H: one axial (straight up/down), one equatorial (outward+angled)
        for (int i = 0; i < 6; i++) {
            double ang = Math.toRadians(90 + i * 60);
            double y   = (i % 2 == 0) ? +chairY : -chairY;

            // Axial H: points straight up for even C, straight down for odd C
            double axY = (i % 2 == 0) ? +B : -B;
            pos3(a.get(6 + i * 2),
                a.get(i).position.getX(),
                y + axY * 0.85,
                a.get(i).position.getZ());

            // Equatorial H: outward from ring center, slightly angled
            double eqX = ringR * Math.cos(ang) * 0.6; // outward component
            double eqZ = ringR * Math.sin(ang) * 0.6;
            double eqY = -y * 0.5; // slightly toward opposite side
            pos3(a.get(6 + i * 2 + 1),
                a.get(i).position.getX() + eqX,
                y + eqY,
                a.get(i).position.getZ() + eqZ);
        }
    }
}
