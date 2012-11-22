package edu.entitymaster.logic;

import java.io.*;
import java.util.Collections;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created with IntelliJ IDEA.
 * User: Антон
 * Date: 10.11.12
 * Time: 16:13
 * To change this template use File | Settings | File Templates.
 */
public class ClientService implements ClientRepository {
    private ConcurrentHashMap<Integer, Client> clients;
    private AtomicInteger indexer;
    private File f = new File(System.getProperty("user.dir") + "\\ClientRepositoryLog.csv");
    private BufferedWriter bufferedWriter;

    public ClientService() {
        clients = readClients();
        //creating clients ID counter
        try {
            indexer = new AtomicInteger(Collections.max(clients.keySet()) + 1);
        } catch (NoSuchElementException e) {
            indexer = new AtomicInteger(0);
        }
    }

    public void createClient(Client client) {
        client.setId(indexer.getAndIncrement());
        if (!clients.containsKey(client.getId())) {
            clients.put(client.getId(), client);
            saveToLog(client);
        } else
            throw new IllegalArgumentException(client.read() + ": client with such ID already exists");
    }

    public void updateClient(Client srcClient, Client destClient) {
        srcClient.setName(destClient.getName()); // set all fields
        saveToLog(srcClient);
    }

    public void deleteClient(Client client) {
        clients.remove(client.getId());
        client.setDeleted(true);
        saveToLog(client);
    }

    private ConcurrentHashMap<Integer, Client> readClients() {
        clients = new ConcurrentHashMap<Integer, Client>();
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(f));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] array = line.split(",");
                int id = Integer.parseInt(array[0]);
                String name = array[1];
                Boolean deleted = Boolean.parseBoolean(array[2]);
                Client client = new Client(id, name, deleted);
                if (deleted)
                    deleteClient(client);
                else if (clients.containsKey(id))
                    updateClient(clients.get(id), client);
                else
                    clients.put(id, client);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return clients;
    }

    private synchronized void saveToLog(final Client client) {
        class FlushToLog implements Runnable {
            public void run() {
                try {
                    bufferedWriter.flush();
                    bufferedWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        try {
//            if (bufferedWriter.)
            bufferedWriter = new BufferedWriter(new FileWriter(f, true));
            bufferedWriter.append(client.read());
            bufferedWriter.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
         //TODO flush every 5 lines
            new FlushToLog().run();
    }

    public ConcurrentHashMap<Integer, Client> getClients() {
        return clients;
    }
}
