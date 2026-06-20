import java.util.Scanner;

class CPU{
	static int[] registers = new int[8];
	static int[] data_memory = new int[16];
	static String[] inst_memory = new String[16];
	static String IR="";
	static int flag = 0;
	static int pc=0;
	public static void main(String args[]){
		Scanner sc = new Scanner(System.in);

		System.out.println("Enter the instructions: ");
		for(int i = 0;i<16;i++){
			String instruction = sc.nextLine();
			inst_memory[i]=instruction;
			if(instruction.equals("111")) break;
		}
		registers[0]=0;
		registers[1]=0;
		for(int i=0;i<10;i++)
			data_memory[i]=i;

		System.out.println("\n\nInstruction Memory:");
		for (int i = 0; i<16;i++)
			System.out.println(i+": "+inst_memory[i]);

		System.out.println("\nData Memory:");
		for (int i = 0; i<16;i++)
			System.out.println(i+": "+data_memory[i]);

		System.out.println("\n\n");
		System.out.println("IR: "+IR);
		System.out.println("pc: "+pc+"\tflag: "+flag);
		System.out.println("Register: ");
		for(int i=0;i<8;i++)
			System.out.println("r"+i+": "+registers[i]);
		while(pc < 16){
			IR=inst_memory[pc];
			pc++;
			decoder(IR);
			System.out.println("\nIR: "+IR);
			System.out.println("pc: "+pc+"\tflag: "+flag);
			System.out.println("Register: ");
			for(int i=0;i<8;i++)
				System.out.println("r"+i+": "+registers[i]);
		}
		System.out.println("\n\nInstruction Memory:");
		for (int i = 0; i<16;i++)
			System.out.println(i+": "+inst_memory[i]);

		System.out.println("\nData Memory:");
		for (int i = 0; i<16;i++)
			System.out.println(i+": "+data_memory[i]);

		sc.close();
	}



	public static void decoder(String IR){
		String operator = operator_decoder(IR.substring(0,3));
		if(!"halt".equals(operator) && !"exit".equals(operator)){
			int reg_op0=operand_decoder(IR.substring(3,6));
			switch (operator) {
				case "load" -> memory_operation(operator, reg_op0,operand_decoder(IR.substring(6,10)));
				case "blt" ->{
					if(flag < 0){
						pc=operand_decoder(IR.substring(3,7));
						flag=0;
					}
					
				}
				case "store" -> memory_operation(operator, reg_op0,operand_decoder(IR.substring(6,10)));
				default -> {
					int reg_op1=operand_decoder(IR.substring(6,9));
					switch(operator){
						case "add","sub","compare" -> alu(operator,reg_op0,reg_op1);
						case "move" -> register_operation(reg_op0,reg_op1);
					}
				}
			}
		}
		else if(operator.equals("exit")){
			System.out.println("Exited successfully");
			pc=17;
		}

		else{
			System.out.println("Halted");
			pc=17;
		}


	}
			
	public static String operator_decoder(String operator){
            return switch (operator) {
                case "000" -> "load";
                case "001" -> "store";
                case "010" -> "add";
                case "011" -> "sub";
                case "100" -> "compare";
                case "101" -> "blt";
                case "110" -> "move";
                case "111" -> "exit";
                default -> "halt";
            };
	}

	public static int operand_decoder(String operand){
		return Integer.parseInt(operand,2);

	}

	public static void alu(String operator, int register1, int register2){
		switch(operator){
			case "add" -> registers[register1] += registers[register2];
			case "sub" -> registers[register1] -= registers[register2];
			case "compare" -> flag = registers[register1] - registers[register2];
		}
	}

	public static void memory_operation(String operation, int r, int m){
		if(operation.equals("load"))
			registers[r] = data_memory[m];

		else if(operation.equals("store"))
			data_memory[m] =registers[r];
	}

	public static void register_operation(int r1, int r2){
		registers[r1] = registers[r2];
	}
	
}
