package com.tbg.myexpenses;

import android.app.Activity;
import android.content.SharedPreferences;
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
import com.tbg.myexpenses.data.ExpensesItem;
import com.tbg.myexpenses.fragments.ExpenseEditFragment;
import com.tbg.myexpenses.fragments.ExpensesExpandableListViewFragment;
import com.tbg.myexpenses.fragments.ExpensesGroupedByDateFragment;
import com.tbg.myexpenses.fragments.ExpensesListViewFragment;

public class MainActivity extends AppCompatActivity implements
    ExpensesListViewFragment.OnExpenseItemSelectedListener, ExpenseEditFragment.OnExpenseEditListener,
        NavigationView.OnNavigationItemSelectedListener, ExpensesGroupedByDateFragment.OnExpensesGroupedFragmentInteractionListener {

    public static String TagExpensesListFragment = "TagExpensesList";
    public static String TagExpensesEditFragment = "TagExpensesEdit";
    public static String TagGroupedByDateFragment = "TagGroupedByDate";


    private ExpensesExpandableListViewFragment firstFragment;
    private ExpensesGroupedByDateFragment groupedByDateFragment;
    private ExpensesListViewFragment expensesListViewFragment;

    private FloatingActionButton fab;
    private ExpensesDbHelper dbHelper;
    private Toolbar toolbarBottom;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_top);
        setSupportActionBar(toolbar);
        toolbarBottom = (Toolbar) findViewById(R.id.toolbar_bottom);

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

        switch (id) {
            case R.id.action_get_total:
                double amount = ExpensesDbHelper.getInstance(getApplication()).getTotalAmount();
                String totalAmount = "Total amount spent: " + amount;
                Toast.makeText(getApplicationContext(), totalAmount, Toast.LENGTH_LONG).show();
                break;
            case R.id.action_delete_all:
                resetDb();
                break;
            case R.id.action_add_dummy:
                addItemsToDb();
                break;
        }

        init();
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
        // TODO remove resetDb method
        onExpenseEdited();
        // update title according to saved value
        SharedPreferences sharedPreferences = getSharedPreferences(MyExpensesApplication.MY_APP_SHARED_PREFS, 0);
        String title = sharedPreferences.getString(MyExpensesApplication.SHARE_GROUPING_VALUE, "Ungrouped");
        setTitle(title);
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
                ft.replace(R.id.fragment_container, groupedByDateFragment, TagGroupedByDateFragment);
            } else {
                ft.add(R.id.fragment_container, groupedByDateFragment, TagGroupedByDateFragment);
            }
            ft.addToBackStack(TagGroupedByDateFragment).commit();
        }

        if(!fab.isShown()){
            fab.show();
        }

        // hide soft keyboard
        hideSoftKeyboard();

        // notify about editing values in db
        notifyBottomBar();
    }

    private void notifyBottomBar() {
        String totalSpent = getResources().getString(R.string.total_spent);
        double amount = ExpensesDbHelper.getInstance(getApplication()).getTotalAmount();
        toolbarBottom.setTitle(totalSpent + amount);
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
        SharedPreferences sharedPreferences = getSharedPreferences(MyExpensesApplication.MY_APP_SHARED_PREFS, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        switch (id) {
            case R.id.nav_group_month:
                editor.putString(MyExpensesApplication.SHARE_GROUPING_VALUE,
                        MyExpensesApplication.GROUPED_BY_MONTH);
                break;
            case R.id.nav_group_week:
                editor.putString(MyExpensesApplication.SHARE_GROUPING_VALUE,
                        MyExpensesApplication.GROUPED_BY_WEEK);
                break;
            case R.id.nav_group_day:
                editor.putString(MyExpensesApplication.SHARE_GROUPING_VALUE,
                        MyExpensesApplication.GROUPED_BY_DAY);
                break;
            default:
                break;
        }
        // initialize view according to new values
        editor.commit();

        DrawerLayout drawer = (DrawerLayout)findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        init();
        return true;
    }

    @Override
    /**
     * Init ExpensesListViewFragment with provided date
     */
    public void onExpensesGroupSelected(long date) {
//        Bundle savedInstanceState = getIntent().getExtras();

        if (findViewById(R.id.fragment_container) != null) {

            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
//            if (savedInstanceState != null) {
//                return;
//            }

            // Create a new Fragment to be placed in the activity layout
            expensesListViewFragment = new ExpensesListViewFragment();

            // In case this activity was started with special instructions from an
            // Intent, pass the Intent's extras to the fragment as arguments
//            groupedByDateFragment.setArguments(getIntent().getExtras());

            Bundle bundle = new Bundle();
            bundle.putLong(ExpensesListViewFragment.ARG_PARAM_DATE, date);
            // TODO: 09/12/2015 change the "TestString value to different implementation"
            bundle.putString(ExpensesListViewFragment.ARG_PARAM_GROUPING_TYPE, "TestString");
            expensesListViewFragment.setArguments(bundle);


            // Add the fragment to the 'fragment_container' FrameLayout
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            if (getSupportFragmentManager().findFragmentById(R.id.fragment_container) != null) {
                ft.replace(R.id.fragment_container, expensesListViewFragment, TagExpensesListFragment);
            } else {
                ft.add(R.id.fragment_container, expensesListViewFragment, TagExpensesListFragment);
            }
            ft.addToBackStack(TagExpensesListFragment).commit();
        }

        if (!fab.isShown()) {
            fab.show();
        }

        // hide soft keyboard
        hideSoftKeyboard();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    public void addItemsToDb() {

        ExpensesDbHelper.getInstance(this).addExpenseItem(new ExpensesItem(250, 1449402321000l, "Breakfast", "SomeText of 0 category", 1));
        ExpensesDbHelper.getInstance(this).addExpenseItem(new ExpensesItem(15, 1449380721000l, "Milk", "SomeText of 0 category", 1));
        ExpensesDbHelper.getInstance(this).addExpenseItem(new ExpensesItem(60, 1449312321000l, "cigarettes", "", 0));
        ExpensesDbHelper.getInstance(this).addExpenseItem(new ExpensesItem(100, 1449279921000l, "gas", "", 4));
        ExpensesDbHelper.getInstance(this).addExpenseItem(new ExpensesItem(300, 1449279921000l, "electricity", "", 2));
        ExpensesDbHelper.getInstance(this).addExpenseItem(new ExpensesItem(25, 1446515121000l, "vegetables", "grochery store near house", 1));
        ExpensesDbHelper.getInstance(this).addExpenseItem(new ExpensesItem(15, 1446515121000l, "bus", "", 4));
        ExpensesDbHelper.getInstance(this).addExpenseItem(new ExpensesItem(30, 1446515121000l, "battery", "Small package", 5));
        ExpensesDbHelper.getInstance(this).addExpenseItem(new ExpensesItem(153, 1449402321000l, "food for week", "am:pm", 1));
        ExpensesDbHelper.getInstance(this).addExpenseItem(new ExpensesItem(300, 1449398721000l, "jeans, shirt, and socks", "Azrieli", 0));
        ExpensesDbHelper.getInstance(this).addExpenseItem(new ExpensesItem(50, 1449312321000l, "lost money", "fuckin hole in my pocket", 0));
        ExpensesDbHelper.getInstance(this).addExpenseItem(new ExpensesItem(90, 1449279921000l, "gas", "yellow", 8));
        ExpensesDbHelper.getInstance(this).addExpenseItem(new ExpensesItem(60, 1449380721000l, "flowers", "mom's birthday", 7));
        ExpensesDbHelper.getInstance(this).addExpenseItem(new ExpensesItem(35, 1449380721000l, "balloons", "mom's birthday", 7));
        ExpensesDbHelper.getInstance(this).addExpenseItem(new ExpensesItem(15, 1449380721000l, "chocolate", "mom's birthday", 7));
        ExpensesDbHelper.getInstance(this).addExpenseItem(new ExpensesItem(250, 1446515121000l, "veterinarian", "dr. klimovich", 11));
        ExpensesDbHelper.getInstance(this).addExpenseItem(new ExpensesItem(399, 1446515121000l, "hp printer + 1 year guarantee", "", 5));
    }

    public void resetDb() {
        ExpensesDbHelper.getInstance(this).clearDb();
        // TODO use date values instead of long
    }
}
