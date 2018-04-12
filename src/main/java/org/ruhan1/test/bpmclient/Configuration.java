package org.ruhan1.test.bpmclient;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by ruhan on 3/21/18.
 */
public class Configuration
{
    private String username;

    private String password;

    private String deploymentId;

    private String processId;

    private String bpmBaseUrl;

    private String usersAuthToken;

    public String getUsername()
    {
        return username;
    }

    public String getPassword()
    {
        return password;
    }

    public String getDeploymentId()
    {
        return deploymentId;
    }

    public String getProcessId()
    {
        return processId;
    }

    public String getBpmBaseUrl()
    {
        return bpmBaseUrl;
    }

    public String getUsersAuthToken()
    {
        return usersAuthToken;
    }

    public Configuration( String configFile ) throws IOException
    {
        Properties prop = new Properties();
        prop.load( new FileInputStream( new File( configFile ) ) );
        username = prop.getProperty( "username" );
        password = prop.getProperty( "password" );
        deploymentId = prop.getProperty( "deploymentId" );
        processId = prop.getProperty( "processId" );
        bpmBaseUrl = prop.getProperty( "bpmBaseUrl" );
        usersAuthToken = prop.getProperty( "usersAuthToken" );
    }
}
