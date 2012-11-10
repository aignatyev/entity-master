package edu.entitymaster.logic;

import java.io.File;
import java.io.IOException;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
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
    {
        try {
            Files.touch(new File(System.getProperty("user.dir") + "ClientRepository.csv"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Boolean createClient(Client client) {
        try {
            Files.append(client.read(), file, Charset.defaultCharset());
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public List<Client> readClients() {
        List<String> clientsRead;
        List<Client> clients = null;
        try {
            clientsRead = Files.readLines(file, Charset.defaultCharset());
            for (String line : clientsRead) {
                String name = line.split(",")[0];
                clients.add(new Client(name));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return clients;
    }

    public Boolean updateClient(Client srcClient, Client destClient) {
        /*CharBuffer charBuffer = Files.map(file, FileChannel.MapMode.READ_WRITE).asCharBuffer();
        charBuffer.put(charBuffer., destClient);
        */
        List<Client> clients = readClients();
        clients.set(clients.indexOf(srcClient), destClient);

        return save(clients);
    }

    private Boolean save(List<Client> clients) {
        String clientsString = "";
        for (Client client : clients) {
            clientsString += client.getName();
            if (clients.indexOf(client) + 1 != clients.size()) clientsString += "\n";
        }
        try {
            Files.write(clientsString, file, Charset.defaultCharset());
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public Boolean deleteClient(Client client) {
        List<Client> clients = readClients();
        if (clients.remove(client)) {
            save(clients);
            return true;
        }
        return false;
    }
}
