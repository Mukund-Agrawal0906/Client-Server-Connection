import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Server {
    static List<Employee> employeeList = new ArrayList<>();

    public static void main(String[] args) {
        initializeEmployeeData();

        try {
            ServerSocket serverSocket = new ServerSocket(12345);

            System.out.println("Server is running...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected.");

                new ClientHandler(clientSocket).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void initializeEmployeeData() {
        // No need to initialize data here; you can add employees interactively.
    }

    public static void addEmployee(Employee employee) {
        employeeList.add(employee);
    }
}

class ClientHandler extends Thread {
    private Socket clientSocket;

    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try {
            ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());

            int requestType = in.readInt();

            if (requestType == 1) {
                out.writeInt(Server.employeeList.size());
                out.flush();
            } else if (requestType == 2) {
                int employeeID = in.readInt();
                Employee employee = findEmployeeByID(employeeID);
                out.writeObject(employee);
                out.flush();
            } else if (requestType == 3) {
                int employeeID1 = in.readInt();
                int employeeID2 = in.readInt();
                String higherSalaryEmployee = compareSalaries(employeeID1, employeeID2);
                out.writeUTF(higherSalaryEmployee);
                out.flush();
            } else if (requestType == 4) {
                String district = in.readUTF();
                List<Employee> employeesInDistrict = findEmployeesByDistrict(district);
                out.writeObject(employeesInDistrict);
                out.flush();
            } else if (requestType == 5) {
                // Handle employee addition request from the client
                Employee newEmployee = (Employee) in.readObject();
                Server.addEmployee(newEmployee);
            }

            in.close();
            out.close();
            clientSocket.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private Employee findEmployeeByID(int employeeID) {
        for (Employee employee : Server.employeeList) {
            if (employee.getEmployeeID() == employeeID) {
                return employee;
            }
        }
        return null; // Employee not found
    }

    private String compareSalaries(int employeeID1, int employeeID2) {
        Employee employee1 = findEmployeeByID(employeeID1);
        Employee employee2 = findEmployeeByID(employeeID2);

        if (employee1 == null || employee2 == null) {
            return "One or both employees not found.";
        } else {
            if (employee1.getSalary() > employee2.getSalary()) {
                return employee1.getName();
            } else if (employee1.getSalary() < employee2.getSalary()) {
                return employee2.getName();
            } else {
                return "Both employees have the same salary.";
            }
        }
    }

    private List<Employee> findEmployeesByDistrict(String district) {
        List<Employee> employeesInDistrict = new ArrayList<>();
        for (Employee employee : Server.employeeList) {
            if (employee.getDistrict().equals(district)) {
                employeesInDistrict.add(employee);
            }
        }
        return employeesInDistrict;
    }
}
