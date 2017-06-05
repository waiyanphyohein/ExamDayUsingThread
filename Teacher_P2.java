import java.util.Iterator;
import java.util.Random;
import java.util.Vector;
import java.util.concurrent.Semaphore;

public class Teacher_P2 extends Thread 
{
	Random Randomizer = new Random();
	public static long time = System.currentTimeMillis();			// the initial starting time of execution of the code.
	public static volatile Vector<Student_P2> que_std;				// To give out student record.
	public static volatile int EXAM_1_TOTAL_STD = 0;				// Total amount of students who took the exam 1
	public static volatile int EXAM_2_TOTAL_STD = 0;				// Total amount of students who took the exam 2
	
	
	public static volatile boolean ARR_CLASS = false;						// Boolean used to signal each other rather than using semaphore(which is the same but more complicated)
	public static volatile boolean EXAM_START = false;
	public static volatile boolean EXAM_END  = false;
	public static volatile boolean FINISH_GRADE = false;
	public static volatile boolean CAN_GO_HOME = false;
	
	public static final int EXAM_1_TIME_LIMIT = 3000;						// EXAM DURATION LIMIT
	public static final int EXAM_2_TIME_LIMIT = 3000;
	public static final int EXAM_1_TIME = 5000;								//Time the exam will start originally
	public static final int EXAM_2_TIME = 10000;
	
	
	public static long EXAM_1_START_TIME  = 0;								//Time the exam actually starts
	public static long EXAM_2_START_TIME = 0;
//*****************************************************************************************************************************************	
	
	public Teacher_P2(int id)											// DEFAULT CONSTRUCTOR
	{
		que_std = new Vector<Student_P2>();
		setName("Teacher"+id);
	}
	
//*****************************************************************************************************************************************	
	
	public void run()													// This is run() method for Teacher thread which is responsible for controlling two exam sections.
	{																	// Please note that all the description of the messages are pretty much self-explanatory.
		
		msg("is coming to classroom.");
		Coming();
		msg("has arrived to school and opening the classroom.");
		msg("let students into classroom.");
		//Teacher thread is coming to classroom.(INITIAL TIME + RANDOM_TIME GENERATOR NUMBER).
	
		ARR_CLASS = true;					//(NOTE: this is mainly used to give out the correct message rather than blocking students.)
		msg("is waiting for students to get into classroom.");
		//Will release StudentBlc when teacher has arrived to classroom.
		Release(Student_P2.StudentBlc);
		
		
				
		Student_P2.Exam_counter++;											
		//This is incremented as according to exam section.
		while(!EXAM_START)																		// This part of the code will make teacher thread to wait for signal from student class to start handing out the exam.
		{	
			if((System.currentTimeMillis()-time) >= EXAM_1_TIME)
			{
				EXAM_START = true;
				break;
			}
		}
		EXAM_1_START_TIME = System.currentTimeMillis() - time;
		ARR_CLASS = false;
		// RESET ARRIVE CLASS FOR NEXT EXAM SECTION HERE IN ORDER TO AVOID CONFLICT WITH DELAYED THREADS.
		
		System.out.println("\n\n**********BEGINNING OF EXAM 1**********\n\n");
		
		msg("starts handing out the exam sheets.");
		msg("is sleeping now until exam ends.");
		//Pretty much self-explanatory.
		
		Sleeping(EXAM_1_TIME_LIMIT);
		System.out.println("\n\n**********ENDING OF EXAM 1**********\n\n");
		
		// Will sleep for fixed amount of time for the exam to end.
		Student_P2.StudentBlc = new Semaphore(0);
		// RESET SEMAPHORE FOR NEXT EXAM SECTION HERE IN ORDER TO AVOID CONFLICT WITH DELAYED THREADS.
		
		
		EXAM_START = false;
		
		msg("starts collecting exams now.");
		Sleeping(100);
		msg("has finished collecting and let students leave from classroom.");
		
		EXAM_END = true;																		//Signaling student class that the exam is ended and they may now leave from classroom.
		Sleeping(500);
		
		msg("is closing the door to go back to office to take a break.");
		Sleeping(300);
		// Waiting for students to leave from classroom completely.
		
		msg("is coming to classroom to prepare for exam 2");
		msg("has opened the classroom and let students come into classroom.");		
		msg("is waiting for students to get into classroom.");
		// Pretty much self-explanatory
		
		Student_P2.STD_QUEU = new Semaphore(Project2_Main.CLASS_CAPACITY,false);//-----					This is done due to incoherence with the students order. (NOTE: This is intentional. Do not remove it)
		ARR_CLASS = true;														//		>				Resetting the values of boolean for next exam section
		EXAM_END = false;														//-----
		
		Release(Student_P2.StudentBlc);
	
		msg("EXAM_START: "+EXAM_START);
		Student_P2.Exam_counter++;
		//This is incremented as according to exam section.
		
		while(!EXAM_START)																		// This part of the code will make teacher thread to wait for signal from student class to start handing out the exam.
		{	
			if((System.currentTimeMillis()-time) >= EXAM_2_TIME)
			{
				EXAM_START = true;
				break;
			}
		}
		EXAM_2_START_TIME = System.currentTimeMillis() - time;
		//To record actual exam 2 starting time.
		System.out.println("\n\n**********BEGINNING OF EXAM 2**********\n\n");
		
		msg("starts handing out the exam sheets.");
		msg("is sleeping now until exam ends.");
		//Pretty much self-explanatory
		
		Sleeping(EXAM_2_TIME_LIMIT);	// Waiting for exam 2 to finish.
		System.out.println("\n\n**********ENDING OF EXAM 2**********\n\n");
		
		msg("starts collecting exams now.");
		Sleeping(100);
		msg("has finished collecting and let students leave from classroom.");
		
		EXAM_END = true;
		
		Sleeping(500);
		msg("is grading all the exams.");
		Sleeping(1000);
		msg("has finished grading and giving back the result to students.");
		Student_P2.StudentBlc = new Semaphore(1);
		EXAM_TAKEN_RECORD();
		EXAM_1Report();
		EXAM_2Report();
		
		FINISH_GRADE = true;
		System.out.println("\n\n**********GROUPING SECTION**********\n\n");
		while(!CAN_GO_HOME)
		{	}
		msg("goes back home.");
	}
//*****************************************************************************************************************************************	
	
	 public void msg(String m) {				// This will print out the message of calling threads on detail of action(s) it is about to do.
	 System.out.println("["+(System.currentTimeMillis()-time)+"] "+getName()+": "+m);
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
		
	public void Sleeping(int Time)				// A method for small amount of time of sleep to threads
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
			return Randomizer.nextInt(1000)+1000;
		}
	
//*****************************************************************************************************************************************	
	
	public void EXAM_TAKEN_RECORD()			// A method that will print out the record of students exam_taken.
	{
		System.out.println("\n\n*********STUDENT_EXAM_TAKEN_REPORT*********\n\n");
		Iterator<Student_P2> Temp_Student = que_std.iterator();
		while(Temp_Student.hasNext())
		{
			Student_P2 tmp = Temp_Student.next();
			System.out.println(tmp.getName()+"'s Total Exam Taken: " + tmp.TOTAL_EXAM_TAKEN);			
		}

		System.out.println("\n");
	}
//*****************************************************************************************************************************************	

	public static void EXAM_1Report()	//Will print Exam 1 Report.
	{
		
		Iterator<Student_P2> Temp_Student = que_std.iterator();
		System.out.println("\n");
		System.out.println("********** EXAM_1 REPORT **********\n\n");
		System.out.println("Exam 1 Starting Time(Original): "+EXAM_1_TIME);
		System.out.println("Exam 1 Starting Time(Actual): "+ EXAM_1_START_TIME);
		System.out.println("Exam 1 Duration Time: "+Teacher_P2.EXAM_1_TIME_LIMIT);
		System.out.println("TOTAL STUDENT WHO TOKE THE EXAM: "+ (EXAM_1_TOTAL_STD));
		System.out.println("STUDENTS WHO MISSED THE EXAM: "+ (Project2_Main.STUDENT_TOTAL-EXAM_1_TOTAL_STD));
		System.out.println("\n	Student-Grade	\n");
		while(Temp_Student.hasNext())	
		{
			Student_P2 tmp = Temp_Student.next();
			System.out.println(tmp.getName()+": "+tmp.EXAM_GRADE_1);
		}
		System.out.println("\n");
	}
//*****************************************************************************************************************************************
	public static void EXAM_2Report()	//Will print Exam 2 Report.
	{
		Iterator<Student_P2> Temp_Student = que_std.iterator();
		System.out.println("\n");
		System.out.println("********** EXAM_2 REPORT **********\n\n");
		System.out.println("Exam 1 Starting Time(Original): "+EXAM_2_TIME);
		System.out.println("Exam 1 Starting Time(Actual): "+ EXAM_2_START_TIME);
		System.out.println("Exam 1 Duration Time: "+Teacher_P2.EXAM_2_TIME_LIMIT);
		System.out.println("TOTAL STUDENT WHO TOKE THE EXAM: "+ (EXAM_2_TOTAL_STD));
		System.out.println("STUDENTS WHO MISSED THE EXAM: "+ (Project2_Main.STUDENT_TOTAL-EXAM_2_TOTAL_STD));
		System.out.println("\n	Student-Grade	\n");
		while(Temp_Student.hasNext())	
		{
			Student_P2 tmp = Temp_Student.next();
			System.out.println(tmp.getName()+": "+tmp.EXAM_GRADE_2);
		}
		System.out.println("\n");
	}
//*****************************************************************************************************************************************	
}
