package org.squashleague;

import static org.junit.Assert.fail;

/**
 * @author jamesdbloom
 */
public class Asserts {

    public static void assertEndsWith(String string, String substring) {
        if (!string.endsWith(substring)) {
            fail("[" + substring + "] does not end with [" + string + "]");
        }
    }

    public static void assertContains(String string, String substring) {
        if (!string.contains(substring)) {
            fail("[" + substring + "] not contained in [" + string + "]");
        }
    }

    public static void assertContainsEitherOr(String string, String either, String or) {
        if (!string.contains(either)) {
            if (!string.contains(or)) {
                fail("Either [" + either + "] or [" + or + "] not contained in [" + string + "]");
            }
        }
    }

    public static void assertDoesNotContain(String string, String substring) {
        if (string.contains(substring)) {
            fail("[" + substring + "] is contained in [" + string + "]");
        }
    }

    public static void assertMoreThan(int referenceValue, int testValue) {
        if (testValue <= referenceValue) {
            fail("expected [" + testValue + "] to be larger than [" + referenceValue + "]");
        }
    }
}
