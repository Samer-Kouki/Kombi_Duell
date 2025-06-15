package service

import entity.Card
import entity.CardValue
import entity.PlayerAction

/**
 * Der Service ist zuständig für das Ausspielen und Bewerten von Kartenkombinationen.
 */
class PlayCardsService(private val rootService: RootService) : AbstractRefreshingService() {

    /**
     * Prüft, ob die übergebenen Karten eine gültige Kombination ergeben.
     * Gültig sind Triplet, Quadruple oder Sequenz.
     */
    fun validate(selectedCards: List<Card>) {
        if (!isTriplet(selectedCards) && !isQuadruple(selectedCards) && !isSequence(selectedCards)) {
            throw IllegalArgumentException("Die ausgewählten Karten ergeben keine gültige Kombination.")
        }
    }

    /**
     * Führt das Ausspielen der gültigen Kombination aus:
     * - Entfernt Karten aus der Hand
     * - Fügt sie dem Ablagestapel hinzu
     * - Vergibt Punkte
     * - Benachrichtigt die GUI
     */
    fun execute(selectedCards: List<Card>) {
        validate(selectedCards)

        when {
            isTriplet(selectedCards) -> playTriplet(selectedCards)
            isQuadruple(selectedCards) -> playQuadruple(selectedCards)
            isSequence(selectedCards) -> playSequence(selectedCards)
        }

        val game = rootService.currentGame
        if (game != null) {
            game.performedActions.add(PlayerAction.PLAY_COMBINATION)
            onAllRefreshables {
                refreshScore(game.activePlayer.score)
            }
        }
    }

    /** Prüft auf Drilling: genau 3 Karten mit gleichem Wert. */
    private fun isTriplet(cards: List<Card>): Boolean {
        return cards.size == 3 && cards.all { it.value == cards[0].value }
    }

    /** Prüft auf Vierling: genau 4 Karten mit gleichem Wert. */
    private fun isQuadruple(cards: List<Card>): Boolean {
        return cards.size == 4 && cards.all { it.value == cards[0].value }
    }

    /**
     * Prüft auf Sequenz:
     * - mindestens 3 Karten
     * - gleiche Farbe
     * - aufeinanderfolgende Werte, wobei A→2 erlaubt ist (ringförmig)
     */
    private fun isSequence(cards: List<Card>): Boolean {
        if (cards.size < 3) return false
        if (!cards.all { it.color == cards[0].color }) return false

        val sorted = cards.sortedBy { it.value.ordinal }

        for (i in 0 until sorted.size - 1) {
            val current = sorted[i].value.ordinal
            val next = sorted[i + 1].value.ordinal
            val expectedNext = (current + 1) % CardValue.values().size
            if (next != expectedNext) return false
        }

        return true
    }

    /** Spielt ein Triplet aus und vergibt 10 Punkte. */
    private fun playTriplet(cards: List<Card>) {
        val game = rootService.currentGame ?: return
        val player = game.activePlayer
        moveCardsToDiscard(cards, player)
        addPoints(10)
    }

    /** Spielt einen Vierling aus und vergibt 15 Punkte. */
    private fun playQuadruple(cards: List<Card>) {
        val game = rootService.currentGame ?: return
        val player = game.activePlayer
        moveCardsToDiscard(cards, player)
        addPoints(15)
    }

    /** Spielt eine Sequenz aus und vergibt 2 Punkte pro Karte. */
    private fun playSequence(cards: List<Card>) {
        val game = rootService.currentGame ?: return
        val player = game.activePlayer
        moveCardsToDiscard(cards, player)
        addPoints(cards.size * 2)
    }

    /** Verschiebt Karten von der Hand in den Ablagestapel. */
    private fun moveCardsToDiscard(cards: List<Card>, player: entity.Player) {
        cards.forEach {
            player.handCards.remove(it)
            player.discardPile.add(it)
        }
    }

    /** Fügt Punkte dem aktiven Spieler hinzu. */
    private fun addPoints(score: Int) {
        val game = rootService.currentGame ?: return
        game.activePlayer.score += score
    }
}
