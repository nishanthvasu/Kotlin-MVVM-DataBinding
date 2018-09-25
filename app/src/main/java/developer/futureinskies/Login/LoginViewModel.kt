package developer.futureinskies.Login

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.databinding.ObservableField
import developer.futureinskies.SingleLiveEvent

class LoginViewModel(context: Application) : AndroidViewModel(context) {
    val mobilenum = ObservableField("")
    val password = ObservableField("")
    internal val taskUpdatedEvent = SingleLiveEvent<Void>()
}