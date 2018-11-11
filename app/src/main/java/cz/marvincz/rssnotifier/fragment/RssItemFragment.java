package cz.marvincz.rssnotifier.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import cz.marvincz.rssnotifier.R;
import cz.marvincz.rssnotifier.adapter.ItemAdapter;
import cz.marvincz.rssnotifier.model.ChannelWithItems;
import cz.marvincz.rssnotifier.model.RssChannel;
import cz.marvincz.rssnotifier.model.RssItem;

/**
 * Activities that contain this fragment must implement the
 * {@link RssItemFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RssItemFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RssItemFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_CHANNEL = "channel_param";

    // TODO: Rename and change types of parameters
    private RssChannel channel;

//    private OnFragmentInteractionListener mListener;
    private Function<RssChannel, List<RssItem>> supplier;
    private ItemAdapter adapter;

    public RssItemFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param channel Parameter 1.
     * @return A new instance of fragment RssItemFragment.
     */
    public static RssItemFragment newInstance(ChannelWithItems channel) {
        RssItemFragment fragment = new RssItemFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_CHANNEL, channel);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            channel = getArguments().getParcelable(ARG_CHANNEL);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.list);
        adapter = new ItemAdapter(getContext(), Collections.emptyList(), this::goToLink);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        return view;
    }

    private void goToLink(Uri link) {
        startActivity(new Intent(Intent.ACTION_VIEW, link)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }

//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//    }

    @Override
    public void onResume() {
        super.onResume();
        reloadData();
    }

    public void reloadData() {
        if (isResumed()) {
            adapter.replaceList(supplier.apply(channel));
        }
    }

    public void setSupplier(Function<RssChannel, List<RssItem>> supplier) {
        this.supplier = supplier;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
    }
}
