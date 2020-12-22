package examscheduler;

public class Auditorium {
	private String name;
	private int capacity;
	private boolean hasComputers;
	private int requiredStaff;
	private boolean belongsToETF;
	
	public Auditorium(String name, int capacity, boolean hasComputers, int requiredStaff, boolean belongsToETF) {
		super();
		this.name = name;
		this.capacity = capacity;
		this.hasComputers = hasComputers;
		this.requiredStaff = requiredStaff;
		this.belongsToETF = belongsToETF;
	}
	
	@Override
	public String toString() {
		return "Name (" + name + "), Capacity (" + capacity + "), Computers (" + hasComputers + 
				"), Staff (" + requiredStaff + "), ETF (" + belongsToETF + ")"; 
	}

	public String getName() {
		return name;
	}

	public int getCapacity() {
		return capacity;
	}

	public boolean isHasComputers() {
		return hasComputers;
	}

	public int getRequiredStaff() {
		return requiredStaff;
	}

	public boolean isBelongsToETF() {
		return belongsToETF;
	}
}
