package com.ruoyi.pms.constant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

class PmsConstantsTest
{
    @Test
    void resolvePollIntervalSec_nullOrNonPositive_usesRealtimeFiveSeconds()
    {
        assertEquals(5, PmsConstants.resolvePollIntervalSec(null));
        assertEquals(5, PmsConstants.resolvePollIntervalSec(Integer.valueOf(0)));
        assertEquals(5, PmsConstants.resolvePollIntervalSec(Integer.valueOf(-1)));
    }

    @Test
    void resolvePollIntervalSec_positive_usesConfiguredSeconds()
    {
        assertEquals(30, PmsConstants.resolvePollIntervalSec(Integer.valueOf(30)));
        assertEquals(1, PmsConstants.resolvePollIntervalSec(Integer.valueOf(1)));
    }
}
