package automail;

import exceptions.BreakingFragileItemException;
import exceptions.ExcessiveDeliveryException;
import exceptions.ItemTooHeavyException;
import strategies.Automail;
import strategies.IMailPool;
import strategies.MailPool;

import java.util.Map;
import java.util.TreeMap;

import static strategies.Automail.movementController;

/**
 * The robot delivers mail!
 */
public class Robot {
	
    static public final int INDIVIDUAL_MAX_WEIGHT = 2000;

    IMailDelivery delivery;
    protected final String id;
    /** Possible states the robot can be in */
    public enum RobotState { DELIVERING, WAITING, RETURNING, CAUTION}
    public RobotState current_state;
    private int current_floor;
    private int destination_floor;
    private IMailPool mailPool;
    private boolean receivedDispatch;

    private boolean wrapping_flag;
    private int wrapping_turns;
    private int unwrapping_turns;

    private MailItem deliveryItem = null;
    private MailItem normalHands = null;
    private MailItem tube = null;
    private MailItem specialHands = null;

    private boolean move;

//    private MailItem deliveryItem = null
    private int deliveryCounter;
    

    /**
     * Initiates the robot's location at the start to be at the mailroom
     * also set it to be waiting for mail.
     * @param behaviour governs selection of mail items for delivery and behaviour on priority arrivals
     * @param delivery governs the final delivery
     * @param mailPool is the source of mail items
     */
    public Robot(IMailDelivery delivery, IMailPool mailPool){
    	id = "R" + hashCode();
        // current_state = RobotState.WAITING;
    	current_state = RobotState.RETURNING;
        current_floor = Building.MAILROOM_LOCATION;
        this.delivery = delivery;
        this.mailPool = mailPool;
        this.receivedDispatch = false;
        this.deliveryCounter = 0;
        this.wrapping_turns = 0;
        this.unwrapping_turns = 0;
        this.wrapping_flag = false;
        this.move = false;
    }
    
    public void dispatch() {
    	receivedDispatch = true;
    }

    /**
     * This is called on every time step
     * @throws ExcessiveDeliveryException if robot delivers more than the capacity of the tube without refilling
     */
    public void step() throws ExcessiveDeliveryException {
        switch(current_state) {
            /** This state is triggered when the robot is returning to the mailroom after a delivery */
            case CAUTION:
                /** Delivery mode for fragile deliveries */
                if (!(this.wrapping_flag)) {
                    this.wrapping_turns++;
                    Simulation.statistics.addTime();
                    System.out.printf("T: %3d > WRAPPING [%s]%n", Clock.Time(), deliveryItem.toString());
                    if (this.wrapping_turns == 2) {
                        this.wrapping_flag = true;
                    }
                    break;
                } else {
                    this.wrapping_turns = 0;
                }
                if(current_floor == destination_floor){ // If already here drop off either way
                    /** Delivery complete, report this to the simulator! */
                    if (this.unwrapping_turns < 1) {
                        Simulation.statistics.addTime();
                        unwrapping_turns++;
                        System.out.printf("T: %3d > UNWRAPPING [%s]%n", Clock.Time(), deliveryItem.toString());
                        break;
                    } else {
                        unwrapping_turns = 0;
                    }
                    delivery.deliver(deliveryItem);
                    Simulation.statistics.addCautionDelivery();
                    Simulation.statistics.addCautionWeight(deliveryItem.getWeight());
                    this.wrapping_flag = false;
                    /** Check if robot need to return */
                    checkItems();
                } else {
                    /** The robot is not at the destination yet, move towards it! */
                    moveTowards(destination_floor);
                }
                break;

            case RETURNING:
                /** If its current position is at the mailroom, then the robot should change state */
                if(current_floor == Building.MAILROOM_LOCATION){
                    if (tube != null) {
                        mailPool.addToPool(tube);
                        System.out.printf("T: %3d >  +addToPool [%s]%n", Clock.Time(), tube.toString());
                        tube = null;
                    }
                    /** Tell the sorter the robot is ready */
                    mailPool.registerWaiting(this);
                    changeState(RobotState.WAITING);
                } else {
                    /** If the robot is not at the mailroom floor yet, then move towards it! */
                    moveTowards(Building.MAILROOM_LOCATION);
                    break;
                }
            case WAITING:
                /** If the StorageTube is ready and the Robot is waiting in the mailroom then start the delivery */
                if(!isEmpty() && receivedDispatch){
                    receivedDispatch = false;
                    deliveryCounter = 0; // reset delivery counter
                    if (specialHands == null) {
                        deliveryItem = normalHands;
                        normalHands = null;
                        setRoute();
                        changeState(RobotState.DELIVERING);
                    } else {
                        deliveryItem = specialHands;
                        specialHands = null;
                        setRoute();
                        changeState(RobotState.CAUTION);
                    }
                }
                break;
            case DELIVERING:
                if(current_floor == destination_floor){ // If already here drop off either way
                    /** Delivery complete, report this to the simulator! */
                    delivery.deliver(deliveryItem);
                    Simulation.statistics.addNormalDelivery();
                    Simulation.statistics.addNormalWeight(deliveryItem.getWeight());
                    /** Check if want to return, i.e. if there is no item in the tube */
                    checkItems();
                } else {
                    /** The robot is not at the destination yet, move towards it! */
                    moveTowards(destination_floor);
                }
                break;
        }
    }

    /**
     * Sets the route for the robot
     */
    private void setRoute() {
        /** Set the destination floor */
        destination_floor = deliveryItem.getDestFloor();
    }

    /**
     * Generic function that moves the robot towards the destination
     * @param destination the floor towards which the robot is moving
     */
//    private void moveTowards(int destination) {
//        if(current_floor < destination){
//            current_floor++;
//        } else {
//            current_floor--;
//        }
//    }
    private void moveTowards(int destination) {
//        for(Robot rob: Automail.robots)
//        {   if(rob.deliveryItem!=null && !(this.equals(rob))) {
//            if (rob.deliveryItem.getFragile())
//                if (rob.current_floor+1 == rob.deliveryItem.getDestFloor() || rob.current_floor-1 == rob.deliveryItem.getDestFloor() || rob.current_floor==rob.deliveryItem.getDestFloor())
//                {
//                    if (this.current_floor+1==rob.deliveryItem.getDestFloor() || this.current_floor-1==rob.deliveryItem.getDestFloor())
//                    {   System.out.println("dont move");
//                        move=false;
//
//                    }else move=true;
//                } else move=true;
//
//        }else move=true
//      }
        movementController.checkMovement(this);
        if(current_floor < destination && move==true){
            current_floor++;
        } else if (move==true) {
            current_floor--;
        }
    }

    
    private String getIdTube() {
    	return String.format("%s(%1d)", id, (tube == null ? 0 : 1));
    }
    
    /**
     * Prints out the change in state
     * @param nextState the state to which the robot is transitioning
     */
    private void changeState(RobotState nextState){
    	assert(!(deliveryItem == null && tube != null));
    	if (current_state != nextState) {
            System.out.printf("T: %3d > %7s changed from %s to %s%n", Clock.Time(), getIdTube(), current_state, nextState);
    	}
    	current_state = nextState;
    	if(nextState == RobotState.DELIVERING){
            System.out.printf("T: %3d > %9s-> [%s]%n", Clock.Time(), getIdTube(), deliveryItem.toString());
    	}
    }

	public MailItem getTube() {
		return tube;
	}

	public int getCurrent_floor() {return current_floor; }

    public MailItem getDeliveryItem() {
        return deliveryItem;
    }

    static private int count = 0;
	static private Map<Integer, Integer> hashMap = new TreeMap<Integer, Integer>();

	@Override
	public int hashCode() {
		Integer hash0 = super.hashCode();
		Integer hash = hashMap.get(hash0);
		if (hash == null) { hash = count++; hashMap.put(hash0, hash); }
		return hash;
	}

	public boolean isEmpty() {
		return (normalHands == null && tube == null && specialHands == null);
	}

	public void addToHand(MailItem mailItem) throws ItemTooHeavyException, BreakingFragileItemException {
//		assert(deliveryItem == null);
//		if(mailItem.fragile) throw new BreakingFragileItemException();
//		deliveryItem = mailItem;
//		if (deliveryItem.weight > INDIVIDUAL_MAX_WEIGHT) throw new ItemTooHeavyException();


        assert(normalHands == null);
        if(mailItem.fragile) throw new BreakingFragileItemException();
        normalHands = mailItem;
        if (normalHands.weight > INDIVIDUAL_MAX_WEIGHT) throw new ItemTooHeavyException();
	}

	public void addToTube(MailItem mailItem) throws ItemTooHeavyException, BreakingFragileItemException {
		assert(tube == null);
		if(mailItem.fragile) throw new BreakingFragileItemException();
		tube = mailItem;
		if (tube.weight > INDIVIDUAL_MAX_WEIGHT) throw new ItemTooHeavyException();
	}

	public boolean checkSpecialHandsIsEmpty() {return (specialHands == null); }

    public void addToSpecialHands(MailItem mailItem) throws ItemTooHeavyException {
        assert(specialHands == null);
        specialHands = mailItem;
        if (specialHands.weight > INDIVIDUAL_MAX_WEIGHT) throw new ItemTooHeavyException();
    }

    public void checkItems() throws ExcessiveDeliveryException {
        deliveryItem = null;
        deliveryCounter++;
        if (deliveryCounter > 3) {
            throw new ExcessiveDeliveryException();
        }
        if (this.normalHands != null) {
            deliveryItem = normalHands;
            normalHands = null;
            setRoute();
            changeState(RobotState.DELIVERING);
        } else if (this.tube != null){
            deliveryItem = tube;
            tube = null;
            setRoute();
            changeState(RobotState.DELIVERING);
        } else {
            changeState(RobotState.RETURNING);
        }
    }

    public void setMove(boolean move) {
	    this.move = move;
    }
}
