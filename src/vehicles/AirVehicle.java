package vehicles;

import exceptions.InvalidOperationException;

public abstract class AirVehicle extends Vehicle {
    private double maxAltitude;

    public AirVehicle(String id, String model, double maxSpeed, double maxAltitude) throws InvalidOperationException {
        super(id, model, maxSpeed);
        if (maxAltitude <= 0) {
            throw new InvalidOperationException("Max altitude must be positive.");
        }
        this.maxAltitude = maxAltitude;
    }

    public double getMaxAltitude() {

        return this.maxAltitude;
    }

    @Override
    public void displayInfo() {
        super.displayInfo();
        System.out.println("Max Altitude: " + this.maxAltitude + " meters");
    }

    @Override
    public double estimateJourneyTime(double distance) {
        double baseTime = distance / super.getMaxSpeed();
        return baseTime * 0.95; // reduce 5% for more direct routes
    }

    @Override
    public abstract void move(double distance) throws InvalidOperationException;

    @Override
    public abstract double calculateFuelEfficiency();
}
