package edu.entitymaster.logic;

import com.google.gson.Gson;

import java.io.*;
import java.util.Collections;
import java.util.Map;
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
    private Map<Integer, Client> clients = new ConcurrentHashMap<Integer, Client>();
    private AtomicInteger indexer = new AtomicInteger(0);
    private File f = new File(System.getProperty("user.dir") + "\\ClientRepositoryLog.csv");
    private BufferedWriter bufferedWriter;

    public ClientService() {
        try {
            bufferedWriter = new BufferedWriter(new FileWriter(f, true));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void createClient(Client client) {
        synchronized (indexer) {
            client.setId(indexer.get());
            clients.put(indexer.getAndIncrement(), client);
            saveToLog(client);
        }
    }

    public void updateClient(Client srcClient, Client destClient) {
        synchronized (srcClient){
            int id = srcClient.getId();
            srcClient = destClient;
            srcClient.setId(id);
            clients.put(srcClient.getId(), srcClient);
            saveToLog(srcClient);
        }
    }

    public void deleteClient(Client client) {
        synchronized (client) {
            clients.remove(client.getId());
            saveToLog(client);           //TODO mark client as deleted
        }
    }

    public Map<Integer, Client> readClients() {
        clients = new ConcurrentHashMap<Integer, Client>();
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(f));
            Gson gson = new Gson();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                Client client = gson.fromJson(line, Client.class);
                clients.put(client.getId(), client);
            }
            //creating clients ID counter
            indexer = new AtomicInteger(Collections.max(clients.keySet()) + 1);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return clients;
    }

    private void saveToLog(final Client client) {
        class FlushToLog implements Runnable {
            public void run() {
                try {
                    bufferedWriter.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        synchronized (client) {
            try {
                bufferedWriter.append(client.toString());
                bufferedWriter.newLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            //TODO flush every 5 lines
            Thread thread = new Thread(new FlushToLog());
            thread.start();
        }
    }

    public Map<Integer, Client> getClients() {
        return clients;
    }
}
