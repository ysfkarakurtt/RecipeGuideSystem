package view;

import business.RecipeController;
import core.Helper;
import entity.Ingredient;
import entity.Recipe;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.ArrayList;

public class HomepageUI extends JFrame {
    private JPanel container;
    private JLabel lbl_title;
    private JPanel pnl_top;
    private JPanel pnl_bottom;
    private JTabbedPane tab_homepage;
    private JPanel pnl_recipes;
    private JLabel lbl_recipes;
    private JTable tbl_recipes;
    private JLabel lbl_recipe_name;
    private JButton btn_search;
    private JTextField fld_find_recipe;
    private JButton btn_add_recipe;
    private JButton btn_refresh_recipe;
    private JLabel lbl_recipe_operation;
    private JTable tbl_recipesBySearch;
    private JComboBox <Recipe.TYPE> cmbx_category;
    private JCheckBox checkbox_ingredient_filter;
    private JButton btn_filter_search;
    private JTable tbl_ingredient_filter;
    private JLabel lbl_category;
    private JComboBox cmbx_filter_option;
    private JLabel lbl_filter_option;
    private JPanel checkbox_;
    private JLabel lbl_title_table;
    private RecipeController recipeController;
    private DefaultTableModel tmdl_recipes = new DefaultTableModel();
    private DefaultTableModel tmdl_recipesBySearch = new DefaultTableModel();
    private DefaultTableModel tmdl_ingredient_filter = new DefaultTableModel();
    private JPopupMenu popup_recipe = new JPopupMenu();
    private Recipe recipe;

    public HomepageUI() throws SQLException {

        this.recipeController  = new RecipeController();
        this.recipe = new Recipe();
        this.add(container);
        this.setTitle("Recipe Guide");
        this.setSize(1000,700);
        this.setBackground(Color.white);

        int x =(Toolkit.getDefaultToolkit().getScreenSize().width-this.getSize().width)/2;
        int y =(Toolkit.getDefaultToolkit().getScreenSize().height-this.getSize().height)/2;
        this.setLocation(x,y);
        this.setVisible(true);

        this.btn_search.addActionListener( e->{

                JTextField[] checkList = {fld_find_recipe};
                JComboBox[] checkList2 = {cmbx_category};
            if(Helper.isFieldListEmpty(checkList) && this.cmbx_category.getSelectedItem()== null && this.cmbx_filter_option.getSelectedIndex()== 0){
                Helper.showMessage("fill");
            }
            else{
                try {
                    ArrayList<Recipe> recipe= this.recipeController.findAllRecipeBySearch(this.fld_find_recipe.getText());
                    if(recipe == null){
                        Helper.showMessage("No food found according to the recipe you are looking for.");
                    }
                    else{

                        ArrayList<Recipe> filteredRecipes =this.recipeController.filter(
                                this.fld_find_recipe.getText(),
                                (Recipe.TYPE) this.cmbx_category.getSelectedItem(),
                                cmbx_filter_option.getSelectedIndex()
                        );
                        loadRecipesBySearch(filteredRecipes);

                    }
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }

        });

        loadRecipes(null);
        loadRecipesBySearch(null);
        loadRecipesPopupMenu();
        loadRecipeButtonEvent();
        
        this.cmbx_category.setModel(new DefaultComboBoxModel<>(Recipe.TYPE.values()));
        this.cmbx_category.setSelectedItem(null);
        checkbox_ingredient_filter.setLayout(new BoxLayout(checkbox_ingredient_filter, BoxLayout.Y_AXIS));
        ArrayList<JCheckBox> checkBoxIngredient = new ArrayList<>();
        checkBoxIngredient= recipe.checkDuplicateIngredient(checkBoxIngredient,true,checkbox_ingredient_filter);
        ArrayList <Ingredient> selectedIngredients=  this.recipeController.findIngredientByRecipeID(this.recipe.getId());
        recipe.loadIngredientDistinct(checkBoxIngredient,selectedIngredients,true);

        cmbx_filter_option.setSelectedIndex(0);
        Object[] column= {"ID","Recipe Name","Category","Preparation Time","Instructions"};
        this.tmdl_ingredient_filter.setColumnIdentifiers(column);
        this.tbl_ingredient_filter.setModel(this.tmdl_ingredient_filter);

        btn_filter_search.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadRecipes2(recipe.ingredientName);
            }
        });
    }
    private void loadRecipeButtonEvent(){
        this.btn_add_recipe.addActionListener(e ->{
            RecipeUI recipeUI = new RecipeUI(new Recipe(),false);

            recipeUI.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    loadRecipesBySearch(null);
                }
            });
        });
        this.btn_search.addActionListener(e ->  {
            ArrayList<Recipe> filteredRecipes =this.recipeController.filter(
                    this.fld_find_recipe.getText(),
                    (Recipe.TYPE) this.cmbx_category.getSelectedItem(),
                    cmbx_filter_option.getSelectedIndex()
            );
            loadRecipesBySearch(filteredRecipes);
        });

        btn_refresh_recipe.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadRecipesBySearch(null);
            }
        });
    }
    private void loadRecipesPopupMenu(){
        this.popup_recipe.add("Güncelle").addActionListener(e -> {

            int selectID= Integer.parseInt(tbl_recipesBySearch.getValueAt(tbl_recipesBySearch.getSelectedRow(), 0).toString());

            RecipeUI recipeUI = new RecipeUI(this.recipeController.findRecipeByID(selectID),true);

            recipeUI.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {

                    loadRecipesBySearch(null);
                }
            });
        });

        this.popup_recipe.add("Sil").addActionListener(e -> {
            int selectID= Integer.parseInt(tbl_recipesBySearch.getValueAt(tbl_recipesBySearch.getSelectedRow(), 0).toString());
            if(Helper.approve("sure")){
                if(this.recipeController.deleteRecipe(selectID)){
                    loadRecipesBySearch(null);
                }
                else{
                    Helper.showMessage("error");
                }
            }

        });
        this.tbl_recipesBySearch.setComponentPopupMenu(popup_recipe);

        this.tbl_recipesBySearch.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int selectedRow = tbl_recipesBySearch.rowAtPoint(e.getPoint());
                tbl_recipesBySearch.setRowSelectionInterval(selectedRow, selectedRow);
            }
        });
    }

     private void loadRecipes2(ArrayList<String> name)
    {
            ArrayList<Recipe> recipes=new ArrayList<>() ;
            Object[] column= {"ID","Recipe Name","Category","Preparation Time","Instructions"};


                    recipes = this.recipeController.findRecipeByIngredientID(name);
        if (recipes.isEmpty()) {

        } else {

            for (Recipe recipe : recipes) {
                if (recipe != null) {

                } else {
                 //   System.out.println("Found null recipe, jumping.");
                }
            }
        }

                DefaultTableModel clearModel = (DefaultTableModel) tbl_ingredient_filter.getModel();
                clearModel.setRowCount(0);
            this.tmdl_ingredient_filter.setColumnIdentifiers(column);
           for(Recipe r:recipes)
           {
               if (r != null) {
                   Object [] row ={r.getId(),r.getName(),r.getCategory(),r.getPreparation_time(),r.getInstructions()};
                   this.tmdl_ingredient_filter.addRow(row);

               } else {
            	   //   System.out.println("Found null recipe, jumping.");
               }

           }

           this.tbl_ingredient_filter.setModel(this.tmdl_ingredient_filter);
           this.tbl_ingredient_filter.getTableHeader().setReorderingAllowed(false);
           this.tbl_ingredient_filter.getColumnModel().getColumn(0).setPreferredWidth(50);
           this.tbl_ingredient_filter.setEnabled(false);

    }

    private void loadRecipes(ArrayList<Recipe> recipe)
    {

        Object[] column= {"ID","Recipe Name","Category","Preparation Time","Instructions"};
        if(recipe==null) {
            try {
                recipe = this.recipeController.findAllRecipe();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        DefaultTableModel clearModel = (DefaultTableModel) tbl_recipes.getModel();
        clearModel.setRowCount(0);
        this.tmdl_recipes.setColumnIdentifiers(column);
        for(Recipe r:recipe)
        {
            Object [] row ={r.getId(),r.getName(),r.getCategory(),r.getPreparation_time(),r.getInstructions()};
            this.tmdl_recipes.addRow(row);
        }

        this.tbl_recipes.setModel(this.tmdl_recipes);
        this.tbl_recipes.getTableHeader().setReorderingAllowed(false);
        this.tbl_recipes.getColumnModel().getColumn(0).setPreferredWidth(50);
        this.tbl_recipes.setEnabled(false);

    }

    private void loadRecipesBySearch(ArrayList<Recipe> recipe)
    {
        Object[] column= {"ID","Recipe Name","Category","Preparation Time","Instructions"};
        if(recipe==null) {
            try {
                recipe = this.recipeController.findAllRecipe();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        DefaultTableModel clearModel = (DefaultTableModel) tbl_recipesBySearch.getModel();
        clearModel.setRowCount(0);
        this.tmdl_recipesBySearch.setColumnIdentifiers(column);
        for(Recipe r:recipe)
        {
            Object [] row ={r.getId(),r.getName(),r.getCategory(),r.getPreparation_time(),r.getInstructions()};
            this.tmdl_recipesBySearch.addRow(row);
        }

        this.tbl_recipesBySearch.setModel(this.tmdl_recipesBySearch);
        this.tbl_recipesBySearch.getTableHeader().setReorderingAllowed(false);
        this.tbl_recipesBySearch.getColumnModel().getColumn(0).setPreferredWidth(50);
        this.tbl_recipesBySearch.setEnabled(false);

    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }

}
