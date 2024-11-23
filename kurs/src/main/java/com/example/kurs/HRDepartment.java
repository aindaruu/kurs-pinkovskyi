package com.example.kurs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class HRDepartment {

    private String name;
    private int id;
    private String position;
    private String hireDate;

    public HRDepartment() {

    }

    public HRDepartment(String name, int id, String position, String hireDate) {
        this.hireDate = hireDate;
        this.id = id;
        this.name = name;
        this.position = position;

    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getHireDate() {
        return hireDate;
    }

    public void setHireDate(String hireDate) {
        this.hireDate = hireDate;
    }

    public void writeData(PrintWriter out) throws IOException {
        out.println(String.format("%s;%d;%s;%s", name, id, position, hireDate));
    }

    public void readData(String line) {
        String[] parts = line.split(";");
        if (parts.length == 4) {
            this.name = parts[0].trim();
            try {
                this.id = Integer.parseInt(parts[1].trim());
                this.position = parts[2].trim();
                this.hireDate = parts[3].trim();
            } catch (NumberFormatException e) {
                System.err.println("Помилка даних: " + e.getMessage());
            }
        }
    }

    public String toString() {
        return String.format("HRDepartment{name='%s', id=%d, position=%s, hireDate=%s}",
                name, id, position, hireDate,);
    }

    public static List<HRDepartment> readFromFile(BufferedReader reader) throws IOException {
        List<HRDepartment> employees = new ArrayList<>();
        String line;
        while ((line = reader.readLine()) != null) {
            HRDepartment employee = new HRDepartment();
            employee.readData(line);
            employees.add(employee);

        }
        return employees;
    }

    public static void writeToFile(List<HRDepartment> employees, printWriter out) throws IOException {
        for (HRDepartment employee : employees) {
            employee.writeData(out);
        }
    }

    public static HRDepartment findEmployeeByName(List<HRDepartment> employee, String name) {
        for (HRDepartment employee : employees) {
            if (employee.getName().equalsIgnoreCase(name)) {
                return employee
            }
        }
        return null;
    }

    public void updateEmployee(int id, String position, String hireDate) {
        this.id = id;
        this.position = position;
        this.hireDate = hireDate;
    }
}