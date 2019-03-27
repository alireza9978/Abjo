package coleo.com.abjo.data_base;

import android.content.Context;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import coleo.com.abjo.constants.Constants;

public class locationRepository {

    private UserLocationDao userLocationDao;

    public locationRepository(TravelDataBase travelDataBase) {
        this.userLocationDao = travelDataBase.userDao();
    }

    public void insert(UserLocation location) {
        new insertAsyncTask(userLocationDao).execute(location);
    }

    public void showSize(Context context) {
        new getAsyncTask(userLocationDao, context).execute();
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

    private static class getAsyncTask extends AsyncTask<Void, Void, Integer> {

        private UserLocationDao mAsyncTaskDao;
        private Context context;

        getAsyncTask(UserLocationDao dao, Context context) {
            mAsyncTaskDao = dao;
            this.context = context;
        }

        @Override
        protected Integer doInBackground(Void... voids) {
            return makeJson(mAsyncTaskDao.getAll()).toString().length();
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            Constants.start_stop.setText(" = " + integer);
        }

        private JSONObject makeJson(List<UserLocation> locations) {
            JSONObject object = new JSONObject();
            try {
                JSONArray array = new JSONArray();
                for (UserLocation location : locations) {
                    JSONObject locationJsonObject = new JSONObject();
                    locationJsonObject.put("lat", location.latitude);
                    locationJsonObject.put("lng", location.longitude);
                    array.put(locationJsonObject);
                }
                object.put("locations", array);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return object;
        }

    }

}
