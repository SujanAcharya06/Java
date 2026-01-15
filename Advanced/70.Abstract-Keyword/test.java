// If we are declaring an abstract method, it must be implemented in the subclass that extends it
// An abstract method must be declared inside an abstract class
// If we are exteding an abstract class we must define/override the abstract method
// We can't create object of abstract class
// We can create a reference of abstract of class
// It is not compulsory to for an abstract class to have abstract method
// If a car cannot implement all the methods then that class can be made abstract class as well

abstract class Car {
	public abstract void drive(); // abstract method

	public abstract void indicator();

	public void playMusic() {
		System.out.println("Play music");
	}
}

abstract class WagonR extends Car {
	public void drive() {
		System.out.println("Driving...");
	}
}

class UpdatedWagonR extends WagonR { // Concrete class
	public void indicator() {
		System.out.println("Indicator features");
	}

}

public class test {
	public static void main(String[] args) {
		// Car car = new WagonR(); // can create reference
		Car car = new UpdatedWagonR(); // can create reference
		car.drive();
		car.playMusic();
	}
}
