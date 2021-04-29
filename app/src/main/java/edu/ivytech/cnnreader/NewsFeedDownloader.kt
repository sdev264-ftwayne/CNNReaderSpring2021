package edu.ivytech.cnnreader

import android.content.Context
import android.util.Log
import org.xml.sax.InputSource
import org.xml.sax.XMLReader
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.net.URL
import javax.xml.parsers.SAXParser
import javax.xml.parsers.SAXParserFactory
import kotlin.math.abs

class NewsFeedDownloader(var context: Context) {
    val CNN_URL = "http://rss.cnn.com/rss/cnn_showbiz.rss"
    val TAG = "Feed Downloader"
    val FILENAME = "news_feed.xml"

    fun downloadFeed() {
        try {
            val url = URL(CNN_URL)
            val inStream: InputStream = url.openStream()
            val out : FileOutputStream = context.openFileOutput(FILENAME,Context.MODE_PRIVATE)
            val buffer = ByteArray(1024)
            var bytesRead: Int = inStream.read(buffer)
            while(bytesRead != -1) {
                out.write(buffer, 0, bytesRead)
                bytesRead = inStream.read(buffer)
            }
            out.close()
            inStream.close()
        } catch (e: IOException) {
            Log.e(TAG, e.toString())
        }
        val feedInfoOld = ArticleRepository.get().getFeedInfo()
        val rssFeedHandler = RSSFeedHandler(true)
        try {
            val factory: SAXParserFactory = SAXParserFactory.newInstance()
            val parser: SAXParser = factory.newSAXParser()
            val xmlReader: XMLReader = parser.xmlReader
            xmlReader.contentHandler = rssFeedHandler
            val fin: FileInputStream = context.openFileInput(FILENAME)
            val source = InputSource(fin)
            xmlReader.parse(source)

        } catch (e : StopSaxException) {
            Log.e("News Reader Download", "Got Pub date")
        } catch (e : Exception) {
            Log.e("News Reader Download", e.toString())
        }

        if(rssFeedHandler.feedInfo.getPubDateLong() != (feedInfoOld?.getPubDateLong() ?:0 ))
        {
            ArticleRepository.get().deleteArticles()
            try {
                val rssFeedHandler2 = RSSFeedHandler(false)
                val factory: SAXParserFactory = SAXParserFactory.newInstance()
                val parser: SAXParser = factory.newSAXParser()
                val xmlReader: XMLReader = parser.xmlReader
                xmlReader.contentHandler = rssFeedHandler2
                val fin: FileInputStream = context.openFileInput(FILENAME)
                val source = InputSource(fin)
                xmlReader.parse(source)

            } catch (e : Exception) {
                Log.e("News Reader Download", e.toString())

            }
        }

    }
}