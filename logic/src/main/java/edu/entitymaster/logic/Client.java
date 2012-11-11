package edu.entitymaster.logic;

/**
 * Created with IntelliJ IDEA.
 * User: Антон
 * Date: 10.11.12
 * Time: 16:09
 * To change this template use File | Settings | File Templates.
 */
public class Client {
    String name;

    public Client(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String read() {
        return name;       //+ number + etc
    }
}
