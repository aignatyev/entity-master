package edu.entitymaster.logic;

import com.google.common.io.Files;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Антон
 * Date: 10.11.12
 * Time: 16:13
 * To change this template use File | Settings | File Templates.
 */
public class ClientService implements ClientRepository {
    private File file = null;// = new File(System.getProperty("user.dir") + "\\ClientRepository.csv");
    private List<Client> clients = new ArrayList<Client>();

    public ClientService(File f) {
        try {
            Files.touch(f);
            file = f;
            clients = readClients();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ClientService() {
        try {
            File f = new File(System.getProperty("user.dir") + "\\ClientRepository.csv");
            Files.touch(f);
            file = f;
            clients = readClients();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Boolean createClient(Client client) {
        clients.add(client);
        return save();
    }

    public List<Client> readClients() {
        return clients;
    }

    public Boolean updateClient(Client srcClient, Client destClient) {
        clients.set(clients.indexOf(srcClient), destClient);
        return save();
    }

    public Boolean deleteClient(Client client) {
        if (clients.remove(client)) {
            return save();
        }
        return false;
    }

    private Boolean save() {
        File tmpFile = new File(System.getProperty("user.dir") + "\\ClientRepositoryTmp.csv");
        try {
            Files.touch(tmpFile);
            Files.write("", tmpFile, Charset.defaultCharset());
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            for (Client client : clients)
                Files.append(client.read(), tmpFile, Charset.defaultCharset());
            Files.copy(tmpFile, file);
            tmpFile.delete();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public void deleteClientRepository(File file) {
        file.delete();
    }
}
