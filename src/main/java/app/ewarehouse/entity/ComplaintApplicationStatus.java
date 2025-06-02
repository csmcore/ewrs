package app.ewarehouse.entity;

public enum ComplaintApplicationStatus {

	PENDING("Pending"),
	ACCEPT("Accept"),
    MARK_DOWN("Mark Down"),
    RESUBMIT("Resubmit"),
    FORWARD("Forward"),
    QUERY("Query"),
    REJECT("Reject"),
    SUSPEND("Suspend"),
    VERIFY("Verify"),
    DRAFT_LETTER("Draft Letter"),
    GENERATE_LETTER("Generate Letter"),
    NOT_RECOMMENDED("Not Recommended"),
    REVOKE_LICENSE("Revoke License"),
    NOT_REVOKE_LICENSE("Not Revoke License"),
	IN_PROGRESS("In Progress"),
    SOLVED("Solved"),
	SUBMITTED("Submitted"),
	COMPLAINT_ACCEPTED("Complaint Accepted"),
	COMPLAINT_REJECTED("Complaint Rejected"),
	UNDER_PROCESS("Under Process"),
	DECISION_TAKEN("Decision Taken");
	
	
	
	private final String actionName;
	
	ComplaintApplicationStatus(String actionName) {
		this.actionName = actionName;
	}
	
	public String getActionName() {
        return actionName;
    }
	
	public static ComplaintApplicationStatus fromActionName(String actionName) {
	    for (ComplaintApplicationStatus action : values()) {
	        if (action.getActionName().equalsIgnoreCase(actionName)) {
	            return action;
	        }
	    }
	    throw new IllegalArgumentException("No enum constant with action name " + actionName);
	}
	
	@Override
    public String toString() {
        return actionName;
    }
}
