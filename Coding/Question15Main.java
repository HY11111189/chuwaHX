class CPU {
    private String brand;
    private double speed;

    public CPU(String brand, double speed) {
        this.brand = brand;
        this.speed = speed;
    }

    @Override
    public String toString() {
        return brand + " @ " + speed + " GHz";
    }
}

class RAM {
    private int size;

    public RAM(int size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return size + " GB";
    }
}

class HardDrive {
    private int size;
    private String type;

    public HardDrive(int size, String type) {
        this.size = size;
        this.type = type;
    }

    @Override
    public String toString() {
        return size + " GB " + type;
    }
}

class Computer {
    // Composition: Component references are encapsulated inside the main entity
    private CPU cpu;
    private RAM ram;
    private HardDrive hardDrive;

    // Requirement: Components are CREATED internally inside the constructor
    public Computer(String cpuBrand, double cpuSpeed, int ramSize, int hdSize, String hdType) {
        this.cpu = new CPU(cpuBrand, cpuSpeed);
        this.ram = new RAM(ramSize);
        this.hardDrive = new HardDrive(hdSize, hdType);
    }

    public String getSpecs() {
        return "Computer Specifications:\n" +
               "------------------------\n" +
               "CPU:        " + cpu.toString() + "\n" +
               "RAM:        " + ram.toString() + "\n" +
               "Hard Drive: " + hardDrive.toString();
    }
}

public class Question15Main {
    public static void main(String[] args) {
        // Instantiating the Computer system with raw values (Composition implementation)
        Computer myComputer = new Computer("Intel Core i7", 3.8, 16, 512, "SSD");
        
        // Display internal state parameters
        System.out.println(myComputer.getSpecs());
    }
}