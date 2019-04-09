package coleo.com.abjo.server_class;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.Nullable;
import coleo.com.abjo.constants.Constants;

public class ObjectRequest extends JsonObjectRequest {

    private final Context context;

    public ObjectRequest(Context context, int method, String url, @Nullable JSONObject jsonRequest,
                         Response.Listener<JSONObject> listener,
                         @Nullable Response.ErrorListener errorListener) {
        super(method, url, jsonRequest, listener, errorListener);
        this.context = context;
    }

    @Override
    public Map getHeaders() throws AuthFailureError {
        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("Content-Type", "application/json");
        headers.put("token", Constants.getToken(context));
        return headers;
    }

}
