package developer.futureinskies.MissionDetails

import android.content.Context
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import developer.futureinskies.R
import developer.futureinskies.SessionManager
import developer.futureinskies.databinding.AdapterPayloadsBinding
import developer.futureinskies.databinding.FragmentMissiondetailsBinding
import fortuner.ai.Login.DataModel.MissionResponseData
import java.util.*


class MissionDetailsFragment : Fragment() {

    private lateinit var mViewBinding: FragmentMissiondetailsBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_missiondetails, container, false)

        mViewBinding = FragmentMissiondetailsBinding.bind(root).apply {
            viewmodel = (activity as MissionDetailsActivity).obtainViewModel()
        }
        setHasOptionsMenu(true)
        retainInstance = false
        return mViewBinding.root
    }

    companion object {
        fun newInstance() = MissionDetailsFragment()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        mViewBinding.viewmodel?.flightno!!.set(resources.getString(R.string.flightno) + " " + SessionManager.getSession("FLIGHTNO", this!!.context!!))
        mViewBinding.viewmodel?.missilename!!.set(SessionManager.getSession("MISSILENAME", this!!.context!!))
        mViewBinding.viewmodel?.launchedat!!.set(SessionManager.getSession("LAUNCHEDAT", this!!.context!!))
        mViewBinding.viewmodel?.wiki!!.set(SessionManager.getSession("WIKI", this!!.context!!))
        mViewBinding.viewmodel?.rocketname!!.set(SessionManager.getSession("RNAME", this!!.context!!)
                + "(" + SessionManager.getSession("RTYPE", this!!.context!!) + ")")

        val date = java.util.Date(SessionManager.getSessionUtc("UTC", this!!.context!!) * 1000L)
        val sdf = java.text.SimpleDateFormat("dd-MM-yyyy HH:mm:ss")
        sdf.timeZone = java.util.TimeZone.getTimeZone("GMT-4")
        val formattedDate = sdf.format(date)
        mViewBinding.viewmodel?.launched!!.set(formattedDate)

        val wiki = SessionManager.getSession("WIKI", this!!.context!!)
        val contentwiki = SpannableString(wiki)
        contentwiki.setSpan(UnderlineSpan(), 0, wiki.length, 0)
        mViewBinding.viewmodel?.wiki!!.set(contentwiki.toString())

        val youtube = SessionManager.getSession("YOUTUBE", this!!.context!!)
        val contentyoutube = SpannableString(youtube)
        contentyoutube.setSpan(UnderlineSpan(), 0, youtube.length, 0)
        mViewBinding.viewmodel?.youtube!!.set(contentyoutube.toString())

        Glide.with(this)
                .load(SessionManager.getSession("IMAGE", this!!.context!!))
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(mViewBinding.ivDetailimg)

        val payloadAdapter = PayLoadsAdapter(SessionManager.getPayLoadsArrayList("PARRAY", this!!.activity!!)!!, activity)
        mViewBinding.rvPayloads.setAdapter(payloadAdapter)
        mViewBinding.rvPayloads.setLayoutManager(LinearLayoutManager(activity))

    }

    inner class PayLoadsAdapter(branchList: ArrayList<MissionResponseData.RocketsData.SecondStage.PayLoadsData>, context: FragmentActivity?) : RecyclerView.Adapter<PayLoadsAdapter.PayLoadsAdapterAdapterViewHolder>() {

        private var payLoadsLost = java.util.ArrayList<MissionResponseData.RocketsData.SecondStage.PayLoadsData>()
        private var context: Context

        init {
            this.payLoadsLost = branchList
            this.context = context!!
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PayLoadsAdapterAdapterViewHolder {
            return PayLoadsAdapterAdapterViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.adapter_payloads, parent, false))
        }

        override fun onBindViewHolder(holder: PayLoadsAdapterAdapterViewHolder, position: Int) {
            holder.bindUser(payLoadsLost[position])
        }

        override fun getItemCount(): Int {
            return payLoadsLost.size
        }

        inner class PayLoadsAdapterAdapterViewHolder(internal var mMissionBinding: AdapterPayloadsBinding) : RecyclerView.ViewHolder(mMissionBinding.getRoot()) {

            internal fun bindUser(branches: MissionResponseData.RocketsData.SecondStage.PayLoadsData) {
                mMissionBinding.tvId.setText(branches.payload_id)
                mMissionBinding.tvNationality.setText(branches.nationality)
                mMissionBinding.tvPayloadtype.setText(branches.payload_type)
                mMissionBinding.tvOrbit.setText(branches.orbit)
            }
        }
    }
}
