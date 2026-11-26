# Product Explorer — Fil rouge Jetpack Compose

ESGI M1 — Développement Android avec Jetpack Compose — Ethan Rieu (Reims)

Application fil rouge construite TP après TP : un petit explorateur de produits avec un écran de détail, un écran d'accueil et un catalogue interactif. Toutes les données sont locales, écrites directement en Kotlin.

## Évolution du projet

| TP | Contenu | Notions |
|---|---|---|
| **TP4** | Écran de détail produit | `Text`, `Image`, `Icon`, `Card`, `Button`, `OutlinedTextField`, composables réutilisables, previews |
| **TP5** | Écran d'accueil | `Column`, `Row` + `weight`, `Box` + `align`, `Spacer`, `Surface` |
| **TP6** | Catalogue local de produits | `LazyColumn`, `LazyRow`, `item` / `items` avec `key`, carte réutilisable `ProductListItem` |
| **TP7** | Personnalisation graphique | Material Design 3 : `ColorScheme` clair / sombre, `Typography`, `Shapes` |
| **TP8** | Catalogue interactif | `remember`, `mutableStateOf`, `rememberSaveable` : recherche, filtre stock, favoris |
| **TP9** | State Hoisting | `ProductCatalogContainer` (stateful) / `ProductCatalogScreen` (stateless) : les données descendent, les événements remontent |

## Organisation du code

- `MainActivity.kt` : modèle `ProductUi`, données d'exemple et tous les composables
  - écran de détail : `ProductDetailScreen`
  - écran d'accueil : `ProductHomeScreen`
  - catalogue : `ProductCatalogContainer` → `ProductCatalogScreen` → `ProductListItem` / `CategoryRow`
- `ui/theme/Theme.kt` : palettes claire / sombre et formes (`TPsTheme`)
- `ui/theme/Type.kt` : typographie `ProductTypography`

L'application affiche le catalogue au lancement. Les écrans de détail et d'accueil restent visibles via leurs previews.

## Versions par TP

Chaque fin de TP est marquée par un tag Git :

`tp4-final` · `tp5-final` · `tp6-final` · `tp7-final` · `tp8-final` · `tp9-final`

Pour récupérer une version : onglet **Tags** sur GitHub → choisir le tag → **Download ZIP**, ou `git checkout tp7-final` par exemple.

## Lancer le projet

1. Ouvrir le dossier dans Android Studio.
2. Attendre la synchronisation Gradle.
3. Lancer la configuration `app` sur un émulateur, ou ouvrir `MainActivity.kt` en mode *Split* pour voir les previews.
