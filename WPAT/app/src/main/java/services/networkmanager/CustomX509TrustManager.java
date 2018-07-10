package services.networkmanager;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.X509TrustManager;

/**
 *CustomX509TrustManager publishes API for SSL Validataion. From API Level 16, default SSLFactory is
 * disabled and enabled back from API Level 20. Since application uses custom SSLSocketFactory, it is
 * expected to validate custom SSL certificate. Currently override implementation is empty to
 * by pass SSL Validation.
 */

public class CustomX509TrustManager implements X509TrustManager {

    @Override
    public void checkClientTrusted(X509Certificate[] chain, String authType)
            throws CertificateException {
    }

    @Override
    public void checkServerTrusted(X509Certificate[] certs,
                                   String authType) throws CertificateException {
    }

    public X509Certificate[] getAcceptedIssuers() {
        return null;
    }

}
