package gen;

import java.util.*;

public class Project {
	int nrOfPro; //子项目的个数
	int nrOfJobs = 0; //所有的工作数(包含虚开始和虚结尾活动),注意开始的虚活动号为0,尾虚活动号位nrOfJobs-1.
	int[] pro;//数组元素个数为子项目的个数,每个元素值表示对应子项目的非虚工作数
	int[] CPMT; //数组元素个数为子项目的个数
	int[] dueData;
	int[] relDate;//数组元素个数为子项目的个数,每个元素表示对应子项目开始的延期时间
	int[] tardCost;//
	Job[] jobs;//定义出所有活动的数组,元素个数为工作数,每一元素为一活动
	int R;//可更新资源种类数
	int D;//双重资源种类数
	int N;//不可更新资源种类
	int horizon;//
//	Time[] T;//数组个数为活动最大数目数,每个元素记录了对应编号的活动的EST/EFT和此时的Mode
	
	int[] SJ;//SJ[k]表示子项目k的开始活动编号
	int[] FJ;//FJ[k]表示子项目k的最后一个活动的编号
	int[] NSJ;//NSJ[k]表示子项目k虚开始活动的直接紧后活动的数目
	int[] NFJ;//NFJ[k]表示子项目k尾虚活动的直接紧前活动的数目
	
	int[] RPer;//可更新资源可用量
	int[] Dper;//双重资源中每阶段的具有可更新资源性质的资源可用量
	int[] DTot;//
	int[] NTot;//不可更新资源可用量
	
	Network net;
	Reqgen reqGen;
	AvailGen availGen;
	
	//===============空间资源相关======================
	TaskGroupGen taskGroupGen;
	ArrayList<TaskGroup> taskGroup;
	int[] nrOfTaskG;//各子项目任务组数量
	SRreqgen srReqgen;//生成空间资源需求
	SRAvailAndOGen srAvailAndOGen;//生成具体空间资源可用量及方向
	
//	int[] SRNr;//记录各类型空间资源的种类数(如SRNr[3]=4,表示第三类空间资源有4种)
	SRType[] typeSR;//记录各类型空间资源
	concreteSR[] SR;//具体的空间资源数组,每个元素表示一种具体的空间资源(记录其可用量及方向)
	int nrOfSRType;//空间资源类型数
	
	Project(){	
		this.pro = new int[Progen.maxNrOfPro];
		this.CPMT = new int[Progen.maxNrOfPro];
		this.dueData = new int[Progen.maxNrOfPro];
		this.relDate = new int[Progen.maxNrOfPro];
		this.tardCost = new int[Progen.maxNrOfPro];
		this.jobs = new Job[Progen.maxNrOfJobs];
		for(int i=0;i<this.jobs.length;i++)
			this.jobs[i] = new Job();
//		this.T = new Time[Progen.maxNrOfJobs];
//		for(int i=0;i<this.T.length;i++)
//			this.T[i] = new Time();
		this.SJ = new int[Progen.maxNrOfPro];
		this.FJ = new int[Progen.maxNrOfPro];
		this.NSJ = new int[Progen.maxNrOfPro];
		this.NFJ = new int[Progen.maxNrOfPro];
		
		this.RPer = new int[Progen.maxR];
		this.Dper = new int[Progen.maxD];
		this.DTot = new int[Progen.maxD];
		this.NTot = new int[Progen.maxN];
		
		this.net = new Network(this);
		this.reqGen = new Reqgen(this);
		this.availGen = new AvailGen(this);
		
		//===============空间资源相关======================
		taskGroupGen = new TaskGroupGen(this);
//		this.taskGroup = new TaskGroup[Progen.maxTaskGroup];
//		for(int i=0;i<this.taskGroup.length;i++)
//			this.taskGroup[i] = new TaskGroup();
		taskGroup = new ArrayList();
		this.nrOfTaskG = new int[Progen.maxNrOfPro];
		
		this.typeSR = new SRType[Progen.maxNrOfSRType];
		for(int i=0;i<this.typeSR.length;i++)
			this.typeSR[i] = new SRType();
		this.SR = new concreteSR[Progen.maxNrOfSR];
		for(int i=0;i<this.SR.length;i++)
			this.SR[i] = new concreteSR();
		this.srReqgen = new SRreqgen(this);
		this.srAvailAndOGen = new SRAvailAndOGen(this);
	}
	
	
	void genProjData(BaseStruct base,Utility utility){
//		this.proInit();//由于多个算例文件的生成使用的是同一Project对象,所有要对对象的内容进行初始化(如清理等)
		
		this.nrOfPro = base.nrOfPro;//子项目的个数
		this.nrOfJobs = 0;//基文件中只规定了活动最小和最大数目,故生成每个实例总活动数目可不同
		int maxRelDate = 0;//记录跨时最长的那个子项目的持续时间
		
		//=======================空间资源相关================
		if(Progen.includeSR){//包含空间资源约束
			//空间资源类型数
			this.nrOfSRType = base.nrOfSR;
			for(int i=0,j=0;i<this.nrOfSRType;i++){
				//空间资源具体类型
				this.typeSR[i].copy(base.SRTypeAll[i]);
				this.typeSR[i].kind = this.typeSR[i].minKind + utility.random.nextInt(this.typeSR[i].maxKind-this.typeSR[i].minKind+1);
				this.typeSR[i].startNr=j;
				this.typeSR[i].endNr=j+this.typeSR[i].kind-1;
				j=j+this.typeSR[i].kind;
			}
		}
		
		//子项目个数
		for(int i=0;i<this.nrOfPro;i++){ 
			this.pro[i] = base.minJob+utility.random.nextInt(base.maxJob-base.minJob+1);
			this.SJ[i] = this.nrOfJobs+1; //计算出子项目i的开始活动编号
			this.nrOfJobs = this.nrOfJobs+this.pro[i];
			this.FJ[i]= this.nrOfJobs; //计算出子项目i的结束活动编号
			this.relDate[i]=utility.random.nextInt(base.maxRel+1);
			maxRelDate = maxRelDate>relDate[i]?maxRelDate : relDate[i];
			tardCost[i] = (int)(utility.random.nextDouble()*this.pro[i]);//Java中强制转换为int就是丢掉小数位
			
			if(Progen.includeSR){
				//=======================空间资源(任务组)相关================
				this.nrOfTaskG[i] = base.minNofTaskG+utility.random.nextInt(base.maxNofTaskG-base.minNofTaskG+1);
			}
		}
		
		this.nrOfJobs = this.nrOfJobs+2;//产生活动数目,包括首尾虚项目所以加2
		this.horizon = 0;
		
		//产生三类资源的各自数目
		this.R = base.minRen+utility.random.nextInt(base.maxRen-base.minRen+1);
		this.N = base.minNon+utility.random.nextInt(base.maxNon-base.minNon+1);
		this.D = base.minDou+utility.random.nextInt(base.maxDou-base.minDou+1);
		
		this.genModes(base,utility);
		//计算this.horizon
		for(int i=0;i<this.nrOfJobs;i++)
			this.horizon = this.horizon + this.jobs[i].modes[this.jobs[i].nrOfModes-1].duration;

		/**
		 * 计算出时间跨度[即各活动都选取执行时间最长的那个模式串行执行,最后完工的时间,
		 * 再加上MaxRelDate(为什么要加上?)],项目完工时间不可能比此更晚
		 */
		this.horizon = this.horizon + maxRelDate;                       		
	}
	
	
	/**
	 * 产生项目中各活动的活动模式
	 */
	void genModes(BaseStruct base,Utility utility){
		//先初始化各活动的活动模式数为0,模式时间为0(实际上在对象的构造函数中已初始化为0,但此处仍不可省略,因为对象构造一次,而算例要生成多次)
		for(int i=0;i<this.nrOfJobs;i++){
			this.jobs[i].nrOfModes = 0;
			for(int j=0;j<Progen.maxNrOfModes;j++)
				this.jobs[i].modes[j].duration = 0;
		}
		//下面设置活动编号为0和编号为(nrOfJobs-1)的首尾活动只有一个工作模式,模式持续时间为0,对各资源需求为0
		this.jobs[0].dumMyJob(this.R,this.N, this.D);
		this.jobs[nrOfJobs-1].dumMyJob(this.R,this.N, this.D);
		//给其他所有的活动生成模式数和模式的持续时间
		for(int i=1;i<this.nrOfJobs-1;i++){
			this.jobs[i].nrOfModes = base.minMode+utility.random.nextInt(base.maxMode-base.minMode+1);
			for(int j=0;j<this.jobs[i].nrOfModes;j++){
				this.jobs[i].modes[j].duration = base.minDur+utility.random.nextInt(base.maxDur-base.minDur+1);
			}
		}
		//对各活动(除首尾)的模式按时间增序排列(插入排序),使得模式0时间最短,模式nrOfModes-1时间最长
		for(int i=1;i<this.nrOfJobs-1;i++){
			for(int j=1,temp,k;j<this.jobs[i].nrOfModes;j++){
				temp = this.jobs[i].modes[j].duration;
				for( k=j-1;k>=0&&this.jobs[i].modes[k].duration>temp;k--)
					this.jobs[i].modes[k+1].duration = this.jobs[i].modes[k].duration;
				this.jobs[i].modes[k+1].duration = temp;
			}
		}		
	}
	
/**
 * 计算每个任务的EST(最早开始时间)、EFT(最早结束时间)【(当type=='d'时)及LST(最晚开始时间)与计算CPMT时间】
 * @param type 资源类型
 * @param resnr 某资源的编号
 */
	 void calcCPMTimes(char type,int resnr) {
		// TODO Auto-generated method stub
		for(int pnr=0;pnr<this.nrOfPro;pnr++){
			for(int j=this.SJ[pnr];j<=this.FJ[pnr];j++){
				this.jobs[j].T.EST = this.relDate[pnr];
				//每个任务的最早开始时间要大于等于其直接紧前任务的最早开始时间
				for(int i=this.SJ[pnr];i<j;i++){
					if(this.jobs[j].dirPred.contains(i))
						this.jobs[j].T.EST = (this.jobs[j].T.EST>this.jobs[i].T.EFT)?this.jobs[j].T.EST:this.jobs[i].T.EFT;
				}
				this.jobs[j].selectMode(type,resnr);
				this.jobs[j].T.EFT = this.jobs[j].T.EST+this.jobs[j].modes[this.jobs[j].T.mode].duration;
			}
			
			if(type=='d'){ //这时还要计算出每个子项目的CPM(关键路径)时间及每个任务的LST(最晚开始时间)
				this.CPMT[pnr]=0;
				
				//关键路径时间为在尾虚工作的紧前工作中最晚结束的那个的结束时间
				for(int j=this.FJ[pnr]-this.NFJ[pnr]+1;j<=this.FJ[pnr];j++){
					this.CPMT[pnr] = (this.CPMT[pnr]>this.jobs[j].T.EFT)?this.CPMT[pnr]:this.jobs[j].T.EFT;
				} 
				
				//下面计算每个任务的LST(最晚开始时间)
				for(int j=this.FJ[pnr];j>=this.SJ[pnr];j--){
					this.jobs[j].T.LST = this.CPMT[pnr]-this.jobs[j].modes[this.jobs[j].T.mode].duration;
					//每个任务的最晚开始时间要小于等于其直接紧后任务的最早开始时间
					for(int i=this.FJ[pnr];i>j;i--){
						if(this.jobs[j].dirSucc.contains(i))
							this.jobs[j].T.LST = (this.jobs[j].T.LST<(this.jobs[i].T.LST-this.jobs[j].modes[this.jobs[j].T.mode].duration))?this.jobs[j].T.LST:(this.jobs[i].T.LST-this.jobs[j].modes[this.jobs[j].T.mode].duration);
					}
				}
				
			}
		}
	}
/**
 * 计算每个子项目的dueData
 * @param base
 */
	 void calcDueDates(BaseStruct base) {
	// TODO Auto-generated method stub
	for(int pnr=0;pnr<this.nrOfPro;pnr++){
		this.dueData[pnr] = (int)(this.CPMT[pnr]+base.dueDateFac*(this.horizon-this.CPMT[pnr]));
	}
}
/**
 * 把当前生成的算例(project对象当前的状态表征)写入到文件
 * @param demand  因为结果文件要显示依据的基文件/随机数种子,故需此参数
 * @param utility	
 * @param filepath 结果文件路径(含文件名/后缀)
 */
	public void writeToFile(Demand demand, Utility utility,String filepath) {
		// TODO Auto-generated method stub
		utility.setPrintStream(filepath,true);
		Formatter f = new Formatter(System.out);
	    System.out.println("************************************************************************");
	    System.out.println("file with basedata            : "+demand.bFileName+demand.extStr);
	    System.out.println("initial value random generator: "+demand.inval);
	    System.out.println("************************************************************************");
	    System.out.println("projects                      :  "+this.nrOfPro);
	    System.out.println("jobs (incl. supersource/sink ):  "+this.nrOfJobs);
	    System.out.println("horizon                       :  "+this.horizon);
	    System.out.println("RESOURCES");
	    System.out.println("  - renewable                 :  "+this.R+"   R");
	    System.out.println("  - nonrenewable              :  "+this.N+"   N");
	    System.out.println("  - doubly constrained        :  "+this.D+"   D");
	    
	    if(Progen.includeSR){
	    	//输出空间资源类型
		    System.out.println("  - SPATIAL  RESOURCES ");
		    for(int i=0;i<this.nrOfSRType;i++){
		    	String div,ori,blank;
		    	if(this.typeSR[i].dividable)
		    		div=" d";
		    	else
		    		div="nd";
		    	if(this.typeSR[i].orientation) 
		    		ori=" o";
		    	else
		    		ori="no";
		    	System.out.println("  	- "+this.typeSR[i].name+"("+this.typeSR[i].dimension+","+div+","+ori+")            :  "+this.typeSR[i].kind+"   "+this.typeSR[i].name);
		    }
	    }
	    
	    if(Progen.includeSR){
		    //输出项目信息
		    System.out.println("************************************************************************");
		    System.out.println("PROJECT INFORMATION:");
		    System.out.println("pronr.  #jobs  #TaskGNr  rel.date duedate tardcost  MPM-Time");
		  //因为原来的Progen中项目编号、活动编号、模式编号、资源编号都从1开始，所以输出时调整一下
		    for(int i=0;i<this.nrOfPro;i++){
		    	f.format("  %3d    %3d    %3d       %3d      %3d      %3d      %3d",i+1,this.pro[i],this.nrOfTaskG[i],this.relDate[i],this.dueData[i],this.tardCost[i],this.CPMT[i]);
		    	System.out.println();
		    }
	    }
	    else{
		    System.out.println("************************************************************************");
		    System.out.println("PROJECT INFORMATION:");
		    System.out.println("pronr.  #jobs rel.date duedate tardcost  MPM-Time");
		  //因为原来的Progen中项目编号、活动编号、模式编号、资源编号都从1开始，所以输出时调整一下
		    for(int i=0;i<this.nrOfPro;i++){
		    	f.format("  %3d    %3d    %3d      %3d      %3d      %3d",i+1,this.pro[i],this.relDate[i],this.dueData[i],this.tardCost[i],this.CPMT[i]);
		    	System.out.println();
		    }
	    }
	    
	    //输出项目网络信息
	    System.out.println("************************************************************************");
	    System.out.println("PRECEDENCE RELATIONS:");
	    System.out.println("jobnr.    #modes  #successors   successors");
	    for(int i=0;i<this.nrOfJobs;i++){
	    	f.format(" %3d      %3d        %3d        ",i+1,this.jobs[i].nrOfModes,this.jobs[i].nrOfSucc);
	    	TreeSet ts = new TreeSet(this.jobs[i].dirSucc);//构造一个TreeSet,TreeSet可以排序
	        ts.comparator();
	    	Iterator iter = ts.iterator(); 
			while (iter.hasNext())
				f.format(" %3d", (Integer)iter.next()+1);
			System.out.println();
	    }
	    
	    if(Progen.includeSR){
	    	//下面输出任务组相关信息
		    System.out.println("************************************************************************");
		    System.out.println("TASKGROUP INFO:");
		    System.out.println("taskGroupNr #belongPro  #tasks  #QTasks  #MTasks  tasks              requestTasks           moveTasks");
		    for(int g=0;g<this.taskGroup.size();g++){
		    	TaskGroup group = (TaskGroup)this.taskGroup.get(g);
		    	f.format(" %3d           %3d      %3d      %3d     %3d     ",g+1,group.belongPro+1,group.taskSet.size(),group.requestTaskSet.size(),group.moveTaskSet.size());
		    	TreeSet ts = new TreeSet(group.taskSet);//构造一个TreeSet,TreeSet可以排序
		    	ts.comparator();
		    	Iterator iter = ts.iterator(); 
				while (iter.hasNext())
					f.format(" %3d", (Integer)iter.next()+1);
				System.out.print("       ");
				ts = new TreeSet(group.requestTaskSet);
				ts.comparator();
		    	iter = ts.iterator(); 
				while (iter.hasNext())
					f.format(" %3d", (Integer)iter.next()+1);
				System.out.print("				");
				ts = new TreeSet(group.moveTaskSet);
				ts.comparator();
		    	iter = ts.iterator(); 
				while (iter.hasNext())
					f.format(" %3d", (Integer)iter.next()+1);
				System.out.println();
		    }
	    }
	   	    
	    
	    //输出常规资源需求信息
	    System.out.println("************************************************************************");
	    System.out.println("REQUESTS/DURATIONS:");
	    System.out.print("jobnr. mode duration");
	    for(int r=0;r<this.R;r++)
	    	f.format("  R%2d",r+1);
	    for(int r=0;r<this.N;r++)
	    	f.format("  N%2d",r+1);
	    for(int r=0;r<this.D;r++)
	    	f.format("  D%2d",r+1);
	    System.out.println();
	    System.out.println("------------------------------------------------------------------------");
	    for(int j=0;j<this.nrOfJobs;j++){
	    	for(int m=0;m<this.jobs[j].nrOfModes;m++){
	    		if(m==0){
	    			f.format("%3d    %3d   %3d   ",j+1,m+1,this.jobs[j].modes[m].duration);
	    			for(int r=0;r<this.R;r++)
	    				f.format("  %3d", this.jobs[j].modes[m].RResReq[r]);
	    			for(int r=0;r<this.N;r++)
	    				f.format("  %3d", this.jobs[j].modes[m].NResReq[r]);
	    			for(int r=0;r<this.D;r++)
	    				f.format("  %3d", this.jobs[j].modes[m].DResReq[r]);
	    			System.out.println();
	    		}
	    		else{
	    			f.format("       %3d   %3d   ",m+1,this.jobs[j].modes[m].duration);
	    			for(int r=0;r<this.R;r++)
	    				f.format("  %3d", this.jobs[j].modes[m].RResReq[r]);
	    			for(int r=0;r<this.N;r++)
	    				f.format("  %3d", this.jobs[j].modes[m].NResReq[r]);
	    			for(int r=0;r<this.D;r++)
	    				f.format("  %3d", this.jobs[j].modes[m].DResReq[r]);
	    			System.out.println();
	    		}
	    	}
	    }
	    
	    //输出常规资源可用量
	    System.out.println("************************************************************************");
	    System.out.println("RESOURCEAVAILABILITIES:");
	    for(int r=0;r<this.R;r++)
	    	f.format("  R%2d",r+1);
	    for(int r=0;r<this.N;r++)
	    	f.format("  N%2d",r+1);
	    for(int r=0;r<this.D;r++)
	    	f.format("'    D%5d",r+1);
	    System.out.println();
	    for(int r=0;r<this.R;r++)
	    	f.format("%5d", this.RPer[r]);
	    for(int r=0;r<this.N;r++)
	    	f.format("%5d", this.NTot[r]);
	    for(int r=0;r<this.D;r++){
	    	f.format("%5d", this.DTot[r]);
	    	f.format("%5d", this.Dper[r]);
	    }
	    System.out.println();
	    System.out.println("************************************************************************");
	    
	    if(Progen.includeSR){
		    //输出空间资源需求量
		    System.out.println("SPATIAL  RESOURCE REQUESTS:");
		    System.out.print("taskGroupNr   #RequestTask ");
		    for(int i=0;i<this.nrOfSRType;i++){
		    	for(int k=0;k<this.typeSR[i].kind;k++){
		    		if(this.typeSR[i].dimension==1)
		    			System.out.print("  "+this.typeSR[i].name+(k+1));
		    		if(this.typeSR[i].dimension==2)
		    			System.out.print("   "+this.typeSR[i].name+(k+1));
		    		if(this.typeSR[i].dimension==3)
		    			System.out.print("    "+this.typeSR[i].name+(k+1));
		    	}   	
		    }
		    System.out.println();
		    System.out.println("------------------------------------------------------------------------");
		    for(int g=0;g<this.taskGroup.size();g++){
		    	TaskGroup group = (TaskGroup)this.taskGroup.get(g);
		    	f.format("%3d     				",g+1);
		    	for(int i=0;i<this.nrOfSRType;i++){
		    		for(int r=this.typeSR[i].startNr;r<=this.typeSR[i].endNr;r++){
		    			if(this.typeSR[i].dimension==1)
		    				System.out.print("("+group.requestSR[r].x+")  ");
		    			if(this.typeSR[i].dimension==2)
		    				System.out.print("("+group.requestSR[r].x+","+group.requestSR[r].y+") ");
		    			if(this.typeSR[i].dimension==3)
		    				System.out.print("("+group.requestSR[r].x+","+group.requestSR[r].y+","+group.requestSR[r].z+")");
		    			System.out.print(" ");
		    		}
		    	}
		    	System.out.println();
		    	//下面输出此任务组下请求性任务的需求
		    	TreeSet ts = new TreeSet(group.requestTaskSet);//构造一个TreeSet,TreeSet可以排序
		    	ts.comparator();
		    	Iterator iter = ts.iterator(); 
				while(iter.hasNext()){
					int requestTaskNr = (Integer)iter.next();
					f.format(" 				%d	  	", requestTaskNr+1);//序号显示的时候加1
					for(int i=0;i<this.nrOfSRType;i++){
			    		for(int r=this.typeSR[i].startNr;r<=this.typeSR[i].endNr;r++){
			    			if(this.typeSR[i].dimension==1)
			    				f.format("("+"%d"+")  ",this.jobs[requestTaskNr].requestSR[r].x);
			    			if(this.typeSR[i].dimension==2)
			    				f.format("("+"%d"+",%d"+") ",this.jobs[requestTaskNr].requestSR[r].x,this.jobs[requestTaskNr].requestSR[r].y);
			    			if(this.typeSR[i].dimension==3)
			    				f.format("("+"%d"+",%d"+",%d"+")",this.jobs[requestTaskNr].requestSR[r].x,this.jobs[requestTaskNr].requestSR[r].y,this.jobs[requestTaskNr].requestSR[r].z);
			    			System.out.print(" ");
			    		}
			    	}
					System.out.println();//这个请求性任务的一行输出完毕
				}   	
		    }
		    
		    //下面输出空间资源可用量
		    System.out.println("************************************************************************");
		    System.out.println("SPATIAL  RESOURCE AVAILABILITIES:");
		    for(int i=0;i<this.nrOfSRType;i++){
		    	for(int k=0;k<this.typeSR[i].kind;k++){
		    		if(this.typeSR[i].dimension==1)
		    			System.out.print(this.typeSR[i].name+(k+1)+"  ");
		    		if(this.typeSR[i].dimension==2)
		    			System.out.print(this.typeSR[i].name+(k+1)+"   ");
		    		if(this.typeSR[i].dimension==3)
		    			System.out.print(this.typeSR[i].name+(k+1)+"    ");
		    	}   	
		    }
		    System.out.println();
		    for(int i=0;i<this.nrOfSRType;i++){
		    	for(int r=this.typeSR[i].startNr;r<=this.typeSR[i].endNr;r++){
		    		if(this.typeSR[i].dimension==1)
		    			System.out.print("("+this.SR[r].avail.x+")  ");
		    		if(this.typeSR[i].dimension==2)
		    			System.out.print("("+this.SR[r].avail.x+","+this.SR[r].avail.y+") ");
		    		if(this.typeSR[i].dimension==3)
		    			System.out.print("("+this.SR[r].avail.x+","+this.SR[r].avail.y+","+this.SR[r].avail.z+")");
		    		System.out.print(" ");
		    	}
		    }
		    System.out.println();
		    
		    //下面输出空间资源方向
		    System.out.println("************************************************************************");
		    System.out.println("SPATIAL RESOURCE ORIENTATION:");
		    for(int i=0;i<this.nrOfSRType;i++){
		    	for(int k=0;k<this.typeSR[i].kind;k++){
		    		if(this.typeSR[i].dimension==1)
		    			System.out.print(this.typeSR[i].name+(k+1)+"  ");
		    		if(this.typeSR[i].dimension==2)
		    			System.out.print(this.typeSR[i].name+(k+1)+"   ");
		    		if(this.typeSR[i].dimension==3)
		    			System.out.print(this.typeSR[i].name+(k+1)+"    ");
		    	}   	
		    }
		    System.out.println();
		    for(int i=0;i<this.nrOfSRType;i++){
		    	for(int r=this.typeSR[i].startNr;r<=this.typeSR[i].endNr;r++){
		    		if(this.typeSR[i].dimension==1)
		    			System.out.print("("+this.SR[r].orientation.x+")  ");
		    		if(this.typeSR[i].dimension==2)
		    			System.out.print("("+this.SR[r].orientation.x+","+this.SR[r].orientation.y+") ");
		    		if(this.typeSR[i].dimension==3)
		    			System.out.print("("+this.SR[r].orientation.x+","+this.SR[r].orientation.y+","+this.SR[r].orientation.z+")");
		    		System.out.print(" ");
		    	}
		    }
		    System.out.println();
		    
		    //如果二维空间资源需生成凸多边形样式的需求,则输出
		    if(Utility.genPolygon  ){
		    	System.out.println("************************************************************************");
			    System.out.println("POLYGON STYLE 2-DIMENSIONAL SPATIAL  RESOURCE REQUESTS:");
			    System.out.println("jobnr. mode duration  EST   LST  REQSP   VertexNr  SHAPE" );
			    System.out.println("------------------------------------------------------------------------");
			    for(int j=0;j<this.nrOfJobs;j++){
			    	for(int m=0;m<this.jobs[j].nrOfModes;m++){
			    		if(m==0){
			    			f.format("%3d    %3d   %3d   ",j+1,m+1,this.jobs[j].modes[m].duration);
			    			if(j==0){
			    				System.out.print("    ");
			    				for(int pnr=0;pnr<this.nrOfPro;pnr++){
			    					System.out.print(this.relDate[pnr]+1);//+1是为了让时间从1开始算
			    					if(pnr<this.nrOfPro-1)
			    						System.out.print("/");
			    				}
			    				System.out.print("   ");
			    				for(int pnr=0;pnr<this.nrOfPro;pnr++){
			    					System.out.print(this.relDate[pnr]+1);
			    					if(pnr<this.nrOfPro-1)
			    						System.out.print("/");
			    				}
			    				System.out.print("   ");
			    			}
			    			else if(j==this.nrOfJobs-1){
			    				System.out.print("   ");
			    				for(int pnr=0;pnr<this.nrOfPro;pnr++){
			    					System.out.print(this.CPMT[pnr]+1);
			    					if(pnr<this.nrOfPro-1)
			    						System.out.print("/");
			    				}
			    				System.out.print("   ");
			    				for(int pnr=0;pnr<this.nrOfPro;pnr++){
			    					System.out.print(this.CPMT[pnr]+1);
			    					if(pnr<this.nrOfPro-1)
			    						System.out.print("/");
			    				}
			    				System.out.print("   ");
			    			}
			    			else{
				    			f.format("  %3d", this.jobs[j].T.EST+1);//+1是为了让时间从1开始算
				    			f.format("  %3d  ", this.jobs[j].T.LST+1);
			    			}
			    			for(int i=0;i<this.nrOfSRType;i++){
					    		for(int r=this.typeSR[i].startNr;r<=this.typeSR[i].endNr;r++){
					    			f.format("("+"%d"+",%d"+")  ",this.jobs[j].requestSR[r].x,this.jobs[j].requestSR[r].y);
					    		}
			    			}
			    			if(this.jobs[j].shape == null){
			    				f.format("  %3d   ", 0);
		    					System.out.print("null");
		    				}
			    			else{
			    				f.format("  %3d   ", this.jobs[j].shape.vCount);
			    				for(int i=0;i<this.jobs[j].shape.vCount;i++){
			    					f.format("("+"%.2f"+",%.2f"+")",this.jobs[j].shape.vSet[i].x,this.jobs[j].shape.vSet[i].y);
			    				}
			    			}
			    			System.out.println();
			    		}
			    	}
			    }
		    }
		    System.out.println("************************************************************************");
	    }
	    
	    utility.reSetPrintStream();
	}	

}


class Job {
	int nrOfModes;
	Mode[] modes;
	
	Time T;
	
	int nrOfPred; //直接紧前活动数目
	int nrOfSucc; //直接紧后活动数目
	Set dirPred; //直接紧前活动集合
	Set dirSucc; //直接紧后活动集合
	Set inDirPred;//紧前活动集合
	Set inDirSucc;//紧后活动集合
	
	//=====================空间资源相关==================
	boolean inGroup;
	SRmeasure[] requestSR;//记录对各空间资源需求量(用于请求性任务)
	
	MyPolygon shape;
		
	Job(){
		this.nrOfModes = 0;
		this.modes = new Mode[Progen.maxNrOfModes];
		for(int i=0;i<this.modes.length;i++)
			this.modes[i] = new Mode();
		this.T = new Time();
		this.nrOfPred = 0;
		this.nrOfSucc = 0;
		this.dirPred = new HashSet();
		this.dirSucc = new HashSet();
		this.inDirPred = new HashSet();
		this.inDirSucc = new HashSet();
		
		//=====================空间资源相关==================
		this.inGroup=false;
		this.requestSR=new SRmeasure[Progen.maxNrOfSR];
		for(int i=0;i<requestSR.length;i++)
			this.requestSR[i] = new SRmeasure();
	}
	
	/**
	 * 若type为'd'(不存在的一种资源类型),则选择模式0,即持续期最小的模式. 否则选择当前活动对type类中资源编号为resnr的资源需求最大的模式. 模式记录在T类型中
	 * @param type
	 * @param resnr
	 */
	 void selectMode(char type, int resnr) {
		// TODO Auto-generated method stub
		int maxReq=-1,maxReqMode=0;
		switch(type){
		case 'd':
			this.T.mode = 0;break; //注意,由于模式编号从0开始,且模式持续时间按升序排列,所以用时最短的模式即为编号为0的模式
		case 'R':
			for(int m=0;m<this.nrOfModes;m++){ //活动的模式编号从0开始到nrofModes-1
				if(maxReq<this.modes[m].RResReq[resnr]){
					maxReq = this.modes[m].RResReq[resnr];
					maxReqMode = m;
				}
			}
			this.T.mode = maxReqMode;
			break;
		case 'D':
			for(int m=0;m<this.nrOfModes;m++){//活动的模式编号从0开始到nrofModes-1
				if(maxReq<this.modes[m].DResReq[resnr]){
					maxReq = this.modes[m].DResReq[resnr];
					maxReqMode = m;
				}
			}
			this.T.mode = maxReqMode;
		}
	}

	/**
	 * 设置某工作模式为1,模式持续时间为0,资源需求为0
	 * @param R 可更新资源种类数
	 * @param N 不可更新资源种类数
	 * @param D 双重资源种类数
	 */
	void dumMyJob(int R,int N,int D){
		nrOfModes =1;
		modes[0].duration=0;
		for(int i=0;i<R;i++)
			modes[0].RResReq[i]=0;
		for(int i=0;i<N;i++)
			modes[0].NResReq[i]=0;
		for(int i=0;i<D;i++)
			modes[0].DResReq[i]=0;		
	}
}

class Mode {
	int duration; //模式的持续时间
	int[] RResReq;
	int[] DResReq;
	int[] NResReq;//对3类资源中每种的需求量
	
	Mode(){
		this.duration = 0;
		this.RResReq = new int[Progen.maxRDN];
		this.DResReq = new int[Progen.maxRDN];
		this.NResReq = new int[Progen.maxRDN];
	}
}


class Time{
	int mode;
	int EST;//最早开始时间
	int EFT;//最早结束时间
	int LST;//最晚开始时间
	
	Time(){
		this.mode = 0;
		this.EST = 0;
		this.EFT = 0;
		this.LST = 0;
	}
}


