package com.thorgaming.throwme.displayobjects.game.characters;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

import com.thorgaming.throwme.R;

public enum Characters {
	GUY(0, "Throw Me Guy", R.drawable.ss0, Guy.class, ""),
	SNAKE(1, "Snake", R.drawable.ss1, Snake.class, "char_snake"),
	CAR(2, "Car", R.drawable.ss2, Car.class, "char_car"),
	CIRCLE(3, "Ball", R.drawable.ss3, Circle.class, "char_ball");

	private Class<? extends Character> clazz;
	private int id;
	private String name;
	private int drawableId;
	private String marketId;

	private Characters(int id, String name, int drawableId, Class<? extends Character> clazz, String marketId) {
		this.clazz = clazz;
		this.id = id;
		this.name = name;
		this.drawableId = drawableId;
		this.marketId = marketId;
	}

	public String getName() {
		return name;
	}

	public int getId() {
		return id;
	}

	public int getDrawableId() {
		return drawableId;
	}

	public String getMarketId() {
		return marketId;
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