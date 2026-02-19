
package com.example.simulearn;

import javafx.geometry.Point3D;
import java.util.*;

public class CoordBuilder {

    public static void build(MoleculeGraph graph, String moleculeName) {
        if (graph.atomCount() == 0) return;
        for (MoleculeAtom a : graph.getAtoms()) a.position = Point3D.ZERO;

        // Organic molecules: use hardcoded correct positions
        if (moleculeName != null && OrganicCoordBuilder.apply(moleculeName, graph)) return;

        // Inorganic molecules: use VSEPR-based positions
        if (moleculeName != null && InorganicCoordBuilder.apply(moleculeName, graph)) return;

        // Aromatic / cyclic molecules: hardcoded positions
        if (moleculeName != null && AromaticCoordBuilder.apply(moleculeName, graph)) return;

    }

}