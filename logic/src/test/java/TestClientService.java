import edu.entitymaster.logic.Client;
import edu.entitymaster.logic.ClientService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.Calendar;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

/**
 * Created with IntelliJ IDEA.
 * User: Антон
 * Date: 10.11.12
 * Time: 21:58
 * To change this template use File | Settings | File Templates.
 */
public class TestClientService {
    ClientService clientService;
    Client client = new Client("test");
    Client client2 = new Client("2test2");

    @Before
    public void setUp() {
        clientService = new ClientService(
                new File(System.getProperty("user.dir") + "\\ClientRepositoryTest" + Calendar.getInstance().getTimeInMillis() + ".csv"));
    }

    @After
    public void tearDown() {
        clientService.deleteClientRepository(clientService.getClientRepositoryFile());
    }

    @Test
    public void testCreateClient() {
        assertTrue(clientService.createClient(client));
        assertTrue(clientService.getClients().contains(client));
    }

    @Test
    public void testUpdateClient() {
        clientService.createClient(client);
        assertTrue(clientService.updateClient(client, client2));
        assertTrue(clientService.getClients().contains(client2));
        assertFalse(clientService.getClients().contains(client));
    }

    @Test
    public void testDeleteClient() {
        clientService.createClient(client);
        assertTrue(clientService.deleteClient(client));
        assertFalse(clientService.getClients().contains(client));
    }

    @Test
    public void testReadOldRepo() {
        clientService.createClient(client);
        ClientService clientService2 = new ClientService(clientService.getClientRepositoryFile());
        assertEquals("test", clientService2.getClients().get(0).getName());
    }

    @Test
    public void testDeleteUnknownClient() {
        clientService.createClient(client);
        assertFalse(clientService.deleteClient(client2));
        assertTrue(clientService.getClients().contains(client));
    }

    @Test
    public void testCreateWrongFile() {
        ClientService clientService = new ClientService(new File("\\|/"));
        assertThat(clientService.getClients(), nullValue());
    }
}
