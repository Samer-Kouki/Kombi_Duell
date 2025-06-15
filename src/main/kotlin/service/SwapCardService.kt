package service

import entity.Card
import entity.PlayerAction

/**
 * Der SwapCardService ist für das Tauschen einer Handkarte
 *
 */
class SwapCardService(private val rootService: RootService) : AbstractRefreshingService() {

    /**
     *  Kartentausch erlaubt ?
     *
     * Voraussetzungen:
     * - Beide Karten existiern
     * - Aktion noch nicht durchgeführt
     */
    fun validate(swapCard: Card, handCard: Card) {
        val game = rootService.currentGame
            ?: throw IllegalStateException("Kein laufendes Spiel vorhanden.")

        val player = game.activePlayer

        if (!game.swapPile.contains(swapCard)) {
            throw IllegalArgumentException("Die angegebene Tauschkarte befindet sich nicht im Tauschbereich.")
        }

        if (!player.handCards.contains(handCard)) {
            throw IllegalArgumentException("Die angegebene Handkarte befindet sich nicht auf der Hand des Spielers.")
        }

        if (game.performedActions.contains(PlayerAction.SWAP_CARD)) {
            throw IllegalStateException("Diese Aktion wurde in diesem Zug bereits ausgeführt.")
        }
    }

    /**
     * Führt den AusTausch :
     *
     */
    fun execute(swapCard: Card, handCard: Card) {
        validate(swapCard, handCard)

        val game = rootService.currentGame ?: return
        val player = game.activePlayer

        val index = player.handCards.indexOf(handCard)
        if (index >= 0) {
            player.handCards.removeAt(index)
            player.handCards.add(index, swapCard)
        }

        game.swapPile.remove(swapCard)
        game.swapPile.add(handCard)

        game.performedActions.add(PlayerAction.SWAP_CARD)

        onAllRefreshables {
            refreshAtSwapCard(swapCard, handCard)
        }
    }

}
