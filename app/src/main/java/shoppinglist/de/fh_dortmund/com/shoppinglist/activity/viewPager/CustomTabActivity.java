package shoppinglist.de.fh_dortmund.com.shoppinglist.activity.viewPager;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import shoppinglist.de.fh_dortmund.com.shoppinglist.activity.AddArtikelActivity;
import shoppinglist.de.fh_dortmund.com.shoppinglist.activity.AddArtikelMainActivity;
import shoppinglist.de.fh_dortmund.com.shoppinglist.activity.MainActivity;
import shoppinglist.de.fh_dortmund.com.shoppinglist.fragment.ArtikelFragment;
import shoppinglist.de.fh_dortmund.com.shoppinglist.fragment.LocationFragment;
import shoppinglist.de.fh_dortmund.com.shoppinglist.fragment.ReminderFragment;
import shoppinglist.de.fh_dortmund.com.shoppinglist.model.ViewPagerAdapter;
import shoppinglist.de.fh_dortmund.com.shoppinglist.R;


public class CustomTabActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private TabLayout tabLayout;
    public int listkeyId;
    private String activityName;
    //Fragments

    ArtikelFragment artikelFragment;
    LocationFragment locationFragment;
    ReminderFragment reminderFragment;

    String[] tabTitle={"+ Artikel","+ Location","+ Reminder"};
    int[] unreadCount={0,0,0};


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_without_icon);

        //Initializing viewPager
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setOffscreenPageLimit(3);
        setupViewPager(viewPager);

        //Initializing the tablayout
        tabLayout = (TabLayout) findViewById(R.id.tablayout);
        tabLayout.setupWithViewPager(viewPager);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            activityName = extras.getString(AddArtikelMainActivity.LIST_NAME2);
            //selectedTemplateIndex = extras.getInt("selectedTemplateIndex");
        }

        this.setTitle(activityName);

        try {
            setupTabIcons();
        } catch (Exception e) {
            e.printStackTrace();
        }

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                viewPager.setCurrentItem(position, false);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }

    private void setupViewPager(ViewPager viewPager)
    {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        artikelFragment=new ArtikelFragment();
        locationFragment=new LocationFragment();
        reminderFragment=new ReminderFragment();
        adapter.addFragment(artikelFragment,"+ Artikel");
        adapter.addFragment(locationFragment,"+ Location");
        adapter.addFragment(reminderFragment,"+ Reminder");
        viewPager.setAdapter(adapter);
    }

    private View prepareTabView(int pos) {
        View view = getLayoutInflater().inflate(R.layout.custom_tabs,null);
        TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
        TextView tv_count = (TextView) view.findViewById(R.id.tv_count);
        tv_title.setText(tabTitle[pos]);
        if(unreadCount[pos]>0)
        {
            tv_count.setVisibility(View.VISIBLE);
            tv_count.setText(""+unreadCount[pos]);
        }
        else
            tv_count.setVisibility(View.GONE);


        return view;
    }

    private void setupTabIcons()
    {

        for(int i=0;i<tabTitle.length;i++)
        {
            /*TabLayout.Tab tabitem = tabLayout.newTab();
            tabitem.setCustomView(prepareTabView(i));
            tabLayout.addTab(tabitem);*/

            tabLayout.getTabAt(i).setCustomView(prepareTabView(i));
        }


    }

}
