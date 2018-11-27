package com.desibreedsindia;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.desibreedsindia.adapter.DessertAdapter;


public class AnimateToolbar extends AppCompatActivity {

    private CollapsingToolbarLayout collapsingToolbar;
    private AppBarLayout appBarLayout;
    private RecyclerView recyclerView;

    private DessertAdapter dessertAdapter;

    private Menu collapsedMenu;
    private boolean appBarExpanded = true;

    private static final int MENU_EDIT = 0;
    private static final int MENU_DELETE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_animate_toolbar);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.anim_toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        appBarLayout = (AppBarLayout) findViewById(R.id.appbar);

        collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle("testing");

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
                R.drawable.header);

        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
            @SuppressWarnings("ResourceType")
            @Override
            public void onGenerated(Palette palette) {
                int vibrantColor = palette.getVibrantColor(R.color.theme_green);
                collapsingToolbar.setContentScrimColor(vibrantColor);
                collapsingToolbar.setStatusBarScrimColor(R.color.theme_grey);
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.scrollableview);
        //  Use when your list size is constant for better performance
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        dessertAdapter = new DessertAdapter(this);
        recyclerView.setAdapter(dessertAdapter);

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                Log.d(AnimateToolbar.class.getSimpleName(), "onOffsetChanged: verticalOffset: " + verticalOffset);

                //  Vertical offset == 0 indicates appBar is fully expanded.
                if (Math.abs(verticalOffset) > 200) {
                    appBarExpanded = false;
                    invalidateOptionsMenu();
                } else {
                    appBarExpanded = true;
                    invalidateOptionsMenu();
                }
            }
        });

    }
/*
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
       *//* if (collapsedMenu != null
                && (!appBarExpanded || collapsedMenu.size() != 1)) {
            //collapsed
            collapsedMenu.add("Add")
                    .setIcon(R.drawable.chick)
                    .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        } else {
            //expanded
        }
        return super.onPrepareOptionsMenu(collapsedMenu);*//*

//        menu.clear();
//        menu.add(0, MENU_EDIT, Menu.NONE, getString(R.string.app_name)).setIcon(R.drawable.chick).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
//        menu.add(0, MENU_DELETE, Menu.NONE, getString(R.string.google_app_id)).setIcon(R.drawable.heart_icon_filled).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return super.onPrepareOptionsMenu(menu);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        collapsedMenu = menu;
        getMenuInflater().inflate(R.menu.home, menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
      *//*  switch (item.getItemId()) {
            case android.R.id.home:

                finish();
                return true;
        }
        if (item.getTitle() == "Add") {
            Toast.makeText(this, "clicked add", Toast.LENGTH_SHORT).show();
            invalidateOptionsMenu();
            item.setIcon(R.drawable.flag_egypt);
        }

        return super.onOptionsItemSelected(item);*//*

        switch (item.getItemId()) {
            case R.id.action_add_fav:
                Toast.makeText(this, "Edit menu item clicked", Toast.LENGTH_SHORT).show();

                collapsedMenu.findItem(R.id.action_add_fav).setIcon(R.drawable.flag_egypt);


                return true;
                //break;
            case MENU_DELETE:
                Toast.makeText(this, "Delete menu item clicked", Toast.LENGTH_SHORT).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }*/




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    boolean canAddItem = false;
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Toast toast;
        if(item.getItemId() == R.id.action_add_fav){
            invalidateOptionsMenu();
            canAddItem = true;
            item.setVisible(false);
        }
        else{
            toast = Toast.makeText(this, item.getTitle()+" Clicked!", Toast.LENGTH_SHORT);
            toast.show();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        if(canAddItem){
            menu.getItem(0).setIcon(0);
            menu.getItem(0).setIcon(R.drawable.flag_egypt);
           /* MenuItem mi = menu.add("New Item");
            mi.setIcon(R.drawable.cow_head);
            mi.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_IF_ROOM);*/
            canAddItem = false;
        }
        /*else{
            menu.getItem(0).setIcon(R.drawable.flag_jamaica);
//            canAddItem = true;
        }*/

        return super.onPrepareOptionsMenu(menu);
    }




}
