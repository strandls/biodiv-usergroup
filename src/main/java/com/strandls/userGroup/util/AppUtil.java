package com.strandls.userGroup.util;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;

import com.google.common.net.InternetDomainName;

public class AppUtil {
	
	public static String getDomain(HttpServletRequest request) {
		String domain = "";
		String tmpDomain = request.getHeader(HttpHeaders.HOST);
		if (tmpDomain != null && !tmpDomain.isEmpty() && tmpDomain.contains(".")) {
			domain = InternetDomainName.from(tmpDomain).topDomainUnderRegistrySuffix().toString();
		}
		return domain;
	}

}
