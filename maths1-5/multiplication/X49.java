import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.awt.image.*;
/*
step=1 to request focus; step=2 show question; step 3 shows e.g. 7,14,21,etc ;  step 4 shows answer + score score
class picture contains the image with specifications for width and height, not accessible on transparent image
*/
public class X49 extends Applet
implements  KeyListener ,   MouseListener , Runnable
{int a=9, b=0, ia=1, ib=1,  s,  max=10, xo=24, yo=100,  q=0, step=1; int hh=0,ww=0, xspacing=1, yspacing=1;
int width, height,  mode=0 , score=0, nbtallimg=0, nbflatimg=0,nbsmallimg=0, pauselength=1100; 
Image offScreenImage, evalimg=null, a_tick, a_cross; Graphics offS;
int[] xfield= new int[4];int[] yfield= new int[4];  ////{300,50,650,900};{100,600,600,100};
Picture[] tallimg=new Picture[6];Picture[] flatimg=new Picture[6];Picture[] smallimg=new Picture[6];
Picture imgnow=null; 
 String guess="",sc="",label="", order="";
 Thread slide;
boolean isrunning=false,  pause=false, evaluation=true, showAnswers=false, commute=false, verticaldraw=false; 
Color lightgreen=new Color(67,227,180); Color lightlightgreen= new Color(117,245,220);
Random r= new Random();
Color FieldColor[]={new Color(209,201,209),new Color(220,173,171),new Color(229,221,229),new Color(240,193,191),new Color(246,227,170)};
Color fieldcolor;
Font smallfont=new Font("Verdana",Font.PLAIN,20); Font bigfont=new Font("Verdana",Font.PLAIN,40);

public void init()
{ mode=Integer.parseInt(getParameter("mode"));
addKeyListener(this); requestFocus(); 
addMouseListener(this); 
height=Integer.parseInt(getParameter("height")); width=Integer.parseInt(getParameter("width"));
pauselength=Integer.parseInt(getParameter("speed"));
offScreenImage=createImage(width, height); offS=offScreenImage.getGraphics();
offS.setColor(lightlightgreen);offS.fillRect(0,0,width, height);
evalimg=null; guess="";}

public Image getAlpha(Image img, int width, int height)
{Image a_img;
int[] pixels= new int[width*height];
PixelGrabber pg= new PixelGrabber(img,0,0,width,height,pixels,0,width);
try{pg.grabPixels();}catch(InterruptedException ie){};
for(int i=0; i<pixels.length; i++)
{int p = pixels[i];  int red = 0xff&(p>>16);
 int green = 0xff&(p>>8);  int blue = 0xff&(p);
 if ((red>245)&&(green>245)&&(blue>245))    {pixels[i]=(0x00000000);}
} a_img=createImage(new MemoryImageSource(width,height,pixels,0,width));
return a_img;
}


public void start()
{if(slide==null) {slide=new Thread(this);}  b=0; isrunning=true; slide.start(); 
} 
public void stop()
{if(slide!=null) {isrunning=false; slide=null;}
} 
public void run()
{while(isrunning==true)
  {if (pause==true)
    {for (int i=0; i<1000; i++)
        {  try{slide.sleep(30);}catch(InterruptedException e){};  if(pause==false) break;}}
  if((step==1)&&(pause==false))     {pause=true; repaint();
  int tracked=0; String name[]= new String[20]; Image img[]=new Image[20];
  try{MediaTracker tracker = new MediaTracker(this);
      StringTokenizer mst= new StringTokenizer(getParameter("img"),"$");
      while(mst.hasMoreTokens()&&tracked<20)      
	{name[tracked]=mst.nextToken();
	img[tracked]=getImage(getClass().getResource(name[tracked]+".gif"));
	tracker.addImage(img[tracked], tracked);  tracked++;}
      tracker.waitForAll();
    }catch(InterruptedException e){showStatus("No picture found");}
a_tick=getAlpha(img[0],104,77);a_cross=getAlpha(img[1],77,60);
for(int i=0; i<(tracked-2);i++) 
{int w=img[i+2].getWidth(null); int h= img[i+2].getHeight(null);
if(h>=80){tallimg[nbtallimg]=new Picture(getAlpha(img[i+2],w,h),w,h);nbtallimg++;}
else if (w>=80){flatimg[nbflatimg]=new Picture(getAlpha(img[i+2],w,h),w,h);nbflatimg++;}
else {smallimg[nbsmallimg]=new Picture(getAlpha(img[i+2],w,h),w,h);nbsmallimg++;}
}}
 if ((step>=2)&&(pause==false))
{ if(step==2){
newSum(); q++;
 hh=0; ww=0; commute=false; verticaldraw=false;
if(a<7){imgnow=tallimg[q%nbtallimg];} 
else if (b<7){imgnow=flatimg[q%nbflatimg]; }
else {imgnow=smallimg[q%nbsmallimg]; }
 ia=0; 
   if (mode==1) { order ="Tick in your answer"; label="";}  //Click to show the answer"
   else if (mode==0) {order="" ; label="SHOW ANSWER";}
  repaint();  pause=true;  }
 if ((step==3)&&(pause==false))
      {   for( ia=0; ia<a;ia++)
 		 {repaint(); try{slide.sleep(pauselength);} catch(InterruptedException ie){};} 
	     ia=a-1; guess=""+s;try{slide.sleep(pauselength*2);} catch(InterruptedException ie){};	 
	if(commute==true)
          {verticaldraw=true; for( ib=0; ib<b;ib++)
 	      	 {repaint(); try{slide.sleep(pauselength);} catch(InterruptedException ie){};}
               ib=b-1; sc=""+s; repaint();    
    }
 label="NEXT"; order="Click for next question"; repaint(); pause=true; }
	 //if(mode==1)  	 { guess=""+s;}
    //if(commute==false){repaint();}
	//if(commute==true){repaint(width-300, height-150, 300,150); commute=false;}
	//else {repaint();}
   // if (mode==1){ try{slide.sleep(2000);} catch(InterruptedException ie){};step=2;}  } 
  
}}}
public void newSum()
{evalimg=null; guess="";  
 b=r.nextInt(6); b=b+3; a=r.nextInt(max-2); a+=2;  s=a*b; sc=""; guess="";  ///random b between 4 and 9, random a between 1  and max
}
public void keyPressed(KeyEvent ke){}
//{if(ke.getKeyCode()==KeyEvent.VK_ENTER)
  //   {if (mode==1) evaluate(guess);}
//}
public void keyReleased(KeyEvent ke){};
public void keyTyped(KeyEvent ke)
 {char ch=ke.getKeyChar();
   if(mode==1){evaluation=false;// guess=guess+ke.getKeyChar();
  if(Character.isDigit(ch)){guess+=ch;
 if(guess.length()==2) evaluate(guess);
 else if((guess.length()==1)&&(s<10)) evaluate(guess);
 repaint();}
 }}
 public void evaluate(String guess)
{if(evaluation==false)
 {String stringans=""+s;   stringans= stringans.trim();
  guess= guess.trim();   
  if(guess.equals(stringans))    {  evalimg=a_tick; score++; }
                     else{ evalimg=a_cross;}
  step=3; repaint(); pause=false;   evaluation=true; 
}}
public void mouseClicked(MouseEvent me)
{ int coY= me.getY(); int coX=me.getX();
    if (step==1) {b=6; step=2; pause=false;  }  
  else if((coX<300)&&(coY>height-100)) 
  {mode++; mode=mode%2;
  if(mode==1){ q=0;score=0;} step=2; pause=false;}
  else  if(coX>width-300)
    {if (coY>height-80)
         {if ((step==2)&&(mode==0)) {pause=false; step=3; } ///if mode ==1 evaluate makes step --> 3
          else if (step==3){step=2; pause=false; repaint();}
	     }
	 else if ((coY>height-210)&&(step==2)){commute=true;pause=false; step=3;}
	}
  }
public void mousePressed(MouseEvent me){}
public void mouseReleased(MouseEvent me){}
public void mouseEntered(MouseEvent me){}
public void mouseExited(MouseEvent me){}

public void update(Graphics g)
{//showStatus("step =  "+step);
 offS.setColor(lightlightgreen); offS.fillRect(0,0,width, height);
  if(step==1)
  {offS.setColor(Color.black); offS.drawRect(255, 250, 165, 80);  offS.drawRect(252, 247, 171, 86); 
   offS.setFont(new Font("Verdana",Font.PLAIN,24));offS.drawString ("START" , 290, 300);
   offS.drawString ("MULTIPLYING  BY  3,  4,  5,  6,  7,  8  or  9" , 100, 120);
     offS.drawString ("Multiplication is Commutative" , 130, 180);
 
   offS.setFont(smallfont);offS.drawString ("Program written by annemarie.govindraj@gmail.com" , 20, 480); }
 else if(step>1)   ///// drawing the sum  and the next button ////////////////////
  {fieldcolor=FieldColor[q%(FieldColor.length)]; 
   int hh=imgnow.height;int ww=imgnow.width;
    xspacing=Math.min(ww, (int)((600-ww)/(b-1))  );
    yspacing=0; if(hh<=110){yspacing=Math.min((int)((480+hh)/a),hh); }
   else {yspacing=Math.min((int)((540-hh)/(a-1)),hh);}
if(verticaldraw==false){for (int ij=ia; ij>=0; ij--) 
    {xfield[0]=50+(yspacing/2)*ij; xfield[1]=50+(yspacing/2)*ij+xspacing*b;  
     xfield[2]=50+(ij+1)*(yspacing/2)+xspacing*b; xfield[3]=50+(yspacing/2)*(ij+1);  
     yfield[0]=600-5-ij*yspacing; yfield[1]=600-5-ij*yspacing; 
     yfield[2]=600+15-(ij+1)*yspacing; yfield[3]= 600+15-(ij+1)*yspacing; 
 offS.setColor(fieldcolor); offS.fillPolygon (xfield,yfield,4);}
   for (int ij=ia; ij>=0; ij--) {for(int ik=b-1; ik>=0;ik--)
    {offS.drawImage(imgnow.image,50+ ik*xspacing+(yspacing/2)*ij,600-imgnow.height-ij*yspacing, this); }
	 offS.setColor(Color.black);offS.setFont(bigfont);
     offS.drawString(""+(ij+1)*b,70+ww+(b-1)*xspacing +(yspacing/2)*ij, 580-ij*yspacing);   }  //720+yspacing*ij/2, 
    offS.setColor(Color.black);offS.setFont(bigfont);
    offS.drawString(""+a+"  x  "+b+" = "+ guess, 10,70);
 if(commute==true){ offS.drawString(""+b+"  x  "+a+" = ", 10,110);} }
 if(verticaldraw==true) {for(int ik=ib; ik>=0;ik--)
   {xfield[0]=50+10+ik*xspacing; xfield[1]=50-10+(ik+1)*xspacing;  
     xfield[2]=300-10+(ik+1)*xspacing; xfield[3]=300+10+ik*xspacing;  
     yfield[0]=600; yfield[1]=600;yfield[2]=100; yfield[3]= 100; 
  offS.setColor(fieldcolor); offS.fillPolygon (xfield,yfield,4);}
 for(int ik=ib; ik>=0;ik--){
    for (int ij=a-1; ij>=0; ij--)
    {offS.drawImage(imgnow.image,50+ ik*xspacing+(yspacing/2)*ij,600-imgnow.height-ij*yspacing, this); }
	 offS.setColor(Color.black);offS.setFont(bigfont);
   offS.drawString(""+(ik+1)*a,80+ik*xspacing+(yspacing/2)*(a-1) , 600-imgnow.height-(a-1)*yspacing);   }   
  if(commute==true){ offS.setColor(Color.black);offS.setFont(bigfont);
      offS.drawString(""+a+"  x  "+b+" = "+ guess, 10,70);
    offS.drawString(""+b+"  x  "+a+" = "+ sc, 10, 110); }}
 if(mode==1)  { offS.setColor(Color.black);offS.setFont(bigfont);
    offS.drawString(""+a+"  x  "+b+" = "+ guess, 10,70);}
	
 	////////////////////drawing buttons Next, to demo,  
 offS.setColor(lightgreen); offS.fillRoundRect(20,height-40, 200, 40, 25,25);
   offS.fillRoundRect(width-210,height-40, 200, 40, 25,25);
   if(mode==0){ offS.fillRoundRect(width-210,height-130, 200, 50, 25,25);}
  offS.setColor(Color.black); offS.setFont(smallfont); 
   if(mode==0) {offS.drawString("to exercises", 30, height-20);
 if(step==2)  {offS.drawString(" show ", width-200, height-110);   
   offS.drawString("commutativity", width-200, height-90);}}
 else if(mode==1) {offS.drawString("to demonstration", 30, height-20);}
   offS.drawString(label, width-200, height-20);   ///label = next or show answer
   offS.drawString(order, 10,30);
 if((mode==1)&&(step==3))  
 {offS.setColor(Color.black); offS.setFont(smallfont); //("Verdana",Font.PLAIN,18)); 
  offS.drawImage(evalimg, 30, 80,this);
			    offS.drawString("Your score =  " ,width-150,560);
				offS.drawString(""+ score +"/ "+q ,width-150,600);}
 // if(helpim==true){g.drawImage(helpimage, 200,200,this);}
 // else if(helpim==false){g.setColor(lightgreen) ; g.fillRect(width-50, 0, 50,60); g.setColor(Color.black); g.setFont(new Font("Verdana",Font.PLAIN,40)); g.drawString(" ?", width-40, 40); }
}paint(g);
}
public void paint(Graphics g)
{ g.drawImage(offScreenImage, 0,0, this);  
  
} 
public class Picture
{Image image; int width=0; int height=0;
 public Picture(Image i, int w, int h)
 {image=i;
  width=w; height=h;
    }}
}