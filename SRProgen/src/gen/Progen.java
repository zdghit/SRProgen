package gen;

import java.util.Scanner;

public class Progen {
	
	public static int maxNrOfPro = 100;
	public static int maxNrOfJobs = 1000;
	
//	static int maxDu = 25;
//	static int maxReq = 200;最两个参数只是为了保证一个足够大的数,所以修改为定义一个MAX
	public static int MAX = 1000000;
	
	public static int maxR = 20;
	public static int maxD = 20;
	public static int maxN = 20;
	public static int maxRDN;
	public static int maxNrOfModes = 20;
	public static int maxF = 3;

//	static int maxRenSup =5000;
//	static int maxNonSup = 50000; 	
//	static int maxCost = 500; 新的方法已经不需要这样设置了
	public static int maxHorizon = 2000;
	
	//==================空间资源相关====================
	public static boolean includeSR = true; //是否包含空间资源约束
	public static int maxNrOfTaskGroup=100;
	public static int maxNrOfSRType=20;
	/**
	 * 最大的具体空间资源种数
	 */
	public static int maxNrOfSR=200;

	
	public Progen(){
		maxRDN = maxR;
		if(maxRDN<maxD) maxRDN = maxD;
		if(maxRDN<maxN) maxRDN = maxN; //maxRDN取三者中最大的那个
	}

	void genProj(Project p,Demand demand) {
		Utility utility = new Utility(demand.inval);
		String curExFileName; //用于表示当前的样例文件名(不含路径,包括后缀)
		
		//清空记录错误的同名文件
		utility.setPrintStream(demand.errFilePath,true);//假设存在记录错误的文件,则清空这个文件
		utility.reSetPrintStream();
		
		for(int i=1;i<=demand.nrOfEx;i++){		//实例编号从1开始
			curExFileName=demand.bFileName+"-"+i+".txt";
			utility.setPrintStream(demand.errFilePath,false);
			System.out.println("------------------------------------------------------------------------");
			System.out.println("sample file -->"+curExFileName);
			System.out.println("------------------------------------------------------------------------");
			utility.reSetPrintStream();
			p.genProjData(demand.base, utility);
			if(p.net.genNetWork(demand, utility)){//生成项目的网络,并且成功的话,则
				p.calcCPMTimes('d',0);//当第一个参数为'd'时,其实设置的模式是第0个模式(计算整个项目中每个任务的EST/EFT/LST及每个子项目的CPMT),第二个参数此时不起作用
				p.calcDueDates(demand.base);
				
				//资源需求生成过程细节还有些模糊
				p.reqGen.resReqMain(demand, utility);
				p.availGen.resAvlMain(demand);
				
				if(Progen.includeSR){
					//===================空间资源相关====================
					//当任务组中不是只有一个任务时，不产生凸多边形样式的2维空间资源需求
					if(demand.base.minNofJobTask!=1 && demand.base.maxNofJobTask!=1)
						Utility.genPolygon = false;
					
					p.calcCPMTimes('d',0);//当第一个参数为'd'时,其实设置的模式是第0个模式(计算整个项目中每个任务的EST/EFT/LST及每个子项目的CPMT),第二个参数此时不起作用
										  //此时要再次计算一次，是因为计算常规资源的可用量availGen.resAvlMain(demand)时，修改了每个任务的EST/EFT，此时任务的模式不一定是第0模式。
					                      //而空间资源时，认为只有这一种执行模式，故要重新设置各任务执行模式为0，并计算此时的EST/EFT，以用来计算任务组的EST/EFT(及凸多边形时任务的最早/最迟开始时间)
		
					//生成任务组信息
					p.taskGroupGen.genTaskGroupMain(demand, utility);	
					//生成空间资源需求
					p.srReqgen.SRresReqMain(demand, utility);
					//生成空间资源可用量信息
					//2013.5.23目前看到这
					p.srAvailAndOGen.SRresAvlAndOMain(demand, utility);
				}
				
				p.writeToFile(demand,utility,demand.dirStr+"/"+curExFileName);
			}
			System.out.println("Generating case "+i+" has done.");
		}
	}
	
	/**
	 * 用于控制台版的界面显示
	 * @param demand
	 */
	 void showMenue(Demand demand){
		System.out.println("=====================================================");
		System.out.println("PROGEN2.0 - Generator For Project Scheduling Problems");
		System.out.println("=====================================================");
		System.out.println("");
		System.out.println("file basedata:"+demand.bFilePath);
		if (demand.inval==0)
			System.out.println("initial valuse:randomly");
		else
			System.out.println("initial value:"+demand.inval);
		System.out.println("number of instances:"+demand.nrOfEx);
		System.out.println("");
		System.out.println("1-basedata");
		System.out.println("2-initial value");
		System.out.println("3-number of instances");
		System.out.println("4-generate");
		System.out.println("5-end program");
		System.out.print("-->");
	}
	 
	/**
	 * 控制台版的主程序
	 * @param argv
	 * @throws Exception
	 */
	public static void main(String argv[]) throws Exception {
//		int rr;
//		//测试d多边形生成
//		for(int k=0;k<500;k++){
//			Polygon  p = Utility.genPolygonK(6, 6, 6);
//			rr=p.check();
//			if(rr>0){
//				System.out.println("出错");
//			}
//		}	
//		System.out.println("end");
		

		Progen progen = new Progen();
		Project p = new Project();
		Demand demand = new Demand();// 不同时候(又选择另一个基文件时),需求demand是不同的,需要重新生成一个对象(因为其中有数组元素个数不定)[此时还没考虑这种情况,需修正]
		int taste = 0;
		String basePath;
		boolean readBaseFSuccess = false;
		Scanner scanner = new Scanner(System.in);
		while (taste != 5) {
			progen.showMenue(demand);
			taste = scanner.nextInt();
			switch (taste) {
			case 1:
				System.out.print("baseFile path:");
				basePath = scanner.next();// Scanner会自动把'\'前面添加转义符,所以不会出现错误
				readBaseFSuccess = demand.getBaseData(basePath);
				break;
			case 2:
				System.out.print("initial value:");
				demand.inval = scanner.nextInt();
				break;
			case 3:
				System.out.print("number of instances:");
				demand.nrOfEx = scanner.nextInt();
				break;
			case 4:
				if (demand.bFilePath.equals("no")) {
					System.out.print("baseFile path:");
					basePath = scanner.next();// Scanner会自动把'\'前面添加转义符,所以不会出现错误
					readBaseFSuccess = demand.getBaseData(basePath);
				}
				if (readBaseFSuccess)
					progen.genProj(p, demand);
				// default:{
				// readBaseFSuccess=demand.getBaseData("d:/2.bas");
				// if(readBaseFSuccess)
				// progen.genProj(p,demand);
				// }
			}
		}
		// System.out.println("exit.");
	}
	
	/**
	 * 主控函数(用于界面版)
	 * @param baseFilePath
	 * @param randomSeed
	 * @param nrOfEx
	 */
	public void gen(String baseFilePath,int randomSeed,int nrOfEx){
		Project p = new Project();
		Demand demand = new Demand();
		demand.inval=randomSeed;
		demand.nrOfEx=nrOfEx;
		if(demand.getBaseData(baseFilePath)){
			this.genProj(p,demand);		
		}			
	}
	
}
