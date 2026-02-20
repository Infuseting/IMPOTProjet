package com.kerware.simulateurreusine;


import com.kerware.simulateur.SituationFamiliale;

public class CalculateursParts {

    public double calculerPartsDeclarants(SituationFamiliale sitFam, int nbEnfants) {
        switch (sitFam) {
            case MARIE:
            case PACSE:
                return 2.0;
            case VEUF:
                return nbEnfants > 0 ? 2.0 : 1.0;
            default:
                return 1.0;
        }
    }

    public double calculerPartsEnfants(int nbEnfants, int nbEnfantsHandicapes, boolean parentIsole) {
        double parts = 0.0;

        if (nbEnfants > 0) {
            if (nbEnfants <= 2) {
                parts += nbEnfants * 0.5;
            } else {
                parts += 1.0 + (nbEnfants - 2) * 1.0;
            }
        }

        parts += nbEnfantsHandicapes * 0.5;

        if (parentIsole && nbEnfants > 0) {
            parts += 0.5;
        }

        return parts;
    }
}
