package MicroArchitecture;

import java.util.LinkedList;
import java.util.Queue;

import Memory.*;

public class MicroController {
	Memory mainMemory;
	Registers reg=new Registers();
	int clock = 1;
	Instruction instform=new Instruction();
	String DecodeDummyPrint="";
	String ExecuteDummyPrint="";

	Queue<Instruction> fetch = new LinkedList<>();
	Queue<Instruction> decode = new LinkedList<>();
	Queue<Instruction> execute = new LinkedList<>();
	Queue<Instruction> memoryLD = new LinkedList<>();
	Queue<Instruction> writeBack = new LinkedList<>();
//	Queue<Integer> temp = new LinkedList<>();
	Instruction[] instformArr = new Instruction[1024];
	int[] registerused = new int[32];
	boolean wait = false;
	
	public MicroController() {
		for(int i =0;i<registerused.length;i++) {
			registerused[i]=0;
		}
		mainMemory = new Memory("Instructions");
	}

	public void instRun(){
		
		reg.setPC(0);
		
		while(!fetch.isEmpty() || !decode.isEmpty() || !execute.isEmpty() || !memoryLD.isEmpty()
				|| !writeBack.isEmpty() || clock == 1){
			
			System.out.println("Clock Cycle: "+clock);
			
			if(clock%2==1) { //odd cycle
				
				if(!writeBack.isEmpty() && clock>=7) {
					writebackMethod(instformArr[pcFinder(writeBack.peek())]);
					writeBack.remove();
				}
				
				if(!execute.isEmpty() && clock>=5) {
					int index=pcFinder(execute.peek());
					this.instformArr[index] = (execute.peek());
					System.out.println("Executing at Instruction Execute is Instuction "+(index+1));
					executeMethodJEQ(instformArr[pcFinder(execute.peek())]);
					if(instformArr[pcFinder(execute.peek())].isFlagJEQ()) {
						instformArr[pcFinder(execute.peek())].setFlagJEQ(false);
						memoryLD.add(execute.peek());
						while(!execute.isEmpty()) {
							execute.poll();
						}
					} else {
						memoryLD.add(execute.remove());
					}
				}
				
				if(!decode.isEmpty() && clock>=3) {
					int index=pcFinder(decode.peek());
					this.instformArr[index] = (decode.peek());
					System.out.println("Executing at Instruction Decode is Instuction "+(index+1));
					execute.add(decode.remove());
				} else if(wait) {
					System.out.println("Instruction " + (pcFinder(decode.peek())+1) +" is forwarded");
				}
				
				if(reg.getPC()<Memory.count) {
					System.out.println("PC: "+(reg.getPC()+1));
					fetchMethod();
				}
				
			} else { //even cycle
				
				if(!memoryLD.isEmpty() && clock>=6) {
					memoryMethod(instformArr[pcFinder(memoryLD.peek())]);
					writeBack.add(memoryLD.remove());
				}
				
				if(!execute.isEmpty() && clock>=4) {
					executeMethod(instformArr[pcFinder(execute.peek())]);
				}
				
				if(!decode.isEmpty()) {
					decodeMethod(instformArr[pcFinder(decode.peek())]);
				}
				
			}
			clock++;
			System.out.println("------------------------------------------");
		}
		System.out.println();
		System.out.println("******* Registers *******");
		for(int i=0;i<reg.getGeneralRegister().length;i++) {
			System.out.print("R"+i+": "+reg.getGeneralRegister()[i]+"  ");
		}
		System.out.println();
		System.out.println();
		System.out.println("********* Memory *********");
		for(int i=0;i<2048;i++) {
			if(i==0)
				System.out.println("****** Instructions ******");
			if(i==1024)
				System.out.println("*********** Data ***********");
			System.out.println(i + ": " + mainMemory.getMemory()[i]);
		}
	}
	
//	public boolean checkForwarding(Instruction instform) {
//		
//		boolean stalled = false; 
//		int instruction = instform.getInstruction();
//		
//		int opcode = (instruction>>28) & 0b1111;  // bits31:28
//		
//		if(opcode!=4 && opcode!=7 && opcode!=11) {
//			registerused[(instruction>>23) & 0b11111] = 1;
//		}
//		
//		if(opcode==0 || opcode==1 || opcode==2 || opcode==5) {
//			int r2 = (instruction>>18) & 0b11111;      // bit22:18
//			int r3 = (instruction>>13) & 0b11111;      // bits17:13
//			for(int i =0;i<registerused.length;i++) {
//				if((r2==i || r3==i) && registerused[i] == 1) {
//					stalled = true;
//				}
//			}
//		} else if(opcode==4 || opcode==11) {
//			int r1 = (instruction>>23) & 0b11111;    // bits27:23
//			int r2 = (instruction>>18) & 0b11111;    // bit22:18
//			for(int i =0;i<registerused.length;i++) {
//				if((r1==i || r2==i) && registerused[i] == 1) {
//					stalled = true;
//				}
//			}
//		} else if(opcode==6 || opcode==8 || opcode==9 || opcode==10) {
//			int r2 = (instruction>>18) & 0b11111;      // bit22:18
//			for(int i =0;i<registerused.length;i++) {
//				if(r2==i && registerused[i] == 1) {
//					stalled = true;
//				}
//			}
//		}
//		
//		for(int i =1;i<registerused.length;i++) {
//			if(registerused[i]==1) {
//				System.out.println("R"+ i + " is in use");
//			}
//		}
//		return stalled;
//	}

	public void fetchMethod() {
		
		if(this.mainMemory.getMemoryInst()[reg.getPC()] != null) {
			
//			int index=pcFinder(fetch.peek());
			this.instformArr[reg.getPC()] = new Instruction();
			this.instformArr[reg.getPC()].setInstruction(mainMemory.getArrayInstructions()[reg.getPC()]);
			this.instformArr[reg.getPC()].number = reg.getPC();
			fetch.add(this.instformArr[reg.getPC()]);
			System.out.println("Executing at Instruction Fetch is Instuction "+(reg.getPC()+1));
			reg.setPC(reg.getPC()+1);
			decode.add(fetch.remove());
		}
	}

	public void decodeMethod(Instruction instform) {
		
		int instruction = instform.getInstruction();

		int opcode = (instruction>>28) & 0b1111;  // bits31:28
		int r1 = (instruction>>23) & 0b11111;    // bits27:23
		int r2 = (instruction>>18) & 0b11111;      // bit22:18
		int r3 = (instruction>>13) & 0b11111;      // bits17:13
		int shamt = instruction & 0b1111111111111;   // bits12:0
		int imm = instruction & 0b111111111111111111;     // bits17:0
		int address = instruction & 0b1111111111111111111111111111; // bits27:0

		int valueR1= reg.getGeneralRegister()[r1];
		int valueR2 =reg.getGeneralRegister()[r2];
		int valueR3 =reg.getGeneralRegister()[r3];

		int check=0;
		check=(imm&(0b100000000000000000));
		if(check!=0) {
			imm=imm-131072;
			imm=imm-1;
			imm=imm*-1;
		}

		instform.setOpcode(opcode);
		instform.setAddress(address);
		instform.setImm(imm);

		instform.setR1(r1);
		instform.setR2(r2);
		instform.setR3(r3);
		instform.setValueR1(valueR1);
		instform.setValueR2(valueR2);
		instform.setValueR3(valueR3);
		instform.setShamt(shamt);
		
		int index=pcFinder(decode.peek());
		this.instformArr[index] = (decode.peek());
		System.out.println("Executing at Instruction Decode is Instuction "+(index+1));
		
//		if(!checkForwarding(instform)) {
//			int index=pcFinder(decode.peek());
//			this.instformArr[index] = (decode.peek());
//			System.out.println("Executing at Instruction Decode is Instuction "+(index+1));
//			wait = false;
//		} else {
//			System.out.println("Instruction " + (pcFinder(decode.peek())+1) +" is forwarded");
//			wait = true;
//		}
	}

	public void executeMethodJEQ(Instruction instform) {
		if(instform.isFlagJEQ()) {
	
			while(!fetch.isEmpty())
				fetch.poll();
	
			while(!decode.isEmpty()) {
				int i = pcFinder(decode.peek());
				if(instformArr[i].getOpcode()==11) {
					registerused[instformArr[i].getR2()] = 0;
				} else if(instformArr[i].getOpcode()!=4 && instformArr[i].getOpcode()!=7) {
					registerused[instformArr[i].getR1()] = 0;
				}
				decode.poll();
			}
			
			if(instform.getOpcode()==4) {
				reg.setPC(pcFinder(execute.peek()));
				reg.setPC(reg.getPC() + 1 + instform.getImm());
			}
			else {
				if(instform.getOpcode()==7) {
					reg.setPC(pcFinder(execute.peek()));
					int temp = (reg.getPC()>>28) & 0b1111;
					String temp2=temp+""+instform.getAddress();
					reg.setPC(Integer.parseInt(temp2)-1);
				}
			}
		}
	}

	public void executeMethod(Instruction instform) {

		int opcode = instform.getOpcode(); // bits31:27
		int r1 = instform.getR1(); // bits26:24
		int r2 = instform.getR2(); // bit23:21
		int r3 = instform.getR3(); // bits20:18	
		int shamt = instform.getShamt(); // bits17:10
		int imm = instform.getImm(); // bits20:0
		int address = instform.getAddress(); // bits26:0

		int valueR1=  instform.getValueR1();
		int valueR2 = instform.getValueR2();
		int valueR3 = instform.getValueR3();
	
		switch (opcode) {
			case 0:
				instform.setSet(valueR2 + valueR3);
				instform.setFlagSet(true);
				break;
			case 1:
				instform.setSet(valueR2 - valueR3);
				instform.setFlagSet(true);
				break;
			case 2:
				instform.setSet(valueR2 * valueR3);
				instform.setFlagSet(true);
				break;
			case 3:
				instform.setSet(imm);
				instform.setFlagSet(true);
				break;
			case 4:
				if (valueR1 == valueR2) {
					instform.setFlagJEQ(true);
				}
				break;
			case 5:
				instform.setSet(valueR2 & valueR3);
				instform.setFlagSet(true);
				break;
			case 6:
				instform.setSet(valueR2 ^ imm);
				instform.setFlagSet(true);
				break;
			case 7:
				instform.setFlagJEQ(true);
				break;
			case 8:
				instform.setSet(valueR2 << shamt);
				instform.setFlagSet(true);
				break;
			case 9:
				instform.setSet(valueR2 >>> shamt);
				instform.setFlagSet(true);
				break;
			case 10:
				instform.setReadmem(valueR2 + imm);
				instform.setFlagreadmem(true);
				instform.setFlagSet(true);
				break;
			case 11:
				instform.setWriteindex(valueR2 + imm);
				instform.setWritemem(reg.getGeneralRegister()[r1]);
				instform.setFlagwritemem(true);
				break;
		}
		int index=pcFinder(execute.peek());
		this.instformArr[index] = (execute.peek());
		System.out.println("Executing at Instruction Execute is Instuction "+(index+1));
	}



	public void memoryMethod(Instruction instform) {
	
		int index=pcFinder(memoryLD.peek());
		this.instformArr[index] = (memoryLD.peek());
		System.out.println("Executing at Memory is Instuction "+(index+1));
	
		if (instform.isFlagwritemem()) {
			mainMemory.setMemory(instform.getWriteindex(), (Object)instform.getWritemem());
			System.out.println("The Value of Memory["+instform.getWriteindex()+"] became "+instform.getWritemem());
			instform.setWriteindex(-1);
			instform.setWritemem(-1);
			instform.setFlagwritemem(false);
			registerused[instform.getR2()] = 0;
		}
		else {
			if (instform.isFlagreadmem()) {
				instform.setSet((int)this.mainMemory.getMemory()[instform.getReadmem()]);
				instform.setReadmem(-1);
				instform.setFlagreadmem(false);
				registerused[instform.getR1()] = 0;
			}
		}
	}

	public void writebackMethod(Instruction instform) {
		if(instform.isFlagSet()&&instform.getR1()!=0) {
			reg.setGeneralRegister(instform.getR1(),instform.getSet());
		
			int index=pcFinder(writeBack.peek());
			this.instformArr[index] = (writeBack.peek());
			System.out.println("Executing at WriteBack is Instuction "+(index+1));
		
			System.out.println("The value of R"+instform.getR1()+" became "+instform.getSet());
			instform.setSet(-1);
			
			if(instform.getOpcode()==11) {
				registerused[instform.getR2()] = 0;
			} else if(instform.getOpcode()!=4 && instform.getOpcode()!=7) {
				registerused[instform.getR1()] = 0;
			}
			
		}
	}

	public int getDecimal(String binary) {
		char[] numbers = binary.toCharArray();
		int result = 0;
		for (int i = numbers.length - 1; i >= 0; i--)
			if (numbers[i] == '1')
				result += Math.pow(2, (numbers.length - i - 1));
		return result;
	}

	public int pcFinder(Instruction instruction) {
		return instruction.number;
	}

	public static void main(String[] args) {
		MicroController f = new MicroController();
		f.instRun();
	}



}