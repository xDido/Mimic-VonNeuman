package Memory;

public class Instruction {

	int instruction = -1;

	int opcode; // bits31:28
	int r1; // bits27:23
	int r2; // bit22:18
	int r3; // bits17:13
	int shamt; // bits12:0
	int imm; // bits17:0
	int address; // bits27:0
	public int number = -1;;

	int valueR1;
	int valueR2;
	int valueR3;

	int set;
	int pcsetter;
	int readmem;
	int writeindex;
	int writemem;
	boolean flagSet;

	boolean flagreadmem;
	boolean flagwritemem;
	boolean flagJEQ;

	public Instruction() {
		instruction = -1;
		opcode = 0;
		r1 = 0;
		r2 = 0;
		r3 = 0;
		shamt = 0;
		imm = 0;
		address = 0;
		valueR1 = 0;
		valueR2 = 0;
		valueR3 = 0;
		set = -1;
		pcsetter = -1;
		readmem = -1;
		writeindex = -1;
		writemem = -1;
		flagSet = false;
		flagreadmem = false;
		flagwritemem = false;
		flagJEQ = false;
	}

	public int getInstruction() {
		return instruction;
	}

	public void setInstruction(int instruction) {
		this.instruction = instruction;
	}

	public int getOpcode() {
		return opcode;
	}

	public void setOpcode(int opcode) {
		this.opcode = opcode;
	}

	public int getR1() {
		return r1;
	}

	public void setR1(int r1) {
		this.r1 = r1;
	}

	public int getR2() {
		return r2;
	}

	public void setR2(int r2) {
		this.r2 = r2;
	}

	public int getR3() {
		return r3;
	}

	public void setR3(int r3) {
		this.r3 = r3;
	}

	public int getShamt() {
		return shamt;
	}

	public void setShamt(int shamt) {
		this.shamt = shamt;
	}

	public int getImm() {
		return imm;
	}

	public void setImm(int imm) {
		this.imm = imm;
	}

	public int getAddress() {
		return address;
	}

	public void setAddress(int address) {
		this.address = address;
	}

	public int getValueR2() {
		return valueR2;
	}

	public void setValueR2(int valueR2) {
		this.valueR2 = valueR2;
	}

	public int getValueR3() {
		return valueR3;
	}

	public void setValueR3(int valueR3) {
		this.valueR3 = valueR3;
	}

	public int getSet() {
		return set;
	}

	public void setSet(int set) {
		this.set = set;
	}

	public int getPcsetter() {
		return pcsetter;
	}

	public void setPcsetter(int pcsetter) {
		this.pcsetter = pcsetter;
	}

	public int getReadmem() {
		return readmem;
	}

	public void setReadmem(int readmem) {
		this.readmem = readmem;
	}

	public int getWritemem() {
		return writemem;
	}

	public void setWritemem(int writemem) {
		this.writemem = writemem;
	}

	public int getWriteindex() {
		return writeindex;
	}

	public void setWriteindex(int writeindex) {
		this.writeindex = writeindex;
	}

	public int getValueR1() {
		return valueR1;
	}

	public void setValueR1(int valueR1) {
		this.valueR1 = valueR1;
	}

	public boolean isFlagSet() {
		return flagSet;
	}

	public void setFlagSet(boolean flagSet) {
		this.flagSet = flagSet;
	}

	public boolean isFlagreadmem() {
		return flagreadmem;
	}

	public void setFlagreadmem(boolean flagreadmem) {
		this.flagreadmem = flagreadmem;
	}

	public boolean isFlagwritemem() {
		return flagwritemem;
	}

	public void setFlagwritemem(boolean flagwritemem) {
		this.flagwritemem = flagwritemem;
	}

	public boolean isFlagJEQ() {
		return flagJEQ;
	}

	public void setFlagJEQ(boolean flagJEQ) {
		this.flagJEQ = flagJEQ;
	}
	
	public String toString() {
		String ins = "";
		ins+= "Opcode: " + this.opcode + ", "; 
		ins+= "R1: " + this.r1 + ", ";
		ins+= "R2: " + this.r2 + ", ";
		ins+= "R3: " + this.r3 + ", ";
		ins+= "Shamt: "+ this.shamt;
		ins+= "Imm: "+ this.imm;
		ins+= "Address: "+ this.address;
		return ins;
	}

}