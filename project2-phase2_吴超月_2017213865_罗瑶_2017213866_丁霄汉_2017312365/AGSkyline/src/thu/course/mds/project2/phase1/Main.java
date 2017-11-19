package thu.course.mds.project2.phase1;

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
		scan.close();
		List<Point> points  = new ArrayList<Point>();
		FileReader reader = null;
		BufferedReader br = null;
		try {
			reader = new FileReader(filename);
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
		System.out.println(points.size());
		
		DSGGenerator sg = new DSGGenerator(points.size(),points,d);
		ProcessResult dsg = sg.generateDSG(k);
		
		AGNew ag = new AGNew();
		long startTime = System.nanoTime();//毫微秒
		long result = ag.agCalculate(k, dsg);
		System.out.println(result);
		long endTine = System.nanoTime();//毫微秒
		System.out.println("time : " +(endTine - startTime)/1000);
	}

}
