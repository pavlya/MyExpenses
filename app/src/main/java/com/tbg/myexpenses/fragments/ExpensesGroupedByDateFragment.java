package com.tbg.myexpenses.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.tbg.myexpenses.MyExpensesApplication;
import com.tbg.myexpenses.R;
import com.tbg.myexpenses.adapters.ExpensesGroupedByDateAdapter;
import com.tbg.myexpenses.data.ExpensesDbHelper;
import com.tbg.myexpenses.data.ExpensesItem;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnExpensesGroupedFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ExpensesGroupedByDateFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ExpensesGroupedByDateFragment extends ListFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Cursor cursor;
    private ExpensesGroupedByDateAdapter mAdapter;

    private OnExpensesGroupedFragmentInteractionListener mListener;

    public ExpensesGroupedByDateFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ExpensesGroupedByDateFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ExpensesGroupedByDateFragment newInstance(String param1, String param2) {
        ExpensesGroupedByDateFragment fragment = new ExpensesGroupedByDateFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onStart() {
        super.onStart();

        // (We do this during onStart because at the point the listview is available)
        if (getFragmentManager().findFragmentById(R.id.fragment_container) != null) {
            getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        ExpensesItem item = ExpensesDbHelper.getInstance(getContext()).getItem(id);
        long date = item.getDate();
        mListener.onExpensesGroupSelected(date);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        SharedPreferences sharedPrefs = getActivity().
                getSharedPreferences(MyExpensesApplication.MY_APP_SHARED_PREFS, 0);
        String currenctGroupingValue = sharedPrefs.getString(MyExpensesApplication.SHARE_GROUPING_VALUE,
                MyExpensesApplication.GROUPED_BY_DAY);
        if (currenctGroupingValue.equals(MyExpensesApplication.GROUPED_BY_DAY)) {
            cursor = ExpensesDbHelper.getInstance(getContext()).getGroupedByDay();
        } else if (currenctGroupingValue.equals(MyExpensesApplication.GROUPED_BY_WEEK)) {
            cursor = ExpensesDbHelper.getInstance(getContext()).getGroupedByWeek();
        } else if (currenctGroupingValue.equals(MyExpensesApplication.GROUPED_BY_MONTH)) {
            cursor = ExpensesDbHelper.getInstance(getContext()).getGroupedByMonth();
        }
        mAdapter = new ExpensesGroupedByDateAdapter(getContext(), cursor, true);
        setListAdapter(mAdapter);
        return inflater.inflate(R.layout.fragment_expenses_list, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnExpensesGroupedFragmentInteractionListener) {
            mListener = (OnExpensesGroupedFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnExpensesGroupedFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnExpensesGroupedFragmentInteractionListener {
        // TODO: Update argument type and name
//        void onFragmentInteraction(Uri uri);
        void onExpensesGroupSelected(long date);
    }
}
