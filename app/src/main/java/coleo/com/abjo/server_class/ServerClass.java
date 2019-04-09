package coleo.com.abjo.server_class;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import coleo.com.abjo.activity.Login;
import coleo.com.abjo.activity.Splash;
import coleo.com.abjo.constants.Constants;

import static coleo.com.abjo.constants.Constants.getErrorMessage;

public class ServerClass {

    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        assert cm != null;
        return cm.getActiveNetworkInfo() != null;
    }

    private static void saveToken(Context context, JSONObject request) {
        try {
            Constants.setToken(context, request.getString("token"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static void handleError(Context context, VolleyError error) {
        if (error.networkResponse == null) {
            Toast.makeText(context, "اتصال اینترنت خود را بررسی کنید", Toast.LENGTH_SHORT).show();
        } else {
            if (error.networkResponse.statusCode == 403) {
                Constants.setToken(context, "");
                Intent intent = new Intent(context, Splash.class);
                context.startActivity(intent);
                ((Activity) context).finish();
                return;
            }
            Toast.makeText(context, getErrorMessage(error), Toast.LENGTH_SHORT).show();
        }
    }

    public static void checkPhone(final Context context, String phone) {

        String url = Constants.URL_CHECK_PHONE;

        //todo make json object and send phone

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        saveToken(context, response);
                        ((Login) context).goCode();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        handleError(context, error);
                    }
                });

        // Access the RequestQueue through your singleton class.
        MySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }

    public static void sendCode(final Context context, final String code) {
        String url = Constants.URL_SEND_CODE;

        JSONObject temp = new JSONObject();
        try {
            JSONObject developer = new JSONObject();
            developer.put("code", code);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ObjectRequest jsonObjectRequest = new ObjectRequest
                (context, Request.Method.POST, url, temp,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                saveToken(context, response);
                            }
                        }
                        , new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        ServerClass.handleError(context, error);
                    }
                }
                );

        MySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);

    }

}
