import core.Helper;
import view.HomepageUI;

import java.awt.*;
import java.sql.SQLException;

public class App {
    public static void main(String[] args) throws SQLException {
        Helper.setTheme();

       // Background bg = new Background("C:\\Users\\Yusuf\\IdeaProjects\\RecipeGuide\\src\\img\\background.jpg");
        HomepageUI homepageUI = new HomepageUI();

       // homepageUI.add(bg);

    }
}
