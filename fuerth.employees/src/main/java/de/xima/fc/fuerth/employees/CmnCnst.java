package de.xima.fc.fuerth.employees;

@SuppressWarnings("nls")
public final class CmnCnst {
	public static final double DAYS_TO_MS = 3600*24*1000;
	public static final String DEFAULT_DUE_DATE = "[%tfDueDate%]";
	public static final String DEFAULT_DUE_DATE_FORMAT = "dd.MM.yyyy";
	public static final String DEFAULT_EXPIRE_TIME = "10";
	public static final String DEFAULT_TARGET_STATE_EXPIRE = "";
	public static final String DEFAULT_TARGET_STATE_NOT_EXPIRE = "";
	public static final String DESC_DUE_DATE = "The date when the form record is due. Use a variable (placeholder) to refer to a form field.";
	public static final String DESC_DUE_DATE_FORMAT = "Format of the due date.";
	public static final String DESC_EXPIRE_TIME = "Controls when the form record is moved to the escalation state. Number of days before the form is due.";
	public static final String DESC_PLUGIN = "Checks whether the time difference between the current time and the time from the given form field is below the given threshold, and puts the forms record in the given state when it is.";
	public static final String DESC_TARGET_STATE_EXPIRE = "The name of the state the form is moved to when it is expired. Leave this empty and the state will not be changed.";
	public static final String DESC_TARGET_STATE_NOT_EXPIRE = "The name of the state the form is moved to when it is not expired. Leave this empty and the state will not be changed.";
	public static final String ERROR_DAO_STATE_LIST = "Failed to get a list of states for this project %s.";
	public static final String ERROR_ENTITY_CONTEXT_NULL = "Entity context must not be null.";
	public static final String ERROR_FORM_RECORD_NULL = "Form record must not be null.";
	public static final String ERROR_INVALID_DATE = "Failed to parse date %s with format %s.";
	public static final String ERROR_INVALID_NUMBER = "Invalid number %s given for expire.";
	public static final String ERROR_NO_SUCH_STATE = "Did not find a state named %s.";
	public static final String ERROR_PLACEHOLDER_REPLACER = "Failed to replace variables for string %s.";
	public static final String ERROR_PROJECT_NULL = "Project must not be null.";
	public static final String ERROR_WORKFLOW_PROCESSING_CONTEXT_NULL = "Workflow processing context must not be null.";
	public static final String KEY_DUE_DATE = "de.xima.fc.fuerth.employee.due.date";
	public static final String KEY_DUE_DATE_FORMAT = "de.xima.fc.fuerth.employee.due.date.format";
	public static final String KEY_EXPIRE_TIME = "de.xima.fc.fuerth.employee.expire.time";
	public static final String KEY_TARGET_STATE_EXPIRE = "de.xima.fc.fuerth.employee.target.state.expire";
	public static final String KEY_TARGET_STATE_NOT_EXPIRE = "de.xima.fc.fuerth.employee.target.state.not.expire";
	public static final String NAME_CHECK_DATE_PLUGIN = "Check date plugin";
	public static final String NAME_DUE_DATE = "Due date";
	public static final String NAME_DUE_DATE_FORMAT = "Due date format";
	public static final String NAME_EXPIRE_TIME = "Escalation time";
	public static final String NAME_TARGET_STATE_EXPIRE = "Target state (expired)";
	public static final String NAME_TARGET_STATE_NOT_EXPIRE = "Target state (not expired)";

	private CmnCnst() {}
}
