package automail;

public class CautionMode {
    private static boolean cautionEnabled;

    public CautionMode(boolean cautionEnabled) {
        this.cautionEnabled = cautionEnabled;
    }

    public static boolean isCautionEnabled() {
        return cautionEnabled;
    }
}
