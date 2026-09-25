package infra.dao;

import domain.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OvChipkaartDaoPsql implements IOvChipkaartDao {

    private final Connection connection;
    private IProductDao productDao;

    public OvChipkaartDaoPsql(Connection connection) {
        this.connection = connection;
    }

    @Override
    public boolean save(OvChipkaart ovChipkaart) throws SQLException {
        boolean gelukt;
        String sql = "INSERT INTO ov_chipkaart (kaart_nummer, geldig_tot, klasse, saldo, reiziger_id) "
                + "VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pst = connection.prepareStatement(sql)) {
            pst.setInt(1, ovChipkaart.getKaartNummer());
            pst.setDate(2, ovChipkaart.getGeldigTot());
            pst.setInt(3, ovChipkaart.getKlasse());
            pst.setDouble(4, ovChipkaart.getSaldo());
            pst.setInt(5, ovChipkaart.getReiziger().getReizigerId());
            gelukt = pst.executeUpdate() > 0;
        }
        for (Product product : ovChipkaart.getProducten()) {
            gelukt = productDao.save(product) && gelukt;
        }
        return gelukt;
    }

    @Override
    public boolean update(OvChipkaart ovChipkaart) throws SQLException {
        boolean gelukt;
        String sql = "UPDATE ov_chipkaart "
                + "SET geldig_tot = ?, klasse = ?, saldo = ?, reiziger_id = ? "
                + "WHERE kaart_nummer = ?";
        try (PreparedStatement pst = connection.prepareStatement(sql)) {
            pst.setDate(1, ovChipkaart.getGeldigTot());
            pst.setInt(2, ovChipkaart.getKlasse());
            pst.setDouble(3, ovChipkaart.getSaldo());
            pst.setInt(4, ovChipkaart.getReiziger().getReizigerId());
            pst.setInt(5, ovChipkaart.getKaartNummer());
            gelukt = pst.executeUpdate() > 0;
        }
        for (Product product : ovChipkaart.getProducten()) {
            gelukt = productDao.update(product) && gelukt;
        }
        return gelukt;
    }

    @Override
    public boolean delete(OvChipkaart ovChipkaart) throws SQLException {
        String koppelSql = "DELETE FROM ov_chipkaart_product WHERE kaart_nummer = ?";
        try (PreparedStatement pst = connection.prepareStatement(koppelSql)) {
            pst.setInt(1, ovChipkaart.getKaartNummer());
            pst.executeUpdate();
        }

        String sql = "DELETE FROM ov_chipkaart WHERE kaart_nummer = ?";
        try (PreparedStatement pst = connection.prepareStatement(sql)) {
            pst.setInt(1, ovChipkaart.getKaartNummer());
            return pst.executeUpdate() > 0;
        }
    }

    @Override
    public OvChipkaart findById(int id) throws SQLException {
        String sql = "SELECT * FROM ov_chipkaart WHERE kaart_nummer = ?";
        try (PreparedStatement pst = connection.prepareStatement(sql)) {
            pst.setInt(1, id);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return buildOvChipkaart(rs);
                }
                return null;
            }
        }
    }

    @Override
    public List<OvChipkaart> findByReiziger(Reiziger reiziger) throws SQLException {
        String sql = "SELECT * FROM ov_chipkaart WHERE reiziger_id = ?";
        List<OvChipkaart> ovChipkaarten = new ArrayList<>();
        try (PreparedStatement pst = connection.prepareStatement(sql)) {
            pst.setInt(1, reiziger.getReizigerId());
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    OvChipkaart ovChipkaart = leesOvChipkaart(rs);
                    ovChipkaart.setReiziger(reiziger);
                    ovChipkaarten.add(ovChipkaart);
                }
            }
        }
        if (productDao != null) {
            for (OvChipkaart ovChipkaart : ovChipkaarten) {
                ovChipkaart.setProducten(productDao.findByOvChipkaart(ovChipkaart));
            }
        }
        return ovChipkaarten;
    }

    @Override
    public List<OvChipkaart> findAll() throws SQLException {
        String sql = "SELECT * FROM ov_chipkaart";
        List<OvChipkaart> ovChipkaarten = new ArrayList<>();
        try (PreparedStatement pst = connection.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                ovChipkaarten.add(buildOvChipkaart(rs));
            }
        }
        return ovChipkaarten;
    }

    private OvChipkaart buildOvChipkaart(ResultSet rs) throws SQLException {
        OvChipkaart ovChipkaart = leesOvChipkaart(rs);
        if (productDao != null) {
            ovChipkaart.setProducten(productDao.findByOvChipkaart(ovChipkaart));
        }
        Reiziger reiziger = new Reiziger();
        reiziger.setReizigerId(rs.getInt("reiziger_id"));
        ovChipkaart.setReiziger(reiziger);
        return ovChipkaart;
    }

    private OvChipkaart leesOvChipkaart(ResultSet rs) throws SQLException {
        return new OvChipkaart(
                rs.getInt("kaart_nummer"),
                rs.getDate("geldig_tot"),
                rs.getInt("klasse"),
                rs.getDouble("saldo")
        );
    }

    public void setProductDao(IProductDao productDao) {
        this.productDao = productDao;
    }
}