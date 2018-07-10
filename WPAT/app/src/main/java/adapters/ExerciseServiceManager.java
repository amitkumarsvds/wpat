package adapters;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONException;

import java.util.HashMap;

import configuration.Configuration;
import models.exercisemodels.ExerciseManagerCallBack;
import models.exercisemodels.Proficiency;
import services.networkmanager.INetworkCallback;
import services.networkmanager.NWHTTPRequestObject;
import services.networkmanager.NWHTTPResponseObject;
import services.networkmanager.NetworkManager;

/**
 * Manager to fetch data from API
 */
public class ExerciseServiceManager implements INetworkCallback {

    private final String LOG_FILE_NAME = getClass().getSimpleName();
    private NWHTTPRequestObject mNwhttpRequestObject;
    private ExerciseManagerCallBack mCallbackObject;
    public static String mGetTitle;

    public ExerciseServiceManager(Context context) {
    }

    /**
     * service to get response of an API
     * @param resp resp
     */
    public void getServiceResponse(String resp) {
        try {
            mNwhttpRequestObject = null;
            mNwhttpRequestObject = new NWHTTPRequestObject();
            HashMap<String,String> accessTokenMap = new HashMap<>();
            accessTokenMap.put("Accept", "application/json");
            accessTokenMap.put("Content-Type", "application/json");
            //accessTokenMap.put(configuration.HTTPHeaderConfig.HEADER_ACCEPT,configuration.HTTPHeaderConfig.HEADER_ACCEPT_JSON_VALUE);
            //accessTokenMap.put(configuration.HTTPHeaderConfig.HEADER_AUTHORIZATION,"Bearer "+prefManager.getStringValueForKey("access_token"));
            String endPointURL = Configuration.EndPoints.URL_ASSIGNMENT;
            mNwhttpRequestObject.setEndPointURL(endPointURL );
            mNwhttpRequestObject.setRequestMethod("GET");
            mNwhttpRequestObject.setParameters(accessTokenMap);
            mNwhttpRequestObject.setPayload("");
            mNwhttpRequestObject.setCallbackObject(ExerciseServiceManager.this);
            NetworkManager networkManager = new NetworkManager();
            networkManager.execute(mNwhttpRequestObject);
        }catch(Exception e){
            Log.d(LOG_FILE_NAME, e.toString());
        }
    }

    @Override
    public void OnNetworkSuccessCallback(NWHTTPResponseObject httpResponse) throws JSONException {

        if(httpResponse == null){
            mCallbackObject.UDManagerFailureCallback("check connectivity");
        }else {
            try  {
                if (httpResponse.getResponseObject()== null){
                        mCallbackObject.UDManagerFailureCallback("No data available");
                    return;
                }
                switch (mNwhttpRequestObject.getEndPointURL()) {
                    case Configuration.EndPoints.URL_ASSIGNMENT:
                        Gson gson = new Gson();
                        Proficiency responseModel = gson.fromJson(httpResponse.getResponseObject(),
                                Proficiency.class);
                        mGetTitle=responseModel.getTitle();
                        mCallbackObject.UDManagerSuccessCallback(responseModel.getRows());
                        break;
                    default:
                        break;
                }
            }catch(Exception exception){
                mCallbackObject.UDManagerFailureCallback("invalid_req_resp");
            }
        }
    }

    @Override
    public void OnNetworkFailureCallback(String failureData) {
        mCallbackObject.UDManagerFailureCallback(failureData);
    }

    /**
     * assigning the callback to activity
     * @param callback callback
     */
    public void startConfiguration(ExerciseManagerCallBack callback) {
        mCallbackObject = callback;
    }
}
