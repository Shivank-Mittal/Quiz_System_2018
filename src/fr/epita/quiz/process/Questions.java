package fr.epita.quiz.process;

import java.sql.SQLException;
import java.util.Scanner;

import fr.epita.quiz.connections.DataConnect;
import fr.epita.quiz.process.Answer;

public class Questions {
	
	private String QuizName;
	private int QuizId;
	private String Question;
	private String QuestionType;
	private String Topic;
	private String Option[];
	private int Dificulty;
	private int Question_Id;
	DataConnect dc;
	
	//initializing DataConect 
	public Questions(DataConnect dc_rev) {
		dc=dc_rev;
	}
	
	//For creating the questions
	public void creation() {
		
		Scanner sc = new Scanner(System.in);
		
		System.out.println("Enter the QuizName in which you want to add question");
		
		QuizName = sc.nextLine();
		
		try {
			QuizId=dc.search(QuizName);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("Error Accoured in seraching the Quiz \n Error is : "+ e);
			e.printStackTrace();
			System.exit(1);
		}
		// id = means no quiz with that name
		
		if (QuizId == 0) {
			System.out.println("No Quiz found with " + QuizName+ " name");
		}
		else {
			
		boolean rep = true;
		String ans;
		
			while(rep) {
				insertQuestion();	
				
				if(Question.isEmpty()) {
					System.out.println("No Question Provided");
				}
				else 
				{
					try {
						if(this.Option == null) {
							Question_Id = dc.insertQuestion(QuizId, Question, Dificulty, Topic);
							System.out.println("Inserted");
						}
						else if(this.Option != null) {
							Question_Id =  dc.insertQuestion(QuizId, Question, Option, Dificulty,Topic);
							System.out.println("Inserted");
							insertAnswer(dc);
						}
						else {
							System.out.println("Some attribute is miising in insertion");
							System.out.println("Not able to insert the Question");
						}
						}catch(SQLException e) {
							System.out.println("Not able to insert the Question");
						}
					
						
				}
				System.out.println("To enter one more question press y else n");
				ans=sc.next();
				
				if (!ans.equalsIgnoreCase("y")) {
					rep = false;
				}	
			}
		}	
		
	}

	//Getting questions from user
	private void insertQuestion( ) {
		this.Option=null;
		Scanner Sc_in = new Scanner(System.in);
		System.out.println("Enter Question ");
		
		this.Question= Sc_in.nextLine();
		
		

		System.out.println("Is it a MCQ type Question ");
		System.out.println("A for MCQ  OR  B for normal type");
		this.QuestionType=Sc_in.nextLine();
		
		if(QuestionType.equalsIgnoreCase("A")) {
			this .Option=null;
			insertOptions();
		}else {
			setDificulty();
		}
		
		
		//getting options

		
	}
	
	// For getting the options for the inserted question
	private void insertOptions() {
		
		Scanner sc_op = new Scanner(System.in);
		 Option = new String[4];
		int max = 4;
		System.out.println("Enter 4 options");
		for(int i =0 ;i<4 ; i++) {
			System.out.println("Option "+(i+1)+":  ");
			Option[i]=sc_op.nextLine();
		}	
		setDificulty();
	}

	//For Setting the difficulty level for the inserted question
	private void setDificulty() {
		
		Scanner sc_dif = new Scanner(System.in);
		System.out.println("Enter The dificulty Level");
		System.out.print("1:Easy");
		System.out.print("  2: Medium");
		System.out.println("  3: Hard");
		System.out.println("Chose 1 | 2 | 3");
		
		Dificulty=sc_dif.nextInt();
		
		setTopicName();
	}
	
	//For Setting the topic name for the inserted question
	private void setTopicName() {
		this.Topic=null;
		System.out.println("Enter the topic of question");
		Scanner sc_tn = new Scanner(System.in);
		this.Topic= sc_tn.nextLine();
		
		
	}

	//For Setting the correct answer for inserted question
	private void insertAnswer(DataConnect dc) {
		Answer ans = new Answer();
		ans.setAnswer(Question_Id,dc);
	}
}
