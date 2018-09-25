package developer.futureinskies.Login

import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.couchbase.lite.*
import developer.futureinskies.Application
import developer.futureinskies.R
import developer.futureinskies.SessionManager
import developer.futureinskies.databinding.FragmentLoginBinding


class LoginFragment : Fragment() {

    private lateinit var mViewBinding: FragmentLoginBinding
    internal lateinit var application: Application
    private var database: Database? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_login, container, false)

        mViewBinding = FragmentLoginBinding.bind(root).apply {
            viewmodel = (activity as LoginActivity).obtainViewModel()
        }
        setHasOptionsMenu(true)
        retainInstance = false
        return mViewBinding.root
    }

    companion object {
        fun newInstance() = LoginFragment()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        application = activity!!.getApplication() as Application
        application.getCurrentDatabase(getString(R.string.app_name), this!!.context!!)
        database = application.database

        mViewBinding.ftvLogin.setOnClickListener {

            if (mViewBinding.etMobilenumber.text.toString().length == 0)
                Toast.makeText(context, resources.getString(R.string.entermobile), Toast.LENGTH_LONG).show()
            else if (mViewBinding.etMobilenumber.text.toString().length < 10)
                Toast.makeText(context, resources.getString(R.string.entervalidmobilenum), Toast.LENGTH_LONG).show()
            else if (mViewBinding.etPassword.text.toString().length == 0)
                Toast.makeText(context, resources.getString(R.string.enterpwd), Toast.LENGTH_LONG).show()
            else {

                var mutableDoc = MutableDocument()
                        .setFloat("version", 2.0f)
                        .setString("mobile", mViewBinding.etMobilenumber.text.toString())
                        .setString("password", mViewBinding.etPassword.text.toString())
                val db = database
                if (db != null) {
                    db.save(mutableDoc)
                    val document = db.getDocument(mutableDoc.id)
                    SessionManager.saveSession("DocumentId", document.id, this!!.context!!)
                    Toast.makeText(activity, resources.getString(R.string.txt_loginsuccess), Toast.LENGTH_LONG).show()
                    mViewBinding.viewmodel?.taskUpdatedEvent!!.call()
                }
            }
        }
    }
}

