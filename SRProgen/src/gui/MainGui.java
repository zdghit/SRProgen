package gui;


import gen.Progen;
import gen.ShowNet;
import gen.Utility;

import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.events.*;

public class MainGui {
	
	public MainGui() {
		final Display display = Display.getDefault();
		final Shell shell = new Shell(display,SWT.DIALOG_TRIM|SWT.MIN);
		shell.setSize(600, 650);
		shell.setText("SRProgen");
		shell.setLocation(300, 200);
		final TabFolder tabFolder = new TabFolder(shell, SWT.NONE);
		FillLayout fillLayout = new FillLayout();
		shell.setLayout(fillLayout);

		final TabItem item2 = new TabItem(tabFolder, SWT.NONE);
		item2.setText("Generate cases");
		{
			Composite comp2 = new Composite(tabFolder, SWT.BORDER);
			item2.setControl(comp2);
			
			Group group1 = new Group(comp2, SWT.NONE);
			group1.setBounds(0, 0, 580, 108);
			Label baseFileL = new Label(group1, SWT.NONE);
			baseFileL.setBounds(21, 23, 52, 14);
			baseFileL.setText("Base file:"); 
			
			final Text baseFileT = new Text(group1, SWT.BORDER);
			baseFileT.setBounds(96, 19, 178, 23);
			
			Button button = new Button(group1, SWT.NONE);
			button.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					FileDialog OpenFileDialog = new FileDialog(shell,SWT.OPEN);
					OpenFileDialog.setText("Choose base file");
					OpenFileDialog.setFilterExtensions(new String[] {"*.bas", "*.*"});
					OpenFileDialog.setFilterNames(new String[] { "The format of base file(*.bas)","All types(*.*)"});
					OpenFileDialog.setFilterPath("D:\\");
					OpenFileDialog.open();
					baseFileT.setText(OpenFileDialog.getFilterPath()+"\\"+OpenFileDialog.getFileName());
				}
			});
			button.setBounds(286, 18, 69, 24);
			button.setText("Scan");
			
			Button showBaseFileB = new Button(group1, SWT.NONE);
			showBaseFileB.setBounds(360, 18, 95, 24);
			showBaseFileB.setText("Check base file");
			
			Label randomSeedL = new Label(group1, SWT.NONE);
			randomSeedL.setBounds(12, 77, 95, 14);
			randomSeedL.setText("Random seeds:");
			
			final Text randomSeedT = new Text(group1, SWT.BORDER);
			randomSeedT.setBounds(110, 71, 40, 20);
			randomSeedT.setText("0");
			
			Label nrOfExL = new Label(group1, SWT.NONE);
			nrOfExL.setBounds(160, 77, 90, 14);
			nrOfExL.setText("Cases' number:");
			
			final Text nrOfExT = new Text(group1, SWT.BORDER);
			nrOfExT.setBounds(260, 71, 40, 20);
			nrOfExT.setText("10");
			
			Button genB = new Button(group1, SWT.NONE);
			genB.setBounds(310, 67, 100, 24);
			genB.setText("Generate cases");
			
			Button config = new Button(group1, SWT.NONE);
			config.setBounds(460, 18, 120, 24);
			config.setText("Parameter settings");
			
			Button showlogB = new Button(group1, SWT.NONE);
			showlogB.setBounds(500, 67, 80, 24);
			showlogB.setText("Check logs");
			
			Button showResultFB = new Button(group1, SWT.NONE);
			showResultFB.setText("Check cases");
			showResultFB.setBounds(415, 67, 80, 24);
			
			Group group2 = new Group(comp2, SWT.NONE);
			group2.setBounds(0, 114, 580, 473);
			group2.setText("Operating records");
			final Text show = new Text(group2, SWT.BORDER | SWT.READ_ONLY | SWT.WRAP | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CANCEL | SWT.MULTI);
			show.setBounds(10, 22, 560, 441);
			GUIPrintStream guiPrintStream = new GUIPrintStream(System.out, show);
			System.setErr(guiPrintStream);
			System.setOut(guiPrintStream);	
			
			//参数设置按钮的监听
			config.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					final Shell configShell = new Shell(shell,SWT.DIALOG_TRIM|SWT.MIN|SWT.ON_TOP);
					configShell.setText("Set operating parameters");
					configShell.setSize(550, 550);
					configShell.setLocation(400, 250);
					
					Group group1 = new Group(configShell, SWT.NONE);
					group1.setBounds(5, 0, 533, 152);
						
					Label maxNrOfProL = new Label(group1, SWT.NONE);
					maxNrOfProL.setBounds(10, 31, 190, 20);
					maxNrOfProL.setText("The max number of subproject:");		
					final Text maxNrOfProT = new Text(group1, SWT.BORDER);
					maxNrOfProT.setBounds(205, 25, 45, 20);					
					maxNrOfProT.setText(new Integer(Progen.maxNrOfPro).toString());
					
					Label maxNrOfJobsL = new Label(group1, SWT.NONE);
					maxNrOfJobsL.setBounds(260, 31, 150, 20);
					maxNrOfJobsL.setText("The max number of task:");	
					final Text maxNrOfJobsT = new Text(group1, SWT.BORDER);
					maxNrOfJobsT.setBounds(415, 25, 45, 20);
					maxNrOfJobsT.setText(new Integer(Progen.maxNrOfJobs).toString());
					
					Label maxHorizonL = new Label(group1, SWT.NONE);
					maxHorizonL.setBounds(10, 61, 170, 20);
					maxHorizonL.setText("The max duration of project:");		
					final Text maxHorizonT = new Text(group1, SWT.BORDER);
					maxHorizonT.setBounds(185, 55, 45, 20);
					maxHorizonT.setText(new Integer(Progen.maxHorizon).toString());
					
					Label maxRL = new Label(group1, SWT.NONE);
					maxRL.setBounds(240, 61, 230, 20);
					maxRL.setText("The max kind of renewable resources:");		
					final Text maxRT = new Text(group1, SWT.BORDER);
					maxRT.setBounds(475, 55, 45, 20);
					maxRT.setText(new Integer(Progen.maxR).toString());
					
					Label maxDL = new Label(group1, SWT.NONE);
					maxDL.setBounds(10, 91, 190, 20);
					maxDL.setText("The max kind of dual resources:");		
					final Text maxDT = new Text(group1, SWT.BORDER);
					maxDT.setBounds(205, 85, 25, 20);
					maxDT.setText(new Integer(Progen.maxD).toString());
					
					Label maxNL = new Label(group1, SWT.NONE);
					maxNL.setBounds(240, 91, 250, 20);
					maxNL.setText("The max kind of non-renewable resources:");		
					final Text maxNT = new Text(group1, SWT.BORDER);
					maxNT.setBounds(495, 85, 25, 20);
					maxNT.setText(new Integer(Progen.maxN).toString());
					
					Label maxNrOfModesL = new Label(group1, SWT.NONE);
					maxNrOfModesL.setBounds(10, 121, 190, 20);
					maxNrOfModesL.setText("The max number of task modes:");	
					final Text maxNrOfModesT = new Text(group1, SWT.BORDER);
					maxNrOfModesT.setBounds(205, 115, 30, 20);
					maxNrOfModesT.setText(new Integer(Progen.maxNrOfModes).toString());
					
					Label maxFL = new Label(group1, SWT.NONE);
					maxFL.setBounds(245, 121, 230, 20);
					maxFL.setText("The max number of function relations:");		
					final Text maxFT = new Text(group1, SWT.BORDER);
					maxFT.setBounds(480, 115, 30, 20);
					maxFT.setText(new Integer(Progen.maxF).toString());
					
					
					Group group2 = new Group(configShell, SWT.NONE);
					group2.setBounds(5, 295, 533, 179);
					group2.setText("The resource parameters needed by generating convex polygon resources");
					
					Label rategenTriangleL = new Label(group2, SWT.NONE);
					rategenTriangleL.setBounds(10, 55, 225, 20);
					rategenTriangleL.setText("The probability of generating triangle:");		
					final Text rategenTriangleT = new Text(group2, SWT.BORDER);
					rategenTriangleT.setBounds(240, 49, 45, 20);
					rategenTriangleT.setText(Double.toString(Utility.rategenTriangle));
					
					Label rategenQuadrangleL = new Label(group2, SWT.NONE);
					rategenQuadrangleL.setBounds(295, 55, 150, 20);
					rategenQuadrangleL.setText("Generating quadrangle:");		
					final Text rategenQuadrangleT = new Text(group2, SWT.BORDER);
					rategenQuadrangleT.setBounds(450, 49, 45, 20);
					rategenQuadrangleT.setText(Double.toString(Utility.rategenQuadrangle));
					
					Label rategenPentagonL = new Label(group2, SWT.NONE);
					rategenPentagonL.setBounds(10, 85, 240, 20);
					rategenPentagonL.setText("The probability of generating pentagon:");		
					final Text rategenPentagonT = new Text(group2, SWT.BORDER);
					rategenPentagonT.setBounds(255, 79, 45, 20);
					rategenPentagonT.setText(Double.toString(Utility.rategenPentagon));
					
					Label rategenHexagonL = new Label(group2, SWT.NONE);
					rategenHexagonL.setBounds(10, 115, 240, 20);
					rategenHexagonL.setText("The probability of generating hexagon:");		
					final Text rategenHexagonT = new Text(group2, SWT.BORDER);
					rategenHexagonT.setBounds(255, 109, 45, 20);
					rategenHexagonT.setText(Double.toString(Utility.rategenHexagon));
					
					Label ratioAreaL = new Label(group2, SWT.NONE);
					ratioAreaL.setBounds(10, 145, 260, 20);
					ratioAreaL.setText("The min area rate with the original rectangle:");		
					final Text ratioAreaT = new Text(group2, SWT.BORDER);
					ratioAreaT.setBounds(275, 142, 30, 20);
					ratioAreaT.setText(Double.toString(Utility.ratioArea));
					
					
					Group group3 = new Group(group2, SWT.NONE);
					group3.setBounds(305, 75, 190, 110);
					group3.setText("When generating quadrangle");
					
					Label rateRectAngeleL = new Label(group3, SWT.NONE);
					rateRectAngeleL.setBounds(10, 20, 130, 20);
					rateRectAngeleL.setText("Generating rectangle:");		
					final Text rateRectAngeleT = new Text(group3, SWT.BORDER);
					rateRectAngeleT.setBounds(145, 20, 32, 20);
					rateRectAngeleT.setText(Double.toString(Utility.rateRectAngele));
					
					Label rateTrapeziumL = new Label(group3, SWT.NONE);
					rateTrapeziumL.setBounds(10, 45, 130, 20);
					rateTrapeziumL.setText("Generating trapezoid:");		
					final Text rateTrapeziumT = new Text(group3, SWT.BORDER);
					rateTrapeziumT.setBounds(145, 39, 32, 20);
					rateTrapeziumT.setText(Double.toString(Utility.rateTrapezium));
					
					Label rateParallelogramL = new Label(group3, SWT.NONE);
					rateParallelogramL.setBounds(10, 70, 130, 20);
					rateParallelogramL.setText("Generating rhomboid:");		
					final Text rateParallelogramT = new Text(group3, SWT.BORDER);
					rateParallelogramT.setBounds(145, 64, 32, 20);
					rateParallelogramT.setText(Double.toString(Utility.rateParallelogram));
															
					final Button isGenPolygonB = new Button(group2, SWT.CHECK);
					isGenPolygonB.setBounds(10, 25, 400, 16);
					isGenPolygonB.setText("The spatial resource needs of generating convex polygon");
					
					
					Group group4 = new Group(configShell, SWT.NONE);
					group4.setBounds(5, 166, 533, 116);
					group4.setText("The spatial resource-constrainted parameters");
					
					final Button SRB = new Button(group4, SWT.CHECK);
					SRB.setBounds(10, 25, 222, 16);
					SRB.setText("Inculde spatial resource constraints");
					
					Label maxNrOfSRTypeL = new Label(group4, SWT.NONE);
					maxNrOfSRTypeL.setBounds(10, 55, 190, 20);
					maxNrOfSRTypeL.setText("The max spatial resource types:");	
					final Text maxNrOfSRTypeT = new Text(group4, SWT.BORDER);
					maxNrOfSRTypeT.setBounds(205, 49, 45, 20);
					maxNrOfSRTypeT.setText(new Integer(Progen.maxNrOfSRType).toString());
							
					Label maxNrOfSRL = new Label(group4, SWT.NONE);
					maxNrOfSRL.setBounds(260, 55, 200, 20);
					maxNrOfSRL.setText("The max kind of specific resource:");	
					final Text maxNrOfSRT = new Text(group4, SWT.BORDER);
					maxNrOfSRT.setBounds(465, 49, 45, 20);
					maxNrOfSRT.setText(new Integer(Progen.maxNrOfSR).toString());
									
					Label maxNrOfTaskGroupL = new Label(group4, SWT.NONE);
					maxNrOfTaskGroupL.setBounds(10, 85, 210, 20);
					maxNrOfTaskGroupL.setText("The max number of activity group:");		
					final Text maxNrOfTaskGroupT = new Text(group4, SWT.BORDER);
					maxNrOfTaskGroupT.setBounds(225, 79, 45, 20);
					maxNrOfTaskGroupT.setText(new Integer(Progen.maxNrOfTaskGroup).toString());
					
					if(Progen.includeSR){
						SRB.setSelection(true);
						maxNrOfSRTypeT.setEditable(true);
						maxNrOfSRT.setEditable(true);
						maxNrOfTaskGroupT.setEditable(true);							
					}
					else{
						SRB.setSelection(false);
						maxNrOfSRTypeT.setEditable(false);
						maxNrOfSRT.setEditable(false);
						maxNrOfTaskGroupT.setEditable(false);
					}
					
					
					if(Utility.genPolygon){
						isGenPolygonB.setSelection(true);
						rategenTriangleT.setEditable(true);
						rategenQuadrangleT.setEditable(true);
						rategenPentagonT.setEditable(true);
						rategenHexagonT.setEditable(true);
						ratioAreaT.setEditable(true);
						 
						rateRectAngeleT.setEditable(true);
						rateTrapeziumT.setEditable(true);
						rateParallelogramT.setEditable(true);
					}
					else{
						isGenPolygonB.setSelection(false);
						rategenTriangleT.setEditable(false);
						rategenQuadrangleT.setEditable(false);
						rategenPentagonT.setEditable(false);
						rategenHexagonT.setEditable(false);
						ratioAreaT.setEditable(false);
						 
						rateRectAngeleT.setEditable(false);
						rateTrapeziumT.setEditable(false);
						rateParallelogramT.setEditable(false);
					}
					
					/**
					 * 是否包含空间资源约束选项的监控器
					 */
					SRB.addSelectionListener(new SelectionAdapter() {
						@Override
						public void widgetSelected(SelectionEvent e) {
							if(SRB.getSelection()){
								maxNrOfSRTypeT.setEditable(true);
								maxNrOfSRT.setEditable(true);
								maxNrOfTaskGroupT.setEditable(true);	
								Progen.includeSR = true;
								isGenPolygonB.setEnabled(true);
							}
							if(!SRB.getSelection()){
								maxNrOfSRTypeT.setEditable(false);
								maxNrOfSRT.setEditable(false);
								maxNrOfTaskGroupT.setEditable(false);
								Progen.includeSR = false;
								isGenPolygonB.setSelection(false);
								isGenPolygonB.setEnabled(false);
							}
						}
					});
					
					/**
					 * 是否产生凸多边形样式的二维空间资源需求的监控器
					 */
					isGenPolygonB.addSelectionListener(new SelectionAdapter() {
						@Override
						public void widgetSelected(SelectionEvent e) {
							if(isGenPolygonB.getSelection()){
								 rategenTriangleT.setEditable(true);
								 rategenQuadrangleT.setEditable(true);
								 rategenPentagonT.setEditable(true);
								 rategenHexagonT.setEditable(true);
								 ratioAreaT.setEditable(true);
								 
								 rateRectAngeleT.setEditable(true);
								 rateTrapeziumT.setEditable(true);
								 rateParallelogramT.setEditable(true);
								 
								 Utility.genPolygon = true;
								 
							}
							if(!isGenPolygonB.getSelection()){
								 rategenTriangleT.setEditable(false);
								 rategenQuadrangleT.setEditable(false);
								 rategenPentagonT.setEditable(false);
								 rategenHexagonT.setEditable(false);
								 ratioAreaT.setEditable(false);
								 
								 rateRectAngeleT.setEditable(false);
								 rateTrapeziumT.setEditable(false);
								 rateParallelogramT.setEditable(false);
								 
								 Utility.genPolygon = false;
							}
							 
							
						}
					});
					
					
					Button OKB = new Button(configShell, SWT.NONE);
					OKB.addSelectionListener(new SelectionAdapter() {
						@Override
						public void widgetSelected(SelectionEvent e) {
							String maxNrOfPro = maxNrOfProT.getText();
							String maxNrOfJobs = maxNrOfJobsT.getText();
							String maxHorizon = maxHorizonT.getText();
							String maxR = maxRT.getText();
							String maxD = maxDT.getText();
							String maxN = maxNT.getText();
							String maxNrOfModes = maxNrOfModesT.getText();
							String maxF = maxFT.getText();
							String maxNrOfSRType = maxNrOfSRTypeT.getText();
							String maxNrOfSR = maxNrOfSRT.getText();
							String maxNrOfTaskGroup = maxNrOfTaskGroupT.getText();
							
							String rategenTriangle = rategenTriangleT.getText();
							String rategenQuadrangle = rategenQuadrangleT.getText();
							String rategenPentagon = rategenPentagonT.getText();
							String rategenHexagon = rategenHexagonT.getText();
							String rateRectAngele = rateRectAngeleT.getText();
							String rateTrapezium  = rateTrapeziumT.getText();
							String rateParallelogram = rateParallelogramT.getText();
							String ratioArea = ratioAreaT.getText();
							
							if(isDigits(maxNrOfPro) && isDigits(maxNrOfJobs)&& isDigits(maxHorizon)&& isDigits(maxR)
									&& isDigits(maxD)&& isDigits(maxN)&& isDigits(maxNrOfModes)&& isDigits(maxF)&& 
									isDigits(maxNrOfSRType)&& isDigits(maxNrOfSR)&& isDigits(maxNrOfTaskGroup)
									&& isDigitsOrDot(rategenTriangle) && isDigitsOrDot(rategenQuadrangle)
									&& isDigitsOrDot(rategenPentagon) && isDigitsOrDot(rategenHexagon)
									&& isDigitsOrDot(rateRectAngele) && isDigitsOrDot(rateTrapezium)
									&& isDigitsOrDot(rateParallelogram) && isDigitsOrDot(ratioArea)){
								Progen.maxNrOfPro = Integer.parseInt(maxNrOfPro);
								Progen.maxNrOfJobs = Integer.parseInt(maxNrOfJobs);
								Progen.maxHorizon = Integer.parseInt(maxHorizon);
								Progen.maxR = Integer.parseInt(maxR);
								Progen.maxD = Integer.parseInt(maxD);
								Progen.maxN = Integer.parseInt(maxN);
								Progen.maxNrOfModes = Integer.parseInt(maxNrOfModes);
								Progen.maxF = Integer.parseInt(maxF);
								Progen.maxNrOfSRType = Integer.parseInt(maxNrOfSRType);
								Progen.maxNrOfSR = Integer.parseInt(maxNrOfSR);
								Progen.maxNrOfTaskGroup = Integer.parseInt(maxNrOfTaskGroup);
								
								Utility.rategenTriangle = Double.parseDouble(rategenTriangle);
								Utility.rategenQuadrangle = Double.parseDouble(rategenQuadrangle);
								Utility.rategenPentagon = Double.parseDouble(rategenPentagon);
								Utility.rategenHexagon = Double.parseDouble(rategenHexagon);
								Utility.rateRectAngele = Double.parseDouble(rateRectAngele);
								Utility.rateTrapezium = Double.parseDouble(rateTrapezium);
								Utility.rateParallelogram = Double.parseDouble(rateParallelogram);
								Utility.ratioArea = Double.parseDouble(ratioArea);
								
								configShell.dispose();
							}							
						}
					});
					OKB.setBounds(362, 480, 69, 24);
					OKB.setText("OK");
					
					Button defaultB = new Button(configShell, SWT.NONE);
					defaultB.addSelectionListener(new SelectionAdapter() {
						@Override
						public void widgetSelected(SelectionEvent e) {
							maxNrOfProT.setText("100");
							maxNrOfJobsT.setText("1000");
							maxHorizonT.setText("2000");
							maxRT.setText("20");
							maxDT.setText("20");
							maxNT.setText("20");
							maxNrOfModesT.setText("20");
							maxFT.setText("3");
							maxNrOfSRTypeT.setText("20");
							maxNrOfSRT.setText("200");
							maxNrOfTaskGroupT.setText("100");	
							

							rategenTriangleT.setText("0.2");
							rategenQuadrangleT.setText("0.3");
							rategenPentagonT.setText("0.3");
							rategenHexagonT.setText("0.2");
							rateRectAngeleT.setText("0.1");
							rateTrapeziumT.setText("0.1");
							rateParallelogramT.setText("0.1");
							ratioAreaT.setText("0.4");
							
						}
					});
					defaultB.setBounds(162, 480, 69, 24);
					defaultB.setText("Default");
					
					Button cancelB = new Button(configShell, SWT.NONE);
					cancelB.addSelectionListener(new SelectionAdapter() {
						@Override
						public void widgetSelected(SelectionEvent e) {
							configShell.dispose();
						}
					});
					cancelB.setBounds(262, 480, 69, 24);
					cancelB.setText("Cancel");
										
					configShell.open();
					configShell.layout();
				}
			});
			
			//生成算例按钮的监听
			genB.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					String baseFilePath = baseFileT.getText();
					String randomSeed = randomSeedT.getText();					
					String nrOfEx = nrOfExT.getText();//生成算例数	
					show.setText("");//清空show文本域内容
					if(baseFilePath!="" && isDigits(randomSeed) && isDigits(nrOfEx) ){
						baseFilePath=baseFilePath.replace('\\', '/');
						int randomSeedInt = Integer.parseInt(randomSeed);
						int nrOfExInt = Integer.parseInt(nrOfEx);
						if(randomSeedInt<0 || nrOfExInt<1 ) return;
						Progen progen = new Progen();
						progen.gen(baseFilePath, randomSeedInt, nrOfExInt);//生成算例
					}
				}
			});
			
			//显示基文件按钮的监听
			showBaseFileB.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					String baseFilePath = baseFileT.getText();
					String logContext;
					show.setText("");//清空show文本域内容
					if(baseFilePath!=""){
						baseFilePath=baseFilePath.replace('\\', '/');
						TextF baseFC = new TextF();
						logContext=baseFC.getLog(baseFilePath);
						if(logContext!=null){
							System.out.println(logContext);
						}
					}
				}
			});
			
			//查看算例按钮的监听
			showResultFB.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					FileDialog OpenFileDialog = new FileDialog(shell,SWT.OPEN);
					OpenFileDialog.setText("Choose example file");
					OpenFileDialog.setFilterExtensions(new String[] {"*.txt", "*.*"});
					OpenFileDialog.setFilterNames(new String[] { "Text format(*.txt)","All types(*.*)"});
					OpenFileDialog.setFilterPath("D:\\");
					OpenFileDialog.open();
					String resultFPath = OpenFileDialog.getFilterPath()+"\\"+OpenFileDialog.getFileName();
					resultFPath=resultFPath.replace('\\', '/');
					String resultFContext;
					show.setText("");//清空show文本域内容
					TextF resultFC = new TextF();
					resultFContext=resultFC.getLog(resultFPath);
					if(resultFContext!=null){
						System.out.println(resultFContext);
					}					
				}
			});
			
			//查看日志按钮的监听
			showlogB.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					String logFilePath = baseFileT.getText();
					String logContext;
					show.setText("");//清空show文本域内容
					if(logFilePath!=""){
						logFilePath=logFilePath.replace('\\', '/');
						logFilePath=logFilePath.replace(".bas", "-log.txt");
						logFilePath=logFilePath.replace(".BAS", "-log.txt");
						TextF logC = new TextF();
						logContext=logC.getLog(logFilePath);
						if(logContext!=null){
							System.out.println(logContext);
						}
					}					
				}
			});
			
			
		}

		final TabItem item3 = new TabItem(tabFolder, SWT.NONE);
		item3.setText("Check network diagram");
		{
			final Canvas canvas ;
			Composite comp3 = new Composite(tabFolder, SWT.BORDER);
			item3.setControl(comp3);
			
			Group group1 = new Group(comp3, SWT.NONE);
			group1.setBounds(0, 0, 580, 71);
			
			Group group2 = new Group(comp3, SWT.NONE);
			group2.setBounds(0, 78, 580, 509);
			group2.setText("Network diagram");
			canvas = new Canvas(group2, SWT.BORDER);
			canvas.setBounds(10, 20, 560, 480);
			canvas.setBackground(new Color(display,244,244,244));	
			Label resultFlieL = new Label(group1, SWT.NONE);
			resultFlieL.setBounds(21, 34, 80, 14);
			resultFlieL.setText("Case file:");
			
			final Text resultFilePathT = new Text(group1, SWT.BORDER);
			resultFilePathT.setBounds(100, 28, 240, 20);
			
			Button button = new Button(group1, SWT.NONE);
			button.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					FileDialog OpenFileDialog = new FileDialog(shell,SWT.OPEN);
					OpenFileDialog.setText("Choose the example file to view");
					OpenFileDialog.setFilterExtensions(new String[] {"*.txt", "*.*"});
					OpenFileDialog.setFilterNames(new String[] { "Text format(*.txt)","All types(*.*)"});
					OpenFileDialog.setFilterPath("D:\\");
					OpenFileDialog.open();
					resultFilePathT.setText(OpenFileDialog.getFilterPath()+"\\"+OpenFileDialog.getFileName());
					canvas.redraw();
				}
			});
			button.setBounds(360, 24, 40, 24);
			button.setText("Scan");
			
			Button button_1 = new Button(group1, SWT.NONE);
			button_1.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					canvas.redraw();
				}
			});
			button_1.setBounds(420, 24, 150, 24);
			button_1.setText("Check network diagram");
			
			// 监听canvas重绘事件
			canvas.addPaintListener(new PaintListener() {
				public void paintControl(final PaintEvent event) {
					String filePath = resultFilePathT.getText();
					if(filePath!=""){
						filePath=filePath.replace('\\', '/');
						ShowNet sn=new ShowNet();
						if(sn.showNetMain(canvas.getSize().x,canvas.getSize().y,filePath)){
							GC gc = new GC(canvas);
							sn.showN(gc,event.display);//显示sn的网络图
							gc.dispose();
						}	
					}
				}
			});			
		}
		
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}				
	}


	public static void main(String[] args) {
		new MainGui();
	}
	
	/**
	* 判断一个字符串是不是数字组成
	* @param s 字符
	* @return
	*/
	public static boolean isDigits(String s){
	if(s==null || s.length()==0)return false;
	for(int i=0;i <s.length();i++){
	if(!Character.isDigit(s.charAt(i)))return false;
	}
	return true;
	} 
	
	/**
	 判断一个字符串是不是数字或'.'组成
	* @param s 字符
	* @return
	 */
	public static boolean isDigitsOrDot(String s){
		if(s==null || s.length()==0)return false;
		for(int i=0;i <s.length();i++){
		if(!(Character.isDigit(s.charAt(i))|| s.charAt(i)=='.' ))return false;
		}
		return true;
		} 
}
