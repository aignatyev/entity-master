package edu.entitymaster.dao;

import com.google.gson.Gson;

/**
 * Created with IntelliJ IDEA.
 * User: Антон
 * Date: 10.11.12
 * Time: 16:09
 * To change this template use File | Settings | File Templates.
 */
public class Client {
    String name;
    int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Client(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String toString() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
