import com.google.common.collect.Sets;
import com.google.common.io.Files;
import com.google.gson.Gson;
import edu.entitymaster.logic.Client;
import edu.entitymaster.logic.ClientService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

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
    Client client = new Client("test");
    Client client2 = new Client("2test2");
    private File f = new File(System.getProperty("user.dir") + "\\ClientRepositoryLog.csv");

    @Before
    public void setUp() {
        try {                                                           //TODO make test repository instead of main one
            Files.write("",
                    f,
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
        Gson gson = new Gson();
        clientService.createClient(client);
        assertThat(clientService.getClients().size(), is(1));
        assertTrue(clientService.getClients().containsValue(client));
        try {
            assertTrue(Files.readLines(f, Charset.defaultCharset()).contains(gson.toJson(client)));
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        clientService2.readClients();
        assertThat(clientService2.getClients().size(), is(1));
        assertEquals("test", clientService2.getClients().get(0).getName());
    }

    @Test
    public void testConcurrentCreate() {
        class TestConcurrentCreate implements Runnable {
            public void run() {
                clientService.createClient(client);
            }
        }
        ExecutorService executorService = Executors.newFixedThreadPool(15);
        for (int i = 0; i<15; i++) {
            executorService.execute(new TestConcurrentCreate());
        }
        try {
            executorService.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertThat(clientService.getClients().size(), is(15));
        assertTrue(clientService.getClients().containsValue(client));
        assertEquals(clientService.getClients().keySet(),
                Sets.newHashSet(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14));
    }

}
