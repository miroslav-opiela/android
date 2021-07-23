package sk.sovy.hangman;

/**
 * Objekt danej triedy obsahuje informacie o hladanom slove aj o aktualnom stave hry - uhadnuta cast slova, pocet zostavajucich pokusov.
 * V konstruktore vygenerujte nahodne slovo, ktore sa bude hladat. Mozete pouzit zoznam stringov, z ktoreho vyberiete nahodny.
 */
public interface Game {

    /**
     * Defaultny pocet pokusov na zaciatku hry.
     */
    int DEFAULT_ATTEMPTS_LEFT = 6;

    /**
     * Kodovany neuhadnuty znak. Na zaciatku je slovo zlozene z tychto znakov.
     */
    char UNGUESSED_CHAR = '_';

    /**
     * Oznaci ci je hra skoncena.
     *
     * @return true ak hra skoncila vitazne - slovo bolo uhadnute.
     */
    boolean isWon();

    /**
     * Vrati aktualny retazec.
     *
     * @return neuhadnute znaky v retazci nie su odhalene, pouzije sa UNGUESSED_CHAR
     */
    CharSequence getGuessedCharacters();

    /**
     * Aktualne hladane slovo.
     *
     * @return hladane slovo.
     */
    String getChallengeWord();

    /**
     * Vrati zostavajuci pocet pokusov.
     *
     * @return pocet pokusov
     */
    int getAttemptsLeft();

    /**
     * Hrac zadal dane pismeno. Spracuje sa jeho tip.
     *
     * @param character pismeno.
     * @return true ak uhadol.
     */
    boolean guess(char character);

}
