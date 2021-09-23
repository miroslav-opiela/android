# Hangman Kotlin

Stratégia - učíme sa jazyk Kotlin spolu s Androidom

Kotlin

* [Kotlin Koans](https://play.kotlinlang.org/koans/overview) - sada cvičení v prehliadači alebo v Android Studiu (resp. IntelliJ IDEA). 
* [Dokumentácia](https://kotlinlang.org/docs/home.html)
* [Codelab](https://developer.android.com/courses/pathways/kotlin-for-java#codelab-https://developer.android.com/codelabs/java-to-kotlin) Kotlin for Java Developers
* [Ďalšie zdroje](https://kotlinlang.org/education/) - (scroll down na Resources) - odkazy na online kurzy a iné zdroje



## #1 Nový projekt ale v Kotline

* zvolíme jazyk Kotlin v úvodnom nastavení projektu
* pozrieme sa na existujúci kód
* nakopírujeme resources
* pridáme metódu onClick (len riešime problémy)
* *Kotlin novinky*
  * rozširovanie tried
  * definovanie metód ([aký je rozdiel medzi metódou a funkciou](https://blog.kotlin-academy.com/kotlin-programmer-dictionary-function-vs-method-vs-procedure-c0216642ee87))
  * [null safety](https://kotlinlang.org/docs/null-safety.html)

## #2 Interface Game v Kotline 

* `Game.kt` - prepísať  do Kotlinu statické premenné a metódy

* *Kotlin novinky*
  * premenné (`val`, `var`) a ich [typy](https://kotlinlang.org/docs/basic-types.html)
  * [companion object](https://medium.com/swlh/kotlin-basics-of-companion-objects-a8422c96779b)

## #3 Implementácia rozhrania

* `HangmanGame.kt`  - (zatiaľ) prepisujeme Java kód do Kotlinu
  * implementovať interface
  * doplniť konštruktor (`Random` - od`java.util` alebo od `kotlin`?) a inštančné premenné
  * implementovať metódy (single expression funkcia kde sa dá)
* *Kotlin novinky*
  * [konštruktory](https://kotlinlang.org/docs/classes.html#constructors)
  * polia
  * [For cykly](https://kotlinlang.org/docs/control-flow.html#for-loops)
  * [single expression funkcie](https://kotlinlang.org/docs/functions.html#single-expression-functions)
  * porovnávanie (== vs ===)

## #4 Kotlin nie je len prepísaná Java

* Android Studio - umožňuje **konvertovať Java súbor do Kotlinu**
  * porovnajme si našu implementáciu `Game` a `HangmanGame` a konvertované súbory
* premenné (+ gettre a settre) namiesto samostatných metód - interface vyzerá viac ako objekt

  1. `guessedCharacters` - override a implementovať getter
  2. `challengeWord` - premenovať `word` na `challengeWord`, vymazať `private` - sprístupní getter, pridať `override`
  3. `attemptsLeft` - vymazanie `private` sprístupní getter a setter (lebo `var`)
  4. `isWon` - definujeme vlastný getter (môžeme použiť single expression funkciu)
  5. `getTime` - tiež vieme prepísať ako premennú s vlastným gettrom - toto v rozhraní nie je, teda `override` vynecháme
* *Kotlin novinky*

  * deklarovanie premenných + gettre a settre
  * [properties in interfaces](https://kotlinlang.org/docs/interfaces.html#properties-in-interfaces), 
* viac [o rozhraniach v Kotline](https://medium.com/mobile-app-development-publication/kotlin-made-interface-so-much-better-bbeaa59abdd7) 

## #5 Hlavná aktivita

* Postup
  1. premenné zodpovedajúce widgetom (využiť `lateinit` a `findViewById` v `onCreate`)

  2. konštanty v `companion object` - ktoré z nich môžu byť `const`?

  3. ostatné premenné - ID obrázkov šibenice (`intArrayOf`) a inštancia hry `Game` - môže byť null?

  4. metódy `updateText` a `updateGallows` -  vysporiadať sa s `null` - `?.` vs `!!`

  5. metóda `restartGame`  - k resources a colorFilter môžeme pristupovať cez premennú nie explicitne cez metódu gettra. 

  6. uloženie stavu pomocou `Bundle` a načítanie v `onCreate` - je potrebné pretypovať

  7. metóda `onGallowsClick`
     * text vytiahnúť z `EditText` widgetu ako `CharSequence`
     * `text.isEmpty()` namiesto overovania či ma nulovú dĺžku
     * `text[0]` namiesto `charAt`
     * `edittext.text = ""` [nefunguje](https://stackoverflow.com/questions/37374075/how-does-kotlin-property-access-syntax-work-for-java-classes-i-e-edittext-sett/37374301#37374301) - pretože `EditText`pracuje s `Editable` namiesto `String`, použijeme metódu `setText`
     * pretypovanie `hangmanGame` - tu *smart cast* nefunguje pretože ide o *mutable* premennú
     
  8. uloženie najlepšieho času do `SharedPreferences` - odporúčam pozrieť do [dokumentácie](https://developer.android.com/training/data-storage/shared-preferences#WriteSharedPreference), využije sa scope funkcia `with`

  9. `AlertDialog` na informovanie o novom najlepšom čase

* *Kotlin novinky*
  * lateinit
  * const
  * null safety
  * random number
  * pretypovanie
  * scope funkcia with
