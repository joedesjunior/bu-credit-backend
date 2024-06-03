package com.bu.credit.backend.enums;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class StatusDebtTest {

    @Test
    void testIsValid() {
        Assertions.assertTrue(StatusDebt.isValid("pay"));
        Assertions.assertTrue(StatusDebt.isValid("pending"));
        Assertions.assertFalse(StatusDebt.isValid("paid"));
    }
}