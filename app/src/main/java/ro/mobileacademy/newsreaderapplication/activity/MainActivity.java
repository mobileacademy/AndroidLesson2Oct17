package ro.mobileacademy.newsreaderapplication.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import ro.mobileacademy.newsreaderapplication.CounterAsyncTask;
import ro.mobileacademy.newsreaderapplication.NewsReaderApplication;
import ro.mobileacademy.newsreaderapplication.R;
import ro.mobileacademy.newsreaderapplication.adapters.PublicationAdapter;
import ro.mobileacademy.newsreaderapplication.events.CountFinishEvent;
import ro.mobileacademy.newsreaderapplication.models.Article;
import ro.mobileacademy.newsreaderapplication.models.Publication;
import ro.mobileacademy.newsreaderapplication.networking.HackerNewsAPI;
import ro.mobileacademy.newsreaderapplication.receivers.PackageInstallReceiver;
import ro.mobileacademy.newsreaderapplication.services.CounterIntentService;
import ro.mobileacademy.newsreaderapplication.services.CounterService;
import ro.mobileacademy.newsreaderapplication.utils.NewsReaderAppPref;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    private ImageView myImageView;

    private GridView publicationsGridView;
    private PublicationAdapter myAdapter;
    private ArrayList<Publication> publicationList = new ArrayList<>();

    public static final String COUNTER_ACTION  = "count";
    public static final String LIST_PACKAGES = "packages";
    public static final String BROADCAST_ACTION = "count_finished";


    // step 1- > new custom broadcast receiver
    private BroadcastReceiver myReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "onReceive") ;
            Toast.makeText(context, "Times up!", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setData();


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerLayout = navigationView.getHeaderView(0); // 0-index header


        myImageView = (ImageView) headerLayout.findViewById(R.id.imageView);
        myImageView.setOnClickListener(this);

        publicationsGridView = (GridView) findViewById(R.id.publications_grid_view);
        myAdapter = new PublicationAdapter(this, publicationList);
        publicationsGridView.setAdapter(myAdapter);

        publicationsGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, ArticleListActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        NewsReaderAppPref pref = new NewsReaderAppPref(this);

        String installedPackage = pref.getString(PackageInstallReceiver.PACKAGE_KEY);
        if(installedPackage.isEmpty()) {
            Toast.makeText(this, "No new package installed!", Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(this, "package=" + installedPackage, Toast.LENGTH_SHORT).show();
        }


        // step 2, 3 -> register my custom broadcast receiver
        IntentFilter intentFilter = new IntentFilter(BROADCAST_ACTION);
        LocalBroadcastManager.getInstance(this).registerReceiver(myReceiver, intentFilter);


        //register to event bus
        EventBus.getDefault().register(this);


        // start asyncTask
        new CounterAsyncTask().execute(5);


        List<Article> articles = NewsReaderApplication.getInstance().getDataSource().getAllArticlesByPublication(HackerNewsAPI.PUBLICATION_LIBERTY_ID);

        Log.d(TAG, "######## article db size entries = " + articles.size());
    }

    @Override
    protected void onStop() {
        super.onStop();

        // step 4 - do unregister
        LocalBroadcastManager.getInstance(this).unregisterReceiver(myReceiver);

        //unregister from EventBus
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(CountFinishEvent event) {
        Toast.makeText(this, "EventBus - event received!" + event.getCount(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {
            Log.d(TAG, "start service");
            //start service from main thread
            Intent intent = new Intent(this, CounterService.class);
            intent.setAction(COUNTER_ACTION);
            startService(intent);

        } else if (id == R.id.nav_send) {

            //start intent service
            Intent intent = new Intent(this, CounterIntentService.class);
            intent.setAction(COUNTER_ACTION);
            startService(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imageView:
                // open camera intent


                break;
            case R.id.fab:
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                break;

        }
    }

    private void setData() {
        Publication p1 = new Publication(1, "Title1", R.drawable.ic_menu_camera);
        Publication p2 = new Publication(2, "Title2", R.drawable.ic_menu_gallery);
        Publication p3 = new Publication(3, "Title3", R.drawable.ic_menu_send);
        Publication p4 = new Publication(4, "Title4", R.drawable.ic_menu_share);
        Publication p5 = new Publication(1, "Title1", R.drawable.ic_menu_camera);
        Publication p6 = new Publication(2, "Title2", R.drawable.ic_menu_gallery);
        Publication p7 = new Publication(3, "Title3", R.drawable.ic_menu_send);
        Publication p8 = new Publication(4, "Title4", R.drawable.ic_menu_share);
        Publication p9 = new Publication(1, "Title1", R.drawable.ic_menu_camera);
        Publication p10 = new Publication(2, "Title2", R.drawable.ic_menu_gallery);
        Publication p11 = new Publication(3, "Title3", R.drawable.ic_menu_send);
        Publication p12 = new Publication(4, "Title4", R.drawable.ic_menu_share);
        Publication p13 = new Publication(1, "Title1", R.drawable.ic_menu_camera);
        Publication p14 = new Publication(2, "Title2", R.drawable.ic_menu_gallery);
        Publication p15 = new Publication(3, "Title3", R.drawable.ic_menu_send);
        Publication p16 = new Publication(4, "Title4", R.drawable.ic_menu_share);

        publicationList.add(p1);
        publicationList.add(p2);
        publicationList.add(p3);
        publicationList.add(p4);
        publicationList.add(p5);
        publicationList.add(p6);
        publicationList.add(p7);
        publicationList.add(p8);
        publicationList.add(p9);
        publicationList.add(p10);
        publicationList.add(p11);
        publicationList.add(p12);
        publicationList.add(p13);
        publicationList.add(p14);
        publicationList.add(p15);
        publicationList.add(p16);
    }
}
