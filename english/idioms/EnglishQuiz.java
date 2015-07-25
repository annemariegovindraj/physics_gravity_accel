import java.awt.*;
import java.awt.event.*;
import java.applet.*;
import java.io.*;
import java.util.*;
import java.awt.image.*;


public class EnglishQuiz extends Applet 
implements MouseListener, Runnable
{String mytext, thecorrectanswer ; String question="", evaluation="";
  Font questionf= new Font("Verdana", Font.BOLD, 18);
 Image a_img, a_tick,a_cross, evalimg, offScreen;
 Graphics offS;
Image bubbles[]= new Image[4]; int bubblesizes[]= new int [4]; 
int bubblecentrex[]= {170, 170, 840,840}; int bubblecentrey[]= {120, 350,350,120}; int bubw[]=new int[4]; int bubh[]= new int[4]; int xrect=0;
Color grass =new Color(25,165,25); Color darkblue=new Color(0, 20,253); 
Color bgcolors[]={new Color(137,142,201), new Color(185,147,200), new Color(250,184,184),new Color(226,207,150), new Color(67,227,180), new Color(236,155,184), new Color(67,201,216), new Color(209,201,209)};    //blue, mauve, pink, beige, green,magenta, turquoise, gray
Color bubblecol[]= {new Color(57,63,128),new Color(149,90,171), new Color(209,97,105), new Color(183,151,50), new Color(27,177,132), new Color(101,20,49),  new Color(30,133,145), new Color(171,155,172)}; boolean colour;
  String ansLabel[]=new String[8]; //normally only 5
       boolean step1=true, step2=false; 
   boolean quizOver=false, isrunning=false;
    int  index, qnb=-1, correctanswers, totalanswers, totalitems;
    int ncorrect, chosenanswer, width,height,  yspace=85, yanswer=100, yquestion=45;    ;
 String quizItem[]= new String[50];
   Random rs = new Random();
  Thread roller;

public void init()    
{height=Integer.parseInt(getParameter("height")); width=Integer.parseInt(getParameter("width"));
 offScreen=createImage(width, height);  offS=offScreen.getGraphics();
offS.setFont(questionf);
mytext=getParameter("text");
for (int i=0; i<50;i++){quizItem[i]="";} 
correctanswers=totalanswers=0;
setFont(questionf);
addMouseListener(this);
 Image correctimg=getImage(getClass().getResource("tick.GIF")); Image wrongimg=getImage(getClass().getResource("cross.GIF"));
a_tick=getAlpha(correctimg,104,77, true); a_cross=getAlpha(wrongimg,77,60, true);
}
public void start()
{if(roller==null)
  {roller=new Thread(this);}
  readyingForStart();isrunning=true;    roller.start();}

public void stop()
{if(roller!=null)  {roller=null;}
   isrunning=false;
}
public void readyingForStart()
 {totalitems=0; index=0; 
String inLine=null; InputStreamReader instream =null;
 try{ 
 instream= new InputStreamReader(getClass().getResourceAsStream("idioms.txt"));
 BufferedReader dataStream = new BufferedReader(instream);
  while ((inLine=dataStream.readLine())!=null)
       {quizItem[totalitems]=inLine;totalitems++;}
     }catch(IOException e){showStatus ("error in data reading");}
////mix questions
for (int ii=0;ii<50;ii++)
{int i1=rs.nextInt(totalitems);
 int i2=rs.nextInt(totalitems);
String tempostr=quizItem[i1];
quizItem[i1]=quizItem[i2];
quizItem[i2]=tempostr;}
 }
public void run()
{while( isrunning==true)
  {while(index<totalitems)
    {
    if(step1==true)
     {step2=false;  qnb++;
       offS.setColor(bgcolors[qnb%8]);  offS.fillRect(0,0,width,height);
       String s[]= new String[9];
      String  q = quizItem[index];
      StringTokenizer t = new StringTokenizer(q,"$");
       int ii=0;
         while (t.hasMoreTokens())
    	{s[ii] = t.nextToken();	  ii++;}
    thecorrectanswer=s[2];
    question=s[1];
   Image   img=null;
   if(s[0].equals("")){}
   else {        
   try{MediaTracker tracker = new MediaTracker(this);
	img=getImage(getClass().getResource(s[0]));
  	tracker.addImage(img, 0);
	tracker.waitForAll();
        }catch(InterruptedException e){showStatus("No picture found");}
       }
   int imgwidth=img.getWidth(null); int imgheight=img.getHeight(null);
   a_img=getAlpha(img, imgwidth, imgheight, false);
  for (int i=0; i<4; i++) 
            { bubbles[i]=drawBubble(bgcolors[qnb%8], bubblecol[qnb%8],s[i+2], i);}
   do {ncorrect=(rs.nextInt()%4);} while(ncorrect<0);
 index++;   evaluation=""; evalimg=null;
  int jj=0;
step1=false;
 while(jj<width)
   { repaint(jj,0,jj+10,height);
     try{roller.sleep(30);}catch(InterruptedException e){};
     jj+=10;}  
for (int ik=0; ik<2000; ik++)
      { try{roller.sleep(30);}catch(InterruptedException e){};
        if((step1==true)||(step2==true)) break;}
}                 //close if step1==true
if (step2==true)
{      
     try{roller.sleep(2000);}catch(InterruptedException e){};
     step2=false; step1=true;}
} //close  while
quizOver=true; evaluation= "YOUR FINAL SCORE :  "; evalimg=null;
repaint();}}

public Image getAlpha(Image img, int imwidth, int imheight, boolean colour )  //  background made transparent, if black and white pict  , make lines black 
{Image a_img;
int[] pixels= new int[imwidth*imheight];
PixelGrabber pg= new PixelGrabber(img,0,0,imwidth,imheight,pixels,0,imwidth);
try{pg.grabPixels();}catch(InterruptedException ie){};
for(int i=0; i<pixels.length; i++)
{int p = pixels[i];
 int red = 0xff&(p>>16);
 int green = 0xff&(p>>8);
 int blue = 0xff&(p);
if(colour==true) {if ((red>185)&&(green>185)&&(blue>185)){pixels[i]=(0x00000000);}}
else if (colour ==false)     {if((red>185)&&(green>185)&&(blue>185)){pixels[i]=(0x00000000);}
    else {pixels[i]=0xff000000;} }
}
a_img=createImage(new MemoryImageSource(imwidth,imheight,pixels,0,imwidth));
return a_img;
}  
Image drawBubble( Color bgc, Color bubc, String s, int answernb)
{  Image im; Graphics img;
   FontMetrics fm; int size,  yrect=0, widthRect=0, heightRect=0;     
   fm=offS.getFontMetrics();
   int linel=fm.stringWidth(s);
   if (linel>600) {size=3;} else if(linel<=250){size=0;} else {size=1;}
   bubblesizes[answernb]=size;
   switch(size)
    {case 0 : bubw[answernb]=200; bubh[answernb]=110; xrect=26 ; yrect=23; widthRect=155; heightRect=55; break;
     case 1:  bubw[answernb]=300; bubh[answernb]=150; xrect=32 ; yrect=30; widthRect=225; heightRect=90; break;
     case 2:  bubw[answernb]=280; bubh[answernb]=300; xrect=45 ; yrect=38; widthRect=200; heightRect=186; break;
     case 3:  bubw[answernb]=320; bubh[answernb]=200; xrect=53 ; yrect=34; widthRect=225; heightRect=136; break;
     }
  im=createImage(bubw[answernb], bubh[answernb]);  img=im.getGraphics();
  img.setColor(bgc); img.fillRect(0,0,bubw[answernb], bubh[answernb]);
  img.setColor(bubc); img.fillOval(0,0,bubw[answernb], bubh[answernb]);
  img.setColor(Color.yellow);
  int lineheight=fm.getHeight();
  if(linel<widthRect)
          {img.drawString(s,xrect, heightRect/2+yrect);}
    else
           {int spacelength = fm.stringWidth(" ");
           int y=yrect+lineheight; int wl=0; linel=0;		                //length of the text
            s=s+" ?";
             StringTokenizer tt=new StringTokenizer(s);
             String word, linee="";    		              	//contains the text
             while(tt.hasMoreTokens())
	{ word= tt.nextToken();
	   if (word.equals("?"))   {img.drawString (linee,xrect, y);}
	  else    {  wl=fm.stringWidth(word);
	  linel=linel+spacelength+wl;
	   if (linel>widthRect)
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
public void mouseClicked(MouseEvent me)
{ chosenanswer=0;
      if(step1==false)  
   {      int coX=me.getX(); int coY=me.getY(); int size= bubblesizes[ncorrect];
          if((coX> (bubblecentrex[ncorrect]-(bubw[ncorrect])/2))&&(coX< (bubblecentrex[ncorrect]+(bubw[ncorrect])/2))&&(coY> (bubblecentrey[ncorrect]-(bubh[ncorrect])/2))&&(coY< (bubblecentrey[ncorrect]+(bubh[ncorrect])/2)))
           {  evaluation="CORRECT";correctanswers++;
	    evalimg=a_tick;  step2=true;  }
          else 	
	{evaluation= "WRONG,  "+thecorrectanswer;
	  evalimg=a_cross; step2=true;
	 }
       totalanswers++; repaint();
    } }
   public void mousePressed(MouseEvent e){};
   public void mouseReleased(MouseEvent e){};
   public void mouseEntered(MouseEvent e){};
   public void mouseExited(MouseEvent e){};

public void update(Graphics g)
{if(quizOver==false)
{  offS.setColor(Color.black); offS.drawString("SCORE=  : "+correctanswers+" / "+totalanswers, 320, 530);

  if (bubblesizes[0]==3) {ncorrect=ncorrect%2;}  
  offS.drawImage(bubbles[0], bubblecentrex[ncorrect]-(bubw[0])/2, bubblecentrey[ncorrect]-(bubh[0])/2, this);
  int j=1, jj=0;
   while (jj<4)
         {if(jj==ncorrect) {jj++;}
          else { offS.drawImage(bubbles[j], bubblecentrex[jj] -(bubw[j])/2, bubblecentrey[jj]-(bubh[j])/2, this); j++; jj++;}
 if(a_img==null)  {offS.drawString("no picture",20, 50);}
  else {offS.drawImage(a_img, 350, 150,this);}
   offS.drawString(question, 350, 60);
    }
 if(step2==true)
  { offS.setColor(bgcolors[qnb%8]);offS.fillRect(250,470, 750,70); 
    offS.setColor(Color.black);offS.drawString(evaluation,320,500);  
    offS.drawImage(evalimg,250,480, 60, 50, this);
    offS.drawString("SCORE=  : "+correctanswers+" / "+totalanswers, 320, 530);
  
/*
      if(chosenanswer==ncorrect)
     {offS.setColor(grass); offS.drawRect(450, yanswer+ncorrect*yspace, 548, 2*lineheight-1);
       offS.drawRect(451, yanswer+ncorrect*yspace+1, 546, 2*lineheight-2); }
    else{ offS.setColor(Color.red); offS.drawRect(451, yanswer+chosenanswer*yspace+1, 548, 2*lineheight-2); offS.drawRect(450, yanswer+chosenanswer*yspace, 549, 2*lineheight-1);}
*/
   }}
else if (quizOver==true){
  offS.setColor(bgcolors[qnb%8]); offS.fillRect(0,0,width, height);
  offS.setColor(Color.black);offS.drawString(evaluation,620,420);  
    offS.drawString(" YOUR FINAL SCORE=  : "+correctanswers+" / "+totalanswers, 620, 460);
    }
paint(g);
}
public void paint(Graphics g)
{g.drawImage(offScreen,0,0,this);}}