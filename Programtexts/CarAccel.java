import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.awt.image.*;

/*<applet code="CarAccel.class" width=850 height=550>
</applet>
  1 tt is .05 sec, v in m/sec
  x and xo (departure of car) in pixels 4800 pix for 240 m road, d in m , 
  for each parameter measured( speed, displacement, momentum) :divy gives the units per division, unityy gives squares per unit( m, m/sec,) 
  each "square=3 pixels, e.g. if unity=1,  1 square per 1 m=3 pix;    divy *unity should be around 10-20
  unittime = unitx=4 for all 3 acccel runs:  1 sec=4[]
*/
public class CarAccel extends Applet implements MouseListener , Runnable
{ boolean isrunning=true,  pause1=true , pause2=false, compare=false; 
 int height, width, dwidth=410, dheight=400,  pauselength=30, xo=20,  x=xo, xt=0,graphasked=-1, exp=0, bigL=5000, bigH=150;
 int  depart=0,  tt=0, lastx=0, modejj=0 , po=18, qo=dheight-15, crashimgwidth,crashimgheight, noimg, obstaclecrashwidth,obstaclecrashheight, obstaclenoimg;  
 int divy[]={5,50,10000,5,50,10000,5,50,50};  //div for v, s, p=mv
 double v=0, d=0, dmax=0, a=5; double lasty[]; double unity[]={2, 0.5,0.001, 2, 0.5, 0.001, 2,1,0.5}; int unitx=4;
 int divx= 1;int ttmax[]={300,600, 190};int yo[]= {68, 69, 78,68, 69, 97, 69};
 int m[]={800,6000,800,800,6000,800,6000};double acc[]={1.8, 0.5, 5,-5,-2.5,-25,-25};  
 Thread roller; String [] veh_name={"small car", "truck", "racingcar"}; 
String[] variab={"speed(m/sec)", "displacement(m)", "momentum(kg.m/sec)","speed(m/sec)", "displacement(m)", "momentum(kg.m/sec)",  "time(sec)"};
 String msg="";
 Color lightblue= new Color(0,255,255); Color grass=new Color(0,136,40);Color green= new Color(78,214,173);
 Color bluegreen= new Color(47,166,187); Color brown= new Color(120, 98, 56);
Image bigScene, vehicle, obstacle, scene_now, commentscreen; Image [] bgitems=new Image[12]; Image graphim[]= new Image[9]; Image crashimg[];Image obstaclecrashimg[];
 Image offScreenImage; Graphics offS, bigg, offS2; //Graphics[] graphg=new Graphics[6];
Random r = new Random();
Font big = new Font("VERDANA", Font.PLAIN, 20);  

public void init()
{height=Integer.parseInt(getParameter("height")); width=Integer.parseInt(getParameter("width"));
pauselength=40;
offScreenImage=createImage(width, height-bigH); offS=offScreenImage.getGraphics();
offS.setFont(big); offS.setColor(green);offS.fillRect(0,0,width,height-bigH);
 bigScene=createImage( bigL, bigH); bigg=bigScene.getGraphics(); //vehicle=createImage(92,40);
bigg.setColor(lightblue); bigg.fillRect(0,0, bigL, 40);bigg.setColor(grass); bigg.fillRect(0,40, bigL,60);
bigg.setColor(brown); bigg.fillRect(0,100, bigL,50);
commentscreen=createImage(width, height); offS2=commentscreen.getGraphics();offS2.setFont(big);
addMouseListener(this);
int tracked=0; String name[]= new String[12]; 
try{MediaTracker tracker = new MediaTracker(this);
      StringTokenizer mst= new StringTokenizer(getParameter("img"),"$");
      while(mst.hasMoreTokens()&&tracked<12)      
	{name[tracked]=mst.nextToken();
	bgitems[tracked]=getImage(getClass().getResource(name[tracked]+".gif"));
  	tracker.addImage(bgitems[tracked], tracked);
	tracked++;showStatus(name[tracked]);}
      tracker.waitForAll();
    }catch(InterruptedException e){showStatus("No picture found");}
for (int i=0;i< bigL; i+=500) {bigg.drawImage(bgitems[0], i,0,this); bigg.drawImage(bgitems[1],i+200,0, this);}
//prepareNextRun(); repaint();
}

public Image getAlpha(Image img, int width, int height)
{Image a_img;
int[] pixels= new int[width*height];
PixelGrabber pg= new PixelGrabber(img,0,0,width,height,pixels,0,width);
try{pg.grabPixels();}catch(InterruptedException ie){};
for(int i=0; i<pixels.length; i++)
{int p = pixels[i];  int red = 0xff&(p>>16);
 int green = 0xff&(p>>8);  int blue = 0xff&(p);
if ((red>245)&&(green>245)&&(blue>245)) {pixels[i]=(0x00000000);}
}
a_img=createImage(new MemoryImageSource(width,height,pixels,0,width));
return a_img;
}
public void prepareNextRun()
{for( modejj=0; modejj<6;modejj++) {graphim[modejj]= createImage(dwidth, dheight); 
     setAxis(graphim[modejj], divy[modejj],unity[modejj]); a=acc[exp]; }graphasked=1;
lastx=0; v=0; d=0; dmax=0; tt=0; x=xo; depart=0;
lasty= new double [3]; vehicle=null;
if((exp==0)||(exp==3)||(exp==5)){vehicle=getAlpha(bgitems[2],105, 52); }  //nano
else if ((exp==1)||(exp==4)||(exp==6)) {vehicle=getAlpha(bgitems[3],174,80);}  //truck
else{vehicle=getAlpha(bgitems[4],184,70);}                        //racingcar

if((exp==5)||(exp==6))
{ obstaclecrashwidth=174; obstaclenoimg=3; obstaclecrashheight=80;
   crashimgwidth=105;  noimg=5; crashimgheight=53; xt=1345+70; 
  if (exp==6) {crashimgwidth=174;  crashimgheight=80;obstaclenoimg=5; xt=1394+120; }
  obstacle=getAlpha(bgitems[6],174,80); 
  try{CropImageFilter f; FilteredImageSource fis;
  MediaTracker t=new MediaTracker(this); crashimg=new Image[noimg];
  for(int n=0;n<noimg;n++)
    {f=new CropImageFilter(crashimgwidth*n, 0,crashimgwidth , crashimgheight);
     fis=new FilteredImageSource((bgitems[exp+2]).getSource(), f);
     crashimg[n]=getAlpha(createImage(fis),crashimgwidth, crashimgheight);
     t.addImage(crashimg[n],n);
    }
  t.waitForAll();
  }catch(InterruptedException e)
        {showStatus("Cropping went wrong");}
try{CropImageFilter f; FilteredImageSource fis;
MediaTracker t=new MediaTracker(this); obstaclecrashimg=new Image[obstaclenoimg];
for(int n=0;n<obstaclenoimg;n++)
  {f=new CropImageFilter(obstaclecrashwidth*n, 0,obstaclecrashwidth , obstaclecrashheight);
   fis=new FilteredImageSource((bgitems[exp+4]).getSource(), f);
   obstaclecrashimg[n]=getAlpha(createImage(fis),obstaclecrashwidth , obstaclecrashheight) ;
   t.addImage(obstaclecrashimg[n],n);
  }
   t.waitForAll();
 }catch(InterruptedException e)
            {showStatus("Cropping went wrong");}
}
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
 {while(exp<3) {prepareNextRun();
  while ( tt<ttmax[exp]) 
     { v=(double) a*tt/20; d=(double) a*tt*tt/800; x= (int)(xo +d*20);         if(x>( bigL-50)) {break;}
       depart=x-600; if(x<600) {depart=0;} //else if (x> ( bigL+600-width)) {depart= bigL-width;}
       double nwy=0; for(modejj=0; modejj<3; modejj++){switch(modejj){case 0: nwy=v; break;case 1: nwy=d; break;case 2: nwy=(m[exp])*v;break; default: nwy=1;}   
       addPoint(graphim[modejj], modejj, nwy);       }
       lastx=tt;    repaint();   
        try{synchronized(this){while(pause1==true) wait();}  tt++;roller.sleep(40);   } catch(InterruptedException ie){};
       }
   pause2=true;repaint(); 
   try{synchronized(this){while(pause2==true) wait();  }} catch(InterruptedException ie){};
  if(exp==3){ exp=10;}
  } 
 while(exp<5)
   {prepareNextRun();
   for(tt=0; tt<60; tt++)           //3 sec at 72 km/hr
          {a=0; v= 20; d=(double) v*tt/20; x= (int)(xo +d*20);         if(x>( bigL-50)) {break;}
            depart=x-300; if(x<300) {depart=0;} else if (x> ( bigL+300-width)) {depart= bigL-width;}
            double nwy=0; for( modejj=0; modejj<3; modejj++){switch(modejj){case 0: nwy=v; break;case 1: nwy=d; break;case 2: nwy=(m[exp])*v;break; default: nwy=1;}   
            addPoint(graphim[modejj], modejj, nwy);       }            repaint();   
           try{synchronized(this){while(pause1==true) wait();} lastx=tt; tt++;roller.sleep(40);   } catch(InterruptedException ie){};
         } 
     double V= v; double D=d; int T=tt;
    do{tt++; a=acc[exp]; v=V+a*(tt-T)/20; d=D+V*(tt-T)/20+a*(tt-T)*(tt-T)/800; x=(int)(xo+d*20);   if(x>( bigL-50)) {break;}
            depart=x-300; if(x<300) {depart=0;} else if (x> ( bigL+300-width)) {depart= bigL-width;}
            double nwy=0; for( modejj=0; modejj<3; modejj++){switch(modejj){case 0: nwy=v; break;case 1: nwy=d; break;case 2: nwy=(m[exp])*v;break; default: nwy=1;}   
            addPoint(graphim[modejj], modejj, nwy);       }           
           lastx=tt;   repaint();   
           try{synchronized(this){while(pause1==true) wait();}  roller.sleep(40);   } catch(InterruptedException ie){};
          }while(v>0);  pause2=true;repaint(); 
   try{synchronized(this){while(pause2==true) wait();  }} catch(InterruptedException ie){};
      //prepareNextRun(); 
  if(exp==5){ exp=13;}
           }
  while(exp<7)
   {prepareNextRun();
    int T=0; double D=0; double V=0; int XT;
    for(tt=0; tt<=60; tt++)           //3 sec at 72 km/hr
          {a=0; v= 20; d=(double) v*tt/20; x= (int)(xo +d*20);         if(x>( bigL-50)) {break;}
            depart=x-300; if(x<300) {depart=0;} else if (x> ( bigL+300-width)) {depart= bigL-width;} repaint();
           double nwy=0; for( modejj=0; modejj<3; modejj++){switch(modejj){case 0: nwy=v; break;case 1: nwy=d; break;case 2: nwy=(m[exp])*v;break; default: nwy=1;}   
        addPoint(graphim[modejj], modejj, nwy);       }
              repaint();   
           try{synchronized(this){while(pause1==true) wait();} lastx=tt; tt++;roller.sleep(40);   } catch(InterruptedException ie){};
         } 
      V= v;  D=d; T=tt;XT=xt;
 switch(exp)
  {case 5: 
      for(tt=62;v>0 ; tt++)
      {vehicle=(tt<75)?crashimg[(int)((tt-T)/3)]:crashimg[4];
       a=acc[exp]; v=V+a*(tt-T)/20; if(v<0){v=0;}
      xt=(int)(XT+(tt-64)*(m[exp])*V/(m[1]+m[exp]));
     showStatus("time  "+tt+" - "+T);        
     d=D+V*(tt-T)/20+a*(tt-T)*(tt-T)/800; x=(int)(D+d*20);  // if(x>( bigL-50)) {break;}
            depart=x-300; if(x<300) {depart=0;} //else if (x> ( bigL+300-width)) {depart= bigL-width;}
            double nwy=0; for( modejj=0; modejj<3; modejj++){switch(modejj){case 0: nwy=v; break;case 1: nwy=d; break;case 2: nwy=(m[exp])*v;break; default: nwy=1;}   
                                                  addPoint(graphim[modejj], modejj, nwy);       } 
          obstacle=(tt<72)?getAlpha(obstaclecrashimg[(tt-T)/4],174,80):getAlpha(obstaclecrashimg[2],174,80);
          lastx=tt;  repaint();   
         try{synchronized(this){while(pause1==true) wait();} roller.sleep(40);}catch(InterruptedException ie){}; 
       }break;
case 6 : double vmom=(m[exp])*V/(m[1]+m[exp]); 
         for(tt=62;v>=vmom ; tt++)
       {vehicle=(tt<75)?crashimg[(int)((tt-T)/3)]:crashimg[4];
             obstacle=(tt<75)?getAlpha(obstaclecrashimg[(tt-T)/4],174,80):getAlpha(obstaclecrashimg[noimg-1],174,80);
            xt=(int)(XT+(tt-72)*(m[exp])*V/(m[1]+m[exp]));
            a=acc[exp]; v=V+a*(tt-T)/20; 
             d=D+V*(tt-T)/20+a*(tt-T)*(tt-T)/800; x=(int)(D+d*20); 
            depart=x-300; if(x<300) {depart=0;} 
            double nwy=0; for( modejj=0; modejj<3; modejj++){switch(modejj){case 0: nwy=v; break;case 1: nwy=d; break;case 2: nwy=(m[exp])*v;break; default: nwy=1;}   
                                                  addPoint(graphim[modejj], modejj, nwy);       } 
          lastx=tt;
showStatus("xt-x= "+(xt-x));  repaint();   
         try{synchronized(this){while(pause1==true) wait();}  roller.sleep(40);}catch(InterruptedException ie){}; }
     V= v;  D=d; T=tt;XT=xt;
    do{ a=-8;tt++; v=V+a*(tt-T)/20;if(v<0)v=0; d=D+V*(tt-T)/20+a*(tt-T)*(tt-T)/800; x=(int)(xo+d*20);  if(x>( bigL-50)) {break;}
            depart=x-300; if(x<300) {depart=0;} xt=x+100;//else if (x> ( bigL+300-width)) {depart= bigL-width;}
            double nwy=0; for( modejj=0; modejj<3; modejj++){switch(modejj){case 0: nwy=v; break;case 1: nwy=d; break;case 2: nwy=(m[exp])*v;break; default: nwy=1;}   
                                                            addPoint(graphim[modejj], modejj, nwy);       }           
          lastx=tt;   repaint();   
           try{synchronized(this){while(pause1==true) wait();}  roller.sleep(40);   } catch(InterruptedException ie){};
                }while(v>0);
            break;
default: }
//showStatus("out of do loop");   
pause2=true; repaint(); 
 try{synchronized(this){while(pause2==true) wait();  }} catch(InterruptedException ie){};
}
if(exp==7){msg="When a moving car collides with a massive object, deceleration occurs over a small distance (the crumble-zone of the car), and  deceleration is very large. Example : if a car, driving at 36 km/hr (=10 m/sec) hits a wall, and the crumble zone is 1 m, the deceleration is a=50 m/sec² (10²-0² =2*a*1). Only a seatbelt can keep you from flying through the windshield.";
pause2=true; repaint(); 
 try{synchronized(this){while(pause2==true) wait();  }} catch(InterruptedException ie){};
exp=8;}
 if(exp==8){msg="END OF THE EXERCISE.  See you, in the meanwhile, don't forget your seatbelt"; repaint();    isrunning=false; }

 if(exp==10){ msg=" GRAPHIC OF SPEED VERSUS TIME    What does a constant acceleration of 1.8 m/sec² mean? Speed is 1.8 m/sec after 1 sec, 3.6 m/sec after 2 sec, 5.4 m/sec after 3 sec etc. We use the formula v=a * t; for different accelerations (each constant over time), we get straight lines with different slopes. The higher the acceleration the steeper the slope";
 graphim[3]= createImage(dwidth, dheight); 
     setAxis(graphim[3], divy[3],unity[3]); drawComparison(graphim[3], 3); repaint(); pause2=true; 
 try{synchronized(this){while(pause2==true) wait();  }} catch(InterruptedException ie){}; }
if (exp==11){msg="GRAPH OF DISTANCE VERSUS TIME. All 3 curves have the same shape, curved upwards. This means that e.g. during the second second the car covers more distance than during the first second. Why ? because speed has increased";
 graphim[4]= createImage(dwidth, dheight); 
     setAxis(graphim[4], divy[4],unity[4]); drawComparison(graphim[4], 4); repaint(); pause2=true; 
 try{synchronized(this){while(pause2==true) wait();  }} catch(InterruptedException ie){}; }
if(exp==12){
msg="In the SPEED versus TIME graph we have coloured the rectangle of average speed x 1 second e.g. the blue rectangle's area represents the distance covered during the fourth second with an average speed of 6.3 m/sec. Remember s = v x t if v constant";
msg = msg+"In the DISTANCE versus TIME graph, we add that rectangle (=distance) to the distance already covered during the past seconds.";
graphim[6]=createImage(dwidth, dheight); 
graphim[7]=createImage(dwidth, dheight);  
setAxis(graphim[6], divy[6], unity[6]); setAxis(graphim[7], divy[7], unity[7]);repaint();
addColoredRect();
pause2=true; repaint(); 
 try{synchronized(this){while(pause2==true) wait();  }} catch(InterruptedException ie){};
exp=3; msg="";}//prepareNextRun();
if(exp==13){msg=""; graphim[3]=createImage(dwidth, dheight);  
setAxis(graphim[3], divy[3], unity[3]); drawComparison(graphim[3],3);
graphim[4]=createImage(dwidth, dheight);  
setAxis(graphim[4], divy[4], unity[4]);  drawComparison(graphim[4],4); repaint(); pause2=true;
 try{synchronized(this){while(pause2==true) wait();  }} catch(InterruptedException ie){};}
if(exp==14){
msg="why do we apply the same formula for deceleration as for acceleration? During the first 3 seconds there is neither acceleration nor deceleration, the distance increases linearly as s=vo*t. When brakes are on , the difference between the straight line vo*t and the distance covered (=blue area) increases more and more as a*t²/2. It is the lack of distance covered due to deceleration that follows the law a*t²/2. Total s= vo* t+a*t²/2 (with a< 0)"; modejj=1; repaint();
graphim[8]=createImage(dwidth, dheight);  
setAxis(graphim[8], divy[8], unity[8]);repaint();
decelerationgraph(graphim[8]);repaint();
pause2=true; repaint(); 
 try{synchronized(this){while(pause2==true) wait();  }} catch(InterruptedException ie){};
 exp=5;}
}}

public Image drawMessage( String s, int widthRect, int rectheight)
{  FontMetrics fm; int y=0;int xrect=5;   
  Image im=createImage(widthRect, rectheight); Graphics img=im.getGraphics();
   img.setColor(bluegreen); img.fillRect(0,0,widthRect, rectheight);
   img.setFont(new Font("Verdana", Font.PLAIN, 16)); fm=img.getFontMetrics();
   int linel=fm.stringWidth(s);
   img.setColor(Color.yellow);
  int lineheight=fm.getHeight();
  if(linel<widthRect-2)
          {img.drawString(s,xrect, y+80);}
    else
           {int spacelength = fm.stringWidth(" ");
            y=y+lineheight; int wl=0; linel=0;		                //length of the text
            s=s+" $";				          //$ is added to detect end of text
             StringTokenizer tt=new StringTokenizer(s);
             String word, linee="";    		              	//contains the text
             while(tt.hasMoreTokens())
	{ word= tt.nextToken();
	   if (word.equals("$"))   {img.drawString (linee,xrect, y);}
	  else    {  wl=fm.stringWidth(word);
	  linel=linel+spacelength+wl;
	   if (linel>widthRect-2)
	         {img.drawString (linee, xrect, y); 
	           y=y+lineheight+2; linel=wl; linee=word;
	         }
	      else
	        {linee=linee+" "+word;}
	     }
	}//end of while	
            } // end of else
return im;
}  

public void mouseReleased(MouseEvent me){}
public void mousePressed(MouseEvent me){}

synchronized public void mouseClicked(MouseEvent me)
 { int coX=me.getX(); int coY=me.getY(); 
    if(exp<7)
 {if((coY>height-80-bigH)&&(coX<160))
            {//if(tt==0) {isrunning=true; roller.start();  repaint();} else
             if (pause1==false)     { pause1=true; notify(); }
             else if (pause1==true) { pause1=false; notify(); }
             if(pause2==true) { exp++; pause2=false; notify();}
	}
  else if((coY>height-80-bigH)&&(coX<260))   // redo same exercise
           { if(pause2==true) {pause2=false;notify();}
           }
   else if ((coX>width-100)&&(coY<(bigH+250)))
      {if (coY<(bigH+130))  {graphasked=0;}  
       else if (coY<(bigH+190)) {graphasked=1;}
       else if (coY<(bigH+250)) {graphasked=2;}
          //   else {compare=!compare;}     
         //if((compare==true)&&(exp<5)) drawComparison(graphim[graphasked], (exp+3)); 
     }}
else if ((exp>6)&&(coX>(width-200))&&(coY> (height-100))){ exp++; pause2=false; notify();}
}

public void mouseEntered(MouseEvent me){}
public void mouseExited(MouseEvent me){}

public void update(Graphics g)
{showStatus("exp  "+exp);
if(exp<7){ bigg.setColor(grass); bigg.fillRect(0,69,  bigL,31);bigg.setColor(brown); bigg.fillRect(0,100,  bigL,50);
if(exp==3){bigg.drawImage(getAlpha(bgitems[5],33,80), 2105, 30, this);}   //trafficlight
else if(exp==4){bigg.drawImage(getAlpha(bgitems[5],33,80), 2935, 30, this);}
else if (exp>=5){bigg.drawImage(obstacle, xt , 69, this);}  //opposite truck
//else if (exp==6){bigg.drawImage(getAlpha(bgitems[6],174,80), 1354, 70, this);}
 bigg.setColor(lightblue);bigg.fillRect(80,7,450,26);bigg.setColor(Color.black);bigg.setFont(big); 
 switch(exp)
 { case 0 : case 1 : case 2 : bigg.drawString("UNIFORM ACCELERATION", 105, 30);break;
    case 3 : case 4 : bigg.drawString(" DECELERATION= negative acceleration", 85, 30);break; 
    case 5 : case 6 : bigg.drawString("COLLISION", 105, 30);break;
    default : break; }
bigg.drawImage(vehicle, x,yo[exp],this); 
CropImageFilter f;
FilteredImageSource fis;
f=new CropImageFilter(depart, 0, width, 150);
   fis=new FilteredImageSource(bigScene.getSource(), f);
scene_now=createImage(fis);
offS.setColor(green); offS.fillRect(0,0,width,height-150); offS.setColor(Color.black);   
offS.drawString("Time : ", 20,40); offS.drawString(""+(double)tt/20, 200,40); offS.drawString(" sec", 260,40);
offS.drawString("Mass : ", 20,100); offS.drawString(""+m[exp]+"   kg", 200,100);
offS.drawString("Acceleration : ", 20,160); offS.drawString(""+a, 200,160);offS.drawString(" m/sec²", 260,160);//offS.drawString("2", 320,155);
offS.drawString("Speed: ", 20, 220);int vv=(int)(v*100); offS.drawString(""+(double)vv/100, 200,220);   //to avoid too many decimals
offS.drawString("  m/sec", 260,220);	vv=(int)(v*3.6*100); offS.drawString(""+(double)vv/100, 200,250); offS.drawString(" km/hr", 260,250);
offS.drawString("Displacement", 20,300);int dd=(int)(d*100); offS.drawString(""+(double)dd/100+"  m",  200, 300);
offS.setColor(bluegreen); offS.fillRect(40, height-80-bigH, 100,80);offS.fillRect(160, height-80-bigH,100,80);//offS.fillRect(width-110, 200, 80, 50); 
offS.fillRect(width-110, 80, 80, 50); offS.fillRect(width-110, 140 ,80,50); offS.fillRect(width-110, 200,80,50);  //buttons for getting graphics
offS.setColor(Color.yellow); //if((compare==false)&&(exp<5)){ offS.drawString("compare", width-110,240);} else if(compare==true){ offS.drawString("runtime", width-110,240);}
offS.drawString("GRAPHS", width-120,60); offS.drawString("v/t", width-100,110); offS.drawString("s/t", width-100,160); offS.drawString("mv/t", width-100,230);
 if (tt==0) {offS.drawString("START ", 50,height-30-bigH);}
else if(pause2==true){offS.drawString("NEXT",  50,height-40-bigH);offS.drawString("SAME",  170, height-40-bigH); }
else if(pause1==false)offS.drawString("STOP ", 50,height-40-bigH);
else {offS.drawString("RESUME ",  50,height-30-bigH);}
if(graphasked>-1)  {offS.drawImage(graphim[graphasked], width-520, 0, this);}
}else if (exp==10){offS2.setColor(green); offS2.fillRect(0,0,width,height); 
  offS2.setColor(bluegreen); offS2.fillRect(width-200, height-100, 180,60);offS2.setColor(Color.yellow); offS2.drawString("Continue",  width-195, height-70);
  offS2.drawImage(drawMessage(msg,450,400),500,20,this);offS2.drawImage(graphim[3],20,20,this);}
else if (exp==11){offS2.setColor(green); offS2.fillRect(0,0,width,height); 
  offS2.setColor(bluegreen); offS2.fillRect(width-200, height-100, 180,60);offS2.setColor(Color.yellow); offS2.drawString("Continue",  width-195, height-70);
  offS2.drawImage(drawMessage(msg,450,400),500,20,this);offS2.drawImage(graphim[4],20,20,this);}
else if (exp==12){offS2.setColor(green); offS2.fillRect(0,0,width,height); 
  offS2.setColor(bluegreen); offS2.fillRect(width-200, height-100, 180,60);offS2.setColor(Color.yellow); offS2.drawString("Continue",  width-195, height-70);
  offS2.drawImage(drawMessage(msg,800,250),20,400,this);
  offS2.drawImage(graphim[6],20,1,this);offS2.drawImage(graphim[7],450,1,this);}
else if (exp==13){offS2.setColor(green); offS2.fillRect(0,0,width,height); 
  offS2.setColor(bluegreen); offS2.fillRect(width-200, height-100, 180,60);offS2.setColor(Color.yellow); offS2.drawString("Continue",  width-195, height-70);
  //offS2.drawImage(drawMessage(msg,800,250),20,400,this);
  offS2.drawImage(graphim[3],20,1,this);offS2.drawImage(graphim[4],450,1,this);}
else if (exp==14){offS2.setColor(green); offS2.fillRect(0,0,width,height); 
  offS2.setColor(bluegreen); offS2.fillRect(width-200, height-100, 180,60);offS2.setColor(Color.yellow); offS2.drawString("Continue",  width-195, height-70);
  offS2.drawImage(drawMessage(msg,800,250),20,400,this);
  offS2.drawImage(graphim[8],20,1,this);}
else if (exp==7){offS2.setColor(green); offS2.fillRect(0,0,width,height); 
  offS2.setColor(bluegreen); offS2.fillRect(width-200, height-100, 180,60);offS2.setColor(Color.yellow); offS2.drawString("Continue",  width-195, height-70);
  offS2.drawImage(drawMessage(msg,800,250),20,200,this);
  }
else if (exp==8){//offS2.setColor(green); offS2.fillRect(0,0,width,height); offS2.setColor(Color.black);
     offS2.drawImage(drawMessage(msg,800,250),20,200,this);
 offS2.setFont(new Font("Verdana", Font.PLAIN, 16));offS2.setColor(Color.black);
 offS2.drawString("Program written by annemarie.govindraj@gmail.com", 200,550);}
 
paint(g);
}

public void setAxis(Image im, int divy, double uny)
  { int previousx=0; double previousy=0; 
     Graphics pg= im.getGraphics(); pg.setFont(new Font("VERDANA", Font.PLAIN, 16)); 
     pg.setColor(Color.orange);
       for(int i=0; i<dwidth; i+=3)   {pg.drawLine(i,0,i,dheight);}
      for(int j=0; j<dheight; j+=3)   {pg.drawLine(0,j, dwidth,j);}
    pg.setColor(Color.black); pg.drawLine( po, qo, dwidth-1, qo); pg.drawLine(po,2, po, qo);
    for(int p=0, i=0; p<dwidth-po; p+=3*divx*unitx, i++){pg.drawLine(p+po, qo,p+po, qo+3);
    if(i%5==0){if(i<6){  pg.drawString(""+i*divx, p+po-4, dheight-1);}else  {pg.drawString(""+i*divx, p+po-8, dheight-1);}}} 

    pg.drawString(""+variab[6], dwidth/2 , qo-2); 
    pg.drawString(""+variab[modejj], 20, 16);   //axis labelling
     if(uny!=0){  for(int q=qo, i=0; q>0; q-=divy*3*uny, i++)
   {pg.drawLine(po-3, qo-(int)(3*i*divy*uny), po, qo-(int)(3*i*divy*uny)); if(i%2==0){pg.drawString(""+i*divy, 1, qo+4-(int)(3*i*divy*uny) );}} } 
  
}
public void addPoint (Image im, int jj, double nwy)
{Graphics pg= im.getGraphics();pg.setColor(Color.black); 
pg.drawLine((int)(po+(double)lastx*unitx*3/20), (int)(qo-lasty[jj]*3*unity[jj]), (int)(po+(double)tt*unitx*3/20), (int)(qo-nwy*unity[jj]*3)); 
     lasty[jj]=nwy;  repaint();
} 

public void drawComparison(Image im, int jk)
{ Graphics pg= im.getGraphics();pg.setColor(Color.black);  pg.setFont(new Font("VERDANA", Font.PLAIN, 16)); 
  if((exp<3)||(exp==10)||(exp==11)){ for (int veh=0; veh<3; veh++){int X=0,Y=0,previousX=0, previousY=0; double time=0;
      for (time=0; time<30; time+=0.0834) {X=(int)(time*3*4);
         switch(jk)
	{case 3 : Y=(int)(3*acc[veh]*time*unity[jk]); break;
               case 4 : Y=(int)(3*acc[veh]*time*time*unity[jk]/2); break;
	  case 5 : Y=(int)(3*m[veh]*acc[veh]*time*unity[jk]); break;
               default : Y=1;
             }
         if(Y<385){pg.drawLine(po+previousX, qo-previousY, po+X, qo-Y);}
         previousX=X; previousY=Y;
         if((X==150)&&(Y>300)){pg.drawString(veh_name[veh], po+100, qo-300);}
        else  if(X==200){pg.drawString(veh_name[veh], po+X+10, qo-Y+10); }
     } repaint();
}   //next vehicle
}else if ((exp<5)||(exp==13))
{ for (int veh=0; veh<2; veh++){
 int Y=0, previousX=0, previousY=0; double time=0;
if(jk==3){  previousY=  (int)(3*20*unity[jk]); }   else if (jk==5) {previousY=(int)(3*m[veh]*20*unity[jk]);}
 int   X=(int)(3*3*4);
  switch(jk){case 3 : Y=(int)(3*20*unity[jk]); break;
                 case 4 : Y=(int)(3*20*3*unity[jk]); break;
	    case 5 :   Y=(int)(3*m[veh]*20*unity[jk]); break;
                 default : Y=1;
               }
     pg.setColor(Color.black); pg.drawLine(po+previousX, qo-previousY, po+X, qo-Y); 
    int YT=Y; previousX=X; previousY=Y;
     for (time=3; time<15; time+=0.0834) {X=(int)(time*3*4);  ///why 0.0834 ?
        if((20+(acc[veh+3])*(time-3))<0) break;
            switch(jk)
	{case 3 : Y=YT+(int)(3*acc[veh+3]*(time-3)*unity[jk]); break;
               case 4 : Y=YT+(int)(3*20*(time-3)*unity[jk]+(0.5*3*acc[veh+3]*(time-3)*(time-3)*unity[jk])); break;
	  case 5 : Y=YT+(int)(3*m[veh]*acc[veh+3]*(time-3)*unity[jk]); break;
               default : Y=1;
             }
         if(Y<385){pg.drawLine(po+previousX, qo-previousY, po+X, qo-Y);}
         previousX=X; previousY=Y;
     if((X==80)&&(Y>360)){pg.drawString(veh_name[veh], po+60, qo-300);}
        else  if(X==80){pg.drawString(veh_name[veh], po+X+10, qo-Y+10); }
     } repaint();
}   //next vehicle
} }
public void addColoredRect()
{Color graphColors[]={Color.orange, Color.red, Color.yellow, Color.green, Color.blue};
Image minigraph=createImage(unitx*3, qo);Graphics minigr=minigraph.getGraphics();
  minigr.setColor(Color.orange);
       for(int i=0; i<12; i+=3)   {minigr.drawLine(i,0,i,qo);}
      for(int j=0; j<qo; j+=3)   {minigr.drawLine(0,j, 12,j);}
Graphics colgr6=graphim[6].getGraphics(); Graphics colgr7=graphim[7].getGraphics(); colgr7.setFont(new Font("Dialog", Font.BOLD, 16)); 
 int  time=0;
int X=0, previousX=0, previousY6=0, previousY7=0, Y6=0, Y7=0, previousavy7=0;
for( time=0; time<14; time++) 
   {X=time*3*4;
     Y6=(int)(acc[0]*time);     //Y7=(int)(3*acc[0]*time*time*unity[7]/2);
     colgr6.setColor(graphColors[time%5]);  minigr.setColor(graphColors[time%5]);
     int avy6=(int)((previousY6+Y6)/2);     int avy7=previousavy7+avy6;  
 int[] polygonX={po+previousX, po+X,po+X,po+previousX};
 int [] polygonY={qo,qo, qo-(int)(avy6*3*unity[6]),qo-(int)(avy6*3*unity[6])};
 colgr6.fillPolygon(polygonX, polygonY, 4);
int [] minipolygonX={0, 3*4 , 3*4,0};
     int [] minipolygonY={qo-(int)(previousavy7*3*unity[7]), qo-(int)(previousavy7*3*unity[7]), qo-(int)(avy7*3*unity[7]), qo-(int)(avy7*3*unity[7])};
 minigr.fillPolygon(minipolygonX, minipolygonY, 4);
 minigr.setColor(Color.black); minigr.drawPolygon(minipolygonX, minipolygonY, 4);
colgr7.drawImage(minigraph, po+previousX, 0,this); 
 if(time<2){colgr7.setColor(Color.black); colgr7.drawLine(po,2, po, qo);   
  for(int q=qo, i=0; q>0; q-=divy[7]*3*unity[7], i++)
   {//colgr7.drawLine(po-3, qo-(int)(3*i*divy[7]*unity[7]), po, qo-(int)(3*i*divy[7]*unity[7])); 
   //if(i%2==0){
   colgr7.drawString(""+i*divy[7], 0, qo+4-(int)(3*i*divy[7]*unity[7]) );}} //} 
 repaint();
 try{roller.sleep(200);} catch(InterruptedException ie){};
  previousX=X; previousY6=Y6; previousY7=Y7;previousavy7=avy7;}
X=0; previousX=0; previousY6=0; previousY7=0; Y6=0; Y7=0;
colgr6.setColor(Color.black); colgr7.setColor(Color.black); 
   for (double tim=0; tim<30; tim+=0.0834) 
   {X=(int)(tim*3*4);
          Y6=(int)(3*acc[0]*tim*unity[6]); 
          Y7=(int)(3*acc[0]*tim*tim*unity[7]/2);
    if(Y6<385){colgr6.drawLine(po+previousX,qo-previousY6, po+X,qo-Y6);}
    if(Y7<385){colgr7.drawLine(po+previousX, qo-previousY7, po+X, qo-Y7);}

showStatus("Y6"+Y6); repaint(); 
// try{roller.sleep(30);} catch(InterruptedException ie){};
  previousX=X; previousY6=Y6; previousY7=Y7;}
}
public void decelerationgraph( Image phim)
{Graphics pg=phim.getGraphics(); int X=0,previousX=0, Y=0, previousY=0;
  pg.setColor(Color.black);pg.drawLine(po,qo,po+10*4*3, qo-(int)(10*20*unity[8]*3));
repaint();try{roller.sleep(1000);}catch(InterruptedException ie){};
   int  YT=(int)(3*20*3*unity[4]); 
    previousY=YT; previousX=3*3*4;
     for (double time=3; time<15; time+=0.0834)
        {X=(int)(time*3*4);  ///why 0.0834 ?
          if((20+(acc[4])*(time-3))<0) break;
          Y=YT+(int)(3*20*(time-3)*unity[4]+(0.5*3*acc[4]*(time-3)*(time-3)*unity[4]));
          if(Y<385){pg.setColor(Color.black);pg.drawLine(po+previousX, qo-previousY, po+X, qo-Y);
              pg.setColor(Color.blue); pg.drawLine(po+(int)(time*4*3), qo-(int)(time*20*unity[4]*3),po+(int)(time*4*3), qo-Y);}
         
       repaint(); try{roller.sleep(100);}catch(InterruptedException ie){}; previousX=X; previousY=Y;}
}

public void paint(Graphics g)
 { if(exp<7)
    {g.drawImage(scene_now, 0,0,this);if((exp>2)&&(tt>=60)&&(v>0)){g.setColor(Color.red);g.setFont(big);g.drawString("BRAKES ON !", 300, 50);}
//  for(int i=1; i<3; i++) g.drawImage(obstaclecrashimg[i],10+i*200,67,this); 
g.drawImage(offScreenImage, 0, bigH,this);}
  else { g.drawImage(commentscreen, 0, 0,this);}
 }}