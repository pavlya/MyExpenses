package com.tbg.myexpenses.fragments;


import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.tbg.myexpenses.R;
import com.tbg.myexpenses.adapters.ExpensesCursorTreeAdapter;
import com.tbg.myexpenses.data.ExpensesDbHelper;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ExpensesExpandableListViewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ExpensesExpandableListViewFragment extends Fragment implements ExpandableListView.OnChildClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    ExpensesListViewFragment.OnExpenseItemSelectedListener itemSelectListener;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public ExpensesExpandableListViewFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ExpensesExpandableListViewFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ExpensesExpandableListViewFragment newInstance(String param1, String param2) {
        ExpensesExpandableListViewFragment fragment = new ExpensesExpandableListViewFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
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
        View parentView = inflater.inflate(R.layout.fragment_expense_expandable_list, container, false);
        Cursor curs = ExpensesDbHelper.getInstance(getContext()).getGroupedByCategory();
        ExpensesCursorTreeAdapter treeAdapter = new ExpensesCursorTreeAdapter(getContext(),curs,
                R.layout.expense_item_header,
                R.layout.expense_item_child);
        ExpandableListView expandableListView = (ExpandableListView)parentView.findViewById(R.id.lv_expandable_items);
        expandableListView.setAdapter(treeAdapter);
        expandableListView.setOnChildClickListener(this);
        return parentView;
    }

    @Override
    public void onAttach(Context context) {
        try {
            itemSelectListener = (ExpensesListViewFragment.OnExpenseItemSelectedListener)context;
        } catch (Exception e){
            e.printStackTrace();
        }

        super.onAttach(context);
    }


    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
        // Notify the parent activity of selected item
        // TODO try to use id
        Log.d("MY_TAG", "ExpensesListViewFragment.onListItemClick id: " + id);
        itemSelectListener.onExpenseItemSelected(id);

        // Set the item as checked to be highlighted when in two-pane layout
//        getListView().setItemChecked(position, true);
        return false;
    }
}
