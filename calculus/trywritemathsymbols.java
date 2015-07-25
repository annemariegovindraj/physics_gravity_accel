import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.awt.image.*;

public class trywritemathsymbols extends Applet
{
String text1, text2, text3;
//char[] tempchars;
Image im; Image offScreenImage;
Graphics offS; 
Font textfont; FontMetrics fm;//new Font("Verdana", Font.PLAIN, 18); 
Color lightyellow= new Color(255,245,191); Color liteblue=new Color(47, 166, 187);

public void init()
{ offScreenImage=createImage(800,500); offS=offScreenImage.getGraphics();
 offS.setColor(liteblue);offS.fillRect(0,0,800,500); Font textfnt= offS.getFont();
  String fontname=textfnt.getName(); textfont= new Font(fontname, Font.PLAIN,18);
 offS.setFont(textfont);
  fm=offS.getFontMetrics();
 
text1=" There is one super-cool function (an infinite polynomial) whose derivative gives the function itself :"; 
text2="There is one super-cool function (an infinite polynomial) whose derivative gives the function itself : $ d( 1x\u2070 +x + x²+x³+x\u2074+! )";
 text3=" $ d!($1x\u2070 +x + x²+x³+x\u2074+ .. !";
 im=textGetLines(text2,550);
/*im=createImage(700,200); Graphics msg=im.getGraphics();
 //Font f=msg.getFont(); String fontname=f.getName();
 //msg.setFont(new Font(fontname, Font.PLAIN,18));
 FontMetrics fm=msg.getFontMetrics();  
 msg.setColor(lightyellow);msg.fillRect(0,0,500,200);
 msg.setColor(Color.black); 
msg.drawString(text1,20,100); 
 msg.setColor(Color.red); 
 tempchars= text2.toCharArray();
 msg.drawChars(tempchars,0,tempchars.length,20,150);*/
update(offS);}


public Image textGetLines( String s, int widthRect) ///text with sub or superscripts 
{  int wl=0; 
  int nblines=0; String[] mylines=new String[50];
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
         int nbsub=0, nbsup=0;  int Xsuperscript[]= new int[20];String strsuper[]=new String[20];  ///place and comtent of superscripts
		 int Xsubscript[]= new int[20];String strsub[]=new String[20];  ///place and content of subscript
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
  { for( int i=0; i<=(nbsub+nbsup);i++)
 {char[] temp= sublines[i].toCharArray();
  msg.drawChars(temp,0,temp.length, Xsublines[i],jx) ;}
	if(nbsub>0){//msg.setFont(new Font("Dialog", Font.PLAIN,12)); 
	for( int i=0; i<(nbsub);i++){
	char[] temp= strsub[i].toCharArray();
	msg.drawChars(temp,0,temp.length,Xsubscript[i], jx+1);}
    } // msg.drawString(strsuper[0],200,20);
	if(nbsup>0){ for( int i=0; i<(nbsup);i++)
	{char[] temp= strsuper[i].toCharArray();
	 msg.drawChars(temp,0,temp.length,Xsuperscript[i], jx-6);}}
	if(nbfrac>0){for( int i=0; i< nbfrac;i++)
	 { char[] temp= strfrac[i].toCharArray();
	 msg.drawChars(temp,0,temp.length, Xfrac[i], jx+fm.getHeight()-3);
	 msg.drawLine(Xfrac[i],jx-3,Xfrac[i]+strfraclength[i],jx-3);}
	   // msg.drawString(strfrac[i],Xfrac[i], jx+fm.getHeight()-3);}
		jx=jx+fm.getHeight()-5;}}
 else 
 {	 jx=jx+fm.getHeight(); 
 char[] temp= mylines[j].toCharArray();
  msg.drawChars(temp,0,temp.length, 4,jx) ;
	
 //msg.drawString(mylines[j],4,jx) ;
    }
 } //next line
CropImageFilter cf=new CropImageFilter(0, 0,widthRect, jx+5);
 FilteredImageSource fis =new FilteredImageSource(im.getSource(), cf);
 Image imm =createImage(fis);
  return imm; } 

public void update(Graphics g)////////////////////////////////////////////////
{offS.setColor(liteblue); offS.fillRect(0,0,800,500); 
 offS.drawImage(im,20,20,this); 
 paint(g);}

public void paint(Graphics g)
  { g.drawImage(offScreenImage,0,0,this);
} }	