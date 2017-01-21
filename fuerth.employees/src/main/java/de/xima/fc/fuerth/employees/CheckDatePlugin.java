package de.xima.fc.fuerth.employees;

import static de.xima.fc.fuerth.employees.Messages.fmt;
import static de.xima.fc.fuerth.employees.Messages.msg;

import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.xima.fc.bl.fdv.processing.result.ProcessingResultSuccess;
import de.xima.fc.dao.DaoProvider;
import de.xima.fc.entities.Status;
import de.xima.fc.entities.Vorgang;
import de.xima.fc.interfaces.IEntityContext;
import de.xima.fc.interfaces.plugin.param.workflow.IPluginProcessingParams;
import de.xima.fc.interfaces.plugin.retval.workflow.IPluginProcessingRetVal;
import de.xima.fc.interfaces.processing.IWorkflowProcessingContext;
import de.xima.fc.placeholder.PlaceholderReplacer;
import de.xima.fc.plugin.abstracts.AFCPlugin;
import de.xima.fc.plugin.config.IDescriptionProvidingPlugin;
import de.xima.fc.plugin.config.IPluginConfigParam;
import de.xima.fc.plugin.config.IPluginConfigParam.EPluginParamBehavior;
import de.xima.fc.plugin.config.IPluginConfigParamList;
import de.xima.fc.plugin.config.IPluginCustomGUI;
import de.xima.fc.plugin.exception.FCPluginException;
import de.xima.fc.plugin.gui.IPluginCustomGUIBean;
import de.xima.fc.plugin.interfaces.workflow.IPluginProcessing;
import de.xima.fc.plugin.models.config.PluginConfigParam;

public class CheckDatePlugin extends AFCPlugin
		implements IPluginProcessing, IPluginConfigParamList, IDescriptionProvidingPlugin, IPluginCustomGUI {
	private static final long serialVersionUID = 1L;
	private final static Logger LOG = LoggerFactory.getLogger(CheckDatePlugin.class);
	private Locale l = Locale.ROOT;

	@Override
	public String getName() {
		return msg(CmnCnst.MSG_NAME_CHECK_DATE_PLUGIN, l);
	}

	@Override
	public void initPlugin() throws FCPluginException {
		l = Locale.ENGLISH;
	}

	@SuppressWarnings("resource") // We did not open the entity context.
	@Override
	public IPluginProcessingRetVal execute(final IPluginProcessingParams params) throws FCPluginException {
		final IWorkflowProcessingContext workflowProcessingContext = params.getWorkflowProcessingContext();
		if (workflowProcessingContext == null) {
			LOG.error(msg(CmnCnst.MSG_ERROR_WORKFLOW_PROCESSING_CONTEXT_NULL, l));
			return new ProcessingResultSuccess(false);
		}
		final Vorgang formRecord = workflowProcessingContext.getVorgang();
		if (formRecord == null) {
			LOG.error(msg(CmnCnst.MSG_ERROR_FORM_RECORD_NULL, l));
			return new ProcessingResultSuccess(false);
		}
		final IEntityContext entityContext = workflowProcessingContext.getEntityContext();
		if (entityContext == null) {
			LOG.error(msg(CmnCnst.MSG_ERROR_ENTITY_CONTEXT_NULL, l));
			return new ProcessingResultSuccess(false);
		}


		final Date dueDate = getDueDate(params, workflowProcessingContext);
		if (dueDate == null)
			return new ProcessingResultSuccess(false);

		final Long expireMillis = getExpireMillis(params);
		if (expireMillis == null)
			return new ProcessingResultSuccess(false);

		final Date currentDate = new Date();
		final long diff = dueDate.getTime() - currentDate.getTime();
		final String targetStatusString = prop(params, diff <= expireMillis.longValue()
				? CmnCnst.KEY_TARGET_STATE_EXPIRE : CmnCnst.KEY_TARGET_STATE_NOT_EXPIRE, StringUtils.EMPTY);
		if (targetStatusString.isEmpty())
			return new ProcessingResultSuccess(true);
		return switchToState(targetStatusString, entityContext, formRecord);
	}

	private IPluginProcessingRetVal switchToState(final String targetStatusString, final IEntityContext entityContext,
			final Vorgang formRecord) {
		final int id;
		final Status targetState;
		try {
			id = Integer.parseInt(targetStatusString);
		}
		catch (final NumberFormatException e) {
			LOG.error(fmt(CmnCnst.MSG_ERROR_INVALID_NUMBER, l, targetStatusString), e);
			return new ProcessingResultSuccess(false);
		}
		try {
			targetState = DaoProvider.STATUS_DAO.read(entityContext, id);
		}
		catch (final Exception e) {
			LOG.error(msg(CmnCnst.MSG_ERROR_DAO_STATE_LIST, l), e);
			return new ProcessingResultSuccess(false);
		}
		if (targetState == null) {
			LOG.error(fmt(CmnCnst.MSG_ERROR_NO_SUCH_STATE, l, targetStatusString));
			return new ProcessingResultSuccess(false);
		}
		formRecord.setCurrentStatus(targetState);
		formRecord.setNewStatus(targetState);
		return new ProcessingResultSuccess(true);
	}

	private Long getExpireMillis(final IPluginProcessingParams params) {
		final String expireString = prop(params, CmnCnst.KEY_EXPIRE_TIME, CmnCnst.DEFAULT_EXPIRE_TIME);
		final double expire;
		try {
			expire = Double.parseDouble(expireString);
		}
		catch (final NumberFormatException e) {
			LOG.error(fmt(CmnCnst.MSG_ERROR_INVALID_NUMBER, l, expireString), e);
			return null;
		}
		final long expireMillis = (long) (expire * CmnCnst.DAYS_TO_MS);
		return Long.valueOf(expireMillis);
	}

	private Date getDueDate(final IPluginProcessingParams params,
			final IWorkflowProcessingContext workflowProcessingContext) {
		final String dueDateString = prop(params, CmnCnst.KEY_DUE_DATE, CmnCnst.DEFAULT_DUE_DATE);
		final String dueDateFormat = prop(params, CmnCnst.KEY_DUE_DATE_FORMAT, CmnCnst.DEFAULT_DUE_DATE_FORMAT);
		final String dueDateStringReplaced;
		try {
			dueDateStringReplaced = PlaceholderReplacer.parse(dueDateString, workflowProcessingContext);
		}
		catch (final Exception e) {
			LOG.error(fmt(CmnCnst.MSG_ERROR_PLACEHOLDER_REPLACER, l, dueDateString), e);
			return null;
		}
		final DateFormat dateFormat = new SimpleDateFormat(dueDateFormat, Locale.ROOT);
		final Date dueDate;
		try {
			dueDate = dateFormat.parse(dueDateStringReplaced);
		}
		catch (final ParseException e) {
			LOG.error(fmt(CmnCnst.MSG_ERROR_INVALID_DATE, l, dueDateStringReplaced, dueDateFormat), e);
			return null;
		}
		return dueDate;
	}

	@Override
	public Map<String, IPluginConfigParam> getConfigParameter() {
		final Map<String, IPluginConfigParam> map = new HashMap<>();
		map.put(CmnCnst.KEY_EXPIRE_TIME, new PluginConfigParam(CmnCnst.KEY_EXPIRE_TIME,
				msg(CmnCnst.MSG_DESC_EXPIRE_TIME, l), true, EPluginParamBehavior.IN, CmnCnst.DEFAULT_EXPIRE_TIME));
		map.put(CmnCnst.KEY_TARGET_STATE_EXPIRE,
				new PluginConfigParam(CmnCnst.KEY_TARGET_STATE_EXPIRE, msg(CmnCnst.MSG_DESC_TARGET_STATE_EXPIRE, l), false,
						EPluginParamBehavior.IN, CmnCnst.DEFAULT_TARGET_STATE_EXPIRE));
		map.put(CmnCnst.KEY_TARGET_STATE_NOT_EXPIRE,
				new PluginConfigParam(CmnCnst.KEY_TARGET_STATE_NOT_EXPIRE, msg(CmnCnst.MSG_DESC_TARGET_STATE_NOT_EXPIRE, l),
						false, EPluginParamBehavior.IN, CmnCnst.DEFAULT_TARGET_STATE_NOT_EXPIRE));
		map.put(CmnCnst.KEY_DUE_DATE, new PluginConfigParam(CmnCnst.KEY_DUE_DATE, msg(CmnCnst.MSG_DESC_DUE_DATE, l), true,
				EPluginParamBehavior.IN, CmnCnst.DEFAULT_DUE_DATE));
		map.put(CmnCnst.KEY_DUE_DATE_FORMAT,
				new PluginConfigParam(CmnCnst.KEY_DUE_DATE_FORMAT, msg(CmnCnst.MSG_DESC_DUE_DATE_FORMAT, l), true,
						EPluginParamBehavior.IN, CmnCnst.DEFAULT_DUE_DATE_FORMAT));
		return map;
	}

	private String prop(final IPluginProcessingParams params, final String key, final String defaultValue) {
		final IPluginConfigParam param = this.getConfigParameter().get(key);
		if (param == null)
			return defaultValue;
		final String value = params.getParams().get(param.getName());
		return value != null ? value : defaultValue;
	}

	@Override
	public String getDescription() {
		return msg(CmnCnst.MSG_DESC_PLUGIN, l);
	}

	@Override
	public String getConfigPage() {
		final URL url = getClass().getResource(CmnCnst.PATH_CUSTOM_UI);
		if (url == null) {
			LOG.error(fmt(CmnCnst.MSG_ERROR_RESOURCE_ACQUISTION, l, CmnCnst.PATH_CUSTOM_UI));
			return StringUtils.EMPTY;
		}
		return url.toExternalForm();
	}

	@Override
	public Class<? extends IPluginCustomGUIBean> getManagedBean() {
		return CheckDatePluginBean.class;
	}

	@Override
	public void initCustomGUI() {
	}
}
