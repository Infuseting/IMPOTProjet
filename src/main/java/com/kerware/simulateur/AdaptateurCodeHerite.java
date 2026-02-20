package com.kerware.simulateur;

public class AdaptateurCodeHerite implements ICalculateurImpot {

    private Simulateur simulateur;

    // Paramètres en attente d'exécution
    private int revenusNet = 0;
    private SituationFamiliale situationFamiliale = SituationFamiliale.CELIBATAIRE;
    private int nbEnfantsACharge = 0;
    private int nbEnfantsHandicapes = 0;
    private boolean parentIsole = false;

    public AdaptateurCodeHerite() {
        this.simulateur = new Simulateur();
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
        simulateur.calculImpot(revenusNet, situationFamiliale, nbEnfantsACharge, nbEnfantsHandicapes, parentIsole);
    }

    @Override
    public int getRevenuFiscalReference() { return simulateur.getRevenuFiscalReferenceCalcule(); }

    @Override
    public int getAbattement() { return simulateur.getAbattementCalcule(); }

    @Override
    public int getNbPartsFoyerFiscal() {
        return (int) simulateur.getNbPartsFoyerFiscalCalcule();
    }

    @Override
    public int getImpotAvantDecote() { return simulateur.getImpotAvantDecoteCalcule(); }

    @Override
    public int getDecote() { return simulateur.getDecoteCalcule(); }

    @Override
    public int getImpotSurRevenuNet() { return simulateur.getImpotFinalCalcule(); }
}