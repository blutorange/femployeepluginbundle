package de.xima.fc.fuerth.employees;

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

	@Override
	public String getName() {
		return CmnCnst.NAME_CHECK_DATE_PLUGIN;
	}

	@Override
	public void initPlugin() throws FCPluginException {
		// no initialization required
	}

	@SuppressWarnings("resource") // We did not open the entity context.
	@Override
	public IPluginProcessingRetVal execute(final IPluginProcessingParams params) throws FCPluginException {
		final IWorkflowProcessingContext workflowProcessingContext = params.getWorkflowProcessingContext();
		if (workflowProcessingContext == null) {
			LOG.error(CmnCnst.ERROR_WORKFLOW_PROCESSING_CONTEXT_NULL);
			return new ProcessingResultSuccess(false);
		}
		final IEntityContext entityContext = workflowProcessingContext.getEntityContext();
		if (entityContext == null) {
			LOG.error(CmnCnst.ERROR_ENTITY_CONTEXT_NULL);
			return new ProcessingResultSuccess(false);
		}
		final Vorgang formRecord = workflowProcessingContext.getVorgang();
		if (formRecord == null) {
			LOG.error(CmnCnst.ERROR_FORM_RECORD_NULL);
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
			LOG.error(String.format(CmnCnst.ERROR_INVALID_NUMBER, targetStatusString), e);
			return new ProcessingResultSuccess(false);
		}
		try {
			targetState = DaoProvider.STATUS_DAO.read(entityContext, id);
		}
		catch (final Exception e) {
			LOG.error(CmnCnst.ERROR_DAO_STATE_LIST, e);
			return new ProcessingResultSuccess(false);
		}
		if (targetState == null) {
			LOG.error(String.format(CmnCnst.ERROR_NO_SUCH_STATE, targetStatusString));
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
			LOG.error(String.format(CmnCnst.ERROR_INVALID_NUMBER, expireString), e);
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
			LOG.error(String.format(CmnCnst.ERROR_PLACEHOLDER_REPLACER, dueDateString), e);
			return null;
		}
		final DateFormat dateFormat = new SimpleDateFormat(dueDateFormat, Locale.ROOT);
		final Date dueDate;
		try {
			dueDate = dateFormat.parse(dueDateStringReplaced);
		}
		catch (final ParseException e) {
			LOG.error(String.format(CmnCnst.ERROR_INVALID_DATE, dueDateStringReplaced, dueDateFormat), e);
			return null;
		}
		return dueDate;
	}

	@Override
	public Map<String, IPluginConfigParam> getConfigParameter() {
		final Map<String, IPluginConfigParam> map = new HashMap<>();
		map.put(CmnCnst.KEY_EXPIRE_TIME, new PluginConfigParam(CmnCnst.KEY_EXPIRE_TIME, CmnCnst.DESC_EXPIRE_TIME, true,
				EPluginParamBehavior.IN, CmnCnst.DEFAULT_EXPIRE_TIME));
		map.put(CmnCnst.KEY_TARGET_STATE_EXPIRE, new PluginConfigParam(CmnCnst.KEY_TARGET_STATE_EXPIRE,
				CmnCnst.DESC_TARGET_STATE_EXPIRE, false, EPluginParamBehavior.IN, CmnCnst.DEFAULT_TARGET_STATE_EXPIRE));
		map.put(CmnCnst.KEY_TARGET_STATE_NOT_EXPIRE,
				new PluginConfigParam(CmnCnst.KEY_TARGET_STATE_NOT_EXPIRE, CmnCnst.DESC_TARGET_STATE_NOT_EXPIRE, false,
						EPluginParamBehavior.IN, CmnCnst.DEFAULT_TARGET_STATE_NOT_EXPIRE));
		map.put(CmnCnst.KEY_DUE_DATE, new PluginConfigParam(CmnCnst.KEY_DUE_DATE, CmnCnst.DESC_DUE_DATE, true,
				EPluginParamBehavior.IN, CmnCnst.DEFAULT_DUE_DATE));
		map.put(CmnCnst.KEY_DUE_DATE_FORMAT, new PluginConfigParam(CmnCnst.KEY_DUE_DATE_FORMAT,
				CmnCnst.DESC_DUE_DATE_FORMAT, true, EPluginParamBehavior.IN, CmnCnst.DEFAULT_DUE_DATE_FORMAT));
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
		return CmnCnst.DESC_PLUGIN;
	}

	@Override
	public String getConfigPage() {
		final URL url = getClass().getResource("/ui.xhtml");
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
