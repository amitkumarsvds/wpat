package services.networkmanager;
import org.apache.http.conn.ssl.SSLSocketFactory;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;

import javax.net.ssl.SSLContext;

/**
 * CustomSSLSocketFactory publishes API for TLS Connection. From API Level 16, default SSLFactory is
 * disabled and enabled back from API Level 20. To force the application to use SSLSocketFactory
 * application has to create CustomSSLSocketFactory.
 */
public class CustomSSLSocketFactory extends SSLSocketFactory {
    SSLContext mSslContext = SSLContext.getInstance("TLS");

    public CustomSSLSocketFactory(SSLContext context)
            throws KeyManagementException, NoSuchAlgorithmException,
            KeyStoreException, UnrecoverableKeyException {
        super(null);
        mSslContext = context;
    }

    @Override
    public Socket createSocket(Socket socket, String host, int port,
                               boolean autoClose) throws IOException, UnknownHostException {
        return mSslContext.getSocketFactory().createSocket(socket, host, port,
                autoClose);
    }

    @Override
    public Socket createSocket() throws IOException {
        return mSslContext.getSocketFactory().createSocket();
    }
}