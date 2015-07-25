import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.awt.image.*;

public class Waves extends Applet implements Runnable , MouseListener , MouseMotionListener//, KeyListener  
{boolean isrunning=true, pause1=true , pause2=false, exerciseover=false, firstmvtover=false, morefun=false;
boolean select=false, select2=false; 
 int height, width, step=1,  pauselength=28, xo=50, yo=50, h=110,L=400,qo=90,po=25;
 int tt=0, x=0,y=0,x2=500, y2=100, x3=680,  exercise=0;
float [] forwcos; float[] phasecos; float[] cospix; 
double st= 0.062832; float delay=0; float phi=(float)1.57; int setdelay=0, setlambda=0; 
int Rgap, Ggap, Bgap;
Thread roller;  int lambda=100;
String text[]=new String [30];
Soundcrest [] r=new Soundcrest[250];
//////////////display
Color lightblue= new Color(0,255,255); Color green= new Color(78,214,173);Color watercol= new Color(47, 166, 187);
Color bluegreen= new Color(47, 166, 187); Color lightyellow= new Color(255,245,191);
Color greens[]={new Color(169,238,228),new Color(40,80,80)};

Image offScreenImage, msgimg1, msgimg2, forward, forward2, backward,backward2, waves,bouncewave;
Image slider,graphpaper90, graphpaper181, graphpaper360,graphpapersh, bigscene, carimg, girlimg; 
Image scene[];
Graphics offS; //Font thizfont;
Font big = new Font("Verdana", Font.PLAIN, 18); Font textfont= new Font("Dialog", Font.PLAIN, 18); FontMetrics fm; 

public void init()
{height=Integer.parseInt(getParameter("height"));
width=Integer.parseInt(getParameter("width"));
 step=Integer.parseInt(getParameter("step"));
setBackground(green); addMouseListener(this);addMouseMotionListener(this);
 offScreenImage=createImage(width, height); offS=offScreenImage.getGraphics();
 offS.setColor(green);offS.fillRect(0,0,width,height);offS.setFont(textfont);
Rgap=greens[0].getRed()-greens[1].getRed();
Ggap=greens[0].getGreen()-greens[1].getGreen();
Bgap=greens[0].getBlue()-greens[1].getBlue();
 slider=createImage(205,32); Graphics slgr=slider.getGraphics();slgr.setColor(lightyellow); slgr.fillRect(0,0,205,32);
 int [] arrowX={195,5,195}; int [] arrowY={8,16,24}; slgr.setColor(Color.red); slgr.fillPolygon(arrowX,arrowY,3);
 graphpaper90=createImage(400,90); graphpaper181=createImage(400,181);graphpaper360=createImage(400,360);
drawGraphpaper(graphpaper90,400,90); drawGraphpaper(graphpaper181, 400, 181);drawGraphpaper(graphpaper360,400,360);
 drawAxes(graphpaper90, 90, 100); drawAxes(graphpaper181, 181, 100);
 //thizfont=offS.getFont(); //offS.setFont(new Font (thizfont.getName(),Font.BOLD, 20));
text[1]="Suppose you give a sudden small snap up and down to a string, a pulse will travel along the cord. . Each bit of string brings into movement the next bit of string. ";
 text[2]="The pulse travels a distance x in a time = x/v where v is the velocity of the travelling pulse through the medium. So at point L, the transverse displacement is the same as at point 0, L/v seconds ago."; 
 text[3]="Suppose that instead of giving one snap, the source is an oscillating system, moving in Simple Harmonic Motion as y=Acos(\u03C9t+\u03C6). Again each vibrating particle brings into vibration it's neighbour,and this neighbour brings into vibration the next neighbour. We get a  sinusoidal wave travelling through the medium. . However due to the restoring force the particles can't move beyond a vibration, so there is no transfer of matter along with the wave."; 
 text[4]="To describe the displacement in time and space, we use y=Acos(\u03C9(t-x/v)+\u03C6) . The term (t-x/v) shows that at point x, the phase will be the one at the origin x/v seconds ago. Note that at a fixed x, the displacement varies with time as a sinusoidal curve(Follow the red point). At a fixed time, the displacement along x axis is also a sinusoidal curve. ";
text[5]="The WAVELENGTH is the distance in space between successive crests (or any 2 equivalent points) at a fixed time.  Their phase difference is 2\u03C0. . (\u03C9(t%1! - x%1!/v)+\u03C6) = (\u03C9(t%1!- x%2!/v)+\u03C6)+2\u03C0 . or (\u03C9x%2!-\u03C9x%1!)/v  =  2\u03C0 . . wavelength \u03BB = (x%2!-x%1!)  =  2\u03C0 $v!/\u03C9! . We can rewrite the equation for the wave as .  y=Acos(\u03C9t-kx+\u03C6) with k= $\u03C9!/v! = $2\u03C0!/\u03BB! . The PERIOD (T) is the distance in time at fixed x between 2 crests.";
text[6]="Exercise . What is the wavelength (\u03BB), angular frequency (\u03C9), frequency (\u0192) and period (T) of the wave described by y= 0.03 cos(10\u03C0 t - \u03C0 x). What is the propagation velocity (v%p!) of the wave? What are the maximal transversal displacement and  maximal transverse velocity (v%tmax!) of a particle of the medium?  " ;
 text[7]="Answer : . Compare with y=Acos(\u03C9t - kx) where k= $2\u03C0!/\u03BB!. . \u03C9 =10\u03C0 hence \u0192 = $\u03C9 !/2\u03C0! = 5 Hz and T= $1!/\u0192! = 0.2/sec. . \u03BB = $2 \u03C0!/k!= 2 m and v%p!= \u03BB \u0192=10 m/sec. This is the velocity of propagation of the wave and depends on the characteristics of the medium . Maximum displacement is when y is maximal i.e. when cos(\u03C9t - kx) =1, . y%max! = A =0.03 m , v%tmax! = maximum of dy/dt. dy/dt = -A\u03C9 sin(\u03C9t - kx) =>v%tmax!= A\u03C9 = 0.03x10\u03C0= 0.942 m. The transversal speed depends on the oscillating source. ";
 text[8]="The example of the string showed a transverse wave, where particles move to and fro perpendicularly to the propagation of the wave. . How does sound propagate in air? The disturbance (sound) pushes the molecules of air forward, creating a small temporary excess of pressure \u0394P, molecules of that zone of compression then move back towards zones of lower pressure. Zones of compression and rarefaction travel through the medium. . We can represent the waves as a succession of darker and brighter regions representing compressed air and rarefied air, respectively. .  We can also represent the wave as a sinusoidal curve. Maximal compression (marked C) occurs where molecules left to C have a positive displacement (i.e. pushing to the right), and molecules right of C have a negative displacement(i.e. pushing to the left). C has zero displacement. Opposite happens at R => rarefaction.";  
 text[9]="The dark/light shade representation is useful when you want a 2-dimensional view. . Here one source emits a wave in all directions";
 text[10]="Top 2 figures : 2 waves with same amplitude (A) and same frequency(\u0192) but different phase constants (\u03C6 ). . Lower figure : Resultant wave : each particle of the medium undergoes a displacement which is the sum of the displacements due to each wave separately. .  Try different phase constants, and note when the resultant wave is a flat curve, or when the amplitude becomes maximum. . We get constructive interference when crests add up with crests and troughs with troughs. We get destructive interference when crests add up with troughs";
 text[11]="Let the equations of the 2 waves be . y%1! = A sin(\u03C9t-kx)  and  y%2! =  A sin(\u03C9t-kx+\u03C6) . The resultant wave is given by y=A sin(\u03C9t-kx)+A sin(\u03C9t-kx+\u03C6)=  .  A2 sin$(\u03C9t-kx+\u03C9t-kx+\u03C6)!/2! cos($\u03C9t-kx-\u03C9t+kx+\u03C6 !/2!) . rewritten as : y= 2A cos($\u03C6!/2!) sin(\u03C9 t-kx+$\u03C6!/2!) , it shows the resultant wave has same angular frequency \u03C9 and same wavelength (k=$2\u03C0!/\u03BB!) as the original waves, the amplitude of the resultant : 2A cos($\u03C6 !/2!) depends on the phase difference , maximum for \u03C6 =0 or n \u03C0, and zero for \u03C6 =$\u03C0!/2!";
 text[12]="The principle of superposition also applies if identical waves are send out from different sources. For this animation, the computer makes the sum of the displacements for each point of the medium, every 30 millisecond ! Enjoy !   ";
 text[13]="What happens when two pulses travel in opposite directions in the same medium? . If both pulses have an upward displacement, at the point where they pass through each other, we see a constructive interference i.e. sum is higher than each pulse . If one pulse has an upward displacement, the other a downward displacement, at the point where they pass through each other, we see a destructive interference . This \'destruction\' is not at all like a collision. It's a temporary phenomenon. The waves go on unchanged , same amplitude, wavelength, speed";
 text[14]="If we're not concerned about boundaries, we take : . wave from left to right :  y%1!=Asin(\u03C9t-kx)  .  wave from right to left : y%2!=Asin(\u03C9t+kx) . y%1!+y%2!=2Asin$(\u03C9t-kx+\u03C9t+kx)!/2! cos$(\u03C9t-kx-\u03C9t-kx)!/2! . = 2Asin(\u03C9t)cos(-kx) . The term cos(kx) does not vary with time. We get some points (=nodes) where displacement is always =0. For the other points the amplitude of displacement varies with time, without any lateral movement. These waves are \'standing\' waves. Points where the amplitude's magnitude is highest are antinodes. Distance between adjacent nodes ( 2Acos(kx)=0) : kx=\u03C0 => x= $\u03BB!/2! "; 
 text[15]="The phenomenon of standing waves is important because it happens when waves are reflected by a boundary. Let's start with reflection of a pulse. . When a crest reaches the end of the medium, the last particle of the medium  receives an upward displacement and tries to pull the first particle of the wall upwards. The first particle of the wall pulls downwards on the last particle of the medium ( Newton's third law ), and this downward pull will be transmitted to the neighbours in the medium. The pulse gets inverted. Inversion of the pulse only happens if the first particle of the wall is not allowed to move i.e. for a fixed end boundary.";
 text[16]="Interference created by 2 small wavetrains of 2 \u03BB emitted at time =0 and after the wave has reached 8\u03BB. The left panels follow only 1 wavetrain. The panel on the right gives the real picture with interference. You can see the standing wave just after first reflection. After the second reflection, look at the phase of the waves going from left to rigth. Depending on the position of the wall, the twice reflected wave  has same phase (L=400 m) or is  1 \u03C0 ahead(L=375 m, 50 m ahead) of the emitted wave. ";
 text[17]="Compare the phase of the wave after 2 reflections to the newly emitted wave, and the thrice-reflected wave to the once reflected wave. They add up (if L=400 m or L=350 m) or cancel each other out(if L=375 m). Same will hold true if hundreds of reflections are superimposed. When the length of the string is just right to make the different reflections add up, we talk of resonance." ;
 text[18]="For this figure the boundary is at L. We have y%1!=Asin(\u03C9(t-$x!/v!)) and y%2!=Asin(\u03C9(t-$L-x!/v!)). Adding  y%1!+ y%2!, we find the position of the nodes : $L!/2! + $\u03BB!/4!, $L!/2! - $\u03BB!/4!, $L!/2!+ $3\u03BB!/4! , etc.";// Check it out. ";
 text[19]="If for a given wavelength, interference depends on length(L) of the resonance box, can we find which wavelength gives maximum resonance for a given length? ";
 text[20]="For a string fixed at both ends, we have resonance in the following situations: .  \u03BB = 2L= $2!/1! L First harmonic .  \u03BB = L = $2!/2! L  Second harmonic .  \u03BB = $2!/3! L Third harmonic . \u03BB = $2!/4! L Fourth harmonic .  \u03BB = $2!/5! L Fifth harmonic .  \u03BB = $2!/6! L Sixth harmonic . etc";
 text[21]="You can try different combinations . Try to find a pattern.";
 text[29]="END OF THE DEMONSTRATION . Program written by annemarie.govindraj@gmail.com";
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
 {  if (step==1)
	{exerciseover=false; firstmvtover=false; msgimg1=textDrawLines(450,150,text[1]);
     	po=25; qo=55; float A=40; L=400;         // float w=(float)Math.PI;
   scene=new Image[2]; Graphics sgr[]= new Graphics[2];
 for(int j=0;j<2;j++)
  {scene[j]=createImage(L,100);  sgr[j]=scene[j].getGraphics(); 
   sgr[j].setColor(lightblue); sgr[j].fillRect(0,0,L,100);sgr[j].setFont(big);
   sgr[j].setColor(Color.black); sgr[j].drawLine(0,qo-1,po,qo-1);sgr[j].drawLine(0,qo,L,qo);sgr[j].fillRect(L-5,0,5,100);}
   //drawVector(scene[j],po, qo, (float)Math.PI/2,A);}
  repaint(); try{roller.sleep(500);   } catch(InterruptedException ie){};
forwcos=new float[L+1];   for(int ii=0; ii<=L;ii++) {forwcos[ii]=0;}
int j=0; for(int tt=0; tt<L; tt++){ fillpulsepix(tt,  forwcos ); ///////////pulse left to right
 	 sgr[j].setColor(lightblue); sgr[j].fillRect(0,0,L,100);
     sgr[j].setColor(Color.black); sgr[j].drawLine(0,qo-1,po,qo-1);//sgr[j].drawLine(0,qo,L,qo);
	 sgr[j].drawLine(po,1,po,99);sgr[j].drawString("x",L-20,qo-5);sgr[j].drawString("y",po-15,20);
     sgr[j].drawLine(0,qo,po, qo-Math.round(A*forwcos[0]));  sgr[j].drawLine(0,qo-1,po, qo-1-Math.round(A*forwcos[0]));//drawVector(scene[0],po, qo-(int)A, 3*(float)Math.PI/2,A);
  for(int xx=0; xx<L; xx++) {sgr[j].drawLine(po+xx,qo-Math.round(A*forwcos[xx]),po+xx+1,qo-1-Math.round(A*forwcos[xx+1]));}
	  repaint(); try{roller.sleep(pauselength/2);   } catch(InterruptedException ie){};}
	 firstmvtover=true;  msgimg2=textDrawLines(450,150,text[2]);
 j=0; while(j<2)
 {  scene[j]=createImage(L,100); sgr[j]=scene[j].getGraphics(); sgr[j].setFont(big);
   sgr[j].setColor(lightblue); sgr[j].fillRect(0,0,L,100);
   sgr[j].setColor(Color.black); sgr[j].drawLine(0,qo-1,po,qo-1);sgr[j].drawLine(0,qo,L,qo);
forwcos=new float[L+1];   for(int ii=0; ii<=L;ii++) {forwcos[ii]=0;}
for(int tt=0; tt<L; tt++){ fillpulsepix(tt,  forwcos ); ///////////pulse left to right
 	 sgr[j].setColor(lightblue); sgr[j].fillRect(0,0,L,100);
     sgr[j].setColor(Color.black); sgr[j].fillRect(L-5,0,5,100);
   sgr[j].drawLine(0,qo,po, qo-Math.round(A*forwcos[0]));  sgr[j].drawLine(0,qo-1,po, qo-1-Math.round(A*forwcos[0]));//drawVector(scene[0],po, qo-(int)A, 3*(float)Math.PI/2,A);
   //sgr[j].drawLine(0,qo,L,qo);
	 sgr[j].drawLine(po,1,po,99);sgr[j].drawString("x",L-20,qo-5);sgr[j].drawString("y",po-15,20);
	 for(int xx=0; xx<L; xx++) {sgr[j].drawLine(po+xx,qo-Math.round(A*forwcos[xx]),po+xx+1,qo-1-Math.round(A*forwcos[xx+1]));}
	  repaint(); try{roller.sleep(pauselength/2);   } catch(InterruptedException ie){};
 if((j==0)&&(tt>=25)){j=1;}; //16*Math.PI
 if((j==1)&&(tt>=125)){j=2; sgr[1].setColor(Color.red);sgr[1].drawLine(po,qo-(int)A-5,po+100, qo-(int)A-5);
 sgr[1].setFont(big);sgr[1].drawString("x = vt",po+10,qo-(int)A+9);
 sgr[1].drawLine(po+100,qo-5,po+100, qo); sgr[1].drawString("L",po+95,qo+15);
 break;}}}
 exerciseover=true; repaint(); pause2=true;
try{ synchronized(this){while(pause2==true) wait();}} catch(InterruptedException ie){};
step=2;}
if(step==2){exerciseover=false;  msgimg1=textGetLines(text[3],550);
 firstmvtover=false;  L=400;  h=110; qo=50; forward=createImage(L,h+1); Graphics forgr=forward.getGraphics();forgr.setFont(big);
 forwcos=new float[L+1]; for(int ii=0; ii<=L;ii++) {forwcos[ii]=0; }
 for (int t=0; t<L;t++)  
  {fillpix(t,3*(float) Math.PI/2, forwcos ); ///////////wave left to right
	forgr.setColor(lightblue); forgr.fillRect(0,0,400, h+1);
    forgr.setColor(Color.black); forgr.drawLine(L,0,L,h);forgr.drawLine(0,0,0,h);
	forgr.drawLine(0,qo,L,qo);
	 forgr.drawLine(po,1,po,h);forgr.drawString("x",L-20,qo-5);forgr.drawString("y",po-15,20);
     forgr.drawLine(0,qo,po, qo-Math.round(45*forwcos[0]));  forgr.drawLine(0,qo-1,po, qo-1-Math.round(45*forwcos[0]));//drawVector(scene[0],po, qo-(int)A, 3*(float)Math.PI/2,A);
 for(int ii=0; ii<L;ii++)
		{forgr.drawLine(po+ii, 50-(int)Math.round(44*forwcos[ii]),po+ii+1,50-(int)Math.round(45*forwcos[ii+1]));}
	repaint(); try{roller.sleep(pauselength/2);   } catch(InterruptedException ie){};
	}firstmvtover=true; forward2=createImage(L,h+1);  forward2.getGraphics().drawImage(forward,0,0,this); 
	for(int ii=0; ii<=L;ii++) {forwcos[ii]=0; } 
	forgr.setColor(lightblue); forgr.fillRect(0,0,400, h+1); msgimg2=textGetLines(text[4],550);repaint();
   for (int t=0; t<L;t++)  
  {fillpix(t,3*(float) Math.PI/2, forwcos ); ///////////wave left to right
	forgr.setColor(lightblue); forgr.fillRect(0,0,400, h+1);
     forgr.setColor(Color.red); if(forwcos[101]>=forwcos[100])
	{ forgr.fillOval(po+100-4, 50-8-(int)Math.round(45*forwcos[100]), 8,8);}
	else { forgr.fillOval(po+100-4, 50-8-(int)Math.round(45*forwcos[100]), 8,8);}
	 forgr.setColor(Color.black); forgr.drawLine(L,0,L,h);forgr.drawLine(0,0,0,h);forgr.drawLine(po+100,0,po+100,h);
	forgr.drawLine(0,qo,L,qo);
	 forgr.drawLine(po,1,po,h);forgr.drawString("x",L-20,qo-5);forgr.drawString("y",po-15,20);
     forgr.drawLine(0,qo,po, qo-Math.round(45*forwcos[0]));  forgr.drawLine(0,qo-1,po, qo-1-Math.round(45*forwcos[0]));//drawVector(scene[0],po, qo-(int)A, 3*(float)Math.PI/2,A);
 for(int ii=0; ii<L;ii++)
		{forgr.drawLine(po+ii, 50-(int)Math.round(44*forwcos[ii]),po+ii+1,50-(int)Math.round(45*forwcos[ii+1]));}
	repaint(); try{roller.sleep(pauselength/2);   } catch(InterruptedException ie){};
}
 exerciseover=true; pause2=true; repaint();
try{ synchronized(this){while(pause2==true) wait();}} catch(InterruptedException ie){};
}
if(step==3){ msgimg2=textGetLines(text[5],700); Graphics for2gr=forward2.getGraphics(); for2gr.setColor(Color.red);
for2gr.drawLine(po+75, 15, po+175, 15); for2gr.setFont(big); for2gr.drawString("\u03BB", po+125, 14);
 pause2=true; repaint();
try{ synchronized(this){while(pause2==true) wait();}} catch(InterruptedException ie){};
}
if(step==4){firstmvtover=false;  msgimg1=textGetLines(text[6],750); msgimg2=textGetLines(text[7],750);
 pause2=true; pause1=true; repaint();
try{ synchronized(this){while(pause1==true) wait();}} catch(InterruptedException ie){};
firstmvtover=true;  repaint(); 
try{ synchronized(this){while(pause2==true) wait();}} catch(InterruptedException ie){};
}
if(step==5){exerciseover=false;firstmvtover=false;
 msgimg2=textDrawLines(250,40,"LONGITUDINAL WAVES"); msgimg1=textGetLines(text[8],530);
	L=400;  h=110; float st=(float)0.08; int pixsize=L; 
	cospix=new float[pixsize+1]; forwcos=new float[pixsize+1];int[] pixels= new int[L]; 
	int red=0, green=0,blue=0;
	forward=createImage(L,h+1); Graphics forgr=forward.getGraphics();forgr.setFont(big);
	for(int ii=0; ii<=L;ii++) {cospix[ii]=0; }
	forgr.setColor(lightblue); forgr.fillRect(0,0,L, h+1);forgr.setColor(Color.black);
 
waves=createImage(L,h); Graphics  wgr=waves.getGraphics(); wgr.setColor(watercol); wgr.fillRect(0,0,L,h);

for (int t=0; t<pixsize-1;t++)
 {	fillpix(t, (float) Math.PI/2, cospix); 
    fillpix(t,0, forwcos); 
	forgr.setColor(lightblue); forgr.fillRect(0,0,L, h+1);forgr.setColor(Color.black);forgr.drawLine(0,h/2,L,h/2);
	
	for(int i=0; i<L; i++)
		{ forgr.drawLine(i, 55+(int)Math.round(44*(forwcos[i])),i+1,55+(int)Math.round(45*(forwcos[i+1])));
		red = 0xff&(int)(Rgap*(1+cospix[i])/2+greens[1].getRed()); if (red>245)red=245;
		green = 0xff&(int)(Ggap*(1+cospix[i])/2+greens[1].getGreen()); if (green>245) green=245;
		blue =0xff&(int)(Bgap*(1+cospix[i])/2+greens[1].getBlue()); if(blue>245) blue=245;
		pixels[i]=((255<<24)|(red<<16)|(green<<8)|blue);}
 Image linimg=createImage(new MemoryImageSource(L,1,pixels,0,L));
 for(int j=0; j<h; j++)
 {wgr.drawImage(linimg,0,j,this);}
  repaint(); try{roller.sleep(10);   } catch(InterruptedException ie){};
}forgr.setColor(Color.black);forgr.drawString("C", 171,h/2 -1);forgr.drawString("C", 271,h/2 -1);
 forgr.drawString("R", 212,h/2 -1);forgr.drawString("R", 312,h/2 -1); 
 exerciseover=true; repaint(); pause2=true; 
 try{ synchronized(this){while(pause2==true) wait();}} catch(InterruptedException ie){};
}
if(step==6){exerciseover=false; msgimg1=textDrawLines(450,40,"LONGITUDINAL WAVES.  2 dimensional view");
 msgimg2=textGetLines(text[9],450);
	L=400;int pixsize=(int)Math.round(Math.sqrt(L*L+h*h));
	cospix=new float [pixsize]; float st=(float)0.08;
	 int red=0, green=0, blue=0; 
	int allpix[]=new int[L*h*2];for(int ii=0; ii<(L-1)*(h*2-1);ii++) {allpix[ii]=0xffa9eee4; }
	

for (int t=0; t<pixsize-1;t++)
	{ fillpix(t, (float) Math.PI/2, cospix); 
	  for(int j=0; j<h;j++)
		{for (int i=0; i<L; i++)
		{int sq=(int)Math.round(Math.sqrt(i*i+j*j));
		if(sq<pixsize){ red = 0xff&(int)(Rgap*(1+cospix[sq])/2+greens[1].getRed()); if (red>245)red=245;
		green = 0xff&(int)(Ggap*(1+cospix[sq])/2+greens[1].getGreen()); if (green>245) green=245;
		blue =0xff&(int)(Bgap*(1+cospix[sq])/2+greens[1].getBlue()); if(blue>245) blue=245;
		int newpix=((255<<24)|(red<<16)|(green<<8)|blue);
		allpix[(int)(h-j)*L+i]=newpix;
		allpix[(int)(h+j)*L+i]=newpix;}}
		}
	waves=createImage(new MemoryImageSource(L,2*h,allpix,0,L));
	repaint(); try{roller.sleep(10);   } catch(InterruptedException ie){};
} exerciseover=true;
repaint(); pause2=true; try{ synchronized(this){while(pause2==true) wait();}} catch(InterruptedException ie){};
}	

if(step==7){msgimg1=textDrawLines(450,70,"SUPERPOSITION OF WAVES with . same direction, different phase constants");
 msgimg2=textGetLines(text[10], 450); firstmvtover=false;
 x2=500; y2=100;L=400;  h=90; pause1=false; exerciseover=false; setdelay=50;
  phi= 2*(float)setdelay*(float)Math.PI/200; delay =phi/(float)st;
 forward=createImage(L,h); forward2=createImage(L,h); bouncewave=createImage(L,h*2+1); 
 Graphics forgr=forward.getGraphics(); Graphics phasegr=forward2.getGraphics(); Graphics supergr=bouncewave.getGraphics();
 repaint();// pause1=true;
//  try{synchronized(this){while(pause1==true) wait(); } } catch(InterruptedException ie){};
  forwcos=new float[L+1]; phasecos=new float[L+1]; int t=0;
     for(int ii=0; ii<=L;ii++) {forwcos[ii]=0;phasecos[ii]=0; }
 while(exerciseover==false)
  {fillpix(t, (float) Math.PI/2, forwcos ); ///////////wave left to right
    forgr.drawImage(graphpaper90,0,0,this);  phasegr.drawImage(graphpaper90,0,0,this);  supergr.drawImage(graphpaper181,0,0,this); repaint(); 
     forgr.setColor(Color.black); //forgr.drawLine(L,0,L,h/2);forgr.drawLine(0,0,0,h/2);
    supergr.setColor(Color.black);// supergr.drawLine(L,0,L,h);supergr.drawLine(0,0,0,h);
	    phi= (float)(setdelay*Math.PI/100);  delay=phi/(float)st;
   if(t>=delay) {fillphasepix(t, (float) Math.PI/2, delay);} ///////////////wave retarded with phase phi
     //phasegr.setColor(lightblue); phasegr.fillRect(0,0,L, h);
	phasegr.setColor(Color.black); 	float lastsupercos=0; 
    for(int ii=1; ii<=L;ii++)
	 {forgr.drawLine(ii-1,45-(int)Math.round(40*forwcos[ii-1]),ii,45-(int)Math.round(40*forwcos[ii]));
 	  phasegr.drawLine(ii-1,45-(int)Math.round(40*phasecos[ii-1]),ii,45-(int)Math.round(40*phasecos[ii]));
	  lastsupercos=forwcos[ii-1]+phasecos[ii-1];
	  supergr.drawLine(ii-1,90-(int)Math.round(40*lastsupercos) ,ii,90-(int)Math.round(40*(phasecos[ii]+forwcos[ii])));
	}
     repaint(); try{synchronized(this){while(pause1==true) wait();} roller.sleep(pauselength/2);   } catch(InterruptedException ie){};
   t++;}
  } 
  if(step==8){ msgimg2=textGetLines(text[11], 600); 
	repaint(); pause2=true; try{ synchronized(this){while(pause2==true) wait();}} catch(InterruptedException ie){};
	}
if(step==9){msgimg1=textDrawLines(450,70,"SUPERPOSITION OF WAVES  . from different sources"); msgimg2=textGetLines(text[12], 450);
	L=300; int h=300; exerciseover=false; int pixsize=(int)Math.round(Math.sqrt(L*L+4*h*h/9));
	int allpix[]=new int[L*h];
    cospix=new float [pixsize]; 
	int red=0, green=0, blue=0; 
 for (int t=0; t<pixsize-1;t++)
  {fillpix(t, (float) Math.PI, cospix); 
   for(int j=0; j< h;j++)
	{for (int i=0; i<L; i++)
	   {int sq1= (int)Math.round(Math.sqrt(i*i+(j-h/3)*(j-h/3)));
		int sq2=(int)Math.round(Math.sqrt(i*i+(j-h*2/3)*(j-h*2/3)));
		if((sq1<pixsize)&&(sq2<pixsize))
		{float average=(cospix[sq1]+cospix[sq2])/2;  //sum of cos from -2 to +2, average -1 to +1
         red = 0xff&(int)(Rgap*(average+1)/2+greens[1].getRed()); if (red>245)red=245;
		green = 0xff&(int)(Ggap*(average+1)/2+greens[1].getGreen()); if (green>245) green=245;
		blue =0xff&(int)(Bgap*(average+1)/2+greens[1].getBlue()); if(blue>245) blue=245;
		int newpix=((255<<24)|(red<<16)|(green<<8)|blue);
		allpix[j*L+i]=newpix;
		}
	}  }	
	waves=createImage(new MemoryImageSource(L,h,allpix,0,L));
	repaint(); try{roller.sleep(10);   } catch(InterruptedException ie){};
   }
exerciseover=true; repaint(); pause2=true; try{ synchronized(this){while(pause2==true) wait();}} catch(InterruptedException ie){};
}
if(step==10){msgimg1=textDrawLines(520,40,"SUPERPOSITION OF 2 PULSES moving in opposite directions");
 msgimg2=textGetLines(text[13], 450); 
 L=400;  h=90; pause1=false; exerciseover=false; 
 forward=createImage(400,h); backward=createImage(400,h); bouncewave=createImage(400,h*2+1); 
 Graphics forgr=forward.getGraphics(); Graphics backgr=backward.getGraphics(); Graphics supergr=bouncewave.getGraphics();
 forgr.setColor(lightblue); forgr.fillRect(0,0,400, h);backgr.setColor(lightblue); backgr.fillRect(0,0,400, h);
 
 firstmvtover=false; repaint(); pause1=true;
  try{synchronized(this){while(pause1==true) wait(); } } catch(InterruptedException ie){};
 for (int ex=0;ex<3;ex++){
 forwcos=new float[L+1];float [] backcos=new float[L+1]; 
  float thissupercos =0; float lastsupercos=thissupercos; 
  for(int ii=0; ii<=L;ii++) {forwcos[ii]=0;backcos[ii]=0; }firstmvtover=true;
for (int t=0; t<=L; t++)
  {fillpulsepix(t,  forwcos ); ///////////wave left to right
   fillpulsepix(t,  backcos); ///////////////wave from right to left
    forgr.setColor(lightblue); forgr.fillRect(0,0,400, h);
     forgr.setColor(Color.black); //forgr.drawLine(L,0,L,h/2);forgr.drawLine(0,0,0,h/2);
      supergr.setColor(lightblue); supergr.fillRect(0,0,400, 2*h+1);
	  supergr.setColor(Color.black);// supergr.drawLine(L,0,L,h);supergr.drawLine(0,0,0,h);
     backgr.setColor(lightblue); backgr.fillRect(0,0,400, h); backgr.setColor(Color.black);
 	  for(int ii=1; ii<=L;ii++)
	 {forgr.drawLine(ii-1,45-(int)Math.round(40*forwcos[ii-1]),ii,45-(int)Math.round(40*forwcos[ii]));
 	if(ex==0){  backgr.drawLine(L-ii+1,45-(int)Math.round(40*backcos[ii-1]),L-ii,45-(int)Math.round(40*backcos[ii]));}
	else if(ex==1){  backgr.drawLine(L-ii+1,45+(int)Math.round(40*backcos[ii-1]),L-ii,45+(int)Math.round(40*backcos[ii]));}
	else if(ex==2){  backgr.drawLine(L-ii+1,45+(int)Math.round(20*backcos[ii-1]),L-ii,45+(int)Math.round(20*backcos[ii]));}
	if(ex<=1){
	  lastsupercos=thissupercos; 
	  if (ex==0){ thissupercos=forwcos[ii]+backcos[L-ii];} else if (ex==1){ thissupercos=forwcos[ii]-backcos[L-ii];}
	  supergr.drawLine(ii-1,90-(int)Math.round(40*lastsupercos) ,ii,90-(int)Math.round(40*thissupercos));}
	 else if (ex==2){lastsupercos=thissupercos; thissupercos=40*forwcos[ii]-20*backcos[L-ii];
			  supergr.drawLine(ii-1,90-(int)Math.round(lastsupercos) ,ii,90-(int)Math.round(thissupercos));}
}
     repaint(); try{synchronized(this){while(pause1==true) wait();} roller.sleep(pauselength/2);   } catch(InterruptedException ie){};
   }}
   pause2=true; exerciseover=true;  repaint(); L=350;
try{ synchronized(this){while(pause2==true) wait();}} catch(InterruptedException ie){};

    }
if(step==11){msgimg1=textGetLines(text[18],530); ///superposition of waves in opposite direction
 msgimg2=textGetLines(text[14], 530);  forwcos=new float[401];float [] backcos=new float[401];
 x2=100; y2=50; h=90;  exerciseover=false; firstmvtover=false; pause1=false;
 forward=createImage(400,h); backward=createImage(400,h); bouncewave=createImage(400,h*2+1); 
 Graphics forgr=forward.getGraphics(); Graphics backgr=backward.getGraphics(); Graphics supergr=bouncewave.getGraphics();
 //forgr.setColor(lightblue); forgr.fillRect(0,0,400, h);backgr.setColor(lightblue); backgr.fillRect(0,0,400, h);
 drawAxes(graphpaper90, 90, 100); drawAxes(graphpaper181, 181, 100);
 forgr.drawImage(graphpaper90,0,0,this);  backgr.drawImage(graphpaper90,0,0,this);  supergr.drawImage(graphpaper181,0,0,this); repaint(); 
 repaint();//try{synchronized(this){while(pause1==true) wait(); } } catch(InterruptedException ie){};
 int t=0;// forwcos=new float[L+1];float [] backcos=new float[L+1]; 
 for(int ii=0; ii<=L;ii++) {forwcos[ii]=0;backcos[ii]=0; }
 float thissupercos =0; float lastsupercos=thissupercos;   
 
while(exerciseover==false)
  {   tt=t;firstmvtover=false; 
      fillpix(t, (float) Math.PI/2, forwcos ); ///////////wave left to right
   fillpix(t, (float) Math.PI/2, backcos); ///////////////wave from right to left
    	forgr.drawImage(graphpaper90,0,0,this);  
  //forgr.setColor(lightblue); forgr.fillRect(0,0,400, h);
     forgr.setColor(Color.black); forgr.fillRect(L,0,400-L,h);
      supergr.drawImage(graphpaper181,0,0,this);  
 	  supergr.setColor(Color.black); supergr.fillRect(L,0,400-L,2*h+1);
  backgr.drawImage(graphpaper90,0,0,this);  backgr.setColor(Color.black);   backgr.fillRect(L,0,400-L,h);
   for(int ii=1; ii<=L;ii++)
	 {forgr.drawLine(ii-1,45-(int)Math.round(40*forwcos[ii-1]),ii,45-(int)Math.round(40*forwcos[ii]));
 	  backgr.drawLine(L-ii+1,45-(int)Math.round(40*backcos[ii-1]),L-ii,45-(int)Math.round(40*backcos[ii]));
	  lastsupercos=thissupercos;thissupercos=forwcos[ii]+backcos[L-ii];
	  supergr.drawLine(ii-1,90-(int)Math.round(40*lastsupercos) ,ii,90-(int)Math.round(40*thissupercos));
	 } if(firstmvtover==true) {exerciseover=true;break;}
     supergr.setFont(big); supergr.drawString("SUM OF THE WAVES", 150,30);repaint();
 try{synchronized(this){while(pause1==true) wait();} roller.sleep(pauselength/2);   } catch(InterruptedException ie){};
  t++;} }//if(exerciseover==true)break;}
 
if(step==12) { pause1=true; exerciseover=false;
  msgimg1=textDrawLines(450,40,"REFLECTION OF A PULSE "); msgimg2=textGetLines(text[15], 450); 
			 st=(float)0.0628; L=400;  h=90; //firstmvtover=false;
  float[] toright0=new float[L+1]; float[] toleft0=new float[L+1]; 
  float[] toright1=new float[L+1]; 
 for(int ii=0; ii<=L;ii++) {toright0[ii]=0;toleft0[ii]=0; toright1[ii]=0;}
forward=createImage(400,h); backward=createImage(400,h);forward2=createImage(400,h); 
 Graphics forgr=forward.getGraphics(); Graphics backgr=backward.getGraphics(); Graphics for1gr=forward2.getGraphics();
  forgr.setColor(lightblue); forgr.fillRect(0,0,400, h);backgr.setColor(lightblue); backgr.fillRect(0,0,400, h);
 for1gr.setColor(lightblue); for1gr.fillRect(0,0,400, h); 
 forgr.setFont(big);for1gr.setFont(big);backgr.setFont(big);  repaint();
  try{synchronized(this){while(pause1==true) wait(); } } catch(InterruptedException ie){};
 for (int t=0; t<3*L; t++)
  {fillpulsepix(t, toright0 ); /////////// left to right
 forgr.setColor(lightblue); forgr.fillRect(0,0,400, h);backgr.setColor(lightblue); backgr.fillRect(0,0,400, h);
 for1gr.setColor(lightblue); for1gr.fillRect(0,0,400, 2*h+1); 
 forgr.setColor(Color.black); backgr.setColor(Color.black);for1gr.setColor(Color.black);
 if(t==L)  {toleft0[L]=toright0[L];}	
 if(t>L){bounceRightBoundary(toleft0, toright0);}
 if(t==2*L) {toright1[0]=toleft0[0];}
 if(t>2*L){bounceLeftBoundary( toright1,toleft0);}
  
  for(int ii=1; ii<=L;ii++)
	{forgr.drawLine(ii-1,45-(int)Math.round(40*toright0[ii-1]),ii,45-(int)Math.round(40*toright0[ii]));
     if(t>L){backgr.drawLine(ii-1,45-(int)Math.round(40*toleft0[ii-1]),ii,45-(int)Math.round(40*toleft0[ii]));}
	 if(t>2*L){for1gr.drawLine(ii-1,45-(int)Math.round(40*toright1[ii-1]),ii,45-(int)Math.round(40*toright1[ii]));}
	} 
	forgr.drawString("Newly emitted pulse", 150,30);backgr.drawString("Pulse after 1 reflection", 150,30);for1gr.drawString("Pulse after 2 reflections", 150,30);
repaint(); try{synchronized(this){while(pause1==true) wait();} roller.sleep(pauselength/4);   } catch(InterruptedException ie){};
   } exerciseover=true; 
repaint(); pause2=true; try{ synchronized(this){while(pause2==true) wait();}} catch(InterruptedException ie){};
   } 
 
if((step==13)||(step==14)||(step==15))
 { exercise=0; exerciseover=false;int maxexercise=2; int Lambdas[]={180,288,240,480}; int Lengths[]={400,375,350}; ///////demo
	if(step==13){msgimg1=textDrawLines(450,40,"REFLECTION OF A WAVETRAIN ");msgimg2=textGetLines(text[16], 750);maxexercise=2;}
	else if(step==14){msgimg1=textDrawLines(450,40,"REFLECTION OF A WAVE ");msgimg2=textGetLines(text[17], 650);maxexercise=3;}
	else if(step==15){msgimg1=textDrawLines(450,40,"REFLECTION OF A WAVE, varying wavelengths");msgimg2=textGetLines(text[19], 650);maxexercise=3;}
   drawAxes(graphpaper90, 90, 100); drawAxes(graphpaper360, 360, 100); 
 while( exercise < maxexercise)    
{if((step==13)||(step==14)){L=Lengths[exercise];lambda=100; }
 else if(step==15){lambda=Lambdas[exercise]; L=360;}
 st=6.2832/lambda; h=90;
 forward=createImage(405,h); backward=createImage(405,h);
 forward2=createImage(405,h); backward2=createImage(405,h); bouncewave=createImage(405,360); 
 Graphics forgr=forward.getGraphics(); Graphics backgr=backward.getGraphics();
  Graphics for1gr=forward2.getGraphics(); Graphics back1gr=backward2.getGraphics(); 
Graphics supergr=bouncewave.getGraphics();
forgr.drawImage(graphpaper90,0,0,this); forgr.setColor(Color.black);  forgr.fillRect(L,0,405-L,h);
    for1gr.drawImage(graphpaper90,0,0,this); for1gr.setColor(Color.black); for1gr.fillRect(L,0,405-L,h); 
    backgr.drawImage(graphpaper90,0,0,this); backgr.setColor(Color.black);  backgr.fillRect(L,0,405-L,h);
	back1gr.drawImage(graphpaper90,0,0,this); back1gr.setColor(Color.black); back1gr.fillRect(L,0,405-L,h);
	supergr.drawImage(graphpaper360,0,0,this);  supergr.setColor(Color.black); supergr.fillRect(L,0,405-L,4*h);
   forgr.setFont(big);for1gr.setFont(big);backgr.setFont(big);back1gr.setFont(big);supergr.setFont(big);
   repaint();
  try{synchronized(this){while(pause1==true) wait(); } } catch(InterruptedException ie){};
 float[] toright0=new float[L+1]; float[] toleft0=new float[L+1]; firstmvtover=false; pause1=false;
  float[] toright1=new float[L+1]; float[] toleft1=new float[L+1];
 float thissupercos =0; float lastsupercos=thissupercos; //float thissupercos2 =0; float lastsupercos2=thissupercos2; 
 for(int ii=0; ii<=L;ii++) {toright0[ii]=0;toleft0[ii]=0; toright1[ii]=0;toleft1[ii]=0;}

 for (int t=0; t<5*L; t++)
  {	fillpix(t, (float)Math.PI/2,toright0 );
	forgr.drawImage(graphpaper90,0,0,this); forgr.setColor(Color.black);  forgr.fillRect(L,0,405-L,h);
    for1gr.drawImage(graphpaper90,0,0,this); for1gr.setColor(Color.black); for1gr.fillRect(L,0,405-L,h); 
    backgr.drawImage(graphpaper90,0,0,this); backgr.setColor(Color.black);  backgr.fillRect(L,0,405-L,h);
	back1gr.drawImage(graphpaper90,0,0,this); back1gr.setColor(Color.black); back1gr.fillRect(L,0,405-L,h);
	supergr.drawImage(graphpaper360,0,0,this);  supergr.setColor(Color.black); supergr.fillRect(L,0,405-L,4*h);
// if(t==L)  {toleft0[L]=toright0[L];}	
 if(t>L){bounceRightBoundary(toleft0, toright0);}
// if(t==2*L) {toright1[0]=toleft0[0];}
 if(t>2*L){bounceLeftBoundary( toright1,toleft0);}
// if(t==3*L) {toleft1[L]=toright1[L];}
 if(t>3*L){bounceRightBoundary(toleft1, toright1);}
 if((step==13)&&(t>=4*L)){break;}
 for(int ii=1; ii<=L;ii++)
	{forgr.drawLine(ii-1,45-(int)Math.round(40*toright0[ii-1]),ii,45-(int)Math.round(40*toright0[ii]));
     if(t<=L){lastsupercos=thissupercos;  thissupercos=toright0[ii]; 
			supergr.drawLine(ii-1,2*h-(int)Math.round(39*lastsupercos) ,ii,2*h-(int)Math.round(40*thissupercos+1));}
	 else if(t<=2*L){backgr.drawLine(ii-1,45-(int)Math.round(40*toleft0[ii-1]),ii,45-(int)Math.round(40*toleft0[ii]));
			if(ii==L){thissupercos=toright0[ii];}
			lastsupercos=thissupercos;  thissupercos=toright0[ii]+toleft0[ii]; 
			supergr.drawLine(ii-1,2*h-(int)Math.round(39*lastsupercos) ,ii,2*h-(int)Math.round(40*thissupercos+1));}
			// if((t==2*L)&&(ii==0)){thissupercos=toright0[ii]+toleft0[ii];}
	else if(t<3*L){backgr.drawLine(ii-1,45-(int)Math.round(40*toleft0[ii-1]),ii,45-(int)Math.round(40*toleft0[ii]));
		        for1gr.drawLine(ii-1,45-(int)Math.round(40*toright1[ii-1]),ii,45-(int)Math.round(40*toright1[ii]));
         if(ii==1){thissupercos=toright0[0]+toleft0[0];} 
			 lastsupercos=thissupercos;  thissupercos=toright0[ii]+toleft0[ii]+toright1[ii];
        supergr.drawLine(ii-1,2*h-(int)Math.round(39*lastsupercos),ii,2*h-(int)Math.round(40*thissupercos+1));}
	else {backgr.drawLine(ii-1,45-(int)Math.round(40*toleft0[ii-1]),ii,45-(int)Math.round(40*toleft0[ii]));
			     for1gr.drawLine(ii-1,45-(int)Math.round(40*toright1[ii-1]),ii,45-(int)Math.round(40*toright1[ii]));
				 back1gr.drawLine(ii-1,45-(int)Math.round(40*toleft1[ii-1]),ii,45-(int)Math.round(40*toleft1[ii]));
			     if(ii==L){thissupercos=toright0[ii]+toleft0[ii]+toright1[ii];}
			lastsupercos=thissupercos;  thissupercos=toright0[ii]+toleft0[ii]+toright1[ii]+toleft1[ii]; 
			supergr.drawLine(ii-1,2*h-(int)Math.round(39*lastsupercos) ,ii,2*h-(int)Math.round(40*thissupercos+1));}
		} //if(firstmvtover==true) {exerciseover=true;  break;}
	if(step==13){forgr.drawString("Newly emitted wavetrain", 150,30);}if((step==14)||(step==15)){forgr.drawString("Newly emitted wave", 150,30);}
	backgr.drawString("Wave after 1 reflection", 150,30);for1gr.drawString("Wave after 2 reflections", 150,30);
    back1gr.drawString("Wave after 3 reflections", 150,30); supergr.drawString("SUM OF THE WAVES", 150,30);
 repaint(); try{synchronized(this){while(pause1==true) wait();} roller.sleep(10);   } catch(InterruptedException ie){};
   }exercise++; pause1=true; repaint();try{synchronized(this){while(pause1==true) wait();} roller.sleep(10);   } catch(InterruptedException ie){};
  } 
exerciseover=true; repaint(); pause2=true; try{ synchronized(this){while(pause2==true) wait();}} catch(InterruptedException ie){};
   }  
if(step==16){h=90; exerciseover= false;msgimg1=textDrawLines(530,40, text[21]);
 forward=createImage(405,h); backward=createImage(405,h);
 forward2=createImage(405,h); backward2=createImage(405,h); bouncewave=createImage(405,360); 
 Graphics forgr=forward.getGraphics(); Graphics backgr=backward.getGraphics();
  Graphics for1gr=forward2.getGraphics(); Graphics back1gr=backward2.getGraphics(); 
Graphics supergr=bouncewave.getGraphics();
forgr.drawImage(graphpaper90,0,0,this); forgr.setColor(Color.black);  forgr.fillRect(L,0,405-L,h);
    for1gr.drawImage(graphpaper90,0,0,this); for1gr.setColor(Color.black); for1gr.fillRect(L,0,405-L,h); 
    backgr.drawImage(graphpaper90,0,0,this); backgr.setColor(Color.black);  backgr.fillRect(L,0,405-L,h);
	back1gr.drawImage(graphpaper90,0,0,this); back1gr.setColor(Color.black); back1gr.fillRect(L,0,405-L,h);
	supergr.drawImage(graphpaper360,0,0,this);  supergr.setColor(Color.black); supergr.fillRect(L,0,405-L,4*h);
   forgr.setFont(big);for1gr.setFont(big);backgr.setFont(big);back1gr.setFont(big);supergr.setFont(big);
   repaint();
 // try{synchronized(this){while(pause1==true) wait(); } } catch(InterruptedException ie){};
 float[] toright0=new float[401]; float[] toleft0=new float[401];  pause1=false;
  float[] toright1=new float[401]; float[] toleft1=new float[401];
while(exerciseover==false) {
 firstmvtover=false;st=6.2832/lambda; 
  float thissupercos =0; float lastsupercos=thissupercos;  
 for(int ii=0; ii<=L;ii++) {toright0[ii]=0;toleft0[ii]=0; toright1[ii]=0;toleft1[ii]=0;}
///////filling th arrays///////
  for(int ii=0; ii<=L;ii++)
 {toright0[ii]=(float)Math.cos((4*L-ii)*st+(float) Math.PI/2);toleft0[ii]=-(float)Math.cos((2*L+ii)*st+(float) Math.PI/2);
  toright1[ii]=(float)Math.cos((2*L-ii)*st+(float) Math.PI/2);toleft1[ii]=-(float)Math.cos((ii)*st+(float) Math.PI/2);}

// try{roller.sleep(100); } catch(InterruptedException ie){};
 firstmvtover=false;//toright1[ii]=40*(float)Math.sin((2*L+1-ii)*st);toleft1[ii]=40*(float)Math.sin((L+1-ii)*st);}
 for (int t=4*L+1; t<6*L; t++)
  {tt=t;	 fillpix(t, (float) Math.PI/2,toright0 );
	bounceRightBoundary(toleft0, toright0);
	bounceLeftBoundary( toright1,toleft0);
	bounceRightBoundary(toleft1, toright1);
	forgr.drawImage(graphpaper90,0,0,this); forgr.setColor(Color.black);  forgr.fillRect(L,0,405-L,h);
    for1gr.drawImage(graphpaper90,0,0,this); for1gr.setColor(Color.black); for1gr.fillRect(L,0,405-L,h); 
    backgr.drawImage(graphpaper90,0,0,this); backgr.setColor(Color.black);  backgr.fillRect(L,0,405-L,h);
	back1gr.drawImage(graphpaper90,0,0,this); back1gr.setColor(Color.black); back1gr.fillRect(L,0,405-L,h);
	supergr.drawImage(graphpaper360,0,0,this);  supergr.setColor(Color.black); supergr.fillRect(L,0,405-L,4*h);
 
 for(int ii=1; ii<=L;ii++)
	{forgr.drawLine(ii-1,45-(int)Math.round(40*toright0[ii-1]),ii,45-(int)Math.round(40*toright0[ii]));
     backgr.drawLine(ii-1,45-(int)Math.round(40*toleft0[ii-1]),ii,45-(int)Math.round(40*toleft0[ii]));
	// if(t<3*L){ for1gr.drawLine(ii-1,45-(int)Math.round(40*toright1[ii-1]),ii,45-(int)Math.round(40*toright1[ii]));
      //   if(ii==1){thissupercos=toright0[0]+toleft0[0];} 
//			 lastsupercos=thissupercos;  thissupercos=toright0[ii]+toleft0[ii]+toright1[ii];
  //      supergr.drawLine(ii-1,2*h-(int)Math.round(40*lastsupercos),ii,2*h-(int)Math.round(40*thissupercos));}
	//else {//backgr.drawLine(ii-1,45-(int)Math.round(40*toleft0[ii-1]),ii,45-(int)Math.round(40*toleft0[ii]));
			     for1gr.drawLine(ii-1,45-(int)Math.round(40*toright1[ii-1]),ii,45-(int)Math.round(40*toright1[ii]));
				 back1gr.drawLine(ii-1,45-(int)Math.round(40*toleft1[ii-1]),ii,45-(int)Math.round(40*toleft1[ii]));
			     if(ii==L){thissupercos=toright0[ii]+toleft0[ii]+toright1[ii];}
			lastsupercos=thissupercos;  thissupercos=toright0[ii]+toleft0[ii]+toright1[ii]+toleft1[ii]; 
			supergr.drawLine(ii-1,2*h-(int)Math.round(39*lastsupercos) ,ii,2*h-(int)Math.round(40*thissupercos+1));
		} if(firstmvtover==true) {  break; }//exerciseover=true;
	forgr.drawString("Newly emitted wave", 150,30);
	backgr.drawString("Wave after 1 reflection", 150,30);for1gr.drawString("Wave after 2 reflections", 150,30);
    back1gr.drawString("Wave after 3 reflections", 150,30); supergr.drawString("SUM OF THE WAVES", 150,30);

repaint(); //
 try{synchronized(this){while(pause1==true) wait();} roller.sleep(10);   } catch(InterruptedException ie){};
   }if(firstmvtover==true) {break;}
   } }
if(step==17){msgimg1=textDrawLines(160,40,"HARMONICS");
 msgimg2=textGetLines(text[20], 280);
graphpapersh=createImage(320,180);drawGraphpaper(graphpapersh,320,180); 
 L=320;  h=180;  pause1=false; exerciseover=false;  scene=new Image[6];  Graphics sgr[]=new Graphics[6];
double[] lambdaHarm={2*L, L, 2*L/3, L/2, 2*L/5, L/3}; int tmax[]={(int)(6.5*L), (int)(6.25*L),(int)(5.5*L),(int)(5.125*L), (int)(5.5*L),(int)( 5.0625*L)};
 for(int j=0;j<6;j++)
 {scene[j]=createImage(L,h); sgr[j]=scene[j].getGraphics();
  //sgr[j].setColor(lightblue); sgr[j].fillRect(0,0,L,h);sgr[j].setFont(big);
  sgr[j].drawImage(graphpapersh,0,0,this); sgr[j].setColor(Color.black);}
for(int j=0;j<6;j++){
 st=6.2832/lambdaHarm[j];
float[] toright0=new float[L+1]; float[] toleft0=new float[L+1]; firstmvtover=false; pause1=false;
  float[] toright1=new float[L+1]; float[] toleft1=new float[L+1];
 float thissupercos =0; float lastsupercos=thissupercos;  
 for(int ii=0; ii<=L;ii++) {toright0[ii]=0;toleft0[ii]=0; toright1[ii]=0;toleft1[ii]=0;}
 for(int ii=0; ii<=L;ii++)
 {toright0[ii]=(float)Math.cos((4*L-ii)*st);toleft0[ii]=-(float)Math.cos((2*L+ii)*st);
  toright1[ii]=(float)Math.cos((2*L-ii)*st);toleft1[ii]=-(float)Math.cos((ii)*st);}
 for (int t=4*L+1; t<tmax[j]; t++)
 { 
 sgr[j].drawImage(graphpapersh,0,0,this); sgr[j].setColor(Color.black);//}
fillpix(t, 0, toright0 );
bounceRightBoundary(toleft0, toright0);
bounceLeftBoundary( toright1,toleft0);
bounceRightBoundary(toleft1, toright1);

 for(int ii=1; ii<=L;ii++)
    { if(ii==1){thissupercos=toright0[0]+toleft0[0]+toright1[0]+toleft1[00];}
	 lastsupercos=thissupercos;  thissupercos=toright0[ii]+toleft0[ii]+toright1[ii]+toleft1[ii]; 
	 sgr[j].drawLine(ii-1,h/2-(int)Math.round(20*lastsupercos) ,ii,h/2-(int)Math.round(20*thissupercos));}

 repaint(); //if(firstmvtover==true) { exerciseover=true; break;}  
 try{synchronized(this){while(pause1==true) wait();} roller.sleep(10);   } catch(InterruptedException ie){};
   }}exerciseover=true;
 repaint();
 pause2=true; try{ synchronized(this){while(pause2==true) wait();}} catch(InterruptedException ie){};
   } 
if(step==18){repaint();////////downloading the pictures
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
carimg=getAlpha(bgitems[0]);girlimg= getAlpha(bgitems[1]);
}	
if(step==19){	bigscene=createImage(800,200); Graphics sgr=bigscene.getGraphics(); ////DOPPLER///////
 msgimg1=textDrawLines(520,40,"DOPPLER EFFECT");
 int spsound=4, tstart=50, xhorn=100, nbcrests=0,firstcrest=0, yc=100,distsound=0;
    pause1=false;double l=0; float spcar=(float)1; exerciseover=false;
  for(tt=0; tt<800; tt++)
		{sgr.setColor(lightblue); sgr.fillRect(0,0,800,150);
		 sgr.setColor(new Color(220,173,171)); sgr.fillRect(0,130,800,70);
		 sgr.setColor(Color.black); 
   	   xhorn=(int)(100+spcar*tt);
  // if (tt<=tstart){xhorn=100;}else {xhorn=(int)(100+spcar*(tt-tstart));}
		 sgr.drawImage(carimg, xhorn-100,100-20,this);sgr.drawImage(girlimg, 500,200-136,this);
		 if(tt%5==0){r[nbcrests]= new Soundcrest(tt,xhorn);
                     nbcrests++;}
		for(int i=firstcrest; i<nbcrests;i++)
					{distsound=spsound*(tt-r[i].to);
					 if(distsound<101) { sgr.setColor(Color.black); 
					 //sgr.drawOval(xhorn-distsound, yc-distsound,2*distsound, 2*distsound);}
					 sgr.drawOval(r[i].xcenter-distsound,yc-distsound,2*distsound, 2*distsound);}
					else{///forward
						 l=Math.sqrt(distsound*distsound-yc*yc);
						double tangenT=(double)100/l;
						double radangle=Math.atan(tangenT);
						double degreeangle=radangle*180/Math.PI;
						sgr.drawArc((int)(r[i].xcenter+l),0,distsound-(int)l,200,(int) degreeangle, -2*(int)degreeangle);
					   sgr.drawArc((int)(r[i].xcenter-distsound),0,distsound-(int)l,200,180-(int) degreeangle, 2*(int)degreeangle);
					 //if((distsound>(800-xhorn))&&(tt%5==0)){firstcrest++;}
					}	
					}
			sgr.drawString("l1 + "+l,20,400);	
   repaint(); try{synchronized(this){while(pause1==true) wait();} roller.sleep(pauselength);   } catch(InterruptedException ie){};
 } 	
pause2=true; exerciseover=true;  repaint();
try{ synchronized(this){while(pause2==true) wait();}} catch(InterruptedException ie){};
}
if(step==20){msgimg1=textDrawLines(300,100,text[29]); repaint(); try{roller.sleep(2000);   } catch(InterruptedException ie){};
 } 	  
}}
public void fillpulsepix(int i,  float[] cospix)
 {float newcos=0; if(i<50)  {newcos=(float)(Math.sin(st*i));}
 // else if ((i>2*L)&&(i<2*L+50)) {newcos=(float)(Math.sin(st*i));} //50*0.0628=PI
  if(i>=L){i=L;} for(int j=i; j>0; j--)
     {cospix[j]=cospix[j-1];}
      cospix[0]=newcos; 
 }
   
public void fillpix(int i, float offset, float[] cospix)
 { float newcos=0; 
 if(step==13)
 {if(morefun==false){if(i<2*lambda){newcos=(float)(Math.cos(st*i+offset));}
                      else if((i>8*lambda)&&(i<(10*lambda))){newcos=(float)(Math.cos(st*i+offset));} } 
  if(morefun==true) { if((i>4*lambda)&&(i<(6*lambda))){newcos=(float)(Math.cos(st*i+offset));}
					 if((i>12*lambda)&&(i<(14*lambda))){newcos=(float)(Math.cos(st*i+offset));}} 
	}				
  else{newcos=(float)(Math.cos(st*i+offset));}
  if(i>=L){i=L;}
  for(int j=i; j>0; j--)
     {cospix[j]=cospix[j-1];}
      cospix[0]=newcos; 
 }
public void fillphasepix(int i, float offset, float delay)
 { float newcos=(float)(Math.cos(st*(i-delay)+offset)); 
    if(i>=L){i=L;} for(int j=i; j>0; j--)
     {phasecos[j]=phasecos[j-1];}
      phasecos[0]=newcos; 
  } 
public void bounceRightBoundary(float[]outgoing, float[] incoming)
{for (int j=0;j<L;j++){outgoing[j]=outgoing[j+1];} outgoing[L]=-incoming[L];}

public void bounceLeftBoundary(float[]outgoing, float[] incoming)
{for (int j=L;j>0;j--){outgoing[j]=outgoing[j-1];} outgoing[0]=-incoming[0];}

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
synchronized public void mouseClicked(MouseEvent me)
 { int coX=me.getX(); int coY=me.getY(); 
  switch(step) 
   {case 1: case 3 :  
          if((coY>height-50)&&(coX>390)&&(coX<510))
                    { step++; pause2=false;notify();} break;
	case 2: case 5 : case 6 : case 17 : case 18: if((coY>height-50)&&(coX>390))
  	  {if (coX<510) { step++; pause2=false;notify();} 
	   else if (coX<640) { pause2=false;notify();} } /////////SAME ANIME
	 break;
	 case 9 : if((exerciseover==true)&&(coY>height-50)&&(coX>390))
  	  {if (coX<510) { step++; pause2=false;notify();} 
	   else if (coX<640) { pause2=false;notify();} } /////////SAME ANIME
	 break;
	case 4:  if(coY>height-50)
	  {if(coX<520){ pause1=false;notify();repaint();}
			else if (coX<720){step++; pause2=false;notify();} 
		}	break;
	case 7 :
	 if((coY>height-110)&&(coX>390)&&(coX<520))
  	   { if (pause1==true) 	{ pause1=false; firstmvtover=false; notify(); }   ////pressed resume
	     else if (pause1==false)	{   pause1=true;  notify(); }           ////pressed STOP
	   }  
	   else if ((coX>=520)&&(coX<640)&&(coY>height-110)) { exerciseover=true; step++;pause1=false;pause2=false; notify();} 
		 break;
	case 8 :if((coY>height-110)&&(coX>390))
	      if(coX<520){ step++; pause2=false;notify();}
  	 		else if (coX<640){ step--; pause2=false;notify();}	
        break;
	case 10 :case 12 : case 19 :	if ((exerciseover==false)&&(coY>height-50)&&(coX<520))
			{if(pause1==true) { pause1=false; notify(); }   
			 else if (pause1==false) {  pause1=true;  notify(); }  }        
		    else if (exerciseover==true){if(coX<510) { step++; pause2=false;notify();} 
					else if (coX>510) { pause2=false;notify();} }/////////SAME ANIME
		break;
    case 11 :  if (coY>height-110)
	   {if((coX>390)&&(coX<520))
  	    {if (pause1==false) { pause1=true;  notify(); }    
		 else if(pause1==true)  {pause1=false; notify(); }  
		 //if(pause2==true){	pause2=false; notify();}         
		 else if (firstmvtover==true)  {pause2=false; notify();}   
	      } 
	   else if ((coX>=520)&&(coX<640)) { exerciseover=true; step++;pause1=false;pause2=false; notify();} 
		 }break;
	case 16: if (coY>height-100)
	   {if((coX>390)&&(coX<520))
  	    {if (pause1==false) { pause1=true;  notify(); }    
		 else if(pause1==true)  {pause1=false; notify(); }  
		 //if(pause2==true){	pause2=false; notify();}         
		  // else if (firstmvtover==true)  {pause1=false; notify();}   
	      } 
	   else if ((coX>=520)&&(coX<640)) { firstmvtover=true; exerciseover=true; step++;pause1=false;pause2=false; notify();} 
		 }break;
	case 13 : case 14: case 15 : if (coY>height-110)
	 {if (exerciseover==false) { if((coX>390)&&(coX<520))
					{if(pause1==true)  {pause1=false; notify(); }  
					else if (pause1==false) { pause1=true;  notify(); }           
		    }}
		else if (exerciseover==true) 
			{if((coX>390)&&(coX<520)) {step++; pause2=false; notify();}
			 else if ((coX>=520)&&(coX<640)) {; pause2=false; notify();} // exerciseover=false; exercise=0
			 //else if ((coX>=720)&&(step==13)) {morefun=true; pause2=false; notify();} 
	        }
			}break;
		 default: step++; pause2=false;notify(); repaint();break;}
 }
public void mouseEntered(MouseEvent me){}
public void mouseExited(MouseEvent me){}
public void mousePressed(MouseEvent me)
 {int coX=me.getX(); int coY=me.getY(); 
  if ((step==16)&&(coX>=x3)&&(coX<x3+205)&&(coY<y2+100)){select2=true;}
  else if((step==7)||(step==11)||(step==16))
  {if ((coX<x2+205)&&(coY<y2+100)){select=true;}}
							
 
  }
public void mouseDragged(MouseEvent me)
  {int coY= me.getY(); if(coY>height-110){return;}
   else if (select==true) {int coX=me.getX(); setdelay=coX-x2;if(setdelay<0) setdelay=0; 
			if(setdelay>200) setdelay=200; firstmvtover=true; }
   else if (select2==true) {int coX=me.getX(); 
	setlambda=coX-x3; if(setlambda<0) setlambda=0;   if(setlambda>200) setlambda=200;}
  firstmvtover=true; //pause1=true;
  repaint();pause1=true; }
      
 
public void mouseMoved(MouseEvent me){}
public void mouseReleased(MouseEvent me)
{int rY= me.getY(); int rX=me.getX(); //firstmvtover=true;
 if(select==true){
   setdelay=rX-x2;if(setdelay<0) setdelay=0; if(setdelay>200) setdelay=200;
   if(step==7) { phi= (float)(setdelay*Math.PI/100);  delay=phi/(float)st;}
     else if((step==11)||(step==16)){L=setdelay+200;}  select=false; repaint();}
 else if (select2==true){  setlambda=rX-x3;if(setlambda<0) setlambda=0; 
   if(setlambda>200) setlambda=200;
  lambda=setlambda*4; st=6.2832/lambda;
   select2=false; repaint();}
}
public void drawGraphpaper(Image im, int dwidth, int dheight)
{   Graphics pg= im.getGraphics();   pg.setColor(Color.white); pg.fillRect(0,0,dwidth,dheight);
	  pg.setColor(Color.orange);
       for(int i=0; i<dwidth; i+=5)   {pg.drawLine(i,0,i,dheight-1);}
      for(int j=0; j<dheight; j+=5)   {pg.drawLine(0,j, dwidth-1,j);}
  }
public void drawAxes(Image im, int height, int scale)
{ Graphics pg= im.getGraphics(); pg.setFont(new Font("Dialog", Font.PLAIN, 16)); 
 int po=0; int qo= height/2;
  pg.setColor(Color.black); pg.drawLine( po, qo, 400, qo); pg.drawLine(po,1, po, height);
      for(int p=0, i=0; p<400; p+=100, i++){pg.drawLine(p+po, qo,p+po, qo+4);
               pg.drawString(""+i*scale, p+po-4,qo+18);}  
       }
/*
public void drawVector(Image im, int Xo, int Yo,float bigangle , float dis)
{ float angle= bigangle%(2*(float)Math.PI);
 Graphics imgr= im.getGraphics(); imgr.setColor(Color.black);
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
*/
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
{offS.setColor(green); offS.fillRect(0,0,width,height); 
offS.setColor(Color.black); offS.drawString("step = "+step, 20,height-20);
   switch (step)
   {case 1 : offS.drawImage(msgimg1,width-450-50,10,this); 
   offS.drawImage(scene[0],10, 60, this);
if(firstmvtover==true){ offS.drawImage(msgimg2,width-450-50,200,this);
 offS.drawImage(scene[1],10, 200, this);}
// for(int jj=1;jj<6;jj++){  offS.drawImage(scene[jj],10, 10+jj*110 ,this); }}
 if(exerciseover==true){offS.setColor(bluegreen);offS.fillRect(390, height-50, 120 ,40);     //button next
    offS.setColor(Color.yellow); offS.drawString("NEXT",400,height-20);}
break;
  case 2 :offS.setColor(Color.black); offS.drawString("TRAVELLING WAVE",100,30); 
   offS.drawImage(msgimg1,width-560,60,this); 
   offS.drawImage(forward,5,60,this);
    if(firstmvtover==true){ offS.drawImage(forward2,5,50,this); offS.drawImage(forward,5,360,this);
	offS.drawImage(msgimg2,width-560,360,this);  } 
  if(exerciseover==true){ offS.setColor(bluegreen);offS.fillRect(390, height-50, 120 ,40); offS.fillRect(520, height-50, 200 ,40);   
    offS.setColor(Color.yellow); offS.drawString("NEXT",400,height-20);    //button next
     offS.drawString("SAME ANIMATION",530,height-20);}
 break;
   case 3 : offS.drawImage(msgimg2,150,50,this);  
   offS.drawImage(forward2,250,400,this);
  offS.setColor(bluegreen);offS.fillRect(390, height-50, 120 ,40);     //button next
    offS.setColor(Color.yellow); offS.drawString("NEXT",400,height-20);
 break;
  case 4 : offS.drawImage(msgimg1,20,10,this);  
  if(firstmvtover==false){ offS.setColor(bluegreen);offS.fillRect(390, height-50, 140 ,40);     //button next or showAnswer
    offS.setColor(Color.yellow); 	offS.drawString("Show Answer",400,height-20);}
	 else if(firstmvtover==true){offS.drawImage(msgimg2,20,210,this);
	  offS.setColor(bluegreen);offS.fillRect(540, height-50, 160 ,40);     //button next or showAnswer
    offS.setColor(Color.yellow);offS.drawString("NEXT",580,height-20);}
  // offS.drawString( "firstmvtover= "+firstmvtover+"  pause1= " +pause1, x2, height-10);

 break;
  case 5 :offS.drawImage(msgimg2,150,5,this); offS.drawImage(msgimg1,width-550,50,this); offS.drawImage(waves,5,50,this);
         offS.drawImage(forward,5,200,this);
 if(exerciseover==true){ offS.setColor(bluegreen);offS.fillRect(390, height-50, 120 ,40);offS.fillRect(520, height-50, 200 ,40);      //button next
    offS.setColor(Color.yellow); offS.drawString("NEXT",400,height-20);offS.drawString("SAME ANIMATION",530,height-20);}
 break;
  case 6 :offS.drawImage(msgimg1,150,5,this); offS.drawImage(msgimg2,width-10-450,150,this); offS.drawImage(waves,5,150,this);
       if(exerciseover==true){    offS.setColor(bluegreen);offS.fillRect(390, height-50, 120 ,40);offS.fillRect(520, height-50, 200 ,40);      //button next
    offS.setColor(Color.yellow); offS.drawString("NEXT",400,height-20);offS.drawString("SAME ANIMATION",530,height-20); }
 break;
 case 7 :  offS.drawImage(msgimg1,150,5,this); offS.drawImage(msgimg2, x2,y2+100,this);
             offS.drawImage(slider, x2,y2,this); offS.setColor(Color.black); offS.fillRect(x2+setdelay,y2-5, 5, 42);
 offS.drawString("drag the slider to change the phase-difference  \u03C6", x2, y2+60);	
 int phi_int=(int)(100*phi);
 offS.drawString("phase difference \u03C6 = "+(float)phi_int/100+" radians", x2, y2+85);
 offS.drawImage(forward,5,160,this);  offS.drawImage(forward2,5,260,this); offS.drawImage(bouncewave,5,360,this);
   offS.setColor(bluegreen);offS.fillRect(390, height-50, 120 ,40); offS.fillRect(520, height-50, 120 ,40);     //button next 
    offS.setColor(Color.yellow); offS.drawString("NEXT",530,height-20);
	if(firstmvtover==false){offS.drawString("START",400,height-20);}
	else if (pause1==false){offS.drawString("STOP",400,height-20);}
	else if (pause1==true){offS.drawString("RESUME",400,height-20);}
//	offS.drawString("setdelay= "+setdelay+ "firstmvtover= "+firstmvtover+"  pause1= " +pause1, x2, height-10);
   
 break;
  case 8 :  offS.drawImage(msgimg2,200,50,this);
  offS.setColor(bluegreen);offS.fillRect(390, height-50, 120 ,40);offS.fillRect(520, height-50, 160 ,40);    
    offS.setColor(Color.yellow);
	offS.drawString("NEXT",400,height-20);	offS.drawString("PREVIOUS PAGE",530,height-20);
   break;
 case 9 : offS.drawImage(msgimg2,width-460,120,this);  
   offS.drawImage(waves,20,120,this);
   if(exerciseover==true){offS.setColor(bluegreen);offS.fillRect(390, height-50, 120 ,40); offS.fillRect(520, height-50, 200 ,40);   
    offS.setColor(Color.yellow); offS.drawString("NEXT",400,height-20);    //button next
     offS.drawString("SAME ANIMATION",530,height-20);}
 break;
 
  case 10 : offS.drawImage(msgimg1,150,5,this); offS.drawImage(msgimg2, x2,160,this);
  offS.drawImage(forward,5,160,this);  offS.drawImage(backward,5,260,this); offS.drawImage(bouncewave,5,360,this);
   offS.setColor(bluegreen);offS.fillRect(390, height-50, 120 ,40); offS.fillRect(520, height-50, 200 ,40);    
      offS.setColor(Color.yellow); 	
	 if(pause2==true){offS.drawString("NEXT",400,height-20);    //button next
     offS.drawString("SAME ANIMATION",530,height-20);}
  else if(firstmvtover==false){offS.drawString("START",400,height-20);}
	else if (pause1==false){offS.drawString("STOP",400,height-20);}
    else if (pause1==true){offS.drawString("RESUME",400,height-20);}
	 break;
case 11 :x2=100; y2=50;offS.drawImage(msgimg1,width-540, 430,this); offS.drawImage(msgimg2, width-540,y2,this);
          offS.drawImage(slider, x2, y2,this); offS.setColor(Color.black); offS.fillRect(x2+L-200,y2-5, 5, 42);
	offS.drawString("SUPERPOSITION OF WAVES moving in opposite directions",200,30);
	offS.drawString("drag the slider to change ", x2, y2+60);	
	offS.drawString(" the distance between the sources", x2, y2+80);	
	offS.drawString("distance = "+L , x2, y2+100);
	offS.drawImage(forward,5,y2+120,this);  offS.drawImage(backward,5,y2+240,this); offS.drawImage(bouncewave,5,y2+360,this);
	offS.setColor(bluegreen);offS.fillRect(390, height-50, 120 ,40); offS.fillRect(520, height-50, 120 ,40);     //button next 
    offS.setColor(Color.yellow); offS.drawString("NEXT",530,height-20);
	if(pause1==true){offS.drawString("RESTART",400,height-20);}
	else if (pause1==false){offS.drawString("STOP",400,height-20);}
	//else if ((firstmvtover==true)&&(pause1==true)){offS.drawString("RESTART",400,height-20);}
 // offS.drawString("t= "+tt+ "firstmvtover= "+firstmvtover+"  pause1= " +pause1, x2, height-10);
 break;
case 12 : offS.drawImage(msgimg1,360,6,this); offS.drawImage(msgimg2,width-500,100,this);
offS.drawImage(forward,5,160,this);  offS.drawImage(backward,5,260,this); offS.drawImage(forward2,5,360,this);
if(exerciseover==true){offS.setColor(bluegreen);offS.fillRect(390, height-50, 120 ,40); offS.fillRect(520, height-50, 200 ,40);   
    offS.setColor(Color.yellow); offS.drawString("NEXT",400,height-20);    //button next
     offS.drawString("SAME ANIMATION",530,height-20);}
else if(exerciseover==false){ offS.setColor(bluegreen);offS.fillRect(390, height-50, 120 ,40);
  offS.setColor(Color.yellow); 
 if (pause1==true)offS.drawString("RESTART",400,height-20);
 else if (pause1==false)offS.drawString("STOP",400,height-20);}
 //offS.drawString( "firstmvtover= "+firstmvtover+"  pause1= " +pause1, x2, height-10);
break;
case 13 : case 14 : case 15 :offS.drawImage(msgimg1,460,5,this); offS.drawImage(msgimg2,220,415,this);
   offS.drawString("  \u03BB = "+ lambda , 5, 475); offS.drawString(" distance to wall= "+L , 5, 500);
 offS.drawImage(forward,5,15,this);  offS.drawImage(backward,5,115,this); 
 offS.drawImage(forward2,5,215,this);  offS.drawImage(backward2,5,315,this);
 offS.drawImage(bouncewave,450,50,this);
if(exerciseover==false){	 offS.setColor(bluegreen);offS.fillRect(390, height-50, 120 ,40);offS.setColor(Color.yellow); 
 if (pause1==true){offS.drawString("RESTART",400,height-20);}
 else if (pause1==false){offS.drawString("STOP",400,height-20);}}
 else if (exerciseover==true){offS.setColor(bluegreen);offS.fillRect(390, height-50, 120 ,40); offS.fillRect(520, height-50, 200 ,40);//offS.fillRect(740, height-50, 200 ,40);    
    offS.setColor(Color.yellow); offS.drawString("NEXT",400,height-20);    //button next
     offS.drawString("SAME ANIMATION",530,height-20);  
	 //if(step==13){offS.drawString("MORE WAVETRAINS",750,height-20);}
	 }
 break;


case 16 ://offS.drawImage(msgimg1,360,6,this); offS.drawImage(msgimg2,width-500,100,this);
	x2=450; y2=100; x3=680;
     offS.drawString("drag this slider to change ", x2, y2-60);	
	  offS.drawString(" the distance of the string", x2, y2-30);	
		offS.drawString(" the wavelength", x3, y2-30);	
  offS.drawImage(slider, x2, y2,this); offS.drawImage(slider, x3, y2,this); //slider =205X32
	offS.setColor(Color.black); offS.fillRect(x2+L-200,y2-5, 5, 42);offS.fillRect(x3+(int)lambda/4,y2-5, 5, 42);
   	  offS.drawString("distance = "+L , x2, y2+60);	offS.drawString("\u03BB = "+ lambda, x3, y2+60);
	 offS.drawImage(forward,5,60,this);  offS.drawImage(backward,5,160,this); 
	 offS.drawImage(forward2,5,260,this);  offS.drawImage(backward2,5,360,this);
	 offS.drawImage(bouncewave,450,200,this);
	 offS.setColor(bluegreen);offS.fillRect(390, height-50, 120 ,40);offS.fillRect(520, height-50, 200 ,40);
	 offS.setColor(Color.yellow); offS.drawString("NEXT",530,height-20);
	if (pause1==true){offS.drawString("RESTART",400,height-20);}
	else if (pause1==false){offS.drawString("STOP",400,height-20);}
//	  offS.drawString("t= "+tt+ "  firstmvtover= "+firstmvtover+"  pause1= " +pause1, x2, height-10);
	
  break;
 case 17 : offS.drawImage(msgimg1,width/2-80,10,this);  offS.drawImage(msgimg2,width/2-140,80,this); 
   for(int jj=0;jj<6; jj++){offS.drawImage(scene[jj],10+(width-330)*(jj%2), 40+(jj/2)*200, this);}
        if(exerciseover==true){    offS.setColor(bluegreen);offS.fillRect(390, height-50, 140 ,40);offS.fillRect(520, height-50, 200 ,40);      //button next
    offS.setColor(Color.yellow); offS.drawString("NEXT",400,height-20);offS.drawString("SAME ANIMATION",530,height-20); }
 break;
case 18 : if(exerciseover==true){    offS.setColor(bluegreen);offS.fillRect(390, height-50, 140 ,40);offS.fillRect(520, height-50, 200 ,40);      //button next
    offS.setColor(Color.yellow); offS.drawString("NEXT",400,height-20);offS.drawString("SAME ANIMATION",530,height-20); }
	break;
case 19 : offS.drawImage(bigscene, 0, 100,this);
 if(exerciseover==true){offS.setColor(bluegreen);offS.fillRect(390, height-50, 120 ,40); offS.fillRect(520, height-50, 200 ,40);   
    offS.setColor(Color.yellow); offS.drawString("NEXT",400,height-20);    //button next
     offS.drawString("SAME ANIMATION",530,height-20);}
 break;
case 20 :  offS.drawImage(msgimg1,width/2-150,100,this);  break;
default : break;}
paint(g);
}
public void paint(Graphics g)
 {if(isrunning==true) 
  { g.drawImage(offScreenImage,0,0,this);
   }
 }
class Soundcrest 
{int to=0; int xcenter=100;
 Soundcrest(int tt, int xc)
 {to=tt; xcenter=xc;}
 public int getorigint(){return to;}
}
}
 // for(j=0;j<5;j++){ 
 //j=0; sgr[j].drawImage(graphpaper90,0,0,this); sgr[j].setColor(Color.black);//}
