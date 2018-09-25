package developer.futureinskies.MissionList

import android.app.Dialog
import android.arch.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import com.couchbase.lite.Database
import com.couchbase.lite.MutableDocument
import developer.futureinskies.Application
import developer.futureinskies.MissionDetails.MissionDetailsActivity
import developer.futureinskies.R
import developer.futureinskies.SessionManager
import developer.futureinskies.databinding.AdapterMissionlistBinding
import developer.futureinskies.databinding.DialogProfileBinding
import developer.futureinskies.databinding.FragmentMissionlistBinding
import fortuner.ai.Login.DataModel.MissionResponseData
import java.util.*

class MissionFragment : Fragment() {

    private lateinit var mViewBinding: FragmentMissionlistBinding
    internal lateinit var application: Application
    private var database: Database? = null
    private var mutableDoc: MutableDocument? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_missionlist, container, false)

        mViewBinding = FragmentMissionlistBinding.bind(root).apply {
            viewmodel = (activity as MissionActivity).obtainViewModel()
        }
        setHasOptionsMenu(true)
        retainInstance = false
        return mViewBinding.root
    }

    companion object {
        fun newInstance() = MissionFragment()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        application = activity!!.getApplication() as Application
        application.getCurrentDatabase(getString(R.string.app_name), this!!.context!!)
        database = application.database
        mViewBinding.viewmodel?.onPageCalled()

        mViewBinding.viewmodel?.changeNotifier!!.observe(this, Observer {
            it?.run {
                if (it != null) {
                    val missionAdapter = MissionsAdapter(it, activity)
                    mViewBinding.rvMissionlist.setAdapter(missionAdapter)
                    mViewBinding.rvMissionlist.setLayoutManager(LinearLayoutManager(activity))
                }
            }
        })
        mViewBinding.ivProfile.setOnClickListener { selectBranches() }
    }

    fun selectBranches() {
        val dialog = Dialog(activity)
        val binding = DialogProfileBinding.inflate(LayoutInflater.from(activity))
        dialog.setContentView(binding.getRoot())
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        dialog.show()
        dialog.setCanceledOnTouchOutside(false)

        binding.ivEditprofile.setOnClickListener { binding.ftvUpdate.visibility == View.VISIBLE }
        binding.ivCloseprofile.setOnClickListener {
            binding.ftvUpdate.visibility == View.GONE
            dialog.dismiss()
        }
        mutableDoc = database!!.getDocument(SessionManager.getSession("DocumentId", this!!.context!!)).toMutable()
        binding.etMobilenumber.setText(mutableDoc!!.getString("mobile"))
        binding.etPassword.setText(mutableDoc!!.getString("password"))

        binding.ftvUpdate.setOnClickListener {
            if (binding.etMobilenumber.text.toString().length == 0)
                Toast.makeText(context, resources.getString(R.string.entermobile), Toast.LENGTH_LONG).show()
            else if (binding.etMobilenumber.text.toString().length < 10)
                Toast.makeText(context, resources.getString(R.string.entervalidmobilenum), Toast.LENGTH_LONG).show()
            else if (binding.etPassword.text.toString().length == 0)
                Toast.makeText(context, resources.getString(R.string.enterpwd), Toast.LENGTH_LONG).show()
            else {
                mutableDoc!!.setString("mobile", binding.etMobilenumber.text.toString())
                mutableDoc!!.setString("password", binding.etPassword.text.toString())
                val db = database
                if (db != null)
                    db.save(mutableDoc)
                Toast.makeText(activity, resources.getString(R.string.txt_profileupdated), Toast.LENGTH_LONG).show()
                dialog.dismiss()
            }
        }
    }

    inner class MissionsAdapter(branchList: ArrayList<MissionResponseData>, context: FragmentActivity?) : RecyclerView.Adapter<MissionsAdapter.MissionAdapterViewHolder>() {

        private var missionList = java.util.ArrayList<MissionResponseData>()
        private var context: Context

        init {
            this.missionList = branchList
            this.context = context!!
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MissionAdapterViewHolder {
            return MissionAdapterViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.adapter_missionlist, parent, false))
        }

        override fun onBindViewHolder(holder: MissionAdapterViewHolder, position: Int) {
            holder.bindUser(missionList[position])
        }

        override fun getItemCount(): Int {
            return missionList.size
        }

        inner class MissionAdapterViewHolder(internal var mMissionBinding: AdapterMissionlistBinding) : RecyclerView.ViewHolder(mMissionBinding.getRoot()) {

            internal fun bindUser(branches: MissionResponseData) {
                mMissionBinding.tvBranchname.setText(branches.mission_name)
                mMissionBinding.tvYear.setText(branches.launch_year)
                mMissionBinding.tvLocation.setText(branches.launch_site!!.site_name)
                if (!branches.launch_success!!) {
                    mMissionBinding.tvStatus.setTextColor(ContextCompat.getColor(context, R.color.colorRed))
                    mMissionBinding.tvStatus.setText(resources.getString(R.string.failure))
                } else {
                    mMissionBinding.tvStatus.setTextColor(ContextCompat.getColor(context, R.color.positive))
                    mMissionBinding.tvStatus.setText(resources.getString(R.string.success))
                }

                mMissionBinding.llAdaptermain.setOnClickListener {
                    SessionManager.saveSession("FLIGHTNO", branches.flight_number!!, context)
                    SessionManager.saveSession("MISSILENAME", branches.mission_name!!, context)
                    SessionManager.saveSession("LAUNCHEDAT", branches.launch_site!!.site_name_long!!, context)
                    SessionManager.saveSession("WIKI", branches.links!!.wikipedia!!, context)
                    SessionManager.saveSession("YOUTUBE", branches.links!!.video_link!!, context)
                    SessionManager.saveSession("IMAGE", branches.links!!.mission_patch_small!!, context)
                    SessionManager.saveSession("RNAME", branches.rocket!!.rocket_name!!, context)
                    SessionManager.saveSession("RTYPE", branches.rocket!!.rocket_type!!, context)
                    SessionManager.saveSessionuUtc("UTC", branches.launch_date_unix!!, context)
                    SessionManager.savePayLoadsArrayList("PARRAY", branches.rocket!!.second_stage!!.payloads, context)
                    startActivity(Intent(context, MissionDetailsActivity::class.java))
                }
            }
        }
    }
}