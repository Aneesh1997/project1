package automail;

public class CautionMode {
    private boolean isCautionOn;

    public CautionMode(boolean enabled) {
        this.isCautionOn = enabled;
    }

    public boolean checkCautionMode() {
        return isCautionOn;
    }
}
