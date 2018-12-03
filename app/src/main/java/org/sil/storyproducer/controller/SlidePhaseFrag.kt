package org.sil.storyproducer.controller

import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.SpannableStringBuilder
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import org.sil.storyproducer.R
import org.sil.storyproducer.controller.keyterm.KeyTermActivity.Companion.stringToKeytermLink
import org.sil.storyproducer.model.Slide
import org.sil.storyproducer.model.Workspace

/**
 * The fragment is for the views that need to set the scripture and reference texts.
 * This is where a user can draft out the story slide by slide
 */
abstract class SlidePhaseFrag : Fragment() {
    protected var rootView: View? = null

    protected var slideNum: Int = 0
    protected var slide: Slide = Workspace.activeSlide!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        slideNum = this.arguments!!.getInt(SLIDE_NUM)

        slide = Workspace.activeStory.slides[slideNum]
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // The last two arguments ensure LayoutParams are inflated
        // properly.
        rootView = inflater.inflate(R.layout.fragment_slide, container, false)

        val arguments = Bundle()
        arguments.putBoolean("enablePlaybackButton", true)
        arguments.putBoolean("enableCheckButton", false)
        arguments.putBoolean("enableMultiRecordButton", true)
        arguments.putBoolean("enableSendAudioButton", false)
        arguments.putInt("slideNum", slideNum)

        val toolbarFrag = childFragmentManager.findFragmentById(R.id.bottom_toolbar) as ToolbarFrag
        toolbarFrag.arguments = arguments
        toolbarFrag.setupToolbarButtons()

        return rootView
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        val item = menu?.getItem(0)
        super.onCreateOptionsMenu(menu, inflater)
        item?.setIcon(R.drawable.ic_mic_white_48dp)
    }

    /**
     * This function serves to handle page changes and stops the audio streams from
     * continuing.
     */

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        //referenceAudioPlayer.stopAudio()
        //referncePlayButton?.setBackgroundResource(R.drawable.ic_play_arrow_white_36dp)
    }

    /**
     * Sets the main text of the layout.
     *
     * @param textView The text view that will be filled with the verse's text.
     */
    protected fun setScriptureText(textView: TextView) {
        val words = slide.content.split(" ")
        textView.text = words.fold(SpannableStringBuilder()){
            result, word -> result.append(stringToKeytermLink(word, activity)).append(" ")
        }
        textView.movementMethod = LinkMovementMethod.getInstance()
    }

    /**
     * This function sets the reference text.
     *
     * @param textView The view that will be populated with the reference text.
     */
    protected fun setReferenceText(textView: TextView) {
        val titleNamePriority = arrayOf(slide.reference, slide.subtitle, slide.title)

        for (title in titleNamePriority) {
            if (title != "") {
                textView.text = title
                return
            }
        }
        //There is no reference text.
        textView.text = ""
    }

    companion object {
        const val SLIDE_NUM = "CURRENT_SLIDE_NUM_OF_FRAG"
    }
}
