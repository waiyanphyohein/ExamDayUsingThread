public class Main extends Thread{
	
	public static int STUDENT_TOTAL = 14;
	public static int CLASS_CAPACITY = 10;
	
	public static final int Exam_1_StartingTime = 5000;
	public static final int EXAM_2_STARTING_TIME = 15000;
	public static final int EXAM_3_STARTING_TIME = 25000;
	
	public static volatile int EXAM_SECTION = 1;
	public static volatile boolean DURING_EXAM = false;	
	public static volatile int ClASS_LIMIT= 1;
	
	public static void main(String[]args) throws InterruptedException
	{		
		
		if (args.length > 0) {
		    try {
		        STUDENT_TOTAL= Integer.parseInt(args[0]);
		        CLASS_CAPACITY = Integer.parseInt(args[1]);
		    } catch (NumberFormatException e) {
		        System.err.println("Argument" + args[0] + "and" + args[1] + "must be an integer.");
		        System.exit(1);
		    }
		}
		
		int Temp_Para = 1;
		System.out.println("************ WELCOME TO EXAM DAY PROGRAM ************\n");
		System.out.println("\t\tStudent Total: "+STUDENT_TOTAL);
		System.out.println("\t\tClass Capacity: "+CLASS_CAPACITY);
		System.out.println("\t\tEXAM 1 Starting Time: "+ Exam_1_StartingTime);
		System.out.println("\t\tEXAM 2 Starting Time: "+ EXAM_2_STARTING_TIME);
		System.out.println("\t\tEXAM 3 Starting Time: "+ EXAM_3_STARTING_TIME+"\n");
		System.out.println("(NOTE: Since teacher is already included as one person, you will only see 9 students at most every exam section.");
		System.out.println("\n");	
		
		Teacher TS = new Teacher(1);
		TS.start();
		
		while(Temp_Para <= STUDENT_TOTAL)
		{	
			Student StD = new Student(Temp_Para++);
			StD.start();	
			Student.STUDENT_RECORD.add(StD);
		}
		Temp_Para = 0;
		
		while(!Teacher.TEACHER_FINISH_GRADE)
		{
			sleep(1000);
		}
		
		
		while(Student.counter>0)	
		{
			if(Student.STUDENT_RECORD.get(Student.counter).isAlive())
				Main.JOIN_STUDENT(Student.STUDENT_RECORD.get(Student.counter));
				while(!Teacher.STUDENT_LEFT);
				Student.counter--;
				Teacher.STUDENT_LEFT = false;
		}
		

	
	}
			
	public static synchronized boolean TeacherArrived()
	{
		return Teacher.TEACHER_ARRIVED;
	}
	
	public static synchronized boolean TeacherReady()
	{
		return Student.STUDENT_READY;
	}
		
	public static synchronized boolean StudentFinishTheExam(Student Current)
	{
		return (Current.STUDENT_TOOK_THE_FIRST_EXAM && Current.STUDENT_TOOK_THE_SECOND_EXAM);
	}

	public static synchronized boolean TeacherFinishGrading()
	{
		return Teacher.TEACHER_FINISH_GRADE;
	}
	
	public static synchronized boolean MAX_CLASS_CAP()
	{
		return (CLASS_CAPACITY == ClASS_LIMIT);
	}
	
	public synchronized static void JOIN_STUDENT(Student tmp)
	{
		try 
		{			
			tmp.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			tmp.msg("has failed to join.");
		}
	}
	
}
