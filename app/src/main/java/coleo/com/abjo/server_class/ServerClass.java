package coleo.com.abjo.server_class;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import coleo.com.abjo.activity.CodeActivity;
import coleo.com.abjo.activity.Login;
import coleo.com.abjo.activity.Splash;
import coleo.com.abjo.constants.Constants;
import coleo.com.abjo.data_class.User;

import static coleo.com.abjo.constants.Constants.getErrorMessage;

public class ServerClass {

    private static final String TAG = "server class";

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

    public static void checkPhone(final Context context, final String phone) {

        String url = Constants.URL_CHECK_PHONE;
        url += phone;
        url += "/";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        ((Login) context).goCode(phone);
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

    public static void sendCode(final Context context, final String phone, final String code) {

        String url = Constants.URL_SEND_CODE;
        url += phone;
        url += "/";
        url += code;
        url += "/";

        ObjectRequest jsonObjectRequest = new ObjectRequest
                (context, Request.Method.GET, url, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                //todo do some thing with this code
//                                saveToken(context, response);
                                User user = parseUser(response);
                                ((CodeActivity) context).goMainPage();
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

    private static User parseUser(JSONObject response) {
        Log.i(TAG, "parseUser: " + response.toString());
        String first,last,number;
        boolean isWoman;
        try {
            JSONObject user = response.getJSONObject("user");
            first = user.getString("first_name");
            last = user.getString("last_name");
            number = user.getString("phone");
            isWoman = user.getBoolean("is_woman");
            return new User(first,last,number,isWoman);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

}
