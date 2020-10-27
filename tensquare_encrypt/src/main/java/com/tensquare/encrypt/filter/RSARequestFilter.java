package com.tensquare.encrypt.filter;

import com.google.common.base.Strings;
import com.google.common.base.Utf8;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import com.netflix.zuul.http.HttpServletRequestWrapper;
import com.netflix.zuul.http.ServletInputStreamWrapper;
import com.tensquare.encrypt.rsa.RsaKeys;
import com.tensquare.encrypt.service.RsaService;
import org.apache.commons.codec.Charsets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Component
public class RSARequestFilter extends ZuulFilter {

    @Autowired
    private RsaService rsaService;

    //过滤器执行环节
    //解密操作需要在转发之前执行
    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;
    }

    //执行顺序
    @Override
    public int filterOrder() {
        return FilterConstants.PRE_DECORATION_FILTER_ORDER + 1;
    }

    //是否使用
    @Override
    public boolean shouldFilter() {
        return true;
    }

    //过滤器具体执行逻辑
    @Override
    public Object run() throws ZuulException {
        //过滤器执行了
        System.out.println("过滤器执行了");

        //获取request和response
        RequestContext currentContext = RequestContext.getCurrentContext();
        HttpServletRequest request = currentContext.getRequest();
        HttpServletResponse response = currentContext.getResponse();

        //声明存放加密后的数据
        String requestData = null;
        //声明存放解密后的数据
        String decryptData = null;
        try {
            ServletInputStream inputStream = request.getInputStream();
            requestData = StreamUtils.copyToString(inputStream, Charsets.UTF_8);
            System.out.println(requestData);
            if (!Strings.isNullOrEmpty(requestData)){
                decryptData = rsaService.RSADecryptDataPEM(requestData, RsaKeys.getServerPrvKeyPkcs8());
                System.out.println(decryptData);
            }
            if (!Strings.isNullOrEmpty(decryptData)){
                byte[] decryptDataBytes = decryptData.getBytes();
                currentContext.setRequest(new HttpServletRequestWrapper(request){
                    @Override
                    public ServletInputStream getInputStream() {
                        return new ServletInputStreamWrapper(decryptDataBytes);
                    }

                    @Override
                    public int getContentLength() {
                        return decryptDataBytes.length;
                    }

                    @Override
                    public long getContentLengthLong() {
                        return decryptDataBytes.length;
                    }
                });
                currentContext.addZuulRequestHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE+";charset = UTF-8");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
