# Hangman

Preberané koncepty:

* logovanie
* toast notifikácie
* životný cyklus aktivity, bundle, uloženie stavu
* meranie času, shared preferences, dialógové okná

## #1 Príprava podkladov

* Stiahnite si obrázky šibenice - [gallows-images.zip](res/gallows-images.zip) a vložte do `res/drawable`

* Pridajte interface [Game.java](res/Game.java), ktorý definuje *herný engine* (ten implementujeme neskôr) - prezrite si, aké metódy sú k dispozícii
*  `string-array` - vytvoríme zoznam slov na hádanie v `res/values/strings.xml`

## #2 Layout šibenice

[ConstraintLayout](https://developer.android.com/training/constraint-layout)

Vytvorme si layout s 3 widgetmi - obrázok, hádaný text a zadané písmenko.

Vhodné je použiť (okrem iných aj) nasledovné atribúty:

`ImageView`

 * `android:src` - referencia na obrázok (pri vektorových obrázkoch je vhodné použiť `app:srcCompat`)
 * `android:contentDescription` - kvôli *accessibility* - uložte string v príslušnom XML súbore

`TextView`

* `android:layout_margin` - často sa zvykne používať `8dp`
* `android:textAllCaps=true`
* `android:textAppearance="@android:style/TextAppearance.Large"`- použitie preddefinovaného štýlu namiesto fixného čísla
* `android:typeface="monospace"`
* ` android:letterSpacing="0.5"`

`EditText` (trieda `EditText` rozširuje triedu `TextView`)

* odporúčam tiež `textAppearance`, `layout_margin` a `typeface`
* `android:gravity="center"` - určuje pozíciu v rámci väčšieho kontajnera ([viď vymenované možností](https://developer.android.com/reference/android/widget/TextView#attr_android:gravity))
* `android:hint`
* `android:maxLength='1'`
* `android:inputType="textCapCharacters"`

## #3 Logika hry

#### Po kliknutí na šibenicu

1. zistiť zadané písmenko
2. ošetriť situáciu, že nebolo zadané žiadne písmeno alebo nejaký nevhodný znak (ten môžeme priamo aj zmazať)
3. prispôsobenie písmena (aby bolo jedno, či sú zadávané písmena a uložené slová tvorené veľkými alebo malými písmenami), 
4. zavolať metódu `guess`
5. vyčistiť `EditText`
6. podľa odpovede z `guess` aktualizovať slovo (`textView.setText`) alebo šibenicu (`imageView.setImageResource`)

[Toast](https://developer.android.com/guide/topics/ui/notifiers/toasts) - zobrazenie jednoduchej informácie pre používateľa

#### Implementácia rozhrania `Game`

* objekt na jedno použitie - slovo sa vygeneruje v konštruktore
* uhádnutie (metóda `guess`) - zistiť, či sa dané písmeno v slove nachádza (ak nie, tak znížiť počet pokusov)

 #### Opakovanie hry

* pomocná metóda `restartGame`, ktorá bude zavolaná v `onCreate` a tiež po kliknutí na šibenicu ak je už skončená hra (výhra alebo prehra)
  * v metóde `guess` môžeme vyhodiť výnimku `IllegalStateException`ak sa volá s nulovým počtom pokusov
  * v pomocnej metóde nech sa vyžrebuje nové slovo a upravia sa widgety do pôvodného stavu

#### Farebné filtre

* použijeme vlastný `ColorFilter` na obrázok - nech je pozadie zelené pri výhre a červené pri prehre
  * [LightingColorFilter](https://developer.android.com/reference/android/graphics/LightingColorFilter)

## #4 Logovanie a životný cyklus aktivity

* vyskúšajte si logovanie metód životného cyklu aktivity (v tejto alebo predošlej aplikácii) 
* [Logovanie](https://developer.android.com/studio/debug/am-logcat)
* [Zoznam](https://developer.android.com/guide/topics/manifest/activity-element.html#config) udalostí, ktoré vynútia reštart aktivity
* Bundle - **load** v metóde `onCreate`, **save** v metóde `onSaveInstanceState`

### Bonus - Easter Eggs

* Kliknite si viackrát na verziu Androidu v nastaveniach
* nájdite zaujímavé premenné/metódy v týchto triedach:
  * [Log](https://developer.android.com/reference/android/util/Log)
  * [ActivityManager](https://developer.android.com/reference/android/app/ActivityManager)
  * [UserManager](https://developer.android.com/reference/android/os/UserManager)
  * [SensorManager](https://developer.android.com/reference/android/hardware/SensorManager)

## #5 Meranie času

* v `HangmanGame` pridať štartovací čas (`SystemClock.elapsedRealTime()`) a metódu na výpočet aktuálneho času od začiatku hry
  * interface neupravujeme, iba implementáciu 
* V aktivite aktualizovať najlepší čas pri výhre cez `SharedPreferences`
  * `getPreferences(Context.MODE_PRIVATE)` metóda v aktivite 
  * ak je lepší čas ako uložená hodnota, tak ju prepíšeme a oznámime používateľovi
* AlertDialog
  * [Formátovanie String resource](https://developer.android.com/guide/topics/resources/string-resource#formatting-strings)
  * využije sa `AlertDialog.Builder` na vytvorenie dialógu, ktorý sa potom zobrazí metódou `show()`