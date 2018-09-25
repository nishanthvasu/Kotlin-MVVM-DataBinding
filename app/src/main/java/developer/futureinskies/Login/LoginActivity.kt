package developer.futureinskies.Login

import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import developer.futureinskies.MissionList.MissionActivity
import developer.futureinskies.R
import developer.futureinskies.obtainViewModel
import developer.futureinskies.replaceFragmentInActivity

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.addtask_act)
        replaceFragmentInActivity(obtainViewFragment(), R.id.lay_fr_container)
        subscribeToNavigationChanges()
    }

    private fun subscribeToNavigationChanges() {
        // The activity observes the navigation events in the ViewModel
        obtainViewModel().taskUpdatedEvent.observe(this, Observer {
            startActivity(Intent(applicationContext, MissionActivity::class.java))
        })
    }

    private fun obtainViewFragment() = supportFragmentManager.findFragmentById(R.id.lay_fr_container)
            ?: LoginFragment.newInstance().apply { }

    fun obtainViewModel(): LoginViewModel = obtainViewModel(LoginViewModel::class.java)

}