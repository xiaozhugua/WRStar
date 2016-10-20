package com.wmx.android.wrstar.biz.response;

import com.google.gson.annotations.SerializedName;
import com.wmx.android.wrstar.entities.Live;

import java.util.List;

/**
 * Created by Administrator on 2016/5/23.
 */
public class LivePageResponse extends BaseResponse {


    @SerializedName("body")
    public BodyEntity body;

    public static class BodyEntity {
        @SerializedName("btns")
        public Object btns;
        @SerializedName("banners")
        public Object banners;
        @SerializedName("next")
        public int next;
        @SerializedName("lives")
        public List<Live> lives;

    }
}
