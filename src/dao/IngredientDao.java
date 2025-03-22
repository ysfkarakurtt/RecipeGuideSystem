package dao;
import business.IngredientController;
import core.db;
import entity.Ingredient;
import entity.Recipe;

import java.sql.*;
import java.util.ArrayList;

public class IngredientDao {
    private Connection connection;

    public IngredientDao() {
        this.connection = db.getInstance();
    }
    
    public  boolean saveUnit(Ingredient ingredient) {
        String query = "INSERT INTO ingredient (name,unit) VALUES(?,?)";
        try {
            PreparedStatement pr = this.connection.prepareStatement(query);
            pr.setString(1, ingredient.getName());
            pr.setString(2,ingredient.getUnit().toString());

            return pr.executeUpdate() != 0;
        } catch (SQLException e) {
           e.printStackTrace();
        }
        return true;

    }

    public int saveAmount(Ingredient ingredient) {
        int generatedId = -1; 
        String query = "INSERT INTO ingredient (name, total_amount, unit, unit_price) VALUES(?,?,?,?)";

        try {
            PreparedStatement pr = this.connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            pr.setString(1, ingredient.getName());
            pr.setInt(2, ingredient.getTotal_amount());
            pr.setString(3, ingredient.getUnit().toString());
            pr.setString(4, ingredient.getUnit_price());

            int affectedRows = pr.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pr.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        generatedId = generatedKeys.getInt(1); 
                    }
                }
            } else {
                throw new SQLException("Ingredient kaydı eklenemedi, ID alınamadı.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return generatedId;
    }

    public boolean saveIngredient(Ingredient ingredient) {
        String query = "INSERT INTO ingredient (name,total_amount,unit,unit_price) VALUES(?,?,?,?)";
        try {
            PreparedStatement pr = this.connection.prepareStatement(query);

            return pr.executeUpdate() !=0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    public boolean updateIngredient(Ingredient ingredient)
    {
        String query="UPDATE ingredient SET name = ?, total_amount = ?, unit = ?, unit_price = ? WHERE id = ?";
        try {
            PreparedStatement pr = this.connection.prepareStatement(query);
            pr.setString(1, ingredient.getName());
            pr.setInt(2, ingredient.getTotal_amount());
            pr.setString(3, ingredient.getUnit().toString());
            pr.setString(4, ingredient.getUnit_price());
            pr.setInt(5, ingredient.getId());
            return pr.executeUpdate() !=-1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }
    public ArrayList<Ingredient> fetchIngredient() {
        String query="SELECT * FROM ingredient ";
        ArrayList<Ingredient> ingredients = new ArrayList<>();
        try {
            PreparedStatement pr = this.connection.prepareStatement(query);
            ResultSet rs = pr.executeQuery();
            while (rs.next()) {
               ingredients.add(this.matchIngredient(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ingredients;
    }

    public ArrayList<Ingredient> fetchIngredientDistinct() {
        String query="SELECT DISTINCT  name FROM ingredient ";
        ArrayList<Ingredient> ingredients = new ArrayList<>();
        try {
            PreparedStatement pr = this.connection.prepareStatement(query);
            ResultSet rs = pr.executeQuery();
            while (rs.next()) {
                ingredients.add(this.matchIngredient(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ingredients;
    }

    public Ingredient matchIngredient(ResultSet rs) throws SQLException {
        Ingredient ingredient = new Ingredient();
        ingredient.setId(rs.getInt("id"));
        ingredient.setName(rs.getString("name"));
        ingredient.setUnit((Ingredient.fromDatabaseValue(rs.getString("unit"))));

        String total_amount=rs.getString("total_amount");
        String unit_price=rs.getString("unit_price");
        
        if(total_amount== null ){
            total_amount="0";
            ingredient.setTotal_amount(Integer.parseInt(total_amount));
        }else {
            ingredient.setTotal_amount(Integer.parseInt(rs.getString("total_amount")));
        }
        
        if(unit_price==null){
            unit_price="0";
            ingredient.setTotal_amount(Integer.parseInt(total_amount));
        }
        else {
            ingredient.setTotal_amount(Integer.parseInt(rs.getString("unit_price")));
        }

        return ingredient;
    }
}
