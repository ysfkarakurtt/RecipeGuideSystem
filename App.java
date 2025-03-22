import core.Helper;
import view.HomepageUI;

import java.awt.*;
import java.sql.SQLException;

public class App {
    public static void main(String[] args) throws SQLException {
        Helper.setTheme();
        HomepageUI homepageUI = new HomepageUI();
    }
}
