# Call Log

Aplikácia, ktorá zobrazuje zoznam hovorov. 

Hlavné novinky:

* Fragmenty
* Permissions
* ContentResolver - ContentProvider

## 1. Fragmenty

* Vytvoríme novú aplikáciu (`empty activity`) - fragmenty sa dajú vygenerovať, teraz to neurobíme

* > A [`Fragment`](https://developer.android.com/reference/androidx/fragment/app/Fragment) represents a reusable portion of your app's UI. A fragment defines and manages its own layout, has its own lifecycle, and can handle its own input events. Fragments cannot live on their own--they must be *hosted* by an activity or another fragment. The fragment’s view hierarchy becomes part of, or *attaches to*, the host’s view hierarchy.

* Landscape - 2 fragmenty vedľa seba

  * 2x trieda fragment (master a detail)
  * 2x príslušný layout súbor, kde bude jeden textview
  * 1x layout pre activity main (s vybranými parametrami - landscape)
    * LinearLayout, 2x fragment (výška - match_parent, šírka - 0dp, weight 1 a 2)

## 2. Recycler View - zoznam hovorov

* Trieda Call - premenné number (string), type (int) a neskôr aj iné, napr. date (string). Viac [v dokumentácii](https://developer.android.com/reference/android/provider/CallLog.Calls).
* V master fragmente vyrobíme recycler view s list adapterom
  * v layoute pre master fragment nahradíme textview novým recycler view widgetom
  * v master fragmente v metóde `onViewCreated` si môžeme vytvoriť default list hovorov
  * fragment má metódu `requireContext` a `requireActivity` - existujú aj `getActivity` a `getContext`, ale tie môžu vrátiť null. Preto `require...` metódy zaistia, aby bol fragment priradený k aktivite. Vráti illegalstateexception namiesto null v zlom prípade.
  * `android.R.layout.simple_list_item_1` - namiesto vlastného layoutu jednej položky
* ak je potrebné, tak v build.gradle umožníme viewBinding - `buildFeatures {viewBinding true}`

## 3. Komunikácia medzi fragmentmi

* [Share data between fragments](https://developer.android.com/topic/libraries/architecture/viewmodel#sharing)
* Do build.gradle pridáme závislosť na [fragment-ktx](https://developer.android.com/kotlin/ktx?gclid=Cj0KCQiAnaeNBhCUARIsABEee8W1FnPuOuPVYghrde17SFOLsL-mrQ_Y6KR8O95n_G4Znbso6wQN5-UaAnBOEALw_wcB&gclsrc=aw.ds#fragment) (neskôr kvôli permissions budeme potrebovať aj activity-ktx)
  * ` implementation "androidx.fragment:fragment-ktx:1.4.0"`
  * `implementation 'androidx.activity:activity-ktx:1.4.0'`
* View model - podľa dokumentácie - mutable live data ktore uchovavaju info o vybranom objekte triedy Call
* Adapter - pridať listener po kliknutí na položku v zozname
* Master fragment - implementuje listener
* Detail fragment - observer na live data - nastavenie textview podla aktualneho vybraneho telefonneho cisla
  * observer mozeme implementovat aj v samostatnej metode (priklad volania takej metody `model.selectedCall.observe(viewLifecycleOwner, ::setCall)`)

## 4. Transakcie fragmentov

* V landscape verzii sú zobrazené oba fragmenty vedľa seba. V portrait natočení sa zobrazí vždy iba jeden fragment. Aktivita bude riadiť prepínanie medzi nimi.
* Layout pre main aktivitu - upravíme pôvodný súbor (okrem neho v projekte je jeden layout pre landscape). 
  * widgety môžeme vymazať 
  * `viewBindingIgnore="true"` - view binding nebudeme používať, pretože root je rozdielny pre rôzne konfigurácie
  * pridáme ku constraint layoutu ID
* Aktivita - v prípade, že ide o portrait mód (overíme, či existuje View s príslušným ID), tak
  * nahradíme constraint layout fragmentom - `supportFragmentManager.commit { replace(R.id.main_activity_portrait, MasterFragment()) }`
  * pridáme observer na live data v zdieľanom view model a v prípade zmeny dát sa vykoná nahradenie detail fragmentom + `addToBackStack(null)`

Bonus k fragmentom:

* [Navigácia - princípy](https://developer.android.com/guide/navigation/navigation-principles)
* Vyskúšať vyrobiť aktivitu s tabbed fragmentmi resp. vybrať aktivitu/fragment z ponuky a sledovať kód

## 5. Permissions

* [Requesting permissions](https://developer.android.com/training/permissions/requesting)
  * kedysi sa manuálne spracovával request code
* V manifeste deklarovať použitie permission ` <uses-permission android:name="android.permission.READ_CALL_LOG"/>`
* Master Fragment
  * requestPermissinLauncher
    * ak je udelené permission -> naplniť zoznam (tie veci, čo tam boli predtým, ideálne ich vložiť do nejakej samostatnej metódy)
    * ak nie je permission -> informovať napr. pomocou toastu (využijeme `requireContext()`)
  * v `onViewCreated`
    * ak je udelené permission -> zavolať metódu, pokračuje sa ďalej
    * shouldShow... -> alert dialog (AlertDialog Builder a scope funkcia apply na vytvorenie)
    * inak zavolat requestPermissionLauncher

## 6. Content Provider a Content Resolver

* [Content provider](https://developer.android.com/guide/topics/providers/content-provider-basics) - sprístupňuje dáta aj iným aplikáciam. V našom prípade použijeme existujúci provider na zoznam hovorov
* [Content Resolver](https://developer.android.com/reference/android/content/ContentResolver) - ním sa aplikácia pripojí ku content provideru a pomocou cursora načítava dáta
* Vyrobíme Application, Repository, ViewModel (novú triedu) ako v prípade databázy
  * na prepojenie tried sa často používa Dependency Injection. V androide [HILT](https://developer.android.com/training/dependency-injection/hilt-android)

* ViewModel 
  * funkcia na načitanie hovorov, vráti `List<Call>`
  * factory na vyrobenie s repository ako parametrom
* Application
  * premenná repository `val repository by lazy { CallsRepository(this) }`
  * upraviť manifest
* Repository
  * načítať zoznam hovorov pomocout content resolvera - získame ho cez context (ten je parametrom repository). Metóda query pri content resolveri vráti `cursor`. Ten sa dá iterovať (metódy `count`, `moveToNext`, `getString`, `getInt`)
* Master Fragment
  * pridať nový view model a v init metóde nahradiť default dáta načítanými

## 7. Aktualizácia, vlastné Live Data

* ViewModel bude namiesto funkcie načítania dát mať live data
* Master fragment bude v init sledovať zmeny v live dátach a podľa toho aktualizovať listadapter
* Repository obsahuje live data (je to abstraktná trieda)
  * implementujeme anonymnú triedu (keyword `object`)
  * parameter `lateinit var observer: ContentObserver`
  * prekryjeme metódu `onActive` - kde sa vyrobí observer s prekrytou metódou `onChange` - kde sa zmenia live data (`postValue(loadCalls())`). Okrem toho sa zaregistruje observer (`context.contentResolver.registerContentObserver(...)`). Môžeme zavolať hneď aj metódu onChange, aby sa načítali dáta pri spustení
  * metóda `onInactive` - odregistrovať content resolver

## 8. Doplnky

* Zmeniť background farbu v recycler view na základe typu hovoru
* pridať date (typ string) alebo inú premennú v triede Call
  * pri cursore môžeme Date long prehodiť na string s využitím SimpleDateFormat.format

* premenná stringType v triede Call - getter, ktorý vyráta hodnotu a urobí popis
