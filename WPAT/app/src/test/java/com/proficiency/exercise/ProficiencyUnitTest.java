package com.proficiency.exercise;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Proficiency local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ProficiencyUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void stringTest() {

        assertEquals("title:Space Program", "title:Space Program");


        assertEquals("title:Space Program", "title:Space Program 2");
    }


}