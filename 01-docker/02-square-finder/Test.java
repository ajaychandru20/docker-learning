public class Test{
	public static void main(String[] args) {
		Integer int_num = Integer.parseInt(args[0]);
		try{
			for (int i = 0; i<int_num ; i++ ) {
				System.out.println("Square of " + i + " is => " + (i * i));	
				
			}
		}catch(Exception e){
			System.out.println("Exception message: " + e.getMessage());
		}



	}
}