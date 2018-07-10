package services.networkmanager;

import java.util.HashMap;

/**
 * NWHTTPResponseObject encapsulates information related to Http Response.
 */

public class NWHTTPResponseObject {
    private String ResponseObject;
    private HashMap<String,String> responseHeader;
    private INetworkCallback nwCallbackObject;

    public NWHTTPResponseObject() {
    }

    public HashMap<String, String> getResponseHeader() {
        return responseHeader;
    }

    public void setResponseHeader(HashMap<String, String> responseHeader) {
        this.responseHeader = responseHeader;
    }

    public String getResponseObject() {
        return ResponseObject;
    }

    public INetworkCallback getNwCallbackObject() {
        return nwCallbackObject;
    }

    public void setResponseObject(String responseObject) {
        ResponseObject = responseObject;
    }

    public void setNwCallbackObject(INetworkCallback nwCallbackObject) {
        this.nwCallbackObject = nwCallbackObject;
    }
}
