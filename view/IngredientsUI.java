package view;

import business.IngredientController;
import business.RecipeController;
import core.Helper;
import dao.IngredientDao;
import entity.Ingredient;
import entity.Recipe;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

public class IngredientsUI extends JFrame {

    private JPanel container;
    private JTextField fld_ingredient_name;
    private JComboBox cmbx_unit;
    private JButton btn_save;
    private JLabel lbl_title;
    private JLabel lbl_name;
    private JLabel lbl_unit;
    private Ingredient ingredient;
    private IngredientController ingredientController;
    private RecipeController recipeController;
    private DefaultComboBoxModel<Ingredient.UNIT> units;
    private  Recipe recipe;
    public static boolean flag_refresh=true;

    public IngredientsUI(Ingredient ingredient,JCheckBox check_box_ingredients) {
        this.ingredient = ingredient;
        this.ingredientController = new IngredientController();
        this.recipeController = new RecipeController();
        this.recipe = new Recipe();

        this.add(container);
        this.setTitle("Ingredient Add/Edit");
        this.setSize(300, 300);

        int x = (Toolkit.getDefaultToolkit().getScreenSize().width - this.getSize().width) / 2;
        int y = (Toolkit.getDefaultToolkit().getScreenSize().height - this.getSize().height) / 2;
        this.setLocation(x, y);
        this.setVisible(true);

        this.cmbx_unit.setModel(new DefaultComboBoxModel<>(Ingredient.UNIT.values()));

        this.btn_save.addActionListener(e->{
            if(Helper.isEmptyField(this.fld_ingredient_name)){
                Helper.showMessage("fiil");
            }
            else{
                boolean result = false;
                this.ingredient.setName(this.fld_ingredient_name.getText());
                this.ingredient.setUnit((Ingredient.UNIT) this.cmbx_unit.getSelectedItem());
                result = this.ingredientController.saveUnit(this.ingredient);

                if(result){
                    Helper.showMessage("done");
                    ingredientController.fetchIngredients();
                        flag_refresh=true;

                    ArrayList<JCheckBox> checkBoxIngredient = new ArrayList<>();
                    ArrayList<Ingredient> ingredientID=new ArrayList<>();
                    checkBoxIngredient = recipe.checkDuplicateIngredient(checkBoxIngredient, false, check_box_ingredients);

                    ArrayList<Ingredient> selectedIngredients = recipeController.findIngredientByRecipeID(recipe.getId());
                    //refresh
                    dispose();
                }
                else{
                    Helper.showMessage("error");
                }
            }

        });
    }

}