
package org.lightfish.business.monitoring.control;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import javax.enterprise.inject.Instance;
import org.codehaus.jettison.json.JSONObject;
import org.junit.Test;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.lightfish.business.authenticator.GlassfishAuthenticator;
import static org.powermock.api.mockito.PowerMockito.*;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

import org.lightfish.business.configuration.boundary.Configurator;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(value = WebResource.Builder.class)
public class SessionTokenRetrieverTest {

    SessionTokenRetriever instance;
    //ClientResponse mockClientResponse;
    WebResource.Builder mockBuilder;
    
    @Before
    public void setUp() {
        instance = new SessionTokenRetriever();
        instance.client = setupMockClient();
        instance.configurator = mock(Configurator.class);
        
        instance.location = mockInstance(String.class, "waynemanor");
        instance.username = mockInstance(String.class, "bruce");
        instance.password = mockInstance(String.class, "iambatman");
        instance.authenticator = mockInstance(GlassfishAuthenticator.class, mock(GlassfishAuthenticator.class));
        
    }
    
    private Client setupMockClient() {
        Client mockClient = mock(Client.class);
        WebResource mockResource = mock(WebResource.class);
        mockBuilder = mock(WebResource.Builder.class);

        when(mockClient.resource(anyString())).thenReturn(mockResource);
        when(mockResource.path(anyString())).thenReturn(mockResource);
        when(mockResource.accept(anyString())).thenReturn(mockBuilder);
        when(mockBuilder.header(anyString(),anyString())).thenReturn(mockBuilder);
        
        
        return mockClient;
    }
    
    
    @Test
    public void retrieve_token() throws Exception {
        JSONObject mockResponse = mockResponseObject("batarang");
        when(mockBuilder.post(JSONObject.class)).thenReturn(mockResponse);
        
        instance.retrieveSessionToken();
        
        verify(instance.configurator, times(1)).setValue("sessionToken", "batarang");
        
    }
    
    private JSONObject mockResponseObject(String token) throws Exception {
        JSONObject jsonObj = mock(JSONObject.class);
        JSONObject jsonExtra = mock(JSONObject.class);
        when(jsonObj.getJSONObject("extraProperties")).thenReturn(jsonExtra);

        if(token!=null){
            when(jsonExtra.getString("token")).thenReturn(token);
        }

        return jsonObj;
    }
    
    private <TYPE> Instance<TYPE> mockInstance(Class<TYPE> type, TYPE value) {
        Instance<TYPE> instance = mock(Instance.class);
        when(instance.get()).thenReturn(value);
        return instance;
    }
    
}