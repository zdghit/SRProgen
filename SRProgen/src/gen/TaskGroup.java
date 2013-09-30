package gen;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class TaskGroup {
	int nrOfTask;//任务组含有的活动数
	int nrOfQTask;//任务组含有的请求性活动数
	int nrOfMTask;//任务组含有的移动性任务数
	SRmeasure[] requestSR;//对各空间资源需求量
	Set taskSet;//任务组包含的活动编号集合
	Set requestTaskSet;//请求性任务集
	Set moveTaskSet;//移动性任务集
	int belongPro;//所属的子项目编号
	int groupN; //此任务组编号
	int EST;//此任务组的最早开始时间
	int EFT;//此任务组的最早结束时间
	
//	Polygon shape;
	
	TaskGroup(){
		this.requestSR = new SRmeasure[Progen.maxNrOfSR];
		for(int i=0;i<requestSR.length;i++)
			this.requestSR[i] = new SRmeasure();
		this.taskSet = new HashSet();
		this.requestTaskSet = new HashSet();
		this.moveTaskSet = new HashSet();
	}
	
	void genGroupData(BaseStruct base,Utility utility){
		this.nrOfTask=base.minNofJobTask+utility.random.nextInt(base.maxNofJobTask-base.minNofJobTask+1);
		this.nrOfQTask=base.minNofQJob+utility.random.nextInt(base.maxNofQJob-base.minNofQJob+1);
		this.nrOfMTask=base.minNofMJob+utility.random.nextInt(base.maxNofMJob-base.minNofMJob+1);
		//下面任务数量的合理性(如果不合理,进行修正)[出现不合理是因为随机,如任务数可取[4,8],请求性任务数可取[2,5],而一点任务数随机为4,请求性任务随机为5,就出现了不合理]
		if(this.nrOfQTask>this.nrOfTask)
			this.nrOfQTask=this.nrOfTask;
		if(this.nrOfMTask>this.nrOfTask)
			this.nrOfMTask=this.nrOfTask;
	}
	
	void initGroup(){
		this.nrOfTask=0;
		this.nrOfQTask=0;
		this.nrOfMTask=0;
		for(int i=0;i<this.requestSR.length;i++){
			this.requestSR[i].x=0;
			this.requestSR[i].y=0;
			this.requestSR[i].z=0;
		}
		this.taskSet.clear();
		this.requestTaskSet.clear();
		this.moveTaskSet.clear();
		this.belongPro=-1;
		this.groupN=-1;
	}
	
	void calcESTAndEFT(Project p){
		this.EST=Progen.maxHorizon;//初始化为最大值
		this.EFT=0;//初始化为最小值
		Iterator iter = this.taskSet.iterator(); 
		while(iter.hasNext()){
			int taskNr=(Integer)iter.next();
			this.EST=(this.EST<(p.jobs[taskNr].T.EST))?this.EST:(p.jobs[taskNr].T.EST);
			this.EFT=(this.EFT>(p.jobs[taskNr].T.EFT))?this.EFT:(p.jobs[taskNr].T.EFT);	
		}	
	}
	
}
