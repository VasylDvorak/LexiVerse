package com.diplomproject

import com.diplomproject.view.base_for_dictionary.BaseFragment
import com.diplomproject.view.description.CURRENT_DATA_MODEl
import org.junit.Assert.assertNotEquals
import org.junit.Test

class TranslatorTest {
    @Test
    fun testForNotEqauls() {
        assertNotEquals(BaseFragment.DIALOG_FRAGMENT_TAG, CURRENT_DATA_MODEl)
    }


}
