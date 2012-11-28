package edu.entitymaster.logic;

import edu.entitymaster.dao.Client;
import edu.entitymaster.dao.ClientRepository;

/**
 * Created with IntelliJ IDEA.
 * User: Антон
 * Date: 29.11.12
 * Time: 0:37
 * To change this template use File | Settings | File Templates.
 */
public class ClientSearch {
    ClientRepository clientRepository;

    public ClientSearch(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public Client findClientByName(String name) {
        for (Client client : clientRepository.getClients().values()) {
            if (client.getName().matches(name))
                return client;
        }
        return null;
    }
}
