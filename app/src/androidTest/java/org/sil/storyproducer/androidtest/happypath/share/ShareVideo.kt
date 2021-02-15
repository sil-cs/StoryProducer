package org.sil.storyproducer.androidtest.happypath.share

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
class ShareVideo : VideoBase() {

    private var base: SharePhaseBase = SharePhaseBase(this)

    @Before
    fun setup() {
        PhaseTestBase.revertWorkspaceToCleanState(this)
        base.setUp()
    }

    @Test
    fun when_thereAreNoExportedVideos_should_showNoVideosMessage() {
        base.when_thereAreNoExportedVideos_should_showNoVideosMessage()
    }

    @Test
    fun when_aVideoHasBeenExported_should_showItInTheList() {
        base.when_aVideoHasBeenExported_should_showItInTheList()
    }
}