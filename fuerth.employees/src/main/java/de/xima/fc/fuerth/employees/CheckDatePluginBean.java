package de.xima.fc.fuerth.employees;

import static de.xima.fc.fuerth.employees.Messages.fmt;
import static de.xima.fc.fuerth.employees.Messages.msg;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.xima.fc.dao.DaoProvider;
import de.xima.fc.entities.Projekt;
import de.xima.fc.entities.Status;
import de.xima.fc.plugin.abstracts.APluginCustomGUIBean;
import de.xima.fc.plugin.config.IParameterModel;
import de.xima.fc.plugin.gui.IPluginCustomGUIBean;

@ManagedBean(name = "checkDatePluginBean")
public class CheckDatePluginBean extends APluginCustomGUIBean implements IPluginCustomGUIBean {
	private static final long serialVersionUID = 1L;
	private final static Logger LOG = LoggerFactory.getLogger(CheckDatePluginBean.class);
	private List<Status> stateList;
	private Locale l = Locale.ROOT;

	public Locale getLocale() {
		return l;
	}

	public void setLocale(final Locale locale) {
		if (locale != null)
			this.l = locale;
	}

	@Override
	public Set<String> getFilterForParameter() {
		return Collections.emptySet();
	}

	public void setTargetStateExpire(final Status state) {
		if (state == null)
			setParam(CmnCnst.KEY_TARGET_STATE_EXPIRE, CmnCnst.DEFAULT_TARGET_STATE_EXPIRE);
		else
			setParam(CmnCnst.KEY_TARGET_STATE_EXPIRE, Integer.toString(state.getId(), 10));
	}

	public Status getTargetStateExpire() {
		return getState(getParam(CmnCnst.KEY_TARGET_STATE_EXPIRE, CmnCnst.DEFAULT_TARGET_STATE_EXPIRE));
	}

	public String getDescTargetStateExpire(final Locale locale) {
		setLocale(locale);
		return msg(CmnCnst.MSG_DESC_TARGET_STATE_EXPIRE, l);
	}

	public String getNameTargetStateExpire(final Locale locale) {
		setLocale(locale);
		return msg(CmnCnst.MSG_NAME_TARGET_STATE_EXPIRE, l);
	}

	public void setTargetStateNotExpire(final Status state) {
		if (state == null)
			setParam(CmnCnst.KEY_TARGET_STATE_NOT_EXPIRE, CmnCnst.DEFAULT_TARGET_STATE_NOT_EXPIRE);
		else
			setParam(CmnCnst.KEY_TARGET_STATE_NOT_EXPIRE, Integer.toString(state.getId(), 10));
	}

	public Status getTargetStateNotExpire() {
		return getState(getParam(CmnCnst.KEY_TARGET_STATE_NOT_EXPIRE, CmnCnst.DEFAULT_TARGET_STATE_NOT_EXPIRE));
	}

	public String getDescTargetStateNotExpire(final Locale locale) {
		setLocale(locale);
		return msg(CmnCnst.MSG_DESC_TARGET_STATE_NOT_EXPIRE, l);
	}

	public String getNameTargetStateNotExpire(final Locale locale) {
		setLocale(locale);
		return msg(CmnCnst.MSG_NAME_TARGET_STATE_NOT_EXPIRE, l);
	}

	public void setExpireTime(final String expireTime) {
		setParam(CmnCnst.KEY_EXPIRE_TIME, expireTime != null ? expireTime : CmnCnst.DEFAULT_EXPIRE_TIME);
	}

	public String getExpireTime() {
		return getParam(CmnCnst.KEY_EXPIRE_TIME, CmnCnst.DEFAULT_EXPIRE_TIME);
	}

	public String getDescExpireTime(final Locale locale) {
		setLocale(locale);
		return msg(CmnCnst.MSG_DESC_EXPIRE_TIME, l);
	}

	public String getNameExpireTime(final Locale locale) {
		setLocale(locale);
		return msg(CmnCnst.MSG_NAME_EXPIRE_TIME, l);
	}

	public void setDueDateFormat(final String dueDateFormat) {
		final String format = dueDateFormat != null ? dueDateFormat : CmnCnst.DEFAULT_DUE_DATE_FORMAT;
		final FacesMessage msg = DateFormatValidator.check(format, l);
		if (msg != null)
			FacesContext.getCurrentInstance().addMessage(null, msg);
		setParam(CmnCnst.KEY_DUE_DATE_FORMAT, format);
	}

	public String getDueDateFormat() {
		return getParam(CmnCnst.KEY_DUE_DATE_FORMAT, CmnCnst.DEFAULT_DUE_DATE_FORMAT);
	}

	public String getDescDueDateFormat(final Locale locale) {
		setLocale(locale);
		return msg(CmnCnst.MSG_DESC_DUE_DATE_FORMAT, l);
	}

	public String getNameDueDateFormat(final Locale locale) {
		setLocale(locale);
		return msg(CmnCnst.MSG_NAME_DUE_DATE_FORMAT, l);
	}


	public void setDueDate(final String dueDateFormat) {
		setParam(CmnCnst.KEY_DUE_DATE, dueDateFormat != null ? dueDateFormat : CmnCnst.DEFAULT_DUE_DATE);
	}

	public String getDueDate() {
		return getParam(CmnCnst.KEY_DUE_DATE, CmnCnst.DEFAULT_DUE_DATE);
	}

	public String getDescDueDate(final Locale locale) {
		setLocale(locale);
		return msg(CmnCnst.MSG_DESC_DUE_DATE, l);
	}

	public String getNameDueDate(final Locale locale) {
		setLocale(locale);
		return msg(CmnCnst.MSG_NAME_DUE_DATE, l);
	}


	public List<Status> getStateList() {
		if (stateList != null)
			return stateList;
		final Projekt project = this.getCurrentProject();
		if (project == null) {
			LOG.error(msg(CmnCnst.MSG_ERROR_PROJECT_NULL, l));
			return stateList = new ArrayList<>();
		}
		try {
			return stateList = DaoProvider.STATUS_DAO.getAllByProjekt(getEntityContext(), project);
		}
		catch (final Exception e) {
			LOG.error(fmt(CmnCnst.MSG_ERROR_DAO_STATE_LIST, l, project), e);
			return stateList = new ArrayList<>();
		}
	}

	private Status getState(final String idString) {
		if (idString.isEmpty())
			return null;
		final int id;
		try {
			id = Integer.parseInt(idString, 10);
		}
		catch (final NumberFormatException e) {
			LOG.error(fmt(CmnCnst.MSG_ERROR_INVALID_NUMBER, l, idString), e);
			return null;
		}
		for (final Status state : getStateList())
			if (state.getId() == id)
				return state;
		return null;
	}

	private void setParam(final String key, final String value) {
		final IParameterModel pm = this.getParameter(key);
		if (pm != null)
			pm.setParamValue(value);
		LOG.error(fmt(CmnCnst.MSG_ERROR_NO_SUCH_PARAMETER_SET, l, key, value));
	}

	private String getParam(final String key, final String defaultValue) {
		final IParameterModel pm = this.getParameter(key);
		if (pm != null) {
			final String value = pm.getParamValue();
			return value != null ? value : defaultValue;
		}
		LOG.error(fmt(CmnCnst.MSG_ERROR_NO_SUCH_PARAMETER_GET, l, key, defaultValue));
		return defaultValue;
	}
}