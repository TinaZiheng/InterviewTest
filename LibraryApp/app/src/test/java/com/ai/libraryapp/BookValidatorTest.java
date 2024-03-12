package com.ai.libraryapp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.ai.libraryapp.utlis.BookVerifyUtil;

import org.junit.Test;

public class BookValidatorTest {
    @Test
    public void nameValidator_Empty_ReturnFalse() {
        assertFalse(BookVerifyUtil.INSTANCE.isValidBookInput(""));
    }

    @Test
    public void nameValidator_Correct_return() {
        assertTrue(BookVerifyUtil.INSTANCE.isValidBookInput("Thinking in java"));
    }
}
