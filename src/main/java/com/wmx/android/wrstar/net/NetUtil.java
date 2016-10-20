package com.wmx.android.wrstar.net;

import android.content.Context;
import android.net.ConnectivityManager;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.wmx.android.wrstar.app.WRStarApplication;

import java.util.HashMap;
import java.util.Map;

public class NetUtil {
	public static final RequestQueue mQueue = Volley.newRequestQueue(WRStarApplication.getContext());

	public static void sendPost(String url, final NetResult result) {
		CharstStringRequest request = new CharstStringRequest(Request.Method.POST, url, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				result.result(response);
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				error.printStackTrace();
				result.result("");
			}
		});
		request.setRetryPolicy(new DefaultRetryPolicy(10 * 1000, 1, 1.0f));
		mQueue.add(request);
	}

	public static void sendPost(String url, final String params, final NetResult result) {
		CharstStringRequest stringRequest = new CharstStringRequest(Request.Method.POST, url,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						result.result(response);
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						error.printStackTrace();
						result.result("");
					}
				}) {
			@Override
			protected Map<String, String> getParams() {
				Map<String, String> map = new HashMap<String, String>();
				String[] a = params.split("&");
				for (int i = 0; i < a.length; i++) {
					String[] b = a[i].split("=");
					if (b.length > 1) {
						map.put(b[0], b[1]);
					}
				}
				return map;
			}
		};
		stringRequest.setRetryPolicy(new DefaultRetryPolicy(10 * 1000, 1, 1.0f));
		mQueue.add(stringRequest);
	}

	public static void sendGet(String url, final NetResult result) {
		CharstStringRequest request = new CharstStringRequest(Request.Method.GET, url, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				result.result(response);
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				error.printStackTrace();
				result.result("");
			}
		});
		request.setRetryPolicy(new DefaultRetryPolicy(10 * 1000, 1, 1.0f));
		mQueue.add(request);
	}

	public static void sendGet(String url, final String params, final NetResult result, int... timeout) {

		CharstStringRequest stringRequest = new CharstStringRequest(Request.Method.GET, url + "?" + params,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						result.result(response);
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						error.printStackTrace();
						result.result("");
					}
				}) {
			// @Override
			// protected Map<String, String> getParams() {
			// Map<String, String> map = new HashMap<String, String>();
			// String[] a = params.split("&");
			// for (int i = 0; i < a.length; i++) {
			// String[] b = a[i].split("=");
			// if (b.length > 1) {
			// map.put(b[0], b[1]);
			// }
			// }
			// return map;
			// }
		};
		if (timeout.length > 0) {
			stringRequest.setRetryPolicy(new DefaultRetryPolicy(timeout[0], 1, 1.0f));
		} else {
			stringRequest.setRetryPolicy(new DefaultRetryPolicy(10 * 1000, 1, 1.0f));
		}
		mQueue.add(stringRequest);
	}

	public static boolean isNetworkAvailable(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		return cm.getActiveNetworkInfo().isAvailable();
		// if (cm == null) {
		// } else {
		// //如果仅仅是用来判断网络连接
		// //则可以使用 cm.getActiveNetworkInfo().isAvailable();
		// NetworkInfo[] info = cm.getAllNetworkInfo();
		// if (info != null) {
		// for (int i = 0; i < info.length; i++) {
		// if (info[i].getState() == NetworkInfo.State.CONNECTED) {
		// return true;
		// }
		// }
		// }
		// }
		// return false;
	}
}
