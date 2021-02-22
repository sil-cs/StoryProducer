package org.sil.storyproducer.model

import org.sil.storyproducer.R
import org.sil.storyproducer.controller.MainActivity
import org.sil.storyproducer.controller.RegistrationActivity
import org.sil.storyproducer.controller.export.FinalizeActivity
import org.sil.storyproducer.controller.export.ShareActivity
import org.sil.storyproducer.controller.learn.LearnActivity
import org.sil.storyproducer.controller.pager.PagerBaseActivity
import org.sil.storyproducer.controller.remote.WholeStoryBackTranslationActivity
import org.sil.storyproducer.controller.wordlink.WordLinkActivity

enum class PhaseType {
    WORKSPACE,
    REGISTRATION,
    STORY_LIST,
    LEARN,
    TRANSLATE_REVISE,
    WORD_LINKS,
    COMMUNITY_WORK,
    WHOLE_STORY,
    BACK_T,
    REMOTE_CHECK,
    ACCURACY_CHECK,
    VOICE_STUDIO,
    FINALIZE,
    SHARE
}

/**
 * The business object for phases that are part of the story
 */
class Phase (val phaseType: PhaseType) {

    /**
     * Get the icon associated with a phase (used when creating the options menu)
     * @return drawable
     */
    fun getIcon(phase: PhaseType = phaseType) : Int {
        return when (phase){
            PhaseType.LEARN -> R.drawable.ic_ear_speak
            PhaseType.TRANSLATE_REVISE -> R.drawable.ic_mic_white_48dp
            PhaseType.FINALIZE -> R.drawable.ic_video_call_white_48dp
            PhaseType.SHARE -> R.drawable.ic_share_icon_v2_white
            PhaseType.COMMUNITY_WORK -> R.drawable.ic_people_white_48dp
            PhaseType.ACCURACY_CHECK -> R.drawable.ic_school_white_48dp
            PhaseType.WHOLE_STORY -> R.drawable.ic_school_white_48dp
            PhaseType.REMOTE_CHECK -> R.drawable.ic_school_white_48dp
            PhaseType.BACK_T -> R.drawable.ic_headset_mic_white_48dp
            PhaseType.VOICE_STUDIO -> R.drawable.ic_mic_box_48dp
            else -> R.drawable.ic_mic_white_48dp
        }
    }

    fun getReferenceAudioFile(slideNum: Int = Workspace.activeSlideNum) : String {
        val filename = when (phaseType){
            PhaseType.TRANSLATE_REVISE -> Workspace.activeStory.slides[slideNum].narrationFile
            PhaseType.COMMUNITY_WORK -> Workspace.activeStory.slides[slideNum].chosenDraftFile
            PhaseType.ACCURACY_CHECK -> Workspace.activeStory.slides[slideNum].chosenDraftFile
            PhaseType.VOICE_STUDIO -> Workspace.activeStory.slides[slideNum].chosenDraftFile
            PhaseType.BACK_T -> Workspace.activeStory.slides[slideNum].chosenDraftFile
            else -> ""
        }
        return Story.getFilename(filename)
    }

    /**
     * TODO: refactor
     * the following 3 functions are used when displaying the phase names and creating paths to store audio files
     * (see Issue #547 for conversation on fix)
     */
    fun getPrettyName() : String {
        return when (phaseType) {
            PhaseType.LEARN -> "Learn"
            PhaseType.TRANSLATE_REVISE -> "Translate + Revise"
            PhaseType.FINALIZE -> "Finalize"
            PhaseType.SHARE -> "Share"
            PhaseType.COMMUNITY_WORK -> "Community Work"
            PhaseType.ACCURACY_CHECK -> "Accuracy Check"
            PhaseType.WHOLE_STORY -> "Whole Story"
            PhaseType.REMOTE_CHECK -> "Remote Check"
            PhaseType.BACK_T -> "Back Translation"
            PhaseType.VOICE_STUDIO -> "Voice Studio"
            else -> phaseType.toString().toLowerCase()
        }
    }
    fun getDisplayName() : String {
        return when (phaseType) {
            PhaseType.TRANSLATE_REVISE -> "Translation Draft"
            PhaseType.COMMUNITY_WORK -> "Comment"
            PhaseType.ACCURACY_CHECK -> "Accuracy"
            PhaseType.WHOLE_STORY -> "Whole"
            PhaseType.REMOTE_CHECK -> "Remote"
            PhaseType.BACK_T -> "BackTrans"
            PhaseType.VOICE_STUDIO -> "Studio Recording"
            PhaseType.FINALIZE -> "Finalize"
            else -> phaseType.toString().toLowerCase()
        }
    }
    fun getShortName() : String {
        return when (phaseType) {
            PhaseType.TRANSLATE_REVISE -> "Translate"
            PhaseType.COMMUNITY_WORK -> "Community"
            PhaseType.ACCURACY_CHECK -> "Accuracy"
            PhaseType.WHOLE_STORY -> "Whole"
            PhaseType.REMOTE_CHECK -> "Remote"
            PhaseType.BACK_T -> "BackTrans"
            PhaseType.VOICE_STUDIO -> "VStudio"
            PhaseType.FINALIZE -> "Finalize"
            else -> phaseType.toString().toLowerCase()
        }
    }

    /**
     * get the activity class of a phase (some based on the PagerBaseActivity)
     * @return class
     */
    fun getTheClass() : Class<*> {
        return when(phaseType){
            PhaseType.WORKSPACE -> RegistrationActivity::class.java
            PhaseType.REGISTRATION -> RegistrationActivity::class.java
            PhaseType.STORY_LIST -> MainActivity::class.java
            PhaseType.LEARN -> LearnActivity::class.java
            PhaseType.TRANSLATE_REVISE -> PagerBaseActivity::class.java
            PhaseType.WORD_LINKS -> WordLinkActivity::class.java
            PhaseType.COMMUNITY_WORK -> PagerBaseActivity::class.java
            PhaseType.ACCURACY_CHECK -> PagerBaseActivity::class.java
            PhaseType.VOICE_STUDIO -> PagerBaseActivity::class.java
            PhaseType.FINALIZE -> FinalizeActivity::class.java
            PhaseType.SHARE -> ShareActivity::class.java
            PhaseType.BACK_T -> PagerBaseActivity::class.java
            PhaseType.WHOLE_STORY -> WholeStoryBackTranslationActivity::class.java
            PhaseType.REMOTE_CHECK -> PagerBaseActivity::class.java
        }
    }

    /**
     * Get list of audio files associated with phase using slideNum
     * @return MutableList<String>
     */
    fun getCombNames(slideNum:Int = Workspace.activeSlideNum) : MutableList<String>?{
        return when (phaseType){
            PhaseType.TRANSLATE_REVISE -> Workspace.activeStory.slides[slideNum].translateReviseAudioFiles
            PhaseType.COMMUNITY_WORK -> Workspace.activeStory.slides[slideNum].communityWorkAudioFiles
            PhaseType.VOICE_STUDIO -> Workspace.activeStory.slides[slideNum].voiceStudioAudioFiles
            PhaseType.BACK_T -> Workspace.activeStory.slides[slideNum].backTranslationAudioFiles
            else -> null
        }
    }

    fun getPhaseDisplaySlideCount() : Int {
        var tempSlideNum = 0
        val validSlideTypes = when(phaseType){
            PhaseType.VOICE_STUDIO -> arrayOf(
                    SlideType.FRONTCOVER,SlideType.NUMBEREDPAGE,
                    SlideType.LOCALSONG)
            else -> arrayOf(
                    SlideType.FRONTCOVER,SlideType.NUMBEREDPAGE,
                    SlideType.LOCALSONG)
        }
        for (s in Workspace.activeStory.slides)
            if(s.slideType in validSlideTypes){
                tempSlideNum++
            }else{
                break
            }
        return tempSlideNum
    }

    fun checkValidDisplaySlideNum(slideNum: Int) : Boolean {
        val slideType = Workspace.activeStory.slides[slideNum].slideType
        return when(phaseType){
            PhaseType.VOICE_STUDIO -> slideType in arrayOf(
                    SlideType.FRONTCOVER,SlideType.NUMBEREDPAGE,
                    SlideType.LOCALSONG)
            else -> slideType in arrayOf(
                    SlideType.FRONTCOVER,SlideType.NUMBEREDPAGE,
                    SlideType.LOCALSONG)
        }
    }

    /**
     * companion object to the Phase class (allows for static function calls)
     */
    companion object {
        fun getLocalPhases() : List<Phase> {
            return listOf(
                    Phase(PhaseType.LEARN),
                    Phase(PhaseType.TRANSLATE_REVISE),
                    Phase(PhaseType.COMMUNITY_WORK),
                    Phase(PhaseType.ACCURACY_CHECK),
                    Phase(PhaseType.VOICE_STUDIO),
                    Phase(PhaseType.FINALIZE),
                    Phase(PhaseType.SHARE))
        }

        fun getRemotePhases() : List<Phase> {
            return listOf(
                    Phase(PhaseType.LEARN),
                    Phase(PhaseType.TRANSLATE_REVISE),
                    Phase(PhaseType.COMMUNITY_WORK),
                    Phase(PhaseType.WHOLE_STORY),
                    Phase(PhaseType.BACK_T),
                    Phase(PhaseType.REMOTE_CHECK),
                    Phase(PhaseType.VOICE_STUDIO),
                    Phase(PhaseType.FINALIZE),
                    Phase(PhaseType.SHARE))
        }

        /**
         * get the filename for the HTML help doc
         * @return String
         */
        fun getHelpName(phase: PhaseType) : String {
            return "${phase.name.toLowerCase()}.html"
        }
    }
}