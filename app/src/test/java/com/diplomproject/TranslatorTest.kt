package com.diplomproject

import com.diplomproject.di.koin_modules.ApiModule
import com.diplomproject.di.koin_modules.AppModule
import com.diplomproject.di.koin_modules.CiceroneModule
import com.diplomproject.domain.base.BaseFragment
import com.diplomproject.model.datasource.RetrofitImplementation
import com.diplomproject.navigation.AndroidScreens
import com.diplomproject.view.DescriptionFragment
import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertSame
import org.junit.Assert.assertTrue
import org.junit.Test

class TranslatorTest {
    @Test
    fun testForNotEqauls() {
        assertNotEquals(BaseFragment.DIALOG_FRAGMENT_TAG, DescriptionFragment.CURRENT_DATA_MODEl)
    }


}
