package com.example.tps.ui.catalog

import com.example.tps.data.sampleProducts
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Données : 6 produits
 * 1 Smartphone Toto X (Smartphones, 699,99 €, 4,6, stock 34)
 * 2 Casque Audio Pulse (Audio, 129,99 €, 4,2, rupture)
 * 3 Montre Connectée FitTime (Wearables, 89,99 €, 4,4, stock 18)
 * 4 Enceinte Mini Boom (Audio, 59,99 €, 4,1, stock 52)
 * 5 Lampe Connectée Lumio (Maison, 39,99 €, 4,3, stock 27)
 * 6 Aspirateur Robot CleanBot (Maison, 249,99 €, 4,5, rupture)
 */
class ProductFiltersTest {

    private val products = sampleProducts()

    private fun ids(
        searchQuery: String = "",
        showOnlyInStock: Boolean = false,
        selectedCategory: String? = null,
        showOnlyFavorites: Boolean = false,
        favoriteProductIds: List<Int> = emptyList()
    ): List<Int> = filterProducts(
        products, searchQuery, showOnlyInStock, selectedCategory, showOnlyFavorites, favoriteProductIds
    ).map { it.id }

    @Test
    fun sans_filtre_tous_les_produits_sont_retournes() {
        assertEquals(listOf(1, 2, 3, 4, 5, 6), ids())
    }

    @Test
    fun recherche_sur_titre_marque_et_categorie_insensible_a_la_casse() {
        assertEquals(listOf(1), ids(searchQuery = "toto"))          // titre
        assertEquals(listOf(4), ids(searchQuery = "BOOMSOUND"))     // marque
        assertEquals(listOf(2, 4), ids(searchQuery = "audio"))      // catégorie
    }

    @Test
    fun recherche_ignore_les_espaces_autour() {
        assertEquals(listOf(3), ids(searchQuery = "  montre  "))
    }

    @Test
    fun recherche_sans_resultat() {
        assertTrue(ids(searchQuery = "introuvable").isEmpty())
    }

    @Test
    fun filtre_stock_exclut_les_ruptures() {
        assertEquals(listOf(1, 3, 4, 5), ids(showOnlyInStock = true))
    }

    @Test
    fun filtre_categorie() {
        assertEquals(listOf(5, 6), ids(selectedCategory = "Maison"))
        assertEquals(listOf(3), ids(selectedCategory = "Wearables"))
    }

    @Test
    fun filtre_favoris() {
        assertEquals(listOf(2, 5), ids(showOnlyFavorites = true, favoriteProductIds = listOf(5, 2)))
        assertTrue(ids(showOnlyFavorites = true).isEmpty())
        // Les favoris n'ont pas d'effet si le filtre est désactivé
        assertEquals(6, ids(favoriteProductIds = listOf(1)).size)
    }

    @Test
    fun filtres_combines() {
        assertEquals(listOf(4), ids(selectedCategory = "Audio", showOnlyInStock = true))
        assertEquals(listOf(5), ids(selectedCategory = "Maison", showOnlyInStock = true, searchQuery = "lumio"))
        assertTrue(ids(selectedCategory = "Maison", searchQuery = "casque").isEmpty())
    }

    @Test
    fun tri_par_defaut_conserve_l_ordre() {
        assertEquals(products, sortProducts(products, SortOrder.DEFAULT))
    }

    @Test
    fun tri_par_prix_croissant_et_decroissant() {
        assertEquals(listOf(5, 4, 3, 2, 6, 1), sortProducts(products, SortOrder.PRICE_ASCENDING).map { it.id })
        assertEquals(listOf(1, 6, 2, 3, 4, 5), sortProducts(products, SortOrder.PRICE_DESCENDING).map { it.id })
    }

    @Test
    fun tri_par_meilleures_notes() {
        assertEquals(listOf(1, 6, 3, 5, 2, 4), sortProducts(products, SortOrder.BEST_RATED).map { it.id })
    }

    @Test
    fun tri_applique_apres_filtre() {
        val filtered = filterProducts(products, "", showOnlyInStock = true)
        assertEquals(listOf(5, 4, 3, 1), sortProducts(filtered, SortOrder.PRICE_ASCENDING).map { it.id })
    }
}
