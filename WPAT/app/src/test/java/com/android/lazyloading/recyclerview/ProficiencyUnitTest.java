package com.android.lazyloading.recyclerview;

import android.util.Log;

import com.android.lazyloading.recyclerview.lazyload.LazyLoadActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

/**
 * Proficiency local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ProficiencyUnitTest {

    @Before
    public void setUp() {
        System.out.println("Set up called");
    }

    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void testString_Good() {
        String result = new String("Hello ").concat("World");
        assertEquals("Hello World", result);
    }


    @Test
    public void testString_GoodAgain() {
        String result = "Hello" + "world";
        assertEquals("Helloworld", result);
    }

    @Test
    public void stringTest() {

        assertEquals("title:Space Program", "title:Space Program");

        assertEquals("title:Space Program", "title:Space Program 2");
    }

    @After
    public void tearDown() {
        System.out.println("tear down called");
    }
}