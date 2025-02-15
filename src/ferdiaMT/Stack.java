package ferdiaMT;

public class Stack {

	private int stackData[];
	private int top;
	private int maxSize;
	
	public Stack(int size) {
		maxSize = size;
		stackData = new int[maxSize];
		top = -1;
	}
	
	public void push(int data) {
		top++;
		stackData[top] = data;
	}
	
	@SuppressWarnings("null")
	public int pop() {
		if(!isEmpty()) {
			int temp  = stackData[top];
			top--;
			return temp;
		}else {
			return (Integer) null;
		}
	}
	
	private boolean isEmpty() {
		return top == -1;
	}
	
	
}
