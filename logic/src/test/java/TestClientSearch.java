import edu.entitymaster.dao.Client;
import edu.entitymaster.dao.ClientService;
import edu.entitymaster.dao.TrLogger;
import edu.entitymaster.logic.ClientSearch;
import org.junit.Test;

import java.io.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created with IntelliJ IDEA.
 * User: Антон
 * Date: 29.11.12
 * Time: 0:56
 * To change this template use File | Settings | File Templates.
 */
public class TestClientSearch {
    public static final String CLIENT_REPOSITORY_LOG_CSV = "ClientRepositoryLog.csv";
    ClientService clientService;
    Client client = new Client("test");

    File f = new File( "./" + CLIENT_REPOSITORY_LOG_CSV);
    BufferedWriter bufferedWriter;
    ClientSearch clientSearch;
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

    {
        try {
            bufferedWriter = new BufferedWriter(new FileWriter(f));
        } catch (IOException e) {
            e.printStackTrace();
        }
        clientService = new ClientService(new TrLogger(bufferedWriter, byteArrayOutputStream));
        clientService.saveClient(client);
        clientSearch = new ClientSearch(clientService);
    }

    @Test
    public void testSearchClient() {
        assertEquals(client, clientSearch.findClientByName("test").toArray()[0]);
    }

    @Test
    public void testSearchNonExistingClient() {
        assertTrue(clientSearch.findClientByName("te2st").isEmpty());
    }
}
