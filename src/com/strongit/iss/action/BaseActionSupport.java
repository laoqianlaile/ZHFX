package com.strongit.iss.action;

import org.apache.log4j.Logger;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;

public abstract class BaseActionSupport<T> extends SimpleActionSupport implements Preparable, ModelDriven<T> {
	private static final long serialVersionUID = 3434328345273358967L;
	
	protected final Logger logger = Logger.getLogger(getClass());
	
	public static final String RELOAD = "reload";

	public String execute() throws Exception {
		return list();
	}

	public String list() throws Exception {
		return null;
	}

	public String save() throws Exception {
		return null;
	}
	
	public String delete() throws Exception {
		return null;
	}

	public String input() throws Exception {
		return null;
	}

	public T getModel() {
		return null;
	}

	public void prepareSave() throws Exception {
		prepareModel();
	}

	public void prepareInput() throws Exception {
		prepareModel();
	}

	public void prepare() throws Exception {
	}

	protected void prepareModel() throws Exception {
	}
	
	

}