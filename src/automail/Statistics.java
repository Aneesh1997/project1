package automail;

public class Statistics {
    private static int num_of_normal_deliveries;
    private static int num_of_caution_deliveries;
    private static int total_wt_of_normal_deliveries;
    private static int total_wt_of_caution_deliveries;
    private static int total_time_spent_wrapping_and_unwrapping;

    public Statistics() {
        num_of_normal_deliveries = 0;
        num_of_caution_deliveries = 0;
        total_wt_of_caution_deliveries = 0;
        total_wt_of_normal_deliveries = 0;
        total_time_spent_wrapping_and_unwrapping = 0;
    }

    public void addNormalDelivery() {
        this.num_of_normal_deliveries++;
    }

    public void addCautionDelivery() {
        this.num_of_caution_deliveries++;
    }

    public void addNormalWeight(int package_wt) {
        total_wt_of_normal_deliveries += package_wt;
    }

    public void addCautionWeight(int package_wt) { total_wt_of_caution_deliveries += package_wt; }

    public void addTime() { total_time_spent_wrapping_and_unwrapping++; }

    public void printStatistics() {
        System.out.printf("There are %d normal deliveries\n", this.num_of_normal_deliveries);
        System.out.printf("The total weight of normal deliveries is %d\n", this.total_wt_of_normal_deliveries);
        System.out.printf("There are %d caution deliveries\n", this.num_of_caution_deliveries);
        System.out.printf("The total weight of caution deliveries is %d\n", this.total_wt_of_caution_deliveries);
        System.out.printf("The total amount of time spent wrapping and unwrapping is %d\n", this.total_time_spent_wrapping_and_unwrapping);
    }

    public int getTotal_time_spent_wrapping_and_unwrapping() {
        return this.total_time_spent_wrapping_and_unwrapping;
    }
}
