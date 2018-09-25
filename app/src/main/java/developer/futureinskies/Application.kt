package developer.futureinskies

import android.content.Context
import android.support.multidex.MultiDexApplication

import com.couchbase.lite.CouchbaseLiteException
import com.couchbase.lite.Database
import com.couchbase.lite.DatabaseConfiguration

class Application : MultiDexApplication() {

    var database: Database? = null

    private fun getDatabase(name: String, cxt: Context): Database? {
        try {
            val config = DatabaseConfiguration(cxt)
            return Database(name, config)
        } catch (e: CouchbaseLiteException) {

        }

        return null
    }

    fun getCurrentDatabase(dataBaseName: String, cxt: Context) {
        database = getDatabase(dataBaseName, cxt)
    }
}
