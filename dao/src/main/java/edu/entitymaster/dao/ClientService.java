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


    public void saveClient(Client cl) {
        awaitForInit();
        if(cl.getId() == 0) {
            cl.setId(indexer.incrementAndGet());
        }
        clients.put(cl.getId(), cl);
    }

    private void awaitForInit() {
        try {
            initLock.await();
        } catch (InterruptedException e) {
            throw new RuntimeException("init exception");
        }
    }

    public void createClient(Client client) {
        synchronized (indexer) {
            client.setId(indexer.get());
            clients.put(indexer.getAndIncrement(), client);
            trLogger.save(client);
        }
    }

    public void updateClient(Client srcClient, Client destClient) {
        awaitForInit();
        synchronized (srcClient){
            int id = srcClient.getId();
            srcClient = destClient;
            srcClient.setId(id);
            clients.put(srcClient.getId(), srcClient);
            trLogger.save(srcClient);
        }
    }

    public void deleteClient(Client client) {
        synchronized (client) {
            clients.remove(client.getId());
            trLogger.markAsDeleted(client);
        }
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
        return Collections.unmodifiableMap(clients);
    }
}
