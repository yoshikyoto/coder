package YLabCoder;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
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
	    File file = new File(root + name + "/" + date);
	    file.mkdir();
	    
	    // コードの保存
		PrintWriter pw = getPW(file.getAbsolutePath() + "/Main.java");
		pw.print(code);
		pw.close();
		
		String actual = "Server Error";
		try{
			// コードのコンパイル
			Runtime rt = Runtime.getRuntime();
			Process p = rt.exec("javac Main.java", null, file);
			p.waitFor();

			// コードの実行
			p = rt.exec("java Main", null, file);//("java Main", null, file);
			InputStream is = p.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);

			actual = "";
			String line = "";
			while((line = br.readLine()) != null){
				actual += line + "\n";
			}
		}catch(Exception e){
			actual += "\n" + e;
		}
		
	    out.println("<h2 class=\"page-header\">Result</h2>");
	    out.println("<h4>Question: " + name + "</h4>");
	    out.println("<h4>Code:</h4>");
	    out.println("<pre class=\"prettyprint\">" + code + "</pre>");
		
	    // 実行結果
	    out.println("<h4>Output</h4>");
	    out.println("<pre class=\"prettyprint\">" + actual + "</pre>");

	    out.println("</body>");
	    out.println("</html>");
	    out.close();
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
}
