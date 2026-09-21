import domain.Adres;
import domain.Reiziger;
import globals.Database;
import infra.dao.AdresDaoPsql;
import infra.dao.IAdresDao;
import infra.dao.IReizigerDao;
import infra.dao.ReizigerDaoPsql;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

/**
 * P2 en P3 - Persistentie volgens het DAO-pattern.
 * Tests voor de CRUD-operaties van ReizigerDAOPsql en AdresDAOPsql.
 */
public class Main {

    private static final String DB_NAME = "ovchip";

    private static Connection connection;

    public static void main(String[] args) {
        try {
            getConnection();

            ReizigerDaoPsql rdao = new ReizigerDaoPsql(connection);
            AdresDaoPsql adao = new AdresDaoPsql(connection);
            rdao.setAdresDao(adao);
            adao.setReizigerDao(rdao);

            testReizigerDAO(rdao);
            testAdresDAO(adao, rdao);
        } catch (SQLException e) {
            System.err.println("Verbinden met of benaderen van de database is mislukt: " + e.getMessage());
        } finally {
            closeConnection();
        }
    }


    private static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            Properties props = new Properties();
            props.setProperty("user", Database.dbUserName);
            props.setProperty("password", Database.dbPassword);
            connection = DriverManager.getConnection(Database.rootDbConnection + DB_NAME, props);
        }
        return connection;
    }

    private static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            System.err.println("Sluiten van de verbinding is mislukt: " + e.getMessage());
        }
    }

    /**
     * P2.d - Test de methoden van ReizigerDAO.
     */
    private static void testReizigerDAO(IReizigerDao rdao) throws SQLException {
        System.out.println("\n---------- Test ReizigerDAO -------------");

        // Haal alle reizigers op uit de database
        List<Reiziger> reizigers = rdao.findAll();
        System.out.println("[Test] ReizigerDAO.findAll() geeft de volgende reizigers:");
        for (Reiziger r : reizigers) {
            System.out.println(r);
        }
        System.out.println();

        // Maak een nieuwe reiziger aan en persisteer deze in de database
        Reiziger sietske = new Reiziger(100, "S", "", "Boers", Date.valueOf("2003-03-14"));
        System.out.print("[Test] Eerst " + reizigers.size() + " reizigers, na ReizigerDAO.save() ");
        rdao.save(sietske);
        reizigers = rdao.findAll();
        System.out.println(reizigers.size() + " reizigers\n");

        // Haal de zojuist opgeslagen reiziger op via zijn id
        System.out.println("[Test] ReizigerDAO.findById(100) geeft: " + rdao.findById(100) + "\n");

        // Wijzig de achternaam en persisteer de wijziging
        sietske.setAchternaam("Boersma");
        rdao.update(sietske);
        System.out.println("[Test] ReizigerDAO.update() geeft findById(100): " + rdao.findById(100) + "\n");

        // Zoek alle reizigers met een bepaalde geboortedatum
        Date gbdatum = Date.valueOf("2002-12-03");
        System.out.println("[Test] ReizigerDAO.findByGbdatum(" + gbdatum + ") geeft de volgende reizigers:");
        for (Reiziger r : rdao.findByGbdatum(gbdatum)) {
            System.out.println(r);
        }
        System.out.println();

        // Verwijder de reiziger weer uit de database
        System.out.print("[Test] Eerst " + reizigers.size() + " reizigers, na ReizigerDAO.delete() ");
        rdao.delete(sietske);
        reizigers = rdao.findAll();
        System.out.println(reizigers.size() + " reizigers\n");
    }

    /**
     * P3 - Test de CRUD-operaties van AdresDAO, inclusief findByReiziger().
     */
    private static void testAdresDAO(IAdresDao adao, IReizigerDao rdao) throws SQLException {
        System.out.println("\n---------- Test AdresDAO -------------");

        // Haal alle adressen op uit de database
        List<Adres> adressen = adao.findAll();
        System.out.println("[Test] AdresDAO.findAll() geeft de volgende adressen:");
        for (Adres a : adressen) {
            System.out.println(a);
        }
        System.out.println();

        // Persisteer een nieuw adres.
        Reiziger albrechts = rdao.findById(6);
        Adres nieuwAdres = new Adres(101, "1234AB", "12", "Teststraat", "Utrecht");
        nieuwAdres.setReiziger(albrechts);
        albrechts.setAdres(nieuwAdres);

        System.out.print("[Test] Eerst " + adressen.size() + " adressen, na AdresDAO.save() ");
        boolean opgeslagen = adao.save(nieuwAdres);
        adressen = adao.findAll();
        System.out.println(adressen.size() + " adressen (save gaf " + opgeslagen + " terug)\n");

        // Haal het zojuist opgeslagen adres op via zijn id
        System.out.println("[Test] AdresDAO.findById(101) geeft: " + adao.findById(101) + "\n");

        // Wijzig het adres en persisteer de wijziging
        nieuwAdres.setStraat("Arnhemseweg");
        nieuwAdres.setWoonplaats("Amersfoort");
        nieuwAdres.setPostcode("3817CH");
        boolean gewijzigd = adao.update(nieuwAdres);
        System.out.println("[Test] AdresDAO.update() (gaf " + gewijzigd + " terug) geeft findById(101): "
                + adao.findById(101) + "\n");

        // Zoek het adres op via de reiziger die eraan hangt
        System.out.println("[Test] AdresDAO.findByReiziger(" + albrechts.getNaam() + ") geeft: "
                + adao.findByReiziger(albrechts) + "\n");

        // Verwijder het adres weer uit de database
        System.out.print("[Test] Eerst " + adressen.size() + " adressen, na AdresDAO.delete() ");
        boolean verwijderd = adao.delete(nieuwAdres);
        albrechts.setAdres(null);
        adressen = adao.findAll();
        System.out.println(adressen.size() + " adressen (delete gaf " + verwijderd + " terug)\n");
    }
}
