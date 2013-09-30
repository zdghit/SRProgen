package gen;

import java.util.ArrayList;

public class Yard {
	double length;//长度((长边与x轴平行)
	double width;//宽度(宽边与y轴平行))
	
	ArrayList<Block> existBlocks;//当前正在此平台上加工的Blocks
	ArrayList<Block> needConfigBlocks;//某时间区间(天)中当前需要配置的分段集合
	
	//辅助(场地的形状坐标)
	MyPolygon shape;	
	
	
	//此构造函数只用于构造虚拟的场地
	Yard(double length,double width,ArrayList<Block> needConfigBlocks){
		this.length =length;
		this.width = width;
		this.needConfigBlocks  = needConfigBlocks;
		
		this.existBlocks = new ArrayList<Block>();
		this.shape = new MyPolygon(this.length,this.width,new POINT(0,0));
	}
	

	/**
	 * 单场地单天场地资源空间布局的主过程
	 */
	public ArrayList<Block> process() {
		for(;this.needConfigBlocks.size()!=0;){
			Block a=this.needConfigBlocks.get(0);
			//计算当前的分段相对于场地的场内空间
			a.insideSpaceRTW = a.shape.insideSpaceRTW(this);
			//计算当前分段相对于场地上已经存在的所有分段的障碍空间
			for(Block b_exist:this.existBlocks){
				b_exist.obstacleSpaceOfA = a.shape.obstacleSpaceRTB(b_exist.positionShape);
			}
			//下面计算可行配置点集
			ArrayList<POINT> availPointSet = new ArrayList<POINT>();
			
			ArrayList<POINT> Do = new ArrayList<POINT>();
			ArrayList<POINT> Di = new ArrayList<POINT>();
			ArrayList<POINT> Doi = new ArrayList<POINT>();
			ArrayList<POINT> Doo = new ArrayList<POINT>();
			
			//①把场内配置空间S(ai|W)的各顶点加入可行配置点集
			for(int i=0;i<a.insideSpaceRTW.vCount;i++){
				availPointSet.add(a.insideSpaceRTW.vSet[i]);
				Di.add(a.insideSpaceRTW.vSet[i]);
			}
			//②对已放置集合B中的每个bj对应的S(ai|bj)，计算其各顶点是否在S(ai|W)内,如果是,把顶点加入可行配置点集
			for(Block b_exist:this.existBlocks){
				for(int i=0;i<b_exist.obstacleSpaceOfA.vCount;i++){
					if(a.insideSpaceRTW.InsideConvexPolygon(b_exist.obstacleSpaceOfA.vSet[i])){
						if(!Utility.arrayListHasP(availPointSet, b_exist.obstacleSpaceOfA.vSet[i]))
							availPointSet.add(b_exist.obstacleSpaceOfA.vSet[i]);//当availPointSet不含与此点相等的点时，加入此点
						Do.add(b_exist.obstacleSpaceOfA.vSet[i]);
					}	
				}
			}
			//③对已放置集合B中的每个bj，计算其对应的S(ai|bj)与S(ai|W)的交点,并把这些交点加入可行配置点集；
			for(Block b_exist:this.existBlocks){
				ArrayList<POINT> points = a.insideSpaceRTW.intersectionPoint(b_exist.obstacleSpaceOfA);
				for(POINT p:points){
					if(!Utility.arrayListHasP(availPointSet, p))
						availPointSet.add(p);//当availPointSet不含与此点相等的点时，加入此点
					Doi.add(p);
				}
			}
			//④对已放置集合B中的任两个bj、bh对应的S(ai|bj)、S(ai|bh)，计算它们的交点,并把交点在S(ai|W)内的加入可行配置点集
			for(int i=0;i<this.existBlocks.size()-1;i++){
				for(int j=i+1;j<this.existBlocks.size();j++){
					ArrayList<POINT> points = this.existBlocks.get(i).obstacleSpaceOfA.intersectionPoint(this.existBlocks.get(j).obstacleSpaceOfA);
					for(POINT p:points){
						if(a.insideSpaceRTW.InsideConvexPolygon(p)){
							if(!Utility.arrayListHasP(availPointSet, p))
								availPointSet.add(p);//当availPointSet不含与此点相等的点时，加入此点
							Doo.add(p);
						}
					}
				}
			}
			//⑤对当前可行配置点集中的每个点，计算其是否在某个S(ai|bj)内(在其上不移除)，如果是，把其从可行配置点集中移除
			ArrayList<POINT> removePoints = new ArrayList<POINT>();
			for(POINT p:availPointSet){
				for(Block b_exist:this.existBlocks){
					if(b_exist.obstacleSpaceOfA.insidepolygon(p)==0){
						removePoints.add(p);
						break;
					}
				}
			}
			availPointSet.removeAll(removePoints);
			
			Do.removeAll(removePoints);
			Di.removeAll(removePoints);
			Doi.removeAll(removePoints);
			Doo.removeAll(removePoints);
						
			//下面对可行配置点选取
			POINT resultPoint = choosePoint(a,availPointSet,Doi);
			setBlock(a,resultPoint,0);//设置当前分段的位置并记录
		}	
		return  this.existBlocks;
	}
	
	
	private POINT choosePoint(Block a,ArrayList<POINT> availPointSet,ArrayList<POINT> Doi) {
		if(availPointSet.isEmpty()){	
			return null;
		}
		else 
			return Blf_PS(a,availPointSet);	
	}



	/**
	 * 当场地上已配置分段(包括轨道)大多为矩形时,借鉴(Bottom-Left-Fill)算法,此算法是对装箱算法BF的一种改进算法.适用于矩形块的装箱问题
	 * 选择最下(优先级高)/最左(优先级低)的参考点
	 * @param a
	 * @param availPointSet
	 * @return
	 */
	private POINT Blf_PS(Block a, ArrayList<POINT> availPointSet) {
		POINT cur = availPointSet.get(0);//能调用到此方法时,availPointSet必定不为空,所有可以直接去第一个元素
		for(int i=0;i<availPointSet.size();i++){
			POINT p = availPointSet.get(i);
			if(p.y < cur.y){
				cur = p;
			}
			else if(p.y == cur.y && p.x < cur.x ){
				cur = p;
			}
		}
		return cur;
	}



	/**
	 * 配置分段a
	 * @param a   配置的分段
	 * @param p   配置的位置(引用点位置)
	 * @param positionAngle 旋转角度
	 * @param k  配置在的天
	 */
	private void setBlock(Block a, POINT p,int positionAngle) {
		a.setPosition(p,positionAngle);
		a.position = new POINT(p);
		a.isConfiged = true;
		
		this.existBlocks.add(a);
		this.needConfigBlocks.remove(a);
		
	}
	

	


}
