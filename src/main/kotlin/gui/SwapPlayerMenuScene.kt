package gui

import tools.aqua.bgw.components.uicomponents.Button
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.core.MenuScene
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.ColorVisual
import service.Refreshable
import tools.aqua.bgw.visual.ImageVisual


/**
 * Diese Szene kommt zwischen zwei Zügen – also wenn der nächste Spieler dran ist.
 * Praktisch für Hotseat-Spiele (ein Gerät, zwei Spieler).
 */
class SwapPlayerMenuScene : MenuScene(400, 200), Refreshable {

    // Button, um den nächsten Zug zu starten
    val continueButton = Button(
        width = 200, height = 40,
        posX = 100, posY = 120,
        text = "Weiter"
    ).apply {
        visual = ColorVisual(180, 220, 180)
    }

    // Info-Text oben, der sagt, dass jetzt der andere dran ist
    private val infoLabel = Label(
        width = 380, height = 60,
        posX = 10, posY = 30,
        text = "Bitte übergebe den Bildschirm an den nächsten Spieler",
        font = Font(size = 16)
    )

    // Szene zusammensetzen
    init {
        background = ImageVisual("1234.png")
        opacity = 1.0
        addComponents(infoLabel, continueButton)
    }
}
