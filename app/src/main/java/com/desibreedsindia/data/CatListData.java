package com.desibreedsindia.data;


/**
 * Created by hp on 1/1/2018.
 */

public class CatListData {

    private String mCategoryId;
    private String mAdImage;
    public CatListData(String categoryId, String adImage) {
        mCategoryId=categoryId;
        mAdImage=adImage;
    }
    public String getCategoryId() {
        return mCategoryId;
    }
    public String getAdImage() {
        return mAdImage;
    }

}
