package app.ewarehouse.entity;

public enum Stage {
	
	    OIC("oic"),
	    INSPECOR("inspector"),
	    OICFIN("oic fin"),
	    APPROVER("approver"),
	    CEO("ceo");

	    private String value;

	    Stage(String value) {
	        this.value = value;
	    }

	    public String getValue() {
	        return value;
	    }

	    public static Stage fromValue(String value) {
	        for (Stage stage : Stage.values()) {
	            if (stage.value.equalsIgnoreCase(value)) {
	                return stage;
	            }
	        }
	        throw new IllegalArgumentException("Unknown enum value: " + value);
	    }
}
