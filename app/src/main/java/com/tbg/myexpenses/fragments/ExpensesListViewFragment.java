package com.tbg.myexpenses.fragments;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.tbg.myexpenses.R;
import com.tbg.myexpenses.adapters.ExpensesCursorAdapter;
import com.tbg.myexpenses.data.ExpensesDbHelper;

/**
 * A placeholder fragment containing a simple view.
 */
public class ExpensesListViewFragment extends ListFragment {

    private OnExpenseItemSelectedListener itemSelectListener;
    private Cursor cursor;
    private ExpensesCursorAdapter expensesCursorAdapter;
    public ExpensesListViewFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

//        expensesArrayAdapterAdapter = new ExpensesArrayAdapter(getContext(), ItemsContainer.expensesItems);
        cursor = ExpensesDbHelper.getInstance(getContext()).getAllItemsCursor();
        expensesCursorAdapter = new ExpensesCursorAdapter(getContext(), cursor, true);
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

    public interface OnExpenseItemSelectedListener {
        void onExpenseItemSelected(long id);
    }
}
