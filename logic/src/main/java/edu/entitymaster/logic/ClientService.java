package edu.entitymaster.logic;

import com.google.common.io.Files;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Антон
 * Date: 10.11.12
 * Time: 16:13
 * To change this template use File | Settings | File Templates.
 */
public class ClientService implements ClientRepository {
    private File clientRepositoryFile;// = new File(System.getProperty("user.dir") + "\\ClientRepository.csv");
    private List<Client> clients;

    public File getClientRepositoryFile() {
        return clientRepositoryFile;
    }

    public ClientService(File f) {
        try {
            Files.touch(f);
            clientRepositoryFile = f;
            clients = readClients();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Boolean createClient(Client client) {
        clients.add(client);
        return save();
    }

    private List<Client> readClients() {
        clients = new ArrayList<Client>();
        try {
            List<String> clientsStrings = Files.readLines(clientRepositoryFile, Charset.defaultCharset());
            for (String clientString : clientsStrings)
                clients.add(new Client(clientString));
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        File tmpFile = new File(System.getProperty("user.dir") + "\\ClientRepositoryTmp" + Calendar.getInstance().getTimeInMillis() + ".csv");
        try {
            Files.touch(tmpFile);
            Files.write("", tmpFile, Charset.defaultCharset());
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            for (Client client : clients)
                Files.append(client.read(), tmpFile, Charset.defaultCharset());
            Files.copy(tmpFile, clientRepositoryFile);
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

    public List<Client> getClients() {
        return clients;
    }
}
