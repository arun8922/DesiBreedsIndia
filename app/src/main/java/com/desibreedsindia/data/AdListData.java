package com.desibreedsindia.data;


/**
 * Created by hp on 1/1/2018.
 */

public class AdListData {



    private String mPostId;
    private String mCategoryId;
    private String mSubCategoryId;
    private String mSellerId;
    private String mAdTitle;
    private String mAdDescription;
    private String mAdPrice;
    private String mAdImage;
    public AdListData(String postId, String categoryId, String subCategoryId, String sellerId, String adTitle, String adDescription, String adPrice, String adImage) {
        mPostId=postId;
        mCategoryId=categoryId;
        mSubCategoryId=subCategoryId;
        mSellerId=sellerId;
        mAdTitle=adTitle;
        mAdDescription=adDescription;
        mAdPrice=adPrice;
        mAdImage=adImage;
    }

    public String getPostId() {
        return mPostId;
    }

    public String getCategoryId() {
        return mCategoryId;
    }
    public String getSubCategoryId() {
        return mSubCategoryId;
    }
    public String getSellerId() {
        return mSellerId;
    }
    public String getAdTitle() {
        return mAdTitle;
    }
    public String getAdDescription() {
        return mAdDescription;
    }
    public String getAdPrice() {
        return mAdPrice;
    }
    public String getAdImage() {
        return mAdImage;
    }

}
