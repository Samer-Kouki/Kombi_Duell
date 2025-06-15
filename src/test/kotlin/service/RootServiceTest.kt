package service

import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertNull

/**
 * Testet die Basics vom RootService.
 */
class RootServiceTest {

    /**
     * checkt, ob alle Services da sind und Spiel am Anfang noch nicht läuft
     */
    @Test
    fun testServiceInitialization() {
        val rootService = RootService()

        // die ganzen Services müssen da sein
        assertNotNull(rootService.modelService)
        assertNotNull(rootService.gameService)
        assertNotNull(rootService.drawService)
        assertNotNull(rootService.swapService)
        assertNotNull(rootService.playCardsService)
        assertNotNull(rootService.passService)

        // direkt nach dem Start gibt’s noch kein aktives Spiel
        assertNull(rootService.currentGame)
    }

    /**
     * prüft, ob nach Spielstart wirklich ein Spiel da ist
     */
    @Test
    fun testStartGameInitializesGame() {
        val rootService = RootService()

        // Spiel starten mit 2 Spielern
        rootService.modelService.createGame("Samer", "Eni")

        // jetzt sollte currentGame nicht mehr null sein
        assertNotNull(rootService.currentGame)
    }
}
