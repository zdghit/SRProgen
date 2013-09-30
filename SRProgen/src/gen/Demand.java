package gen;

import java.io.*;

/**
 * 这个类的作用是描述所有的要求,如算例个数/随机数种子值/基文件中设置的所有值(对应于SampleInfo)
 * @author xung
 *
 */
public class Demand {
	BaseStruct base;//基文件数据读入后放入其中
	String bFilePath; //基文件文件路径(包括路径/文件名/扩展名)
	String bFileName;//基文件文件名(不含后缀)
	String dirStr; //基文件父目录
	String extStr;//基文件扩展名(包含".")
	String errFilePath;//生成过程中记录错误的文件
	int nrOfEx; //要生成的算例的个数
	int inval; //随机数种子值
	
	Demand(){
		this.base = new BaseStruct();
		this.nrOfEx = 10;
		this.inval = 0;
		this.bFilePath = "no";
	}

	public boolean getBaseData(String path){
	    try {
	    	File f = new File(path);
	    	path= f.getAbsolutePath();
	    	FileReader fr = new FileReader(path);
	  
	    	readFileToChar(fr,':'); base.nrOfPro=(int)readAParameter(fr);
	    	readFileToChar(fr,':'); base.minJob=(int)readAParameter(fr);
	    	readFileToChar(fr,':'); base.maxJob=(int)readAParameter(fr);
	    	readFileToChar(fr,':'); base.maxRel=(int)readAParameter(fr);
	    	readFileToChar(fr,':'); base.dueDateFac=readAParameter(fr);
	    	readFileToChar(fr,':'); base.minMode=(int)readAParameter(fr);
	    	readFileToChar(fr,':'); base.maxMode=(int)readAParameter(fr);
	    	readFileToChar(fr,':'); base.minDur=(int)readAParameter(fr);
	    	readFileToChar(fr,':'); base.maxDur=(int)readAParameter(fr);
	    	
	    	readFileToChar(fr,':'); base.minOutSour=(int)readAParameter(fr);
	    	readFileToChar(fr,':'); base.maxOutSour=(int)readAParameter(fr);
	    	readFileToChar(fr,':'); base.maxOut=(int)readAParameter(fr);
	    	readFileToChar(fr,':'); base.minInSink=(int)readAParameter(fr);
	    	readFileToChar(fr,':'); base.maxInSink=(int)readAParameter(fr);
	    	readFileToChar(fr,':'); base.maxIn=(int)readAParameter(fr);
	    	readFileToChar(fr,':'); base.compl=readAParameter(fr);
	    	
	    	readFileToChar(fr,':'); base.minRen=(int)readAParameter(fr);
	    	readFileToChar(fr,':'); base.maxRen=(int)readAParameter(fr);
	    	readFileToChar(fr,':'); base.minRReq=(int)readAParameter(fr);
	    	readFileToChar(fr,':'); base.maxRReq=(int)readAParameter(fr);
	    	readFileToChar(fr,':'); base.minRRU=(int)readAParameter(fr);
	    	readFileToChar(fr,':'); base.maxRRU=(int)readAParameter(fr);
	    	readFileToChar(fr,':'); base.RRF=readAParameter(fr);
	    	readFileToChar(fr,':'); base.RRS=readAParameter(fr);
	    	readFileToChar(fr,':'); base.nrOfRFunc=(int)readAParameter(fr);
	    	base.RFuncProb = new float[base.nrOfRFunc];
	    	for(int i=0;i<base.nrOfRFunc;i++){
	    		readFileToChar(fr,':');
	    		base.RFuncProb[i]=readAParameter(fr);
	    	}
	    	
	    	readFileToChar(fr,':'); base.minNon=(int)readAParameter(fr);
	    	readFileToChar(fr,':'); base.maxNon=(int)readAParameter(fr);
	    	readFileToChar(fr,':'); base.minNReq=(int)readAParameter(fr);
	    	readFileToChar(fr,':'); base.maxNReq=(int)readAParameter(fr);
	    	readFileToChar(fr,':'); base.minNRU=(int)readAParameter(fr);
	    	readFileToChar(fr,':'); base.maxNRU=(int)readAParameter(fr);
	    	readFileToChar(fr,':'); base.NRF=readAParameter(fr);
	    	readFileToChar(fr,':'); base.NRS=readAParameter(fr);
	    	readFileToChar(fr,':'); base.nrOfNFunc=(int)readAParameter(fr);
	    	base.NFuncProb = new float[base.nrOfNFunc];
	    	for(int i=0;i<base.nrOfNFunc;i++){
	    		readFileToChar(fr,':');
	    		base.NFuncProb[i]=readAParameter(fr);
	    	}
	    	
	    	readFileToChar(fr,':'); base.minDou=(int)readAParameter(fr);
	    	readFileToChar(fr,':'); base.maxDou=(int)readAParameter(fr);
	    	readFileToChar(fr,':'); base.minDReq=(int)readAParameter(fr);
	    	readFileToChar(fr,':'); base.maxDReq=(int)readAParameter(fr);
	    	readFileToChar(fr,':'); base.minDRU=(int)readAParameter(fr);
	    	readFileToChar(fr,':'); base.maxDRU=(int)readAParameter(fr);
	    	readFileToChar(fr,':'); base.DRF=readAParameter(fr);
	    	readFileToChar(fr,':'); base.DRST=readAParameter(fr);
	    	readFileToChar(fr,':'); base.DRSP=readAParameter(fr);
	    	readFileToChar(fr,':'); base.nrOfDFunc=(int)readAParameter(fr);
	    	base.DFuncProb = new float[base.nrOfDFunc];
	    	for(int i=0;i<base.nrOfDFunc;i++){
	    		readFileToChar(fr,':');
	    		base.DFuncProb[i]=readAParameter(fr);
	    	}
	    	
	    	if(Progen.includeSR){		//如果包含空间资源约束
	    		//任务组相关参数读入
		    	readFileToChar(fr,':'); 
		    	base.minNofTaskG=(int)readAParameter(fr);
		    	readFileToChar(fr,':'); base.maxNofTaskG=(int)readAParameter(fr);
		    	readFileToChar(fr,':'); base.minNofJobTask=(int)readAParameter(fr);
		    	readFileToChar(fr,':'); base.maxNofJobTask=(int)readAParameter(fr);
		    	readFileToChar(fr,':'); base.minNofQJob=(int)readAParameter(fr);
		    	readFileToChar(fr,':'); base.maxNofQJob=(int)readAParameter(fr);
		    	readFileToChar(fr,':'); base.minNofMJob=(int)readAParameter(fr);
		    	readFileToChar(fr,':'); base.maxNofMJob=(int)readAParameter(fr);
		    	
		    	//空间资源相关参数读入
		    	readFileToChar(fr,':'); base.nrOfSR=(int)readAParameter(fr);
		    	base.SRTypeAll = new SRType[base.nrOfSR];
		    	for(int i=0;i<base.nrOfSR;i++){
		    		base.SRTypeAll[i]=new SRType();
		    		base.SRTypeAll[i].name=readAStringParameter(fr,'(');
		    		base.SRTypeAll[i].dimension=Integer.parseInt(readAStringParameter(fr,','));
		    		String temp=readAStringParameter(fr,',');
		    		if(temp.equals("d")||temp.equals("D"))
		    			base.SRTypeAll[i].dividable=true;
		    		else
		    			base.SRTypeAll[i].dividable=false;
		    		temp=readAStringParameter(fr,')');
		    		if(temp.equals("o")||temp.equals("O"))
		    			base.SRTypeAll[i].orientation=true;
		    		else
		    			base.SRTypeAll[i].orientation=false;
		    		readFileToChar(fr,'\n');
		    	}
		    	for(int i=0;i<base.nrOfSR;i++){
		    		readFileToChar(fr,':'); base.SRTypeAll[i].minKind=(int)readAParameter(fr);
		    		readFileToChar(fr,':'); base.SRTypeAll[i].maxKind=(int)readAParameter(fr);
		    		readFileToChar(fr,'(');
		    		if(base.SRTypeAll[i].dimension==1){
		    			base.SRTypeAll[i].minDemand.x=Integer.parseInt(readAStringParameter(fr,')'));
		    			readFileToChar(fr,'(');
		    			base.SRTypeAll[i].maxDemand.x=Integer.parseInt(readAStringParameter(fr,')'));
		    		}
		    		else if(base.SRTypeAll[i].dimension==2){
		    			base.SRTypeAll[i].minDemand.x=Integer.parseInt(readAStringParameter(fr,','));
		    			base.SRTypeAll[i].minDemand.y=Integer.parseInt(readAStringParameter(fr,')'));
		    			readFileToChar(fr,'(');
		    			base.SRTypeAll[i].maxDemand.x=Integer.parseInt(readAStringParameter(fr,','));
		    			base.SRTypeAll[i].maxDemand.y=Integer.parseInt(readAStringParameter(fr,')'));
		    		}
		    		else if(base.SRTypeAll[i].dimension==3){
		    			base.SRTypeAll[i].minDemand.x=Integer.parseInt(readAStringParameter(fr,','));
		    			base.SRTypeAll[i].minDemand.y=Integer.parseInt(readAStringParameter(fr,','));
		    			base.SRTypeAll[i].minDemand.z=Integer.parseInt(readAStringParameter(fr,')'));
		    			readFileToChar(fr,'(');
		    			base.SRTypeAll[i].maxDemand.x=Integer.parseInt(readAStringParameter(fr,','));
		    			base.SRTypeAll[i].maxDemand.y=Integer.parseInt(readAStringParameter(fr,','));
		    			base.SRTypeAll[i].maxDemand.z=Integer.parseInt(readAStringParameter(fr,')'));
		    		}
		    		readFileToChar(fr,':'); base.SRTypeAll[i].minR=(int)readAParameter(fr);
		    		readFileToChar(fr,':'); base.SRTypeAll[i].maxR=(int)readAParameter(fr);
		    		readFileToChar(fr,':'); base.SRTypeAll[i].SRF=readAParameter(fr);
		    		readFileToChar(fr,':'); base.SRTypeAll[i].SRS=readAParameter(fr);
		    	}
	    	}
	    	
	    	
	    	readFileToChar(fr,':'); base.netTol=readAParameter(fr);
	    	readFileToChar(fr,':'); base.reqTol=readAParameter(fr);
	    	readFileToChar(fr,':'); base.maxTrials=(int)readAParameter(fr);

	        fr.close();
	        
	        //文件存在且已读入,下面获取文件名称/后缀等信息
	        this.bFilePath = path;
	        if(path.contains("\\")){
	        	this.bFileName = path.substring(path.lastIndexOf("\\")+1,path.lastIndexOf(".")); 
	        	this.dirStr = path.substring(0,path.lastIndexOf("\\"));
	        }
	        else {
	        	this.bFileName = path.substring(path.lastIndexOf("/")+1,path.lastIndexOf(".")); 
	        	this.dirStr = path.substring(0,path.lastIndexOf("/"));//不包括这个"/"
	        }
	        this.extStr = path.substring(path.lastIndexOf("."));//包括这个"."	        
	        this.errFilePath = this.dirStr+"/"+this.bFileName+"-log.txt";	  
	        
	        return true;
	        
	      } catch (FileNotFoundException e) {
	        System.out.println("找不到指定文件");
	        return false;
	      } catch (Exception e) {//因为文件读取过程中,不仅有IO异常,还可能有数字格式转换的异常,所以在此就捕获所有异常(文件未找到的异常先于此被捕获)
	        System.out.println("文件读取错误");
	        return false;
	      }
	}
	
	/**
	 * 从文件当前位置读到mark标记的符号处
	 * @param fr
	 * @param mark
	 * @throws IOException
	 */
	static void readFileToChar(FileReader fr,char mark) {
		try{
			int ch = fr.read();
			while (ch != mark){
				if(ch == -1){
					return;
				}
				ch =  fr.read();
				continue;	
			}
				
		}catch (Exception e) {//因为文件读取过程中,不仅有IO异常,还可能有数字格式转换的异常,所以在此就捕获所有异常(文件未找到的异常先于此被捕获)
	        System.out.println("文件读取错误");
		}
	}
	
/**读出一个参数,以浮点数形式返回(返回时已读到了'\r')
 * @param fr
 * @return 
 * @throws IOException
 */
	static float readAParameter(FileReader fr) throws Exception{
    	char c;
    	char[] buff = new char[20];
    	String s = null;
    	int flag =0;//flag为0表示还没读到真正的数据;为1表示正在读数据的过程中;
    	int i = 0;
		while (true) {
			c = (char) fr.read();
        	if((c!=' ')&&(c!='\t')&&flag==0){//找到了数据开始的位置
        		flag = 1;
        		buff[i++]=c;
        		continue;
        	}
        	if(((c==' ')||(c=='\t')||((char)c=='\r'))&&flag==1){ //在读数据过程中遇到空格或换行或回车,表示数据读取结束(windows系统下\r\n,Unix下\n,mac系统下\r表示一行结束)
//        		flag = 2;
        		s = String.valueOf(buff,0,i);
//        		i = 0;
        		if(c!='\r')
        			readFileToChar(fr,'\r');//保证读到'\n'结束
        		return Float.parseFloat(s);
        	}
        	if((c!=' ')&&flag==1){//在数据读取过程中遇到非空格的,就记入真正的数据中并继续读(此if不可与前一个if交换位置)
        		buff[i++]=c;
        		continue;
        	}      	
		}
	}	

	/**
	 * 此函数从当前游标开始读入有效字符串并返回(返回时已读到了字符end)
	 * @param fr
	 * @return
	 * @throws Exception
	 */
	static String readAStringParameter(FileReader fr,char end) throws Exception{
    	char c;
    	char[] buff = new char[20];
    	String s = null;
    	int flag =0;//flag为0表示还没读到真正的数据;为1表示正在读数据的过程中;
    	int i = 0;
		while (true) {
			c = (char) fr.read();
        	if((c!=' ')&&(c!='\t')&&(c!='\n')&&flag==0){//找到了数据开始的位置
        		flag = 1;
        		buff[i++]=c;
        		continue;
        	}
        	if(((c==' ')||(c=='\t')||((char)c==end))&&flag==1){ 
        		s = String.valueOf(buff,0,i);
        		if((char)c!=end)
        			readFileToChar(fr,end);//保证此函数调用完毕时,已经读到了end
        		return s;
        	}
        	if((c!=' ')&&flag==1){
        		buff[i++]=c;
        		continue;
        	}      	
		}
	}
}

class BaseStruct{
	int nrOfPro; 
	int minJob;
	int maxJob;
	int maxRel;
	float dueDateFac;
	int minMode;
	int maxMode;
	int minDur;
	int maxDur;
	
	int minOutSour;
	int maxOutSour;
	int maxOut;
	int minInSink;
	int maxInSink;
	int maxIn;
	float compl;
	
	int minRen;
	int maxRen;
	int minRReq;
	int maxRReq;
	int minRRU;
	int maxRRU;
	float RRF;
	float RRS;
	int nrOfRFunc;
	float[] RFuncProb;
	
	int minNon;
	int maxNon;
	int minNReq;
	int maxNReq;
	int minNRU;
	int maxNRU;
	float NRF;
	float NRS;
	int nrOfNFunc;
	float[] NFuncProb;
	
	int minDou;
	int maxDou;
	int minDReq;
	int maxDReq;
	int minDRU;
	int maxDRU;
	float DRF;
	float DRST;
	float DRSP;
	int nrOfDFunc;
	float[] DFuncProb;
	
	//任务组相关参数
	int minNofTaskG;//最小任务组数
	int maxNofTaskG;//最大任务组数
	
	//每个任务组相关参数
	int minNofJobTask;//每个任务组中最小活动(或称任务)数
	int maxNofJobTask;//每个任务组中最大活动(或称任务)数
	int minNofQJob;//每个任务组中最小请求性任务数
	int maxNofQJob;//每个任务组中最大请求性任务数
	int minNofMJob;//每个任务组中最小移动性任务数
	int maxNofMJob;//每个任务组中最大移动性任务数
	
	//空间资源相关参数
	int nrOfSR;//空间资源类型数
	SRType[] SRTypeAll;
	
	
	int maxTrials;
	float netTol;
	float reqTol;
		
}
