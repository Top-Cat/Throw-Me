package com.thorgaming.throwme.displayobjects.game.characters;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

import com.thorgaming.throwme.R;

/**
 * List of characters that can be used
 * 
 * @author Thomas Cheyney
 * @version 1.0
 */
public enum Characters {
	GUY(0, "Throw Me Guy", R.drawable.ss0, Guy.class, ""),
	SNAKE(1, "Snake", R.drawable.ss1, Snake.class, "char_snake"),
	CAR(2, "Car", R.drawable.ss2, Car.class, "char_car"),
	CIRCLE(3, "Ball", R.drawable.ss3, Circle.class, "char_ball");

	/**
	 * Class to be initialised to make a character
	 */
	private Class<? extends Character> clazz;
	/**
	 * Used elsewhere to check and store which character is selected
	 */
	private int id;
	/**
	 * Human readable name of the character
	 */
	private String name;
	/**
	 * Id of a drawable to show in the purchase screen
	 */
	private int drawableId;
	/**
	 * Id of the product in the market
	 */
	private String marketId;

	private Characters(int id, String name, int drawableId, Class<? extends Character> clazz, String marketId) {
		this.clazz = clazz;
		this.id = id;
		this.name = name;
		this.drawableId = drawableId;
		this.marketId = marketId;
	}

	/**
	 * Get the name of the character
	 * 
	 * @return Character name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Get the id of the character
	 * 
	 * @return Character Id
	 */
	public int getId() {
		return id;
	}

	/**
	 * Get the drawable to show in the purchase screen
	 * 
	 * @return Drawable to show in the purchase screen
	 */
	public int getDrawableId() {
		return drawableId;
	}

	/**
	 * Get the id of the product in the market
	 * 
	 * @return Id of the product in the market
	 */
	public String getMarketId() {
		return marketId;
	}

	/**
	 * Initialise a character class
	 * 
	 * @return The newly created character
	 */
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

	/**
	 * Map used to get values from ids
	 */
	private static HashMap<Integer, Characters> map = new HashMap<Integer, Characters>();

	/**
	 * Put values into the value<->id map
	 */
	static {
		for (Characters ch : values()) {
			map.put(ch.getId(), ch);
		}
	}

	/**
	 * Retrieves a value from the map for an id
	 * 
	 * @param id Id of desired character
	 * @return The enum value
	 */
	public static Characters getFromId(int id) {
		return map.get(id);
	}
}