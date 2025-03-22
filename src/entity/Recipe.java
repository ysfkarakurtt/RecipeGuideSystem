package entity;
import business.IngredientController;
import core.Helper;
import view.RecipeUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

public class Recipe {
        private int id;
        private String name;
        private TYPE category;
        private String preparation_time;
        private String instructions;
        private String img_path;
        public static ArrayList<Integer> ingredientID =new ArrayList<>();
        public static  ArrayList<String> ingredientName = new ArrayList<>();
        IngredientController ingredientController = new IngredientController();

        public enum TYPE{
           Main_Dishes,
            Sweets,
            Soups,
            Fishes,
            Hot_Appetizers
        }
        public Recipe(){
            this.ingredientController = new IngredientController();
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

    public String getImg_path() {
        return img_path;
    }

    public void setImg_path(String img_path) {
        this.img_path = img_path;
    }

    public TYPE getCategory() {
        return category;
    }

    public void setCategory(TYPE category) {
        this.category = category;
    }

     public String getPreparation_time() {
            return preparation_time;
        }

        public void setPreparation_time(String preparation_time) {
            this.preparation_time = preparation_time;
        }

        public String getInstructions() {
            return instructions;
        }

        public void setInstructions(String instructions) {
            this.instructions = instructions;
        }

    @Override
    public String toString() {
        return "Recipe{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", category=" + category +
                ", preparation_time='" + preparation_time + '\'' +
                ", instructions='" + instructions + '\'' +
                ", img_path='" + img_path + '\'' +
                ", ingredientController=" + ingredientController +
                '}';
    }

    public static TYPE fromDatabaseValue(String dbValue) {

            switch (dbValue) {
                case "Main_Dishes":
                    return TYPE.Main_Dishes;
                case "Sweets":
                    return TYPE.Sweets;
                case "Soups":
                    return TYPE.Soups;
                case "Fishes":
                    return TYPE.Fishes;
                case "Hot_Appetizers":
                    return TYPE.Hot_Appetizers;

            default:
                throw new IllegalArgumentException("Bilinmeyen kategori: " + dbValue);

            }
    }


    public String toDatabaseValue(TYPE category) {
        switch (category) {
                case Main_Dishes:
                    return "Main_Dishes";
                case Sweets:
                    return "Sweets";
                case Soups:
                    return "Soups";
                case Fishes:
                    return "Fishes";
                case Hot_Appetizers:
                    return "Hot_Appetizers";
            default:
                throw new IllegalArgumentException("Bilinmeyen kategori: " + this);
        }
    }


    public ArrayList<JCheckBox> checkDuplicateIngredient(ArrayList<JCheckBox> checkBoxIngredient, boolean flag,JCheckBox check_box_ingredients) {
        ArrayList<Ingredient> ingredients =this.ingredientController.fetchIngredients();
        ArrayList<String> ingredientNames = new ArrayList<>();

        for (Ingredient ingredient : ingredients) {
            ingredientNames.add(ingredient.getName());
        }
        ArrayList<JCheckBox> check_box_ingredientsList = new ArrayList<>();

        for (Ingredient ingredient : ingredients) {
            JCheckBox checkBox = new JCheckBox(ingredient.getName());

            boolean exists = false;

            for (JCheckBox c1 : check_box_ingredientsList) {

                if (c1.getText().equals(checkBox.getText())) {

                    exists = true;
                    break;
                }
            }

            if (!exists) {
                check_box_ingredientsList.add(checkBox);
                check_box_ingredients.add(checkBox);

                checkBox.addItemListener(e -> {
                    if (checkBox.isSelected()) {
                    	
                       ingredientID(ingredient.getId());
                       ingredientName.add(ingredient.getName());

                        if(flag==false) {
                            createNewWindow(checkBox, ingredient,checkBoxIngredient,flag);
                        }
                    }
                });
            }
        }

        return check_box_ingredientsList;
    }
    
    public void ingredientID(int id)
    {
       ingredientID.add(id);
    }
    public void ingredientName(String name)
    {
        ingredientName.add(name);
    }

    public void loadIngredientDistinct(ArrayList<JCheckBox> checkBoxIngredient,ArrayList<Ingredient> selectedIngredients,boolean flag) {
        for (JCheckBox checkBox : checkBoxIngredient) {
            for (Ingredient ingredient : selectedIngredients) {
                if (checkBox.getText().equals(ingredient.getName())) {
                    checkBox.setSelected(true);
                    flag=true;
                }
            }
        }
    }
    private void createNewWindow(JCheckBox checkBox, Ingredient ingredient,ArrayList<JCheckBox> checkBoxIngredient,boolean flag) {
        JFrame newFrame = new JFrame("Ingredient Details");
        newFrame.setSize(300, 250);
        JPanel panel = new JPanel();
        newFrame.setLayout(new GridLayout(4, 1));
        JLabel lbl_amount = new JLabel( checkBox.getText()+ " Amount :");
        JTextField fld_amount = new JTextField();
        JPanel panel2 = new JPanel();
        JLabel lbl_unit_price = new JLabel( "Unit Price:");
        JTextField fld_unit_price = new JTextField();
        JButton saveButton = new JButton("Save");

        panel.add(lbl_amount);
        panel2.add(lbl_unit_price);
        panel2.add(fld_unit_price);
        panel.add(fld_amount);
        panel2.add(saveButton);

        newFrame.add(panel);
        newFrame.add(panel2);

        newFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {

                checkBox.setSelected(false);
            }
        });
        saveButton.addActionListener(e -> {
            String input_amount = fld_amount.getText();
            String input_unit_price = fld_unit_price.getText();
            ingredient.setTotal_amount(Integer.parseInt(fld_amount.getText().toString()));
            ingredient.setUnit_price(fld_unit_price.getText().toString());

            if(input_amount.isEmpty() || input_unit_price.isEmpty()){
                Helper.showMessage("fiil");
            }else{
                int result;
                result = this.ingredientController.saveAmount(ingredient);

                if(result>0){
                    Helper.showMessage("done");
                    newFrame.dispose();
                    RecipeUI.ingredient_id.add(result);
                }
                else{
                    Helper.showMessage("error");
                }
            }
        });

        newFrame.add(lbl_amount);
        newFrame.add(lbl_unit_price);
        newFrame.add(fld_amount);
        newFrame.add(fld_unit_price);
        newFrame.add(saveButton);

        newFrame.setLocationRelativeTo(null);
        newFrame.setVisible(true);
    }
    }

