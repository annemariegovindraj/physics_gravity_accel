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
*/
public class Potential3 extends Applet implements MouseListener , KeyListener , Runnable
{////calculations for step2 and /// 
boolean isrunning=true, downwards=true, pause1=true , pause2=false, on=false, evaluated=false,exerciseover=false, superimpose=false, compressing = false; 
 int height, width, dwidth=410,   pauselength=30, xo=50, yo=50, x=xo,y=yo, x1=50,x2=390,  y1=50, y2=400;
 int  depart=0,  a=10, tt=0, lasttt=0, jj=0 , po=18, qo=385,graphasked=-1, exp=0, step=1, springwidth=280;final int ho=115;
float v=0, d=0, dmax=0, m=(float)(0.1), Ek=0, Ep=1150, ampl=(float)(0.4); //maximum height 115 m but origin is at d =0, yo=48;
float lasty[]; float dx=0, dy=0; int dheight[]={400,400,400,400,400,75,400,200,200,300,300,400};
 float divy[]={5,10,100,100,10,(float)(0.1),(float)(0.1),(float)(0.05),(float)(0.05),  1,1,(float)(0.1)}; 
 float unity[]={2,1,(float)(0.1),(float)(0.1),1,160,160,400,400,20,20,33}; float e[]={1,(float)(0.8),(float)(0.6)};

 Thread roller;
 ////calculations for step2 
 int t=0, flicker=0; String []guess=new String[5]; int correctvar[]; float corrvar[]; 
 String[] formularray= {"v=u+a*t","s=a*t²/2","Ek=m*v²/2","Ep=m*g*(H-s)","2*H/a"};
 int foundvar[]={0,0,0,0,0}; boolean flickering=false;
/////////////////display///////////////
 String[] variab={"speed(m/sec)", "displacement(m)", "Kinetic energy(J)", "Potential Energy (J)", "time(sec)","height (m)","", "Kinetic energy(J)", "Potential Energy (J)","Kinetic energy(J)", "Potential Energy (J)","displacement(m)"};
 Color lightblue= new Color(0,255,255); Color grass=new Color(0,136,40);Color green= new Color(78,214,173);Color bluegreen= new Color(47, 166, 187); Color lightyellow= new Color(255,245,191);
Image offScreenImage,  msgimg, msgimg2, bigScene, scene_now, springimg, arrowimg;
Image [] bgitems=new Image[10]; Image graphim[]= new Image[12];
Graphics offS, msg2, bigg; 
Font big = new Font("Dialog", Font.PLAIN, 20); Font textfont= new Font("Verdana", Font.PLAIN, 18); FontMetrics fm; 
int lineheight=0; //heightRect=260; 
Image correctimg, wrongimg, evalimg=null;

public void init()
{ addKeyListener(this); requestFocus();
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
{for( jj=0; jj<5;jj++) {graphim[jj]= createImage(dwidth,dheight[jj]); 
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
 {  if (step==1){msgimg=textDrawLines(550,320," Potential Energy is the energy that a body has, because of its position in a field of force (electrical field, gravitational field, magnetic field) or by its configuration (e.g. elastic energy).  .  Let us look at the speed (v), the displacement (s), the Kinetic energy (Ek) and the Potential energy (Ep) when a ball is dropped from a high building. Click the buttons to see the graphs. "); 
  exerciseover=true; pause2=true;   repaint();
  ///preparing step 2//////////
int tracked=0; String name[]= new String[10]; 
try{MediaTracker tracker = new MediaTracker(this);
      StringTokenizer mst= new StringTokenizer(getParameter("img"),"$");
      while(mst.hasMoreTokens()&&tracked<10)      
	{name[tracked]=mst.nextToken();
	bgitems[tracked]=getImage(getDocumentBase(), name[tracked]+".gif");
  	tracker.addImage(bgitems[tracked], tracked);
	tracked++;}
      tracker.waitForAll();
    }catch(InterruptedException e){showStatus("No picture found");}
correctimg=getAlpha(bgitems[2],104,77); wrongimg=getAlpha(bgitems[3],77,60);
springimg=getAlpha(bgitems[4], 280,122); arrowimg=getAlpha(bgitems[5],48,46);
	
for (int i=0;i<37; i++) {bigg.drawImage(bgitems[0],0,(i+2)*30,this);} bigg.drawImage(bgitems[1], 0, 30*39,this);

  try{  synchronized(this){while(pause2==true) wait();} }  catch(InterruptedException e){};  
  step=2;superimpose=false; }

  if(step==2){/////falling ball animation + computer-calculations
 msgimg=textDrawLines(500,40, "FREE FALL, PERFECTLY ELASTIC COLLISION");
 exerciseover=false; prepareNextRun(); repaint(); 
 pause1=true; try{  synchronized(this){while(pause1==true) wait();} }  catch(InterruptedException e){};
  int Tup=0, Tdn=0; tt=0; float Vdn=0;  v=0; d=0; dmax=0; Ek=0; Ep=1150;lasty[3]=1150;  downwards=true; 
  while ( tt<240)
  {if(downwards==true){v=(float) a*(tt-Tup)/20; d=dmax+(float) a*(tt-Tup)*(tt-Tup)/800;
                        y= (int)(yo +d*10); Ek=v*v/2; Ep=a*(ho-d);
                       if(y>=1190) {Tdn=tt; if(exp==0){Vdn=v;} else if (exp==1) {Vdn=(float)(v*0.8);} else {break;} 
					    downwards=false;}}
     else{v= Vdn-(float)a*(tt-Tdn)/20; d =ho- Vdn*(tt-Tdn)/20+(float) a*(tt-Tdn)*(tt-Tdn)/800; 
    Ek=v*v/2; Ep=a*(ho-d);y=yo+(int)(d*10); if (v<=0) {downwards=true; Tup=tt;dmax=d;} }
   depart=y-300; if(y<300) {depart=0;} else if (y> (1600-height)) {depart=1300-height;}
   if (superimpose==true){addGreenPoint(graphim[2],3,Ep);}
float nwy=0; for( jj=0; jj<4; jj++){switch(jj){case 0: nwy=v; break;case 1: nwy=d; break; case 2: nwy=Ek; break;
case 3: nwy=Ep; break;default: nwy=1;}   addPoint(graphim[jj], jj, nwy); }    repaint();  
try{synchronized(this){while(pause1==true) wait();}lasttt=tt; tt++;roller.sleep(pauselength);   } catch(InterruptedException ie){};
 } pause2=true; exerciseover=true; superimpose=false; repaint(); 
 try{synchronized(this){while(pause2==true) wait();  }} catch(InterruptedException ie){}; }

 if(step==3){ //////////////////calculations by student/////////////////////////////
 t=0; 
 if(t==0){msgimg=textDrawLines(500,80, "Here are the initial conditions. Click next to continue.");
   guess[0]="0"; guess[1]="0";guess[2]="0"; guess[3]=""+1150; guess[4]=""; 
   flicker=3; repaint();    pause1=true;    //flicker =3 to show graph of potential Energy
   try{ synchronized(this){while(pause1==true) wait();}} catch(InterruptedException ie){};
   t=1; } 
   while( t<5)
  { correctvar=new int[5];  
   msgimg=textDrawLines(400,80, "Type in the value for time="+t+" and press ENTER ; Compare with the graph.");
   pause1=true; for (int ii=0;ii<5;ii++){guess[ii]="";foundvar[ii]=0;} evalimg=null;
   flicker=0; flickering=true; repaint();
   correctvar[0]=a*t; correctvar[1]=a*t*t/2; 
	  correctvar[2]=(correctvar[0])*(correctvar[0])/2; correctvar[3]=(ho-correctvar[1])*a;correctvar[4]=0;
 	int counter=0; 
	while(flickering==true)
   {counter++;  if (counter%12==0){ on=!on;} 
  
   if (evaluated==true){ flicker++; showStatus("flicker " +flicker+" , t = "+t); if (flicker==4) {flickering=false;};evaluated=false;}
   repaint();  try{roller.sleep(50);}catch(InterruptedException e){};}
 on=false; repaint(); //flickering=false;
    pause1=true; try{ synchronized(this){while(pause1==true) wait();}} catch(InterruptedException ie){};
     t++; 
   }step=4; }
 if(step==4){msgimg=textDrawLines(500, 360, "In the previous exercise, sum of Kinetic Energy (Ek) and Potential Energy (Ep) stays constant. In real life, collisions are not perfectly elastic, and energy is lost in each collision. The ratio of the relative speed of separation after collision to the relative speed of approach before collision is a constant, called coefficient of elasticity : e  .  The next 3 animations show : .  1. perfectly elastic collision: e=1 .  2. non-elastic collision: e=0.8 .  3. non-elastic collision: e=0.5.");
    repaint();
    pause2=true;try{ synchronized(this){while(pause2==true) wait();}} catch(InterruptedException ie){};
   exp=0;step=5;}
 
 while(step==5) /////falling ball animation + non-elastic
 { if(exp==0){msgimg=textDrawLines(500,40, "FREE FALL, PERFECTLY ELASTIC COLLISION  e= "+e[exp]);}
  else {msgimg=textDrawLines(500,40, "FREE FALL, NON-ELASTIC COLLISION : e= "+e[exp]);}
 exerciseover=false; prepareNextRun(); repaint(); 
 pause1=true; try{  synchronized(this){while(pause1==true) wait();} }  catch(InterruptedException e){};
  int Tup=0, Tdn=0; tt=0; float Vdn=0;  d=0; dmax=0;  Ep=1150; downwards=true; superimpose=false;
  while ( tt<400)
  {if(downwards==true){v=(float) a*(tt-Tup)/20; d=dmax+(float) a*(tt-Tup)*(tt-Tup)/800;
                        y= (int)(yo +d*10); Ek=v*v/2; Ep=a*(ho-d);
                       if(y>=1190) {Tdn=tt; if(exp==0){Vdn=v;} else  {Vdn=(float)(v*e[exp]);}  
					    downwards=false;}}
     else{v= Vdn-(float)a*(tt-Tdn)/20; d =ho- Vdn*(tt-Tdn)/20+(float) a*(tt-Tdn)*(tt-Tdn)/800; 
    Ek=v*v/2; Ep=a*(ho-d);y=yo+(int)(d*10); if (v<=0) {downwards=true; Tup=tt;dmax=d;} }
   depart=y-300; if(y<300) {depart=0;} else if (y> (1600-height)) {depart=1300-height;}
float nwy=0; for( jj=0; jj<4; jj++){switch(jj){case 0: nwy=v; break;case 1: nwy=d; break; case 2: nwy=Ek; break;
case 3: nwy=Ep; break;default: nwy=1;}   addPoint(graphim[jj], jj, nwy); } lasttt=tt;
   repaint();  
try{synchronized(this){while(pause1==true) wait();}lasttt=tt; tt++;roller.sleep(pauselength);   } catch(InterruptedException ie){};
 } pause2=true; exerciseover=true; repaint(); 
 try{synchronized(this){while(pause2==true) wait();  }} catch(InterruptedException ie){}; 
 //showStatus("exp "+exp);
 if(exp>=3){step=6;} prepareNextRun(); }
 
if(step==6){msgimg=textDrawLines(500,300,"In the PENDULUM and the SWING, the POTENTIAL energy is converted into KINETIC energy (speed). This speed is used to gain height upto a maximum of potential energy ( speed falls to 0); then again this potential energy allows the bob to gain speed , and so on .  In this example, the pendulum's string is 1 m long, the bob's weight is 0.5 kg and the amplitude (maximal horizontal displacement) is 0.2 m.");
 exerciseover=false; float length=1; float g= (float)(9.81);  float T=(float)(2*(Math.sqrt(length/g))*Math.PI);
 float w= (float)(Math.sqrt(g/length)); int p2=150, q2=25;ampl=(float)0.2;
 dx=ampl; dy=(float)(Math.sqrt(length*length-ampl*ampl)); pause1=false;
 for(tt=0;tt<240;tt++){dx=(float)(ampl*(Math.sin((w*(float)tt/20)+Math.PI/2))); x=x1+p2+(int)(480*dx);
 dy=(float)(Math.sqrt(length*length-dx*dx)); y=y1+q2+(int)(480*dy);   
 repaint(); try{roller.sleep(pauselength); } catch(InterruptedException ie){};
}  pause2=true; exerciseover=true;superimpose=false; 
repaint();try{synchronized(this){while(pause2==true) wait();  }} catch(InterruptedException ie){}; 
 step=7;}

if(step==7){msgimg=textDrawLines(300,30,"SIMPLE PENDULUM"); /////////////animation + graphics of pendulum
 msgimg2=createImage(150, 300); Graphics msg2=msgimg2.getGraphics();
   msg2.setFont(textfont);  msg2.setColor(lightyellow); msg2.fillRect(0,0,300,300);
   msg2.setColor(Color.black); po=25;
  float length=1; float g= (float)(9.81); float Ek=0; float Ep=0;float mass=(float)(0.5);
 tt=0; ampl=(float)0.2; exerciseover=false; pause1=true; 
 float T=(float)(2*(Math.sqrt(length/g))*Math.PI);
 float w= (float)(Math.sqrt(g/length)); 
  for(jj=5; jj<=8; jj++){
 switch(jj)
    { case 5 :  dwidth=400;      ///height =l-y
	 graphim[jj]= createImage(dwidth,dheight[jj]); po=25; 
	 setAxis(graphim[jj], divy[jj],unity[jj]);  break;
	case 7 : dwidth=400; dheight[7]=200;//if(superimpose==true){ dheight[7]=400;}
	 graphim[jj]= createImage(dwidth,dheight[jj]); po=25; 
	 setAxis(graphim[jj], divy[jj],unity[jj]);  break;
	 case 8: dwidth=400; 
	 graphim[jj]= createImage(dwidth,dheight[jj]); po=25; 
	 setAxis(graphim[jj], divy[jj],unity[jj]);  break;
  /* case 6 :  dwidth=300;    ////////////horizontal displacement graph/
	 graphim[jj]= createImage(dwidth,dheight[jj]); po=dwidth/2; qo=25;
	 setAxis(graphim[jj], divy[jj],unity[jj]);
	qo=25; po=150; setVerticalAxis(jj);
	 break; */                         
	default : break;} 
}
  dx=ampl; dy=(float)(Math.sqrt(length*length-ampl*ampl));///d and dy calculated displacement at tt=0	
float Epo=mass*g*(length-dy); int p2=150, q2=25; x=(int)(x1+p2+ampl*480); y=(int)(y1+q2+dy*480);////displacement on the pendulum pict
lasty=new float[9];lasttt=0;//lasty stores last value of height, x ,Ek,Ep
lasty[6]=dx; lasty[7]=0; lasty[5]=length-dy;lasty[8]=mass*g*lasty[5]; po=18; 
repaint();try{synchronized(this){while(pause1==true) wait();}  } catch(InterruptedException ie){};
for(tt=0;tt<200;tt++){dx=(float)(ampl*(Math.sin((w*(float)tt/20)+Math.PI/2))); x=x1+p2+(int)(dx*480);
 dy=(float)(Math.sqrt(length*length-dx*dx)); y=y1+q2+(int)(480*dy); 
 Ep=mass*g*(length-dy); Ek=Epo-Ep;//(float)(Math.sqrt(length*length-ampl*ampl))*mass*g-Ep;
if(superimpose==true){addGreenPoint(graphim[8],7,Ek);}//addGreenPoint(graphim[7], 6, dx);
 float nwy=0; for( jj=5; jj<9; jj++)
 { switch(jj){case 5: nwy=length-dy;  break;
			case 6:  break; case 7: nwy=Ek;  break; case 8: nwy=Ep;  break; default: nwy=1;} 
   if(jj!=6){addPoint(graphim[jj], jj, nwy); }}
//graphim[6].getGraphics().drawLine((int)(p2+(float)lasty[6]*unity[6]*3), (int)(q2+lasttt*3),(int)(p2+dx*unity[6]*3) ,(int)(q2+(float)tt*3)); 
 msg2.setColor(lightyellow); msg2.fillRect(0,0,150,300);
   msg2.setColor(Color.black);
  msg2.drawString("time :  "+(float)tt/20, 5,30);  msg2.drawString("height :  "+(length-dy), 5,60); msg2.drawString("Ep :  "+Ep, 5,150);	
 msg2.drawString("horiz x :  "+dx, 5,90); msg2.drawString("Ek:  "+Ek, 5,120);   msg2.drawString("Ek+Ep :  "+(Ek+Ep), 5,200);
  repaint(); try{synchronized(this){while(pause1==true) wait();}lasttt=tt;lasty[6]=dx; roller.sleep(pauselength);   } catch(InterruptedException ie){};
}  pause2=true; exerciseover=true;superimpose=false; repaint();try{synchronized(this){while(pause2==true) wait();  }} catch(InterruptedException ie){}; 
 }
 
 if(step==8){msgimg=textDrawLines(600,270,"A SPRING has a resting or equilibrium position where its potential energy is defined as 0. When you pull (or compress) the spring , you give it potential energy. When you release it, the bob  moves towards the resting position, overshoots it, hence compressing (or extending) the spring. This compression (or extension) goes on upto the point, where velocity becomes 0, and potential energy is maximum. From there, the bob again gains speed, overshoots the equilibrium position and reaches it's starting point. The whole process starts over again. The bob is  oscillating. "); 
   float mass=(float)(0.5); int springcst=8; exerciseover=false;
   float w= (float)(Math.sqrt(springcst/mass)); float T=(float)(2*(Math.sqrt(mass/springcst))*Math.PI);
 ampl=(float)0.96; tt=0; /// calculated displacement at tt=0	
 int p2=280; springwidth=280;  x=x1+(int)springwidth;
  msgimg2=textDrawLines(170,30,"EQUILIBRIUM");
   pause1=true; repaint(); 
    repaint();pause1=true; try{ synchronized(this){while(pause1==true) wait();}} catch(InterruptedException ie){};
try{roller.sleep(100);   } catch(InterruptedException ie){};
 msgimg2=textDrawLines(250,30,"Compressing the spring"); compressing=true;
  for(int xx=0;xx<100*ampl;xx+=4){  springwidth=280-xx;  x=x1+(int)springwidth;////displacement on the  pict
   repaint(); try{roller.sleep(30);   } catch(InterruptedException ie){};}
 try{roller.sleep(300);   } catch(InterruptedException ie){};
 compressing=false; msgimg2=textDrawLines(170,30,"OSCILLATION");
 
 for(tt=0;tt<103;tt++){dx=(float)(ampl*(Math.sin(w*(float)tt/20+Math.PI/2)));springwidth=280-(int)(dx*100); 
 x=(int)(x1+springwidth); repaint();
 try{roller.sleep(pauselength);   } catch(InterruptedException ie){};}
 pause2=true; exerciseover=true;repaint(); try{ synchronized(this){while(pause2==true) wait();}} catch(InterruptedException ie){};
 step=9; superimpose=false;}
 
 if(step==9)
{msgimg=textDrawLines(300,30,"A SPRING");  /////////////animation + graphics of spring
 msgimg2=createImage(150, 300); Graphics msg2=msgimg2.getGraphics();
   msg2.setFont(textfont); msg2.setColor(lightyellow); msg2.fillRect(0,0,300,300);
   msg2.setColor(Color.black);
   float Ek=0; float Ep=0;float mass=(float)(0.5); int springcst=8;
 tt=0; exerciseover=false; pause1=true; int p2=130;int q2=25;
 float w= (float)(Math.sqrt(springcst/mass));  float T=(float)(2*(Math.sqrt(mass/springcst))*Math.PI);
for(jj=9; jj<=11; jj++){
 switch(jj)
    {	case 9 : dwidth=320; 
	 graphim[jj]= createImage(dwidth,300); po=18; 
	 setAxis(graphim[jj], divy[jj],unity[jj]);  break;
	 case 10: dwidth=320; 
	 graphim[jj]= createImage(dwidth,300); po=18; 
	 setAxis(graphim[jj], divy[jj],unity[jj]);  break;
    case 11: dwidth=260; po=dwidth/2; qo=25; ////////////horizontal displacement graph/
	 graphim[jj]= createImage(dwidth,dheight[jj]); 
	 setAxis(graphim[jj], divy[jj],unity[jj]);
	//setVerticalAxis(jj);

	 break;
   default : break;} 
}
 ampl=(float)0.96; dx=ampl; /// calculated displacement at tt=0	
float Epo=springcst*ampl*ampl/2; 
springwidth=280-(int)(ampl*100); x=x1+springwidth; ////displacement on the  pict
lasty=new float[12];lasttt=0;//lasty stores last value of  x ,Ek,Ep
 lasty[9]=0; lasty[10]=Epo; lasty[11]=x; po=18; 
repaint();try{synchronized(this){while(pause1==true) wait();}  } catch(InterruptedException ie){};
for(tt=0;tt<91;tt++){dx=(float)(ampl*(Math.sin(w*(float)tt/20-Math.PI/2)));springwidth=(int)(dx*100)+280; 
 x=(int)(x1+springwidth);  Ep=springcst*dx*dx/2; Ek=(float)(mass*ampl*ampl*w*w*(Math.cos(w*(float)tt/20-(Math.PI)/2))*(Math.cos(w*(float)tt/20-(Math.PI)/2)))/2;
 if (superimpose==true){addGreenPoint(graphim[9],10, Ep);}
 addPoint(graphim[9], 9, Ek);addPoint(graphim[10], 10, Ep);  
 graphim[11].getGraphics().drawLine((int)(p2+(float)lasty[11]*unity[11]*3), (int)(q2+lasttt*3),(int)(p2+dx*unity[11]*3) ,(int)(q2+(float)tt*3)); 
 msg2.setColor(lightyellow); msg2.fillRect(0,0,150,300);
 msg2.setColor(Color.black);
  msg2.drawString("time :  "+(float)tt/20, 5,30); msg2.drawString("T :  "+T, 5,60);   msg2.drawString("Ep :  "+Ep, 5,150);	
 msg2.drawString("horiz x :  "+dx, 5,90); msg2.drawString("Ek :  "+Ek, 5,120);   msg2.drawString("Ep+Ek :  "+(Ek+Ep), 5,200);
   repaint(); try{synchronized(this){while(pause1==true) wait();}lasttt=tt; lasty[11]=dx; roller.sleep(pauselength);   } catch(InterruptedException ie){};
	}
pause2=true;exerciseover=true; repaint(); try{ synchronized(this){while(pause2==true) wait();}} catch(InterruptedException ie){};
  pause1=false;}
 if(step==10){repaint();
 isrunning=false;} 
 }}
public void keyReleased(KeyEvent ke){}
public void keyPressed(KeyEvent ke)
{if(ke.getKeyCode()==KeyEvent.VK_ENTER)
     {if(step==3){evaluate(guess[flicker]);} 
	  else if (step==4) {evaluatefloat(guess[flicker]);}}
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
 {//input (float) has to be compared to correctvalue. Locate decimalpoint; read the rest and convert it to int
  //them do the divisions according to decipoint
  char[] cA=guesss.toCharArray();int N=0, ic=0; float convert=0;
 int  decipt=20;    
 for(ic=0;ic<guesss.length();ic++)
   {if(cA[ic]=='.'){ decipt=ic;} 
    else if (Character.isDigit(cA[ic])){int n=Integer.parseInt(""+cA[ic]);N=N*10+n; 
	if(ic==(decipt+2)) break;}
   } 
  if (decipt==20) {convert=N;}
  else{ convert=(float)N; int dec=Math.min(2,(guesss.length()-decipt-1 ));
  for (int pt=0;pt<dec;pt++){convert=convert/10;}}
  showStatus("convert  "+convert+ "sqrt 23 "+corrvar[0]);
 if((convert>0.98*corrvar[flicker])&&(convert<1.02*corrvar[flicker]) )  
  {evalimg=correctimg; foundvar[flicker]=1; showStatus("comparison  "+0.98*corrvar[0]);
 } 
 else  {
 evalimg=wrongimg; foundvar[flicker]=-1; }
   evaluated=true;  repaint(); notifyAll();
 } 
public void mouseReleased(MouseEvent me){}
public void mousePressed(MouseEvent me){}
synchronized public void mouseClicked(MouseEvent me)
 { int coX=me.getX(); int coY=me.getY(); 
  switch(step) 
   {case 1:  if((coY>height-50)&&(coX>390)&&(coX<510))
                    { step++; pause2=false;notify();} break;
	
     case 2: case 5 : if((coX>240)&&(coX<360))
	   {  if(coY>height-50)
           { if (pause1==false)     { pause1=true; notify(); }   ////pressed STOP
             else if (pause1==true) { pause1=false; notify(); }           ////pressed RESUME
     	   }
		   else if(coY>height-110)
              {if(pauselength==40) {pauselength=200;}         //////////presssed  SLOW
               else if(pauselength==200) {pauselength=40;}}   //////////pressed NORMAL
       }
	     else if ((coX>380)&&(coX<500))
		   {if(coY>height-50){if(step==2){pause2=false; notify(); step++;} else if (step==5){ pause2=false; notify();exp++;}}   ////pressed NEXT 
			else if(coY>height-110) {pause1=false; pause2=false;notify(); }     ////pressed SAME EXPERIM
            else if((coY>height-170)&&(step==2)) { superimpose=true;  pause2=false;notify();}
			}
      else if ((coX>width-200)&&(coY<140))
        {if (coX>width-110){ if (coY<70) {graphasked=1;} 
                                 else {graphasked=3;}}
      else if (coY<70) {graphasked=0;} 
      else {graphasked=2;}} 
        break;
     case 3 : if((coY>height-50)&&(coX>190)&&(coX<310))
		    if(t==0){ pause1=false; notify();}  //t++;
	        else if (flicker>=4){ pause1=false; notify(); }  //to avoid skipping answering and clicking next
			// else if ((coX>380)&&(coX<500))   {  pause2=false; step=4; notify();}
		   showStatus("step "+step);
		break;
	  case 4 : case 6 : case 10 : if((coY>height-50)&&(coX>390)&&(coX<510))
		   {pause2=false; notify();}
	  case 7 :  if((coX>width-360)&&(coX<width-240)){  if(coY>height-50)
           { if (pause1==false)     { pause1=true; notify(); }   ////pressed STOP
             else if (pause1==true) { pause1=false; notify(); }           ////pressed RESUME
     	   } }
	     else if ((coX>width-230)&&(exerciseover=true))
		   {if(coY>height-50)   {step++ ;pause1=false;  pause2=false;superimpose=false; notify();} ////pressed NEXT /////////
			else if(coY>height-110) {pause1=false;pause2=false;superimpose=false;notify();}    ////pressed SAME EXPERIM
            else if(coY>height-170) { superimpose=true;pause1=false; pause2=false;notify();}} 
			break;
	case 8: if((coY>height-50)&&(coX>390)&&(coX<510))
                    { if(tt>100){step++;pause1=true; pause2=false;notify();}
                      else if (tt==0) {pause1=false; notify();} } break;
	case  9 :if(coY>height-190)
        { if((coX>400)&&(coX<510))
           { if (pause1==false)     { pause1=true; notify(); }   ////pressed STOP
             else if (pause1==true) { pause1=false; notify(); }           ////pressed RESUME
     	   } 
	     else if ((coX>190)&&(coX<400)&&(exerciseover=true))
		   {if(coY>height-50)   {step++ ; pause1=false; pause2=false; superimpose=false; notify();} ////pressed NEXT ////
			else if(coY>height-110) {pause1=false; pause2=false;superimpose=false; notify();}    ////pressed SAME EXPERIM
            else  { superimpose=true; pause1=false; pause2=false;notify();}}  
		}	break; 
	default: break;
}}

public void mouseEntered(MouseEvent me){}
public void mouseExited(MouseEvent me){}


public void setAxis(Image im, float divy, double uny)
  {  int divx= 1; int unitx=10; if(step==8){unitx=20;} if (step==5) {unitx=5; divx=2;} 
     int previousx=0; float previousy=0; qo=dheight[jj]-15; if ((superimpose==true)&&(jj==7)) {qo=185; }///1 unitx=1 sec=10[]
     Graphics pg= im.getGraphics(); pg.setFont(new Font("Dialog", Font.PLAIN, 16));  // x=tt
      pg.setColor(Color.white); pg.fillRect(0,0,dwidth,dheight[jj]);
	  pg.setColor(Color.orange);
       for(int i=0; i<dwidth; i+=3)   {pg.drawLine(i,0,i,dheight[jj]);}
      for(int j=0; j<dheight[jj]; j+=3)   {pg.drawLine(0,j, dwidth,j);}
       if((step==8)&&(jj==6)){return;}
	   pg.setColor(Color.black); pg.drawLine( po, qo, dwidth-1, qo); pg.drawLine(po,2, po, qo);
      for(int p=0, i=0; p<dwidth-po; p+=3*(divx)*unitx, i++){pg.drawLine(p+po, qo,p+po, qo+3);
               pg.drawString(""+i*divx, p+po-4,dheight[jj]-1);}  //1 sec = 10 [] =30 pix 
      pg.drawString(""+variab[4], dwidth/2 , qo-2); pg.drawString(""+variab[jj], 20, 16); 
      if(uny!=0){  for(int q=qo, i=0; q>0; q-=(float)(divy*3*uny), i++)
       {pg.drawLine(po-3, qo-(int)(3*i*divy*uny), po, qo-(int)(3*i*divy*uny)); 
	   if(i%2==0){pg.drawString(""+i*divy, 1, qo+4-(int)(3*i*divy*uny) );}} }//  1 m=3 pix =1[]
  }
public void setVerticalAxis(int jj)
  {Graphics pg= graphim[jj].getGraphics(); po=dwidth/2; qo=25;  pg.setFont(new Font("Dialog", Font.PLAIN, 16)); 
 pg.setColor(Color.black); pg.drawLine( 1, qo, dwidth-1, qo); 
 pg.drawLine(po,qo,po,dheight[jj]-2);
     float fi=0; for(int p=0; p<(dwidth-2); p+=50)  //should be unity*3/5
	  {pg.drawLine(po+p,qo ,po+p, qo-3);pg.drawLine(po-p,qo ,po-p, qo-3);
       pg.drawString(""+fi, po+p-12, 20);if(p>0){pg.drawString("-"+fi, po-p-12, 20);} fi+=0.5 ;}//if(i>0) to avoid 2 zeroes 
      pg.drawString("Horizontal displacement", 5+dwidth/2, 40); pg.drawString(""+variab[4], 5+dwidth/2,dheight[jj]-26);   //axis labelling
      for(int q=qo, i=0; q<dheight[jj]-20; q+=60, i++)
       {pg.drawLine(po+3, q, po, q); 
	    pg.drawString(""+i, po-15, q+4);} //  1 unit=1sec=60 pix =20[]
 }
 /*public void setVerticalAxis()
  {Graphics pg= graphim[6].getGraphics();   pg.setFont(new Font("Dialog", Font.PLAIN, 16)); 
 pg.setColor(Color.black); pg.drawLine( 1, qo, dwidth-1, qo); pg.drawLine(po,qo,po,dheight[jj]-2);
      for(int p=0, i=0; p<(dwidth-2); p+=30, i++)
	  {pg.drawLine(po+p,qo ,po+p, qo-3);pg.drawLine(po-p,qo ,po-p, qo-3);
       pg.drawString("0."+i, po+p-12, 20);if(i>0){pg.drawString("-0."+i, po-p-12, 20);}}  //1 sec = 10 [] =30 pix 
      pg.drawString("Horizontal displacement", 5+dwidth/2, 40); pg.drawString(""+variab[4], 5+dwidth/2,dheight[6]-26);   //axis labelling
      for(int q=qo, i=0; q<dheight[jj]-20; q+=60, i++)
       {pg.drawLine(po+3, q, po, q); 
	    pg.drawString(""+i, po-15, q+4);}}//  1 unit=1sec=60 pix =20[]
 */
public void addPoint (Image im, int jj, float nwy)
{Graphics pg= im.getGraphics();pg.setColor(Color.black); int divx= 1; int unitx=10; qo=dheight[jj]-15;if(step==5){unitx=5;} if(step==8){unitx=20; if(jj!=5){qo=185;}}
  pg.drawLine((int)(po+(float)lasttt*unitx*3/20), (int)(qo-lasty[jj]*3*unity[jj]), (int)(po+(float)tt*(unitx)*3/20), (int)(qo-nwy*unity[jj]*3)); 
     lasty[jj]=nwy;  repaint();
//showStatus(" lasttt  "+lasttt+" Now  "+tt);
}
public void addGreenPoint (Image im, int jj, float nwy)
{Graphics pg= im.getGraphics();pg.setColor(Color.green); int divx= 1; int unitx=10; qo=dheight[jj]-15; if (step==5) {unitx=5; divx=2;} if(step==8){unitx=20; if(jj==6){qo=185;}}
  pg.drawLine((int)(po+(float)lasttt*unitx*3/20), (int)(qo-lasty[jj]*3*unity[jj]), (int)(po+(float)tt*(unitx)*3/20), (int)(qo-nwy*unity[jj]*3)); 
      repaint();
}  

public Image textDrawLines( int widthRect, int heightRect, String s)
{ Image im=createImage(widthRect, heightRect); Graphics msg=im.getGraphics();
   msg.setFont(textfont);  fm=msg.getFontMetrics();  
   msg.setColor(lightyellow); msg.fillRect(0,0,widthRect, heightRect);
   msg.setColor(Color.black);
   int wl=0;  wl=fm.stringWidth(s); 
        int linel=0;   lineheight=fm.getHeight();int y=lineheight+2;
       int spacelength = fm.stringWidth(" ");
         StringTokenizer tt=new StringTokenizer(s+"  ."); //  fullstop to find end of string
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
 
 
public void update(Graphics g)////////////////////////////////////////////////
{ offS.setColor(green); offS.fillRect(0,0,width,height);
   switch (step)
   {case 1 :   offS.drawImage(msgimg,250,100,this);
              offS.setColor(bluegreen); offS.fillRect(390, height-50,120,60); 
               offS.setColor(Color.yellow); offS.drawString("NEXT  ",400,height-20); break;
			   /////////////free falling ball animation
  case 2: case 5 :bigg.setColor(lightblue); bigg.fillRect(150,0,20,1200); 
          bigg.setColor(grass); bigg.fillRect(150,1200, 20,100);
bigg.setColor(Color.red); bigg.fillOval(152,y,8,8); 
CropImageFilter f;
FilteredImageSource fis;
f=new CropImageFilter(0,depart, 200, height);
   fis=new FilteredImageSource(bigScene.getSource(), f);
 scene_now=createImage(fis);
  offS.drawImage(msgimg,50,2,this);
offS.setColor(Color.black);   
  offS.drawString("Time : (sec)", 20,120); offS.drawString(""+(float)tt/20, 270,120);
offS.drawString("Acceleration : (m/sec²) ", 20,160); offS.drawString(""+a, 270,160);
offS.drawString("Speed: (m/sec)", 20,200);int vv=(int)(v*100); offS.drawString(""+(float)vv/100, 270,200);   //to avoid too many decimals
offS.drawString("Displacement: (m) ", 20,240); offS.drawString(""+((y-yo)/10), 270,240);
offS.drawString("Kinetic Energy: (J)", 20,280);int ek=(int)(Ek*100); offS.drawString(""+(float)ek/100, 270,280);   //to avoid too many decimals
offS.drawString("Potential Energy :(J)", 20,320);int ep=(int)(Ep*100+1); offS.drawString(""+(float)ep/100, 270,320);
offS.setColor(bluegreen); offS.fillRect(40, height-50,120,40);//offS.fillRect(180, height-50,120,40);
offS.fillRect(40, height-110,120,40); 
offS.fillRect(width-400, 20, 80, 50); offS.fillRect(width-310, 20 ,80,50);
offS.fillRect(width-400, 90, 80,50);offS.fillRect(width-310, 90,80,50);
offS.setColor(Color.yellow);  
offS.drawString("v/t", width-390,60); offS.drawString("s/t", width-290,60);
offS.drawString("Ek", width-390,130); offS.drawString("Ep", width-290,130);
 if (pause1==true){ if(tt==0) {offS.drawString("START ", 50,height-20);}
                    else {offS.drawString("RESUME ",  50,height-20);}}
 else if(pause1==false) {offS.drawString("STOP ", 50,height-20);}
 if(exerciseover==true){offS.setColor(bluegreen);offS.fillRect(180, height-50,120,40);
    offS.fillRect(180, height-110,200,40);
    offS.setColor(Color.yellow); offS.drawString("NEXT  ",190, height-20);
    offS.drawString("Same experiment ", 180,height-80); 
 if(step==2){offS.setColor(bluegreen);offS.fillRect(180, height-170,200,40);
 offS.setColor(Color.yellow); offS.drawString("Superimpose Ep on Ek", 183,height-140);}
 }
if(pauselength==40){offS.drawString("SLOW ", 50,height-80);}
else if(pauselength==200){offS.drawString("NORMAL ", 50,height-80);}
if(superimpose==true){graphasked=2;}
if(graphasked>-1)  {offS.drawImage(graphim[graphasked], width-dwidth-200, height-410, this);}
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
{if((foundvar[ii]<0)&&(t>0)){ offS.drawString("no,it is "+correctvar[ii], 400,400+ii*40);}}
  for (int ii=0; ii<4;ii++)
   {offS.drawString(variab[ii], 10,400+ii*40); offS.drawString(guess[ii], 250,400+ii*40);}
if((t>0)&&(flicker>3)){offS.drawString("Ek+Ep  : (J)", 10,400+4*40);
  offS.drawString(""+(correctvar[2]+correctvar[3]), 250,400+4*40);}
 offS.drawString(variab[4], 10,360); offS.drawString(""+t, 250,360);
if(evalimg!=null){ offS.drawImage(evalimg, 350, 370+(flicker-1)*40, 50,40,this); }
offS.setColor(lightyellow); offS.fillRect(width-dwidth,100,300,80);   /////formula  to use 
if(flicker<4){offS.setColor(Color.black); offS.drawString(formularray[flicker], width-dwidth+10,140);}
offS.drawImage(graphim[flicker], width-dwidth, height-dheight[jj],this);
if((flicker==4)||(t==0)){offS.setColor(bluegreen);offS.fillRect(190, height-50, 140 ,40);  //button next
offS.setColor(Color.yellow); offS.drawString("next exercise",200,height-20); }
 //if(t>4) {offS.drawString("NEXT  ",390,height-20); }
break;  
case 4: offS.drawImage(msgimg, 200, 10,this); 
    offS.setColor(bluegreen);offS.fillRect(390, height-50, 120 ,40);     //button next
offS.setColor(Color.yellow); offS.drawString("NEXT",400,height-20); 	
break;
 case 6 : offS.drawImage(msgimg,400,y1,this);
	if(exerciseover==true){offS.setColor(bluegreen); offS.fillRect(390, height-50,120,60); 
	offS.setColor(Color.yellow); offS.drawString("NEXT  ",400,height-20); }
	offS.setColor(lightblue); offS.fillRect(x1,y1,300,520); offS.setColor(Color.black);
	offS.drawLine(x1+150,y1+25,x,y);offS.setColor(Color.red); offS.fillOval(x-6,y-6,12,12);
 break;

case 7 : offS.setColor(lightblue); offS.fillRect(x1,y1,300,520);
 offS.drawImage(msgimg, 10, 10,this); offS.drawImage(msgimg2, width-160, 50,this);
 offS.setColor(Color.black);
offS.drawLine(x1+150,y1+25,x,y);
offS.setColor(Color.red); offS.fillOval(x-6,y-6,12,12);
offS.drawImage(graphim[5], x2, 495,this); ///495 to align on the bob y1=50 q2=25 => y=555
//offS.drawImage(graphim[6], x1,y2,this);
offS.drawImage(graphim[7], x2, y1-10,this);
offS.drawImage(graphim[8], x2,y1+215,this);

offS.setColor(bluegreen); offS.fillRect(width-360, height-50,120,40);//offS.fillRect(40, height-110,120,40); 
offS.setColor(Color.yellow);
if (pause1==true){ if(tt==0) {offS.drawString("START ", width-350,height-20);}

                    else {offS.drawString("RESUME ",  width-350,height-20);}}
 else if(pause1==false) {offS.drawString("STOP ", width-350,height-20);}
 if(exerciseover==true){offS.setColor(bluegreen);offS.fillRect(width-230, height-50,200,40);offS.fillRect(width-230, height-110,200,40);offS.fillRect(width-230, height-170,200,40);
    offS.setColor(Color.yellow); offS.drawString("NEXT  ",width-220, height-20);
    offS.drawString("Same experiment ", width-220,height-80);offS.drawString("Superimpose ", width-220,height-140);}
break;
case 8 :  offS.drawImage(msgimg, 50, 300,this);  offS.drawImage(msgimg2, 80, 0,this); 
offS.drawImage(springimg, x1, y1, springwidth, 122,this); 
offS.setColor(Color.red); offS.fillOval(x,y1-18,48,48);
offS.setColor(Color.black);offS.fillRect(0,0,50,300);offS.drawLine(354, 170, 280+50+24, 180);offS.drawString("equilibrium position", 300,200);
if(compressing==true){offS.drawImage(arrowimg, x+60, y1+33, this);}
if ((pause1==true)&&(tt==0)) { offS.setColor(bluegreen);offS.fillRect(390, height-50, 120 ,40);  
                                 offS.setColor(Color.yellow); offS.drawString("START ", 400,height-20);}
if(exerciseover==true){   offS.setColor(bluegreen);offS.fillRect(390, height-50, 120 ,40);     //button next
    offS.setColor(Color.yellow); offS.drawString("NEXT",400,height-20); 	}
break;
case 9 :  offS.drawImage(springimg, 50, 50,springwidth, 122,this); 
offS.setColor(Color.red); offS.fillOval(x,y1-18,48,48);
offS.setColor(Color.black);offS.drawLine(354, 170, 280+50+24, 180);offS.drawString("equilibrium position", 300,200);
offS.fillRect(0,0,50,300);
offS.drawImage(graphim[9], 490,5,this);
offS.drawImage(graphim[10], 490, 330,this);
offS.drawImage(graphim[11], 224, 210,this);
offS.drawImage(msgimg2, width-130, 50,this);

offS.setColor(bluegreen); offS.fillRect(390, height-50,120,40);//offS.fillRect(40, height-110,120,40); 
offS.setColor(Color.yellow);
if (pause1==true){ if(tt==0) {offS.drawString("START ", 400,height-20);}

                    else {offS.drawString("RESUME ",  400,height-20);}}
 else if(pause1==false) {offS.drawString("STOP ", 400,height-20);}
 if(exerciseover==true){offS.setColor(bluegreen);offS.fillRect(190, height-50,200,40);offS.fillRect(190, height-110,200,40);offS.fillRect(190, height-170,200,40);
    offS.setColor(Color.yellow); offS.drawString("NEXT  ",240, height-20);
    offS.drawString("Same experiment ", 200, height-80);  offS.drawString("Superimpose", 200,height-140);}
	break;
case 10 : offS.setColor(bluegreen);offS.fillRect(200,200,200,40);
	offS.setColor(Color.yellow); offS.drawString(" END OF THE DEMONSTRATION", 210,230); break;
default : break;
}
paint(g);
}
public void paint(Graphics g)
 {if(isrunning==true) 
  {if((step==2)||(step==5)) { g.drawImage(offScreenImage,200,0,this); 
            g.drawImage(scene_now,0,0,this);}
   else{ g.drawImage(offScreenImage,0,0,this);} // g.setColor(Color.black); g.setFont(big);g.drawString("End of the exercise",200,200);
   
}}}
