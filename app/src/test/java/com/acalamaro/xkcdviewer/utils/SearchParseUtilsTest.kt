package com.acalamaro.xkcdviewer.utils

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class SearchParseUtilsTest {

    class URLValidatorTest {
        @Test
        fun urlValidator_correctUrlSimple_ReturnsTrue() {
            assertTrue(SearchParseUtils.isUrlValidXkcd("https://xkcd.com/235/"))
        }
        @Test
        fun urlValidator_incorrectUrlNegativeNumber_ReturnsFalse() {
            assertFalse(SearchParseUtils.isUrlValidXkcd("https://xkcd.com/-235/"))
        }

        @Test
        fun urlValidator_incorrectUrlText_ReturnsFalse() {
            assertFalse(SearchParseUtils.isUrlValidXkcd("https://xkcd.com/235/image"))
        }
    }

    class URLExtractionTest {
        @Test
        fun urlExtractionValidator_correct_returnsSameNumber() {
            assertEquals(235, SearchParseUtils.getComicNumberFromUrl("https://xkcd.com/235/"))
        }
    }
}