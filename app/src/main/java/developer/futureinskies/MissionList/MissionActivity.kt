package developer.futureinskies.MissionList

import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import developer.futureinskies.Login.LoginActivity
import developer.futureinskies.R
import developer.futureinskies.SessionManager
import developer.futureinskies.obtainViewModel
import developer.futureinskies.replaceFragmentInActivity

class MissionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.addtask_act)
        replaceFragmentInActivity(obtainViewFragment(), R.id.lay_fr_container)
        subscribeToNavigationChanges()
    }

    private fun subscribeToNavigationChanges() {
        // The activity observes the navigation events in the ViewModel
        obtainViewModel().logOutEvent.observe(this, Observer {
            finishAffinity()
            SessionManager.saveSession("DocumentId", "", this)
            startActivity(Intent(this, LoginActivity::class.java))
        })
    }

    private fun obtainViewFragment() = supportFragmentManager.findFragmentById(R.id.lay_fr_container)
            ?: MissionFragment.newInstance()

    fun obtainViewModel(): MissionViewModel = obtainViewModel(MissionViewModel::class.java)
}