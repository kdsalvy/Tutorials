package demo.thread.readwrite;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ReadWriteLockSimulation {

    public static void main(String[] args) {
	CarService cs = new CarService();
	Thread t1 = new Thread(() -> {
	    while (true) {
		System.out.println("Register");
		cs.registerCar(new Car("Tesla", Double.toString(Math.random())));
		try {
		    Thread.sleep(70);
		} catch (InterruptedException e) {
		    e.printStackTrace();
		}
	    }
	});

	Thread t2 = new Thread(() -> {
	    while (true) {
		System.out.println("Deregister");
		cs.deRegisterCar(new Car("Tesla", Double.toString(Math.random())));
		try {
		    Thread.sleep(60);
		} catch (InterruptedException e) {
		    e.printStackTrace();
		}
	    }
	});

	Thread t3 = new Thread(() -> {
	    while (true) {
		System.out.println(Thread.currentThread().getId() + " : " + cs.getCar(Double.toString(Math.random())));
		try {
		    if (Math.random() % 2 == 0)
			Thread.sleep(20);
		    else
			Thread.sleep(80);
		} catch (InterruptedException e) {
		    e.printStackTrace();
		}
	    }
	});

	Thread t4 = new Thread(() -> {
	    while (true) {
		System.out.println(Thread.currentThread().getId() + " : " + cs.getCar(Double.toString(Math.random())));
		try {
		    if (Math.random() % 5 == 0)
			Thread.sleep(10);
		    else
			Thread.sleep(50);
		} catch (InterruptedException e) {
		    e.printStackTrace();
		}
	    }
	});

	Thread t5 = new Thread(() -> {
	    while (true) {
		System.out.println(Thread.currentThread().getId() + " : " + cs.getCar(Double.toString(Math.random())));
		try {
		    Thread.sleep(100);
		} catch (InterruptedException e) {
		    e.printStackTrace();
		}
	    }
	});

	t1.start();
	t2.start();
	t3.start();
	t4.start();
	t5.start();
    }

}

class Car {
    String model;
    String regNo;

    public Car(String model, String regNo) {
	this.model = model;
	this.regNo = regNo;
    }

    @Override
    public String toString() {
	return "Car [model=" + model + ", regNo=" + regNo + "]";
    }
}

class CarService {
    private List<Car> carList = new ArrayList<>();
    private volatile int readers = 0;
    private static final Object writeLock = new Object();

    public void registerCar(Car car) {
	synchronized (writeLock) {
	    while (readers > 0)
		try {
		    System.out.println("waiting in register");
		    writeLock.wait();
		} catch (InterruptedException e) {
		    e.printStackTrace();
		}
	    carList.add(car);
	}
    }

    public void deRegisterCar(Car car) {
	synchronized (writeLock) {
	    while (readers > 0)
		try {
		    System.out.println("Waiting in deregister");
		    writeLock.wait();
		} catch (InterruptedException e) {
		    e.printStackTrace();
		}
	    carList.remove(car);
	}
    }

    public Car getCar(String regNo) {
	readers++;
	Car car = null;
	synchronized (writeLock) {
	    Iterator<Car> itr = carList.iterator();
	    while (itr.hasNext()) {
		car = itr.next();
		itr.remove();
		if (car.regNo.equals(regNo))
		    break;
	    }
	    readers--;
	    if (readers == 0) {
		System.out.println("Notifying");
		writeLock.notifyAll();
	    }
	}
	return car;
    }
}