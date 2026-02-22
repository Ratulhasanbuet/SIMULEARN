package com.example.simulearn;

import javafx.geometry.Point3D;
import java.util.List;

/**
 * Builds 3D coordinates for inorganic molecules using VSEPRCalculator.
 *
 * Each inorganic molecule has a single central atom.
 * VSEPRCalculator gives us the positions[] array for all electron pairs
 * (both bonded atoms AND lone pairs) around that central atom.
 *
 * Atom order in MoleculePresets matches the positions[] array order.
 * Lone pair positions are stored in MoleculeAtom.lonePairPositions[]
 * so Renderer3D can draw them as grey spheres (like the reference image).
 */
public class InorganicCoordBuilder {

    /**
     * Try to apply VSEPR-based positions for the given inorganic molecule.
     * Returns true if handled.
     */
    public static boolean apply(String moleculeName, MoleculeGraph graph) {
        List<MoleculeAtom> atoms = graph.getAtoms();
        if (atoms.isEmpty()) return false;

        switch (moleculeName) {
            case "Water"                    -> buildWater(atoms);
            case "Ammonia"                  -> buildAmmonia(atoms);
            case "Methane"                  -> buildMethane(atoms);
            case "Carbon Dioxide"           -> buildCO2(atoms);
            case "Boron Trifluoride"        -> buildBF3(atoms);
            case "Sulfur Hexafluoride"      -> buildSF6(atoms);
            case "Phosphorus Pentachloride" -> buildPCl5(atoms);
            case "Xenon Difluoride"         -> buildXeF2(atoms);
            case "Sulfur Dioxide"           -> buildSO2(atoms);
            case "Hydrogen Sulfide"         -> buildH2S(atoms);
            default                         -> { return false; }
        }
        return true;
    }

    // ── Bond length ───────────────────────────────────────────────────────────
    private static final double R = 70.0; // matches VSEPRCalculator bondLength

    // ── Shared helper: place atoms from VSEPR positions[] ─────────────────────

    /**
     * Central atom is atoms.get(0), placed at origin.
     * bondedAtoms: the non-H/non-lone-pair neighbors, placed at positions[0..n-1]
     * lonePairDirs: directions for lone pair spheres, placed after bonded atoms
     */
    private static void place(List<MoleculeAtom> atoms,
                               Point3D[] allPositions,   // all VSEPR slots (bonds + lp)
                               int bondedCount,           // how many are real atoms
                               int lonePairCount) {       // how many are lone pairs

        // Central atom at origin
        atoms.get(0).position = Point3D.ZERO;
        atoms.get(0).lonePairPositions = null; // reset

        // Place bonded atoms (skip index 0 = central)
        for (int i = 0; i < bondedCount && (i + 1) < atoms.size(); i++) {
            atoms.get(i + 1).position = allPositions[i];
        }

        // Store lone pair positions on the central atom
        if (lonePairCount > 0) {
            Point3D[] lpPos = new Point3D[lonePairCount];
            for (int i = 0; i < lonePairCount; i++) {
                lpPos[i] = allPositions[bondedCount + i];
            }
            atoms.get(0).lonePairPositions = lpPos;
        }
    }

    // ═════════════════════════════════════════════════════════════════════════
    // WATER  H₂O — bent, steric=4, lp=2
    // atoms: O(0) H(1) H(2)
    // VSEPR: tetrahedral positions, 2 bonds + 2 lone pairs
    // ═════════════════════════════════════════════════════════════════════════
    private static void buildWater(List<MoleculeAtom> atoms) {
        // Bent shape ~104.5°
        // Place 2 H atoms and 2 lone pair directions using tetrahedral geometry
        double angle = Math.toRadians(104.5 / 2); // half angle from Y axis
        double r = R;

        // H atoms in XY plane, symmetric about Y axis, pointing down
        Point3D h1 = new Point3D( Math.sin(angle) * r, -Math.cos(angle) * r, 0);
        Point3D h2 = new Point3D(-Math.sin(angle) * r, -Math.cos(angle) * r, 0);

        // Lone pairs above, using tetrahedral angle offsets
        double lpAngle = Math.toRadians(109.5 / 2);
        Point3D lp1 = new Point3D( Math.sin(lpAngle) * r * 0.85, Math.cos(lpAngle) * r * 0.85, 0);
        Point3D lp2 = new Point3D(-Math.sin(lpAngle) * r * 0.85, Math.cos(lpAngle) * r * 0.85, 0);

        atoms.get(0).position = Point3D.ZERO;
        atoms.get(1).position = h1;
        atoms.get(2).position = h2;
        atoms.get(0).lonePairPositions = new Point3D[]{lp1, lp2};
    }

    // ═════════════════════════════════════════════════════════════════════════
    // AMMONIA  NH₃ — trigonal pyramidal, steric=4, lp=1
    // atoms: N(0) H(1) H(2) H(3)
    // ═════════════════════════════════════════════════════════════════════════
    private static void buildAmmonia(List<MoleculeAtom> atoms) {
        // 3 H below, 1 lone pair above
        double bondAngle = Math.toRadians(107.0);
        double r = R;

        // H atoms: cone below N, 120° apart around Y axis
        double coneAngle = Math.toRadians(90 - (180 - 107) / 2.0); // tilt from horizontal
        double hy = -r * Math.sin(Math.toRadians(20)); // slightly below center
        double hr =  r * Math.cos(Math.toRadians(20));

        atoms.get(0).position = Point3D.ZERO;
        atoms.get(1).position = new Point3D(hr, hy, 0);
        atoms.get(2).position = new Point3D(hr * Math.cos(Math.toRadians(120)),
                                             hy,
                                             hr * Math.sin(Math.toRadians(120)));
        atoms.get(3).position = new Point3D(hr * Math.cos(Math.toRadians(240)),
                                             hy,
                                             hr * Math.sin(Math.toRadians(240)));

        // Lone pair above N
        atoms.get(0).lonePairPositions = new Point3D[]{
            new Point3D(0, r * 0.85, 0)
        };
    }

    // ═════════════════════════════════════════════════════════════════════════
    // METHANE  CH₄ — tetrahedral, steric=4, lp=0
    // atoms: C(0) H(1) H(2) H(3) H(4)
    // ═════════════════════════════════════════════════════════════════════════
    private static void buildMethane(List<MoleculeAtom> atoms) {
        double a = R / Math.sqrt(3);
        atoms.get(0).position = Point3D.ZERO;
        atoms.get(1).position = new Point3D( a,  a,  a);
        atoms.get(2).position = new Point3D( a, -a, -a);
        atoms.get(3).position = new Point3D(-a,  a, -a);
        atoms.get(4).position = new Point3D(-a, -a,  a);
        atoms.get(0).lonePairPositions = null;
    }

    // ═════════════════════════════════════════════════════════════════════════
    // CO₂ — linear, steric=2, lp=0
    // atoms: C(0) O1(1) O2(2)
    // ═════════════════════════════════════════════════════════════════════════
    private static void buildCO2(List<MoleculeAtom> atoms) {
        double bl = R * 0.87; // double bond shorter
        atoms.get(0).position = Point3D.ZERO;
        atoms.get(1).position = new Point3D( bl, 0, 0);
        atoms.get(2).position = new Point3D(-bl, 0, 0);
        atoms.get(0).lonePairPositions = null;
    }

    // ═════════════════════════════════════════════════════════════════════════
    // BF₃ — trigonal planar, steric=3, lp=0
    // atoms: B(0) F(1) F(2) F(3)
    // ═════════════════════════════════════════════════════════════════════════
    private static void buildBF3(List<MoleculeAtom> atoms) {
        atoms.get(0).position = Point3D.ZERO;
        atoms.get(1).position = new Point3D(R, 0, 0);
        atoms.get(2).position = new Point3D(R * Math.cos(Math.toRadians(120)),
                                             0,
                                             R * Math.sin(Math.toRadians(120)));
        atoms.get(3).position = new Point3D(R * Math.cos(Math.toRadians(240)),
                                             0,
                                             R * Math.sin(Math.toRadians(240)));
        atoms.get(0).lonePairPositions = null;
    }

    // ═════════════════════════════════════════════════════════════════════════
    // SF₆ — octahedral, steric=6, lp=0
    // atoms: S(0) F(1..6)
    // ═════════════════════════════════════════════════════════════════════════
    private static void buildSF6(List<MoleculeAtom> atoms) {
        atoms.get(0).position = Point3D.ZERO;
        atoms.get(1).position = new Point3D( R,  0,  0);
        atoms.get(2).position = new Point3D(-R,  0,  0);
        atoms.get(3).position = new Point3D( 0,  R,  0);
        atoms.get(4).position = new Point3D( 0, -R,  0);
        atoms.get(5).position = new Point3D( 0,  0,  R);
        atoms.get(6).position = new Point3D( 0,  0, -R);
        atoms.get(0).lonePairPositions = null;
    }

    // ═════════════════════════════════════════════════════════════════════════
    // PCl₅ — trigonal bipyramidal, steric=5, lp=0
    // atoms: P(0) Cl(1..5)
    // ═════════════════════════════════════════════════════════════════════════
    private static void buildPCl5(List<MoleculeAtom> atoms) {
        double rCl = R * 1.1; // Cl bonds longer
        atoms.get(0).position = Point3D.ZERO;
        // Axial (up/down)
        atoms.get(1).position = new Point3D(0,  rCl, 0);
        atoms.get(2).position = new Point3D(0, -rCl, 0);
        // Equatorial (120° apart in XZ plane)
        atoms.get(3).position = new Point3D(rCl, 0, 0);
        atoms.get(4).position = new Point3D(rCl * Math.cos(Math.toRadians(120)),
                                             0,
                                             rCl * Math.sin(Math.toRadians(120)));
        atoms.get(5).position = new Point3D(rCl * Math.cos(Math.toRadians(240)),
                                             0,
                                             rCl * Math.sin(Math.toRadians(240)));
        atoms.get(0).lonePairPositions = null;
    }

    // ═════════════════════════════════════════════════════════════════════════
    // XeF₂ — linear, steric=5, lp=3
    // atoms: Xe(0) F(1) F(2)
    // Lone pairs occupy 3 equatorial positions
    // ═════════════════════════════════════════════════════════════════════════
    private static void buildXeF2(List<MoleculeAtom> atoms) {
        atoms.get(0).position = Point3D.ZERO;
        // F atoms axial (linear)
        atoms.get(1).position = new Point3D(0,  R, 0);
        atoms.get(2).position = new Point3D(0, -R, 0);

        // 3 lone pairs equatorial
        double lpR = R * 0.8;
        atoms.get(0).lonePairPositions = new Point3D[]{
            new Point3D(lpR, 0, 0),
            new Point3D(lpR * Math.cos(Math.toRadians(120)), 0, lpR * Math.sin(Math.toRadians(120))),
            new Point3D(lpR * Math.cos(Math.toRadians(240)), 0, lpR * Math.sin(Math.toRadians(240)))
        };
    }

    // ═════════════════════════════════════════════════════════════════════════
    // SO₂ — bent, steric=3, lp=1
    // atoms: S(0) O1(1) O2(2)
    // ═════════════════════════════════════════════════════════════════════════
    private static void buildSO2(List<MoleculeAtom> atoms) {
        double angle = Math.toRadians(119.0 / 2);
        double bl = R * 0.87;

        atoms.get(0).position = Point3D.ZERO;
        // 2 O atoms bent downward
        atoms.get(1).position = new Point3D( Math.sin(angle) * bl, -Math.cos(angle) * bl, 0);
        atoms.get(2).position = new Point3D(-Math.sin(angle) * bl, -Math.cos(angle) * bl, 0);

        // 1 lone pair above S
        atoms.get(0).lonePairPositions = new Point3D[]{
            new Point3D(0, R * 0.85, 0)
        };
    }

    // ═════════════════════════════════════════════════════════════════════════
    // H₂S — bent, steric=4, lp=2
    // atoms: S(0) H(1) H(2)
    // ═════════════════════════════════════════════════════════════════════════
    private static void buildH2S(List<MoleculeAtom> atoms) {
        double angle = Math.toRadians(92.0 / 2);
        double r = R;

        atoms.get(0).position = Point3D.ZERO;
        atoms.get(1).position = new Point3D( Math.sin(angle) * r, -Math.cos(angle) * r, 0);
        atoms.get(2).position = new Point3D(-Math.sin(angle) * r, -Math.cos(angle) * r, 0);

        // 2 lone pairs above S
        double lpAngle = Math.toRadians(109.5 / 2);
        double lpR = R * 0.8;
        atoms.get(0).lonePairPositions = new Point3D[]{
            new Point3D( Math.sin(lpAngle) * lpR, Math.cos(lpAngle) * lpR, 0),
            new Point3D(-Math.sin(lpAngle) * lpR, Math.cos(lpAngle) * lpR, 0)
        };
    }
}
