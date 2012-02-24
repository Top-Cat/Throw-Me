package com.thorgaming.throwme;

/**
 * @author Thomas Cheyney
 * @version 1.0
 */
public enum GameState {
	/**
	 * Before throw, when the character is on a spring connected to wherever the user is touching
	 */
	ON_SPRING,
	/**
	 * Free from the spring, most of the game
	 */
	LOOSE,
	/**
	 * When the user has lost the game and the application should switch to the highscore submission screen
	 */
	END
}