package edu.entitymaster.dao;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Антон
 * Date: 10.11.12
 * Time: 16:07
 * To change this template use File | Settings | File Templates.
 */
public interface ClientRepository {
    public Map<Integer, Client> getClientsMap();
    public void deleteClient(int id);
}
