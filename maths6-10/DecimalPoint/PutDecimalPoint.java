import java.awt.*;
import java.awt.event.*;
import java.applet.*;
import java.io.*;
import java.util.*;
import java.awt.image.*;


public class PutDecimalPoint extends Applet 
implements MouseListener, Runnable

{ String longstr[]= new String[20];
  Font questionf= new Font("Verdana", Font.PLAIN, 20);
 Image sumimg=null, offScreen, helpimage;// Image[] bgitems=new Image[10];
 String msg="",msg2="", evaluation="";
 Graphics imggr,  offS; 
 Color darkcream =new Color(200,84,6);Color verylightcream =new Color(244,115,23); Color lightcream= new Color(247,203,191);  
       boolean step1=true, step2=false; 
   boolean quizOver=false, isrunning=false,helpim=false;
    int  index=-1, max=0,  totalitems=0 ,score=0;
//int flickerPlace[]=new int[20]; 
char operator='+'; 
    int  width,height, xo=120,yo=100, textheight, width_;
   Thread roller;  FontMetrics fm;  
Term [] t;

public void init()    
{height=Integer.parseInt(getParameter("height")); width=Integer.parseInt(getParameter("width"));
 offScreen=createImage(width, height);  offS=offScreen.getGraphics();
 offS.setColor(lightcream); offS.fillRect(0,0,width, height);
 offS.setFont(questionf); fm=offS.getFontMetrics();
textheight=fm.getHeight(); width_=Math.min(fm.stringWidth("_"), fm.stringWidth("4"));
totalitems=0;
String inLine=null; InputStreamReader instream =null;
 try{ 
 instream= new InputStreamReader(getClass().getResourceAsStream("decimalpointquiz.txt"));
 BufferedReader dataStream = new BufferedReader(instream);
  while ((inLine=dataStream.readLine())!=null)
       {longstr[totalitems]=inLine;totalitems++;}
     }catch(IOException e){showStatus ("error in data reading");}
int tracked=0; 
StringTokenizer mst= new StringTokenizer(getParameter("text"),"$");
      while(mst.hasMoreTokens()&&tracked<20)      
	     {longstr[tracked]=mst.nextToken(); tracked++;}
totalitems=tracked;
setFont(questionf);
addMouseListener(this);
 }

public void showHelp()
{helpimage=createImage(400,200); Graphics helpimg=helpimage.getGraphics();
helpimg.setColor(darkcream);helpimg.fillRect(0,0,400,200);
helpimg.setColor(lightcream);helpimg.fillRect(5,5,390,190);
helpimg.setColor(Color.black);helpimg.drawRect(5,5,390,190);
helpimg.drawString("Program written by annemarie.govindraj@gmail.com", 10, 185);
helpimg.drawRect(204,116,40,40);helpimg.setFont(new Font("Verdana",Font.PLAIN,40)); helpimg.drawString("X",210,150);
}

public void start()
{if(roller==null)  {roller=new Thread(this);} isrunning=true;    roller.start();}
public void stop(){if(roller!=null)  {roller=null;}   isrunning=false;}

public void run()
{while( isrunning==true)
  {index=-1; while(index<totalitems)
    { if(step1==true)
        {step2=false;index++;evaluation=""; msg=""; msg2=""; t=parseSum(longstr[index]); sumimg=paintSum(t);
	findFlickerandDeciPt( t);
	if(operator=='X'){msg="Which blank should have the decimal point? Click it.";}
	else if((operator=='+')||(operator=='-')){msg="Which 2 blanks should have  decimal points? Click them.";} 
	evaluation="";
repaint(); 
         step1=false;  }
if (step2==true){ try{roller.sleep(5000);}catch(InterruptedException e){};  step1=true;}
for (int i=0; i<200; i++)
        { try{roller.sleep(30);}catch(InterruptedException e){};
          if((step1==true)||(step2==true)) break;}
     
}
quizOver=true; msg= "Exercise is over"; evaluation="Final Score  : "+score+" / "+totalitems; repaint();
}}
public Image paintSum(Term[] t)
{Image img=createImage((t[2].End)+10,textheight+10); imggr=img.getGraphics(); 
imggr.setColor(lightcream);  imggr.fillRect(0,0,(t[2].End)+10,textheight+10); 
imggr.setColor(Color.black);for (int i=0;i<3;i++)
    { imggr.drawString(  t[i].cutterm,t[i].Start,textheight+2);}
 for(int jj=0;jj<3; jj++){if((t[jj].cutterm).charAt(0)=='_')
	  {imggr.setColor(Color.yellow);for(int i=0;i<=(t[jj].nbstring).length();i++)
	     {imggr.fillRect(t[jj].Start+i*2*width_,4, width_,textheight+4);}}}
	imggr.setColor(Color.black);imggr.drawString(""+operator,  t[0].End+width_, textheight+2);
 imggr.drawString(  "=" , t[1].End+width_ ,textheight+2);
return img;
}


public Term[] parseSum(String fs )
 {Term []term=new Term[3];  int termnb=0;int start=0; int end=0;
 String cutterm= "";
 char[] cA=(fs+"=").toCharArray();   ///cannot handle end of line
String nbstring= "";
//try{Thread.sleep(2000);}catch(InterruptedException e){};
for(int ii=0;ii<cA.length; ii++)
	{if (cA[ii]=='_'){cutterm +="_";}
	else if(Character.isDigit(cA[ii])||(cA[ii]=='.')){cutterm+=cA[ii];nbstring+=cA[ii];}
	else if  ((cA[ii]=='+')||(cA[ii]=='-')||(cA[ii]=='=')||(cA[ii]=='X'))
  	    {end= (width_*(cutterm).length())+start;
		term[termnb]=new Term(cutterm, nbstring,start,end);	      
		 cutterm = ""; nbstring="";
		 termnb++;
	     start=end+2*width_;
	       if  ((cA[ii]=='+')||(cA[ii]=='X')||
		   (cA[ii]=='-')){operator=cA[ii];}
	}   }
return term;
}
public void findFlickerandDeciPt( Term[] t)
{ int deci=0; 
for( int jj=0;jj<3;jj++)
{char[] cA=t[jj].cutterm.toCharArray();   
  if(cA[0]=='_'){   ////alternate places will be _
	t[jj].setFlickerPlaces(t[jj].Start);t[jj].setDecipt(-1);}
  else {///// //there are no _ only digits and .
    	for (int l=(cA.length-1);l>=0;l--)  
     	 {if (cA[l]=='.'){break;}
		 else if(Character.isDigit(cA[l])){deci++;}
    	 }if(deci==cA.length){deci=0;}
     t[jj].setDecipt(deci);t[jj].setFlickerPlaces(-1);
}     }}
 public void evaluate(int whichterm, int deci)
 {float fl[]=new float[3];String st[]= new String[3]; char[] chnb=new char[10];
 if(operator=='X')
 { fl[whichterm]=(float)Integer.parseInt(t[whichterm].nbstring);chnb = (t[whichterm].nbstring +"$").toCharArray(); int l = chnb.length;
 for(int p=0;p<deci;p++){fl[whichterm]=fl[whichterm]/10; chnb[l-p-1]= t[whichterm].nbstring.charAt(l-p-2);}chnb[l-deci-1]='.';st[whichterm]=new String(chnb);
 for (int j=0;j<3;j++){if((j!=whichterm)&&(t[j].decipt>-1)){fl[j]=findfloat(t[j]);st[j]=t[j].cutterm;}}
if((fl[2]>=(fl[0]*fl[1]*0.99))&&(fl[2]<=(fl[0]*fl[1]*1.01))){evaluation="CORRECT"; score++;}
 else {evaluation="WRONG";
		msg2=""+st[0]+" X "+st[1]+" = "+(fl[0]*fl[1])+";  not   "+st[2];}
 step2=true;}
 else if ((operator=='+')||(operator=='-'))
 {msg2="";boolean boo=true;for(int j=0;j<3;j++){boo=boo&((t[j].guessedDeci>-1)||(t[j].decipt>-1));}
  if (boo==true)
  {for (int j=0;j<3;j++)
    {if(t[j].decipt>-1){fl[j]=findfloat(t[j]);st[j]=t[j].cutterm;}
     else if (t[j].guessedDeci>-1)
					 { fl[j]=(float)Integer.parseInt(t[j].nbstring);chnb = (t[j].nbstring +"$").toCharArray(); int l = chnb.length;
					  for(int p=0;p<t[j].guessedDeci;p++){fl[j]=fl[j]/10; chnb[l-p-1]= t[j].nbstring.charAt(l-p-2);}
                      chnb[l-t[j].guessedDeci-1]='.';st[j]= new String(chnb);}
	 else {showStatus ("All terms not covered");}
if(operator=='+')
 {if((fl[2]>=(fl[0]+fl[1])*0.99)&&(fl[2]<=(fl[0]+fl[1])*1.01)){evaluation="CORRECT"; score++;}
   else {evaluation="WRONG"; msg2=""+st[0]+" + "+st[1]+" = "+(fl[0]+fl[1])+";  not   "+st[2];}}
 else if(operator=='-')
 {if((fl[2]>=(fl[0]-fl[1])*0.99)&&(fl[2]<=(fl[0]-fl[1])*1.01)){evaluation="CORRECT"; score++;}
  else {evaluation="WRONG"; msg2=""+st[0]+" - "+st[1]+" = "+(fl[0]-fl[1])+";  not   "+st[2];}}

}step2=true;}
else {msg= "Click for the decimal point in the other term too";}
 }repaint();}
 
 
public float findfloat (Term tt)
 {//input (float) : Locate decimalpoint; read the rest and convert it to int ,then do the divisions according to decipoint
  char[] cA=tt.cutterm.toCharArray();int N=0, ic=0; float convert=0;
 int  decipt=20;    
 for(ic=0;ic<tt.cutterm.length();ic++)
   {if(cA[ic]=='.'){ decipt=ic;} 
    else if (Character.isDigit(cA[ic])){int n=Integer.parseInt(""+cA[ic]);N=N*10+n; 
	if(ic==(decipt+3)) break;}
   } 
  if (decipt==20) {convert=N;}
  else{ convert=(float)N; int dec=Math.min(3,(tt.cutterm.length()-decipt-1 ));
  for (int pt=0;pt<dec;pt++){convert=convert/10;}}
return convert;}      

public void mouseClicked(MouseEvent me)
{int coY= me.getY(); int coX=me.getX();
  if((coY<60)&&(coX>width-60)){showHelp(); helpim=true;repaint();}
 else if ((helpim==true)&&(coX<600)&&(coY<400)){helpim=false; repaint();}
 else if((coY>yo)&&(coY<(yo+textheight+10)))
 { int cooX=coX-xo; 
 for(int jj=0;jj<3;jj++){if((cooX>t[jj].Start)&&(cooX<t[jj].End))
    {int end =t[jj].End; int deci=0;
	for(int ip=0; ip<=t[jj].nbstring.length();ip++)
	  {if((cooX<end)&&(cooX>end-width_))
		{deci=ip; 
		Graphics sumg=sumimg.getGraphics(); sumg.setColor(Color.black); sumg.drawString(".", (t[jj].End)-deci*2*width_- 6, textheight+2);
	     t[jj].putGuessedDeci(ip);
			 repaint();
		 break;}
		else{deci++; end=end-2*width_;}
	  }evaluate(jj, deci);}
 }}}
 

public void mousePressed(MouseEvent me){}
public void mouseReleased(MouseEvent me){}
public void mouseEntered(MouseEvent e){};
public void mouseExited(MouseEvent e){};


public void update(Graphics g)
{ offS.setColor(lightcream); offS.fillRect(0,0,width, height);
   offS.setFont(questionf); offS.setColor(Color.black);
  if(quizOver==false){
	 offS.drawString(msg,50,50);
	 offS.drawImage(sumimg,xo,yo,this); 
	 
       offS.setColor(Color.black);offS.drawString(evaluation,200,yo+200); 
	  if((step2==true)&&(evaluation.equals("WRONG"))){offS.drawString(msg2,200,yo+250);}
       offS.drawString("score :  "+score+" / "+(index+1), 200,yo+300);
  } 
 else if (quizOver==true){
 offS.setColor(lightcream); offS.fillRect(0,0,width, height);
  offS.setColor(Color.black); offS.drawString(msg,50,50);
  offS.drawString(evaluation,50,200);  
  }
paint(g);
}
public void paint(Graphics g)
{g.drawImage(offScreen,0,0,this); 
  

  if(helpim==true){g.drawImage(helpimage, 200,200,this);}
 // else if(helpim==false){g.setColor(darkcream) ; g.fillRect(width-50, 0, 50,60); g.setColor(Color.yellow); g.setFont(new Font("Verdana",Font.PLAIN,40)); g.drawString(" ?", width-40, 40); }
}
class Term

{String cutterm, nbstring; int Start, End, decipt=0; int flickerPlaces=-1, guessedDeci=-1 ;
Term (String cutterm, String nbstring, int  start, int end)
{ this.cutterm=cutterm; this.nbstring=nbstring; Start = start; End = end;}
public void setFlickerPlaces(int pl)
   {flickerPlaces=pl;}
public void setDecipt(int d)  ////decipt in data
{decipt=d;}
public void putGuessedDeci(int d)   //////decipt put by student
{guessedDeci=d;}
  }}