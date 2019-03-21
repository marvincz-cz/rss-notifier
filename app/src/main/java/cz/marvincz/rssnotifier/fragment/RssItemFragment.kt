package cz.marvincz.rssnotifier.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cz.marvincz.rssnotifier.R
import cz.marvincz.rssnotifier.RssApplication
import cz.marvincz.rssnotifier.adapter.ItemAdapter
import cz.marvincz.rssnotifier.room.Database
import kotlinx.android.synthetic.main.fragment_list.*
import java.util.function.Consumer
import javax.inject.Inject

/**
 * Activities that contain this fragment must implement the
 * [RssItemFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [RssItemFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RssItemFragment : Fragment() {

    // TODO: Rename and change types of parameters
    private lateinit var channelUrl: String
    @Inject
    lateinit var database: Database

    //    private OnFragmentInteractionListener mListener;
    private lateinit var adapter: ItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        channelUrl = arguments!!.getString(ARG_CHANNEL)!!
        RssApplication.appComponent.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        adapter = ItemAdapter(context!!,
                Consumer { this.goToLink(it) })
        list.adapter = adapter
        list.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        list.itemAnimator = DefaultItemAnimator()

        database.dao().getItems(channelUrl).observe(this, Observer { adapter.data = it })
    }

    private fun goToLink(link: Uri) {
        startActivity(Intent(Intent.ACTION_VIEW, link)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments](http://developer.android.com/training/basics/fragments/communicating.html) for more information.
     */
    interface OnFragmentInteractionListener// TODO: Update argument type and name

    companion object {
        // TODO: Rename parameter arguments, choose names that match
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private val ARG_CHANNEL = "channel_param"

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param channelUrl Parameter 1.
         * @return A new instance of fragment RssItemFragment.
         */
        fun newInstance(channelUrl: String): RssItemFragment {
            val fragment = RssItemFragment()
            val args = Bundle()
            args.putString(ARG_CHANNEL, channelUrl)
            fragment.arguments = args
            return fragment
        }
    }
}// Required empty public constructor
