package developer.futureinskies.MissionDetails

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import developer.futureinskies.R
import developer.futureinskies.obtainViewModel
import developer.futureinskies.replaceFragmentInActivity

class MissionDetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.addtask_act)
        replaceFragmentInActivity(obtainViewFragment(), R.id.lay_fr_container)
        subscribeToNavigationChanges()
    }

    private fun subscribeToNavigationChanges() {
        // The activity observes the navigation events in the ViewModel
        obtainViewModel().onBackPressed.observe(this, Observer {
            onBackPressed()
        })
    }

    private fun obtainViewFragment() = supportFragmentManager.findFragmentById(R.id.lay_fr_container)
            ?: MissionDetailsFragment.newInstance()

    fun obtainViewModel(): MissionDetailsViewModel = obtainViewModel(MissionDetailsViewModel::class.java)

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}