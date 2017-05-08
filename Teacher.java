import java.util.Iterator;
import java.util.Random;

public class Teacher extends Thread{
	
	Random Randomizer = new Random();
	public static long time = System.currentTimeMillis();
	
	
	public static volatile boolean TEACHER_ARRIVED = false;
	public static volatile boolean TEACHER_FINISH_GRADE = false;
	public static volatile boolean STUDENT_LEFT = false;
	
	public static final int EXAM_1_TIME_LIMIT = 3000;						//Time the exam will start
	public static final int EXAM_2_TIME_LIMIT = 3000;
	public static final int EXAM_3_TIME_LIMIT = 3000;
	//*********************************************
	//DEFAULT CONSTRUCTOR FOR TEACHER
	public Teacher(int ID)
	{
		setName("Teacher " + ID);
	}
	//*********************************************
	// MAIN THREAD CLASS FOR TEACHER
	
	public void run()
	{
		msg("is on his way to classroom.");
		
		Is_Waiting();
		
		
//*****************************************************************************************		
		
		DET_TIME();		

		System.out.println("\n");	
		System.out.println("**********EXAM 1**********");
		System.out.println("\n");	
		
		msg("Teacher has arrived to classroom.");
		TEACHER_ARRIVED = true;

		//EXAM_1 PREPARATION
		
		msg("Exam Starting Time: "+Student.TEM_EXAM_TIME);
		msg("has entered classroom.");
		msg("is letting students entering to classroom.");
		Is_Sleeping(400);
		msg("is waiting for students to get a seat.");
	
		while(!Student.IS_TIME());
			
		//EXAM_1 STARTING
		Main.DURING_EXAM = true;
		msg("is handing out exam sheets to students.");
		msg("is sleeping now.");
			
		
		
		Is_Sleeping(EXAM_1_TIME_LIMIT);
		Main.EXAM_SECTION++;
		
		TEACHER_ARRIVED = false;
		
		msg("announces exam is over.");
		msg("is collecting the exam.");
		
		Is_Waiting();
		msg("has collected all student exams.");
		
		Main.ClASS_LIMIT = 1;
		Main.DURING_EXAM = false;
		
		Student.IS_READY();
		
		
		Main.ClASS_LIMIT = 1;
		msg("Exam "+(Main.EXAM_SECTION-1) +" has completed.");
		
		
		msg("is taking a break.");
		Is_Waiting();
		
//*****************************************************************************************		
		
		DET_TIME();

		System.out.println("\n");	
		System.out.println("**********EXAM 2**********");
		System.out.println("\n");
		
		msg("Exam Starting Time: "+Student.TEM_EXAM_TIME);
		msg("is coming back to classroom.");		
		msg("has entered into classroom and let students into the classroom.");
		
		
		//EXAM_2 PREPARATION
		TEACHER_ARRIVED = true;
		Is_Waiting();
		
		while(!Student.IS_TIME());
		
		//EXAM_2 STARTING
		Main.DURING_EXAM = true;
		msg("is handing out exam sheets to students.");
		msg("is sleeping now.");
		
		Is_Sleeping(EXAM_2_TIME_LIMIT);
		Main.EXAM_SECTION++;
		TEACHER_ARRIVED = false;
		
		msg("announces exam is over.");
		msg("is collecting the exam.");
		Is_Waiting();
		msg("has collected all student exams.");
		
		
		Main.ClASS_LIMIT = 1;
		Main.DURING_EXAM = false;
		Main.ClASS_LIMIT = 1;
		
		Student.IS_READY();
		
			
		msg("Exam "+(Main.EXAM_SECTION-1) +" has completed.");
		
		
		msg("is taking a break.");
		Is_Waiting();
	
//*****************************************************************************************		

		DET_TIME();
		
		System.out.println("\n");	
		System.out.println("**********EXAM 3**********");
		System.out.println("\n");	
		
		msg("Exam Starting Time: "+Student.TEM_EXAM_TIME);
		msg("is coming back to classroom.");		
		msg("has entered into classroom and let students into the classroom.");
			
		//EXAM_3 PREPARATION
		TEACHER_ARRIVED = true;
		Is_Waiting();
		
		while(!Student.IS_TIME());
		
		//EXAM_3 STARTING
		Main.DURING_EXAM = true;
		msg("is handing out exam sheets to students.");
		msg("is sleeping now.");
		
		Is_Sleeping(EXAM_3_TIME_LIMIT);
		Main.EXAM_SECTION++;
		TEACHER_ARRIVED = false;
		
		msg("announces exam is over.");
		msg("is collecting the exam.");
		Is_Waiting();
		msg("has collected all student exams.");
		
		
		Main.ClASS_LIMIT = 1;
		Main.DURING_EXAM = false;
		
		Student.IS_READY();
		
			
		
		msg("Exam "+(Main.EXAM_SECTION-1)+" has completed.");
		
		
		msg("is taking a break.");
		Is_Waiting();
	
//*****************************************************************************************		
		
		Is_Waiting();
		
		msg("is grading Exam 1.");
		Student.EXAM_1Report();
		
		msg("is grading Exam 2.");
		Student.EXAM_2Report();
		
		msg("is grading Exam 3.");
		Student.EXAM_3Report();
		
		EXAM_TAKEN_RECORD();
		Is_Waiting();
		
		
		msg("has finished grading all the exam and is giving the grade back to students.");
		Student.counter = Student.STUDENT_RECORD.size()-1;
		TEACHER_FINISH_GRADE = true;
		Is_Waiting();

		
		while(Student.counter > 0);
		Is_Waiting();	
		msg("has gone back home.");
		
		return; // Careful this return method will terminate the thread at the end of its task. You don't want to remove it from the code totally.
	}
	//*********************************************	
	public void msg (String m)
	{
		System.out.println("["+(System.currentTimeMillis()-time)+"]"+getName()+": "+m);
	}
	
	//*********************************************
	
	public synchronized int RANDOM_TIME_GENERATOR()
	{
		return Randomizer.nextInt(1000)+100;
	}
	//*********************************************
	
	public void Is_Waiting()	
	{
		try
		{	
			sleep(RANDOM_TIME_GENERATOR());
		}
		catch(InterruptedException e)
		{
			e.printStackTrace();
		}
		
	}
	//*********************************************
	
	public void Is_Sleeping(int Time)
	{
		try
		{
			sleep(Time);
		}
		catch(InterruptedException e)
		{
			e.printStackTrace();
		}
	}
	//*********************************************

	public synchronized void DET_TIME()
	{	
		if(Main.EXAM_SECTION == 1)
			Student.TEM_EXAM_TIME = Main.Exam_1_StartingTime;
		else if(Main.EXAM_SECTION == 2)
			Student.TEM_EXAM_TIME = Main.EXAM_2_STARTING_TIME;
		else if(Main.EXAM_SECTION == 3)
			Student.TEM_EXAM_TIME = Main.EXAM_3_STARTING_TIME;
	}
	
	public synchronized void EXAM_TAKEN_RECORD()
	{
		System.out.println("*********STUDENT_EXAM_TAKEN_REPORT*********\n\n");
		Iterator<Student> Temp_Student = Student.STUDENT_RECORD.iterator();
		while(Temp_Student.hasNext())
		{
			Student tmp = Temp_Student.next();
			System.out.println(tmp.getName()+"'s Total Exam Taken: " + tmp.TOTAL_EXAM_TAKEN);			
		}

		System.out.println("\n");
	}
	
	
}
