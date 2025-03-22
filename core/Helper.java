package core;
import javax.swing.*;

public class Helper {
    public static void setTheme(){
        for( UIManager.LookAndFeelInfo info :UIManager.getInstalledLookAndFeels()){
            if(info.getName().equals("Nimbus")) {
                try {
                    UIManager.setLookAndFeel(info.getClassName());
                } catch (ClassNotFoundException | UnsupportedLookAndFeelException | IllegalAccessException | RuntimeException |   InstantiationException e) {
                    e.printStackTrace();
                }
                break;
            }
            }

    }

    public static boolean isEmptyField(JTextField fields){
        return fields.getText().trim().isEmpty();
    }
    public static boolean isFieldListEmpty(JTextField[] fields){
        for (JTextField field : fields) {
            if(isEmptyField(field)){
                return true;
            }
        }
            return false;
    }

    public static void showMessage(String message){
        String msg;
        String title;

        switch (message)
        {
            case "fill":
                msg = "Fill all fields";
                title = "Error";
                break;

            case "done":
                msg = "Process Success";
                title = "Result";
                break;

            case "error":
                msg = "Something went wrong";
                title = "Error";
                break;

            default:
                msg = message;
                title ="Message";
        }

        JOptionPane.showMessageDialog(null, msg, title, JOptionPane.INFORMATION_MESSAGE);
    }
    
    public static boolean approve(String text)
    {
        optionPaneDialog();
        String message;

        if(text.equals("sure")) {
            message="Are you sure about that operation?";
        }
        else {
            message=text;
        }
        return JOptionPane.showConfirmDialog(null, message, "Are you sure?", JOptionPane.YES_NO_OPTION) == 0;
    }
    
    public static void optionPaneDialog(){
        UIManager.put("OptionPane.okButtonText","Okey");
        UIManager.put("OptionPane.yesButtonText","Yes");
        UIManager.put("OptionPane.noButtonText","No");
    }

}
