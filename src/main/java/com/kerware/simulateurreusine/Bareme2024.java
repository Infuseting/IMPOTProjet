package com.kerware.simulateurreusine;

public class Bareme2024{
    public static final double TAUX_ABATTEMENT = 0.10;
    public static final int PLAFOND_ABATTEMENT = 14171;
    public static final int PLANCHER_ABATTEMENT = 495;

    public static final double[] TRANCHES_REVENU = {11294, 28797, 82341, 177106};
    public static final double[] TRANCHES_TAUX = {0.11, 0.30, 0.41, 0.45};

    public static final double PLAFOND_DEMI_PART = 1759.0;

    public static final double SEUIL_DECOTE_SEUL = 1929.0;
    public static final double SEUIL_DECOTE_COUPLE = 3191.0;
    public static final double DECOTE_MAX_SEUL = 873.0;
    public static final double DECOTE_MAX_COUPLE = 1444.0;
    public static final double TAUX_DECOTE = 0.4525;

    public static final double SEUIL_CEHR_SEUL_1 = 250000.0;
    public static final double SEUIL_CEHR_SEUL_2 = 500000.0;
    public static final double SEUIL_CEHR_COUPLE_1 = 500000.0;
    public static final double SEUIL_CEHR_COUPLE_2 = 1000000.0;
    public static final double TAUX_CEHR_3_POURCENT = 0.03;
    public static final double TAUX_CEHR_4_POURCENT = 0.04;
}
