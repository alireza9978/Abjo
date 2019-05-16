package coleo.com.abjo.data_base;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Environment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import coleo.com.abjo.service.SaverReceiver;

import static coleo.com.abjo.constants.Constants.context;

public class locationRepository {

    private UserLocationDao userLocationDao;
    private PauseDao pauseDao;
    private TravelDataBase travelDataBase;
    private static locationRepository repository;

    public void close() {
        travelDataBase.close();
    }

    public void setUserLocationDao(UserLocationDao userLocationDao) {
        this.userLocationDao = userLocationDao;
    }
    public void setActionDao(PauseDao userLocationDao) {
        this.pauseDao = userLocationDao;
    }

    private locationRepository(TravelDataBase travelDataBase) {
        this.travelDataBase = travelDataBase;
        this.userLocationDao = travelDataBase.userDao();
    }

    public static locationRepository get(TravelDataBase travelDataBase) {
        if (repository != null) {
            return repository;
        } else {
            if (travelDataBase != null) {
                repository = new locationRepository(travelDataBase);
                return repository;
            }
            return null;
        }
    }

    public void insert(UserLocation[] location) {
        new insertAsyncTask(userLocationDao).execute(location);
    }

    public void insert(Action action) {
        new insertPauseAsyncTask(pauseDao).execute(action);
    }

    public void nukeTable() {
        new nukePauseAsyncTask(pauseDao).execute();
        new nukeAsyncTask(userLocationDao).execute();
    }

    public void makeJsonAndSend() {
        new getAsyncTask(userLocationDao, travelDataBase, pauseDao).execute();
    }

    private static class insertPauseAsyncTask extends AsyncTask<Action, Void, Void> {

        private PauseDao mAsyncTaskDao;

        insertPauseAsyncTask(PauseDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Action... params) {
            mAsyncTaskDao.insertAll(params[0]);
            return null;
        }
    }

    private static class insertAsyncTask extends AsyncTask<UserLocation[], Void, Void> {

        private UserLocationDao mAsyncTaskDao;

        insertAsyncTask(UserLocationDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final UserLocation[]... params) {
            UserLocation[] temp = params[0];
            for (int i = 0; i < 3; i++) {
                mAsyncTaskDao.insertAll(temp[i]);
            }
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

    private static class nukePauseAsyncTask extends AsyncTask<Void, Void, Void> {

        private PauseDao mAsyncTaskDao;

        nukePauseAsyncTask(PauseDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            mAsyncTaskDao.nukeTable();
            return null;
        }
    }

    private static class getAsyncTask extends AsyncTask<Void, Void, JSONObject[]> {

        private UserLocationDao mAsyncTaskDao;
        private TravelDataBase travelDataBase;
        private PauseDao pauseDao;

        getAsyncTask(UserLocationDao dao, TravelDataBase db, PauseDao pauseDao) {
            this.pauseDao = pauseDao;
            travelDataBase = db;
            mAsyncTaskDao = dao;
        }

        @Override
        protected JSONObject[] doInBackground(Void... voids) {
            return makeJson(mAsyncTaskDao.getAll(), pauseDao.getAll());
        }

        @Override
        protected void onPostExecute(JSONObject[] jsonObject) {
            super.onPostExecute(jsonObject);
            travelDataBase.close();
            Intent intent = new Intent(context, SaverReceiver.class);
            context.stopService(intent);
            //todo save jsons in file
//            SendJsonDialog dialog = new SendJsonDialog(context, jsonObject.toString());
//            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
//            dialog.setCanceledOnTouchOutside(false);
//            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
//                @Override
//                public void onDismiss(DialogInterface dialog) {
//                    ReportDialog temp = new ReportDialog(context);
//                    temp.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
//                    temp.setOnDismissListener(new DialogInterface.OnDismissListener() {
//                        @Override
//                        public void onDismiss(DialogInterface dialog) {
//                            ((MainActivity) context).backToMain();
//                        }
//                    });
//                    temp.show();
//                }
//            });
//            dialog.show();
        }

        private JSONObject[] makeJson(List<UserLocation> locations, List<Action> actions) {
            ArrayList<JSONObject> arrayList = new ArrayList<>();
            JSONArray pauseObject = new JSONArray();
            try {
                for (Action p : actions) {
                    JSONObject temp = new JSONObject();
                    temp.put("number", p.number);
                    temp.put("time", p.time);
                    temp.put("action", p.action);
                    pauseObject.put(temp);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            onLoc:
            for (UserLocation location : locations) {
                JSONObject locationJsonObject = new JSONObject();
                try {
                    locationJsonObject.put("lat", location.latitude);
                    locationJsonObject.put("lng", location.longitude);
                    locationJsonObject.put("acc", location.accuracy);
                    locationJsonObject.put("time", location.time);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                for (JSONObject object : arrayList) {
                    try {
                        if (object.getInt("method") == location.method) {
                            object.getJSONArray("locations").put(locationJsonObject);
                            continue onLoc;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                JSONObject method = new JSONObject();
                try {
                    method.put("method", location.method);
                    JSONArray temp = new JSONArray();
                    method.put("locations", temp);
                    arrayList.add(method);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            for (JSONObject object : arrayList) {
                try {
                    object.put("actions", pauseObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            writeToFile(arrayList, context);
            JSONObject[] tempArray = new JSONObject[arrayList.size()];
            int i = 0;
            for (JSONObject object : arrayList) {
                tempArray[i] = object;
                i++;
            }
            return tempArray;
        }

        private void writeToFile(ArrayList<JSONObject> data, Context context) {
            for (JSONObject temp : data) {
                try {
                    File path = Environment.getExternalStoragePublicDirectory(
                            Environment.DIRECTORY_DOWNLOADS);
                    File myFile = new File(path, "Location" + System.currentTimeMillis() + ".txt");
                    FileOutputStream fOut = new FileOutputStream(myFile, true);
                    OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
                    myOutWriter.append(temp.toString());
                    myOutWriter.close();
                    fOut.close();
                } catch (java.io.IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

}
