package com.thorgaming.throwme.displayobjects;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Canvas;
import android.graphics.Rect;

import com.thorgaming.throwme.ThrowMe;
import com.thorgaming.throwme.animation.Anim;
import com.thorgaming.throwme.callback.MouseCallback;
import com.thorgaming.throwme.drawing.Camera;
import com.thorgaming.throwme.drawing.RenderPriority;

/**
 * Abstract class that represents an object that can be drawn on screen
 * 
 * @author Thomas Cheyney
 * @version 1.0
 */
public abstract class DispObj {

	/**
	 * Width the object should display relative to 800
	 */
	private int width = 0;
	/**
	 * Height the object should display relative to 480
	 */
	private int height = 0;
	/**
	 * X position of the object should display at relative to 800
	 */
	private int x = 0;
	/**
	 * Y position of the object should display at relative to 480
	 */
	private int y = 0;
	/**
	 * Alpha value to display at, between 0 and 255
	 */
	private int alpha = 255;
	/**
	 * If this object can continue to move and receive mouse calls while the main game is paused
	 */
	private boolean ignorePause = false;
	/**
	 * Hitbox used to detect mouse presses
	 */
	protected Rect hitBox = new Rect();
	/**
	 * Hitbox placed at the point the mouse press it at,
	 * used to check intersection with the main hitbox
	 */
	private Rect hitRegion = new Rect();

	/**
	 * Adds this object to the screen with a given render priority
	 * This will cause the draw thread to call this object's draw method
	 * 
	 * @param priority Priority the object should draw at
	 * @return This object
	 */
	public DispObj addToScreen(RenderPriority priority) {
		ThrowMe.getInstance().stage.registerForRender(priority, this);
		return this;
	}

	/**
	 * @see #addToScreen(RenderPriority priority)
	 * Uses the normal render priority
	 */
	public DispObj addToScreen() {
		return addToScreen(RenderPriority.Normal);
	}

	/**
	 * Callbacks called when the mouse interacts with this object
	 */
	private MouseCallback onMouseUpEvent, onMouseDownEvent, onMouseMoveEvent;

	/**
	 * Called by the draw thread when the object should draw itself
	 * 
	 * @param canvas The canvas on which to draw
	 * @param camera Camera object to get screen size and position of camera
	 */
	public abstract void draw(Canvas canvas, Camera camera);

	/**
	 * Sets the event to be called when the mouse is released
	 * 
	 * @param event Event to be called
	 * @return This object
	 */
	public DispObj setMouseUpEvent(MouseCallback event) {
		onMouseUpEvent = event;
		return this;
	}

	/**
	 * Retrieve the event to be called when the mouse is released
	 * @return The event
	 */
	public MouseCallback getMouseUpEvent() {
		return !ThrowMe.getInstance().stage.drawThread.getPaused() || ignorePause ? onMouseUpEvent : null;
	}

	/**
	 * Sets the event to be called when the mouse is moved over this object
	 * 
	 * @param event Event to be called
	 * @return This object
	 */
	public DispObj setMouseMoveEvent(MouseCallback event) {
		onMouseMoveEvent = event;
		return this;
	}

	/**
	 * Retrieve the event to be called when the mouse is moved over this object
	 * @return The event
	 */
	public MouseCallback getMouseMoveEvent() {
		return !ThrowMe.getInstance().stage.drawThread.getPaused() || ignorePause ? onMouseMoveEvent : null;
	}

	/**
	 * Sets the event to be called when the mouse is pressed
	 * 
	 * @param event Event to be called
	 * @return This object
	 */
	public DispObj setMouseDownEvent(MouseCallback event) {
		onMouseDownEvent = event;
		return this;
	}

	/**
	 * Retrieve the event to be called when the mouse is pressed
	 * @return The event
	 */
	public MouseCallback getMouseDownEvent() {
		return !ThrowMe.getInstance().stage.drawThread.getPaused() || ignorePause ? onMouseDownEvent : null;
	}

	/**
	 * Sets the alpha value at which this object will display
	 * 
	 * @param alpha New alpha value
	 * @return This object
	 */
	public DispObj setAlpha(int alpha) {
		this.alpha = alpha;
		return this;
	}

	/**
	 * Sets that this object will ignore the pause state on the game
	 * 
	 * @return This object
	 */
	public DispObj setIgnorePause() {
		ignorePause = true;
		return this;
	}

	/**
	 * Gets the alpha value this object draws at
	 * 
	 * @return Current alpha value
	 */
	public int getAlpha() {
		return alpha;
	}

	/**
	 * Gets the X position of the object should display at relative to 800
	 * 
	 * @return X position
	 */
	public int getX() {
		return x;
	}

	/**
	 * Gets the Y position of the object should display at relative to 480
	 * 
	 * @return Y position
	 */
	public int getY() {
		return y;
	}

	/**
	 * Move to an x,y position relative to 800,480
	 * 
	 * @param x New X position
	 * @param y New y position
	 */
	public void move(int x, int y) {
		setX(x);
		setY(y);
	}

	/**
	 * Sets the X position of the object should display at relative to 800
	 * 
	 * @param x New X position
	 * @return This object
	 */
	public DispObj setX(int x) {
		this.x = x;
		return this;
	}

	/**
	 * Sets the Y position of the object should display at relative to 480
	 * 
	 * @param y New Y position
	 * @return This object
	 */
	public DispObj setY(int y) {
		this.y = y;
		return this;
	}

	/**
	 * Gets the width the object should display relative to 800
	 * 
	 * @return Width of this object
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * Sets the width the object should display relative to 800
	 * 
	 * @param width New width of this object
	 * @return This object
	 */
	public DispObj setWidth(int width) {
		this.width = width;
		return this;
	}
	
	/**
	 * Gets the height the object should display relative to 480
	 * 
	 * @return Height of this object
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * Sets the height the object should display relative to 480
	 * 
	 * @param height New height of this object
	 * @return This object
	 */
	public DispObj setHeight(int height) {
		this.height = height;
		return this;
	}

	/**
	 * Get the rectangle used for hit padding
	 * 
	 * @return Hit rectangle
	 */
	public Rect getHitPadding() {
		return hitRegion;
	}

	/**
	 * Gets the hitbox used to detect this object
	 * 
	 * @return Object's hitbox
	 */
	public Rect getHitBox() {
		return hitBox;
	}

	/**
	 * Set the hit padding by value
	 * 
	 * @param hitPadding 2*padding on each side
	 * @return This object
	 */
	public DispObj setHitPadding(int hitPadding) {
		hitRegion.set(0, 0, hitPadding, hitPadding);
		return this;
	}

	/**
	 * Set a rectangle for hit padding
	 * 
	 * @param hitRegion Hit padding rectangle
	 * @return This object
	 */
	public DispObj setHitPadding(Rect hitRegion) {
		this.hitRegion.set(hitRegion);
		return this;
	}

	/**
	 * Removes this object from the draw list and clears any pending animations on it
	 */
	public void destroy() {
		List<Anim> over = new ArrayList<Anim>();
		for (Anim i : ThrowMe.getInstance().stage.animations) {
			if (i.getObject() == this) {
				over.add(i);
			}
		}
		ThrowMe.getInstance().stage.animations.removeAll(over);

		ThrowMe.getInstance().stage.unregisterForRender(this);
	}

	/**
	 * Checks if a mouse event should be passed to this object
	 * 
	 * @param x X position of mouse press
	 * @param y Y position of mouse press
	 * @return True if the mouse event effects this object
	 */
	public boolean checkPress(int x, int y) {
		hitRegion.offsetTo(x - hitRegion.width() / 2, y - hitRegion.height() / 2);
		return Rect.intersects(hitRegion, hitBox);
	}

}