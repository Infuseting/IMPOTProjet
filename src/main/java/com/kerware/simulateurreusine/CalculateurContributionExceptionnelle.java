package com.kerware.simulateurreusine;


import com.kerware.simulateur.SituationFamiliale;

public class CalculateurContributionExceptionnelle {

    public int calculer(int revenuFiscalReference, SituationFamiliale sitFam) {
        double contribution = 0.0;

        // Déterminer si c'reest une imposition commune (couple) ou seule
        boolean estCouple = (sitFam == SituationFamiliale.MARIE || sitFam == SituationFamiliale.PACSE);

        double seuil1 = estCouple ? Bareme2024.SEUIL_CEHR_COUPLE_1 : Bareme2024.SEUIL_CEHR_SEUL_1;
        double seuil2 = estCouple ? Bareme2024.SEUIL_CEHR_COUPLE_2 : Bareme2024.SEUIL_CEHR_SEUL_2;

        // Calcul par tranches de la contribution
        if (revenuFiscalReference > seuil1) {
            double base3Pourcent = Math.min(revenuFiscalReference, seuil2) - seuil1;
            contribution += base3Pourcent * Bareme2024.TAUX_CEHR_3_POURCENT;
        }

        if (revenuFiscalReference > seuil2) {
            double base4Pourcent = revenuFiscalReference - seuil2;
            contribution += base4Pourcent * Bareme2024.TAUX_CEHR_4_POURCENT;
        }

        return (int) Math.round(contribution);
    }
}