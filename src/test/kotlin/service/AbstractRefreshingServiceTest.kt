package entity

import org.junit.jupiter.api.Assertions.assertTrue
import service.AbstractRefreshingService
import service.Refreshable
import kotlin.test.BeforeTest
import kotlin.test.Test

/**
 * Test für AbstractRefreshingService – prüft, ob Refreshables benachrichtigt werden
 */
class AbstractRefreshingServiceTest {

    private lateinit var testRefreshable: Refreshable
    private lateinit var abstractRefreshingService: AbstractRefreshingService

    /**
     * Vor jedem Test: neues Refreshable und Service erstellen, verknüpfen
     */
    @BeforeTest
    fun setUp() {
        testRefreshable = object : Refreshable {}
        abstractRefreshingService = object : AbstractRefreshingService() {}
        abstractRefreshingService.addRefreshable(testRefreshable)
    }

    /**
     * Testet, ob das Refreshable benachrichtigt wurde (refresh wurde aufgerufen)
     */
    @Test
    fun testIfRefreshableIsNotified() {
        var refreshWasCalled = false
        var isTestRefreshable = false

        // Hilfsfunktion, die wir nur hier nutzen zum Überprüfen
        fun Refreshable.refreshForTesting() {
            refreshWasCalled = true
            isTestRefreshable = this === testRefreshable
        }

        // Alle verbundenen Refreshables durchgehen und testen
        abstractRefreshingService.onAllRefreshables { this.refreshForTesting() }

        // Jetzt sollte refresh aufgerufen worden sein
        assertTrue(refreshWasCalled, "refresh() sollte aufgerufen werden.")
        assertTrue(isTestRefreshable, "refresh() sollte auf dem richtigen Objekt " +
                "aufgerufen werden.")
    }
}
