import com.google.common.collect.Sets;
import com.google.common.io.Files;
import com.google.gson.Gson;
import edu.entitymaster.dao.Client;
import edu.entitymaster.dao.ClientService;
import edu.entitymaster.dao.TrLogger;
import org.hamcrest.CoreMatchers;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.*;

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
    File f = new File("./ClientRepositoryLog.csv");
    BufferedWriter bufferedWriter;
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
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
        clientService = new ClientService(new TrLogger(bufferedWriter, byteArrayOutputStream));
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testCreateClient() {
        Gson gson = new Gson();
        clientService.saveClient(client);
        Assert.assertThat(clientService.getClientsMap().size(), CoreMatchers.is(1));
        Assert.assertTrue(clientService.getClientsMap().containsValue(client));
        try {
            Assert.assertTrue(Files.readLines(f, Charset.defaultCharset()).contains(gson.toJson(client)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testUpdateClient() {
        int id = clientService.saveClient(client);
        clientService.saveClient(client.setName("2test2"));
        Assert.assertThat(clientService.getClientsMap().size(), CoreMatchers.is(1));
        Assert.assertEquals("2test2", clientService.getClientsMap().get(id).getName());
    }

    @Test
    public void testDeleteClient() {
        int id = clientService.saveClient(client);
        clientService.deleteClient(id);
        Assert.assertThat(clientService.getClientsMap().size(), CoreMatchers.is(0));
        Assert.assertFalse(clientService.getClientsMap().containsValue(client));
    }

    @Test
    public void testReadOldRepo() throws InterruptedException, FileNotFoundException {
        int id = clientService.saveClient(client);
        Thread.sleep(3000); //wait for client to flush
        ClientService clientService2 = new ClientService(new TrLogger(bufferedWriter, byteArrayOutputStream), new FileReader(f));
        Assert.assertThat(clientService2.getClientsMap().size(), CoreMatchers.is(1));
        Assert.assertEquals("test", clientService2.getClientsMap().get(id).getName());
    }

    @Test
    public void testConcurrentUpdate() {
        class TestConcurrentCreate implements Runnable {
            public void run() {
                clientService.saveClient(client);
            }
        }
        int threadsNum = 15;
        ExecutorService executorService = Executors.newFixedThreadPool(threadsNum);
        for (int i = 0; i<threadsNum; i++) {
            executorService.execute(new TestConcurrentCreate());
        }
        try {
            executorService.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Assert.assertThat(clientService.getClientsMap().size(), CoreMatchers.is(1));
        Assert.assertTrue(clientService.getClientsMap().containsValue(client));
        Assert.assertEquals(Sets.newHashSet(1),
                clientService.getClientsMap().keySet());
    }

    @Test
    public void testPerformance() {
        class TestConcurrentCreate implements Runnable {
            public void run() {
                for (int i=0; i<100000 ; i++) {
                    clientService.saveClient(client);
                }
            }
        }
        int threadsNum = 10;
        List<Future> futures = new ArrayList<Future>();
        ExecutorService executorService = Executors.newFixedThreadPool(threadsNum);
        long start = Calendar.getInstance().getTimeInMillis();
        for (int i = 0; i<threadsNum; i++) {
            futures.add(executorService.submit(new TestConcurrentCreate()));
        }
        for (Future f : futures) {
            try {
                f.get();
            } catch (InterruptedException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            } catch (ExecutionException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
        System.out.println(Calendar.getInstance().getTimeInMillis() - start + " took to write");
        try {
            start = Calendar.getInstance().getTimeInMillis();
            ClientService clientService2 = new ClientService(new TrLogger(bufferedWriter, byteArrayOutputStream), new FileReader(f));
            System.out.println(Calendar.getInstance().getTimeInMillis() - start + " took to read");

        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

}
