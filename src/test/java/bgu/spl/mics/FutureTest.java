package bgu.spl.mics;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterAll;

import org.junit.jupiter.api.Test;

import java.time.Year;
import java.util.concurrent.TimeUnit;


import static java.util.concurrent.TimeUnit.DAYS;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.junit.jupiter.api.Assertions.*;


public class FutureTest {

    private Future<String> future; //ignore this note

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
        assertEquals(future.get(Long.MAX_VALUE, DAYS) , future.get());

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

        long timeout2 = Long.MAX_VALUE;
        TimeUnit unit2 = DAYS;

        assertEquals(null, future.get(timeout,unit));
   //    assertEquals(future.get(), future.get(timeout2,unit2));

        future.resolve("someResult");

        assertEquals(null, future.get(timeout,unit));
        assertEquals(future.get(), future.get(timeout2,unit2));

    }

}
