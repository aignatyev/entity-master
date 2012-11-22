import com.google.common.collect.Sets;
import com.google.common.io.Files;
import edu.entitymaster.logic.Client;
import edu.entitymaster.logic.ClientService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.hamcrest.CoreMatchers.is;
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
    Client client = new Client(0, "test", false);
    Client client2 = new Client(1, "2test2", false);

    @Before
    public void setUp() {
        try {                                                           //TODO make test repository instead of main one
            Files.write("",
                    new File(System.getProperty("user.dir") + "\\ClientRepositoryLog.csv"),
                    Charset.defaultCharset());
        } catch (IOException e) {
            e.printStackTrace();
        }
        clientService = new ClientService();
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testCreateClient() {
        clientService.createClient(client);
        assertThat(clientService.getClients().size(), is(1));
        assertTrue(clientService.getClients().containsValue(client));
    }

    @Test
    public void testUpdateClient() {
        clientService.createClient(client);
        clientService.updateClient(client, client2);
        assertThat(clientService.getClients().size(), is(1));
        assertEquals("2test2", clientService.getClients().get(0).getName());
    }

    @Test
    public void testDeleteClient() {
        clientService.createClient(client);
        clientService.deleteClient(client);
        assertThat(clientService.getClients().size(), is(0));
        assertFalse(clientService.getClients().containsValue(client));
    }

    @Test
    public void testReadOldRepo() {
        clientService.createClient(client);
        ClientService clientService2 = new ClientService();
        assertThat(clientService2.getClients().size(), is(1));
        assertEquals("test", clientService2.getClients().get(0).getName());
    }

    @Test
    public void testDeleteUnknownClient() {
        clientService.createClient(client);
        clientService.deleteClient(client2);
        assertThat(clientService.getClients().size(), is(1));
        assertTrue(clientService.getClients().containsValue(client));
    }

    @Test
    public void testConcurrentCreate() {
        class TestConcurrentCreate implements Runnable {
            public void run() {
                clientService.createClient(client);
            }
        }

        new TestConcurrentCreate().run();
        new TestConcurrentCreate().run();
        new TestConcurrentCreate().run();
        new TestConcurrentCreate().run();
        new TestConcurrentCreate().run();
        new TestConcurrentCreate().run();
        new TestConcurrentCreate().run();
        new TestConcurrentCreate().run();
        new TestConcurrentCreate().run();
        new TestConcurrentCreate().run();
        new TestConcurrentCreate().run();
        new TestConcurrentCreate().run();
        new TestConcurrentCreate().run();
        new TestConcurrentCreate().run();
        new TestConcurrentCreate().run();

        assertThat(clientService.getClients().size(), is(15));
        assertTrue(clientService.getClients().containsValue(client));
        assertEquals(clientService.getClients().keySet(),
                Sets.newHashSet(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14));
    }

}
