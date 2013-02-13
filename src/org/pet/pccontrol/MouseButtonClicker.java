package org.pet.pccontrol;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.InputEvent;

public class MouseButtonClicker {

	public static enum ButtonAction {
		LEFT_CLICK, RIGHT_CLICK
	};

	private Robot robot;
	
	public MouseButtonClicker() throws AWTException{
		robot = new Robot();
	}
	
	public void click(ButtonAction action) {
		switch (action) {
		case LEFT_CLICK:
			leftClick();
			break;
		case RIGHT_CLICK:
			rightClick();
			break;
		}
	}
	
	private void leftClick(){
		robot.mousePress(InputEvent.BUTTON1_MASK);
        robot.mouseRelease(InputEvent.BUTTON1_MASK);
	}
	
	private void rightClick(){
        robot.mousePress(InputEvent.BUTTON3_MASK);
        robot.mouseRelease(InputEvent.BUTTON3_MASK);
	}

}
