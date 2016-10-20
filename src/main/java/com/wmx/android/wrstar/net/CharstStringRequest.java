package com.wmx.android.wrstar.net;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;

import java.io.UnsupportedEncodingException;

/**
 * Created by Administrator on 2016/5/31.
 */
public class CharstStringRequest extends StringRequest {
    public CharstStringRequest(int method, String url, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(method, url, listener, errorListener);
    }

    public CharstStringRequest(String url, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(url, listener, errorListener);
    }

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        try {
            String String = new String(response.data, "UTF-8");
            return Response.success(new String(String),
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException   e) {
            return Response.error(new ParseError(e));
        }
//        return super.parseNetworkResponse(response);
    }

}
