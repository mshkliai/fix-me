package database;

import java.sql.*;
import java.util.ArrayList;

public class DataBase {
    private static DataBase db = null;

    private static Statement statm;
    private static ResultSet info;
    private final String driverName;
    private final String connectionString;
    private Connection connection;

    private DataBase() {
        this.driverName = "org.sqlite.JDBC";
        this.connectionString = "jdbc:sqlite:../transactions.db";
        connectDb();
    }

    public static DataBase getDb() {
        if (db == null) {
            db = new DataBase();
        }

        return db;
    }

    public void newNote(String time, String idS, String idR, String product, int count, int price) {
        String  requestAdd = "VALUES ('" + time + "', '"
                + idS + "', '"
                + idR + "' ,'"
                + product + "',"
                + count + ","
                + price + ");";

        try {
            statm.execute("INSERT INTO 'transactions' ('time', 'idS', 'idR', 'product', 'count', 'price')" + requestAdd );
        } catch (SQLException e) {
            System.err.println("Error adding new note in DataBase: " + e.getMessage());
        }
    }

    public ArrayList<String>    getNotes() throws Exception {
        info = statm.executeQuery("SELECT * FROM transactions");
        ArrayList<String>   notes = new ArrayList<>();

        while (info.next()) {
            notes.add(
                    info.getString("idS") + " -> "
                    + info.getString("idR") + ";"
                    + info.getString("product") + ";"
                    + info.getInt("count") + ";"
                    + info.getInt("price") + ";"
                    + info.getString("time") + ";"
            );
        }

        return notes;
    }

    private void connectDb() {
        try {
            Class.forName(this.driverName);
            this.connection = DriverManager.getConnection(this.connectionString);

            statm = this.connection.createStatement();
            statm.execute("CREATE  TABLE if not EXISTS 'transactions' " +
                    "('time' text, " +
                    "'idS' text, " +
                    "'idR' text, " +
                    "'product' text," +
                    "'count' INT, " +
                    "'price' INT);");
        }
        catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}