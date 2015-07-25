import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.awt.image.*;
import java.io.*;


public class FillFractions extends Applet  implements Runnable, MouseListener, MouseMotionListener
/*mode=1 cutting apple,cake,putting mangoes in basket; mode2 =filling mangoes, apples,; mode 3= filling squares, circles*/

{boolean isrunning=true , newquestion=false;
 int height, width,pauselength,xo=160,x1=80, x2=700, yo=150, y1=100,tile=125, radius=180, squarewidth=360, picturewidth, pictureheight; 
  int  index, totalitems, mode=1, nbfractions, step=1, dragX=-1, dragY=-1,  colorChosen, partsAsked, partsTaken;
  Image offScreenImage,mypicture=null, knife1,knife2 ,knife3, knife4, panel, mangogreen, basket, bigbasket, correctimg, wrongimg,evalimg; 
  Graphics offS,mypictureg, panelg;
 Thread wind; int[] taken;
boolean pause1=false, colorPicked=false, isbusy=false;
 Color lightblue= new Color(0,250,252); Color chosenColor;
int piececenter[]; int PosX[]; int PosY[];
int pictpix[]; String s[];
Image [] bigappl= new Image[16]; Image [] cake= new Image[11];  
Random r = new Random();
Color [] ColorChoice ={new Color(228,85,228), new Color(255,255,0),new Color(0,0,255),new Color(255,0,0),new Color(0,255,0), new Color(250,184,184)};
int [] colorarray={0xffee55ee,0xffffff00, 0xff0000ff,0xffff0000, 0xff00ff00, 0xfffab8b8};
 String quizItem[]= new String[100]; String mytext, msg=""; 
Font bigfont=new Font("Verdana", Font.BOLD, 24); 
Font textfont=new Font("Verdana", Font.PLAIN, 20); 

public void init()
{height=Integer.parseInt(getParameter("height"));width=Integer.parseInt(getParameter("width"));
//pauselength=Integer.parseInt(getParameter("sleep"));
offScreenImage=createImage(width, height); offS=offScreenImage.getGraphics();
offS.setColor(lightblue); offS.fillRect(0,0,width,height); 
showStatus("Program written by annemarie.govindraj@gmail.com");
mytext=getParameter("text");
mypicture=createImage(400, 400); mypictureg=mypicture.getGraphics();
panel=createImage(600, height); panelg=panel.getGraphics();
addMouseListener(this);addMouseMotionListener(this);
///// loading pictures+cutting+storing                                                
int tracked =0;String imgname[]= new String[11]; Image img[]=new Image[11];
try{MediaTracker tracker = new MediaTracker(this);
      StringTokenizer mst= new StringTokenizer(getParameter("picture"),"$");
      while(mst.hasMoreTokens()&&tracked<15)      
	{imgname[tracked]=mst.nextToken();
	img[tracked]=getImage(getClass().getResource(imgname[tracked]+".gif"));
  	tracker.addImage(img[tracked], tracked);
	tracked++;}
Image bigapples=img[0]; Image cake6p=img[1];
mangogreen=getAlpha(img[5],66,94);
basket=getAlpha(img[6], 142,98); bigbasket=getAlpha(img[7], 218,212);
correctimg=getAlpha(img[9],104,77); wrongimg=getAlpha(img[10],77,60);
int tw= 900; //(bigapples.getWidth(null));
int th=  476; //(bigapples.getHeight(null));
int x, y,i;
CropImageFilter f3, f4,f2,ff;
FilteredImageSource fis3, fis4, fis2,ffis;
MediaTracker t=new MediaTracker(this);
for (x=0, i=0;x<3;x++, i++)
  {f3=new CropImageFilter((int)(tw*x/3), 0, (int)(tw/3), 292);
   fis3=new FilteredImageSource(bigapples.getSource(), f3);
   bigappl[i]=createImage(fis3);
   t.addImage(bigappl[i],i);
   t.waitForID(i);
 }
for (x=0, i=3;x<4;x++, i++)
  {f4=new CropImageFilter((int)(tw*x/4), 292,(int)(tw/4), 184);
   fis4=new FilteredImageSource(bigapples.getSource(), f4);
   bigappl[i]=createImage(fis4);
   t.addImage(bigappl[i],i);
   t.waitForID(i);
 }
 for (x=0 ;x<3;x++)
{ f2=new CropImageFilter(376+x*173, 476,173 , 275);
   fis2=new FilteredImageSource(bigapples.getSource(), f2);
   cake[1+x]=createImage(fis2); 
   t.addImage(cake[1+x],x);
   t.waitForID(x);}
for (x=0, i=4;x<6;x++, i++)
  {ff=new CropImageFilter(140*x, 0, 140, 205);
   ffis=new FilteredImageSource(cake6p.getSource(), ff);
   cake[i]=createImage(ffis);
   t.addImage(cake[i],x);
   t.waitForID(x);
 }
  f2=new CropImageFilter(0, 476, 376, 275);   /////the whole cake  with white background
   fis2=new FilteredImageSource(bigapples.getSource(), f2);
  bigappl[7]=createImage(fis2);
   t.addImage(bigappl[7],1);
   t.waitForID(1);

 t.waitForAll();
}catch(InterruptedException e){showStatus("Cropping went wrong");}
Image a_bigappl=getAlpha(bigappl[0],300,292); bigappl[0]=a_bigappl;
knife2=getAlpha(img[4],163,206); knife1=getAlpha(img[2],300,38);knife3=getAlpha(img[3],108,206);knife4=getAlpha(img[8],132,36);
cake[0]=getAlpha(bigappl[7], 386,275);totalitems=0; index=0; 
//String inLine=null;
StringTokenizer   t = new StringTokenizer(mytext,"%");  
         while (t.hasMoreTokens()) {quizItem[totalitems] = t.nextToken(); totalitems++; }
// try{FileReader instream= new FileReader(mytext);
//  BufferedReader dataStream = new BufferedReader(instream);
//  while ((inLine=dataStream.readLine())!=null)
//       {quizItem[totalitems]=inLine;totalitems++;}
//     }catch(IOException e){showStatus ("error in data reading");}
repaint();}

public void start()
{if(wind==null)  {wind=new Thread(this);}
  isrunning=true;wind.start();}
public void stop()
{if(wind!=null)  { isrunning=false; wind=null;} } 

public void run()
{while (isrunning==true)
     {if(mode==1)
  {panelg.setColor(lightblue); panelg.fillRect(0,0,600,height);
    panelg.setFont(bigfont);  
showStatus("     ");
panelg.drawImage(bigappl[0], 150,50,this);    //one whole apple
       panelg.drawImage(knife2, 145,70,200,250,this); repaint(); pause1=true;
        try {  synchronized(this){while(pause1==true) wait();}  }catch(InterruptedException e){}; 
for(int i=0;i<3;i++){panelg.setColor(lightblue); panelg.fillRect(0,0,600,height);
       panelg.drawImage(bigappl[0], 150,50,this);    //////move the knife above apple
       panelg.drawImage(knife2, 145,70+i*5,200,250,this); repaint(); 
       try{wind.sleep(40);}catch(InterruptedException e){};
      }    try{wind.sleep(100);}catch(InterruptedException e){};
for(int i=3;i<34;i++){panelg.setColor(lightblue); panelg.fillRect(0,0,600,height);
       panelg.drawImage(bigappl[0], 150,50,this);panelg.setColor(Color.black); 
       panelg.drawLine((150+148),(50+ 72),298,152+i*5        );  //move the knife in apple
       panelg.drawImage(knife3, 162,70+i*5,135,250,this); repaint(); 
       try{wind.sleep(40);}catch(InterruptedException e){};
      }    try{wind.sleep(100);}catch(InterruptedException e){};
 
   panelg.setColor(lightblue); panelg.fillRect(0,0,600,height);
       panelg.drawImage(bigappl[1], 0,100,this);   //two halves apple
       panelg.drawImage(bigappl[2],300,100,this);
      panelg.setColor(Color.black);  panelg.drawString("Cut into 2 equal parts", 120,80); repaint(); pause1=true;
       try {  synchronized(this){while(pause1==true) wait();}  }catch(InterruptedException e){}; 
     panelg.setColor(lightblue); panelg.fillRect(0,0,600,height);
       panelg.drawImage(bigappl[1], 150,100,this);   //one half
         panelg.setColor(Color.black);  panelg.drawString("1 half  = ", 200,80); 
       panelg.drawImage(drawFraction(1,2), 350,30,this);
       repaint(); pause1=true;
       try {  synchronized(this){while(pause1==true) wait();}  }catch(InterruptedException e){}; 
     panelg.setColor(lightblue); panelg.fillRect(0,0,600,height);
       panelg.drawImage(bigappl[1], 0,100,this);   //two halves apple
       panelg.drawImage(bigappl[2],300,100,this);
      panelg.setColor(Color.black);  panelg.drawString(" 2 halves  = ", 120,80);   panelg.drawImage(drawFraction(2,2), 350,30,this);
     repaint(); pause1=true;
       try {  synchronized(this){while(pause1==true) wait();}  }catch(InterruptedException e){}; 
 panelg.setColor(lightblue); panelg.fillRect(0,0,600,height);
       panelg.drawImage(bigappl[0], 150,50,this);  //one whole apple
       panelg.drawImage(knife1,150,82,450, 38,this);
       panelg.drawImage(knife2,145,70,200,250,this);
        repaint(); pause1=true;
       try {  synchronized(this){while(pause1==true) wait();}  }catch(InterruptedException e){}; 
for(int i=0;i<3;i++){panelg.setColor(lightblue); panelg.fillRect(0,0,600,height);  //move knife2
       panelg.drawImage(bigappl[0], 150,50,this);     panelg.drawImage(knife1,150,82,450,38,this);
       panelg.drawImage(knife2, 145,70+i*5,200,250,this); repaint(); 
       try{wind.sleep(40);}catch(InterruptedException e){};
      }    try{wind.sleep(100);}catch(InterruptedException e){};
for(int i=3;i<34;i++){panelg.setColor(lightblue); panelg.fillRect(0,0,600,height);
       panelg.drawImage(bigappl[0], 150,50,this); panelg.setColor(Color.black); 
       panelg.drawLine((150+148),(50+ 72),298,152+i*5        );  
        panelg.drawImage(knife1,150,82,450,38,this);    
       panelg.drawImage(knife3, 162,70+i*5,135,250,this); repaint(); 
       try{wind.sleep(40);}catch(InterruptedException e){};
      }    try{wind.sleep(100);}catch(InterruptedException e){};
for(int i=0;i<40;i++){panelg.setColor(lightblue); panelg.fillRect(0,0,600,height);   //move other knife1
     panelg.drawImage(knife1,150,82+5*i,450,38,this);   panelg.drawImage(bigappl[0], 150,50,this);  
       panelg.drawImage(knife3, 162,235,135,250,this);
	   panelg.setColor(Color.black);  panelg.drawLine((150+148),(50+ 72),298,317);   repaint(); 
       try{wind.sleep(30);}catch(InterruptedException e){};
      }    try{wind.sleep(100);}catch(InterruptedException e){};

  panelg.setColor(lightblue); panelg.fillRect(0,0,600,height);
       panelg.drawImage(bigappl[3], 75,82,this);   //4  1/4 apple
       panelg.drawImage(bigappl[4],300,82,this);
       panelg.drawImage(bigappl[5], 75,266,this);   //4  1/4 apple
       panelg.drawImage(bigappl[6],300,266,this);
       panelg.setColor(Color.black); panelg.drawString("Cut into 4 equal parts", 120,80); repaint(); pause1=true;
       try {  synchronized(this){while(pause1==true) wait();}  }catch(InterruptedException e){}; 
     panelg.setColor(lightblue); panelg.fillRect(0,0,600,height);
       panelg.drawImage(bigappl[4], 300,82,this);
        panelg.setColor(Color.black); panelg.drawString("1 quarter or 1 fourth  =  ", 125,80); 
       panelg.drawImage(drawFraction(1,4), 450,40,this);
       repaint(); pause1=true;
       try {  synchronized(this){while(pause1==true) wait();}  }catch(InterruptedException e){}; 
      panelg.setColor(lightblue); panelg.fillRect(0,0,600,82);
         panelg.drawImage(bigappl[5],75,266,this);
         panelg.setColor(Color.black); panelg.drawString("2 fourth  = ", 250,80);     panelg.drawImage(drawFraction(2,4), 450,40,this);
          repaint(); pause1=true;
       try {  synchronized(this){while(pause1==true) wait();}  }catch(InterruptedException e){}; 
      panelg.setColor(lightblue); panelg.fillRect(0,0,600,82);
       panelg.drawImage(bigappl[6],300,266,this);
       panelg.setColor(Color.black); panelg.drawString(" 3 fourth  =", 250,80);      panelg.drawImage(drawFraction(3,4), 450,40,this);
     repaint(); pause1=true;
       try {  synchronized(this){while(pause1==true) wait();}  }catch(InterruptedException e){}; 
    panelg.setColor(lightblue); panelg.fillRect(0,0,600,82);
       panelg.drawImage(bigappl[3],75, 82,this);
       panelg.setColor(Color.black); panelg.drawString(" 4 fourth  = ", 250,80);      panelg.drawImage(drawFraction(4,4), 450,40,this);
       repaint(); pause1=true;
       try {  synchronized(this){while(pause1==true) wait();}  }catch(InterruptedException e){}; 

panelg.setColor(lightblue); panelg.fillRect(0,0,600,height);                                         //cake with still knives
       panelg.drawImage(cake[0], 150,100,this);                                                   //one whole cake
       panelg.drawImage(knife2, 133,167,200,250,this);panelg.drawImage(knife2, 237,167,200,250,this); repaint(); pause1=true;
        try {  synchronized(this){while(pause1==true) wait();}  }catch(InterruptedException e){}; 
for(int i=0;i<5;i++){panelg.setColor(lightblue); panelg.fillRect(0,0,600,height);
       panelg.drawImage(cake[0], 150,100,this);   
       panelg.drawImage(knife2, 133,167+i*5,200,250,this); repaint(); 
       panelg.drawImage(knife2, 237,167+i*5,200,250,this); repaint(); 
       try{wind.sleep(40);}catch(InterruptedException e){};
      }    try{wind.sleep(100);}catch(InterruptedException e){};
for(int i=5;i<16;i++){panelg.setColor(lightblue); panelg.fillRect(0,0,600,height);
       panelg.drawImage(cake[0], 150,100,this);panelg.setColor(Color.black);   
       panelg.drawImage(knife3, 133,167+i*5,135,250,this); panelg.drawLine(318,200,264, 280  ); 
       panelg.drawImage(knife3, 237,167+i*5,135,250,this); panelg.drawLine(417,200,369, 280  );repaint(); 
       if(i>10){panelg.drawLine(264,280,264, 346  );panelg.drawLine(369,280,369, 346  );}
 try{wind.sleep(40);}catch(InterruptedException e){};      } 
 pause1=true;
       try {  synchronized(this){while(pause1==true) wait();}  }catch(InterruptedException e){}; 
 // try{wind.sleep(2600);}catch(InterruptedException e){};
 panelg.setColor(lightblue); panelg.fillRect(0,0,600,height);              // 3 pieces of cake 
       for (int x=0 ;x<3;x++)
        {panelg.drawImage(cake[1+x], 50+173*x, 50,this);}    
       panelg.setColor(Color.black); panelg.drawString("Cut into 3 equal parts", 50, 30);repaint(); pause1=true;
        try {  synchronized(this){while(pause1==true) wait();}  }catch(InterruptedException e){}; 
 panelg.setColor(lightblue); panelg.fillRect(0,0,600,height);
       panelg.drawImage(cake[1], 50,82,this);
        panelg.setColor(Color.black); panelg.drawString("1 third   =  ", 250,80); 
       panelg.drawImage(drawFraction(1,3), 450,40,this);
       repaint(); pause1=true;
       try {  synchronized(this){while(pause1==true) wait();}  }catch(InterruptedException e){}; 
      panelg.setColor(lightblue); panelg.fillRect(0,0,600,82);
         panelg.drawImage(cake[3],50+2*173,82,this);
         panelg.setColor(Color.black); panelg.drawString("2 third  = ", 250,80);     panelg.drawImage(drawFraction(2,3), 450,40,this);
          repaint(); pause1=true;
       try {  synchronized(this){while(pause1==true) wait();}  }catch(InterruptedException e){}; 
   panelg.setColor(lightblue); panelg.fillRect(0,0,600,82);
         panelg.drawImage(cake[2],50+173,82,this);
         panelg.setColor(Color.black); panelg.drawString("3 third  = ", 250,80);     panelg.drawImage(drawFraction(3,3), 450,40,this);
          repaint(); pause1=true;
       try {  synchronized(this){while(pause1==true) wait();}  }catch(InterruptedException e){}; 
panelg.setColor(lightblue); panelg.fillRect(0,0,600,height);                                                 //cake with 3 still knives
       panelg.drawImage(cake[0], 50,100,this);                                                                  //one whole cake
       panelg.drawImage(knife2, 33,167,200,250,this);panelg.drawImage(knife2, 137,167,200,250,this);
       panelg.drawImage(knife1,150,82,450, 38,this); repaint(); pause1=true;
     try {  synchronized(this){while(pause1==true) wait();}  }catch(InterruptedException e){}; 
for(int i=0;i<5;i++){panelg.setColor(lightblue); panelg.fillRect(0,0,600,height);
       panelg.drawImage(cake[0], 50,100,this);   
       panelg.drawImage(knife1,150,82,450, 38,this);
       panelg.drawImage(knife2, 33,167+i*5,200,250,this); repaint(); 
       panelg.drawImage(knife2, 137,167+i*5,200,250,this); repaint(); 
       try{wind.sleep(50);}catch(InterruptedException e){};      }    
for(int i=5;i<20;i++){panelg.setColor(lightblue); panelg.fillRect(0,0,600,height);
       panelg.drawImage(knife1,150,82,450, 38,this);
       panelg.drawImage(cake[0], 50,100,this);panelg.setColor(Color.black);   
       panelg.drawImage(knife3, 33,167+i*5,135,250,this); panelg.drawLine(218,200,164, 280  ); panelg.drawLine(219,200,165, 280  ); 
       panelg.drawImage(knife3, 137,167+i*5,135,250,this); panelg.drawLine(317,200,269, 280  ); panelg.drawLine(318,200,270, 280  );
       if(i>15){panelg.drawLine(164,280,164, 346  );panelg.drawLine(269,280,269, 346  );
	             panelg.drawLine(165,280,165, 346  );panelg.drawLine(270,280,270, 346  );}repaint();
try{wind.sleep(50);}catch(InterruptedException e){};
      }    try{wind.sleep(100);}catch(InterruptedException e){};  ////  //move other knife1
for(int i=0;i<33;i++){panelg.setColor(lightblue); panelg.fillRect(0,0,600,height); 
        panelg.drawImage(cake[0], 50,100,this);  panelg.drawImage(knife1,150,82+5*i,450,38,this); 
    panelg.setColor(Color.black); panelg.drawLine(218,200,164, 280  ); panelg.drawLine(317,200,269, 280  );   
    panelg.drawLine(164,280,164, 346  );panelg.drawLine(269,280,269, 346  ); 
	 panelg.drawImage(knife3, 33,263,135,250,this); 
     panelg.drawImage(knife3, 137,263,135,250,this); repaint(); 
      try{wind.sleep(30);}catch(InterruptedException e){};
      }   
  for(int i=33;i<44;i++){panelg.setColor(lightblue); panelg.fillRect(0,0,600,height);   //move other knife1
       panelg.drawImage(cake[0], 50,100,this);  panelg.drawImage(knife4,376,82+5*i,198,36,this);    
        panelg.setColor(Color.black); panelg.drawLine(218,200,164, 280  ); panelg.drawLine(317,200,269, 280  );   
    panelg.drawLine(164,280,164, 346  );panelg.drawLine(269,280,269, 346  ); 
	 panelg.drawLine(375,241,375,297);panelg.drawLine(127,238,375,241);
	panelg.drawImage(knife3, 33,263,135,250,this); 
    panelg.drawImage(knife3, 137,263,135,250,this); repaint(); 
      try{wind.sleep(50);}catch(InterruptedException e){};
      } pause1=true;
       try {  synchronized(this){while(pause1==true) wait();}  }catch(InterruptedException e){}; 
panelg.setColor(lightblue); panelg.fillRect(0,0,600,height);                                             // 6 pieces of cake 
       for (int x=0 ;x<6;x++)
        {panelg.drawImage(cake[4+x], 173*(x%3),(int)(x/3)*206 +50,this);}    
       repaint(); pause1=true;
        try {  synchronized(this){while(pause1==true) wait();}  }catch(InterruptedException e){}; 
 panelg.setColor(lightblue); panelg.fillRect(0,0,600,height);
       panelg.drawImage(cake[7], 50,82,this);
        panelg.setColor(Color.black); panelg.drawString("1 sixth   =  ", 250,40); 
       panelg.drawImage(drawFraction(1,6), 450,0,this);
       repaint(); pause1=true;
       try {  synchronized(this){while(pause1==true) wait();}  }catch(InterruptedException e){}; 
  panelg.setColor(lightblue); panelg.fillRect(0,0,600,height);
     for (int x=0 ;x<4;x++)
        {panelg.drawImage(cake[4+x], 173*(x%3),(int)(x/3)*206 +50,this);}    
         panelg.setColor(Color.black); panelg.drawString("4 sixth   =  ", 250,40); 
       panelg.drawImage(drawFraction(4,6), 450,0,this);
       repaint(); pause1=true;
       try {  synchronized(this){while(pause1==true) wait();}  }catch(InterruptedException e){}; 
panelg.setColor(lightblue); panelg.fillRect(0,0,600,height);                                           // 6 mangoes
  for(int i=0; i<3;i++){    panelg.drawImage(basket, i*200,380,this);}    
  for(int i=0; i<6; i++){panelg.drawImage(mangogreen, 206+(i%4)*50+(int)(i/4)*12, 185-(int)( i/4)*56,48,68,this);}
         panelg.drawImage(bigbasket, 200,55,this);   
panelg.setColor(Color.black); panelg.drawString("What is one third of 6 mangoes ? ", 10,40); 
    repaint(); pause1=true;
        try {  synchronized(this){while(pause1==true) wait();}  }catch(InterruptedException e){}; 
 panelg.setColor(lightblue); panelg.fillRect(0,0,600,height);                    // 6 mangoes
    for(int i=0; i<3;i++){panelg.drawImage(mangogreen,5+ i*200,375,this);    
     panelg.drawImage(mangogreen, 70+ i*200,375,this);    
     panelg.drawImage(basket, i*200,380,this);}     panelg.drawImage(bigbasket, 200,55,this);  
     panelg.setColor(Color.black); panelg.drawString("divide the 6 mangoes into 3 equal parts ", 10,40);  
    repaint(); pause1=true;
        try {  synchronized(this){while(pause1==true) wait();}  }catch(InterruptedException e){}; 
 panelg.setColor(lightblue); panelg.fillRect(0,0,600,height);                    // 6 mangoes
     panelg.drawImage(mangogreen,5,375,this);  panelg.drawImage(mangogreen, 70,375,this);    
     panelg.drawImage(basket, 0,380,this);    
     panelg.setColor(Color.black); panelg.drawString("one third of 6 mangoes  =  2 mangoes ", 10,40); 
     panelg.drawImage(drawFraction(1,3), 10,50,this);panelg.drawString(" of 6  =  2", 50, 90); 
   repaint(); pause1=true;
        try {  synchronized(this){while(pause1==true) wait();}  }catch(InterruptedException e){}; 
 panelg.setColor(lightblue); panelg.fillRect(0,0,600,120);                    // 6 mangoes
     panelg.drawImage(mangogreen,205,375,this);  panelg.drawImage(mangogreen, 270,375,this);    
     panelg.drawImage(basket, 200,380,this);    
     panelg.setColor(Color.black); panelg.drawString("two third of 6 mangoes  =  4 mangoes ", 10,40); 
     panelg.drawImage(drawFraction(2,3), 10,50,this);panelg.drawString(" of 6  =  4", 50, 90); 
   repaint(); pause1=true;
        try {  synchronized(this){while(pause1==true) wait();}  }catch(InterruptedException e){}; 
 panelg.setColor(lightblue); panelg.fillRect(0,0,600,120);                    // 6 mangoes
     panelg.drawImage(mangogreen,405,375,this);  panelg.drawImage(mangogreen, 470,375,this);    
     panelg.drawImage(basket,400,380,this);    
     panelg.setColor(Color.black); panelg.drawString("three third of 6 mangoes  =  6 mangoes ", 10,40); 
     panelg.drawImage(drawFraction(3,3), 10,50,this);panelg.drawString(" of 6  =  6", 50, 90); 
   repaint(); pause1=true;
        try {  synchronized(this){while(pause1==true) wait();}  }catch(InterruptedException e){}; 
panelg.setColor(lightblue); panelg.fillRect(0,0,600,height);                                                    // 9 mangoes
       for(int i=0; i<3;i++){    panelg.drawImage(basket, i*200,380,this);}    
         for(int i=0; i<9; i++){panelg.drawImage(mangogreen, 206+(i%4)*50+(int)(i/4)*12, 185-(int)( i/4)*56,48,68,this);}
         panelg.drawImage(bigbasket, 200,55,this);  
           panelg.setColor(Color.black); panelg.drawString("What is one third of 9 mangoes ? ", 10,30); 
    repaint(); pause1=true;
        try {  synchronized(this){while(pause1==true) wait();}  }catch(InterruptedException e){}; 
 panelg.setColor(lightblue); panelg.fillRect(0,0,600,height);                    
    for(int i=0; i<3;i++){panelg.drawImage(mangogreen,5+ i*200,375,this);    
     panelg.drawImage(mangogreen, 70+ i*200,375,this); panelg.drawImage(mangogreen, 35+ i*200,320,this);      
     panelg.drawImage(basket, i*200,380,this);}    
     panelg.setColor(Color.black); panelg.drawString("divide the 9 mangoes in equal parts ", 10,40); 
   repaint(); pause1=true;
        try {  synchronized(this){while(pause1==true) wait();}  }catch(InterruptedException e){}; 
 panelg.setColor(lightblue); panelg.fillRect(0,0,600,height);                    //1 basket with mangoes
    panelg.drawImage(mangogreen,5,305,this);    
     panelg.drawImage(mangogreen, 70, 305,this); panelg.drawImage(mangogreen, 35,255,this);      
     panelg.drawImage(basket, 0,300,this);    
     panelg.setColor(Color.black); panelg.drawString("one third of 9 mangoes = 3 mangoes ", 10,40);
     panelg.drawImage(drawFraction(1,3), 10,50,this);panelg.drawString(" of 9  =  3", 50, 90); 
  repaint(); pause1=true;
        try {  synchronized(this){while(pause1==true) wait();}  }catch(InterruptedException e){}; 
  panelg.setColor(lightblue); panelg.fillRect(0,0,600,200);                    // 2 baskets with mangoes
    panelg.drawImage(mangogreen,205,305,this);    
     panelg.drawImage(mangogreen, 270, 305,this); panelg.drawImage(mangogreen, 235,255,this);      
     panelg.drawImage(basket, 200,300,this);    
     panelg.setColor(Color.black); panelg.drawString("two third of 9 mangoes = 6 mangoes ; ", 10,40); 
      panelg.drawImage(drawFraction(2,3), 10,50,this);panelg.drawString(" of 9  =  6", 50, 90); 
  repaint(); pause1=true;
        try {  synchronized(this){while(pause1==true) wait();}  }catch(InterruptedException e){}; 
  mode=3;
}       
else{ 
 index=0;
 while(index<totalitems)
        {newSum(index); msg=""; evalimg=null; taken=new int[partsAsked]; partsTaken=0;
        repaint();   pause1=true;
       try {  synchronized(this){while(pause1==true) wait();}  }catch(InterruptedException e){}; 
        }
}}  } 

public void newSum(int index)
{   s= new String[9];
      String  q = quizItem[index];
         StringTokenizer t = new StringTokenizer(q,"$");
         int ii=0;
         while (t.hasMoreTokens())
    	{s[ii] = t.nextToken();
	  ii++;
	 }
  mode =Integer.parseInt(s[1]);
  nbfractions=Integer.parseInt(s[2]);
  partsAsked=Integer.parseInt(s[3]);
  mypictureg.setColor(lightblue); mypictureg.fillRect(0,0,400,400); 
if(mode==2){panelg.setColor(lightblue); panelg.fillRect(0,0,600,height); 
     Image im=null; 
    MediaTracker tracker = new MediaTracker(this);
   try{im=getImage(getClass().getResource(s[0]));
     tracker.addImage(im, 0);  tracker.waitForAll();
    }catch(InterruptedException e){showStatus("No picture found");}
picturewidth=im.getWidth(null); pictureheight=im.getHeight(null);  pictpix=new int[picturewidth*pictureheight];
 pictpix=getPixels(im, picturewidth, pictureheight);
for (int i=0;i<nbfractions;i++){panelg.drawImage(im, tile*(i%4), tile*(i/4), this);}}
else if((mode==3)&&(s[0].equals("circle")==true)){mypictureg.setColor(Color.black);
mypictureg.drawOval(6,6,radius*2,radius*2);
for(int i=0; i<nbfractions;i++){mypictureg.drawLine(6+radius, 6+radius, 6+radius+(int)(radius*Math.cos(2*(Math.PI)*i/nbfractions)),6+radius+(int)(radius*Math.sin(2*(Math.PI)*i/nbfractions)));}}
else if ((mode==3)&&(s[0].equals("square")==true)){mypictureg.setColor(Color.black);mypictureg.drawRect(6,6,squarewidth,
squarewidth);for(int i=0; i<nbfractions;i++){mypictureg.drawLine(6+(int)(i*squarewidth/nbfractions),6,6+i*squarewidth/nbfractions,6+squarewidth);}
}}

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
public int[] getPixels(Image img, int width, int height)
{int pixels[]= new int[width*height];
PixelGrabber pg= new PixelGrabber(img,0,0, width, height, pixels, 0, width);
try{pg.grabPixels();} catch(InterruptedException iie){showStatus("Dont get Pixels");}
return pixels;
}
public Image definePiece(int center,int lowerlimitx,int higherlimitx,
int lowerlimity,int higherlimity)
{isbusy=true; Image img=null;
int area=picturewidth*pictureheight;
int piecepix[] = new int [area];
int nborig=1, oldnborig=0, nbexpanded=1;
int contorig[]=new int [area]; int contexp[]=new int[area];
for(int j=0; j<area; j++)
{contorig[j]=-1; contexp[j]=-1;}
contexp[0]=center; int cycle=0;
do{ for(int i=0; i<nbexpanded; i++) {contorig[i]=contexp[i];}
       nborig=nbexpanded;// if(nborig>(picturewidth*pictureheight/2)) break;
       for (int i=oldnborig; i<nborig; i++)
{   // expanding to west
if((contorig[i]%picturewidth!=0)&&(itcontains((contorig[i]-1), contexp,nbexpanded)==false)&&((contorig[i])%picturewidth>lowerlimitx))
	{if(pictpix[contorig[i]-1]==0xffffffff) 
	 {contexp[nbexpanded]=(contorig[i]-1); nbexpanded++;}
	}
//east
 if(((contorig[i]+1)%picturewidth!=0)&&(itcontains((contorig[i]+1), contexp,nbexpanded)==false)&&((contorig[i])%picturewidth<higherlimitx))
              {if(pictpix[contorig[i]+1]==0Xffffffff) 
	 {contexp[nbexpanded]=(contorig[i]+1); nbexpanded++;}
	}
 //north
 if((contorig[i]>(picturewidth-1))&&(itcontains((contorig[i]-picturewidth), contexp,nbexpanded)==false)&&((contorig[i])/picturewidth>lowerlimity))
	{if(pictpix[contorig[i]-picturewidth]==0Xffffffff)  
	 {contexp[nbexpanded]=(contorig[i]-picturewidth); nbexpanded++;}
	}
//south
 if((contorig[i]<(picturewidth*(pictureheight-1)))&&(itcontains((contorig[i]+picturewidth), contexp,nbexpanded)==false)&&((contorig[i])/picturewidth<higherlimity))
	{if(pictpix[contorig[i]+picturewidth]==0Xffffffff)  
	 {contexp[nbexpanded]=(contorig[i]+picturewidth); nbexpanded++;}
	}
}cycle++; oldnborig=nborig;
showStatus("nb= "+nbexpanded+ "  for center at  "+center+  "  in  "+cycle+ "  cycles");
}while((nbexpanded<area) &&(nborig<nbexpanded)&&(cycle<(int)(picturewidth*pictureheight/2)));
for (int kk=0; kk<(picturewidth*pictureheight); kk++)
{if(itcontains(kk,contexp,nbexpanded)==true) {piecepix[kk]=colorChosen;} 
 else {piecepix[kk]=(0x00000000);}
}
if(mode==1){img=createImage(new MemoryImageSource(picturewidth, pictureheight, piecepix,0,picturewidth));}
else if (mode==2){img=createImage(new MemoryImageSource(picturewidth, pictureheight, piecepix,0,picturewidth));}
isbusy=false;
return img;
}
public boolean itcontains(int k, int[]contexp, int nbexpanded)
 { for (int ik=0; ik<nbexpanded; ik++)
    {if(k==contexp[ik]){return true;}
    }
return false;
}
public Image drawFraction(int N, int D)  // N=nominator, D=denominator
{Image fract=createImage(37, 75);Graphics frg=fract.getGraphics();
frg.setColor(lightblue); frg.fillRect(0,0,37,75);frg.setColor(Color.black);frg.setFont(bigfont); 
frg.drawLine(2,35, 34,35);frg.drawString(""+N, 6,30);
frg.drawString(""+D, 4,65);
return fract;}

public boolean itcontains( int[] tak,int k, int nbexpanded)
 { for (int ik=0; ik<nbexpanded; ik++)
    {if(k==tak[ik]){return true;}
    }
return false;
}
public void mousePressed(MouseEvent me){}
public void mouseReleased(MouseEvent me){}
 
synchronized public void mouseClicked(MouseEvent me)
  {int coX=me.getX(); int coY=me.getY(); 
  if(mode==1)  {  pause1=false; notify(); repaint(); }
  else{  if((coX>x2)&&(coY<yo+300))
           { chosenColor=(Color)(ColorChoice[(int)((coY-yo)/50)]); 
             colorPicked=true; colorChosen=colorarray[(int)((coY-yo)/50)];    
             showStatus("Color  "+colorChosen);      }
  else if((coX>width-300)&&(coY>height-150)){ pause1=false; notify(); repaint();  index++; colorPicked=false;}
  else if((mode==2)&&(coX>x1)&&(coX<x1+600)&&(coY>y1)&&(coY<y1+450)&&(isbusy==false))
        {int tile=125; int xx=(coX-x1)/tile; int yy=(coY-y1)/tile; 
           if(partsTaken>=partsAsked){msg="too many parts taken !";evalimg=wrongimg;} 
          else if (itcontains(taken, (yy*4+xx), partsTaken)==false) {  taken[partsTaken]=yy*4+xx; partsTaken++;
            if(partsTaken== partsAsked){evalimg=correctimg; }
			 Image mypicture2=definePiece(coX-x1-xx*tile+(coY-y1-yy*tile)*tile, 0,picturewidth,0,pictureheight); 
          panelg.drawImage(mypicture2, xx*tile, yy*tile, this);} repaint();}
  else if ((mode==3)&&(coX>xo)&&(coX<xo+squarewidth+12)&&(coY>y1)&&(coY<=(y1+squarewidth+12))) 
       {    if(s[0].equals("square")==true)  
             {int rectwidth=squarewidth/nbfractions; 
               int rectnb=(int)((coX-xo-6)/rectwidth);
             if(partsTaken>=partsAsked){msg="too many parts taken !";evalimg=wrongimg;} 
             else  if (itcontains(taken, rectnb, partsTaken)==false) {  taken[partsTaken]=rectnb; partsTaken++;
		   if(partsTaken==partsAsked) {evalimg=correctimg;}    mypictureg.setColor(chosenColor);
               mypictureg.fillRect(6+rectwidth*rectnb, 6, rectwidth, squarewidth);}}
      else{int start=0,sweep=90;               /////////////pie
               int x=coX-xo-6-radius; int y=y1+radius-coY+6;
                 //showStatus("x= "+x+" y = "+y);
              
			  if(nbfractions==4){  if(x>0) {start=0;} else {start=180;}
                                    if(y*x<0) {sweep=-90;} else {sweep=90;}
                                    if (partsTaken>=partsAsked){msg="too many parts taken !";evalimg=wrongimg;}
									else {partsTaken++; if(partsTaken== partsAsked){evalimg=correctimg;}
                                   mypictureg.setColor(chosenColor);
                                   mypictureg.fillArc(6,6, 2*radius,2*radius,start,sweep);	}
                                }
               else if ((x!=0)&&(nbfractions==8))
		        	{if(Math.abs((float) y/x)>1)
                          {if(y>0) { start=90;} else{ start=270;}
			               if(((float)y/x)<0){sweep=45; } else { sweep=-45;}
			              }
                     else {  if (x>0){start=0; } else {start=180;}
  			          if(((float) y/x)>0) {sweep=45;} else {sweep=-45;}}
                         if (partsTaken>=partsAsked){msg="too many parts taken !";evalimg=wrongimg;}
                         else{  partsTaken++;if(partsTaken== partsAsked){evalimg=correctimg;  } 
                              mypictureg.setColor(chosenColor);
              mypictureg.fillArc(6,6, 2*radius,2*radius,start,sweep);	}
      }  }   //end of circle
repaint();
}}}
public void mouseEntered(MouseEvent me){}
public void mouseExited(MouseEvent me){}
public void mouseMoved(MouseEvent me)
{  if(colorPicked==true){dragX=me.getX(); dragY=me.getY(); repaint();              
}}
public void mouseDragged(MouseEvent me){}

public void update(Graphics g)
{offS.setColor(lightblue); offS.fillRect(0,0,width,height); 
 offS.setColor(Color.black);offS.setFont(bigfont); 
if(mode==1){offS.drawImage(panel, xo, 0, this);}
if(mode!=1){offS.setFont(bigfont);offS.drawString("Fractions : Fill", 100,50);offS.setFont(textfont);
offS.drawString("Choose a color by clicking it (don't drag) ", 570,40);  offS.drawString("then click in the part to colour", 570,90);
offS.setColor(Color.red);offS.drawString(msg, 100,95);
 offS.drawImage(drawFraction(partsAsked, nbfractions), 350, 20, this);
offS.setColor(new Color(67,201,214)); offS.fillRect(width-200,height-100,150,80); 
 offS.setColor(Color.black); offS.drawString("NEXT", width-180, height-40);
 for (int i=0; i<6; i++)
    {offS.drawRect(x2, yo+50*i, 40,40); 
      offS.setColor(ColorChoice[i]);  offS.fillRect(x2, yo+50*i, 40,40); }
if(mode==3){ if(mypicture!=null){offS.drawImage(mypicture, xo, y1, this);}if(evalimg!=null){offS.drawImage(evalimg,x2-80, yo, 77, 60,this);}}
else if (mode==2){offS.drawImage(panel,x1,y1,this);if(evalimg!=null){offS.drawImage(evalimg,x2-80, yo, 77, 60,this);}}
  if((colorPicked==true)&&(dragX!=-1))
{offS.setColor(chosenColor);offS.fillOval(dragX,dragY,20,20);}       }
paint(g);
}
public void paint(Graphics g)
   {g.drawImage(offScreenImage,0,0,this);}
} 