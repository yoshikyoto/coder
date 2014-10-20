package YLabCoder;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/* 提出コード保存ディレクトリについて
 * root フォルダにディレクトリを作ると、Question が追加される
 * root/QUESTION_NAME/DATE/Main.java -> コンパイル前ファイル
 * root/QUESTION_NAME/DATE/Main.class -> コンパイル後ファイル
 */

public class Submit extends HttpServlet {
	public static String root = "/Users/admin/junit/";
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {

		response.setContentType("text/html; charset=UTF-8");
		request.setCharacterEncoding("UTF-8");

	    String name = request.getParameter("name");
	    String code = request.getParameter("submited_code");
		
	    // HTML PrintWriter
	    PrintWriter out = response.getWriter();
	    out.println("<html>");
	    printHeader(out, name);
	    out.println("<body>");
	    
	    // ディレクトリの準備
        String date = getDateString();
        File qfile = new File(root + name);
        File ofile = new File(root + name + "/output.txt");
	    File file = new File(root + name + "/" + date);
	    file.mkdir();
	    
	    // コードの保存
		PrintWriter pw = getPW(file.getAbsolutePath() + "/Main.java");
		pw.print(code);
		pw.close();
		
		/* 
		 * 0 結果
		 * 1 引っかかったinput
		 * 2 引っかかった想定のoutput
		 * 3 引っかかった実際のoutput
		 */
		String strs[] = new String[4];
		
		String result = "";
		try{
			// コードのコンパイル
			Runtime rt = Runtime.getRuntime();
			Process p = rt.exec("javac Main.java", null, file);
			p.waitFor();
			
			// 入出力ファイルの取得
			ArrayList<String> inputs = getAll("input", qfile);
			ArrayList<String> outputs = getAll("output", qfile);
			
			if(inputs.size() == 0){
				// input が無い場合
				String expected = getStringFromFile(ofile);
				strs = executeCode(file, expected);
			}else{
				// input が複数ある場合
				for(int i = 0; i < inputs.size(); i++){
					String input = inputs.get(i);
					String output = outputs.get(i);
					strs = executeCode(file, input, output);
				}
			}

			if(strs[0].equals("AC")){
				result = "<span class=\"label label-success\">Accepted</span>";
			}else{
				result = "<span class=\"label label-warning\">Wrong Answer</span>";
			}
		}catch(Exception e){
			result += "<span class=\"label label-danger\">Server Error</span>" + e;
		}
		
	    out.println("<h2 class=\"page-header\">Result</h2>");
	    out.println("<h4>" + result + "</h4>");
	    out.println("<h4>Question: " + name + "</h4>");
	    out.println("<h4>Code:</h4>");
	    out.println("<pre class=\"prettyprint\">" + code + "</pre>");
		
	    // 実行結果
	    out.println("<h4>Output:</h4>");
	    out.println("<pre class=\"prettyprint\">" + strs[3] + "</pre>");

	    out.println("</body>");
	    out.println("</html>");
	    out.close();
	}
	
	public static String[] executeCode(File file, String input, String output) throws IOException{
		String strs[] = new String[4];
		// コードの実行
		Runtime rt = Runtime.getRuntime();
		Process p = rt.exec("java Main", null, file);//("java Main", null, file);
		InputStream is = p.getInputStream();
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(isr);
		
		OutputStream os = p.getOutputStream();
		PrintWriter pw = new PrintWriter(os);
		pw.println(output);

		String actual = "";
		String line = "";
		while((line = br.readLine()) != null){
			actual += line + "\n";
		}
		if(actual.equals(output)){
			strs[0] = "AC";
		}else{
			strs[0] = "WA";
		}
		strs[1] = input;
		strs[2] = output;
		strs[3] = actual;
		return strs;
	}
	
	public static String[] executeCode(File file, String expected) throws IOException{
		String strs[] = new String[4];
		// コードの実行
		Runtime rt = Runtime.getRuntime();
		Process p = rt.exec("java Main", null, file);//("java Main", null, file);
		InputStream is = p.getInputStream();
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(isr);

		String actual = "";
		String line = "";
		while((line = br.readLine()) != null){
			actual += line + "\n";
		}
		if(actual.equals(expected)){
			strs[0] = "AC";
		}else{
			strs[0] = "WA";
		}
		strs[1] = "(no inputs)";
		strs[2] = expected;
		strs[3] = actual;
		return strs;
	}
	
	public static void printHeader(PrintWriter out, String title){
	    out.println("<head>");
	    out.println("<title>" + title + " - YLabCoder</title>");
	    out.println("<link rel=\"stylesheet\" href=\"/YLabCoder/bootstrap/css/bootstrap.min.css\">");
	    out.println("<link rel=\"stylesheet\" href=\"/YLabCoder/bootstrap/css/bootstrap.css\">");
	    out.println("</head>");	
	}
	
	public static String getDateString(){
	    Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
        String date = sdf.format(cal.getTime());
        return date;
	}
	
	public static PrintWriter getPW(String filename) throws IOException{
		FileWriter fw = new FileWriter(filename);
		BufferedWriter bw = new BufferedWriter(fw);
		PrintWriter pw = new PrintWriter(bw);
		return pw;
	}
	
	public static ArrayList<String> getAll(String kind, File dir) throws IOException{
		ArrayList<String> arr = new ArrayList<String>();
		int i = 0;
		while(true){
			File file = new File(dir.getAbsolutePath() + "/" + kind + i + ".txt");
			if(!file.exists()) break;
			String str = getStringFromFile(file);
			arr.add(str);
		}
		return arr;
	}
	
	public static String getStringFromFile(File file) throws IOException{
		FileReader fr = new FileReader(file);
		BufferedReader br = new BufferedReader(fr);
		String line, result = "";
		while((line = br.readLine()) != null)
			result += line + "\n";
		br.close();
		fr.close();
		return result;
	}
}
