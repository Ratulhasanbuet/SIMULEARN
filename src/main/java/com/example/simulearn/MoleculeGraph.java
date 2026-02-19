package com.example.simulearn;

import java.util.*;

public class MoleculeGraph {

    private final List<MoleculeAtom> atoms = new ArrayList<>();
    private final List<MoleculeBond> bonds = new ArrayList<>();
    private int nextId = 0;

    // ── Atom management ───────────────────────────────────────────────────────

    public MoleculeAtom addAtom(String symbol) {
        MoleculeAtom a = new MoleculeAtom(nextId++, symbol);
        atoms.add(a);
        return a;
    }
    // ── Bond management ───────────────────────────────────────────────────────

    public MoleculeBond addBond(MoleculeAtom a, MoleculeAtom b, int order) {
        // update if already bonded
        for (MoleculeBond existing : bonds) {
            if (existing.connects(a, b)) {
                existing.order = order;
                return existing;
            }
        }
        MoleculeBond bond = new MoleculeBond(a, b, order);
        bonds.add(bond);
        a.bonds.add(bond);
        b.bonds.add(bond);
        return bond;
    }

    public List<MoleculeAtom> getAtoms() { return Collections.unmodifiableList(atoms); }
    public int atomCount() { return atoms.size(); }
}
