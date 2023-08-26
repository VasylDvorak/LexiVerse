package com.diplomproject

import com.diplomproject.view.base_fragment_dictionary.BaseFragment
import com.diplomproject.view.description.DescriptionFragment
import org.junit.Assert.assertNotEquals
import org.junit.Test

class TranslatorTest {
    @Test
    fun testForNotEqauls() {
        assertNotEquals(BaseFragment.DIALOG_FRAGMENT_TAG, DescriptionFragment.CURRENT_DATA_MODEl)
    }


}
