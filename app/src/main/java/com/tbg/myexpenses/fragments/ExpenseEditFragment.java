package com.tbg.myexpenses.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.tbg.myexpenses.R;
import com.tbg.myexpenses.Utility;
import com.tbg.myexpenses.data.ExpensesDbHelper;
import com.tbg.myexpenses.data.ExpensesItem;

import java.util.Calendar;

public class ExpenseEditFragment extends Fragment
        implements View.OnClickListener, TimePicker.OnTimeChangedListener,
        DatePicker.OnDateChangedListener {

    public final static String ARG_POSITION = "position";
    long mCurrentId = -1;
    private EditText etAmount;
    private EditText etTitle;
    private EditText etExplanation;
    private Spinner spinnerCategory;
    private Activity parentActivity;
    private TextView tvDate;
    private long dateAndTime = -1;
    private Calendar calendar;
    private OnExpenseEditListener expenseEditListener;

    public ExpenseEditFragment() {
        // Required empty public constructor
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_delete:
                deleteValueAndCloseActivity();
                return;
            case R.id.btn_submit:
                saveValuesAndCloseActivity();
                return;
            default:
                return;
        }
    }

    @Override
    public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {

        // set date and time
        calendar.set(Calendar.HOUR, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);

        dateAndTime = calendar.getTimeInMillis();
        tvDate.setText(Utility.getDateText(dateAndTime));
    }

    @Override
    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, monthOfYear);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        dateAndTime = calendar.getTimeInMillis();
        tvDate.setText(Utility.getDateText(dateAndTime));
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
            updateOrInitEditView(args.getLong(ARG_POSITION));
        } else if (mCurrentId != -1){
            updateOrInitEditView(mCurrentId);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(savedInstanceState != null){
            mCurrentId = savedInstanceState.getInt(ARG_POSITION);
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

    private void deleteValueAndCloseActivity() {
        if(mCurrentId >= 0) {
//            ItemsContainer.expensesItems.remove(mCurrentPosition);
            ExpensesDbHelper.getInstance(getContext()).deleteExpenseItem(mCurrentId);
        }
        expenseEditListener.onExpenseEdited();
    }

    private void saveValuesAndCloseActivity() {
        ExpensesItem item = new ExpensesItem();
        if(!etAmount.getText().toString().isEmpty()){
            item.setAmount(Double.parseDouble(etAmount.getText().toString()));
            item.setTitle(etTitle.getText().toString());
            item.setExplanation(etExplanation.getText().toString());
            item.setcategory(spinnerCategory.getSelectedItemPosition());
            // save date and time in long format
            if (dateAndTime != -1) {
                item.setDate(dateAndTime);
            } else {
                item.setDate(System.currentTimeMillis());
            }
            if(mCurrentId != -1) {
                ExpensesDbHelper.getInstance(getContext()).updateExpenseItem(item, mCurrentId);
            }
            else {
                ExpensesDbHelper.getInstance(getContext()).addExpenseItem(item);
            }
            expenseEditListener.onExpenseEdited();
        } else {
            Toast.makeText(getContext(), "Amount can't be empty", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        parentActivity = activity;
        try {
            expenseEditListener = (OnExpenseEditListener)activity;
        } catch (ClassCastException e){
            throw new ClassCastException(activity.toString()
                    + " must implement OnExpenseItemSelectedListener");
        }
    }

    /**
     * @param id init the view
     */
    public void updateOrInitEditView(long id) {
        etAmount = (EditText)getView().findViewById(R.id.et_amount);
        etTitle = (EditText)getView().findViewById(R.id.et_title);
        etExplanation = (EditText)getView().findViewById(R.id.et_description);
        spinnerCategory = (Spinner)getView().findViewById(R.id.sp_category);
        tvDate = (TextView) getView().findViewById(R.id.tv_date);
        tvDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(getContext());
                dialog.setContentView(R.layout.custom_date_time_layout);

                DatePicker datePicker = (DatePicker) dialog.findViewById(R.id.date_picker_dialog);
                TimePicker timePicker = (TimePicker) dialog.findViewById(R.id.time_picker_dialog);

                dialog.show();

                calendar = Calendar.getInstance();
                // init the time
                if (dateAndTime != -1) {
                    calendar.setTimeInMillis(dateAndTime);
                } else {
                    calendar.setTimeInMillis(System.currentTimeMillis());
                }

                timePicker.setOnTimeChangedListener(ExpenseEditFragment.this);
                datePicker.setCalendarViewShown(false);
                datePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH), ExpenseEditFragment.this);

            }
        });
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(
                getContext(), android.R.layout.simple_spinner_item,
                getContext().getResources().getStringArray(R.array.categories));
        spinnerCategory.setAdapter(spinnerAdapter);

        if(id != -1) {
            // get item using database id
            ExpensesItem item = ExpensesDbHelper.getInstance(getContext()).getItem(id);
            etAmount.setText(item.getAmount() + "");
            etTitle.setText(item.getTitle());
            etExplanation.setText(item.getExplanation());
            spinnerCategory.setSelection(item.getCategory());
            // get the date value and convert it
            tvDate.setText(Utility.getDateText(item.getDate()));
            dateAndTime = item.getDate();
            mCurrentId = id;
        } else {
            dateAndTime = System.currentTimeMillis();
            tvDate.setText(Utility.getDateText(dateAndTime));
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Save the current article selection in case we need to recreate the fragment
        outState.putLong(ARG_POSITION, mCurrentId);
    }

    public void onDateSet() {

    }

    public interface OnExpenseEditListener {
        void onExpenseEdited();
    }

    public interface DatePickerListener {
        void onDateSet(long dateValue);
    }
}
