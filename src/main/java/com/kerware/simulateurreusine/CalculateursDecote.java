package com.kerware.simulateurreusine;

public class CalculateursDecote {

    public int calculer(int impotBrut, double partsDeclarants) {
        double decote = 0.0;

        if (partsDeclarants == 1.0 && impotBrut < Bareme2024.SEUIL_DECOTE_SEUL) { // Personne seule [cite: 231]
            decote = Bareme2024.DECOTE_MAX_SEUL - (impotBrut * Bareme2024.TAUX_DECOTE); // [cite: 235]
        } else if (partsDeclarants == 2.0 && impotBrut < Bareme2024.SEUIL_DECOTE_COUPLE) { // Couple [cite: 230]
            decote = Bareme2024.DECOTE_MAX_COUPLE - (impotBrut * Bareme2024.TAUX_DECOTE); // [cite: 234]
        }

        return (int) Math.max(0, Math.round(decote));
    }
}