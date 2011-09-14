package au.edu.monash.ges.solweig;

public class BiDimensionalArray<T> {

	Object[][] backupArray;
	int lengthX;
	int lengthY;

	public BiDimensionalArray(int lengthX, int lengthY) {
		backupArray = new Object[lengthX][lengthY];
		this.lengthX = lengthX;
		this.lengthY = lengthY;
	}

	public void set(int x, int y, T value) {
		backupArray[x][y] = value;
	}

	public T get(int x, int y) {
		return (T) backupArray[x][y];
	}

	public void addX(T[] valuesY) {
		Object[][] newArray = new Object[lengthX + 1][lengthY];
		System.arraycopy(backupArray, 0, newArray, 0, lengthX);
		newArray[lengthX] = valuesY;
		backupArray = newArray;
		lengthX = lengthX + 1;
	}

}
