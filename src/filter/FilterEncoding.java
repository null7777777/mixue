package filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebFilter(filterName="FilterEncoding",
			urlPatterns="/*",
			initParams= {@WebInitParam(name="encoding",value="utf-8")})
public class FilterEncoding implements Filter {
	private String encoding;
	

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		
		HttpServletRequest httpRequest=(HttpServletRequest) request;
		HttpServletResponse httpResponse=(HttpServletResponse) response;
		
		httpRequest.setCharacterEncoding(this.encoding);
		httpResponse.setCharacterEncoding(this.encoding);
		
		//System.out.println(DateUtil.show()+">>设置encoding"+this.encoding);
		
		chain.doFilter(request, response);
	}

	public void init(FilterConfig fConfig) throws ServletException {
		this.encoding=fConfig.getInitParameter("encoding");
	}

	@Override
	public void destroy() {
		
	}

}
