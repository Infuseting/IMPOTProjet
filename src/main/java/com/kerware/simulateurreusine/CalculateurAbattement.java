package com.kerware.simulateurreusine;

public class CalculateurAbattement {

    /**
     * Calcule l'abattement de 10% sur les revenus nets (EXG_IMPOT_02).
     * @param revenuNet Le revenu net du foyer
     * @return Le montant de l'abattement calculé
     */
    public int calculer(int revenuNet) {
        double abattement = revenuNet * Bareme2024.TAUX_ABATTEMENT;

        if (abattement > Bareme2024.PLAFOND_ABATTEMENT) {
            abattement = Bareme2024.PLAFOND_ABATTEMENT;
        } else if (abattement < Bareme2024.PLANCHER_ABATTEMENT) {
            abattement = Bareme2024.PLANCHER_ABATTEMENT;
        }

        return (int) Math.round(abattement);
    }

    public int calculerRevenuFiscalReference(int revenuNet, int abattement) {
        return revenuNet - abattement;
    }
}