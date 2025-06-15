package service

import entity.PlayerAction

class DrawCardService(private val rootService: RootService) : AbstractRefreshingService() {

    /**
     *
     * Voraussezungen :
     * -  weniger  10 Handkarten
     * -  Nachziehstapel enthält noch Karten
     * - gleiche Aktion hintereindander
     */
    fun validate() {
        val game = rootService.currentGame
            ?: throw IllegalStateException("Kein aktives Spiel.")

        val player = game.activePlayer

        if (player.handCards.size >= 10) {
            throw IllegalStateException("Spieler darf nicht mehr als 10 Handkarten haben.")
        }

        if (game.drawPile.isEmpty()) {
            throw IllegalStateException("Nachziehstapel ist leer.")
        }

        if (game.performedActions.contains(PlayerAction.DRAW_CARD)) {
            throw IllegalStateException("Ziehen wurde bereits in diesem Zug ausgeführt.")
        }
    }

    /**
     *
     * - Eine Karte wird vom Nachziehstapel genommen
     * - Der Spieler erhält die Karte auf die Hand
     * - Die Aktion wird hinzugefügt zu "performedActions"
     * - Die Benutzeroberfläche wird informiert
     */
    fun execute() {
        validate()

        val game = rootService.currentGame ?: return
        val player = game.activePlayer

        val card = game.drawPile.removeFirst()
        player.handCards.add(card)

        game.performedActions.add(PlayerAction.DRAW_CARD)

        onAllRefreshables {
            refreshAtDrawCard(card)
        }
    }
}
