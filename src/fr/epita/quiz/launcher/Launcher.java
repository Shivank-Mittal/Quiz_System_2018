package fr.epita.quiz.launcher;

import java.sql.SQLException;
import java.util.Scanner;
import fr.epita.quiz.process.Quiz;
import fr.epita.quiz.process.StudentProcess;
import fr.epita.quiz.connections.DataConnect;
import fr.epita.quiz.process.Questions;
import java.util.concurrent.TimeUnit;

public class Launcher {
	
	// Professour login credentials
	private static final String Prof_id = "ADMIN";
	private static final String Prof_pass = "PASS";
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner scan = new Scanner(System.in);
		DataConnect dc = new DataConnect();
		Quiz qz = new Quiz(dc);
		Questions ques = new Questions(dc);
		
		
		int choiceType;
		
		choiceType=LoginType(scan);
		
		while (!authentication(choiceType, scan)) {
			System.out.println("You are not authenticated : \n");
			// exit(0) 0 means exit without error
			System.exit(0);
		}
		
		if(choiceType ==1){
		System.out.println("\n\n ----- Welcome Profesor ----- \n");
		int todo =0;
		boolean rep = true;
		while (rep) {
		todo = 0;	
		todo = profMenu();
		
			switch (todo) {
			case 1: // 1 is for creating Quiz
				quizCreation(qz);
				break;
			case 2: // 2 is for creating Question
				questionCreation(ques);
				break;
			case 3:
				studentsAttemted(dc);
				
				break;
			case 4:
				searchQuiz(qz);
				break;
			case 5:
				System.out.println("Good Bye");
				rep = false;
				break;
			default:
				System.out.println("Wrong Choice");
				}
			
			}
		}
		else if (choiceType ==2) {
		System.out.println("\n ----- Welcome Students ----- \n");
		try {
			StartQuiz(dc);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
		
	}
	
	//For Selecting the type of user (Student or Professor)
	private static int LoginType(Scanner sc) {
		
		int ans;	
		System.out.println("Login as");
		System.out.println("1.professor");
		System.out.println("2.Student");
		System.out.println("Enter your choice (1|2) : \n");
		
		return ans= sc.nextInt();	
	}
	
	//For authenticating the user
	private static boolean authentication(int type ,Scanner sc) {
		// 1- professor , 2- Student
		String login;
		String pass;
		if(type == 1) {
			System.out.println("Enter Professor login");
			
			login=sc.next();
			
			System.out.println("Enter Professor Password");
			pass=sc.next();
			
			return  Prof_id.equals(login) && Prof_pass.equals(pass);
		}
		else if(type == 2) {
			return true;
		}
		else {
			return false;
		}
	}
	
	
	//providing options of tasks which professor can use
	private static int profMenu() {		
		int ans;
		Scanner sc_menu = new Scanner(System.in);
		System.out.println("--Menu--");
		System.out.println("1.Create Quiz");
		System.out.println("2.Create Question");
		System.out.println("3.Students Attemted the Quiz");
		System.out.println("4.Search Quiz");
		System.out.println("5.Quit Application");
		System.out.println("Enter your choice (1|2|3|4|5)");
		ans=sc_menu.nextInt();
		return ans;
	}

	//creating a questions for professor 
	private static void questionCreation(Questions ques) {
		System.out.println("Creating Question....");
		ques.creation();
	}
	
	//creating a quiz for professor 
	private static void quizCreation(Quiz qz) {

		System.out.println("Creating Quiz....");
		
		qz.createQuiz();
		
		
	}
	
	//searching quiz for professor 
	private static void searchQuiz(Quiz qz) {
		System.out.println("Searching Quiz....");
		
		
		qz.searchQuiz();
	}
	
	//used for getting the name of the students who have attempted the Quiz with their Marks and Quiz name
	private static void studentsAttemted( DataConnect dc) {
		String[][] values = null;
		String QuizName;
		Scanner sc = new Scanner(System.in);
		System.out.println("Enter Quiz Name");
		QuizName=sc.nextLine();
	
		try {
			values =dc.Students_attempted(QuizName);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		for(int i=0;i<values.length ;i++) {
			for(int j=0;j<3;j++) {
				System.out.print(values[i][j]);
				if(j<2) {
					System.out.print(" - ");
				}		
			}
			System.out.println("");
		}
		
	} 
	
	//For Starting and going throw the Quiz for student
	private static void StartQuiz(DataConnect dc) throws InterruptedException {
		String StudentName;
		StudentProcess st = new StudentProcess();
		Scanner sc_name = new Scanner(System.in);
		System.out.println("Please Enter Your Name");
		
		StudentName = sc_name.nextLine();
		
		System.out.println("...............Welcome " +StudentName);
		System.out.println("Your Quiz will Start in After 10 Sec..... ALL THE BEST");
		
		TimeUnit.SECONDS.sleep(10);
		st.info(dc,StudentName);
		
	}
}
