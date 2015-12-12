package com.tbg.myexpenses.fragments;

import android.app.Activity;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.tbg.myexpenses.MyExpensesApplication;
import com.tbg.myexpenses.R;
import com.tbg.myexpenses.adapters.ExpensesCursorAdapter;
import com.tbg.myexpenses.data.ExpensesDbHelper;

/**
 * A placeholder fragment containing a simple view.
 */
public class ExpensesListViewFragment extends ListFragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    public static final String ARG_PARAM_DATE = "date";
    public static final String ARG_PARAM_GROUPING_TYPE = "grouping_type";

    private OnExpenseItemSelectedListener itemSelectListener;
    private Cursor cursor;
    private ExpensesCursorAdapter expensesCursorAdapter;
    private long date_param;
    private String groupingTypeParam;
    public ExpensesListViewFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param date              parameter with long representation of date.
     * @param groupingTypeParam represents the grouping type for the framgent - month, week, day.
     * @return A new instance of fragment ExpensesGroupedByDateFragment.
     */
    public static ExpensesListViewFragment newInstance(long date, String groupingTypeParam) {
        ExpensesListViewFragment fragment = new ExpensesListViewFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_PARAM_DATE, date);
        args.putString(ARG_PARAM_GROUPING_TYPE, groupingTypeParam);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

//        expensesArrayAdapterAdapter = new ExpensesArrayAdapter(getContext(), ItemsContainer.expensesItems);
        cursor = ExpensesDbHelper.getInstance(getContext()).getAllItemsCursor();
//        expensesCursorAdapter = new ExpensesCursorAdapter(getContext(), cursor, true);
        setListAdapter(expensesCursorAdapter);
        return inflater.inflate(R.layout.fragment_expenses_list, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        // (We do this during onStart because at the point the listview is available)
        if (getFragmentManager().findFragmentById(R.id.fragment_container) != null) {
            getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        }
    }

    public void notifyAdapter() {
        expensesCursorAdapter.notifyDataSetChanged();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            itemSelectListener = (OnExpenseItemSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnExpenseItemSelectedListener");
        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        // Notify the parent activity of selected item
        // TODO try to use id
        Log.d("MY_TAG", "ExpensesListViewFragment.onListItemClick id: " + id);
        itemSelectListener.onExpenseItemSelected(id);

        // Set the item as checked to be highlighted when in two-pane layout
        getListView().setItemChecked(position, true);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            date_param = getArguments().getLong(ARG_PARAM_DATE);
            groupingTypeParam = getArguments().getString(ARG_PARAM_GROUPING_TYPE);
            Log.d(MyExpensesApplication.LOG_TAG, "date: " + date_param);
            SharedPreferences sharedPrefs = getActivity().
                    getSharedPreferences(MyExpensesApplication.MY_APP_SHARED_PREFS, 0);
            String currenctGroupingValue = sharedPrefs.getString(MyExpensesApplication.SHARE_GROUPING_VALUE,
                    MyExpensesApplication.GROUPED_BY_DAY);
            if (currenctGroupingValue.equals(MyExpensesApplication.GROUPED_BY_DAY)) {
                cursor = ExpensesDbHelper.getInstance(getContext()).getItemsByDay(date_param);
            } else if (currenctGroupingValue.equals(MyExpensesApplication.GROUPED_BY_WEEK)) {
                cursor = ExpensesDbHelper.getInstance(getContext()).getItemsByWeek(date_param);
            } else if (currenctGroupingValue.equals(MyExpensesApplication.GROUPED_BY_MONTH)) {
                cursor = ExpensesDbHelper.getInstance(getContext()).getItemsByMonth(date_param);
            }


            if (cursor != null) {
                Log.d(MyExpensesApplication.LOG_TAG, "Cursor is not null");

            } else {
                Log.d(MyExpensesApplication.LOG_TAG, "Cursor is null :(");
            }
            // updating cursor according to provided data
//            expensesCursorAdapter.changeCursor(cursor);
            expensesCursorAdapter = new ExpensesCursorAdapter(getContext(), cursor, true);
        }
    }

    public interface OnExpenseItemSelectedListener {
        void onExpenseItemSelected(long id);
    }
}
