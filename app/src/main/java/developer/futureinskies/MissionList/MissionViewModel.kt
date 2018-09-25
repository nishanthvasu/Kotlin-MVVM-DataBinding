package developer.futureinskies.MissionList

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import android.content.Context
import android.widget.Toast
import developer.futureinskies.Constants
import developer.futureinskies.R
import developer.futureinskies.SingleLiveEvent
import fortuner.ai.Login.DataModel.MissionResponseData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MissionViewModel(context: Application) : AndroidViewModel(context) {
    private val context: Context = context.applicationContext //Application Context to avoid leaks.
    internal val logOutEvent = SingleLiveEvent<Void>()
    val changeNotifier = MutableLiveData<ArrayList<MissionResponseData>>()

    fun onLogout() {
        logOutEvent.call()
    }

    fun onPageCalled() {
        if (Constants.isNetworkAvailableActivity(context)) {
            try {
                val call = Constants.client.getMission("XMLHttpRequest", "")

                call.enqueue(object : Callback<ArrayList<MissionResponseData>> {
                    override fun onResponse(call: Call<ArrayList<MissionResponseData>>, response: Response<ArrayList<MissionResponseData>>) {
                        val model = response.body()
                        try {
                            if (model != null && response.isSuccessful()) {
                                changeNotifier.value = model
                            }
                        } catch (e: NullPointerException) {

                        }
                    }

                    override fun onFailure(call: Call<ArrayList<MissionResponseData>>, t: Throwable) {
                        changeNotifier.value = null
                    }
                })
            } catch (e: IllegalArgumentException) {

            }
        } else
            Toast.makeText(context, context.getString(R.string.txt_network_failed), Toast.LENGTH_LONG).show()
    }
}