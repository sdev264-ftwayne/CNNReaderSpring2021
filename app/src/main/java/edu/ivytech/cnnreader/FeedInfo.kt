package edu.ivytech.cnnreader

import android.util.Log
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

@Entity
data class FeedInfo (@PrimaryKey val id: Int = 1,
                     var pubDate: String = "date",
                     var pubTitle:String = "Title") {
    companion object {
        val dateOutFormat: SimpleDateFormat = SimpleDateFormat("EEEE h:mm a (MMMM d)", Locale.US)
        val dateInFormat: SimpleDateFormat = SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.US)
    }

    fun getPubDateFormatted(): String? {
        try {
            val date: Date? = dateInFormat.parse(pubDate!!.trim())
            return dateOutFormat.format(date!!)
        } catch(e: ParseException) {
            Log.e("feed", e.toString())
            return "Date Error"
        }
    }

    fun getPubDateLong() : Long {
        try {
            val date: Date? = dateInFormat.parse(pubDate!!.trim())
            if(date != null) {
                return date.time
            }
        } catch (e:ParseException) {
            Log.e("feed", e.toString())
            return 0
        }
        return 0
    }
}