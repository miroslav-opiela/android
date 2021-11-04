# Weblinks

* Vytvoriť nový Kotlin projekt s jednou empty aktivitou

## #1 RecyclerView a Adapter so zoznamom Stringov

* RecyclerView [v dokumentácii](https://developer.android.com/guide/topics/ui/layout/recyclerview)
* v layoute pridáme `RecyclerView`, ktorý vyplní celú obrazovku (`match_parent`)
* V `MainActivity`
  * vyrobíme premennú typu`RecyclerView` (musíme to explicitne uviesť) a vytiahneme pomocou `findViewById`
  * nastavíme `LayoutManager` na `LinearLayoutManager` - aby boli položky usporiadané za sebou
  * priradíme adaptér - vytvoríme `WeblinksAdapter` - viď kód (pozor! sú tam 2 triedy v jednej)
* `WeblinksAdapter` 
  * pridáme zoznam Stringov napr. cez `listOf`
  * `ViewHolder` prepojí layout jednej položky s adapterom - zatiaľ tam iba nájdeme `textView` a priradíme do premennej
    * je potrebné urobiť layout jednej položky - odporúča sa `FrameLayout` na jediný widget
  * implementujeme 3 metódy
    * `onCreateViewHolder` - ktorý pomocou `LayoutInflater` nafúkne príslušný layout položky
    * `onBindViewHolder` - prepojíme konkrétny záznam (identifikovaný premennou `position`) a `ViewHolder` - v tomto prípade nastavíme text
    * `getItemCount` - vráti počet prvkov celkovo - použijme single-expression funkciu

* *Kotlin novinky*
  * dedičnosť a konštruktory

## #2 Vlastná trieda 

* Vyrobiť vlastnú triedu `Weblink`, ktorá bude `data class` a bude uchovávať okrem názvu aj rating 
* Upraviť layout jednej položky, aby sa pridal `RatingBar` 
  * definovať atribúty `stepSize` a `numStars`
  * `FrameLayout` je vhodný pre jednen widget, môžeme použiť napr. `ConstraintLayout` alebo `LinearLayout`
* Prispôsobiť `WeblinksAdapter`
  * z listu Stringov urobiť list `Weblink`-ov pomocou *map transformácie*

* *Kotlin novinky*
  * data class
  * destructuring declarations
  * collections transformations, [map funkcia](https://kotlinlang.org/docs/collection-transformations.html#map)

## #3 Listener

* Interface s jednou metódou `onClick`
* Poslucháč - ten kto implementuje interface. Keď nastane udalosť, poslucháč ju spracuje (vykoná metódu).
* Pozorovaný - adapter pri kliknutí na widget upozorní poslucháčov, že nastala akcia
* Poslucháč sa prihlási u pozorovaného

* [Iná implementácia](https://github.com/android/views-widgets-samples/blob/main/RecyclerViewKotlin/app/src/main/java/com/example/recyclersample/flowerList/FlowersAdapter.kt) - alternatíva k listeneru cez let a konštruktor 

## #4 Intent a nová aktivita

* Intentom spustiť (`Intent.ACTION_VIEW`) prehliadač s wikipediou
* Intentom spustiť novú aktivitu
  * aktivitu pridávame cez menu v android studiu (pridávajú sa veci aj v manifeste)
  * nová detail aktivita
    * v layoute jeden `TextView` neskôr pridať aj `EditText`
* Intentom spustiť novú aktivitu a čakať na výsledok 
  * `startActivityForResult` je deprecated a nahradené `registerForActivityResult` - [ukážka na porovnanie](https://stackoverflow.com/questions/62671106/onactivityresult-method-is-deprecated-what-is-the-alternative) + [v dokumentácii](https://developer.android.com/training/basics/intents/result)
  * výsledok spracovať takým spôsobom, aby sa aktualizoval `recyclerView`

## #5 Toolbar a pridanie novej položky

* Toolbar 
  * V manifeste nastavíme, aby bola aplikácia bez ActionBaru (namiesto neho si vyrobíme vlastný toolbar) - `android:theme="@style/Theme.AppCompat.Light.NoActionBar"` 
  * Upraviť layout hlavnej aktivity - je potrebné pridať `androidx.appcompat.widget.Toolbar` - nastaviť vlastnosti (viď priložený layout dole) a vyriešiť constraints, aby recyclerView nezaberal celú výšku.
  * Upraviť aj layout detail aktivity
  * V aktivite pridať príkaz `setSupportActionBar(findViewById(R.id.toolbar))` ktorý nastaví toolbar ako action bar - teda zobrazí sa názov aplikácie.
* Add tlačidlo v toolbare
  * pre toolbar vyrobíme menu layout (ak v `res` chýba priečinok `menu`, vyrobíme ho kliknutím na `new->Android Resource Directory`). 
  * v menu layoute pridáme jeden `item` 
  * nafúkneme (inflate) menu v `onCreateOptionsMenu`
  * v `onOptionsItemSelected` implementujeme akciu po kliknutí na item (využijeme `when` čo je v kotline podobné ako switch statement v jave)
* Navigácie v toolbare
  * v manifeste nastavíme pre detail aktivitu jej rodičovskú aktivitu (`android:parentActivityName`)
  * v detail aktivite pridáme `supportActionBar?.setDisplayHomeAsUpEnabled(true)`
* Pridanie nového weblinku
  * spustenie aktivity s default prázdnym objektom (vyrobiť ho ako statickú metódu v triede `Weblink`)
  * aktualizovať kde je potrebné - url v objekte ešte v detail aktivite pred save a zoznam po kliknutí na save
  * môžeme pridať `RESULT_CANCELED` ak máme prázdny názov
  * sledujme správanie sa hlavnej aktivity (volanie metódy `onCreate`) pri použití BACK šípky v toolbare a na telefóne

## #6 Swipe gestá a odstránenie položky

* `ItemTouchHelper` môže byť priradený k recycler view, vyžaduje `callback`
* `onMove`, `onSwipe` - v konštruktore označujeme číslom, ktoré akcie nás zaujímajú 
  * `ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT`  
  * UP = 1, DOWN = 2, LEFT = 4, RIGHT = 8, ešte START a END - podľa layoutu RecyclerView
* v adaptéri pridať metódu na vymazanie položky podľa indexu pozície

## #7 ViewBinding

* https://developer.android.com/topic/libraries/view-binding

# Databáza

* [android room codelab](https://developer.android.com/codelabs/android-room-with-a-view-kotlin#1) - vysvetlené jednotlivé komponenty
* [coroutines codelab](https://developer.android.com/codelabs/kotlin-coroutines#0)

## #8 Ukladanie weblinkov do databázy

* [4] update gradle súborov
  * `File -> Project structure -> Dependencies` - update variable/dependency ak sú novšie verzie
* [5] vyrobiť entitu - prerobiť data class `Weblink`
  * primárny kľúč - ak potrebujeme, vieme nastaviť autogenerate a zmeniť názvy stĺpcov cez `@ColumnInfo`
* [6] DAO
  * [prehľad možností](https://developer.android.com/training/data-storage/room/accessing-data)
    * napr. vararg pri update, viacnásobný insert a pod.
  * [Základy Kotlin coroutines](https://kotlinlang.org/docs/coroutines-basics.html)
    * okrem select metódy, ktorá vráti zoznam weblinkov - všade dávame `suspend`
* [8] RoomDatabase
  * singleton, `@Volatile` - aby bol aktuálny obsah premennej viditeľný v iných vláknach
* [9] Repository
  * má prístup k DAO, implementujme iba metódy, ktoré používame
* [10] ViewModel
* [13] Application
  * kotlin delegation (`by lazy` - až keď to je potrebné, 1x get() zavolá definovanú lambda funkciu)
  * pridať `android:name` v manifeste
* [14] Populate database
  * vyrobiť callback, pridať scope
* [16] Connect with the data



* V Android Studio - overenie cez App Inspector - pohľad (aj live) na data + možnosti robiť SQL dopyty a volať DAO metódy.

* [7 tipov na prácu s databázou](https://medium.com/androiddevelopers/7-pro-tips-for-room-fbadea4bfbd1)

## #9 ListAdapter

* DiffUtil - použiť Kotlin object - je to statická inštancia triedy, ktorá je len jedna (singleton)



### Doplnenie - layout pre detail aktivitu s toolbarom

```xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".DetailActivity">

    <androidx.appcompat.widget.Toolbar
        android:id="@+id/toolbarDetailActivity"
        android:layout_width="match_parent"
        android:layout_height="?attr/actionBarSize"
        android:background="?attr/colorPrimary"
        android:elevation="4dp"
        app:layout_constraintTop_toTopOf="parent"
        android:theme="@style/ThemeOverlay.AppCompat.ActionBar"
        app:popupTheme="@style/ThemeOverlay.AppCompat.Light"/>


    <TextView
        android:id="@+id/weblinkDetailTextView"
        android:text="@string/weblink"
        android:textAppearance="@style/TextAppearance.AppCompat.Large"
        android:layout_margin="10dp"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toBottomOf="@id/toolbarDetailActivity"
        />

    <EditText
        android:id="@+id/weblinkDetailEditText"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_margin="10dp"
        android:inputType="text"
        android:importantForAutofill="no"
        android:hint="@string/insert_weblink_title"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toBottomOf="@id/weblinkDetailTextView"
        />

    <Button
        android:id="@+id/buttonSave"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toBottomOf="@id/weblinkDetailEditText"
        android:onClick="save"
        android:text="@string/save" />

</androidx.constraintlayout.widget.ConstraintLayout>
```

### Doplnenie - layout pre hlavnú aktivitu s toolbarom

```xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".MainActivity">

    <androidx.appcompat.widget.Toolbar
        android:id="@+id/toolbar"
        android:layout_width="match_parent"
        android:layout_height="?attr/actionBarSize"
        android:background="?attr/colorPrimary"
        android:elevation="4dp"
        app:layout_constraintTop_toTopOf="parent"
        android:theme="@style/ThemeOverlay.AppCompat.ActionBar"
        app:popupTheme="@style/ThemeOverlay.AppCompat.Light"
        />

    <androidx.recyclerview.widget.RecyclerView
        android:id="@+id/recyclerView"
        android:layout_width="match_parent"
        android:layout_height="0dp"
        android:layout_marginHorizontal="10dp"
        app:layout_constraintTop_toBottomOf="@id/toolbar"
        />

</androidx.constraintlayout.widget.ConstraintLayout>
```

