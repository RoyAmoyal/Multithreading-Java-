package bgu.spl.mics;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;

import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;


import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.junit.jupiter.api.Assertions.*;


public class FutureTest {

    private Future<String> future;

    @BeforeEach
    public void setUp(){
        future = new Future<>();
    }

    @AfterEach
    public void tearDown() {}

    @Test
    public void testResolve(){
        String str = "someResult";
        future.resolve(str);
        assertTrue(future.isDone());
        assertTrue(str.equals(future.get()));
    }

    @Test
    public void testGet(){


        future.resolve("someResult");
        assertEquals(future.get(99999, MILLISECONDS) , future.get());

    }

    @Test
    public void testIsDone(){
        assertFalse(future.isDone()); //  We expect to get false if future isn`t been resolved.
        future.resolve("someResult");
        assertTrue(future.isDone());  //  We expect to get True if future has been resolved.
    }

    @Test
    public void testGetWithArg(){
        long timeout = 0;
        TimeUnit unit = MILLISECONDS;

        long timeout2 = 9999999;
        TimeUnit unit2 = MILLISECONDS;

        assertEquals(null, future.get(timeout,unit));

        future.resolve("someResult");

        assertEquals(null, future.get(timeout,unit));
        assertEquals(future.get(), future.get(timeout2,unit2));

    }

}
