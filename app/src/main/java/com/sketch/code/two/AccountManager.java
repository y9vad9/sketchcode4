package com.sketch.code.two;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

public class AccountManager {
    Activity activity;
    static SharedPreferences data;
    static Account account;
    static AccountData accountData;
    int serverResponseCode;
    public AccountManager(Activity a) {
        activity = a;
        data = activity.getSharedPreferences("account", Activity.MODE_PRIVATE);
    }
    public void login(String login, String password, Account _account) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("email", login);
        hashMap.put("password", password);
        account = _account;
        AccountRequests.login(hashMap, getActivity());
    }
    public void register(String login, String password, String username, Account _account) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("email", login);
        hashMap.put("password", password);
        hashMap.put("nickname", username);
        account = _account;
        AccountRequests.register(hashMap, getActivity());
    }
    public void resend(String login, String password) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("email", login);
        hashMap.put("password", password);
        AccountRequests.login(hashMap, getActivity());
    }
    public void openProfile(){
        Intent intent = new Intent();
        intent.setClass(activity, ProfileActivity.class);
        activity.startActivity(intent);
    }
    public boolean isAuthorized() {
        if(data.getString("email", "").equals("")) {
            return false;
        } else {
            return true;
        }
    }
    public void getAccountData(AccountData accountData1) {
        accountData = accountData1;
        AccountRequests.loadAccoundData(activity);
    }
    public String getSavedProfileIconUrl() {
        return data.getString("icon_profile", "%default%");
    }
    public String getSavedUsername() {
        return data.getString("username", "");
    }
    public void logout() {
        data.edit().clear().apply();
    }
    public Activity getActivity(){
        return activity;
    }
    public interface Account {
        void onSuccess(int resultCode, String response);
        void onError(int resultCode, String response);
        void onConnectionError(String message);
    }
    public interface AccountData {
        void onDataReceived(HashMap<String, Object> _data);
        void onError(String message);
    }
    public void changePassword(final String newPassword, final UpdateListener updateListener) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("email", data.getString("email",""));
        map.put("password", data.getString("password",""));
        map.put("newPassword", newPassword);
        RequestNetwork requestNetwork = new RequestNetwork(getActivity());
        requestNetwork.setParams(map, RequestNetworkController.REQUEST_PARAM);
        requestNetwork.startRequestNetwork(RequestNetworkController.POST, "https://neonteam.net/sketchcode/account/changepassword.php", "", new RequestNetwork.RequestListener() {
            @Override
            public void onResponse(String tag, String response) {
                if(response.equals("Success")) {
                    data.edit().putString("password", newPassword).apply();
                    updateListener.onDataUpdated();
                } else {
                    Toast.makeText(getActivity(), response, Toast.LENGTH_SHORT).show();
                    updateListener.onError(response);
                }
            }

            @Override
            public void onErrorResponse(String tag, String message) {
                updateListener.onError(message);
            }
        });
    }
    public void changeName(final String newName, final UpdateListener updateListener) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("email", data.getString("email",""));
        map.put("password", data.getString("password",""));
        map.put("newname", newName);
        RequestNetwork requestNetwork = new RequestNetwork(getActivity());
        requestNetwork.setParams(map, RequestNetworkController.REQUEST_PARAM);
        requestNetwork.startRequestNetwork(RequestNetworkController.GET, "https://neonteam.net/sketchcode/account/rename.php", "", new RequestNetwork.RequestListener() {
            @Override
            public void onResponse(String tag, String response) {
                if(response.equals("Success")) {

                }
                Toast.makeText(getActivity(), response, Toast.LENGTH_SHORT).show();
                updateListener.onDataUpdated();
            }

            @Override
            public void onErrorResponse(String tag, String message) {
                updateListener.onError(message);
            }
        });
    }
    public void setStatus(String status, final UpdateListener updateListener) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("email", data.getString("email",""));
        map.put("password", data.getString("password",""));
        map.put("status", status);
        RequestNetwork requestNetwork = new RequestNetwork(getActivity());
        requestNetwork.setParams(map, RequestNetworkController.REQUEST_PARAM);
        requestNetwork.startRequestNetwork(RequestNetworkController.POST, "https://neonteam.net/sketchcode/account/setNewStatus.php", "", new RequestNetwork.RequestListener() {
            @Override
            public void onResponse(String tag, String response) {
                if(JsonUtil.getGsonDataKey(response, "responce").equals("Success")) {
                    updateListener.onDataUpdated();
                } else {
                    updateListener.onError(JsonUtil.getGsonDataKey(response, "responce"));
                }

            }

            @Override
            public void onErrorResponse(String tag, String message) {
                updateListener.onError(message);
            }
        });
    }
    public interface UpdateListener {
        void onDataUpdated();
        void onError(String message);
    }
    public String uploadFile(String imagePath) throws IOException
    {
        BufferedReader br = null;

        String fileName = imagePath;
        HttpURLConnection conn = null;
        DataOutputStream dos = null;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;
        File sourceFile = new File(imagePath);

        if (!sourceFile.isFile())
        {



            System.out.println("uploadFile: Source File not exist :" + imagePath);


        }
        else
        {
            try
            {


                FileInputStream fileInputStream = new FileInputStream(sourceFile);
                URL url = new URL("https://neonteam.net/sketchcode/res/upload.php");
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);

                conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setUseCaches(false);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                conn.setRequestProperty("uploaded_file", fileName);

                dos = new DataOutputStream(conn.getOutputStream());

                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\""
                        + fileName + "\"" + lineEnd);

                dos.writeBytes(lineEnd);


                bytesAvailable = fileInputStream.available();

                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                buffer = new byte[bufferSize];



                bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                while (bytesRead > 0)
                {

                    dos.write(buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                }


                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);


                int serverResponseCode = conn.getResponseCode();
                String serverResponseMessage = conn.getResponseMessage();

                System.out.println("uploadFile : HTTP Response is : "
                        + serverResponseMessage + ": " + serverResponseCode);

                if (200 <= conn.getResponseCode() && conn.getResponseCode() <= 299)
                {
                    br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                }
                else
                {
                    br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
                }



                fileInputStream.close();
                dos.flush();
                dos.close();

            }
            catch (MalformedURLException ex)
            {


                ex.printStackTrace();

                System.out.println("Upload file to server: error: " + ex.getMessage());
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }




        }
        System.out.println(br.read());
        return br.readLine();
    }

    public void changeProfileIcon(final String url, final ProfileIconListener profileIconListener) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("email", data.getString("email",""));
        map.put("password", data.getString("password",""));
        map.put("avatar", url);
        RequestNetwork requestNetwork = new RequestNetwork(getActivity());
        requestNetwork.setParams(map, RequestNetworkController.REQUEST_PARAM);
        requestNetwork.startRequestNetwork(RequestNetworkController.POST, "https://neonteam.net/sketchcode/account/changeprofileicon.php", "", new RequestNetwork.RequestListener() {
            @Override
            public void onResponse(String tag, String response) {
                profileIconListener.onLoaded(url, JsonUtil.getGsonDataKey(response, "responce"));

            }

            @Override
            public void onErrorResponse(String tag, String message) {
                profileIconListener.onError(message, url);
            }
        });
    }
    public interface ProfileIconListener {
        void onLoaded(String url, String message);
        void onError(String message, String url);
    }


}
