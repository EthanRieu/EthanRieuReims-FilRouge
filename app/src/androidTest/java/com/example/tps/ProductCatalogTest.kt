package com.example.tps

import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotSelected
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.tps.data.sampleCategories
import com.example.tps.data.sampleProducts
import com.example.tps.ui.catalog.ProductCatalogContainer
import com.example.tps.ui.catalog.ProductCatalogScreen
import com.example.tps.ui.catalog.SortOrder
import com.example.tps.ui.theme.TPsTheme
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Tests d'interface du catalogue : le container (avec son état) et l'écran stateless.
 * Données : 6 produits, dont 2 en rupture (Casque Audio Pulse, Aspirateur Robot CleanBot).
 */
@RunWith(AndroidJUnit4::class)
class ProductCatalogTest {

    @get:Rule
    val rule = createComposeRule()

    private fun setCatalog() {
        rule.setContent {
            TPsTheme {
                ProductCatalogContainer(
                    products = sampleProducts(),
                    categories = sampleCategories(),
                    onProductClick = {}
                )
            }
        }
    }

    // Pluriel français : 0 et 1 au singulier
    private fun assertCount(count: Int) {
        val text = if (count <= 1) "$count produit affiché" else "$count produits affichés"
        rule.onNodeWithText(text).assertIsDisplayed()
    }

    @Test
    fun affiche_tous_les_produits_au_depart() {
        setCatalog()
        rule.onNodeWithText("Catalogue produits").assertIsDisplayed()
        assertCount(6)
    }

    @Test
    fun recherche_par_titre_marque_et_categorie() {
        setCatalog()
        val field = rule.onNodeWithText("Rechercher un produit")

        field.performTextInput("toto")          // titre / marque
        assertCount(1)

        field.performTextClearance()
        field.performTextInput("AUDIO")          // catégorie, insensible à la casse
        assertCount(2)

        field.performTextClearance()
        field.performTextInput("introuvable")
        assertCount(0)

        field.performTextClearance()
        assertCount(6)
    }

    @Test
    fun bouton_effacer_vide_la_recherche() {
        setCatalog()
        rule.onNodeWithContentDescription("Effacer la recherche").assertDoesNotExist()
        rule.onNodeWithText("Rechercher un produit").performTextInput("toto")
        assertCount(1)

        rule.onNodeWithContentDescription("Effacer la recherche").performClick()
        assertCount(6)
    }

    @Test
    fun aucun_resultat_puis_reinitialiser_les_filtres() {
        setCatalog()
        rule.onNodeWithText("Maison").performClick()
        rule.onNodeWithText("Rechercher un produit").performTextInput("casque")
        assertCount(0)
        rule.onNodeWithText("Aucun produit ne correspond à votre recherche.").assertIsDisplayed()

        rule.onNodeWithText("Réinitialiser les filtres").performClick()
        assertCount(6)
        rule.onNodeWithText("Maison").assertIsNotSelected()
    }

    @Test
    fun filtre_stock_active_puis_desactive() {
        setCatalog()
        val stockChip = rule.onNodeWithText("En stock uniquement")

        stockChip.performClick()
        stockChip.assertIsSelected()
        assertCount(4)
        rule.onAllNodesWithText("Casque Audio Pulse").assertCountEquals(0)

        stockChip.performClick()
        stockChip.assertIsNotSelected()
        assertCount(6)
    }

    @Test
    fun filtre_par_categorie_et_deselection() {
        setCatalog()
        rule.onNodeWithText("Maison").performClick()
        assertCount(2)

        // Une seule catégorie active à la fois
        rule.onNodeWithText("Audio").performClick()
        assertCount(2)
        rule.onAllNodesWithText("Lampe Connectée Lumio").assertCountEquals(0)

        // Re-cliquer désélectionne
        rule.onNodeWithText("Audio").performClick()
        assertCount(6)
    }

    @Test
    fun filtres_combines_categorie_et_stock() {
        setCatalog()
        rule.onNodeWithText("Audio").performClick()
        rule.onNodeWithText("En stock uniquement").performClick()
        assertCount(1)
        rule.onNodeWithText("Enceinte Mini Boom").assertIsDisplayed()
    }

    @Test
    fun tri_par_prix_fait_remonter_les_produits_concernes() {
        setCatalog()
        // Par défaut, la lampe (5e) et l'aspirateur (6e) sont hors de l'écran
        rule.onNodeWithText("Lampe Connectée Lumio").assertDoesNotExist()

        rule.onNodeWithText("Par défaut").performClick()
        rule.onNodeWithText("Prix croissant").performClick()
        // La moins chère (39,99 €) passe en tête
        rule.onNodeWithText("Lampe Connectée Lumio").assertIsDisplayed()

        rule.onNodeWithText("Prix croissant").performClick()
        rule.onNodeWithText("Prix décroissant").performClick()
        // Les deux plus chers : smartphone (699,99 €) puis aspirateur (249,99 €)
        rule.onNodeWithText("Smartphone Toto X").assertIsDisplayed()
        rule.onNodeWithText("Aspirateur Robot CleanBot").assertIsDisplayed()
    }

    @Test
    fun ajout_puis_retrait_d_un_favori() {
        setCatalog()
        rule.onNodeWithContentDescription("Retirer des favoris").assertDoesNotExist()

        rule.onAllNodesWithContentDescription("Ajouter aux favoris").onFirst().performClick()
        rule.onAllNodesWithContentDescription("Retirer des favoris").assertCountEquals(1)
        rule.onNodeWithText("Favoris (1)").assertIsDisplayed()

        rule.onNodeWithContentDescription("Retirer des favoris").performClick()
        rule.onNodeWithContentDescription("Retirer des favoris").assertDoesNotExist()
        rule.onNodeWithText("Favoris (0)").assertIsDisplayed()
    }

    @Test
    fun filtre_favoris_uniquement() {
        setCatalog()
        rule.onAllNodesWithContentDescription("Ajouter aux favoris").onFirst().performClick()
        rule.onNodeWithText("Favoris (1)").performClick()
        assertCount(1)
        rule.onNodeWithText("Smartphone Toto X").assertIsDisplayed()

        // Retirer le seul favori vide la liste filtrée
        rule.onNodeWithContentDescription("Retirer des favoris").performClick()
        assertCount(0)
    }

    @Test
    fun favori_conserve_quand_le_produit_est_masque_puis_reaffiche() {
        setCatalog()
        // Le casque (hors stock) est le 2e produit
        rule.onAllNodesWithContentDescription("Ajouter aux favoris")[1].performClick()
        rule.onNodeWithText("En stock uniquement").performClick()
        rule.onNodeWithContentDescription("Retirer des favoris").assertDoesNotExist()
        rule.onNodeWithText("En stock uniquement").performClick()
        rule.onAllNodesWithContentDescription("Retirer des favoris").assertCountEquals(1)
    }

    @Test
    fun ecran_stateless_affiche_les_valeurs_recues_et_remonte_les_evenements() {
        val queries = mutableListOf<String>()
        var stockToggles = 0
        var favoritesToggles = 0
        val favoriteClicks = mutableListOf<Int>()
        val categoryClicks = mutableListOf<String>()
        rule.setContent {
            TPsTheme {
                ProductCatalogScreen(
                    products = sampleProducts().take(2),
                    categories = sampleCategories(),
                    selectedCategory = "Audio",
                    onCategoryClick = { categoryClicks += it },
                    searchQuery = "",
                    onSearchQueryChange = { queries += it },
                    showOnlyInStock = true,
                    onToggleStockFilter = { stockToggles++ },
                    favoriteProductIds = listOf(1),
                    onFavoriteClick = { favoriteClicks += it },
                    showOnlyFavorites = false,
                    onToggleFavoritesFilter = { favoritesToggles++ },
                    sortOrder = SortOrder.DEFAULT,
                    onSortOrderChange = {},
                    onProductClick = {}
                )
            }
        }
        // Affiche exactement ce qu'il reçoit
        assertCount(2)
        rule.onNodeWithText("En stock uniquement").assertIsSelected()
        rule.onNodeWithText("Audio").assertIsSelected()
        rule.onNodeWithText("Favoris (1)").assertIsNotSelected()
        rule.onAllNodesWithContentDescription("Retirer des favoris").assertCountEquals(1)

        // Ne modifie rien lui-même : il fait remonter les événements
        rule.onNodeWithText("Rechercher un produit").performTextInput("x")
        rule.onNodeWithText("En stock uniquement").performClick()
        rule.onNodeWithText("Favoris (1)").performClick()
        rule.onNodeWithContentDescription("Retirer des favoris").performClick()
        rule.onNodeWithText("Smartphones").performClick()

        assertEquals("x", queries.first())
        assertEquals(1, stockToggles)
        assertEquals(1, favoritesToggles)
        assertEquals(listOf(1), favoriteClicks)
        assertEquals(listOf("Smartphones"), categoryClicks)
        assertCount(2)
    }
}
