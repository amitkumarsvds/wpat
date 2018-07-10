package services.networkmanager;

import android.util.Log;

import org.json.JSONException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
/*import org.json.JSONObject;
import java.util.HashMap;
import configuration.configuration;
import models.errormanager.ErrorModel;
import models.usermanager.IUserManagerCallback;
import models.usermanager.UMUserManager;
import models.usermanager.UMUserSignInDetails;
import services.JSONManager;
import services.PrefManager;*/

public class NetworkManager implements INetworkCallback {
    private static final String TAG = "NetworkManager";
    private NWHTTPRequestObject currentRequestObject;
    private INetworkCallback currentCallbackObject;

    public void execute(NWHTTPRequestObject nwhttpRequestObject){
        HttpRequestManager httpRequestManager = HttpRequestManager.getInstance(this);
        currentRequestObject = new NWHTTPRequestObject();
        currentCallbackObject = nwhttpRequestObject.getCallbackObject();
        currentRequestObject.assign(nwhttpRequestObject);
        currentRequestObject.setCallbackObject(NetworkManager.this);
        httpRequestManager.execute(currentRequestObject);
    }

    /*private void refreshAccessToken() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    UMUserManager umUserManager = new UMUserManager();
                    UMUserSignInDetails umUserSignInDetails = new UMUserSignInDetails();
                    PrefManager prefManager = PrefManager.getPreferenceManager();
                    JSONObject jsonObject = new JSONObject(prefManager.getStringValueForKey("userSignInDetails"));

                    int idp = JSONManager.getIntFromJson(jsonObject, "idp");
                    if(idp == configuration.LoginMode.SSO_FACEBOOK) {
                        umUserSignInDetails.setUserName(JSONManager.getStringFromJson(jsonObject,"userName"));
                        umUserSignInDetails.setAuthCode(JSONManager.getStringFromJson(jsonObject,"authCode"));
                        umUserSignInDetails.setIdp(configuration.LoginMode.SSO_FACEBOOK);
                        umUserSignInDetails.setAppName(JSONManager.getStringFromJson(jsonObject,"appName"));
                    } else {
                        umUserSignInDetails.setUserName(JSONManager.getStringFromJson(jsonObject, "userName"));
                        umUserSignInDetails.setPassword(JSONManager.getStringFromJson(jsonObject, "password"));
                    }
                    umUserManager.StartSignInWith(NetworkManager.this, umUserSignInDetails);
                }catch (JSONException exception){
                    Log.d("NetworkManager", exception.getMessage());
                }catch (NullPointerException exception){
                    Log.d("NetworkManager", exception.getMessage());
                    currentCallbackObject.OnNetworkFailureCallback("sso_authorization_failed");
                }
            }
        }).start();
    }*/

    /*@Override
    public void UserManagerSignUpSuccessCallback(String successData) {

    }*/

    /*@Override
    public void UserManagerSignInSuccessCallback(String successData) {
        HttpRequestManager httpRequestManager = HttpRequestManager.getInstance(this);
        PrefManager prefManager = PrefManager.getPreferenceManager();
        HashMap<String,String> accessTokenMap = currentRequestObject.getParameters();
        accessTokenMap.put(configuration.HTTPHeaderConfig.HEADER_AUTHORIZATION,"Bearer "+prefManager.getStringValueForKey("access_token"));
        currentRequestObject.setCallbackObject(currentCallbackObject);
        httpRequestManager.execute(currentRequestObject);
    }

    @Override
    public void UserManagerFailureCallback(String failureData) {

    }*/

    @Override
    public void OnNetworkSuccessCallback(NWHTTPResponseObject httpResponse) throws JSONException {

        if(httpResponse.getResponseObject() == null){
            System.out.println("success........result.........");

            if(HttpRequestManager.isConnectionRefused)
                currentCallbackObject.OnNetworkFailureCallback("webservice_unavailable");
            else
                currentCallbackObject.OnNetworkFailureCallback("network_unavailable");
        } else {
            System.out.println("success........result.........");

            currentCallbackObject.OnNetworkSuccessCallback(httpResponse);
      }
        /*else if(httpResponse.getResponseObject().contains("Make sure your have given the correct access token")){
            //refreshAccessToken();
        }  else if(responseHasError(JSONManager.getJSONObjectFromJson(
                (new JSONObject(httpResponse.getResponseObject())),"error"))) {
            String errorKey = JSONManager.getStringFromJson(JSONManager.getJSONObjectFromJson((new JSONObject(httpResponse.getResponseObject())),"error"), "errorKey");
            currentCallbackObject.OnNetworkFailureCallback(errorKey);
        }*/
        /*else {
            Document document = getDomElement(httpResponse.getResponseObject());
            Log.d(TAG, document.getDocumentURI());
            currentCallbackObject.OnNetworkSuccessCallback(httpResponse);
        }*/
    }

   /* private boolean responseContainsError(String responseObject) {
        try {
            JSONObject errorJson = new JSONObject(responseObject);
            if(errorJson != null){
                String error = JSONManager.getStringFromJson(errorJson, "error");
                if( error != null && !error.equals("")){
                    return true;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }*/

    @Override
    public void OnNetworkFailureCallback(String failureData) {
        currentCallbackObject.OnNetworkFailureCallback(failureData);
        System.out.println("failed...........result......");
    }

    public Document getDomElement(String xml){
        Document doc = null;
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {

            DocumentBuilder db = dbf.newDocumentBuilder();

            InputSource is = new InputSource();
            is.setCharacterStream(new StringReader(xml));
            doc = db.parse(is);

        } catch (ParserConfigurationException e) {
            Log.e("Error: ", e.getMessage());
            return null;
        } catch (SAXException e) {
            Log.e("Error: ", e.getMessage());
            return null;
        } catch (IOException e) {
            Log.e("Error: ", e.getMessage());
            return null;
        }
        // return DOM
        return doc;
    }

    public String getValue(Element item, String str) {
        NodeList n = item.getElementsByTagName(str);
        return this.getElementValue(n.item(0));
    }

    public final String getElementValue( Node elem ) {
        Node child;
        if( elem != null){
            if (elem.hasChildNodes()){
                for( child = elem.getFirstChild(); child != null; child = child.getNextSibling() ){
                    if( child.getNodeType() == Node.TEXT_NODE  ){
                        return child.getNodeValue();
                    }
                }
            }
        }
        return "";
    }

    /*private boolean responseHasError(JSONObject errorObject)throws JSONException {
        boolean responseError = false;
        if(errorObject != null) {
            ErrorModel errorModel = new ErrorModel();
            errorModel.updateFromJsonData(errorObject);

            if (!errorModel.getErrorKey().equals(""))
                responseError = true;
        }
        return responseError;
    }*/
}
