import edu.entitymaster.logic.Client;
import edu.entitymaster.logic.ClientService;
import junit.framework.TestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

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
    Client client2 = new Client("test2");

    @Before
    public void setUp() {
        clientService = new ClientService(new File(System.getProperty("user.dir") + "\\ClientRepositoryTest.csv"));
    }

    @After
    public void tearDown() {
        clientService.deleteClientRepository(new File(System.getProperty("user.dir") + "\\ClientRepositoryTest.csv"));
    }

    @Test
    public void testCreateClient() {
        assertTrue(clientService.createClient(client));
        assertTrue(clientService.readClients().contains(client.read()));
    }

    @Test
    public void testUpdateClient() {
        clientService.createClient(client);
        assertTrue(clientService.updateClient(client, client2));
        assertTrue(clientService.readClients().contains(client2.read()));
        assertFalse(clientService.readClients().contains(client.read()));
    }

    @Test
    public void testDeleteClient() {
        clientService.createClient(client);
        assertTrue(clientService.deleteClient(client));
        assertFalse(clientService.readClients().contains(client.read()));
    }
}
