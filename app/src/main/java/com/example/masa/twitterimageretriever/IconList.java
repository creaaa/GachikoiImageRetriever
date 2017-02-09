
package com.example.masa.twitterimageretriever;

public class IconList {

    private String[] iListName = {
            "alert_dark_frame",
            "alert_light_frame",
            "arrow_down_float",
            "arrow_up_float",
            "bottom_bar",
            "btn_default",
            "btn_default_small",
            "btn_dialog",
            "btn_dropdown",
            "btn_minus",
            //
            "alert_dark_frame",
            "alert_light_frame",
            "arrow_down_float",
            "arrow_up_float",
            "bottom_bar",
            "btn_default",
            "btn_default_small",
            "btn_dialog",
            "btn_dropdown",
            "btn_minus",
            //
            "alert_dark_frame",
            "alert_light_frame",
            "arrow_down_float",
            "arrow_up_float",
            "bottom_bar",
            "btn_default",
            "btn_default_small",
            "btn_dialog",
            "btn_dropdown",
            "btn_minus"
    };

    private Integer[] iList = {
            android.R.drawable.alert_dark_frame
            , android.R.drawable.alert_light_frame
            , android.R.drawable.arrow_down_float
            , android.R.drawable.arrow_up_float
            , android.R.drawable.bottom_bar
            , android.R.drawable.btn_default
            , android.R.drawable.btn_default_small
            , android.R.drawable.btn_dialog
            , android.R.drawable.btn_dropdown,

    };

    public IconList() {
    }

    public int maxNum() {
        return iList.length;
    }

    public String getName(int number) {
        return iListName[number];
    }

    public int getIcon(int number) {
        return iList[number];

    }
}
