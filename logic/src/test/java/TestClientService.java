import com.google.common.collect.Sets;
import com.google.common.io.Files;
import com.google.gson.Gson;
import edu.entitymaster.logic.Client;
import edu.entitymaster.logic.ClientService;
import edu.entitymaster.logic.TrLogger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
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
    File f = new File(System.getProperty("user.dir") + "\\ClientRepositoryLog.csv");
    BufferedWriter bufferedWriter;
    {
        try {
            bufferedWriter = new BufferedWriter(new FileWriter(f));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Before
    public void setUp() {
        try {
            Files.write("",
                    f,
                    Charset.defaultCharset());
        } catch (IOException e) {
            e.printStackTrace();
        }
        clientService = new ClientService(new TrLogger(bufferedWriter));
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
        assertEquals("2test2", clientService.getClients().get(1).getName());
    }

    @Test
    public void testDeleteClient() {
        clientService.createClient(client);
        clientService.deleteClient(client);
        assertThat(clientService.getClients().size(), is(0));
        assertFalse(clientService.getClients().containsValue(client));
    }

    @Test
    public void testReadOldRepo() throws InterruptedException {
        clientService.createClient(client);
        Thread.sleep(1000);      // wait until client is flushed
        ClientService clientService2 = new ClientService(new TrLogger(bufferedWriter));
        try {
            clientService2.readClients(new FileReader(f));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        assertThat(clientService2.getClients().size(), is(1));
        assertEquals("test", clientService2.getClients().get(1).getName());
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
        assertEquals(Sets.newHashSet(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15),
                clientService.getClients().keySet());
    }

}
