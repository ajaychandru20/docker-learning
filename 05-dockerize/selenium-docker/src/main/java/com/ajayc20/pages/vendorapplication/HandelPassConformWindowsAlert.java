package com.ajayc20.pages.vendorapplication;

import org.openqa.selenium.WebDriver;

import java.awt.*;
import java.awt.event.KeyEvent;

public class HandelPassConformWindowsAlert {
    public static void handelPasswordWindowAlert(WebDriver driver){
        try {
            Robot robot = new Robot();
            robot.keyPress(KeyEvent.VK_ENTER);
            robot.keyRelease(KeyEvent.VK_ENTER);
            System.out.println("Password alert appeared");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
