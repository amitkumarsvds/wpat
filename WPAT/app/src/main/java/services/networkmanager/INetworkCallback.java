package services.networkmanager;

import org.json.JSONException;

/**
 * Network Callback Interface
 */

public interface INetworkCallback {
    public void OnNetworkSuccessCallback(NWHTTPResponseObject httpResponse) throws JSONException;
    public void OnNetworkFailureCallback(String failureData);
}
