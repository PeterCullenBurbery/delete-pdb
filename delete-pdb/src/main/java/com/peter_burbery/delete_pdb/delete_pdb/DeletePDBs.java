/**
 * @since 2024-W41-5 07.31.00.457 -0400
 * @author peter
 */
package com.peter_burbery.delete_pdb.delete_pdb;

/**
 * 
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.Scanner;

public class DeletePDBs {
    private static final String DB_URL = "jdbc:oracle:thin:@localhost:1521/orcl.localdomain";
    private static final String USERNAME = "sys as sysdba";
    private static final String PASSWORD = "1234";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the names of the PDBs to delete, separated by spaces:");
        String input = scanner.nextLine();
        String[] pdbNames = input.split("\\s+");
        
        try (Connection connection = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD)) {
            for (String pdbName : pdbNames) {
                deletePDB(connection, pdbName);
            }
            System.out.println("PDB(s) deletion process completed.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void deletePDB(Connection connection, String pdbName) {
        String closePdbSQL = "ALTER PLUGGABLE DATABASE " + pdbName + " CLOSE IMMEDIATE";
        String unplugPdbSQL = "ALTER PLUGGABLE DATABASE " + pdbName + " UNPLUG INTO 'pdb_" + pdbName + ".xml'";
        String dropPdbSQL = "DROP PLUGGABLE DATABASE " + pdbName + " INCLUDING DATAFILES";

        try (Statement statement = connection.createStatement()) {
            System.out.println("Closing PDB: " + pdbName);
            statement.execute(closePdbSQL);
            System.out.println("Unplugging PDB: " + pdbName);
            statement.execute(unplugPdbSQL);
            System.out.println("Dropping PDB: " + pdbName);
            statement.execute(dropPdbSQL);
            System.out.println("Successfully deleted PDB: " + pdbName);
        } catch (SQLException e) {
            System.err.println("Error processing PDB " + pdbName + ": " + e.getMessage());
        }
    }
}

