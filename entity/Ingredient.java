package entity;

public class Ingredient {
    private int id;
    private String name;
    private int total_amount;
    private String unit_price;
    private UNIT unit;

    public enum UNIT {
        Kg,
        Grams,
        L,
        Piece

    }
        public Ingredient() {

        }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(int total_amount) {
        this.total_amount = total_amount;
    }

    public String getUnit_price() {
        return unit_price;
    }

    public void setUnit_price(String unit_price) {
        this.unit_price = unit_price;
    }

    public UNIT getUnit() {
        return unit;
    }

    public void setUnit(UNIT unit) {
        this.unit = unit;
    }

    @Override
    public String toString() {
        return "ingredients{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", total_amount=" + total_amount +
                ", unit_price='" + unit_price + '\'' +
                ", unit=" + unit +
                '}';
    }

    public static Ingredient.UNIT fromDatabaseValue(String dbValue) {

        switch (dbValue) {
            case "Kg":
                return Ingredient.UNIT.Kg;
            case "Grams":
                return Ingredient.UNIT.Grams;
            case "L":
                return Ingredient.UNIT.L;
            case "Piece":
               return Ingredient.UNIT.Piece;

            default:
                throw new IllegalArgumentException("Unknown Unit: " + dbValue);
        }
    }
}
