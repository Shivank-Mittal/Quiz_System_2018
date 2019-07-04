package fr.epita.quiz.process;

import java.awt.Choice;
import java.io.StreamCorruptedException;
import java.sql.Array;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import fr.epita.quiz.connections.DataConnect;

public class StudentProcess {
	
	private static int DificultyLevel ;
	private static String QuizName;
	private static int QuizId;
	private static String Topic;
	private static String StudentName;
	private static int Marks_Obtained;
	
	private static DataConnect dc ;
	
	
	public static void info(DataConnect dc_recv,String SName) {
		StudentName=SName;
		dc=dc_recv;
		Scanner  sc = new Scanner(System.in);
		System.out.println("Enter the Quiz Name You want to attempt");
		QuizName = sc.nextLine();
		
		try {
		 QuizId=dc.search(QuizName);		
		}catch (SQLException e) {
			System.out.println("Sonthing in connection went wrong ");
			System.out.println("Good Bye");
			System.exit(1);
		}
		
		 if(QuizId==0) {
			System.out.println("No Quiz Found with this name ");
			System.out.println("");
			 
		 }
		 
		 
		System.out.println("....Enter Dificulty Level...");
		System.out.print("1:Easy");
		System.out.print("  2: Medium");
		System.out.println("  3: Hard");
		System.out.println("Chose 1 | 2 | 3");
		DificultyLevel = sc.nextInt();
		
		 Marks_Obtained=getQuesId() ;	
		 try {
			dc.insertStudentInfor(StudentName, QuizName, Marks_Obtained);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 System.out.println(" Your Total Marks For MCQ Questions is : "+Marks_Obtained );
	}
	
	// For getting the desired Question Id
	private static int  getQuesId() {
		Scanner scan_ans = new Scanner(System.in);
		Scanner scan_MCQ = new Scanner(System.in);
		List<Integer> QId_List=new ArrayList<Integer>();
		String[] Op = new String[4];
		int Choice=-1;
		int Cal = 0;
		try {
			QId_List=dc.getQuestionIds(QuizId, DificultyLevel);
			}
			catch(SQLException e) {
				System.out.println("Some problem accured");
			}
		
			for(int id : QId_List) {
			try {
				System.out.println(dc.getQuestion(id));
				Op=dc.getQptions(id);
				if(Op[0]==null)
				{
					System.out.println("Enter The answer");
					scan_ans.nextLine();
				}
				else {
					for(int i =0 ;i<Op.length ;i++) {
						System.out.print( (i+1)+"> "+Op[i] +"   ");
					}
					System.out.print("\n");
					System.out.println("Enter The Choice (Only Options (1|2|3|4))");
					Choice=scan_MCQ.nextInt();
					if(getAnswers(id,(Choice) )) {
						Cal=Cal+1;
					}
				}
				System.out.println("\n");
				
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
			
		return Cal;	
	}

	// For getting the correct answer 
	private static boolean getAnswers(int Q_id,int Choice) {
		int ans=-1;
		
		try {
			ans=dc.CorrectAnswer(Q_id);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return ans == Choice ;		
	}
}
