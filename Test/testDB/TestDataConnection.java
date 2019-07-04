package testDB;

import java.sql.SQLException;
import java.util.Scanner;

import fr.epita.quiz.connections.DataConnect;


public class TestDataConnection {
	
	
	private static String QuizName;
	
	
	public static void main(String[] args) throws SQLException {
		// TODO Auto-generated method stub
		
		Scanner scan = new Scanner(System.in);
		System.out.println("Please Enter the Quiz Name ");
		QuizName = scan.nextLine();
		
		
		String Op[] = new String[4];
		Op[0]="0";
		Op[1]="1";
		Op[2]="2";
		Op[3]="3";
		DataConnect dc = new DataConnect();
		
		int id = dc.search(QuizName);
		
		try {
			//dc.insertQuestion(id,"Test2", Op, 1);
			System.out.println("done");
		}
		catch(Exception e){
			System.out.println(e);
		}
		finally {
			dc.closeConnect();
		}
		
	}
}
