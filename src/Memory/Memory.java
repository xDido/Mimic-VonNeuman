package Memory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Memory {
	Object[] Memory = new Object[2048];//binary 
	int[] ArrayInstructions = new int[1024]; //dec
	String[] MemoryInst = new String[1024]; //string
	public static int count = 0;
	String InstructionInput = "";
	String[] Instruction;
	String BinaryInstruction;

	public Memory(String fileName) { // parser_one + helper methods
		try {
			FileReader file = new FileReader(fileName);
			BufferedReader myReader = new BufferedReader(file);
			String line;

			while ((line = myReader.readLine()) != null) {
				MemoryInst[count] = line;
				count++;
			}
			file.close();
			myReader.close();

		} catch (IOException e) {
			System.out.println("An error occurred.");
		}
		for (int i = 0; i < MemoryInst.length && MemoryInst[i] != null; i++) {
			InstructionInput = MemoryInst[i];
			Instruction = InstructionInput.split(" ");
			switch (Instruction[0]) {
			case "ADD":
				BinaryInstruction = "0000";
				break;
			case "SUB":
				BinaryInstruction = "0001";
				break;
			case "MUL":
				BinaryInstruction = "0010";
				break;
			case "MOVI":
				BinaryInstruction = "0011";
				break;
			case "JEQ":
				BinaryInstruction = "0100";
				break;
			case "AND":
				BinaryInstruction = "0101";
				break;
			case "XORI":
				BinaryInstruction = "0110";
				break;
			case "JMP":
				BinaryInstruction = "0111";
				break;
			case "LSL":
				BinaryInstruction = "1000";
				break;
			case "LSR":
				BinaryInstruction = "1001";
				break;
			case "MOVR":
				BinaryInstruction = "1010";
				break;
			case "MOVM":
				BinaryInstruction = "1011";
				break;
			default:
				System.out.println("-------Invalid Operation-------");
			}

			// r-type 3 registers
			for (int j = 0; j < 32; j++) {

				if (Instruction[1].equals("R" + j)) {
					BinaryInstruction += dectobin(j, 5);

				}
			}
			for (int j = 0; j < 32; j++) {
				if (Instruction.length > 2) {
					if (Instruction[2].equals("R" + j))
						BinaryInstruction += dectobin(j, 5);
				}
			}
			for (int j = 0; j < 32; j++) {

				if (Instruction.length == 4) {
					if (Instruction[3].equals("R" + j))
						BinaryInstruction += dectobin(j, 5);
				}
			}

			if (Instruction.length == 5) // r-type
				BinaryInstruction += dectobin(Integer.parseInt(Instruction[4]), 13);
			else {
				if (Instruction.length == 2 && (Character.toLowerCase(Instruction[1].charAt(0)) < 'a'
						|| Character.toLowerCase(Instruction[1].charAt(0)) > 'z')) // j-type
					BinaryInstruction += dectobin(Integer.parseInt(Instruction[1]), 28);
				else {
					if (Instruction.length == 3) { // movi i-type
						BinaryInstruction += "00000"; // r2
						BinaryInstruction += dectobin(Integer.parseInt(Instruction[2]), 18);
					} else {
						if (Instruction.length == 4 && (Character.toLowerCase(Instruction[3].charAt(0)) < 'a'
								|| Character.toLowerCase(Instruction[3].charAt(0)) > 'z')) { // i-type
							if (Instruction[0] == "LSL" || Instruction[0] == "LSR") {
								BinaryInstruction += dectobin(Integer.parseInt(Instruction[3]), 13);
							} else {
								BinaryInstruction += dectobin(Integer.parseInt(Instruction[3]), 18);
							}
						} else {
							BinaryInstruction += "0000000000000"; // shamt
						}
					}
				}
			}
			Memory[i] = BinaryInstruction;
			ArrayInstructions[i] = getTwosComplement(BinaryInstruction);
		}

	}

	public static String dectobin(int decimal, int len) {
		String binary = "";
		if (decimal >= 0) {
			while (decimal > 0) {
				binary = decimal % 2 + binary;
				decimal = decimal / 2;

			}
			if (binary.length() != len) {
				while (binary.length() < len) {
					binary = "0" + binary;
				}
			}
		} else {
			decimal = (decimal * -1) + 1;
			while (decimal > 0) {
				binary = decimal % 2 + binary;
				decimal = decimal / 2;

			}
			if (binary.length() != len - 1) {
				while (binary.length() < len - 1)
					binary = "0" + binary;
				binary = "1" + binary;

			}

		}

		return binary;
	}

	public static int getTwosComplement(String binaryInt) {

		if (binaryInt.charAt(0) == '1') {

			String invertedInt = invertDigits(binaryInt);
			int decimalValue = Integer.parseInt(invertedInt, 2);
			decimalValue = (decimalValue + 1) * -1;
			return decimalValue;
		} else {

			return Integer.parseInt(binaryInt, 2);
		}
	}

	public static String invertDigits(String binaryInt) {
		String result = binaryInt;
		result = result.replace("0", " "); // temp replace 0s
		result = result.replace("1", "0"); // replace 1s with 0s
		result = result.replace(" ", "1"); // put the 1s back in
		return result;
	}

	public int[] getArrayInstructions() {
		return ArrayInstructions;
	}

	public void setArrayInstructions(int[] arrayListInstructions) {
		ArrayInstructions = arrayListInstructions;
	}

	public String[] getMemoryInst() {
		return MemoryInst;
	}

	public void setMemoryInst(String[] memoryInst) {
		MemoryInst = memoryInst;

	}

	public Object[] getMemory() {
		return Memory;
	}

	public void setMemory(int x, Object y) {
		Memory[x] = y;
	}
}
