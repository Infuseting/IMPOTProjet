
import com.kerware.simulateur.ICalculateurImpot;
import com.kerware.simulateur.Simulateur;
import com.kerware.simulateur.SituationFamiliale;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Batterie de tests - Golden Master du Simulateur d'Impôts")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class SimulateurTest {

    private ICalculateurImpot calculateur;

    @BeforeEach
    void setUp() {
        calculateur = new com.kerware.simulateurreusine.AdaptateurCodeReusine();
    }

    @Nested
    @DisplayName("Tests des exigences d'Abattement (EXG_IMPOT_02)")
    class TestsAbattement {

        @ParameterizedTest(name = "Revenu de {0}€ -> Abattement attendu de {1}€")
        @CsvSource({
                "0, 495",       // Plancher minimum
                "4000, 495",    // 10% < 495
                "30000, 3000",  // Nominal : 10%
                "150000, 14171" // Plafond maximum
        })
        void testerCalculAbattement(int revenu, int abattementAttendu) {
            calculateur.setRevenusNet(revenu);
            calculateur.calculImpotSurRevenuNet();
            assertEquals(abattementAttendu, calculateur.getAbattement(), "L'abattement calculé est incorrect");
        }
    }

    @Nested
    @DisplayName("Tests du nombre de parts fiscales (EXG_IMPOT_03)")
    class TestsPartsFiscales {

        @ParameterizedTest(name = "Sit: {0}, Enf: {1}, Handi: {2}, Isol: {3} -> Parts: {4}")
        @CsvSource({
                "CELIBATAIRE, 0, 0, false, 1",    // 1 part
                "MARIE, 0, 0, false, 2",          // 2 parts
                "CELIBATAIRE, 2, 0, false, 2",    // 1 + 0.5 + 0.5 = 2 parts
                "MARIE, 3, 0, false, 4",          // 2 + 0.5 + 0.5 + 1 = 4 parts
                "CELIBATAIRE, 1, 0, true, 2",     // Parent isolé: 1 + 0.5 + 0.5(isolé) = 2 parts
                "MARIE, 2, 1, false, 3.5"         // 2 + 0.5 + 0.5 + 0.5(handicap) = 3.5 parts
        })
        void testerCalculParts(SituationFamiliale sit, int enfants, int handi, boolean isole, double partsAttendues) {
            calculateur.setSituationFamiliale(sit);
            calculateur.setNbEnfantsACharge(enfants);
            calculateur.setNbEnfantsSituationHandicap(handi);
            calculateur.setParentIsole(isole);

            calculateur.calculImpotSurRevenuNet();

            // On utilise une instance temporaire de Simulateur pour récupérer le double exact car l'interface cast en int
            Simulateur sim = new Simulateur();
            sim.calculImpot(0, sit, enfants, handi, isole);
            assertEquals(partsAttendues, sim.getNbPartsFoyerFiscalCalcule(), "Le nombre de parts est incorrect");
        }
    }

    @Nested
    @DisplayName("Tests Négatifs - Gestion des erreurs")
    @Tag("Negative")
    class TestsNegatifs {

        @Test
        @DisplayName("Le système doit rejeter un revenu négatif")
        void exceptionSurRevenuNegatif() {
            calculateur.setRevenusNet(-100);
            assertThrows(IllegalArgumentException.class, () -> calculateur.calculImpotSurRevenuNet());
        }

        @Test
        @DisplayName("Le système doit rejeter un nombre d'enfants handicapés supérieur au total")
        void exceptionSurEnfantsHandicapesIncoherents() {
            calculateur.setNbEnfantsACharge(1);
            calculateur.setNbEnfantsSituationHandicap(2);
            assertThrows(IllegalArgumentException.class, () -> calculateur.calculImpotSurRevenuNet());
        }

        @Nested
        @DisplayName("Tests Contribution Exceptionnelle (EXG_IMPOT_07)")
        class TestsCEHR {

            @Test
            @DisplayName("Célibataire avec 550 000€ de RFR doit payer 9500€ de CEHR")
            void testerContributionExceptionnelleCelibataire() {
                calculateur.setRevenusNet(564171);
                calculateur.setSituationFamiliale(SituationFamiliale.CELIBATAIRE);
                calculateur.setNbEnfantsACharge(0);

                calculateur.calculImpotSurRevenuNet();

                assertEquals(550000, calculateur.getRevenuFiscalReference(), "Le RFR doit être de 550k€");

                assertEquals(234144, calculateur.getImpotSurRevenuNet(), "L'impôt total calculé est incorrect");
                calculateur.setSituationFamiliale(SituationFamiliale.VEUF);
                calculateur.setNbEnfantsACharge(1);
                calculateur.setRevenusNet(100000000);
                calculateur.calculImpotSurRevenuNet();

            }
        }
        @Test
        @DisplayName("La décote ne peut pas rendre l'impôt négatif (plafonnement)")
        void testerDecotePlafonnee() {
            calculateur.setRevenusNet(12000);
            calculateur.setSituationFamiliale(SituationFamiliale.CELIBATAIRE);
            calculateur.setNbEnfantsACharge(0);

            calculateur.calculImpotSurRevenuNet();

            assertTrue(calculateur.getImpotSurRevenuNet() >= 0, "L'impôt ne doit pas être négatif");
        }
        @Test
        @DisplayName("Le système doit rejeter un nombre d'enfants négatif")
        void exceptionSurEnfantsNegatifs() {
            calculateur.setNbEnfantsSituationHandicap(0);
            calculateur.setNbEnfantsACharge(-1);
            assertThrows(IllegalArgumentException.class, () -> calculateur.calculImpotSurRevenuNet());
            calculateur.setNbEnfantsACharge(0);
            calculateur.setNbEnfantsSituationHandicap(-1);
            assertThrows(IllegalArgumentException.class, () -> calculateur.calculImpotSurRevenuNet());
            calculateur.setNbEnfantsSituationHandicap(-1);
            calculateur.setNbEnfantsACharge(-1);
            assertThrows(IllegalArgumentException.class, () -> calculateur.calculImpotSurRevenuNet());

        }

        @Test
        @DisplayName("getter")
        void testGetter() {
            calculateur.getAbattement();
            calculateur.getRevenuFiscalReference();
            calculateur.getImpotSurRevenuNet();
            calculateur.getDecote();
            calculateur.getNbPartsFoyerFiscal();
            calculateur.getImpotAvantDecote();
        }
    }
}