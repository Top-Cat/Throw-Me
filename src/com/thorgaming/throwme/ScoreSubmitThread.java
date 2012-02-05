package com.thorgaming.throwme;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.app.Activity;

import com.thorgaming.throwme.screens.Highs;
import com.thorgaming.throwme.screens.Screen;
import com.thorgaming.throwme.screens.Submit;

public class ScoreSubmitThread extends Thread {

	private String name;
	private int score;
	private String deviceId;

	public ScoreSubmitThread(String name, int score, String deviceId) {
		this.name = name;
		this.score = score;
		this.deviceId = deviceId;
	}

	@Override
	public void run() {
		try {
			MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
			digest.update((name + score + "ZDbVEKAx5PAEe5Z" + deviceId).getBytes());
			byte[] messageDigest = digest.digest();
			StringBuffer hexString = new StringBuffer();
			for (byte element : messageDigest) {
				int j = 0xFF & element;
				if (j < 16) {
					hexString.append("0");
				}
				hexString.append(Integer.toHexString(j));
			}
			try {
				URL url = new URL("http://thomasc.co.uk/throwme/api.php?action=submit&score=" + score + "&name=" + name + "&deviceid=" + deviceId + "&checkstring=" + hexString);
				HttpURLConnection con = (HttpURLConnection) url.openConnection();
				BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
				String i = "";
				String out = "";
				while ((i = in.readLine()) != null) {
					out += i;
				}
				in.close();
				if (out.equals("k")) {
					ThrowMe.getInstance().stage.drawThread.runOnUi(new Runnable() {
						@Override
						public void run() {
							final Activity act = ThrowMe.getInstance();
							int check = Screen.checkCount;
							act.runOnUiThread(new Runnable() {
								@Override
								public void run() {
									new Highs(act, null);
								}
							});
							while (check == Screen.checkCount) {
							}
						}
					});
					return;
				}
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		System.out.println(ThrowMe.getInstance().screen);
		if (ThrowMe.getInstance().screen instanceof Submit) {
			System.out.println("yes");
			((Submit) ThrowMe.getInstance().screen).failSubmit();
		}
	}

}