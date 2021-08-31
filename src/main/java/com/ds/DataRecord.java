package com.ds;

public class DataRecord {

    String data;

    public DataRecord() {
    }

    public DataRecord(String data) {
        this.data = data;
    }

    public String getData() {
        return data;
    }

    public String toString() {
        return new com.google.gson.Gson().toJson(this);
    }

}