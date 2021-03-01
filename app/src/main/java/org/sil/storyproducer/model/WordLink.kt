package org.sil.storyproducer.model

import android.content.Intent
import android.text.Spannable
import android.text.SpannableString
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import org.sil.storyproducer.R
import org.sil.storyproducer.controller.wordlink.WordLinkActivity

/**
 * A list of all the wordlinks (used for saving all wordlinks in a single file)
 **/
class WordLinkList (val wordLinks: List<WordLink>) {
    companion object
}

data class WordLinkRecording (
    var audioRecordingFilename : String = "",
    var textBackTranslation : String = "",
    var isTextBackTranslationSubmitted: Boolean = false) {
        companion object
}

data class WordLink (
        var term: String = "",
        var termForms: List<String> = listOf(),
        var alternateRenderings: List<String> = listOf(),
        var explanation: String = "",
        var relatedTerms: List<String> = listOf(),
        var wordLinkRecordings: MutableList<WordLinkRecording> = mutableListOf(),
        var chosenWordLinkFile: String = "") {
    companion object
}

/**
 * Takes a string and returns a spannable string with links to open wordlink Activity
 *
 * @param string The string that contains wordlinks
 * @param fragmentActivity The current activity
 * @return A spannable string
 **/
fun stringToWordLink (string: String, fragmentActivity: FragmentActivity?) : SpannableString {
    val spannableString = SpannableString(string)
    if (Workspace.termFormToTermMap.containsKey(string.toLowerCase())) {
        val clickableSpan = createWordLinkClickableSpan(string, fragmentActivity)
        spannableString.setSpan(clickableSpan, 0, string.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    }
    return spannableString
}

/**
 * Converts a string to a clickableSpan that will open the WordLinkActivity when clicked
 *
 * @param term the wordlink
 * @param fragmentActivity the current activity
 * @return The link to open the wordlink
 **/
private fun createWordLinkClickableSpan(term: String, fragmentActivity: FragmentActivity?): ClickableSpan {
    return object : ClickableSpan() {
        override fun onClick(textView: View) {
            if (Workspace.activePhase.phaseType == PhaseType.WORD_LINKS && fragmentActivity is WordLinkActivity) {
                fragmentActivity.replaceActivityWordLink(term)
            }
            else if (Workspace.activePhase.phaseType != PhaseType.WORD_LINKS) {
                //Start a new word links activity and keep a reference to the parent phase
                val intent = Intent(fragmentActivity, WordLinkActivity::class.java)
                intent.putExtra(PHASE, Workspace.activePhase.phaseType)
                intent.putExtra(WORDLINKS_CLICKED_TERM, term)
                fragmentActivity?.startActivity(intent)
            }
        }

        override fun updateDrawState(drawState: TextPaint) {
            val wordLink = Workspace.termToWordLinkMap[Workspace.termFormToTermMap[term.toLowerCase()]]
            val hasRecording = wordLink?.wordLinkRecordings?.isNotEmpty()

            if(hasRecording != null && hasRecording){
                drawState.linkColor = ContextCompat.getColor(fragmentActivity!!.applicationContext, R.color.lightGray)
            }
            super.updateDrawState(drawState)
        }
    }
}
