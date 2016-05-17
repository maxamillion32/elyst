package com.elystapp.elyst;

import android.app.ListFragment;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.elystapp.elyst.testing.code.TestDataFragmentList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@linkosting Fragment. OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HostingFragment# newInstance} factory method to
 * create an instance of this fragment.
 */
public class WatchlistFragment extends ListFragment {

    // Boolean Values and Position Selector
    int mCurrentPosition = 0;

    // onActivityCreated() is called when the activity's onCreate() method
    // has returned.
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Populate list with our static array of titles in list in the
        // test class
        setListAdapter(new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_activated_1,
                TestDataFragmentList.EVENTS));

        if (savedInstanceState != null) {
            // Restore last state for checked position.
            mCurrentPosition = savedInstanceState.getInt("curChoice", 0);
        }

        // We also highlight in uni-pane just for fun
        getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        getListView().setItemChecked(mCurrentPosition, true);

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("curChoice", mCurrentPosition);
    }

    // If the user clicks on an item in the list (e.g., Party2 then the
    // onListItemClick() method is called. It calls a helper function in
    // this case.
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {

        Toast.makeText(getActivity(),
                TestDataFragmentList.EVENTS[position] + " is selected",
                Toast.LENGTH_LONG).show();
    }
}
