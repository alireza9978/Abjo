package coleo.com.abjo.data_base;

import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import coleo.com.abjo.activity.MainActivity;
import coleo.com.abjo.dialogs.ReportDialog;
import coleo.com.abjo.dialogs.SendJsonDialog;

import static coleo.com.abjo.constants.Constants.context;

public class locationRepository {

    private UserLocationDao userLocationDao;

    public locationRepository(TravelDataBase travelDataBase) {
        this.userLocationDao = travelDataBase.userDao();
    }

    public void insert(UserLocation location) {
        new insertAsyncTask(userLocationDao).execute(location);
    }

    public void nukeTable() {
        new nukeAsyncTask(userLocationDao).execute();
    }

    public void makeJsonAndSend() {
        new getAsyncTask(userLocationDao).execute();
    }

    private static class insertAsyncTask extends AsyncTask<UserLocation, Void, Void> {

        private UserLocationDao mAsyncTaskDao;

        insertAsyncTask(UserLocationDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final UserLocation... params) {
            if (params[0] != null)
                mAsyncTaskDao.insertAll(params[0]);
            return null;
        }
    }

    private static class nukeAsyncTask extends AsyncTask<Void, Void, Void> {

        private UserLocationDao mAsyncTaskDao;

        nukeAsyncTask(UserLocationDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            mAsyncTaskDao.nukeTable();
            return null;
        }
    }

    private static class getAsyncTask extends AsyncTask<Void, Void, JSONObject> {

        private UserLocationDao mAsyncTaskDao;

        getAsyncTask(UserLocationDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected JSONObject doInBackground(Void... voids) {
            return makeJson(mAsyncTaskDao.getAll());
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            super.onPostExecute(jsonObject);
            SendJsonDialog dialog = new SendJsonDialog(context, jsonObject.toString());
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            dialog.setCanceledOnTouchOutside(false);
            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    ReportDialog temp = new ReportDialog(context);
                    temp.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                    temp.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            ((MainActivity) context).backToMain();
                        }
                    });
                    temp.show();
                }
            });
            dialog.show();
        }

        private JSONObject makeJson(List<UserLocation> locations) {
            JSONObject object = new JSONObject();
            try {
                JSONArray array = new JSONArray();
                for (UserLocation location : locations) {
                    JSONObject locationJsonObject = new JSONObject();
                    locationJsonObject.put("lat", location.latitude);
                    locationJsonObject.put("lng", location.longitude);
                    locationJsonObject.put("time", location.time);
                    array.put(locationJsonObject);
                }
                object.put("locations", array);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.i("location rep", "makeJson: " + object.toString());
            return object;
        }

    }

}
