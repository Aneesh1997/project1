package exceptions;

/**
 *  This exception is thrown when a user tries to run the simulation with fragile enabled but caution mode disabled
 */
public class CautionDisabledFragileEnabledException extends Exception {
    public CautionDisabledFragileEnabledException() { super("Caution mode is disabled but fragile items are enabled!");}
}
