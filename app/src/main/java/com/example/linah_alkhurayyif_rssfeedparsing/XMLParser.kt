package com.example.linah_alkhurayyif_rssfeedparsing

import android.util.Xml
import org.xmlpull.v1.XmlPullParser
import java.io.InputStream

class XMLParser {
    private val ns: String? = null

    fun parse(inputStream: InputStream): ArrayList<RecentQuestionsDetails> {
        inputStream.use { inputStream ->
            val parser: XmlPullParser = Xml.newPullParser()
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
            parser.setInput(inputStream, null)
            parser.nextTag()
            return readSongsRssFeed(parser)
        }
    }

    private fun readSongsRssFeed(parser: XmlPullParser): ArrayList<RecentQuestionsDetails> {

        val question = ArrayList<RecentQuestionsDetails>()

        parser.require(XmlPullParser.START_TAG, ns, "feed")

        while (parser.next() != XmlPullParser.END_TAG) {

            if (parser.eventType != XmlPullParser.START_TAG) {
                continue
            }

            if (parser.name == "entry") {
                parser.require(XmlPullParser.START_TAG, ns, "entry")
                var rank: Int? = 0
                var title: String? = null
                var author: String? = null
                var published: String? = null
                var updated: String? = null
                var summary: String? = null
                while (parser.next() != XmlPullParser.END_TAG) {
                    if (parser.eventType != XmlPullParser.START_TAG) {
                        continue
                    }
                    when (parser.name) {
                        "re:rank" -> rank = readRank(parser)
                        "title" -> title = readTag(parser,"title")
                        "author" -> {
                            parser.require(XmlPullParser.START_TAG, ns, "author")
                            var name:String?=null
                            while (parser.next() != XmlPullParser.END_TAG)
                            {
                                if (parser.eventType != XmlPullParser.START_TAG) {
                                    continue
                                }
                                if(parser.name=="name"){author=readTag(parser,"name")}
                                else{skip(parser)}
                            }

                        }
                        "published" -> published = readTag(parser,"published")
                        "updated" -> updated = readTag(parser,"updated")
                        "summary" -> summary = readTag(parser,"summary")

                        else -> skip(parser)
                    }
                }
                question.add(RecentQuestionsDetails(rank,title,author,published,updated,summary))
            } else {
                skip(parser)
            }
        }
        return question
    }

    private fun readTag(parser: XmlPullParser,tag:String): String {
        parser.require(XmlPullParser.START_TAG, ns, tag)
        val myTag = readText(parser)
        parser.require(XmlPullParser.END_TAG, ns, tag)
        return myTag
    }

    private fun readRank(parser: XmlPullParser): Int {
        parser.require(XmlPullParser.START_TAG, ns, "re:rank")
        val summary = readText(parser).toInt()
        parser.require(XmlPullParser.END_TAG, ns, "re:rank")
        return summary
    }

    private fun readText(parser: XmlPullParser): String {
        var result = ""
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.text
            parser.nextTag()
        }
        return result
    }

    private fun skip(parser: XmlPullParser) {
        if (parser.eventType != XmlPullParser.START_TAG) {
            throw IllegalStateException()
        }
        var depth = 1
        while (depth != 0) {
            when (parser.next()) {
                XmlPullParser.END_TAG -> depth--
                XmlPullParser.START_TAG -> depth++
            }
        }
    }
}