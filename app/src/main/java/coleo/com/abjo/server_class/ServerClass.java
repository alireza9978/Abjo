package coleo.com.abjo.server_class;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import coleo.com.abjo.activity.MainActivity;
import coleo.com.abjo.activity.login.CodeActivity;
import coleo.com.abjo.activity.login.Login;
import coleo.com.abjo.activity.login.SignUpActivity;
import coleo.com.abjo.constants.Constants;
import coleo.com.abjo.data_class.ActivityKind;
import coleo.com.abjo.data_class.DateAction;
import coleo.com.abjo.data_class.History;
import coleo.com.abjo.data_class.Introduce;
import coleo.com.abjo.data_class.LeaderBoardData;
import coleo.com.abjo.data_class.NewUserForServer;
import coleo.com.abjo.data_class.ProfileData;
import coleo.com.abjo.data_class.Transition;
import coleo.com.abjo.data_class.User;
import coleo.com.abjo.data_class.UserLevel;

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
        error.printStackTrace();
        if (error.networkResponse == null) {
            Toast.makeText(context, "اتصال اینترنت خود را بررسی کنید", Toast.LENGTH_SHORT).show();
        } else {
//            if (error.networkResponse.statusCode == 403) {
//                Constants.setToken(context, "");
//                Intent intent = new Intent(context, Splash.class);
//                context.startActivity(intent);
//                ((Activity) context).finish();
//                return;
//            }
            error.printStackTrace();
            Toast.makeText(context, getErrorMessage(error), Toast.LENGTH_SHORT).show();
        }
    }

    public static void checkPhone(final Context context, final String phone) {

        String url = Constants.URL_CHECK_PHONE;
        url += phone;
        url += "/";

        FirstObjectRequest jsonObjectRequest = new FirstObjectRequest
                (context, Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        ((Login) context).goCode(phone);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        ((Login) context).enable();
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

        StrImplRequest request = new StrImplRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject object = null;
                try {
                    object = new JSONObject(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                saveToken(context, object);
                if (StrImplRequest.isNewUser) {
                    ((CodeActivity) context).goSignUp();
                    ((CodeActivity) context).finish();
                } else {
                    ((CodeActivity) context).goMainPage();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ((CodeActivity) context).wrongCode();
                ServerClass.handleError(context, error);

            }
        });
        request.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS, 0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(context).addToRequestQueue(request);

    }

    public static void getProfile(final Context context) {

        String url = Constants.URL_GET_USER_PROFILE;

        ObjectRequest jsonObjectRequest = new ObjectRequest
                (context, Request.Method.GET, url, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                saveToken(context, response);
                                ((MainActivity) context).updateProfile(parseProfile(response));
                            }
                        }
                        , new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error != null) {
                            if (error.networkResponse != null) {
                                if (error.networkResponse.statusCode == 201) {
                                    String jsonString = new String(error.networkResponse.data);
                                    try {
                                        saveToken(context, new JSONObject(jsonString));
                                        ((CodeActivity) context).goSignUp();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                        ServerClass.handleError(context, error);
                    }
                }
                );

        MySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);

    }

    public static void makeNewUser(final Context context, final NewUserForServer user) {

        String url = Constants.URL_MAKE_USER;

        JSONObject mainJson = new JSONObject();
        JSONObject userJsonObject = new JSONObject();
        try {
            userJsonObject.put("first_name", user.getFirstName());
            userJsonObject.put("last_name", user.getLastName());
            userJsonObject.put("student_id", user.getStudentId());
            userJsonObject.put("is_woman", user.isWoman());
            userJsonObject.put("introduce_by", user.getIntroduceCode());
            userJsonObject.put("app_version", Constants.VERSION);
            userJsonObject.put("android_version", "" + Build.VERSION.SDK_INT);
            mainJson.put("user", userJsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ObjectRequest jsonObjectRequest = new ObjectRequest
                (context, Request.Method.POST, url, mainJson,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                saveToken(context, response);
                                ((SignUpActivity) context).goMain();
                            }
                        }
                        , new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        ((SignUpActivity) context).enable();
                        ServerClass.handleError(context, error);
                    }
                }
                );

        MySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);

    }

    private static ProfileData parseProfile(JSONObject response) {
        Log.i(TAG, "parseProfile: " + response.toString());
        int coins, hours;
        String note = " ";
        try {
            User user = parseUser(response, "user_data");
            try {
                note = response.getString("note");
            } catch (JSONException e) {
                Log.i(TAG, "parseProfile: note not found");
            }
            JSONObject user_data = response.getJSONObject("user_data");
            coins = user_data.getInt("coins");
            hours = user_data.getInt("hours");
            UserLevel level = parseLevel(user_data);
            return new ProfileData(user, coins, hours, level, note);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void getLeaderBoard(final Context context) {
        String url = Constants.URL_GET_LEADER_BOARD;

        ObjectRequest jsonObjectRequest = new ObjectRequest
                (context, Request.Method.GET, url, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.i(TAG, "getLeaderBoard: " + response.toString());
                                saveToken(context, response);
                                ArrayList<LeaderBoardData> arrayList = new ArrayList<>();
                                try {
                                    JSONArray users = response.getJSONArray("users");
                                    for (int i = 0; i < users.length(); i++) {
                                        LeaderBoardData temp = parseLeaderBoardUser(users.getJSONObject(i));
                                        if (temp != null)
                                            arrayList.add(temp);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                ((MainActivity) context).updateLeaderBoard(arrayList);
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

    public static void getHistory(final Context context, int page) {
        String url = Constants.URL_GET_HISTORY;
        url += page;
        url += "/";

        ObjectRequest jsonObjectRequest = new ObjectRequest
                (context, Request.Method.GET, url, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.i(TAG, "get history: " + response.toString());
                                saveToken(context, response);
                                ArrayList<DateAction> actions = new ArrayList<>();
                                try {
                                    JSONArray records = response.getJSONArray("user_records");
                                    for (int i = 0; i < records.length(); i++) {
                                        DateAction temp = historyParser(records.getJSONObject(i));
                                        if (temp != null)
                                            actions.add(temp);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                ((MainActivity) context).updateHistory(actions);
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

    private static DateAction historyParser(JSONObject object) {
        DateAction date = DateParser(object, "date_created");
        int coin = 0, point = 0;
        boolean work = false;
        try {
            //"user_spend"
            coin = object.getInt("coin");
            point = object.getInt("point");
            work = true;

            JSONObject spend = object.getJSONObject("user_spend");
            JSONObject store = spend.getJSONObject("store");
            String title = store.getString("title");
            return new Transition(date, title, coin);

        } catch (JSONException e) {
            e.printStackTrace();
            if (work) {
                try {
                    //"user_activity"
                    JSONObject information = object.getJSONObject("user_introduce_point");
                    return new Introduce(date, coin, point);
                } catch (Exception se) {
                    se.printStackTrace();
                    try {
                        //"user_activity"
                        JSONObject info = object.getJSONObject("user_activity");
                        JSONObject type = info.getJSONObject("activity_type");
                        int id = type.getInt("id");
                        ActivityKind kind;
                        if (id == 1) {
                            kind = ActivityKind.walk;
                        } else
                            kind = ActivityKind.bike;

                        int distance = info.getInt("distance");
                        return new History(date, coin, point, distance, kind);
                    } catch (Exception te) {
                        te.printStackTrace();
                    }
                }
            }
        }
        return null;
    }

    private static DateAction DateParser(JSONObject object, String key) {
        try {
            JSONObject date = object.getJSONObject(key);
            return new DateAction(date.getInt("day"), date.getInt("month"), date.getInt("year"),
                    date.getInt("hour"), date.getInt("minute"), date.getInt("second"),
                    date.getString("day_name"), date.getString("month_name"));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static LeaderBoardData parseLeaderBoardUser(JSONObject raw) {
        try {
            String first = raw.getString("first_name");
            String last = raw.getString("last_name");
            int point = raw.getInt("point");
            int rank = raw.getInt("rank");
            boolean is_mine = raw .getBoolean("is_you");
            User user = new User(first, last, "", false);
            return new LeaderBoardData(user, rank, point,is_mine,false);//todo blur
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static User parseUser(JSONObject response, String from) {
        Log.i(TAG, "parseUser: " + response.toString());
        String first, last, number;
        boolean isWoman;
        try {
            JSONObject user = response.getJSONObject(from);
            first = user.getString("first_name");
            last = user.getString("last_name");
            number = user.getString("phone");
//            isWoman = user.getBoolean("is_woman");
            isWoman = false;//todo fuckkkkkk
            return new User(first, last, number, isWoman);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static UserLevel parseLevel(JSONObject response) {
        return parseLevel(response, "user_level");
    }

    private static UserLevel parseLevel(JSONObject response, String from) {
        UserLevel level = new UserLevel();
        try {
            JSONObject data = response.getJSONObject(from);
            level.setPoint(data.getInt("user_point"));
            JSONObject user_level = data.getJSONObject("user_level");
            level.setLevelMaxPoint(user_level.getInt("capacity"));
            level.setLevel(user_level.getInt("number"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return level;
    }

    private static User parseUser(JSONObject response) {
        return parseUser(response, "user");
    }

    public static void openNewSession(final Context context, final boolean step, final ProfileData data) {
        String url = Constants.URL_OPEN_SESSION;

        FirstObjectRequest jsonObjectRequest = new FirstObjectRequest
                (context, Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        long id = 1512;//todo get this from server
                        Constants.setSession(id);
                        Constants.saveUserSession(data);
                        ((MainActivity) context).showAfterStart(step,data);
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

    public static class StrImplRequest extends StringRequest {

        static boolean isNewUser = false;

        public StrImplRequest(int method, String url, Response.Listener<String> listener, @Nullable Response.ErrorListener errorListener) {
            super(method, url, listener, errorListener);
        }

        @Override
        public Map<String, String> getHeaders() throws AuthFailureError {
            HashMap<String, String> headers = new HashMap<String, String>();
            headers.put("Content-Type", "application/json");
            headers.put("role", "user");
            return headers;
        }

        @Override
        protected Response<String> parseNetworkResponse(NetworkResponse response) {
            int temp = response.statusCode;
            Log.i(TAG, "parseNetworkResponse: ");
            if (temp == 201 || temp == 202) {
                isNewUser = true;
            }
            return super.parseNetworkResponse(response);
        }


    }

    public static void sendActivityData(JSONObject data,Context context){

        if (!isNetworkConnected(context)) {
            ((MainActivity) context).restart();
            return;
        } else {

        }

        //todo send req
        //on 200 show dialog & delete that file
        //on error save json
        //at end check remain count


    }


}
