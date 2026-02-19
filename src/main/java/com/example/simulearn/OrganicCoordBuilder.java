package com.example.simulearn;

import javafx.geometry.Point3D;

import java.util.List;

/**
 * Hardcoded 3D coordinates for organic molecules.
 * Atom order matches exactly what MoleculePresets creates.
 * <p>
 * Tetrahedral geometry constants:
 * bond length B = 72
 * tet angle   = 109.5°
 * cos(109.5°) = -0.3333,  sin(109.5°) = 0.9428
 * <p>
 * For a CH3 group where C is at origin and the chain goes along +X:
 * H1 = B * ( 0.3333,  0.9428,  0      )  — up
 * H2 = B * ( 0.3333, -0.4714,  0.8165 )  — front-right
 * H3 = B * ( 0.3333, -0.4714, -0.8165 )  — back-right
 * <p>
 * For a staggered CH3 on the right end (incoming bond from -X):
 * H1 = B * (-0.3333,  0.9428,  0      )
 * H2 = B * (-0.3333, -0.4714,  0.8165 )
 * H3 = B * (-0.3333, -0.4714, -0.8165 )
 * <p>
 * Naming convention in set*() methods:
 * atoms.get(i) corresponds to the i-th atom added in MoleculePresets.
 */
public class OrganicCoordBuilder {

    private static final double B = 72.0;   // bond length
    // tetrahedral helpers
    private static final double TC = 0.3333; // -cos(109.5°) = +0.3333  (forward component)
    private static final double TS = 0.9428; // sin(109.5°)
    private static final double TA = 0.4714; // TS/2  (split component)
    private static final double TB = 0.8165; // sqrt(2/3) (lateral component)

    /**
     * Try to apply hardcoded positions for the given molecule name.
     * Returns true if handled, false if not an organic molecule.
     */
    public static boolean apply(String moleculeName, MoleculeGraph graph) {
        List<MoleculeAtom> atoms = graph.getAtoms();
        switch (moleculeName) {
            case "Ethane" -> setEthane(atoms);
            case "Ethanol" -> setEthanol(atoms);
            case "Acetic Acid" -> setAceticAcid(atoms);
            case "Acetone" -> setAcetone(atoms);
            case "Ethylene" -> setEthylene(atoms);
            case "Acetylene" -> setAcetylene(atoms);
            case "Propane" -> setPropane(atoms);
            case "Formaldehyde" -> setFormaldehyde(atoms);
            case "Dimethyl Ether" -> setDimethylEther(atoms);
            case "Chloroform" -> setChloroform(atoms);
            default -> {
                return false;
            }
        }
        return true;
    }

    // ── helpers ───────────────────────────────────────────────────────────────

    private static Point3D p(double x, double y, double z) {
        return new Point3D(x * B, y * B, z * B);
    }

    private static void pos(MoleculeAtom a, double x, double y, double z) {
        a.position = new Point3D(x, y, z);
    }

    // ═══════════════════════════════════════════════════════════════════════
    // ETHANE  CH3-CH3
    // atoms: C1(0) C2(1) H(2) H(3) H(4) H(5) H(6) H(7)
    //   C1 at origin, C2 along +X
    //   C1's 3H spread to the LEFT  (negative X component)
    //   C2's 3H spread to the RIGHT (positive X component) — staggered 60°
    // ═══════════════════════════════════════════════════════════════════════
    private static void setEthane(List<MoleculeAtom> a) {
        // C1 at -B/2, C2 at +B/2 along X (centered)
        double cx = B / 2;
        pos(a.get(0), -cx, 0, 0);       // C1
        pos(a.get(1), cx, 0, 0);       // C2

        // C1's H: go LEFT (-X) and fan out
        double hx1 = -cx - TC * B;
        pos(a.get(2), hx1, TS * B, 0);  // H up
        pos(a.get(3), hx1, -TA * B, TB * B);  // H front
        pos(a.get(4), hx1, -TA * B, -TB * B);  // H back

        // C2's H: go RIGHT (+X) and fan out — staggered 60° relative to C1
        double hx2 = cx + TC * B;
        pos(a.get(5), hx2, TS * B, 0);  // H up   (60° offset → rotated)
        pos(a.get(6), hx2, -TA * B, TB * B);  // H front
        pos(a.get(7), hx2, -TA * B, -TB * B);  // H back

    }

    // ═══════════════════════════════════════════════════════════════════════
    // ETHANOL  CH3-CH2-OH
    // atoms: C1(0) C2(1) O(2) H(3,4,5)=C1 H(6,7)=C2 H(8)=O
    // ═══════════════════════════════════════════════════════════════════════
    private static void setEthanol(List<MoleculeAtom> a) {
        // C1 — C2 — O along X axis (zigzag slightly for realism)
        pos(a.get(0), -B, 0, 0);   // C1
        pos(a.get(1), 0, 0, 0);   // C2
        pos(a.get(2), B * Math.cos(Math.toRadians(60)), 0, B * Math.cos(Math.toRadians(30))); // O (bent downward slightly)

        // C1 (left CH3): H go further left
        double c1x = -B;
        pos(a.get(3), c1x - TC * B, TS * B, 0);
        pos(a.get(4), c1x - TC * B, -TA * B, TB * B);
        pos(a.get(5), c1x - TC * B, -TA * B, -TB * B);

        // C2 (middle CH2): 2 H perpendicular to chain, staggered
//        pos(a.get(6),  TC*B,  TS*B*0.8,  TB*B*0.8);
//        pos(a.get(7),  TC*B,  TS*B*0.8, -TB*B*0.8);

        pos(a.get(6), 0, B, 0);
        pos(a.get(7), 0, -B, 0);
        // O-H: continue along O direction
        Point3D oPos = a.get(2).position;
        pos(a.get(8), oPos.getX() + B - 5,
                oPos.getY(),
                oPos.getZ());
    }

    // ═══════════════════════════════════════════════════════════════════════
    // ACETIC ACID  CH3-C(=O)-OH
    // atoms: C1(0) C2(1) O1(2)=double O2(3)=single H(4,5,6)=C1 H(7)=O2
    // ═══════════════════════════════════════════════════════════════════════
    private static void setAceticAcid(List<MoleculeAtom> a) {
        double bl = B * 0.87; // double bond shorter
        pos(a.get(0), -B, 0, 0);         // C1 (methyl)
        pos(a.get(1), 0, 0, 0);         // C2 (carbonyl) — trigonal planar
        pos(a.get(2), bl, bl * 0.866, 0);    // O1 =O (120°)
        pos(a.get(3), bl, -bl * 0.866, 0);    // O2 -OH (120°)

        // C1 H (methyl, tetrahedral)
        pos(a.get(4), -B - TC * B, TS * B, 0);
        pos(a.get(5), -B - TC * B, -TA * B, TB * B);
        pos(a.get(6), -B - TC * B, -TA * B, -TB * B);

        // O2-H
        Point3D o2 = a.get(3).position;
        pos(a.get(7), o2.getX() + TC * B * 0.7,
                o2.getY() - TS * B * 0.5, 0);
    }

    // ═══════════════════════════════════════════════════════════════════════
    // ACETONE  CH3-C(=O)-CH3
    // atoms: C1(0) C2(1) C3(2) O(3) H(4,5,6)=C1 H(7,8,9)=C3
    // ═══════════════════════════════════════════════════════════════════════
    private static void setAcetone(List<MoleculeAtom> a) {
        double bl = B * 0.87;
        pos(a.get(0), -B, 0, 0);         // C1
        pos(a.get(1), 0, 0, 0);         // C2 carbonyl — trigonal planar
        pos(a.get(2), B, 0, 0);         // C3
        pos(a.get(3), 0, bl, 0);         // O =O (up)

        // C1 methyl
        pos(a.get(4), -B - TC * B, TS * B, 0);
        pos(a.get(5), -B - TC * B, -TA * B, TB * B);
        pos(a.get(6), -B - TC * B, -TA * B, -TB * B);

        // C3 methyl (staggered)
        pos(a.get(7), B + TC * B, TS * B, 0);
        pos(a.get(8), B + TC * B, -TA * B, TB * B);
        pos(a.get(9), B + TC * B, -TA * B, -TB * B);

        // Apply 60° stagger to C3's H
        double cos60 = 0.5, sin60 = Math.sqrt(3) / 2;
        for (int i = 7; i <= 9; i++) {
            double y = a.get(i).position.getY();
            double z = a.get(i).position.getZ();
            a.get(i).position = new Point3D(
                    a.get(i).position.getX(),
                    y * cos60 - z * sin60,
                    y * sin60 + z * cos60);
        }
    }

    // ═══════════════════════════════════════════════════════════════════════
    // ETHYLENE  CH2=CH2 — trigonal planar, all in one plane
    // atoms: C1(0) C2(1) H(2,3)=C1 H(4,5)=C2
    // ═══════════════════════════════════════════════════════════════════════
    private static void setEthylene(List<MoleculeAtom> a) {
        double bl = B * 0.87;
        pos(a.get(0), -bl / 2, 0, 0);  // C1
        pos(a.get(1), bl / 2, 0, 0);  // C2

        // Each C has 2H at 120° in the XY plane
        double hx = bl / 2 + B * Math.cos(Math.toRadians(60));
        double hy = B * Math.sin(Math.toRadians(60));
        pos(a.get(2), -bl / 2 - B * Math.cos(Math.toRadians(60)), hy, 0);
        pos(a.get(3), -bl / 2 - B * Math.cos(Math.toRadians(60)), -hy, 0);
        pos(a.get(4), bl / 2 + B * Math.cos(Math.toRadians(60)), hy, 0);
        pos(a.get(5), bl / 2 + B * Math.cos(Math.toRadians(60)), -hy, 0);
    }

    // ═══════════════════════════════════════════════════════════════════════
    // ACETYLENE  HC≡CH — linear
    // atoms: C1(0) C2(1) H(2)=C1 H(3)=C2
    // ═══════════════════════════════════════════════════════════════════════
    private static void setAcetylene(List<MoleculeAtom> a) {
        double bl = B * 0.78;
        pos(a.get(0), -bl / 2, 0, 0);
        pos(a.get(1), bl / 2, 0, 0);
        pos(a.get(2), -bl / 2 - B, 0, 0);
        pos(a.get(3), bl / 2 + B, 0, 0);
    }

    // ═══════════════════════════════════════════════════════════════════════
    // PROPANE  CH3-CH2-CH3
    // atoms: C1(0) C2(1) C3(2) H(3,4,5)=C1 H(6,7)=C2 H(8,9,10)=C3
    // ═══════════════════════════════════════════════════════════════════════
    private static void setPropane(List<MoleculeAtom> a) {
        pos(a.get(0), -B, 0, 0);   // C1
        pos(a.get(1), 0, 0, 0);   // C2
        pos(a.get(2), B, 0, 0);   // C3

        // C1 (left CH3)
        pos(a.get(3), -B - TC * B, TS * B, 0);
        pos(a.get(4), -B - TC * B, -TA * B, TB * B);
        pos(a.get(5), -B - TC * B, -TA * B, -TB * B);

        // C2 (middle CH2) — 2H up and perpendicular
        pos(a.get(6), 0, TS * B, TB * B);
        pos(a.get(7), 0, TS * B, -TB * B);

        // C3 (right CH3) — staggered 60°
        pos(a.get(8), B + TC * B, TS * B, 0);
        pos(a.get(9), B + TC * B, -TA * B, TB * B);
        pos(a.get(10), B + TC * B, -TA * B, -TB * B);

        double cos60 = 0.5, sin60 = Math.sqrt(3) / 2;
        for (int i = 8; i <= 10; i++) {
            double y = a.get(i).position.getY();
            double z = a.get(i).position.getZ();
            a.get(i).position = new Point3D(
                    a.get(i).position.getX(),
                    y * cos60 - z * sin60,
                    y * sin60 + z * cos60);
        }
    }

    // ═══════════════════════════════════════════════════════════════════════
    // FORMALDEHYDE  H2C=O — trigonal planar
    // atoms: C(0) O(1) H(2) H(3)
    // ═══════════════════════════════════════════════════════════════════════
    private static void setFormaldehyde(List<MoleculeAtom> a) {
        double bl = B * 0.87;
        pos(a.get(0), 0, 0, 0);   // C
        pos(a.get(1), bl, 0, 0);   // O

        double hx = -B * Math.cos(Math.toRadians(60));
        double hy = B * Math.sin(Math.toRadians(60));
        pos(a.get(2), hx, hy, 0);
        pos(a.get(3), hx, -hy, 0);
    }

    // ═══════════════════════════════════════════════════════════════════════
    // DIMETHYL ETHER  CH3-O-CH3
    // atoms: O(0) C1(1) C2(2) H(3,4,5)=C1 H(6,7,8)=C2
    // ═══════════════════════════════════════════════════════════════════════
    private static void setDimethylEther(List<MoleculeAtom> a) {
        // O at center. C-O-C angle = 109.5 deg.
        // C1 goes left, C2 goes right, symmetric about Y axis.
        // Image: O at bottom-center, C1 upper-left, C2 upper-right.
        double halfAngle = Math.toRadians(109.5 / 2); // ~54.75 deg

        pos(a.get(0), 0, 0, 0);  // O at origin

        // C arms: spread left and right, angled upward
        double cx = B * Math.sin(halfAngle);  // horizontal spread
        double cy = B * Math.cos(halfAngle);  // vertical rise

        pos(a.get(1), -cx, cy, 0);  // C1 upper-left
        pos(a.get(2), cx, cy, 0);  // C2 upper-right

        // C1 H atoms: fan away from O (upper-left direction)
        // outward from C1 away from O = direction (-sin, +cos, 0) continue
        double c1x = -cx;
        double c1y = cy;
        // H1: straight up-left continuation
        pos(a.get(3), c1x-B*Math.cos(Math.toRadians(60)), c1y-B*Math.sin(Math.toRadians(60)), 0);
        // H2, H3: spread front and back
        pos(a.get(4), c1x - TC * B * 0.6, c1y + TA * B, TB * B);
        pos(a.get(5), c1x - TC * B * 0.6, c1y + TA * B, -TB * B);

        // C2 H atoms: mirror of C1 (upper-right)
        double c2x = cx;
        double c2y = cy;
        pos(a.get(6), c2x+B*Math.cos(Math.toRadians(60)), c2y-B*Math.sin(Math.toRadians(60)), 0);
        pos(a.get(7), c2x + TC * B * 0.6, c2y + TA * B, TB * B);
        pos(a.get(8), c2x + TC * B * 0.6, c2y + TA * B, -TB * B);
    }

    // ═══════════════════════════════════════════════════════════════════════
    // CHLOROFORM  CHCl3
    // atoms: C(0) H(1) Cl(2) Cl(3) Cl(4)
    // ═══════════════════════════════════════════════════════════════════════
    private static void setChloroform(List<MoleculeAtom> a) {
        double clB = B * 1.1; // Cl bonds slightly longer
        pos(a.get(0), 0, 0, 0);   // C
        pos(a.get(1), 0, B, 0);   // H — up

        // 3 Cl atoms below, 120° apart around Y axis, angled down at 109.5°
        double clY = -TC * clB;         // downward component
        double clR = TS * clB;         // radial component
        for (int i = 0; i < 3; i++) {
            double angle = Math.toRadians(120 * i);
            pos(a.get(2 + i),
                    clR * Math.cos(angle),
                    clY,
                    clR * Math.sin(angle));
        }
    }
}