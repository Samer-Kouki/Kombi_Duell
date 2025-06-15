package gui

import tools.aqua.bgw.visual.ImageVisual
import service.RootService
import service.Refreshable
import tools.aqua.bgw.components.uicomponents.Button
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.components.uicomponents.TextField
import tools.aqua.bgw.core.MenuScene
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.ColorVisual

/**
 * Menü, das beim Start des Spiels angezeigt wird.
 * Spieler geben hier ihre Namen ein und können das Spiel starten.
 */
class NewGameMenuScene(private val rootService: RootService) : MenuScene(400, 300), Refreshable {



    // Eingabefeld für Spieler 1
    val player1Input = TextField(
        width = 200, height = 35,
        posX = 100, posY = 60,
        prompt = "Spieler 1"
    )

    // Eingabefeld für Spieler 2
    val player2Input = TextField(
        width = 200, height = 35,
        posX = 100, posY = 110,
        prompt = "Spieler 2"
    )

    // Button zum Starten des Spiels
    val startButton = Button(
        width = 140, height = 35,
        posX = 50, posY = 200,
        text = "Start"
    ).apply {
        visual = ColorVisual(136, 221, 136)
    }

    // Button zum Beenden der App
    val quitButton = Button(
        width = 140, height = 35,
        posX = 210, posY = 200,
        text = "Beenden"
    ).apply {
        visual = ColorVisual(221, 136, 136) // rot = Stopp
    }

    // Überschrift im Menü
    private val headlineLabel = Label(
        width = 300, height = 40,
        posX = 50, posY = 10,
        text = "Neues Spiel starten",
        font = Font(size = 22)
    )

    // Szene zusammensetzen
    init {
        background = ImageVisual("1234.png")
        opacity = 1.0

        addComponents(
            headlineLabel,
            player1Input,
            player2Input,
            startButton,
            quitButton
        )
    }
}
