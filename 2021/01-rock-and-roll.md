# Rock & Roll

Preberané koncepty: 

* vytváranie XML layoutov
* prepojenie XML resources a Java kódu
* Aktivita a jej životný cyklus

## #1 Inštalácia Android Studio

* [Stiahnúť a nainštalovať Android Studio](https://developer.android.com/studio) - oficiálne podporované IDE na vývoj Android aplikácii
* SDK manager - slúži na stiahnutie potrebných knižníc, hlavne SDK pre konkrétnu verziu Androidu

Spustenie aplikácie:

* AVD (Android Virtual Device) manager - vytvoríte si [vlastné virtuálne zariadenie](https://developer.android.com/studio/run/managing-avds)
* spustenie priamo na telefóne, pripojenom cez kábel. Vyhľadajte si ako sa zapína Developer options na vašom zariadení a povoľte tam USB debugging. Občas je potrebné povoliť aj prístup k dátam (nie iba režim na nabíjanie).

## #2 Nový projekt

Pre naše potreby použijeme nasledovný postup:

* Empty Activity (dá sa to vyrobiť aj neskôr, ale zjednoduší to prácu, ak to máme teraz)
* Name: RockAndRoll
* Package name, save location - podľa vlastného uváženia
* Language: Java
* Minimum SDK: API 21 Android 5.0 (Lollipop) - odporúčam kliknúť na *help me choose* a pozrieť si prehľad

Prezrite si existujúce súbory v adresároch manifests, res, java.

## #3 Layout - pridávanie tlačidiel

* Preskúmať existujúci `TextView` *Hello world* a vymazať ho 
* Pridať nový `Button` a nastaviť vhodné atribúty (rozmery, constraints, text)
* Druhý `Button` a spoločné uchytenie - potrebné `ID`

## #4 Klikanie - prepojenie XML a Java

* pridať `onClick` metódu v XML
* v Jave vytiahnúť referenciu na `Button` cez `findViewById`
* implementovať metódu v Jave - nastaviť príslušný text s náhodným číslom

## #5 Iné resources a životný cyklus aktivity

* vložíme nejaký mp3 súbor do `/res/raw` - ak to nie je vytvorené, tak klikneme na res`New->Android Resource Directory` a vytvoríme `raw` folder
* [Media Player](https://developer.android.com/guide/topics/media/mediaplayer) - poctivá práca s tým je komplikovanejšia. Kde pôjdu jednotlivé metódy `create`, `start` a `release`?

* [Životný cyklus aktivity](https://developer.android.com/guide/components/activities/activity-lifecycle)

  



