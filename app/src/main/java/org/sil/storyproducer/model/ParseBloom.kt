package org.sil.storyproducer.model

import android.content.Context
import android.graphics.Rect
import android.support.v4.provider.DocumentFile
import org.sil.storyproducer.tools.file.getText
import java.util.*
import java.util.regex.Pattern
import org.jsoup.Jsoup
import org.sil.storyproducer.tools.file.getChildDocuments

fun parseBloomHTML(context: Context, storyPath: DocumentFile): Story? {
    //See if there is a BLOOM html file there
    val childDocs = getChildDocuments(context, storyPath.name!!)
    var html_name = ""
    for (f in childDocs) {
        if (f.endsWith(".html") || f.endsWith(".htm")){
            html_name = f
            continue
        }
    }
    if(html_name == "") return null
    val htmlText = getText(context,"${storyPath.name}/$html_name") ?: return null
    //The file "index.html" is there, it is a Bloom project.  Parse it.
    val slides: MutableList<Slide> = ArrayList()

    //Image and transition pattern
    val reSlideType = Pattern.compile("(^[^\"]+)")
    val reNarration = Pattern.compile("id=\"narration[^>]+>([^<]+)")
    val reSoundTrack = Pattern.compile("data-backgroundaudio=\"([^\"]+)")
    val reSoundTrackVolume = Pattern.compile("data-backgroundaudiovolume=\"([^\"]+)")
    val reImage = Pattern.compile("\"(\\w+.(jpg|png))")
    val reHW = Pattern.compile("bloom-backgroundImage[^\\n]+title=\"[\\w.]+ [0-9]+ \\w+ ([0-9]+) x ([0-9]+)")
    val reSR = Pattern.compile("data-initialrect=\"([0-9.]+) ([0-9.]+) ([0-9.]+) ([0-9.]+)")
    val reER = Pattern.compile("data-finalrect=\"([0-9.]+) ([0-9.]+) ([0-9.]+) ([0-9.]+)")

    val pageTextList = htmlText.split("class=\"bloom-page")

    if(pageTextList.size <= 2) return null

    for(i in 1 until pageTextList.size){
        //Don't keep the first element, as it is before the first slide.
        val slide = Slide()
        val t = pageTextList[i]

        //slide type
        val mSlideVariables = reSlideType.matcher(t)
        if(mSlideVariables.find()){
            val sv = mSlideVariables.group(1).split(" ")
            if("frontCover" in sv) slide.slideType = SlideType.FRONTCOVER
            if("numberedPage" in sv) slide.slideType = SlideType.NUMBEREDPAGE
            if("credits1" in sv) slide.slideType = SlideType.CREDITS1
            if("credits2" in sv) slide.slideType = SlideType.CREDITS2ATTRIBUTIONS
            if("theEndPage" in sv) slide.slideType = SlideType.ENDPAGE
        }

        //narration
        val mNarration = reNarration.matcher(t)
        if(mNarration.find()){
            slide.narrationFile = "audio/narration${i-1}.mp3"
            slide.content = mNarration.group(1)
            if(i==1) slide.title = slide.content  //first slide title
        }

        //soundtrack
        val mSoundTrack = reSoundTrack.matcher(t)
        if(mSoundTrack.find()) {slide.musicFile = "audio/${mSoundTrack.group(1)}"}
        val mSoundTrackV = reSoundTrackVolume.matcher(t)
        if(mSoundTrackV.find()) {slide.volume = mSoundTrackV.group(1).toDouble()}

        //image
        val mImage = reImage.matcher(t)
        if(mImage.find()){
            slide.imageFile = mImage.group(1)
        }
        val mHW = reHW.matcher(t)
        if(mHW.find()){
            slide.width = mHW.group(1).toInt()
            slide.height = mHW.group(2).toInt()
            slide.startMotion = Rect(0, 0, slide.width, slide.height)
            slide.endMotion = Rect(0, 0, slide.width, slide.height)
        }
        val mSR = reSR.matcher(t)
        if(mSR.find()) {
            val x = mSR.group(1).toDouble()*slide.width
            val y = mSR.group(2).toDouble()*slide.width
            val w = mSR.group(3).toDouble()*slide.width
            val h = mSR.group(4).toDouble()*slide.width
            slide.startMotion = Rect((x).toInt(), //left
                    (y).toInt(),  //top
                    (x+w).toInt(),   //right
                    (y+h).toInt())  //bottom
        }
        val mER = reER.matcher(t)
        if(mER.find()) {
            val x = mER.group(1).toDouble()*slide.width
            val y = mER.group(2).toDouble()*slide.width
            val w = mER.group(3).toDouble()*slide.width
            val h = mER.group(4).toDouble()*slide.width
            slide.endMotion = Rect((x).toInt(), //left
                    (y).toInt(),  //top
                    (x+w).toInt(),   //right
                    (y+h).toInt())  //bottom
        }
        slides.add(slide)
    }

    return Story(storyPath.name!!,slides)
}