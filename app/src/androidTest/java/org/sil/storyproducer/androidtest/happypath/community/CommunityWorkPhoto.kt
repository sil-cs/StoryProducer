package org.sil.storyproducer.androidtest.happypath.community

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.sil.storyproducer.androidtest.happypath.PhaseTestBase
import org.sil.storyproducer.androidtest.happypath.base.PhotoBase
import org.sil.storyproducer.androidtest.happypath.base.annotation.PhotoTest

@LargeTest
@PhotoTest
@RunWith(AndroidJUnit4::class)
class CommunityWorkPhoto() : PhotoBase() {
    private var base: CommunityWorkPhaseBase = CommunityWorkPhaseBase(this)

    @Before
    fun setup() {
        PhaseTestBase.revertWorkspaceToCleanState(this)
        base.setUp()
    }

    @Test
    fun should_BeAbleToSwipeBetweenSlides() {
        base.should_BeAbleToSwipeBetweenSlides()
    }

    @Test
    fun should_BeAbleToPlayTranslationOfASlide() {
        base.should_BeAbleToPlayTranslationOfASlide()
    }

    @Test
    fun should_BeAbleToRecordFeedback() {
        base.should_BeAbleToRecordFeedback()
    }

    @Test
    fun should_beAbleToSwipeToNextPhase() {
        base.should_beAbleToSwipeToNextPhase()
    }
}