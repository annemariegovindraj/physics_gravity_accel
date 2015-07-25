import java.util.LinkedList;
import java.util.Iterator;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.awt.image.*;

public class KButton{
	public static LinkedList<KButton> AllButtons= new LinkedList<KButton>();	//Helps keep track of all those buttons you don't want to put in a board
	//public static Graphics gfx;
	public int startX,startY, width,height;
	public String text;
	public Font font =new Font("Dialog", Font.PLAIN, 18);
	public Color bgcolor = new Color(47, 166, 187);
	public Color labelcolor= Color.black;
	public String label;
	public boolean visible;
	int margin = 10;
	
	/*
	public static KButton newFreeButton(String lbl, int coords[],String t, Color clr, Font font){
		KButton kb = new KButton(lbl, coords,t, clr, font);
		freeButtons.add(kb);
		return kb; }
	*/
	public KButton( String lbl, int coords[])
	{	label = lbl;
		startX = coords[0];
		startY = coords[1];
		width = coords[2];
		height = coords[3];
		visible = true;
		AllButtons.add(this);
	}
	public void setmyStyle(Color clr, Color lblcolor,Font afont) 
  {  bgcolor=clr; labelcolor=lblcolor; font=afont; }

	public void changeLabel(String newlabel)
	{label=newlabel;}
	
	//public void drawButton(){
	//	drawButton(gfx);   }
	public void drawButton (Graphics gg, ImageObserver observer)
	{if(!visible)
			return;	
		gg.setColor(bgcolor); 
		gg.fillRect(startX, startY, width,height);
		gg.setColor(Color.black); 
		gg.drawRect(startX+1, startY+1, width-2,height-2);gg.setFont(font); 
		FontMetrics fm=gg.getFontMetrics();
		int labelll=fm.stringWidth(label); int offset=(width-labelll)/2; if (offset<0) offset=0;
			
		gg.drawString(label, startX+offset, startY+height - margin);
	}
	
	public boolean inRange(int cX,int cY){
		return ( visible && cX > startX && cX < startX+width && cY > startY && cY < startY+height );
	}
	public boolean checkvisibility(){
		return visible;}
	
	public void setVisibility(boolean setTo){
		visible = setTo;
	}
	
	public static KButton resolveButton(int cX,int cY){	//Checks the free buttons
		Iterator<KButton> bI = AllButtons.iterator();
		KButton check;
		while(bI.hasNext()){
			check = bI.next();
			if((check.checkvisibility())&&(check.inRange(cX,cY) ))
				return check;
		}
		return null;
	}
	
	//public static void setGraphics(Graphics g){
	//	gfx = g;
	//}
	
	//public static void drawAllButtons(){
	//	drawAllButtons(gfx);	}
	
	public static void drawAllButtons(Graphics gg, ImageObserver observer){
		Iterator<KButton> bI = AllButtons.iterator();
		KButton currentButton;
		while(bI.hasNext()){
			currentButton = bI.next();
			currentButton.drawButton( gg,  observer);
		}
	}
}