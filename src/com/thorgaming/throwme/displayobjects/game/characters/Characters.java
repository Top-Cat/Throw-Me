package com.thorgaming.throwme.displayobjects.game.characters;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

public enum Characters {
	GUY(0, "Throw Me Guy", Guy.class),
	SNAKE(1, "Snake", Snake.class),
	CAR(2, "Car", Car.class),
	CIRCLE(3, "Ball", Circle.class),
	;
	
	private Class<? extends Character> clazz;
	private int id;
	private String name;
	
	private Characters(int id, String name, Class<? extends Character> clazz) {
		this.clazz = clazz;
		this.id = id;
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public int getId() {
		return id;
	}
	
	public Character createNew() {
		try {
			Constructor<? extends Character> c = clazz.getDeclaredConstructor(new Class[] {});
			return c.newInstance();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private static HashMap<Integer, Characters> map = new HashMap<Integer, Characters>();
	
	static {
		for (Characters ch : values()) {
			map.put(ch.getId(), ch);
		}
	}
	
	public static Characters getFromId(int id) {
		return map.get(id);
	}
}