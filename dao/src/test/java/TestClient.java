import edu.entitymaster.dao.Client;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created with IntelliJ IDEA.
 * User: Антон
 * Date: 13.11.12
 * Time: 22:14
 * To change this template use File | Settings | File Templates.
 */
public class TestClient {
    Client client = new Client("test");

    @Test
    public void testClientName(){
        client.setName("test");
        Assert.assertThat(client.getName(), CoreMatchers.is("test"));
    }
}
