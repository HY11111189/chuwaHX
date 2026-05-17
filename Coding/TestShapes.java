// 1. Interface Drawable
interface Drawable {
    void draw();
}

// 2. Abstract Class Shape
abstract class Shape {
    protected String color;

    // Constructor taking the color
    public Shape(String color) {
        this.color = color;
    }

    // Abstract methods to be implemented by concrete subclasses
    public abstract double getArea();
    public abstract double getPerimeter();

    // Concrete method returning the color
    public String getColor() {
        return this.color;
    }
}

// 3. Rectangle Subclass
class Rectangle extends Shape implements Drawable {
    private double width;
    private double height;

    // Constructor
    public Rectangle(String color, double width, double height) {
        super(color);
        this.width = width;
        this.height = height;
    }

    @Override
    public double getArea() {
        return width * height;
    }

    @Override
    public double getPerimeter() {
        return 2 * (width + height);
    }

    @Override
    public void draw() {
        System.out.println("Drawing a " + getColor() + " rectangle with width " + width + " and height " + height);
    }
}

// 4. Circle Subclass
class Circle extends Shape implements Drawable {
    private double radius;

    // Constructor
    public Circle(String color, double radius) {
        super(color);
        this.radius = radius;
    }

    @Override
    public double getArea() {
        return Math.PI * radius * radius;
    }

    @Override
    public double getPerimeter() {
        return 2 * Math.PI * radius;
    }

    @Override
    public void draw() {
        System.out.println("Drawing a " + getColor() + " circle with radius " + radius);
    }
}

// Main Testing Class
public class TestShapes {
    public static void main(String[] args) {
        // Create an array of Shape objects containing a Rectangle and a Circle
        Shape[] shapes = new Shape[2];
        shapes[0] = new Rectangle("Red", 5.0, 4.0);
        shapes[1] = new Circle("Blue", 3.0);

        // Process each shape dynamically using polymorphism
        for (Shape shape : shapes) {
            System.out.println("--- Shape: " + shape.getClass().getSimpleName() + " ---");
            System.out.printf("Color: %s%n", shape.getColor());
            System.out.printf("Area: %.2f%n", shape.getArea());
            System.out.printf("Perimeter: %.2f%n", shape.getPerimeter());

            // Check if the shape is Drawable and call draw() if true
            if (shape instanceof Drawable) {
                Drawable drawableShape = (Drawable) shape;
                drawableShape.draw();
            }
            System.out.println();
        }
    }
}
