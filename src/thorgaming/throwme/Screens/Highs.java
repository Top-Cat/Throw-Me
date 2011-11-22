package thorgaming.throwme.screens;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.EditText;
import thorgaming.throwme.DrawThread;
import thorgaming.throwme.Stage;
import thorgaming.throwme.MouseCallback;
import thorgaming.throwme.R;
import thorgaming.throwme.ThrowMe;
import thorgaming.throwme.logins;
import thorgaming.throwme.displayobjects.DispGif;
import thorgaming.throwme.displayobjects.DispObj;
import thorgaming.throwme.displayobjects.DispRes;
import thorgaming.throwme.displayobjects.RoundRect;
import thorgaming.throwme.displayobjects.ScoreRow;

public class Highs extends Screen {
	
	private DispObj loader;
	private DispObj dayBW;
	private DispObj weekBW;
	private DispObj monthBW;
	private DispObj timeBW;
	private DispObj dayC;
	private DispObj weekC;
	private DispObj monthC;
	private DispObj timeC;
	
	private RoundRect roundedBorderBackground, roundedBorder;
	private Connection conn;
	public static final String PREFS_NAME = "throwmedevicekey";
	private String deviceid = "";
	//private SecureRandom gen = new SecureRandom();
	
	private boolean gettingScores = false;
	private int mouseY;
	private int downY;
	private int scrollStart;
	private int previousMovement;
	private int movement;
	
	private int scroll = 0;
	
	private List<ScoreRow> highScores = new ArrayList<ScoreRow>();
	
	public Highs(Stage stage, Activity activity, Object[] data) {
		super(stage, activity, data);
		
		SharedPreferences settings = activity.getSharedPreferences(PREFS_NAME, 0);
		deviceid = settings.getString("deviceid", UUID.randomUUID().toString());
		
		DrawThread.resetGradient();
		
		RoundRect rr = new RoundRect(stage, 480, 480, 160, 0, 50, 20);
		rr.paint.setARGB(50, 0, 0, 0);
		rr.stroke.setARGB(150, 0, 0, 0);
		
		stage.draw = null;
		
		loader = new DispGif(activity.getApplicationContext(), stage, R.drawable.ajax, activity.getResources(), 128, 128, 336, 176, 255, 0, -1, 1);
		dayBW = new DispRes(stage, R.drawable.day_bw, activity.getResources(), 150, 60, 645, 10, 255, 10);
		dayBW.setMouseDownEvent(new daysel());
		weekBW = new DispRes(stage, R.drawable.week_bw, activity.getResources(), 150, 44, 645, 90, 255, 10);
		weekBW.setMouseDownEvent(new weeksel());
		monthBW = new DispRes(stage, R.drawable.month_bw, activity.getResources(), 150, 33, 645, 154, 255, 10);
		monthBW.setMouseDownEvent(new monthsel());
		timeBW = new DispRes(stage, R.drawable.time_bw, activity.getResources(), 150, 25, 645, 207, 255, 10);
		timeBW.setMouseDownEvent(new timesel());
		
		dayC = new DispRes(stage, R.drawable.day, activity.getResources(), 150, 60, 645, 10, 0, 0);
		weekC = new DispRes(stage, R.drawable.week, activity.getResources(), 150, 44, 645, 90, 0, 0);
		monthC = new DispRes(stage, R.drawable.month, activity.getResources(), 150, 33, 645, 154, 0, 0);
		timeC = new DispRes(stage, R.drawable.time, activity.getResources(), 150, 25, 645, 207, 0, 0);
		
		new GetHighs().start();
		boolean send = (data != null && data[0] != null) ? (Boolean) data[0] : false;
		final int score = (data != null && data[1] != null) ? (Integer) data[1] : 0;
		if (send) {
			final AlertDialog.Builder alert = new AlertDialog.Builder(activity);
			final EditText input = new EditText(activity);
			alert.setView(input);
			alert.setMessage("Enter name:");
			alert.setPositiveButton("Submit score", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					String value = input.getText().toString().trim();
					System.out.println(value + " scored " + score);
					MessageDigest digest;
		
					try {
						digest = java.security.MessageDigest.getInstance("MD5");
						digest.update((value+score+"ZDbVEKAx5PAEe5Z"+deviceid).getBytes());
						byte[] messageDigest = digest.digest();
						StringBuffer hexString = new StringBuffer();
						for (int i=0;i<messageDigest.length;i++) {
							hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
						}
						insert("INSERT INTO scores VALUES (NULL, '" + deviceid + "', '" + value + "', " + score + ", NULL, '" + hexString + "')");
						new daysel().sendCallback();
					} catch (NoSuchAlgorithmException e) {
						e.printStackTrace();
					}
				}
			});

			alert.setNegativeButton("Discard score",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int whichButton) {
							dialog.cancel();
						}
					});
			alert.setOnCancelListener(new OnCancelListener() {

				@Override
				public void onCancel(DialogInterface arg0) {
					new daysel().sendCallback();
				}
				
			});
			alert.show();
		} else {
			new daysel().sendCallback();
		}
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
	
	public class timesel implements MouseCallback {

		@Override
		public void sendCallback(int x, int y) {
			resetOptions();
			timeBW.setAlpha(0);
			timeC.setAlpha(255);
			load("SELECT * FROM scores ORDER BY score DESC LIMIT 50");
		}

		@Override
		public void sendCallback() {
			sendCallback(0, 0);
		}

	}
	
	public class monthsel implements MouseCallback {

		@Override
		public void sendCallback(int x, int y) {
			resetOptions();
			monthBW.setAlpha(0);
			monthC.setAlpha(255);
			load("SELECT * FROM scores WHERE date >= DATE_SUB(CURDATE(), INTERVAL 1 MONTH) ORDER BY score DESC LIMIT 50");
		}

		@Override
		public void sendCallback() {
			sendCallback(0, 0);
		}

	}
	
	public class weeksel implements MouseCallback {

		@Override
		public void sendCallback(int x, int y) {
			resetOptions();
			weekBW.setAlpha(0);
			weekC.setAlpha(255);
			load("SELECT * FROM scores WHERE date >= DATE_SUB(CURDATE(), INTERVAL 7 DAY) ORDER BY score DESC LIMIT 50");
		}

		@Override
		public void sendCallback() {
			sendCallback(0, 0);
		}

	}
	
	public class daysel implements MouseCallback {

		@Override
		public void sendCallback(int x, int y) {
			resetOptions();
			dayBW.setAlpha(0);
			dayC.setAlpha(255);
			load("SELECT * FROM scores WHERE date >= DATE_SUB(CURDATE(), INTERVAL 1 DAY) ORDER BY score DESC LIMIT 50");
		}

		@Override
		public void sendCallback() {
			sendCallback(0, 0);
		}

	}
	
	class GetHighs extends Thread {
		
		@Override
		public void run() {
			gettingScores = true;
			try {
				Class.forName("com.mysql.jdbc.Driver").newInstance();
				// Step 2: Establish the connection to the database. 
				conn = DriverManager.getConnection(new logins().url);
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			gettingScores = false;
		}
		
	}
	
	public void insert(String query) {
		boolean success = false;
		while (!success) {
			while (conn == null) {
				if (!gettingScores) {
					new GetHighs().start();
				}
				ThrowMe.waiting(200);
			}
			if (conn != null) {
				PreparedStatement pr;
				try {
					pr = conn.prepareStatement(query);
					pr.executeUpdate();
					success = true;
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public void load(String sql) {
		new load().setsql(sql).start();
	}
	
	class load extends Thread {
		String q;
		
		public Thread setsql(String sql) {
			q = sql;
			return this;
		}
		
		public void run() {
			boolean s = false;
			while (!s) {
				for (ScoreRow i : highScores) {
					i.destroy(stage);
				}
				highScores.clear();
				loader.setAlpha(255);
				while (conn == null) {
					if (!gettingScores) {
						new GetHighs().start();
					}
					ThrowMe.waiting(200);
				}
				if (conn != null) {
					int a = 0;
					
					PreparedStatement pr;
					try {
						pr = conn.prepareStatement(q);
						ResultSet r = pr.executeQuery();
						loader.setAlpha(0);
						while (r.next()) {
							highScores.add(new ScoreRow(stage, a + 1, r.getString("name"), r.getInt("score"), r.getDate("date"), (a * 60)));
							a++;
						}
						
						if (roundedBorderBackground != null) {
							roundedBorderBackground.destroy(stage);
							roundedBorder.destroy(stage);
						}
						
						roundedBorderBackground = new RoundRect(stage, 491, 490, 155, -5, 0, 24);
						roundedBorderBackground.paint.setARGB(0, 0, 0, 0);
						roundedBorderBackground.stroke.setStrokeWidth(10);
						roundedBorderBackground.stroke.setShader(new LinearGradient(0, 0, 0, 480, Color.rgb(0, 102, 204), Color.rgb(255, 255, 255), Shader.TileMode.MIRROR));
						
						roundedBorder = new RoundRect(stage, 480, 480, 160, 0, 0, 20);
						roundedBorder.paint.setARGB(0, 0, 0, 0);
						roundedBorder.stroke.setARGB(255, 0, 0, 0);
						roundedBorder.stroke.setStrokeWidth(1);
						s = true;
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	private int scrollBound(int scroll) {
		if (scroll > 0) { scroll = 0; }
		int maximum = -((60 * highScores.size()) - 400);
		if (maximum > 0) { maximum = 0; }
		if (scroll < maximum) { scroll = maximum; }
		return scroll;
	}
	
	public boolean onTouch(MotionEvent event) {
		mouseY = (int) event.getY();
		
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			downY = mouseY;
			scrollStart = scroll;
		} else if (event.getAction() == MotionEvent.ACTION_MOVE) {
			if (Math.abs(mouseY - downY) > 10) {
				scroll = scrollBound(scrollStart + (mouseY - downY));
				movement = previousMovement - (mouseY - downY);
				previousMovement = (mouseY - downY);
			} else {
				movement = 0;
			}
		} else if (event.getAction() == MotionEvent.ACTION_UP) {
			int direction = movement > 0 ? 1 : -1;
			while (Math.abs(movement) > 1) {
				scroll -= movement;
				scroll = scrollBound(scroll);
				movement -= direction;
				
				for (ScoreRow scoreRow : highScores) {
					scoreRow.setScroll(scroll);
				}
				
				ThrowMe.waiting(10);
			}
		}
		
		for (ScoreRow scoreRow : highScores) {
			scoreRow.setScroll(scroll);
		}
		
		return false;
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			new Main(stage, activity, new Object[] {true});
		}
		return super.onKeyDown(keyCode, event);
	}
	
}