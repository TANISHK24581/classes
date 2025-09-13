package vehicles;

import exceptions.InvalidOperationException;

public abstract class LandVehicle extends Vehicle {

    private int numberOfWheels;

    public LandVehicle(String id, String model, double maxSpeed, int numberOfWheels) throws InvalidOperationException {
        super(id, model, maxSpeed);
        if (numberOfWheels <= 0) {
            throw new InvalidOperationException("Number of wheels must be positive.");
        }
        this.numberOfWheels = numberOfWheels;
    }

    public int getNumberOfWheels() {
        return this.numberOfWheels;
    }

    @Override
    public void displayInfo() {
        super.displayInfo();
        System.out.println("Number of Wheels: " + this.numberOfWheels);
    }

    @Override
    public double estimateJourneyTime(double distance) {
        double baseTime = distance / super.getMaxSpeed();
        return baseTime * 1.1; // add 10% for traffic
    }

    // move() and calculateFuelEfficiency() remain abstract
    @Override
    public abstract void move(double distance) throws InvalidOperationException;

    @Override
    public abstract double calculateFuelEfficiency();
}
