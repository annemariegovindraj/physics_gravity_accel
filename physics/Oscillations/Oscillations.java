import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.awt.image.*;

public class Oscillations extends Applet implements Runnable , MouseListener //, KeyListener  
{boolean isrunning=true, pause1=true , pause2=false, exerciseover=false, firstmvt=true,superimpose=false; 
 int height, width,   pauselength=30, xo=50, yo=50, x=xo,y=yo, x1=50, x2=0,  y1=90, y2=0,  A=150,B=80;
 int   a=10, tt=0, lasttt=0, step=-1, springwidth=280,jj=0; 
float v=0, d=0, m=(float)(0.1), Ek=0, Ep=0, ampl=(float)(0.2); float lasty[];
Thread roller; 
String text[]=new String [20];
float [] divy={(float)(0.25),(float)0.5,(float)0.1,(float)0.5,(float)0.1,(float)0.1,(float)0.2,(float)0.025,(float)0.025};
int unity[]= {40,40,160,15,60,60,30,400,400};
//////////////display
 Color lightblue= new Color(0,255,255); Color green= new Color(78,214,173);
 Color bluegreen= new Color(47, 166, 187); Color lightyellow= new Color(255,245,191);
Image offScreenImage,  msgimg1, msgimg2, msgimg3, scene, scene2; 
Image[] graphim =new Image[12]; int[] dwidth={600,300,300,400,400,400,400,400,400}; int dheight[]={200,400,400,200,200,200,200,200,200};
Graphics offS; Font thizfont;
Image springimg, arrowimg, pendulumimg;
Font big = new Font("Verdana", Font.PLAIN, 20); Font textfont= new Font("Dialog", Font.PLAIN, 18); FontMetrics fm; 
 String[] variab={"", "displacement(m)","displacement(m)", "speed(m/sec)","Kinetic energy(J)", "Potential Energy (J)", "speed(m/sec)", "Kinetic energy(J)", "Potential Energy (J)" };

public void init()
{text[1]= "Periodic motion repeats itself after regular intervals of time. All 3 curves show periodic motion. Only the lower curve represents simple harmonic motion (SHM) : here the displacement follows a sinusoidal curve and can be described as y= A cos("+"\u03C9"+" t+"+"\u03C6"+") (or as B sin("+"\u03C9"+"t+"+"\u03C8). As examples of simple harmonic motion, we will study the displacement and energy in . - a spring . - a simple pendulum.";
text[2]= "What are the forces that make the bob of the spring oscillate around it's equilibrium position? . The spring is an elastic body : for small deformations, a restoring force tends to bring the bob back to equilibrium position. This restoring force is proportional to the increase (decrease) in length of the spring, or the displacement of the bob from the equilibrium point : F=- kx . where k is the springconstant and depends on the material and geometry of the spring. The minus sign shows that when we pull the bob to the right (i.e. x>0), the force (F<0) will pull the bob to the left and vice versa."; 
text[3]=" We have : . restoring force F=- kx . Newton's law F = ma with a=$d²x!/dt²! . . becomes : m$d²x!/dt²! +kx = 0 . One possible solution is x= A cos(\u03C9 t) (another is x= Bsin(\u03C9t+\u03C6)  since  $d²x!/dt²! is -A\u03C9²cos(\u03C9t) .  . For a given mass (m) and springconstant (k), we can calculate \u03C9² : . -mA\u03C9² cos(\u03C9 t)+kAcos(\u03C9t) =0 or \u03C9²=$k!/m!";
text[4]="How can we describe the oscillation of the pendulum around it's mean position ? Two forces act on the bob . - gravity (g) .  -tension in the string (T)  . Gravity can be resolved into 2 components : gcos\u03B8 along the radius and gsin\u03B8 tangentially to the displacement . The gcos\u03B8 component is counterbalanced by the tension in the string. . The restoring force mg sin\u03B8 is directed towards the equilibrium position . ";
text[5]=" We have : . restoring force : F = -mgsin\u03B8 . Newton's law : F=ma  with a = $d²x!/dt²! . Here sin\u03B8 = $x!/L! where L = length of the string. For small angles (<20\u00B0), sin\u03B8 ~\u03B8 . So m$d²x!/dt²!  = -mg $x!/L! . or $d²x!/dt²!  + $g!/L!x=0. Again we use the solution x=Acos\u03C9t where \u03C9² =$g!/L!";
text[6]="DEFINITIONS  . - PERIOD (T) : smallest interval of time after which the motion is repeated. Here 2 sec. . -FREQUENCY (\u03BD) : Number of oscillations per sec. Here 0.5 oscillations per sec = 0.5 Hertz. . ";    
text[7]="AMPLITUDE : the maximum displacement from the equilibrium position. Oscillation is between -A and +A if x= Acos(\u03C9t) and between -B and +B if x=Bcos(\u03C9t) .";
text[8]=" PHASE : the argument of the cosine or sine function e.g.(\u03C9t) or cos(\u03C9t+\u03C6) . PHASE CONSTANT OR EPOCH : the phase at t=0 : here 0 (black curve) -\u03C0/2 (red curve)";
text[9]="What does  \u03C9 stand for ? . Let's go back to uniform circular motion. Here movement is described as \u03B8 = \u03C9 t with the angular velocity \u03C9 = constant.  ";
text[10]="Now imagine that we project the movement of the ball on the X-axis, or that we look at the movement of the ball from the side. We obtain a Simple harmonic motion (x=Acos(\u03C9t) with the speed highest around mean position . The angular frequency \u03C9 of simple harmonic motion is the same as the angular speed of the reference point.";
text[11]="There are 2 ways to find the speed of the particle in SHM . 1 -using the reference circle (of radius A) : velocity v =A\u03C9 and is directed tangentially to the circle. The projection of v on the X-axis is  vsin(\u03C9t)=A\u03C9sin(\u03C9t). . 2 -using the derivative v=$dx!/dt! = $d(Acos\u03C9t)!/dt! = A\u03C9sin(\u03C9t) . Kinetic energy is  $mv²!/2! = $m!/2!A²\u03C9²sin²(\u03C9t) =  $m!/2!\u03C9²A²(1-cos²(\u03C9t))  = $m!/2! \u03C9²(A²-x²)." ; 
text[12]="Potential energy  of a spring is the work done to compress the spring over a distance x. The force to overcome increases with x as F= kx.  . Work done = W =\u222Bkx.dx = $kx²!/2! = $m\u03C9²x²!/2! . Note that potential energy is highest where displacement is highest and speed is zero. . Sum of Kinetic and Potential Energy remains constant: . $m\u03C9² (A²-x²)!/2! + $m\u03C9²x²!/2!= $m\u03C9²A²!/2! = $mkA²!/2m! = $kA²!/2!. . Next we calculate and show on graphics the displacement, speed, Kinetic Energy (E%k!) and Potential Energy (E%p!) for a frictionless spring and a pendulum. Note that both E%k! and E%p! follow a simple harmonic motion with doubled frequency (remember 2sin²\u03C9t = 1-cos2\u03C9t) . You can stop the animation anytime.";
//addKeyListener(this); requestFocus();
height=Integer.parseInt(getParameter("height"));
width=Integer.parseInt(getParameter("width"));
addMouseListener(this);
 setBackground(green);
offScreenImage=createImage(width, height); offS=offScreenImage.getGraphics();
offS.setFont(big); offS.setColor(green);offS.fillRect(0,0,width,height);
 repaint();}
 
 public Image getAlpha(Image img)
{Image a_img; int awidth=img.getWidth(null); int aheight=img.getHeight(null);
int[] pixels= new int[awidth*aheight];
PixelGrabber pg= new PixelGrabber(img,0,0,awidth,aheight,pixels,0,awidth);
try{pg.grabPixels();}catch(InterruptedException ie){};
for(int i=0; i<pixels.length; i++)
{int p = pixels[i];  int red = 0xff&(p>>16);
 int green = 0xff&(p>>8); int blue = 0xff&(p);
 if ((red>245)&&(green>245)&&(blue>245)){pixels[i]=(0x00000000);}
}
a_img=createImage(new MemoryImageSource(awidth,aheight,pixels,0,awidth));
return a_img;
}
 public void start()
{if(roller==null)   {roller=new Thread(this);}
  isrunning=true; roller.start(); pause1=true;
 }
public void stop()
{if(roller!=null)  { isrunning=false; roller=null;} 
  } 
public void run()
{ while (isrunning==true)
 {  if (step==-1)
	{msgimg1=textDrawLines(250,40," PERIODIC MOTION ");  
     msgimg2=textGetLines(text[1], 450); int ww=300, hh=450;//scene.getWidth(), int hh=scene.getHeight());
	 scene=createImage(ww,hh); Graphics sgr= scene.getGraphics();
	sgr.setColor(Color.white);sgr.fillRect(0,0,ww,hh);sgr.setFont(big);
    sgr.setColor(Color.orange);
       for(int i=0; i<ww; i+=3)   {sgr.drawLine(i,0,i,hh);}
      for(int j=0; j<hh; j+=3)   {sgr.drawLine(0,j, ww,j);}
     sgr.setColor(Color.black); 
	 for(int j=0; j<3;j++)
	 { sgr.drawLine(0,j*150+75, ww,j*150+75);
       sgr.drawString ("Displacement", 5, j*150 +10);sgr.drawString ("Time", ww-65, (j+1)*150-80);}
	 float t=0;
	for(int HT=0; HT<10; HT++){sgr.drawLine(HT*30,(int)(75+60*Math.pow(-1,HT)),(HT+1)*30,(int)(75+(int)60*Math.pow(-1,(HT+1))));}
 	
	for(int HT=0; HT<10; HT++)
		{if(HT%2==0) { t=0; for(int x=HT*30; x<(HT+1)*30; x=x+3) 
	         {sgr.drawLine(x, (int)(225- t*t), x+3,(int)(225-(t+0.8)*(t+0.8))); t=t+(float)0.8;}}
		 
		if(HT%2==1) { t=0; for(int x=HT*30; x<(HT+1)*30; x=x+3) 
	         {sgr.drawLine(x, (int)(225-64+ (t*t)), x+3,(int)( 225-64+ (t+0.8)*(t+0.8))); t=t+(float)0.8;}}}
		 t=0;
	for(int x=0; x<300; x+=3)
	{sgr.drawLine(x, 375+(int)(60*Math.cos(t)), x+3,(int)( 375+60*Math.cos(t+Math.PI/10))); t=t+(float)(Math.PI/10);}
	repaint(); pause2=true; 	
///////////////////preparing step 2/////////////
 Image sprngim =null, arrwim=null;
 int tracked=0; String name[]= new String[5]; Image bgitems[]= new Image[5];
try{MediaTracker tracker = new MediaTracker(this);
      StringTokenizer mst= new StringTokenizer(getParameter("img"),"$");
      while(mst.hasMoreTokens()&&tracked<5)      
	{name[tracked]=mst.nextToken();
	bgitems[tracked]=getImage(getClass().getResource(name[tracked]+".gif"));
  	tracker.addImage(bgitems[tracked], tracked);
	tracked++;}
      tracker.waitForAll();
    }catch(InterruptedException e){showStatus("No picture found");}
    springimg=getAlpha(bgitems[0]); arrowimg=getAlpha(bgitems[1]);

try{ synchronized(this){while(pause2==true) wait();}} catch(InterruptedException ie){};
 }
 if(step==0){msgimg2=textGetLines(text[6],600);  ///////////////////////////definitions period frequency
   jj=0;scene=createImage(dwidth[jj],dheight[jj]); Graphics sgr= scene.getGraphics();
 drawGraphpaper(scene); setAxis(scene,(float)0.5,20);
 sgr.setColor(Color.black); sgr.setFont(big);//thizfont.getName(), Font.Bold, 18);
	int po=18; float t=0;	for(int x=po; x<dwidth[jj]; x+=3)
	{sgr.drawLine(x, 100-(int)(80*Math.cos(t)), x+3,(int)( 100-80*Math.cos(t+Math.PI/20))); t=t+(float)(Math.PI/20);}
	 sgr.drawLine(18, 2, 18 , dheight[jj]-2); sgr.drawLine(0, 100, dwidth[jj], 100); sgr.drawString("T", 300,35);
	 drawVector(scene,po+240,15,0,(120-4)); drawVector(scene,po+360,15,(float)Math.PI,(120-4));
    //   sgr.drawString ("Displacement", 20, 30);sgr.drawString ("Time", dwidth[jj]-65, 98);
	repaint(); pause2=true; try{synchronized(this){while(pause2==true) wait();  }} catch(InterruptedException ie){};  	
 }
 if(step==1){///showing  amplitude and phase diff////// 
   msgimg1=textDrawLines(600,80,text[7]); msgimg2=textDrawLines(600,120,text[8]); 
   jj=0; scene=createImage(dwidth[jj],dheight[jj]); Graphics sgr= scene.getGraphics();
 	int po=18;  drawGraphpaper(scene);  sgr.setColor(Color.black); sgr.setFont(big);setAxis(scene,(float)0.5,20);
		sgr.drawLine(po, 2, po , dheight[0]-2);sgr.drawLine(0, 100, dwidth[0], 100); 
		sgr.drawString("x=Acos(\u03C9t)",240,30);sgr.drawString("A", po+120-5,  100-80-2); sgr.drawString("-A",po+60-8,  100+80+12);
// definitions amplitude 
   float t=0;	for(int x=po; x<dwidth[0]; x+=3)
	{sgr.drawLine(x, 100-(int)(80*Math.cos(t)), x+3,(int)( 100-80*Math.cos(t+Math.PI/20))); t=t+(float)(Math.PI/20);}
	   t=0;sgr.setColor(Color.red); sgr.drawString("x=Bcos(\u03C9t)", 450, 20); sgr.drawString("B", po+120-5,  100-50-2);sgr.drawString("-B", po+60-8,  100+50+12);
	for(int x=po; x<dwidth[0]; x+=3)
	{sgr.drawLine(x, 100-(int)(50*Math.cos(t)), x+3,(int)( 100-50*Math.cos(t+Math.PI/20))); t=t+(float)(Math.PI/20);}
 scene2=createImage(dwidth[0],dheight[0]);  Graphics sgr2= scene2.getGraphics(); sgr2.setFont(big); drawGraphpaper(scene2);  sgr2.setColor(Color.black); sgr.setFont(big);
		setAxis(scene2,(float)0.5,20);sgr2.drawLine(po, 2, po , dheight[2]-2);sgr2.drawLine(0, 100, dwidth[0], 100);
     sgr2.drawString("x=Acos(\u03C9t)",240,20); 
    t=0;	for(int x=po; x<dwidth[0]; x+=3)
	{sgr2.drawLine(x, 100-(int)(80*Math.cos(t)), x+3,(int)( 100-80*Math.cos(t+Math.PI/20))); t=t+(float)(Math.PI/20);}
	   t=0;sgr2.setColor(Color.red); sgr2.drawString("x=Acos(\u03C9t+\u03C6)",450,20);
	for(int x=po; x<dwidth[0]; x+=3)
	{sgr2.drawLine(x, 100-(int)(80*Math.cos(t-Math.PI/2)), x+3,(int)( 100-80*Math.cos(t-9*Math.PI/20))); t=t+(float)(Math.PI/20);}

 	repaint(); pause2=true; try{synchronized(this){while(pause2==true) wait();  }} catch(InterruptedException ie){};  	
 }
 if(step==2){ 
 msgimg1=textDrawLines(width/2,150,text[9]);  msgimg2=textDrawLines(width/2,250,text[10]); 
 scene=createImage(400,400); Graphics sgr= scene.getGraphics();
 	firstmvt=true; exerciseover=false;
	int po=200; int qo=200; float A=150, B=150; float w=(float)Math.PI/4;
	sgr.setColor(lightblue); sgr.fillRect(0,0,400,400);
	sgr.setColor(Color.black); sgr.setFont(big);
    sgr.drawOval(50,(int)(qo-B),300,300); sgr.setColor(Color.red); sgr.fillOval(po+(int)A-12, qo-12, 24,24);
	 for (int tt=0;tt<160; tt++) {x=(int)(A*Math.cos(w*tt/20));y=(int)(B*Math.sin(w*tt/20)); 
	 sgr.setColor(lightblue); sgr.fillRect(0,0,400,400);
	 sgr.setColor(Color.black); sgr.drawLine(po,qo,po+x,qo-y); sgr.drawString("\u03B8 = \u03C9t", po+x/2, qo-y/2);
     sgr.drawOval(50,(int)(qo-B),300,300); sgr.setColor(Color.red); sgr.fillOval(po+(int)x-12, qo-y-12, 24,24);
	  repaint(); try{roller.sleep(pauselength);   } catch(InterruptedException ie){};}
	firstmvt=false;
	  for (int tt=0;tt<600; tt++) {B=150-tt/2;;if(B<0){B=0;}
	  x=(int)(A*Math.cos(w*tt/20));y=(int)(B*Math.sin(w*tt/20)); 
	 sgr.setColor(green); sgr.fillRect(0,0,400,400);
	 sgr.setColor(lightblue); sgr.fillRect(0,(int)(qo-B*20/15),400,(int)(2*B*20/15));
	 sgr.setColor(Color.black); if(B>0){sgr.drawLine(po,qo,po+x,qo-y); sgr.drawString("\u03B8 = \u03C9t", po+x/2, qo-y/2);}
	 sgr.drawOval(50,(int)(qo-B), 300,(int)(2*B));
     sgr.setColor(Color.red); sgr.fillOval(po+(int)x-12, qo-y-12, 24,24);
	  repaint(); try{roller.sleep(pauselength);   } catch(InterruptedException ie){};}
	  
  pause2=true; exerciseover=true;repaint(); try{ synchronized(this){while(pause2==true) wait();}} catch(InterruptedException ie){};

 	  }
 if(step==3){step=4;}
 if (step==4) {msgimg1=textDrawLines(300,30,"A FRICTIONLESS SPRING"); msgimg2=textDrawLines(800,260,text[2]);
 float mass=(float)(0.5); int springcst=8; exerciseover= false;
 float w= (float)(Math.sqrt(springcst/mass));  float T=(float)(2*(Math.sqrt(mass/springcst))*Math.PI);
 ampl=(float) 0.5; tt=0; /// calculated displacement at tt=0	
  springwidth=280;  x=x1+(int)springwidth;
  msgimg3=textDrawLines(170,30,"EQUILIBRIUM");
   pause1=true; repaint(); 
   try{ synchronized(this){while(pause1==true) wait();}} catch(InterruptedException ie){};
try{roller.sleep(100);   } catch(InterruptedException ie){};
 msgimg3=textDrawLines(250,30,"Compressing the spring"); firstmvt=true;
  for(int xx=0;xx<120*ampl;xx+=4){  springwidth=280-xx;  x=x1+(int)springwidth;////displacement on the  pict
   repaint(); try{roller.sleep(30);   } catch(InterruptedException ie){};}
 try{roller.sleep(300);   } catch(InterruptedException ie){};
 firstmvt=false; msgimg3=textDrawLines(170,30,"OSCILLATION");
  ////displacement on the  pict
  for(tt=0;tt<103;tt++){float dx=(float)(ampl*(Math.sin(w*(float)tt/20+Math.PI/2)));springwidth=280-(int)(dx*unity[1]*3); 
 x=(int)(x1+springwidth); repaint();
 try{roller.sleep(pauselength);   } catch(InterruptedException ie){};}
 pause2=true; exerciseover=true;repaint(); try{ synchronized(this){while(pause2==true) wait();}} catch(InterruptedException ie){};
 }
 
 if(step==5){ msgimg2=textGetLines(text[3],430); float mass=(float)(0.5); int springcst=8;
 tt=0; jj=1;exerciseover=false; pause1=true; 
 float w= (float)(Math.sqrt(springcst/mass));  float T=(float)(2*(Math.sqrt(mass/springcst))*Math.PI);
 int p2=(dwidth[jj])/2; int q2=25;
   ampl=(float)0.5;   float dx=ampl;////////////horizontal displacement graph/
	graphim[jj]= createImage(dwidth[jj],dheight[jj]); 
	 drawGraphpaper(graphim[jj]);
    setVerticalAxis(jj);
springwidth=280-(int)(ampl*unity[1]*3); x=springwidth; ////displacement on the  pict
lasty=new float[8];lasttt=0;lasty[0]=x;
repaint(); try{synchronized(this){while(pause1==true) wait();}  } catch(InterruptedException ie){};
for(tt=0;tt<120;tt++){dx=(float)(ampl*(Math.cos(w*(float)tt/20+Math.PI)));springwidth=(int)(dx*unity[1]*3)+280; 
 x= springwidth; 
 graphim[1].getGraphics().drawLine((int)(p2+(float)lasty[jj]*unity[jj]*3), (int)(q2+lasttt*3),(int)(p2+dx*unity[jj]*3) ,(int)(q2+(float)tt*3)); 
  repaint(); try{synchronized(this){while(pause1==true) wait();}lasttt=tt; lasty[jj]=dx; ;roller.sleep(pauselength);   } catch(InterruptedException ie){};
	}
pause2=true; exerciseover=true; repaint(); try{ synchronized(this){while(pause2==true) wait();}} catch(InterruptedException ie){};
  }
if(step==6){ msgimg2=textDrawLines(500,300, text[4]); msgimg1=textDrawLines(220,40,"SIMPLE PENDULUM");
 exerciseover=false; float length=1; float g= (float)(9.81);  float T=(float)(2*(Math.sqrt(length/g))*Math.PI);
 float w= (float)(Math.sqrt(g/length)); int p2=150, q2=25;ampl=(float)0.2;
 float dx=ampl; float dy=(float)(Math.sqrt(length*length-ampl*ampl)); 
 pendulumimg=createImage(300,520); Graphics pendg=pendulumimg.getGraphics();
 for(tt=0;tt<=(int)(50*Math.PI);tt++){dx=(float)(ampl*(Math.cos(w*(float)tt/20))); x=p2+(int)(480*dx);
 dy=(float)(Math.sqrt(length*length-dx*dx)); y=q2+(int)(480*dy); 
 pendg.setColor(lightblue); pendg.fillRect(0,0,300,520); pendg.setColor(Color.black);
 pendg.fillRect(0,0,300,25); pendg.drawLine(p2,q2,x,y);pendg.setColor(Color.red); pendg.fillOval(x-6,y-6,12,12);
   pendg.setColor(Color.orange);pendg.drawLine(150,25,x,y);pendg.setColor(Color.red); pendg.fillOval(x-6,y-6,12,12);
repaint(); try{roller.sleep(pauselength); } catch(InterruptedException ie){};
  }step=7; ///continues immediately
   }
else if (step==7){msgimg2=textDrawLines(500,300, text[4]);
pendulumimg=createImage(350,620); Graphics pendg=pendulumimg.getGraphics();
 pendg.setColor(lightblue); pendg.fillRect(0,0,350,620); pendg.setColor(Color.black);pendg.fillRect(0,0,350,25);
 exerciseover=false; float length=100; float g= (float)(9.81); float m= (float)0.5; float T=(float)(2*(Math.sqrt(length/g))*Math.PI);
 float w= (float)(Math.sqrt(g/length)); thizfont=pendg.getFont(); pendg.setFont(new Font (thizfont.getName(),Font.PLAIN, 20));
 int p2=150, q2=25;ampl=20;
x= p2+(int)(4.8*ampl); y=q2+(int)(4.8*Math.sqrt(length*length-ampl*ampl));  
    pendg.setColor(Color.orange);pendg.drawLine(p2,q2,x,y);pendg.setColor(Color.red); pendg.fillOval(x-6,y-6,12,12);
 pendg.setColor(Color.black);
 pendg.drawString("mgsin(\u03B8)",x-85,y-5);pendg.drawString("mgcos(\u03B8)",x+10,y+40); pendg.drawString("g",x-12 , y+50);
  pendg.drawString("\u03B8",p2-4,50); pendg.drawString("T",x ,y-50);
 drawVector(pendulumimg, x,y, (float)(1.5*Math.PI), m*g*20);  //gravity
float theta=(float)(Math.PI+0.2);
  drawVector(pendulumimg, x,y, theta, (float)(m*g*4)); //sin
 theta=(float)(Math.PI/2+0.2);
  drawVector(pendulumimg, x,y, theta, (float)(m*g*20*Math.sqrt(0.96)));
 theta=(float)(1.5*Math.PI+0.2);
  drawVector(pendulumimg, x,y, theta, (float)(m*g*20*Math.sqrt(0.96)));
 pause2=true; exerciseover=true;superimpose=false; 
 repaint();try{synchronized(this){while(pause2==true) wait();  }} catch(InterruptedException ie){}; 
}
if(step==8){msgimg2=textGetLines(text[5],450); //pendulum grahic of x
exerciseover=false; float length=1; float g= (float)(9.81);  float T=(float)(2*(Math.sqrt(length/g))*Math.PI);
 tt=0; float w= (float)(Math.sqrt(g/length));  ampl=(float)0.2;
 float dx=ampl; float dy=(float)(Math.sqrt(length*length-ampl*ampl)); 
  ////////////horizontal displacement graph/
jj=2;	graphim[jj]= createImage(dwidth[jj],dheight[jj]);drawGraphpaper(graphim[jj]); 
	setVerticalAxis(jj);
 lasty=new float[4];lasttt=0;//lasty stores last value of  x 
 lasty[jj]=dx; dy=(float)(Math.sqrt(length*length-dx*dx)); 
 ///moving bob
 pendulumimg=createImage(300,520); Graphics pendg=pendulumimg.getGraphics();
 int p2=dwidth[2]/2, q2=25;x=(int)(p2+unity[jj]*3*dx); y=  (int)(q2+480*dy); 
 pendg.setColor(lightblue); pendg.fillRect(0,0,300,520); pendg.setColor(Color.black);pendg.setFont(big);
 pendg.fillRect(0,0,300,25); pendg.drawLine(p2,q2,x,y);pendg.drawString("x", p2+55, q2+460); pendg.drawString("L", p2+75, q2+340);
 pendg.setColor(Color.red); pendg.fillOval(x-6,y-6,12,12);
 pause1=true;
 repaint();try{synchronized(this){while(pause1==true) wait();}  } catch(InterruptedException ie){};

 for(tt=0;tt<120;tt++){dx=(float)(ampl*(Math.cos(w*(float)tt/20))); x=p2+(int)(dx*unity[2]*3);
 dy=(float)(Math.sqrt(length*length-dx*dx)); y=q2+(int)(unity[jj]*3*dy); 
 pendg.setColor(lightblue); pendg.fillRect(0,0,300,520); pendg.setColor(Color.black);
 pendg.fillRect(0,0,300,25); pendg.drawLine(150,25,x,y); 
 pendg.setColor(Color.red); pendg.fillOval(x-6,y-6,12,12);
  
graphim[jj].getGraphics().drawLine((int)(p2+(float)lasty[jj]*unity[jj]*3), (int)(q2+lasttt*3),(int)(p2+dx*unity[jj]*3) ,(int)(q2+(float)tt*3)); 

repaint(); try{roller.sleep(pauselength); } catch(InterruptedException ie){}; lasty[jj]=dx; lasttt=tt;
 } pendg.setColor(Color.black);pendg.drawString("x", p2+55, q2+460);pendg.drawString("L", p2+75, q2+340);
pause2=true; exerciseover=true;superimpose=false; 
repaint();try{synchronized(this){while(pause2==true) wait();  }} catch(InterruptedException ie){}; 
 }

 if(step==9){msgimg2=textGetLines(text[11],450);  ///speed from reference circle
 msgimg1=textDrawLines(600,40,"ENERGY of a particle in Simple Harmonic Motion (SHM)"); 
  scene=createImage(400,400); Graphics sgr= scene.getGraphics(); sgr.setFont(big);
 		int po=200; int qo=200; float A=150; float w=(float)Math.PI/4;
	for (int tt=0;tt<174; tt++) {x=(int)(A*Math.cos(w*tt/20));y=(int)(A*Math.sin(w*tt/20)); 
	sgr.setColor(lightblue); sgr.fillRect(0,0,400,400);
	sgr.setColor(Color.black); sgr.drawLine(po,qo,po+x,qo-y); sgr.drawString("\u03B8 = \u03C9t", po+x/2, qo-y/2); 
    sgr.drawOval(50,50,300,300); sgr.setColor(Color.red); sgr.fillOval(po+(int)x-12, qo-y-12, 24,24);
	  repaint(); try{roller.sleep(pauselength);   } catch(InterruptedException ie){};}
    sgr.setColor(lightblue); sgr.fillRect(0,0,400,400);
		sgr.setColor(Color.black);  sgr.drawOval(50,50,300,300);
	x= (int)(A*Math.cos(Math.PI/6)); y= (int)(A*Math.sin(Math.PI/6));  
   	float theta=(float)(2*Math.PI/3);drawVector(scene,po+x, qo-y, theta, (float)(100)); 
  	theta=(float)(Math.PI); drawVector(scene, po+x,qo-y, theta, (float)(100*Math.sin(Math.PI/6))); 
	sgr.drawString("v" ,po+x-15,qo-y-50); sgr.drawString("v sin(\u03C9t)" ,po+x-100, qo-y-5);
	sgr.drawLine(po,qo,po+x,qo-y); sgr.drawString("\u03B8 = \u03C9t", po+x/2, qo-y/2);
 sgr.setColor(Color.red); sgr.fillOval(po+x-12, qo-y-12, 24,24);
pause2=true; 
repaint();try{synchronized(this){while(pause2==true) wait();  }} catch(InterruptedException ie){}; 
step=10;	}
 if(step==10){
 msgimg2=textGetLines(text[12], 600);
 msgimg1=textDrawLines(300,40,"POTENTIAL ENERGY"); 
  scene=createImage(300,300); Graphics sgr= scene.getGraphics();
 sgr.setColor(lightblue); sgr.fillRect(0,0,300,300);
		int po=150; int qo=280; int k=8, A=5;
   float lastEp= 100, lastEk=0, lastxx=-A;
	sgr.setColor(Color.black); sgr.setFont(big); sgr.drawLine(2,qo,298,qo); sgr.drawLine(po,2,po,298); 
  // sgr.drawLine(po-25*A,qo,po+25*A,qo); sgr.drawLine(po,qo+4,po,qo-(int)(lastEp*2));
	sgr.drawLine(po-25*A,qo-8,po-25*A,qo+8); sgr.drawLine(po+25*A,qo-8,po+25*A,qo+8);
	sgr.drawString("-A", po-25*A,qo+18);sgr.drawString("0", po,qo+18);sgr.drawString("A", po+25*A,qo+18);
	sgr.drawString("kA²/2", po+5,qo-1-(int)(lastEp*2)); ///verticalaxis
    for (float xx=-A; xx<=A; xx+=(float)0.08)
	 {Ep=k*xx*xx/2; sgr.setColor(Color.black);sgr.drawLine(po+(int)(25*lastxx),(int)(qo-lastEp*2), (int)(po+xx*25), (int)(qo-Ep*2)); 
	  Ek=100-Ep; sgr.setColor(Color.red); sgr.drawLine(po+(int)(lastxx*25),(int)(qo-lastEk*2), (int)(po+xx*25), (int)(qo-Ek*2));
	   lastEp=Ep; lastEk=Ek; lastxx=xx;
	 }sgr.setColor(Color.black);sgr.drawLine(po-9,qo-(int)(lastEp*2),po+9,qo-(int)(lastEp*2)); 
	
	 pause2=true; 
repaint();try{synchronized(this){while(pause2==true) wait();  }} catch(InterruptedException ie){}; 
	}
	if(step==11){msgimg1=textDrawLines(300,30,"ENERGY of a  SPRING");  /////////////animation + graphics of spring
 msgimg2=createImage(150, 300); Graphics msg2=msgimg2.getGraphics();
   msg2.setFont(textfont); msg2.setColor(lightyellow); msg2.fillRect(0,0,300,300);
   msg2.setColor(Color.black); float dx=0; float v=0;
   float Ek=0; float Ep=0;float mass=(float)(0.5); int springcst=8;
 tt=0; exerciseover=false; pause1=true; int p2=150;int q2=25;
 float w= (float)(Math.sqrt(springcst/mass));  float T=(float)(2*(Math.sqrt(mass/springcst))*Math.PI);
for(jj=3; jj<=5; jj++)
{ graphim[jj]= createImage(dwidth[jj],dheight[jj]);drawGraphpaper(graphim[jj]);
  setAxis(graphim[jj], divy[jj],unity[jj]);
  }
  jj=1;  graphim[jj]= createImage(dwidth[jj],dheight[jj]); drawGraphpaper(graphim[jj]);
	setVerticalAxis(1);
 ampl=(float)0.5; dx=ampl; /// calculated displacement at tt=0	
float Epo=springcst*ampl*ampl/2; 
springwidth=280-(int)(ampl*unity[1]*3); x=springwidth; ////displacement on the  pict
lasty=new float[6];lasttt=0;//lasty stores last value of  x ,Ek,Ep
 lasty[1]=dx; lasty[5]=Epo; lasty[4]=0; lasty[3]=0;  
repaint();try{synchronized(this){while(pause1==true) wait();}  } catch(InterruptedException ie){};
for(tt=0;tt<120;tt++){dx=(float)(ampl*(Math.sin(w*(float)tt/20-Math.PI/2)));springwidth=(int)(dx*unity[1]*3)+280; 
 x=(int)(springwidth); v=(float)(ampl*w*(Math.cos(w*(float)tt/20-Math.PI/2)));Ep=springcst*dx*dx/2; 
 Ek=(float)(mass*ampl*ampl*w*w*(Math.cos(w*(float)tt/20-(Math.PI)/2))*(Math.cos(w*(float)tt/20-(Math.PI)/2)))/2;
// if (superimpose==true){addGreenPoint(graphim[9],10, Ep);}
 addPoint(graphim[3], 3, v);
 addPoint(graphim[4], 4, Ek);addPoint(graphim[5], 5, Ep);  
 graphim[1].getGraphics().drawLine((int)(p2+(float)lasty[1]*unity[1]*3), (int)(q2+lasttt*3),(int)(p2+dx*unity[1]*3) ,(int)(q2+(float)tt*3)); 
 msg2.setColor(lightyellow); msg2.fillRect(0,0,150,300);
 msg2.setColor(Color.black);
  msg2.drawString("time(sec):  "+(float)tt/20, 5,30); msg2.drawString("period T: "+T, 5,60); msg2.drawString("  sec", 5,80);    msg2.drawString("Ep(mJ) :  "+(Ep*1000), 5,230);	
 msg2.drawString("displacemt:  "+(dx*100), 5,120);msg2.drawString("   (cm) ", 5,140); msg2.drawString("v(cm/sec): "+v*100, 5,170);  
 msg2.drawString("Ek(mJ) : "+Ek*1000, 5,200);   msg2.drawString("Ep+Ek :  "+((Ek+Ep)*1000), 5,260);
   repaint(); try{synchronized(this){while(pause1==true) wait();}lasttt=tt; lasty[1]=dx;lasty[3]=v; lasty[4]=Ek; lasty[5]=Ep; roller.sleep(pauselength);   } catch(InterruptedException ie){};
	}
pause2=true;exerciseover=true; repaint(); try{ synchronized(this){while(pause2==true) wait();}} catch(InterruptedException ie){};
  pause1=false;}
  
if(step==12){msgimg1=textDrawLines(300,30,"SIMPLE PENDULUM"); /////////////animation + graphics of pendulum
 msgimg2=createImage(195, 300); Graphics msg2=msgimg2.getGraphics();
   msg2.setFont(textfont);  msg2.setColor(lightyellow); msg2.fillRect(0,0,300,300);
   msg2.setColor(Color.black); 
  float length=1; float g= (float)(9.81); float Ek=0; float Ep=0;float mass=(float)(0.5); float v=0;
 tt=0; ampl=(float)0.2; exerciseover=false; pause1=true; 
 float T=(float)(2*(Math.sqrt(length/g))*Math.PI);
 float w= (float)(Math.sqrt(g/length)); 
  for(jj=6; jj<=8; jj++){graphim[jj]= createImage(dwidth[jj],dheight[jj]);  
	drawGraphpaper(graphim[jj]); setAxis(graphim[jj], divy[jj],unity[jj]); }
     jj=2;      ////////////horizontal displacement graph/
	 graphim[jj]= createImage(dwidth[jj],dheight[jj]); 
     drawGraphpaper(graphim[jj]); 	 setVerticalAxis(jj);
	
float  dx=ampl;float dy=(float)(Math.sqrt(length*length-ampl*ampl));///d and dy calculated displacement at tt=0	
  float Epo=mass*g*(length-dy);///moving bob
 pendulumimg=createImage(300,520); Graphics pendg=pendulumimg.getGraphics();
 int p2=dwidth[2]/2, q2=25; x=p2+(int)(480*dx);  y=q2+(int)(480*dy);
 pendg.setColor(lightblue); pendg.fillRect(0,0,300,520); pendg.setColor(Color.black);
 pendg.drawLine(150,25,x,y);pendg.setColor(Color.red); pendg.fillOval(x-6,y-6,12,12);
lasty=new float[10];lasttt=0;//lasty stores last value of height, x ,Ek,Ep
lasty[2]=dx; lasty[6]=0; lasty[7]=0;lasty[9]=length-dy;lasty[8]=mass*g*lasty[9]; 
repaint();try{synchronized(this){while(pause1==true) wait();}  } catch(InterruptedException ie){};
for(tt=0;tt<120;tt++){dx=(float)(ampl*(Math.sin((w*(float)tt/20)+Math.PI/2)));
	v=(float)(ampl*w*(Math.cos(w*(float)tt/20-Math.PI/2)));
 x=p2+(int)(dx*480);
 dy=(float)(Math.sqrt(length*length-dx*dx)); y=q2+(int)(480*dy); 
 pendg.setColor(lightblue); pendg.fillRect(0,0,300,520); pendg.setColor(Color.black);
 pendg.fillRect(0,0,300,25); pendg.drawLine(150,25,x,y);pendg.setColor(Color.red); pendg.fillOval(x-6,y-6,12,12);
 Ep=mass*g*(length-dy); Ek=Epo-Ep;//(float)(Math.sqrt(length*length-ampl*ampl))*mass*g-Ep;
//if(superimpose==true){addGreenPoint(graphim[8],7,Ek);}addGreenPoint(graphim[7], 6, dx);
  addPoint(graphim[7], 7, Ek); addPoint(graphim[8], 8, Ep); 
   addPoint(graphim[6], 6, v); 
 graphim[2].getGraphics().drawLine((int)(p2+(float)lasty[2]*unity[2]*3), (int)(q2+lasttt*3),(int)(p2+dx*unity[2]*3) ,(int)(q2+(float)tt*3)); 
 msg2.setColor(lightyellow); msg2.fillRect(0,0,195,300);
   msg2.setColor(Color.black);
  msg2.drawString("time(sec): "+(float)tt/20, 5,30); float hh=Math.round((length-dy)*10000); msg2.drawString("height(cm): "+(float)hh/100, 5,60);int ep=(int)(Ep*100000); msg2.drawString("Ep (mJ):  "+(float)ep/100, 5,150);	
 int ddx=(int)(dx*10000); msg2.drawString("displacemt(cm): "+(float)ddx/100, 5,90); int ek=(int)(Ek*100000); msg2.drawString("Ek(mJ):  "+(float)ek/100, 5,120);   msg2.drawString("Ek+Ep :  "+1000*(Ek+Ep), 5,200);
  repaint(); try{synchronized(this){while(pause1==true) wait();}lasttt=tt;lasty[2]=dx;lasty[6]=v;lasty[7]=Ek; lasty[8]=Ep;lasty[9]=length-dy;  roller.sleep(pauselength);   } catch(InterruptedException ie){};
}  pause2=true; exerciseover=true;superimpose=false; repaint();try{synchronized(this){while(pause2==true) wait();  }} catch(InterruptedException ie){}; 
 }
 if(step==13){msgimg1=textDrawLines(350,120,"END OF THE DEMONSTRATION . . Program written by annemarie.govindraj@gmail.com");
 repaint(); }	
//isrunning=false;	
}}


synchronized public void mouseClicked(MouseEvent me)
 { int coX=me.getX(); int coY=me.getY(); 
  switch(step) 
   {case -1: case 0 : case 1 : case 7 : case 9 : case 10 :
          if((coY>height-50)&&(coX>390)&&(coX<510))
                    { step++; pause2=false;notify();} break;
	
    case 4: if((coX>190)&&(coX<410))
                    { if ((tt==0)&&(coY>height-50)) {pause1=false; notify();} 
					  else if(exerciseover=true)
						{if(coY>height-50)   {step++ ; pause1=false; pause2=false;  notify();} ////pressed NEXT ////
			             else if(coY>height-110) {pause1=false; pause2=false; notify();}    ////pressed SAME EXPERIM
					}
                    } break;
	case 5:	case 8 :if((coX>500)&&(coX<610)&&(coY>height-190))
           { if (pause1==false)     { pause1=true; notify(); }   ////pressed STOP
                                   else if (pause1==true) { pause1=false; notify(); }  }         ////pressed RESUME
     	   else if ((coX>520)&&(coX<710))
		       {if(coY>height-50)   {step++ ; pause1=false; pause2=false; notify();} ////pressed NEXT ////
			else if(coY>height-110) {pause1=false; pause2=false;notify();}    ////pressed SAME EXPERIM
	       }break;	
 			            
	case  2 : if((coX>330)&&(coX<510))
	     {  if((coY>height-50)&&(exerciseover=true))
		        {step++ ; pause1=false; pause2=false;  notify();} ////pressed NEXT ////
			else if(coY>height-110) {pause1=false; pause2=false; notify();}    ////pressed SAME EXPERIM
            }
	    break;
		case  11 : case 12 : if(coY>height-190)
        { if((coX>400)&&(coX<510))
           { if (pause1==false)     { pause1=true; notify(); }   ////pressed STOP
             else if (pause1==true) { pause1=false; notify(); }           ////pressed RESUME
     	   } 
	     else if ((coX>190)&&(coX<400)&&(exerciseover=true))
		   {if(coY>height-50)   {step++ ; pause1=false; pause2=false; superimpose=false; notify();} ////pressed NEXT ////
			else if(coY>height-110) {pause1=false; pause2=false;superimpose=false; notify();}    ////pressed SAME EXPERIM
            //else  { superimpose=true; pause1=false; pause2=false;notify();}
			}  
		}	break; 
	
 /*  case 5 :  if((coX>width-360)&&(coX<width-240)){  if(coY>height-50)
           { if (pause1==false)     { pause1=true; notify(); }   ////pressed STOP
             else if (pause1==true) { pause1=false; notify(); }           ////pressed RESUME
     	   } }
	     else if ((coX>width-230)&&(exerciseover=true))
		   {if(coY>height-50)   {step++ ;pause1=false;  pause2=false;superimpose=false; notify();} ////pressed NEXT /////////
			else if(coY>height-110) {pause1=false;pause2=false;superimpose=false;notify();}    ////pressed SAME EXPERIM
            else if(coY>height-170) { superimpose=true;pause1=false; pause2=false;notify();}} 
			break;*/
	 
	default: break;
}}
public void mouseEntered(MouseEvent me){}
public void mouseExited(MouseEvent me){}
public void mouseReleased(MouseEvent me){}
public void mousePressed(MouseEvent me){}



	 
	 public Image textDrawLines( int widthRect, int heightRect, String s)
{ Image im=createImage(widthRect, heightRect); Graphics msg=im.getGraphics();
   msg.setFont(textfont);  fm=msg.getFontMetrics();  
   msg.setColor(lightyellow); msg.fillRect(0,0,widthRect, heightRect);
   msg.setColor(Color.black);
   int wl=0;  wl=fm.stringWidth(s); 
        int linel=0; int lineheight=fm.getHeight();int y=lineheight+2;
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
 
 

public void drawVector(Image im, int Xo, int Yo,float angle , float dis)
{ Graphics imgr= im.getGraphics(); imgr.setColor(Color.black);
 int xp=Xo +(int)(dis*Math.cos(angle)); int yp=Yo - (int)(dis*Math.sin(angle));
  imgr.drawLine(Xo,Yo, xp,yp); //int []triangleX= new int [3]; int []triangleY= new int [3];
   if((angle>=0.15*Math.PI)&&(angle<0.25*Math.PI))  //45*
    { int[] triangleX={xp-3, xp+3,xp+6}; int[] triangleY={yp-3,yp+3,yp-8};
      imgr.fillPolygon(triangleX,triangleY,3); } 
	 else if ((angle>=0.25*Math.PI)&&(angle<0.38*Math.PI))   //<90*
    { int[] triangleX={xp-4, xp+4,xp+4}; int[] triangleY={yp-3,yp+3,yp-6};
      imgr.fillPolygon(triangleX,triangleY,3); }  
     else if ((angle>=0.38*Math.PI)&&(angle<0.62*Math.PI))   //90*
    { int[] triangleX={xp-4, xp,xp+4}; int[] triangleY={yp,yp-8,yp};
      imgr.fillPolygon(triangleX,triangleY,3); }  
     else if ((angle>=0.62*Math.PI)&&(angle<0.74*Math.PI))   //135*
    { int[] triangleX={xp-3, xp+3,xp-6}; int[] triangleY={yp+3,yp-3,yp-6};
      imgr.fillPolygon(triangleX,triangleY,3); }
	 else if ((angle>=0.74*Math.PI)&&(angle<0.88*Math.PI))   //>135*
    { int[] triangleX={xp-2, xp+2,xp-7}; int[] triangleY={yp+4,yp-4,yp-4};
    imgr.fillPolygon(triangleX,triangleY,3); }
	else if ((angle>=0.88*Math.PI)&&(angle<1.12*Math.PI))  //180*
    { int[] triangleX={xp, xp,xp-8}; int[] triangleY={yp+4,yp-4,yp};
      imgr.fillPolygon(triangleX,triangleY,3); }
     else if ((angle>=1.12*Math.PI)&&(angle<1.26*Math.PI))   //>180 <225
    { int[] triangleX={xp+2, xp-2,xp-7}; int[] triangleY={yp+4,yp-4,yp+6};
      imgr.fillPolygon(triangleX,triangleY,3); }  
	else if ((angle>=1.26*Math.PI)&&(angle<1.38*Math.PI))   //225*
    { int[] triangleX={xp-3, xp+3,xp-6}; int[] triangleY={yp-3,yp+3,yp+6};
      imgr.fillPolygon(triangleX,triangleY,3); }
	else if ((angle>=1.38*Math.PI)&&(angle<1.62*Math.PI))   //270*
    { int[] triangleX={xp-4, xp,xp+4}; int[] triangleY={yp,yp+8,yp};
      imgr.fillPolygon(triangleX,triangleY,3); }  
	else if((angle>=1.62*Math.PI)&&(angle<1.88*Math.PI))  //315*
    { int[] triangleX={xp-3, xp+3,xp+6}; int[] triangleY={yp+3,yp-3,yp+6};
      imgr.fillPolygon(triangleX,triangleY,3); } 
    else if ((angle>=1.88*Math.PI)||(angle<0.12*Math.PI))
    { int[] triangleX={xp, xp,xp+8}; int[] triangleY={yp-4,yp+4,yp};
      imgr.fillPolygon(triangleX,triangleY,3); }
	}
	
 

public void drawGraphpaper(Image im)
{   Graphics pg= im.getGraphics();   pg.setColor(Color.white); pg.fillRect(0,0,dwidth[jj],dheight[jj]);
	  pg.setColor(Color.orange);
       for(int i=0; i<dwidth[jj]; i+=3)   {pg.drawLine(i,0,i,dheight[jj]);}
      for(int j=0; j<dheight[jj]; j+=3)   {pg.drawLine(0,j, dwidth[jj],j);}
  }
 public void setAxis(Image im, float divy, double uny)
  {  Graphics pg= im.getGraphics(); pg.setFont(new Font("Dialog", Font.PLAIN, 20));  // x=tt
    int divx= 1; int unitx=20;int qo=dheight[jj]-20; if((jj==0)||(jj==3)||(jj==6)){ qo=100;} int po=20;
       pg.setColor(Color.black); pg.drawLine( po, qo, dwidth[jj]-1, qo); pg.drawLine(po,2, po, dheight[jj]-2);
      for(int p=0, i=0; p<dwidth[jj]-po; p+=3*(divx*unitx), i++){pg.drawLine(p+po, qo,p+po, qo+4);
               pg.drawString(""+i*divx, p+po-4,qo+18);}  //1 sec = 10 [] =40 pix 
      pg.drawString("Time (sec)", dwidth[jj]-200 , qo-2); pg.drawString(""+variab[jj], 20, 16); 
      if(uny!=0){  for(int q=qo, i=0; q>0; q-=(float)(divy*uny*3),  i++)
       {pg.drawLine(po-3, (int)(qo-(float)(3*i*divy*uny)), po, (int)(qo-3*i*(divy*uny)));/// (int)(i*q));	   
	   if(i%2==0){pg.drawString(""+(i*divy), 1, (int)(qo-3*i*(divy*uny)));}}}
  }
  public void setVerticalAxis(int jj)
  {Graphics pg= graphim[jj].getGraphics();int po=dwidth[jj]/2; int qo=25; int unitx=20; float divx=1; 
 pg.setFont(new Font("Dialog", Font.PLAIN, 20)); 
 pg.setColor(Color.black); pg.drawLine( 1, qo, dwidth[jj]-1, qo); 
 pg.drawLine(po,qo,po,dheight[jj]-2);
      for(int p=0, i=0; p<(dwidth[jj]-2);i++, p+=(int)(unity[jj]*divy[jj]*3))  //should be unity*3/5
	  {pg.drawLine(po+p,qo ,po+p, qo-3);pg.drawLine(po-p,qo ,po-p, qo-3);
       pg.drawString(""+(divy[jj]*i), po+p-12, 20);if(p>0){pg.drawString("-"+(i*divy[jj]), po-p-12, 20);}  ;}//if(p>0) to avoid 2 zeroes 
      pg.drawString("Horizontal displacement", 5+dwidth[jj]/2, 40); pg.drawString("Time", 5+dwidth[jj]/2,dheight[jj]-26);   //axis labelling
      for(int q=qo, i=0; q<dheight[jj]; q+=divx*unitx*3, i++)
       {pg.drawLine(po+3, q, po, q); 
	    pg.drawString(""+i, po-15, q+4);} //  1 div= 1sec=60 pix =20[];
 }
public void addPoint (Image im, int jj, float nwy)
{Graphics pg= im.getGraphics();pg.setColor(Color.black); int divx= 1; int unitx=20;int po=18; int qo=dheight[jj]-15;if((jj==3)||(jj==6)){ qo=100;}
  pg.drawLine((int)(po+(float)(lasttt*unitx*3/20)), (int)(qo-lasty[jj]*3*unity[jj]),(int) (po+(float)(tt*unitx*3/20)), (int)(qo-nwy*unity[jj]*3)); 
  //lasty[jj]=nwy;
  }    
  
public Image textGetLines( String s, int widthRect) ///text with sub or superscripts 
{  int wl=0;  offS.setFont(textfont);  fm=offS.getFontMetrics();  
  int nblines=0; String[] mylines=new String[30];
        int linel=0;  int lineheight=fm.getHeight();
       int spacelength = fm.stringWidth(" ");
         StringTokenizer tt=new StringTokenizer(s+" ."); //  fullstop to find end of string
             String word=" ", linee="";   					//contains the text
   while(tt.hasMoreTokens())
	{ word= tt.nextToken();
	   if (word.equals("."))
	      {  mylines[nblines]= linee; linel=0; linee=""; nblines++;}   
	   else  {int oldlinel=linel;
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
         int nbsub=0, nbsup=0;  int Xsuperscript[]= new int[10];String strsuper[]=new String[10];  ///place and comtent of superscripts
		 int Xsubscript[]= new int[10];String strsub[]=new String[10];  ///place and content of subscript
		 int Xsublines[]= new int [20];String[] sublines=new String[20]; ///place and content of non sub or super-script
               int Xfrac[]= new int[10]; String strfrac[]=new String[10]; int nbfrac=0;int [] strfraclength=new int[5];
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
			strfraclength[nbfrac]= fm.stringWidth(strfrac[nbfrac]);
			index=index+1+leftstring.indexOf('!'); 
			//if(strfraclength>strsuperlength) {Xsublines[nbsup+nbsub+1]+=(strfraclength-strsuperlength);}
			 strfraclength[nbfrac]=Math.max(strfraclength[nbfrac], strsuperlength);  nbfrac++;
		}			 
	 startindex=index+1; nbsup++;
     sublines[nbsup+nbsub]=mylines[j].substring(startindex,wline);
    }            

  else if(mylines[j].charAt(index)=='%')
          { sublines[nbsup+nbsub]=mylines[j].substring(startindex, index);
            Xsubscript[nbsub]=Xsublines[nbsup+nbsub]+fm.stringWidth(sublines[nbsup+nbsub]);
			String leftstring=mylines[j].substring(index+1, wline);
            strsub[nbsub]= leftstring.substring(0,leftstring.indexOf('!'));
			Xsublines[nbsup+nbsub+1]=Xsubscript[nbsub]+fm.stringWidth(strsub[nbsub])+spacelength;
	        index=index+1+leftstring.indexOf('!');
			startindex=index+1; nbsub++;
            sublines[nbsup+nbsub]=mylines[j].substring(startindex,wline);
		}
  else{index++;} }//end of while
  
 if((nbsup>0)||(nbsub>0))
  {msg.setFont(textfont); for( int i=0; i<=(nbsub+nbsup);i++){msg.drawString(sublines[i],Xsublines[i],jx) ;}
	if(nbsub>0){msg.setFont(new Font("Dialog", Font.PLAIN,12)); for( int i=0; i<(nbsub);i++){msg.drawString(strsub[i],Xsubscript[i], jx+1);}
    msg.setFont(textfont); }  
	if(nbsup>0){ for( int i=0; i<(nbsup);i++){msg.drawString(strsuper[i],Xsuperscript[i], jx-6);}}
	if(nbfrac>0){for( int i=0; i< nbfrac;i++){ msg.drawLine(Xfrac[i],jx-3,Xfrac[i]+strfraclength[i],jx-3);
	    msg.drawString(strfrac[i],Xfrac[i], jx+fm.getHeight()-3);}
		jx=jx+fm.getHeight();}}
 else {	msg.drawString(mylines[j],4,jx) ;} 
 } //next line
CropImageFilter f=new CropImageFilter(0, 0,widthRect, jx+5);
 FilteredImageSource fis =new FilteredImageSource(im.getSource(), f);
 Image imm =createImage(fis);
  return imm; } 


public void update(Graphics g)////////////////////////////////////////////////
{ offS.setColor(green); offS.fillRect(0,0,width,height); 
offS.setColor(Color.black); //offS.drawString("step = "+step,20,20);
   switch (step)
   {			   
  case -1 :  offS.drawImage(msgimg1,450,2,this); offS.drawImage(msgimg2,450,100,this);
  offS.drawImage(scene,50,100,this);
 offS.setColor(bluegreen);offS.fillRect(390, height-50, 120 ,40);     //button next
    offS.setColor(Color.yellow); offS.drawString("NEXT",400,height-20);  break;
  case 0 :  offS.drawImage(msgimg2,50,300,this); offS.drawImage(scene, 50, 50,this) ; 
	offS.setColor(bluegreen); offS.fillRect(390, height-50,120,60); 
	offS.setColor(Color.yellow); offS.drawString("NEXT  ",400,height-20); 
	 break;
 case 1: offS.drawImage(msgimg1,50,210,this);offS.drawImage(msgimg2,50,540,this);
 offS.drawImage(scene, 50, 02,this) ;offS.drawImage(scene2, 50, 337,this) ;
	offS.setColor(bluegreen); offS.fillRect(390, height-50,120,60); 
	offS.setColor(Color.yellow); offS.drawString("NEXT  ",400,height-20); 
	 break;
 case 2 : offS.drawImage(msgimg1,width/2,50,this); offS.drawImage(scene, 50,50,this) ;
  if(firstmvt==false){offS.drawImage(msgimg2,width/2,210,this);}
  if(exerciseover==true){offS.setColor(bluegreen); offS.fillRect(390, height-50,120,40); offS.fillRect(320, height-110,200,40); 
	offS.setColor(Color.yellow); offS.drawString("NEXT  ",400,height-20); offS.drawString("SAME ANIMATION  ",330,height-80); }
	 break;
 case 4 : offS.drawImage(msgimg1, 50, 0,this); offS.drawImage(msgimg3, 50, 40,this);   offS.drawImage(msgimg2, 80, 260,this); 
offS.drawImage(springimg, x1, y1, springwidth, 122,this); 
offS.setColor(Color.red); offS.fillOval(x,y1-18,48,48);
offS.setColor(Color.black);offS.fillRect(0,0,50,300);offS.drawLine(354, 210, 280+50+24, 220);offS.drawString("equilibrium position", 300,250);
if(firstmvt==true){offS.drawImage(arrowimg, x+60, y1+33, this);}
if ((pause1==true)&&(tt==0)) { offS.setColor(bluegreen);offS.fillRect(190, height-50, 120 ,40);  
                                 offS.setColor(Color.yellow); offS.drawString("START ", 200,height-20);}
if(exerciseover==true){offS.setColor(bluegreen);offS.fillRect(190, height-50,200,40);offS.fillRect(190, height-110,200,40);
    offS.setColor(Color.yellow); offS.drawString("NEXT  ",240, height-20);
    offS.drawString("Same experiment ", 200, height-80);  }
 break;
  case 5 :  offS.drawImage(springimg, 0, y1,springwidth, 122,this); 
offS.setColor(Color.red); offS.fillOval(x,y1-18,48,48);
offS.setColor(Color.black);offS.drawLine(304,y1+ 120, 280+24, y1+130);offS.drawString("equilibrium position", 250,240);
offS.fillRect(0,0,50,300);
offS.drawImage(graphim[1], 154,250,this);
offS.drawImage(msgimg2, width/2, 5,this);
if (exerciseover==false)
	{offS.setColor(bluegreen); offS.fillRect(490, height-50,120,40);
	offS.setColor(Color.yellow);
	if(pause1==true){ if(tt==0) {offS.drawString("START ", 500,height-20);}
						else {offS.drawString("RESUME ",  500,height-20);}}
	else if (pause1==false) {offS.drawString("STOP ", 500,height-20);}}
 else if(exerciseover==true){offS.setColor(bluegreen); offS.fillRect(590, height-50,120,40); offS.fillRect(520, height-110,200,40); 
	offS.setColor(Color.yellow); offS.drawString("NEXT  ",600,height-20); offS.drawString("SAME ANIMATION  ",530,height-80); }
	
 	break;
 case 6: offS.drawImage(msgimg2,400,2,this); offS.drawImage(pendulumimg, x1,0,this);
	//if(exerciseover==true){offS.setColor(bluegreen); offS.fillRect(390, height-50,120,60); 
	//offS.setColor(Color.yellow); offS.drawString("NEXT  ",400,height-20); }
	 break;
case 7 : offS.drawImage(msgimg2,400,2,this); offS.drawImage(pendulumimg, x1,0,this);
	if(exerciseover==true){offS.setColor(bluegreen); offS.fillRect(390, height-50,120,60); 
	offS.setColor(Color.yellow); offS.drawString("NEXT  ",400,height-20); }
	 break; 
case 8 : offS.drawImage(msgimg2,400,2,this);  offS.drawImage(graphim[2], xo, 250, this);
CropImageFilter f=new CropImageFilter(0,300,300,220);
 FilteredImageSource fis =new FilteredImageSource(pendulumimg.getSource(), f);
  Image croppedimg=createImage(fis);
  offS.drawImage(croppedimg,xo,20,this);
if (exerciseover==false)
	{offS.setColor(bluegreen); offS.fillRect(490, height-50,120,40);
	offS.setColor(Color.yellow);
	if(pause1==true){ if(tt==0) {offS.drawString("START ", 500,height-20);}
						else {offS.drawString("RESUME ",  500,height-20);}}
	else if (pause1==false) {offS.drawString("STOP ", 500,height-20);}}
 else if(exerciseover==true){offS.setColor(bluegreen); offS.fillRect(590, height-50,120,40); offS.fillRect(520, height-110,200,40); 
	offS.setColor(Color.yellow); offS.drawString("NEXT  ",600,height-20); offS.drawString("SAME ANIMATION  ",530,height-80); }
	break;
case 9 : case 10: 	offS.drawImage(msgimg1,(int)(width/4),10,this); 
   if(step==9){offS.drawImage(msgimg2,width/2,80,this); }
   else if (step==10){offS.drawImage(msgimg2,width-620,80,this); }
       offS.drawImage(scene, 50,100,this) ;
    offS.setColor(bluegreen); offS.fillRect(390, height-50,120,60); 
	offS.setColor(Color.yellow); offS.drawString("NEXT  ",400,height-20); 
	 break; 
 case 11 : 
 offS.drawImage(springimg, 0, y1,springwidth, 122,this); 
offS.setColor(Color.red); offS.fillOval(x,y1-18,48,48);
offS.setColor(Color.black);offS.fillRect(0,0,50,300);offS.drawLine(304, 170, 280+24, 180);offS.drawString("equilibrium position", 300,200);
offS.fillRect(0,0,10,300);
offS.drawImage(graphim[1],154,200 ,this); offS.drawImage(graphim[3], 490,2,this);
offS.drawImage(graphim[4], 490, 212,this); offS.drawImage(graphim[5], 490, 422,this);
offS.drawImage(msgimg2, 2, 350,this);

offS.setColor(bluegreen); offS.fillRect(390, height-50,120,40);//offS.fillRect(40, height-110,120,40); 
offS.setColor(Color.yellow);
if (pause1==true){ if(tt==0) {offS.drawString("START ", 400,height-20);}

                    else {offS.drawString("RESUME ",  400,height-20);}}
 else if(pause1==false) {offS.drawString("STOP ", 400,height-20);}
 if(exerciseover==true){offS.setColor(bluegreen);offS.fillRect(190, height-50,200,40);offS.fillRect(190, height-110,200,40);//offS.fillRect(190, height-170,200,40);
    offS.setColor(Color.yellow); offS.drawString("NEXT  ",240, height-20);
    offS.drawString("Same experiment ", 200, height-80);  }//ffS.drawString("Superimpose", 200,height-140);}
	break;
	
case 12 :  offS.drawImage(msgimg2,2,350,this); 
 offS.drawImage(graphim[2], 200, 250, this); 
offS.drawImage(graphim[6], width/2,2,this);
offS.drawImage(graphim[7], width/2, 212,this);
offS.drawImage(graphim[8], width/2,422,this);
 f=new CropImageFilter(0, 320, 300, 200);
 fis =new FilteredImageSource(pendulumimg.getSource(), f);
croppedimg=createImage(fis); offS.drawImage(croppedimg,200,20,this); 
// offS.drawImage(pendulumimg,160,20,this); 

offS.setColor(bluegreen); offS.fillRect(390, height-50,120,40);//offS.fillRect(40, height-110,120,40); 
offS.setColor(Color.yellow);
if (pause1==true){ if(tt==0) {offS.drawString("START ", 400,height-20);}

                    else {offS.drawString("RESUME ",  400,height-20);}}
 else if(pause1==false) {offS.drawString("STOP ", 400,height-20);}
 if(exerciseover==true){offS.setColor(bluegreen);offS.fillRect(190, height-50,200,40);offS.fillRect(190, height-110,200,40);
    offS.setColor(Color.yellow); offS.drawString("NEXT  ",240, height-20);
    offS.drawString("Same experiment ", 200,height-80);}
break;
 case 13 :  offS.drawImage(msgimg1,200,150,this); 
default : break;
}
paint(g);
}
public void paint(Graphics g)
 {if(isrunning==true) 
  { g.drawImage(offScreenImage,0,0,this); }
 }}
  
 
