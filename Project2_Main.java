
import static java.lang.Integer.parseInt;

import java.util.Queue;
import java.util.concurrent.Semaphore;

public class Project2_Main extends Thread {

	public static Queue<Student> WAIT_TURN;
	
	public static Semaphore BIN_SEMA = new Semaphore(1,false);
	
	public static int STUDENT_TOTAL = 14;
	public static int CLASS_CAPACITY = 8;
	public static int GROUP_SIZE = 3;
	
	
	public static void main(String[]args)
	{
		//The order of argument input must be StudentTotal, Class-capacity, and Group-size.
		if (args.length > 0) {
		    try
		    {
		        STUDENT_TOTAL= parseInt(args[0]);
		        CLASS_CAPACITY = parseInt(args[1]);			//This section will accept the input arguments from args string and parse them into integer.
		        GROUP_SIZE = parseInt(args[2]);
		    } 
		    
		    catch (NumberFormatException e)
		    {
		        System.err.println("Argument" + args[0] + "," + args[1] +"and, "+args[3]+ "must be an integer.");		// This section will give out error if args string input is invalid such as not a string numbers.
		        System.exit(1);
		    }
		}
				
		System.out.println("************ WELCOME TO EXAM DAY PROGRAM ************\n");
		System.out.println("\t\tStudent Total: "+STUDENT_TOTAL);
		System.out.println("\t\tClass Capacity: "+CLASS_CAPACITY);
		System.out.println("\t\tGroup Size: "+GROUP_SIZE);
		System.out.println("\t\tEXAM 1 Starting Time: "+ Teacher_P2.EXAM_1_TIME);
		System.out.println("\t\tEXAM 2 Starting Time: "+ Teacher_P2.EXAM_2_TIME);
		System.out.println("(NOTE: Since teacher is not included as one person, you will see 8 students at most every exam section for default values.)");
		System.out.println("\n");	
		
		
		Teacher_P2 NEW_TEC = new Teacher_P2(1);  				// Creating a thread of teacher to start execution.
		NEW_TEC.start();
		
		for(int i = 1;i<= STUDENT_TOTAL ; i++)
		{
			Student_P2 Std= new Student_P2(i);					// Creating student threads based on the amount of total students
			Teacher_P2.que_std.addElement(Std);
			Std.start();
		}
		
		
	}
	
	
}
