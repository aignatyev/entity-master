package edu.entitymaster.logic;

import com.google.common.base.Predicate;
import edu.entitymaster.dao.Client;
import edu.entitymaster.dao.ClientRepository;

import javax.annotation.Nullable;
import java.util.Collection;

import static com.google.common.collect.Collections2.filter;

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

    public Collection findClientByName(final String name) {
        return filter(clientRepository.getClientsMap().values(),
                new Predicate<Client>() {
                    public boolean apply(@Nullable Client input) {
                        return input.getName().matches(name);
                    }
                });
    }
}
