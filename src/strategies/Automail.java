package strategies;

import automail.IMailDelivery;
import automail.MovementController;
import automail.Robot;

public class Automail {
	      
    public static Robot[] robots;
    public IMailPool mailPool;
    public static MovementController movementController;
    
    public Automail(IMailPool mailPool, IMailDelivery delivery, int numRobots) {
    	// Swap between simple provided strategies and your strategies here
    	    	
    	/** Initialize the MailPool */
    	
    	this.mailPool = mailPool;
    	
    	/** Initialize robots */
    	robots = new Robot[numRobots];
    	for (int i = 0; i < numRobots; i++) robots[i] = new Robot(delivery, mailPool);
        movementController = new MovementController(robots);
    }
    
}
