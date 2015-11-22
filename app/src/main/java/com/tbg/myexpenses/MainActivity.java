package com.tbg.myexpenses;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import com.tbg.myexpenses.data.ExpensesDbHelper;
import com.tbg.myexpenses.data.ExpensesItem;
import com.tbg.myexpenses.data.ItemsContainer;
import com.tbg.myexpenses.fragments.ExpenseEditFragment;
import com.tbg.myexpenses.fragments.ExpensesListViewFragment;

public class MainActivity extends AppCompatActivity implements
    ExpensesListViewFragment.OnExpenseItemSelectedListener, ExpenseEditFragment.OnExpenseEditListener{

    private ExpensesListViewFragment firstFragment;
    private FloatingActionButton fab;
    private ExpensesDbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // connect to database
        dbHelper = ExpensesDbHelper.getInstance(this);
        dbHelper.getReadableDatabase();

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                // Add item to array and start Editing
                onExpenseItemSelected(-1);
            }
        });

        // Check that the activity is using the layout version with
        // the fragment_container FrameLayout
        if (findViewById(R.id.fragment_container) != null) {

            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                return;
            }

            // Create a new Fragment to be placed in the activity layout
            firstFragment = new ExpensesListViewFragment();

            // In case this activity was started with special instructions from an
            // Intent, pass the Intent's extras to the fragment as arguments
            firstFragment.setArguments(getIntent().getExtras());

            // Add the fragment to the 'fragment_container' FrameLayout
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, firstFragment).commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onExpenseItemSelected(long id) {
        ExpenseEditFragment editFragment = (ExpenseEditFragment)getSupportFragmentManager().
                findFragmentById(R.id.fragment);

        if(editFragment != null){
            // if edit fragment is available, we're in two pane layout...

            // Call a method in the Edit fragment to update its content
            editFragment.updateEditView(id);
        } else {
            // If the fragment is not available, we're in the one-pane layout and must swap frags..

            // Create fragment and give it and argument for hte selected article
            ExpenseEditFragment newFragment = new ExpenseEditFragment();
            Bundle args = new Bundle();
            args.putLong(ExpenseEditFragment.ARG_POSITION, id);
            newFragment.setArguments(args);

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            // Replace whatever is in the fragment_container view with this fragment,
            // and add the transaction to the back stack so the user can navigate back
            transaction.replace(R.id.fragment_container, newFragment);

            transaction.addToBackStack(null);

            // commit the transaction
            transaction.commit();
            fab.hide();
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(!fab.isShown()){
            fab.show();
        }
        // check if item was edited, in case of emtpy item - remove it from array
//        if(wasntEdited(ItemsContainer.expensesItems.get(0))){
//            ItemsContainer.expensesItems.remove(0);
//            firstFragment.notifyAdapter();
//        }
    }

//    private boolean wasntEdited(ExpensesItem expensesItem) {
//        if(expensesItem != null){
//            return false;
//        }
//        return(expensesItem.getExplanation().length() <=0 && expensesItem.getAmount() <=0);
//    }

    @Override
    public void onExpenseEdited() {
        ExpensesListViewFragment newFragment = new ExpensesListViewFragment();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack so the user can navigate back
        transaction.replace(R.id.fragment_container, newFragment);

//        transaction.addToBackStack(null);

        // commit the transaction
        transaction.commit();
        if(!fab.isShown()){
            fab.show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(dbHelper!= null){
            dbHelper.close();
        }
    }
}
