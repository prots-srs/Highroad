package ua.com.biz_s.highroad

import android.app.Application
import ua.com.biz_s.highroad.articles.database.HighroadDatabase

class HighroadApplication : Application() {
    val database: HighroadDatabase by lazy { HighroadDatabase.getDatabase(this) }
}