package org.pet.pccontrol;

import java.awt.AWTException;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import javax.swing.JTextArea;
import javax.swing.text.BadLocationException;

import org.pet.pccontrol.MouseButtonClicker.ButtonAction;

class MouseMoverThread extends Thread {

	private BufferedInputStream inputStream;

	private JTextArea textArea;

	public MouseMoverThread(InputStream inputStream, OutputStream outputStream,
			JTextArea textArea) {
		this.inputStream = new BufferedInputStream(inputStream);
		this.textArea = textArea;
	}

	private String getLogString(String newLog) {
		String existing = textArea.getText();
		StringBuilder sb = new StringBuilder(existing);
		sb.append("\n").append(newLog);
		return sb.toString();
	}

	private void scrollToBottom() {
		try {
			int endPosition = textArea.getDocument().getLength();
			Rectangle bottom = textArea.modelToView(endPosition);
			textArea.scrollRectToVisible(bottom);
		} catch (BadLocationException e) {
			System.err.println("Could not scroll to " + e);
		}
	}

	public void run() {
		textArea.setText(getLogString("Start listenning..."));
		scrollToBottom();
		while (true) {
			InputStreamReader reader = null;
			BufferedReader bufferedReader = null;
			String line;
			try {
				reader = new InputStreamReader(inputStream);
				bufferedReader = new BufferedReader(reader);
				boolean isQuitNeeded = false;
				while ((line = bufferedReader.readLine()) != null) {
					Robot robot = new Robot();
					line = line.toLowerCase().trim();
					if (line.startsWith("button=")) {
						String[] buttonStrArr = line.split("\\=");
						int actionInt = Integer.parseInt(buttonStrArr[1]);
						MouseButtonClicker clicker = new MouseButtonClicker();
						switch (actionInt) {
						case 1:
							clicker.click(ButtonAction.LEFT_CLICK);
							break;
						case 2:
							clicker.click(ButtonAction.RIGHT_CLICK);
							break;
						}
					} else {
						String[] coordinateArr = line.split("\\,");
						int x = Integer.parseInt(coordinateArr[0]);
						int y = Integer.parseInt(coordinateArr[1]);

						Point mouseLocation = MouseInfo.getPointerInfo()
								.getLocation();
						int currentX = (int) mouseLocation.getX();
						int currentY = (int) mouseLocation.getY();

						robot.mouseMove(currentX + x, currentY + y);

						if (line.equals("quit")) {
							isQuitNeeded = true;
						}
					}
				}
				if (isQuitNeeded)
					break;
			} catch (IOException e) {
				break;
			} catch (AWTException e) {
				break;
			} finally {
				try {
					if (bufferedReader != null)
						bufferedReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				try {
					if (reader != null)
						reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		textArea.setText(getLogString("End listenning..."));
		scrollToBottom();
	}

}