package thu.course.mds.project2;

/**
 * 读取的原始数据集条目结构
 * @author wcy
 *
 */
public class Point implements Comparable<Point>{
	private int idx;
	private double[] attributes;

	public Point(int idx, double[] attributes) {
		super();
		this.idx = idx;
		this.attributes = attributes;
	}

	public int getIdx() {
		return idx;
	}

	public void setIdx(int idx) {
		this.idx = idx;
	}

	public double[] getAttributes() {
		return attributes;
	}

	public void setAttributes(double[] attributes) {
		this.attributes = attributes;
	}
	/**
	 * 判断该点是否被p1支配，若被p1支配，则返回true
	 * @param p1
	 * @param d dimension
	 * @return
	 */
	public boolean isDominatedBy(Point p1,int d){
		double[]attr = p1.getAttributes();
		boolean isGreater = false;
		for(int i = 0;i<d;i++){
			//如果当前有一个维度的属性比p1的小，则该点不被p1支配
			if(this.attributes[i]<attr[i]){
				return false;
			}
			//要求如果p1支配当前点，则必须有一位属性比当前点的小
			if(this.attributes[i]>attr[i]){
				isGreater = true;
			}
		}
		return isGreater;
	}

	@Override
	public int compareTo(Point o) {
		double d1 = o.getAttributes()[0];
		if(this.attributes[0]>d1){
			return 1;
		}else if(this.attributes[0]<d1){
			return -1;
		}
		return 0;
	}
	public String toString(){
		String s = "idx:"+this.idx+" ";
		for(int i = 0;i<this.attributes.length;i++){
			s += this.attributes[i];
			s+=",";
		}
		s += "\n";
		return s;
	}
}
