package gen;

public class AvailGen {
	Project p;
	Demand demand;	
	AvailGen(Project p){
		this.p = p;
	}
	
	/**
	 * 计算资源可用量
	 * @param demand
	 */
	void resAvlMain(Demand demand){
		//假设资源r在各阶段可获得量K是资源r的最小需求水平Kmin和最大需求水平Kmax的凸组合。
		//其计算公式为Kr=Kmin+ROUND(RS*(Kmax一Kmin))
		for(int r=0;r<this.p.R;r++){
			this.p.calcCPMTimes('R', r);
			this.p.RPer[r]=(int)((1-demand.base.RRS)*minUse(r,'R')+demand.base.RRS*maxUse('R',r));
		}
		for(int r=0;r<this.p.N;r++){
			this.p.NTot[r]=(int)((1-demand.base.NRS)*minCon(r,'N')+demand.base.NRS*maxCon(r,'N'));
		}
		for(int r=0;r<this.p.D;r++){
			this.p.calcCPMTimes('D', r);
			this.p.Dper[r]=(int)((1-demand.base.DRSP)*minUse(r,'D')+demand.base.DRSP*maxUse('D',r));
			this.p.DTot[r]=(int)((1-demand.base.DRST)*minCon(r,'D')+demand.base.DRST*maxCon(r,'D'));
		}
				
	}



	private float maxCon(int r, char type) {
		int totalMaxCon=0,maxConJ;
		for(int j=1;j<this.p.nrOfJobs-1;j++){
			maxConJ=0;
			for(int m=0;m<this.p.jobs[j].nrOfModes;m++)
				maxConJ = (maxConJ>resReq(j,m,r,type))?maxConJ:resReq(j,m,r,type);
			totalMaxCon = totalMaxCon+maxConJ;
		}
		return totalMaxCon;
	}

	/**
	 * 求得type资源类型(不可更新资源)的资源r的最小需求量(整个工期)
	 * @param r
	 * @param type
	 * @return
	 */
	private float minCon(int r, char type) {
		int totalMinCon=0,minConJ;
		for(int j=1;j<this.p.nrOfJobs-1;j++){
//			minConJ=Progen.maxReq*Progen.maxDu;//maxDu表示什么?单个活动的最大持续期?
			minConJ=Progen.MAX;
			for(int m=0;m<this.p.jobs[j].nrOfModes;m++)
				minConJ = (minConJ<resReq(j,m,r,type))?minConJ:resReq(j,m,r,type);
			totalMinCon = totalMinCon+minConJ;
		}
		return totalMinCon;
	}

	private float maxUse(char type, int r) {
		int maxReq;
		int[] reqAtTime = new int[Progen.maxHorizon+1]; //由于时间从1开始计,故reqAtTime[0]闲置不用
		for(int t=1;t<=this.p.horizon;t++)
			reqAtTime[t]=0;
		for(int j=1;j<this.p.nrOfJobs-1;j++){
			for(int t=this.p.jobs[j].T.EST+1;t<=this.p.jobs[j].T.EFT;t++)
				reqAtTime[t]=reqAtTime[t]+resReq(j,this.p.jobs[j].T.mode,r,type);
		}
		maxReq=0;
		for(int t=1;t<=this.p.horizon;t++)
			maxReq=(maxReq>reqAtTime[t])?maxReq:reqAtTime[t];
		return maxReq;
	}
	



	private float minUse(int r, char type) {
		int minReq=0,minReqJ;
		for(int j=1;j<this.p.nrOfJobs-1;j++){
//			minReqJ = Progen.maxReq;
			minReqJ = Progen.MAX;
			for(int m=0;m<this.p.jobs[j].nrOfModes;m++){
				minReqJ = (minReqJ<resReq(j,m,r,type))?minReqJ:resReq(j,m,r,type);
			}
			minReq = (minReq>minReqJ) ? minReq:minReqJ; 
		}
		return minReq;
	}
	

	private int resReq(int j, int m, int r, char type) {
		switch(type){
		case 'R':return this.p.jobs[j].modes[m].RResReq[r];
		case 'D':return this.p.jobs[j].modes[m].DResReq[r];
		default :return this.p.jobs[j].modes[m].NResReq[r]; //type为N时
		}
	}
}


