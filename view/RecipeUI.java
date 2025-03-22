package view;

import business.IngredientController;
import business.RecipeController;
import core.Helper;
import entity.Ingredient;
import entity.Recipe;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class RecipeUI  extends JFrame {
    public static ArrayList<Integer> ingredient_id = new ArrayList<>();
    private JPanel container;
    private JTextField fld_recipe_name;
    private JComboBox<Recipe.TYPE> cmbx_category;
    private JTextField fld_preparation_time;
    private JTextArea txt_area_instructions;
    private JButton btn_save;
    private JLabel lbl_recipe_title;
    private JLabel lbl_recipe_name;
    private JLabel lbl_recipe_category;
    private JLabel lbl_preparation_time;
    private JLabel lbl_area_instructions;
    private JComboBox cmbx_ingredients;
    private JButton btn_add_ingredients;
    private JCheckBox check_box_ingredients;
    private JLabel lbl_recipe_img;
    private Recipe recipe;
    private RecipeController recipeController;
    private IngredientController ingredientController;


    public RecipeUI(Recipe recipe, boolean flag) {
        this.recipe = recipe;
        this.recipeController = new RecipeController();
        this.ingredientController = new IngredientController();

        this.add(container);
        this.setTitle("Recipe Add/Edit");
        this.setSize(800, 700);

        int x = (Toolkit.getDefaultToolkit().getScreenSize().width - this.getSize().width) / 2;
        int y = (Toolkit.getDefaultToolkit().getScreenSize().height - this.getSize().height) / 2;
        this.setLocation(x, y);
        this.setVisible(true);


        this.cmbx_category.setModel(new DefaultComboBoxModel<>(Recipe.TYPE.values()));
        ArrayList<JCheckBox> checkBoxIngredient = new ArrayList<>();
        checkBoxIngredient = recipe.checkDuplicateIngredient(checkBoxIngredient, flag, check_box_ingredients);


        check_box_ingredients.setLayout(new BoxLayout(check_box_ingredients, BoxLayout.Y_AXIS));
        if (this.recipe.getId() == 0) {
            this.lbl_recipe_title.setText("Recipe Add");
        } else {
            this.lbl_recipe_title.setText("Recipe Edit");
            this.fld_recipe_name.setText(this.recipe.getName());

            this.fld_preparation_time.setText(String.valueOf(this.recipe.getPreparation_time()));
            this.cmbx_category.getModel().setSelectedItem(this.recipe.getCategory());
            this.txt_area_instructions.setText(this.recipe.getInstructions());

            int width = 500; 
            int height = 200; 

            ImageIcon originalIcon = new ImageIcon("src/img/" + this.recipe.getCategory() + "/" + this.recipe.getName() + ".jpg");
            Image scaledImage = originalIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
            this.lbl_recipe_img.setIcon(new ImageIcon(scaledImage));
            ArrayList<Ingredient> selectedIngredients = this.recipeController.findIngredientByRecipeID(this.recipe.getId());
            recipe.loadIngredientDistinct(checkBoxIngredient, selectedIngredients, flag);

        }
        btn_save.addActionListener(e -> {
            JTextField[] checkList = {this.fld_recipe_name, this.fld_preparation_time};
            if (Helper.isFieldListEmpty(checkList)) {
                Helper.showMessage("fiil");
            } else {
                boolean result = false;

                this.recipe.setName(this.fld_recipe_name.getText());
                this.recipe.setInstructions(this.txt_area_instructions.getText());
                this.recipe.setPreparation_time(this.fld_preparation_time.getText());
                this.recipe.setCategory((Recipe.TYPE) this.cmbx_category.getSelectedItem());

                if (this.recipe.getId() == 0) {
                    result = this.recipeController.saveRecipe(this.recipe, ingredient_id);

                } else {
                    result = this.recipeController.updateRecipe(this.recipe);
                }

                if (result) {
                    Helper.showMessage("done");
                    dispose();

                } else {
                    Helper.showMessage("error");
                }
            }
        });
        btn_add_ingredients.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                IngredientsUI ingredientsUI = new IngredientsUI(new Ingredient(),check_box_ingredients);


            }
        });
    }

}
