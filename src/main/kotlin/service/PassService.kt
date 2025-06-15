package service

import entity.PlayerAction

/**
 *
 */
class PassService(private val rootService: RootService) : AbstractRefreshingService() {

    /**
     *
     * Passen ist immer erlaubt
     *
     */
    fun validate() {
        // absichtlich leer
    }

    /**
     * Führt die Pass-Aktion aus.
     * - Trägt die Aktion ins performedActions
     * - Beendet  den Zug
     */
    fun execute() {
        val game = rootService.currentGame ?: return

        game.performedActions.add(PlayerAction.PASS)

        
        rootService.gameService.endTurn()
    }
}
