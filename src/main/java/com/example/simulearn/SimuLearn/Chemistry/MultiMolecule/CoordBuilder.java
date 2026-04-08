package com.example.simulearn.SimuLearn.Chemistry.MultiMolecule;

import javafx.geometry.Point3D;

public class CoordBuilder {

    public static void build(MoleculeGraph graph, String moleculeName) {
        if (graph.atomCount() == 0) return;
        for (MoleculeAtom a : graph.getAtoms()) a.position = Point3D.ZERO;

        if (moleculeName != null && OrganicCoordBuilder.apply(moleculeName, graph)) return;

        if (moleculeName != null && InorganicCoordBuilder.apply(moleculeName, graph)) return;

        if (moleculeName != null && AromaticCoordBuilder.apply(moleculeName, graph)) return;

    }

}