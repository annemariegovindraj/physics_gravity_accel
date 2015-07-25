import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.awt.image.*;

/*<applet code="Gravity.class" width=850 height=550>
</applet>long program, without external classes
  1 tt is .05 sec, v in m/sec, Ep and Ek in Joule
  yo departure of ball in pixels from top of picture, d in m , 1200 pix =120 m of tower 
  divy each division in units m or J, unity squares per unit e.g. 0.1 square per J => 1200J=120 squares 
  divy *unity should be around 10-20
  two function s to write text are used , nl text editor TextDrawLines, for use of superscript (  $  !)  and fractions ($  !/   !). No superscripts inside the fractions.  
  
*/
public class Gravity extends Applet implements MouseListener, KeyListener , Runnable
{////calculations for step2 and /// 
boolean isrunning=true, downwards=true, pause1=true , pause2=false, on=false, evaluated=false,exerciseover=false, superimpose=false; 
 int height, width,   pauselength=30, xo=50, yo=50, x=xo,y=yo, x1=50,x2=200,  y1=50, y2=300,  A=150,B=80;
 int  depart=0,  a=10, tt=0, lasttt=0, jj=0 , po=18, qo=385,graphasked=-1, exp=0, step=9, springwidth=280; final int ho=115;
float v=0, d=0, dmax=0, m=(float)(0.1), Ek=0, Ep=1150, ampl=(float)(0.2); //maximum height 115 m but origin is at d =0, yo=48;
 float dx=0, dy=0; int dwidth[]={410,410,280,280,410}; int dheight[]={400,400, 400 ,550, 400};
float lasty[]; float divy[]={5,10,2,(float)0.5,10};  float unity[]={2,1,8,50,1}; 
  String[] variab={"speed(m/sec)", "displacement(m)", "speed(m/sec)", "displacement(m)", "time(sec)" };
Thread roller;
 int Xapple[]={120,232, 280};int Yapple[]={120, 50,80};int Xmoon=920, Ymoon=300;
 int Yball[]={106,91,70,42,4};
 ////calculations for step3 
 int ta=0, flicker=0; String []guess=new String[5]; int correctvar[]; float corrvar[]; 
 String[] formularray= {"v=u+a*t","s=a*t²/2","s = H +u*t+a*t²/2 with H initial height","v²-u² = 2*a*s","2*H/a"};
 int foundvar[]={0,0,0,0,0}; boolean flickering=false;
/////////////////display///////////////
 Color lightblue= new Color(0,255,255); Color grass=new Color(0,136,40);Color green= new Color(78,214,173);
 Color bluegreen= new Color(47, 166, 187); Color lightyellow= new Color(255,245,191);Color darkblue=new Color(0,75,151);
Image offScreenImage,  msgimg, msgimg2, bigScene, scene_now, boyimg,ballthrow, ballimg;
Image correctimg, wrongimg, evalimg=null, earthimg, moonimg, appleimg, newtonimg, branchimg, solarsystemimg, galaxiesimg;
Image [] bgitems=new Image[20]; Image graphim[]= new Image[5]; Image ballthrowing[]= new Image[5];
Graphics offS, bigg; 
Font big = new Font("Dialog", Font.PLAIN, 20); Font textfont= new Font("Verdana", Font.PLAIN, 18); FontMetrics fm; 
int  lineheight=0;  

public void init()
{addKeyListener(this); requestFocus();
height=Integer.parseInt(getParameter("height"));
width=Integer.parseInt(getParameter("width"));
pauselength=40;
addMouseListener(this); setBackground(green);
offScreenImage=createImage(width, height); offS=offScreenImage.getGraphics();
offS.setFont(big); offS.setColor(green);offS.fillRect(0,0,width,height);
 bigScene=createImage( 200,1300); bigg=bigScene.getGraphics();
bigg.setColor(lightblue); bigg.fillRect(0,0,200,1300);bigg.setColor(grass); bigg.fillRect(0,1200,200,100);
 repaint();}

public Image getAlpha(Image img, int width, int height)
{Image a_img;
int[] pixels= new int[width*height];
PixelGrabber pg= new PixelGrabber(img,0,0,width,height,pixels,0,width);
try{pg.grabPixels();}catch(InterruptedException ie){};
for(int i=0; i<pixels.length; i++)
{int p = pixels[i];  int red = 0xff&(p>>16);
 int green = 0xff&(p>>8); int blue = 0xff&(p);
 if ((red>245)&&(green>245)&&(blue>245)){pixels[i]=(0x00000000);}
}
a_img=createImage(new MemoryImageSource(width,height,pixels,0,width));
return a_img;
}
public void prepareNextRun()
{for( jj=0; jj<5;jj++) {graphim[jj]= createImage(dwidth[jj],dheight[jj]); 
setAxis(graphim[jj], divy[jj],unity[jj]); graphasked=1; }
lasttt=0; v=0; d=0; dmax=0;  Ek=0; Ep=1150; y=48;
lasty= new float [4];}

public void start()
{if(roller==null)   {roller=new Thread(this);}
  isrunning=true; roller.start(); pause1=true;
 }
public void stop()
{if(roller!=null)  { isrunning=false; roller=null;} 
  } 
public void run()
{ while (isrunning==true)
 {  if (step==9){exerciseover=false; msgimg=textDrawLines(250,40," CENTRIPETAL FORCE "); 
  msgimg2=textDrawLines(450,300,"The stone moves along a circular path, continuously changing direction. When the string breaks, the stone flies off along a straight line. That is because it continues in the same direction as it was moving at the instant the string broke (Newton's first law). So, to keep the stone in a circular path, a force is needed. This is the CENTRIPETAL FORCE"); 
int tracked=0; String imgname[]= new String[20]; 
try{MediaTracker tracker = new MediaTracker(this);
      StringTokenizer mst= new StringTokenizer(getParameter("img"),"$");
      while(mst.hasMoreTokens()&&tracked<20)      
	{imgname[tracked]=mst.nextToken();
	bgitems[tracked]=getImage(getClass().getResource(imgname[tracked]+".gif"));
  	tracker.addImage(bgitems[tracked], tracked);
	tracked++;}
    tracker.waitForAll();
    }catch(InterruptedException e){showStatus("No picture found");}
boyimg=getAlpha(bgitems[4], 107,210);  evaluated=false; repaint();
correctimg=getAlpha(bgitems[2],104,77); wrongimg=getAlpha(bgitems[3],77,60);
earthimg=getAlpha(bgitems[5], 133,132);moonimg=getAlpha(bgitems[6], 165,163);
appleimg=getAlpha(bgitems[7], 42,47); newtonimg=bgitems[8]; evaluated=true;
branchimg=getAlpha(bgitems[9],311,150);solarsystemimg=bgitems[10]; galaxiesimg=bgitems[11];
ballthrow=bgitems[12]; ballimg=getAlpha(bgitems[13],39,39);

 ////////////////////////////anime of boy turning sling///////////////// 
float th=0;A=150; B=80; evaluated=true; for (tt =0; tt<153; tt++) 
{ th=(float)(tt*Math.PI/20); x=(int)(A*Math.cos(th)); y=(int)(-B*Math.sin(th)); 
repaint();try{roller.sleep(20);   } catch(InterruptedException ie){};}
 int Yesc=y; int Xesc=x; x2=200;  //ang speed =w=589 pix/sec, th=3*PI/2+PI/6
 for (tt =154; tt<195; tt++){x=Xesc+(int) (589*0.86*(tt-154)/40); y=Yesc-(int)(589*(tt-154)/80);if (x>width-x2) break;
 //x= (int)(32.8*(float)(tt-150));if (x>width-x2) break;
 repaint();try{roller.sleep(20);   } catch(InterruptedException ie){};}
exerciseover=true; pause2=true;   repaint();  try{  synchronized(this){while(pause2==true) wait();} }  catch(InterruptedException e){};  
 step=10; }
if(step==10){//////////anime earth and moon
exerciseover=false; msgimg=textDrawLines(250,40," GRAVITATIONAL FORCE "); 
  msgimg2=textDrawLines(450,230,"The motion of the moon around the earth is also due to a centripetal force, no string though! The 	GRAVITATIONAL FORCE attracts the moon to the earth (and the earth to the moon.) ");
 float th=0;A=200; B=140;for (tt =0; tt<200; tt++)
{ th=(float)(tt*Math.PI/100); x=(int)(A*Math.cos(th)); y=(int)(-B*Math.sin(th)); 
repaint();try{roller.sleep(80);   } catch(InterruptedException ie){};}
 exerciseover=true; pause2=true;   repaint(); 
//////////preparing next text
Image msgimg5=createImage(450,500); Graphics msg5=msgimg5.getGraphics(); msg5.setFont(textfont);
  fm=msg5.getFontMetrics(); lineheight=2 + fm.getHeight();  
  msg5.setColor(lightyellow); msg5.fillRect(0,0,450,500);
   msg5.setColor(Color.black);
  Image msgimg4= textDrawLines(450,304,"Isaac Newton wondered if the force that made the apple fall was the same as the one keeping the moon in an orbit around the earth. He also used Kepler's laws of planetary motion to derive his UNIVERSAL LAW OF GRAVITATION .  .  . .  .  .  M and m are the masses of the 2 objects . R is the distance between the 2 objects .");
   msg5.drawImage(msgimg4,0,0,this) ; 
 msg5.drawString("F =", 50, 5*lineheight+40 ); msg5.drawLine(80,5*lineheight+37,200,5*lineheight+37);
   msg5.drawString("G x M x m", 80,5*lineheight+35); msg5.drawString("R²", 120,5*lineheight+60);
  Image msgimg3=textGetLines (" where G = 6.67 x 10$-11! N m² /kg² . is the universal gravitational constant",450);
   msg5.drawImage(msgimg3, 8,  5*lineheight+71,this); 
   try{  synchronized(this){while(pause2==true) wait();} }  catch(InterruptedException e){};  
   msgimg2=msgimg5;}

 if(step==11){///////////////////newton + apple+ moon
exerciseover=false; //msgimg=textDrawLines(250,50," UNIVERSAL LAW OF GRAVITATION "); 
  Xapple[0]=120; Xapple[2]= 280; Yapple[0]=120; Yapple[2]=80; repaint();
  repaint();try{roller.sleep(160);   } catch(InterruptedException ie){};
for (tt =0; tt<150; tt++){Yapple[1]=(int)(50+(float) 10*(tt)*(tt)/8); if (Yapple[1]>(height-120)) break;
 repaint();try{roller.sleep(pauselength);   } catch(InterruptedException ie){};}
  float th=(float)(Math.PI/6); A=1040; B=600;
  for (tt =0; tt<400; tt++)
{ th=(float)(Math.PI/6+tt*Math.PI/1200); Xmoon=(int)(A*Math.cos(th)); Ymoon=(int)(-B*Math.sin(th)); //if(Xmoon<0) Xmoon=0;
if(tt>200){Yapple[2]=(int)(80+(float) 10*(tt-200)*(tt-200)/8); if (Yapple[2]>(height-80)) Yapple[2]=height-80;}
repaint();try{roller.sleep(pauselength);   } catch(InterruptedException ie){};}
for (tt =0; tt<150; tt++){Yapple[0]=(int)(120+(float) 10*(tt)*(tt)/8); if (Yapple[0]>(height-270)) break;
 repaint();try{roller.sleep(pauselength);   } catch(InterruptedException ie){};}
  exerciseover=true; pause2=true;   repaint();  
  try{  synchronized(this){while(pause2==true) wait();}  } catch(InterruptedException e){};  
}
if(step==12){///////////////////solar system ////////galaxies
msgimg=textDrawLines(250,200," This UNIVERSAL LAW OF GRAVITATION  applies to all planets of the solar system "); 
   msgimg2=textDrawLines(250,140," This UNIVERSAL LAW OF GRAVITATION  applies to all galaxies of the universe "); 
   exerciseover=true; pause2=true;   repaint();  
///////////preparing  text of step 13 
 Image msgimg5=createImage(600,500); Graphics msg5=msgimg5.getGraphics(); 
 msg5.setFont(textfont);  msg5.setColor(lightyellow); msg5.fillRect(0,0,600,500); 
 fm=msg5.getFontMetrics(); lineheight=2 + fm.getHeight();  

  Image msgimg6=textGetLines("To calculate the force with which the Sun attracts the Earth, assume ; . mass of the Sun : 2 x 10$30! kg . mass of the Earth : 6 x 10$24! kg . radius of Earth's orbit around the Sun : 15 x 10$10! m  .  .  .  .  We obtain 3.56 x 10 $22! N",600);
   Image msgimg3=textGetLines ("6.67 x 2 x 6 x 10$-11+30+24! N m² kg² ",450); int lw= fm.stringWidth("6.67 x 2 x 6 x 10$-11+30+24! N m² kg²");
  Image msgimg4=textGetLines ( "15 x 15 x  10$20! kg² m²", 450);
  msg5.setColor(Color.black);
  msg5.drawImage(msgimg6, 0, 0,this);
  msg5.drawImage(msgimg3, 50, 154,this);
  msg5.drawImage(msgimg4, 100, lineheight+162,this);
  msg5.drawString(" F = ", 10, lineheight+178); msg5.drawLine(50,lineheight+172,50 +lw,lineheight+172);
   try{  synchronized(this){while(pause2==true) wait();}  } catch(InterruptedException e){};  
msgimg2=msgimg5;}

if(step==13){///////////calculting force Earth - Sun
  repaint();
 exerciseover=true; pause2=true;   repaint();  
 ///////////preparing  text of step 14
 Image msgimg5=createImage(700,500); Graphics msg5=msgimg5.getGraphics(); 
 msg5.setFont(textfont);  msg5.setColor(lightyellow); msg5.fillRect(0,0,700,500); 
 fm=msg5.getFontMetrics(); lineheight=2 + fm.getHeight();  
 msg5.setColor(Color.black);                                                                                                                                                            ///// N m² /kg²   x kg  / m²  = m/sec²
  Image msgimg6=textGetLines(" Let's apply this Universal Law of Gravitation to the Earth and to us . If we compare the Universal Law of Gravitation F = $G x M x m!/R²!   . to Newton's second law of motion  F = m x a ,   we see that indeed . $G x M!/R²! has the dimensions of an acceleration . $N m²!/kg²! x  $ kg !/m²! =  $m!/sec²! .  This value is a constant  for objects near the surface of the earth. We use : . G= universal gravitation constant : 6.67 x 10$-11! N m²/kg² . M=mass of earth : 6.0 x 10$24! kg . R= radius of the earth : 6.37 X 10$6! m  .  .  .  . We obtain the value 9.81 m/sec², which we call g .", 700);
  msg5.drawImage(msgimg6, 0,0,this);
 Image msgimg3=textGetLines ("6.67 x 6.0 x 10$-11+24! N m² kg ",450);
  Image msgimg4=textGetLines ("6.37 x 6.37 x 10$12! kg² m²", 300);
  msg5.setColor(Color.black);int lw= fm.stringWidth(" acceleration = ");
  msg5.drawImage(msgimg3, lw, lineheight+340,this);
  msg5.drawImage(msgimg4, lw, lineheight+374,this);
  msg5.drawString(" acceleration = ", 0, 3*lineheight+340); msg5.drawLine(lw,3*lineheight+335,lw+300,3*lineheight+335);
 try{ synchronized(this){while(pause2==true) wait();}  } catch(InterruptedException e){};
  msgimg2=msgimg5;}
if(step==14){///////////////////calcul of g 
  exerciseover=true; pause2=true; repaint();     
 
  try{  synchronized(this){while(pause2==true) wait();}  } catch(InterruptedException e){};  
}
 if(step==15){msgimg2=textDrawLines(500,300," In the next exercise we calculate the speed and displacement of a ball falling from a 115 m high building. It's a FREE FALL because no forces other than gravity are considered. We use the formulae : . v=u+at .  s=ut+at²/2 . where a = g = 9.81 m/sec² .  . Use the buttons to see the graphs of v and s varying with time, or to stop the animation"); 
  exerciseover=true; pause2=true; repaint();     
  try{  synchronized(this){while(pause2==true) wait();}  } catch(InterruptedException e){};  

 step=2;}

  if(step==2){/////falling ball animation + computer-calculations
 msgimg=textDrawLines(500,40, "FREE FALL");
for (int i=0;i<37; i++) {bigg.drawImage(bgitems[0],0,(i+2)*30,this);} bigg.drawImage(bgitems[1], 0, 30*39,this);
tt=0; exerciseover=false; prepareNextRun(); repaint(); 
 pause1=true; try{  synchronized(this){while(pause1==true) wait();} }  catch(InterruptedException e){};
  int Tup=0, Tdn=0; tt=0; float Vdn=0;  v=0; d=0; dmax=0; Ek=0; Ep=1150;lasty[3]=1150; downwards=true; 
  while ( tt<240)
  {if(downwards==true){v=(float) a*(tt-Tup)/20; d=dmax+(float) a*(tt-Tup)*(tt-Tup)/800;
                        y= (int)(yo +d*10);
                       if(y>=1190) {Tdn=tt; if(exp==0){Vdn=v;} else if (exp==1) {Vdn=(float)(v*0.8);} else {break;} 
					    downwards=false;}}
     else{v= Vdn-(float)a*(tt-Tdn)/20; d =ho- Vdn*(tt-Tdn)/20+(float) a*(tt-Tdn)*(tt-Tdn)/800;  ///float ???
    y=yo+(int)(d*10); if (v<=0) {downwards=true; Tup=tt;dmax=d;} }
   depart=y-300; if(y<300) {depart=0;} else if (y> (1600-height)) {depart=1300-height;}
 float nwy=0; for( jj=0; jj<2; jj++){switch(jj){case 0: nwy=v; break;case 1: nwy=d; break; default: nwy=1;} 
  addPoint(graphim[jj], jj, nwy); } lasttt=tt;
   repaint();  
try{synchronized(this){while(pause1==true) wait();}lasttt=tt; tt++;roller.sleep(pauselength);   } catch(InterruptedException ie){};
 } pause2=true; exerciseover=true; superimpose=false; repaint(); 
 try{synchronized(this){while(pause2==true) wait();  }} catch(InterruptedException ie){}; }

 if(step==3){ //////////////////calculations by student/////////////////////////////
 ta=0; 
 if(ta==0){msgimg=textDrawLines(500,80, "Let us calculate some points. Here are the initial conditions. ");
   guess[0]="0"; guess[1]="0";guess[2]="0"; //guess[3]=""+1150; guess[4]=""; 
   flicker=0; repaint();    pause1=true;    
   try{ synchronized(this){while(pause1==true) wait();}} catch(InterruptedException ie){};
   ta=1; } 
   while( ta<5)
  { correctvar=new int[5]; evaluated=false; 
   msgimg=textDrawLines(400,80, "Type in the value for time="+ta+" and press ENTER ; Compare with the graph.");
   pause1=true; for (int ii=0;ii<3;ii++){guess[ii]="";foundvar[ii]=0;} evalimg=null;
   flicker=0; flickering=true; repaint();
   correctvar[0]=a*ta; correctvar[1]=a*ta*ta/2; 
	  //correctvar[2]=(correctvar[0])*(correctvar[0])/2; correctvar[3]=(ho-correctvar[1])*a;correctvar[4]=0;
 	int counter=0; 
	while(flickering==true)
   {counter++;  if (counter%12==0){ on=!on;} 
  
   if (evaluated==true){ flicker++; showStatus("flicker " +flicker+" , t = "+ta); 
                 if (flicker==2) {flickering=false;};evaluated=false;}
   repaint();  try{roller.sleep(50);}catch(InterruptedException e){};}
 on=false; repaint(); //flickering=false;
    pause1=true; try{ synchronized(this){while(pause1==true) wait();}} catch(InterruptedException ie){};
     ta++; 
   }step=4; }
  
 if(step==4){  ////////////calculate time of impact///////////////////
 msgimg=textDrawLines(480,80,"At what time will the ball touch the ground? With what speed? Type your answer +ENTER");	
 msgimg2=textDrawLines(480,60,"Hint : when displacement equals height of tower: s=H");
 pause1=true; for (int ii=0;ii<3;ii++){guess[ii]="";foundvar[ii]=0;} evalimg=null;evaluated=false;
 corrvar=new float[3]; flicker=0; flickering=true;
 corrvar[0]=(2*ho/a); corrvar[1]=a* (float)Math.sqrt(corrvar[0]);corrvar[2]=0; //int tc=(int)(corrvar[0]*100); corrvar[0]=tc/100; 
 int counter=0; 
	while(flickering==true)
   {counter++; if (counter%12==0){ on=!on;} 
    if (evaluated==true){ flicker++;if(flicker==2){flickering=false;} evaluated=false;}
	repaint(); try{roller.sleep(50);}catch(InterruptedException e){};} 
	on=false; repaint();  pause2=true; 	
	/////////////// preparing anime step 5 , 6       ball thrown up///////////
try{	int tw= 470, th=  193; //(ballthrow.getWidth(null)); //(ballthrow.getHeight(null));
CropImageFilter ff;
FilteredImageSource fis;
MediaTracker t=new MediaTracker(this);
for (int i=0;i<5; i++)
  {ff=new CropImageFilter((int)(tw*i/5), 0, (int)(tw/5), 193);
   fis=new FilteredImageSource(ballthrow.getSource(), ff);
   Image imgg=createImage(fis);
   t.addImage(ballthrowing[i],i);
   t.waitForID(i);
  ballthrowing[i]=getAlpha(imgg, tw/5, 193);}
 } catch(InterruptedException ie){};
 try{ synchronized(this){while(pause2==true) wait();}} catch(InterruptedException ie){};
   step=5;}
  
  if(step==5)
  {msgimg=textDrawLines(400, 500, "In the previous exercise, when the ball is dropped, acceleration, speed and displacement have the same direction i.e. downwards. So, it is easy to take the positive direction downwards, so  a, v and s are positive. The origin is at the top of the building. .  . In this example, a ball is thrown up. Acceleration g is still downward. Velocity and displacement are first upwards. Velocity  decreases until v=0. After that, direction of velocity and displacement become downward. Here, we take the positive direction upwards. i.e. displacement is counted from the ground up.");
   exerciseover=false; pauselength=40; 
 scene_now = ballthrowing[0]; y=Yball[0]-39;v=0; d=0; tt=0; lasttt=0;
 repaint();
 float vo=(float)6.4; tt=0; pause1=true;
 try{synchronized(this){while(pause1==true) wait();} } catch(InterruptedException ie){};
lasty= new float [5];    /////throwing ball up  animation 
 for(int tu=0;tu<4;tu++)
  {v=0; d=0; scene_now = ballthrowing[tu]; y=Yball[tu]-39;   repaint();  
   try{ roller.sleep(pauselength);   } catch(InterruptedException ie){}; }
 for(tt=0;tt<37;tt++){scene_now=ballthrowing[4];                 //// d in m, y in pixels  1m=150 pix
   v=vo-a*(float)(tt)/25; d=vo*tt/25-(float)a*tt*tt/1250; y= -(int)(150*d); //if(y>180) break;
      repaint();  
try{synchronized(this){while(pause1==true) wait();}lasttt=tt; roller.sleep(pauselength);   } catch(InterruptedException ie){};
 } pause2=true; exerciseover=true; repaint(); 
 try{synchronized(this){while(pause2==true) wait();  }} catch(InterruptedException ie){}; 
 } 
  
 
 if(step==6) /////throwing ball up  animation 
 {float vo=(float)6.4; float so= (float) 1.15; tt=0; 
  exerciseover=false; pauselength=40; scene_now = ballthrowing[0];
 prepareNextRun(); 
 graphasked =3;
 v=0; d=so; lasttt=0; y=Yball[0]-39; 
 repaint(); 
 pause1=true; try{  synchronized(this){while(pause1==true) wait();} }  catch(InterruptedException e){}; 
lasty= new float [5]; 
 for(int tu=0;tu<4;tu++)
  {v=0; d=so; scene_now = ballthrowing[tu]; y=Yball[tu]-39;
  repaint();  try{roller.sleep(pauselength);   } catch(InterruptedException ie){};}
 tt=-1; do{tt++; scene_now=ballthrowing[4];                 //// d in m, y in pixels  1m=150 pix
   v=vo-a*(float)(tt)/25; d= so+vo*tt/25-(float)a*tt*tt/1250;if(d<0)break; y= (int)(150*( so-d)); //150 =3 pix/[] and unity=50;
   float nwy=0; for( jj=2; jj<4; jj++){switch(jj){case 2: nwy=v; break;case 3: if (d<0) d=0;nwy=d;  break; default: nwy=1;} 
  addPoint(graphim[jj], jj, nwy);
 }  repaint();  
try{synchronized(this){while(pause1==true) wait();}lasttt=tt; roller.sleep(pauselength);   } catch(InterruptedException ie){};
 } while (tt<36); pause2=true; exerciseover=true; repaint(); 
 try{synchronized(this){while(pause2==true) wait();  }} catch(InterruptedException ie){}; 
 }
 if(step==7){step=16;}
    if(step==16)
  {/////////////////////////calculating height reached etc //////////////
  msgimg=textDrawLines(400, 150, "Let's calculate the following : . When does the ball reach it's highest point?  Hint : at v=0 . How high will the ball go? . Type in your answer  +ENTER");
  flickering=true; corrvar=new float[5]; jj=2;
  //   msgimg2=textDrawLines(200, 200, "");   pause1=true; 
  for (int ii=0;ii<5;ii++){guess[ii]="";foundvar[ii]=0; corrvar[ii]=0;} 
  evalimg=null;evaluated=false;
   flicker=2;   float vo=(float) 6.4; float so= (float) 1.152;
   corrvar[2]= vo/a; corrvar[3]=so+vo*corrvar[2]-(a*corrvar[2]*corrvar[2])/2; 
   int counter=0; //repaint();

	while(flickering==true)
   {counter++; if (counter%12==0){ on=!on;} 
    if (evaluated==true){ flicker++;if(flicker==4){flickering=false;} evaluated=false;}
	repaint(); try{roller.sleep(50);}catch(InterruptedException e){};} 
	on=false;

 repaint();  pause1=true; 	
  try{ synchronized(this){while(pause1==true) wait();}} catch(InterruptedException ie){};
  step=17; }
 if(step==17)
 {/////////////////////////calculating when reaching initial height //////////////
  msgimg=textDrawLines(400, 150, " When does the ball reach it's initial level again( that is the hands of the boy)? .  What is the speed of the ball then ? Hint s=H. . Type in your answer  +ENTER");
  flickering=true; corrvar=new float[5]; 
  //   msgimg2=textDrawLines(200, 200, "");   pause1=true; 
  for (int ii=0;ii<5;ii++){guess[ii]="";foundvar[ii]=0; corrvar[ii]=0;} 
  evalimg=null;evaluated=false;
   flicker=2;   float vo=(float) 6.4; float so= (float) 1.152;
   corrvar[2]= 2*vo/a; corrvar[3]= -vo;
   int counter=0; //repaint();
	while(flickering==true)
   {counter++; if (counter%12==0){ on=!on;} 
    if (evaluated==true){ flicker++; if(flicker>=4){flickering=false;} evaluated=false;}
	repaint(); try{roller.sleep(50);}catch(InterruptedException e){};} 
	on=false;
  repaint();  pause1=true; 	
  try{ synchronized(this){while(pause1==true) wait();}} catch(InterruptedException ie){};

 isrunning=false; msgimg=textDrawLines(300,200, "END OF THE PROGRAM . Program written by annemarie.govindraj@gmail.com");
  repaint();}
}}
public void keyReleased(KeyEvent ke){}
public void keyPressed(KeyEvent ke)
{if(ke.getKeyCode()==KeyEvent.VK_ENTER)
     {if(step==3){evaluate(guess[flicker]);} 
	  else if ((step==4)||(step==16)||(step==17)) {evaluatefloat(guess[flicker]);}}
else if(ke.getKeyCode()==KeyEvent.VK_BACK_SPACE){guess[flicker]="";}
}

public void keyTyped(KeyEvent ke)
{ char ch=ke.getKeyChar();  if((Character.isLetterOrDigit(ch)==true)||(ch=='.')||(ch=='-'))
 { if(flicker>=0){guess[flicker]=guess[flicker]+ch; repaint(); guess[flicker].trim(); }
  evaluated=false; }}

synchronized public void evaluate (String guesss)
 {if(guesss.equals(""+correctvar[flicker]) ){guess[flicker]=guesss;  evalimg=correctimg;
 foundvar[flicker]=1; 
  } 
 else{  evalimg=wrongimg; foundvar[flicker]=-1;  }
  evaluated=true; repaint(); notifyAll();
 }
public void evaluatefloat (String guesss)
 {double convert=new Double(guesss).doubleValue(); 
 //input (float) has to be compared to correctvalue. Locate decimalpoint; read the rest and convert it to int
  //then do the divisions according to decipoint
  /*char[] cA=guesss.toCharArray();int N=0, ic=0; float convert=0;
 int  decipt=20;    
 for(ic=0;ic<guesss.length();ic++)
   {if(cA[ic]=='.'){ decipt=ic;} 
    else if (Character.isDigit(cA[ic])){int n=Integer.parseInt(""+cA[ic]);N=N*10+n; 
	if(ic==(decipt+2)) break;}
   } 
  if (decipt==20) {convert=N;  }
  else{ convert=(float)N; int dec=Math.min(2,(guesss.length()-decipt-1 ));
  for (int pt=0;pt<dec;pt++){convert=convert/10;}}*/ 
 if(corrvar[flicker]>=0){if((convert>0.985*corrvar[flicker])&&(convert<1.015*corrvar[flicker]) )  
  {evalimg=correctimg; foundvar[flicker]=1;  } 
 else  {
 evalimg=wrongimg; foundvar[flicker]=-1; }}
 else if(corrvar[flicker]<0){if((convert<0.985*corrvar[flicker])&&(convert>1.015*corrvar[flicker]) )  
  {evalimg=correctimg; foundvar[flicker]=1;  } 
 else  {
 evalimg=wrongimg; foundvar[flicker]=-1; }}
   evaluated=true;  repaint(); notifyAll();
 } 
public void mouseReleased(MouseEvent me){}
public void mousePressed(MouseEvent me){}

synchronized public void mouseClicked(MouseEvent me)
 { int coX=me.getX(); int coY=me.getY(); 
  switch(step) 
   {case 1: if((coY>height-50)&&(coX>390)&&(coX<510))
                    { step++;pause1=true; pause2=false;notify();} break;
	
     case 2:  if((coX>240)&&(coX<360))
	   {  if(coY>height-50)
           { if (pause1==false)     { pause1=true; notify(); }   ////pressed STOP
             else if (pause1==true) { pause1=false; notify(); }           ////pressed RESUME
     	   }
		   else if(coY>height-110)
              {if(pauselength==40) {pauselength=200;}         //////////presssed  SLOW
               else if(pauselength==200) {pauselength=40;}   //////////pressed NORMAL
              }}
	     else if ((coX>380)&&(coX<500))
		   {if(coY>height-50)   {step++;  pause2=false; notify();} ////pressed NEXT //////////////////Changed 3 in 4
			else if(coY>height-110) {pause2=false;notify();}     ////pressed SAME EXPERIM
           }
      else if ((coX>width-200)&&(coY<140))
        {if (coX>width-110){ if (coY<70) {graphasked=1;} 
                                 else {graphasked=3;}}
      else if (coY<70) {graphasked=0;} 
      else {graphasked=2;} notify();}
      break;
     case 3 :  if((coY>height-50)&&(coX>190)&&(coX<310))
		    if(ta==0){ pause1=false; notify();}  //ta++;
	        else if (flicker>=2){ pause1=false; notify(); }  //to avoid skipping answering and clicking next
			// else if ((coX>380)&&(coX<500))   {  pause2=false; step=4; notify();}
		  
		break;
	case 4 :  case 7 : case 12 : case 13 : case 14 : case 15 : 
		if((coY>height-50)&&(coX>390)&&(coX<510)) {step++;pause2=false; notify();}break;
	case 5 : case 6 :
	     if(coY>height-50)
			{if((coX>240)&&(coX<360))
              { if (pause1==false)     { pause1=true; notify(); }   ////pressed STOP
                else if (pause1==true) { pause1=false; notify(); }           ////pressed RESUME
     	      }
		     else if ((coX>380)&&(coX<500))
		     { step++;  pause2=false; notify();} ////pressed NEXT ///////////
		   }
		   else if(coY>height-110)
		   { if((coX>380)&&(coX<500)) {pause2=false;notify();}     ////pressed SAME EXPERIM
             else if ((coX>240)&&(coX<360))
              {if(pauselength==40) {pauselength=200;}         //////////presssed  SLOW
               else if(pauselength==200) {pauselength=40;}   //////////pressed NORMAL
              }}
      else if((coY<60)&&(step==6))
       {if((coX>width-400+120)&&(coX<width-400+220)){ graphasked=3;} 
         else if((coX>width-400+120)&&(coX<width-400+320)) {graphasked=2;} 
         repaint(); } 
      break;
	case 9 : case 10 : case 11:if(coY>height-50)
		{ if((coX>390)&&(coX<510)) {step++; pause2=false; notify();} ///next panel
		  else if (coX>520) { pause2=false; notify();}}
 break;  //same anime
     case 16: case 17 : if((coY>height-50)&&(flicker>=2)){ pause1=false; notify(); }  //to avoid skipping answering and clicking next
	default: break;
}}
public void mouseEntered(MouseEvent me){}
public void mouseExited(MouseEvent me){}


public void setAxis(Image im, float divy, double uny)
  {  float divx= 1; int unitx=10; if (step==6) {unitx=50; divx=(float)  0.2;}  
     int previousx=0; float previousy=0;
     qo=dheight[jj]-15; if ((step==6)&&(jj==2)) {qo=(int)dheight[2]/2; }///1 unitx=1 sec=10[] or 50 [](step=6)
     Graphics pg= im.getGraphics(); pg.setFont(new Font("Dialog", Font.PLAIN, 16));  // x=tt
      pg.setColor(Color.white); pg.fillRect(0,0,dwidth[jj],dheight[jj]);
	  pg.setColor(Color.orange);
       for(int i=0; i<dwidth[jj]; i+=3)   {pg.drawLine(i,0,i,dheight[jj]);}
      for(int j=0; j<dheight[jj]; j+=3)   {pg.drawLine(0,j, dwidth[jj],j);}
     pg.setColor(Color.black); pg.drawLine( po, qo, dwidth[jj]-1, qo); pg.drawLine(po,2, po, dheight[jj]-15);
      for(int p=0, i=0; p<dwidth[jj]-po; p+=(int)(3*(divx)*unitx), i++){pg.drawLine(p+po, qo,p+po, qo+3);
               pg.drawString(""+(float)divx*i, p+po-4,qo+14);}  //1 sec = 10 [] =30 pix 
      pg.drawString(""+variab[4], dwidth[jj]/2 , qo-2); pg.drawString(""+variab[jj], 20, 16); 
      if(uny!=0){  for(int q=qo, i=0; q>0; q-=(float)(divy*3*uny), i++)
       {pg.drawLine(po-3, qo-(int)(3*i*divy*uny), po, qo-(int)(3*i*divy*uny)); //  1 m=3 pix =1[]
	   if(i%2==0){pg.drawString(""+i*divy, 1, qo+4-(int)(3*i*divy*uny) );}
	   if((step==6)&&(jj==2)){pg.drawLine(po-3, qo+(int)(3*i*divy*uny), po, qo+(int)(3*i*divy*uny)); 
	   if(i%2==0){pg.drawString("-"+i*divy, 1, qo+4+(int)(3*i*divy*uny) );} }}}
  }

public void addPoint (Image im, int jj, float nwy)
{Graphics pg= im.getGraphics();pg.setColor(Color.black); float divx= 1; int unitx=10; qo=dheight[jj]-15;
 if(step==2){ pg.drawLine((int)(po+(float)lasttt*unitx*3/20), (int)(qo-lasty[jj]*3*unity[jj]), (int)(po+(float)tt*(unitx)*3/20), (int)(qo-nwy*unity[jj]*3)); }
if(step==6){unitx=50;   divx = (float) 0.2; if ((step==6)&&(jj==2)) {qo=(int)dheight[2]/2;;}
          pg.drawLine((int)(po+(float)lasttt*unitx*3/25), (int)(qo-lasty[jj]*3*unity[jj]), (int)(po+(float)tt*(unitx)*3/25), (int)(qo-nwy*unity[jj]*3)); }
    lasty[jj]=nwy;  repaint();
//showStatus(" lasttt  "+lasttt+" Now  "+tt);
}

public Image textDrawLines( int widthRect, int heightRect, String s)
{ Image im=createImage(widthRect, heightRect); Graphics msg=im.getGraphics();
   msg.setFont(textfont);  fm=msg.getFontMetrics();  
   msg.setColor(lightyellow); msg.fillRect(0,0,widthRect, heightRect);
   msg.setColor(Color.black);
   int wl=0;  wl=fm.stringWidth(s); 
        int linel=0;   lineheight=fm.getHeight();int y=lineheight+2;
       int spacelength = fm.stringWidth(" ");
         StringTokenizer tt=new StringTokenizer(s+" ."); //  fullstop to find end of string
             String word=" ", linee="";   					//contains the text
   while(tt.hasMoreTokens())
	{ word= tt.nextToken();
	   if (word.equals("."))
	      {  msg.drawString (linee, 8,   y); linel=0; linee=""; y=y+lineheight+2;}   
	  else    {
	   wl=fm.stringWidth(word);
	  linel=linel+spacelength+wl;
	   if (linel>(widthRect-8))
	         { msg.drawString (linee, 8,  y);     
                         linel=wl; linee=word; y+=lineheight+2;  }
	    else {linee=linee+" "+word; }//showStatus("lines = "+linee); }
	     }
	} msg.drawString (linee, 8,  y);     //end of while	
 return im;}
 
 public Image textGetLines( String s, int widthRect)  
{  int wl=0; 
 int nblines=0; String[] mylines=new String[20];
        int linel=0;  int lineheight; //=fm.getHeight();
       int spacelength = fm.stringWidth(" ");
         StringTokenizer tt=new StringTokenizer(s+" ."); //  fullstop to find end of string
             String word=" ", linee="";   					//contains the text
   while(tt.hasMoreTokens())
	{ word= tt.nextToken();
	   if (word.equals("."))
	      {  mylines[nblines]= linee; linel=0; linee=""; nblines++;}   
	  else    {int oldlinel=linel;
	   wl=fm.stringWidth(word);
	  linel=linel+spacelength+wl;
	   if (linel>(widthRect-8))
	         { mylines[nblines]=linee;     
                         linel=wl; linee=word; nblines++;  }
	    else {linee=linee+" "+word; }
	     }
	}     //end of while	
 lineheight=30;  
 Image im=createImage(widthRect, (5+nblines)*lineheight); Graphics msg=im.getGraphics();
 msg.setFont(textfont);  fm=msg.getFontMetrics();  
 msg.setColor(lightyellow);msg.fillRect(0,0,widthRect,lineheight*(5+nblines));
 msg.setColor(Color.black);  int jx=2;
 for (int j=0; j<nblines; j++)
  {   jx+=lineheight;///among the [] of lines ,look for $ or % ,super or sub script. Then note string upto ! into sublines[]
       int wline=(mylines[j]).length();   int index=0, startindex=0; int Xstart=0;
         int nbsub=0, nbsup=0;  int Xsuperscript[]= new int[5];String strsuper[]=new String[5];  ///place and comtent of superscripts
		 int Xsubscript[]= new int[5];String strsub[]=new String[5];  ///place and content of subscript
		 int Xsublines[]= new int [10];String[] sublines=new String[10]; ///place and content of non sub or super-script
               int Xfrac[]= new int[5]; String strfrac[]=new String[5]; int nbfrac=0;int strfraclength=0;
	 Xsublines[0]=8;    
  while(index<wline)                                     //check for $ for superscript, % =subscript
{  if( mylines[j].charAt(index)=='$')  
   { sublines[nbsup+nbsub]=mylines[j].substring(startindex, index);
     Xsuperscript[nbsup]=Xsublines[nbsup+nbsub]+spacelength+fm.stringWidth(sublines[nbsup+nbsub]);
     String leftstring=mylines[j].substring(index+1, wline);
     strsuper[nbsup]= leftstring.substring(0,leftstring.indexOf('!'));
	int strsuperlength= fm.stringWidth(strsuper[nbsup]);
	Xsublines[nbsup+nbsub+1]=Xsuperscript[nbsup]+fm.stringWidth(strsuper[nbsup])+spacelength;
	index=index+1+leftstring.indexOf('!'); 
		 
     if((mylines[j].charAt(index+1)=='/')||(mylines[j].charAt(index+2)=='/'))
       	{  Xsuperscript[nbsup]+=spacelength;	 Xfrac[nbfrac]=Xsuperscript[nbsup];
		   leftstring=mylines[j].substring(index+1, wline);
            strfrac[nbfrac]= leftstring.substring(1,leftstring.indexOf('!')); 
			strfraclength= fm.stringWidth(strfrac[nbfrac]); nbfrac++;
			index=index+1+leftstring.indexOf('!'); 
			//if(strfraclength>strsuperlength) {Xsublines[nbsup+nbsub+1]+=(strfraclength-strsuperlength);}
			 strfraclength=Math.max(strfraclength, strsuperlength); 
		}			 
	   startindex=index+1; nbsup++;
                  

   //   else if(chh.equals("%")==true) 
                    // { sublines[nbsup+nbsub]=mylines[j].substring(start, index);
                  //    Xsubscript[nbsub]=1+fm.stringWidth(sublines[nbsup+nbsub]);String leftstring=mylines[j].substring(index, wline);
                     // strsub[nbsub]= leftstring.substring(0,leftstring.indexOf('!'));index+=leftstring.indexOf('!');start=index; nbsub++;}
                   
	sublines[nbsup+nbsub]=mylines[j].substring(startindex,wline);
     }
  else{index++;} }//end of while
  
 if((nbsup>0)||(nbsub>0))
  { for( int i=0; i<=(nbsub+nbsup);i++){msg.drawString(sublines[i],Xsublines[i],jx) ;}
	if(nbsup>0){ for( int i=0; i<(nbsup);i++){msg.drawString(strsuper[i],Xsuperscript[i], jx-4);}}
	if(nbfrac>0){for( int i=0; i< nbfrac;i++){ msg.drawLine(Xfrac[i],jx-1,Xfrac[i]+strfraclength   ,jx-1);
	    msg.drawString(strfrac[i],Xfrac[i], jx+fm.getHeight()-1);}
		jx=jx+fm.getHeight();}
//if(nbsub>0){ for( int i=0; i<(nbsub);i++){msg.drawString(strsub[i],Xsubscript[i], jx(j+1)*lineheight+3);}}
     }  
	else {	msg.drawString(mylines[j],4,jx) ;} 
 
 } //next line
CropImageFilter f=new CropImageFilter(0, 0,widthRect, jx+5);
 FilteredImageSource fis =new FilteredImageSource(im.getSource(), f);

 Image imm =createImage(fis);
 
  return imm; }   
 //return mylines; }
 
  
public void update(Graphics g)////////////////////////////////////////////////
{ offS.setColor(green); offS.fillRect(0,0,width,height);
   switch (step)
   {			   /////////////free falling ball animation
  case 2: bigg.setColor(lightblue); bigg.fillRect(150,0,20,1200); 
          bigg.setColor(grass); bigg.fillRect(150,1200, 20,100);
bigg.setColor(Color.red); bigg.fillOval(152,y,8,8); 
CropImageFilter f;
FilteredImageSource fis;
f=new CropImageFilter(0,depart, 200, height);
   fis=new FilteredImageSource(bigScene.getSource(), f);
 scene_now=createImage(fis);
 
 offS.drawImage(msgimg,50,2,this);
offS.setColor(Color.black);   
  offS.drawString("Time : (sec)", 50,120); offS.drawString(""+(float)tt/20, 300,120);
offS.drawString("Acceleration : (m/sec²) ", 50,160); offS.drawString(""+a, 300,160);
offS.drawString("Speed: (m/sec)", 50,200);int vv=(int)(v*100); offS.drawString(""+(float)vv/100, 300,200);   //to avoid too many decimals
offS.drawString("Displacement: (m) ", 50,240); offS.drawString(""+((y-yo)/10), 300,240);
offS.setColor(bluegreen); offS.fillRect(40, height-50,120,40);//offS.fillRect(180, height-50,120,40);
offS.fillRect(40, height-110,120,40); 
offS.fillRect(width-400, 20, 80, 50); offS.fillRect(width-310, 20 ,80,50);
//offS.fillRect(width-400, 90, 80,50);offS.fillRect(width-310, 90,80,50);
offS.setColor(Color.yellow);  
offS.drawString("v/t", width-390,60); offS.drawString("s/t", width-290,60);
 if (pause1==true){ if(tt==0) {offS.drawString("START ", 50,height-20);}
                    else {offS.drawString("RESUME ",  50,height-20);}}
 else if(pause1==false) {offS.drawString("STOP ", 50,height-20);}
 if(exerciseover==true){offS.setColor(bluegreen);offS.fillRect(180, height-50,120,40);offS.fillRect(180, height-110,200,40);
    offS.setColor(Color.yellow); offS.drawString("NEXT  ",190, height-20);
    offS.drawString("Same animation ", 180,height-80); }

if(pauselength==40){offS.drawString("SLOW ", 50,height-80);}
else if(pauselength==200){offS.drawString("NORMAL ", 50,height-80);}

if(graphasked>-1)  {offS.drawImage(graphim[graphasked], width-dwidth[graphasked]-200, height-420, this);}
break;

case 3 :if(on==true){ offS.setColor(lightyellow); offS.fillRect(240, 380+flicker*40, 50,30);}///flickering
 offS.drawImage(msgimg, 10, 10,this);
 offS.setColor(Color.black);
  offS.drawString("CONSTANTS", 10,150);
offS.drawString("Acceleration : (m/sec²)", 10,180); offS.drawString(""+a, 250,180); 
offS.drawString("Initial height :(m) ", 10,210); offS.drawString(""+ho, 250,210); 
 offS.drawString("Mass : (kg)", 10,240); offS.drawString("1", 250,240); 
offS.drawString("VARIABLES", 10,320);
  for (int ii=0; ii<=flicker;ii++)
{if((foundvar[ii]<0)&&(ta>0)){ offS.drawString("no,it is "+correctvar[ii], 400,400+ii*40);}}
  for (int ii=0; ii<2;ii++)
   {offS.drawString(variab[ii], 10,400+ii*40); offS.drawString(guess[ii], 250,400+ii*40);}
 offS.drawString(variab[4], 10,360); offS.drawString(""+ta, 250,360);
if(evalimg!=null){ offS.drawImage(evalimg, 350, 370+(flicker-1)*40, 50,40,this); }
offS.setColor(lightyellow); offS.fillRect(width-dwidth[0],100,300,80);   /////formula  to use 
if(flicker<2){offS.setColor(Color.black); offS.drawString(formularray[flicker], width-dwidth[0]+10,140);
offS.drawImage(graphim[flicker], width-dwidth[flicker], height-dheight[flicker]-10,this);}
if((flicker==2)||(ta==0)){offS.setColor(bluegreen);offS.fillRect(190, height-50, 140 ,40);  //button next
offS.setColor(Color.yellow); offS.drawString("next exercise",200,height-20); 
offS.drawImage(graphim[1], width-dwidth[1], height-dheight[1]-10,this);
}
break;  
  ////////////////////t of impact//////////////
case 4:if(on==true){ offS.setColor(lightyellow); offS.fillRect(240, 380+flicker*40, 50,30);}///flickering
 offS.drawImage(msgimg, 10, 10,this); offS.drawImage(msgimg2, 10, 100,this);
 offS.setColor(lightyellow); offS.fillRect(510,100,500,60);   /////formula  to use 
 offS.setColor(Color.black); offS.drawString(formularray[1]+" =>  t= ", 530,125);
int l=150; int polyX[]={520+l,525+l, 530+l,590+l}; int polyY[]={102,145,102,102};
offS.drawString(formularray[4], 530+l,125);
 offS.setColor(Color.black); offS.drawPolyline(polyX, polyY,4);
  offS.drawString("CONSTANTS", 10,200);
offS.drawString("Acceleration : (m/sec²)", 10,230); offS.drawString(""+a, 250,230); 
offS.drawString("Initial height :(m) ", 10,260); offS.drawString(""+ho, 250,260); 
offS.drawString("VARIABLES", 10,320); 
offS.drawString("T", 10,400); offS.drawString(guess[0], 250,400);
  fm=offS.getFontMetrics();  int wl=fm.stringWidth("T"); offS.drawString("impact", 10+wl,402); 
offS.drawString("V", 10,440); offS.drawString(guess[1], 250,440);
 wl=fm.stringWidth("V"); offS.drawString("impact", 10+wl,442); 
 int pollyX[]={220,225, 230,290}; int pollyY[]={362,405,362,362};
offS.setColor(Color.black); offS.drawPolyline(pollyX, pollyY,4);
 
 for (int ii=0; ii<=flicker;ii++)
    {if(foundvar[ii]<0){int uu=(int)Math.round(corrvar[ii]*100); offS.drawString("no,it is "+(float)uu/100, 400,400+ii*40);}
	   else if (foundvar[0]>0) {int uu=(int)(Math.sqrt(corrvar[0])*1000); offS.drawString(""+(float)uu/1000, 400,400);}  }
	
if(evalimg!=null){ offS.drawImage(evalimg, 350, 370+(flicker-1)*40, 50,40,this); }
offS.drawImage(graphim[0], width-dwidth[0], height-dheight[0]-10,this);
if(flicker==2){offS.setColor(bluegreen);offS.fillRect(390, height-50, 120 ,40);     //button next
offS.setColor(Color.yellow); offS.drawString("NEXT",400,height-20); }	
break;
case 5: offS.drawImage(msgimg, 300, 10,this); 
  y2=height-250; x2=80; offS.drawImage(scene_now, x2, y2,this); 
 offS.drawImage(ballimg, x2+26, y2+y,this); 
   offS.setColor(bluegreen);offS.fillRect(240, height-50, 120 ,40);     //button next
  offS.setColor(Color.yellow); 
 if (pause1==true){ if(tt==0) {offS.drawString("START ", 250,height-20);}
                    else {offS.drawString("RESUME ",  250,height-20);}}
 else if(pause1==false) {offS.drawString("STOP ", 250,height-20);}
 if(exerciseover==true){offS.setColor(bluegreen);offS.fillRect(380, height-50,120,40);
    offS.setColor(Color.yellow); offS.drawString("NEXT  ",390, height-20);}
 break;
 
case 6: //offS.drawImage(msgimg2, 450, 160,this);
  y2=height-350; x2=80; offS.drawImage(scene_now, x2, y2,this); 
 offS.drawImage(ballimg, x2+26,y2+y,this); 
if(graphasked==3) {offS.drawImage(graphim[3] ,x2+120, y2-dheight[3]+30+(int)(1.152*150), this);}
else if (graphasked==2) {offS.drawImage(graphim[2], x2+120, 50, this);}
offS.setColor(Color.black);   
  offS.drawString("Time : (sec)", 650,120); offS.drawString(""+(float)(tt)/25, 900,120);
offS.drawString("Acceleration : (m/sec²) ", 650,160); offS.drawString("-"+a, 900,160);
offS.drawString("Initial Speed: (m/sec)", 650,200); offS.drawString("6.4", 900,200);  
offS.drawString("Speed: (m/sec)", 650,240);int vu=(int)(v*100); offS.drawString(""+(float)vu/100, 900,240);   //to avoid too many decimals
offS.drawString("Displacement: (m) ", 650,280); int du=(int) Math.round(d*100); offS.drawString(""+(float)du/100, 900,280);
offS.drawString("GRAPHS",width-400, 50);
offS.setColor(bluegreen); offS.fillRect(width-400+120, 10, 80, 50); offS.fillRect(width-400+220, 10 ,80,50);
offS.fillRect(240, height-50,120,40); offS.fillRect(240, height-110,120,40);
offS.setColor(Color.yellow); offS.drawString("s/t", width-400+130,50); offS.drawString("v/t", width-400+230,50);
if(pauselength==40){offS.drawString("SLOW ", 250,height-80);}else if(pauselength==200){offS.drawString("NORMAL ", 250,height-80);}
if (pause1==true){ if(tt==0) {offS.drawString("START ", 250,height-20);}
                    else {offS.drawString("RESUME ",  250,height-20);}}
 else if(pause1==false) {offS.drawString("STOP ", 250,height-20);}
 
 if(exerciseover==true){offS.setColor(bluegreen);offS.fillRect(380, height-50, 120 ,40); offS.fillRect(380, height-110, 200 ,40);    //button next
offS.setColor(Color.yellow); offS.drawString("NEXT",390,height-20); offS.drawString("Same animation ", 390,height-80); } 	

break;

case 9 :  offS.drawImage(msgimg, 200, 10,this); offS.drawImage(msgimg2, 450, 160,this);
 int handx=88, handy=17; x2=200; y2=300;offS.drawImage(boyimg, x2, y2,this); offS.setColor(Color.red); 
if(evaluated==true){offS.fillOval(x2+handx-6+(int)x, y2+handy-6+(int)y, 12,12); offS.setColor(Color.black); 
if(tt<154){offS.drawLine(x2+handx, y2+handy, x2+handx+(int)x, y2+handy+(int)y);}
else if (tt<161){offS.drawArc(x2+handx-40, y2+handy, 80, 147,0,90);}  //-40 because pix for full circle =80
else {offS.drawArc(x2+handx-20, y2+handy, 40, 175,0,90);}}
if(exerciseover==true){offS.setColor(bluegreen);offS.fillRect(390, height-50, 120 ,40); offS.fillRect(520, height-50, 200 ,40);    //button next
offS.setColor(Color.yellow); offS.drawString("NEXT",400,height-20); offS.drawString("Same animation ", 530,height-20); } 	
break;
case 10: offS.setColor(darkblue); offS.fillRect(0,0,width,height);          ////////////earth + moon
offS.drawImage(msgimg, 200, 10,this); offS.drawImage(msgimg2, 500, 100,this);
x2=250; y2=250;offS.drawImage(earthimg, x2-50,y2-50,100,100,this); offS.drawImage(moonimg, (x2+x-25), (y2+y-25), 50,50, this);
  if(exerciseover==true){offS.setColor(bluegreen);offS.fillRect(390, height-50, 120 ,40); offS.fillRect(520, height-50, 200 ,40);    //button next
offS.setColor(Color.yellow); offS.drawString("NEXT",400,height-20); offS.drawString("Same animation ", 530,height-20); } 	
break;
case 11:  offS.setColor(darkblue); offS.fillRect(0,0,width,height);        /////////Newton, apple,moon
 offS.drawImage(msgimg2, 500, 280,this);offS.drawImage(moonimg,Xmoon-110, 600+Ymoon,82,81, this);  
 offS.drawImage(branchimg,0,0,this);offS.drawImage(newtonimg, 0, height-228, this); 
for (int i=0; i<3;i++){ offS.drawImage(appleimg,Xapple[i], Yapple[i], this); }
   if(exerciseover==true){offS.setColor(bluegreen);offS.fillRect(390, height-50, 120 ,40); offS.fillRect(520, height-50, 200 ,40);    //button next
offS.setColor(Color.yellow); offS.drawString("NEXT",400,height-20); offS.drawString("Same animation ", 530,height-20); } break;	
case 12: offS.setColor(darkblue); offS.fillRect(0,0,width,height);
  offS.drawImage(msgimg, 550, 10,this); offS.drawImage(msgimg2, 300,360, this);
  offS.drawImage(solarsystemimg, 100, 10,this); 
  offS.drawImage(galaxiesimg, 550, 350,this); 
  offS.setColor(bluegreen);offS.fillRect(390, height-50, 120 ,40);     //button next
  offS.setColor(Color.yellow); offS.drawString("NEXT",400,height-20);
     break;
case 13 : case 14 : case 15://offS.drawImage(msgimg, 100, 100,this);
 offS.drawImage(msgimg2, 100, 60,this);
 offS.setColor(bluegreen);offS.fillRect(390, height-50, 120 ,40);     //button next
offS.setColor(Color.yellow); offS.drawString("NEXT",400,height-20);
break;
case 16: offS.drawImage(msgimg, 20, 20,this); 
  if(on==true){ offS.setColor(lightyellow); offS.fillRect(240, 375+(flicker-2)*40, 50,30);}///flickering
offS.setColor(lightyellow); offS.fillRect(510,25,400,45);   /////formula  to use 
 offS.setColor(Color.black); 
if (flicker==2){offS.drawString(formularray[0]+" =>  t= -u/a ", 530,55);}
else if (flicker>=3){offS.drawString(formularray[2], 530,55);}
  offS.drawString("CONSTANTS", 10,200);
offS.drawString("Acceleration : (m/sec²)", 10,230); offS.drawString("-"+a, 250,230); 
offS.drawString("Initial height :(m ) ", 10,260); offS.drawString(""+ 1.15 , 250,260); 
offS.drawString("Initial speed :(m/sec ) ", 10,290); offS.drawString(""+ 6.4 , 250,290); 
offS.drawString("Now you calculate :", 10,360); 
offS.drawString("Time", 10,400); offS.drawString(guess[2], 250,400);
  fm=offS.getFontMetrics();  wl=fm.stringWidth("Time"); offS.drawString("highest point", 10+wl,403); 
wl=fm.stringWidth("Time highest point"); offS.drawString(" (sec)", 10+wl,400); 
offS.drawString("highest point (m)", 10,440); offS.drawString(guess[3], 250,440);
for (int ii=2; ii<=flicker;ii++)
    {if(foundvar[ii]<0){int uu=Math.round(corrvar[ii]*100); offS.drawString("no,it is "+(float)uu/100, 400,400+(ii-2)*40);}}
	  
if(evalimg!=null){ offS.drawImage(evalimg, 350, 370+(flicker-3)*40, 50,40,this); }
offS.drawImage(graphim[3], width-dwidth[3]-10, height-dheight[3]-10,this);
if(flicker==4){offS.setColor(bluegreen);offS.fillRect(390, height-50, 120 ,40);     //button next
offS.setColor(Color.yellow); offS.drawString("NEXT",400,height-20); }	
break;
case 17: offS.drawImage(msgimg, 20, 20,this); 
  if(on==true){ offS.setColor(lightyellow); offS.fillRect(240, 375+(flicker-2)*40, 50,30);}///flickering
offS.setColor(lightyellow); offS.fillRect(510,25,500,45);   /////formula  to use 
 offS.setColor(Color.black); 
if (flicker==2){offS.drawString(formularray[2], 530,55);}
else if (flicker>=3){offS.drawString(formularray[0], 530,55);}
  offS.drawString("CONSTANTS", 10,200);
offS.drawString("Acceleration : (m/sec²)", 10,230); offS.drawString("-"+a, 250,230); 
offS.drawString("Initial height :(m ) ", 10,260); offS.drawString(""+ 1.15 , 250,260); 
offS.drawString("Initial speed :(m/sec ) ", 10,290); offS.drawString(""+ 6.4 , 250,290); 
offS.drawString("Now you calculate :", 10,360); 
offS.drawString("Time", 10,400); offS.drawString(guess[2], 250,400);
  fm=offS.getFontMetrics();  wl=fm.stringWidth("Time"); offS.drawString("initial height", 10+wl,403); 
wl=fm.stringWidth("Time initial height"); offS.drawString(" (sec)", 10+wl,400); 
offS.drawString("speed  (m /sec)", 10,440); offS.drawString(guess[3], 250,440);
for (int ii=2; ii<=flicker;ii++)
    {if(foundvar[ii]<0){int uu=Math.round(corrvar[ii]*100); offS.drawString("no,it is "+(float)uu/100, 400,400+(ii-2)*40);}}
	  
if(evalimg!=null){ offS.drawImage(evalimg, 350, 370+(flicker-3)*40, 50,40,this); }
if(flicker==2){offS.drawImage(graphim[3], width-dwidth[3]-10, height-dheight[3]-10,this);}
if(flicker>=3){offS.drawImage(graphim[2], width-dwidth[2]-10, height-dheight[2]-10,this);}
if(flicker>=3){offS.setColor(bluegreen);offS.fillRect(390, height-50, 120 ,40);     //button next
offS.setColor(Color.yellow); offS.drawString("NEXT",400,height-20); }	
break;
case 18:offS.setColor(green); offS.fillRect(0,0,width,height);
offS.drawImage(msgimg,100,100,this); break;
default : break;
 }
paint(g);
}
public void paint(Graphics g)
 {if(isrunning==true) 
  {if(step==2) { g.drawImage(offScreenImage,200,0,this); 
            g.drawImage(scene_now,0,0,this);}
    else{ g.drawImage(offScreenImage,0,0,this); // g.setColor(Color.black); g.setFont(big);g.drawString("End of the exercise",200,200);
 }}
else { g.setColor(green); g.fillRect(0,0,width,height);
g.setColor(Color.black); g.setFont(big);
g.drawString("END OF THE PROGRAM", 200,300); }
}}