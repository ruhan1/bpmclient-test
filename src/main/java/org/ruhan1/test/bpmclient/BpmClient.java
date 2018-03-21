package org.ruhan1.test.bpmclient;

import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.manager.RuntimeEngine;
import org.kie.api.runtime.process.ProcessInstance;
import org.kie.remote.client.api.RemoteRuntimeEngineFactory;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.net.URL;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;

import static org.ruhan1.test.bpmclient.Configuration.PROCESS_PARAMETERS;

/**
 * Created by ruhan on 3/21/18.
 */
public class BpmClient
{
    private KieSession session;

    final static int TIMEOUT_S = 120;

    public BpmClient() throws Exception
    {
        session = initKieSession();
    }

    protected KieSession initKieSession() throws Exception
    {
        Configuration config = new Configuration();
        RuntimeEngine restSessionFactory;
        restSessionFactory = RemoteRuntimeEngineFactory.newRestBuilder()
                                                       .addDeploymentId( config.deploymentId )
                                                       .addUrl( new URL( config.bpmInstanceUrl ) )
                                                       .addUserName( config.username )
                                                       .addPassword( config.password )
                                                       .addTimeout( TIMEOUT_S )
                                                       .build();

        return restSessionFactory.getKieSession();
    }

    public synchronized boolean startTask( String processId, Map<String, Object> parameters ) throws Exception
    {
        ProcessInstance processInstance = session.startProcess( processId, parameters );
        if ( processInstance == null )
        {
            System.out.println( "Failed to create new process instance." );
            return false;
        }
        System.out.println( ">>> " + processInstance.getId() );
        return true;

    }

    public static void main( String[] args ) throws Exception
    {
        ignoreSSLCert();

        BpmClient bpmClient = new BpmClient();
        Map<String, Object> params = new HashMap<String, Object>();
        params.put( "taskId", 0 );
        params.put( "processParameters", PROCESS_PARAMETERS );
        params.put( "usersAuthToken", "XXX" );
        for ( int i = 0; i < 2; i++ )
        {
            bpmClient.startTask( "ncl-workflows.componentbuild", params );
        }
    }

    private static void ignoreSSLCert() throws Exception
    {
        // Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager()
        {
            public java.security.cert.X509Certificate[] getAcceptedIssuers()
            {
                return null;
            }

            public void checkClientTrusted( X509Certificate[] certs, String authType )
            {
            }

            public void checkServerTrusted( X509Certificate[] certs, String authType )
            {
            }
        } };

        // Install the all-trusting trust manager
        SSLContext sc = SSLContext.getInstance( "SSL" );
        sc.init( null, trustAllCerts, new java.security.SecureRandom() );
        HttpsURLConnection.setDefaultSSLSocketFactory( sc.getSocketFactory() );

        // Create all-trusting host name verifier
        HostnameVerifier allHostsValid = new HostnameVerifier()
        {
            public boolean verify( String hostname, SSLSession session )
            {
                return true;
            }
        };

        // Install the all-trusting host verifier
        HttpsURLConnection.setDefaultHostnameVerifier( allHostsValid );
    }
}
