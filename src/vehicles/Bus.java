package vehicles;

import exceptions.InvalidOperationException;
import exceptions.OverloadException;
import exceptions.InsufficientFuelException;
import interfaces.FuelConsumable;
import interfaces.CargoCarrier;
import interfaces.PassengerCarrier;
import interfaces.Maintainable;

public class Bus extends LandVehicle implements FuelConsumable, PassengerCarrier, CargoCarrier, Maintainable {

    private double fuelLevel;
    private final int passengerCapacity;
    private int currentPassengers;
    private final double cargoCapacity;
    private double currentCargo;
    private boolean maintenanceNeeded;

    public Bus(String id, String model, double maxSpeed, int numberOfWheels) throws InvalidOperationException {
        super(id, model, maxSpeed, numberOfWheels);
        this.fuelLevel = 0.0;
        this.passengerCapacity = 50;
        this.currentPassengers = 0;
        this.cargoCapacity = 500.0;
        this.currentCargo = 0.0;
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
            throw new InvalidOperationException("Not enough fuel to transport passengers and cargo.");
        }
        updateMileage(distance);
        System.out.println("Transporting passengers and cargo for " + distance + " km...");
    }

    @Override
    public double calculateFuelEfficiency() {
        return 10.0;
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
        if (currentPassengers + count > passengerCapacity) {
            throw new OverloadException("Cannot board passengers. Exceeds capacity.");
        }
        currentPassengers += count;
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
    public void loadCargo(double weight) throws OverloadException {
        if (currentCargo + weight > cargoCapacity) {
            throw new OverloadException("Cannot load cargo. Exceeds capacity.");
        }
        currentCargo += weight;
        System.out.println(weight + " kg loaded. Current cargo: " + currentCargo + " kg");
    }

    @Override
    public void unloadCargo(double weight) throws InvalidOperationException {
        if (weight > currentCargo) {
            throw new InvalidOperationException("Cannot unload more than current cargo.");
        }
        currentCargo -= weight;
        System.out.println(weight + " kg unloaded. Current cargo: " + currentCargo + " kg");
    }

    @Override
    public double getCargoCapacity() {
        return cargoCapacity;
    }

    @Override
    public double getCurrentCargo() {
        return currentCargo;
    }

    @Override
    public void scheduleMaintenance() {
        maintenanceNeeded = true;
        System.out.println("Maintenance scheduled for Bus ID: " + getId());
    }

    @Override
    public boolean needsMaintenance() {
        return maintenanceNeeded || this.maintance() > 10000;
    }

    @Override
    public void performMaintenance() {
        maintenanceNeeded = false;
        this.setmaintance(0);
        System.out.println("Maintenance performed for Bus ID: " + getId());
    }

    @Override
    public void displayInfo() {
        super.displayInfo();
        System.out.println("Passenger Capacity: " + passengerCapacity);
        System.out.println("Current Passengers: " + currentPassengers);
        System.out.println("Cargo Capacity: " + cargoCapacity + " kg");
        System.out.println("Current Cargo: " + currentCargo + " kg");
        System.out.println("Fuel Level: " + fuelLevel + " liters");
        System.out.println("Needs Maintenance: " + needsMaintenance());
    }
}

