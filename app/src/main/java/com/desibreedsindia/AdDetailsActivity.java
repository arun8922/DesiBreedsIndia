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

//import com.suleiman.material.adapter.DessertAdapter;

public class AdDetailsActivity extends AppCompatActivity {

    private CollapsingToolbarLayout collapsingToolbar;
    private AppBarLayout appBarLayout;
    private RecyclerView recyclerView;
    private boolean canAddItem = false;


//    private DessertAdapter dessertAdapter;

    private Menu collapsedMenu;
    private boolean appBarExpanded = true;

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
        collapsingToolbar.setTitle(getString(R.string.app_name));

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
                R.drawable.flag_north_korea);

        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
            @SuppressWarnings("ResourceType")
            @Override
            public void onGenerated(Palette palette) {
                int vibrantColor = palette.getVibrantColor(R.color.theme_green);
                collapsingToolbar.setContentScrimColor(vibrantColor);
                collapsingToolbar.setStatusBarScrimColor(R.color.white);
            }
        });

//        recyclerView = (RecyclerView) findViewById(R.id.scrollableview);
        //  Use when your list size is constant for better performance
//        recyclerView.setHasFixedSize(true);

//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
//        recyclerView.setLayoutManager(linearLayoutManager);

//        dessertAdapter = new DessertAdapter(this);
//        recyclerView.setAdapter(dessertAdapter);

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
//                Log.d(AnimateToolbar.class.getSimpleName(), "onOffsetChanged: verticalOffset: " + verticalOffset);

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

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        /*if (collapsedMenu != null && (!appBarExpanded || collapsedMenu.size() != 1)) {
            //collapsed
            collapsedMenu.add("Add")
                    .setIcon(R.drawable.chick)
                    .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        } else {
            //expanded
        }*/
            if(canAddItem)
            {
//                collapsedMenu.getItem(0).setIcon(R.drawable.heart_icon_filled);

               /* collapsedMenu.add("Add")
                        .setIcon(R.drawable.heart_icon_filled)
                        .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);*/
                canAddItem = false;

            }else {
                /*collapsedMenu.add("Add")
                        .setIcon(R.drawable.heart_icon_outer)
                        .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);*/
//                collapsedMenu.getItem(0).setIcon(R.drawable.heart_icon_outer);

                canAddItem = true;

            }

      /*  collapsedMenu.add("sub")
                .setIcon(R.drawable.chick)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);*/
        return super.onPrepareOptionsMenu(collapsedMenu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);
        collapsedMenu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
           /* case android.R.id.home:
                finish();
                return true;
            case R.id.action_settings:
                return true;*/
        }
        if (item.getItemId() == R.id.action_add_fav){
            invalidateOptionsMenu();



           /* collapsedMenu.add("Add")
                    .setIcon(R.drawable.heart_icon_filled)
                    .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);*/
            Toast.makeText(this, "clicked add", Toast.LENGTH_SHORT).show();
        }else {

        }

        return super.onOptionsItemSelected(item);
    }
}
