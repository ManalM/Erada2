package com.example.android.erada2;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.yalantis.contextmenu.lib.ContextMenuDialogFragment;
import com.yalantis.contextmenu.lib.MenuObject;
import com.yalantis.contextmenu.lib.MenuParams;
import com.yalantis.contextmenu.lib.interfaces.OnMenuItemClickListener;
import com.yalantis.contextmenu.lib.interfaces.OnMenuItemLongClickListener;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements OnMenuItemClickListener, OnMenuItemLongClickListener {

    private ContextMenuDialogFragment mMenuDialogFragment;
    private FragmentManager fragmentManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_home);


        fragmentManager= getSupportFragmentManager();

        initMenuFregment();
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);

        // Create an adapter that knows which fragment should be shown on each page
        CategoryAdapter adapter = new CategoryAdapter(this, getSupportFragmentManager());

        // Set the adapter onto the view pager
        viewPager.setAdapter(adapter);

        // Find the tab layout that shows the tabs
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        // Connect the tab layout with the view pager. This will
        //   1. Update the tab layout when the view pager is swiped
        //   2. Update the view pager when a tab is selected
        //   3. Set the tab layout's tab names with the view pager's adapter's titles
        //      by calling onPageTitle()
        tabLayout.setupWithViewPager(viewPager);

    }


    private void initMenuFregment(){

        MenuParams menuParams = new MenuParams();
        menuParams.setActionBarSize((int) getResources().getDimension(R.dimen.tool_bar_height));
        menuParams.setMenuObjects(getMenuObject());
        menuParams.setClosableOutside(false);
        // set other settings to meet your needs
        mMenuDialogFragment = ContextMenuDialogFragment.newInstance(menuParams);
        mMenuDialogFragment.setItemClickListener(this);
        mMenuDialogFragment.setItemLongClickListener(this);
    }
    private List<MenuObject> getMenuObject(){
        List<MenuObject> menuObject = new ArrayList<>();

        MenuObject close = new MenuObject();
        close.setResource(R.drawable.close);
        close.setDividerColor(R.color.divider_color);
        MenuObject search = new MenuObject("البحث");
        search.setResource(R.drawable.search);
        close.setDividerColor(R.color.divider_color);

        MenuObject setting = new MenuObject("إضافة جهة");
        setting.setResource(R.drawable.plus);

        menuObject.add(close);
        menuObject.add(search);
        menuObject.add(setting);
        return menuObject;
    }
    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_main:
                if(fragmentManager.findFragmentByTag(ContextMenuDialogFragment.TAG) == null){

                mMenuDialogFragment.show(fragmentManager, "ContextMenuDialogFragment");

            }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMenuItemClick(View clickedView, int position) {

        switch (position){
            case 1:
                Intent intent1 =new Intent(HomeActivity.this , searchActivity.class);
                startActivity(intent1);
                break;
            case 2:
                Intent intent =new Intent(HomeActivity.this , SignActivity.class);
                startActivity(intent);
                break;

                default:
                    break;

        }
    }

    @Override
    public void onMenuItemLongClick(View clickedView, int position) {

    }
}
