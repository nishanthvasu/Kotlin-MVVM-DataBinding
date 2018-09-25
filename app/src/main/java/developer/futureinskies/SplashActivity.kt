package developer.futureinskies

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import developer.futureinskies.Login.LoginActivity
import developer.futureinskies.MissionList.MissionActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        val handler = Handler()
        handler.postDelayed({
            if (SessionManager.getSession("DocumentId", applicationContext).equals(""))
                startActivity(Intent(this, LoginActivity::class.java))
            else
                startActivity(Intent(this, MissionActivity::class.java))

        }, 2000)

    }
}
