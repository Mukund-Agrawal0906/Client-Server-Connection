import java.io.*;
import java.net.Socket;
import java.util.List;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 12345);

            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            Scanner scanner = new Scanner(System.in);

            while (true) {
                System.out.println("Enter your choice:");
                System.out.println("1 - Request for the total number of Employees");
                System.out.println("2 - Send an employee ID and request for all the details");
                System.out.println("3 - Send two employee IDs for salary comparison");
                System.out.println("4 - Send the name of a district to find employees");
                System.out.println("5 - Add a new employee");
                System.out.println("0 - Quit");

                int choice = scanner.nextInt();
                out.writeInt(choice);
                out.flush();

                if (choice == 0) {
                    break;
                }

                switch (choice) {
                    case 1:
                        int totalEmployees = in.readInt();
                        System.out.println("Total number of employees: " + totalEmployees);
                        break;

                    case 2:
                        System.out.print("Enter the employee ID: ");
                        int employeeID = scanner.nextInt();
                        out.writeInt(employeeID);

                        Employee employee = (Employee) in.readObject();
                        if (employee != null) {
                            System.out.println("Employee Details: " + employee);
                        } else {
                            System.out.println("Employee not found.");
                        }
                        break;

                    case 3:
                        System.out.print("Enter the first employee ID: ");
                        int employeeID1 = scanner.nextInt();
                        System.out.print("Enter the second employee ID: ");
                        int employeeID2 = scanner.nextInt();

                        out.writeInt(employeeID1);
                        out.writeInt(employeeID2);

                        String higherSalaryEmployee = in.readUTF();
                        System.out.println("Employee with a higher salary: " + higherSalaryEmployee);
                        break;

                    case 4:
                        System.out.print("Enter the name of the district: ");
                        String district = scanner.next();
                        out.writeUTF(district);

                        List<Employee> employeesInDistrict = (List<Employee>) in.readObject();
                        if (employeesInDistrict.isEmpty()) {
                            System.out.println("No employees found in the district.");
                        } else {
                            System.out.println("Employees in the district:");
                            for (Employee emp : employeesInDistrict) {
                                System.out.println(emp);
                            }
                        }
                        break;

                    case 5:
                        // Input and send new employee details to the server
                        System.out.print("Enter Employee ID: ");
                        int newEmployeeID = scanner.nextInt();
                        System.out.print("Enter Employee Name: ");
                        String newEmployeeName = scanner.next();
                        System.out.print("Enter Employee Salary: ");
                        double newEmployeeSalary = scanner.nextDouble();
                        System.out.print("Enter Employee District: ");
                        String newEmployeeDistrict = scanner.next();
                        System.out.print("Enter Employee Type (Temporary/Permanent): ");
                        String newEmployeeType = scanner.next();

                        Employee newEmployee = new Employee(newEmployeeID, newEmployeeName, newEmployeeSalary, newEmployeeDistrict, newEmployeeType);
                        out.writeObject(newEmployee);
                        break;

                    default:
                        System.out.println("Invalid choice. Please enter a valid option.");
                        break;
                }
            }

            in.close();
            out.close();
            socket.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
