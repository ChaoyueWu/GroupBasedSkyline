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
		int k = 6;
		try {
			reader = new FileReader("dataSet/corr_4.txt");
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
		
		DSGGenerator sg = new DSGGenerator(k,points,d);
		
		ProcessResult dsg = sg.generateDSG();
//		for(DSGNode node:dsg.DSG){
//			node.showNode();
//		}
		
//		long startTime = System.nanoTime();//毫微秒
//		PointWise pointWise = new PointWise();
//		int groupsSize = pointWise.pointWiseCalculate(k, dsg);
//		long endTine = System.nanoTime();//毫微秒
//		System.out.println("point wise size: "+ groupsSize);
//		System.out.println("point wise time : " +(endTine - startTime)/1000);
		
//		long startTime = System.nanoTime();//毫微秒
//		PointWiseUsingArray pointWiseUsingArray = new PointWiseUsingArray();
//		int groupsSize = pointWiseUsingArray.pointWiseCalculate(k, dsg);
//		long endTine = System.nanoTime();//毫微秒
//		System.out.println("point wise array size: "+ groupsSize);
//		System.out.println("point wise array time : " +(endTine - startTime)/1000);
//		
		UnitWise uw = new UnitWise();
		long startTime = System.nanoTime();//毫微秒
		List<List<Integer>> result = uw.unitWiseCalculate(k, dsg.DSG);
		long endTine = System.nanoTime();//毫微秒
		System.out.println("unit wise+ size "+(result.size()+dsg.perfectNodeList.size()));
		System.out.println("unit wise+ time : " +(endTine - startTime)/1000);
		
//		UnitWise2 uw = new UnitWise2();
//		long startTime = System.nanoTime();//毫微秒
//		List<List<Integer>> result = uw.unitWiseCalculate(4, dsg.DSG);
//		long endTine = System.nanoTime();//毫微秒
//		System.out.println("unit wise2 size "+result.size());
//		System.out.println("unit wise2 time : " +(endTine - startTime)/1000);
		
//		UnitWise3 uw = new UnitWise3();
//		long startTime = System.nanoTime();//毫微秒
//		List<Set<Integer>> result = uw.unitWiseCalculate(4, dsg.DSG);
//		long endTine = System.nanoTime();//毫微秒
//		System.out.println("unit wise3 size "+result.size());
//		System.out.println("unit wise3 time : " +(endTine - startTime)/1000);
		
//		UnitWise4 uw = new UnitWise4();
//		long startTime = System.nanoTime();//毫微秒
//		List<Set<Integer>> result = uw.unitWiseCalculate(4, dsg.DSG);
//		long endTine = System.nanoTime();//毫微秒
//		System.out.println("unit wise4 size "+result.size());
//		System.out.println("unit wise4 time : " +(endTine - startTime)/1000);
	}

}
