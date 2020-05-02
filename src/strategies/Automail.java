package strategies;

import automail.IMailDelivery;
import automail.CautionRobot;
import automail.Robot;
import automail.CautionMode;

public class Automail {
	      
    public Robot[] robots;
    public IMailPool mailPool;
    
    public Automail(IMailPool mailPool, IMailDelivery delivery, int numRobots, CautionMode cautionEnabled) {
    	// Swap between simple provided strategies and your strategies here
    	    	
    	/** Initialize the MailPool */
    	
    	this.mailPool = mailPool;
    	
    	/** Initialize robots */
    	robots = new Robot[numRobots];
        if (cautionEnabled.checkCautionMode()) {
            for (int i = 0; i < numRobots; i++) robots[i] = new CautionRobot(delivery, mailPool);
        }
    	//for (int i = 0; i < numRobots; i++) robots[i] = new NormalRobot(delivery, mailPool);
    }
    
}
