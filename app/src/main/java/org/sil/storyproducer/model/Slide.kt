package org.sil.storyproducer.model

import android.graphics.Rect
import android.net.Uri
import com.google.gson.annotations.SerializedName
import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonClass
import com.squareup.moshi.ToJson
import java.util.*

/**
 * This class contains metadata pertinent to a given slide from a story template.
 */
@JsonClass(generateAdapter = true)
class Slide{
    // template information
    var slideType: SlideType = SlideType.NUMBEREDPAGE
    var narrationFile = ""
    var title = ""
    var subtitle = ""
    var reference = ""
    var content = ""
    var imageFile = ""
    var textFile = ""

    //TODO initialize to no crop and no motion from picture size
    var width: Int = 0
    var height: Int = 0
    var crop: Rect? = null
    var startMotion: Rect? = null
    var endMotion: Rect? = null

    var musicFile = MUSIC_CONTINUE
    var volume = 0.0f

    //translated text
    var translatedContent: String = ""

    // recorded audio files
    //
    // use @SerializedName for legacy project.json files, vars can change as phase names change but
    //  json keys will always be the original name for the phase
    @SerializedName("draftAudioFiles")
    var translateReviseAudioFiles: MutableList<String> = ArrayList()
    @SerializedName("chosenDraftFile")
    var chosenTranslateReviseFile = ""
    @SerializedName("communityCheckAudioFiles")
    var communityWorkAudioFiles: MutableList<String> = ArrayList()
    @SerializedName("consultantCheckAudioFiles")
    var accuracyCheckAudioFiles: MutableList<String> = ArrayList()
    @SerializedName("dramatizationAudioFiles")
    var voiceStudioAudioFiles: MutableList<String> = ArrayList()
    @SerializedName("chosenDramatizationFile")
    var chosenVoiceStudioFile = ""
    @SerializedName("backTranslationAudioFiles")
    var backTranslationAudioFiles: MutableList<String> = ArrayList()
    @SerializedName("chosenBackTranslationFile")
    var chosenBackTranslationFile = ""

    //consultant approval
    var isChecked: Boolean = false

    fun isFrontCover() = slideType == SlideType.FRONTCOVER
    fun isNumberedPage() = slideType == SlideType.NUMBEREDPAGE

    companion object
}

enum class SlideType {
    // LOCALCREDITS is obsolete but provided for reading projects from v3.0.2 or earlier
    NONE, FRONTCOVER, NUMBEREDPAGE, LOCALSONG, LOCALCREDITS, COPYRIGHT, ENDPAGE
}

const val MUSIC_NONE = "noSoundtrack"
const val MUSIC_CONTINUE = "continueSoundtrack"

@JsonClass(generateAdapter = true)
class RectJson(var left: Int = 0,
               var top: Int = 0,
               var right: Int = 0,
               var bottom: Int = 0)

class RectAdapter {
    @FromJson fun rectFromJson(rectJson: RectJson): Rect {
        return Rect(rectJson.left,rectJson.top,rectJson.right,rectJson.bottom)
    }

    @ToJson fun rectToJson(rect: Rect): RectJson {
        return RectJson(rect.left,rect.top,rect.right,rect.bottom)
    }
}

class UriAdapter {
    @FromJson fun fromJson(uriString: String): Uri {
        return Uri.parse(uriString)
    }

    @ToJson fun toJson(uri: Uri): String {
        return uri.toString()
    }
}
