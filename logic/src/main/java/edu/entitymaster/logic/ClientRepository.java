package edu.entitymaster.logic;

import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: Антон
 * Date: 10.11.12
 * Time: 16:07
 * To change this template use File | Settings | File Templates.
 */
public interface ClientRepository {
    public void createClient(Client client);
    public HashMap<Integer, Client> getClients();
    public void updateClient(Client srcClient, Client destClient);
    public void deleteClient(Client client);
}
