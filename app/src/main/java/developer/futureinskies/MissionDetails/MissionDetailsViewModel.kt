package developer.futureinskies.MissionDetails

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.content.Context
import android.content.Intent
import android.databinding.ObservableField
import android.net.Uri
import developer.futureinskies.SessionManager
import developer.futureinskies.SingleLiveEvent

class MissionDetailsViewModel(context: Application) : AndroidViewModel(context) {

    private val context: Context = context.applicationContext //Application Context to avoid leaks.
    val flightno = ObservableField("")
    val missilename = ObservableField("")
    val launched = ObservableField("")
    val launchedat = ObservableField("")
    val wiki = ObservableField("")
    val rocketname = ObservableField("")
    val youtube = ObservableField("")
    internal val onBackPressed = SingleLiveEvent<Void>()
    fun onBackClicked() {
        onBackPressed.call()
    }

    fun openYoutube() {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(SessionManager.getSession("YOUTUBE", this!!.context!!))
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.setPackage("com.google.android.youtube")
        context.startActivity(intent)
    }

    fun openBrowser() {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(SessionManager.getSession("WIKI", this!!.context!!)))
        browserIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(browserIntent)
    }
}
