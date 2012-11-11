package edu.entitymaster.logic;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import com.google.common.io.Files;

/**
 * Created with IntelliJ IDEA.
 * User: Антон
 * Date: 10.11.12
 * Time: 16:13
 * To change this template use File | Settings | File Templates.
 */
public class ClientService implements ClientRepository {
    private File file = null;// = new File(System.getProperty("user.dir") + "ClientRepository.csv");

    public ClientService(File f) {
        try {
            Files.touch(f);
            file = f;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ClientService() {
        try {
            File f = new File(System.getProperty("user.dir") + "\\ClientRepository.csv");
            Files.touch(f);
            file = f;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Boolean createClient(Client client) {
        try {
            Files.append(client.read() + "\n", file, Charset.defaultCharset());
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public List<String> readClients() {
        List<String> clientsRead = new ArrayList<String>();
        List<Client> clients = new ArrayList<Client>();
        try {
            clientsRead = Files.readLines(file, Charset.defaultCharset());
            /*for (String line : clientsRead) {
                String name = line.split(",")[0];
                clients.add(new Client(name));
            }*/
        } catch (IOException e) {
            e.printStackTrace();
        }
        return clientsRead;
    }

    public Boolean updateClient(Client srcClient, Client destClient) {
        /*CharBuffer charBuffer = Files.map(file, FileChannel.MapMode.READ_WRITE).asCharBuffer();
        charBuffer.put(charBuffer., destClient);
        */
        List<String> clients = readClients();
        clients.set(clients.indexOf(srcClient.read()), destClient.read());

        return save(clients);
    }

    private Boolean save(List<String> clients) {
        try {
            Files.write("", file, Charset.defaultCharset());
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            for (String client : clients)
                Files.append(client, file, Charset.defaultCharset());
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public Boolean deleteClient(Client client) {
        List<String> clients = readClients();
        if (clients.remove(client.read())) {
            return save(clients);
        }
        return false;
    }

    public void deleteClientRepository(File file) {
        file.delete();
    }
}
