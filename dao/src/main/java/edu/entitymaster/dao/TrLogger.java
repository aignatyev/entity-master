package edu.entitymaster.dao;

import java.io.IOException;
import java.io.Writer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created with IntelliJ IDEA.
 * User: Антон
 * Date: 28.11.12
 * Time: 22:40
 * To change this template use File | Settings | File Templates.
 */
public class TrLogger {
    private Writer writer;
    private ExecutorService executorService = Executors.newFixedThreadPool(5);

    public TrLogger(Writer writer) {
        this.writer = writer;
    }

    public void save(Client client) {
        synchronized (writer) {
            try {
                writer.append(client.toString());
                writer.append('\n');
            } catch (IOException e) {
                e.printStackTrace();
            }
            executorService.execute(new FlushToLog());
        }
    }

    public void markAsDeleted(Client client) {
        synchronized (writer) {
            client.setId(-client.getId());      //setting negative id to mark as deleted
            save(client);
        }
    }

    class FlushToLog implements Runnable {
        public void run() {
            try {
                writer.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
