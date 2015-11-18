package com.tbg.myexpenses.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.tbg.myexpenses.data.ExpensesItem;
import com.tbg.myexpenses.R;
import com.tbg.myexpenses.data.ItemsContainer;

public class ExpenseEditFragment extends Fragment implements View.OnClickListener{

    private EditText etAmount;
    private EditText etDescription;
    private Spinner spinnerCategory;

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_delete:
                deletValueAndCloseActivity();
                return;
            case R.id.btn_submit:
                saveValuesAndCloseActivity();
                return;
        }
    }

    public interface OnExpenseEditListener{
        public void onExpenseEdited(int itemNumber);
    }


    public final static String ARG_POSITION = "position";
    int mCurrentPosition = -1;
    private OnExpenseEditListener expenseEditListener;

    public ExpenseEditFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();

        // During startup, check if there are arguments passed to the fragment.
        // onStart is a good place to do this because the layout has already been
        // applied to the fragment at this point so we can safely call the method
        // below that sets the article text.

        Bundle args = getArguments();
        if(args != null){
            // Edit Expense item, based on argument passed in
            updateEditView(args.getInt(ARG_POSITION));
        } else if (mCurrentPosition != -1){
            updateEditView(mCurrentPosition);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(savedInstanceState != null){
            mCurrentPosition = savedInstanceState.getInt(ARG_POSITION);
        }
        // Inflate the layout for this fragment
        View parentView = inflater.inflate(R.layout.fragment_expense_edit, container, false);
//        return inflater.inflate(R.layout.fragment_expense_edit, container, false);
        Button btnSubmit = (Button)parentView.findViewById(R.id.btn_submit);
        btnSubmit.setOnClickListener(this);
        Button btnDelete = (Button)parentView.findViewById(R.id.btn_delete);
        btnDelete.setOnClickListener(this);
        return parentView;
    }

    private void deletValueAndCloseActivity() {
        if(mCurrentPosition >= 0) {
            ItemsContainer.expensesItems.remove(mCurrentPosition);
        }
        expenseEditListener.onExpenseEdited(mCurrentPosition);
    }


    private void saveValuesAndCloseActivity() {
        ExpensesItem item = new ExpensesItem();
        item.setAmount(Double.parseDouble(etAmount.getText().toString()));
        item.setExplanation(etDescription.getText().toString());
        item.setDate(System.currentTimeMillis());
        ItemsContainer.expensesItems.set(mCurrentPosition, item);
        expenseEditListener.onExpenseEdited(mCurrentPosition);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            expenseEditListener = (OnExpenseEditListener)activity;
        } catch (ClassCastException e){
            throw new ClassCastException(activity.toString()
                    + " must implement OnExpenseItemSelectedListener");
        }
    }

    public void updateEditView(int position) {
        etAmount = (EditText)getView().findViewById(R.id.et_amount);
        etDescription = (EditText)getView().findViewById(R.id.et_description);
        spinnerCategory = (Spinner)getView().findViewById(R.id.sp_category);

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(
                getContext(), android.R.layout.simple_spinner_item, ItemsContainer.categories);
        if(position != -1) {
            ExpensesItem item = ItemsContainer.expensesItems.get(position);
            etAmount.setText(item.getAmount() + "");
            etDescription.setText(item.getExplanation());
            spinnerCategory.setAdapter(spinnerAdapter);
            mCurrentPosition = position;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Save the current article selection in case we need to recreate the fragment
        outState.putInt(ARG_POSITION, mCurrentPosition);
    }
}
