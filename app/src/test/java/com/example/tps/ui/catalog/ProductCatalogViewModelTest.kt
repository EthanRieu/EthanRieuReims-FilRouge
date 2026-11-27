package com.example.tps.ui.catalog

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

/**
 * Tests du ViewModel du catalogue (JVM, sans émulateur).
 * Données : 6 produits, 2 en rupture (ids 2 et 6), 2 en Audio (2, 4), 2 en Maison (5, 6).
 */
class ProductCatalogViewModelTest {

    private lateinit var viewModel: ProductCatalogViewModel

    private val state: ProductCatalogUiState get() = viewModel.uiState.value

    private fun displayedIds() = state.products.map { it.id }

    @Before
    fun setUp() {
        viewModel = ProductCatalogViewModel()
    }

    @Test
    fun etat_initial() {
        assertEquals("", state.searchQuery)
        assertFalse(state.showOnlyInStock)
        assertTrue(state.favoriteProductIds.isEmpty())
        assertNull(state.selectedCategory)
        assertEquals(SortOrder.DEFAULT, state.sortOrder)
        assertEquals(listOf(1, 2, 3, 4, 5, 6), displayedIds())
        assertEquals(listOf("Smartphones", "Audio", "Wearables", "Maison"), state.categories)
    }

    @Test
    fun la_recherche_met_a_jour_la_liste() {
        viewModel.onSearchQueryChange("audio")
        assertEquals("audio", state.searchQuery)
        assertEquals(listOf(2, 4), displayedIds())

        viewModel.onSearchQueryChange("")
        assertEquals(6, displayedIds().size)
    }

    @Test
    fun le_filtre_stock_s_active_et_se_desactive() {
        viewModel.onToggleStockFilter()
        assertTrue(state.showOnlyInStock)
        assertEquals(listOf(1, 3, 4, 5), displayedIds())

        viewModel.onToggleStockFilter()
        assertFalse(state.showOnlyInStock)
        assertEquals(6, displayedIds().size)
    }

    @Test
    fun ajout_et_retrait_d_un_favori() {
        viewModel.onFavoriteClick(3)
        assertEquals(listOf(3), state.favoriteProductIds)

        viewModel.onFavoriteClick(3)
        assertTrue(state.favoriteProductIds.isEmpty())
    }

    @Test
    fun clic_categorie_selectionne_puis_deselectionne() {
        viewModel.onCategoryClick("Maison")
        assertEquals("Maison", state.selectedCategory)
        assertEquals(listOf(5, 6), displayedIds())

        viewModel.onCategoryClick("Audio")
        assertEquals("Audio", state.selectedCategory)

        viewModel.onCategoryClick("Audio")
        assertNull(state.selectedCategory)
    }

    @Test
    fun categorie_selectionnee_depuis_l_accueil_reste_active() {
        viewModel.onCategorySelected("Maison")
        viewModel.onCategorySelected("Maison")
        assertEquals("Maison", state.selectedCategory)
    }

    @Test
    fun filtre_favoris_et_tri() {
        viewModel.onFavoriteClick(1)
        viewModel.onFavoriteClick(5)
        viewModel.onToggleFavoritesFilter()
        assertEquals(listOf(1, 5), displayedIds())

        viewModel.onSortOrderChange(SortOrder.PRICE_ASCENDING)
        assertEquals(listOf(5, 1), displayedIds())
    }

    @Test
    fun reinitialiser_les_filtres_garde_favoris_et_tri() {
        viewModel.onFavoriteClick(2)
        viewModel.onSortOrderChange(SortOrder.BEST_RATED)
        viewModel.onSearchQueryChange("casque")
        viewModel.onToggleStockFilter()
        viewModel.onCategoryClick("Audio")
        viewModel.onToggleFavoritesFilter()
        assertTrue(displayedIds().isEmpty())

        viewModel.onResetFilters()
        assertEquals("", state.searchQuery)
        assertFalse(state.showOnlyInStock)
        assertNull(state.selectedCategory)
        assertFalse(state.showOnlyFavorites)
        assertEquals(listOf(2), state.favoriteProductIds)
        assertEquals(SortOrder.BEST_RATED, state.sortOrder)
        assertEquals(6, displayedIds().size)
    }

    @Test
    fun retirer_un_favori_met_a_jour_la_liste_filtree_par_favoris() {
        viewModel.onFavoriteClick(1)
        viewModel.onToggleFavoritesFilter()
        assertEquals(listOf(1), displayedIds())

        viewModel.onFavoriteClick(1)
        assertTrue(displayedIds().isEmpty())
    }

    @Test
    fun chaque_changement_publie_un_nouvel_etat_sans_modifier_l_ancien() {
        val before = state
        viewModel.onFavoriteClick(4)
        viewModel.onSearchQueryChange("audio")

        assertTrue(before.favoriteProductIds.isEmpty())
        assertEquals("", before.searchQuery)
        assertEquals(6, before.products.size)
        assertEquals(listOf(4), state.favoriteProductIds)
        assertEquals(listOf(2, 4), displayedIds())
    }

    @Test
    fun recherche_d_un_produit_par_identifiant() {
        assertEquals("Lampe Connectée Lumio", viewModel.getProduct(5)?.title)
        assertNull(viewModel.getProduct(99))
        assertEquals(1, viewModel.featuredProduct.id)
    }
}
