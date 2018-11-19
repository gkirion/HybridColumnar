private ArrayList<Tuple3<E, Integer, Integer>> arrayList;
	private String name;
	private Integer id;
	
	public ColumnRle() {
		arrayList = new ArrayList<>();
		name = "";
		id = 0;
	}
	
	public ColumnRle(String name) {
		arrayList = new ArrayList<>();
		this.name = name;
		id = 0;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public void add(E item) {
		int size = arrayList.size();
		if (size > 0 && arrayList.get(size - 1).getFirst().equals(item)) {
			arrayList.get(size - 1).setSecond(arrayList.get(size - 1).getSecond() + 1);
		}
		else {
			arrayList.add(new Tuple3<E, Integer, Integer>(item, 1, id));
		}
		id++;
	}
	
	protected int find(int i, int left, int right) {
		if (left > right) {
			return -1;
		}
		int mid = (left + right) / 2;
		if (arrayList.get(mid).getThird() > i) {
			return find(i, left, mid -1);
		}
		else if (arrayList.get(mid).getSecond() + arrayList.get(mid).getThird() <= i) {
			return find(i, mid + 1, right);
		}
		else {
			return mid;
		}
		
	}
	
	public Tuple2<E, Integer> get(int i) {
		int key = find(i, 0, arrayList.size() - 1);
		if (key == -1) {
			return null;
		}
		return new Tuple2<E, Integer>(arrayList.get(key).getFirst(), arrayList.get(key).getThird() + arrayList.get(key).getSecond() - i); 
	}
	
	@Override
	public int getLength() {
		if (arrayList.isEmpty()) {
			return 0;
		}
		int size = arrayList.size();
		return arrayList.get(size - 1).getThird() + arrayList.get(size - 1).getSecond();
	}
	
	public int getRunCount() {
		return arrayList.size();
	}
	
	public ArrayList<Tuple3<E, Integer, Integer>> entrySet() {
		return arrayList;
	}
	
	public String toString() {
		return arrayList.toString();
	}
	
