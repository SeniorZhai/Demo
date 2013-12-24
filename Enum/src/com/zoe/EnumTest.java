package com.zoe;

public class EnumTest {
	
	public enum test{fen,jiao,yuan};
	public static void main(String[] args)
	{
		for(test t : test.values())
		{
			System.out.println(t+""+t.ordinal());
		}
	}
}
