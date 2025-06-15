package service


import entity.PlayerAction

/**
 * Der GameService steuert den Spielablauf in Kombi-Duell:
 *
 */
class GameService(private val rootService: RootService) : AbstractRefreshingService() {

    /**
     * Startet ein neues Spiel mit 2 Spielern.
     * Initialisiert das Spielmodell und informiert die Benutzeroberfläche.
     */
    fun startGame(firstPlayerName: String, secondPlayerName: String) {
        rootService.modelService.createGame(firstPlayerName, secondPlayerName)

        // Benutzeroberfläche benachrichtigen
        onAllRefreshables { refreshAtGameStart() }

        startTurn()
    }

    /**
     * Wird zu Beginn eines Spielerzugs aufgerufen.
     * Die GUI kann darauf reagieren, z. B. aktiven Spieler anzeigen.
     */
    fun startTurn() {
        onAllRefreshables { refreshAtTurnStart() }
    }

    /**
     * Beendet den aktuellen Spielzug:
     * - UI benachrichtigen
     * - Spieler wechseln
     * - neue Runde beginnen
     */
    fun endTurn() {
        val game = rootService.currentGame ?: return

        // Wurde in diesem Zug ausschließlich gepasst?
        val onlyPassedThisTurn = game.performedActions.size == 1 &&
                game.performedActions.contains(PlayerAction.PASS)

        val playerHasNoCards = game.activePlayer.handCards.isEmpty()

        // Spielende prüfen: zwei aufeinanderfolgende reine PASS-Züge oder keine Karten mehr
        if ((onlyPassedThisTurn && game.lastTurnWasPass) || playerHasNoCards) {
            rootService.gameService.endGame()
            return
        }

        // Merken: Hat der aktuelle Spieler ausschließlich gepasst?
        game.lastTurnWasPass = onlyPassedThisTurn

        onAllRefreshables { refreshAtTurnEnd() }
        rootService.modelService.goToNextPlayer()
        startTurn()
    }



    /**
     * Prüft, ob die angegebene Aktion noch erlaubt ist.
     * - Es dürfen max. 2 verschiedene Aktionen pro Zug gemacht werden
     * - `Pass` zählt dabei nicht als Aktion
     */
    fun canDoAction(action: PlayerAction): Boolean {
        val game = rootService.currentGame ?: return false

        if (action == PlayerAction.PASS) return true // Pass ist immer erlaubt

        val alreadyUsed = game.performedActions.contains(action)
        val limitReached = game.performedActions.size >= 2

        return !alreadyUsed && !limitReached
    }

    /**
     * Beendet das Spiel und informiert die Benutzeroberfläche.
     */
    fun endGame() {
        onAllRefreshables { refreshAtGameEnd() }
    }
}
