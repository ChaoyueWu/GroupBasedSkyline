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
		
		DSGGenerator sg = new DSGGenerator(4,points,d);
		
		ProcessResult dsg = sg.generateDSG();
//		for(DSGNode node:dsg.DSG){
//			node.showNode();
//		}
		
		long startTime = System.nanoTime();//毫微秒
		PointWise pointWise = new PointWise();
		int groupsSize = pointWise.pointWiseCalculate(4, dsg);
		long endTine = System.nanoTime();//毫微秒
		System.out.println("point wise size: "+ groupsSize);
		System.out.println("point wise time : " +(endTine - startTime)/1000);
		
		
		//unit wise
		UnitWise uw = new UnitWise();
		startTime = System.nanoTime();//毫微秒
		List<List<Integer>> result = uw.unitWiseCalculate(4, dsg.DSG);
		endTine = System.nanoTime();//毫微秒
		System.out.println("unit wise size "+result.size());
		System.out.println("point wise time : " +(endTine - startTime)/1000);
	}

}
