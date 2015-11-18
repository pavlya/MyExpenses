package com.tbg.myexpenses.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.tbg.myexpenses.ExpensesArrayAdapter;
import com.tbg.myexpenses.data.ExpensesItem;
import com.tbg.myexpenses.R;
import com.tbg.myexpenses.data.ItemsContainer;

/**
 * A placeholder fragment containing a simple view.
 */
public class ExpensesListViewFragment extends ListFragment {

    public interface OnExpenseItemSelectedListener {
        public void onExpenseItemSelected(int position);
    }

    private OnExpenseItemSelectedListener itemSelectListener;
    private ExpensesArrayAdapter expensesArrayAdapterAdapter;

    public ExpensesListViewFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        expensesArrayAdapterAdapter = new ExpensesArrayAdapter(getContext(), ItemsContainer.expensesItems);
        setListAdapter(expensesArrayAdapterAdapter);
        return inflater.inflate(R.layout.fragment_expenses_list, container, false);
    }

    public void addItemToArray(){
        ItemsContainer.expensesItems.add(0, new ExpensesItem());
        notifyAdapter();
    }

    public void notifyAdapter(){
        expensesArrayAdapterAdapter.notifyDataSetChanged();
    }


    public static boolean checkNullOrEmptyString(String string){
        return (string == null || string.length() <= 0);
    }

    @Override
    public void onStart() {
        super.onStart();

        // (We do this during onStart because at the point the listview is available)
        if(getFragmentManager().findFragmentById(R.id.fragment_container) != null){
            getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            itemSelectListener = (OnExpenseItemSelectedListener)activity;
        } catch (ClassCastException e){
            throw new ClassCastException(activity.toString()
                    + " must implement OnExpenseItemSelectedListener");
        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        // Notify the parent activity of selected item
        // TODO try to use id
        itemSelectListener.onExpenseItemSelected(position);

        // Set the item as checked to be highlighted when in two-pane layout
        getListView().setItemChecked(position, true);
    }
}
