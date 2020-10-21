package org.sil.storyproducer.model

// import android.support.v4.app.FragmentActivity
// import android.support.v4.content.ContextCompat
// import org.sil.storyproducer.controller.keyterm.KeyTermActivity

// a list of all the word links (used for saving all word links in a single file)
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