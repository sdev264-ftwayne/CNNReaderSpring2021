package edu.ivytech.cnnreader

import android.util.Log
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

@Entity
data class Article(@PrimaryKey val id:UUID = UUID.randomUUID(),
                   var title:String = "title",
                   var description: String = "desc",
                   var link: String = "link",
                   var pubDate: String? = "date"
) {
    companion object {
        val dateOutFormat: SimpleDateFormat = SimpleDateFormat("EEEE h:mm a (MMMM d)", Locale.US)
        val dateInFormat: SimpleDateFormat = SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.US)
    }

    fun getPubDateFormatted(): String? {
        try {
            if(pubDate != null) {
                val date: Date? = dateInFormat.parse(pubDate!!.trim())
                return dateOutFormat.format(date!!)
            }
        } catch(e: ParseException) {
            Log.e("article", e.toString())
            return "Date Error"
        }
        return "Unknown Date"
    }
}