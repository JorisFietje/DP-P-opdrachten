package infra.dao;

import domain.*;

import java.sql.*;
import java.util.List;

public class ProductDaoPsql implements IProductDao {

    public ProductDaoPsql(Connection connection) {

    }

    @Override
    public boolean save(Product product) throws SQLException {
        return false;
    }

    @Override
    public boolean update(Product product) throws SQLException {
        return false;
    }

    @Override
    public boolean delete(Product product) throws SQLException {
        return false;
    }

    @Override
    public Product findById(int id) throws SQLException {
        return null;
    }

    @Override
    public List<Product> findByOvChipkaart(OvChipkaart ovChipkaart) throws SQLException {
        return null;
    }

    @Override
    public List<Product> findAll() throws SQLException {
        return null;
    }
}
