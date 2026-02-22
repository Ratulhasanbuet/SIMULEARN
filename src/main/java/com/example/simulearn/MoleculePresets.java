package com.example.simulearn;

import java.util.*;

/**
 * All preset molecules organized by category.
 * 28 total: 10 Inorganic, 10 Organic, 8 Aromatic
 */
public class MoleculePresets {

    public static final String CAT_INORGANIC = "Inorganic";
    public static final String CAT_ORGANIC   = "Organic";
    public static final String CAT_AROMATIC  = "Aromatic";

    public record PresetInfo(
            String name, String category, String formula,
            String shape, String angles, MoleculeGraph graph) {}

    public static List<PresetInfo> all() {
        List<PresetInfo> list = new ArrayList<>();
        // Inorganic
        list.add(water());
        list.add(ammonia());
        list.add(methane());
        list.add(co2());
        list.add(bf3());
        list.add(sf6());
        list.add(pcl5());
        list.add(xef2());
        list.add(so2());
        list.add(h2s());
        // Organic
        list.add(ethane());
        list.add(ethanol());
        list.add(aceticAcid());
        list.add(acetone());
        list.add(ethylene());
        list.add(acetylene());
        list.add(propane());
        list.add(formaldehyde());
        list.add(dimethylEther());
        list.add(chloroform());
        // Aromatic
        list.add(benzene());
        list.add(toluene());
        list.add(phenol());
        list.add(aniline());
        list.add(naphthalene());
        list.add(pyridine());
        list.add(furan());
        list.add(cyclohexane());
        return list;
    }

    public static Map<String, List<PresetInfo>> byCategory() {
        Map<String, List<PresetInfo>> map = new LinkedHashMap<>();
        map.put(CAT_INORGANIC, new ArrayList<>());
        map.put(CAT_ORGANIC,   new ArrayList<>());
        map.put(CAT_AROMATIC,  new ArrayList<>());
        for (PresetInfo p : all()) map.get(p.category()).add(p);
        return map;
    }

    // ═══════════════════════════════════════════════════════════════════════
    // INORGANIC
    // ═══════════════════════════════════════════════════════════════════════

    private static PresetInfo water() {
        MoleculeGraph g = new MoleculeGraph();
        MoleculeAtom O  = g.addAtom("O");  O.lonePairs = 2;
        MoleculeAtom H1 = g.addAtom("H");
        MoleculeAtom H2 = g.addAtom("H");
        g.addBond(O, H1, 1); g.addBond(O, H2, 1);
        return new PresetInfo("Water", CAT_INORGANIC, "H₂O", "Bent", "~104.5°", g);
    }

    private static PresetInfo ammonia() {
        MoleculeGraph g = new MoleculeGraph();
        MoleculeAtom N  = g.addAtom("N");  N.lonePairs = 1;
        MoleculeAtom H1 = g.addAtom("H");
        MoleculeAtom H2 = g.addAtom("H");
        MoleculeAtom H3 = g.addAtom("H");
        g.addBond(N, H1, 1); g.addBond(N, H2, 1); g.addBond(N, H3, 1);
        return new PresetInfo("Ammonia", CAT_INORGANIC, "NH₃", "Trigonal Pyramidal", "~107°", g);
    }

    private static PresetInfo methane() {
        MoleculeGraph g = new MoleculeGraph();
        MoleculeAtom C = g.addAtom("C");
        addH(g, C, 4);
        return new PresetInfo("Methane", CAT_INORGANIC, "CH₄", "Tetrahedral", "109.5°", g);
    }

    private static PresetInfo co2() {
        MoleculeGraph g = new MoleculeGraph();
        MoleculeAtom C  = g.addAtom("C");
        MoleculeAtom O1 = g.addAtom("O"); O1.lonePairs = 2;
        MoleculeAtom O2 = g.addAtom("O"); O2.lonePairs = 2;
        g.addBond(O1, C, 2); g.addBond(C, O2, 2);
        return new PresetInfo("Carbon Dioxide", CAT_INORGANIC, "CO₂", "Linear", "180°", g);
    }

    private static PresetInfo bf3() {
        MoleculeGraph g = new MoleculeGraph();
        MoleculeAtom B = g.addAtom("B");
        for (int i = 0; i < 3; i++) {
            MoleculeAtom F = g.addAtom("F"); F.lonePairs = 3; g.addBond(B, F, 1);
        }
        return new PresetInfo("Boron Trifluoride", CAT_INORGANIC, "BF₃", "Trigonal Planar", "120°", g);
    }

    private static PresetInfo sf6() {
        MoleculeGraph g = new MoleculeGraph();
        MoleculeAtom S = g.addAtom("S");
        for (int i = 0; i < 6; i++) {
            MoleculeAtom F = g.addAtom("F"); F.lonePairs = 3; g.addBond(S, F, 1);
        }
        return new PresetInfo("Sulfur Hexafluoride", CAT_INORGANIC, "SF₆", "Octahedral", "90°, 180°", g);
    }

    private static PresetInfo pcl5() {
        MoleculeGraph g = new MoleculeGraph();
        MoleculeAtom P = g.addAtom("P");
        for (int i = 0; i < 5; i++) {
            MoleculeAtom Cl = g.addAtom("Cl"); Cl.lonePairs = 3; g.addBond(P, Cl, 1);
        }
        return new PresetInfo("Phosphorus Pentachloride", CAT_INORGANIC, "PCl₅",
                "Trigonal Bipyramidal", "90°, 120°", g);
    }

    private static PresetInfo xef2() {
        MoleculeGraph g = new MoleculeGraph();
        MoleculeAtom Xe = g.addAtom("Xe"); Xe.lonePairs = 3;
        MoleculeAtom F1 = g.addAtom("F");  F1.lonePairs = 3;
        MoleculeAtom F2 = g.addAtom("F");  F2.lonePairs = 3;
        g.addBond(Xe, F1, 1); g.addBond(Xe, F2, 1);
        return new PresetInfo("Xenon Difluoride", CAT_INORGANIC, "XeF₂", "Linear", "180°", g);
    }

    private static PresetInfo so2() {
        MoleculeGraph g = new MoleculeGraph();
        MoleculeAtom S  = g.addAtom("S"); S.lonePairs = 1;
        MoleculeAtom O1 = g.addAtom("O"); O1.lonePairs = 2;
        MoleculeAtom O2 = g.addAtom("O"); O2.lonePairs = 2;
        g.addBond(S, O1, 2); g.addBond(S, O2, 2);
        return new PresetInfo("Sulfur Dioxide", CAT_INORGANIC, "SO₂", "Bent", "~119°", g);
    }

    private static PresetInfo h2s() {
        MoleculeGraph g = new MoleculeGraph();
        MoleculeAtom S  = g.addAtom("S"); S.lonePairs = 2;
        MoleculeAtom H1 = g.addAtom("H");
        MoleculeAtom H2 = g.addAtom("H");
        g.addBond(S, H1, 1); g.addBond(S, H2, 1);
        return new PresetInfo("Hydrogen Sulfide", CAT_INORGANIC, "H₂S", "Bent", "~92°", g);
    }

    // ═══════════════════════════════════════════════════════════════════════
    // ORGANIC
    // ═══════════════════════════════════════════════════════════════════════

    private static PresetInfo ethane() {
        // CH3-CH3
        MoleculeGraph g = new MoleculeGraph();
        MoleculeAtom C1 = g.addAtom("C");
        MoleculeAtom C2 = g.addAtom("C");
        g.addBond(C1, C2, 1);
        addH(g, C1, 3); addH(g, C2, 3);
        return new PresetInfo("Ethane", CAT_ORGANIC, "C₂H₆", "Tetrahedral", "109.5°", g);
    }

    private static PresetInfo ethanol() {
        // CH3-CH2-OH
        MoleculeGraph g = new MoleculeGraph();
        MoleculeAtom C1 = g.addAtom("C");
        MoleculeAtom C2 = g.addAtom("C");
        MoleculeAtom O  = g.addAtom("O"); O.lonePairs = 2;
        g.addBond(C1, C2, 1); g.addBond(C2, O, 1);
        addH(g, C1, 3); addH(g, C2, 2); addH(g, O, 1);
        return new PresetInfo("Ethanol", CAT_ORGANIC, "C₂H₅OH",
                "Tetrahedral (C), Bent (O)", "109.5°", g);
    }

    private static PresetInfo aceticAcid() {
        // CH3-C(=O)-OH
        MoleculeGraph g = new MoleculeGraph();
        MoleculeAtom C1 = g.addAtom("C");
        MoleculeAtom C2 = g.addAtom("C");
        MoleculeAtom O1 = g.addAtom("O"); O1.lonePairs = 2;
        MoleculeAtom O2 = g.addAtom("O"); O2.lonePairs = 2;
        g.addBond(C1, C2, 1); g.addBond(C2, O1, 2); g.addBond(C2, O2, 1);
        addH(g, C1, 3); addH(g, O2, 1);
        return new PresetInfo("Acetic Acid", CAT_ORGANIC, "CH₃COOH",
                "Trigonal Planar (C=O)", "120°", g);
    }

    private static PresetInfo acetone() {
        // CH3-C(=O)-CH3
        MoleculeGraph g = new MoleculeGraph();
        MoleculeAtom C1 = g.addAtom("C");
        MoleculeAtom C2 = g.addAtom("C");
        MoleculeAtom C3 = g.addAtom("C");
        MoleculeAtom O  = g.addAtom("O"); O.lonePairs = 2;
        g.addBond(C1, C2, 1); g.addBond(C2, C3, 1); g.addBond(C2, O, 2);
        addH(g, C1, 3); addH(g, C3, 3);
        return new PresetInfo("Acetone", CAT_ORGANIC, "CH₃COCH₃",
                "Trigonal Planar (C=O)", "120°", g);
    }

    private static PresetInfo ethylene() {
        // CH2=CH2
        MoleculeGraph g = new MoleculeGraph();
        MoleculeAtom C1 = g.addAtom("C");
        MoleculeAtom C2 = g.addAtom("C");
        g.addBond(C1, C2, 2);
        addH(g, C1, 2); addH(g, C2, 2);
        return new PresetInfo("Ethylene", CAT_ORGANIC, "C₂H₄", "Trigonal Planar", "120°", g);
    }

    private static PresetInfo acetylene() {
        // HC≡CH
        MoleculeGraph g = new MoleculeGraph();
        MoleculeAtom C1 = g.addAtom("C");
        MoleculeAtom C2 = g.addAtom("C");
        g.addBond(C1, C2, 3);
        addH(g, C1, 1); addH(g, C2, 1);
        return new PresetInfo("Acetylene", CAT_ORGANIC, "C₂H₂", "Linear", "180°", g);
    }

    private static PresetInfo propane() {
        // CH3-CH2-CH3
        MoleculeGraph g = new MoleculeGraph();
        MoleculeAtom C1 = g.addAtom("C");
        MoleculeAtom C2 = g.addAtom("C");
        MoleculeAtom C3 = g.addAtom("C");
        g.addBond(C1, C2, 1); g.addBond(C2, C3, 1);
        addH(g, C1, 3); addH(g, C2, 2); addH(g, C3, 3);
        return new PresetInfo("Propane", CAT_ORGANIC, "C₃H₈", "Tetrahedral", "109.5°", g);
    }

    private static PresetInfo formaldehyde() {
        // H2C=O
        MoleculeGraph g = new MoleculeGraph();
        MoleculeAtom C = g.addAtom("C");
        MoleculeAtom O = g.addAtom("O"); O.lonePairs = 2;
        g.addBond(C, O, 2);
        addH(g, C, 2);
        return new PresetInfo("Formaldehyde", CAT_ORGANIC, "CH₂O", "Trigonal Planar", "120°", g);
    }

    private static PresetInfo dimethylEther() {
        // CH3-O-CH3
        MoleculeGraph g = new MoleculeGraph();
        MoleculeAtom O  = g.addAtom("O"); O.lonePairs = 2;
        MoleculeAtom C1 = g.addAtom("C");
        MoleculeAtom C2 = g.addAtom("C");
        g.addBond(O, C1, 1); g.addBond(O, C2, 1);
        addH(g, C1, 3); addH(g, C2, 3);
        return new PresetInfo("Dimethyl Ether", CAT_ORGANIC, "CH₃OCH₃", "Bent (O)", "~109°", g);
    }

    private static PresetInfo chloroform() {
        // CHCl3
        MoleculeGraph g = new MoleculeGraph();
        MoleculeAtom C = g.addAtom("C");
        addH(g, C, 1);
        for (int i = 0; i < 3; i++) {
            MoleculeAtom Cl = g.addAtom("Cl"); Cl.lonePairs = 3; g.addBond(C, Cl, 1);
        }
        return new PresetInfo("Chloroform", CAT_ORGANIC, "CHCl₃", "Tetrahedral", "109.5°", g);
    }

    // ═══════════════════════════════════════════════════════════════════════
    // AROMATIC / CYCLIC
    // ═══════════════════════════════════════════════════════════════════════

    private static PresetInfo benzene() {
        MoleculeGraph g = new MoleculeGraph();
        MoleculeAtom[] C = new MoleculeAtom[6];
        for (int i = 0; i < 6; i++) C[i] = g.addAtom("C");
        for (int i = 0; i < 6; i++)
            g.addBond(C[i], C[(i+1)%6], i%2==0 ? 2 : 1);
        for (MoleculeAtom c : C) addH(g, c, 1);
        return new PresetInfo("Benzene", CAT_AROMATIC, "C₆H₆", "Trigonal Planar", "120°", g);
    }

    private static PresetInfo toluene() {
        MoleculeGraph g = new MoleculeGraph();
        MoleculeAtom[] C = new MoleculeAtom[6];
        for (int i = 0; i < 6; i++) C[i] = g.addAtom("C");
        for (int i = 0; i < 6; i++)
            g.addBond(C[i], C[(i+1)%6], i%2==0 ? 2 : 1);
        // CH3 attached to C[0] — CoordBuilder will place it outward
        MoleculeAtom Cme = g.addAtom("C");
        g.addBond(C[0], Cme, 1);
        addH(g, Cme, 3);
        for (int i = 1; i < 6; i++) addH(g, C[i], 1);
        return new PresetInfo("Toluene", CAT_AROMATIC, "C₇H₈",
                "Trigonal Planar (ring)", "120°", g);
    }

    private static PresetInfo phenol() {
        MoleculeGraph g = new MoleculeGraph();
        MoleculeAtom[] C = new MoleculeAtom[6];
        for (int i = 0; i < 6; i++) C[i] = g.addAtom("C");
        for (int i = 0; i < 6; i++)
            g.addBond(C[i], C[(i+1)%6], i%2==0 ? 2 : 1);
        MoleculeAtom O = g.addAtom("O"); O.lonePairs = 2;
        g.addBond(C[0], O, 1);
        addH(g, O, 1);
        for (int i = 1; i < 6; i++) addH(g, C[i], 1);
        return new PresetInfo("Phenol", CAT_AROMATIC, "C₆H₅OH",
                "Trigonal Planar (ring)", "120°", g);
    }

    private static PresetInfo aniline() {
        MoleculeGraph g = new MoleculeGraph();
        MoleculeAtom[] C = new MoleculeAtom[6];
        for (int i = 0; i < 6; i++) C[i] = g.addAtom("C");
        for (int i = 0; i < 6; i++)
            g.addBond(C[i], C[(i+1)%6], i%2==0 ? 2 : 1);
        MoleculeAtom N = g.addAtom("N"); N.lonePairs = 1;
        g.addBond(C[0], N, 1);
        addH(g, N, 2);
        for (int i = 1; i < 6; i++) addH(g, C[i], 1);
        return new PresetInfo("Aniline", CAT_AROMATIC, "C₆H₅NH₂",
                "Trigonal Planar (ring)", "120°", g);
    }

    private static PresetInfo naphthalene() {
        // Two fused 6-membered rings.
        // Ring 1: C0-C1-C2-C3-C4-C5 (closed loop)
        // Ring 2 shares C4-C5 bond: C4-C9-C8-C7-C6-C5
        MoleculeGraph g = new MoleculeGraph();
        MoleculeAtom[] C = new MoleculeAtom[10];
        for (int i = 0; i < 10; i++) C[i] = g.addAtom("C");

        // Ring 1 bonds
        g.addBond(C[0], C[1], 2);
        g.addBond(C[1], C[2], 1);
        g.addBond(C[2], C[3], 2);
        g.addBond(C[3], C[4], 1);
        g.addBond(C[4], C[5], 2); // shared bond
        g.addBond(C[5], C[0], 1);

        // Ring 2 bonds (shares C4-C5)
        g.addBond(C[4], C[9], 1);
        g.addBond(C[9], C[8], 2);
        g.addBond(C[8], C[7], 1);
        g.addBond(C[7], C[6], 2);
        g.addBond(C[6], C[5], 1);

        // H on outer atoms only (C4, C5 are bridgehead — no H)
        addH(g, C[0], 1); addH(g, C[1], 1); addH(g, C[2], 1); addH(g, C[3], 1);
        addH(g, C[6], 1); addH(g, C[7], 1); addH(g, C[8], 1); addH(g, C[9], 1);

        return new PresetInfo("Naphthalene", CAT_AROMATIC, "C₁₀H₈",
                "Trigonal Planar (fused rings)", "120°", g);
    }

    private static PresetInfo pyridine() {
        // 6-membered ring with one N replacing a C
        MoleculeGraph g = new MoleculeGraph();
        MoleculeAtom N  = g.addAtom("N"); N.lonePairs = 1;
        MoleculeAtom C1 = g.addAtom("C");
        MoleculeAtom C2 = g.addAtom("C");
        MoleculeAtom C3 = g.addAtom("C");
        MoleculeAtom C4 = g.addAtom("C");
        MoleculeAtom C5 = g.addAtom("C");
        // Ring: N=C1-C2=C3-C4=C5-N
        g.addBond(N,  C1, 2);
        g.addBond(C1, C2, 1);
        g.addBond(C2, C3, 2);
        g.addBond(C3, C4, 1);
        g.addBond(C4, C5, 2);
        g.addBond(C5, N,  1);
        addH(g, C1, 1); addH(g, C2, 1); addH(g, C3, 1);
        addH(g, C4, 1); addH(g, C5, 1);
        return new PresetInfo("Pyridine", CAT_AROMATIC, "C₅H₅N",
                "Trigonal Planar (ring)", "120°", g);
    }

    private static PresetInfo furan() {
        // 5-membered ring with O
        MoleculeGraph g = new MoleculeGraph();
        MoleculeAtom O  = g.addAtom("O"); O.lonePairs = 2;
        MoleculeAtom C1 = g.addAtom("C");
        MoleculeAtom C2 = g.addAtom("C");
        MoleculeAtom C3 = g.addAtom("C");
        MoleculeAtom C4 = g.addAtom("C");
        g.addBond(O,  C1, 1);
        g.addBond(C1, C2, 2);
        g.addBond(C2, C3, 1);
        g.addBond(C3, C4, 2);
        g.addBond(C4, O,  1);
        addH(g, C1, 1); addH(g, C2, 1); addH(g, C3, 1); addH(g, C4, 1);
        return new PresetInfo("Furan", CAT_AROMATIC, "C₄H₄O",
                "Trigonal Planar (ring)", "108°", g);
    }

    private static PresetInfo cyclohexane() {
        // 6-membered ring, all single bonds (chair-like in 3D)
        MoleculeGraph g = new MoleculeGraph();
        MoleculeAtom[] C = new MoleculeAtom[6];
        for (int i = 0; i < 6; i++) C[i] = g.addAtom("C");
        for (int i = 0; i < 6; i++) g.addBond(C[i], C[(i+1)%6], 1);
        for (MoleculeAtom c : C) addH(g, c, 2);
        return new PresetInfo("Cyclohexane", CAT_AROMATIC, "C₆H₁₂",
                "Chair (Tetrahedral C)", "109.5°", g);
    }

    // ── Helper ────────────────────────────────────────────────────────────────

    private static void addH(MoleculeGraph g, MoleculeAtom atom, int count) {
        for (int i = 0; i < count; i++) g.addBond(atom, g.addAtom("H"), 1);
    }
}