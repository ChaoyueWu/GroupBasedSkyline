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
			reader = new FileReader("dataSet/test.txt");
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
		
//		double[]d = {4,400};
//		Point p = new Point(d);
//		points.add(p);
//		double[]d1 = {24,380};
//		Point p1 = new Point(d1);
//		points.add(p1);
//		double[]d2 = {14,340};
//		Point p2 = new Point(d2);
//		points.add(p2);
//		double[]d3 = {36,300};
//		Point p3 = new Point(d3);
//		points.add(p3);
//		double[]d4 = {26,280};
//		Point p4 = new Point(d4);
//		points.add(p4);
//		double[]d5 = {8,260};
//		Point p5 = new Point(d5);
//		points.add(p5);
//		double[]d6 = {40,200};
//		Point p6 = new Point(d6);
//		points.add(p6);
//		double[]d7 = {20,180};
//		Point p7 = new Point(d7);
//		points.add(p7);
//		double[]d8 = {34,140};
//		Point p8 = new Point(d8);
//		points.add(p8);
//		double[]d9 = {28,120};
//		Point p9 = new Point(d9);
//		points.add(p9);
//		double[]d10 = {16,60};
//		Point p10 = new Point(d10);
//		points.add(p10);
		
		DSGGenerator sg = new DSGGenerator(4,points,d);
		DSG dsg = sg.generateDSG();
		for(DSGNode node:dsg.DSG){
			node.showNode();
		}
		PointWise pointWise = new PointWise();
		List<PointWise_Group> groups = pointWise.pointWiseCalculate(4, dsg);
		for(PointWise_Group group : groups) {
			System.out.println("group:");
			List<DSGNode> list = group.list;
			for(DSGNode node : list) {
				node.showNode();
			}
		}
	}

}
