package gen;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.sun.xml.internal.bind.v2.schemagen.xmlschema.List;

public class TaskGroupGen {
	Project p;//用于保存网络的父对象,即项目的引用	
	Utility utility;
	Demand demand;
	
	TaskGroupGen(Project p){
		this.p = p;
	}
	
	
	/**
	 * 产生任务组
	 */
	 void genTaskGroupMain(Demand demand,Utility utility) {
		 this.demand = demand;
		 this.utility = utility;
		this.init();
		//产生每个子项目的任务组
		for(int pnr=0;pnr<p.nrOfPro;pnr++){
			genTaskGroup(pnr);
		}		
	}

	/**
	 * 产生一个子项目的任务组
	 * @param pnr
	 * @return
	 */
	boolean genTaskGroup(int pnr) {
		int trialNr;// 表示当前是第几次试验生成pnr这个子项目的所有任务组
//		boolean success = false; //表示一个子项目的任务组是否生成成功
		ArrayList<TaskGroup> TGtemp=new ArrayList<TaskGroup>();
		for(trialNr=1;trialNr<=demand.base.maxTrials;trialNr++){
			int gnr;
			//循环生成此子项目的p.nrOfTaskG个任务组
			for(gnr=0;gnr<this.p.nrOfTaskG[pnr];gnr++){
				String pnrStr,gnrStr,remark;
				pnrStr=new Integer(pnr+1).toString();
				gnrStr=new Integer(gnr+1).toString();
				remark = pnrStr+"的第"+gnrStr+"个";
				int testNr=p.pro[pnr];//设每个任务组可尝试的生成次数testNr为这个子项目包含的任务数
				for(;testNr>0;testNr--){
					TaskGroup tempTaskGroup = new TaskGroup();
					tempTaskGroup.initGroup();
					tempTaskGroup.belongPro=pnr;//设置当前生成任务组所属的子项目
					tempTaskGroup.genGroupData(this.demand.base, utility);
					testNrOfQTaskWithTypeMaxDemand(tempTaskGroup);
					if(genOneGroup(pnr,gnr,tempTaskGroup)){
						this.utility.error(!genRequsetTask(tempTaskGroup),44,demand.errFilePath,remark);//生成请求性任务
						this.utility.error(!genMoveTask(tempTaskGroup),45,demand.errFilePath,remark);//生成移动性任务)
						TGtemp.add(tempTaskGroup);
						break;
					}					
				}
				if(testNr==0){//这个任务组在尝试生成足够多次后生成失败,此时任务生成这个子项目的所有任务组的这次试验失败,无需再生成后面的几个任务组了
					this.utility.error(true,43,demand.errFilePath,remark);
					break;
				}
			}
			if(gnr==this.p.nrOfTaskG[pnr]){
				for(int index=0;index<TGtemp.size();index++){
					p.taskGroup.add(TGtemp.get(index));
				}
				return true;
			}
			else{//此时对此子项目重新生产所有的任务组
				//先清空此项目已经生成的任务组
				TGtemp.clear();
				//再清除此项目中所有任务的inGroup标记
				for(int j=this.p.SJ[pnr];j<=this.p.FJ[pnr];j++)
					this.p.jobs[j].inGroup=false;
			}
		}
		
		this.utility.error(true,42,demand.errFilePath,new Integer(pnr+1).toString());//在设定尝试次数内,没有成功生成子项目的pnr任务组.
		return false;
	}


	/**
	 * 产生一个子项目的一个任务组
	 * @param pnr 子项目编号
	 * @param gnr 任务组编号
	 * @return
	 */
	private boolean genOneGroup(int pnr,int gnr,TaskGroup tempTaskGroup) {
		int curTask; //当前选中的任务编号
		int index;// 用于随机从候选集合中选元素
		ArrayList candidateTaskSet = new ArrayList();//候选扩展的任务集合
		//计算当前生成任务组的编号
		int groups=0;
		for(int i=0;i<pnr;i++){
			groups+=this.p.nrOfTaskG[i];
		}
		tempTaskGroup.groupN=groups+gnr;
		
		//把当前项目中所有不在任何任务组中的任务加入候选集
		for(int i=p.SJ[pnr];i<=p.FJ[pnr];i++){
			if(!p.jobs[i].inGroup){
				candidateTaskSet.add(i);
			}
		}
		if(candidateTaskSet.isEmpty())
			return false; //此时此子项目中的所有活动都已经在其它任务组中了
		
		//生成此任务组的第一个任务curTask
		index = utility.random.nextInt(candidateTaskSet.size());
		curTask = (Integer)candidateTaskSet.get(index);
		candidateTaskSet.clear();//清空,因为后面的扩展方法不同了
		
		for(;tempTaskGroup.taskSet.size()<tempTaskGroup.nrOfTask;){
			tempTaskGroup.taskSet.add(curTask);//把当前任务加入任务组中
			p.jobs[curTask].inGroup = true;
					
			Iterator iter = p.jobs[curTask].dirPred.iterator(); 
			while (iter.hasNext()) {
				int tempTask = (Integer)iter.next();
				//把当前任务的直接紧前集合中的还不在某一任务组中的任务加入候选集
				//(注意:新增加的任务可能已经在候选集了,由于使用的是List数据类型,所以需要判重.
				//本来使用Set,但是不容易从Set中随机选元素),另外虚开始和虚结尾不能作为任务组中任务
				if((!p.jobs[tempTask].inGroup)&&(!candidateTaskSet.contains(tempTask))&&(tempTask!=0)&&(tempTask!=(p.nrOfJobs-1)))  
					candidateTaskSet.add(tempTask);
			}
			iter = p.jobs[curTask].dirSucc.iterator(); 
			while (iter.hasNext()) {
				int tempTask = (Integer)iter.next();
				if((!p.jobs[tempTask].inGroup)&&(!candidateTaskSet.contains(tempTask))&&(tempTask!=0)&&(tempTask!=(p.nrOfJobs-1)))  //把当前任务的直接紧后集合中的还不在某一任务组中的任务加入候选集
					candidateTaskSet.add(tempTask);
			}
			
			if(candidateTaskSet.isEmpty()){
				if(tempTaskGroup.taskSet.size()<tempTaskGroup.nrOfTask){
					//如果生成失败,要把已加入到任务组中的任务都重新标记为不在任务组中,并清空任务组
					iter = tempTaskGroup.taskSet.iterator();
					while (iter.hasNext()){
						p.jobs[(Integer)iter.next()].inGroup = false;
					}
					tempTaskGroup.taskSet.clear();
					return false; //没有达到规定的任务数,却没有可扩展的任务了
				}
				else{
					//下面注释的两句移动此函数外部了
//					genRequsetTask(tempTaskGroup);//生成请求性任务
//					p.taskGroup.add(tempTaskGroup);
					return true;//此时虽然不能再扩展了,但是由于加上了一个新的curTask,任务组的数量正好达到要求,所有生成成功
				}				
			}		
			//下面从候选集合中随机选择一个任务,作为新的当前任务
			else
			{
				index = utility.random.nextInt(candidateTaskSet.size());
				curTask = (Integer)candidateTaskSet.get(index);
				candidateTaskSet.remove(index);//把新选中作为当前任务的任务从候选集移除			
			}			
		}		
		return true;
	}
	
	/**
	 * 由于后面生成对空间资源需求的时候,要对资源需求划分到每个请求性任务上,
	 * 所以要保证请求性任务的数量必须不大于对某类型资源的最大需求.
	 * 而一旦请求性任务生成完毕,也无法修改最大需求.所以要在生成前就满足此条件
	 * @param taskGroup
	 */
	void testNrOfQTaskWithTypeMaxDemand(TaskGroup taskGroup){
		for(int i=0;i<this.p.nrOfSRType;i++){
			int max=this.p.typeSR[i].maxDemand.x;
			if(this.p.typeSR[i].dimension==2){
				max=(max>this.p.typeSR[i].maxDemand.y)?max:this.p.typeSR[i].maxDemand.y;
			}
			if(this.p.typeSR[i].dimension==3){
				max=(max>this.p.typeSR[i].maxDemand.y)?max:this.p.typeSR[i].maxDemand.y;
				max=(max>this.p.typeSR[i].maxDemand.z)?max:this.p.typeSR[i].maxDemand.z;
			}
			taskGroup.nrOfQTask=(taskGroup.nrOfQTask<max)?taskGroup.nrOfQTask:max;
		}
		this.utility.error(taskGroup.nrOfQTask<this.demand.base.minNofQJob, 41,demand.errFilePath,"");
	}

	boolean genRequsetTask(TaskGroup taskGroup){
		if(0==taskGroup.nrOfQTask)
			return true;		
		int index,curTask;
		ArrayList candidateRequsetTaskSet = new ArrayList();//候选扩展的请求性任务集合
		//下面寻找此任务组中没有紧前在此任务组中任务,加入到candidateRequsetTaskSet中(因为要保证请求任务必需至少有一个是这样的)
		Iterator iter = taskGroup.taskSet.iterator(); 
		while (iter.hasNext()){
			int tempTask = (Integer)iter.next();
			if(!this.utility.hasIntersection(this.p.jobs[tempTask].dirPred, taskGroup.taskSet))
				candidateRequsetTaskSet.add(tempTask);
		}
		if(candidateRequsetTaskSet.isEmpty())
			return false; //这种情况应该是不可能发生的
		//下面添加第一个请求性任务
		index = utility.random.nextInt(candidateRequsetTaskSet.size());
		curTask = (Integer)candidateRequsetTaskSet.get(index);
		taskGroup.requestTaskSet.add(curTask);//先保证生成一个无紧前在任务组中的请求性任务
		candidateRequsetTaskSet.clear();
		
		//下面添加其它请求性任务
		iter = taskGroup.taskSet.iterator(); 
		while (iter.hasNext()){
			candidateRequsetTaskSet.add((Integer)iter.next());
		}
		candidateRequsetTaskSet.remove(new Integer(curTask));//候选集中移除刚添加的第一个任务
	     
		//然后在任务组的其它任务中随机选择任务作为请求性任务，直到请求性任务数量达到指定的值。
		for(int i=1;i<taskGroup.nrOfQTask;i++){
			index = utility.random.nextInt(candidateRequsetTaskSet.size());
			curTask = (Integer)candidateRequsetTaskSet.get(index);
			taskGroup.requestTaskSet.add(curTask);
			candidateRequsetTaskSet.remove(index);			
		}
		return true;
	}
	


    /**
     * 由于移动性任务没有规定结构约束，故直接在任务组中随机选择指定数目的任务作为移动性任务即可
     * @param taskGroup
     * @return
     */
	boolean genMoveTask(TaskGroup taskGroup){
		int index,curTask;
		ArrayList candidateMoveTaskSet = new ArrayList();//候选扩展的移动性性任务集合
		Iterator iter = taskGroup.taskSet.iterator(); 
		iter = taskGroup.taskSet.iterator(); 
		while (iter.hasNext()){
			candidateMoveTaskSet.add((Integer)iter.next());
		}
		for(int i=0;i<taskGroup.nrOfMTask;i++){
			index = utility.random.nextInt(candidateMoveTaskSet.size());
			curTask = (Integer)candidateMoveTaskSet.get(index);
			taskGroup.moveTaskSet.add(curTask);
			candidateMoveTaskSet.remove(index);			
		}
		return true;
	}

	/**
	 * 初始化
	 */
	void init(){
//		for(int i=0;i<p.nrOfTaskG;i++){
//			p.taskGroup[i].initGroup();
//		}
		this.p.taskGroup.clear();
		//把每个活动的是否在任务组中的标记都置为flase
		for(int i=0;i<p.nrOfJobs;i++){
			p.jobs[i].inGroup = false;
		}
	}
		
}
