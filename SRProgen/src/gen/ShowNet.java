package gen;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Display;

public class ShowNet {
	Project pF;
//	String filePath;
	ArrayList<Integer> pathG[];
	JobShowAssist JobShow[];
	
	public ShowNet(){
		pF = new Project();
	}
	
	public boolean showNetMain(int x,int y,String filePath){
		if(!getNetInfo(filePath)) return false;
		if(!getGroupInfo(filePath)) return false;
		calcLongPath(0);//计算所有任务到最后一个活动(尾虚活动的路径距离)
		TaskDivideIntoG();
		calcXandY(x,y);
		return true;
	}
	
	public void showN( GC gc, Display display) {
		//下面先绘制各个任务
		for(int j=0;j<this.JobShow.length;j++){
			gc.setBackground(display.getSystemColor(SWT.COLOR_BLUE));
			gc.fillOval(this.JobShow[j].x-6, this.JobShow[j].y-6,12,12);
			gc.setForeground(display.getSystemColor(SWT.COLOR_RED));
			gc.drawString(new Integer(j+1).toString(), this.JobShow[j].x+6, this.JobShow[j].y-6, true);//最后一个参数表示文字背景是否透明
		}
		//下面绘制任务间的连线
		gc.setForeground(display.getSystemColor(SWT.COLOR_BLUE));
		for(int j=0;j<this.JobShow.length;j++ ){
			Iterator iter =this.pF.jobs[j].dirSucc.iterator();
			while(iter.hasNext()){
				int nowTask = (Integer)iter.next();
//				if(this.pF.jobs[j].inGroup && this.pF.jobs[nowTask].inGroup)
//					gc.setForeground(display.getSystemColor(SWT.COLOR_GREEN));
//				else
//					gc.setForeground(display.getSystemColor(SWT.COLOR_BLUE));
				gc.drawLine(this.JobShow[j].x, this.JobShow[j].y, this.JobShow[nowTask].x,this.JobShow[nowTask].y );
			}
		}
		
		//下面绘制所有任务组
		for(int g=0;g<this.pF.taskGroup.size();g++){
			Color color = new Color(display,new Random().nextInt(256),new Random().nextInt(256),new Random().nextInt(256));//随机生成一种颜色作为此任务组的颜色
			TaskGroup group = this.pF.taskGroup.get(g);
			//绘制任务组中任务间的连线
			gc.setForeground(color);
			Iterator iter3 =group.taskSet.iterator();
			while(iter3.hasNext()){
				int TaskA = (Integer)iter3.next();
				Iterator iter4 =group.taskSet.iterator();
				while(iter4.hasNext()){
					int TaskB = (Integer)iter4.next();
					if(TaskA!=TaskB && (this.pF.jobs[TaskA].dirSucc.contains(TaskB))){
						gc.drawLine(this.JobShow[TaskA].x, this.JobShow[TaskA].y, this.JobShow[TaskB].x,this.JobShow[TaskB].y );
					}
				}				
			}
			//绘制任务组中的任务
			gc.setBackground(color);
			Iterator iter =group.taskSet.iterator();
			while(iter.hasNext()){
				int nowTask = (Integer)iter.next();
				gc.fillOval(this.JobShow[nowTask].x-5, this.JobShow[nowTask].y-5,10,10);
			}
			//绘制请求性任务
			gc.setBackground(display.getSystemColor(SWT.COLOR_RED));
			Iterator iter1 =group.requestTaskSet.iterator();
			while(iter1.hasNext()){
				int nowTask = (Integer)iter1.next();
				gc.fillOval(this.JobShow[nowTask].x-2, this.JobShow[nowTask].y-2,4,4);
			}
			//绘制移动性任务
			gc.setForeground(display.getSystemColor(SWT.COLOR_RED));
			Iterator iter2 =group.moveTaskSet.iterator();
			while(iter2.hasNext()){
				int nowTask = (Integer)iter2.next();
				gc.drawRectangle(this.JobShow[nowTask].x-3, this.JobShow[nowTask].y-3,6,6);
			}		
		}
	}



	boolean readNet(String filePath,ArrayList<Integer> list){
		String str = "";
		try {
			FileReader fr = new FileReader(filePath);

			char[] ch = new char[1024];

			while (fr.read(ch, 0, 1024) != -1) {
				str += new String(ch);
				ch = new char[1024];
			}
			str += new String(ch);
			str=findSubStr(str,"successors","TASKGROUP INFO:");
			str=str.substring(str.indexOf("1"),str.length());
			char [] v = new char[str.length()];
			str.getChars(0, str.length(), v, 0);
			int flag=0;
			int begin=0,end=0;
			for(int i=0;i<v.length;i++){
				if(v[i]>='0' && v[i]<='9' &&flag==0){
					begin=i;flag=1;
				}
				if(v[i]>='0' && v[i]<='9' &&flag==1 &&(v[i+1]<'0'||v[i+1]>'9')){
					end=i;flag=0;
					int n=0;
					for(int j=begin;j<=end;j++)
						n=n*10+v[j]-48;
					list.add(n);			
				}			
			}
			return true;
		} catch (FileNotFoundException e) {
//	        System.out.println("找不到指定文件");
	        return false;
	      } catch (Exception e) {//因为文件读取过程中,不仅有IO异常,还可能有数字格式转换的异常,所以在此就捕获所有异常(文件未找到的异常先于此被捕获)
//	        System.out.println("文件读取错误");
	        return false;
	      }
	}
	
	String findSubStr(String str,String BeginStr,String endStr){
		int begin = str.indexOf(BeginStr);
		int end = str.indexOf(endStr);
		return str.substring(begin, end);
	}
	
	/**
	 * 从结果文件中读取网络结构信息,保存于this.pF中
	 * @param filePath
	 * @return
	 */
	boolean getNetInfo(String filePath){
		ArrayList<Integer> list = new ArrayList();
		if(readNet(filePath,list)){
			int i=0,j=0;
			for(j=0;j<this.pF.jobs.length && i<list.size();j++){
				i++;
				this.pF.jobs[j].nrOfModes=list.get(i);
				i++;
				this.pF.jobs[j].nrOfSucc=list.get(i);
				i++;
				int newJobNrIndex=i+this.pF.jobs[j].nrOfSucc;
				for(;i<newJobNrIndex;i++){
					this.pF.jobs[j].dirSucc.add((list.get(i)-1));
				}
			}
			this.pF.nrOfJobs=j;//总任务数,包括虚开始和虚结束(0,1,...,j-1)
			JobShow = new JobShowAssist[this.pF.nrOfJobs];
			for(j=0;j<this.JobShow.length;j++)
				JobShow[j]=new JobShowAssist();
			return true;
		}
		return false;
	}
	
	/**
	 * 从结果文件中读取任务组信息,保存于this.pF.taskGroup中
	 * @param filePath
	 * @return
	 */
	boolean getGroupInfo(String filePath){
		ArrayList<Integer> list = new ArrayList();
		if(readGroup(filePath,list)){
			int i=0,g=0;
			int tasks=0,qTasks=0,mTasks=0;
			for(g=0;i<list.size();g++){
				TaskGroup group = new TaskGroup();
				group.groupN=g;
				i++;
				group.belongPro=list.get(i++)-1;
				tasks=list.get(i++);
				qTasks=list.get(i++);
				mTasks=list.get(i++);
				int qTasksBeginIndex = i+tasks;
				for(;i<qTasksBeginIndex;i++){
					group.taskSet.add(list.get(i)-1);
				}
				int mTaskBeginIndex = i+qTasks;
				for(;i<mTaskBeginIndex;i++){
					group.requestTaskSet.add(list.get(i)-1);
				}
				int newGroupNrIndex=i+mTasks;
				for(;i<newGroupNrIndex;i++){
					group.moveTaskSet.add(list.get(i)-1);
				}
				//下面设置任务组中的任务的inGroup属性
				Iterator iter =group.taskSet.iterator();
				while(iter.hasNext()){
					int nowTask = (Integer)iter.next();
					this.pF.jobs[nowTask].inGroup=true;
				}
				this.pF.taskGroup.add(group);
			}
			return true;
		}
		return false;
	}
	
	boolean readGroup(String filePath,ArrayList<Integer> list){
		String str = "";
		try {
			FileReader fr = new FileReader(filePath);

			char[] ch = new char[1024];

			while (fr.read(ch, 0, 1024) != -1) {
				str += new String(ch);
				ch = new char[1024];
			}
			str += new String(ch);
			str=findSubStr(str,"TASKGROUP INFO:","REQUESTS/DURATIONS");
			str=str.substring(str.indexOf("1"),str.length());
			char [] v = new char[str.length()];
			str.getChars(0, str.length(), v, 0);
			int flag=0;
			int begin=0,end=0;
			for(int i=0;i<v.length;i++){
				if(v[i]>='0' && v[i]<='9' &&flag==0){
					begin=i;flag=1;
				}
				if(v[i]>='0' && v[i]<='9' &&flag==1 &&(v[i+1]<'0'||v[i+1]>'9')){
					end=i;flag=0;
					int n=0;
					for(int j=begin;j<=end;j++)
						n=n*10+v[j]-48;
					list.add(n);			
				}			
			}
			return true;
		} catch (FileNotFoundException e) {
//	        System.out.println("找不到指定文件");
	        return false;
	      } catch (Exception e) {//因为文件读取过程中,不仅有IO异常,还可能有数字格式转换的异常,所以在此就捕获所有异常(文件未找到的异常先于此被捕获)
//	        System.out.println("文件读取错误");
	        return false;
	      }
	}
	
	
	/**
	 * 递归计算任务n到尾虚任务的路径长度,记录在数组JobShow[]中
	 * @param n
	 */
	void calcLongPath(int n){
		if(n==this.pF.nrOfJobs-1)
			this.JobShow[n].pathNr=0;//递归出口
		else{
			Iterator iter =this.pF.jobs[n].dirSucc.iterator();
			while(iter.hasNext()){
				int nowTask = (Integer)iter.next();
				calcLongPath(nowTask);
				this.JobShow[n].pathNr=this.JobShow[n].pathNr>(this.JobShow[nowTask].pathNr+1)?this.JobShow[n].pathNr:(this.JobShow[nowTask].pathNr+1);
			}
		}
	}
	
	/**
	 * 把到尾虚活动路径长度相等的任务分到一个组里
	 */
	void TaskDivideIntoG(){
		pathG =new ArrayList[this.pF.nrOfJobs];
		for(int i=0;i<this.pathG.length;i++)
			this.pathG[i]=new ArrayList<Integer>();
		for(int i=0;i<this.JobShow.length;i++){
			pathG[this.JobShow[i].pathNr].add(i);//由于是从小到大添加活动,所有在每个List里都是有序递增的(这样后面就无需排序)
		}
	}
	
	/**
	 * 计算各任务的坐标
	 */
	void calcXandY(int x,int y){
		int column=this.JobShow[0].pathNr+1;
		float curX=(float)x*(((float)(2*column-1))/(float)(2*column));
		for(int c=0;c<column;c++){
			float dY=(float)y/(float)(pathG[c].size()+1);
			float CurY=dY;
			for(int index=0;index<pathG[c].size();index++){
				this.JobShow[pathG[c].get(index)].x=(int)curX;
				this.JobShow[pathG[c].get(index)].y=(int)CurY;
				CurY=CurY+dY;
			}
			curX=curX-((float)x)/(float)column;
		}
	}

	
}

	class JobShowAssist{
		int pathNr;//记录各活动(任务)到虚尾活动的路径长度
		int x;//在画布上的x坐标
		int y;//在画布上的y坐标
		
		JobShowAssist(){
			this.pathNr=0;
			this.x=0;
			this.y=0;
		}
	}
