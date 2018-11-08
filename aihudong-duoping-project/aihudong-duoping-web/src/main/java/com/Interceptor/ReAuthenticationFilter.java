package com.Interceptor;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.jasig.cas.client.authentication.DefaultGatewayResolverImpl;
import org.jasig.cas.client.authentication.GatewayResolver;
import org.jasig.cas.client.util.AbstractCasFilter;
import org.jasig.cas.client.util.CommonUtils;
import org.jasig.cas.client.validation.Assertion;

public class ReAuthenticationFilter extends AbstractCasFilter {

	private String[] excludePaths;

	private String casServerLoginUrl;
	private boolean renew = false;

	private boolean gateway = false;
	private GatewayResolver gatewayStorage = new DefaultGatewayResolverImpl();

	protected void initInternal(final FilterConfig filterConfig) throws ServletException {
		if (!isIgnoreInitConfiguration()) {
			super.initInternal(filterConfig);
			setCasServerLoginUrl(getPropertyFromInitParams(filterConfig, "casServerLoginUrl", null));
			log.trace("Loaded CasServerLoginUrl parameter: " + this.casServerLoginUrl);
			setRenew(parseBoolean(getPropertyFromInitParams(filterConfig, "renew", "false")));
			log.trace("Loaded renew parameter: " + this.renew);
			setGateway(parseBoolean(getPropertyFromInitParams(filterConfig, "gateway", "false")));
			log.trace("Loaded gateway parameter: " + this.gateway);
			final String gatewayStorageClass = getPropertyFromInitParams(filterConfig, "gatewayStorageClass", null);
			if (gatewayStorageClass != null) {
				try {
					this.gatewayStorage = (GatewayResolver) Class.forName(gatewayStorageClass).newInstance();
				} catch (final Exception e) {
					log.error(e, e);
					throw new ServletException(e);
				}
			}

			// 拦截器过滤修改************begin*************************
			String _excludePaths = getPropertyFromInitParams(filterConfig, "excludePaths", null);
			if (CommonUtils.isNotBlank(_excludePaths)) {
				setExcludePaths(_excludePaths.trim().split(","));
			}
			// 拦截器过滤修改************end************************
		}
	}

	public final void doFilter(final ServletRequest servletRequest, final ServletResponse servletResponse, final FilterChain filterChain) throws IOException, ServletException {
        final HttpServletRequest request = (HttpServletRequest) servletRequest;
        final HttpServletResponse response = (HttpServletResponse) servletResponse;
        final HttpSession session = request.getSession(false);
        final Assertion assertion = session != null ? (Assertion) session.getAttribute(CONST_CAS_ASSERTION) : null;


        //拦截器过滤修改************begin********************
        boolean isAir = request.getParameter("isAir")!=null && "true".equals(request.getParameter("isAir"));
        if (isAir ) {
            filterChain.doFilter(request, response);
        }

        String uri = request.getRequestURI()+request.getQueryString();
        boolean isInWhiteList = false;
        if(excludePaths!=null && excludePaths.length>0 && uri!=null){
            for(String path : excludePaths){
             if(CommonUtils.isNotBlank(path)){
              isInWhiteList = uri.indexOf(path.trim())>-1;
               if(isInWhiteList){
                 break;
               }
              }
            }
        }
        
        if(isInWhiteList){
            filterChain.doFilter(request, response);
            return;
           }
        //拦截器过滤修改************end********************************
        
        if (assertion != null) {
            filterChain.doFilter(request, response);
            return;
        }
        final String serviceUrl = constructServiceUrl(request, response);
        final String ticket = CommonUtils.safeGetParameter(request,getArtifactParameterName());
        final boolean wasGatewayed = this.gatewayStorage.hasGatewayedAlready(request, serviceUrl);
        if (CommonUtils.isNotBlank(ticket) || wasGatewayed) {
            filterChain.doFilter(request, response);
            return;
        }
        final String modifiedServiceUrl;
        log.debug("no ticket and no assertion found");
        if (this.gateway) {
            log.debug("setting gateway attribute in session");
            modifiedServiceUrl = this.gatewayStorage.storeGatewayInformation(request, serviceUrl);
        } else {
            modifiedServiceUrl = serviceUrl;
        }
        if (log.isDebugEnabled()) {
            log.debug("Constructed service url: " + modifiedServiceUrl);
        }
        final String urlToRedirectTo = CommonUtils.constructRedirectUrl(this.casServerLoginUrl, getServiceParameterName(), modifiedServiceUrl, this.renew, this.gateway);
        if (log.isDebugEnabled()) {
            log.debug("redirecting to \"" + urlToRedirectTo + "\"");
        }
        response.sendRedirect(urlToRedirectTo);
    }

	public final void setRenew(boolean renew) {
		this.renew = renew;
	}

	public final void setGateway(boolean gateway) {
		this.gateway = gateway;
	}

	public final void setCasServerLoginUrl(String casServerLoginUrl) {
		this.casServerLoginUrl = casServerLoginUrl;
	}

	public final void setGatewayStorage(GatewayResolver gatewayStorage) {
		this.gatewayStorage = gatewayStorage;
	}

	public String[] getExcludePaths() {
		return excludePaths;
	}

	public void setExcludePaths(String[] excludePaths) {
		this.excludePaths = excludePaths;
	}
	
}
