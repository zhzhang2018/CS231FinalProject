/** Hashmap.java  Data structure that imitates HashMap
  * Zhuofan Zhang
  * 11/22/2015
  * Project8 CS231 */
import java.util.LinkedList;

class Hashmap<S, T> {
	// The array stores the data we need for the hashmap
	private LinkedList[] list;
	private int size;
	
	// constructor
	public Hashmap() {
		this.list = new LinkedList[5];
		this.size = 0;
	}
	
	// same as the put() method; adds or substitutes an Entry into the array.
	// Add the Entry to the end of the corresponding LinkedList in the array.
	public void add(S key, T value) {
		// if the array is half-full, use a larger one
		if (this.size*2 >= this.list.length) {
			LinkedList[] temp = this.list;
			this.list = new LinkedList[2*this.list.length];
			for (int i=0; i<temp.length; i++) {
				if (temp[i] != null) {
					for (Object entry : temp[i]) {
						Entry eentry = (Entry) entry;
						this.add(eentry.getKey(), eentry.getData());
					}
				}
			}
		}
		
		// add a new Entry or change an existing Entry
		if (this.list[key.hashCode() % this.list.length] == null) {
			this.list[key.hashCode() % this.list.length] = new LinkedList();
			this.list[key.hashCode() % this.list.length].add(new Entry(key, value));
		} else if (this.list[key.hashCode() % this.list.length] != null) {
			boolean ifExist = false;
			for (Object entry : this.list[key.hashCode() % this.list.length]) {
				Entry eentry = (Entry) entry;
				if (eentry.getKey().equals(key)) {
					eentry.setData(value);
					ifExist = true;
					break;
				}
			}
			
			if (!ifExist) {
				this.list[key.hashCode() % this.list.length].add(new Entry(key, value));
			}
		}
		this.size++;
	}
	
	// clears the current hashmap
	public void clear() {
		this.list = new LinkedList[100];
	}
	
	// return the corresponding value of the key by first getting the index and 
	// then going through the LinkedList
	public T get(S key) {
		// if no such Entry exists
		if (this.list[key.hashCode() % this.list.length] == null) {
			return null;
		} else {
			for (Object entry : this.list[key.hashCode() % this.list.length]) {
				Entry eentry = (Entry) entry;
				if (eentry.getKey().equals(key)) {
					return eentry.getData();
				}
			}
			return null;
		}
	}
	
	// removes the indicated Entry by using the same thing as in get()
	public boolean remove(S key) {
		if (this.list[key.hashCode() % this.list.length] == null) {
			return false;
		} else {
			for (Object entry : this.list[key.hashCode() % this.list.length]) {
				Entry eentry = (Entry) entry;
				if (eentry.getKey().equals(key)) {
					return this.list[key.hashCode() % this.list.length].remove(eentry);
				}
			}
			return false;
		}
	}

	// class Entry stores all the information of the keys and values
	private class Entry {
		S key;
		T data;
		
		public Entry(S key, T data) {
			this.key = key;
			this.data = data;
		}
		
		public T getData() {
			return this.data;
		}
		
		public S getKey() {
			return this.key;
		}
		
		public void setData(T data) {
			this.data = data;
		}
	}
	
	// unit test
	public static void main(String[] args) {
		Hashmap<Integer, Integer> map = new Hashmap<Integer, Integer>();
		for (int i=1234; i<2333; i++) {
			map.add(i, i*10);
		}
		for (int i=1444; i<2330; i++) {
			System.out.println(map.remove(i));
		}
		for (int i=1230; i<2333; i++) {
			System.out.println(map.get(i));
		}
		
		// records the time needed
/* credit to: http://stackoverflow.com/questions/3382954/measure-execution-time-for-a-java-method */
		long startTime = System.currentTimeMillis();
		map = new Hashmap<Integer, Integer>();
		for (int i=0; i<50000; i++) {
			map.add(i, i/12450);
		}
		long stopTime = System.currentTimeMillis();
		long elapsedTime = stopTime - startTime;
		System.out.println(elapsedTime);
	}
	
}