package com.zoe.reusing;

class Art {
	Art() {
		System.out.println("Art constructor");
	}
}

class Drawing extends Art {
	public Drawing() {
		System.out.println("Drawing constructor");
	}
}

public class Cartoon extends Drawing{
	public Cartoon()
	{
		System.out.println("Cartton constructor");
	}
	public static void main(String[] args) {
		Cartoon x = new Cartoon();
	}
}
/* Output
  Art constructor
Drawing constructor
Cartton constructor
*///:~
