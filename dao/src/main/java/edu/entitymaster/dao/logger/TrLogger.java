package edu.entitymaster.dao.logger;

import edu.entitymaster.dao.Client;

import java.io.IOException;
import java.io.OutputStream;
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
    private OutputStream outputStream;
    private ExecutorService executorService = Executors.newFixedThreadPool(5);
    private LogStrategy logStrategy;

    public TrLogger(Writer writer, OutputStream outputStream, LogStrategy logStrategy) {
        this.writer = writer;
        this.outputStream = outputStream;
        this.logStrategy = logStrategy;
    }

    public void save(Client client) {
        try {
            writer.append(client.toString() + '\n');
        } catch (IOException e) {
            e.printStackTrace();
        }
        executorService.execute(new FlushToLog());
    }

    public void markAsDeleted(Client client) {
        //setting negative id to mark as deleted
        save(client.setId(-client.getId()));
    }

    class FlushToLog implements Runnable {
        public void run() {
            logStrategy.flush();
        }
    }

    public void setWriter(Writer writer) {
        this.writer = writer;
    }

}
