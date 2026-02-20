package com.kerware.simulateurreusine;

public class CalculateursTranches {

    public int calculerImpotBrut(double revenuFiscalReference, double nombreParts) {
        double quotient = revenuFiscalReference / nombreParts;
        double impot = 0.0;
        final int TRANCHE_4 = 3;
        if (quotient > Bareme2024.TRANCHES_REVENU[0]) {
            impot += (Math.min(quotient, Bareme2024.TRANCHES_REVENU[1])
                    - Bareme2024.TRANCHES_REVENU[0]) * Bareme2024.TRANCHES_TAUX[0];
        }
        if (quotient > Bareme2024.TRANCHES_REVENU[1]) {
            impot += (Math.min(quotient, Bareme2024.TRANCHES_REVENU[2])
                    - Bareme2024.TRANCHES_REVENU[1]) * Bareme2024.TRANCHES_TAUX[1];
        }
        if (quotient > Bareme2024.TRANCHES_REVENU[2]) {
            impot += (Math.min(quotient, Bareme2024.TRANCHES_REVENU[TRANCHE_4])
                    - Bareme2024.TRANCHES_REVENU[2]) * Bareme2024.TRANCHES_TAUX[2];
        }
        if (quotient > Bareme2024.TRANCHES_REVENU[TRANCHE_4]) {
            impot += (quotient - Bareme2024.TRANCHES_REVENU[TRANCHE_4])
                    * Bareme2024.TRANCHES_TAUX[TRANCHE_4];
        }

        return (int) Math.round(impot * nombreParts);
    }
}
