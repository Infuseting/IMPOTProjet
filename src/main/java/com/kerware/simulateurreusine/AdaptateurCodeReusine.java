package com.kerware.simulateurreusine;


import com.kerware.simulateur.ICalculateurImpot;
import com.kerware.simulateur.SituationFamiliale;

public class AdaptateurCodeReusine implements ICalculateurImpot {

    private SimulateurReusine simulateur;

    // Paramètres en attente d'exécution
    private int revenusNet = 0;
    private SituationFamiliale situationFamiliale = SituationFamiliale.CELIBATAIRE;
    private int nbEnfantsACharge = 0;
    private int nbEnfantsHandicapes = 0;
    private boolean parentIsole = false;

    // Stockage du résultat final pour l'interface
    private int impotFinalCalcule = 0;

    public AdaptateurCodeReusine() {
        this.simulateur = new SimulateurReusine();
    }

    @Override
    public void setRevenusNet(int rn) { this.revenusNet = rn; }

    @Override
    public void setSituationFamiliale(SituationFamiliale sf) { this.situationFamiliale = sf; }

    @Override
    public void setNbEnfantsACharge(int nbe) { this.nbEnfantsACharge = nbe; }

    @Override
    public void setNbEnfantsSituationHandicap(int nbesh) { this.nbEnfantsHandicapes = nbesh; }

    @Override
    public void setParentIsole(boolean pi) { this.parentIsole = pi; }

    @Override
    public void calculImpotSurRevenuNet() {
        long resultat = simulateur.calculImpot(
                revenusNet,
                situationFamiliale,
                nbEnfantsACharge,
                nbEnfantsHandicapes,
                parentIsole
        );
        this.impotFinalCalcule = (int) resultat;
    }

    @Override
    public int getRevenuFiscalReference() { return simulateur.getRevenuFiscalReference(); }

    @Override
    public int getAbattement() { return simulateur.getAbattement(); }

    @Override
    public int getNbPartsFoyerFiscal() { return (int) simulateur.getNbPartsFoyerFiscal(); }

    @Override
    public int getImpotAvantDecote() { return simulateur.getImpotAvantDecote(); }

    @Override
    public int getDecote() { return simulateur.getDecote(); }

    @Override
    public int getImpotSurRevenuNet() { return this.impotFinalCalcule; }
}