package com.example.tps

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.espresso.Espresso
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.tps.ui.ProductExplorerApp
import com.example.tps.ui.theme.TPsTheme
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NavigationTest {

    @get:Rule
    val rule = createAndroidComposeRule<ComponentActivity>()

    @Before
    fun setUp() {
        rule.setContent {
            TPsTheme {
                ProductExplorerApp()
            }
        }
    }

    @Test
    fun l_application_demarre_sur_le_catalogue() {
        rule.onNodeWithText("Catalogue produits").assertIsDisplayed()
    }

    @Test
    fun voir_le_produit_ouvre_le_detail_et_retour_revient_au_catalogue() {
        rule.onAllNodesWithText("Voir le produit").onFirst().performClick()
        rule.onNodeWithText("Description").assertIsDisplayed()
        rule.onNodeWithText("Ajouter au panier").assertExists()

        rule.onNodeWithContentDescription("Retour").performClick()
        rule.onNodeWithText("Catalogue produits").assertIsDisplayed()
    }

    @Test
    fun produit_en_rupture_ne_peut_pas_etre_ajoute_au_panier() {
        // Le casque (2e produit) est en rupture
        rule.onAllNodesWithText("Voir le produit")[1].performClick()
        rule.onNodeWithText("Produit indisponible").performScrollTo().assertIsNotEnabled()
    }

    @Test
    fun ajouter_au_panier_affiche_une_confirmation() {
        rule.onAllNodesWithText("Voir le produit").onFirst().performClick()
        rule.onNodeWithText("Ajouter au panier").performScrollTo().performClick()
        rule.onNodeWithText("Smartphone Toto X ajouté au panier").assertIsDisplayed()
    }

    @Test
    fun favori_partage_entre_le_detail_et_le_catalogue() {
        rule.onAllNodesWithText("Voir le produit").onFirst().performClick()
        rule.onNodeWithContentDescription("Ajouter aux favoris").performClick()
        rule.onNodeWithContentDescription("Retirer des favoris").assertIsDisplayed()

        rule.onNodeWithContentDescription("Retour").performClick()
        rule.onNodeWithText("Favoris (1)").assertIsDisplayed()
        rule.onAllNodesWithContentDescription("Retirer des favoris").assertCountEquals(1)
    }

    @Test
    fun categorie_de_l_accueil_ouvre_le_catalogue_filtre() {
        rule.onNodeWithText("Accueil").performClick()
        rule.onNodeWithText("Maison").performClick()
        rule.onNodeWithText("Catalogue produits").assertIsDisplayed()
        rule.onNodeWithText("2 produits affichés").assertIsDisplayed()
        rule.onNodeWithText("Maison").assertIsSelected()
    }

    @Test
    fun retour_systeme_ferme_le_detail() {
        rule.onAllNodesWithText("Voir le produit").onFirst().performClick()
        rule.onNodeWithText("Description").assertIsDisplayed()

        Espresso.pressBack()
        rule.onNodeWithText("Catalogue produits").assertIsDisplayed()
    }

    @Test
    fun onglet_accueil_puis_produit_mis_en_avant() {
        rule.onNodeWithText("Accueil").performClick()
        rule.onNodeWithText("Produit mis en avant").assertIsDisplayed()

        rule.onNodeWithText("Voir le produit").performClick()
        rule.onNodeWithText("Description").assertIsDisplayed()

        // Le retour ramène à l'onglet d'où l'on vient
        rule.onNodeWithContentDescription("Retour").performClick()
        rule.onNodeWithText("Produit mis en avant").assertIsDisplayed()
    }

    @Test
    fun la_recherche_est_conservee_en_changeant_d_onglet() {
        rule.onNodeWithText("Rechercher un produit").performTextInput("montre")
        rule.onNodeWithText("1 produit affiché").assertIsDisplayed()

        rule.onNodeWithText("Accueil").performClick()
        rule.onNodeWithText("Catalogue").performClick()
        rule.onNodeWithText("1 produit affiché").assertIsDisplayed()
    }

    @Test
    fun la_barre_de_recherche_de_l_accueil_ouvre_le_catalogue() {
        rule.onNodeWithText("Accueil").performClick()
        rule.onNodeWithText("Rechercher un produit…").performClick()
        rule.onNodeWithText("Catalogue produits").assertIsDisplayed()
    }
}
