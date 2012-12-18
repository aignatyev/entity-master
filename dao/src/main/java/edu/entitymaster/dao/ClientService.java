package edu.entitymaster.dao;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;

/**
 * Created with IntelliJ IDEA.
 * User: Антон
 * Date: 10.11.12
 * Time: 16:13
 * To change this template use File | Settings | File Templates.
 */
public class ClientService implements ClientRepository {
    private Map<Integer, Client> clients = new ConcurrentHashMap<Integer, Client>();
    private AtomicInteger indexer = new AtomicInteger(1);
    private TrLogger trLogger;
    private CountDownLatch initLock;
    private Lock saveLock;

    public ClientService(TrLogger trLogger) {
        this.trLogger = trLogger;
        initLock = new CountDownLatch(0);
    }

    public ClientService(TrLogger trLogger, Reader initialLoad) {
        this.trLogger = trLogger;
        initLock = new CountDownLatch(1);
        readClients(initialLoad);
        initLock.countDown();
    }

    public int saveClient(Client client) {
        awaitForInit();
        synchronized (client) {
            if(client.getId() == 0) client.setId(indexer.getAndIncrement());
            clients.put(client.getId(), client);
            trLogger.save(client);
            return client.getId();
        }
    }

    private void awaitForInit() {
        try {
            initLock.await();
        } catch (InterruptedException e) {
            throw new RuntimeException("init exception");
        }
    }

    public void deleteClient(int id) {
        awaitForInit();
        trLogger.markAsDeleted(clients.get(id));
        clients.remove(id);
    }

    private Map<Integer, Client> readClients(Reader reader) {
        try {
            BufferedReader bufferedReader = new BufferedReader(reader);
            Gson gson = new Gson();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                Client client = gson.fromJson(line, Client.class);
                if (client.getId() > 0)
                    clients.put(client.getId(), client);
                else clients.remove(-client.getId());
            }
            //creating clients ID counter
            if (!clients.isEmpty())
                indexer = new AtomicInteger(Collections.max(clients.keySet()) + 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return clients;
    }

    public Map<Integer, Client> getClientsMap() {
        awaitForInit();
        return Collections.unmodifiableMap(clients);
    }

    public void setTrLogger(TrLogger trLogger) {
        this.trLogger = trLogger;
    }
}
