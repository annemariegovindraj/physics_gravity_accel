import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.awt.image.*;

/*<applet code="MathsClouds1" width=800 height=500>
<param name="img" value="whitecloud+greycloud+blackcloud+lightning">
<param name="sleep" value="50">
</applet>
*/

public class MathsClouds1 extends Applet implements KeyListener, Runnable,MouseListener
{boolean isrunning=false, newquestion=false, evaluated=true;
 int height, width,pauselength, nbcl,firstcl,questionnb,counter,smartindex,step=1;
int[] smart={160,140,120,100,90,90,80,80,70,70,60,60,50,-1};
 Image offScreenImage,a_tick,a_cross, evalimg, helpimage; Graphics offS;
String stringguess="";
 Thread wind;
 Image[] img=new Image[7];
Random r = new Random();
Cloud[] cloud= new Cloud[200];
Color grey=new Color(20,155,155);Color rainblue=new Color(0,55,55), lesslightblue=new Color(135,220,245);
 Color grass= new Color(0,178, 0);Color lightblue= new Color(82,205,252); 
 int x[][]= new int[200][50];
int y[]= new int [200];
int d=32; int jmin=0, jmax=0; // sparcity of rain
boolean holdit=false, helpim=false, pause1=false;
Font bigfont=new Font("Verdana", Font.BOLD,18 );

public void init()
{height=Integer.parseInt(getParameter("height"));
width=Integer.parseInt(getParameter("width"));
addMouseListener(this);
pauselength=Integer.parseInt(getParameter("sleep"));
offScreenImage=createImage(width, height);
 offS=offScreenImage.getGraphics();
 addKeyListener(this);  requestFocus();
 setBackground(lightblue);
 pauselength=Integer.parseInt(getParameter("sleep"));
 String name[]= new String[7];
int tracked=0;
try{MediaTracker tracker = new MediaTracker(this);
      StringTokenizer mst= new StringTokenizer(getParameter("img"),"$");
      while(mst.hasMoreTokens()&&tracked<10)      
	{name[tracked]=mst.nextToken();
	img[tracked]=getImage(getDocumentBase(), name[tracked]+".gif");
  	tracker.addImage(img[tracked], tracked);
	tracked++;}
      tracker.waitForAll();
    }catch(InterruptedException e){showStatus("No picture found");}
a_tick=getAlpha(img[4],104,77);
a_cross=getAlpha(img[5],77,60);
}

public void showHelp()
{helpimage=createImage(400,200); Graphics helpimg=helpimage.getGraphics();
helpimg.setColor(lesslightblue);helpimg.fillRect(0,0,400,200);
helpimg.setColor(Color.black);helpimg.drawString("Type the answer for the sum in the Cloud in the yellow rectangle ",5,30);
helpimg.drawString("then press ENTER;If correct the rectangle changes to green, else to red.  ",5,60);
helpimg.drawString("  You cannot erase your mistake, just go on typing your answers ",5,90);
helpimg.drawString("Clouds will move faster and become darker when your score increasas",5, 120);
helpimg.drawString("Program written by annemarie.govindraj@gmail.com", 5, 195);
helpimg.drawRect(200,140,40,40);helpimg.setFont(bigfont); helpimg.drawString("X",210,170);
}

public void start()
{if(wind==null)
  {wind=new Thread(this);}
  readyingForStart(); 
  wind.start();}

public void readyingForStart()
{newquestion=false;
 firstcl=0; nbcl=1; counter=0; smartindex=0;
for (int j=0; j<200; j++) {cloud[j]=null;}
createNewCloud(0); questionnb=0;
cloud[0].changeState(1);
requestFocus();
isrunning=true;
}
public void stop()
{if(wind!=null)  { isrunning=false; wind=null;} } 

public void run()
{if(step==1) {repaint(); pause1=true; try {  synchronized(this){while(pause1==true) wait();}  }catch(InterruptedException e){}; }
  isrunning=true;
 if(step==2)
 {while (isrunning==true)
  {   if(smartindex<48)
         {if(holdit==false){counter++;}
         int nowsmart = smart[(int)(smartindex/4)];
       //  if (nowsmart==-1){isrunning=false; repaint();}
         if(counter%nowsmart==(nowsmart-1))  //lifespan of cloud before new cloud
  	  {createNewCloud(nbcl);nbcl++;}
         if(newquestion==true)
	 { if((questionnb<(nbcl-1))&&(questionnb>=firstcl))
	        {questionnb++; stringguess=""; cloud[questionnb].changeState(1); newquestion=false;}
                  else{};
       }
for (int j=firstcl; j<nbcl; j++)
 { int coX=(int)(cloud[j].getX());
    if(holdit==false)
       {coX=coX-3;
        cloud[j].putX(coX);
        }
    if (coX<3-120) {firstcl++;} 
    int ii=cloud[firstcl].getState();int ij=cloud[firstcl].getX();
        if((ij<0)&&(ii==1)&&(holdit==false)){holdit=true; smartindex-=3;}
}
repaint();
try {wind.sleep(pauselength);} catch(InterruptedException ie){};
}
else if(smartindex>47)
{   createNewRow(jmax);         //row of raindrops
showStatus("jmax= "+jmax);
   try{wind.sleep(50);} catch(InterruptedException ie){};
        jmax+=1; if(jmax>200){isrunning=false;}// end of rain  
       for(int j=jmin; j<jmax; j++)    
        {for(int i=0;i<width/d;i++)
           {x[j][i]= x[j][i] - 2; if (x[j][i]<1){x[j][i]=width-2;}
           }
         y[j]=y[j]+6; if(y[j]>=height) jmin=j+1;
       }  
}}}}
public void createNewRow(int row) 
{for (int i=0;i<width/d;i++)  //to draw rain
 {x[row][i]=r.nextInt(d);
   x[row][i]+=i*d;}
   y[row]=0;
  repaint();
}

public void createNewCloud(int nbcl)
{int a, b, i;    i=nbcl; int ans=0; 
  String sum="";int state =0;// 0= not highlighted
   int x=width; evaluated=false;
int y=r.nextInt()%4; if (y<0) {y=-y;}
y=y*70+80;
do{a=r.nextInt()%9;} while(a<=0);   //avoid too many zeroes
a=a+1;
b=r.nextInt()%10; if(b<0) {b=-b;}
sum=sum+a+"X"+b+" = ";
ans = a*b;
cloud[i]= new Cloud(smartindex, sum, ans, state,x,y);
cloud[i]  .putguess("");
}
public void keyPressed (KeyEvent ke)
 {if(ke.getKeyCode()==KeyEvent.VK_ENTER) {holdit=false;evaluate(stringguess);}
   else {};}
public void keyReleased (KeyEvent ke){};

public void keyTyped (KeyEvent ke)
 {stringguess= stringguess+ke.getKeyChar();
//   showStatus("Your answer="+stringguess);
 int answer =(int)cloud[questionnb].getAnswer();
if(stringguess.length()==2) evaluate(stringguess);else if(answer<10){evaluate(stringguess);}
}

public void evaluate( String stringguess)
 {if(evaluated==false)
   { int answer =(int)cloud[questionnb].getAnswer();
   String stringans=""+answer; 
stringans=stringans.trim();
stringguess=stringguess.trim();
cloud[questionnb].putguess(stringguess);
if(stringguess.equals(stringans))
       {smartindex++; cloud[questionnb].changeState(2);showStatus("Correct");
        // if (smartindex>47){}
       }
else{smartindex--; if(smartindex<0) {smartindex=0;}
        cloud[questionnb].changeState(3);showStatus("Wrong");
       }
newquestion=true; holdit=false;
repaint();
}}
public Image getAlpha(Image img, int iwidth, int iheight)
{Image a_img;
int[] pixels= new int[iwidth*iheight];
PixelGrabber pg= new PixelGrabber(img,0,0,iwidth,iheight,pixels,0,iwidth);
try{pg.grabPixels();}catch(InterruptedException ie){};
for(int i=0; i<pixels.length; i++)
{int p = pixels[i];
 int red = 0xff&(p>>16);
 int green = 0xff&(p>>8);
 int blue = 0xff&(p);
if ((red>245)&&(green>245)&&(blue>245)){pixels[i]=(0x00000000);}
}
a_img=createImage(new MemoryImageSource(iwidth,iheight,pixels,0,iwidth));
return a_img;
}  
public void mousePressed(MouseEvent me){}
public void mouseReleased(MouseEvent me){}
synchronized public void mouseClicked(MouseEvent me)
{ int coY= me.getY(); int coX=me.getX();
  if(helpim==true){if ((coX>400)&&(coX<460)&&(coY>340)&&(coY<400)){helpim=false;repaint();}}
 else if((coY<40)&&(coX>width-40)){showHelp(); helpim=true;repaint();}
if(step==1){ step=2;  pause1=false; notify();  repaint();} 
{};}
public void mouseEntered(MouseEvent me){}
public void mouseExited(MouseEvent me){}


public void update(Graphics g)
{offS.setColor(lightblue);  offS.fillRect(0,0,width, height);
   if(step==1){offS.setColor(lesslightblue); offS.fillRect(200,200,300,100);   
   offS.setColor(Color.black);   offS.setFont(bigfont); offS.drawString(" START ",250,250);}
 else if((step==2)&&(smartindex<48))
{//offS.setColor(lightblue);  offS.fillRect(0,0,width, height);
   Font textf= new Font("Dialog", Font.PLAIN, 18); 
 
  for (int j=firstcl; j<nbcl; j++)
   {Image cloudimg;
     int s=(cloud[j].getSmartIndex())/12; 
     switch(s)
      {case 0 : cloudimg=img[0]; break;  // whitecloud
        case 1 : cloudimg=img[1]; break;  // raincloud
        case 2 : cloudimg=img[2]; break;  // blackcloud
        case 3 : cloudimg=img[3]; break;  // cloudwithlightning
        default : cloudimg=img[0]; break;  // whitecloud
       }

String imsum = cloud[j].getSum();
String stringguess=cloud[j].getguess();
int imState  = cloud[j].getState();
int imx = cloud[j].getX();
int imy = cloud[j].getY();
offS.setColor(Color.black); 
if(cloudimg==null){offS.drawString("no picture", 50,300);}
else
{ Font f= new Font("Dialog", Font.BOLD, 16); 
 offS.drawImage(cloudimg,imx,imy,this);
  offS.setColor(Color.white);  offS.fillRect(imx + 15, imy + 25, 65, 15);
  offS.setColor(Color.black);offS.setFont(f);
offS.drawString(imsum+stringguess, imx + 17, imy + 38);
offS.setFont(textf);
offS.drawString("Smartindex = "+smartindex, 500, 50);
offS.drawString("Tick in the answer for the cloud with the yellow border" , 20, 40);
Color c;
switch(imState)
{case 1 : c=Color.yellow; evalimg=null;break;
  case 2 : c=grass ; evalimg=a_tick;break;
  case 3 : c=Color.red; evalimg=a_cross; break;
  case 0 : default : c=lightblue;evalimg=null; break;
}
offS.drawImage(evalimg,imx+30,imy,this);
offS.setColor(c);
offS.drawRect(imx-2, imy -2, 140, 75);
offS.drawRect(imx-1, imy -1, 137, 73);
offS.drawRect(imx, imy , 135, 71);
}}}
else if(smartindex>47)
   {offS.setColor(grey);offS.fillRect(0,0,width, height);
    offS.setFont( new Font("Dialog", Font.BOLD, 24));
    offS.setColor(Color.red); offS.drawString("You won, You made it rain", 50,300);
    offS.setColor(rainblue);
    for (int j=jmin; j<jmax;j++)
      {for (int i=0;i<width/d;i++) 
       {offS.drawLine(x[j][i], y[j], (x[j][i]-4),(y[j]+12));}
      }
  }
offS.setColor(lesslightblue);offS.fillRect(width-40, 0, 40,40);offS.setFont(new  Font("Verdana",Font.BOLD,22));
offS.setColor(Color.black); offS.drawString("?", width-30,30);
if(helpim==true){offS.drawImage(helpimage, 200,200,this);}

paint(g);
}
public void paint(Graphics g)
{g.drawImage(offScreenImage,0,0,this);}

public class Cloud
{ int smartindex, ans, state,x,y;
String sum, guess;
public Cloud(int smart, String sum, int ans, int state, int x, int y)
{smartindex=smart; this.sum=sum;  this.ans=ans;
   this.state=state; this.x = x; this.y = y;}
public int getSmartIndex() 	{ return smartindex;}
public String getSum()  	{return sum;}
public int getAnswer()	{return ans;}
public int getState()		{return state;}
public int getX()		{return x;}
public int getY()		{return y;}
public void putX(int newx)	{x=newx;}
public void changeState (int st){ state = st;}
public void setSum(String str) 	{sum= sum+str;}
public String getguess()	{return guess;}
public void putguess(String stringguess)	{guess=stringguess;}

}}


