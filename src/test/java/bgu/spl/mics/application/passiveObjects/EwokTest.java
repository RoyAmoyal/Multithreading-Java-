package bgu.spl.mics.application.passiveObjects;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EwokTest {
    Ewok e1;

    @BeforeEach
    void setUp() {
        e1 = new Ewok();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testAcquire() {
        e1.available = true;
        e1.acquire();
        assertTrue(!e1.available); //We expect for acquired Ewok to be unavailable.
    }

    @Test
    void testRelease() {
        e1.available = false;
        e1.release();
        assertTrue(e1.available); //We expect for released Ewok to be available.
    }
}