package com.netkawai;

import java.awt.AWTException;
import java.awt.Robot;

public class VirtualNumpad implements INumpadListener {
	
	private Robot robot;
	
	public VirtualNumpad() throws AWTException {
		robot = new Robot();
	}

	@Override
    public void onKeyPressed(String keyName) {
        this.robot.keyPress(this.keycode(keyName));
    }

    @Override
    public void onKeyReleased(String keyName) {
        this.robot.keyRelease(this.keycode(keyName));
    }

	
    private int keycode(String keyName) {
    	System.out.println(keyName);
    	try {
            return 96 + Integer.parseInt(keyName);
        }
        catch (NumberFormatException e) {
        	String string = keyName.toLowerCase();
            switch (string.hashCode()) {
                case 42: {
                    if (string.equals("*")) return 106;
                    return 144;
                }
                case 43: {
                    if (string.equals("+")) return 107;
                    return 144;
                }
                case 45: {
                    if (string.equals("-")) return 109;
                    return 144;
                }
                case 46: {
                    if (string.equals(".")) return 110;
                    return 144;
                }
                case 47: {
                    if (string.equals("/")) return 111;
                    return 144;
                }
                case 96667352: {
                    if (!string.equals("enter")) return 144;
                    return 10;
                }
            }
        	return 144;
        }
    }    		
}
