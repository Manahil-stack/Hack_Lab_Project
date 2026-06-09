package hacklab;


import hacklab.ui.SplashScreen;
import javax.swing.SwingUtilities;


public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SplashScreen splash = new SplashScreen();
            splash.showAndProceed();
        });
    }
}