package thu.course.mds.project2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("please input k : ");
		Scanner scan = new Scanner(System.in);
		int k = scan.nextInt();
		System.out.println("please input the filename:");
		String filename = scan.next();
		System.out.println("please input the function : 1 means point wise , 2 means unit wise:");
		int funcNum = scan.nextInt();
		scan.close();
		List<Point> points  = new ArrayList<Point>();
		FileReader reader = null;
		BufferedReader br = null;
		try {
			reader = new FileReader("dataSet/"+filename+".txt");
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
		
		int d = points.get(0).getAttributes().length;
		
		DSGGenerator sg = new DSGGenerator(k,points,d);
		ProcessResult dsg = sg.generateDSG();
		
		if(funcNum == 1) {
			long startTime = System.nanoTime();//毫微秒
			PointWiseUsingArray pointWiseUsingArray = new PointWiseUsingArray();
			long groupsSize = pointWiseUsingArray.pointWiseCalculate(k, dsg);
			long endTine = System.nanoTime();//毫微秒
			System.out.println("point wise array size: "+ groupsSize);
			System.out.println("point wise array time : " +(endTine - startTime)/1000);
		}
		else {
			UnitWise uw = new UnitWise();
			long startTime = System.nanoTime();//毫微秒
			long result = uw.unitWiseCalculate(k, dsg);
			long endTine = System.nanoTime();//毫微秒
			System.out.println("unit wise+ size "+(result+dsg.perfectNodeList.size()));
			System.out.println("unit wise+ time : " +(endTine - startTime)/1000);
		}
	}

}
