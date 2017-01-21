package de.xima.fc.fuerth.employees;

@SuppressWarnings("nls")
public final class CmnCnst {
	public static final double DAYS_TO_MS = 3600*24*1000;

	public static final String DEFAULT_DUE_DATE = "[%tfDueDate%]";
	public static final String DEFAULT_DUE_DATE_FORMAT = "dd.MM.yyyy";
	public static final String DEFAULT_EXPIRE_TIME = "10";
	public static final String DEFAULT_TARGET_STATE_EXPIRE = "";
	public static final String DEFAULT_TARGET_STATE_NOT_EXPIRE = "";

	public static final String KEY_DUE_DATE = "de.xima.fc.fuerth.employee.due.date";
	public static final String KEY_DUE_DATE_FORMAT = "de.xima.fc.fuerth.employee.due.date.format";
	public static final String KEY_EXPIRE_TIME = "de.xima.fc.fuerth.employee.expire.time";
	public static final String KEY_TARGET_STATE_EXPIRE = "de.xima.fc.fuerth.employee.target.state.expire";
	public static final String KEY_TARGET_STATE_NOT_EXPIRE = "de.xima.fc.fuerth.employee.target.state.not.expire";

	public static final String MSG_DESC_DUE_DATE = "desc.due.state";
	public static final String MSG_DESC_DUE_DATE_FORMAT = "desc.due.date.format";
	public static final String MSG_DESC_EXPIRE_TIME = "desc.expire.time";
	public static final String MSG_DESC_PLUGIN = "desc.plugin";
	public static final String MSG_DESC_TARGET_STATE_EXPIRE = "desc.target.state.expire";
	public static final String MSG_DESC_TARGET_STATE_NOT_EXPIRE = "desc.target.state.not.expire";
	public static final String MSG_ERROR_DAO_STATE_LIST = "error.dao.state.list";
	public static final String MSG_ERROR_ENTITY_CONTEXT_NULL = "error.entity.context.null";
	public static final String MSG_ERROR_FORM_RECORD_NULL = "error.form.record.null";
	public static final String MSG_ERROR_INVALID_DATE = "error.invalid.date";
	public static final String MSG_ERROR_INVALID_NUMBER = "error.invalid.number";
	public static final String MSG_ERROR_NO_SUCH_PARAMETER_GET = "error.no.such.parameter.get";
	public static final String MSG_ERROR_NO_SUCH_PARAMETER_SET = "error.no.such.parameter.set";
	public static final String MSG_ERROR_NO_SUCH_STATE = "error.no.such.state";
	public static final String MSG_ERROR_PLACEHOLDER_REPLACER = "error.placeholder.replacer";
	public static final String MSG_ERROR_PROJECT_NULL = "error.project.null";
	public static final String MSG_ERROR_WORKFLOW_PROCESSING_CONTEXT_NULL = "error.wpc.null";
	public static final String MSG_NAME_CHECK_DATE_PLUGIN = "name.check.date.plugin";
	public static final String MSG_NAME_DUE_DATE = "name.due.date";
	public static final String MSG_NAME_DUE_DATE_FORMAT = "name.due.date.format";
	public static final String MSG_NAME_EXPIRE_TIME = "name.expire.time";
	public static final String MSG_NAME_TARGET_STATE_EXPIRE = "name.target.state.expire";
	public static final String MSG_NAME_TARGET_STATE_NOT_EXPIRE = "name.target.state.not.expire";

	public static final String PATH_CUSTOM_UI = "/de/xima/fc/fuerth/employees/ui.xhtml";

	public static final String MSG_ERROR_RESOURCE_ACQUISTION = "error.resource.acquisition";
	private CmnCnst() {}

}