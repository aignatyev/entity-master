package edu.entitymaster.dao;

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

    public TrLogger(Writer writer, OutputStream outputStream) {
        this.writer = writer;
        this.outputStream = outputStream;
    }

    public void save(Client client) {
        try {
//            byte[] buf = (client.toString() + '\n').getBytes();
//            outputStream.write(buf);
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

    public void setWriter(Writer writer) {
        this.writer = writer;
    }

    class FlushToLog implements Runnable {
        public void run() {
            try {
//                writer.write(outputStream.toString
                writer.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
