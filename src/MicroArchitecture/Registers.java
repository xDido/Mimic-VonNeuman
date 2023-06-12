package MicroArchitecture;

public class Registers {
	private int[] generalRegister;
	private final int zeroRegister = 0;
	private int PC;

	public Registers() {
		generalRegister = new int[32];
		PC = 0;
	}

	public int getPC() {
		return PC;
	}

	public void setPC(int PC) {
		this.PC = PC;
	}

	public int getZeroRegister() {
		return zeroRegister;
	}

	public int[] getGeneralRegister() {
		return generalRegister;
	}

	public void setGeneralRegister(int x, int y) {
		generalRegister[x] = y;
	}

}
