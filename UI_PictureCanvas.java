package cn.itcast.picture.ui;

import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
/*
 * 拼图类
 */
public class PictureCanvas extends JPanel implements MouseListener{
	//静态变量
	public static int pictureID = 1; //图片ID
	public static int stepNum = 0; //步数
	
	//成员变量
	private Cell[] cell;//小方格
	private boolean hasAddActionListener =  false; 
	//用来表示是否为方格添加了点击监听，添加了值为true,未添加值为false
	private Rectangle nullCell;//指定一个空的小方格
	
	//构造方法
	public PictureCanvas(){
		//设置拼图区的布局
		this.setLayout(null);//帧布局
		//创建12个图片小方格，添加到拼图区
		cell = new Cell[12];
		for (int i = 0; i < 4; i++) {//代表的是行数
			for (int j = 0; j < 3; j++) {//代表的是列数
				//加载图片
				//ImageIcon icon = new ImageIcon("picture\\1_1.gif");
				//i=0,1
				//j=0,1,2
				//0*3+0+1 = 1
				//0*3+1+1 = 2
				//0*3+2+1 = 3
				//1*3+0+1 = 4
				//1*3+1+1 = 5
				ImageIcon icon = new ImageIcon("picture\\1_"+(i*3+j+1)+".gif");
				//创建图片小方格
				cell[i*3+j] = new Cell(icon);
				//指定图片小方格显示的位置
				cell[i*3+j].setLocation(j*150+20, i*150+20);
				//把图片小方格添加到拼图区显示
				this.add(cell[i*3+j]);
			}
		}
		
		//删除第12个图片小方格
		this.remove(cell[11]);
		
		//指定一个空的小方格
		nullCell = new Rectangle(300+20, 450+20, 150, 150);
	}
	
	//重新加载图片，并添加数字提示
	public void reLoadPictureAddNumber(){
		//获取到每一个图片小方格
		for (int i = 0; i < 4; i++) {//行数
			for (int j = 0; j < 3; j++) {//列数
				//获取小方格  cell[i*3+j]
				//设置小方格显示的图片
				ImageIcon icon = new ImageIcon("picture\\"+pictureID+"_"+(i*3+j+1)+".gif");
				cell[i*3+j].setIcon(icon);
				//设置小方格显示的数字提示
				cell[i*3+j].setText("" + (i*3+j+1));
				//设置小方格数字显示的位置
				cell[i*3+j].setVerticalTextPosition(this.getY()/2);//设置文字垂直显示位置[居中显示]
				cell[i*3+j].setHorizontalTextPosition(this.getX()/2);//设置文字水平显示位置[居中显示]
			}
		}
		
	}
	
	//重新加载图片，并去除数字提示
	public void reLoadPictureClearNumber(){
		//获取到每一个图片小方格
		for (int i = 0; i < 4; i++) {//行数
			for (int j = 0; j < 3; j++) {//列数
				//获取小方格  cell[i*3+j]
				//设置小方格显示的图片
				ImageIcon icon = new ImageIcon("picture\\"+pictureID+"_"+(i*3+j+1)+".gif");
				cell[i*3+j].setIcon(icon);
				//设置小方格显示的数字提示
				cell[i*3+j].setText("");
			}
		}
	}
	
	//对小方格进行重新位置排序，打乱顺序
	public void start(){
		//如果没有给小方格添加鼠标点击监听的话，则添加监听
		if ( !hasAddActionListener) {
			//添加监听
			for (int i = 0; i < 11; i++) {
				cell[i].addMouseListener(this);
			}
			//更新鼠标点击监听的状态
			hasAddActionListener = true;
		}
		
		//判断当前第一个小方格距离左上交的较近时，进行方格与空方格互换
		//如果第一个小方格在左上角的四个方格位置内，就不断的循环，进行方法与空方格位置的互换
		while(cell[0].getBounds().x <=170 && cell[0].getBounds().y <=170 ){
			//获取到空方格的位置
			int nullX = nullCell.getBounds().x;
			int nullY = nullCell.getBounds().y;
			
			//随机产生一个方向，进行空方格与小方法的互换
			//产生0-3之间的随机数，对应空方格的上下左右移动
			int direction = (int)(Math.random()*4);//0,1,2,3
			switch (direction) {
				case 0://空方格向左移动，与左边的方格进行互换位置，左侧方格要向右移动
					//nullX = nullX - 150;
					nullX -= 150;
					cellMove(nullX, nullY, "RIGHT");
					break;
				case 1://空方格向右移动，与右边的方格进行互换位置，右侧方格要向左移动
					nullX += 150;
					cellMove(nullX, nullY, "LEFT");
					break;
				case 2://空方格向上移动，与上边的方格进行互换位置，上侧方格要向下移动
					nullY -= 150;
					cellMove(nullX, nullY, "DOWN");
					break;
				case 3://空方格向下移动，与下边的方格进行互换位置，下侧方格要向上移动
					nullY += 150;
					cellMove(nullX, nullY, "UP");
					break;
			}
		}
		
	}

	/**
	 * 方格与空方格位置互换
	 * @param nullX 空方格的x轴坐标
	 * @param nullY 空方格的y轴坐标
	 * @param direction 方格要移动的方向
	 */
	private void cellMove(int nullX, int nullY, String direction) {
		for (int i = 0; i < 11; i++) {
			//获取到与空方格位置相同的小方格
			if (cell[i].getBounds().x == nullX && cell[i].getBounds().y == nullY) {
				//当前方格的移动
				cell[i].move(direction);
				//空方格的移动
				nullCell.setLocation(nullX, nullY);
				//交换位置后，结束循环
				break;
			}
		}
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {//鼠标按下去
		//获取到当前所点击的小方格
		Cell button = (Cell)e.getSource();
		//获取所点击方格的x，y坐标
		int clickX = button.getBounds().x;
		int clickY = button.getBounds().y;
				
		//获取当前空方格的x，y坐标
		int nullX = nullCell.getBounds().x;
		int nullY = nullCell.getBounds().y;
		
		//进行比较，如果满足条件进行位置的交换
		if (clickX == nullX && clickY - nullY == 150) {//点击的为空方格下面的方格
			button.move("UP");//所点击的方格 向上移动
		} else if (clickX == nullX && clickY - nullY == -150) {//点击的为空方格上面的方格
			button.move("DOWN");//所点击的方格 向下移动
		} else if(clickX - nullX == 150 && clickY == nullY){//点击的为空方格右面的方格
			button.move("LEFT");//所点击的方格 向左移动
		} else if(clickX - nullX == -150 && clickY == nullY){//点击的为空方格左面的方格
			button.move("RIGHT");//所点击的方格 向右移动
		} else {
			return ;// 不满足移动条件，就不进行任何的处理
		}
		//更新空方格的位置
		nullCell.setLocation(clickX, clickY);
		//拼图区界面重新绘制
		this.repaint();
		//更新步数，将游戏状态区的步数更新
		stepNum++;
		PictureMainFrame.step.setText("步数："+stepNum);
		
		//判断当前游戏是否完成，若完成，给玩家一个友好提示
		if (this.isFinish()) {
			//弹出窗口提示
			JOptionPane.showMessageDialog(this, "恭喜你完成拼图，加油！ \n 所用步:"+stepNum);
			//撤销每一个小方格上的鼠标点击监听, 让鼠标点击小方格步再起作用了
			for (int i = 0; i < 11; i++) {
				cell[i].removeMouseListener(this);
			}
			//更新方格的动作监听器的状态
			hasAddActionListener = false;
		}
		
	}
	
	//判断当前游戏是否完成, 根据坐标判断是否拼图成功
	private boolean isFinish(){
		for (int i = 0; i < 11; i++) {
			//获取每一个方格的位置
			int x = cell[i].getBounds().x;
			int y = cell[i].getBounds().y;
			if ( ((y-20)/150*3 + (x-20)/150) != i ) {
				//i=0,  (20-20)/150*3 + (20-20)/150) = 0
				//i=1,  (20-20)/150*3 + (170-20)/150) = 1
				//....
				//i=5,  (170-20)/150*3 + (320-20)/150)= 5 
				return false;
			}
		}
		
		return true;
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}
	
	
	
	
	
	
	
	
	
	
}
