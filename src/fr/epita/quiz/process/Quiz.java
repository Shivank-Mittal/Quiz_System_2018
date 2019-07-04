package fr.epita.quiz.process;


import java.util.Scanner;
import fr.epita.quiz.connections.DataConnect;

public class Quiz {
	private String QuizName;
	DataConnect dc ;
	

	public Quiz(DataConnect dc_rev) {
		// TODO Auto-generated constructor stub
		dc=dc_rev;
	}

	// For Setting Quiz name
	public void createQuiz() {
		
		Scanner scan = new Scanner(System.in);	
		System.out.println("Please Enter the Quiz Name ");
		this.QuizName=scan.nextLine();
		
		System.out.println(QuizName);
		
		
		try {
			if(dc.insertQuiz(QuizName))	
			{
				System.out.println("Created A Quiz with name :- " +QuizName);
			}
			else {
				System.out.println("Can not create :- " +QuizName+" Quiz name");
			}
		}catch(Exception e) {
			System.out.println("Exception encountered in inserting Quiz name /n Error :- " +e);
		}
		finally {
		}
		
	}

	// For Getting Quiz name from Database
	public void searchQuiz() {
		Scanner scan = new Scanner(System.in);
		
		System.out.println("Enter the Quiz name ");
		String Qname=scan.nextLine();
		 
		try {
			
			if(dc.search(Qname) != 0) {
				System.out.println("Quiz Found");
			}
			else {
				System.out.println("Quiz Not Found");
			}
			
			
		}catch(Exception e) {
			System.out.println("Some Error accoured in search function \n Error :- " +e);
		}
		finally {
		}
	}
}
