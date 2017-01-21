package de.xima.fc.fuerth.employees;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
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

@ManagedBean(name="checkDatePluginBean")
public class CheckDatePluginBean extends APluginCustomGUIBean implements IPluginCustomGUIBean {
	private static final long serialVersionUID = 1L;
	private final static Logger LOG = LoggerFactory.getLogger(CheckDatePluginBean.class);
	private List<Status> stateList;

	@PostConstruct
	public void initialize() {
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

	public String getDescTargetStateExpire() {
		return CmnCnst.DESC_TARGET_STATE_EXPIRE;
	}

	public String getNameTargetStateExpire() {
		return CmnCnst.NAME_TARGET_STATE_EXPIRE;
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

	public String getDescTargetStateNotExpire() {
		return CmnCnst.DESC_TARGET_STATE_NOT_EXPIRE;
	}

	public String getNameTargetStateNotExpire() {
		return CmnCnst.NAME_TARGET_STATE_NOT_EXPIRE;
	}


	public void setExpireTime(final String expireTime) {
		setParam(CmnCnst.KEY_EXPIRE_TIME, expireTime != null ? expireTime : CmnCnst.DEFAULT_EXPIRE_TIME);
	}

	public String getExpireTime() {
		return getParam(CmnCnst.KEY_EXPIRE_TIME, CmnCnst.DEFAULT_EXPIRE_TIME);
	}

	public String getDescExpireTime() {
		return CmnCnst.DESC_EXPIRE_TIME;
	}

	public String getNameExpireTime() {
		return CmnCnst.NAME_EXPIRE_TIME;
	}


	public void setDueDateFormat(final String dueDateFormat) {
		final FacesMessage msg = DateFormatValidator.check(dueDateFormat);
		if (msg != null)
			FacesContext.getCurrentInstance().addMessage(null, msg);
		setParam(CmnCnst.KEY_DUE_DATE_FORMAT, dueDateFormat != null ? dueDateFormat : CmnCnst.DEFAULT_DUE_DATE_FORMAT);
	}

	public String getDueDateFormat() {
		return getParam(CmnCnst.KEY_DUE_DATE_FORMAT, CmnCnst.DEFAULT_DUE_DATE_FORMAT);
	}

	public String getDescDueDateFormat() {
		return CmnCnst.DESC_DUE_DATE_FORMAT;
	}

	public String getNameDueDateFormat() {
		return CmnCnst.NAME_DUE_DATE_FORMAT;
	}


	public void setDueDate(final String dueDateFormat) {
		setParam(CmnCnst.KEY_DUE_DATE, dueDateFormat != null ? dueDateFormat : CmnCnst.DEFAULT_DUE_DATE);
	}

	public String getDueDate() {
		return getParam(CmnCnst.KEY_DUE_DATE, CmnCnst.DEFAULT_DUE_DATE);
	}

	public String getDescDueDate() {
		return CmnCnst.DESC_DUE_DATE;
	}

	public String getNameDueDate() {
		return CmnCnst.NAME_DUE_DATE;
	}


	public List<Status> getStateList() {
		if (stateList != null)
			return stateList;
		final Projekt project = this.getCurrentProject();
		if (project == null) {
			LOG.error(CmnCnst.ERROR_PROJECT_NULL);
			return stateList = new ArrayList<>();
		}
		try {
			return stateList = DaoProvider.STATUS_DAO.getAllByProjekt(getEntityContext(), project);
		}
		catch (final Exception e) {
			LOG.error(String.format(CmnCnst.ERROR_DAO_STATE_LIST, project), e);
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
			LOG.error(String.format(CmnCnst.ERROR_INVALID_NUMBER, idString), e);
			return null;
		}
		for (final Status state : getStateList())
			if (state.getId() == id)
				return state;
		return null;
//		try {
//			return DaoProvider.STATUS_DAO.read(getEntityContext(), id);
//		}
//		catch (final Exception e) {
//			LOG.error(String.format("Failed to read state with id %s.", id), e);
//			return null;
//		}
	}

	private void setParam(final String key, final String value) {
		final IParameterModel pm = this.getParameter(key);
		if (pm != null)
			pm.setParamValue(value);
		LOG.error(String.format("No such parameter %s, cannot set value %s.", key, value));
	}

	private String getParam(final String key, final String defaultValue) {
		final IParameterModel pm = this.getParameter(key);
		if (pm != null) {
			final String value = pm.getParamValue();
			return value != null ? value : defaultValue;
		}
		LOG.error(String.format("No such parameter %s, defaulting to.", key, defaultValue));
		return defaultValue;
	}
}