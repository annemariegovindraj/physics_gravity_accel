import java.awt.*;
import java.util.*;
import java.awt.FontMetrics;
import java.awt.image.*;

class Mathstext
 {	Image textbox,trimm;	
	Graphics msg; 
	int xoffset=0, yoffset=0, widthRect, heightRect; //for image
	int index=0,yline=2 , spacelength; //for processing text
		int lineheight; 
	FontMetrics fm;
	Font textfont= new Font("Verdana", Font.PLAIN, 18);
	Font lilfont= new Font("Verdana", Font.BOLD, 14);
	Color lightyellow= new Color(255,245,191);
	Color textcolor= Color.black; 
	Color bgcolor;
	
  public Mathstext (Image textbox, int widthrect, int heightrect, int Letterheight )
	{widthRect=widthrect; heightRect=heightrect;
	 this.textbox=textbox;
	 msg=textbox.getGraphics();
	lineheight=Letterheight;
	bgcolor=lightyellow;
	}
	
 public void setmyStyle(Color bg, Color textcolor) 
  { bgcolor=bg; this.textcolor=textcolor;  
	}
	
 public void setmyText (String s) 
{    msg.setColor(bgcolor); msg.fillRect(0,0,widthRect,heightRect);
	 msg.setColor(textcolor);  
	 msg.setFont(textfont);
	 index=0;
	 s=s+" . ";  //  fullstop to find and print end of string
	fm=msg.getFontMetrics();
	spacelength = fm.stringWidth(" "); int letterheight=fm.getHeight();
	yline=2+lineheight;
	 					//contains the text
	int wordl=0; int Xsublines=4; int linel=4; 
	    
	int  Xsuperscript=0; String strsuper="";  ///place and content of superscripts
	int  Xsubscript=0; String strsub="";  ///place and content of subscript
	int strsuperlength=0, strsublength=0;
 	 int startindex=0,endindex =0;
	while(index< s.length())
	{ if (s.charAt(index)=='#')
		{msg.drawString(s.substring(startindex, endindex), Xsublines, yline);//msg.drawString(linee, Xsublines, yline);
		 Xsublines+= fm.stringWidth(s.substring(startindex, endindex))+spacelength; 
		 //linee=""; 
		 Xsuperscript=Xsublines;//reference to beginning of fraction
		 int Numstartindex=index+1;
		 int Numendindex=Numstartindex;
		 while((Numendindex<s.length())&&(s.charAt(Numendindex)!='/'))
			{Numendindex++;}
		 int Denomstartindex=Numendindex+1;
		 int Denomendindex= Denomstartindex;
		 while((Denomendindex<s.length())&&(s.charAt(Denomendindex)!='#'))
			{Denomendindex++;}
// msg.drawString("fract from  "+Numstartindex+" to "+Numendindex+"and from "+Denomstartindex+" to "+Denomendindex,10,400);
		String Numstring= s.substring(Numstartindex,Numendindex); 
		String Denomstring= s.substring(Denomstartindex,Denomendindex); 
		strsuperlength=fm.stringWidth(Numstring);strsublength=fm.stringWidth(Denomstring);		
		strsuperlength= Math.max(strsuperlength,strsublength);					
		linel=Xsuperscript+strsuperlength;
		if (linel>(widthRect-6))
					{ linel=4;  Xsublines=4; Xsuperscript=4; yline+=lineheight;  }
	 
		StringTokenizer tt=new StringTokenizer(Numstring,"$!"); 
		int wl=0;
		while(tt.hasMoreTokens())
			{String nl= tt.nextToken();
			 msg.drawString(nl,Xsuperscript+wl, yline-5);
			 wl=wl+fm.stringWidth(nl);
			 if (tt.hasMoreTokens())
				{String exp= tt.nextToken();
				msg.setFont(lilfont);
				msg.drawString(exp,Xsuperscript+wl, yline-4-(int)(letterheight/2));
				wl=wl+fm.stringWidth(exp);
				msg.setFont(textfont);}
			}
			int numstrlength=wl;	  
			StringTokenizer ts=new StringTokenizer(Denomstring,"$!"); 
			wl=0;
		while(ts.hasMoreTokens())
			{String nl= ts.nextToken();
			 msg.drawString(nl,Xsuperscript+wl, yline+(int)(letterheight*3/4));
			 wl=wl+fm.stringWidth(nl);
			 if (ts.hasMoreTokens())
				{String exp= ts.nextToken();
				msg.setFont(lilfont);
				msg.drawString(exp,Xsuperscript+wl, yline+(int)(letterheight/2));
				wl=wl+fm.stringWidth(exp);
				msg.setFont(textfont);}
			}
		int denomstrlength=wl;	  
		//msg.drawString(Numstring, Xsuperscript, yline-3);
		//int numstrlength= fm.stringWidth(Numstring);
		//	msg.drawString(Denomstring, Xsuperscript, yline+(int)(letterheight/2)+3);
		// int denomstrlength = fm.stringWidth(Denomstring);
		strsuperlength= Math.max(numstrlength,denomstrlength);					
		msg.drawLine(Xsuperscript,yline-3,Xsuperscript+strsuperlength,yline-3);
	
		index=Denomendindex+1;startindex=index ; endindex=index;
					
		Xsublines= Xsuperscript+ strsuperlength +spacelength;
		}	
	else if(s.charAt(index)=='$')
	  {	msg.drawString(s.substring(startindex, endindex), Xsublines, yline);  //linee
		Xsublines+= fm.stringWidth(s.substring(startindex, endindex));
		wordl=fm.stringWidth(s.substring(endindex, index));
		Xsuperscript=Xsublines+spacelength+wordl;
		String leftstring=s.substring(index+1, s.length());  //leftstring=nonprocessed string
		int end=leftstring.indexOf('!');
		strsuper= leftstring.substring(0,end);
		strsuperlength= fm.stringWidth(strsuper);
		linel=Xsuperscript+strsuperlength;
		if (linel>(widthRect-12))
					{ linel=4;  Xsublines=4; Xsuperscript=4+wordl; yline+=lineheight;  }
		msg.drawString(s.substring(endindex, index), Xsuperscript-wordl ,yline) ;
		msg.setFont(lilfont);
		msg.drawString(strsuper, Xsuperscript, yline-(int)(letterheight/2)) ;
		
		Xsublines=Xsuperscript+strsuperlength;
		msg.setFont(textfont);
		index=index+2+end; 
		startindex=index ; endindex=index;
	 }
	else if(s.charAt(index)=='%')
          {msg.drawString(s.substring(startindex,endindex), Xsublines, yline);	    
		Xsublines= Xsublines+fm.stringWidth(s.substring(startindex,endindex));
		wordl=fm.stringWidth(s.substring(endindex, index));
		Xsubscript=Xsublines+spacelength+wordl;
		String leftstring=s.substring(index+1, s.length());  //leftstring=nonprocessed string
		int end=leftstring.indexOf('!');
		strsub= leftstring.substring(0,end);
		strsublength= fm.stringWidth(strsub);
		
 	    linel=Xsubscript+strsublength;
		if (linel>(widthRect-12))
					{ linel=4;  Xsublines=4; Xsubscript=4+wordl; yline+=lineheight;  }
		msg.drawString(s.substring(endindex, index), Xsubscript-wordl,yline) ;
		msg.setFont(lilfont);
		msg.drawString(strsub, Xsubscript, yline+(int)(letterheight/2)-3) ;
		Xsublines= Xsubscript+strsublength;
	    msg.setFont(textfont);
		index=index+2+end;
		startindex=index ; endindex=startindex;
	}
	
	else if ((""+s.charAt(index)).equals(" "))
	{	wordl=fm.stringWidth(s.substring(endindex, index));
		linel=linel+spacelength+wordl;
		if (linel>(widthRect-6)) 
		 {msg.drawString(" "+s.substring(startindex,endindex), Xsublines, yline);           
			linel=4+wordl; Xsublines=4;yline+=lineheight;
			startindex=endindex ; endindex=index;   }
		else {endindex=index; }
		index++;
		if ((s.charAt(index)==('.'))&&((""+s.charAt(index+1)).equals(" ")))
			{msg.drawString(s.substring(startindex,endindex), Xsublines, yline);//forced newline
			yline+=lineheight; linel=4; Xsublines=4;
			index+=2;// while ((""+s.charAt(index)).equals(" ")){index++;}
			endindex=index ; startindex=endindex ; 
			}   	   
	}    
	else
	{ index++; } 
   }
	
}

 public void drawTextbox(Component comp, Graphics gg, int xoffset, int yoffset, ImageObserver observer){
	CropImageFilter f=new CropImageFilter(0, 0,widthRect, yline+10);
    FilteredImageSource fis =new FilteredImageSource(textbox.getSource(), f);
    Image trimm =comp.createImage(fis);
	gg.drawImage(trimm, xoffset, yoffset, observer);
	}
}  

