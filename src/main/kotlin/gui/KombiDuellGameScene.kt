package gui

// Imports für Logik, UI und Grafik
import entity.Card
import service.RootService
import service.Refreshable
import tools.aqua.bgw.components.container.CardStack
import tools.aqua.bgw.components.gamecomponentviews.CardView
import tools.aqua.bgw.components.uicomponents.Button
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.core.BoardGameScene
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.ColorVisual
import tools.aqua.bgw.visual.ImageVisual


// Das ist die Hauptspielszene – hier passiert das eigentliche Spiel
class KombiDuellGameScene(private val rootService: RootService)
    : BoardGameScene(1920, 1080), Refreshable {

    // Bilder für die Karten laden
    private val cardImageLoader = CardImageLoader()

    // Obere Karte vom Nachziehstapel (Rückseite zeigen)
    private val drawPileTopCardView = CardView(
        height = 200, width = 130,
        front = cardImageLoader.blankImage,
        back = cardImageLoader.backImage
    ).apply {
        posX = 100.0
        posY = 400.0
        showBack()
    }

    // Anzahl Karten im Nachziehstapel
    private val drawPileCountLabel = Label(
        width = 100, height = 50,
        posX = 100, posY = 360,
        text = "x",
        font = Font(size = 18)
    )

    // Drei Plätze für Tauschbereich
    private val swapPileViews = List(3) { index ->
        CardStack<CardView>(
            height = 200, width = 130,
            posX = 800 + index * 150, posY = 400,
            visual = ColorVisual(150, 150, 250)
        )
    }

    // Handkarten, Gegnerkarten, ausgewählte Kombikarten
    private val handCardViews = mutableListOf<CardView>()
    private val opponentCardViews = mutableListOf<CardView>()
    private val selectedCombinationCards = mutableListOf<Card>()

    // Ablagebereich für gespielte Kombinationen
    private val discardPileView = CardStack<CardView>(
        height = 200, width = 130,
        posX = 1600, posY = 440,
        visual = ColorVisual(255, 200, 200)
    )

    // Punkteanzeige + Rundenanzeige
    private val player1ScoreLabel = Label(width = 300, height = 50, posX = 50,
        posY = 50, text = "Spieler 1: 0", font = Font(size = 20))
    private val player2ScoreLabel = Label(width = 300, height = 50, posX = 50, posY = 100,
        text = "Spieler 2: 0", font = Font(size = 20))
    private val roundLabel = Label(width = 200, height = 50, posX = 1650, posY = 50,
        text = "Runde: 1", font = Font(size = 24))

    // Textzeile unten für Hinweise
    private val instructionLabel = Label(width = 600, height = 30, posX = 650, posY = 900,
        text = "", font = Font(size = 18))

    // Die vier Buttons für Aktionen
    private val drawButton = Button(width = 160, height = 40, posX = 400, posY = 950, text = "Karte ziehen")
    private val swapButton = Button(width = 160, height = 40, posX = 600, posY = 950, text = "Karte tauschen")
    private val playButton = Button(width = 160, height = 40, posX = 800, posY = 950, text = "Kombi ausspielen")
    private val passButton = Button(width = 160, height = 40, posX = 1000, posY = 950, text = "Passen")

    // Merkt sich, welche Karte für den Tausch ausgewählt wurde
    private var selectedHandCard: Card? = null

    // Konstruktor – Szene wird hier aufgebaut
    init {
        background = ImageVisual("1234.png")
        opacity = 1.0

        // Alles zur Szene hinzufügen
        addComponents(
            drawPileTopCardView, drawPileCountLabel,
            discardPileView, roundLabel, instructionLabel,
            drawButton, swapButton, playButton, passButton,
            player1ScoreLabel, player2ScoreLabel
        )
        addComponents(*swapPileViews.toTypedArray())

        // Wenn man auf "Karte ziehen" klickt
        drawButton.onMouseClicked = { rootService.drawService.execute() }

        // Wenn man tauschen will , zuerst Handkarte wählen
        swapButton.onMouseClicked = {
            instructionLabel.text = "Wähle eine Handkarte"
        }

        // Wenn man eine Kombination ausspielen will
        playButton.onMouseClicked = {
            try {
                rootService.playCardsService.execute(selectedCombinationCards.toList())
                refreshAtPlayCard(selectedCombinationCards.toList())
                selectedCombinationCards.clear()
                refreshAtGameStart()
                instructionLabel.text = "Kombi erfolgreich gespielt"

                val game = rootService.currentGame
                if (game != null && game.activePlayer.handCards.isEmpty()) {
                    rootService.gameService.endGame()
                }
            } catch (e: Exception) {
                instructionLabel.text = "Fehler: ${e.message}"
            }
        }

        // Wenn Spieler passt
        passButton.onMouseClicked = {
            rootService.passService.execute()
        }
    }

    // Macht eine Karte sichtbar (mit Bild vorne)
    private fun createCardView(card: Card): CardView =
        CardView(
            height = 200,
            width = 130,
            front = cardImageLoader.frontImageFor(card.color, card.value),
            back = cardImageLoader.backImage
        ).apply { showFront() }

    // Wird jedes Mal aufgerufen, wenn ein Zug startet (auch beim 1. Mal)
    override fun refreshAtGameStart() {
        val game = rootService.currentGame ?: return
        val player = game.activePlayer

        // Handkarten neu aufbauen
        handCardViews.forEach { it.removeFromParent() }
        handCardViews.clear()
        selectedCombinationCards.clear()

        player.handCards.forEachIndexed { index, card ->
            val cardView = createCardView(card).apply {
                posX = 200.0 + index * 140.0
                posY = 700.0
                onMouseClicked = {
                    if (selectedCombinationCards.contains(card)) {
                        selectedCombinationCards.remove(card)
                        this.posY = 700.0
                    } else {
                        selectedCombinationCards.add(card)
                        this.posY = 670.0
                    }
                    selectedHandCard = card
                    instructionLabel.text = "Ausgewählt: ${selectedCombinationCards.size} Karte(n)"
                }
            }
            handCardViews.add(cardView)
            addComponents(cardView)
        }

        // Karten des Gegners (verdeckt)
        opponentCardViews.forEach { it.removeFromParent() }
        opponentCardViews.clear()
        game.inactivePlayer.handCards.forEachIndexed { index, _ ->
            val backView = CardView(
                height = 200, width = 130,
                front = cardImageLoader.blankImage,
                back = cardImageLoader.backImage
            ).apply {
                posX = 300.0 + index * 140.0
                posY = 100.0
                showBack()
            }
            opponentCardViews.add(backView)
            addComponents(backView)
        }

        // Tauschbereich anzeigen
        game.swapPile.forEachIndexed { index, card ->
            val view = createCardView(card).apply {
                onMouseClicked = {
                    if (selectedHandCard != null) {
                        try {
                            rootService.swapService.execute(card, selectedHandCard!!)
                            selectedHandCard = null
                            instructionLabel.text = ""
                        } catch (e: Exception) {
                            instructionLabel.text = "Fehler: ${e.message}"
                        }
                    } else {
                        instructionLabel.text = "Bitte zuerst eine Handkarte wählen"
                    }
                }
            }
            swapPileViews[index].clear()
            swapPileViews[index].add(view)
        }

        drawPileTopCardView.showBack()
        drawPileCountLabel.text = "${game.drawPile.size}"
        roundLabel.text = "Runde: ${game.round}"
        player1ScoreLabel.text = "${game.activePlayer.name}: ${game.activePlayer.score} Punkte"
        player2ScoreLabel.text = "${game.inactivePlayer.name}: ${game.inactivePlayer.score} Punkte"

        // Spiel beenden, wenn Hand leer
        if (game.activePlayer.handCards.isEmpty()) {
            rootService.gameService.endGame()
        }
    }

    // Wird auch bei Spielzugstart aufgerufen dann macht dasselbe
    override fun refreshAtTurnStart() = refreshAtGameStart()

    // Neue Karte gezogen danach zur Hand hinzufügen
    override fun refreshAtDrawCard(drawnCard: Card) {
        val cardView = createCardView(drawnCard).apply {
            posX = 200.0 + handCardViews.size * 140.0
            posY = 700.0
            onMouseClicked = {
                if (selectedCombinationCards.contains(drawnCard)) {
                    selectedCombinationCards.remove(drawnCard)
                    this.posY = 700.0
                } else {
                    selectedCombinationCards.add(drawnCard)
                    this.posY = 670.0
                }
                selectedHandCard = drawnCard
                instructionLabel.text = "Ausgewählt: ${selectedCombinationCards.size} Karte(n)"
            }
        }
        handCardViews.add(cardView)
        addComponents(cardView)

        val game = rootService.currentGame ?: return
        drawPileTopCardView.showBack()
        drawPileCountLabel.text = "${game.drawPile.size}"
    }


    // Nach einem Tausch , alles neu anzeigen
    override fun refreshAtSwapCard(swapCard: Card, handCard: Card) {
        refreshAtGameStart()
    }

    // Gespielte Karten auf Ablagestapel anzeigen
    override fun refreshAtPlayCard(playedCards: List<Card>) {
        discardPileView.clear()
        playedCards.forEach { card ->
            val view = createCardView(card)
            discardPileView.add(view)
        }
    }

    // Punktestand wird aktualisiert
    override fun refreshScore(activePlayerScore: Int) {
        player1ScoreLabel.text = "Aktueller Spieler: $activePlayerScore Punkte"
    }
}
