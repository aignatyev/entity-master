package edu.entitymaster.dao.logger;

import java.io.IOException;
import java.io.Writer;

/**
 * Created with IntelliJ IDEA.
 * User: ignatyan
 * Date: 20.12.12
 * Time: 8:59
 */
public class LogStrategyImplFixedWidth implements LogStrategy {
    Writer writer;

    public LogStrategyImplFixedWidth(Writer writer) {
        this.writer = writer;
    }

    public void flush() {
        if (true) try { //TODO
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
