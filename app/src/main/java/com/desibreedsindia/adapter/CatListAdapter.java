package com.desibreedsindia.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.desibreedsindia.R;
import com.desibreedsindia.Utils.FontHelper;
import com.desibreedsindia.Utils.RoundedCornersTransformation;
import com.desibreedsindia.data.AdListData;
import com.desibreedsindia.data.CatListData;
import com.github.siyamed.shapeimageview.CircularImageView;
import com.like.LikeButton;
import com.like.OnLikeListener;

import org.json.JSONObject;

import java.util.ArrayList;


/**
 * Created by developer on 11/19/15.
 */
public class CatListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>/*RecyclerView.Adapter<MailListAdapter.MainViewHolder>*/ {
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_SELECTED = 1;
    private final int VIEW_TYPE_LOADING = 2;
    private static int sCorner = 15;
    private static int sMargin = 5;
    public static int sBorder = 8;
    private static String sColor = "#7D9067";

    LayoutInflater inflater;
    ArrayList<CatListData> catListData;
    Context context;
    int colorCode;
    private JSONObject data;
    private int getSelectedItem = -1;


    public CatListAdapter(Context context, ArrayList<CatListData> modelList) {
        this.inflater = LayoutInflater.from(context);
        this.catListData = modelList;
        this.context = context;
        data = new JSONObject();

    }


    @NonNull
    @Override
    public /*MainViewHolder*/ RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        /*View view = inflater.inflate(R.layout.mail_list, parent, false);
        return new MainViewHolder(view);*/

        if (viewType == VIEW_TYPE_ITEM) {
            //View view = inflater.inflate(R.layout.mail_list, parent, false);
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cat_list, parent, false);
            return new MainViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        try {

            if (holder instanceof MainViewHolder) {
                MainViewHolder mainViewHolder = (MainViewHolder) holder;
                if (!catListData.get(position).getAdImage().isEmpty()) {

                    try {
//                        Glide.with(context).load(catListData.get(position).getAdImage()).apply(RequestOptions.bitmapTransform(new RoundedCornersTransformation(context, sCorner,sColor,sBorder))).into(mainViewHolder.adLogo);
                    } catch (Exception e) {
//                        Glide.with(context).load(hotelListData.get(position).getHotelLogo()).apply(RequestOptions.bitmapTransform(new RoundedCornersTransformation(context, sCorner,sColor, sMargin))).into(mainViewHolder.hotelLogo);
                        e.printStackTrace();
                    }
                } else {
//                    mainViewHolder.adLogo.setBackgroundResource(R.drawable.ic_menu_send);
                }


//                mainViewHolder.adName.setText(catListData.get(position).getAdTitle());
//                mainViewHolder.adPrice.setText(catListData.get(position).getAdDescription());
                /*if (hotelListData.get(position).getHotelOpenStatus() == 1) {
                    mainViewHolder.orderNow.setTextColor(context.getResources().getColor(R.color.theme_green));
                    mainViewHolder.orderNow.setText(context.getResources().getString(R.string.order_now));
                    if (hotelListData.get(position).getHotelPreOrderStatus() == 0) {
                        mainViewHolder.preOrder.setVisibility(View.VISIBLE);
                        mainViewHolder.dot.setVisibility(View.VISIBLE);
                    } else {
                        mainViewHolder.preOrder.setVisibility(View.GONE);
                        mainViewHolder.dot.setVisibility(View.GONE);
                    }

                } else {
                    if (hotelListData.get(position).getHotelPreOrderStatus() == 1) {
                        mainViewHolder.preOrder.setVisibility(View.VISIBLE);
                        mainViewHolder.dot.setVisibility(View.VISIBLE);
                    } else {
                        mainViewHolder.preOrder.setVisibility(View.GONE);
                        mainViewHolder.dot.setVisibility(View.GONE);
                    }
                    mainViewHolder.orderNow.setTextColor(context.getResources().getColor(R.color.theme_red));
                    mainViewHolder.orderNow.setText(context.getResources().getString(R.string.closed));
                }*/
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return catListData.size();
    }

    @Override
    public int getItemViewType(int position) {
        return VIEW_TYPE_ITEM;
    }


    private class MainViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private CircularImageView adLogo;

        MainViewHolder(View itemView) {
            super(itemView);

            FontHelper.applyFontLang(itemView.getContext(), itemView.findViewById(R.id.cat_list_root));

            adLogo = (CircularImageView) itemView.findViewById(R.id.cat_logo);

            adLogo.setOnClickListener(this);

        }


        @Override
        public void onClick(View v) {

            switch (v.getId())
            {

            }
            try {

               /* Intent detailIntent = new Intent(context, SignInActivity.class);
                detailIntent.putExtra("hotel_id", "" + hotelListData.get(getLayoutPosition()).getHotelId());
                detailIntent.putExtra("hotel_name", "" + hotelListData.get(getLayoutPosition()).getHotelName());
                detailIntent.putExtra("hotel_latitude", "" + hotelListData.get(getLayoutPosition()).getHotelLatitude());
                detailIntent.putExtra("hotel_longitude", "" + hotelListData.get(getLayoutPosition()).getHotelLongitude());
                detailIntent.putExtra("hotel_open_status", "" + hotelListData.get(getLayoutPosition()).getHotelOpenStatus());
                detailIntent.putExtra("hotel_pre_order_status", "" + hotelListData.get(getLayoutPosition()).getHotelPreOrderStatus());
                detailIntent.putExtra("hotel_logo", "" + hotelListData.get(getLayoutPosition()).getHotelLogo());
                detailIntent.putExtra("ratings", "" + hotelListData.get(getLayoutPosition()).getHotelRatings());
                context.startActivity(detailIntent);*/

//                ((Activity) context).startActivityForResult(detailIntent, 1);
            } catch (Exception e) {
                e.printStackTrace();
                //Toast.makeText(context, "ISSUEEXCEPTION" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }

        }

    }

}