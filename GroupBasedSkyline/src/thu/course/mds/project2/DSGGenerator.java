package thu.course.mds.project2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * 构造K条skyline
 * 并生成DSG
 * @author wcy
 *
 */
public class DSGGenerator {
	private int k;//layers of skyline
	private List<Point> points;//传入的待分组点的集合
	private int d;//dimension of each point's attributes
	private List<ArrayList<Point>> skylines;//存放每条skyline
	
	public DSGGenerator(int k, List<Point> points, int d) {
		super();
		this.k = k;
		this.points = points;
		this.d = d;
		//初始化k条skyline
		skylines = new ArrayList<ArrayList<Point>>();
		for(int i = 0;i<k;i++){
			ArrayList<Point> list = new ArrayList<Point>();
			skylines.add(list);
		}
	}

	/**
	 * 生成k个skyline
	 */
	private void generateSkylines(){
		Collections.sort(points);//按照第一维度属性升序排序
		if(d==2){
			this.generate2D();
		}else{
			this.generateHighDimension();
		}
	}
	
	/**
	 * 属性只有二维，利用单调性加速生成时间
	 */
	private void generate2D(){
		int maxLayer = 1;//记录当前的最大层数
		Point[]tail = new Point[k];//存放每一层的尾部节点（拥有在该层最大的y轴大小）
		this.skylines.get(0).add(points.get(0));//排好序的第一个点属于layer1
		tail[0] = points.get(0);
		int n = points.size();
		for(int i = 1;i<n;i++){
			Point tmp = points.get(i);
			if(!tmp.isDominatedBy(tail[0], this.d)){	//属于第一层
				skylines.get(0).add(tmp);
				tail[0] = tmp;
			}else if(tmp.isDominatedBy(tail[maxLayer-1], this.d)){	//需要新建一层
				if(maxLayer == k){	//只找在前k个skyline中的点
					continue;
				}else{
					maxLayer++;
					skylines.get(maxLayer-1).add(tmp);
					tail[maxLayer-1] = tmp;
				}
			}else{	//find the layer-j to put current point
				int j = binarySearchLayer(tail,0,maxLayer-1,tmp);//属于第j+1层
				skylines.get(j).add(tmp);
				tail[j] = tmp;
			}
		}
	}
	/**
	 * 利用二分法加快查找给定p所属的层数j
	 * @param tail
	 * @param l
	 * @param r
	 * @param p
	 * @return
	 */
	private int binarySearchLayer(Point[]tail,int l,int r,Point p){
		if(l>=r)
			return l;
		int mid = (l+r)/2;
		if(p.isDominatedBy(tail[mid], this.d)){
			return binarySearchLayer(tail,mid+1,r,p);
		}else{
			if(p.isDominatedBy(tail[mid-1], this.d))
				return mid;
			return binarySearchLayer(tail,l,mid-1,p);
		}
	}
	/**
	 * 属性有多维，只能遍历
	 */
	private void generateHighDimension(){
		int maxLayer = 1;//记录当前的最大层数
		this.skylines.get(0).add(points.get(0));//排好序的第一个点属于layer1
		int n = points.size();
		for(int i = 1;i<n;i++){
			Point tmp = points.get(i);
			boolean foundLayer = false;
			for(int j = 0;j<maxLayer;j++){	//遍历当前每一层skyline
				boolean isDominate = false;
				for(Point p:skylines.get(j)){	//判断第j层skyline是否有点支配tmp,若没有，则找到所属skyline
					if(tmp.isDominatedBy(p, this.d)){
						isDominate = true;
						break;
					}
				}
				//没有被当前层支配 则属于该层
				if(!isDominate){
					skylines.get(j).add(tmp);
					foundLayer = true;
					break;
				}
			}
			//没有找到一层使得该层中的每个点都不支配当前点，则新建一个层
			if(!foundLayer){
				if(maxLayer<k)
				maxLayer++;
				skylines.get(maxLayer-1).add(tmp);
			}
		}
	}
	
	public void showSkylines(){
		int i = 1;
		for(ArrayList<Point> list:skylines){
			System.out.println("skyline "+i++);
//			for(Point p:list){
//				System.out.print(p);
//				
//			}
			System.out.println(list.size());
		}
	}
	/**
	 * 公开的方法
	 * 生成DSG供后续步骤使用
	 * @return
	 */
	public DSG generateDSG(){
		this.generateSkylines();
		this.showSkylines();
		
		DSG dsg = new DSG();
		List<DSGNode> perfectNodeList = dsg.perfectNodeList;
		for(int i = k -1 ; i >= 0 ; i --) {//从大往小遍历预处理
			ArrayList<Point> skyline = skylines.get(i);
			Iterator<Point> it = skyline.listIterator();
			while(it.hasNext()) {
				Point p = it.next();
				int countPatrents = 0;
				List<Integer> parents = new ArrayList<Integer>();
				for(int m = 0 ; m < i ; m ++) {
					ArrayList<Point> skylineP = skylines.get(m);
					for(int n = 0 ; n < skylineP.size() ; n ++) {
						if(p.isDominatedBy(skylineP.get(n), d)) {
							countPatrents ++;
							parents.add(skylineP.get(n).getIdx());
						}
					}
				}
				if(countPatrents > k - 1) {
					it.remove();
				}
				else if(countPatrents == k - 1) {
					it.remove();
//					System.out.println("oriPerfect");
					DSGNode node = new DSGNode(i + 1,p.getIdx(),this.d,toArrayList(p.getAttributes()));
					perfectNodeList.add(node);
				}
			}
		}
		int index = 0;		
		
		for(int i = 0 ; i < k ; i ++) {
			ArrayList<Point> skyline = skylines.get(i);
			for(int j = 0 ; j < skyline.size() ; j ++) {
				Point p = skyline.get(j);
				p.setIdx(index);
				index ++;
			}
		}
		
		List<DSGNode> dsgList = dsg.DSG;
		for(int i = 0 ; i < k ; i ++) {//从大往小遍历
			ArrayList<Point> skyline = skylines.get(i);
			for(int j = 0 ; j < skyline.size() ; j ++) {
				Point p = skyline.get(j);
				DSGNode node = new DSGNode(i + 1,p.getIdx(),this.d,toArrayList(p.getAttributes()));
				List<Integer> parents = node.getParents();
				
				for(int m = 0 ; m < i ; m ++) {
					ArrayList<Point> skylineP = skylines.get(m);
					for(int n = 0 ; n < skylineP.size() ; n ++) {
						if(p.isDominatedBy(skylineP.get(n), d)) {
							parents.add(skylineP.get(n).getIdx());
						}
					}
				}
				
				List<Integer> children = node.getChildren();
				for(int m = i + 1 ; m < k ; m ++) {
					ArrayList<Point> skylineC = skylines.get(m);
					for(int n = 0 ; n < skylineC.size() ; n ++) {
						if(skylineC.get(n).isDominatedBy(p, d)) {
							children.add(skylineC.get(n).getIdx());
						}
					}
				}
				dsgList.add(node);
			}
		}
		return dsg;
	}
	/**
	 * 将double类型的数组封装为List<Double>
	 * @param arr
	 * @return
	 */
	private List<Double> toArrayList(double[]arr){
		List<Double> list = new ArrayList<Double>();
		for(int i = 0;i<this.d;i++){
			list.add(Double.valueOf(arr[i]));
		}
		return list;
	}
}
