import java.io.Serializable;

public class TestBean implements Comparable<TestBean>, Serializable{

	public String testName, obtainedMarks;
	public Float thresholdMarks, maxMarks;
	public int clas, id;

	public Float getMaxMarks() {
		return maxMarks;
	}

	public void setMaxMarks(Float maxMarks) {
		this.maxMarks = maxMarks;
	}

	public String getTestName() {
		return testName;
	}

	public void setTestName(String testName) {
		this.testName = testName;
	}

	public Float getThresholdMarks() {
		return thresholdMarks;
	}

	public void setThresholdMarks(Float thresholdMarks) {
		this.thresholdMarks = thresholdMarks;
	}

	public String getObtainedMarks() {
		return obtainedMarks;
	}

	public void setObtainedMarks(String obtainedMarks) {
		this.obtainedMarks = obtainedMarks;
	}

	public int getClas() {
		return clas;
	}

	public void setClas(int clas) {
		this.clas = clas;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Override
	public int compareTo(TestBean arg) {
		
		if(arg.getId() < id)
			return -1;
		return 1;
	}

}
