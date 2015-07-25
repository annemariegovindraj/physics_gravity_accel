import java.awt.*;
import java.awt.event.*;
import java.applet.*;
import java.io.*;
import java.util.*;
import java.awt.image.*;
/* map is for a simple quis where something can be indicated by an asterisk, the text is in the same folder and called Quiz.txt*/

public class Map extends Applet 
implements MouseListener, Runnable // ActionListener

{String mytext, thecorrectanswer ; String question="", evaluation="";
  Font questionf= new Font("Dialog", Font.BOLD, 18);
 Image img, a_tick,a_cross, evalimg, offScreen;
 Graphics imggr,  offS;
Color grass =new Color(130,244,138);Color greenPastel= new Color(185,249,190);
  String ansLabel[]=new String[8]; //normally only 5
       boolean step1=true, step2=false; 
   boolean quizOver=false, isrunning=false;
    int  index, correctanswers, totalanswers, totalitems;
    int ncorrect, chosenanswer, width,height;
 String quizItem[]= new String[100];
   Random rs = new Random();
  Thread roller;

public void init()    
{height=Integer.parseInt(getParameter("height")); width=Integer.parseInt(getParameter("width"));
 offScreen=createImage(width, height);  offS=offScreen.getGraphics();
 offS.setColor(grass); offS.fillRect(0,0,width, height);
mytext=getParameter("text");
for (int i=0; i<50;i++){quizItem[i]="";} 
correctanswers=totalanswers=0;
setFont(questionf);
addMouseListener(this);
 Image correctimg=getImage(getClass().getResource("tick.gif")); Image wrongimg=getImage(getClass().getResource("cross.gif"));
a_tick=getAlpha(correctimg,104,77); a_cross=getAlpha(wrongimg,77,60);
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
 instream= new InputStreamReader(getClass().getResourceAsStream("Quiz.txt"));
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
     {step2=false;
      String s[]= new String[9];
      String  q = quizItem[index];
         StringTokenizer t = new StringTokenizer(q,"$");
         int ii=0;
         while (t.hasMoreTokens())
    	{s[ii] = t.nextToken();
	  ii++;
	 }
    thecorrectanswer=s[2];
    question=s[1];
    Image im=null;
    img=createImage(550, height-2);imggr=img.getGraphics();
    imggr.setColor(grass); imggr.fillRect(0,0,550,height-2);
   try{MediaTracker tracker = new MediaTracker(this);
	im=getImage(getClass().getResource(s[0]));
  	tracker.addImage(im, 0);
	tracker.waitForAll();
       }catch(InterruptedException e){showStatus("No picture found");}
  imggr.drawImage(im,0,0,this);
  if(s[7].equals("?")){}
  else {  int offsetx=10; int offsety=20;
	int mx=Integer.parseInt(s[7]);
	int my=Integer.parseInt(s[8]); 
	imggr.setColor(Color.black);
	imggr.setFont(new Font("Dialog", Font.BOLD, 36));
	imggr.drawString("*", mx-offsetx, my+offsety);
           }
   do {ncorrect=(rs.nextInt()%5);} while(ncorrect<0);
     int j=0, i=3;
   while (j<5)
         {  if (j==ncorrect){ansLabel[j]=s[2]; j++;}
             else {ansLabel[j]=s[i]; i++; j++;}
         }
 index++;   evaluation=""; evalimg=null;
  offS.setColor(grass);  offS.fillRect(0,0,width,height);
  int jj=0;
step1=false;
 while(jj<width)
   { repaint(jj,0,jj+10,height);
     try{roller.sleep(30);}catch(InterruptedException e){};
     jj+=10;}  
for (int ik=0; i<2000; i++)
      { try{roller.sleep(30);}catch(InterruptedException e){};
        if((step1==true)||(step2==true)) break;}
}                 //close if step1==true
if (step2==true)
{    showScore(chosenanswer);
     repaint();
     try{roller.sleep(1000);}catch(InterruptedException e){};
     step2=false; step1=true;}
} //close  while
quizOver=true; evaluation= "YOUR FINAL SCORE :  "; evalimg=null;
repaint();}}

public Image getAlpha(Image img, int imwidth, int imheight)
{Image a_img;
int[] pixels= new int[imwidth*imheight];
PixelGrabber pg= new PixelGrabber(img,0,0,imwidth,imheight,pixels,0,imwidth);
try{pg.grabPixels();}catch(InterruptedException ie){};
for(int i=0; i<pixels.length; i++)
{int p = pixels[i];
 int red = 0xff&(p>>16);
 int green = 0xff&(p>>8);
 int blue = 0xff&(p);
if ((red>245)&&(green>245)&&(blue>245)){pixels[i]=(0x00000000);}
}
a_img=createImage(new MemoryImageSource(imwidth,imheight,pixels,0,imwidth));
return a_img;
}  
/*
public void actionPerformed(ActionEvent e)
    {   if (e.getActionCommand()== "Next") 
	{displayNextQuizItem();      } }
*/
public void mouseClicked(MouseEvent me)
{ chosenanswer=0;
      if(step1==false)  
   {      int coX=me.getX(); int coY=me.getY(); 
           if((coX>590)&&(coY<420)&&(coY>100))
           { chosenanswer=(int)((coY-100)/50); 
                step2=true;  }
           else{};
} }
void showScore(int chosenl)
{ if (chosenl==ncorrect)
	{ evaluation="CORRECT";correctanswers++;
	    evalimg=a_tick;
	 }
   else 	
	{evaluation= "WRONG, it is  "+thecorrectanswer;
	     evalimg=a_cross;
	 }
       totalanswers++; 
   //  showStatus(evaluation+correctanswers+" / "+totalanswers);
    offS.setColor(grass); offS.fillRect(560,400, width-560, height-400);
    offS.setColor(Color.black); offS.drawString(evaluation,660,420);  
    offS.drawString("SCORE=  : "+correctanswers+" / "+totalanswers, 620, 460);
      repaint();    //(500,400,width-500,height-400);
     }
   public void mousePressed(MouseEvent e){};
   public void mouseReleased(MouseEvent e){};
   public void mouseEntered(MouseEvent e){};
   public void mouseExited(MouseEvent e){};

public void update(Graphics g)
{if(quizOver==false)
{    if(img==null)  {offS.drawString("no picture",20, 50);}
  else {offS.drawImage(img,20,2,this);}
   FontMetrics fm;
      offS.setFont(questionf);
  offS.setColor(Color.black); offS.drawString("SCORE=  : "+correctanswers+" / "+totalanswers, 620, 460);
          fm=offS.getFontMetrics();
        int linel=fm.stringWidth(question);
        int lineheight=fm.getHeight();
        if(linel<440)
          {offS.setColor(greenPastel); offS.fillRect(550,( 55-lineheight+4),450 ,lineheight+6);
             offS.setColor(Color.black);offS.drawString(question,560,55);}
       else
           {int spacelength = fm.stringWidth(" ");
             int  y=55,wl=0; linel=0;					//length of the text
             StringTokenizer tt=new StringTokenizer(question+" ?");
             String word, linee="";    					//contains the text
             while(tt.hasMoreTokens())
	{ word= tt.nextToken();
	   if (word.equals("?"))
	      {offS.setColor(greenPastel); offS.fillRect(550,y-lineheight+4,450, lineheight+6);
                        offS.setColor(Color.black);  offS.drawString (linee+"  ?", 560, y);}
	  else    {
	  wl=fm.stringWidth(word);
	  linel=linel+spacelength+wl;
	   if (linel>440)
	         {offS.setColor(greenPastel); offS.fillRect(550,y-lineheight+4,450,lineheight+6);
                            offS.setColor(Color.black);   offS.drawString (linee, 560, y);
	           y=y+lineheight; linel=wl; linee=word;
	         }
	      else
	        {linee=linee+" "+word;}
	     }
	}//end of while	
            } // end of else

 for(int k=0;k<5;k++)
   {   fm=offS.getFontMetrics();
       linel=fm.stringWidth(ansLabel[k]);
        int  y=100;      
        if(linel<440)
         { offS.setColor(greenPastel);  offS.fillRect(590,100+k*50+2,400,lineheight+2);
           offS.setColor(Color.black); offS.drawString(ansLabel[k],  600, k*50+100+lineheight);
         }
       else
           {int spacelength = fm.stringWidth(" ");
             int wl=0; linel=0;
             StringTokenizer tt=new StringTokenizer(ansLabel[k]);
             String word, linee="";
             while(tt.hasMoreTokens())
	 {word= tt.nextToken();
	   wl=fm.stringWidth(word);
	       linel=linel+spacelength+wl;
	       if (linel>440)
	         {offS.setColor(greenPastel);  offS.fillRect(590,k*50+y+2,400,lineheight+2);
                           offS.setColor(Color.black);offS.drawString (linee, 600, k*50+y+lineheight);
	           y=y+lineheight; linel=wl; linee=word;
	         }
	      else
	        {linee=linee+" "+word;}
	}//end of while
                          offS.setColor(greenPastel);  offS.fillRect(590,k*50+y+2,400,lineheight+2 );
                           offS.setColor(Color.black);offS.drawString (linee, 600, y+k*50+lineheight);
	            } // end of else
  }// end of for
if(step2==true){offS.drawImage(evalimg,550,102+chosenanswer*50, 40,32,this);
                      offS.drawImage(a_tick, 550,102+ncorrect*50,40,32,this);}
}	
else if (quizOver==true){
  offS.setColor(grass); offS.fillRect(0,0,width, height);
  offS.setColor(Color.black);offS.drawString(evaluation,620,420);  
    offS.drawString("SCORE=  : "+correctanswers+" / "+totalanswers, 620, 460);
 }
paint(g);
}
public void paint(Graphics g)
{g.drawImage(offScreen,0,0,this);}}