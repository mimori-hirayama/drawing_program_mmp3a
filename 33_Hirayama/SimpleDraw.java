import java.awt.event.*;
import java.awt.Color;
import javax.swing.*;

public class SimpleDraw extends JFrame implements MouseMotionListener, ActionListener{
    int lastx=0, lasty=0, newx, newy;
    DrawPanel panel;
    JFileChooser fileChooser;
    boolean dragged=false;

    public void mouseMoved(MouseEvent arg0){
        lastx=arg0.getX();    /*ドラッグしたところのみ線が表示されるようにした */
        lasty=arg0.getY();
    }
    
    public void mouseDragged(MouseEvent arg0){   
        dragged=true;
        newx=arg0.getX();
        newy=arg0.getY();
        if(panel.rainbow) {
        	panel.hsb+=0.01f;
        }
        panel.drawLine(lastx,lasty,newx,newy);
        lastx=newx;
        lasty=newy;
    }
    
    JMenuBar menubar=new JMenuBar();    /*actionPerformed用ににコンストラクタではなくクラス内で宣言 */
    JMenu pen=new JMenu("Pen");
    JMenu eraser=new JMenu("Eraser");
    JMenu menuFile=new JMenu("File");
    JMenu penWidth=new JMenu("penWidth");
    JMenu background=new JMenu("Background");
    JColorChooser colorchooser=new JColorChooser();
    private void addMenuItem(JMenu targetMenu, String itemName, String actionName, ActionListener listener){
    	JMenuItem menuItem=new JMenuItem(itemName);   /*後で使われないのでここで宣言しても大丈夫*/
        menuItem.setActionCommand(actionName);
        menuItem.addActionListener(listener);
        targetMenu.add(menuItem);
    }


    private void init(){
        this.setTitle("Simple Draw");
        
        pen.addActionListener(this);
        pen.setActionCommand("pen");
        this.addMenuItem(pen,"monocolor","color",this);
        this.addMenuItem(pen, "rainbow", "rainbow", this);
        this.addMenuItem(penWidth,"pt1","pt1",this);
        this.addMenuItem(penWidth,"pt10","pt10",this);
        pen.add(penWidth);
        menubar.add(pen);

        
        eraser.addActionListener(this);
        eraser.setActionCommand("eraser");
        this.addMenuItem(eraser, "pt1", "pt1e",this);
        this.addMenuItem(eraser, "pt10", "pt10e", this);
        this.addMenuItem(eraser, "reset", "reset", this);
        menubar.add(eraser);
        
        this.addMenuItem(menuFile,"newF","newF",this);
        this.addMenuItem(menuFile,"saveF","saveF",this);
        menubar.add(menuFile);
        
        background.addActionListener(this);
        background.setActionCommand("background");
        this.addMenuItem(background,"background color", "backcolor", this);       
        menubar.add(background);

        fileChooser=new JFileChooser();
        this.setJMenuBar(menubar);
        this.setSize(300,200);
        this.setJMenuBar(menubar);
        this.addMouseMotionListener(this);
        panel=new DrawPanel();
        this.getContentPane().add(panel);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        

    	if(panel.bufferGraphics==null) { 
    		panel.createBuffer(7*panel.getWidth(),7*panel.getHeight());
    	}
    }

    public void actionPerformed(ActionEvent e){ /*override (ActionListener) */
        if(e.getActionCommand().equals("color")){/*カラーパレットの表示とペンの色の変更 */
        	panel.rainbow=false;
            panel.setPenColor(colorchooser.showDialog(this, "choose a color", Color.BLUE));
        } else if(e.getActionCommand().equals("rainbow")){ /*レインボーペン*/
        	panel.rainbow=true;
        }
        
        else if (e.getActionCommand().equals("pt1")){ /*ペンの太さの変更 */
            panel.setPenWidth(1.0f);

        } else if(e.getActionCommand().equals("pt10")){
            panel.setPenWidth(10.0f);
        }
        
        else if (e.getActionCommand().equals("pt1e")){ /*消しゴム（背景色のペン）*/
        	panel.rainbow=false;
        	panel.setPenColor(panel.backColor);
        	panel.setPenWidth(1.0f);
        }else if(e.getActionCommand().equals("pt10e")) {
        	panel.rainbow=false;
        	panel.setPenColor(panel.backColor);
        	panel.setPenWidth(10.0f);
        }else if(e.getActionCommand().equals("reset")) { /*描いたものを全部消す（背景色で塗りつぶす）*/
        	panel.changeBack(panel.backColor);
        }
        else if(e.getActionCommand().equals("newF")) { /*jpgファイルへの書き込み*/
        	int returnVal=fileChooser.showOpenDialog(this);
        	if(returnVal==JFileChooser.APPROVE_OPTION) {
        		panel.openFile(fileChooser.getSelectedFile());
        	}
        }else if(e.getActionCommand().equals("saveF")) { /*jpgファイルとして保存*/
        	int returnVal=fileChooser.showSaveDialog(this);
        	if(returnVal==JFileChooser.APPROVE_OPTION) {
        		panel.saveFile(fileChooser.getSelectedFile());
        	}
        }
        else if(e.getActionCommand().equals("backcolor")) { /*背景の色を変える、なお描いていた内容は消える*/
        	Color back=(colorchooser.showDialog(this, "choose a color", Color.black));
        	panel.changeBack(back);
        }
        else {
            System.out.println(e.getActionCommand());
        } 
    }

    public static void main (String[] args){
        SimpleDraw frame=new SimpleDraw();
        frame.init();
    }
}