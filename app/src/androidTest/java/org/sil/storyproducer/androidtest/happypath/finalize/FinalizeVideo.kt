package org.sil.storyproducer.androidtest.happypath.finalize

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.sil.storyproducer.androidtest.happypath.PhaseTestBase
import org.sil.storyproducer.androidtest.happypath.base.PhotoBase
import org.sil.storyproducer.androidtest.happypath.base.VideoBase
import org.sil.storyproducer.androidtest.happypath.base.annotation.PhotoTest
import org.sil.storyproducer.androidtest.happypath.base.annotation.VideoTest

@LargeTest
@VideoTest
@RunWith(AndroidJUnit4::class)
class FinalizeVideo() : VideoBase() {
    private var base: FinalizePhaseBase = FinalizePhaseBase(this)

    @Before
    fun setup() {
        PhaseTestBase.revertWorkspaceToCleanState(this)
        base.setUp()
    }

    @Test
    fun test_updateLocalCredits() {
        base.test_updateLocalCredits()
    }

    @Test
    // Will fail if updateLocalCredits() doesn't work correctly.
    fun when_createVideoButtonPressedWithDefaultOptions_should_produceVideoFileWithMp4Extension() {
        base.when_createVideoButtonPressedWithDefaultOptions_should_produceVideoFileWithMp4Extension()
    }
}