package fr.epita.quiz.process;

import fr.epita.quiz.connections.DataConnect;

import java.sql.SQLException;
import java.util.Scanner;

public class Answer {
	
	private int answer;
	
	// For setting up the correct answer of the inserted question
	public void setAnswer(int Question_id,DataConnect dc) {
		
		Scanner sc = new Scanner(System.in);	
		int correct_op=0;
		
		
		while ( (correct_op == 0) || correct_op >4) {
			System.out.println("Enter the correct option (1|2|3|4)");
			correct_op=sc.nextInt();
			
			if ((correct_op == 0) || correct_op >4) {
				System.out.println("Wrong Choice");
			}else {
				
			}
		}
		try {
		dc.insertAnswers(Question_id, correct_op);
		System.out.println("inserted");
		}catch(SQLException e)
		{
			System.out.println("Can not record answer");
		}
		
	}
	
	

}
