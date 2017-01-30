package indinasportsnews.com.isnapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(SaveSharedPreferences.getFlag(this).length() == 0) {
            Intent intent = new Intent(getApplicationContext() , Welcome.class) ;
            startActivity(intent) ;
        } else {
            setContentView(R.layout.activity_main);
            startService(new Intent(getApplicationContext(), MyService.class));
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    MainActivity.this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.addDrawerListener(toggle);
            toggle.syncState();

            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);

            Menu menu = navigationView.getMenu();
            Set set = SaveSharedPreferences.getPrefSportsList(getApplicationContext());
            ArrayList<String> selectedStrings = new ArrayList<>();
            try {
                selectedStrings.addAll(set);
            } catch (Exception ex) {

            }
            for (int i = 0; i < selectedStrings.size(); i++)
                menu.add(R.id.my_group, Menu.NONE, 0, selectedStrings.get(i)).setIcon(R.drawable.logo);

            viewPager = (ViewPager) findViewById(R.id.viewpager);
            setupViewPager(viewPager);

            tabLayout = (TabLayout) findViewById(R.id.tabs);
            tabLayout.setupWithViewPager(viewPager);
            tabLayout.setTabTextColors(
                    ContextCompat.getColor(getApplicationContext(), R.color.white),
                    ContextCompat.getColor(getApplicationContext(), R.color.light_green)
            );
        }
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
        if (id == R.id.sports_select) {
            Intent intent = new Intent(this , SportsSelection.class) ;
            startActivity(intent) ;
            return true;
        }

        if(id == R.id.menu_refresh) {
            // Signal SwipeRefreshLayout to start the progress indicator
            final SwipeRefreshLayout mySwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefresh) ;
            mySwipeRefreshLayout.setRefreshing(true);
            Toast.makeText(getApplicationContext(), "News updating ...", Toast.LENGTH_SHORT).show();
            mySwipeRefreshLayout . setRefreshing(false) ;
            finish();
            startActivity(getIntent());
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        String title = item.getTitle().toString() ;
        int catid1 = 0 , catid2 = 0 ;
        Intent intent=new Intent(MainActivity.this, SportNews.class) ;
        switch(title) {
            case "Cricket" : {
                catid1 = 908 ;
                catid2 = 983 ;
                break ;
            }
            case "Football" : {
                catid1 = 28 ;
                catid2 = 1025 ;
                break ;
            }
            case "Basketball" : {
                catid1 = 44 ;
                catid2 = 979 ;
                break ;
            }
            case "Badminton" : {
                catid1 = 1007 ;
                catid2 = 0 ;
                break ;
            }
            case "Chess" : {
                catid1 = 26 ;
                catid2 = 1043 ;
                break ;
            }
            case "Table Tennis" : {
                catid1 = 32 ;
                catid2 = 1050 ;
                break ;
            }
            case "Golf" : {
                catid1 = 29 ;
                catid2 = 975 ;
                break ;
            }
            case "Hockey" : {
                catid1 = 986 ;
                catid2 = 1035 ;
                break ;
            }
            case "Boxing" : {
                catid1 = 27 ;
                catid2 = 1029 ;
                break ;
            }
            case "Tennis" : {
                catid1 = 30 ;
                catid2 = 1024 ;
                break ;
            }
            case "Racing" : {
                catid1 = 33 ;
                catid2 = 1026 ;
                break ;
            }
            case "Shooting" : {
                catid1 = 995 ;
                catid2 = 1042 ;
                break ;
            }
            case "Wrestling" : {
                catid1 = 998 ;
                catid2 = 0 ;
                break ;
            }
            case "Athletics" : {
                catid1 = 1002 ;
                catid2 = 982 ;
                break ;
            }
            case "Snooker/Billiards" : {
                catid1 = 1006 ;
                catid2 = 45 ;
                break ;
            }
            case "BLOG" : {
                catid1 = 1065 ;
                catid2 = 0 ;
                break ;
            }
        }
        intent . putExtra("catid1" , catid1) ;
        intent . putExtra("catid2" , catid2) ;
        intent . putExtra("title" , title) ;
        startActivity(intent) ;
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new RecentNewsFragment() , "Recent News");
        adapter.addFrag(new ScoreboardFragment() , "Scoreboard");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            int size = 0 ;
            if(mFragmentList != null)
                size = mFragmentList.size() ;
            return size ;
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

}
