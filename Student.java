import java.util.Iterator;
import java.util.Random;
import java.util.Vector;

public class Student extends Thread{

	private Random RAN_GEN = new Random();
	
	public static volatile boolean STUDENT_READY = false;
	public static volatile int TEM_EXAM_TIME = 0;
	public static volatile int counter = 0;
	
	
	
	public volatile boolean STUDENT_TOOK_THE_FIRST_EXAM = false;
	public volatile boolean STUDENT_TOOK_THE_SECOND_EXAM = false;
	public volatile boolean STUDENT_TOOK_THE_THIRD_EXAM = false;
	public volatile int TOTAL_EXAM_TAKEN = 0;
	
	private double Exam_1_Grade= 0;
	private double Exam_2_Grade= 0;
	private double Exam_3_Grade= 0;
	
	public static volatile Vector<Student> STUDENT_RECORD= new Vector<Student>();
	public static volatile Vector<Student>THE_ORDER = new Vector<Student>();
	
	public static int EXAM_1_TOTAL_STUDENT = 0;
	public static int EXAM_2_TOTAL_STUDENT = 0;
	public static int EXAM_3_TOTAL_STUDENT = 0;
	
	public static long time = System.currentTimeMillis() + 1;
	

	//*********************************************
	//DEFAULT CONSTRUCTOR FOR STUDENT
	
	public Student(int id)	
	{
		//Thread Section (Constructor)		
		setName("Student- "+id);
	}	
	//*********************************************
	// MAIN THREAD CLASS FOR STUDENT
	
	public void run()
	{
		msg("is on the way to classroom.");
		Is_Waiting();
		msg("has arrived to school.");
		
		while(!Teacher.TEACHER_ARRIVED)
		{		
			msg("is going around school while waiting for teacher to arrive.");
			Is_Waiting();
			msg("has arrived to classroom.");
			if(!Main.TeacherArrived())
				msg("is now going around school campus becausea teacher is still not there yet.");
		}
			
	
//*****************************************************************************************		
		
		if(Main.ClASS_LIMIT <= 9 && !IS_TIME()) //For Students who are going take exam 1.
		{					
				msg("is now entering into classroom and preparing to take a test.");
				SET_PRIORITY_STD();
				
				if(IS_FULL() || IS_TIME())
					IS_READY();
				
				EXAM_1_TOTAL_STUDENT=Main.ClASS_LIMIT;
				
				this.STUDENT_TOOK_THE_FIRST_EXAM = true;	
				this.TOTAL_EXAM_TAKEN++;
				
				GRADE_EXAM();
				while(!Main.DURING_EXAM)			//BUSY WAIT
				{	}
				
				msg("has started taking exam 1.");
				Is_TakingExam(RAN_GEN.nextInt(1500)+1500);
				msg("has finished the exam and waiting at the line to leave from the classroom.");
				
				while(Main.DURING_EXAM)				//BUSY WAIT
				{	}
				
				while(!THE_ORDER.get(counter).equals(this) && !isInterrupted())
				{	}
				
				if(!isInterrupted())
					interrupt();
				
				msg("has left the room.");
				counter++;
				RESET();
				msg("is taking a break.");
				Is_Waiting();
				
		}
			
		else
		{
			
			LATE_OR_FULL();
					
			Thread.yield();
			Thread.yield();		
				
			while(!Main.DURING_EXAM)
			{}
			
			while(Main.DURING_EXAM);
			{}
			
			msg("is waiting for second exam to take place.");	
			Is_Waiting();
			
		}		
		
		
//*****************************************************************************************		

		msg("is waiting for teacher for exam 2.");
		
		while(!Teacher.TEACHER_ARRIVED)
		{ 		}
		
		if(this.STUDENT_TOOK_THE_FIRST_EXAM)
		{	Is_Waiting();	}
		
		
		
		if(Main.ClASS_LIMIT <= 9 && !IS_TIME()) //For Students who are going take exam 1.
		{					
				msg("is now entering into classroom and preparing to take a test.");
				SET_PRIORITY_STD();
				
				if(IS_FULL() || IS_TIME())
					IS_READY();
				
				EXAM_2_TOTAL_STUDENT=Main.ClASS_LIMIT;
				
				this.TOTAL_EXAM_TAKEN++;
				this.STUDENT_TOOK_THE_SECOND_EXAM = true;
				
				GRADE_EXAM();
				while(!Main.DURING_EXAM)			//BUSY WAIT
				{	}
				Is_Waiting();
				
				msg("has started taking exam 2.");
				Is_TakingExam(RAN_GEN.nextInt(1500)+1500);
				msg("has finished the exam and waiting at the line to leave from the classroom.");
				
				while(Main.DURING_EXAM)				//BUSY WAIT
				{	}
				
				while(!THE_ORDER.get(counter).equals(this))
				{	}
				
				if(!isInterrupted())
					interrupt();
				
				msg("has left the room.");
				counter++;
				RESET();
				msg("is taking a break.");
				Is_Waiting();
				
		}
			
		else
		{
			
			LATE_OR_FULL();
					
			Thread.yield();
			Thread.yield();		

			while(!Main.DURING_EXAM);
			
			while(Main.DURING_EXAM);
			
			msg("is waiting for third exam to take place.");	
			Is_Waiting();
		}		
	
//*****************************************************************************************		
		
		
		msg("is waiting for teacher for exam 3.");
		
		while(!Teacher.TEACHER_ARRIVED)
		{ 	}
		
		if(this.STUDENT_TOOK_THE_SECOND_EXAM)
		{	Is_Waiting();	}
		
		
			
		if(Main.ClASS_LIMIT <= 9 && !IS_TIME()) //For Students who are going take exam 1.
		{					
				msg("is now entering into classroom and preparing to take a test.");
				SET_PRIORITY_STD();
				
				if(IS_FULL() || IS_TIME())
					IS_READY();
				
				EXAM_3_TOTAL_STUDENT = Main.ClASS_LIMIT;
				
				this.TOTAL_EXAM_TAKEN++;
				this.STUDENT_TOOK_THE_THIRD_EXAM = true;
				
				GRADE_EXAM();
				while(!Main.DURING_EXAM)			//BUSY WAIT
				{	}
				Is_Waiting();
				
				msg("has started taking exam 3.");
				Is_TakingExam(RAN_GEN.nextInt(1500)+1500);
				msg("has finished the exam and waiting at the line to leave from the classroom.");
				
				while(Main.DURING_EXAM)				//BUSY WAIT
				{	}
				
				while(!THE_ORDER.get(counter).equals(this))
				{	}
				
				if(!isInterrupted())
					interrupt();
				
				msg("has left the room.");
				counter++;
				RESET();
				Is_Waiting();
							
		}
			
		else
		{
			
			LATE_OR_FULL();
			msg("has to wait for exam grade outside of classroom.");	
					
			Thread.yield();
			Thread.yield();		
			
			while(!Main.DURING_EXAM)
			{ }
			
			while(Main.DURING_EXAM)
			{ }

			Is_Waiting();
			
		}	
		
//*****************************************************************************************		
		
		msg("is waiting for exam grade.");	
		while(!Teacher.TEACHER_FINISH_GRADE);
		
		while(!STUDENT_RECORD.get(counter).equals(this))
		{ 
			Is_Waiting();
		}
		msg("has gone back home.");				
		Teacher.STUDENT_LEFT = true;
		
		
		return; // Careful this return method is required if the program doesn't want to have any error.
			
	}

//*****************************************************************************************		
	public synchronized void Is_Waiting()		//As mentioned this is to put sleep for a short amount of time to calling thread.
	{
		try
		{
			sleep(RAN_GEN.nextInt(500)+100);
		}
		catch(InterruptedException e)
		{
			
		}
	}
	//*********************************************
	
	public synchronized void Is_TakingExam(int Time)	// Will take exam for given random time input for calling thread.
	{
		try
		{
			sleep(Time);
		}
		catch(InterruptedException e)
		{
			
		}
	}
	//*********************************************
	
	public synchronized void msg(String m)		// Outputting message for each thread action
	{
		System.out.println("["+(System.currentTimeMillis()-time)+"]"+getName()+": "+m);
	}
	//*********************************************
	
	public synchronized double GRADE_OF_STUDENT()	// Random generator for grade
	{
		return (RAN_GEN.nextInt(90)+10);
	}
	//*********************************************
	
	public static synchronized void IS_READY()	//If both Is_Full() and/or Is_time() is satisfied, change the state to ready as well as return to default after the section.
	{
		//Tell if the student is ready or not at the same time turn it to true or false if the opposite.
		STUDENT_READY = (STUDENT_READY == true) ? false : true;
		
	}
	//*********************************************
	public static synchronized boolean IS_FULL()		// Determine if the classroom is full
	{
		//Whether the class is full (NOTE: We already included a teacher in the number.)
		return Main.ClASS_LIMIT == 9;
	}
	
	//*********************************************
	public static synchronized boolean IS_TIME()		// Determine if the exam is upon.
	{
		//CONDITION TRANSFERRING EXAM STARTING TIME.
		
		//RETURN WHETHER THE EXAM IS STARTED
		if((System.currentTimeMillis() - time) >= TEM_EXAM_TIME)
			return true;
		else
			return false;
	}
	
	
	
	//*********************************************
	public synchronized void LATE_OR_FULL()			// Will output the reason of waiting outside of classroom.
	{
		if((System.currentTimeMillis() - time) >= TEM_EXAM_TIME)
			msg("has to wait outside because the exam has started.");
		else
			if(Main.ClASS_LIMIT >= Main.CLASS_CAPACITY)
				msg("has to wait outside because the class is full.");
	}
	
	//*********************************************
	public static synchronized void EXAM_1Report()	//Will print Exam 1 Report.
	{
		
		Iterator<Student> Temp_Student = STUDENT_RECORD.iterator();
		System.out.println("\n");
		System.out.println("********** EXAM_1 REPORT **********\n\n");
		System.out.println("Exam 1 Starting Time: "+Main.Exam_1_StartingTime);
		System.out.println("Exam 1 Duration Time: "+Teacher.EXAM_1_TIME_LIMIT);
		System.out.println("TOTAL STUDENT WHO TOKE THE EXAM: "+ (EXAM_1_TOTAL_STUDENT-1));
		System.out.println("STUDENTS WHO MISSED THE EXAM: "+ (Main.STUDENT_TOTAL-THE_ORDER.size()));
		System.out.println("\n	Student-Grade	\n");
		while(Temp_Student.hasNext())	
		{
			Student tmp = Temp_Student.next();
			System.out.println(tmp.getName()+": "+tmp.Exam_1_Grade);
		}
		System.out.println("\n");
	}
	
	//*********************************************
	public static synchronized void EXAM_2Report()	//Will print Exam 2 Report.
	{
		Iterator<Student> Temp_Student = STUDENT_RECORD.iterator();
		System.out.println("\n");
		System.out.println("********** EXAM_2 REPORT **********\n\n");
		System.out.println("Exam 2 Starting Time: "+Main.EXAM_2_STARTING_TIME );
		System.out.println("Exam 2 Duration Time: "+Teacher.EXAM_2_TIME_LIMIT);
		System.out.println("TOTAL STUDENT WHO TOKE THE EXAM: "+ (EXAM_2_TOTAL_STUDENT-1));
		System.out.println("STUDENTS WHO MISSED THE EXAM: "+ (Main.STUDENT_TOTAL-THE_ORDER.size()));
		System.out.println("\n	Student-Grade	\n");
		while(Temp_Student.hasNext())	
		{
			Student tmp = Temp_Student.next();
			System.out.println(tmp.getName()+": "+tmp.Exam_2_Grade);
		}
		System.out.println("\n");
	}
	
	//*********************************************
	public static synchronized void EXAM_3Report()	//Will print Exam 3 Report.
	{
		Iterator<Student> Temp_Student = STUDENT_RECORD.iterator();
		System.out.println("\n");
		System.out.println("********** EXAM_3 REPORT **********\n\n");
		System.out.println("Exam 3 Starting Time: "+Main.EXAM_3_STARTING_TIME );
		System.out.println("Exam 3 Duration Time: "+Teacher.EXAM_3_TIME_LIMIT);
		System.out.println("TOTAL STUDENT WHO TOKE THE EXAM: "+ (EXAM_3_TOTAL_STUDENT-1));
		System.out.println("STUDENTS WHO MISSED THE EXAM: "+ (14-THE_ORDER.size()));
		System.out.println("\n	Student-Grade	\n");
		while(Temp_Student.hasNext())	
		{
			Student tmp = Temp_Student.next();
			System.out.println(tmp.getName()+": "+tmp.Exam_3_Grade);
		}
		System.out.println("\n");
	}
	
	//*********************************************
	public void GRADE_EXAM()		// Will generate random grade for students who took the exam.
	{
		if(Main.EXAM_SECTION == 1)	
			Exam_1_Grade = GRADE_OF_STUDENT();
		else if(Main.EXAM_SECTION == 2)	
			Exam_2_Grade = GRADE_OF_STUDENT();
		else if(Main.EXAM_SECTION == 3)	
			Exam_3_Grade = GRADE_OF_STUDENT();
	}
	
	//*********************************************
	public synchronized void RESET()	//Will reset all the order, the counter amd boolean ready state to initial state.
	{
		if(counter >= 9)
		{
			THE_ORDER.clear();
			counter = 0;
			IS_READY();			
		}
	}
	
	//********************************************
	public synchronized void SET_PRIORITY_STD()	//Setting Priority and resetting to default
	{		
		this.setPriority(Main.ClASS_LIMIT++);
		THE_ORDER.add(this);
		Is_Waiting();
		this.setPriority(NORM_PRIORITY);		
		
	}
	//********************************************


}
