package vehicles;

import exceptions.InvalidOperationException;
import exceptions.OverloadException;
import exceptions.InsufficientFuelException;
import interfaces.FuelConsumable;
import interfaces.PassengerCarrier;
import interfaces.Maintainable;

public class Car extends LandVehicle implements FuelConsumable, PassengerCarrier, Maintainable {

    private double fuelLevel;
    private final int passengerCapacity;
    private int currentPassengers;
    private boolean maintenanceNeeded;

    public Car(String id, String model, double maxSpeed, int numberOfWheels) throws InvalidOperationException {
        super(id, model, maxSpeed, numberOfWheels);
        this.fuelLevel = 0.0;
        this.passengerCapacity = 5;
        this.currentPassengers = 0;
        this.maintenanceNeeded = false;
    }

    @Override
    public void move(double distance) throws InvalidOperationException {
        if (distance < 0) {
            throw new InvalidOperationException("Distance cannot be negative.");
        }
        try {
            consumeFuel(distance);
        } catch (InsufficientFuelException e) {
            throw new InvalidOperationException("Not enough fuel to drive the distance.");
        }
        updateMileage(distance);
        System.out.println("Driving on road for " + distance + " km...");
    }

    @Override
    public double calculateFuelEfficiency() {
        return 15.0;
    }

    @Override
    public void refuel(double amount) throws InvalidOperationException {
        if (amount <= 0) {
            throw new InvalidOperationException("Refuel amount must be positive.");
        }
        fuelLevel += amount;
        System.out.println("Refueled " + amount + " liters. Current fuel: " + fuelLevel + " liters");
    }

    @Override
    public double getFuelLevel() {
        return fuelLevel;
    }

    @Override
    public double consumeFuel(double distance) throws InsufficientFuelException {
        double required = distance / calculateFuelEfficiency();
        if (fuelLevel < required) {
            throw new InsufficientFuelException("Insufficient fuel. Needed: " + required + " liters");
        }
        fuelLevel -= required;
        return required;
    }

    @Override
    public void boardPassengers(int count) throws OverloadException {
        if (count + currentPassengers > passengerCapacity) {
            throw new OverloadException("Cannot board passengers. Exceeds capacity.");
        }
        this.currentPassengers += count;
        System.out.println(count + " passengers boarded. Current passengers: " + currentPassengers);
    }

    @Override
    public void disembarkPassengers(int count) throws InvalidOperationException {
        if (count > currentPassengers) {
            throw new InvalidOperationException("Cannot disembark more passengers than current.");
        }
        currentPassengers -= count;
        System.out.println(count + " passengers disembarked. Current passengers: " + currentPassengers);
    }

    @Override
    public int getPassengerCapacity() {
        return passengerCapacity;
    }

    @Override
    public int getCurrentPassengers() {
        return currentPassengers;
    }

    @Override
    public void scheduleMaintenance() {
        maintenanceNeeded = true;
        System.out.println("Maintenance scheduled for Car ID: " + getId());
    }

    @Override
    public boolean needsMaintenance() {
        return maintenanceNeeded || this.maintance() > 10000;
    }

    @Override
    public void performMaintenance() {
        maintenanceNeeded = false;
        this.setmaintance(0);
        System.out.println("Maintenance performed for Car ID: " + getId());
    }

    @Override
    public void displayInfo() {
        super.displayInfo();
        System.out.println("Passenger Capacity: " + passengerCapacity);
        System.out.println("Current Passengers: " + currentPassengers);
        System.out.println("Fuel Level: " + fuelLevel + " liters");
        System.out.println("Needs Maintenance: " + needsMaintenance());
    }
}
