package com.kafka.cripto.producer.department.model;

public class Data {

    private String department;
    private String data;

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Data {" +
                "department='" + department + '\'' +
                ", data='" + data + '\'' +
                '}';
    }
}