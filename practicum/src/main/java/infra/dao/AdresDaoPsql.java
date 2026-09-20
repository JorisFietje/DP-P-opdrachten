package infra.dao;

import domain.Adres;
import domain.Reiziger;

import java.sql.*;
import java.util.List;

public class AdresDaoPsql implements IAdresDao {

    public AdresDaoPsql(Connection connection) {

    }

    @Override
    public boolean save(Adres adres) throws SQLException {
        return false;
    }

    @Override
    public boolean update(Adres adres) throws SQLException {
        return false;
    }

    @Override
    public boolean delete(Adres adres) throws SQLException {
        return false;
    }

    @Override
    public Adres findById(int id) throws SQLException {
        return null;
    }

    @Override
    public Adres findByReiziger(Reiziger reiziger) throws SQLException {
        return null;
    }

    @Override
    public List<Adres> findAll() throws SQLException {
        return null;
    }
}
