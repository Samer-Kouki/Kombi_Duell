package service
import entity.Card


/**
 * This interface provides a mechanism for the service layer classes to communicate
 * (usually to the GUI classes) that certain changes have been made to the entity
 * layer, so that the user interface can be updated accordingly.
 *
 * Default (empty) implementations are provided for all methods, so that implementing
 * GUI classes only need to react to events relevant to them.
 *
 * @see AbstractRefreshingService
 */
interface Refreshable {

    /**
     *  new game .
     * refresh the UI for initial state.
     */
    fun refreshAtGameStart() {}

    /**
     *  beginning of a player's turn.
     *   which player is active,.
     */
    fun refreshAtTurnStart() {}

    /**
     * the end of a player's turn.
     */
    fun refreshAtTurnEnd() {}

    /**
     *  after a Drawcard
     *
     * @param drawnCard the card that was drawn
     */
    fun refreshAtDrawCard(drawnCard: Card) {}

    /**
     *  swap action
     *
     * @param swapCard the card that was taken from the swap pile
     * @param handCard the card that was placed into the swap pile
     */
    fun refreshAtSwapCard(swapCard: Card, handCard: Card) {}

    /**
     * play combinations.
     *
     * @param playedCards the list of all cards that were played
     */
    fun refreshAtPlayCard(playedCards: List<Card>) {}

    /**
     *active player's score has changed.
     *
     * @param activePlayerScore the updated score
     */
    fun refreshScore(activePlayerScore: Int) {}

    /**
     *  game has ended.
     *
     */
    fun refreshAtGameEnd() {}
}