import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import java.io.File;
import java.awt.Color;

public class DrawPanel extends JPanel{
    Color currentColor=Color.black;
    Color backColor=Color.white;
    Boolean rainbow=false;
    Float currentWidth=1.0f;
    Float hsb=0.0f;
    /*バッファ用*/
    BufferedImage bufferImage=null;
    Graphics2D bufferGraphics=null;
    
    public void createBuffer(int bWidth, int bHeight) {
    	bufferImage=new BufferedImage(bWidth,bHeight,BufferedImage.TYPE_INT_BGR);
    	bufferGraphics=bufferImage.createGraphics();
    	bufferGraphics.setBackground(this.backColor);
    	bufferGraphics.clearRect(0,0,bWidth,bHeight);
    }

    public void drawLine(int x1, int y1, int x2, int y2){
    	if(this.rainbow) {
    		this.currentColor=Color.getHSBColor(hsb,1.0f,1.0f);
    	}
        bufferGraphics.setColor(this.currentColor);
        bufferGraphics.setStroke(new BasicStroke(this.currentWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER));
        bufferGraphics.drawLine(x1,y1,x2,y2);
        repaint();
    }

    public void setPenColor(Color newColor){
        currentColor=newColor;
    }

    public void setPenWidth(Float newWidth){
        currentWidth=newWidth;
    }
    
    public void openFile(File file2open) {
    	BufferedImage pictureImage;
    	try {
    		pictureImage=ImageIO.read(file2open);
    	} catch (Exception e) {
    		System.out.println("Error: reading file="+file2open.getName());
    		return;
    	}
    	this.createBuffer(pictureImage.getWidth(),pictureImage.getHeight());
    	bufferGraphics.drawImage(pictureImage, 0, 0, this);
    	repaint();
    }
    
    
    public void changeBack(Color color) {
    	this.backColor=color;
    	bufferGraphics.setBackground(color);
    	bufferGraphics.clearRect(0,0,7*this.getHeight(),7*this.getWidth());
    	repaint();
    }
    
    public void saveFile(File file2save) {  /*指定したファイル内にお絵描きした内容をjpgファイルとして出力*/
    	try {
    		ImageIO.write(bufferImage, "jpg", file2save);
    		} catch (Exception e) {
    			System.out.println("Error: writing file="+file2save.getName());
    			return;
    		}
    }
    
  
    
    public void paintComponent(Graphics g) {
    	super.paintComponent(g);
    	if(bufferImage!=null) {
    		g.drawImage(bufferImage, 0, 0, this);
    	}
    }
   
}