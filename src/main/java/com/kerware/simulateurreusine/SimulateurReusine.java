package com.kerware.simulateurreusine;

import com.kerware.simulateur.SituationFamiliale;

public class SimulateurReusine {

    private int abattementCalcule;
    private int revenuFiscalReference;
    private double partsFoyer;
    private int impotAvantDecote;
    private int decoteCalculee;

    public long calculImpot(int revNet, SituationFamiliale sitFam,
                            int nbEnfants, int nbEnfantsHandicapes,
                            boolean parentIsol) {

        if (revNet < 0) {
            throw new IllegalArgumentException("Le revenu net ne peut pas être négatif");
        }
        if (nbEnfants < 0 || nbEnfantsHandicapes < 0) {
            throw new IllegalArgumentException("Nombre d'enfants invalide");
        }
        if (nbEnfantsHandicapes > nbEnfants) {
            throw new IllegalArgumentException(
                    "Le nombre d'enfants handicapés ne peut pas excéder le total"
            );
        }
        // 1. Abattement et RFR (EXG_IMPOT_02)
        CalculateurAbattement calcAbattement = new CalculateurAbattement();
        this.abattementCalcule = calcAbattement.calculer(revNet);
        this.revenuFiscalReference = calcAbattement.calculerRevenuFiscalReference(revNet, this.abattementCalcule);

        // 2. Calcul des parts (EXG_IMPOT_03)
        CalculateursParts calcParts = new CalculateursParts();
        double partsDeclarant = calcParts.calculerPartsDeclarants(sitFam, nbEnfants);
        double partsEnfants = calcParts.calculerPartsEnfants(nbEnfants, nbEnfantsHandicapes, parentIsol);
        this.partsFoyer = partsDeclarant + partsEnfants;

        // 3. Calcul de l'impôt brut par tranches (EXG_IMPOT_04)
        CalculateursTranches calcTranches = new CalculateursTranches();
        int impotSansEnfants = calcTranches.calculerImpotBrut(this.revenuFiscalReference, partsDeclarant);
        int impotAvecEnfants = calcTranches.calculerImpotBrut(this.revenuFiscalReference, this.partsFoyer);

        // 4. Plafonnement du quotient familial (EXG_IMPOT_05)
        double gainFiscal = impotSansEnfants - impotAvecEnfants;
        double plafondMax = (partsEnfants / 0.5) * Bareme2024.PLAFOND_DEMI_PART; // [cite: 227]

        this.impotAvantDecote = impotAvecEnfants;
        if (gainFiscal > plafondMax) {
            this.impotAvantDecote = (int) Math.round(impotSansEnfants - plafondMax);
        }

        CalculateursDecote calcDecote = new CalculateursDecote();
        this.decoteCalculee = calcDecote.calculer(this.impotAvantDecote, partsDeclarant);

        int impotFinal = Math.max(0, this.impotAvantDecote - this.decoteCalculee);

        CalculateurContributionExceptionnelle calcCEHR = new CalculateurContributionExceptionnelle();
        int contributionExceptionnelle = calcCEHR.calculer(this.revenuFiscalReference, sitFam);

        return impotFinal + contributionExceptionnelle;
    }

    public int getAbattement() { return this.abattementCalcule; }
    public int getRevenuFiscalReference() { return this.revenuFiscalReference; }
    public double getNbPartsFoyerFiscal() { return this.partsFoyer; }
    public int getImpotAvantDecote() { return this.impotAvantDecote; }
    public int getDecote() { return this.decoteCalculee; }
}