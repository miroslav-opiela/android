# Open Data Game

* Aplikácia uchováva zoznam krstných mien a prislúchajúci počet osôb s daným menom v meste. Po spustení hry sa vyberie zopár mien, ktoré sú zobrazené v zozname. Úlohou hráča je usporiadať mená podľa početnosti. 
* Dáta sú dostupné na stránke ako [náhľad](https://egov.presov.sk/Default.aspx?NavigationState=925:0:) resp. [v rôznych formátoch](https://egov.presov.sk/Default.aspx?NavigationState=1100:0:) (json, xml)
* Prehodená verzia cez REST API je dostupná [na tomto odkaze](https://ics.upjs.sk/~opiela/rest/index.php/names)

## 1. Príprava aplikácie

* vyrobíme nový projekt s jednou **Basic Activity** - aby sme mali k dispozícii aj floating action button
* spustíme aplikáciu a pozrieme, aký kód je vygenerovaný
* fragmenty zatiaľ nepoužívame, môžeme teda vymazať: 
  * kotlin triedy - FirstFragment, SecondFragment 
  * fragment_first.xml, fragment_second.xml v `res/layout`
  * nav_graph.xml v `res/navigation`
  * v `res/layout/content_main.xml` vymazať celý fragment
  * v MainActivity - všetko, čo podčiarkuje červenou (vrátane funkcie `onSupportNavigateUp`)
* aktualizujte gradle (ideálne podľa predošlého projektu weblinks), aby ste mali k dispozícii závislosti potrebné na prácu s databázou
  * nechajte si z aktuálneho projektu dependency: `implementation 'androidx.navigation:navigation-ui-ktx:2.3.5'`

## 2. Databáza

Pri jednotlivých triedach sa inšpirujte minulým projektom Weblinks

* **Entita** - v tomto prípade môžeme použiť `data class` pre triedu, ktorá bude uchovávať jeden záznam s menom a počtom osôb. Primárnym kľúčom bude meno.
* **Dao** - nech obsahuje tri funkcie :
  * READ všetkých záznamov (vráti Flow)
  * CREATE pridanie jedného záznamu (suspend funkcia)
  * DELETE vymazanie všetkých položiek (suspend funkcia)
* **Database** - vytvoríme abstraktnú triedu so singleton inštanciou 
* **Repository** - s dvoma časťami:
  * property typu Flow - kde sa vloží select funkcia z dao
  * suspend funkcia  (s anotáciou WorkerThread) načítania mien - tu nech sa vymažu všetky údaje a vložia nejaké default mená. Neskôr na tomto mieste urobíme REST volanie
* **Application** -  urobiť coroutineScope a inicializovať database a repository + do manifestu pridať `android:name=...`
* **ViewModel** - pridať factory (okopírovať z vhodného miesta) + property live data + dve funkcie na insert a loadNames, ktoré spustia coroutinu a príslušnú funkciu z repository
* Overenie:
  * v MainActivity pridajte property view modelu 
  * do toolbaru pridajte položku `load names`, ktorá nikdy nebude ako ikonka (settings položku si tam nechajme, ešte to použijeme)
  * po kliknutí nech sa zavolá funkcia load names vo view modeli (+ je potrebné pri onOptionsItemSelected vrátiť true)
  * v android studio cez App Inspection skontrolujte, že databáza nie je prázdna

## 3. Recycler View

* **layout** - `content_main.xml` - pridať recycler view s atribútmi - výška, šírka (match parent), id, layout_margin, constraints

* **item layout** - vytvoriť nový layout - kód je priložený nižšie (CardView)

* **adapter** - vyskladať template - potrebujeme vnorenú triedu ViewHolder a vnorený object DiffUtil.ItemCallback

  * Diff callback - kedy ide o rovnaké položky a kedy o rovnaký obsah? 
  * onCreateViewHolder - využitie bindingu. Použije sa funkcia inflate na binding triede.
  * ViewHolder - upraviť konštruktor, aby bral binding (ten si uložiť aj ako property pomocou val). Potom rodičovský konštruktor použije `binding.root`
  * onBindViewHolder - preniesť zodpovednosť na viewholder, kde bude funkcia `bind` a parametrom je jeden záznam
  * bind - do text view nastaviť text podľa mena a počtu

* **layout** - `activity_main.xml` - k include pridať id, aby to bolo identifikovateľné aj pomocou bindingu

* **aktivita** 

  * vytiahnúť recyclerView pomocou bindingu a priradiť mu layoutManager a adapter (v onCreate)
  * pridať observer na live data a lokálne si uložiť aktuálny zoznam (namiesto null môžeme využiť `emptyList()`)
  * pridať funkciu, ktorá vráti náhodný podzoznam so zadaným počtom prvkov (napr. `fun getRandomList(count: Int): List<Record> `) - výber z aktuálneho zoznamu
  * po kliknutí na floating action button nech sa zoberie náhodný zoznam a vloží sa adaptéru ako list (submitList). Počet nech je zatiaľ pevne daný.

## 4. Priebeh hry

* Hlavná myšlienka - po prvom kliknutí na Floating Action Button sa spustí hra (náhodný podzoznam iba s menami bez počtov). Zoznam je možné preusporiadať. Po druhom kliknutí sa hra vyhodnotí - pribudne Toast a zobrazia sa počty. Ďalšie kliknutie spustí novú hru.

* **adapter**
  * pridať boolean premennú, ktorá určuje, či sa majú zobraziť počty k jednotlivým menám
  * upraviť setter premennej, aby sa aktualizovalo zobrazenie zoznamu
  * pridať val property, pričom v gettri sa vypočíta, či ide o výhru (`currentList.zipWithNext`)
  * použiť property `visibility` na textView, ktorý zobrazuje číslo
* **aktivita** 
  * pridať boolean premennú označujúcu, či hra beží
  * vytvoriť onClick metódu pre FAB v ktorej začneme novú hru alebo vyhodnotíme aktuálnu a povieme adaptéru, aby boli zobrazované počty
* **reorder** - možnosť preusporiadať poradie pomocou ItemTouchHelper
  * *aktivita* - pridať ItemTouchHelper s prekrytou metódou `onMove` - tam sa zavolá metóda `exchangeItems` v adaptéri
  * *aktivita* - pripojiť itemTouchHelper k recyclerView
  * *adapter* - implementovať `exchangeItems` - `Collections.swap(list, from, to)`
  * pre lepšie ovládanie pridať startDrag v aktivite na základe listenera 

## 5. REST API

## 6. Preferences



--------------

```xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.cardview.widget.CardView xmlns:android="http://schemas.android.com/apk/res/android"
xmlns:app="http://schemas.android.com/apk/res-auto"
android:layout_width="match_parent"
android:layout_height="wrap_content"
android:layout_marginStart="10dp"
android:layout_marginTop="5dp"
android:layout_marginEnd="10dp"
android:layout_marginBottom="5dp"
android:background="@android:color/white"
android:elevation="5dp"
app:contentPadding="5dp">

<androidx.constraintlayout.widget.ConstraintLayout
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:fadingEdge="vertical"
    android:orientation="horizontal">

    <TextView
        android:id="@+id/textViewName"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_margin="10dp"
        android:textAppearance="@style/TextAppearance.AppCompat.Large"

        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent" />

    <TextView
        android:id="@+id/textViewNumber"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_margin="10dp"
        android:textAppearance="@style/TextAppearance.AppCompat.Large"

        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toStartOf="@id/reorderIcon"
        app:layout_constraintTop_toTopOf="parent" />

    <ImageView
        android:id="@+id/reorderIcon"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"

        android:contentDescription="reorder"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintTop_toTopOf="parent"
        app:srcCompat="@android:drawable/ic_menu_sort_by_size" />


</androidx.constraintlayout.widget.ConstraintLayout>
</androidx.cardview.widget.CardView>
```

