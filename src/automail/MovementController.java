package automail;

public class MovementController {
    private Robot[] robots;

    public MovementController(Robot[] robots) {
        this.robots = robots;
    }

    public void checkMovement(Robot robot) {
        for (Robot rob: robots) {
            if (!(robot.equals(rob)) && rob.getDeliveryItem() != null) {
                if (rob.getDeliveryItem().getFragile()) {
                    if (rob.getCurrent_floor()+1 == rob.getDeliveryItem().getDestFloor() || rob.getCurrent_floor() == rob.getDeliveryItem().getDestFloor()) {
                        if (robot.getCurrent_floor()+1 == rob.getDeliveryItem().getDestFloor() || robot.getCurrent_floor()-1 == rob.getDeliveryItem().getDestFloor()) {
                            robot.setMove(false);
                        } else {
                            robot.setMove(true);
                        }
                    } else {
                        robot.setMove(true);
                    }
                }
            } else {
                robot.setMove(true);
            }
        }
    }
}
