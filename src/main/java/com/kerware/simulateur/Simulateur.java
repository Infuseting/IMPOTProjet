package com.kerware.simulateur;

/**
 *  Cette classe permet de simuler le calcul de l'impôt sur le revenu
 *  en France pour l'année 2024 sur les revenus de l'année 2023 pour
 *  des cas simples de contribuables célibataires, mariés, divorcés, veufs
 *  ou pacsés avec ou sans enfants à charge ou enfants en situation de handicap
 *  et parent isolé.
 *
 *  EXEMPLE DE CODE DE TRES MAUVAISE QUALITE FAIT PAR UN DEBUTANT
 *  Pas de lisibilité, pas de commentaires, pas de tests
 *  Pas de documentation, pas de gestion des erreurs
 *  Pas de logique métier, pas de modularité
 *  Pas de gestion des exceptions, pas de gestion des logs
 *  Principe "Single Responsability" non respecté
 *  Pas de traçabilité vers les exigences métier
 *  Pourtant ce code fonctionne correctement
 *  Il s'agit d'un "legacy" code qui est difficile à maintenir
 *  L'auteur n'a pas fourni de tests unitaires
 **/
public class Simulateur {

    // Constantes pour les tailles de tableaux
    private static final int LIMITE_TAILLE = 6;
    private static final int TAUX_TAILLE = 5;

    private static final int INDICE_0 = 0;
    private static final int INDICE_1 = 1;
    private static final int INDICE_2 = 2;
    private static final int INDICE_3 = 3;
    private static final int INDICE_4 = 4;
    private static final int INDICE_5 = 5;

    private static final int REVNET_1 = 65000;
    private static final int NBENF_1 = 3;
    private static final int REVNET_2 = 35000;
    private static final int NBENF_2 = 1;
    private static final int NBENF_3 = 2;
    private static final int REVNET_3 = 50000;
    private static final int NBENF_4 = 3;
    private static final int REVNET_4 = 200000;

    private final double[] taux = new double[TAUX_TAILLE];
    private final int[] limites = new int[LIMITE_TAILLE];

    private double abt = 0;
    private double nbPtsDecl = 0;
    private double nbPts = 0;
    private double decote = 0;
    private double mImp = 0;
    private double rFRef = 0;

    public long calculImpot(int revNet, SituationFamiliale sitFam,
                            int nbEnfants, int nbEnfantsHandicapes,
                            boolean parentIsol) {
        final int l00 = 0;
        final int l01 = 11294;
        final int l02 = 28797;
        final int l03 = 82341;
        final int l04 = 177106;
        final int l05 = Integer.MAX_VALUE;
        final double t00 = 0.0;
        final double t01 = 0.11;
        final double t02 = 0.3;
        final double t03 = 0.41;
        final double t04 = 0.45;
        final int lAbtMax = 14171;
        final int lAbtMin = 495;
        final double tAbt = 0.1;
        final double plafDemiPart = 1759;
        final double seuilDecoteDeclarantSeul = 1929;
        final double seuilDecoteDeclarantCouple = 3191;
        final double decoteMaxDeclarantSeul = 873;
        final double decoteMaxDeclarantCouple = 1444;
        final double tauxDecote = 0.4525;

        // Initialisation des tableaux
        limites[INDICE_0] = l00;
        limites[INDICE_1] = l01;
        limites[INDICE_2] = l02;
        limites[INDICE_3] = l03;
        limites[INDICE_4] = l04;
        limites[INDICE_5] = l05;

        taux[INDICE_0] = t00;
        taux[INDICE_1] = t01;
        taux[INDICE_2] = t02;
        taux[INDICE_3] = t03;
        taux[INDICE_4] = t04;

        // Abattement
        abt = revNet * tAbt;
        if (revNet < 0) {
            throw new IllegalArgumentException("Le revenu net ne peut pas être négatif");
        }
        if (nbEnfants < 0 || nbEnfantsHandicapes < 0) {
            throw new IllegalArgumentException("Le nombre d'enfants ne peut pas être négatif");
        }
        if (nbEnfantsHandicapes > nbEnfants) {
            throw new IllegalArgumentException("Le nombre d'enfants handicapés ne peut pas excéder le nombre total d'enfants");
        }
        if (abt > lAbtMax) {
            abt = lAbtMax;
        }
        if (abt < lAbtMin) {
            abt = lAbtMin;
        }
        rFRef = revNet - abt;

        // parts déclarants
        switch ( sitFam ) {
            case CELIBATAIRE:
                nbPtsDecl = 1;
                break;
            case MARIE:
                nbPtsDecl = 2;
                break;
            case DIVORCE:
                nbPtsDecl = 1;
                break;
            case VEUF:
                if ( nbEnfants == 0 ) {
                    nbPtsDecl = 1;
                } else {
                    nbPtsDecl = 2;
                }
                break;
        }

        // parts enfants à charge
        if ( nbEnfants <= 2 ) {
            nbPts = nbPtsDecl + nbEnfants * 0.5;
        } else {
            nbPts = nbPtsDecl + 1.0 + ( nbEnfants - 2 );
        }

        // parent isolé
        if ( parentIsol ) {
            if ( nbEnfants > 0 ) {
                nbPts = nbPts + 0.5;
            }
        }

        // enfant handicapé
        nbPts = nbPts + nbEnfantsHandicapes * 0.5;

        // impôt des declarants
        double rImposable = rFRef / nbPtsDecl;
        double mImpDecl = 0;
        int i = 0;
        do {
            if ( rImposable >= limites[i] && rImposable < limites[i+1] ) {
                mImpDecl += ( rImposable - limites[i] ) * taux[i];
                break;
            } else {
                mImpDecl += ( limites[i+1] - limites[i] ) * taux[i];
            }
            i++;
        } while( i < TAUX_TAILLE);
        mImpDecl = mImpDecl * nbPtsDecl;
        mImpDecl = Math.round( mImpDecl );

        // impôt foyer fiscal complet
        rImposable =  rFRef / nbPts;
        mImp = 0;
        i = 0;
        do {
            if ( rImposable >= limites[i] && rImposable < limites[i+1] ) {
                mImp += ( rImposable - limites[i] ) * taux[i];
                break;
            } else {
                mImp += ( limites[i+1] - limites[i] ) * taux[i];
            }
            i++;
        } while( i < TAUX_TAILLE);
        mImp = mImp * nbPts;
        mImp = Math.round( mImp );

        // baisse impot
        double baisseImpot = mImpDecl - mImp;
        // dépassement plafond
        double ecartPts = nbPts - nbPtsDecl;
        double plafond = (ecartPts / 0.5) * plafDemiPart;
        if ( baisseImpot >= plafond ) {
            mImp = mImpDecl - plafond;
        }
        decote = 0;
        // decote
        if ( nbPtsDecl == 1 ) {
            if ( mImp < seuilDecoteDeclarantSeul ) {
                decote = decoteMaxDeclarantSeul - ( mImp  * tauxDecote );
            }
        }
        if (  nbPtsDecl == 2 ) {
            if ( mImp < seuilDecoteDeclarantCouple ) {
                decote =  decoteMaxDeclarantCouple - ( mImp  * tauxDecote  );
            }
        }
        decote = Math.round( decote );
        if ( mImp <= decote ) {
            decote = mImp;
        }
        mImp = mImp - decote;
        return Math.round( mImp );
    }

    public static void main(String[] args) {
        Simulateur simulateur = new Simulateur();
        long impot = simulateur.calculImpot(REVNET_1, SituationFamiliale.MARIE, NBENF_1, 0, false);
        System.out.println("Impot sur le revenu net : " + impot);
        impot = simulateur.calculImpot(REVNET_1, SituationFamiliale.MARIE, NBENF_1, 1, false);
        System.out.println("Impot sur le revenu net : " + impot);
        impot = simulateur.calculImpot(REVNET_2, SituationFamiliale.DIVORCE, NBENF_2, 0, true);
        System.out.println("Impot sur le revenu net : " + impot);
        impot = simulateur.calculImpot(REVNET_2, SituationFamiliale.DIVORCE, NBENF_3, 0, true);
        System.out.println("Impot sur le revenu net : " + impot);
        impot = simulateur.calculImpot(REVNET_3, SituationFamiliale.DIVORCE, NBENF_4, 0, true);
        System.out.println("Impot sur le revenu net : " + impot);
        impot = simulateur.calculImpot(REVNET_3, SituationFamiliale.DIVORCE, NBENF_4, 1, true);
        System.out.println("Impot sur le revenu net : " + impot);
        impot = simulateur.calculImpot(REVNET_4, SituationFamiliale.CELIBATAIRE, 0, 0, true);
        System.out.println("Impot sur le revenu net : " + impot);
    }
    public int getAbattementCalcule() { return (int) Math.round(abt); }
    public int getRevenuFiscalReferenceCalcule() { return (int) Math.round(rFRef); }
    public double getNbPartsFoyerFiscalCalcule() { return nbPts; }
    public int getImpotAvantDecoteCalcule() { return (int) Math.round(mImp + decote); } // Approximation basée sur la logique legacy
    public int getDecoteCalcule() { return (int) Math.round(decote); }
    public int getImpotFinalCalcule() { return (int) Math.round(mImp); }
}
