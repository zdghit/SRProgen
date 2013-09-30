package gen;

import java.lang.Math;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

/**
 * 一个辅助工具类
 * @author xung
 *
 */
public class Utility {
	 Random random ;
	 PrintStream   oldPS;
	 
	 
	 final static  double	INF		= 1E200 ;  //最大值
	 final static double	EP		= 1E-5 ;   //最小值
	 final static int		MAXV	= 100;  //多边形最大的定点数
	 final static double	PI		= 3.14159265; 
	 
	 public static boolean genPolygon = false;
	 public static double rategenTriangle  = 0.2;//生成三角形的概率
	 public static double rategenQuadrangle  = 0.3;//生成四边形的概率
	 public static double rategenPentagon  = 0.3;//生成五边形的概率
	 public static double rategenHexagon  = 0.2;//生成六边形的概率
	 
	 public static double rateRectAngele = 0.1;//生成四边形时，生成矩形的概率
	 public static double rateTrapezium  = 0.1;//生成四边形时，生成梯形的概率
	 public static double rateParallelogram  = 0.1;//生成四边形时，生成平行四边形的概率
	 
	 public static double ratioArea  = 0.4;//生成的凸多边形面积与原矩形面积的最小比率
	 
	 
	 Utility(int seed){
		 if(seed==0)
			 random = new Random();
		 else
			 random = new Random(seed); 
	 }

	 /**
	  * 设置输出流到文件
	  * @param filepath  输出流设定文件路径
	  * @param firstWrite  是否是第一次写入此文件(为true,会清空原有同名文件,如果为false,会续写)
	  */
	void setPrintStream(String filepath,boolean firstWrite){
		this.oldPS = System.out;
        PrintStream ps = null;
        try {
        	FileOutputStream fos;
        	if(firstWrite)
                fos = new FileOutputStream(filepath);
        	else
        		fos = new FileOutputStream(filepath,true);       
          ps = new PrintStream(fos);
        } catch (IOException e) {
          e.printStackTrace();
        }
        if(ps != null){
          System.setOut(ps);
        }
	}
	
	/**
	 * 恢复PrintStream输出指向
	 */
	void reSetPrintStream(){
		System.out.close();//关闭当前到文件的输出流(这样文件就可以打开了)
		System.setOut(this.oldPS);		
	}
	
	/**
	 * 判断两个Set是否有交集
	 * @param a
	 * @param b
	 * @return
	 */
	boolean hasIntersection(Set a,Set b){
		Iterator iter = a.iterator(); 
		while (iter.hasNext()){
			if(b.contains(iter.next()))
				return true;
		}
		return false;
	}
	boolean error(boolean crit,int errorNr,String filepath,String s){
			if(crit){
				this.setPrintStream(filepath,false);
				switch(errorNr){
				case 1:
					System.out.println("ERROR   1: Predecessor could not be determined."); break;
				case 2:
					System.out.println("ERROR   2: Successor could not be determined.");break;
				case 3:
					System.out.println("ERROR   3: Complexity could not be achieved (实际生成低于设定值).");break;
				case 4:
					System.out.println("ERROR   4: Complexity could not be achieved (实际生成高于设定值).");break;
				case 11:
					System.out.println("ERROR  11: max # req. resources > # resources for type R; -> max# := #.");break;
				case 12:
					System.out.println("ERROR  12: max # req. resources > # resources for type D; -> max# := #.");break;
				case 13:
					System.out.println("ERROR  13: max # req. resources > # resources for type N; -> max# := #.");break;
				case 14:
					System.out.println("ERROR  14: min # req. resources > max # for type R; -> min # := max #.");break;
				case 15:
					System.out.println("ERROR  15: min # req. resources > max # for type D; -> min # := max #.");break;
				case 16:
					System.out.println("ERROR  16: min # req. resources > max # for type N; -> min # := max #.");break;
				case 17:
					System.out.println("ERROR  17: RF for R can`t be achieved; min # req. resources too large.");break;
				case 18:
					System.out.println("ERROR  18: RF for D can`t be achieved; min # req. resources too large.");break;
				case 19:
					System.out.println("ERROR  19: RF for N can`t be achieved; min # req. resources too large.");break;
				case 20:
					System.out.println("ERROR  20: RF for R can`t be achieved; max # req. resources too small.");break;
				case 21:
					System.out.println("ERROR  21: RF for D can`t be achieved; max # req. resources too small.");break;
				case 22:
					System.out.println("ERROR  22: RF for N can`t be achieved; max # req. resources too small.");break;
				case 23:
					System.out.println("ERROR  23: Obtained RF falls short the tolerated range for R.");break;
				case 24:
					System.out.println("ERROR  24: Obtained RF falls short the tolerated range for D.");break;
				case 25:
					System.out.println("ERROR  25: Obtained RF falls short the tolerated range for N.");break;
				case 26:
					System.out.println("ERROR  26: Obtained RF exceeds the tolerated range for R.");break;
				case 27:
					System.out.println("ERROR  27: Obtained RF exceeds the tolerated range for D.");break;
				case 28:
					System.out.println("ERROR  28: Obtained RF exceeds the tolerated range for N.");break;
				case 29:
					System.out.println("ERROR  29: More than 1 trial was used to produce a job with non dominated modes.");break;
					
				//任务组生成相关
				case 41:
					System.out.println("ERROR  41: 请求性任务数量大于了任务组对空间资源的最大需求量且不可修正(最小请求性任务数量大于了对所有空间资源类型最大需求量的最小值).");break;
				case 42:
					System.out.println("ERROR  42: 在设定的尝试次数内,子项目"+s+"的任务组生成失败.");break;
				case 43:
					System.out.println("ERROR  43: 在设定的尝试次数内,生成子项目"+s+"任务组失败.(重新生成此子项目的所有任务组.)");break;
				case 44:
					System.out.println("ERROR  44: 生成子项目"+s+"任务组的请求性任务失败.");break;
				case 45:
					System.out.println("ERROR  45: 生成子项目"+s+"任务组的移动性任务失败.");break;
				
				//空间资源相关	
				case 51:
//					System.out.println("ERROR  51: max # req. resources > # resources for type "+s+"; -> max# := #.");break;
					System.out.println("ERROR 51: 对空间资源类型"+s+"的最大需求种数 > 该空间资源类型的种数 ; -> 调整为:最大需求种数 =该空间资源类型种数");break;
				case 52:
//					System.out.println("ERROR  52: min # req. resources > max # resources for type "+s+"; -> min # := max #.");break;
					System.out.println("ERROR 52: 对空间资源类型"+s+"的最小需求种数 > 最大需求种数 ; -> 调整为:最小需求种数 =最大需求种数");break;
				case 53:
//					System.out.println("ERROR  53: SRF for "+s+" can`t be achieved; min # req. resources too large.");break;
					System.out.println("ERROR 53: 空间资源类型"+s+"的SRF无法满足;(因为最小需求种数太大了.)");break;
				case 54:
//					System.out.println("ERROR  54: SRF for "+s+" can`t be achieved; max # req. resources too small.");break;
					System.out.println("ERROR 54: 空间资源类型"+s+"的SRF无法满足;(因为最大需求种数太小了.)");break;
				case 55:
//					System.out.println("ERROR  55: Obtained SRF falls short the tolerated range for "+s+".");break;
					System.out.println("ERROR 55: 生成的空间资源类型"+s+"的SRF小于了可允许的范围)");break;
				case 56:
//					System.out.println("ERROR  56: Obtained SRF exceeds the tolerated range for "+s+".");break;
					System.out.println("ERROR 56: 生成的空间资源类型"+s+"的SRF大于了可允许的范围)");break;
					
				case 1000:
					System.out.println("ERROR1000: Network generation without success.");break;
				case 1001:
					System.out.println("ERROR1001: Redundant arcs in network");break;
				case 1002:
					System.out.println("ERROR1002: Non dominated modes for a job could`nt be produced with max # trials.");break;
				}
				this.reSetPrintStream();
//				if(errorNr>=1000){
//					System.out.println("Error:"+errorNr);
//					System.out.println("PROJECT Generation stopped!!!");
//					System.out.println("---->>>>fatal error execution stopped");
//					System.out.println("terminate->>T::continue ->> RETURN");
//				}
			}
			return crit;
		}
	

	 
	 
	 /**********************
	  *                    * 
	  *   点的基本运算     * 
	  *                    * 
	  **********************/ 
	 
	 /**
	  * 1. [平面上两点之间距离 ](说明:加[]的表示已测试)
	  */
	static double dist(POINT p1,POINT p2)                // 返回两点之间欧氏距离 
	{ 
		return( Math.sqrt( (p1.x-p2.x)*(p1.x-p2.x)+(p1.y-p2.y)*(p1.y-p2.y) ) ); 
	} 
	
	/**
	 * 2. [判断两点是否重合 ](注意:用到了EP,当精确度EP太小时,可能会出现判断失误)
	 * @param p1
	 * @param p2
	 * @return
	 */
	static boolean equal_point(POINT p1,POINT p2)           // 判断两个点是否重合  
	{ 
		return ( (Math.abs(p1.x-p2.x)<EP)&&(Math.abs(p1.y-p2.y)<EP) ); 
	} 
	

	/**
	 * 3. [矢量叉乘 ](注:计算矢量叉积是与直线和线段相关算法的核心部分.注意误差的判断)
	 * r=multiply(sp,ep,op),得到(sp-op)和(ep-op)的叉积 
	 * r>0：ep在矢量op-sp的逆时针方向； 
	   r=0：op/sp/ep三点共线； 
	   r<0：ep在矢量op-sp的顺时针方向 
	   r的物理意义是:平行四边形的带符号的面积
	 */
	static double multiply(POINT sp,POINT ep,POINT op) 
	{ 
		//return((sp.x-op.x)*(ep.y-op.y)-(ep.x-op.x)*(sp.y-op.y)); 
		double result = (sp.x-op.x)*(ep.y-op.y)-(ep.x-op.x)*(sp.y-op.y);
		if(Math.abs(result)<EP)
			return 0;
		else
			return result;
		
	} 
	
	
	/**
	 * 4.[矢量点乘 ](注意误差的判断)
	 * r=dotmultiply(p1,p2,op),得到矢量(p1-op)和(p2-op)的点积(如果两个矢量都非零矢量) 
	 * @param p1
	 * @param p2
	 * @param p0
	 * @return	r<0：两矢量夹角为锐角；
				r=0：两矢量夹角为直角；
				r>0：两矢量夹角为钝角 
	 */
	double dotmultiply(POINT p1,POINT p2,POINT p0) 
	{ 
		//return ((p1.x-p0.x)*(p2.x-p0.x)+(p1.y-p0.y)*(p2.y-p0.y)); 
		double result = (p1.x-p0.x)*(p2.x-p0.x)+(p1.y-p0.y)*(p2.y-p0.y);
		if(Math.abs(result)<EP)
			return 0;
		else
			return result;
	} 
	
	
	/**
	 * 5[判断点是否在线段上] 	
	 * 在线段上的依据：(p在线段l所在的直线上) && (点p在以线段l为对角线的矩形内,此矩形两边分别与x轴y轴平行)
	 * @param l
	 * @param p
	 * @return
	 */
	static boolean online(LINESEG l,POINT p) 
	{ 
		return( (multiply(l.e,p,l.s)==0) &&( ( (p.x-l.s.x)*(p.x-l.e.x)<=0 )&&( (p.y-l.s.y)*(p.y-l.e.y)<=0 ) ) ); 
	} 
	
	/**
	 * 6.[求一点饶某点旋转后的坐标]
	 * (java中所有对象都是在堆内存分配的,在函数中new对象,然后返回对象的引用是可以的.垃圾回收器只会回收堆中没有引用指向的对象)
	 * @param o  旋转的圆心点
	 * @param alpha  逆时针旋转的角度
	 * @param p		原始点
	 * @return	返回点p以点o为圆心逆时针旋转alpha(单位：弧度)后所在的位置
	 */
	static POINT rotate(POINT o,double alpha,POINT p) 
	{ 
		POINT tp=new POINT(); 
		POINT pp = new POINT(p);//不能修改原p的值
		pp.x-=o.x; 
		pp.y-=o.y; 
		tp.x=pp.x*Math.cos(alpha)-pp.y*Math.sin(alpha)+o.x; 
		tp.y=pp.y*Math.cos(alpha)+pp.x*Math.sin(alpha)+o.y; 
		return tp; 
	} 

	/**
	 * 7.[求矢量夹角]
	 * 返回顶角在o点，起始边为os，终止边为oe的夹角(单位：弧度) 
		可以用于求线段之间的夹角 
	原理：
		r = dotmultiply(s,e,o) / (dist(o,s)*dist(o,e))
		r'= multiply(s,e,o)

		r >= 1	angle = 0;
		r <= -1	angle = -PI
		-1<r<1 && r'>0	angle = arccos(r)
	-	1<r<1 && r'<=0	angle = -arccos(r)
	 * @param o  顶角
	 * @param s	 起始点(起始边卫os)
	 * @param e  终止点(终止边为oe)
	 * @return	角度小于pi，返回正值 
	 * 			角度大于pi，返回负值 
	 * 			角度等于pi，返回-PI
	 */
	static double angle(POINT o,POINT s,POINT e) 
	{ 
		double cosfi,fi,norm; 
		double dsx = s.x - o.x; 
		double dsy = s.y - o.y; 
		double dex = e.x - o.x; 
		double dey = e.y - o.y; 

		cosfi=dsx*dex+dsy*dey; 
		norm=(dsx*dsx+dsy*dsy)*(dex*dex+dey*dey); 
		cosfi /= Math.sqrt( norm ); 

		if (cosfi >=  1.0 ) return 0; 
		if (cosfi <= -1.0 ) return -PI; 

		fi=Math.acos(cosfi); 
		if (dsx*dey-dsy*dex>0) return fi;      // 说明矢量os 在矢量 oe的顺时针方向 
		return -fi; 
	}
	
	
	  /*****************************\ 
	  *                             * 
	  *      线段及直线的基本运算   * 
	  *                             * 
	  \*****************************/
	
	/**
	 * 1.[点与线段的关系]
	 * 判断点与线段的关系,用途很广泛 
		本函数是根据下面的公式写的，P是点C到线段AB所在直线的垂足 

               AC dot AB 
       r =     --------- 
                ||AB||^2 
            (Cx-Ax)(Bx-Ax) + (Cy-Ay)(By-Ay) 
         = ------------------------------- 
                         L^2         
    * @param p   P是点C到线段AB所在直线的垂足 (注意:p为垂足)
	 * @param l		线段l
	 * @return	r=0      P = A 
	 * 			r=1      P = B 
	 * 			r<0		 P is on the backward extension of AB(即在A的左方) 
	 * 			r>1      P is on the forward extension of AB(即在B的右方)
	 * 			0<r<1	 P is interior to AB (在AB中)
	 */
	double relation(POINT p,LINESEG l) 
	{ 
		LINESEG tl = new LINESEG(); 
		tl.s=l.s; 
		tl.e=p; 
		return dotmultiply(tl.e,l.e,l.s)/(dist(l.s,l.e)*dist(l.s,l.e)); 
	} 
	
	/**
	 * 2.[求点到线段所在直线垂线的垂足]
	 * @param p	点p
	 * @param l 线段l
	 * @return	垂足点
	 */
	POINT perpendicular(POINT p,LINESEG l) 
	{ 
		double r=relation(p,l); 
		POINT tp = new POINT(); 
		tp.x=l.s.x+r*(l.e.x-l.s.x); 
		tp.y=l.s.y+r*(l.e.y-l.s.y); 
		return tp; 
	} 

	/**
	 * 3.[点到线段的最近点] 
	 * 求点p到线段l的最近点 
	 * 注意：np是线段l上到点p最近的点，不一定是垂足(因为垂足不一定在线段上)
	 * @param p	点p
	 * @param l	线段l
	 * @return	p到线段l的最近点
	 */
	POINT ptolinesegdist(POINT p,LINESEG l) 
	{ 
		double r=relation(p,l); 
		if(r<0) 
		{ 
			return l.s; 
		} 
		if(r>1) 
		{ 
			return l.e;
		} 
		return perpendicular(p,l); 
	} 
	
	/**
	 * [点到线段的最短距离]
	 * @param p
	 * @param l
	 * @return
	 */
	double ptolinesegdistLength(POINT p,LINESEG l) 
	{ 
		 POINT np = ptolinesegdist(p,l);
		 return dist(p,np);
	} 
		
	/**
	 * 4.[点到线段所在直线的距离]
	 * 求点p到线段l所在直线的距离,注意本函数与上个函数的区别
	 * @param p
	 * @param l
	 * @return
	 */
	static double ptoldist(POINT p,LINESEG l) 
	{ 
		return Math.abs(multiply(p,l.e,l.s))/dist(l.s,l.e); //此处利用平行四边形面积的两种求法
	} 
	
	/**
	 * 5.[点到线段所在直线的距离的求法2(利用垂足)]
	 * @param p
	 * @param l
	 * @return
	 */
	double ptoldist2(POINT p,LINESEG l) 
	{ 
		POINT tp = perpendicular(p,l);
		return dist(p,tp);
	} 

	

	
	/**
	 * 7.[求矢量夹角余弦]
	 * 注意：如果想从余弦求夹角的话，注意反余弦函数的定义域是从 0到pi
	 * @param l1	矢量线段l1
	 * @param l2	矢量线段l2
	 * @return		l1与l2夹角的余弦值(-1~1)
	 */
	double cosine(LINESEG l1,LINESEG l2) 
	{ 
		return (((l1.e.x-l1.s.x)*(l2.e.x-l2.s.x) + 
		(l1.e.y-l1.s.y)*(l2.e.y-l2.s.y))/(dist(l1.e,l1.s)*dist(l2.e,l2.s))) ; 
	} 
	
	/**
	 * 8.[求线段之间的夹角]
	 * 返回线段l1与l2之间的夹角 单位:弧度    范围(-pi，pi) 从l1逆时针转到l2为正值,否则为负值  
	 * @param l1
	 * @param l2
	 * @return
	 */
	static double lsAngle(LINESEG l1,LINESEG l2) 
	{ 
		POINT o = new POINT();
		POINT s = new POINT();
		POINT e = new POINT();
		o.x=o.y=0; 
		s.x=l1.e.x-l1.s.x; 
		s.y=l1.e.y-l1.s.y; 
		e.x=l2.e.x-l2.s.x; 
		e.y=l2.e.y-l2.s.y; 
		return angle(o,s,e); 
	} 
	
	/**
	 * 9.[判断线段是否相交]
	 * 如果线段u和v相交(包括相交在端点处)时，返回true 
	 * 判断P1P2跨立Q1Q2的依据是：( P1 - Q1 ) × ( Q2 - Q1 ) * ( Q2 - Q1 ) × ( P2 - Q1 ) >= 0。
	 * 判断Q1Q2跨立P1P2的依据是：( Q1 - P1 ) × ( P2 - P1 ) * ( P2 - P1 ) × ( Q2 - P1 ) >= 0。
	 * @param u		线段u
	 * @param v		线段u
	 * @return		是否相交(包括相交在端点)
	 */
	static boolean intersect(LINESEG u,LINESEG v) 
	{ 
		return( (Math.max(u.s.x,u.e.x)>=Math.min(v.s.x,v.e.x))&&           //排斥实验 
				(Math.max(v.s.x,v.e.x)>=Math.min(u.s.x,u.e.x))&& 
				(Math.max(u.s.y,u.e.y)>=Math.min(v.s.y,v.e.y))&& 
				(Math.max(v.s.y,v.e.y)>=Math.min(u.s.y,u.e.y))&& 
				(multiply(v.s,u.e,u.s)*multiply(u.e,v.e,u.s)>=0)&&         //跨立实验 
				(multiply(u.s,v.e,v.s)*multiply(v.e,u.e,v.s)>=0)); 
	} 
	
	/**
	 * 10.[判断线段是否相交但不交在端点处]
	 * @param u		线段u
	 * @param v		线段u
	 * @return	(线段u和v相交)&&(交点不是双方的端点) 时返回true
	 */
	static boolean intersect_A(LINESEG u,LINESEG v) 
	{ 
		return	((intersect(u,v))&& 
				(!online(u,v.s))&& 
				(!online(u,v.e))&& 
				(!online(v,u.e))&& 
				(!online(v,u.s))); 
	} 
		
	/**
	 * [判断线段u是否跨立线段v所在直线]  
	 * @param u	
	 * @param v	
	 * @return	线段v所在直线与线段u相交时返回true
	 */
	boolean intersect_l(LINESEG u,LINESEG v) 
	{ 
		return multiply(u.s,v.e,v.s)*multiply(v.e,u.e,v.s)>=0; 
	} 
	
	/**
	 * 11.[求线段所在直线的方程]
	 * 根据已知两点坐标，求过这两点的直线解析方程： a*x+b*y+c = 0  (a >= 0)
	 * @param p1
	 * @param p2
	 * @return
	 */
	static LINE makeline(POINT p1,POINT p2) 
	{ 
		LINE tl = new LINE(); 
		int sign = 1; 
		tl.a=p2.y-p1.y; 
		if(tl.a<0) 
		{ 
			sign = -1; 
			tl.a=sign*tl.a; 
		} 
		tl.b=sign*(p1.x-p2.x); 
		tl.c=sign*(p1.y*p2.x-p1.x*p2.y); 
		return tl; 
	} 
	
	/**
	 * 12.[求直线的斜率]
	 * @param l
	 * @return
	 */
	double slope(LINE l) 
	{ 
		if(Math.abs(l.a) < 1e-20)
			return 0; 
		if(Math.abs(l.b) < 1e-20)
			return INF; 
		return -(l.a/l.b); 
	} 
	
	/**
	 * 13.[求直线的倾斜角]
	 * @param l	直线l
	 * @return	返回直线的倾斜角alpha(0 - pi) 
	 */
	double alpha(LINE l) 
	{ 
		if(Math.abs(l.a)< EP)
			return 0; 
		if(Math.abs(l.b)< EP)
			return PI/2; 
		double k=slope(l); 
		if(k>0) 
			return Math.atan(k); 
		else 
			return PI+Math.atan(k); 
	}
	
	/**
	 * 14.[求点关于某直线的对称点] 
	 * @param l	直线l
	 * @param p	点p
	 * @return	点p关于直线l的对称点
	 */
	POINT symmetry(LINE l,POINT p) 
	{ 
	   POINT tp = new POINT(); 
	   tp.x=((l.b*l.b-l.a*l.a)*p.x-2*l.a*l.b*p.y-2*l.a*l.c)/(l.a*l.a+l.b*l.b); 
	   tp.y=((l.a*l.a-l.b*l.b)*p.y-2*l.a*l.b*p.x-2*l.b*l.c)/(l.a*l.a+l.b*l.b); 
	   return tp; 
	} 

	/**
	 * 15.[判断两条直线是否相交及求直线交点]
	 * @param l1	直线 l1(a1*x+b1*y+c1 = 0)
	 * @param l2	直线l2(a2*x+b2*y+c2 = 0)
	 * @return	如果相交,返回交点p;否则返回null
	 */
	static POINT  lineintersect(LINE l1,LINE l2) // 是 L1，L2 
	{ 
		POINT p = null;
		double d=l1.a*l2.b-l2.a*l1.b; 
		if(Math.abs(d)<EP) // 不相交 
			return p; 
		p = new POINT();
		p.x = (l2.c*l1.b-l1.c*l2.b)/d; 
		p.y = (l2.a*l1.c-l1.a*l2.c)/d; 
		return p; 
	} 
	
	/**
	 * 16.[判断线段是否相交，如果相交返回交点]
	 * @param l1
	 * @param l2
	 * @return 如果线段l1和l2相交,返回交点;否则返回null
	 */
	static POINT intersection(LINESEG l1,LINESEG l2) 
	{ 
		LINE ll1,ll2; 
		ll1=makeline(l1.s,l1.e); 
		ll2=makeline(l2.s,l2.e); 
		POINT p = lineintersect(ll1,ll2);
		if(p!=null){
			if(online(l1,p)&&online(l2,p))
				return p;
			else
				return null;
		}
		else 
			return null; 
	} 
	
	/******************************\ 
	*							  * 
	* 多边形常用算法模块		  * 
	*							  * 
	\******************************/ 
	// 如果无特别说明，输入多边形顶点要求按逆时针排列 

	
//	/**
//	 * 1.[判断多边形是否是简单多边形] 
//	 * 要 求：输入顶点序列按逆时针排序 
//	 * 说 明：简单多边形定义(简单多边形是边不相交的多边形,能将平面分成两个区域，即区内和区外.按凸性区分，简单多边形分凸多边形和凹多边形，“凸”的表示它的内角都不大于180°，凹反之。)： 
//	 * 1：循环排序中相邻线段对的交是他们之间共有的单个点 
//	 * 2：不相邻的线段不相交 
//	 * 本程序默认第一个条件已经满足
//	 * @param vcount 顶点数目
//	 * @param polygon[]	顶点数组
//	 * @return	输入的多边形是简单多边形，返回true
//	 */
//	boolean issimple(int vcount,POINT polygon[]) 
//	{ 
//		int i,cn; 
//		LINESEG l1 = new LINESEG();
//		LINESEG l2 = new LINESEG();
//		for(i=0;i<vcount;i++) 
//		{ 
//			l1.s=polygon[i]; 
//			l1.e=polygon[(i+1)%vcount]; 
//			cn=vcount-3; 
//			while(cn>0) 
//			{ 
//				l2.s=polygon[(i+2)%vcount]; 
//				l2.e=polygon[(i+3)%vcount]; 
//				if(intersect(l1,l2)) 
//					break; 
//				cn--; 
//			} 
//			if(cn>0) 
//				return false; 
//		} 
//		return true; 
//	} 
//	
//	/**
//	 * 2. 检查多边形顶点的凸凹性
//	 * @param vcount
//	 * @param polygon
//	 * @param bc
//	 * 返回值：按输入顺序返回多边形顶点的凸凹性判断，bc[i]=1,iff:第i个顶点是凸顶点
//	 */
//	void checkconvex(int vcount,POINT polygon[],boolean bc[]) 
//	{ 
//		int i,index=0; 
//		POINT tp=polygon[0]; 
//		for(i=1;i<vcount;i++) // 寻找第一个凸顶点 
//		{ 
//			if(polygon[i].y<tp.y||(polygon[i].y == tp.y&&polygon[i].x<tp.x)) 
//			{ 
//				tp=polygon[i]; 
//				index=i; 
//			} 
//		} 
//		int count=vcount-1; 
//		bc[index]=true; 
//		while(count>0) // 判断凸凹性 
//		{ 
//			if(multiply(polygon[(index+1)%vcount],polygon[(index+2)%vcount],polygon[index])>=0 ) 
//				bc[(index+1)%vcount]=true; 
//			else 
//				bc[(index+1)%vcount]=false; 
//			index++; 
//			count--; 
//		} 
//	}
//	
//	
//	/**
//	 * 3. 判断多边形是否凸多边形  
//	 * @param vcount
//	 * @param polygon
//	 * @return
//	 */
//	boolean isconvex(int vcount,POINT polygon[]) 
//	{ 
//		boolean [] bc = new boolean[MAXV]; 
//		checkconvex(vcount,polygon,bc); 
//		for(int i=0;i<vcount;i++) // 逐一检查顶点，是否全部是凸顶点 
//			if(!bc[i]) 
//				return false; 
//		return true; 
//	} 
//	
	
	
	/**
	 * 7. 射线法判断点是否在多边形内 10
	 * 射线法判断点q与多边形polygon的位置关系，要求polygon为简单多边形，顶点逆时针排列 
	 * 如果点在多边形内：   返回0 
	 * 如果点在多边形边上： 返回1 
	 * 如果点在多边形外：	返回2
	 * @param vcount
	 * @param Polygon
	 * @param q
	 * @return
	 */
	int insidepolygon(int vcount,POINT Polygon[],POINT q) 
	{ 
		int c=0,i,n; 
		LINESEG l1= new LINESEG();
		LINESEG l2= new LINESEG();
		boolean bintersect_a,bonline1,bonline2,bonline3; 
		double r1,r2; 

		l1.s=q; 
		l1.e=q; 
		l1.e.x=INF; 
		n=vcount; 
		for (i=0;i<vcount;i++) 
		{ 
			l2.s=Polygon[i]; 
			l2.e=Polygon[(i+1)%n]; 
			if(online(l2,q))
				return 1; // 如果点在边上，返回1 
			if ( (bintersect_a=intersect_A(l1,l2))|| // 相交且不在端点 
			( (bonline1=online(l1,Polygon[(i+1)%n]))&& // 第二个端点在射线上 
			( (!(bonline2=online(l1,Polygon[(i+2)%n])))&& /* 前一个端点和后一个端点在射线两侧 */ 
			((r1=multiply(Polygon[i],Polygon[(i+1)%n],l1.s)*multiply(Polygon[(i+1)%n],Polygon[(i+2)%n],l1.s))>0) ||    
			(bonline3=online(l1,Polygon[(i+2)%n]))&&     /* 下一条边是水平线，前一个端点和后一个端点在射线两侧  */ 
				((r2=multiply(Polygon[i],Polygon[(i+2)%n],l1.s)*multiply(Polygon[(i+2)%n], 
			Polygon[(i+3)%n],l1.s))>0) 
					) 
				) 
			) c++; 
		} 
		if(c%2 == 1) 
			return 0; 
		else 
			return 2; 
	} 
	
	
	/**
	 * 8. 判断点是否在凸多边形内 11 
	 * 点q是凸多边形polygon内时，返回true；注意：多边形polygon一定要是凸多边形 
	 * @param vcount
	 * @param polygon
	 * @param q
	 * @return
	 */
	boolean InsideConvexPolygon(int vcount,POINT polygon[],POINT q) // 可用于三角形！ 
	{ 
		POINT p= new POINT(); 
		LINESEG l = new LINESEG(); 
		int i; 
		p.x=0;p.y=0; 
		for(i=0;i<vcount;i++) // 寻找一个肯定在多边形polygon内的点p：多边形顶点平均值 
		{ 
			p.x+=polygon[i].x; 
			p.y+=polygon[i].y; 
		} 
		p.x /= vcount; 
		p.y /= vcount; 

		for(i=0;i<vcount;i++) 
		{ 
			l.s=polygon[i];l.e=polygon[(i+1)%vcount]; 
			if(multiply(p,l.e,l.s)*multiply(q,l.e,l.s)<0) /* 点p和点q在边l的两侧，说明点q肯定在多边形外 */ 
			break; 
		} 
		return (i==vcount); 
	} 
		
	/**
	 * 9. 寻找点集的graham算法 12 
	 * @param PointSet
	 * @param ch
	 * @param n
	 */
	void Graham_scan(POINT PointSet[],POINT ch[],int n,int len) 
	{ 
		int i,j,k=0,top=2; 
		POINT tmp; 
		// 选取PointSet中y坐标最小的点PointSet[k]，如果这样的点有多个，则取最左边的一个 
		for(i=1;i<n;i++) 
			if ( PointSet[i].y<PointSet[k].y || (PointSet[i].y==PointSet[k].y) && (PointSet[i].x<PointSet[k].x) ) 
				k=i; 
		tmp=PointSet[0]; 
		PointSet[0]=PointSet[k]; 
		PointSet[k]=tmp; // 现在PointSet中y坐标最小的点在PointSet[0] 
		for (i=1;i<n-1;i++) /* 对顶点按照相对PointSet[0]的极角从小到大进行排序，极角相同的按照距离PointSet[0]从近到远进行排序 */ 
		{ 
			k=i; 
			for (j=i+1;j<n;j++) 
				if ( multiply(PointSet[j],PointSet[k],PointSet[0])>0 ||  // 极角更小    
					(multiply(PointSet[j],PointSet[k],PointSet[0])==0) && /* 极角相等，距离更短 */        
					dist(PointSet[0],PointSet[j])<dist(PointSet[0],PointSet[k])
				   ) 
					k=j; 
			tmp=PointSet[i]; 
			PointSet[i]=PointSet[k]; 
			PointSet[k]=tmp; 
		} 
		ch[0]=PointSet[0]; 
		ch[1]=PointSet[1]; 
		ch[2]=PointSet[2]; 
		for (i=3;i<n;i++) 
		{ 
			while (multiply(PointSet[i],ch[top],ch[top-1])>=0) 
				top--; 
			ch[++top]=PointSet[i]; 
		} 
		len=top+1; 
	} 
	
	/**
	 * 10.寻找点集凸包的卷包裹法 13  
	 * 参数说明同graham算法
	 * @param PointSet
	 * @param ch
	 * @param n
	 */
	void ConvexClosure(POINT PointSet[],POINT ch[],int n,int len) 
	{ 
		int top=0,i,index,first; 
		double curmax,curcos,curdis; 
		POINT tmp; 
		LINESEG l1 = new LINESEG();
		LINESEG l2 = new LINESEG();
		boolean []use = new boolean[MAXV]; 
		tmp=PointSet[0]; 
		index=0; 
		// 选取y最小点，如果多于一个，则选取最左点 
		for(i=1;i<n;i++) 
		{ 
			if(PointSet[i].y<tmp.y||PointSet[i].y == tmp.y&&PointSet[i].x<tmp.x) 
			{ 
				index=i; 
			} 
			use[i]=false; 
		} 
		tmp=PointSet[index]; 
		first=index; 
		use[index]=true; 

		index=-1; 
		ch[top++]=tmp; 
		tmp.x-=100; 
		l1.s=tmp; 
		l1.e=ch[0]; 
		l2.s=ch[0]; 

		while(index!=first) 
		{ 
			curmax=-100; 
			curdis=0; 
			// 选取与最后一条确定边夹角最小的点，即余弦值最大者 
			for(i=0;i<n;i++) 
			{ 
				if(use[i])continue; 
				l2.e=PointSet[i]; 
				curcos=cosine(l1,l2); // 根据cos值求夹角余弦，范围在 （-1 -- 1 ） 
				if(curcos>curmax || Math.abs(curcos-curmax)<1e-6 && dist(l2.s,l2.e)>curdis) 
				{ 
					curmax=curcos; 
					index=i; 
					curdis=dist(l2.s,l2.e); 
				} 
			} 
			use[first]=false;            //清空第first个顶点标志，使最后能形成封闭的hull 
			use[index]=true; 
			ch[top++]=PointSet[index]; 
			l1.s=ch[top-2]; 
			l1.e=ch[top-1]; 
			l2.s=ch[top-1]; 
		} 
		len=top-1; 
	} 
	
	
	/**
	 * 计算凸多边形(顶点逆时针序)P与Q的交[已初步测试验证]
	 * 要求P/Q必须为凸多边形
	 * @param P
	 * @param Q
	 * @return
	 */
	static MyPolygon conVexPolgyonAnd (MyPolygon P,MyPolygon Q){
		POINT[] P_Q = P.thisInQ(Q);
		POINT[] Q_P = Q.thisInQ(P);
		int numOfP_Q,numOfQ_P;
		for(numOfP_Q=0;P_Q[numOfP_Q]!=null;numOfP_Q++);
		for(numOfQ_P=0;Q_P[numOfQ_P]!=null;numOfQ_P++);
		if(numOfP_Q==0 || numOfQ_P==0)
			return null;//此时说明两个凸多边形没有交点
		for(int i=0;i<numOfQ_P;i++){
			if(pINArr(P_Q,numOfP_Q,Q_P[i])){
				Utility.rightShift(Q_P,numOfQ_P,(numOfQ_P-i));
				break;
			}
		}
		//step2
		for(;;){
			for(int i=0;i<numOfP_Q;i++){
				if(Utility.equal_point(P_Q[i],Q_P[0])){
					Utility.rightShift(P_Q,numOfP_Q,(numOfP_Q-i-1));
					break;
				}
			}
			
			for(;;){
				//下面删除Q_P的首顶点
				for(int i=1;i<numOfQ_P;i++){
					Q_P[i-1].setPoint(Q_P[i]);
				}
				numOfQ_P--;
				if(pINArr(P_Q,numOfP_Q,Q_P[0]) || numOfQ_P==0)
					break;
				else{
					P_Q[numOfP_Q] = new POINT(Q_P[0]);
					numOfP_Q++;
				}
			}
			if(numOfQ_P==0)
				break;
		}
		
		//点计算完毕，下面生成新的凸多边形
		MyPolygon and = new MyPolygon(numOfP_Q,P_Q);
		and.format();
		return and;
	}
	
	static boolean pINArr(POINT []arr,int k,POINT p){
		for(int i=0;i<k;i++){
			if(Utility.equal_point(arr[i],p))
				return true;
		}
		return false;
	}
	
	/**
	 * 实现数组的循环右移
	 * @param arr	数组
	 * @param N	数组元素数
	 * @param k	循环右移位数
	 */
	 static void rightShift(POINT []arr, int N, int k)
	{
	    k %= N;
	    Utility.reverse(arr, 0, N-k-1);
	    Utility.reverse(arr, N-k, N-1);
	    Utility.reverse(arr, 0, N-1);
	}
	 static void  reverse(POINT []arr, int b, int e)
	{
	    for(; b < e; b++, e--)
	    {
	        POINT temp = new POINT(arr[e]);
	        arr[e].setPoint(arr[b]);
	        arr[b].setPoint(temp);
	    }
	}
	 
	 static MyPolygon genPolygon(double x,double y){
		 Random random = new Random();
		 double v = random.nextDouble();
		 MyPolygon genP;
		 if(v<Utility.rategenTriangle){
			 genP = Utility.genPolygonK(x, y, 3);
		 }
		 else if(v<Utility.rategenQuadrangle+Utility.rategenTriangle){
			 genP = Utility.genPolygonK(x, y, 4);
		 }
		 else if(v<Utility.rategenPentagon+Utility.rategenQuadrangle+Utility.rategenTriangle){
			 genP = Utility.genPolygonK(x, y, 5);
		 }
		 else {
			 genP = Utility.genPolygonK(x, y, 6);
		 }
		 return genP;
	 }
	 
	 /**
	  * 以x,y为外接矩形，生成k凸边形
	  * @param x
	  * @param y
	  * @param k
	  * @return
	  */
	static MyPolygon  genPolygonK(double x,double y,int k){
		if(k>6)  
			k=6;
		MyPolygon rectangle = new MyPolygon(x,y,new POINT(0,0));
		 POINT[] vSet = new POINT[k];
		 Random random = new Random();
		 for(int i=0;i<k;i++)
			 vSet[i]=new POINT();
		 MyPolygon result;
		 int numOfTest = 0;
		 do{
			 numOfTest++;
			 if(k==3){
					if(random.nextFloat()<0.5){
						//选择矩形长边两个端点，在对边选择一个点生成三角形
						vSet[0].setPoint(rectangle.vSet[0]);
						vSet[1].setPoint(rectangle.vSet[1]);
						vSet[2].x = random.nextDouble()*x;
						vSet[2].y = rectangle.vSet[2].y;
					}
					else{
						//选择矩形宽边两个端点，在对边选择一个点生成三角形
						vSet[0].setPoint(rectangle.vSet[1]);
						vSet[1].setPoint(rectangle.vSet[2]);
						vSet[2].x = 0;
						vSet[2].y = random.nextDouble()*y;
					}	
				 }
			 else if(k==4){
				 double v = random.nextDouble();
				 if(v<Utility.rateRectAngele){
					 //此时生成矩形，直接用原矩形即可
					 result = new MyPolygon(rectangle,new POINT(0,0));
					 return result;//此时不再旋转,且面积比率也肯定是满足的
				 }
				 else if(v<Utility.rateRectAngele+Utility.rateParallelogram){
					 //此时生成平行四边形
					 if(random.nextFloat()<0.5){
						 vSet[0].setPoint(rectangle.vSet[0]);
						 vSet[1].x = random.nextDouble()*x;
						 for(;vSet[1].x<x/5;)
							 vSet[1].x = random.nextDouble()*x;//保证生成的平行四边形面积不小于原矩形面积的1/5
						 vSet[1].y = 0;
						 vSet[2].setPoint(rectangle.vSet[2]);
						 vSet[3].x = x - vSet[1].x;
						 vSet[3].y = y;
					 }
					 else{
						 vSet[0].setPoint(rectangle.vSet[1]);
						 vSet[1].x = vSet[0].x;
						 vSet[1].y = random.nextDouble()*y;
						 for(;vSet[1].y<y/5;)
							 vSet[1].y = random.nextDouble()*y;
						 vSet[2].setPoint(rectangle.vSet[3]);
						 vSet[3].x = 0;
						 vSet[3].y = y-vSet[1].y;
					 }
				 }
				 else if(v<Utility.rateRectAngele+Utility.rateParallelogram+Utility.rateTrapezium){
					 //此时生成梯形
					 if(random.nextFloat()<0.5){
						 vSet[0].setPoint(rectangle.vSet[0]);
						 vSet[1].setPoint(rectangle.vSet[1]);
						 vSet[2].y = y;
						 vSet[3].y = y;
						 double min = random.nextDouble()*x;
						 double max = random.nextDouble()*x;
						 for(;Math.abs(max-min)<x/10;){ //保证上边长度不小于原矩形此边长的1/10
							 min = random.nextDouble()*x;
							 max = random.nextDouble()*x;
						 }
						 if(min<max){
							 vSet[2].x = max;
							 vSet[3].x = min;
						 }
						 else{
							 vSet[2].x = min;
							 vSet[3].x = max;
						 }			 
					 }
					 else{
						 vSet[0].setPoint(rectangle.vSet[1]);
						 vSet[1].setPoint(rectangle.vSet[2]);
						 vSet[2].x = 0;
						 vSet[3].x = 0;
						 double min = random.nextDouble()*y;
						 double max = random.nextDouble()*y;
						 for(;Math.abs(max-min)<y/10;){ //保证上边长度不小于原矩形此边长的1/10
							 min = random.nextDouble()*y;
							 max = random.nextDouble()*y;
						 }
						 if(min<max){
							 vSet[2].y = max;
							 vSet[3].y = min;
						 }
						 else{
							 vSet[2].y = min;
							 vSet[3].y = max;
						 }			 
					 }
				 }
				 else{  //此时生成普通的四边形
					 vSet[0].x = random.nextDouble()*x;
					 vSet[0].y = 0;
					 vSet[1].x = x;
					 vSet[1].y = random.nextDouble()*y;
					 for(;Utility.equal_point(vSet[0], vSet[1]);)//保证vSet[0]与vSet[1]不重合
						 vSet[1].y = random.nextDouble()*y;
					 vSet[2].y = y;
					 vSet[2].x = random.nextDouble()*x;
					 for(;Utility.equal_point(vSet[1], vSet[2]);)
						 vSet[2].x = random.nextDouble()*x;
					 vSet[3].x = 0;
					 vSet[3].y = random.nextDouble()*y;
					 for(;Utility.equal_point(vSet[3], vSet[2]) || Utility.equal_point(vSet[3], vSet[0]) ;)
						 vSet[3].y = random.nextDouble()*y;			 
				 }			 
			 }
			 else if(k==5){
				 vSet[0].y = 0;
				 vSet[1].y = 0;
				 double min = random.nextDouble()*x;
				 double max = random.nextDouble()*x;
				 for(;Math.abs(max-min)<x/10;){ //保证上边长度不小于原矩形此边长的1/10
					 min = random.nextDouble()*x;
					 max = random.nextDouble()*x;
				 }
				 if(min<max){
					 vSet[0].x = min;
					 vSet[1].x = max;	
				 }
				 else{
					 vSet[0].x = max;
					 vSet[1].x = min;
				 }	
				 vSet[2].x = x;
				 vSet[2].y = random.nextDouble()*y;
				 for(;Utility.equal_point(vSet[2], vSet[1]);)//保证vSet[2]与vSet[1]不重合
					 vSet[2].y = random.nextDouble()*y;
				 vSet[3].y = y;
				 vSet[3].x = random.nextDouble()*x;
				 for(;Utility.equal_point(vSet[3], vSet[2]);)
					 vSet[3].x = random.nextDouble()*x;
				 vSet[4].x = 0;
				 vSet[4].y = random.nextDouble()*y;
				 for(;Utility.equal_point(vSet[4], vSet[3]) || Utility.equal_point(vSet[4], vSet[0]) ;)
					 vSet[4].y = random.nextDouble()*y;		 
			 }
			 else { //超过6边形，全部按6变形处理
				 vSet[0].y = 0;
				 vSet[1].y = 0;
				 double min = random.nextDouble()*x;
				 double max = random.nextDouble()*x;
				 for(;Math.abs(max-min)<x/10;){ //保证上边长度不小于原矩形此边长的1/10
					 min = random.nextDouble()*x;
					 max = random.nextDouble()*x;
				 }
				 if(min<max){
					 vSet[0].x = min;
					 vSet[1].x = max;	
				 }
				 else{
					 vSet[0].x = max;
					 vSet[1].x = min;
				 }	
				 int ran = random.nextInt(3);
				 if(ran == 0){
					 vSet[2].x = x;
					 vSet[3].x = x;
					 min = random.nextDouble()*y;
					 max = random.nextDouble()*y;
					 for(;Math.abs(max-min)<y/10 || (Utility.equal_point(vSet[1],new POINT(x,min))) ||(Utility.equal_point(vSet[1],new POINT(x,max)));){ //保证上边长度不小于原矩形此边长的1/10
						 min = random.nextDouble()*y;
						 max = random.nextDouble()*y;
					 }
					 if(min<max){
						 vSet[2].y = min;
						 vSet[3].y = max;	
					 }
					 else{
						 vSet[2].y = max;
						 vSet[3].y = min;
					 }	
					 vSet[4].y = y;
					 vSet[4].x = random.nextDouble()*x;
					 for(;Utility.equal_point(vSet[4], vSet[3]);)
						 vSet[4].x = random.nextDouble()*x;
					 vSet[5].x = 0;
					 vSet[5].y = random.nextDouble()*y;
					 for(;Utility.equal_point(vSet[5], vSet[4]) || Utility.equal_point(vSet[5], vSet[0]) ;)
						 vSet[5].y = random.nextDouble()*y;	
				 }
				 else if(ran == 1){
					 vSet[2].x = x;
					 vSet[2].y = random.nextDouble()*y;
					 for(;Utility.equal_point(vSet[2], vSet[1]);)//保证vSet[2]与vSet[1]不重合
						 vSet[2].y = random.nextDouble()*y;
					 vSet[3].y = y;
					 vSet[4].y = y;
					 min = random.nextDouble()*x;
					 max = random.nextDouble()*x;
					 for(;Math.abs(max-min)<x/10 || (Utility.equal_point(vSet[2],new POINT(min,y))) ||(Utility.equal_point(vSet[2],new POINT(max,y)));){ //保证上边长度不小于原矩形此边长的1/10
						 min = random.nextDouble()*x;
						 max = random.nextDouble()*x;
					 }
					 if(min<max){
						 vSet[3].x = max;
						 vSet[4].x = min;	
					 }
					 else{
						 vSet[3].x = min;
						 vSet[4].x = max;
					 }	
					 vSet[5].x = 0;
					 vSet[5].y = random.nextDouble()*y;
					 for(;Utility.equal_point(vSet[5], vSet[4]) || Utility.equal_point(vSet[5], vSet[0]) ;)
						 vSet[5].y = random.nextDouble()*y;	
				 }
				 else{
					 vSet[2].x = x;
					 vSet[2].y = random.nextDouble()*y;
					 for(;Utility.equal_point(vSet[2], vSet[1]);)//保证vSet[2]与vSet[1]不重合
						 vSet[2].y = random.nextDouble()*y;
					 vSet[3].y = y;
					 vSet[3].x = random.nextDouble()*x;
					 for(;Utility.equal_point(vSet[3], vSet[2]);)
						 vSet[3].x = random.nextDouble()*x;
					 vSet[4].x = 0;
					 vSet[5].x = 0;
					 min = random.nextDouble()*y;
					 max = random.nextDouble()*y;
					 for(;Math.abs(max-min)<y/10 || (Utility.equal_point(vSet[3],new POINT(0,min))) ||(Utility.equal_point(vSet[3],new POINT(0,max)))||(Utility.equal_point(vSet[0],new POINT(0,min))) ||(Utility.equal_point(vSet[0],new POINT(0,max)));){ //保证上边长度不小于原矩形此边长的1/10
						 min = random.nextDouble()*y;
						 max = random.nextDouble()*y;
					 }
					 if(min<max){
						 vSet[5].y = min;
						 vSet[4].y = max;	
					 }
					 else{
						 vSet[5].y = max;
						 vSet[4].y = min;
					 }				 
				 }		 
			 }
			 result = new MyPolygon(k,vSet);
			 result.format();
		 }while(result.area/(x*y)<Utility.ratioArea && k!=3 && numOfTest <1000);
		 
		 MyPolygon result2 = result.rotate(random.nextInt(4)*90);//仅旋转0、90、180、270四种角度
		 result2.moveTo(new POINT(0,0));
		 return result2;
	 }
	
	
	//其它辅助函数(非几何)
	 /**
	  * 判断两个ArrayList<POINT>是否包含相同的点
	  */
	static boolean arrayListIsEqual(ArrayList<POINT> A,ArrayList<POINT> B){
		for(POINT o:A){
			if(!Utility.arrayListHasP(B, o))
				return false;
		}
		for(POINT o:B){
			if(!Utility.arrayListHasP(A, o))
				return false;
		}
		return true;
	}
	
	/**
	 * 判断ArrayList arr中是否包含点p
	 * @return
	 */
	static boolean arrayListHasP(ArrayList<POINT> arr,POINT p){
		for(POINT temp:arr){
			if(temp.equ(p))
				return true;
		}
		return false;
	}
	
}

/**
 * 点
 * @author xung
 *
 */
class POINT {
		double x; 
		double y; 
		
		POINT(double a, double b) { 
			x=a; 
			y=b;
			} //constructor 
		POINT() { 
			x=0; 
			y=0;
			}
		
		POINT(POINT p){
			this.x = p.x;
			this.y = p.y;
		}
		
		/**
		 * 设置当前点的坐标与点p相同
		 * @param p
		 */
		void setPoint(POINT p){
			this.x = p.x;
			this.y = p.y;
		}
		
		void setPoint(double x,double y){
			this.x = x;
			this.y = y;
		}
		
		boolean equ(POINT p){
			return ( (Math.abs(this.x-p.x)<Utility.EP)&&(Math.abs(this.y-p.y)<Utility.EP) ); 
		}
	}; 

/**
 * 线段
 * @author xung
 *
 */
class LINESEG 
{ 
	POINT s; 
	POINT e; 
	LINESEG(POINT s, POINT e) {
		this.s=s; 
		this.e=e;
	} 
	LINESEG() { } 
}; 

/**
 * 直线
 * @author xung
 *
 */
class LINE           // 直线的解析方程 a*x+b*y+c=0  为统一表示，约定 a >= 0
{ 
   double a; 
   double b; 
   double c; 
   
   LINE(double d1, double d2, double d3) {
	   a=d1; b=d2; c=d3;
	   } 
   LINE() {
	   a=1; b=-1; c=0;
	   } 
}; 

/**
 * 多边形
 * @author xung
 *
 */
class MyPolygon{
	int vCount;//顶点数目
	POINT vSet[];//顶点集,多边形顶点要求按逆时针排列(考虑到程序的通用性,如将来移植到C++下,在这里用数组实现,而没有使用java的ArrayList实现)
	double angle[];//边的法向量与x轴的角度
	double area; //面积
	
	MyPolygon(){
		this.vCount =0;
		this.vSet = new POINT[Utility.MAXV];
		this.angle = new double[Utility.MAXV];
		this.area = 0;
	}
	
	MyPolygon(int vCount,POINT []vSet){
		this.vCount = vCount;
		this.vSet = new POINT[Utility.MAXV];
		for(int i=0;i<vCount;i++){
			this.vSet[i] = new POINT();
			this.vSet[i].x = vSet[i].x;
			this.vSet[i].y = vSet[i].y;
		}
		this.vSet[this.vCount] = new POINT();//构造this.vSet[this.vCount]
		this.angle = new double[Utility.MAXV];
		this.area = 0;//先预设为0,用到时再计算
	}
	
	MyPolygon(ArrayList<POINT> list){
		this.vCount = list.size();
		this.vSet = new POINT[Utility.MAXV];
		for(int i=0;i<this.vCount;i++){
			this.vSet[i] = new POINT();
			this.vSet[i].x = list.get(i).x;
			this.vSet[i].y =list.get(i).y;
		}
		this.vSet[this.vCount] = new POINT();//构造this.vSet[this.vCount]	
		this.angle = new double[Utility.MAXV];
		this.area = 0;//先预设为0,用到时再计算
	}
	
	/**
	 * 根据已有凸多边形A构造一个新的凸多边形,使其引用点位于点p
	 * @param A
	 * @param p
	 */
	MyPolygon(MyPolygon A,POINT p){
		//先计算出移动的向量
		POINT move = new POINT();
		move.x = p.x-A.vSet[0].x;
		move.y = p.y-A.vSet[0].y;
		
		this.vCount=A.vCount;
		this.vSet = new POINT[Utility.MAXV];
		for(int i=0;i<A.vCount;i++){
			this.vSet[i] = new POINT();
			this.vSet[i].x = A.vSet[i].x+move.x;
			this.vSet[i].y = A.vSet[i].y+move.y;
		}
		this.vSet[this.vCount] = new POINT();//构造this.vSet[this.vCount]	
		this.angle = new double[Utility.MAXV];
		for(int i=0;i<A.vCount;i++){
			this.angle[i]=A.angle[i];
		}
		this.area = A.area;
		this.format();
	}
	
	//根据长(长边与x轴平行)、宽(宽边与y轴平行)构造一个矩形，其引用点位于点p
	MyPolygon(double length,double width,POINT p){
		this.vCount = 4;
		this.vSet = new POINT[Utility.MAXV];
		for(int i=0;i<vCount;i++){
			this.vSet[i] = new POINT();
		}
		this.vSet[0].x = p.x;
		this.vSet[0].y = p.y;
		this.vSet[1].x = p.x+length;
		this.vSet[1].y = p.y;
		this.vSet[2].x = p.x+length;
		this.vSet[2].y = p.y+width;
		this.vSet[3].x = p.x;
		this.vSet[3].y = p.y+width;
		this.vSet[this.vCount] = new POINT();//构造this.vSet[this.vCount]	
		this.angle = new double[Utility.MAXV];
		this.area = 0;//先预设为0,用到时再计算
		this.format();
	}
	
	/**
	 * 对多边形进行检查(顶点顺序/简单性/凸凹性)
	 * @return
	 */
	int  check(){
		if(this.isconterclock()){
			if(this.isSimple()){
				if(this.isConvex()){
					return 0;//
				}
				return 3;//表示不是凸多边形
			}
			return 2;//表示不是简单多边形
		}
		return 1;//表示顶点不是按逆时针顺序输入
	}
	
	/**
	 * 计算引用点(最下点,如果有多个,再取最左侧的)在数组中的index
	 */
	int getRefV(){
		int index=0;
		for(int i=1;i<this.vCount;i++){
			if(this.vSet[i].y<this.vSet[index].y){
				index=i;
				continue;
			}
			if(this.vSet[i].y==this.vSet[index].y){
				if(this.vSet[i].x<this.vSet[index].x){
					index=i;
				}
			}
		}
		return index;
	}
		
	/**
	 * 实现数组的循环右移(用于纠正虽按逆时针输入顶点且引用点坐标在原点,但开始点不是引用点的情况)
	 * @param arr	数组
	 * @param N	数组元素数
	 * @param k	循环右移位数
	 */
	void rightShift(POINT []arr, int N, int k)
	{
	    k %= N;
	    reverse(arr, 0, N-k-1);
	    reverse(arr, N-k, N-1);
	    reverse(arr, 0, N-1);
	}
	void reverse(POINT []arr, int b, int e)
	{
	    for(; b < e; b++, e--)
	    {
	        POINT temp = new POINT(arr[e]);
	        arr[e].setPoint(arr[b]);
	        arr[b].setPoint(temp);
	    }
	}
	
	/**
	 * 计算各边与x轴正向的夹角(前提是已经是规定的标准形式,即引用点位于数组第0位,这样可保证夹角递增)
	 */
	void calcAngle(){
		POINT os = new POINT(0,0);
		POINT oe = new POINT(1,0);
		LINESEG l1 = new LINESEG(os,oe);//此线段代表x轴正方向
		for(int i=0;i<this.vCount;i++){
			LINESEG l2 = new LINESEG(this.vSet[i],this.vSet[(i+1)%this.vCount]);
			this.angle[i]=Utility.lsAngle(l1,l2) ;
			if(this.angle[i]<0)
				this.angle[i]=this.angle[i]+2*Utility.PI;
		}
	}
	
	void format(){
		//计算引用点(最下点,如果有多个,再取最左侧的),并调整使得引用点位于vSet数组的第0位
		int refVIndex = this.getRefV();
		if(refVIndex!=0)
			this.rightShift(this.vSet,this.vCount,this.vCount-refVIndex);//引用点不在第0位,则进行移位	
		this.vSet[this.vCount].setPoint(this.vSet[0]);//设置this.vSet[this.vCount]的值与第0位的引用点的坐标相同
		//下面计算面积
		this.area = this.areaOfPolygon();
	}

	

	/**
	 * [判断多边形是否是简单多边形] 
	 * 要 求：输入顶点序列按逆时针排序 
	 * 说 明：简单多边形定义(简单多边形是边不相交的多边形,能将平面分成两个区域，即区内和区外.按凸性区分，简单多边形分凸多边形和凹多边形，“凸”的表示它的内角都不大于180°，凹反之。)： 
	 * 1：循环排序中相邻线段对的交是他们之间共有的单个点 
	 * 2：不相邻的线段不相交 
	 * 本程序默认第一个条件已经满足
	 * @param vcount 顶点数目
	 * @param polygon[]	顶点数组
	 * @return	输入的多边形是简单多边形，返回true
	 */
	boolean isSimple() 
	{ 
		int i,cn; 
		LINESEG l1 = new LINESEG();
		LINESEG l2 = new LINESEG();
		for(i=0;i<vCount;i++) 
		{ 
			l1.s=vSet[i]; 
			l1.e=vSet[(i+1)%vCount]; 
			cn=vCount-3; 
			while(cn>0) 
			{ 
				l2.s=vSet[(i+2)%vCount]; 
				l2.e=vSet[(i+3)%vCount]; 
				if(Utility.intersect(l1,l2)) 
					break; 
				cn--; 
			} 
			if(cn>0) 
				return false; 
		} 
		return true; 
	} 
		
	
	/**
	 * [判断多边形是否凸多边形]  
	 * 要 求：输入顶点序列按逆时针排序(必需,否则判断会出现错误)
	 * @param vcount
	 * @param polygon
	 * @return
	 */
	boolean isConvex() 
	{ 
		boolean [] bc = new boolean[Utility.MAXV]; 
		//下面按输入顺序检查多边形顶点的凸凹性判断，bc[i]=1,iff:第i个顶点是凸顶点
		int index=0; 
		POINT tp=vSet[0]; 
		for(int i=1;i<vCount;i++) // 寻找第一个凸顶点 
		{ 
			if(vSet[i].y<tp.y||(vSet[i].y == tp.y&&vSet[i].x<tp.x)) 
			{ 
				tp=vSet[i]; 
				index=i; 
			} 
		} 
		int count=vCount-1; 
		bc[index]=true; 
		while(count>0) // 判断凸凹性 
		{ 
			if(Utility.multiply(vSet[(index+1)%vCount],vSet[(index+2)%vCount],vSet[index%vCount])>=0 ) 
				bc[(index+1)%vCount]=true; 
			else 
				bc[(index+1)%vCount]=false; 
			index++; 
			count--; 
		} 
		
		for(int i=0;i<vCount;i++) // 逐一检查顶点，是否全部是凸顶点 
			if(!bc[i]) 
				return false; 
		return true; 
	} 
	
	/**
	 * [求多边形面积 ](凸凹都可以,非简单多边形结果将为0)
	 * @param vcount
	 * @param polygon
	 * @return	逆时针时返回正值,顺时针返回负的面积值
	 */
	double areaOfPolygon() 
	{ 
		int i; 
		double s; 
		if (vCount<3) 
			return 0; 
		s=vSet[0].y*(vSet[vCount-1].x-vSet[1].x); 
		for (i=1;i<vCount;i++) 
			s+=vSet[i].y*(vSet[(i-1)].x-vSet[(i+1)%vCount].x); 
		return s/2; 
	}
	
	/**
	 * [判断多边形顶点的排列方向(方法一)]
	 * @param vcount
	 * @param polygon
	 * @return	逆时针方向,返回ture
	 */
	boolean isconterclock() 
	{ 
		return areaOfPolygon()>0; 
	} 
	
	
//	/**
//	 * 6. 判断多边形顶点的排列方向(方法二)
//	 * @param vcount
//	 * @param polygon
//	 * @return	逆时针方向,返回ture
//	 */
//	boolean isccwize() 
//	{ 
//		int i,index; 
//		POINT a,b,v; 
//		v=vSet[0]; 
//		index=0; 
//		for(i=1;i<vCount;i++) // 找到最低且最左顶点，肯定是凸顶点 
//		{ 
//			if(vSet[i].y<v.y||vSet[i].y == v.y && vSet[i].x<v.x) 
//			{ 
//				index=i; 
//			} 
//		} 
//		a=vSet[(index-1+vCount)%vCount]; // 顶点v的前一顶点 
//		b=vSet[(index+1)%vCount]; // 顶点v的后一顶点 
//		return Utility.multiply(v,b,a)>0; 
//	} 
	
	/**
	 * 7. [射线法判断点q与多边形polygon的位置关系]
	 * 要求:①polygon为简单多边形②多边形顶点逆时针排列  
	 * @param q
	 * @return	 返回0(点在多边形内 );  
	 * 			返回1(点在多边形边上);  
	 * 			返回2(点在多边形外);
	 */
	int insidepolygon(POINT q) {
		int c = 0, i, n;
		LINESEG l1 = new LINESEG();
		LINESEG l2 = new LINESEG();
		// boolean bintersect_a,bonline1,bonline2,bonline3;
		// double r1,r2;

		l1.s = q;
		POINT end = new POINT(Utility.INF,q.y);
		l1.e = end;
		n = this.vCount;
		for (i = 0; i < this.vCount; i++) {
			l2.s = this.vSet[i];
			l2.e = this.vSet[(i + 1) % n];
			if (Utility.online(l2, q))
				return 1; // 如果点在边上，返回1
			if ((Utility.intersect_A(l1, l2))
					|| // 相交且不在端点
					((Utility.online(l1, this.vSet[(i + 1) % n])) && // 第二个端点在射线上
					((!(Utility.online(l1, this.vSet[(i + 2) % n])))
							&& /* 前一个端点和后一个端点在射线两侧 */
							((Utility.multiply(this.vSet[i], this.vSet[(i + 1)% n], l1.s) * Utility.multiply(this.vSet[(i + 1) % n], this.vSet[(i + 2)% n], l1.s)) > 0) 
							|| (Utility.online(l1, this.vSet[(i + 2) % n]))
							&& /* 下一条边是水平线，前一个端点和后一个端点在射线两侧 */
							((Utility.multiply(this.vSet[i], this.vSet[(i + 2)% n], l1.s) * Utility.multiply(this.vSet[(i + 2) % n], this.vSet[(i + 3)% n], l1.s)) > 0))))
				c++;
		}
		if (c % 2 == 1)
			return 0;
		else
			return 2;
	}
	
	/**
	 * [判断点是否在凸多边形内或上]
	 * 要求:多边形polygon一定要是凸多边形 
	 * @param q
	 * @return	点q是凸多边形polygon内或凸多边形上时，返回true;否则返回false
	 */
	boolean InsideConvexPolygon(POINT q) { 
		POINT p= new POINT(); 
		LINESEG l = new LINESEG(); 
		int i; 
		p.x=0;p.y=0; 
		for(i=0;i<this.vCount;i++) // 寻找一个肯定在多边形polygon内的点p：多边形顶点平均值 
		{ 
			p.x+=this.vSet[i].x; 
			p.y+=this.vSet[i].y; 
		} 
		p.x /= this.vCount; 
		p.y /= this.vCount; 

		for(i=0;i<this.vCount;i++) 
		{ 
			l.s=this.vSet[i];l.e=this.vSet[(i+1)%this.vCount]; 
			if(Utility.multiply(p,l.e,l.s)*Utility.multiply(q,l.e,l.s)<0) /* 点p和点q在边l的两侧，说明点q肯定在多边形外 */ 
			break; 
		} 
		return (i==this.vCount); 
	} 
	
	
	/*********************************************************************************************  
	判断线段是否在简单多边形内(注意：如果多边形是凸多边形，下面的算法可以化简) 
   	必要条件一：线段的两个端点都在多边形内(如果是凸多边形,只此即可)； 
	必要条件二：线段和多边形的所有边都不内交； 
	用途：	1. 判断折线是否在简单多边形内 
			2. 判断简单多边形是否在另一个简单多边形内 
**********************************************************************************************/ 
	/**
	 * [判断线段是否在简单多边形内]
	 * 注意:线段若有端点在简单多边形边上,则认为其不在多边形内
	 * @param l
	 * @return 如果在内部,返回true
	 */
	boolean LinesegInsidePolygon(LINESEG l) 
	{ 
		// 判断线端l的端点是否不都在多边形内 
		if(this.insidepolygon(l.s)>0||this.insidepolygon(l.e)>0) 
			return false; 
		int top=0,i,j; 
		POINT []PointSet = new POINT[Utility.MAXV]; 
		POINT tmp = new POINT();
		LINESEG s = new LINESEG(); 

		for(i=0;i<this.vCount;i++) 
		{ 
			s.s=this.vSet[i]; 
			s.e=this.vSet[(i+1)%this.vCount]; 
			if(Utility.online(s,l.s)) //线段l的起始端点在线段s上 
				PointSet[top++]=l.s; 
			else if(Utility.online(s,l.e)) //线段l的终止端点在线段s上 
				PointSet[top++]=l.e; 
			else 
			{ 
				if(Utility.online(l,s.s)) //线段s的起始端点在线段l上 
					PointSet[top++]=s.s; 
				else if(Utility.online(l,s.e)) // 线段s的终止端点在线段l上 
					PointSet[top++]=s.e; 
				else 
				{ 
					if(Utility.intersect(l,s)) // 这个时候如果相交，肯定是内交，返回false 
					return false; 
				} 
			} 
		} 

		for(i=0;i<top-1;i++) /* 冒泡排序，x坐标小的排在前面；x坐标相同者，y坐标小的排在前面 */ 
		{ 
			for(j=i+1;j<top;j++) 
			{ 
				if( PointSet[i].x>PointSet[j].x || Math.abs(PointSet[i].x-PointSet[j].x)<Utility.EP && PointSet[i].y>PointSet[j].y ) 
				{ 
					tmp=PointSet[i]; 
					PointSet[i]=PointSet[j]; 
					PointSet[j]=tmp; 
				} 
			} 
		} 

		for(i=0;i<top-1;i++) 
		{ 
			tmp.x=(PointSet[i].x+PointSet[i+1].x)/2; //得到两个相邻交点的中点 
			tmp.y=(PointSet[i].y+PointSet[i+1].y)/2; 
			if(this.insidepolygon(tmp)>0) 
				return false; 
		} 
		return true; 
	} 
	

//	Polygon setSum(Polygon a){
//		Polygon c = new Polygon();
//		int ea=1,eb=1,vc=0;
//		double ang,offset=0;
////		while(a.angle[ea]<=this.angle[1] || a.angle[ea-1]>=this.angle[1]){
////			ea++;
////		}
//		while(a.angle[ea]<=this.angle[1]){
//			ea++;
//		}
//		c.vSet[0] = new POINT();
//		c.vSet[0].x = a.vSet[ea-1].x + this.vSet[0].x;
//		c.vSet[0].y = a.vSet[ea-1].y + this.vSet[0].y;
//		
//		while(eb<=this.vCount){
//			vc++;
//			ang = offset+a.angle[ea];
//			if(ang<=this.angle[eb]){
//				if(ea>=a.vCount){
////					offset=2*Utility.PI;
//					offset=360;
//					ea=1;
//				}
//				else
//					ea++;
//			}
//			if(ang>=this.angle[eb]){
//				eb++;
//			}	
//			c.vSet[vc] = new POINT();
//			c.vSet[vc].x = a.vSet[ea-1].x + this.vSet[eb-1].x;
//			c.vSet[vc].y = a.vSet[ea-1].y + this.vSet[eb-1].y;
//		}
//		c.vCount=vc+1;
//		c.vSet[c.vCount] = new POINT();
//		c.vSet[c.vCount].x = c.vSet[0].x;
//		c.vSet[c.vCount].y = c.vSet[0].y;	
//		return c;
//	}
	
//	/**
//	 * [计算当前的凸多边形与凸多边形a的和(当前的凸多边形作为b)]
//	 * @param a 
//	 * @return	 返回当前凸多边形与凸多边形a的和——凸多边形c
//	 */
//	MyPolygon setSum(MyPolygon a){
//		//下面计算x轴正方向按逆时针方向到当前凸多边形和多边形a的各边(有方向)的夹角,分别记录在angle[]数组中
//		this.calcAngle();
//		a.calcAngle();
//		MyPolygon c = new MyPolygon();
//		int ea=0,eb=0,vc=0;
//		double ang,offset=0;
//		while(a.angle[ea]<this.angle[0]){   //原算法中此处是"<=",但是在实际计算过程中，当开始时，如果a.angle[0]==this.angle[0]，则会出现计算错误
//			ea++;
//		}
//		c.vSet[0] = new POINT();
//		c.vSet[0].x = a.vSet[ea].x + this.vSet[0].x;
//		c.vSet[0].y = a.vSet[ea].y + this.vSet[0].y;
//		int start = ea;
//		
//		while(eb<this.vCount){
//			vc++;
//			ang = offset+a.angle[ea];
//			if(ang<=this.angle[eb]){
//				if(ea>=(a.vCount-1)){
//					offset=2*Utility.PI;
//					ea=0;
//				}
//				else
//					ea++;
//			}
//			if(ang>=this.angle[eb]){
//				eb++;
//			}
//			if(eb==this.vCount && ea==start){
//				vc--;
//				break;
//			}			
//			c.vSet[vc] = new POINT();
//			c.vSet[vc].x = a.vSet[ea].x + this.vSet[eb].x;
//			c.vSet[vc].y = a.vSet[ea].y + this.vSet[eb].y;
//		}
//		c.vCount=vc+1;
//		c.vSet[c.vCount] = new POINT();
//		c.vSet[c.vCount].x = c.vSet[0].x;
//		c.vSet[c.vCount].y = c.vSet[0].y;
//		//由于原始的算法是要求this和a的相交点在原点的,而现在this的引用点却是this.vSet[0].这样的话,要移动到原点,则需this和a都减去this.vSet[0]
//		//但这样算得结果后,需要再在结果上加上this.vSet[0],以匹配this.故这里直接先算得结果,在减去一次this.vSet[0]
//		for(int i=0;i<=c.vCount;i++){
//			c.vSet[i].x=c.vSet[i].x-this.vSet[0].x;
//			c.vSet[i].y=c.vSet[i].y-this.vSet[0].y;
//		}
//		
//		c.format();
//		return c;
//	}
	
	/**
	 * [计算当前的凸多边形与凸多边形a的和(当前的凸多边形作为b)]
	 * @param a 
	 * @return	 返回当前凸多边形与凸多边形a的和——凸多边形c
	 */
	MyPolygon setSum_new(MyPolygon a){
		//下面计算x轴正方向按逆时针方向到当前凸多边形和多边形a的各边(有方向)的夹角,分别记录在angle[]数组中
		this.calcAngle();
		a.calcAngle();
		MyPolygon c = new MyPolygon();
		int ea=0,eb=0,vc=0;
		double ang_a,ang_b,offset_a=0,offset_b =0;
		while(a.angle[ea]<this.angle[0]){   //原算法中此处是"<=",但是在实际计算过程中，当开始时，如果a.angle[0]==this.angle[0]，则会出现计算错误
			ea++;
		}
		c.vSet[0] = new POINT();
		c.vSet[0].x = a.vSet[ea].x + this.vSet[0].x;
		c.vSet[0].y = a.vSet[ea].y + this.vSet[0].y;
		int start = ea;
		
		for(;;){
			vc++;
			ang_a = offset_a+a.angle[ea];
			ang_b = offset_b+this.angle[eb];
			if(ang_a<=ang_b){
				if(ea>=(a.vCount-1)){
					offset_a=2*Utility.PI;
					ea=0;
				}
				else
					ea++;
			}
			if(ang_a>=ang_b){
				eb++;
				if(eb == this.vCount){
					offset_b=2*Utility.PI;
					eb =0;
				}
			}
			if(eb==0 && ea==start){
//				vc--;
				break;
			}			
			c.vSet[vc] = new POINT();
			c.vSet[vc].x = a.vSet[ea].x + this.vSet[eb].x;
			c.vSet[vc].y = a.vSet[ea].y + this.vSet[eb].y;
		}
		c.vCount=vc;
		c.vSet[c.vCount] = new POINT();
		c.vSet[c.vCount].x = c.vSet[0].x;
		c.vSet[c.vCount].y = c.vSet[0].y;
		//由于原始的算法是要求this和a的相交点在原点的,而现在this的引用点却是this.vSet[0].这样的话,要移动到原点,则需this和a都减去this.vSet[0]
		//但这样算得结果后,需要再在结果上加上this.vSet[0],以匹配this.故这里直接先算得结果,在减去一次this.vSet[0]
		for(int i=0;i<=c.vCount;i++){
			c.vSet[i].x=c.vSet[i].x-this.vSet[0].x;
			c.vSet[i].y=c.vSet[i].y-this.vSet[0].y;
		}		
		c.format();
		return c;
	}
	
	/**
	 * [求当前凸多边形相对于(Relative To)凸多边形B的障碍空间]
	 * @param B
	 * @return
	 */
	MyPolygon obstacleSpaceRTB(MyPolygon B){
		MyPolygon A1 = new MyPolygon(this,B.vSet[0]);
		for(int i=0;i<A1.vCount;i++){
			A1.vSet[i].x = 2*A1.vSet[0].x-A1.vSet[i].x;
			A1.vSet[i].y = 2*A1.vSet[0].y-A1.vSet[i].y;
		}
		A1.format();//A1标准化(注意:A1无需check了,因为A1是重this而来,this已检查过时凸多边形了,平移反转变化后还是凸多边形)
		MyPolygon C = B.setSum_new(A1);
		return C;
	}
	
	/**
	 * [求当前凸多边形相对于平台W的场内空间]
	 * @param W	平台(在这里,假设W的起点位于坐标原点,长和x轴正方向的部分重叠)
	 * @return	返回的凸多边形是一个矩形
	 */
	MyPolygon insideSpaceRTW(Yard W){
		//先计算出当前凸多边形最左/右点的x坐标值,最上点的y坐标值(对于format的凸多边形,最下的值即为引用点的y坐标值)
		double left,right,top,bottom;
		bottom=top=this.vSet[0].y;//bottom已计算完毕
		left=right=this.vSet[0].x;
		for(int i=1;i<this.vCount;i++){
			if(this.vSet[i].x<left)
				left=this.vSet[i].x;
			if(this.vSet[i].x>right)
				right=this.vSet[i].x;
			if(this.vSet[i].y>top)
				top=this.vSet[i].y;
		}
		
		//由于平台为矩形,故场内空间必定也为矩形,下面计算场内空间坐标
		POINT []tempSpace = new POINT[4];
		for(int i=0;i<tempSpace.length;i++)
			tempSpace[i]=new POINT();
		//计算左下角顶点
		tempSpace[0].x = this.vSet[0].x-(left-0);
		tempSpace[0].y = this.vSet[0].y-(bottom-0);
		//计算右下角顶点
		tempSpace[1].x = this.vSet[0].x-(right-W.length);
		tempSpace[1].y = this.vSet[0].y-(bottom-0);
		//计算右上角顶点
		tempSpace[2].x = this.vSet[0].x-(right-W.length);
		tempSpace[2].y = this.vSet[0].y-(top-W.width);
		//计算左上角顶点
		tempSpace[3].x = this.vSet[0].x-(left-0);
		tempSpace[3].y = this.vSet[0].y-(top-W.width);
		
		MyPolygon insideSpace = new MyPolygon(4,tempSpace);
		insideSpace.format();
		return insideSpace;
	}
	
	/**
	 * 计算与凸多边形B的交点
	 * @param B
	 * @return 
	 */
	ArrayList<POINT> intersectionPoint(MyPolygon B){
		ArrayList<POINT> points = new ArrayList<POINT>();
		for(int i=0;i<this.vCount;i++){
			LINESEG la = new LINESEG(this.vSet[i],this.vSet[i+1]);
			for(int j=0;j<B.vCount;j++){
				LINESEG lb = new LINESEG(B.vSet[j],B.vSet[j+1]);
				POINT p = Utility.intersection(la,lb);
				if(p!=null)
					points.add(p);
			}
		}
		return points;
	}
	
	/**
	 * 计算当前凸多边形各顶点中到点p的最大距离
	 * @param p
	 * @return
	 */
	double maxDisTo(POINT p){
		double dis = 0;
		double temp;
		for(int i=0;i<this.vCount;i++){
			temp = Utility.dist(this.vSet[i],p);
			if(temp>dis)
				dis=temp;
		}
		return dis;
	}
	
	/**
	 * 计算当前凸多边形各定点到矩形rectangle最邻近边的平均距离
	 * @param rectangle
	 * @return
	 */
	public double AvgDisToAdjEdge(MyPolygon rectangle) {
		double minAvgDis=Utility.dist(rectangle.vSet[0],rectangle.vSet[2]);//初始化为矩形的对角线长度
		for(int i=0;i<rectangle.vCount;i++){
			LINESEG l = new LINESEG(rectangle.vSet[i],rectangle.vSet[i+1]);
			double dis=0;
			for(int j=0;j<this.vCount;j++){
				dis = dis+Utility.ptoldist(this.vSet[j], l);
			}
			dis = dis/this.vCount;
			if(dis < minAvgDis){
				minAvgDis = dis;
			}
		}
		return minAvgDis;
	}
	
	/**
	 * 以引用点为中心，旋转当前凸多边形angle(角度，非弧度)角度，返回形成的新的凸多边形
	 * @param angle
	 * @return
	 */
	MyPolygon rotate(int angle){
		double newAngle = ((double)angle/180)*Utility.PI;
		MyPolygon newP = new MyPolygon(this,this.vSet[0]);
		for(int i=1;i<newP.vCount;i++){
			POINT p = Utility.rotate(newP.vSet[0], newAngle, newP.vSet[i]);
			newP.vSet[i].setPoint(p);
		}
		newP.format();
		return newP;		
	}
	
	/**
	 * 移动当前凸多边形，使其在新的位置的引用点坐标为p
	 * @param p
	 */
	void moveTo(POINT p){
		POINT vector = new POINT();
		vector.x = this.vSet[0].x-p.x;
		vector.y = this.vSet[0].y-p.y;
		for(int i=0;i<=this.vCount;i++){
			this.vSet[i].x = this.vSet[i].x-vector.x;
			this.vSet[i].y = this.vSet[i].y-vector.y;
		}
	}
	
	/**
	 * 计算当前凸多边形的顶点序列中包含在多边形Q内部的顶点 和多边形P、Q的交点组成的点序列
	 * @param Q
	 * @return
	 */
	POINT[] thisInQ(MyPolygon Q){
		POINT[] this_Q = new POINT[20];
//		for(int i=0;i<this_Q.length;i++)
//			this_Q[i] = new POINT();
		int now =0;
		for(int i=0;i<this.vCount;i++){
			int num = 0;
			if(Q.insidepolygon(this.vSet[i])==0){
				this_Q[now]  = new POINT(this.vSet[i]);
				now++;
			}
			LINESEG l_P = new LINESEG(this.vSet[i],this.vSet[i+1]);
			for(int j=0;j<Q.vCount;j++){
				LINESEG l_Q = new LINESEG(Q.vSet[j],Q.vSet[j+1]);
				POINT p = Utility.intersection(l_P,l_Q);
				if(p!=null){
					int flag = 0;
					for(int h=0;h<now;h++){
						if(Utility.equal_point(this_Q[h], p)){
							flag = 1;//表示此点已经加入过了
							break;
						}
					}
					if(flag==0){
						this_Q[now]  = new POINT(p);
						now++;
						num++;
					}
				}
			}
			if(num>1){//此时说明线段l_P与Q有两个交点,则需刚加入的两个点到this.vSet[i]的距离从小到大
				if(Utility.dist(this.vSet[i],this_Q[now-1])<Utility.dist(this.vSet[i], this_Q[now-2])){
					POINT temp = new POINT(this_Q[now-1]);
					this_Q[now-1].setPoint(this_Q[now-2]);
					this_Q[now-2].setPoint(temp);
				}
			}
		}
		return this_Q;
	}
	
	/**
	 * 计算当前多边形的 Orthogonal  Circumscribed  Rectangle(正交包围矩形)
	 * @return
	 */
	MyPolygon calcOCR(){
		//先计算出当前多边形最左/右点的x坐标值,最上点的y坐标值(对于format的凸多边形,最下的值即为引用点的y坐标值)
		double left,right,top,bottom;
		bottom=top=this.vSet[0].y;//bottom已计算完毕
		left=right=this.vSet[0].x;
		for(int i=1;i<this.vCount;i++){
			if(this.vSet[i].x<left)
				left=this.vSet[i].x;
			if(this.vSet[i].x>right)
				right=this.vSet[i].x;
			if(this.vSet[i].y>top)
				top=this.vSet[i].y;
		}
		
		POINT []tempSpace = new POINT[4];
		for(int i=0;i<tempSpace.length;i++)
			tempSpace[i]=new POINT();
		//计算左下角顶点
		tempSpace[0].x = left;
		tempSpace[0].y = bottom;
		//计算右下角顶点
		tempSpace[1].x = right;
		tempSpace[1].y = bottom;
		//计算右上角顶点
		tempSpace[2].x = right;
		tempSpace[2].y = top;
		//计算左上角顶点
		tempSpace[3].x = left;
		tempSpace[3].y = top;
		
		MyPolygon OCR = new MyPolygon(4,tempSpace);
		OCR.format();
		return OCR;
	}

	//求凸多边形的重心 
	POINT gravityCenter() 
	{  
		double x,y,s,x0,y0,cs,k; 
		x=0;y=0;s=0; 
		for(int i=1;i<this.vCount-1;i++) 
		{ 
			x0=(this.vSet[0].x+this.vSet[i].x+this.vSet[i+1].x)/3; 
			y0=(this.vSet[0].y+this.vSet[i].y+this.vSet[i+1].y)/3; //求当前三角形的重心 
			cs=Utility.multiply(this.vSet[i],this.vSet[i+1],this.vSet[0])/2; 
			//三角形面积可以直接利用该公式求解 
			if(Math.abs(s)<1e-20) { 
				x=x0;y=y0;s+=cs;
				continue; 
			} 
			k=cs/s; //求面积比例 
			x=(x+k*x0)/(1+k); 
			y=(y+k*y0)/(1+k); 
			s += cs; 
		} 
		return new POINT(x,y);
	}

	public boolean isEqual(MyPolygon b) {
		this.format();
		b.format();
		if(this.vCount == b.vCount){
			for(int i=0;i<this.vCount;i++){
				if(!this.vSet[i].equ(b.vSet[i]))
					return false;
			}
			return true;
		}
		return false;
	} 
	
}
