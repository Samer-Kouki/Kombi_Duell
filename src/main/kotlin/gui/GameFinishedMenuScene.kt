package gui

import service.Refreshable
import service.RootService
import tools.aqua.bgw.components.uicomponents.Button
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.core.MenuScene
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.ColorVisual
import tools.aqua.bgw.visual.ImageVisual


// Szene, die erscheint wenn das Spiel zu Ende ist
class GameFinishedMenuScene(private val rootService: RootService)
    : MenuScene(400, 300), Refreshable {

    // Überschrift oben
    private val headlineLabel = Label(
        width = 300, height = 40,
        posX = 50, posY = 20,
        text = "Spiel beendet",
        font = Font(size = 22)
    )

    // Punktestand von Spieler 1
    private val p1ScoreLabel = Label(
        width = 300, height = 30,
        posX = 50, posY = 80,
        text = ""
    )

    // Punktestand von Spieler 2
    private val p2ScoreLabel = Label(
        width = 300, height = 30,
        posX = 50, posY = 120,
        text = ""
    )

    // Zeigt an, wer gewonnen hat oder ob’s unentschieden ist
    private val resultLabel = Label(
        width = 300, height = 30,
        posX = 50, posY = 160,
        text = ""
    )

    // Button, um ein neues Spiel zu starten
    val newGameButton = Button(
        width = 140, height = 35,
        posX = 50, posY = 220,
        text = "Neues Spiel"
    ).apply {
        visual = ColorVisual(136, 221, 136)
    }

    // Button zum Beenden der App
    val quitButton = Button(
        width = 140, height = 35,
        posX = 210, posY = 220,
        text = "Beenden"
    ).apply {
        visual = ColorVisual(221, 136, 136)
    }

    // Szene zusammenbauen
    init {
        background = ImageVisual("1234.png")
        opacity = 1.0
        addComponents(
            headlineLabel,
            p1ScoreLabel,
            p2ScoreLabel,
            resultLabel,
            newGameButton,
            quitButton
        )
    }

    // Wird aufgerufen, wenn das Spiel vorbei ist  danch zeigt Punkte & Gewinner
    override fun refreshAtGameEnd() {
        val game = rootService.currentGame ?: return

        val p1 = game.activePlayer
        val p2 = game.inactivePlayer

        // Punktestände setzen
        p1ScoreLabel.text = "${p1.name}: ${p1.score} Punkte"
        p2ScoreLabel.text = "${p2.name}: ${p2.score} Punkte"

        // Gewinner bestimmen oder Unentschieden anzeigen
        resultLabel.text = when {
            p1.score > p2.score -> "${p1.name} gewinnt!"
            p2.score > p1.score -> "${p2.name} gewinnt!"
            else -> "Unentschieden."
        }
    }
}
