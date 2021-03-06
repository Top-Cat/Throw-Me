package com.thorgaming.throwme.screens;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.yaml.snakeyaml.Yaml;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.view.KeyEvent;
import android.view.MotionEvent;

import com.thorgaming.throwme.R;
import com.thorgaming.throwme.ThrowMe;
import com.thorgaming.throwme.callback.Callback;
import com.thorgaming.throwme.callback.MouseCallback;
import com.thorgaming.throwme.displayobjects.DispGif;
import com.thorgaming.throwme.displayobjects.DispObj;
import com.thorgaming.throwme.displayobjects.DispRes;
import com.thorgaming.throwme.displayobjects.scores.ScoreRow;
import com.thorgaming.throwme.displayobjects.shape.RoundRect;
import com.thorgaming.throwme.drawing.RenderPriority;

/**
 * @author Thomas Cheyney
 * @version 1.0
 */
public class Highs extends Screen {

	/**
	 * Object representing spinning loading wheel
	 */
	private DispObj loader;
	/**
	 * Object representing unselected day button
	 */
	private DispObj dayBW;
	/**
	 * Object representing unselected week button
	 */
	private DispObj weekBW;
	/**
	 * Object representing unselected month button
	 */
	private DispObj monthBW;
	/**
	 * Object representing unselected all time button
	 */
	private DispObj timeBW;
	/**
	 * Object representing selected day button
	 */
	private DispObj dayC;
	/**
	 * Object representing selected week button
	 */
	private DispObj weekC;
	/**
	 * Object representing selected month button
	 */
	private DispObj monthC;
	/**
	 * Object representing selected all time button
	 */
	private DispObj timeC;
	/**
	 * Background behind the scores
	 */
	private RoundRect roundedBorderBackground, roundedBorder;
	/**
	 * Stores the device's uniqueid to be checked against records to tell if they belong to this phone
	 */
	private String deviceid = "";
	/**
	 * Where the mouse was at the start of the scroll, used to calculate difference
	 */
	private int downY;
	/**
	 * How far was scrolled at the start of the scroll
	 */
	private int scrollStart;
	/**
	 * How far has been moved from the start of the scroll already
	 */
	private int previousMovement;
	/**
	 * How fast is the scroll happening, used to slow down the scroll when the user has released
	 */
	private int movement;
	/**
	 * Distance of scroll to draw, used to make sure all the scores are drawn with the same value instead of one that changes during the draw
	 */
	public int scrollDraw = 0;
	/**
	 * Current scroll value
	 */
	private int scroll = 0;
	/**
	 * List of high score rows
	 */
	private List<ScoreRow> highScores = new ArrayList<ScoreRow>();

	public Highs(Object[] data) {
		super(data);

		SharedPreferences settings = ThrowMe.getInstance().getSharedPreferences("throwmedevicekey", 0);
		deviceid = settings.getString("deviceid", UUID.randomUUID().toString());
		Editor editor = settings.edit();
		editor.putString("deviceid", deviceid);
		editor.commit();

		RoundRect rr = (RoundRect) new RoundRect(20).setHeight(480).setWidth(550).setAlpha(50).setX(40).addToScreen(RenderPriority.High);
		rr.paint.setARGB(50, 0, 0, 0);
		rr.stroke.setARGB(150, 0, 0, 0);

		ThrowMe.getInstance().stage.draw = new Callback() {
			@Override
			public void sendCallback() {
				scrollDraw = scroll;
			}
		};

		loader = new DispGif(R.drawable.ajax, -1, 1).setWidth(128).setHeight(128).setX(251).setY(176).addToScreen();
		new DispRes(R.drawable.back).setHitPadding(16).setWidth(48).setHeight(48).setX(736).setY(416).setMouseDownEvent(new MouseCallback() {
			@Override
			public boolean sendCallback(int x, int y) {
				new Main(new Object[] {true});
				return true;
			}
		}).addToScreen();
		dayBW = new DispRes(R.drawable.day_bw).setHitPadding(15).setWidth(184).setHeight(74).setX(608).setY(10).addToScreen();
		dayBW.setMouseDownEvent(new MouseCallback() {
			@Override
			public boolean sendCallback(int x, int y) {
				if (!loading) {
					resetOptions();
					dayBW.setAlpha(0);
					dayC.setAlpha(255);
					new loader("day").start();
				}
				return false;
			}
		});
		weekBW = new DispRes(R.drawable.week_bw).setHitPadding(15).setWidth(184).setHeight(54).setX(608).setY(120).addToScreen();
		weekBW.setMouseDownEvent(new MouseCallback() {
			@Override
			public boolean sendCallback(int x, int y) {
				if (!loading) {
					resetOptions();
					weekBW.setAlpha(0);
					weekC.setAlpha(255);
					new loader("week").start();
				}
				return false;
			}
		});
		monthBW = new DispRes(R.drawable.month_bw).setHitPadding(15).setWidth(184).setHeight(40).setX(608).setY(214).addToScreen();
		monthBW.setMouseDownEvent(new MouseCallback() {
			@Override
			public boolean sendCallback(int x, int y) {
				if (!loading) {
					resetOptions();
					monthBW.setAlpha(0);
					monthC.setAlpha(255);
					new loader("month").start();
				}
				return false;
			}
		});
		timeBW = new DispRes(R.drawable.time_bw).setHitPadding(15).setWidth(184).setHeight(31).setX(608).setY(297).addToScreen();
		timeBW.setMouseDownEvent(new MouseCallback() {
			@Override
			public boolean sendCallback(int x, int y) {
				if (!loading) {
					resetOptions();
					timeBW.setAlpha(0);
					timeC.setAlpha(255);
					new loader("").start();
				}
				return false;
			}
		});

		dayC = new DispRes(R.drawable.day).setWidth(184).setHeight(74).setX(608).setY(10).setAlpha(0).addToScreen();
		weekC = new DispRes(R.drawable.week).setWidth(184).setHeight(54).setX(608).setY(120).setAlpha(0).addToScreen();
		monthC = new DispRes(R.drawable.month).setWidth(184).setHeight(40).setX(608).setY(214).setAlpha(0).addToScreen();
		timeC = new DispRes(R.drawable.time).setWidth(184).setHeight(31).setX(608).setY(297).setAlpha(0).addToScreen();

		dayBW.getMouseDownEvent().sendCallback();
	}

	public void resetOptions() {
		dayC.setAlpha(0);
		weekC.setAlpha(0);
		monthC.setAlpha(0);
		timeC.setAlpha(0);

		dayBW.setAlpha(255);
		weekBW.setAlpha(255);
		monthBW.setAlpha(255);
		timeBW.setAlpha(255);
	}

	/**
	 * Prevents another load being started while one is taking place
	 */
	boolean loading = false;

	private class loader extends Thread {

		String type = "";

		public loader(String type) {
			this.type = type;
		}

		@Override
		@SuppressWarnings("unchecked")
		public void run() {
			try {
				if (!loading) {
					loading = true;

					for (ScoreRow i : highScores) {
						i.destroy();
					}
					highScores.clear();
					loader.setAlpha(255);

					URL url = new URL("http://thomasc.co.uk/throwme/api.php?deviceid=" + deviceid + "&type=" + type);
					BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
					Yaml yaml = new Yaml();
					Object yamlObj = yaml.load(reader);
					reader.close();

					if (ThrowMe.getInstance().screen == Highs.this) {
						if (yamlObj instanceof ArrayList<?>) {
							ArrayList<Map<String, Object>> list = (ArrayList<Map<String, Object>>) yamlObj;

							int a = 0;
							SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							for (Map<String, Object> i : list) {
								try {
									highScores.add((ScoreRow) new ScoreRow(Highs.this, a + 1, (String) i.get("name"), Integer.valueOf((String) i.get("score")), sdf.parse((String) i.get("date")), (Boolean) i.get("isdevice")).setY(a++ * 60).addToScreen());
								} catch (NumberFormatException e) {
									e.printStackTrace();
								} catch (ParseException e) {
									e.printStackTrace();
								}
							}
						}

						loader.setAlpha(0);
						if (roundedBorderBackground != null) {
							roundedBorderBackground.destroy();
							roundedBorder.destroy();
						}

						roundedBorderBackground = (RoundRect) new RoundRect(24).setWidth(561).setHeight(490).setAlpha(0).setX(35).setY(-5).addToScreen(RenderPriority.Low);
						roundedBorderBackground.paint.setARGB(0, 0, 0, 0);
						roundedBorderBackground.stroke.setStrokeWidth(10);
						roundedBorderBackground.stroke.setShader(new LinearGradient(0, 0, 0, ThrowMe.getInstance().stage.camera.getScreenHeight(), Color.rgb(0, 102, 204), Color.rgb(255, 255, 255), Shader.TileMode.MIRROR));

						roundedBorder = (RoundRect) new RoundRect(20).setWidth(550).setHeight(480).setAlpha(0).setX(40).addToScreen();
						roundedBorder.paint.setARGB(0, 0, 0, 0);
						roundedBorder.stroke.setARGB(255, 0, 0, 0);
						roundedBorder.stroke.setStrokeWidth(1);
					}
					loading = false;
				}
			} catch (MalformedURLException e) {
			} catch (IOException e) {
			}
		}
	}

	/**
	 * Prevents scrolling outside the bounds
	 * 
	 * @param scroll Proposed scroll value
	 * @return Corrected scroll value
	 */
	private int scrollBound(int scroll) {
		if (scroll > 0) {
			scroll = 0;
		}
		int maximum = -(60 * highScores.size() - 400);
		if (maximum > 0) {
			maximum = 0;
		}
		if (scroll < maximum) {
			scroll = maximum;
		}
		return scroll;
	}

	@Override
	public boolean onTouch(MotionEvent event) {
		int mouseY = ThrowMe.getInstance().stage.camera.rTransformY((int) event.getY());

		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			downY = mouseY;
			scrollStart = scroll;
		} else if (event.getAction() == MotionEvent.ACTION_MOVE) {
			if (Math.abs(mouseY - downY) > 10) {
				scroll = scrollBound(scrollStart + mouseY - downY);
				movement = previousMovement - (mouseY - downY);
				previousMovement = mouseY - downY;
			} else {
				movement = 0;
			}
		} else if (event.getAction() == MotionEvent.ACTION_UP) {
			new Thread() {
				@Override
				public void run() {
					int direction = movement > 0 ? 1 : -1;
					while (Math.abs(movement) > 1) {
						scroll -= movement;
						int scroll2 = scrollBound(scroll);
						if (scroll != scroll2) {
							scroll = scroll2;
							movement = 0;
						} else {
							movement -= direction;
						}

						try {
							sleep(10);
						} catch (InterruptedException e) {
							System.out.println("o noes, thread interupted!");
						}
					}
				}
			}.start();
		} else {
			return false;
		}
		return true;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			new Main(new Object[] {true});
		}
		return super.onKeyDown(keyCode, event);
	}

}