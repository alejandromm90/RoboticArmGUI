package leapMotion;

/**
 * @author Alejandro Moran
 * 
 * */

import appInterface.Flavor;
import java.util.ArrayList;

import com.leapmotion.leap.*;
import com.leapmotion.leap.Gesture.State;

public class LeapMotionListener extends Listener{

	private String currentFlavor;
	private int numberFlavor;
	private boolean printing;
	private ArrayList<Flavor> flavors;
	
	public LeapMotionListener(ArrayList<Flavor> flavors){
		currentFlavor = "chocolate";
		numberFlavor = 0;
		printing = false;
		this.flavors = flavors;
	}
	
	public void onInit(Controller controller) {
        System.out.println("Initialized");
    }

    public void onConnect(Controller controller) {
        System.out.println("Connected");
        controller.enableGesture(Gesture.Type.TYPE_SWIPE);
        controller.enableGesture(Gesture.Type.TYPE_CIRCLE);
        controller.enableGesture(Gesture.Type.TYPE_SCREEN_TAP);
        controller.enableGesture(Gesture.Type.TYPE_KEY_TAP);
    }

    public void onDisconnect(Controller controller) {
        //Note: not dispatched when running in a debugger.
        System.out.println("Disconnected");
    }

    public void onExit(Controller controller) {
        System.out.println("Exited");
    }
    
    public void onFrame(Controller controller) {
        // Get the most recent frame and report some basic information
        Frame frame = controller.frame();

        if (!frame.hands().isEmpty()) {
            // Get the first hand
            Hand hand = frame.hands().get(0);

            // Check if the hand has any fingers
            FingerList fingers = hand.fingers();
            
            if(fingers.count() == 5) {
            	System.out.println("Printing " + currentFlavor);
            	printing = true;
            } else {
            	printing = false;
            }
            
            // if it is printing it should not recognize any gesture
            if (!printing) {   
            	GestureList gestures = frame.gestures();
//        	for (int i = 0; i < gestures.count(); i++) {
            	Gesture gesture = gestures.get(0);
            
	            switch (gesture.type()) {
	                case TYPE_CIRCLE:
	                    CircleGesture circle = new CircleGesture(gesture);
	
	                    // Calculate clock direction using the angle between circle normal and pointable
	                    String clockwiseness;
	                    if (circle.pointable().direction().angleTo(circle.normal()) <= Math.PI/4) {
	                        // Clockwise if angle is less than 90 degrees
	                        clockwiseness = "clockwise";
	                        
	                        if (numberFlavor == flavors.size()-1) {
	                        	numberFlavor = 0;
	                        } else {
	                        	numberFlavor ++;
	                        }
	                        
	                    } else {
	                    	
	                    	if (numberFlavor == 0) {
	                        	numberFlavor = flavors.size()-1;
	                        } else {
	                        	numberFlavor --;
	                        }
	                    	
	                        clockwiseness = "counterclockwise";
	                    }
	                    
	                    currentFlavor = flavors.get(numberFlavor).getFlavor();
	                    
	                    // Calculate angle swept since last frame
	                    double sweptAngle = 0;
	                    if (circle.state() != State.STATE_START) {
	                        CircleGesture previousUpdate = new CircleGesture(controller.frame(1).gesture(circle.id()));
	                        sweptAngle = (circle.progress() - previousUpdate.progress()) * 2 * Math.PI;
	                    }
	
	                    System.out.println("Circle id: " + circle.id()
	                               + ", " + circle.state()
	                               + ", progress: " + circle.progress()
	                               + ", radius: " + circle.radius()
	                               + ", angle: " + Math.toDegrees(sweptAngle)
	                               + ", " + clockwiseness);
	                    
	                    try {
	                    	System.out.println("waiting");
	            			Thread.sleep(1000);
	            			System.out.println("waited");
	            		} catch (InterruptedException e) {
	            			e.printStackTrace();
	            		}
	                    
	                    break;
	//                case TYPE_SWIPE:
	//                    SwipeGesture swipe = new SwipeGesture(gesture);
	//                    System.out.println("Swipe id: " + swipe.id()
	//                               + ", " + swipe.state()
	//                               + ", position: " + swipe.position()
	//                               + ", direction: " + swipe.direction()
	//                               + ", speed: " + swipe.speed());
	//                    break;
//	                case TYPE_SCREEN_TAP:
//	                    ScreenTapGesture screenTap = new ScreenTapGesture(gesture);
//	                    System.out.println("Screen Tap id: " + screenTap.id()
//	                               + ", " + screenTap.state()
//	                               + ", position: " + screenTap.position()
//	                               + ", direction: " + screenTap.direction());
//	                    break;
	//                case TYPE_KEY_TAP:
	//                    KeyTapGesture keyTap = new KeyTapGesture(gesture);
	//                    System.out.println("Key Tap id: " + keyTap.id()
	//                               + ", " + keyTap.state()
	//                               + ", position: " + keyTap.position()
	//                               + ", direction: " + keyTap.direction());
	//                    break;
	                default:
	//                    System.out.println("Unknown gesture type.");
	                    break;
	//            }
	        }
        
        
        
	        if (!frame.hands().isEmpty() || !gestures.isEmpty()) {
	           // System.out.println();
	        }
         }
       }
    }
    
    public int getNumberFlavor() {
    	return this.numberFlavor;
    }
    
    public void setNumberFlavor(int numberFlavor) {
    	this.numberFlavor = numberFlavor;
    }
    
    public boolean isPrinting() {
    	return printing;
    }
}
