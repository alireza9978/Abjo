package coleo.com.abjo.server_class;

import android.content.Context;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import coleo.com.abjo.constants.Constants;

public class FirstObjectRequest extends JsonObjectRequest {

    private final Context context;

    public FirstObjectRequest(Context context, int method, String url, @Nullable JSONObject jsonRequest,
                              Response.Listener<JSONObject> listener,
                              @Nullable Response.ErrorListener errorListener) {
        super(method, url, jsonRequest, listener, errorListener);
        this.context = context;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("Content-Type", "application/json");
        headers.put("role", "user");
        return headers;
    }

}
