import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.awt.image.*;
import java.io.*;


public class AddFractions extends Applet  implements Runnable, MouseListener, KeyListener
/*step=1 question + 2 pies different denom + fill in denominator+evaluation; step=2 fill in numerators and add +evaluation; step 3 showanswer piece of pie ratates + moves*/

{
 int height, width, pauselength,xo=10,x1=4, x2=304, yo=100, radius=146, denomlength, lineheight, nbsolvedex=7; 
 long counter=0; int theAnswer; 
  int  ex=0, step=1, flicker=-2,  nbfractions=-1; int answers[]={-1,-1,-1};
 int piex[] ={ 4,304}; Fraction fr[]= new Fraction[2];
  Image offScreenImage, pie, textpanel, correctimg, wrongimg,evalimg, textpanel2,textpanel1; //textg for showsum =basic question
  Graphics offS,pieg, textg,text1g; //text1g for showsteps for fillimg numerators with common denom; text2panel (no Graphics) for textgetLins
 Thread wind; Thread rotator=null; Thread painting;
boolean pause1=false, exerciseover=false, flickering=false, flickeron=false,isrunning=true , newquestion=false, evaluated=false;
int  correctanswer=0; //+1 = correct; -1=wrong; 0=not evaluated yet
 Color lightblue= new Color(0,250,252); Color shadowblue=new Color(67,201,214);Color lightyellow= new Color(255,245,191), verylight=new Color(166,244,175);
 String text[]=new String[10]; String guess="";String examples[]={"1/3+1/3", "2/5+1/5", "1/6+1/3","1/6+1/2","1/3+1/2", "1/5+1/3","2/3+1/4", "1/4+1/2","1/3+1/2", "1/3+1/4","1/6+1/2","1/2+1/5","1/5+1/3","1/2+1/8","2/5+1/3", "2/3+2/9","1/2+2/5","1/2+3/8", "3/4+1/6","2/3+2/5"};
String equifraction=""; String msg="";
 char sign=' ';
Random r = new Random();
Color [] ColorChoice ={ new Color(255,255,0),new Color(0,0,255),new Color(255,0,0),new Color(0,255,0), new Color(228,85,228),new Color(250,184,184), new Color(78,214,173),new Color(47, 166, 187)};
Font textfont=new Font("Verdana", Font.PLAIN, 20); 
FontMetrics fm;    

public void init()
{text[0]="When the denominators are the same, that is when both pies are cut the same way, we can just add the numerators leaving the same, common denominator"; 
 text[1]="Here the denominators are not the same";
text[2]="if we add the fractions as such, we don't reach one of the denominators, neither ";
text[3]="  let's try cutting the pie in smaller pieces and try with the smallest common denominator";
text[4]="Which is the smallest common denominator ? .  Denominator =  ";
text[5]=" Smallest common denominator =";
text[6]="Very good. Next Exercise"; 
addMouseListener(this); addKeyListener(this); requestFocus();
height=Integer.parseInt(getParameter("height"));width=Integer.parseInt(getParameter("width"));
offScreenImage=createImage(width, height); offS=offScreenImage.getGraphics();
offS.setColor(lightblue); offS.fillRect(0,0,width,height); 
offS.setFont(textfont);  fm=offS.getFontMetrics(); denomlength = fm.stringWidth("Denominator = ");
  lineheight=fm.getHeight();
 pie=createImage(600, 300); pieg=pie.getGraphics();pieg.setColor(lightblue); pieg.fillRect(0,0,600,300); 
}

public void start()
{if(wind==null)  {wind=new Thread(this);}
  isrunning=true;wind.start();}
public void stop()
{if(wind!=null)  { isrunning=false; wind=null;} } 

public void run()
{while (isrunning==true)
 {flicker=-2; 
 while(ex< examples.length)
	{if(step==1)
		{newSum(ex);  evalimg=null; guess=""; exerciseover=false;  evaluated=false;flickering=false; counter=0;
		nbfractions=  fr[0].getCommonDenom(fr[0], fr[1]);
		textpanel=createImage(300, 80); textg=textpanel.getGraphics();textg.setFont(textfont); showSum();//string of fractions to add  
		textpanel1=null; textpanel2=null;
		pieg.setColor(lightblue); pieg.fillRect(0,0,600,300); 
		if(ex<2){textpanel1=textGetLines( text[0],360);}
		else if(ex<4){textpanel1=textGetLines( text[1],360);}
		else if(ex<7){textpanel1=textGetLines( text[2]+fr[0].denom+" nor "+fr[1].denom+text[3], 360);}
		else if(ex>=nbsolvedex){textpanel1=textGetLines( text[4], 380);}
	for(int ii=0; ii<2; ii++)
			{fillFraction(piex[ii],  fr[ii], (int)(fr[ii].startangle*360+90));
			drawPie(fr[ii].denom, piex[ii] ); }      
	repaint(); 	
	
	if(ex==4){		///// loading pictures+cutting+storing              
		int tracked =0;String imgname[]= new String[5]; Image img[]=new Image[5];
		try{MediaTracker tracker = new MediaTracker(this);
			StringTokenizer mst= new StringTokenizer(getParameter("picture"),"$");
			while(mst.hasMoreTokens()&&tracked<5)      
			{imgname[tracked]=mst.nextToken();
			img[tracked]=getImage(getCodeBase(),imgname[tracked]+".gif");  //getImage(getClass().getResource(imgname[tracked]+".gif"));
			tracker.addImage(img[tracked], tracked);
			tracked++;}tracker.waitForAll();}
		catch(InterruptedException e){showStatus("no pict found");}
		correctimg=getAlpha(img[0],104,77); wrongimg=getAlpha(img[1],77,60);
		}
	if((ex>3)&&(ex<nbsolvedex)) { 
	 rotate(piex[0],  fr[0], piex[1],  fr[1]);//repaint();
	fillFraction(piex[1],  fr[1], (int)(fr[1].startangle*360+90)); pieg.setColor(Color.black);
	pieg.drawOval(piex[1],4,2*radius,2*radius); drawPie(fr[0].denom, piex[0] ); 
	repaint();    
	translate(piex[0],  fr[0], piex[1],  fr[1]); 
	drawPie(fr[0].denom, piex[0] );
	repaint(); fr[1].startangle=0;
	//pause1=true; try {synchronized(this){while(pause1==true) wait();}}catch(InterruptedException e){};
		}
		if(ex<nbsolvedex){
    pause1=true; try {synchronized(this){while(pause1==true) wait();}}catch(InterruptedException e){}; 
			step=3;}
	
	if ( ex >= nbsolvedex) 
		{//pause1=true; try {synchronized(this){while(pause1==true) wait();}}catch(InterruptedException e){}; 
		flicker=-1; flickering=true; 
		while(flickering==true)
			{counter++;  try{ wind.sleep(100); } catch(InterruptedException e){};
			if((counter>100)&&(evaluated==false)&&(guess.length()==1)) {evalimg=wrongimg; correctanswer=-1;  } 
			if (counter%8==0){ flickeron=!flickeron;}
			repaint(); //630,yo+100,width,height-100);  
			if(evaluated==true) flickering=false;}
	if(correctanswer==1){ flickeron=true; 
	for(int ii=0; ii<2; ii++) {drawPie(nbfractions, piex[ii] ); }      
	repaint(); try{ wind.sleep(1000); } catch(InterruptedException e){};
	step=2;flicker=0;}}
}

if(step==2){evalimg=null; guess=""; for(int ij=0;ij<3; ij++){answers[ij]=-1;} exerciseover=false;
 showSteps(); repaint(); 
 while(flicker<3){
  flickering=true; evaluated=false; guess="";correctanswer=0;
   while(flickering==true){counter++; if (counter%8==0){ flickeron=!flickeron;} 
   if((counter>200)&&(guess.length()==1)) {correctanswer=-1; }
  repaint();//630,yo+100,420,height-yo-100);  
   try{ wind.sleep(100); } catch(InterruptedException e){};}}
   try{ wind.sleep(500); } catch(InterruptedException e){}; step=3; 
 }

 if(step==3)
 {
 if((ex>3)&&(ex<nbsolvedex)) {////////need of common denom
	//nbfractions= getCommonDenom(fr[0], fr[1]);// SCdenom[ex]; //
	pieg.setColor(lightblue); pieg.fillRect(0,0,600,300); 
		for(int ii=0; ii<2; ii++)
			{fillFraction(piex[ii],  fr[ii], (int)(fr[ii].startangle*360+90));
			drawPie(nbfractions, piex[ii] ); }      
		repaint();
			}
 if(ex>1)
	{int equinum0=fr[0].num*nbfractions/fr[0].denom; 
	 int equinum1=fr[1].num*nbfractions/fr[1].denom;
 
 if(ex<4){equifraction="$"+fr[1].num+"!/"+fr[1].denom+"! = $"+equinum1+"!/"+nbfractions+"! . So, . $"+fr[0].num+"!/"+fr[0].denom+"! + $"+fr[1].num+"!/"+fr[1].denom+
	 "! = . $"+fr[0].num+"!/"+nbfractions+"! + $"+equinum1+"!/"+nbfractions+"! = $"+(equinum1+fr[0].num)+"!/"+nbfractions+"! . "+nbfractions+"  is a common denominator" ;}
 else if(ex<nbsolvedex){equifraction="$"+fr[0].num+"!/"+fr[0].denom+"! = $"+equinum0+"!/"+nbfractions+"! and $"+fr[1].num+"!/"+fr[1].denom+"! = $"+equinum1+"!/"+nbfractions+"! . So, . $"+fr[0].num+"!/"+fr[0].denom+"! + $"+fr[1].num+"!/"+fr[1].denom+
	 "! = . $"+equinum0+"!/"+nbfractions+"! + $"+equinum1+"!/"+nbfractions+"! = $"+(equinum0+equinum1)+"!/"+nbfractions+"!  ";}
	
	if(ex<4) {textpanel2=textGetLines(" We see that . "+ equifraction, 380);}  ///
	else if(ex<nbsolvedex){textpanel2=textGetLines(text[5]+nbfractions+" . "+equifraction, 420);}
	//else {textpanel2=textGetLines(equifraction, 420);}
	
	repaint();
	//try {wind.sleep(1000);}catch(InterruptedException e){}; 
	}
if((ex<nbsolvedex)&&(ex>3))
	{pause1=true; try {synchronized(this){while(pause1==true) wait();}}catch(InterruptedException e){}; }
		
 rotate(piex[0],  fr[0], piex[1],  fr[1]);//repaint();
 fillFraction(piex[1],  fr[1], (int)(fr[1].startangle*360+90)); pieg.setColor(Color.black);
 pieg.drawOval(piex[1],4,2*radius,2*radius);  
 repaint();    
 translate(piex[0],  fr[0], piex[1],  fr[1]); 
 drawPie(nbfractions, piex[0] );
  if(ex>=nbsolvedex){textpanel2=textGetLines( text[6], 380); repaint();
 try{ wind.sleep(1000); } catch(InterruptedException e){}; }
 
 else if(ex<2) {showSum();}
 exerciseover=true; repaint(); 
pause1=true; try {synchronized(this){while(pause1==true) wait();}}catch(InterruptedException e){}; 
 }
}}}


public Image drawFraction(int N, int D)  // N=nominator, D=denominator
{Image fract=createImage(30, 75); Graphics frg=fract.getGraphics();
frg.setColor(lightyellow); frg.fillRect(0,0,33,75);frg.setColor(Color.black);frg.setFont(textfont); 
frg.drawLine(2,35,27,35);
if(D!=0){frg.drawString(""+N, 5,30);frg.drawString(""+D, 5,65);}
else {frg.drawString(""+nbfractions, 5,65);}
return fract;}

public void drawPie(int nbparts, int wherex)  //draws only outlines in black
{  pieg.setColor(Color.black);
   pieg.drawOval(wherex,4,2*radius,2*radius);  
   for(int i=0; i<nbparts; i++)
          {pieg.drawLine(wherex+radius, 4+radius, wherex+radius+(int)(radius*Math.cos(Math.PI/2-2*Math.PI*i/nbparts)),4+radius-(int)(radius*Math.sin(Math.PI/2-2*Math.PI*i/nbparts)));}
}
public void drawPieRot(int nbparts, int wherex, float beginangle)
{  pieg.setColor(Color.black);
   pieg.drawOval(wherex,4,2*radius,2*radius);  
   for(int i=0; i<nbparts; i++)
          {pieg.drawLine(wherex+radius, 4+radius, wherex+radius+(int)(radius*Math.cos(2*Math.PI*beginangle+Math.PI/2-2*Math.PI*i/nbparts)),4+radius-(int)(radius*Math.sin(2*Math.PI*beginangle+Math.PI/2-2*Math.PI*i/nbparts)));}
}
public void evaluate1(String guess)
{int answer=Integer.parseInt(guess); correctanswer=0;
 if((answer%fr[0].denom==0)&&(answer%fr[1].denom==0)){nbfractions= answer; 
             correctanswer=1; evalimg=correctimg; guess=""; repaint(); 
			 flickering=false; flickeron=true;
			 evaluated=true; flicker++; counter=0;}
}			 
public void evaluate2(String guess)
{int answer=Integer.parseInt(guess); correctanswer=0;  theAnswer=-1;
switch(flicker)
{case -1 : theAnswer= fr[0].getCommonDenom(fr[0], fr[1]);
			if((answer%fr[0].denom==0)&&(answer%fr[1].denom==0)){nbfractions= answer; //theAnswer=answer;
             correctanswer=1; evalimg=correctimg; guess=""; repaint(); }
			else { evalimg=wrongimg;} 
			 break;
  case 0 : case 1 : theAnswer=(fr[flicker].num*nbfractions/fr[flicker].denom);
			if(answer==theAnswer)
            { answers[flicker]= answer;   
			 text1g.setColor(Color.black); text1g.drawString(""+answers[flicker], 20+flicker*50+5, 132);
			 correctanswer=1;  guess="";
			 text1g.drawImage(correctimg, 20+50*flicker,75,35,35,this); repaint(); }
			 else if (theAnswer<10){correctanswer=-1; evalimg=wrongimg; repaint();}
			break;
  case 2 : theAnswer=answers[0]+answers[1];
			if(answer==theAnswer) { answers[2]=answer;
			text1g.setColor(Color.black); text1g.drawString(""+answers[2], 20+100+5, 132);
			 correctanswer=1;  guess=""; 
			 text1g.drawImage(correctimg, 20+50*flicker,75,35,35,this); repaint(); }
			else if (theAnswer<10){correctanswer=-1; evalimg=wrongimg; repaint();} 
			break;
  default : msg= "reached default";}
  if(correctanswer==1){  evaluated=true; flicker++; counter=0; flickering=false; flickeron=true;}
   else if(guess.length()==2) 
    {evalimg=wrongimg; correctanswer=-1;
	 repaint();}
}

public void fillFraction(int x, Fraction f, int thestartangle)
{Color c=f.fractionc;                         ////////thestartangle in degrees int !
 int sweep= (int)(360*(float)f.num/f.denom); 
  pieg.setColor(c);
  pieg.fillArc(x,4,2*radius, 2*radius, thestartangle,-sweep);//pieg.drawString(""+sweep,200,50);
}
public void newSum(int ex)
{ int index=0;
   int numerator =Integer.parseInt(""+examples[ex].charAt(index));
       index+=2; ///for / bar
   //while(examples[ex].charAt(index)==' ')   index++;
	int denom= Integer.parseInt(""+examples[ex].charAt(index));
    fr[0]= new Fraction (numerator, denom, 0, ColorChoice[2*(ex%4)]);	
    //while(examples[ex].charAt(index)==' ')
	index++;
	sign= examples[ex].charAt(index);
	// while(examples[ex].charAt(index)==' ')
	index++;
	numerator= Integer.parseInt(""+examples[ex].charAt(index));
    index+=2; ///for / bar 
	//while(examples[ex].charAt(index)==' ')	index++;
	denom= Integer.parseInt(""+examples[ex].charAt(index));
	   fr[1]= new Fraction (numerator, denom, 0,ColorChoice[2*(ex%4)+1]);	
 }	

public void rotate(int xxo, Fraction fra, int xx1, Fraction frb)
{if(rotator==null){rotator=new Thread();}
 rotator.start();
 int start = (int)( frb.startangle*360+90);
  int end= (int)((fra.startangle-(float)fra.num/fra.denom)*360+90);
 for (int theta=start; theta>=end; theta-=2)
    {pieg.setColor(lightblue); pieg.fillRect(xx1,0,4+radius*2, 300);
	 fillFraction(xx1,frb,theta);
	pieg.setColor(Color.black); pieg.drawOval(piex[1],4,2*radius,2*radius);  
	if(step==1){drawPie(fra.denom, piex[0]);}
	else {drawPie(nbfractions, piex[0]);}
	repaint(0,0,xo+600,yo+300);
	try{rotator.sleep(20);}catch(InterruptedException e){}; 
 }rotator=null;
 frb.startangle= fra.startangle-(float)fra.num/fra.denom; 
 }
public void showSteps()
{textpanel1=createImage(300, 200);  text1g=textpanel1.getGraphics(); text1g.setColor(lightyellow);
 text1g.setFont(textfont);  text1g.fillRect(0,0,300,200);
  for (int ii=0;ii<2;ii++){Image frpanel=drawFraction(fr[ii].getNum(), fr[ii].getDenom());
 text1g.drawImage(frpanel,20+ 50*ii,0,this);}
 text1g.setColor(Color.black); text1g.drawString(""+sign,55,35);text1g.drawString("= ",108,35);
 Image frpanel=drawFraction(0,0); 
 for (int ii=0;ii<3;ii++) { text1g.drawImage(frpanel,20+50*ii,100,this);}
 text1g.setColor(Color.black); text1g.drawString(""+sign,55,135);text1g.drawString("= ",108,135);
 }
public void showSum()
{ textg.setColor(lightyellow); textg.fillRect(0,0,300,80);
  for (int ii=0;ii<2;ii++){Image frpanel=drawFraction(fr[ii].getNum(), fr[ii].getDenom());
 textg.drawImage(frpanel,20+ 50*ii,0,this);}
 textg.setColor(Color.black); textg.drawString(""+sign,50,37);
 if((ex<2)&&(step==3)){textg.drawString("= ",108,37); textg.drawImage(drawFraction(fr[0].getNum()+fr[1].getNum(),fr[0].getDenom()),130,0,this);}
 else{textg.drawString("= ?",108,37);}
 }
 
public void translate(int xxo, Fraction  fra, int xx1, Fraction  frb)
{  if (ex<=1){nbfractions=fra.denom;}

for (int x= xx1; x>=xxo; x-=5)
   { pieg.setColor(lightblue); pieg.fillRect(0,0,600, 300);
	 fillFraction(xxo,fra,(int)(360*(fra.startangle)+90));
     fillFraction(x,frb,(int)(360*(frb.startangle)+90));
 pieg.setColor(Color.black); pieg.drawOval(piex[1],4,2*radius,2*radius);  
	if(step==1){drawPie(fra.denom, xxo );}
	else {drawPie(nbfractions,xxo);}
	repaint(0,0,xo+600,yo+300);
	try{rotator.sleep(20);}catch(InterruptedException e){}; 
 }
 rotator=null;
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

public Image textGetLines( String s, int widthRect) ///text with sub or superscripts 
{  int wl=0;   fm=getFontMetrics(textfont);  
  int nblines=0; String[] mylines=new String[30];
        int linel=0;  
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
			if(strfraclength[nbfrac]>strsuperlength) {Xsublines[nbsup+nbsub+1]+=(strfraclength[nbfrac]-strsuperlength);}
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
		jx=jx+fm.getHeight()-5;}}
 else {	msg.drawString(mylines[j],4,jx) ;} 
 } //next line
CropImageFilter f=new CropImageFilter(0, 0,widthRect, jx+5);
 FilteredImageSource fis =new FilteredImageSource(im.getSource(), f);
 Image imm =createImage(fis);
  return imm; }
 
 public void keyPressed(KeyEvent ke)
 { if (ke.getKeyCode()==KeyEvent.VK_BACK_SPACE)
    {guess="";           //  wpg.setColor(Color.orange);wpg.fillRect(xo+dflicker*space, yo-30*(dcut), 30 ,30);}
       repaint();}  
  else if(ke.getKeyCode()==KeyEvent.VK_ENTER)
     { if(evaluated==false) {evaluate2(guess);} } 
    }
public void keyReleased(KeyEvent ke){};

public void keyTyped(KeyEvent ke)
 { //long msecnow=date.getTime();
   char mych=ke.getKeyChar(); 
  if(Character.isDigit(mych)==true)
    { guess+=mych;
      repaint();
	  if(step==2){ evaluate2(guess);}
		else if ((step==1)&&(evaluated==false)&&(guess.length()==2)){ evaluate2(guess);}
	  else if ((step==1)&&(guess.length()==1)){evaluate1 (guess);	 }
}  }
  
public void mousePressed(MouseEvent me){}
public void mouseReleased(MouseEvent me){}

synchronized public void mouseClicked(MouseEvent me)
  { int coX=me.getX(); int coY=me.getY(); 
      if((coY>height-100)&&(coX>390))
         {	if(exerciseover==true)   {  step=1; ex++;}
						pause1=false; notify(); }  
  }             
public void mouseEntered(MouseEvent me){}
public void mouseExited(MouseEvent me){}

public void update(Graphics g)
{offS.setColor(lightblue); offS.fillRect(0,0,width,height); 
 offS.setColor(Color.black);offS.setFont(textfont); 
 offS.setColor(shadowblue); offS.fillRect(width-200,height-60,180,50); 
 offS.setColor(Color.black);
 if(exerciseover==true){offS.drawString("NEXT", width-180, height-20);}
 else if (exerciseover==false){if(ex<4){offS.drawString("ADD", width-180, height-20);}  
				else {offS.drawString("CONTINUE", width-180, height-20);}}  	
	
 offS.drawImage(pie,xo,yo,this);
 offS.drawImage(textpanel,620,yo,this); ///textpanel height= 80 in ex<2; 

 if(step==1)
	{offS.drawImage(textpanel1,630,yo+100,this); 
	if(ex>=nbsolvedex){if(flickeron==true)
			{offS.setColor(verylight);offS.fillRect(630+denomlength,yo+80+3*lineheight,30,30); }
		offS.setColor(Color.blue); offS.drawString( guess, (635+ denomlength) , 100+yo+3*lineheight);
		if(evalimg!=null){offS.drawImage(evalimg,(665+ denomlength) , yo+60+3*lineheight,50,50, this);}}
	}
 else if(step==2)
	{offS.drawImage(textpanel1,620,yo+100,this);
	if(flickeron==true){offS.setColor(verylight);offS.fillRect(640+flicker*50,yo+200,30,30); }
	 if(evaluated==false){offS.setColor(Color.blue); offS.drawString(""+guess, 655+flicker*50,yo+232);	}
	 if(correctanswer==-1){offS.drawImage(wrongimg, 640+50*flicker, yo+175,35,35, this);}
		//{offS.setColor(Color.blue); offS.drawString("correct answer is "+theAnswer, 625, yo+180);}
	}
 else if(step==3) 
 { if (ex>nbsolvedex){offS.drawImage(textpanel1,620,yo+100,this);}
    else if (ex>3){offS.drawImage(textpanel2,620,yo+100,this);}
    else if ((ex>1)&&(exerciseover==true)){offS.drawImage(textpanel2,620,yo+100,this);}
	else if (ex<2){offS.drawImage(textpanel1,630,yo+100,this);}
 }
paint(g);
}
public void paint(Graphics g)
   {g.drawImage(offScreenImage,0,0,this);}

class Fraction
{ int num=0; int denom=0; float startangle=0;float endangle=0; Color fractionc; //int [] PrimeFactors;
  Fraction (int numerator, int denominator, float startangle, Color c)
    {num=numerator; denom=denominator; this.startangle=startangle; fractionc=c; }
 public int getNum() {return num;}
 public int getDenom() {return denom;}
 public float getStartAngle() {return startangle;} //angle is in fraction of circle e.g. if 1/2 then 180 degree
 public float getEndAngle() {endangle=startangle+num/denom;
return endangle;}
 public int[] primFactors ()
        { int j=0; int []PrimeFactors=new int[denom]; Arrays.fill(PrimeFactors,1);int quotient=denom; int i=2;
         while (quotient!=1){if(quotient%i==0) {PrimeFactors[j]=i; j++; quotient=quotient/i; } else{i++;}}
		 return PrimeFactors;}
 public int getCommonDenom(Fraction f1, Fraction f2)
 {int [] primesa=f1.primFactors(); int [] primesb=f2.primFactors(); int commondenom=1;
 int ii=0, jj=0; 
  while((ii<primesa.length)&&(jj<primesb.length))
	{if(primesa[ii]==primesb[jj]) {commondenom*=primesa[ii];ii++;jj++;}
	else if (primesa[ii]<primesb[jj]) {commondenom*=primesa[ii]; ii++;}
	else {commondenom*=primesb[jj]; jj++;}}
  while (ii<primesa.length) {commondenom*=primesa[ii]; ii++;}
  while (jj<primesb.length) {commondenom*=primesb[jj]; jj++;}
  return commondenom;

}}
}