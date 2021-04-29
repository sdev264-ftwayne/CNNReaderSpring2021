package edu.ivytech.cnnreader

import org.xml.sax.Attributes
import org.xml.sax.SAXException
import org.xml.sax.helpers.DefaultHandler

class StopSaxException : SAXException() {}

class RSSFeedHandler(var readFeedOnly:Boolean): DefaultHandler() {
    private var item: Article? = null
    var feedInfo:FeedInfo = FeedInfo()
    private val articleRepository = ArticleRepository.get()
    private var feedTitleHasBeenRead = false
    private var feedPubDateHasBeenRead = false

    private var isTitle = false
    private var isDescription = false
    private var isLink = false
    private var isPubDate = false

    override fun startElement(
        uri: String?,
        localName: String?,
        qName: String?,
        attributes: Attributes?
    ) {
        if(qName == "item") {
            item = Article()
            return
        } else if (qName == "title") {
            isTitle = true
            return
        } else if (qName == "description") {
            isDescription = true
            return
        } else if (qName ==  "link") {
            isLink = true
            return
        } else if (qName == "pubDate") {
            isPubDate = true
            return
        }
    }

    override fun endElement(uri: String?, localName: String?, qName: String?) {
        if(qName == "item") {
            articleRepository.addArticle(item!!)
            return
        }
    }

    override fun characters(ch: CharArray?, start: Int, length: Int) {
        val s = String(ch!!, start, length)
        if(isTitle) {
            if(!feedTitleHasBeenRead) {
                feedInfo.pubTitle = s
                feedTitleHasBeenRead = true
            } else {
                item?.title = s
            }
            isTitle = false
        } else if(isPubDate){
            if(!feedPubDateHasBeenRead) {
                feedInfo.pubDate = s
                feedPubDateHasBeenRead = true
                if(readFeedOnly)
                {
                    throw StopSaxException()
                }
                articleRepository.updateFeed(feedInfo)
            } else {
                item?.pubDate = s
            }
            isPubDate = false
        } else if(isLink) {
            item?.link = s
            isLink = false
        } else if(isDescription) {
            if(s.startsWith("<")) {
                item?.description = "No description available"
            } else {
                item?.description = s
            }
            isDescription = false
        }

    }
}