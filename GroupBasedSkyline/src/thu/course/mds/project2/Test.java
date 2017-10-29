package thu.course.mds.project2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Test {
	public static void main(String[] args) {
		List<Point> points  = new ArrayList<Point>();
		FileReader reader = null;
		BufferedReader br = null;
		int d = 0;
		try {
			reader = new FileReader("dataSet/anti_2.txt");
			br = new BufferedReader(reader);
			String str;
			while((str = br.readLine()) != null) {
				String[] s = str.split(" ");
				double[]d0 = new double[s.length];
				for(int i = 0 ; i < s.length ; i ++) {
					d0[i] = Double.parseDouble(s[i]);
				}
				Point p = new Point(d0);
				points.add(p);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // 读取文本中内容
		
		d = points.get(0).getAttributes().length;
		long startTime = System.nanoTime();//毫微秒
		DSGGenerator sg = new DSGGenerator(4,points,d);
		ProcessResult dsg = sg.generateDSG();
		System.out.println("预处理之后剩余的节点数:"+dsg.DSG.size());
//		for(DSGNode node:dsg.DSG){
//			node.showNode();
//		}
		
		PointWise pointWise = new PointWise();
		int groupsSize = pointWise.pointWiseCalculate(4, dsg);
		long endTime = System.nanoTime();//毫微秒
		System.out.println("PointWise:"+(endTime - startTime)/1000);
		System.out.println("groups size : " + groupsSize);
	}

}
