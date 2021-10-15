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



```xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".DetailActivity">


    <TextView
        android:id="@+id/weblinkDetailTextView"
        android:text="@string/weblink"
        android:textAppearance="@style/TextAppearance.AppCompat.Large"
        android:layout_margin="10dp"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent"
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

