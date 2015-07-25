import java.awt.*;
import java.awt.event.*;
import java.applet.*;
import java.io.*;
import java.util.*;
import java.awt.image.*;


public class Match extends Applet 
implements MouseListener, MouseMotionListener, Runnable // ActionListener

{String mytext,  evaluation=""; String longstr[]= new String[5];
  Font questionf= new Font("Dialog", Font.PLAIN, 16);
 Image img=null, offScreen, helpimage;
 Graphics imggr,  offS; 
  Anaterm ana[]= new Anaterm[22];
Color darkcream =new Color(109,48,46);Color lightcream =new Color(219,173,173);  
       boolean step1=true, step2=false; 
   boolean quizOver=false, isrunning=false,helpim=false;
    int  index=0, max=0,  totalitems=0, xo=0,yo=0, terml=0, textheight;
    int  width,height, startX=0, startY=0, select=-1, offsetX=0, offsetY=0, imx=0, imy=10;
   Random rs = new Random();
  Thread roller;  FontMetrics fm;  

public void init()    
{height=Integer.parseInt(getParameter("height")); width=Integer.parseInt(getParameter("width"));
 offScreen=createImage(width, height);  offS=offScreen.getGraphics();
 offS.setColor(lightcream); offS.fillRect(0,0,width, height);
 offS.setFont(questionf); fm=offS.getFontMetrics();
textheight=fm.getHeight();
 img=createImage(600,height); imggr=img.getGraphics(); 
imggr.setColor(lightcream); imggr.fillRect(0,0,600, height);
 mytext=getParameter("text");
for (int i=0; i<5;i++){longstr[i]="";} 
totalitems=0; index=0; 
String inLine=null;InputStreamReader instream =null;
 try{  instream= new InputStreamReader(getClass().getResourceAsStream("Match.txt"));
   BufferedReader dataStream = new BufferedReader(instream);
  while ((inLine=dataStream.readLine())!=null)
       {longstr[totalitems]=inLine;totalitems++;}
     }catch(IOException e){showStatus ("error in data reading");}
setFont(questionf);
addMouseListener(this);addMouseMotionListener(this);
}
public void showHelp()
{helpimage=createImage(400,200); Graphics helpimg=helpimage.getGraphics();
helpimg.setColor(darkcream);helpimg.fillRect(0,0,400,200);
helpimg.setColor(lightcream);helpimg.fillRect(5,5,390,190);
helpimg.setColor(Color.black);helpimg.drawRect(5,5,390,190);helpimg.drawString("drag the name and release the ",10,50);
helpimg.drawString("mousebutton when the label covers the asterisk ",10,80);
helpimg.drawString("Program written by annemarie.govindraj@gmail.com", 10, 185);
helpimg.drawRect(204,116,40,40);helpimg.setFont(new Font("Verdana",Font.PLAIN,40)); helpimg.drawString("X",210,150);
}

public void start()
{if(roller==null)
  {roller=new Thread(this);}
 isrunning=true;    roller.start();}

public void stop()
{if(roller!=null)  {roller=null;}
   isrunning=false;
}
public void run()
{while( isrunning==true)
  {index=0; while(index<=totalitems)
    { for (int i=0; i<200; i++)
        { try{roller.sleep(30);}catch(InterruptedException e){};
          if((step1==true)||(step2==true)) break;}
      if(step1==true)
        {step2=false; prepareNextPanel(index);evaluation="";repaint(); index++;
         step1=false; }
      if (step2==true)
            {boolean endd; endd=checkPanelOver();
if(endd==true) {step1=true;
             step2=false; evaluation="Well Done";repaint();}step2=false;}
   } //close  while
quizOver=true; evaluation = "Match  Over"; 
repaint();
}}
public void prepareNextPanel(int index)
 {    offS.setColor(lightcream); offS.fillRect(0,0,width, height); repaint();
    String s[]= new String[22];
         StringTokenizer t = new StringTokenizer(longstr[index],"$");
         max=0;
         while (t.hasMoreTokens())
    	 {s[max] = t.nextToken();
	  max++;   //nb of anat_names
	 }
//showStatus(s[0]+" ' "+s[1]);      
Image im=null;
 try{MediaTracker tracker = new MediaTracker(this);
	im=getImage(getClass().getResource( s[0]));
  	tracker.addImage(im, 0);
	tracker.waitForAll();
         }catch(InterruptedException e){showStatus("No picture found");}
int imw= im.getWidth(null); imx=(width-imw)/2;  
imggr.setColor(lightcream); imggr.fillRect(0,0,600, height);
 imggr.drawImage(im,0,0,this); imggr.setColor(Color.blue);imggr.setFont(new Font("Dialog", Font.BOLD,36));

 int  locations[]= new int[max];  //allocate a nb to each country label
     for(int i=0; i<max;i++){ locations[i]=i;}
   for (int ii=0;ii<10;ii++)   //mix locations
       {int i1=rs.nextInt(max-1);          int i2=rs.nextInt(max-1);
        int  tempo=locations[i1];
        locations[i1]=locations[i2]; locations[i2]=tempo;}
    int j=1;  while (j<max)
      { StringTokenizer at = new StringTokenizer(s[j],"&");
        String term; int xt=0,  yt=0;
         while (at.hasMoreTokens())
    	{term= at.nextToken();
                 xt=Integer.parseInt(at.nextToken());	 
                 yt=Integer.parseInt(at.nextToken());
                imggr.drawString("*",xt-10,yt+20);                
                 int terml=fm.stringWidth(term);
                 if (locations[j-1]<(int)(max/2)){xo=50;} else {xo=(imw)+5+imx;}         
              int space =  2*(height-100)/max  ;
               yo=30+(48*((locations[j-1])%(max/2)));
               ana[j-1]= new  Anaterm(term, xo,yo,xt,yt, terml);
           // showStatus(""+xt+" ' "+yt);      
   }   j++;   
      }
repaint();
}
public void mouseClicked(MouseEvent me)
{int coY= me.getY(); int coX=me.getX();
  if((coY<60)&&(coX>width-60)){showHelp(); helpim=true;repaint();}
 else if ((helpim==true)&&(coX<600)&&(coY<400)){helpim=false; repaint();}
 }
public void mousePressed(MouseEvent me)
    {    int coX=me.getX(); int coY=me.getY() ;   for (int ii=0; ii<max; ii++) 
          {if ((coX<(ana[ii].xo+ana[ii].terml))&&(coX>ana[ii].xo)&&(coY<(ana[ii].yo+30))&&(coY>ana[ii].yo))
                                   {select=ii; offsetX=coX-ana[ii].xo; offsetY=coY-ana[ii].yo; startX=coX-offsetX; startY=coY-offsetY;}
           else{};}
  }  
public void mouseDragged(MouseEvent me)
 {if (select>=0) { int DragX=me.getX();int DragY=me.getY();ana[select].xo=DragX-offsetX; 
                         ana[select].yo=DragY-offsetY;      repaint();
  }   }
public void mouseMoved(MouseEvent me){}
public void mouseReleased(MouseEvent me)
{if(select>=0)  
 { int coX=me.getX(); int coY=me.getY() ;  
   if(( (coX-imx-offsetX) < (ana[select].xt))&&((coX-offsetX-imx+ana[select].terml) > (ana[select].xt))&&( (coY-imy-offsetY) < (ana[select].yt))&&((coY-imy-offsetY+30) > (ana[select].yt)))
     { ana[select].placed=true;select=-1; step2=true;evaluation="Correct";} 
   else {ana[select].xo=startX; ana[select].yo=startY;evaluation="Wrong"; }
 repaint();}
}
public void mouseEntered(MouseEvent e){};
public void mouseExited(MouseEvent e){};

public boolean checkPanelOver()
  {boolean end=true;
    for (int i=0; i<(max-1); i++)
      {end =(end&& ana[i].placed); if(end==false)return end;}
     return end;
   }
public void update(Graphics g)
{ offS.setColor(lightcream); offS.fillRect(0,0,width, height);
   offS.setFont(questionf); if(quizOver==false)
       { if(img==null)  {offS.drawString("no picture",20, 50);}     
         else { int imgwidth= (img.getWidth(null));   offS.drawImage(img, imx,imy,this);}
         for(int i=0; i<(max-1); i++)
          {  offS.setColor(darkcream); offS.fillRoundRect(ana[i].xo, ana[i].yo, 10+ana[i].terml, textheight,20,10);
             offS.setColor(Color.yellow);offS.drawString(ana[i].term, 5+ana[i].xo, textheight-5+ana[i].yo); }
        offS.setColor(Color.black);offS.drawString(evaluation,width-200,height-180);  
      }
  else if (quizOver==true){
 offS.setColor(lightcream); offS.fillRect(0,0,width, height);
  offS.setColor(Color.black);offS.drawString(evaluation,width-200,height-80);  
 }
 if(helpim==true){offS.drawImage(helpimage, 200,200,this);}
  else if(helpim==false){offS.setColor(darkcream) ; offS.fillRect(width-50, 0, 50,60);
         offS.setColor(Color.yellow); offS.setFont(new Font("Verdana",Font.PLAIN,40)); offS.drawString(" ?", width-40, 40); }

paint(g);
}
public void paint(Graphics g)
{g.drawImage(offScreen,0,0,this);}
class Anaterm
{int xo,yo,xt,yt,terml;
 boolean placed=false;  String term;
Anaterm (String termname, int actualx, int actualy, int targetx, int targety, int termlength)
    {term=termname; xo=actualx; yo=actualy;
      xt= targetx; yt= targety; terml= termlength;}
}
}
