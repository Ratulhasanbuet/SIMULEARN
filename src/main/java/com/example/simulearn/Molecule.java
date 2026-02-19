

package com.example.simulearn;

import java.util.ArrayList;
import java.util.List;

public class Molecule {
    String centralAtom = "C";
    List<String> bondedAtoms = new ArrayList<>();
    List<Integer> bondTypes = new ArrayList<>();
    int lonePairs = 0;

    // Track the order of additions: "atom" or "lonepair"
    private List<String> additionOrder = new ArrayList<>();

    void setCentralAtom(String atom) {
        this.centralAtom = atom;
    }

    void addAtom(String atom, int bondType) {
        bondedAtoms.add(atom);
        bondTypes.add(bondType);
        additionOrder.add("atom");
    }

    void addLonePair() {
        lonePairs++;
        additionOrder.add("lonepair");
    }

    void removeLast() {
        if (additionOrder.isEmpty()) {
            return; // Nothing to remove
        }

        String lastAddition = additionOrder.remove(additionOrder.size() - 1);

        if (lastAddition.equals("atom")) {
            // Remove last atom
            if (!bondedAtoms.isEmpty()) {
                bondedAtoms.remove(bondedAtoms.size() - 1);
                bondTypes.remove(bondTypes.size() - 1);
            }
        } else if (lastAddition.equals("lonepair")) {
            // Remove last lone pair
            if (lonePairs > 0) {
                lonePairs--;
            }
        }
    }

    void clear() {
        bondedAtoms.clear();
        bondTypes.clear();
        lonePairs = 0;
        additionOrder.clear();
    }

    VSEPRGeometry getGeometry() {
        int bondedCount = bondedAtoms.size();
        int stericNumber = bondedCount + lonePairs;
        return VSEPRCalculator.calculate(stericNumber, lonePairs, bondedCount);
    }
}