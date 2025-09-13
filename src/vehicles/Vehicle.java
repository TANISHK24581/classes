package vehicles;

import exceptions.InvalidOperationException;

public abstract class Vehicle implements Comparable<Vehicle> {
    private String id;
    private String model;
    private double maxSpeed;
    private double currentMileage;
    private double maintance;



    public Vehicle(String id, String model, double maxSpeed) throws InvalidOperationException {
        this.maintance = 0;
        if (id == null || id.trim().isEmpty()) {
            throw new InvalidOperationException("Vehicle ID cannot be empty.");
        }
        this.id = id;
        this.model = model;
        this.maxSpeed = maxSpeed;
        this.currentMileage = 0.0;
    }

    public abstract void move(double distance) throws InvalidOperationException;
    public abstract double calculateFuelEfficiency();
    public abstract double estimateJourneyTime(double distance);

    public void displayInfo() {
        System.out.println("Vehicle Info:");
        System.out.println("ID: " + this.id);
        System.out.println("Model: " + this.model);
        System.out.println("Max Speed: " + this.maxSpeed + " km/h");
        System.out.println("Mileage: " + this.currentMileage + " km");
    }
    public void setCurrentMileage(double mileage) {
        this.currentMileage = mileage;
    }
    public double maintance() {
        return this.maintance;
    }
    public void setmaintance(double z){
        this.maintance=z;
    }


    public double getCurrentMileage() {
        return this.currentMileage;
    }
    public double getMaxSpeed() {
        return this.maxSpeed;
    }

    public String getId() {
        return this.id;
    }
    public String getModel() {
        return this.model;
    }


    protected void updateMileage(double distance) {
        this.currentMileage += distance;
        this.maintance += distance;


    }

    @Override
    public int compareTo(Vehicle other) {
        return Double.compare(this.calculateFuelEfficiency(), other.calculateFuelEfficiency());
    }


}

