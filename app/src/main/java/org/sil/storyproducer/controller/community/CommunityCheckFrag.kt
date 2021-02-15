package org.sil.storyproducer.controller.community

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.sil.storyproducer.film.R
import org.sil.storyproducer.controller.MultiRecordFrag
import org.sil.storyproducer.controller.SlidePlayerFrag
import org.sil.storyproducer.controller.VideoPlayerFrag
import org.sil.storyproducer.controller.adapter.RecordingsListAdapter
import org.sil.storyproducer.model.Workspace
import org.sil.storyproducer.tools.toolbar.RecordingToolbar

/**
 * Fragment for the community check view. The purpose of this phase is for the community to make
 * sure the draft is okay and leave any comments should they feel the need
 */
class CommunityCheckFrag : MultiRecordFrag() {
    override var recordingToolbar: RecordingToolbar = RecordingToolbar()
    private var dispList : RecordingsListAdapter.RecordingsListModal? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_community_check, container, false)
        setToolbar()
        dispList = RecordingsListAdapter.RecordingsListModal(context!!, recordingToolbar)
        dispList?.embedList(rootView!! as ViewGroup)
        dispList?.setSlideNum(slideNum)
        //This enables the "onStartedToolbarMedia" to be invoked.
        dispList?.setParentFragment(this)
        dispList?.show()

        setupCameraAndEditButton()

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        storyPlayer = if(Workspace.activeStory.isVideoStory) {
            VideoPlayerFrag()
        } else {
            SlidePlayerFrag()
        }
        storyPlayer?.startSlide = slideNum
        storyPlayer?.slideRange = 1
        storyPlayer?.phaseType = Workspace.activePhase.phaseType
        var transaction = childFragmentManager.beginTransaction()
        transaction.replace(R.id.phase_player, storyPlayer!!).commit()
    }

    override fun onPause() {
        super.onPause()
        dispList?.stopAudio()
    }

    /**
     * This function serves to handle page changes and stops the audio streams from
     * continuing.
     */
    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        dispList?.stopAudio()
    }

    override fun onStartedToolbarMedia() {
        super.onStartedToolbarMedia()

        dispList!!.stopAudio()
        //this is needed here to - when you are playing the reference audio and start recording
        //the new audio file pops up, and in the wrong format.
        dispList?.resetRecordingList()
    }

    override fun onStartedSlidePlayBack() {
        super.onStartedSlidePlayBack()

        dispList!!.stopAudio()
    }
}
