package org.example;

import java.sql.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws SQLException {
        SQLConnect sqlConnect = new SQLConnect();
        Scanner scanner = new Scanner(System.in);

        beforeRegistered(sqlConnect, scanner);
        controlPanel(sqlConnect, scanner);

    }

    public static void beforeRegistered(SQLConnect sqlConnect, Scanner scanner) {
        while (!sqlConnect.isSignedIn) {
            System.out.println("\nBefore you can communicate, you should sign_in or sign_up \n");
            switch (scanner.nextLine()) {
                case "sign_in":
                    signInProcess(sqlConnect);
                    break;
                case "sign_up":
                    signUpProcess(sqlConnect);
                    break;
                case "exit", "^C":
                    System.out.println("Exiting...");
                    return;
                default:
                    System.err.println("Wrong command");
                    break;
            }
        }
    }

    public static void signInProcess(SQLConnect sqlConnect) {
        Scanner nextString = new Scanner(System.in);
        try (Connection connection = DriverManager.getConnection(
                sqlConnect.getUrl(),
                sqlConnect.getUser(),
                sqlConnect.getPassword())) {
            PreparedStatement statement = connection.prepareStatement("SELECT COUNT(*) FROM account WHERE (name, password) = (?, ?)");

            System.out.println("Enter your account name");
            statement.setString(1, nextString.nextLine());
            System.out.println("Enter your account password");
            statement.setString(2, nextString.nextLine());

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next() && resultSet.getInt(1) > 0) {
                    System.out.println("Sign in successful");
                    sqlConnect.setSignedIn(true);
                } else {
                    System.out.println("Sign in failed, please try again");
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void signUpProcess(SQLConnect sqlConnect) {
        Scanner nextString = new Scanner(System.in);
        Scanner nextString2 = new Scanner(System.in);
        try (Connection connection = DriverManager.getConnection(
                sqlConnect.getUrl(),
                sqlConnect.getUser(),
                sqlConnect.getPassword())) {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO account (name, password) VALUES (?, ?)");
            System.out.println("Enter your name");
/*
            This part of code used to insert id  automatically
            statement.setInt(1, nextInt.nextInt());
            int rowsInserted = statement.executeUpdate();
            System.out.println("Rows inserted: " + rowsInserted);
*/
            statement.setString(1, nextString.nextLine());
            System.out.println("Enter your password");
            statement.setString(2, nextString2.nextLine());
            int rowsInserted = statement.executeUpdate();

            if (rowsInserted > 0) {
                System.out.println("Sign up successful");
                sqlConnect.setSignedIn(true);
            } else {
                System.out.println("Sign up failed, please try again");
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void controlPanel(SQLConnect sqlConnect, Scanner scanner) {
        while (sqlConnect.isSignedIn) {
            System.out.println("\nEnter the command, to see list of available commands use help\n");
            switch (scanner.nextLine()) {
                case "get":
                    get(sqlConnect);
                    break;
                case "get_messages":
                    getMessages(sqlConnect);
                    break;
                case "post_message":
                    postMessage(sqlConnect);
                    break;
                case "update":
                    update(sqlConnect);
                    break;
                case "delete":
                    delete(sqlConnect);
                    break;
                case "help":
                    help();
                    break;
                case "exit", "^C":
                    System.out.println("Exiting...");
                    return;
                default:
                    System.err.println("Wrong command");
                    break;
            }
        }
    }

    public static void get(SQLConnect sqlConnect) {
        try (Connection connection = DriverManager.getConnection(
                sqlConnect.getUrl(),
                sqlConnect.getUser(),
                sqlConnect.getPassword())) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM account");
            ResultSet resultSet = statement.executeQuery();

            System.out.printf("%-10s | %-10s | %-10s%n", "ID", "Name", "Password");
            System.out.println("-------------------------------");

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String password = resultSet.getString("password");
                System.out.printf("%-10d | %-10s | %-10s%n", id, name, password);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void getMessages(SQLConnect sqlConnect) {
        try (Connection connection = DriverManager.getConnection(
                sqlConnect.getUrl(),
                sqlConnect.getUser(),
                sqlConnect.getPassword())) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM messages");
            ResultSet resultSet = statement.executeQuery();

            System.out.printf("%-10s%n", "Message");
            System.out.println("-------------------------------");

            while (resultSet.next()) {
                String message = resultSet.getString("message");
                System.out.printf("%-10s%n", message);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void postMessage(SQLConnect sqlConnect) {
        Scanner nextString = new Scanner(System.in);
        try (Connection connection = DriverManager.getConnection(
                sqlConnect.getUrl(),
                sqlConnect.getUser(),
                sqlConnect.getPassword())) {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO messages (message) VALUES (?)");
            System.out.println("Enter your message");
            statement.setString(1, nextString.nextLine());
            int rowsInserted = statement.executeUpdate();
            System.out.println("Rows inserted: " + rowsInserted);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void update(SQLConnect sqlConnect) {
        Scanner nextString = new Scanner(System.in);
        try (Connection connection = DriverManager.getConnection(
                sqlConnect.getUrl(),
                sqlConnect.getUser(),
                sqlConnect.getPassword())) {
            PreparedStatement statement = connection.prepareStatement("UPDATE account SET name = ? WHERE id = ?");
            System.out.println("Enter your new name");
            statement.setString(1, nextString.nextLine());
            System.out.println("Enter your id");
            statement.setInt(2, nextString.nextInt());
            int rowsUpdated = statement.executeUpdate();
            System.out.println("Rows updated: " + rowsUpdated);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void delete(SQLConnect sqlConnect) {
        Scanner nextString = new Scanner(System.in);
        try (Connection connection = DriverManager.getConnection(
                sqlConnect.getUrl(),
                sqlConnect.getUser(),
                sqlConnect.getPassword())) {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM account WHERE id = ?");
            System.out.println("Enter your id");
            statement.setInt(1, nextString.nextInt());
            int rowsDeleted = statement.executeUpdate();
            System.out.println("Rows deleted: " + rowsDeleted);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void help() {
        System.out.println("Available commands:\n get\n get_messages\n post_message\n update\n delete\n exit");
    }
}
