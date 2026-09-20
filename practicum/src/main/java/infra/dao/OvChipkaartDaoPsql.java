package infra.dao;

import domain.*;

import java.sql.*;
import java.util.List;

public class OvChipkaartDaoPsql implements IOvChipkaartDao {

    public OvChipkaartDaoPsql(Connection connection) {

    }
    @Override
    public boolean save(OvChipkaart ovChipkaart) throws SQLException {
        return false;
    }

    @Override
    public boolean update(OvChipkaart ovChipkaart) throws SQLException {
        return false;
    }

    @Override
    public boolean delete(OvChipkaart ovChipkaart) throws SQLException {
        return false;
    }

    @Override
    public OvChipkaart findById(int id) throws SQLException {
        return null;
    }

    @Override
    public List<OvChipkaart> findByReiziger(Reiziger reiziger) throws SQLException {
        return null;
    }

    @Override
    public List<OvChipkaart> findAll() throws SQLException {
        return null;
    }

    public void setProductDao(IProductDao productDao) {

    }
}
