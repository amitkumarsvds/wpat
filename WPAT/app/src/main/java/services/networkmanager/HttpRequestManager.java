package services.networkmanager;

import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;


import org.apache.http.client.HttpClient;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
/*import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.ksoap2.transport.HttpsTransportSE;
import org.ksoap2.transport.KeepAliveHttpsTransportSE;*/

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

/*import services.CustomSSLSocketFactory;
import services.CustomX509TrustManager;
import configuration.configuration;*/

/**
 * Provides API's for invoking HttpRequest and returns the response from registered callbacks.
 * HttpRequestManager is a AsyncTask, that means all the tasks execute in background and
 * multiple instances execute in a seperate thread and does not conflict with other HttpRequests.
 */
public class HttpRequestManager extends AsyncTask<NWHTTPRequestObject, Integer, NWHTTPResponseObject> {
    private final String TAG = getClass().getSimpleName();

    private static Dictionary<INetworkCallback, INetworkCallback> NetworkCallbackDictionary;
    private static HttpRequestManager ourInstance;
    public static boolean isConnectionRefused = false;
    private int HttpResult;

    public static HttpRequestManager getInstance(INetworkCallback networkCallback) {
        HttpRequestManager ourInstance = new HttpRequestManager();
        if(NetworkCallbackDictionary.get(networkCallback) == null)
            NetworkCallbackDictionary.put(networkCallback, networkCallback);
        return ourInstance;
    }

    private HttpRequestManager() {
        NetworkCallbackDictionary = new Dictionary<INetworkCallback, INetworkCallback>() {
            @Override
            public Enumeration<INetworkCallback> elements() {
                return null;
            }

            @Override
            public INetworkCallback get(Object o) {
                return null;
            }

            @Override
            public boolean isEmpty() {
                return false;
            }

            @Override
            public Enumeration<INetworkCallback> keys() {
                return null;
            }

            @Override
            public INetworkCallback put(INetworkCallback iNetworkCallback, INetworkCallback oNetworkCallback) {
                return null;
            }

            @Override
            public INetworkCallback remove(Object o) {
                return null;
            }

            @Override
            public int size() {
                return 0;
            }
        };
    }

    public NWHTTPResponseObject getHttpURLResponse(NWHTTPRequestObject requestObject)
            throws IOException, KeyManagementException,
            URISyntaxException, KeyStoreException, UnrecoverableKeyException {
        NWHTTPResponseObject nwhttpResponseObject = new NWHTTPResponseObject();
        URL object;
        try {
            object=new URL(requestObject.getEndPointURL());
            Log.d("URL", requestObject.getEndPointURL());
            if(object.getProtocol().equals("http")){

                try{


                    Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress( object.getHost(), object.getPort()));

                    HttpURLConnection connection = (HttpURLConnection) object.openConnection(proxy);
                    connection.setConnectTimeout(60000);
                    //connection.setReadTimeout(configuration.ApplicationConfig.CONNECTION_TIMEOUT);

                    connection.setRequestMethod(requestObject.getRequestMethod());
                    if(!requestObject.getRequestMethod().equals("GET")){
                        connection.setDoInput(true);
                        connection.setDoOutput(true);
                    }
                    if(requestObject.getParameters() != null) {
                        Iterator entries = requestObject.getParameters().entrySet().iterator();
                        while (entries.hasNext()) {
                            Map.Entry entry = (Map.Entry) entries.next();
                            String key = (String) entry.getKey();
                            String value = (String) entry.getValue();
                            connection.setRequestProperty(key, value);
                        }
                    }

                    String jsonString = requestObject.getPayload();
                    Log.d(TAG,"PayLoad Data : "+jsonString);
                    if(!requestObject.getRequestMethod().equals("GET")) {
                        OutputStream wr = connection.getOutputStream();
                        if(jsonString.length() > 1)
                            wr.write(jsonString.getBytes());
                        wr.flush();
                        wr.close();
                    }

                    //display what returns the POST request
                    InputStream inputStream;
                    StringBuilder sb = new StringBuilder();
                    HttpResult = connection.getResponseCode();

                    switch (HttpResult) {
                        case 200:
                        case 201:
                            inputStream = connection.getInputStream();
                            connection.getHeaderFields();
                            break;
                        default:
                            String urlResponse = "Invalid Response";
                            inputStream = connection.getErrorStream();
                    }
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    String temp, responses = "";
                    while ((temp = bufferedReader.readLine()) != null) {
                        sb.append(temp + "\n");
                    }

                    connection.disconnect();
                    nwhttpResponseObject.setResponseObject(sb.toString());
                    HashMap<String,String> responseHeaders = new HashMap<>();
                    for(int i=0;;i++) {
                        String key = connection.getHeaderFieldKey(i);
                        if(key == null){
                            break;
                        }
                        String value = connection.getHeaderField(key);
                        responseHeaders.put(key,value);
                    }
                    nwhttpResponseObject.setResponseHeader(responseHeaders);
                }catch (IOException ioException){
                    if(ioException.toString().contains("ECONNREFUSED"))
                        isConnectionRefused = true;
                    Log.d(TAG, ioException.toString());
                }catch (Exception e) {
                    Log.d(TAG, e.toString());
                    //requestObject.getCallbackObject().OnNetworkFailureCallback("");
                }
            }else {


                try {
                    SSLContext ctx = SSLContext.getInstance("TLS");
                    ctx.init(null, new TrustManager[]{new CustomX509TrustManager()},
                            new SecureRandom());
                    HttpClient client = new DefaultHttpClient();
                    SSLSocketFactory ssf = new CustomSSLSocketFactory(ctx);
                    ssf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
                    ClientConnectionManager ccm = client.getConnectionManager();
                    SchemeRegistry sr = ccm.getSchemeRegistry();
                    sr.register(new Scheme("https", ssf, 443));
                    HttpsURLConnection con = (HttpsURLConnection) object.openConnection();

                    con.setConnectTimeout(50000);
                    con.setReadTimeout(15000);
                    con.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
                    con.setSSLSocketFactory(ctx.getSocketFactory());

                    if(!requestObject.getRequestMethod().equals("GET")){
                        con.setDoInput(true);
                        con.setDoOutput(true);
                    }

                    if(requestObject.getParameters() != null) {
                        Iterator entries = requestObject.getParameters().entrySet().iterator();
                        while (entries.hasNext()) {
                            Map.Entry entry = (Map.Entry) entries.next();
                            String key = (String) entry.getKey();
                            String value = (String) entry.getValue();
                            con.setRequestProperty(key, value);
                        }
                    }

                    con.setRequestProperty("Accept-Language", Locale.getDefault().getLanguage());

                    //PATCH request method workaround for api<=19
                    if(requestObject.getRequestMethod().equals("PATCH") && Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
                        con.setRequestProperty("X-HTTP-Method-Override", "PATCH");
                        con.setRequestMethod("POST");
                    }else {
                        con.setRequestMethod(requestObject.getRequestMethod());
                    }
                    String jsonString = requestObject.getPayload();
                    Log.d(TAG,"PayLoad Data : "+jsonString);
                    if(!requestObject.getRequestMethod().equals("GET")) {
                        OutputStream wr = con.getOutputStream();
                        if(jsonString.length() > 1)
                            wr.write(jsonString.getBytes());
                        wr.flush();
                        wr.close();
                    }

                    //display what returns the POST request
                    InputStream inputStream;
                    StringBuilder sb = new StringBuilder();
                    HttpResult = con.getResponseCode();
                    Log.d("code----------", ""+HttpResult);

                    switch (HttpResult) {
                        case 200:
                        case 201:
                            requestXml();
                            inputStream = con.getInputStream();
                            con.getHeaderFields();
                            break;
                        case 401:
                        case 500:
                        default:
                            String urlResponse = "Invalid Response";
                            inputStream = con.getErrorStream();
                    }
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    String temp, responses = "";
                    while ((temp = bufferedReader.readLine()) != null) {
                        sb.append(temp + "\n");
                    }
                    Log.d(TAG, sb.toString());
                    con.disconnect();
                    nwhttpResponseObject.setResponseObject(sb.toString());
                    HashMap<String,String> responseHeaders = new HashMap<>();
                    for(int i=0;;i++) {
                        String key = con.getHeaderFieldKey(i);
                        if(key == null){
                            break;
                        }
                        String value = con.getHeaderField(key);
                        responseHeaders.put(key,value);
                    }
                    nwhttpResponseObject.setResponseHeader(responseHeaders);

                }catch (IOException ioException){
                    if(ioException.toString().contains("ECONNREFUSED"))
                        isConnectionRefused = true;
                    Log.d(TAG, ioException.toString());
                }catch (KeyManagementException keyManagementException) {
                    requestObject.getCallbackObject().OnNetworkFailureCallback("");
                }catch (KeyStoreException keyStoreException) {
                    requestObject.getCallbackObject().OnNetworkFailureCallback("");
                }catch (UnrecoverableKeyException unrecoverableKeyException) {
                    requestObject.getCallbackObject().OnNetworkFailureCallback("");
                }catch (Exception e){
                    Log.d(TAG, e.toString());
                }
            }

        }catch (MalformedURLException e){
            e.printStackTrace();
           // requestObject.getCallbackObject().OnNetworkFailureCallback("");
        }


        return nwhttpResponseObject;

    }

    private void requestXml() {
        /*try {
            String NAMESPACE = "http://tempuri.org/imports";
            String METHOD_NAME = "soapHotlistCard";
            String SOAP_ACTION = NAMESPACE+METHOD_NAME;
            String URL = "https://172.16.41.8/services/application/frontoffice/HotlistService.svc";

            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

            //DeviceInfo deviceInfo = new DeviceInfo(1,12,123,"326452734",1,1);
            CardHotlistDetails details = new CardHotlistDetails();
            details.setAgentId("1");
            details.setCardSerialNumber("576672311");
            details.setDateTime(123474844);
            details.setDeviceId(4545);
            details.setDeviceType(5);
            details.setDeviceSerialNumber("765658");
            details.setVersion(1);
            details.setReason("no");

            PropertyInfo pi = new PropertyInfo();
            pi.setNamespace("urn:hotlistservice");
            pi.setName("details");
            pi.setValue(details);
            pi.setType(CardHotlistDetails.class);
            request.addProperty(pi);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.addMapping("urn:hotlistservice","details",new CardHotlistDetails().getClass());
            envelope.setOutputSoapObject(request);
            Log.d("http", request.toString());
            FakeX509TrustManager.allowAllSSL();
            HttpsTransportSE httpsTransportSE = new HttpsTransportSE
                    ("172.16.41.8",443,"/services/application/frontoffice/HotlistService.svc",60000);
            httpsTransportSE.debug = true;
            httpsTransportSE.call(SOAP_ACTION, envelope);

            *//*HttpTransportSE androidHttpTransport = new HttpTransportSE(URL,
                    60000);
            androidHttpTransport.call(SOAP_ACTION, envelope);*//*

            SoapObject so = (SoapObject) envelope.getResponse();
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }*/
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
    }

    @Override
    protected void onPostExecute(NWHTTPResponseObject httpResponse) {
        super.onPostExecute(httpResponse);

        if(httpResponse != null){
            if(HttpResult == 500) {
                httpResponse.getNwCallbackObject().OnNetworkFailureCallback("internal_server_error");
                return;
            }else if(HttpResult == 400){
                httpResponse.getNwCallbackObject().OnNetworkFailureCallback("bad_req");
                return;
            }
            try {
                httpResponse.getNwCallbackObject().OnNetworkSuccessCallback(httpResponse);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else {
            httpResponse.getNwCallbackObject().OnNetworkFailureCallback("network_unavailable");
        }
    }

    @Override
    protected void onCancelled(NWHTTPResponseObject httpResponse) {
        super.onCancelled(httpResponse);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }

    /**
     doInBackgroud consumes the webservices and other network related URLS.
     Takes NWHTTPRequestObject instances containing webmethod, endpoint with URL with URL Parameters if required
     and body.
     */
    @Override
    protected NWHTTPResponseObject doInBackground(NWHTTPRequestObject... nwhttpRequestObjects) {

        try {
            NWHTTPResponseObject nwhttpResponseObject = getHttpURLResponse(nwhttpRequestObjects[0]);
            if(nwhttpResponseObject != null)
                nwhttpResponseObject.setNwCallbackObject(nwhttpRequestObjects[0].getCallbackObject());
            return  nwhttpResponseObject;
        }catch (IOException e) {
            Log.d(TAG, e.toString());
        } catch (KeyManagementException e) {
            Log.d(TAG, e.toString());
        } catch (URISyntaxException e) {
            Log.d(TAG, e.toString());
        } catch (KeyStoreException e) {
            Log.d(TAG, e.toString());
        } catch (UnrecoverableKeyException e) {
            Log.d(TAG, e.toString());
        }
        return  null;
    }


}

