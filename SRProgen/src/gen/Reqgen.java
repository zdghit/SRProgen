package gen;

public class Reqgen {
	Project p;//用于保存网络的父对象,即项目的引用
	int[][][] resDens;
	FuncSel funcSel;
	Utility utility;
	Demand demand;
	
	Reqgen(Project p){
		this.p = p;
		resDens = new int[Progen.maxNrOfJobs][Progen.maxNrOfModes][Progen.maxRDN];
		funcSel = new FuncSel();
	}
	
	void resReqMain(Demand demand,Utility utility){
		this.utility = utility;
		this.demand = demand;
		
		resReqSub('R');
		chooseResFunc('R');
		
		chooseResFunc('D');
		resReqSub('D');
		
		chooseResFunc('N');
		resReqSub('N');
		
		testEfficiency();
	}


	private void testEfficiency() {
		int numTrials;
		for(int j=1;j<this.p.nrOfJobs-1;j++){
			if(this.p.jobs[j].nrOfModes>1){
				//如果该过程出现缺陷
				if(!efficiency(j)){
					//实验次数
					numTrials=0;
					do{
						//重新生成资源需求以及需求量
						assNewDens(j,'R');
						assNewDens(j,'D');
						assNewReq(j,'R');
						assNewReq(j,'D');
						assNewDens(j,'N');
						assNewReq(j,'N');
						numTrials++;
					}
					while(!efficiency(j) && numTrials<this.demand.base.maxTrials);
					
					if(efficiency(j))
						this.utility.error(true, 29,demand.errFilePath,"");//通过多次试验活动j有效性得到纠正
					else
						this.utility.error(true, 1002,demand.errFilePath,"");
				}
			}
		}
		
	}

	private void assNewReq(int j, char type) {
		// TODO Auto-generated method stub
		initReq(j,type);
		for(int r=0;r<numRes(type);r++)
			reqAm(j,r,type);
	}

	private void assNewDens(int j, char type) {
		int nonZeros,element,sum,rchosen;
		nonZeros=0;
		for(int m=0;m<this.p.jobs[j].nrOfModes;m++){
			for(int r=0;r<numRes(type);r++){
				if(perReq(j,m,r,type)>0)
					nonZeros++;
			}
		}
		initResDens(j,type);
		setMinResDens(j,type);
		nonZeros=nonZeros-minResUsed(type)*p.jobs[j].nrOfModes;
		while(nonZeros>0){		//还产生原来那么多个数的非0元素,使得总的RF不变
			element=1+this.utility.random.nextInt(nonZeros);
			sum=0;
			boolean found=false;
			for(int m=0;((m<this.p.jobs[j].nrOfModes)&&!found);m++){
				int k=0;//k用来记录当前[j][m]的活动模式中,已经用了多少种当前资源类型的资源
				for(int r=0;r<numRes(type);r++){
					if(resDens[j][m][r]==1)
						k++;	
				}
				sum=sum+(maxResUsed(type)-k);
				if(sum>=element){
					rchosen=1+this.utility.random.nextInt(numRes(type)-k);//在当前[j][m]的活动模式中那些值为0的元素中随机选择一个
					int n=0;
					for(int r=0;r<numRes(type);r++){
						if(resDens[j][m][r]==0)
							n++;
						if(n==rchosen){
							resDens[j][m][r]=1;
							nonZeros--;
							found=true;
							break;
						}
					}						
				}
			}
		}		
	}

	private int perReq(int j, int m, int r, char type) {
		// TODO Auto-generated method stub
		switch(type){
		case 'R': return this.p.jobs[j].modes[m].RResReq[r];
		case 'D': return this.p.jobs[j].modes[m].DResReq[r];
		default : return this.p.jobs[j].modes[m].NResReq[r]; //type为'N'时
		}
	}

	/**
	 * zdg-判断是否出现下列情况：对于两种模式M1和M2，M1的持续期比M2短，但是
	 *     M1的资源需求量却比M2大
	 * @param j
	 * @return
	 */
	private boolean efficiency(int j) {
		// TODO Auto-generated method stub
		int m1=0,m2=1;
		boolean M1supM2,M2supM1,efficient=true;
		
		while((efficient)&& (m1<this.p.jobs[j].nrOfModes-1)){
			M1supM2=false;
			M2supM1=false;
			if(this.p.jobs[j].modes[m1].duration<this.p.jobs[j].modes[m2].duration) 
				M1supM2=true;
			else if(this.p.jobs[j].modes[m1].duration>this.p.jobs[j].modes[m2].duration)
				M2supM1=true;
			if(!(M1supM2 && M2supM1)){
				for(int r=0;r<this.p.R;r++){
					if(this.p.jobs[j].modes[m1].RResReq[r]<this.p.jobs[j].modes[m2].RResReq[r]) 
						M1supM2=true;
					else if(this.p.jobs[j].modes[m1].RResReq[r]>this.p.jobs[j].modes[m2].RResReq[r])
						M2supM1=true;
				}
			}
			if(!(M1supM2 && M2supM1)){
				for(int r=0;r<this.p.D;r++){
					if(this.p.jobs[j].modes[m1].DResReq[r]<this.p.jobs[j].modes[m2].DResReq[r]) 
						M1supM2=true;
					else if(this.p.jobs[j].modes[m1].DResReq[r]>this.p.jobs[j].modes[m2].DResReq[r])
						M2supM1=true;
				}
			}
			if(!(M1supM2 && M2supM1)){
				for(int r=0;r<this.p.N;r++){
					if(this.p.jobs[j].modes[m1].NResReq[r]<this.p.jobs[j].modes[m2].NResReq[r]) 
						M1supM2=true;
					else if(this.p.jobs[j].modes[m1].NResReq[r]>this.p.jobs[j].modes[m2].NResReq[r])
						M2supM1=true;
				}
			}
			if(!(M1supM2 && M2supM1))
				efficient=false;
			if(m2==this.p.jobs[j].nrOfModes-1){
				m1++;
				m2=m1+1;
			}
			else
				m2++;							
		}
		return efficient;
	}

	/**
	 * 为指定的资源类型(type)利用轮盘赌的方法,选定它们中每个具体资源的需求量与活动持续期的函数关系编号
	 * @param type 指定的资源类型
	 */
	private void chooseResFunc(char type) {
		// TODO Auto-generated method stub
		int func;
		double choice,cumProb;
		if(type=='R'){
			for(int r=0;r<this.p.R;r++){
				choice=utility.random.nextDouble();//这里稍修改了一些,采用轮盘赌方法(原程序中为近似的轮盘赌)=
				cumProb=0;
				for(func=0;(func<this.demand.base.RFuncProb.length)&&(cumProb<choice);func++)
					cumProb=cumProb+this.demand.base.RFuncProb[func];
				this.funcSel.R[r]=func-1;//值从0开始,为0,1.   0表示无关系;1表示成反关系
			}			
		}
		else if(type=='D'){
			for(int r=0;r<this.p.D;r++){
				choice=utility.random.nextDouble();//这里稍修改了一些,采用轮盘赌方法(原程序中为近似的轮盘赌)=
				cumProb=0;
				for(func=0;(func<this.demand.base.DFuncProb.length)&&(cumProb<choice);func++)
					cumProb=cumProb+this.demand.base.DFuncProb[func];
				this.funcSel.D[r]=func-1;
			}
		}
		else if(type=='N'){
			for(int r=0;r<this.p.N;r++){
				choice=utility.random.nextDouble();//这里稍修改了一些,采用轮盘赌方法(原程序中为近似的轮盘赌)=
				cumProb=0;
				for(func=0;(func<this.demand.base.NFuncProb.length)&&(cumProb<choice);func++)
					cumProb=cumProb+this.demand.base.NFuncProb[func];
				this.funcSel.N[r]=func-1;
			}
		}		
	}
	

	/**
	 * 为每个活动生成对指定的资源类型中的每种具体资源的需求(分两步,1.确定是否需要;2.确定需要的量)
	 * @param type  指定的资源类型
	 */
	private void resReqSub(char type) {
		genResDens(type);
		for(int j=1;j<this.p.nrOfJobs-1;j++){
			initReq(j,type);
			for(int r=0;r<numRes(type);r++){
				reqAm(j,r,type);
			}
		}
	}
	

	private void reqAm(int j, int r, char type) {
		// TODO Auto-generated method stub
		int req1,req2,reqMin,reqMax,d;
		float delta;
		if(resFunc(type,r)==0){
			req1=minResReq(type)+this.utility.random.nextInt(maxResReq(type)-minResReq(type)+1);
			//=====================需求量和模式的执行时间无关.无关时,只要需求该资源,同一活动各种模式需求量相等,这样合适吗??(2011年1月21日:这样还是合适的)
			for(int m=0;m<this.p.jobs[j].nrOfModes;m++){
				if(resDens[j][m][r]==1){
//					req1=minResReq(type)+this.utility.random.nextInt(maxResReq(type)-minResReq(type)+1);//不按原程序,修改了此处==========================================
					writeResReq(j,m,r,req1,type);
				}
			}
		}
		else if(resFunc(type,r)==1){
			req1=minResReq(type)+this.utility.random.nextInt(maxResReq(type)-minResReq(type)+1);
			req2=minResReq(type)+this.utility.random.nextInt(maxResReq(type)-minResReq(type)+1);
			reqMin=(req1<req2)?req1:req2;
			reqMax=(req1>req2)?req1:req2;
			d=1;
			delta=(float)(reqMax-reqMin)/(float)((1>modesReqResWithoutSameDur(j,r))?1:modesReqResWithoutSameDur(j,r));
			for(int m=0;m<this.p.jobs[j].nrOfModes;m++){
				if(resDens[j][m][r]==1){
					req1=(int)(reqMax-d*delta)+this.utility.random.nextInt((int)(reqMax-(d-1)*delta)-(int)(reqMax-d*delta)+1);
					writeResReq(j,m,r,req1,type);
					if(nextModeWithDiffDur(j,m))
						d++;
				}
			}
		}
	}

	/**
	 * 判断下活动j的下一个模式和当前模式m是否有不同执行时间
	 * @param j
	 * @param m
	 * @return
	 */
	private boolean nextModeWithDiffDur(int j, int m) {
		if((m<this.p.jobs[j].nrOfModes-1)&&(this.p.jobs[j].modes[m].duration==this.p.jobs[j].modes[m+1].duration))
			return false;
		else
			return true;
	}

	/**
	 * 统计活动j的模式中需要资源r且执行时间不同的模式数
	 * @param j
	 * @param r
	 * @return
	 */
	private int modesReqResWithoutSameDur(int j, int r) {
		int count=0;
		for(int m=0;m<this.p.jobs[j].nrOfModes;m++){
			if(resDens[j][m][r]==1){
				int flag=0;
				for(int i=0;i<m;i++){
					if((resDens[j][i][r]==1)&&(this.p.jobs[j].modes[i].duration==this.p.jobs[j].modes[m].duration)){
						flag=1;break;
					}
				}
				if(flag==0)
					count++;
			}
		}
		return count;
	}

	private int maxResReq(char type) {
		// TODO Auto-generated method stub
		switch(type){
		case 'R':return this.demand.base.maxRReq;
		case 'D':return this.demand.base.maxDReq;
		default :return this.demand.base.maxNReq;//type为N时
		}
	}

	private int minResReq(char type) {
		// TODO Auto-generated method stub
		switch(type){
		case 'R':return this.demand.base.minRReq;
		case 'D':return this.demand.base.minDReq;
		default :return this.demand.base.minNReq;//type为N时
		}
	}

	/**
	 * 返回指定某类资源的某个具体资源的函数关系编号
	 * @param type 某类资源
	 * @param r	 某个具体资源的编号
	 * @return
	 */
	private int resFunc(char type, int r) {
		// TODO Auto-generated method stub
		switch(type){
		case 'R':return this.funcSel.R[r];
		case 'D':return this.funcSel.D[r];
		default :return this.funcSel.N[r];//type为N时
		}
	}
	
	private void initReq(int j, char type) {
		// TODO Auto-generated method stub
		for(int m=0;m<this.p.jobs[j].nrOfModes;m++){
			for(int r=0;r<numRes(type);r++)
				writeResReq(j,m,r,0,type);
		}
	}

	/**
	 * 写入对资源r的需求到活动j的模式m中
	 * @param j
	 * @param m
	 * @param r
	 * @param req
	 * @param type
	 */
	private void writeResReq(int j, int m, int r, int req, char type) {
		// TODO Auto-generated method stub
		switch(type){
		case 'R':this.p.jobs[j].modes[m].RResReq[r]=req; break;
		case 'D':this.p.jobs[j].modes[m].DResReq[r]=req; break;
		default :this.p.jobs[j].modes[m].NResReq[r]=req; break;//type为N时
		}
	}

	/**
	 * 产生对type类资源中的每种资源是否需求的资源需求密度矩阵(满足指定的资源需求因子)
	 * @param type
	 */
	private void genResDens(char type) {
		// TODO Auto-generated method stub
		testResDens(type);
		for(int j=1;j<this.p.nrOfJobs-1;j++){
			initResDens(j,type);//必需的,因为三类资源,共用数组,所以每类资源生成时,都要初始化
			setMinResDens(j,type);
		}
		setResDens(type);
	}
	

	/**
	 * 使整个资源需求密度矩阵的密度达到设定的资源需求因子(范围内,因为可有偏差),即把当前的RF调整到要求的RF
	 * @param type
	 */
	private void setResDens(char type) {
		// TODO Auto-generated method stub
		float actResFac ; //当前的资源需求密度矩阵的密度(也即资源需求因子)
		int jchosen,element,freeelements;
		
		actResFac=(float)minResUsed(type)/(1>numRes(type)?1:numRes(type));//防止分母为0
		freeelements=0;
		for(int j=1;j<this.p.nrOfJobs-1;j++)
			freeelements=freeelements+this.p.jobs[j].nrOfModes;
		freeelements=freeelements*(maxResUsed(type)-minResUsed(type));//当前矩阵中真正可置为1的元素数目
		while((resFac(type)>actResFac) && (freeelements>0)){
			element=1+this.utility.random.nextInt(freeelements);//由于random.nextInt(freeelements)产生0到freeelements-1这些数,故+1
			jchosen=determine(element,type);
			freeelements--;
			actResFac=actResFac+(float)1/(float)((this.p.nrOfJobs-2)*numRes(type)*this.p.jobs[jchosen].nrOfModes);//新的RF值			
		}
		//设ξ为容许误差，生成的算例中实际RF(actualRF，简称ARF),
		//满足ARF属于[ARF(l-ξ),ARF(1+ξ)]即可
		this.utility.error(actResFac<(resFac(type)*(1-this.demand.base.reqTol)), errorNum(22,type),demand.errFilePath,"");
		this.utility.error(actResFac>(resFac(type)*(1+this.demand.base.reqTol)), errorNum(25,type),demand.errFilePath,"");
	}
	

	/**
	 * 根据(随机得到的)element,找到矩阵中的某个单元设置其为1.
	 * @param element  
	 * @param type
	 * @return 返回设置为1的单元属于的活动编号
	 */
	private int determine(int element, char type) {
		// TODO Auto-generated method stub
		int rchosen,sum=0;
		for(int j=1;j<this.p.nrOfJobs-1;j++){
			for(int m=0;m<this.p.jobs[j].nrOfModes;m++){
				int k=0;//k用来记录当前[j][m]的活动模式中,已经需要了多少种当前资源类型的资源
				for(int r=0;r<numRes(type);r++){
					if(resDens[j][m][r]==1)
						k++;	
				}
				sum=sum+(maxResUsed(type)-k);
				if(sum>=element){
					rchosen=1+this.utility.random.nextInt(numRes(type)-k);//在当前[j][m]的活动模式中那些值为0的元素中随机选择一个
					for(int r=0,n=0;r<numRes(type);r++){
						if(resDens[j][m][r]==0)
							n++;
						if(n==rchosen){
							resDens[j][m][r]=1;	
							return j;
						}
					}						
				}
			}
		}
		return -1;					
	}

	
	/**
	 * 使活动j在其每种模式下,对type类资源中的需求种数等于要求最小需求种数(需求某种资源,在三维密度矩阵中把对应单元设置为1)
	 * @param j
	 * @param type
	 */
	private void setMinResDens(int j, char type) {
		// TODO Auto-generated method stub
		int r;
		for(int m=0;m<this.p.jobs[j].nrOfModes;m++){
			for(int actNr=0;actNr<minResUsed(type);){
				r=this.utility.random.nextInt(numRes(type));//返回一个伪随机数，它是取自此随机数生成器序列的、在 0（包括）和指定值（不包括）之间均匀分布的 int 值
				if(resDens[j][m][r]==0){
					resDens[j][m][r]=1;
					actNr++;
			}
			}
		}
	}


	/**
	 * 初始化活动j在其各模式下对type类中的每种资源都不需要(即设置资源需求密度矩阵的一个平面上的值都为0)
	 * @param j
	 * @param type
	 */
	private void initResDens(int j, char type) {
		// TODO Auto-generated method stub
		for(int m=0;m<this.p.jobs[j].nrOfModes;m++)
			for(int r=0;r<numRes(type);r++)
				this.resDens[j][m][r]=0;
	}

	/**
	 * 检测当前的参数(根据基文件随机生成的参数)是否有问题,如有,可纠正的纠正并记录错误,否则只报错
	 * @param type
	 */
	private void testResDens(char type) {
		// TODO Auto-generated method stub
		if(this.utility.error(maxResUsed(type)>numRes(type),errorNum(10,type),demand.errFilePath,""))//由于某类资源的种类数是运行时在指定范围随机生成的,固有可能跟RRU冲突
			adjustMaxResUsed(type);
		if(this.utility.error(minResUsed(type)>maxResUsed(type), errorNum(13,type),demand.errFilePath,""))
			adjustMinResUsed(type);
		if(numRes(type)>0){
			this.utility.error(resFac(type)<(float)minResUsed(type)/(float)numRes(type),errorNum(16,type),demand.errFilePath,"");
			this.utility.error(resFac(type)>(float)maxResUsed(type)/(float)numRes(type),errorNum(19,type),demand.errFilePath,"");
		}
	}

	/**
	 * 获取type类资源的资源需求因子
	 * @param type
	 * @return
	 */
	private float resFac(char type) {
		// TODO Auto-generated method stub
		switch(type){
		case 'R':return this.demand.base.RRF;
		case 'D':return this.demand.base.DRF;
		default: return this.demand.base.NRF;//此时type为N 
		}
	}

	/**
	 * 调整对type类资源需求的最小种数(为对type类资源需求的最大种数)
	 * @param type
	 */
	private void adjustMinResUsed(char type) {
		// TODO Auto-generated method stub
		switch(type){
		case 'R': this.demand.base.minRRU=this.demand.base.maxRRU;break;
		case 'D':this.demand.base.minDRU=this.demand.base.maxDRU;break;
		default: this.demand.base.minNRU=this.demand.base.maxNRU;//此时type为N
		}
	}

	/**
	 * 获取对type类资源的最小需求种数
	 * @param type
	 * @return
	 */
	private int minResUsed(char type) {
		// TODO Auto-generated method stub
		switch(type){
		case 'R':return this.demand.base.minRRU;
		case 'D':return this.demand.base.minDRU;
		default: return this.demand.base.minNRU;//此时type为N
		}
	}

	/**
	 * 调整对type类资源需求的最大种数(为type类资源拥有的种数)
	 * @param type
	 */
	private void adjustMaxResUsed(char type) {
		// TODO Auto-generated method stub
		switch(type){
		case 'R': this.demand.base.maxRRU=this.p.R;break;
		case 'D':this.demand.base.maxDRU=this.p.D;break;
		default: this.demand.base.maxNRU=this.p.N;//此时type为N
		}
	}

	/**
	 * 获取对type类资源需求的最大种数
	 * @param type
	 * @return
	 */
	private int maxResUsed(char type) {
		// TODO Auto-generated method stub
		switch(type){
		case 'R':return this.demand.base.maxRRU;
		case 'D':return this.demand.base.maxDRU;
		default: return this.demand.base.maxNRU;//此时type为N
		}
	}

	private int errorNum(int i, char type) {
		// TODO Auto-generated method stub
		switch(type){
		case 'R':return i+1;
		case 'D':return i+2;
		default: return i+3;//此时type为N
		}
	}

	/**
	 * 获取type类资源的种数
	 * @param type
	 * @return
	 */
	private int numRes(char type) {
		// TODO Auto-generated method stub
		switch(type){
		case 'R':return this.p.R;
		case 'D':return this.p.D;
		case 'N':return this.p.N;
		default: return -1;
		}
	}

}


/**
 * 此类保存各具体资源需求量与活动持续期关系的函数编号
 * @author xung
 *
 */
class FuncSel{
	int[] N;
	int[] R;
	int[] D;

	/**
	 * FuncSel类的构造函数,new3个保存3类资源的需求量与活动持续期关系函数编号的数组
	 */
	FuncSel(){
		N = new int[Progen.maxRDN];
		R = new int[Progen.maxRDN];
		D = new int[Progen.maxRDN];
	}
}