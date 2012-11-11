package edu.entitymaster.logic;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Антон
 * Date: 10.11.12
 * Time: 16:07
 * To change this template use File | Settings | File Templates.
 */
public interface ClientRepository {
    public Boolean createClient(Client client);
    public List<String> readClients();
    public Boolean updateClient(Client srcClient, Client destClient);
    public Boolean deleteClient(Client client);
}
