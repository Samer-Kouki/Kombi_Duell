package service


import entity.KombiDuell
import entity.Player
import kotlin.random.Random
import entity.Card
import entity.CardSuit
import entity.CardValue


/**
 *zentrale Spielmodell .
 * Er ist zuständig für Erstellung und Spielerwechsel.
 */
class ModelService(private val rootService: RootService) : AbstractRefreshingService() {

    /**
      neues Spiel mit zwei Spielern und initialisiert das Modell.
     */


    fun createGame(firstPlayerName: String, secondPlayerName: String) {
        val player1 = Player(name = firstPlayerName)
        val player2 = Player(name = secondPlayerName)

        // Zufällig Startspieler wählen
        val activePlayer: Player
        val inactivePlayer: Player
        if (Random.nextBoolean()) {
            activePlayer = player1
            inactivePlayer = player2
        } else {
            activePlayer = player2
            inactivePlayer = player1
        }

        // Alle 52 Karten mischen
        val fullDeck = CardSuit.values().flatMap { suit ->
            CardValue.values().map { value -> Card(value, suit) }
        }.shuffled()

        // Karten austeilen
        player1.handCards.addAll(fullDeck.take(7))
        player2.handCards.addAll(fullDeck.drop(7).take(7))
        val swapPile = fullDeck.drop(14).take(3).toMutableList()
        val drawPile = fullDeck.drop(17).toMutableList()

        // Spiel erzeugen
        rootService.currentGame = KombiDuell(
            activePlayer = activePlayer,
            inactivePlayer = inactivePlayer
        ).apply {
            this.swapPile.addAll(swapPile)
            this.drawPile.addAll(drawPile)
        }
    }


    /**
     * aktuelle Spielmodell als Rückgabe .
     * @throws IllegalStateException wenn kein Spiel aktiv ist.
     */
    fun getGame(): KombiDuell {
        return rootService.currentGame
            ?: throw IllegalStateException("Es wurde noch kein Spiel gestartet.")
    }

    /**
     * Tauscht den aktiven und inaktiven Spieler
     * , Aktionsliste löschen
     * und
     * die Rundenzahl ++.
     */
    fun goToNextPlayer() {
        val game = rootService.currentGame ?: return

        val previousActive = game.activePlayer
        game.activePlayer = game.inactivePlayer
        game.inactivePlayer = previousActive

        game.performedActions.clear()

        game.round += 1
    }
}
