package fr.epita.quiz.connections;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import fr.epita.quiz.serv.LoadConfig;;;


public class DataConnect {
	private static String DriverClass="db.driver.class";
	private static String DBURL="db.url";
	private static String DBUserName="db.username";
	private static String DBUserPass="db.password";
	//private static Properties prop;
	private static Connection connect;
	
	public DataConnect(){
		try {
			 getProperties();
			 connect = getConnection();
		
								
		}catch(Exception e) {		
			System.out.println(e);
			System.exit(1);
		}
		
	}
	
	//For Searching and confirming that if quiz exist or not and returns quiz id. If returned id is 0 then searched quiz does not exist. 
	public int search(String QName) throws SQLException {
		

		String query = "Select ID , QUIZ_NAME From QUIZ where QUIZ_NAME = '"+QName+"'";
		int id=0;
		PreparedStatement pstmt = connect.prepareStatement(query);
	
		ResultSet rs = pstmt.executeQuery();
		while(rs.next()) {
			 id = rs.getInt("ID");
			//String QuizName = rs.getString("QUIZ_NAME");
			//System.out.println("id : " + id + " QUIZ_NAME:" +  QuizName);
		}
		
		pstmt.close();
		rs.close();
	
		// return 0 id is quiz name is not present in database
		return id;
	}

	//For inserting the quiz name in data base
	public boolean insertQuiz(String QName) throws SQLException{
		
		int id ; 
		id = search(QName);
		if (id == 0) {
			String query = "insert into QUIZ (QUIZ_NAME)  values ('" + QName.trim() + "') ";
			Statement st = connect.createStatement();
			st.executeUpdate(query);
			st.close();
			
			return true;
		}
		else {
			System.out.println("This Quiz already exist");
			return false;
		}	
	}
	
	
	//For inserting the questions with MCQ options in database and returning the inserted question id;
	public int insertQuestion(int QuizId, String Question,String[] Options , int Dificulty,String TOPIC  )  throws SQLException{
		
		int id=-1;
		String insertQuery="insert into questions(QUESTION ,OPTION_1 ,OPTION_2 ,OPTION_3 ,OPTION_4 ,QUIZ_ID ,DIFICULTY ,TOPIC )\r\n" + 
						"values('"+Question+"','"+Options[0]+"','"+Options[1]+"','"+Options[2]+"','"+Options[3]+"',"+QuizId+","+Dificulty+",'"+TOPIC+"')";
		String QuestionId="SELECT max(id) as ID FROM QUESTIONS where QUESTION = '"+Question+"'";
		Statement st = connect.createStatement();
		st.executeUpdate(insertQuery);
		ResultSet rs = st.executeQuery(QuestionId);
		while(rs.next()) {
			 id = rs.getInt("ID");
			//String QuizName = rs.getString("QUIZ_NAME");
			//System.out.println("id : " + id + " QUIZ_NAME:" +  QuizName);
		}
		rs.close();
		st.close();
		
		
		return id;
		
	}
	
	//For inserting the questions without MCQ options in database and returning the inserted question id;
	public int insertQuestion(int QuizId, String Question, int Dificulty,String TOPIC  )  throws SQLException{
		
		
		//String query="insert into questions(QUESTION ,QUIZ_ID ,DIFICULTY ,TOPIC )\r\n" + 
			//			"values('"+Question+"'"+QuizId+","+Dificulty+",'"+TOPIC+"')";
		int id =-1;
		String InsertQuery="insert into questions (QUESTION ,QUIZ_ID ,DIFICULTY ,TOPIC )\r\n" + 
						"values('"+Question+"',"+QuizId+","+Dificulty+",'"+TOPIC+"')";
		
		String QuestionId="SELECT max(id) as ID FROM QUESTIONS where QUESTION = '"+Question+"'" ;
		Statement st = connect.createStatement();
		st.executeUpdate(InsertQuery);
		ResultSet rs = st.executeQuery(QuestionId);
		while(rs.next()) {
			 id = rs.getInt("ID");
			//String QuizName = rs.getString("QUIZ_NAME");
			//System.out.println("id : " + id + " QUIZ_NAME:" +  QuizName);
		}
		rs.close();
		st.close();
		
		return id;
		
	}
	
	// for inserting the correct answer for the inserted question. 
	public void insertAnswers(int Q_id,int Cor_op)throws SQLException {
		String InsertQuery="insert into answers(Question_ID,CORRECT_OPTION)\r\n" + 
							"values("+Q_id+","+Cor_op+") ";
		
		Statement st = connect.createStatement();
		st.executeUpdate(InsertQuery);
		st.close();
	}
	
	//For inserting Student information including their marks.
	public void insertStudentInfor(String Name , String Qname , int Marks_Scored) throws SQLException {
		String insert = "insert into Students (STUDENT_NAME ,QUIZ_ATTEMTED ,MARKS_OBTAINED )\r\n" + 
						"values('"+Name+"','"+Qname+"',"+Marks_Scored+")";
		
		Statement st = connect.createStatement();
		st.executeUpdate(insert);
		st.close();
		
	}
	
	//Getting questions Id's related to a particular quiz and difficulty level from Database.
	public List getQuestionIds(int Quiz_Id, int Dificulty)throws SQLException {
		
		
		String query ="SELECT * FROM QUESTIONS  where Quiz_ID ="+Quiz_Id+" and Dificulty = "+Dificulty;
		List<Integer> al=new ArrayList<Integer>(); 
		PreparedStatement pstmt = connect.prepareStatement(query);
	
		ResultSet rs = pstmt.executeQuery();
		while(rs.next()) {
			
			 al.add(rs.getInt("ID"))  ;
			//String QuizName = rs.getString("QUIZ_NAME");
			//System.out.println("id : " + id + " QUIZ_NAME:" +  QuizName);
		}
		pstmt.close();
		rs.close();
		return al ;
		
	}
	
	//Getting the question depending on the question id.
	public String  getQuestion(int Quest_Id) throws SQLException {
		String query = "Select Question From Questions Where id = "+Quest_Id;
		String  Q=null;
		PreparedStatement pstmt = connect.prepareStatement(query);
	
		ResultSet rs = pstmt.executeQuery();
		while(rs.next()) {
			 Q = rs.getString("Question");
			//String QuizName = rs.getString("QUIZ_NAME");
			//System.out.println("id : " + id + " QUIZ_NAME:" +  QuizName);
		}
		
		pstmt.close();
		rs.close();
		
		return Q;
	
	}

	//Retrieving correct option depending on the question id.
	public int CorrectAnswer( int Ques_Id) throws SQLException {
		String query = "SELECT * FROM  ANSWERS  where Question_id ="+ Ques_Id;
		int  ans=-1;
		PreparedStatement pstmt = connect.prepareStatement(query);
	
		ResultSet rs = pstmt.executeQuery();
		while(rs.next()) {
			 ans = rs.getInt("CORRECT_OPTION" );
		}		
		pstmt.close();
		rs.close();
		
		return ans;
	}
	
	//For getting the options with respect to their questions
	public String[] getQptions(int Quest_Id) throws SQLException{
		String query = "SELECT OPTION_1 ,OPTION_2 ,OPTION_3 ,OPTION_4  FROM QUESTIONS where id = "+Quest_Id;
		String[] Options=new String[4];
		PreparedStatement pstmt = connect.prepareStatement(query);
		
		
		ResultSet rs = pstmt.executeQuery();
		while(rs.next()) {
			 Options[0] = rs.getString("OPTION_1");
			 Options[1] = rs.getString("OPTION_2");
			 Options[2] = rs.getString("OPTION_3");
			 Options[3] = rs.getString("OPTION_4");
			//String QuizName = rs.getString("QUIZ_NAME");
			//System.out.println("id : " + id + " QUIZ_NAME:" +  QuizName);
		}
		
		pstmt.close();
		rs.close();
		
		return Options;
	}
	
	//For Retrieving the Student's name ,quiz attempted and marks obtained.
	public  String[][] Students_attempted(String Qname)throws SQLException {
		String[][] values;
		int rows=0;
		String query="SELECT STUDENT_NAME ,QUIZ_ATTEMTED ,MARKS_OBTAINED  FROM STUDENTS where QUIZ_ATTEMTED ='"+Qname+"'";
		String countQuery="Select count(STUDENT_NAME) as cou from STUDENTS where QUIZ_ATTEMTED ='"+Qname+"'";	
		PreparedStatement pstmt = connect.prepareStatement(query);
		PreparedStatement psForCount = connect.prepareStatement(countQuery);
		
		ResultSet rsCount = psForCount.executeQuery();
		while(rsCount.next()) {
			rows =rsCount.getInt("cou");
		}
		
		ResultSet rs = pstmt.executeQuery();
		values=new String[rows][3];
		int counter =0;
		while(rs.next()) {
			values[counter][0] = rs.getString("STUDENT_NAME");
			values[counter][1] = rs.getString("QUIZ_ATTEMTED");
			values[counter][2] = rs.getString("MARKS_OBTAINED");
			counter =counter+1;
		}
		
		pstmt.close();
		rs.close();
		return values;
	} 
	
	//for closing the Database Connection
	public void closeConnect() {
		try {
		connect.close();
		}catch (SQLException e)
		{
			System.out.println("Cannot close the Database Connection \n Error is :- "+e);
		}
	}
	
	// getting properties from config\Database.properties file
	private void getProperties()  throws IOException{
		LoadConfig prop = LoadConfig.getInstance() ;
		DriverClass= prop.getConfigurationValue(DriverClass, "Noclass");
		DBURL = prop.getConfigurationValue(DBURL, "NoURL");
		DBUserName=prop.getConfigurationValue(DBUserName, "NoName");
		DBUserPass=prop.getConfigurationValue(DBUserPass, "NoPass");
		
	}

	// Getting the connection
	private static Connection getConnection() throws SQLException {
		
		Connection connect = DriverManager.getConnection(DBURL,DBUserName,DBUserPass);
		return connect;
	}
	
	
}
