package services.networkmanager;

import java.util.HashMap;

/**
 *NWHTTPRequest object encapsulates information related to Http Request.
 */

public class NWHTTPRequestObject {
    private  String EndPointURL;
    private String EndPoint;
    private String RequestMethod;
    private String ContentType;
    private HashMap<String, String> Parameters;
    private String Payload;
    private INetworkCallback CallbackObject;

    public  NWHTTPRequestObject() {

    }

    public String getEndPointURL() {
        return EndPointURL;
    }

    public String getEndPoint() {
        return EndPoint;
    }

    public String getRequestMethod() {
        return RequestMethod;
    }

    public String getContentType() {
        return ContentType;
    }

    public HashMap<String, String> getParameters() {
        return Parameters;
    }

    public String getPayload() {
        return Payload;
    }

    public INetworkCallback getCallbackObject() {
        return CallbackObject;
    }

    public void setParameters(HashMap<String, String> parameters) {
        Parameters = parameters;
    }

    public void setEndPointURL(String endPointURL) {
        EndPointURL = endPointURL;
    }

    public void setEndPoint(String endPoint) {
        EndPoint = endPoint;
    }

    public void setRequestMethod(String requestMethod) {
        RequestMethod = requestMethod;
    }

    public void setContentType(String contentType) {
        ContentType = contentType;
    }

    public void setPayload(String payloadData) {
        Payload = payloadData;
    }

    public void setCallbackObject(INetworkCallback callbackObject) {
        CallbackObject = callbackObject;
    }

    public void assign(NWHTTPRequestObject requestObject){
        this.EndPointURL = requestObject.getEndPointURL();
        this.EndPoint = requestObject.getEndPoint();
        this.RequestMethod = requestObject.getRequestMethod();
        this.ContentType = requestObject.getContentType();
        this.Parameters = new HashMap<>();
        this.Parameters = (HashMap<String, String>) requestObject.getParameters().clone();
        this.Payload = requestObject.getPayload();
        this.CallbackObject = requestObject.getCallbackObject();
    }

    public void reset() {
        this.EndPointURL = "";
        this.EndPoint = "";
        this.RequestMethod = "";
        this.ContentType = "";
        this.Parameters.clear();
        this.Payload = "";
    }
}
