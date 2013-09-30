package gen;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Network {
	int arcs;// 网络中的边数(一个子项目中的)
	Project p;// 用于保存网络的父对象,即项目的引用

	public Network(Project p) {
		this.p = p;
	}

	/**
	 * 产生项目的网络
	 */
	boolean genNetWork(Demand demand, Utility utility) {
		int trialNr;// 表示当前是第几次试验
		int pnr; // 当前子项目的编号
		initNetw(0, p.nrOfJobs - 1);
		// 下面循环生成每个子项目的网络
		boolean success = false; // 表示一个子项目的网络是否生成成功
		for (pnr = 0; pnr < p.nrOfPro;) {
			trialNr = 1; // 表示当前的试验次数(对于第pnr个子项目试验)
			arcs = 0;// 每个子项目(每次试验中)开始生成网络时网络边数置0
			success = false;
			while (!success && trialNr <= demand.base.maxTrials) {
				success = true;
				// 首虚拟活动的直接紧后活动的数目
				p.NSJ[pnr] = demand.base.minOutSour
						+ utility.random.nextInt(demand.base.maxOutSour
								- demand.base.minOutSour + 1);
				// 尾虚拟活动的直接紧前活动的数目
				p.NFJ[pnr] = demand.base.minInSink
						+ utility.random.nextInt(demand.base.maxInSink
								- demand.base.minInSink + 1);
				arcs = p.NSJ[pnr] + p.NFJ[pnr];// 当前子项目网络中的边数

				// 给每个活动(排除虚开始的直接后继)添加一个紧前活动
				success = !utility.error(!(addPred(demand.base, pnr, utility)),
						1, demand.errFilePath, "");
				// 给那些还没有后继的所有活动添加一个后继
				if (success)
					success = !utility.error(
							!(addSucc(demand.base, pnr, utility)), 2,
							demand.errFilePath, "");
				// 网络复杂度(NC)的定义即是(子)项目中平均每项活动（含虚活动1，J）拥有的非冗余弧数量.
				if (success)
					// 如果需要的复杂性低，即NC约为1，很可能会发生添加到网络中的弧的数目太多
					success = !utility.error(arcs > (1 + demand.base.netTol)
							* demand.base.compl * (p.pro[pnr] + 2), 4,
							demand.errFilePath, "");

				// 添加更多的弧，直到达到预定的NC。
				if (success)
					success = !utility.error(
							!(addArcsToCompl(demand.base, pnr, utility)), 3,
							demand.errFilePath, "");//

				//没有成功则重新生成
				if (!success) {
					trialNr++;
					initNetw(p.SJ[pnr], p.FJ[pnr]);
					arcs = 0;
				}
			}
			success = !utility.error(trialNr > demand.base.maxTrials, 1000,
					demand.errFilePath, "");
			if (success)
				pnr++;
			else
				// 在规定的实验次数下,没有生成符合要求的某个子项目的网络,此时整个项目的网络生成也就失败了
				return false;
		}
		//把所有子项目的(真正的)开始活动(可多个)与虚开始活动和虚结束活动相连
		addArcsToSourceASink(demand.base);
		
		// 原程序下面还要对整个网络是否有冗余弧进行检查,为什么还会出现冗余弧?==========================================
		if (!(this.noRedund())) {
			utility.error(true, 1001, demand.errFilePath, "");
			return false;
		}

		return true;// 整个项目的网络生成成功
	}

	/**
	 * 检查整个网络是否有冗余弧(怎么会产生冗余?)
	 * 
	 * @return
	 */
	private boolean noRedund() {
		for (int i = 0; i < this.p.nrOfJobs - 1; i++) {
			for (int j = i + 1; j < this.p.nrOfJobs; j++) {
				if (this.p.jobs[i].dirSucc.contains(j)) {
					for (int k = i + 1; k < j; k++) {
						if (this.p.jobs[i].dirSucc.contains(k)) {
							if (this.p.jobs[k].inDirSucc.contains(j)) {
								return false;
							}
						}
					}
				}
			}
		}
		return true;
	}

	/**
	 * 初始化项目网路,每个活动的紧前/紧后活动数目都设为0,(直接/间接)紧前/紧后集都为空
	 * 
	 * @param begin
	 *            开始活动编号
	 * @param end
	 *            结束活动编号
	 */
	void initNetw(int begin, int end) {
		for (int i = begin; i <= end; i++) {
			p.jobs[i].nrOfPred = 0;
			p.jobs[i].nrOfSucc = 0;
			p.jobs[i].dirPred.clear();
			p.jobs[i].dirSucc.clear();
			p.jobs[i].inDirPred.clear();
			p.jobs[i].inDirSucc.clear();
		}
	}

	/**
	 * 为每个活动(除了前NSJ[pnr]个活动,因为这些活动作为虚开始活动的直接后继)添加一个紧前活动,
	 * 且虚结束活动的任一直接紧前活动不会作为其他活动的紧前活动
	 * 
	 * @param base
	 * @param pnr
	 *            当前子项目编号
	 * @param arcs
	 *            网络中边数
	 * @param utility
	 * @return
	 */
	private boolean addPred(BaseStruct base, int pnr, Utility utility) {
		int j, iMax, index;
		List predecessor = new ArrayList();

		for (j = p.SJ[pnr] + p.NSJ[pnr]; j <= p.FJ[pnr]; j++) {
			iMax = (j - 1) < (p.FJ[pnr] - p.NFJ[pnr]) ? (j - 1)
					: (p.FJ[pnr] - p.NFJ[pnr]);

			// 下面从该子项目pnr的第一个活动SJ[pnr]到iMax(小于j)中寻找所有可以作为j的紧前活动的,记录在predecessor中
			predecessor.clear();
			for (int i = p.SJ[pnr]; i <= iMax; i++) {
				// 如果活动i的紧后活动数小于活动的最大紧后数目,并且添加上弧(i,j),并不造成冗余
				if ((p.jobs[i].nrOfSucc < base.maxOut)
						&& !(redundant(i, j, p.FJ[pnr]))) {
					predecessor.add(i);
				}
			}
			if (predecessor.size() > 0) {
				index = utility.random.nextInt(predecessor.size());
				addArc((Integer) predecessor.get(index), j);
			} else
				return false;// 当任一活动没有合适可选的紧前活动时,就失败.此次试验失败
		}
		return true;
	}

	private boolean addSucc(BaseStruct base, int pnr, Utility utility) {
		int jMin, index;
		List successor = new ArrayList();

		for (int i = p.SJ[pnr]; i <= p.FJ[pnr] - p.NFJ[pnr]; i++) {
			if (p.jobs[i].nrOfSucc == 0) {
				jMin = (i + 1) > (p.SJ[pnr] + p.NSJ[pnr]) ? (i + 1)
						: (p.SJ[pnr] + p.NSJ[pnr]);
				// 下面从jMin到该子项目最后一个活动中寻找所有可作为i的紧后活动的,记录在successor中
				successor.clear();
				for (int j = jMin; j <= p.FJ[pnr]; j++) {
					if ((p.jobs[j].nrOfPred < base.maxIn)
							&& !(redundant(i, j, p.FJ[pnr])))
						successor.add(j);
				}

				if (successor.size() > 0) {
					index = utility.random.nextInt(successor.size());
					addArc(i, (Integer) successor.get(index));
				} else
					return false;
			}
		}
		return true;
	}

	/**
	 * 给网络中不断添加边,直到达到要求的网络复杂度
	 * 
	 * @param base
	 * @param pnr
	 * @param utility
	 * @return
	 */
	private boolean addArcsToCompl(BaseStruct base, int pnr, Utility utility) {
		int root;// 随机选择的一个活动,作为欲添加边的起始活动
		List successor = new ArrayList();
		int trialNr; // 实验的次数
		int jMin, index;
		// ==========================================下面循环的判断条件感觉可以改进(在阅读原文件中注释过了)
		for (trialNr = 0; (arcs < base.compl * (p.pro[pnr] + 2))
				&& (trialNr <= base.maxTrials);) {
			root = p.SJ[pnr]
					+ utility.random.nextInt((p.FJ[pnr] - p.NFJ[pnr])
							- p.SJ[pnr] + 1);
			// 不断的为随机选择的活动添加紧后活动
			if (p.jobs[root].nrOfSucc < base.maxOut) {
				jMin = (root + 1) > (p.SJ[pnr] + p.NSJ[pnr]) ? (root + 1)
						: (p.SJ[pnr] + p.NSJ[pnr]);
				successor.clear();
				for (int j = jMin; j <= p.FJ[pnr]; j++) {
					if ((p.jobs[j].nrOfPred < base.maxIn)
							&& !(redundant(root, j, p.FJ[pnr])))
						successor.add(j);
				}
				if (successor.size() > 0) {
					index = utility.random.nextInt(successor.size());
					addArc(root, (Integer) successor.get(index));
				} else
					trialNr++;
			} else
				trialNr++;
		}

		// 对于添加更多的弧，直到达到预定的NC这一标准，所需的复杂性难以获得。
		// 因此在一个有限的尝试随机选择一个节点并计算可能的后继者时，
		// 没有更多的弧可以添加使得arcs>=J*NC*(1-ξ)时，即可认为达到标准。
		return (trialNr <= base.maxTrials)
				&& (arcs >= base.compl * (1 - base.netTol) * (p.pro[pnr] + 2));
	}

	/**
	 * 为把所有子项目的(真正的)开始活动(可多个)和虚开始活动相连,尾活动和虚结束活动相连
	 * 
	 * @param base
	 */
	void addArcsToSourceASink(BaseStruct base) {
		for (int pnr = 0; pnr < p.nrOfPro; pnr++) {
			for (int i = 0; i < p.NSJ[pnr]; i++)
				addArc(0, p.SJ[pnr] + i);// 第一个子项目是从活动1开始的,活动0为所有活动的虚开始活动
			for (int j = 0; j < p.NFJ[pnr]; j++)
				addArc(p.FJ[pnr] - j, p.nrOfJobs - 1);// 注意尾虚活动的编号是p.nrOfJobs-1
		}
	}

	/**
	 * 判断arc(i,j)是否是多余的(假设i小于j)
	 * 
	 * @param i
	 * @param j
	 * @param FJ
	 * @return
	 */
	boolean redundant(int i, int j, int FJ) {
		int k;
		Iterator iter;
		// case(a)如果j包含在i的后续活动中，则i，j是冗余弧
		if (p.jobs[i].inDirSucc.contains(j))
			return true;
		// case(b)如果k包含在j的后续活动中，并且i的紧前活动包含k的直接紧前活动，则i，j是冗余弧
		for (k = j + 1; k <= FJ; k++) {
			if (p.jobs[j].inDirSucc.contains(k)) {
				iter = p.jobs[k].dirPred.iterator();
				while (iter.hasNext()) {
					if (p.jobs[i].inDirPred.contains(iter.next()))
						return true;
				}
			}
		}
		// case(c)如果i的紧前活动包含j的直接紧前活动，则i，j是冗余弧
		iter = p.jobs[j].dirPred.iterator();
		while (iter.hasNext()) {
			if (p.jobs[i].inDirPred.contains(iter.next()))
				return true;
		}
		// case(d)如果j的紧后活动包含i的直接紧后活动，则i，j是冗余弧
		iter = p.jobs[i].dirSucc.iterator();
		while (iter.hasNext()) {
			if (p.jobs[j].inDirSucc.contains(iter.next()))
				return true;
		}
		return false;
	}

	/**
	 * 添加一条(i,j)边
	 * 
	 * @param i
	 * @param j
	 * @param arcs
	 */
	void addArc(int i, int j) {
		p.jobs[i].nrOfSucc++;// i的直接后继数目加1
		p.jobs[i].dirSucc.add(j);
		p.jobs[i].inDirSucc.add(j);
		Iterator iter = p.jobs[j].inDirSucc.iterator();
		// 把j的紧后活动集合加入到i的紧后活动集合中
		while (iter.hasNext()) {
			p.jobs[i].inDirSucc.add(iter.next());
		}

		p.jobs[j].nrOfPred++; // j的直接紧前数目加1
		p.jobs[j].dirPred.add(i);
		p.jobs[j].inDirPred.add(i);
		iter = p.jobs[i].inDirPred.iterator();
		// 把i的紧前活动集合加入到j的紧前活动集合中
		while (iter.hasNext()) {
			p.jobs[j].inDirPred.add(iter.next());
		}

		arcs++;// 网络边数加1

		// 重新初始化i的所有紧前活动的紧后活动
		for (int k = 0; k < i; k++) { // 这个地方从k=0开始是否合适?这样会把所有活动都加为0的间接后继?
			if (p.jobs[i].inDirPred.contains(k)) {
				iter = p.jobs[i].inDirSucc.iterator();
				while (iter.hasNext())
					p.jobs[k].inDirSucc.add(iter.next());
			}
		}

		// 重新初始化j的所有紧后活动的紧前活动
		for (int k = j + 1; k < p.nrOfJobs; k++) {// 这个地方到k=p.nrOfJobs-1是否合适?这样会把所有活动都加为p.nrOfJobs的间接前驱==========================================
			if (p.jobs[j].inDirSucc.contains(k)) {
				iter = p.jobs[j].inDirPred.iterator();
				while (iter.hasNext())
					p.jobs[k].inDirPred.add(iter.next());
			}
		}

	}

}
