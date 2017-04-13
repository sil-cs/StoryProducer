package org.sil.storyproducer.tools.file;

import android.graphics.Rect;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents the bare bones of the "project.xml" file present with every story
 * template which was generated by PhotoStory. The observed structure is given below,
 * though additional tags and attributes are allowed.
 * For a full picture of the current known schema, see project_xml.xsd in the project resources.
 *
 * <pre>
 * MSPhotoStoryProject
 *     VisualUnit
 *         [Narration] (path)
 *         Image (path, width, height)
 *             Edit
 *                 RotateAndCrop
 *                     Rectangle (upperLeftX, upperLeftY, width, height)
 *             Motion
 *                 Rect{2} (upperLeftX, upperLeftY, width, height)
 *             [MusicTrack] (volume)
 *                 SoundTrack (path)
 * </pre>
 */
public class ProjectXML {

    private static final String XML_FILE_NAME = "project.xml";

    public List<VisualUnit> units = new ArrayList<>();

    private static File getProjectXMLFile(String story) {
        return new File(FileSystem.getTemplatePath(story), XML_FILE_NAME);
    }

    /**
     * Construct a POJO model of the project.xml file for the specified story.
     * @param story
     * @throws XmlPullParserException
     * @throws IOException
     */
    public ProjectXML(String story) throws XmlPullParserException, IOException {
        InputStream in = new FileInputStream(getProjectXMLFile(story));

        XmlPullParser parser = Xml.newPullParser();
        parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
        parser.setInput(in, null);
        parser.nextTag();

        parser.require(XmlPullParser.START_TAG, null, "MSPhotoStoryProject");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String tag = parser.getName();
            switch(tag) {
                case "VisualUnit":
                    units.add(parseVisualUnit(parser));
                    break;
                default:
                    skip(parser);
            }
        }
    }

    private static void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }

    private static VisualUnit parseVisualUnit(XmlPullParser parser) throws XmlPullParserException, IOException {
        VisualUnit slide = new VisualUnit();

        parser.require(XmlPullParser.START_TAG, null, "VisualUnit");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String tag = parser.getName();
            switch(tag) {
                case "Narration":
                    slide.narrationFilename = parseNarration(parser);
                    break;
                case "Image":
                    slide.imageInfo = parseImage(parser);
                    break;
                case "Transition":
                    //ignore
                default:
                    skip(parser);
            }
        }
        return slide;

    }

    private static VisualUnit.Image parseImage(XmlPullParser parser) throws IOException, XmlPullParserException {
        VisualUnit.Image image = new VisualUnit.Image();

        parser.require(XmlPullParser.START_TAG, null, "Image");

        image.filename = parser.getAttributeValue(null, "path");
        image.width = Integer.parseInt(parser.getAttributeValue(null, "width"));
        image.height = Integer.parseInt(parser.getAttributeValue(null, "height"));

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String tag = parser.getName();
            switch(tag) {
                case "Edit":
                    image.edit = parseEdit(parser);
                    break;
                case "Motion":
                    image.motion = parseMotion(parser);
                    break;
                case "MusicTrack":
                    image.musicTrack = parseMusicTrack(parser);
                    break;
                case "Motion2":
                    //ignore
                case "Transition2":
                    //ignore
                default:
                    skip(parser);
            }
        }
        return image;
    }

    private static VisualUnit.Image.MusicTrack parseMusicTrack(XmlPullParser parser) throws IOException, XmlPullParserException {
        VisualUnit.Image.MusicTrack info = new VisualUnit.Image.MusicTrack();

        parser.require(XmlPullParser.START_TAG, null, "MusicTrack");

        info.volume = Integer.parseInt(parser.getAttributeValue(null, "volume"));

        parser.nextTag();
        parser.require(XmlPullParser.START_TAG, null, "SoundTrack");

        info.filename = parser.getAttributeValue(null, "path");

        parser.nextTag();
        parser.require(XmlPullParser.END_TAG, null, "SoundTrack");

        parser.nextTag();
        parser.require(XmlPullParser.END_TAG, null, "MusicTrack");

        return info;
    }

    private static VisualUnit.Image.Motion parseMotion(XmlPullParser parser) throws IOException, XmlPullParserException {
        VisualUnit.Image.Motion motion = new VisualUnit.Image.Motion();

        parser.require(XmlPullParser.START_TAG, null, "Motion");

        String rectTag = "Rect";
        parser.nextTag();
        motion.start = parseRect(parser, rectTag);
        parser.nextTag();
        motion.end = parseRect(parser, rectTag);

        parser.nextTag();
        parser.require(XmlPullParser.END_TAG, null, "Motion");

        return motion;
    }

    private static VisualUnit.Image.Edit parseEdit(XmlPullParser parser) throws IOException, XmlPullParserException {
        VisualUnit.Image.Edit info = new VisualUnit.Image.Edit();

        parser.require(XmlPullParser.START_TAG, null, "Edit");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String tag = parser.getName();
            switch(tag) {
                case "RotateAndCrop":
                    parser.nextTag();
                    info.crop = parseRect(parser, "Rectangle");
                    parser.nextTag();
                    parser.require(XmlPullParser.END_TAG, null, "RotateAndCrop");
                    break;
                case "TextOverlay":
                    //ignore
                default:
                    skip(parser);
            }
        }
        return info;
    }

    private static String parseNarration(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, "Narration");
        String filename = parser.getAttributeValue(null, "path");
        parser.nextTag();
        parser.require(XmlPullParser.END_TAG, null, "Narration");
        return filename;
    }

    private static Rect parseRect(XmlPullParser parser, String rectangleTag) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, rectangleTag);

        int left = Integer.parseInt(parser.getAttributeValue(null, "upperLeftX"));
        int top = Integer.parseInt(parser.getAttributeValue(null, "upperLeftY"));
        int width = Integer.parseInt(parser.getAttributeValue(null, "width"));
        int height = Integer.parseInt(parser.getAttributeValue(null, "height"));

        int right = left + width;
        int bottom = top + height;

        parser.nextTag();
        parser.require(XmlPullParser.END_TAG, null, rectangleTag);

        return new Rect(left, top, right, bottom);
    }

    public static class VisualUnit {
        public String narrationFilename;

        public Image imageInfo;

        public static class Image {
            public int width;
            public int height;

            public String filename;

            public Edit edit;
            public Motion motion;

            public MusicTrack musicTrack;

            public static class Edit {
                public Rect crop;
            }

            public static class Motion {
                public Rect start;
                public Rect end;
            }

            public static class MusicTrack {
                public int volume;
                public String filename;
            }
        }
    }

}
