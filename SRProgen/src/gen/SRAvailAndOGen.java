package gen;

import java.util.ArrayList;

public class SRAvailAndOGen {
	Project p;
	Demand demand;	
	Utility utility;
	
	SRAvailAndOGen(Project p){
		this.p = p;
	}
	
	void SRresAvlAndOMain(Demand demand,Utility utility){
		this.utility = utility;
		this.demand = demand;
		this.init();
		//计算此任务组的最早开始时间和最晚结束时间
		this.calcGroupsESTAndEFT();
		
		//空间资源供给量及资源方向生成
		SRmeasure minUse = new SRmeasure();
		SRmeasure maxUse = new SRmeasure();
//		this.p.calcCPMTimes('d', -1);//当第一个参数为'd'时,其实设置的模式是第0个模式(计算整个项目中每个任务的EST/EFT及每个子项目的CPMT),第二个参数就不需要了,所以设置的是一个没有意义的-1
		for(int i=0;i<this.p.nrOfSRType;i++){
			SRType type=this.p.typeSR[i];
			for(int r=type.startNr;r<=type.endNr;r++){
				SRminUse(r,type,minUse);
				SRmaxUse(r,type,maxUse);
				if(type.dimension==1){
					this.p.SR[r].avail.x=(int)((1-type.SRS)*minUse.x+type.SRS*maxUse.x);
					genOrientation(r, type);
				}
				if(this.p.typeSR[i].dimension==2){
					this.p.SR[r].avail.x=(int)((1-type.SRS)*minUse.x+type.SRS*maxUse.x);
					this.p.SR[r].avail.y=(int)((1-type.SRS)*minUse.y+type.SRS*maxUse.y);
					genOrientation(r, type);
				}
				if(this.p.typeSR[i].dimension==3){
					this.p.SR[r].avail.x=(int)((1-type.SRS)*minUse.x+type.SRS*maxUse.x);
					this.p.SR[r].avail.y=(int)((1-type.SRS)*minUse.y+type.SRS*maxUse.y);
					this.p.SR[r].avail.z=(int)((1-type.SRS)*minUse.z+type.SRS*maxUse.z);
					genOrientation(r, type);
				}
			}
		}	
	}
	
	void init(){
		for(int i=0;i<this.p.nrOfSRType;i++){
			for(int r=this.p.typeSR[i].startNr;r<=this.p.typeSR[i].endNr;r++){
				this.p.SR[r].avail.clear();
				this.p.SR[r].belongSRTypeNr=0;
				this.p.SR[r].orientation.clear();
			}
		}
	}
	
	/**
	 * 计算type类的r空间资源的最大供给量
	 * @param r
	 * @param type
	 * @return
	 */
	private void SRmaxUse(int r, SRType type, SRmeasure maxUse) {
		SRmeasure reqAtTime[] = new SRmeasure[Progen.maxHorizon+1]; //由于时间从1开始计,故reqAtTime[0]闲置不用
		for(int t=0;t<reqAtTime.length;t++)
			reqAtTime[t]=new SRmeasure();//在构造函数中已初始化为(0,0,0),所以无需再设置为0
		
		if(type.dimension==1){
			for(int g=0;g<this.p.taskGroup.size();g++){
				TaskGroup group = (TaskGroup)this.p.taskGroup.get(g);
				for(int t=group.EST+1;t<=group.EFT;t++)
					reqAtTime[t].x=reqAtTime[t].x+group.requestSR[r].x;
			}
			maxUse.clear();
			for(int t=1;t<=this.p.horizon;t++){
				maxUse.x=maxUse.x>reqAtTime[t].x?maxUse.x:reqAtTime[t].x;
			}
		}
//		if(type.dimension==2){
//			int random=this.utility.random.nextInt(2);//用于随机选择一个维度,这个维度上的值是相加的,其它维度上的值取最大值
//			for(int g=0;g<this.p.taskGroup.size();g++){
//				TaskGroup group = (TaskGroup)this.p.taskGroup.get(g);
//				for(int t=group.EST+1;t<=group.EFT;t++){
//					if(random==0){
//						reqAtTime[t].x=reqAtTime[t].x+group.requestSR[r].x;
//						reqAtTime[t].y=reqAtTime[t].y>group.requestSR[r].y?reqAtTime[t].y:group.requestSR[r].y;
//					}
//					else{
//						reqAtTime[t].x=reqAtTime[t].x>group.requestSR[r].x?reqAtTime[t].x:group.requestSR[r].x;
//						reqAtTime[t].y=reqAtTime[t].y+group.requestSR[r].y;
//					}
//				}
//			}
//			//maxUse取各时段每个维度上的最大值
//			maxUse.clear();
//			for(int t=1;t<=this.p.horizon;t++){
//				maxUse.x=maxUse.x>reqAtTime[t].x?maxUse.x:reqAtTime[t].x;
//				maxUse.y=maxUse.y>reqAtTime[t].y?maxUse.y:reqAtTime[t].y;
//			}
//		}
		
//		if(type.dimension==2){
//			for(int g=0;g<this.p.taskGroup.size();g++){
//				TaskGroup group = (TaskGroup)this.p.taskGroup.get(g);
//				for(int t=group.EST+1;t<=group.EFT;t++){
//					int maxX = reqAtTime[t].x>group.requestSR[r].x?reqAtTime[t].x:group.requestSR[r].x;
//					int maxY = reqAtTime[t].y>group.requestSR[r].y?reqAtTime[t].y:group.requestSR[r].y;
//					if((reqAtTime[t].x+group.requestSR[r].x)*maxY < (reqAtTime[t].y+group.requestSR[r].y)*maxX){
//						reqAtTime[t].x=reqAtTime[t].x+group.requestSR[r].x;
//						reqAtTime[t].y=maxY;
//					}
//					else if((reqAtTime[t].x+group.requestSR[r].x)*maxY == (reqAtTime[t].y+group.requestSR[r].y)*maxX){
//						int random=this.utility.random.nextInt(2);
//						if(random==0){
//							reqAtTime[t].x=reqAtTime[t].x+group.requestSR[r].x;
//							reqAtTime[t].y=maxY;
//						}
//						else{
//							reqAtTime[t].x=maxX;
//							reqAtTime[t].y=reqAtTime[t].y+group.requestSR[r].y;
//						}
//					}
//					else{
//						reqAtTime[t].x=maxX;
//						reqAtTime[t].y=reqAtTime[t].y+group.requestSR[r].y;
//					}
//				}
//			}
//			//maxUse取各时段每个维度上的最大值
//			maxUse.clear();
//			for(int t=1;t<=this.p.horizon;t++){
//				maxUse.x=maxUse.x>reqAtTime[t].x?maxUse.x:reqAtTime[t].x;
//				maxUse.y=maxUse.y>reqAtTime[t].y?maxUse.y:reqAtTime[t].y;
//			}
//		}
		ArrayList<Block> reqAtTimeArr[] = new ArrayList[Progen.maxHorizon+1]; 
		for(int i=0;i<reqAtTimeArr.length;i++){
			reqAtTimeArr[i] = new ArrayList<Block>();
		}
		if(type.dimension==2){
			for(int g=0;g<this.p.taskGroup.size();g++){
				TaskGroup group = (TaskGroup)this.p.taskGroup.get(g);
				for(int t=group.EST+1;t<=group.EFT;t++){
					MyPolygon shape = new MyPolygon(group.requestSR[r].x,group.requestSR[r].y,new POINT(0,0));
					Block  tempB = new Block(shape);
					reqAtTimeArr[t].add(tempB);
				}
			}
			for(int i=1;i<reqAtTimeArr.length;i++){
				double sumX=0,sumY=0,maxX=0;
				for(int b=0;b<reqAtTimeArr[i].size();b++){
					sumX= sumX+reqAtTimeArr[i].get(b).shape.vSet[2].x;
					sumY = sumY+reqAtTimeArr[i].get(b).shape.vSet[2].y;
					maxX = maxX>reqAtTimeArr[i].get(b).shape.vSet[2].x ? maxX:reqAtTimeArr[i].get(b).shape.vSet[2].x;
				}
				int yardX = (int)(sumX/Math.sqrt(reqAtTimeArr[i].size())+sumX/reqAtTimeArr[i].size());
				yardX = (int) (yardX>maxX ? yardX:maxX);
				Yard yard = new Yard(yardX,sumY,reqAtTimeArr[i]);
				reqAtTimeArr[i] = yard.process();
				reqAtTime[i].x = yardX;
				for(int b=0;b<reqAtTimeArr[i].size();b++){
					reqAtTime[i].y = (int) (reqAtTime[i].y>reqAtTimeArr[i].get(b).positionShape.vSet[2].y?reqAtTime[i].y:reqAtTimeArr[i].get(b).positionShape.vSet[2].y);
				}			
			}
			//maxUse取各时段每个维度上的最大值
			maxUse.clear();
			for(int t=1;t<=this.p.horizon;t++){
				maxUse.x=maxUse.x>reqAtTime[t].x?maxUse.x:reqAtTime[t].x;
				maxUse.y=maxUse.y>reqAtTime[t].y?maxUse.y:reqAtTime[t].y;
			}
		}
		
		
		if(type.dimension==3){
			for(int g=0;g<this.p.taskGroup.size();g++){
				TaskGroup group = (TaskGroup)this.p.taskGroup.get(g);
				for(int t=group.EST+1;t<=group.EFT;t++){
					int maxX = reqAtTime[t].x>group.requestSR[r].x?reqAtTime[t].x:group.requestSR[r].x;
					int maxY = reqAtTime[t].y>group.requestSR[r].y?reqAtTime[t].y:group.requestSR[r].y;
					int maxZ = reqAtTime[t].z>group.requestSR[r].z?reqAtTime[t].z:group.requestSR[r].z;
					if(((reqAtTime[t].x+group.requestSR[r].x)*maxY*maxZ < (reqAtTime[t].y+group.requestSR[r].y)*maxX*maxZ) 
							&& ((reqAtTime[t].x+group.requestSR[r].x)*maxY*maxZ < (reqAtTime[t].z+group.requestSR[r].z)*maxX*maxY)){
						reqAtTime[t].x=reqAtTime[t].x+group.requestSR[r].x;
						reqAtTime[t].y=maxY;
						reqAtTime[t].z=maxZ;
					}
					else if(((reqAtTime[t].y+group.requestSR[r].y)*maxX*maxZ < (reqAtTime[t].x+group.requestSR[r].x)*maxY*maxZ) 
							&& ((reqAtTime[t].y+group.requestSR[r].y)*maxX*maxZ < (reqAtTime[t].z+group.requestSR[r].z)*maxX*maxY)){
						reqAtTime[t].y=reqAtTime[t].y+group.requestSR[r].y;
						reqAtTime[t].x=maxX;
						reqAtTime[t].z=maxZ;
					}
					else if(((reqAtTime[t].z+group.requestSR[r].z)*maxX*maxY < (reqAtTime[t].x+group.requestSR[r].x)*maxY*maxZ) 
							&& ((reqAtTime[t].z+group.requestSR[r].z)*maxX*maxY < (reqAtTime[t].y+group.requestSR[r].y)*maxX*maxZ)){
						reqAtTime[t].z=reqAtTime[t].z+group.requestSR[r].z;
						reqAtTime[t].x=maxX;
						reqAtTime[t].y=maxY;
					}
					else if(((reqAtTime[t].x+group.requestSR[r].x)*maxY*maxZ == (reqAtTime[t].y+group.requestSR[r].y)*maxX*maxZ) 
							&& ((reqAtTime[t].x+group.requestSR[r].x)*maxY*maxZ < (reqAtTime[t].z+group.requestSR[r].z)*maxX*maxY)){
						int random=this.utility.random.nextInt(2);
						if(random==0){
							reqAtTime[t].x=reqAtTime[t].x+group.requestSR[r].x;
							reqAtTime[t].y=maxY;
							reqAtTime[t].z=maxZ;
						}
						else{
							reqAtTime[t].y=reqAtTime[t].y+group.requestSR[r].y;
							reqAtTime[t].x=maxX;
							reqAtTime[t].z=maxZ;
						}
					}
					else if(((reqAtTime[t].x+group.requestSR[r].x)*maxY*maxZ == (reqAtTime[t].z+group.requestSR[r].z)*maxX*maxY) 
							&& ((reqAtTime[t].x+group.requestSR[r].x)*maxY*maxZ < (reqAtTime[t].y+group.requestSR[r].y)*maxX*maxZ)){
						int random=this.utility.random.nextInt(2);
						if(random==0){
							reqAtTime[t].x=reqAtTime[t].x+group.requestSR[r].x;
							reqAtTime[t].y=maxY;
							reqAtTime[t].z=maxZ;
						}
						else{
							reqAtTime[t].z=reqAtTime[t].z+group.requestSR[r].z;
							reqAtTime[t].x=maxX;
							reqAtTime[t].y=maxY;
						}
					}
					else if(((reqAtTime[t].y+group.requestSR[r].y)*maxX*maxZ == (reqAtTime[t].z+group.requestSR[r].z)*maxX*maxY) 
							&& ((reqAtTime[t].y+group.requestSR[r].y)*maxX*maxZ < (reqAtTime[t].x+group.requestSR[r].x)*maxY*maxZ)){
						int random=this.utility.random.nextInt(2);
						if(random==0){
							reqAtTime[t].y=reqAtTime[t].y+group.requestSR[r].y;
							reqAtTime[t].x=maxX;
							reqAtTime[t].z=maxZ;
						}
						else{
							reqAtTime[t].z=reqAtTime[t].z+group.requestSR[r].z;
							reqAtTime[t].x=maxX;
							reqAtTime[t].y=maxY;
						}
					}
					else{
						int random=this.utility.random.nextInt(3);
						if(random==0){
							reqAtTime[t].x=reqAtTime[t].x+group.requestSR[r].x;
							reqAtTime[t].y=maxY;
							reqAtTime[t].z=maxZ;
						}
						else if(random==1){
							reqAtTime[t].y=reqAtTime[t].y+group.requestSR[r].y;
							reqAtTime[t].x=maxX;
							reqAtTime[t].z=maxZ;
						}
						else{
							reqAtTime[t].z=reqAtTime[t].z+group.requestSR[r].z;
							reqAtTime[t].x=maxX;
							reqAtTime[t].y=maxY;
						}
					}
				}
			}
			//maxUse取各时段每个维度上的最大值
			maxUse.clear();
			for(int t=1;t<=this.p.horizon;t++){
				maxUse.x=maxUse.x>reqAtTime[t].x?maxUse.x:reqAtTime[t].x;
				maxUse.y=maxUse.y>reqAtTime[t].y?maxUse.y:reqAtTime[t].y;
				maxUse.z=maxUse.z>reqAtTime[t].z?maxUse.z:reqAtTime[t].z;			
			}
		}
		
//		if(type.dimension==3){
//			int random=this.utility.random.nextInt(3);//用于随机选择一个维度,这个维度上的值是相加的,其它维度上的值取最大值
//			for(int g=0;g<this.p.taskGroup.size();g++){
//				TaskGroup group = (TaskGroup)this.p.taskGroup.get(g);
//				for(int t=group.EST+1;t<=group.EFT;t++){
//					if(random==0){
//						reqAtTime[t].x=reqAtTime[t].x+group.requestSR[r].x;
//						reqAtTime[t].y=reqAtTime[t].y>group.requestSR[r].y?reqAtTime[t].y:group.requestSR[r].y;
//						reqAtTime[t].z=reqAtTime[t].z>group.requestSR[r].z?reqAtTime[t].z:group.requestSR[r].z;
//					}
//					else if(random==1){
//						reqAtTime[t].x=reqAtTime[t].x>group.requestSR[r].x?reqAtTime[t].x:group.requestSR[r].x;
//						reqAtTime[t].y=reqAtTime[t].y+group.requestSR[r].y;
//						reqAtTime[t].z=reqAtTime[t].z>group.requestSR[r].z?reqAtTime[t].z:group.requestSR[r].z;
//					}
//					else{
//						reqAtTime[t].x=reqAtTime[t].x>group.requestSR[r].x?reqAtTime[t].x:group.requestSR[r].x;
//						reqAtTime[t].y=reqAtTime[t].y>group.requestSR[r].y?reqAtTime[t].y:group.requestSR[r].y;
//						reqAtTime[t].z=reqAtTime[t].z+group.requestSR[r].z;
//					}
//				}
//			}
//			//maxUse取各时段每个维度上的最大值
//			maxUse.clear();
//			for(int t=1;t<=this.p.horizon;t++){
//				maxUse.x=maxUse.x>reqAtTime[t].x?maxUse.x:reqAtTime[t].x;
//				maxUse.y=maxUse.y>reqAtTime[t].y?maxUse.y:reqAtTime[t].y;
//				maxUse.z=maxUse.z>reqAtTime[t].z?maxUse.z:reqAtTime[t].z;			
//			}
//		}

	}
	
	
	/**
	 * 计算type类的r空间资源的最小供给量
	 * @param r
	 * @param type
	 * @return
	 */
	void SRminUse(int r, SRType type,SRmeasure minUse){
		minUse.clear();
		if(type.dimension==1){
			for(int g=0;g<this.p.taskGroup.size();g++){
				minUse.x = (minUse.x>(((TaskGroup)this.p.taskGroup.get(g)).requestSR[r].x))?minUse.x:(((TaskGroup)this.p.taskGroup.get(g)).requestSR[r].x);
			}
			return ;
		}
		
		if(type.dimension==2){
			for(int g=0;g<this.p.taskGroup.size();g++){
				minUse.x = (minUse.x>(((TaskGroup)this.p.taskGroup.get(g)).requestSR[r].x))?minUse.x:(((TaskGroup)this.p.taskGroup.get(g)).requestSR[r].x);
				minUse.y = (minUse.y>(((TaskGroup)this.p.taskGroup.get(g)).requestSR[r].y))?minUse.y:(((TaskGroup)this.p.taskGroup.get(g)).requestSR[r].y);
			}
			return ;
		}
		
		if(type.dimension==3){
			for(int g=0;g<this.p.taskGroup.size();g++){
				minUse.x = (minUse.x>(((TaskGroup)this.p.taskGroup.get(g)).requestSR[r].x))?minUse.x:(((TaskGroup)this.p.taskGroup.get(g)).requestSR[r].x);
				minUse.y = (minUse.y>(((TaskGroup)this.p.taskGroup.get(g)).requestSR[r].y))?minUse.y:(((TaskGroup)this.p.taskGroup.get(g)).requestSR[r].y);
				minUse.z = (minUse.z>(((TaskGroup)this.p.taskGroup.get(g)).requestSR[r].z))?minUse.z:(((TaskGroup)this.p.taskGroup.get(g)).requestSR[r].z);
			}
			return ;
		}
	}
	
	/**
	 * 生成type型空间资源中r资源的方向
	 * @param r
	 * @param type
	 */
	void genOrientation(int r, SRType type){
		if(type.orientation){
			if(type.dimension==1){
				if(this.utility.random.nextInt(2)==0)
					this.p.SR[r].orientation.x=1;
				else
					this.p.SR[r].orientation.x=-1;
			}
		
			if(type.dimension==2){
				while((this.p.SR[r].orientation.x==0) && (this.p.SR[r].orientation.y==0)){
					int random = this.utility.random.nextInt(3)-1;
					this.p.SR[r].orientation.x=random;
					random = this.utility.random.nextInt(3)-1;
					this.p.SR[r].orientation.y=random;
				}
			}
			
			if(type.dimension==3){
				while((this.p.SR[r].orientation.x==0) && (this.p.SR[r].orientation.y==0) && (this.p.SR[r].orientation.z==0)){
					int random = this.utility.random.nextInt(3)-1;
					this.p.SR[r].orientation.x=random;
					random = this.utility.random.nextInt(3)-1;
					this.p.SR[r].orientation.y=random;
					random = this.utility.random.nextInt(3)-1;
					this.p.SR[r].orientation.z=random;
				}
			}
		}
	}
	
	/**
	 * 计算p的所有任务组的EST及EFT(任务组中的任一任务总是选择第0种模式,也即与资源无关)
	 */
	void calcGroupsESTAndEFT(){
		for(int g=0;g<this.p.taskGroup.size();g++){
			TaskGroup group = (TaskGroup)this.p.taskGroup.get(g);
			group.calcESTAndEFT(p);
		}
	}
	
}//类结束
