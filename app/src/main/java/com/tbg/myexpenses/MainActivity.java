package com.tbg.myexpenses;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.tbg.myexpenses.data.ExpensesDbHelper;
import com.tbg.myexpenses.fragments.ExpenseEditFragment;
import com.tbg.myexpenses.fragments.ExpensesExpandableListViewFragment;
import com.tbg.myexpenses.fragments.ExpensesGroupedByDateFragment;
import com.tbg.myexpenses.fragments.ExpensesListViewFragment;

public class MainActivity extends AppCompatActivity implements
    ExpensesListViewFragment.OnExpenseItemSelectedListener, ExpenseEditFragment.OnExpenseEditListener,
        NavigationView.OnNavigationItemSelectedListener, ExpensesGroupedByDateFragment.OnExpensesGroupedFragmentInteractionListener {

    public static String TagExpensesListFragment = "TagExpensesList";
    public static String TagExpensesEditFragment = "TagExpensesEdit";

    private ExpensesExpandableListViewFragment firstFragment;
    private ExpensesGroupedByDateFragment groupedByDateFragment;
    private FloatingActionButton fab;
    private ExpensesDbHelper dbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Navigation Drawer
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                drawer, toolbar, R.string.open_navigation_drawer, R.string.close_navigation_drawer);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView)findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // connect to database
        dbHelper = ExpensesDbHelper.getInstance(this);
        dbHelper.getReadableDatabase();

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onExpenseItemSelected(-1);
            }
        });

        init();
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
        if (id == R.id.action_get_total) {
            double amount = ExpensesDbHelper.getInstance(getApplication()).getTotalAmount();
            String totalAmount = "Total amount spent: " + amount;
            Toast.makeText(getApplicationContext(), totalAmount, Toast.LENGTH_LONG).show();
            String[] stringArray = getResources().getStringArray(R.array.categories);
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
            editFragment.updateOrInitEditView(id);
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

        if(!fab.isShown()){
            fab.show();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void init() {
        onExpenseEdited();
    }


    /**
     * Called when application started or item in Expense fragment was modified
     */
    public void onExpenseEdited() {
        Bundle savedInstanceState = getIntent().getExtras();

        if (findViewById(R.id.fragment_container) != null) {

            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                return;
            }

            // Create a new Fragment to be placed in the activity layout
            groupedByDateFragment = new ExpensesGroupedByDateFragment();

            // In case this activity was started with special instructions from an
            // Intent, pass the Intent's extras to the fragment as arguments
            groupedByDateFragment.setArguments(getIntent().getExtras());

            // Add the fragment to the 'fragment_container' FrameLayout
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            if(getSupportFragmentManager().findFragmentById(R.id.fragment_container) != null) {
                ft.replace(R.id.fragment_container, groupedByDateFragment, TagExpensesListFragment);
            } else {
                ft.add(R.id.fragment_container, groupedByDateFragment, TagExpensesListFragment);
            }
            ft.commit();
        }

        if(!fab.isShown()){
            fab.show();
        }

        // hide soft keyboard
        hideSoftKeyboard();
    }


    public void onExpenseEditedOld() {
        Bundle savedInstanceState = getIntent().getExtras();
        // Check that the activity is using the layout version with
        // the fragment_container FrameLayout
//        if (findViewById(R.id.fragment_container) != null) {
//
//            // However, if we're being restored from a previous state,
//            // then we don't need to do anything and should return or else
//            // we could end up with overlapping fragments.
//            if (savedInstanceState != null) {
//                return;
//            }
//
//            // Create a new Fragment to be placed in the activity layout
//            firstFragment = new ExpensesListViewFragment();
//
//            // In case this activity was started with special instructions from an
//            // Intent, pass the Intent's extras to the fragment as arguments
//            firstFragment.setArguments(getIntent().getExtras());
//
//            // Add the fragment to the 'fragment_container' FrameLayout
//            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//            if(getSupportFragmentManager().findFragmentById(R.id.fragment_container) != null) {
//                ft.replace(R.id.fragment_container, firstFragment, TagExpensesListFragment);
//            } else {
//                ft.add(R.id.fragment_container, firstFragment, TagExpensesListFragment);
//            }
//            ft.commit();
//        }
        if (findViewById(R.id.fragment_container) != null) {

            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                return;
            }

            // Create a new Fragment to be placed in the activity layout
            firstFragment = new ExpensesExpandableListViewFragment();

            // In case this activity was started with special instructions from an
            // Intent, pass the Intent's extras to the fragment as arguments
            firstFragment.setArguments(getIntent().getExtras());

            // Add the fragment to the 'fragment_container' FrameLayout
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            if (getSupportFragmentManager().findFragmentById(R.id.fragment_container) != null) {
                ft.replace(R.id.fragment_container, firstFragment, TagExpensesListFragment);
            } else {
                ft.add(R.id.fragment_container, firstFragment, TagExpensesListFragment);
            }
            ft.commit();
        }

        if (!fab.isShown()) {
            fab.show();
        }

        // hide soft keyboard
        hideSoftKeyboard();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(dbHelper!= null){
            dbHelper.close();
        }
    }

    public void hideSoftKeyboard() {
        View focus = getCurrentFocus();
        if(focus != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.nav_group_month){
            System.out.println();
        }

        DrawerLayout drawer = (DrawerLayout)findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onExpensesGroupSelected() {

    }
}
