package hacklab.util;


import java.awt.*;


public final class Theme {

    private Theme() {} 

    
    public static final Color BG_DARKEST = new Color(0x06, 0x08, 0x0A);
    public static final Color BG_DARK    = new Color(0x0C, 0x10, 0x14);
    public static final Color BG_PANEL   = new Color(0x0F, 0x15, 0x1A);
    public static final Color BG_CARD    = new Color(0x12, 0x1A, 0x22);
    public static final Color BG_HOVER   = new Color(0x18, 0x24, 0x2E);

  
    public static final Color ACCENT_CYAN   = new Color(0x00, 0xF5, 0xC8);
    public static final Color ACCENT_GREEN  = new Color(0x00, 0xE5, 0x70);
    public static final Color ACCENT_RED    = new Color(0xFF, 0x33, 0x55);
    public static final Color ACCENT_YELLOW = new Color(0xFF, 0xCC, 0x00);
    public static final Color ACCENT_BLUE   = new Color(0x00, 0xB4, 0xFF);

   
    public static final Color TEXT_PRIMARY   = new Color(0xE8, 0xF4, 0xF8);
    public static final Color TEXT_SECONDARY = new Color(0x7A, 0x9A, 0xAB);
    public static final Color TEXT_DIM       = new Color(0x3A, 0x52, 0x60);
    public static final Color TEXT_SUCCESS   = new Color(0x00, 0xF5, 0xC8);
    public static final Color TEXT_WARNING   = new Color(0xFF, 0xCC, 0x00);
    public static final Color TEXT_ERROR     = new Color(0xFF, 0x44, 0x55);
    public static final Color TEXT_CMD       = new Color(0x00, 0xE5, 0x70);
    public static final Color TEXT_INFO      = new Color(0x88, 0xBB, 0xCC);

   
    public static final Color BORDER_DIM    = new Color(0x1A, 0x2A, 0x35);
    public static final Color BORDER_BRIGHT = new Color(0x00, 0xF5, 0xC8, 60);

 
    public static final Font FONT_MONO       = new Font("Consolas", Font.PLAIN, 13);
    public static final Font FONT_MONO_LARGE = new Font("Consolas", Font.BOLD,  16);
    public static final Font FONT_MONO_SMALL = new Font("Consolas", Font.PLAIN, 11);
    public static final Font FONT_TITLE      = new Font("Consolas", Font.BOLD,  22);

    public static Color withAlpha(Color base, int alpha) {
        return new Color(base.getRed(), base.getGreen(), base.getBlue(), alpha);
    }
}
