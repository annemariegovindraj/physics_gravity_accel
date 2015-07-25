import java.awt.*;
import java.util.*;
import java.awt.image.*;

public class Graphpaper
 {  int dwidth=0,dheight=0;
    int pix,unitx ,  po, qo; 
    float previousx=0, previousy=0, divx,divy,unity; 
	Image paper; Graphics pg; 
	FontMetrics fm;
	
Graphpaper(Image p, int width, int height)
  { paper=p;
   pg=paper.getGraphics();
   dwidth= width; dheight=height;
   setScale(3);
   drawbg();
   }

 Graphpaper(Image p, int width, int height,int pixpersquare)
  {paper=p;
   pg=paper.getGraphics();
   dwidth= width; dheight=height;
   setScale(pixpersquare);
   drawbg();
   }

public void setScale(int pixpersquare)
	{pix= pixpersquare;}
   
public void drawbg()
{pg.setColor(Color.white);pg.fillRect(0,0,dwidth,dheight);
 pg.setColor(Color.orange);
  for(int i=0; i< dwidth; i+=pix)
   {pg.drawLine(i,0,i,dheight);}
  for(int j=0; j<dheight; j+=pix)
   {pg.drawLine(0,j,dwidth,j);}  
  previousx=0; previousy=0;
}
public void setAxis( int unx, float divx, String lbl_x, float uny, float divy, String lbl_y)   
{ 	po=6*pix; qo=(dheight-5*pix);
	setAxis( unx, divx, lbl_x,  uny,  divy,  lbl_y, po, qo) ;}
  
public void setAxis( int unx, float divx, String lbl_x, float uny, float divy, String lbl_y, int origin_x, int origin_y)   
	{//unitx : squares per unit
	//divx, divy : each division in units
	// divy*unity should be around 10-20
	unitx=unx; unity=uny; this.divx=divx; this.divy=divy;	
	po=origin_x; qo=origin_y;
	
	pg.setFont(new Font("Dialog", Font.PLAIN, 16)); fm=pg.getFontMetrics();
	pg.setColor(Color.black);
	int zeroslength=fm.stringWidth("0");
	
	if(po!=6*pix){setHorizontalAxis(lbl_x);}
	else
		{ //////default horizontal axis
		pg.drawLine( po, qo, (dwidth-1), qo);  
		for(int p=po, i=0; p<dwidth; p+=pix*divx*unitx, i++)
			{pg.drawLine(p, qo,p, qo-pix);   //divisions on Xaxis
			if (i%2==0)
			  {	if((divx-(int)(divx))==0) {int div=(int)(divx);
					pg.drawString(""+i*div, p-zeroslength, qo+4*pix+2); }
				else {	pg.drawString(""+i*divx, p-zeroslength, qo+4*pix+2); }
			  }
	}	}
	int lbllength=fm.stringWidth(lbl_x);
	pg.drawString(""+lbl_x, dwidth-lbllength , qo-2);  //axis labelling
		
	if (qo!=dheight-5*pix){setVerticalAxis(lbl_y);}
	else 
		{pg.drawLine(po,2, po, dheight-2);
		for(int q=qo, i=0; q>0; q-=divy*pix*unity, i++)
			{pg.drawLine(po-pix, q, po, q); 
			if ((i%2==0)&&((divy-(int)(divy))==0)) {int div=(int)(divy);
			pg.drawString(""+i*div, po-17, pix+q) ;}
	        else if(i%2==0){pg.drawString(""+i*divy, po-17, pix+q) ;}
			}	
		}	
	lbllength=fm.stringWidth(lbl_y);
	pg.drawString(""+lbl_y, po+pix, 16);  
		
	}
  public void setVerticalAxis(String lbl_y)
  {int zeroslength=fm.stringWidth("0");
	pg.setColor(Color.black);	
	pg.drawLine(po,2,po,dheight-2);
     for(int q=qo, i=0; q<dheight; q+=divy*unity*pix, i++)
        {pg.drawLine(po+3, q, po, q); 
	    if ((i%2==0)&&(q!=qo))
			{if((divy-(int)(divy))==0) {int div=(int)(divy);
			pg.drawString("-"+i*div, po-17, pix+q) ;}
	        else {pg.drawString("-"+i*divy, po-17, pix+q) ;}
		}}
	 for(int q=qo, i=0; q>0; q-=divy*unity*pix, i++)
        {pg.drawLine(po+3, q, po, q); 
	    if (i%2==0)
			{if((divy-(int)(divy))==0) {int div=(int)(divy);
			pg.drawString(""+i*div, po-17, pix+q) ;}
	        else {pg.drawString(""+i*divy, po-17, pix+q) ;}
			} 
		}
 }
 public void setHorizontalAxis(String lbl_x)
  {int zeroslength=fm.stringWidth("0");
	pg.setColor(Color.black);
  pg.drawLine( 1, qo, dwidth-1, qo); 
	for(int p=po, i=0; p<(dwidth-1);i++, p+=(int)(unitx*divx*pix))  
		{pg.drawLine(p, qo ,p, qo-pix);
		if (i%2==0)
			  {	if((divx-(int)(divx))==0) {int div=(int)(divx);
					pg.drawString(""+i*div, p-zeroslength, qo+4*pix+2); }
				else {	pg.drawString(""+i*divx, p-zeroslength, qo+4*pix+2); }
		}	 }
	for(int p=po, i=0; p>0;i++, p-=(int)(unitx*divx*pix))  
		{pg.drawLine(p,qo ,p, qo-pix);	// only if(p>0) to avoid 2 zeroes 
		if ((i%2==0)&&(p!=po))
			  {	if((divx-(int)(divx))==0) {int div=(int)(divx);
					pg.drawString("-"+i*div, p-zeroslength, qo+4*pix+2); }
				else {	pg.drawString("-"+i*divx, p-zeroslength, qo+4*pix+2); }
			  } 
		}
 }
 public Image getImage()
  {return paper;}
 
 public void addPoint (float nwx, float nwy)
{ 	pg.drawLine((int)(po+(float)previousx*unitx*pix), (int)(qo-previousy*pix*unity), (int)(po+(float)nwx*unitx*pix), (int)(qo-nwy*unity*pix)); 
	previousx=nwx; previousy=nwy;}

public void drawGraph (Component comp, Graphics gg, int xoffset, int yoffset, ImageObserver observer)
	{gg.drawImage(paper, xoffset, yoffset, observer);
	}
}

