package gen;

import java.util.Iterator;

public class SRreqgen {
	Project p;//用于保存网络的父对象,即项目的引用
	int [][] SRresDens;
	Utility utility;
	Demand demand;
	
	SRreqgen(Project p){
		this.p = p;
		SRresDens = new int[Progen.maxNrOfTaskGroup][Progen.maxNrOfSR];
	}
	
	void SRresReqMain(Demand demand,Utility utility){
		this.utility = utility;
		this.demand = demand;

		init();
		for(int i=0;i<this.p.nrOfSRType;i++){
			SRresReqSub(this.p.typeSR[i]);
		}
	}
	
	private void init() {
		for(int g=0;g<Progen.maxNrOfTaskGroup;g++){
			for(int r=0;r<Progen.maxNrOfSR;r++)
				SRresDens[g][r]=0;
		}
		for(int j=0;j<this.p.jobs.length;j++){
			for(int r=0;r<this.p.jobs[j].requestSR.length;r++)
				this.p.jobs[j].requestSR[r].clear();//设置任一任务的空间资源需要量都为0(针对请求性任务)
		}
		
	}

	/**
	 * 为每个任务组生成指定的type资源类型中的每种具体资源的需求
	 * @param type
	 */
	private void SRresReqSub(SRType type) {
		genSRresDens(type);
		for(int g=0;g<this.p.taskGroup.size();g++){
			initSRReq(g,type);
			//获取资源需求量
			for(int r=type.startNr;r<=type.endNr;r++){
				SRreqAm(g,r,type);
			}
		}
	}
	
	/**
	 * 产生对type类空间资源中的每种具体资源是否需求的资源需求密度矩阵(满足指定的资源需求因子)
	 * @param type
	 */
	private void genSRresDens(SRType type) {
		testSRResDens(type);
		for(int g=0;g<this.p.taskGroup.size();g++){
			initSRResDens(g,type);
			setMinSRResDens(g,type);
		}
		setSRResDens(type);
	}
	
	/**
	 * 检测当前的空间资源的参数(及根据基文件随机生成的参数)是否有问题,如有,可纠正的纠正并记录错误,否则只报错
	 * @param type
	 */
	private void testSRResDens(SRType type) {
		if(this.utility.error(type.maxR>type.kind,51,demand.errFilePath,type.name))
			type.maxR=type.kind;
		if(this.utility.error(type.minR>type.maxR,52,demand.errFilePath,type.name))
			type.minR=type.maxR;
		if(type.kind>0){
			this.utility.error(type.SRF<(float)type.minR/(float)type.kind,53,demand.errFilePath,type.name);
			this.utility.error(type.SRF>(float)type.maxR/(float)type.kind,54,demand.errFilePath,type.name);
		}
	}
	
	/**
	 * 初始化任务组g对type类中的每种资源都不需要
	 * @param g
	 * @param type
	 */
	private void initSRResDens(int g, SRType type) {
		for(int r=type.startNr;r<=type.endNr;r++)
			this.SRresDens[g][r]=0;
	}
	
	/**
	 * 使任务g对type类资源的需求种数等于要求的最小需求种数(需求某种资源,在二维密度矩阵中把对应单元设置为1)
	 * @param g
	 * @param type
	 */
	private void setMinSRResDens(int g, SRType type) {
		for(int actNr=0,r;actNr<type.minR;){
			r=type.startNr+this.utility.random.nextInt(type.kind);
			if(this.SRresDens[g][r]==0){
				this.SRresDens[g][r]=1;
				actNr++;
			}
		}
	}
	
	/**
	 * 使整个资源需求密度矩阵的密度达到设定的资源需求因子(范围内,因为可有偏差),
	 * 即把当前的SRF调整到要求的SRF
	 * @param type
	 */
		float actSRF;
		private void setSRResDens(SRType type) {
		int element,freeElements;
		
		actSRF=(float)type.minR/(1>type.kind?1:type.kind);
		freeElements=0;
//		for(int g=0;g<this.p.taskGroup.size();g++)
		freeElements=this.p.taskGroup.size()*(type.maxR-type.minR);
		while((type.SRF>actSRF)&&(freeElements>0)){
			element=1+this.utility.random.nextInt(freeElements);//由于random.nextInt(freeElements)产生0到freeElements-1这些数,故+1
			determineSR(element,type);
			freeElements--;
			actSRF=actSRF+(float)1/(float)(this.p.taskGroup.size()*type.kind);
		}
		this.utility.error(actSRF<(type.SRF*(1-this.demand.base.reqTol)), 55,demand.errFilePath,type.name);
		this.utility.error(actSRF>(type.SRF*(1+this.demand.base.reqTol)), 56,demand.errFilePath,type.name);
	}
	
	private void determineSR(int element, SRType type) {
		int rchosen,sum=0;
		for(int g=0;g<this.p.taskGroup.size();g++){
			int k=0;
			for(int r=type.startNr;r<=type.endNr;r++){
				if(this.SRresDens[g][r]==1)
					k++;
			}
			//当前任务组一共还需要的资源数
			sum=sum+(type.maxR-k);
			if(sum>element){
				rchosen=1+this.utility.random.nextInt(type.kind-k);
				for(int r=type.startNr,n=0;r<=type.endNr;r++){
					if(this.SRresDens[g][r]==0)
						n++;
					if(n==rchosen){
						this.SRresDens[g][r]=1;
						return;
					}
				}
			}
		}
	}
	
	private void initSRReq(int g, SRType type) {
		for(int r=type.startNr;r<=type.endNr;r++){
			((TaskGroup)this.p.taskGroup.get(g)).requestSR[r].x=0;
			((TaskGroup)this.p.taskGroup.get(g)).requestSR[r].y=0;
			((TaskGroup)this.p.taskGroup.get(g)).requestSR[r].z=0;
		}
	}
	
	private void SRreqAm(int g, int r, SRType type) {
		//对空间资源有需求
		if(this.SRresDens[g][r]==1){
			((TaskGroup)this.p.taskGroup.get(g)).requestSR[r].x=type.minDemand.x+this.utility.random.nextInt(type.maxDemand.x-type.minDemand.x+1);
			if(type.dimension>1){
				((TaskGroup)this.p.taskGroup.get(g)).requestSR[r].y=type.minDemand.y+this.utility.random.nextInt(type.maxDemand.y-type.minDemand.y+1);
//				//生成形状--开始==========================================
//				TaskGroup gtemp = (TaskGroup)this.p.taskGroup.get(g);
//				int k = 3+this.utility.random.nextInt(4);
//				gtemp.shape = Utility.genPolygon(gtemp.requestSR[r].x, gtemp.requestSR[r].y, k);
//				//生成形状--结束==========================================
				
				if(type.dimension>2){
					((TaskGroup)this.p.taskGroup.get(g)).requestSR[r].z=type.minDemand.z+this.utility.random.nextInt(type.maxDemand.z-type.minDemand.z+1);
				}					
			}
			//生成任务组g中各请求性任务对type类的资源r的需求量
			genRequsetTaskRes(g,r,type);
		}
	}

	/**
	 * 生成任务组g中各请求性任务对type类的资源r的需求量
	 * @param g
	 * @param r
	 * @param type
	 */
	private void genRequsetTaskRes(int g, int r, SRType type) {
		TaskGroup group=((TaskGroup)this.p.taskGroup.get(g));
		int requestTaskNr = group.requestTaskSet.size();
		
		if(type.dimension==1){
			if(requestTaskNr>group.requestSR[r].x){//因为需求是随机生成的,所以可能会出现这种情况
				//下面有两种调整需求的策略:①调整需求到最大需求((因为在请求性任务生成的时候,已经
				//保证了请求性任务数量一定不大于最大需求的))②调整需求到请求性任务数量
				if(this.utility.random.nextFloat()<0.5)
					group.requestSR[r].x=requestTaskNr;//此时请求性任务就是均分;
				else
					group.requestSR[r].x=type.maxDemand.x;
			}
			//划分请求
			divideRequest(1, 'x', group, requestTaskNr,r);
		}
		if(type.dimension==2){
			int min=group.requestSR[r].x<group.requestSR[r].y?group.requestSR[r].x:group.requestSR[r].y;
			int max=group.requestSR[r].x>group.requestSR[r].y?group.requestSR[r].x:group.requestSR[r].y;
			if(requestTaskNr<=min){
				int choosen=this.utility.random.nextInt(2);
				if(choosen==0){
					divideRequest(2, 'x', group, requestTaskNr,r);
				}
				else{
					divideRequest(2, 'y', group, requestTaskNr,r);
				}
			}
			if((requestTaskNr>min)&&(requestTaskNr<=max)){
				if(group.requestSR[r].x==max){
					divideRequest(2, 'x', group, requestTaskNr,r);
				}
				else{
					divideRequest(2, 'y', group, requestTaskNr,r);
				}
			}
			if(requestTaskNr>max){
				//先调整任务组对资源r的需求,再生成请求性任务的需求
				if(type.maxDemand.x>type.maxDemand.y){
					if(this.utility.random.nextFloat()<0.5)
						group.requestSR[r].x=requestTaskNr;//此时请求性任务就是均分;
					else
						group.requestSR[r].x=type.maxDemand.x;
					divideRequest(2, 'x', group, requestTaskNr,r);
				}					
				else{
					if(this.utility.random.nextFloat()<0.5)
						group.requestSR[r].y=requestTaskNr;//此时请求性任务就是均分;
					else
						group.requestSR[r].y=type.maxDemand.y;
					divideRequest(2, 'y', group, requestTaskNr,r);
				}									
			}
		}
		
		if(type.dimension==3){
			int xx=group.requestSR[r].x;
			int yy=group.requestSR[r].y;
			int zz=group.requestSR[r].z;
			//共8中情况
			//第1种情况
			if(requestTaskNr<=xx && requestTaskNr<=yy && requestTaskNr<=zz){
				int choosen=this.utility.random.nextInt(3);
				if(choosen==0)
					divideRequest(3, 'x', group, requestTaskNr,r);
				else if(choosen==1)
					divideRequest(3, 'y', group, requestTaskNr,r);
				else
					divideRequest(3, 'z', group, requestTaskNr,r);
			}
			//第2种情况
			if(requestTaskNr>xx &&requestTaskNr<=yy && requestTaskNr<=zz){
				int choosen=this.utility.random.nextInt(2);
				if(choosen==0)
					divideRequest(3, 'y', group, requestTaskNr,r);
				else
					divideRequest(3, 'z', group, requestTaskNr,r);
			}
			//第3种情况
			if(requestTaskNr>yy &&requestTaskNr<=xx && requestTaskNr<=zz){
				int choosen=this.utility.random.nextInt(2);
				if(choosen==0)
					divideRequest(3, 'x', group, requestTaskNr,r);
				else
					divideRequest(3, 'z', group, requestTaskNr,r);
			}
			//第4种情况
			if(requestTaskNr>zz &&requestTaskNr<=xx && requestTaskNr<=yy){
				int choosen=this.utility.random.nextInt(2);
				if(choosen==0)
					divideRequest(3, 'x', group, requestTaskNr,r);
				else
					divideRequest(3, 'y', group, requestTaskNr,r);
			}
			//第5种情况
			if(requestTaskNr<=xx &&requestTaskNr>yy && requestTaskNr>zz){
					divideRequest(3, 'x', group, requestTaskNr,r);
			}
			//第6种情况
			if(requestTaskNr<=yy &&requestTaskNr>xx && requestTaskNr>zz){
				divideRequest(3, 'y', group, requestTaskNr,r);
			}	
			//第7种情况
			if(requestTaskNr<=zz &&requestTaskNr>xx && requestTaskNr>yy){
				divideRequest(3, 'z', group, requestTaskNr,r);
			}
			//第8种情况
			if(requestTaskNr>xx &&requestTaskNr>yy && requestTaskNr>zz){
				//先调整任务组对资源r的需求,再生成请求性任务的需求
				int max=type.maxDemand.x>type.maxDemand.y?type.maxDemand.x:type.maxDemand.y;
				max=max>type.maxDemand.z?max:type.maxDemand.z;
				if(type.maxDemand.x==max){
					if(this.utility.random.nextFloat()<0.5)
						group.requestSR[r].x=requestTaskNr;//此时请求性任务就是均分;
					else
						group.requestSR[r].x=type.maxDemand.x;
					divideRequest(3, 'x', group, requestTaskNr,r);
				}
				if(type.maxDemand.y==max){
					if(this.utility.random.nextFloat()<0.5)
						group.requestSR[r].y=requestTaskNr;//此时请求性任务就是均分;
					else
						group.requestSR[r].y=type.maxDemand.y;
					divideRequest(3, 'y', group, requestTaskNr,r);
				}
				if(type.maxDemand.z==max){
					if(this.utility.random.nextFloat()<0.5)
						group.requestSR[r].z=requestTaskNr;//此时请求性任务就是均分;
					else
						group.requestSR[r].z=type.maxDemand.z;
					divideRequest(3, 'z', group, requestTaskNr,r);
				}
			}
					
		}
		
	}
	
	/**
	 * 把temp[temp.length-1]随机分为temp.length-1段(需要选temp.length-2个分割点,这些分割点的位置记录在temp[1]~temp[temp.length-2]中)
	 * @param temp
	 */
	void genSection(int[] temp){
		int endNr = temp[temp.length-1];
		int chooseFlag[] = new int[endNr];
		//先把标记数组各元素置为0
		for(int i=0;i<chooseFlag.length;i++)
			chooseFlag[i]=0;
		//在[1~endNr-1]范围内随机生成temp.length-2个数
		for(int i=0;i<temp.length-2;){
			int random = 1+this.utility.random.nextInt(endNr-1);
			if(chooseFlag[random]==0){
				chooseFlag[random]=1;
				i++;
			}
		}
		for(int i=1,j=1;i<temp.length-1;i++){
			for(;j<chooseFlag.length;j++){
				if(chooseFlag[j]==1){
					temp[i]=j;
					j++;
					break;
				}
			}
		}
	}
	
	void divideRequest(int SRdimension,char Dividedimension,TaskGroup group,int requestTaskNr,int r){
		int temp[] = new int[requestTaskNr+1];
		temp[0]=0;
		Iterator iter = group.requestTaskSet.iterator(); 
		if(SRdimension==1){
			temp[temp.length-1]=group.requestSR[r].x;
			genSection(temp);	
			for(int i=1;i<temp.length;i++){
				int task=(Integer)(iter.next());
				this.p.jobs[task].requestSR[r].x=temp[i]-temp[i-1];			
			}
		}
		if(SRdimension==2){
			if(Dividedimension=='x'){
				temp[temp.length-1]=group.requestSR[r].x;
				genSection(temp);
				for(int i=1;i<temp.length;i++){
					int task=(Integer)(iter.next());
					this.p.jobs[task].requestSR[r].x=temp[i]-temp[i-1];		
					this.p.jobs[task].requestSR[r].y=group.requestSR[r].y;
					//===========生成形状--开始===========================
					if(Utility.genPolygon){
						this.p.jobs[task].shape = Utility.genPolygon(this.p.jobs[task].requestSR[r].x, this.p.jobs[task].requestSR[r].y);
					}
					//==========================================
				}
			}
			if(Dividedimension=='y'){
				temp[temp.length-1]=group.requestSR[r].y;
				genSection(temp);
				for(int i=1;i<temp.length;i++){
					int task=(Integer)(iter.next());
					this.p.jobs[task].requestSR[r].y=temp[i]-temp[i-1];		
					this.p.jobs[task].requestSR[r].x=group.requestSR[r].x;
					//============生成形状--开始==============================
					if(Utility.genPolygon){
						this.p.jobs[task].shape = Utility.genPolygon(this.p.jobs[task].requestSR[r].x, this.p.jobs[task].requestSR[r].y);
					}
					//==========================================
				}
			}
		}
		if(SRdimension==3){
			if(Dividedimension=='x'){
				temp[temp.length-1]=group.requestSR[r].x;
				genSection(temp);
				for(int i=1;i<temp.length;i++){
					int task=(Integer)(iter.next());
					this.p.jobs[task].requestSR[r].x=temp[i]-temp[i-1];		
					this.p.jobs[task].requestSR[r].y=group.requestSR[r].y;
					this.p.jobs[task].requestSR[r].z=group.requestSR[r].z;
				}
			}
			if(Dividedimension=='y'){
				temp[temp.length-1]=group.requestSR[r].y;
				genSection(temp);
				for(int i=1;i<temp.length;i++){
					int task=(Integer)(iter.next());
					this.p.jobs[task].requestSR[r].y=temp[i]-temp[i-1];		
					this.p.jobs[task].requestSR[r].x=group.requestSR[r].x;
					this.p.jobs[task].requestSR[r].z=group.requestSR[r].z;
				}
			}
			if(Dividedimension=='z'){
				temp[temp.length-1]=group.requestSR[r].z;
				genSection(temp);
				for(int i=1;i<temp.length;i++){
					int task=(Integer)(iter.next());
					this.p.jobs[task].requestSR[r].z=temp[i]-temp[i-1];		
					this.p.jobs[task].requestSR[r].x=group.requestSR[r].x;
					this.p.jobs[task].requestSR[r].y=group.requestSR[r].y;
				}
			}
		}
	}
		
}
