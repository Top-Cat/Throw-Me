package thorgaming.throwme.Screens;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
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
import thorgaming.throwme.Stage;
import thorgaming.throwme.DispObj;
import thorgaming.throwme.MouseCallback;
import thorgaming.throwme.R;
import thorgaming.throwme.ThrowMe;
import thorgaming.throwme.logins;
import thorgaming.throwme.score;
import thorgaming.throwme.DispObjs.DispGif;
import thorgaming.throwme.DispObjs.DispRes;
import thorgaming.throwme.DispObjs.RoundRect;

public class Highs extends Screen {
	
	DispObj loader, b1,b2,b3,b4,l1,l2,l3,l4;
	RoundRect rrb, rb;
	Connection conn;
	public static final String PREFS_NAME = "throwmedevicekey";
	String deviceid = "";
	SecureRandom gen = new SecureRandom();
	
	int scroll = 0;
	
	List<score> highs = new ArrayList<score>();
	
	/** Called when the activity is first created. */
	public Highs(Stage stage, Activity a, Object[] o) {
		super(stage, a, o);
		
		SharedPreferences settings = activity.getSharedPreferences(PREFS_NAME, 0);
		deviceid = settings.getString("deviceid", UUID.randomUUID().toString());
		
		stage.t.resetGradient();
		
		RoundRect rr = new RoundRect(stage, 480, 480, 160, 0, 50, 20);
		rr.paint.setARGB(50, 0, 0, 0);
		rr.stroke.setARGB(150, 0, 0, 0);
		
		stage.draw = null;
		
		loader = new DispGif(activity.getApplicationContext(), stage, R.drawable.ajax, activity.getResources(), 128, 128, 336, 176, 255, 0, -1, 1);
		b1 = new DispRes(stage, R.drawable.day_bw, activity.getResources(), 150, 60, 645, 10, 255, 10);
		b1.setMouseDownEvent(new daysel());
		b2 = new DispRes(stage, R.drawable.week_bw, activity.getResources(), 150, 44, 645, 90, 255, 10);
		b2.setMouseDownEvent(new weeksel());
		b3 = new DispRes(stage, R.drawable.month_bw, activity.getResources(), 150, 33, 645, 154, 255, 10);
		b3.setMouseDownEvent(new monthsel());
		b4 = new DispRes(stage, R.drawable.time_bw, activity.getResources(), 150, 25, 645, 207, 255, 10);
		b4.setMouseDownEvent(new timesel());
		
		l1 = new DispRes(stage, R.drawable.day, activity.getResources(), 150, 60, 645, 10, 0, 0);
		l2 = new DispRes(stage, R.drawable.week, activity.getResources(), 150, 44, 645, 90, 0, 0);
		l3 = new DispRes(stage, R.drawable.month, activity.getResources(), 150, 33, 645, 154, 0, 0);
		l4 = new DispRes(stage, R.drawable.time, activity.getResources(), 150, 25, 645, 207, 0, 0);
		
		new GetHighs().start();
		boolean send = (o != null && o[0] != null) ? (Boolean) o[0] : false;
		final int score = (o != null && o[1] != null) ? (Integer) o[1] : 0;
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
	
	public void bw() {
		l1.setAlpha(0);
		l2.setAlpha(0);
		l3.setAlpha(0);
		l4.setAlpha(0);
		
		b1.setAlpha(255);
		b2.setAlpha(255);
		b3.setAlpha(255);
		b4.setAlpha(255);
	}
	
	public class timesel implements MouseCallback {

		@Override
		public void sendCallback(int x, int y) {
			bw();
			b4.setAlpha(0);
			l4.setAlpha(255);
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
			bw();
			b3.setAlpha(0);
			l3.setAlpha(255);
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
			bw();
			b2.setAlpha(0);
			l2.setAlpha(255);
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
			bw();
			b1.setAlpha(0);
			l1.setAlpha(255);
			load("SELECT * FROM scores WHERE date >= DATE_SUB(CURDATE(), INTERVAL 1 DAY) ORDER BY score DESC LIMIT 50");
		}

		@Override
		public void sendCallback() {
			sendCallback(0, 0);
		}

	}
	
	boolean t = false;
	
	class GetHighs extends Thread {
		
		@Override
		public void run() {
			t = true;
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
			t = false;
		}
		
	}
	
	int mx, my, y, s_t, p, m;
	
	private int cs(int s) {
		if (s > 0) { s = 0; }
		int ma = -((60 * highs.size()) - 400);
		if (ma > 0) { ma = 0; }
		if (s < ma) { s = ma; }
		return s;
	}
	
	public void insert(String q) {
		boolean s = false;
		while (!s) {
			while (conn == null) {
				if (!t) {
					new GetHighs().start();
				}
				ThrowMe.waiting(200);
			}
			if (conn != null) {
				PreparedStatement pr;
				try {
					pr = conn.prepareStatement(q);
					pr.executeUpdate();
					s = true;
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
				for (score i : highs) {
					i.destroy(stage);
				}
				highs.clear();
				loader.setAlpha(255);
				while (conn == null) {
					if (!t) {
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
							highs.add(new score(stage, a + 1, r.getString("name"), r.getInt("score"), r.getDate("date"), (a * 60)));
							a++;
						}
						
						if (rrb != null) {
							rrb.destroy(stage);
							rb.destroy(stage);
						}
						
						rrb = new RoundRect(stage, 491, 490, 155, -5, 0, 24);
						rrb.paint.setARGB(0, 0, 0, 0);
						rrb.stroke.setStrokeWidth(10);
						rrb.stroke.setShader(new LinearGradient(0, 0, 0, 480, Color.rgb(0, 102, 204), Color.rgb(255, 255, 255), Shader.TileMode.MIRROR));
						
						rb = new RoundRect(stage, 480, 480, 160, 0, 0, 20);
						rb.paint.setARGB(0, 0, 0, 0);
						rb.stroke.setARGB(255, 0, 0, 0);
						rb.stroke.setStrokeWidth(1);
						s = true;
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	public boolean onTouch(MotionEvent event) {
		mx = (int) event.getX();
		my = (int) event.getY();
		
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			y = my;
			s_t = scroll;
		} else if (event.getAction() == MotionEvent.ACTION_MOVE) {
			if (Math.abs(my - y) > 10) {
				scroll = cs(s_t + (my - y));
				m = p - (my - y);
				p = (my - y);
			} else {
				m = 0;
			}
		} else if (event.getAction() == MotionEvent.ACTION_UP) {
			int a = m > 0 ? 1 : -1;
			while (Math.abs(m) > 1) {
				scroll -= m;
				scroll = cs(scroll);
				m -= a;
				
				for (score i : highs) {
					i.setScroll(scroll);
				}
				
				ThrowMe.waiting(10);
			}
		}
		
		for (score i : highs) {
			i.setScroll(scroll);
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