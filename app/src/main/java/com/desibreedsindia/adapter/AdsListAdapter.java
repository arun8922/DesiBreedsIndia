package com.desibreedsindia.adapter;

import android.content.Context;
import android.content.Intent;
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
import com.desibreedsindia.AdDetailsActivity;
import com.desibreedsindia.AnimateToolbar;
import com.desibreedsindia.R;
import com.desibreedsindia.Utils.FontHelper;
import com.desibreedsindia.Utils.RoundedCornersTransformation;
import com.desibreedsindia.data.AdListData;
import com.desibreedsindia.signIn.SignInActivity;
import com.like.LikeButton;
import com.like.OnLikeListener;

import org.json.JSONObject;

import java.util.ArrayList;


/**
 * Created by developer on 11/19/15.
 */
public class AdsListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>/*RecyclerView.Adapter<MailListAdapter.MainViewHolder>*/ {
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_SELECTED = 1;
    private final int VIEW_TYPE_LOADING = 2;
    private static int sCorner = 15;
    private static int sMargin = 5;
    public static int sBorder = 8;
    private static String sColor = "#7D9067";

    LayoutInflater inflater;
    ArrayList<AdListData> hotelListData;
    Context context;
    int colorCode;
    private JSONObject data;
    private int getSelectedItem = -1;


    public AdsListAdapter(Context context, ArrayList<AdListData> modelList) {
        this.inflater = LayoutInflater.from(context);
        this.hotelListData = modelList;
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
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ads_list, parent, false);
            return new MainViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        try {

            if (holder instanceof MainViewHolder) {
                MainViewHolder mainViewHolder = (MainViewHolder) holder;
                if (!hotelListData.get(position).getAdImage().isEmpty()) {

                    try {
                        Glide.with(context).load(hotelListData.get(position).getAdImage()).apply(RequestOptions.bitmapTransform(new RoundedCornersTransformation(context, sCorner,sColor,sBorder))).into(mainViewHolder.adLogo);
                    } catch (Exception e) {
//                        Glide.with(context).load(hotelListData.get(position).getHotelLogo()).apply(RequestOptions.bitmapTransform(new RoundedCornersTransformation(context, sCorner,sColor, sMargin))).into(mainViewHolder.hotelLogo);
                        e.printStackTrace();
                    }
                } else {
                    mainViewHolder.adLogo.setBackgroundResource(R.drawable.ic_menu_send);
                }


                mainViewHolder.adName.setText(hotelListData.get(position).getAdTitle());
                mainViewHolder.adPrice.setText(hotelListData.get(position).getAdDescription());
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
        return hotelListData.size();
    }

    @Override
    public int getItemViewType(int position) {
        return VIEW_TYPE_ITEM;
    }


    private class MainViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private LinearLayout adListLay;
        private ImageView adLogo;
        private LikeButton heartIcon;
        private TextView adName, adPrice;

        MainViewHolder(View itemView) {
            super(itemView);

            FontHelper.applyFontLang(itemView.getContext(), itemView.findViewById(R.id.ad_list_root));

            adLogo = (ImageView) itemView.findViewById(R.id.ad_image);
            adName = (TextView) itemView.findViewById(R.id.txt_ad_name);
            adPrice = (TextView) itemView.findViewById(R.id.txt_ad_price);
            heartIcon = (LikeButton) itemView.findViewById(R.id.fav_icon);
            adListLay = (LinearLayout) itemView.findViewById(R.id.lay_ad_list);
            adListLay.setOnClickListener(this);
            heartIcon.setOnLikeListener(new OnLikeListener() {
                @Override
                public void liked(LikeButton likeButton) {

                }

                @Override
                public void unLiked(LikeButton likeButton) {

                }
            });
        }


        @Override
        public void onClick(View v) {


            try {
                Intent detailIntent = new Intent(context, AnimateToolbar.class);
                context.startActivity(detailIntent);

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