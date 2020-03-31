package com.strongit.iss.common;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.transform.ResultTransformer;

public class ResultToMap implements ResultTransformer {
	private static final long serialVersionUID = -6126968741247252813L;

	public Object transformTuple(Object[] tuple, String[] aliases) {
		Map<String, Object> result = new LinkedHashMap<String, Object>();
		for (int i = 0; i < aliases.length; i++) {
			result.put(aliases[i], tuple[i]);
		}
		return result;
	}

	@SuppressWarnings("rawtypes")
	public List transformList(List collection) {
		return collection;
	}

}
