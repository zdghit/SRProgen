package gen;

public class SRType{
	String name;//空间资源类型名称(基文件中自定义)
	int dimension;//维度
	boolean dividable;//可划分性
	boolean orientation;//方向性(通过维度+可划分性+方向性来唯一确定一种空间资源类型)
	int minKind;//该类空间资源中资源的最小种数
	int maxKind;//该类空间资源中资源的最大种数
	SRmeasure minDemand;
	SRmeasure maxDemand;
	int minR;//每个任务组需要SRA类资源的最小种数
	int maxR;//每个任务组需要SRA类资源的最大种数
	float SRF;
	float SRS;
	
	int kind;//该类空间资源具体种数
	int startNr;//该类空间资源在concreteSR数组中的开始编号
	int endNr;//该类空间资源在concreteSR数组中的结束编号
	
	SRType(){
		minDemand = new SRmeasure();
		maxDemand = new SRmeasure();
	}

	public void copy(SRType type) {
		this.name = type.name;
		this.dimension = type.dimension;
		this.dividable = type.dividable;
		this.orientation = type.orientation;
		this.minKind = type.minKind;
		this.maxKind = type.maxKind;
		this.minDemand.x= type.minDemand.x;
		this.minDemand.y= type.minDemand.y;
		this.minDemand.z= type.minDemand.z;
		this.maxDemand.x = type.maxDemand.x;
		this.maxDemand.y = type.maxDemand.y;
		this.maxDemand.z = type.maxDemand.z;
		this.minR = type.minR;
		this.maxR = type.maxR;
		this.SRF = type.SRF;
		this.SRS = type.SRS;
		
		this.kind = type.kind;
		this.startNr = type.startNr;
		this.endNr = type.endNr;		
	}
}

class SRmeasure{
	int x;
	int y;
	int z;
	
	SRmeasure(){
		this.x=0;
		this.y=0;
		this.z=0;
	}
	
	void clear(){
		this.x=0;
		this.y=0;
		this.z=0;
	}
}

class concreteSR{
	SRmeasure avail;//空用量
	SRmeasure orientation;//具体的方向
	int belongSRTypeNr;//所属资源类型编号
	
	concreteSR(){
		avail = new SRmeasure();
		orientation = new SRmeasure();
	}
}
