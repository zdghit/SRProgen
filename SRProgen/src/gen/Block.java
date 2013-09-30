package gen;



public class Block {
	String name;//分段名称
	MyPolygon shape;//分段的形状(二维凸多边形)
	
	//下面是配置结果信息
	POINT position;//表示配置后引用点的位置
	String yard_code;//所属平台编号
	boolean isConfiged;//是否已被配置
	
	
	MyPolygon insideSpaceRTW;//分段相对于场地W的场内空间
	MyPolygon obstacleSpaceOfA;//A分段相对于此分段(B)的障碍空间
	MyPolygon positionShape;//分段配置后在场地中的形状(位置)

	Block(MyPolygon shape){
		this.shape = shape;
	}
	
	
	Block(String block_code,MyPolygon shape){
		this.name = block_code;
		this.shape = shape;
	}
	
	

	public Block(String block_code, MyPolygon shape,boolean isConfiged) {
		this.name = block_code;
		this.shape = shape;
		this.isConfiged = isConfiged;
	}
	
	//此构造函数用来生成虚拟分段(轨道/虚拟障碍)
	Block(String block_code,MyPolygon shape,int beginT,int finishT,int b_type){
		this.name = block_code;
		this.shape = shape;
		this.positionShape = shape;
		this.isConfiged = true;
	}

	/**
	 * 设置当前分段在场地中的位置到坐标p(设置的结果记录在positionShape中)
	 * @param p
	 * @param Angle
	 */
	public void setPosition(POINT p, int Angle) {
		this.positionShape = this.shape.rotate(Angle);
		this.positionShape.moveTo(p);						
	}
}
