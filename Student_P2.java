import java.util.Random;
import java.util.concurrent.Semaphore;

public class Student_P2 extends Thread{																// Beginning of Student_P2 class
	
	
	Random Randomizer = new Random();
	
	public static volatile Semaphore StudentBlc = new Semaphore(0,true);							// This semaphore will be used to block students before they get into classroom.
	public static volatile Semaphore Student_Announce = new Semaphore(1);							// This semaphore will be used to have mutual exclusion when they go into classroom.
	public static volatile Semaphore STD_QUEU = new Semaphore(Project2_Main.CLASS_CAPACITY,false);	// This semaphore will be used to keep track of Students amount going into classroom.
	public static volatile Semaphore GROUP_SEMA = new Semaphore(0,false);	// This semaphore will be used to group students at the end of the all exam sections to go back home.

	public static int Group_Counter = 1;
	public static int Counter=0;
	public static int Exam_counter = 0;																// This is for exam section tracker.
	public static volatile long time = System.currentTimeMillis();									// this is for initial time of start.
	
	public int TOTAL_EXAM_TAKEN = 0;																// This integer will keep track of how many exams this student has taken.
	public int EXAM_GRADE_1 = 0;																	// the grade of each individual student record. (EXAM 1 Score)
	public int EXAM_GRADE_2 = 0;																	//												(EXAM 2	Score)
	
//*****************************************************************************************************************************************	
	
	public Student_P2(int id)																		// DEFAULT CONSTRUCTOR
	{	
		setName("Student_"+id);
	}
	
//*****************************************************************************************************************************************	
	
	public void run()																				// Thread Run() method to run the code accordingly to the instruction. 
	{				
		msg("coming to classroom from home.");
		Coming();
		// Coming to classroom in different timeframe of sleep.
			
		msg("has arrived to classroom.");
		
		if(!Teacher_P2.ARR_CLASS)																	// Will wait for signal from teacher class to preceed to next line of codes.
		{
			msg("is waiting for instructor to arrive.");
			Block(StudentBlc);
			
		}
		
		Release(StudentBlc);																	// To release other threads by released threads from queue.
		
		ENT_CLASS();																			// This is the calling of ENT_CLASS() method that will let students enter in classroom.
		
		if(!Teacher_P2.ARR_CLASS)
		{
			msg("is waiting for instructor to arrive.");
			Block(StudentBlc);
			
		}
		Release(StudentBlc);																	// To release other threads by released threads from queue.
			
		ENT_CLASS();
		msg("is waiting grade to come out.");
		while(!Teacher_P2.FINISH_GRADE)
		{ 	}
		msg("is grouping now to go back home.");
		Waiting(500);
		
		
		GROUP_STUDENT();
	}
	
//*****************************************************************************************************************************************	

	public void msg(String m) 				// This is string message to print out the time and thread info for which thread is using this method.
	{
		System.out.println("["+(System.currentTimeMillis()-time)+"] "+getName()+": "+m);
	}
	
//*****************************************************************************************************************************************	
	public void ENT_CLASS()					// This method will let students enter into classroom up until the class_capacity limit. Will also let teacher know that the exam can be started.
	{
		Block(Student_Announce);												// This semaphore for mutual exclusion purpose in order to avoid conflict.
		
		if(!Teacher_P2.EXAM_START && STD_QUEU.availablePermits() > 0)			//If there is still available spot for student to come in, it will let them in and increment the semaphore.
		{										
			TOTAL_STD_COUNTER();
			msg("is entering into classroom.");	
			
			Block(STD_QUEU);
			if(STD_QUEU.availablePermits()==0)									// Last student who got into classroom let teacher know that exam is ready to begin.
			{
				msg("let teacher know the exam needs to start.");
				Teacher_P2.EXAM_START = true;
			}
			
			Release(Student_Announce);											// Mutual exclusion release
			
			this.TOTAL_EXAM_TAKEN++;
			while(!Teacher_P2.EXAM_START)
			{		}
			
			Waiting(100);												
			//Wait for teacher to finish displaying all the message.
			msg("is getting the exam sheet.");
			Waiting(Randomizer.nextInt(3000));									// Students taking exam time
			
			msg("has finished taking the exam "+Exam_counter+".");
			msg("is waiting for teacher to give signal to leave from classroom.");
			
			Grade_Exam();
			while(!Teacher_P2.EXAM_END)
			{	}
			msg("is leaving from classroom.");
			Release(STD_QUEU);
			msg("is talking to other students about exam.");
			msg("is taking a break.");
			Waiting(1000);
		}
		
		else 																// This is for those students who couldn't get into classroom. They will give out message saying they couldn't get into classroom due to lateness.
		{
			Release(Student_Announce);
			WAIT_OUTSIDE();													// This is a method that will make late students wait outside of classroom.
			msg("is talking to other students about exam.");
		}

		
		
	}
//*****************************************************************************************************************************************	
	public void WAIT_OUTSIDE()
	{
		msg("is waiting for another exam section due to lateness.");
		while(!Teacher_P2.EXAM_END)
		{	}
	}
//*****************************************************************************************************************************************		
	public void Block(Semaphore temp)		// For mutual Exclusion purpose and high synchronization
	{										//A method which is P(Sempahore);
		try 
		{	
			temp.acquire();
			//This will decrement Semaphore counter.
		} 
		catch (InterruptedException e) 
		{
			e.printStackTrace();
			//This will create error message if interrupted or some threads have been released due to some reasons.
		}
	}
	
//*****************************************************************************************************************************************
	public void Release(Semaphore temp)		//A method which is V(Semaphore);
	{
		temp.release();
	}
	
//*****************************************************************************************************************************************	

	public void Coming()				// A method for small amount of time of sleep to threads
	{
		try
		{	
			sleep(RANDOM_TIME_GENERATOR());	
			//Will make the thread declaring it sleep.
		}
		catch(InterruptedException e)
		{
			e.printStackTrace();
			//If interrupt is occurred, it will generate error message.
		}
		
	}
	
//*****************************************************************************************************************************************	
	
	public void Waiting(int Time)				// A method for small amount of time of sleep to threads
	{
		try
		{	
			sleep(Time);	
			//Will make the thread declaring it sleep.
		}
		catch(InterruptedException e)
		{
			e.printStackTrace();
			//If interrupt is occurred, it will generate error message.
		}
		
	}
	
//*****************************************************************************************************************************************	
	
	public int RANDOM_TIME_GENERATOR()		// A method that generates random_integer for time of sleep
	{
		return Randomizer.nextInt(1000)+100;
	}
//*****************************************************************************************************************************************	
	public void Grade_Exam()				// A method that will give out the grade of students.
	{
		if(Exam_counter == 1)
			this.EXAM_GRADE_1 = Randomizer.nextInt(90)+10;
		else if(Exam_counter == 2)
			this.EXAM_GRADE_2 = Randomizer.nextInt(90)+10;
	}
//*****************************************************************************************************************************************	
	public void TOTAL_STD_COUNTER()					// Counting amount of students got into classroom for each exam section.
	{
		Block(StudentBlc);
		if(Exam_counter == 1)
			Teacher_P2.EXAM_1_TOTAL_STD++;
		else if(Exam_counter == 2)
			Teacher_P2.EXAM_2_TOTAL_STD++;
		Release(StudentBlc);
	}
//*****************************************************************************************************************************************	
	public void GROUP_STUDENT()					// This is a method that will let all student threads to group in the order as they come in. Also be noted that I have waited time before getting to this section to due high incoherence.
	{
		Block(StudentBlc);
		Counter++;
		if(Counter%Project2_Main.GROUP_SIZE == 0)									// This section is when a group is full with group size.
		{
			msg("Group: "+Group_Counter+"; "+this.getName());
			msg("Group: "+Group_Counter+ " students have left from classroom.");
			Group_Counter++;
			Release(StudentBlc);
			for(int i = 0; i < Project2_Main.GROUP_SIZE;i++)
				Release(GROUP_SEMA);
		}
		else																		// This section is when a group is less than group size.
		{	
			if(Project2_Main.STUDENT_TOTAL - Counter < Project2_Main.GROUP_SIZE)	// If students in classroom is less than group size, they will leave without grouping.
			{
				if(Project2_Main.STUDENT_TOTAL == Counter)							// This is for last student who is about to leave from classroom but will notify teacher that he/she can leave.
				{
					msg("is a last student to leave from classroom.");
					msg("reminds teacher to go back home.");
					msg("goes back home.");
					Release(StudentBlc);
					Teacher_P2.CAN_GO_HOME = true;
				}
				else																// This is for those before the last students.
				{
					msg("is leaving without grouping because there are less than group size of students left in classroom.");
					Release(StudentBlc);
				}
			}
			else																	// This will be executed when there are enough students in classroom to group of group size.
			{				
				msg("Group: "+Group_Counter+"; "+this.getName());
				Release(StudentBlc);
				Block(GROUP_SEMA);
			}
		}
	}
}
