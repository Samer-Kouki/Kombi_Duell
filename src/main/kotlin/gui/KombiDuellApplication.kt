package gui

import service.RootService
import service.Refreshable
import tools.aqua.bgw.core.BoardGameApplication
import tools.aqua.bgw.visual.ColorVisual
import tools.aqua.bgw.visual.ImageVisual


// Hauptklasse der App – hier wird alles gestartet und gesteuert
class KombiDuellApplication : BoardGameApplication("Kombi-Duell"), Refreshable {

    // Zentrale Spiellogik (alle Services + Spielstatus)
    private val rootService = RootService()

    // Die Spielszene – mit Handkarten, Tauschstapel usw.
    private val gameScene = KombiDuellGameScene(rootService)

    // Startmenü – hier geben die Spieler ihre Namen ein
    private val newGameMenuScene = NewGameMenuScene(rootService).apply {
        // Wenn man auf „Start“ klickt , Spiel wird gestartet
        startButton.onMouseClicked = {
            val name1 = player1Input.text.trim()
            val name2 = player2Input.text.trim()

            if (name1.isBlank() || name2.isBlank()) {
                player1Input.text = ""
                player2Input.text = ""
                player1Input.prompt = "Namen eingeben!"
            } else if (name1 == name2) {
                player2Input.text = ""
                player2Input.prompt = "Namen dürfen nicht gleich sein!"
            } else {
                rootService.gameService.startGame(name1, name2)
                showGameScene(gameScene)
                hideMenuScene()
            }
        }



        // Wenn man auf „Beenden“ klickt  dann App schließen
        quitButton.onMouseClicked = { exit() }
    }

    // Diese Szene kommt beim Spielerwechsel
    private val swapPlayerMenuScene = SwapPlayerMenuScene().apply {
        continueButton.onMouseClicked = { hideMenuScene() }
    }

    // Menü nach Spielende – zeigt Punkte und bietet Neustart/Beenden
    private val gameFinishedMenuScene = GameFinishedMenuScene(rootService).apply {
        newGameButton.onMouseClicked = {
            showMenuScene(newGameMenuScene)
        }
        quitButton.onMouseClicked = { exit() }
    }

    // Wird beim Start der App ausgeführt
    init {
        // Alle Szenen werden „refreshable“ gemacht dann sie können reagieren
        rootService.addRefreshables(
            this,
            gameScene,
            newGameMenuScene,
            swapPlayerMenuScene,
            gameFinishedMenuScene
        )

        background = ImageVisual("1234.png")

        showMenuScene(newGameMenuScene)
    }

    // Wenn ein Spiel gestartet wird und  Menü ausblenden
    override fun refreshAtGameStart() {
        hideMenuScene()
    }

    // Wenn das Spiel vorbei ist dann  Ergebnis-Szene anzeigen
    override fun refreshAtGameEnd() {
        showMenuScene(gameFinishedMenuScene)
    }

    // Wenn ein Spieler fertig ist danch  Spielerwechsel-Szene zeigen
    override fun refreshAtTurnEnd() {
        showMenuScene(swapPlayerMenuScene)
    }
}
