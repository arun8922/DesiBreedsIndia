package com.desibreedsindia.Utils;

import android.content.Context;
import android.graphics.Typeface;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

public class FontHelper {
    private static final String TAG = FontHelper.class.getSimpleName();

    /**
     * Apply specified font for all text views (including nested ones) in the specified root view.
     */
    public static void applyFont(final Context context, final View root, final String fontPath) {
        try {
            if (root instanceof ViewGroup) {
                ViewGroup viewGroup = (ViewGroup) root;
                int childCount = viewGroup.getChildCount();
                for (int i = 0; i < childCount; i++)
                    applyFont(context, viewGroup.getChildAt(i), fontPath);

            } else if (root instanceof TextView)
                ((TextView) root).setTypeface(Typeface.createFromAsset(context.getAssets(), fontPath));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void applyFontLang(final Context context, final View root) {
        try {
            if (root instanceof ViewGroup) {
                ViewGroup viewGroup = (ViewGroup) root;
                int childCount = viewGroup.getChildCount();
                for (int i = 0; i < childCount; i++) {
                    applyFontLang(context, viewGroup.getChildAt(i));

                }

            } else if (root instanceof TextView) {
              /*  if (SessionSave.getSession("Lang", context).equals("my")) {
                    if (SessionSave.getSession("Font_Type", context).equals("zawgyi")) {

                        ((TextView) root).setTypeface(Typeface.createFromAsset(context.getAssets(), "mynamar-zawgyi.ttf"));
                    } else {
                        ((TextView) root).setTypeface(Typeface.createFromAsset(context.getAssets(), "myanmar-unicode.ttf"));
                    }

                } else {
                    ((TextView) root).setTypeface(Typeface.createFromAsset(context.getAssets(), "JosefinSans-SemiBold.otf"));
                }*/
                ((TextView) root).setTypeface(Typeface.createFromAsset(context.getAssets(), "DroidSerif_Regular.ttf"));

            }
            else if (root instanceof EditText) {
                ((TextInputLayout) root).setTypeface(Typeface.createFromAsset(context.getAssets(), "DroidSerif_Regular.ttf"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
