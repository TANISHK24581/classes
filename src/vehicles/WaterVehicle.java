package vehicles;

import exceptions.InvalidOperationException;

public abstract class WaterVehicle extends Vehicle {

    private boolean hasSail;

    public WaterVehicle(String id, String model, double maxSpeed, boolean hasSail) throws InvalidOperationException {
        super(id, model, maxSpeed);
        this.hasSail = hasSail;
    }

    public boolean hasSail() {
        return this.hasSail;
    }

    @Override
    public void displayInfo() {
        super.displayInfo();
        System.out.println("Has Sail: " + this.hasSail);
    }

    @Override
    public double estimateJourneyTime(double distance) {
        double baseTime = distance / super.getMaxSpeed();
        return baseTime * 1.15; // add 15% for currents
    }

    @Override
    public abstract void move(double distance) throws InvalidOperationException;

    @Override
    public abstract double calculateFuelEfficiency();
}
